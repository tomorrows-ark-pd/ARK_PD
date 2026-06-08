package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WaterParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mula_1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossMultiHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

//패턴 : 1) 발판 파괴 미법공격 레이저; 2) 근거리 물리공격; 3) 파도 특수패턴
public class IsharmlaSeabornHead extends Mob {
    {
        spriteClass = Mula_1Sprite.class;

        HP = HT = 1000;

        defenseSkill = 20;

        actPriority = MOB_PRIO - 1;

        properties.add(Property.SEA);
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);

        state = new Hunting();
    }

    // 모든 믈라 파츠가 파괴되면 사망
    @Getter
    private boolean isDead = false;

    private int laserCooldown = 6;
    private static boolean isAngry = false;
    private static boolean isEnraged = false;
    private static boolean isHeadEnraged = false;
    private static int enrageDuration = 20;
    private int waveCooldown = 1;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(40, 70);
    }

    @Override
    public int attackSkill(Char target) {
        return 50;
    }

    @Override
    public void notice() {
        BossMultiHealthBar.assignBoss(this);
    }

    @Override
    public int defenseSkill(Char enemy) {
        if (isDead) return INFINITE_EVASION;

        // 캐릭터가 물 밖이라면 데미지를 입지 않습니다
        if (enemy instanceof Hero && Dungeon.level.map[enemy.pos] == Terrain.EMPTY) {
            return INFINITE_EVASION;
        } else return super.defenseSkill(enemy);
    }

    // 사거리 2
    @Override
    protected boolean canAttack(Char enemy) {
        return !isDead && this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 2;
    }

    @Override
    protected boolean act() {

        sprite.turnTo(pos, 999999);
        rooted = true;

        if (isDead) {
            if (Dungeon.mulaCount == 3 || allBodyPartsDead()) {
                Badges.validateVictory();
                Badges.validateChampion(Challenges.activeChallenges());
                Badges.validateChampion_char(Challenges.activeChallenges());
                Badges.saveGlobal();

                Certificate.specialEndingBouns();

                Badges.silentValidateHappyEnd();
                Badges.validateiberia2();
                Dungeon.win(Amulet.class);
                Dungeon.deleteGame(GamesInProgress.curSlot, true);
                Game.switchScene(SurfaceScene.class);
            }
            alerted = false;
            return super.act();
        }

        if (laserCooldown <= 0) {

            boolean terrainAffected = false;
            HashSet<Char> affected = new HashSet<>();
            int targetPos = Dungeon.hero.pos;

            Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
            //shoot beams
            sprite.parent.add(new Beam.WaterRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
            for (int p : b.path) {
                Char ch = Actor.findChar(p);
                if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
                    affected.add(ch);
                }
                if (Dungeon.level.flamable[p]) {
                    Dungeon.level.destroy(p);
                    GameScene.updateMap(p);
                    terrainAffected = true;
                }

                Platform platform = Dungeon.level.platforms.get(p);
                if (platform != null) {
                    platform.destroy();
                    GameScene.updateMap(p);
                    terrainAffected = true;
                }
            }
            if (terrainAffected) {
                Dungeon.observe();
            }

            int dmg = Random.NormalIntRange(12, 34);

            for (Char ch : affected) {
                ch.damage(dmg, this);
                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }

                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(Char.class, "kill", name()));
                }
            }

            laserCooldown = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 5 : 6;
        } else {
            laserCooldown--;
        }

        if (isEnraged) {
            specialAttack();
        } else if (isAngry) {
            specialAttack();
            enrageDuration--;
            if (enrageDuration <= 0) {
                isAngry = false;
            }
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {

        if (isDead) return;

        // 캐릭터가 물 밖이라면 데미지를 입지 않습니다
        int heroTile = Dungeon.level.map[Dungeon.hero.pos];
        if (heroTile == Terrain.EMPTY || heroTile == Terrain.EMPTY_DECO) {
            return;
        }

        // 믈라의 머리는 파괴되지 않은 부위 하나당 33/50%의 피해저항을 얻습니다
        float resistance = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 0.50f : 0.33f;
        dmg = (int) (dmg * (1.0 - (resistance * (2 - Dungeon.mulaCount))));

        int hpThreshold = HT / 2;
        super.damage(dmg, src);

        if (HP < hpThreshold && !isHeadEnraged) {
            HP = hpThreshold;
            isHeadEnraged = true;
            IsharmlaSeabornHead.triggerAnger();
        } else if (HP < 1) {
            isDead = true;
            Buff.affect(this, Doom.class);
            Dungeon.mulaCount++;
        }
    }

    @Override
    public void die(Object cause) {
    }

    @Override
    public boolean isAlive() {
        return !isDead;
    }

    private boolean allBodyPartsDead() {
        for (Mob mob : Dungeon.level.mobs) {
            if ((mob instanceof IsharmlaSeabornHead
                    || mob instanceof IsharmlaSeabornBody
                    || mob instanceof IsharmlaSeabornTail) && mob.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public static void triggerAnger() {
        if (IsharmlaSeabornHead.isHeadEnraged) {
            isEnraged = true;
            enrageDuration = 999;
        } else {
            int newDuration = (Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 20 : 15) * Dungeon.mulaCount;
            if (!isAngry || newDuration > enrageDuration) {
                enrageDuration = newDuration;
            }
            isAngry = true;
        }
    }

    public static void resetBoss() {
        isAngry = false;
        isEnraged = false;
        isHeadEnraged = false;
        enrageDuration = 20;
    }

    public void specialAttack() {
        if (waveCooldown > 0) {
            waveCooldown--;
        } else {
            waveCooldown = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) || isEnraged ? 2 : 3;
            sendWaves(this);
        }
    }

    public static void sendWaves(final Char thrower) {
        WaveAbility waveAbility = Buff.append(thrower, WaveAbility.class);
        waveAbility.width = isHeadEnraged ? 7 : 3 + 2 * Dungeon.mulaCount;
        waveAbility.setStartPos();
    }

    private static final String IS_DEAD_HEAD = "isDeadHead";
    private static final String LASER_COOLDOWN = "laserCooldown";
    private static final String IS_ANGRY = "isAngry";
    private static final String IS_ENRAGED = "isEnraged";
    private static final String IS_HEAD_ENRAGED = "isHeadEnraged";
    private static final String ENRAGE_DURATION = "enrageDuration";
    private static final String WAVE_COOLDOWN = "waveCooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(IS_DEAD_HEAD, isDead);
        bundle.put(IS_ANGRY, isAngry);
        bundle.put(IS_ENRAGED, isEnraged);
        bundle.put(IS_HEAD_ENRAGED, isHeadEnraged);

        bundle.put(LASER_COOLDOWN, laserCooldown);
        bundle.put(ENRAGE_DURATION, enrageDuration);
        bundle.put(WAVE_COOLDOWN, waveCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        isDead = bundle.getBoolean(IS_DEAD_HEAD);
        isAngry = bundle.getBoolean(IS_ANGRY);
        isEnraged = bundle.getBoolean(IS_ENRAGED);
        isHeadEnraged = bundle.getBoolean(IS_HEAD_ENRAGED);

        laserCooldown = bundle.getInt(LASER_COOLDOWN);
        enrageDuration = bundle.getInt(ENRAGE_DURATION);
        waveCooldown = bundle.getInt(WAVE_COOLDOWN);
    }

    public static class WaveAbility extends Buff {
        public int start;
        public int width;
        public int previousStart = -1;
        private int[] curCells;

        HashSet<Integer> toCells = new HashSet<>();

        @Override
        public boolean act() {

            if (target instanceof IsharmlaSeabornHead && ((IsharmlaSeabornHead) target).isDead()) {
                detach();
                return true;
            }

            toCells.clear();

            if (curCells == null) {
                curCells = initialCells(start);
                spreadFromCells(curCells);
            } else {
                for (Integer c : curCells) {
                    if (WaterBlob.volumeAt(c, WaterBlob.class) > 0) spreadFromCell(c);
                }
            }

            for (Integer c : curCells) {
                toCells.remove(c);
            }

            if (toCells.isEmpty()) {
                detach();
            } else {
                curCells = new int[toCells.size()];
                int i = 0;
                for (Integer c : toCells) {
                    GameScene.add(Blob.seed(c, 2, WaterBlob.class));
                    curCells[i] = c;
                    i++;
                }
            }

            spend(TICK);
            return true;
        }

        private int[] initialCells(int cell) {
            HashSet<Integer> cells = new HashSet<>();
            cells.add(cell);
            addLeft(cell, width / 2, cells);
            addRight(cell, width / 2, cells);
            return convertToArray(cells);
        }

        private int[] convertToArray(Set<Integer> cells) {
            int[] outArr = new int[cells.size()];
            int index = 0;
            for (int cell : cells) {
                outArr[index] = cell;
                index++;
            }
            return outArr;
        }

        private void addLeft(int cell, int width, Set<Integer> cells) {
            for (int i = 1; i <= width; i++) {
                if (!Dungeon.level.solid[cell - i]) {
                    cells.add(cell - i);
                }
            }
        }

        private void addRight(int cell, int width, Set<Integer> cells) {
            for (int i = 1; i <= width; i++) {
                if (!Dungeon.level.solid[cell + i]) {
                    cells.add(cell + i);
                }
            }
        }

        private void spreadFromCells(int[] cells) {
            for (int cell : cells) {
                spreadFromCell(cell);
            }
        }

        private void spreadFromCell(int cell) {
            if (!Dungeon.level.solid[cell + PathFinder.NEIGHBOURS4[3]]) {
                toCells.add(cell + PathFinder.NEIGHBOURS4[3]);
            }
        }

        private void setStartPos() {
            int newStart = 199;
            for (int i = 0; i < 10; i++) {
                newStart = Random.Int(169 + width / 2, 187 - width / 2);
                if (previousStart != newStart) {
                    break;
                }
            }
            start = newStart;
            previousStart = newStart;
        }

        private static final String START = "start";
        private static final String WIDTH = "width";
        private static final String CUR_CELLS = "cur_cells";
        private static final String PREVIOUS_START = "previousStart";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(START, start);
            bundle.put(WIDTH, width);
            bundle.put(CUR_CELLS, curCells);
            bundle.put(PREVIOUS_START, previousStart);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            start = bundle.getInt(START);
            width = bundle.getInt(WIDTH);
            curCells = bundle.getIntArray(CUR_CELLS);
            previousStart = bundle.getInt(PREVIOUS_START);
        }

        public static class WaterBlob extends Blob {

            {
                actPriority = BUFF_PRIO - 1;
                alwaysVisible = true;
            }

            @Override
            protected void evolve() {

                boolean burned = false;

                int cell;
                for (int i = area.left; i < area.right; i++) {
                    for (int j = area.top; j < area.bottom; j++) {
                        cell = i + j * Dungeon.level.width();
                        off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

                        if (off[cell] > 0) {
                            volume += off[cell];
                        }

                        if (cur[cell] > 0 && off[cell] == 0) {

                            Char ch = Actor.findChar(cell);
                            if (ch != null
                                    && !(ch instanceof IsharmlaSeabornHead)
                                    && !(ch instanceof IsharmlaSeabornBody)
                                    && !(ch instanceof IsharmlaSeabornTail)) {
                                Buff.prolong(ch, Blindness.class, 3f);
                                Buff.affect(ch, NervousImpairment.class).sum(40);
                                ch.damage(Random.Int(25, 55), this);
                            }

                            burned = true;
                            CellEmitter.get(cell).start(WaterParticle.SPLASHING, 0.07f, 10);
                        }
                    }
                }

                if (burned) {
                    Sample.INSTANCE.play(Assets.Sounds.SPLASH);
                }
            }

            @Override
            public void use(BlobEmitter emitter) {
                super.use(emitter);
                emitter.y -= DungeonTilemap.SIZE * 0.2f;
                emitter.height *= 0.4f;
                emitter.pour(WaterParticle.FALLING, 0.2f);
            }

            @Override
            public String tileDesc() {
                return Messages.get(this, "desc");
            }
        }
    }

    protected class Hunting implements AiState {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy)) {

                target = enemy.pos;
                return doAttack(enemy);

            } else {
                spend(TICK);
                return true;
            }
        }
    }
}





