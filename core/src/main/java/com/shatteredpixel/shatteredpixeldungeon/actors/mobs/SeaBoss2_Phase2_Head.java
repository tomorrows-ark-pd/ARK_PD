package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mula_1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

//패턴 : 미정
public class SeaBoss2_Phase2_Head extends Mob {
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
    private boolean dieChacke = false;
    private int cooldown = 6;

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
        if (dieChacke) return INFINITE_EVASION;
        else return 20;
    }

    // 사거리 6
    @Override
    protected boolean canAttack(Char enemy) {
        return !dieChacke && this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 6;
    }

    @Override
    protected boolean act() {
        sprite.turnTo(pos, 999999);
        rooted = true;
        if (dieChacke) {
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

        if (cooldown > 0) {
            cooldown--;
        } else {
            Ballistica beam = new Ballistica(this.pos, enemy.pos, Ballistica.WONT_STOP);
            int maxDistance = Math.min(8, beam.dist);
            int cell = beam.path.get(Math.min(beam.dist, maxDistance));
            this.sprite.parent.add(new Beam.DeathRay(this.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
            boolean terrainAffected = false;

            ArrayList<Char> chars = new ArrayList<>();

            Blob web = Dungeon.level.blobs.get(Web.class);

            int terrainPassed = 2;
            for (int c : beam.subPath(1, maxDistance)) {

                Char ch;
                if ((ch = Actor.findChar( c )) != null) {

                    //we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
                    //terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
                    terrainPassed = terrainPassed%3;

                    chars.add( ch );
                }

                if (Dungeon.level.solid[c]) {
                    terrainPassed++;
                }

                if (Dungeon.level.flamable[c]) {

                    Dungeon.level.destroy( c );
                    GameScene.updateMap( c );
                    terrainAffected = true;

                }

                Platform platform = Dungeon.level.platforms.get(c);
                if (platform != null) {
                    platform.destroy();
                    GameScene.updateMap( c );
                    terrainAffected = true;
                }

                CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
            }

            if (terrainAffected) {
                Dungeon.observe();
            }

            int dmg = Random.NormalIntRange(4, 36);
            for (Char ch : chars) {
                ch.damage(dmg, this );
                ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
                ch.sprite.flash();
            }

            if (Dungeon.isChallenged(Challenges.DECISIVE_BATTLE)) cooldown = 4;
            else cooldown = 6;
        }

        return super.act();
    }

    @Override
    protected float attackDelay() {
        return super.attackDelay() * 2;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dieChacke) return;
        
        // 믈라의 머리는 다른 부위가 파괴되지않았다면 절반의 피해를 받습니다
        if (Dungeon.mulaCount < 2) dmg/=2;

        super.damage(dmg, src);

        if (HP < 1) {
            dieChacke = true;
            Buff.affect(this, Doom.class);
            Dungeon.mulaCount++;
        }
    }


    @Override
    public void die(Object cause) { }


    private static final String DIECHACKE_HEAD   = "dieChackeHead";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DIECHACKE_HEAD, dieChacke );
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        dieChacke = bundle.getBoolean(DIECHACKE_HEAD);
    }
    }





