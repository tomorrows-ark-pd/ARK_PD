package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mula_1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

//패턴 : 미정
public class IsharmlaSeabornHead extends Mob {
    {
        spriteClass = Mula_1Sprite.class;

        HP = HT = 1000;

        defenseSkill = 25;

        properties.add(Property.SEA);
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);

        state = HUNTING;

    }

    // 모든 믈라 파츠가 파괴되면 사망
    private boolean isDead = false;
    private int cooldown = 4;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(40, 70);
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }


    @Override
    public int defenseSkill(Char enemy) {
        if (isDead) return INFINITE_EVASION;
        else return 20;
    }

    // 사거리 6
    @Override
    protected boolean canAttack(Char enemy) {
        return !isDead && this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 6;
    }

    @Override
    protected boolean act() {
        sprite.turnTo(pos, 999999);
        rooted = true;
        if (isDead) {
            if (Dungeon.mulaCount == 3) {
                Badges.validateVictory();
                Badges.validateChampion(Challenges.activeChallenges());
                Badges.validateChampion_char(Challenges.activeChallenges());
                Badges.saveGlobal();

                Certificate.specialEndingBouns();

                Badges.silentValidateHappyEnd();
                Badges.validateiberia2();
                Badges.validatewill();
                Dungeon.win(Amulet.class);
                Dungeon.deleteGame(GamesInProgress.curSlot, true);
                Game.switchScene(SurfaceScene.class);
            }
            return super.act();
        }

        if (cooldown <= 0) {

            boolean terrainAffected = false;
            HashSet<Char> affected = new HashSet<>();

            HashSet<Integer> affectedCells = new HashSet<>();
            int targetPos = Dungeon.hero.pos;

            Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
            //shoot beams
            sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
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

            int dmg = Random.NormalIntRange(4, 36);

            for (Char ch : affected) {
                ch.damage(dmg, this );
                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }

                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(Char.class, "kill", name()));
                }
            }

            if (Dungeon.isChallenged(Challenges.DECISIVE_BATTLE)) cooldown = 2;
            else cooldown = 3;
        } else {
            cooldown --;
        }

        return super.act();
    }

    @Override
    protected float attackDelay() {
        return super.attackDelay() * 2;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (isDead) return;
        
        // 믈라의 머리는 다른 부위가 파괴되지않았다면 절반의 피해를 받습니다
        if (Dungeon.mulaCount < 2) dmg/=2;

        super.damage(dmg, src);

        if (HP < 1) {
            isDead = true;
            Buff.affect(this, Doom.class);
            Dungeon.mulaCount++;
        }
    }


    @Override
    public void die(Object cause) { }


    private static final String IS_DEAD_HEAD  = "isDeadHead";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( IS_DEAD_HEAD, isDead);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        isDead = bundle.getBoolean(IS_DEAD_HEAD);
    }
    }





