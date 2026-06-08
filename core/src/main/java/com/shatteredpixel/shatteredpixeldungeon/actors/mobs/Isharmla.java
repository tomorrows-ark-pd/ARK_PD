package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Skadi_mulaSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Isharmla extends Mob {
    {
        spriteClass = Skadi_mulaSprite.class;

        HP = HT = 1500;
        defenseSkill = 60;

        actPriority = MOB_PRIO-1;

        WANDERING = new Wandering();
        HUNTING = new Hunting();

        state = WANDERING;

        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.STATIC);
    }

    int summonCooldown = 5;
    int shieldCooldown = 8;
    int shieldAmount = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 30 : 15;

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void notice() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (src != this) dmg = 0;

        super.damage(dmg, src);
    }

    private HashSet<Mob> getSubjects(){
        HashSet<Mob> subjects = new HashSet<>();
        for (Mob m : Dungeon.level.mobs){
            if (m.alignment == alignment && (m instanceof SummonRunner || m instanceof SummonLeef || m instanceof SummonOcto)){
                subjects.add(m);
            }
        }
        return subjects;
    }

    public void detach() {
        Bestiary.skipCountingEncounters = true;
        for (Mob m : getSubjects()) {
            m.die(null);
        }
        Bestiary.skipCountingEncounters = false;

        Ballistica trajectory = new Ballistica(pos, Dungeon.hero.pos, Ballistica.STOP_TARGET);
        trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
        WandOfBlastWave.throwChar(Dungeon.hero, trajectory, 6); // 넉백 효과

        IsharmlaSeabornHead.resetBoss();

        IsharmlaSeabornHead boss1 = new IsharmlaSeabornHead();
        boss1.pos = 197;
        boss1.notice();
        GameScene.add( boss1 );

        IsharmlaSeabornBody boss2 = new IsharmlaSeabornBody();
        boss2.pos = 199;
        boss2.notice();
        GameScene.add( boss2 );

        IsharmlaSeabornTail boss3 = new IsharmlaSeabornTail();
        boss3.pos = 201;
        boss3.notice();
        GameScene.add( boss3 );

        updateTerrain();

        GameScene.flash(0x80FFFFFF);
        Camera.main.shake(2, 2f);
        Dungeon.observe();
        GameScene.updateFog();
    }

    private void updateTerrain() {
        int[] positions = new int[]{197, 198, 199, 200, 201};

        for (int pos : positions) {
            Platform platform = Dungeon.level.platforms.get(pos);
            if (platform != null) {
                platform.destroy();
            }

            set( pos, Terrain.WELL );
            GameScene.updateMap( pos );
        }
    }

    @Override
    protected boolean act() {
        rooted = true;

        if (state == WANDERING) {
            return super.act();
        }

        if (summonCooldown <= 0) {
            this.damage(250,this);
            if (!this.isAlive()) {
                return super.act(); // detach() already ran via die(); skip shield code
            }
            SummonEnemy();
        } else {
            summonCooldown--;
        }

        if (shieldCooldown <= 0) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.isAlive() && (mob instanceof SummonRunner || mob instanceof SummonLeef || mob instanceof SummonOcto)) {
                    Buff.affect(mob, Barrier.class).setShield(shieldAmount);
                }
            }
            shieldCooldown = (Dungeon.isChallenged(Challenges.DECISIVE_BATTLE)) ? 8 : 12;
        } else {
            shieldCooldown--;
        }

        return super.act();
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        //yell(Messages.get(this, "die"));
        this.detach();
    }

    // summon pos = 50, 54

    private void SummonEnemy()
    {
        Mob summonEnemy1;
        int summonpos1 = 169;
        if (Random.Int(4) != 0)
            summonEnemy1 = new SummonRunner();
        else
            summonEnemy1 = new SummonOcto();

        Mob summonEnemy2;
        int summonpos2 = 187;
        if (Random.Int(4) != 0)
            summonEnemy2 = new SummonRunner();
        else
            summonEnemy2 = new SummonLeef();

        Mob summonEnemy3;
        int summonpos3 = 196;
        if (Random.Int(4) != 0)
            summonEnemy3 = new SummonRunner();
        else
            summonEnemy3 = new SummonLeef();

        Mob summonEnemy4;
        int summonpos4 = 192;
        if (Random.Int(4) != 0)
            summonEnemy4 = new SummonRunner();
        else
            summonEnemy4 = new SummonOcto();


        summonEnemy1.pos = summonpos1;
        GameScene.add(summonEnemy1, 1f);
        if (summonpos1 == Dungeon.hero.pos)  ScrollOfTeleportation.teleportChar_unobstructed(summonEnemy1);

        summonEnemy2.pos = summonpos2;
        GameScene.add(summonEnemy2, 1f);
        if (summonpos2 == Dungeon.hero.pos)  ScrollOfTeleportation.teleportChar_unobstructed(summonEnemy2);

        summonEnemy3.pos = summonpos3;
        GameScene.add(summonEnemy3, 1f);
        if (summonpos3 == Dungeon.hero.pos)  ScrollOfTeleportation.teleportChar_unobstructed(summonEnemy3);

        summonEnemy4.pos = summonpos4;
        GameScene.add(summonEnemy4, 1f);
        if (summonpos4 == Dungeon.hero.pos)  ScrollOfTeleportation.teleportChar_unobstructed(summonEnemy4);

        for (Mob mob : Dungeon.level.mobs) {
            mob.beckon( Dungeon.hero.pos );
        }
        summonCooldown = 10;

    }

    // 보스 소환물 (3종) 한번에 4마리 소환
    // 기본 소환은 런너 (확률 75%)
    // 각각 25%확률로 Octo, Leaf가 소환될 수 있음
    public static class SummonRunner extends SeaRunner {
        {
            state = HUNTING;

            immunities.add(Drowsy.class);
            immunities.add(MagicalSleep.class);
            immunities.add(AllyBuff.class);

            //no loot or exp
            maxLvl = -5;
        }
    }

    public static class SummonOcto extends Sea_Octo {
        {
            state = HUNTING;

            immunities.add(Drowsy.class);
            immunities.add(MagicalSleep.class);
            immunities.add(AllyBuff.class);

            //no loot or exp
            maxLvl = -5;
        }
    }

    public static class SummonLeef extends SeaLeef {
        {
            state = HUNTING;

            immunities.add(Drowsy.class);
            immunities.add(MagicalSleep.class);
            immunities.add(AllyBuff.class);

            //no loot or exp
            maxLvl = -5;
        }
    }

    protected class Wandering implements AiState {

        public static final String TAG	= "PASSIVE";

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;
            if (enemySeen) {
                notice();
                state = HUNTING;
            }
            spend( TICK );
            return true;
        }
    }

    protected class Hunting implements AiState {

        public static final String TAG	= "PASSIVE";

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            spend( TICK );
            return true;
        }
    }
}
