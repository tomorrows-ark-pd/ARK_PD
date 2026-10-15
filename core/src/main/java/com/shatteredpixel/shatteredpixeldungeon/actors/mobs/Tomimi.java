package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Tomimi_BossSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Tomimi_towerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Tomimi extends Mob {
    {
        spriteClass = Tomimi_BossSprite.class;

        HP = HT = 1500;
        defenseSkill = 20;

        state = HUNTING;

        EXP = 40;

        properties.add(Property.BOSS);
        properties.add(Property.INFECTED);
        immunities.add(Amok.class);
        immunities.add(Terror.class);
        immunities.add(Silence.class);
    }

    private int phase = 0; // 1~3까지
    private int enemyspawncooldown = 2; // 1페이즈 7, 2페이즈 5, 3페이즈 3. 각 페이즈 변경시 잡몹 전부 제거
    private int BurstPos = -1;
    private int BurstTime = 0;
    public static int tomimitower = 0;

    @Override
    public int attackSkill(Char target) {
        return 35;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(36, 48);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 18);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (phase == 2) {
            sprite.showStatus( CharSprite.NEUTRAL, Messages.get(Talu_BlackSnake.class, "invincibility") );
            if (src == Dungeon.hero) {
                ScrollOfTeleportation.appear(Dungeon.hero, Dungeon.level.entrance());
            }
            return;
        }

        super.damage(dmg, src);

        if (phase==1 && HP <= 750) {
            HP = 750;
            phase = 2;
            GameScene.flash(0x80FF0000);
            yell(Messages.get(this, "phase2"));
            boss_tel(52);
            ScrollOfTeleportation.appear(Dungeon.hero, Dungeon.level.entrance());

            summonSubject(47, new TomimiTower());
            summonSubject(57, new TomimiTower());
            tomimitower = 2;
        }
    }

    @Override
    public void notice() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));

            if (phase == 0) {
                phase = 1;
            }
        }
    }

    @Override
    protected boolean act() {
        if (phase == 0) {
            if (Dungeon.hero.viewDistance >= Dungeon.level.distance(pos, Dungeon.hero.pos)) {
                Dungeon.observe();
            }
            if (Dungeon.level.heroFOV[pos]) {
                notice();
            }
        }

        if (phase == 0) {
            spend(TICK);
            return true;
        }
        else {
            enemyspawn();

            if (phase == 2) {
                if (tomimitower == 0) {
                    HP = 350;
                    phase = 3;
                    GameScene.flash(0x80FF0000);
                    yell(Messages.get(this, "phase3"));
                    boss_tel(241);

                    Bestiary.skipCountingEncounters = true;
                    try {
                        for (Mob m : getSubjects()) {
                            m.destroy();
                            m.sprite.killAndErase();
                        }
                    } finally {
                        Bestiary.skipCountingEncounters = false;
                    }
                }
                RPG_SHOT(Dungeon.hero.pos);
            }
        }

        if (enemyspawncooldown >= 1) enemyspawncooldown--;

        if (phase == 3) {
            spend(TICK);
            return true;
        }
        return super.act();
    }

    protected void boss_tel(int movepos) {
        sprite.move(pos, movepos);
        pos = movepos;
    }

    public static class RPG7 {}

    protected void RPG_SHOT(int targetpos) {
        if (BurstPos == -1) {
            // 위치 미지정시, 이번 턴에는 폭발을 일으킬 지점을 정합니다.
            BurstPos = Dungeon.hero.pos;
            sprite.parent.addToBack(new TargetedCell(BurstPos, 0xFF0000));

            for (int i : PathFinder.NEIGHBOURS9) {
                int vol = Fire.volumeAt(BurstPos+i, Fire.class);
                if (vol < 4){
                    sprite.parent.addToBack(new TargetedCell(BurstPos + i, 0xFF0000));
                }
            }

            Sample.INSTANCE.play( Assets.Sounds.ZAP_GUN );
            BurstTime++;

            return;
        }
        else if (BurstTime == 1) {
            BurstTime++;
            return;
        }
        else if (BurstTime == 2) {
            PathFinder.buildDistanceMap(BurstPos, BArray.not(Dungeon.level.solid, null), 1);
            for (int cell = 0; cell < PathFinder.distance.length; cell++) {
                if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    Char ch = Actor.findChar(cell);
                    if (ch != null) {
                        int damage = Random.NormalIntRange(65, 90);
                        if (ch != Dungeon.hero) damage *= 2;
                        ch.damage(damage - ch.drRoll(),RPG7.class);
                        if (!ch.isAlive() && ch == Dungeon.hero) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(this, "destroy"));
                        }
                    }}}
            Camera.main.shake(2, 0.5f);
            BurstTime++;

            Sample.INSTANCE.play( Assets.Sounds.BURNING );
            return;
        }
        else {
            BurstPos = -1;
            BurstTime = 0;
            return;
        }
    }


    protected void enemyspawn() {
        if (enemyspawncooldown <= 0) {
            if (Random.Int(7) == 0) {
                summonSubject(401, new TomimiLancer());
                summonSubject(417, new TomimiFanatic());
            }
            else {
                summonSubject(401, new TomimiWarrior());
                summonSubject(417, new TomimiFanatic());
            }

            if (phase == 1) enemyspawncooldown = 9;
            else if (phase == 2) enemyspawncooldown = 5;
            else enemyspawncooldown = 3;
        }
    }

    private void summonSubject( int pos, Mob enemy ){
        enemy.pos = pos;
        if (Actor.findChar(enemy.pos) != null) {
            int pushPos = pos;
            for (int c : PathFinder.NEIGHBOURS8) {
                if (Actor.findChar(enemy.pos + c) == null
                        && Dungeon.level.passable[enemy.pos + c]
                        && (Dungeon.level.openSpace[enemy.pos + c] || !hasProp(Actor.findChar(enemy.pos), Property.LARGE))
                        && Dungeon.level.trueDistance(pos, enemy.pos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
                    pushPos = enemy.pos + c;
                }
            }

            //push enemy, or wait a turn if there is no valid pushing position
            if (pushPos != pos) {
                Char ch = Actor.findChar(enemy.pos);
                Actor.addDelayed( new Pushing( ch, ch.pos, pushPos ), -1 );

                ch.pos = pushPos;
                Dungeon.level.occupyCell(ch );

            } else {

                Char blocker = Actor.findChar(enemy.pos);
                if (blocker.alignment != alignment){
                    blocker.damage( Random.NormalIntRange(22, 30), this );
                }
            }
        }

        GameScene.add( enemy );
    }

    private HashSet<Mob> getSubjects(){
        HashSet<Mob> subjects = new HashSet<>();
        for (Mob m : Dungeon.level.mobs){
            if (m.alignment == alignment && (m instanceof TiacauhWarrior || m instanceof TiacauhLancer || m instanceof TiacauhFanatic)){
                subjects.add(m);
            }
        }
        return subjects;
    }

    @Override
    public void die(Object cause) {
        yell(Messages.get(this, "die"));

        Bestiary.skipCountingEncounters = true;
        for (Mob m : getSubjects()) {
            m.die(null);
        }
        Bestiary.skipCountingEncounters = false;

        Dungeon.level.drop(new Certificate(40), pos).sprite.drop(pos);

        GameScene.bossSlain();
        Dungeon.level.unseal();

        Badges.validategavial1();

        super.die(cause);
    }

    @Override
    public boolean isAlive() {
        return (HP > 0 || phase < 3);
    }

    private static final String PHASE   = "phase";
    private static final String SKILL2POS   = "BurstPos";
    private static final String SKILL2TIME   = "BurstTime";
    private static final String SUMMON = "enemyspawncooldown";
    private static final String TOWER = "tomimitower";


    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
        bundle.put( SKILL2POS, BurstPos );
        bundle.put( SKILL2TIME, BurstTime );
        bundle.put( SUMMON, enemyspawncooldown );
        bundle.put( TOWER, tomimitower );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        BurstPos = bundle.getInt(SKILL2POS);
        BurstTime = bundle.getInt(SKILL2TIME);
        enemyspawncooldown = bundle.getInt(SUMMON);
        tomimitower = bundle.getInt(TOWER);
        BossHealthBar.assignBoss(this);

    }

    public static class TomimiWarrior extends TiacauhWarrior {
        {
            state = HUNTING;
            maxLvl = -1;
        }
    }

    public static class TomimiLancer extends TiacauhLancer {
        {
            state = HUNTING;
            maxLvl = -1;
        }
    }

    public static class TomimiFanatic extends TiacauhFanatic {
        {
            state = HUNTING;
            maxLvl = -1;
        }
    }

    public static class TomimiTower extends Mob {
        {
            spriteClass = Tomimi_towerSprite.class;

            HT = HP = 150;
            state = PASSIVE;

            properties.add(Property.IMMOVABLE);
            properties.add(Property.MINIBOSS);
            immunities.add( Paralysis.class );
            immunities.add( Amok.class );
            immunities.add( Sleep.class );
            immunities.add( Terror.class );
            immunities.add( Vertigo.class );
        }

        @Override
        public void beckon(int cell) {
            //do nothing
        }

        @Override
        public void damage(int dmg, Object src) {
            if (src != Tomimi.RPG7.class) {
                sprite.showStatus( CharSprite.NEUTRAL, Messages.get(Talu_BlackSnake.class, "invincibility") );
                return;
            }
            else dmg = 50;
            super.damage(dmg, src);
        }

        @Override
        public void die(Object cause) {
            tomimitower--;
            super.die(cause);
        }
    }
}
