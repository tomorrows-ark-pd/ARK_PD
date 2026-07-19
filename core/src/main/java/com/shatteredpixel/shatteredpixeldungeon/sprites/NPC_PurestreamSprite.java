package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NPC_PurestreamSprite extends MobSprite {

    public NPC_PurestreamSprite() {
        super();

        texture( Assets.Sprites.NPC_PURESTREAM );

        TextureFilm frames = new TextureFilm( texture, 36, 36 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 4, true );
        run.frames( frames, 0, 1);

        attack = new Animation( 4, false );
        attack.frames( frames, 0, 1 );

        die = new Animation( 4, false );
        die.frames( frames, 0, 1 );

        play( idle );
    }
}
