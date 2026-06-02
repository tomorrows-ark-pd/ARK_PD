package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Gamzashield extends MeleeWeapon {
    public static final String AC_ZAP = "ZAP";

    {
        image = ItemSpriteSheet.GAMZA_SHIELD;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1f;
        usesTargeting = true;
        bones = true;

        tier = 3;

        defaultAction = AC_ZAP;
    }

    protected int collisionProperties = Ballistica.MAGIC_BOLT;

    @Override
    public int max(int lvl) {
        return Math.round(2.5f * (tier + 1)) +     //10 base, down from 20
                lvl * (tier - 1);                   //+2 per level, down from +4
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        SPCharge(Random.IntRange(5, 5 + buffedLvl()));
        updateQuickslot();
        return super.proc(attacker, defender, damage);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ZAP);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ZAP) && isEquipped(hero)) {
            curUser = hero;
            curItem = this;
            GameScene.selectCell(zapper);
        }
    }


    @Override
    public int defenseFactor(Char owner) {
        return 4 + 2 * buffedLvl();     //4 extra defence, plus 2 per level;
    }

    public String statsInfo() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", 4 + 2 * buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 4);
        }
    }

    @Override
    public String status() {

        //if the artifact isn't IDed, or is cursed, don't display anything
        if (!isIdentified() || cursed) {
            return null;
        }
        //display as percent
        if (chargeCap == 100)
            return Messages.format("%d%%", (int) charge);


        //otherwise, if there's no charge, return null.
        return null;
    }


    public boolean tryToZap(Hero owner, int target) {

        if (owner.buff(MagicImmune.class) != null) {
            GLog.w(Messages.get(this, "no_magic"));
            return false;
        }

        if (charge >= chargeCap) {
            return true;
        } else {
            GLog.w(Messages.get(this, "fizzles"));
            return false;
        }
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return new Ballistica(user.pos, dst, collisionProperties).collisionPos;
    }

    protected int collisionProperties(int target) {
        return collisionProperties;
    }

    protected void fx(Ballistica beam, Callback callback) {
        curUser.sprite.parent.add(
                new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
        callback.call();
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                final Gamzashield gamza;
                if (curItem instanceof Gamzashield) {
                    gamza = (Gamzashield) Gamzashield.curItem;

                    Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
                    int cell = shot.collisionPos;

                    if (target == curUser.pos || cell == curUser.pos) {
                        GLog.i(Messages.get(Gamzashield.class, "self_target"));
                        return;
                    }

                    curUser.sprite.zap(cell);

                    //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                    if (Actor.findChar(target) != null)
                        QuickSlotButton.target(Actor.findChar(target));
                    else
                        QuickSlotButton.target(Actor.findChar(cell));

                    if (gamza.tryToZap(curUser, target)) {
                        curUser.busy();
                        gamza.fx(shot, new Callback() {
                            public void call() {
                                gamza.onZap(shot);
                            }
                        });
                    }

                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Gamzashield.class, "prompt");
        }
    };


    protected void onZap(Ballistica beam) {
        affectMap(beam);
        charge = 0;
        updateQuickslot();

        Char ch = Actor.findChar(beam.collisionPos);
        if (ch != null) {
            affectTarget(ch);
        }

        Invisibility.dispel();
        curUser.spendAndNext(0f);
    }

    private void affectTarget(Char ch) {

        Buff.prolong(ch, Blindness.class, 7f);
        ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6);
        ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
        Sample.INSTANCE.play(Assets.Sounds.SHINNING);
    }

    private void affectMap(Ballistica beam) {
        boolean noticed = false;
        for (int c : beam.subPath(0, beam.dist)) {
            if (!Dungeon.level.insideMap(c)) {
                continue;
            }
            for (int n : PathFinder.NEIGHBOURS9) {
                int cell = c + n;

                if (Dungeon.level.discoverable[cell])
                    Dungeon.level.mapped[cell] = true;

                int terr = Dungeon.level.map[cell];
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover(cell);

                    GameScene.discoverTile(cell, terr);
                    ScrollOfMagicMapping.discover(cell);

                    noticed = true;
                }
            }

            CellEmitter.center(c).burst(RainbowParticle.BURST, Random.IntRange(1, 2));
        }
        if (noticed)
            Sample.INSTANCE.play(Assets.Sounds.SECRET);

        GameScene.updateFog();
    }
}

