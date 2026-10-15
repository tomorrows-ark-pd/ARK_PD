package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.BonusStageQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Bug_ASprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PompeiiSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Pompeii extends Mob {
    {
        spriteClass = PompeiiSprite.class;

        HP = HT = 2300;
        defenseSkill = 25;

        EXP = 100;

        //so that allies can attack it. States are never actually used.
        state = HUNTING;
        baseSpeed = 1f;

        viewDistance = 12;

        properties.add(Property.BOSS);
        properties.add(Property.FIERY);
        properties.add(Property.INFECTED);

        immunities.add(Amok.class);
        immunities.add(ParalyticGas.class);
        immunities.add(Terror.class);
        immunities.add(Silence.class);
        immunities.add(Blindness.class);
        immunities.add(Frost.class);
        immunities.add(Freezing.class);
        immunities.add(ScrollOfPsionicBlast.class);
    }

    private int phase = 0;
    private int blastcooldown = 0;
    private int summoncooldown = 6;
    private int barriercooldown = 4;
    private int volcanocooldown = 9;
    private int volcanotime = 0;
    private int restorecooldown = 10;

    @Override
    public int damageRoll() {
        if (phase == 3) return Random.NormalIntRange(55, 65);
        return Random.NormalIntRange(45, 55);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0,24);
    }

    @Override
    public int attackSkill(Char target) {
       return 48;
    }


    @Override
    public void damage(int dmg, Object src) {

        if (phase == 2) {
            sprite.showStatus( CharSprite.NEUTRAL, Messages.get(Talu_BlackSnake.class, "invincibility") );
            return;
        }
        if (buff(RestorBuff.class) != null) {
            HP = Math.min(HP + dmg/2, HT);
            int bufftime = Math.min(3, dmg/50);
            Buff.affect(this, Adrenaline.class, bufftime);
            this.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
            this.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", dmg/2);
            return;
        }

        if (this.buff(Barrier.class) != null) {
            dmg /= 4;
        }
        else if (volcanotime > 0) dmg /= 8;

        if (dmg > 300) {
            int thedamage = 300 + dmg/10;
            dmg = thedamage;
        }

        super.damage(dmg, src);

        if (phase==1 && HP < 1700) {
            HP = 1700;
            phase = 2;
            Buff.detach(this, Barrier.class);
            summoncooldown = 1;
            blastcooldown = 1;
            barriercooldown = 4;
            volcanocooldown = 7;
            restorecooldown = 10;
            GameScene.flash(0x80FF0000);
        }
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (buff(RestorBuff.class) != null && !(enemy instanceof Hero)) {
            damage = 0;
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    protected boolean act() {
        if (phase == 3 && HP < 1) {
            Dungeon.hero.HP = Dungeon.hero.HT;
            Badges.validateVictory();
            Badges.validateChampion(Challenges.activeChallenges());
            Badges.validateChampion_char(Challenges.activeChallenges());
            Badges.saveGlobal();

            Certificate.specialEndingBouns();

            Badges.silentValidateHappyEnd();
            Badges.validatesiesta2();
            BonusStageQuestLine.complete(BonusStageQuestLine.SiestaQuestLine.class);
            Dungeon.win(Amulet.class);
            Dungeon.deleteGame(GamesInProgress.curSlot, true);
            Game.switchScene(SurfaceScene.class);
        }
        else {
            if (Dungeon.level.map[this.pos] == Terrain.WATER) {
                if (buff(RestorBuff.class) == null) {
                if (this.buff(Barrier.class) != null) {
                    this.damage(200, this);
                    Level.set(this.pos, Terrain.EMPTY);
                    GameScene.updateMap(this.pos);
                    CellEmitter.get(this.pos).burst(Speck.factory(Speck.STEAM), 10);
                } else {
                    this.damage(6, this);
                }}
            }

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
            } else {
                if (UseAbility()) return true;
            }

            if (phase == 2) {
                spend(TICK);
                if (summoncooldown > 0) summoncooldown--;
                return true;
            }


            if (phase == 1 || phase == 3) {
                if (blastcooldown > 0) blastcooldown--;
                if (barriercooldown > 0) barriercooldown--;
                if (volcanocooldown > 0) volcanocooldown--;
                if (summoncooldown > 0) summoncooldown--;
                if (restorecooldown >0) restorecooldown--;
            }
        }
        return super.act();
    }

    private boolean UseAbility() {
        // 화염파 > 리틀 폼페이 소환 > 용암 장갑 > 화산분출 순으로 사용합니다.

        //화염파
        if (FireBlast()) return true;

        // 소환
        while (summoncooldown <= 0) {

            Mob summon = new Pompeii.BossSlug();

            int spawnPos = -1;
            for (int i : PathFinder.NEIGHBOURS8) {
                if (Actor.findChar(pos + i) == null) {
                    if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, pos + i)) {
                        spawnPos = pos + i;
                    }
                }
            }

            if (spawnPos != -1) {
                summon.pos = spawnPos;
                GameScene.add(summon);
                Actor.addDelayed(new Pushing(summon, pos, summon.pos), -1);
                summon.beckon(Dungeon.hero.pos);


                if (phase==3) summoncooldown = 7;
                else if (phase == 2) {
                    int dmg = 50 - Statistics.coreAlive*3;
                    HP -= dmg;
                    sprite.showStatus( CharSprite.WARNING,""+dmg );
                    summoncooldown = 1;
                    if (HP < 1200) {
                        HP = 1200;
                        phase = 3;
                        GameScene.flash(0x80FF0000);
                    }
                }
                else summoncooldown = 10;

                spend(TICK);
                return true;
            } else {
                spend(TICK);
                return true;
            }
        }


        // 흡수. 3페이즈 전용
        if (restorecooldown <= 0 && phase == 3) {
            Buff.affect(this, RestorBuff.class, 5f);
            GLog.w(Messages.get(this, "skill"));

            restorecooldown = 35;
        }



        //용암장갑
        if (barriercooldown <= 0) {
            if (phase == 3) Buff.affect(this, Barrier.class).setShield(150);
            else Buff.affect(this, Barrier.class).setShield(100);

            CellEmitter.center(pos).burst(FlameParticle.FACTORY, 4);
            Sample.INSTANCE.play(Assets.Sounds.BURNING, 2f);
            barriercooldown = 25 - Statistics.coreAlive;
            return true;
        }


        //화산폭발
        if (volcanocooldown <= 0) {
            PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 3);
            if (volcanotime < 3) {
                sprite.parent.addToBack(new TargetedCell(pos, 0xFF0000));

                if (volcanotime == 0 || volcanotime == 2)
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                    int vol = Fire.volumeAt(i, Fire.class);
                    if (vol < 4){
                        sprite.parent.addToBack(new TargetedCell( i, 0xFF0000));
                    }
                }}
                volcanotime+=1;
                spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2*TICK));
                return true;
            }
            else {
                boolean isHit = false;
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                        Char ch = Actor.findChar(i);
                        int vol = Fire.volumeAt(i, Fire.class);
                        if (vol < 4){
                            CellEmitter.center(i).burst(BlastParticle.FACTORY, 1);
                        }
                        if (ch != null && !isHit) {
                        if ((ch.alignment != alignment || ch instanceof Bee)) {
                            if (phase == 3) ch.damage(Random.NormalIntRange(120, 180), new Pompeii.Volcano());
                            else ch.damage(Random.NormalIntRange(80, 120), new Pompeii.Volcano());
                            if (ch.isAlive()) {
                                Buff.affect(ch, Blindness.class, 10f);
                            }
                            else if (!ch.isAlive() && ch == Dungeon.hero) {
                                Dungeon.fail(getClass());
                                GLog.n(Messages.get(Char.class, "kill", name()));
                            }
                            isHit = true;
                        }}
                    }}}

                Camera.main.shake(2, 0.5f);
                Sample.INSTANCE.play(Assets.Sounds.BLAST, 2f);
                Sample.INSTANCE.play(Assets.Sounds.BURNING, 3f);
                Buff.affect(this, Stamina.class, 2f);

                volcanotime=0;
                volcanocooldown= 10 - (Statistics.coreAlive/2);
                spend(TICK);
                return true;
            }


        return false;
    }

    public class Blast { }
    public class Volcano { }

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
                if (phase==3)  ch.damage(Random.NormalIntRange(60, 70), new Pompeii.Blast());
                else ch.damage(Random.NormalIntRange(45, 55), new Pompeii.Blast());
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

        if (blastcooldown <= 0){

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
                    CellEmitter.center(p).burst(FlameParticle.FACTORY, 14);
                    affectedCells.add(p);
                }
            }

            //don't want to overly punish players with slow move or attack speed
            Dungeon.hero.interrupt();

            if (phase == 3) blastcooldown = 4;
            else blastcooldown = 6;

            spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2*TICK));
            return true;

        }
        return false;
    }

    @Override
    public boolean isAlive() { return true; }


    @Override
    public void notice() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            if (phase == 0) {
                phase = 1;
            }
        }
    }

    private static final String PHASE   = "phase";
    private static final String BLAST_CD   = "blastcooldown";
    private static final String BARRIER_CD  = "barriercooldown";
    private static final String VOCAL_CD   = "volcanocooldown";
    private static final String VOCAL_TIME   = "volcanotime";
    private static final String SUMMON_CD   = "summoncooldown";
    private static final String RESTORE_CD   = "restorecooldown";
    private static final String TARGETED_CELLS = "targeted_cells";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
        bundle.put( BLAST_CD, blastcooldown );
        bundle.put( BARRIER_CD, barriercooldown );
        bundle.put( VOCAL_CD, volcanocooldown );
        bundle.put( VOCAL_TIME, volcanotime );
        bundle.put( SUMMON_CD, summoncooldown);
        bundle.put( RESTORE_CD, restorecooldown);

        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        blastcooldown = bundle.getInt(BLAST_CD);
        barriercooldown = bundle.getInt(BARRIER_CD);
        summoncooldown = bundle.getInt(SUMMON_CD);
        volcanocooldown = bundle.getInt(VOCAL_CD);
        volcanotime = bundle.getInt(VOCAL_TIME);
        restorecooldown = bundle.getInt(RESTORE_CD);

        if (phase != 0) BossHealthBar.assignBoss(this);

        for (int i : bundle.getIntArray(TARGETED_CELLS)){
            targetedCells.add(i);
        }

    }


    public class BossSlug extends LavaSlug {
        {
            HP = HT = 65;
            maxLvl = -5;
        }
    }


    public static class RestorBuff extends FlavourBuff {
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
