package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Texas_shopkeeperSprite;

public class MiniShopkeeper extends Shopkeeper {
    {
        spriteClass = Texas_shopkeeperSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {

        if (Dungeon.level.heroFOV[pos]){
            Notes.add(Notes.Landmark.MINI_SHOP);
            Bestiary.setSeen(getClass());
        }

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    public void flee() {
        destroy();

        Notes.remove(Notes.Landmark.MINI_SHOP);

        sprite.killAndErase();
        CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
    }
}
