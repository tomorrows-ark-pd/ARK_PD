package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaTerror;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class W0502 extends Item {
    public static final String AC_FIRE = "FIRE";
    public static final String AC_RELOAD = "RELOAD";
    {
        image = ItemSpriteSheet.W0520;

        defaultAction = AC_FIRE;
    }

    private int charge = 3;
    private int chargecap = 3;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_FIRE);
        actions.add(AC_RELOAD);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (!Dungeon.bossLevel()){
        if (action.equals(AC_FIRE) && charge > 0) {
                GameScene.selectCell(zapper);
        }}

        if (action.equals(AC_RELOAD)) {
            Item item = Dungeon.hero.belongings.getItem(PotionOfLiquidFlame.class);
            if (item != null) {
                item.identify();
                item.detach(Dungeon.hero.belongings.backpack);
                charge = chargecap;
                updateQuickslot();
            }
            else GLog.i(Messages.get(W0502.class, "noflame"));
            curUser = hero;
        }
    }

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                final W0502 ss;
                if (curItem instanceof W0502) {
                    ss = (W0502) W0502.curItem;

                    Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.WONT_STOP);
                    int cell = shot.collisionPos;

                    if (target == curUser.pos || cell == curUser.pos) {
                        GLog.i(Messages.get(W0502.class, "self_target"));
                        return;
                    }

                    curUser.sprite.zap(cell);

                    //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                    if (Actor.findChar(target) != null)
                        QuickSlotButton.target(Actor.findChar(target));
                    else
                        QuickSlotButton.target(Actor.findChar(cell));

                    if (ss.tryToZap(curUser, target)) {
                        ss.fx(shot, new Callback() {
                            public void call() {
                                ss.onZap(shot);
                            }
                        });
                    }

                }
            }

        }

        @Override
        public String prompt() {
                return Messages.get(W0502.class, "prompt2");
        }
    };

    protected void onZap( Ballistica beam ) {

        boolean terrainAffected = false;

        int maxDistance = Math.min(8, beam.dist);

        ArrayList<Char> chars = new ArrayList<>();

        Blob web = Dungeon.level.blobs.get(Web.class);

        for (int c : beam.subPath(1, maxDistance)) {

            Char ch;
            if ((ch = Actor.findChar( c )) != null) {

                //we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
                //                //terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.

                chars.add(ch);
            }

            if (Dungeon.level.flamable[c]) {

                Dungeon.level.destroy( c );
                GameScene.updateMap( c );
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

            if (Dungeon.level.map[c] == Terrain.WATER || Dungeon.level.map[c] == Terrain.EMPTY) {
                Level.set(c, Terrain.EMBERS);
                CellEmitter.get(c).burst(Speck.factory(Speck.STEAM), 6);
                GameScene.updateMap( c );
            }

            if (Dungeon.level.map[c] == Terrain.TRAP || Dungeon.level.map[c] == Terrain.INACTIVE_TRAP || Dungeon.level.map[c] == Terrain.SECRET_TRAP) {
                Dungeon.level.map[c] = Terrain.EMBERS;
                Trap t = Dungeon.level.traps.get(c);
                    t.reveal();
                    t.disarm();
                    CellEmitter.get(t.pos).burst(Speck.factory(Speck.STEAM), 6);
                GameScene.updateMap( c );
            }

            CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        for (Char ch : chars) {
            Buff.affect(ch, Burning.class).reignite(ch);
        }

        Sample.INSTANCE.play( Assets.Sounds.BURNING, 1.32f, 1.12f );
        updateQuickslot();

        Dungeon.hero.spendAndNext(1f);
    }

    protected void fx( Ballistica beam, Callback callback ) {

        int cell = beam.path.get(Math.min(beam.dist, 8));
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FIRE_SHOT,
                curUser.sprite,
                beam.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        callback.call();
    }

    public boolean tryToZap(Hero owner, int target) {

        if (charge >= 1) {
            charge--;
            return true;
        } else {
            GLog.w(Messages.get(this, "fizzles"));
            return false;
        }
    }

    private static final String CHARGE = "charge";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (chargecap > 0) charge = Math.min(chargecap, bundle.getInt(CHARGE));
        else charge = bundle.getInt(CHARGE);
    }

    @Override
    public String status() {
        return charge + "/" + chargecap;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int value() {
        return 100;
    }
}
