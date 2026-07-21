package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NPC_ShuSprite extends MobSprite {

    public NPC_ShuSprite() {
        super();

        texture( Assets.Sprites.NPC_SHU );

        TextureFilm frames = new TextureFilm( texture, 42, 38 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0,1);

        run = new Animation( 10, true );
        run.frames( frames, 0 );

        attack = new Animation( 15, false );
        attack.frames( frames, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }
}