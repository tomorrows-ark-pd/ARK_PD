package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaBossLevel2;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.List;

public class SeaPlatform extends Platform {

    {
        image = 5;
        generatorClass = LittleHandy.class;
    }

    @Override
    public void activate(Char ch) {
        return;
    }

    public static class LittleHandy extends Platform.Generator {
        {
            image = ItemSpriteSheet.SAINT_HAND;

            platformClass = SeaPlatform.class;

            bones = false;
        }

        @Override
        protected void onThrow( int cell ) {
            // Only activate in boss level 2 in Iberia and if thrown tile is a Nethersea Brand
            if (Dungeon.level instanceof SeaBossLevel2 && Dungeon.level.seaTerrors.get(cell) != null) {
                Dungeon.level.createPlatform(this, cell);
            } else {
                super.onThrow(cell);
            }
        }

        @Override
        public List<Platform> generate(int pos, Level level ) {
            if (level != null && level.heroFOV != null && level.heroFOV[pos]) {
                Sample.INSTANCE.play(Assets.Sounds.GRASS);
            }

            List<Platform> platforms = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS9) {
                int c = pos + n;
                // Generate Platform in 3x3 if it is a Nethersea Brand tile
                if (c >= 0 && c < Dungeon.level.length() && Dungeon.level.seaTerrors.get(c) != null) {
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    }

                    Platform platform = Reflection.newInstance(platformClass);
                    platform.pos = c;
                    platforms.add(platform);
                }
            }
            return platforms;
        }
    }
}
