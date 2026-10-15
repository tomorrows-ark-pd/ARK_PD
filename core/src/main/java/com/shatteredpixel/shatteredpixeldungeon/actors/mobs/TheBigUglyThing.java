package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.BonusStageQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Big_UglySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FanaticSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HandclapSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class TheBigUglyThing extends Mob {
    {
        spriteClass = Big_UglySprite.class;

        HT = HP = 1700;
        defenseSkill = 25;
        EXP = 100;

        state = HUNTING;

        properties.add(Property.BOSS);
        immunities.add(Poison.class);
        immunities.add(Paralysis.class);
        immunities.add(Silence.class);
    }

    private int phase = 0; // 1~3까지
    private int beamcooldown = 0;
    private int summoncooldown = 4; // 소환 쿨타임
    private int ragecooldown = 5; // 격노 쿨타임
    private int firecooldown = 1; // 사격 쿨타임
    private int firetime = 0; // 1이면 사격능력 발동

    @Override
    public int damageRoll() {
        if (buff(rageBuff.class) != null) Random.NormalIntRange( 120, 150 );
        return Random.NormalIntRange( 50, 65 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 20);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (buff(rageBuff.class) != null || firetime != 0) {
            dmg /= 5;
        }

        if (dmg > 500) {
            int thedamage = 500 + dmg/10;
            dmg = thedamage;
        }

        super.damage(dmg, src);

        if (phase==1 && HP <= 500) {
            HP = 600;
            phase = 2;
            firetime = 0;
            Buff.detach(this, rageBuff.class);
            GameScene.flash(0x80FF0000);
            Buff.affect(this, Barrier.class).setShield(1000);
        }
    }

    @Override
    protected boolean act() {
        if (phase == 0) {
            phase = 1;
            BossHealthBar.assignBoss(this);
        }
        if (phase == 3 && HP < 1) {
            Dungeon.hero.HP = Dungeon.hero.HT;
            Badges.validateVictory();
            Badges.validateChampion(Challenges.activeChallenges());
            Badges.validateChampion_char(Challenges.activeChallenges());
            Badges.saveGlobal();

            Certificate.specialEndingBouns();

            Badges.silentValidateHappyEnd();
            Badges.validategavial2();
            BonusStageQuestLine.complete(BonusStageQuestLine.GavialQuestLine.class);
            Dungeon.win(Amulet.class);
            Dungeon.deleteGame(GamesInProgress.curSlot, true);
            Game.switchScene(SurfaceScene.class);
        }

        if (buff(Barrier.class) != null) {
            HP = Math.min(HP + 20, HT);
        }
        else if (buff(Barrier.class) == null && phase == 2) {
            HP = Math.max(HP, 800);
            phase = 3;
            GameScene.flash(0x80FF0000);
        }

        if (phase != 2) {
            UseAbility();
        }
        else {
            FireBlast();
            spend(TICK);
            if (beamcooldown >= 1) beamcooldown--;
            return true;
        }

        if (phase != 2) {
            if (summoncooldown >= 1) summoncooldown--;
            if (ragecooldown >= 1) ragecooldown--;
            if (firecooldown >= 1) firecooldown--;
        }

        return super.act();
    }

    protected boolean UseAbility() {
        // 우선 순서는 격노 > 원거리 공격 > 소환 순입니다. 단, 격노 상태일 땐 발동하지 않습니다.

        if (buff(rageBuff.class) != null) return true;

        if (ragecooldown <= 0 && firetime == 0) {
            Buff.affect(this, rageBuff.class, 4f);
            GameScene.flash(0x80FF0000);
            GLog.w(Messages.get(this, "rage"));
            spend(TICK);
            ragecooldown = 12;
            Sample.INSTANCE.play(Assets.Sounds.BURNING, 1.3f);
            return true;
        }

        else if (firecooldown <= 0) {
            if (firetime == 0) {
                GLog.w(Messages.get(this, "fire_ready"));
                sprite.parent.addToBack(new TargetedCell(Dungeon.hero.pos, 0xFF0000));
                firetime++;
                return true;
            }
            else {
                Char Target = Dungeon.hero;
                int damage = Random.IntRange(60,90);
                damage -= Target.drRoll();
                if (Target.buff(PortableCover.CoverBuff.class) == null) {
                    if (phase == 1) damage += Target.HT / 4;
                    else damage += Target.HT / 2;

                    Target.damage(damage, this);

                    if (Target.isAlive()) Buff.affect(Target, Paralysis.class, 1f);
                }
                else {
                    damage /= 2;
                    Target.damage(damage, this);
                }

                Sample.INSTANCE.play(Assets.Sounds.ZAP_GUN, 1.3f);
                CellEmitter.center(Dungeon.hero.pos).burst(BlastParticle.FACTORY, 3);
                Camera.main.shake(2, 0.5f);
                firetime = 0;
                if (phase == 1) firecooldown = 18;
                else firecooldown = 7;

                spend(1f);

                return true;
            }
        }

        else if (summoncooldown <= 0) {
            Mob summonEnemy;
            if (phase == 1) summonEnemy = new BossRipper();
            else summonEnemy = new BossBrave();

            int summonpos = Dungeon.hero.pos;

            Ballistica trajectory = new Ballistica(pos, Dungeon.hero.pos, Ballistica.STOP_TARGET);
            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
            WandOfBlastWave.throwChar(Dungeon.hero, trajectory, 3); // 넉백 효과

            summonEnemy.pos = summonpos;
            GameScene.add(summonEnemy, 1f);
            if (summonpos == Dungeon.hero.pos)  ScrollOfTeleportation.teleportChar_unobstructed(summonEnemy);

                    summoncooldown = 10;

            return true;
        }

        return true;
    }

    public class Beam {}

    private ArrayList<Integer> targetedCells = new ArrayList<>();
    private boolean FireBlast() {
        boolean terrainAffected = false;
        HashSet<Char> affected = new HashSet<>();
        //delay fire on a rooted hero
        if (!Dungeon.hero.rooted) {
            for (int i : targetedCells) {
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                //shoot beams
                for (int p : b.path) {
                    CellEmitter.center(p).burst(BlastParticle.FACTORY, 7);
                    Char ch = Actor.findChar(p);
                    if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
                        affected.add(ch);
                    }
                    if (Dungeon.level.flamable[p]) {
                        Dungeon.level.destroy(p);
                        GameScene.updateMap(p);
                        terrainAffected = true;
                    }
                }

                Sample.INSTANCE.play(Assets.Sounds.BLAST, 2f);
                Camera.main.shake(2, 0.5f);
            }
            if (terrainAffected) {
                Dungeon.observe();
            }
            for (Char ch : affected) {
                if (phase==3)  ch.damage(Random.NormalIntRange(60, 70), new TheBigUglyThing.Beam());
                else ch.damage(Random.NormalIntRange(45, 55), new TheBigUglyThing.Beam());
                if (ch.isAlive()) Buff.affect(ch, Paralysis.class, 1f);

                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }
                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(Char.class, "kill", name()));
                }
            }
            targetedCells.clear();
        }

        if (beamcooldown <= 0){

            int beams = 3;
            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++){

                int targetPos = Dungeon.hero.pos;
                if (i != 0){
                    do {
                        targetPos = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                    } while (Dungeon.level.trueDistance(pos, Dungeon.hero.pos)
                            > Dungeon.level.trueDistance(pos, targetPos));
                }
                targetedCells.add(targetPos);
                Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                affectedCells.addAll(b.path);
            }

            //remove one beam if multiple shots would cause every cell next to the hero to be targeted
            boolean allAdjTargeted = true;
            for (int i : PathFinder.NEIGHBOURS9){
                if (!affectedCells.contains(Dungeon.hero.pos + i) && Dungeon.level.passable[Dungeon.hero.pos + i]){
                    allAdjTargeted = false;
                    break;
                }
            }
            if (allAdjTargeted){
                targetedCells.remove(targetedCells.size()-1);
            }
            for (int i : targetedCells){
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                for (int p : b.path){
                    sprite.parent.add(new TargetedCell(p, 0xFF0000));
                    affectedCells.add(p);
                }
            }

            //don't want to overly punish players with slow move or attack speed
            Dungeon.hero.interrupt();

            beamcooldown = 3;

            spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2*TICK));
            return true;

        }
        return false;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    private static final String PHASE   = "phase";
    private static final String BEAM   = "beamcooldown";
    private static final String SUMMONCD   = "summoncooldown";
    private static final String RAGECD   = "ragecooldown";
    private static final String FIRECD   = "firecooldown";
    private static final String FIRETIME   = "firetime";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
        bundle.put( BEAM, beamcooldown );
        bundle.put( SUMMONCD, summoncooldown );
        bundle.put( RAGECD, ragecooldown );
        bundle.put( FIRECD, firecooldown );
        bundle.put( FIRETIME, firetime );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        beamcooldown = bundle.getInt(BEAM);
        summoncooldown = bundle.getInt(SUMMONCD);
        ragecooldown = bundle.getInt(RAGECD);
        firecooldown = bundle.getInt(FIRECD);
        firetime = bundle.getInt(FIRETIME);
        BossHealthBar.assignBoss(this);
    }



    public static class BossRipper extends TiacauhRipper {
        {
            state = HUNTING;
            maxLvl = -1;
        }
    }

    public static class BossBrave extends TiacauhBrave {
        {
            state = HUNTING;
            maxLvl = -1;
        }
    }

    public static class rageBuff extends FlavourBuff {
        {
            immunities.add(ToxicGas.class);
            immunities.add(CorrosiveGas.class);
        }
        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.HIKARI);
            else target.sprite.remove(CharSprite.State.HIKARI);
        }
    }
}
