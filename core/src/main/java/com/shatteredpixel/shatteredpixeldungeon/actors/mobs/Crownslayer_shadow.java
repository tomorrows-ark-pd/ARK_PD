package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrownslayerSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

// 이후 크붕이 스프라이트는 검은색 처리되도록(타락과 같이) 처리할 것.
public class Crownslayer_shadow extends Mob {
    private int blinkCooldown = 0;
    {
        spriteClass = CrownslayerSprite.class;

        EXP = 0;
        maxLvl = 40;

        state = WANDERING;

        properties.add(Property.BOSS);
        properties.add(Property.INFECTED);
        immunities.add(Silence.class);
        immunities.add(AllyBuff.class);
    }

    public Crownslayer_shadow() {
        super();

        HP = HT = 5 + Dungeon.depth * 5;
        defenseSkill = 1 + Dungeon.depth;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1 + Dungeon.depth / 2, 5 + Dungeon.depth ); }

    @Override
    public int attackSkill( Char target ) {
        return 12 + Dungeon.depth;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1 + Dungeon.depth / 2);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (blinkCooldown <= 0) {
            int i;
            do {
                i = Random.Int(Dungeon.level.length());
            } while (Dungeon.level.heroFOV[i]
                    || Dungeon.level.solid[i]
                    || Actor.findChar(i) != null
                    || PathFinder.getStep(i, Dungeon.level.exit(), Dungeon.level.passable) == -1);
            CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 10);
            ScrollOfTeleportation.appear(this, i);

            damage *= 1.5f;
            Buff.affect(enemy, Blindness.class, 4f);
            blinkCooldown = 120;
        }

        return super.attackProc(enemy, damage);
    }

    @Override
    protected boolean getCloser( int target ) {
            blinkCooldown--;
            return super.getCloser( target );
    }


    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.drop( new Gold(100 + Dungeon.depth * 50), pos ).sprite.drop( pos );
    }

    private static final String BLINK = "blinkcooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BLINK, blinkCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        blinkCooldown = bundle.getInt(BLINK);
    }
}
