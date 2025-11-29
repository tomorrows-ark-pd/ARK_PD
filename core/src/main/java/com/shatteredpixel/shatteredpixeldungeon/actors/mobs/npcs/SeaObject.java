package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BelfrySprite;
import com.watabou.utils.PathFinder;

public class SeaObject extends NPC{
    {
        spriteClass = BelfrySprite.class;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
    }

    public SeaObject() {
        super();
        HT=HP = 2000;
    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

    @Override
    protected void spend(float time) {
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
            if (ch != null && ch.isAlive() && (ch instanceof Hero || ch instanceof DriedRose.GhostHero)) {
                if (ch.buff(NervousImpairment.class) != null) ch.buff(NervousImpairment.class).sum(-15 * time);
            }
        }

        super.spend(time);
    }
}
