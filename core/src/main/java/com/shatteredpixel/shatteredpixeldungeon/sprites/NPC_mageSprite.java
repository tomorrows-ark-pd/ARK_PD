package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NPC_mageSprite extends MobSprite {

    public NPC_mageSprite() {
        super();

        texture( Assets.Sprites.NPC_MAGE );

        TextureFilm frames = new TextureFilm( texture, 40, 46 );

        idle = new Animation( 20, true );
        idle.frames( frames, 0);

        run = new Animation( 10, true );
        run.frames( frames, 0 );

        attack = new Animation( 15, false );
        attack.frames( frames, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }
}