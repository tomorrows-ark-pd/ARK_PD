package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

// Guaranteed chasm on the MagicGolem floor: a central pit ringed by a 2-tile walkway so every door stays connected
public class GolemPitRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(9, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(9, super.minHeight());
    }

    @Override
    public float[] sizeCatProbs() {
        return new float[]{2, 1, 0};
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

        Painter.fill( level, this, 3, Terrain.CHASM );
    }
}
