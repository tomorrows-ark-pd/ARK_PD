package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BelfrySprite;
import com.watabou.utils.PathFinder;

public class SeaObject extends NPC{
    {
        spriteClass = BelfrySprite.class;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.STATIC);
        immunities.add(Burning.class);

        state = PASSIVE;
    }

    public SeaObject() {
        super();
        HT=HP = 2000;
    }

    @Override
    public void beckon(int cell) {
        return;
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]){
            Bestiary.setSeen(getClass());
        }
        spend(TICK);
        return true;
    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

    @Override
    protected void spend(float time) {
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
            if (ch != null && ch.isAlive() && (ch.alignment == Alignment.ALLY)) {
                if (ch.buff(NervousImpairment.class) != null) ch.buff(NervousImpairment.class).sum(-15 * time);
            }
        }

        super.spend(time);
    }
}
