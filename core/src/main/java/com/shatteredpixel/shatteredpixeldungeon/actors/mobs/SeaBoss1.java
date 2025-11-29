package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SeaObject;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.First_talkSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HandclapSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SeaBoss1 extends Mob{
    {
        spriteClass = First_talkSprite.class;

        HP = HT = 1000;
        EXP = 40;

        defenseSkill = 25;

        state = HUNTING;

        properties.add(Property.SEA);
        properties.add(Property.BOSS);
    }

    boolean SkillActive = false; // true라면 받는 피해 감소
    int phase = 0;

    int skillCD = 7;
    int skillAttackPoint1 = 0;

    int skillProcVaule = 0; // 스킬 진행상항.


    @Override
    public int damageRoll() {
        if (HP <= 500) return Random.NormalIntRange(45, 60);
        return Random.NormalIntRange(30, 50);
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 16);
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        if (enemy instanceof Hero || enemy instanceof DriedRose.GhostHero) {
            if (enemy.buff(NervousImpairment.class) == null) {
                Buff.affect(enemy, NervousImpairment.class);
            } else enemy.buff(NervousImpairment.class).sum(10);
        }

        return super.attackProc(enemy, damage);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (SkillActive) dmg /= 3;

        super.damage(dmg, src);

        if (phase==1 && HP <= 600) {
            HP = 600;
            phase = 2;
            GameScene.flash(0x80FF0000);
            Buff.affect(Dungeon.hero, Silence.class, 15f);
        }
    }

    @Override
    protected boolean act() {

        if (skillCD > 0) skillCD--;
        else {

            Skill();
            SkillActive = true;
            spend(TICK);
            return true;
        }

        if (phase == 2) {
            int healpoint = 4;
            if (Dungeon.isChallenged(Challenges.DECISIVE_BATTLE)) healpoint = 8;
            this.HP = Math.min(this.HT, this.HP + healpoint);
            this.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
        }

        return super.act();
    }

    public static class SeaBoss_SkillAttack{};

    boolean Skill() {
        switch (skillProcVaule) {
            case 0:
                skillAttackPoint1 = Dungeon.hero.pos;
                sprite.parent.addToBack(new TargetedCell(skillAttackPoint1, 0xFF0000));
                Camera.main.shake(2, 0.5f);
                break;
            case 1:
                skillAttackPoint1 = Dungeon.hero.pos;
                sprite.parent.addToBack(new TargetedCell(skillAttackPoint1, 0xFF0000));
                break;
            case 2:
                skillAttackPoint1 = Dungeon.hero.pos;
                sprite.parent.addToBack(new TargetedCell(skillAttackPoint1, 0xFF0000));
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                SkillAttack(skillAttackPoint1);
                break;
        }

        skillProcVaule++;

        if (skillProcVaule == 10) {
            skillProcVaule = 0;
            skillCD = 8;
            SkillActive = false;
        }

        return  true;
    }


    void SkillAttack(int point) {
        PathFinder.buildDistanceMap(point, BArray.not(Dungeon.level.solid, null), 1);
        for (int cell = 0; cell < PathFinder.distance.length; cell++) {
            if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                CellEmitter.center(cell).burst(HandclapSprite.GooParticle.FACTORY, 10);
                Char ch = Actor.findChar(cell);
                if (ch != null) {
                    int damage = Random.IntRange(13, 19);
                    if (ch != Dungeon.hero) damage /= 4;
                    if (ch == this) damage = 0;
                    if (ch instanceof SeaObject) damage = Random.IntRange(95,110);
                    ch.damage(damage, SeaBoss_SkillAttack.class);

                    if (ch instanceof Hero || ch instanceof DriedRose.GhostHero) {
                        if (ch.buff(NervousImpairment.class) == null) {
                            Buff.affect(ch, NervousImpairment.class);
                        } else ch.buff(NervousImpairment.class).sum(20);
                    }

                    if (!ch.isAlive() && ch == Dungeon.hero) {
                        Dungeon.fail(getClass());
                        GLog.n(Messages.get(this, "destroy"));
                    }
                }}}
    }

    @Override
    public void die(Object cause) {
        Badges.validateiberia1();

       // yell(Messages.get(this, "defeated")); 보스 사망 대사


        GameScene.bossSlain();
        Dungeon.level.unseal();
        super.die( cause );
    }

    @Override
    public void notice() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
           // yell(Messages.get(this, "notice"));

            if (phase == 0) {
                phase = 1;
            }
        }
    }

    private static final String PHASE   = "phase";


    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        BossHealthBar.assignBoss(this);

    }
}
