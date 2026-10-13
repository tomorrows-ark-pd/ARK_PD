package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dummy;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Bonk;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Enfild2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MidnightSword;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WildMark extends Buff implements ActionIndicator.Action {
    {
        type = buffType.NEUTRAL;
    }

    private int charge = 0;
    private float pcharge = 0;
    private float fcharge = 0;
    private final static int chargeTurn = 100;
    private final static int maxcharge = 5;

    @Override
    public int icon() {
        return BuffIndicator.HEX;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", charge, Math.floor(pcharge));
    }

    public void Charged(float time) {
        if (charge >= maxcharge) {
            ActionIndicator.setAction(this);
            return;
        }

        fcharge += time;
        while (fcharge >= 0.75f) {
            pcharge += 1;
            fcharge = Math.max(0, fcharge-0.75f);
        }

        if (pcharge >= chargeTurn) {
            charge++;
            pcharge -= 100;
        }

        if (charge > 0) ActionIndicator.setAction(this);
        else ActionIndicator.clearAction(this);
     }

     public void gainCharge() {
        charge++;
         ActionIndicator.setAction(this);
     }

    @Override
    public Image getIcon() {
        return new ItemSprite(ItemSpriteSheet.THROWING_KNIFE, null);
    }

    @Override
    public void doAction() {

        Hero hero = Dungeon.hero;
        if (hero == null) return;

        GameScene.selectCell(zapper);
    }


    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            WildMark ss = Dungeon.hero.buff(WildMark.class);
            if (target != null) {

                    Ballistica shot = new Ballistica(Dungeon.hero.pos, target, Ballistica.PROJECTILE);
                    int cell = shot.collisionPos;

                    if (target == Dungeon.hero.pos || cell == Dungeon.hero.pos) {
                        GLog.i(Messages.get(WildMark.class, "self_target"));
                        return;
                    }

                    Dungeon.hero.sprite.zap(cell);

                    //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                    if (Actor.findChar(target) != null)
                        QuickSlotButton.target(Actor.findChar(target));
                    else
                        QuickSlotButton.target(Actor.findChar(cell));

                    Sample.INSTANCE.play(Assets.Sounds.LIGHTNING, 1f, 1.72f);
                    Beam(cell);
                    ss.charge -= 1;
                    if (ss.charge < 1) ActionIndicator.clearAction(ss);

                Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                if (buff != null) buff.detach();
                buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (buff != null) buff.detach();

                if (Dungeon.hero.buff(Bonk.BonkBuff.class) != null) Buff.detach(Dungeon.hero, Bonk.BonkBuff.class);

                Invisibility.dispel();
                    Dungeon.hero.spendAndNext(1f);
            }
        }

        @Override
        public String prompt() {
            return Messages.get(WildMark.class, "prompt");
        }
    };

    public static void Beam(int targetcell) {
        Ballistica beam = new Ballistica(Dungeon.hero.pos, targetcell, Ballistica.WONT_STOP);
        int maxDistance = 5;
        boolean terrainAffected = false;

        ArrayList<Char> chars = new ArrayList<>();

        for (int c : beam.subPath(1, maxDistance)) {

            Char ch;
            if ((ch = Actor.findChar( c )) != null) {
                chars.add( ch );
            }

            CellEmitter.get( c ).burst( Speck.factory( Speck.WOOL ), 5 );
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        float damageRate = 0.6f;
        if (Dungeon.hero.hasTalent(Talent.SWORD_WIND_UPGRADE)) {
            damageRate += 0.03f + (Dungeon.hero.pointsInTalent(Talent.SWORD_WIND_UPGRADE) * 0.03f);
        }
        if (Dungeon.hero.belongings.weapon instanceof Enfild2) damageRate /= 2;

        int damage = Dungeon.hero.damageRoll();
        damage *= damageRate;
        for (Char ch : chars) {
            ch.damage(damage, Dungeon.hero );
            ch.sprite.flash();

            if (Dungeon.hero.hasTalent(Talent.WIND_SCAR)) {
                Ballistica trajectory = new Ballistica(Dungeon.hero.pos, ch.pos, Ballistica.STOP_TARGET);
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                WandOfBlastWave.throwChar(ch, trajectory, Random.IntRange(Dungeon.hero.pointsInTalent(Talent.WIND_SCAR) - 1, 2)); // 넉백 효과
            }

            if (Dungeon.hero.hasTalent(Talent.OPPORTUNIST)) {
                if (Random.Int(3) < Dungeon.hero.pointsInTalent(Talent.OPPORTUNIST)) {
                    Buff.affect(ch, Blindness.class, 2f);
                }
            }
        }
    }

    private static final String CHARGE = "Energy";
    private static final String PCHARGE = "pcharge";
    private static final String FCHARGE = "fcharge";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
        bundle.put(PCHARGE, pcharge);
        bundle.put(FCHARGE, fcharge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        charge = bundle.getInt(CHARGE);
        if (charge > 0) ActionIndicator.setAction(this);

        pcharge = bundle.getFloat(PCHARGE);
        fcharge = bundle.getFloat(FCHARGE);
    }
}
