package com.shatteredpixel.shatteredpixeldungeon.items.wands.SP;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAmplified;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaTerror;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StaffOfVigna extends DamageWand {
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing( 0xDC143C );
    {
        image = ItemSpriteSheet.WAND_DISINTEGRATION;

        collisionProperties = Ballistica.WONT_STOP;
    }


    public int min(int lvl){
        return 4+lvl;
    }

    public int max(int lvl){
        return 8+6*lvl+ RingOfAmplified.DamageBonus(Dungeon.hero) * 6;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return COL;
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return dst;
    }

    @Override
    protected void onZap( Ballistica beam ) {

        boolean terrainAffected = false;

        int level = buffedLvl();

        int maxDistance = Math.min(distance(), beam.dist);

        ArrayList<Char> chars = new ArrayList<>();

        Blob web = Dungeon.level.blobs.get(Web.class);

        int terrainPassed = 2, terrainBonus = 0;
        for (int c : beam.subPath(1, maxDistance)) {

            Char ch;
            if ((ch = Actor.findChar( c )) != null) {

                //we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
                //terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
                terrainBonus += terrainPassed/3;
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

            SeaTerror seaTerror = Dungeon.level.seaTerrors.get(c);
            if (seaTerror != null) {
                seaTerror.destroy();
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

        int lvl = level + (chars.size()-1) + terrainBonus;
        for (Char ch : chars) {
            processSoulMark(ch, chargesPerCast());
            ch.damage( damageRoll(lvl), this );
            Buff.affect(ch, Vulnerable.class, 2 + lvl);
            ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
            ch.sprite.flash();
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //no direct effect, see magesStaff.reachfactor
    }

    private int distance() {
        return buffedLvl() / 2 + 3;
    }

    @Override
    protected void fx( Ballistica beam, Callback callback ) {

        int cell = beam.path.get(Math.min(beam.dist, distance()));
        curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
        callback.call();
    }

}
