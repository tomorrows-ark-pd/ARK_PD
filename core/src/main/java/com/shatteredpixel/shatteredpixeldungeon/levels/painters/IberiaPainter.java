package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class IberiaPainter extends RegularPainter {
    @Override
    protected void decorate(Level level, ArrayList<Room> rooms) {

        int[] map = level.map;
        int w = level.width();
        int l = level.length();

        for (int i=0; i < w; i++) {
            if (map[i] == Terrain.WALL &&
                    map[i + w] == Terrain.WATER &&
                    Random.Int( 3 ) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i=w; i < l - w; i++) {
            if (map[i] == Terrain.WALL &&
                    map[i - w] == Terrain.WALL &&
                    map[i + w] == Terrain.WATER &&
                    Random.Int( 2 ) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i=w + 1; i < l - w - 1; i++) {
            if (map[i] == Terrain.EMPTY) {

                int count =
                        (map[i + 1] == Terrain.WALL ? 1 : 0) +
                                (map[i - 1] == Terrain.WALL ? 1 : 0) +
                                (map[i + w] == Terrain.WALL ? 1 : 0) +
                                (map[i - w] == Terrain.WALL ? 1 : 0);

                if (Random.Int( 16 ) < count * count) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }
    }
}
