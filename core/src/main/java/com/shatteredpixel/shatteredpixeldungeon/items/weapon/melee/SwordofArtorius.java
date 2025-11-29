package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMistress;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaTerror;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SwordofArtorius extends MeleeWeapon {
    {
        image = ItemSpriteSheet.ARTORIUS;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.12f;

        tier = 5;
    }

    @Override
    public int max(int lvl) {
        return  4 * (tier-2) +
                lvl*(tier-2);
    }

    public String statsInfo() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", 4+buffedLvl(), 12+buffedLvl()*4);
        } else {
            return Messages.get(this, "typical_stats_desc", 4, 12);
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        Ballistica beam = new Ballistica(attacker.pos, defender.pos, Ballistica.WONT_STOP);
        int maxDistance = Math.min(5, beam.dist);
        int cell = beam.path.get(Math.min(beam.dist, maxDistance));
        attacker.sprite.parent.add(new Beam.DeathRay(attacker.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
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

        int dmg = Random.NormalIntRange(4+buffedLvl(), 12+buffedLvl()*4);
        if(isSetbouns()) dmg *= 1.3f;
        for (Char ch : chars) {
            ch.damage(dmg, this );
            ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
            ch.sprite.flash();
        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (isSetbouns()) info += "\n\n" + Messages.get( SwordofArtorius.class, "setbouns");

        return info;
    }

    private boolean isSetbouns() {
        if (Dungeon.hero.belongings.getItem(RingOfMistress.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfMistress.class).isEquipped(Dungeon.hero))
                return true;
        }
        return false;
    }
}
