package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NPC_guardSprite extends MobSprite {

    public NPC_guardSprite() {
        super();

        texture( Assets.Sprites.NPC_GUARD );

        TextureFilm frames = new TextureFilm( texture, 34, 34 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0,1,2);

        run = new Animation( 10, true );
        run.frames( frames, 0 );

        attack = new Animation( 15, false );
        attack.frames( frames, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }
}