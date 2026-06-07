package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class Delivery_dronSprite extends MobSprite {
    public Delivery_dronSprite() {
        super();

        texture( Assets.Sprites.DELIVERY_DRON );

        TextureFilm frames = new TextureFilm( texture, 32, 28 );

        idle = new MovieClip.Animation( 8, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = new MovieClip.Animation( 15, false );
        run.frames( frames, 0 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 0 );

        zap = attack.clone();

        die = new MovieClip.Animation( 8, false );
        die.frames( frames, 0 );

        play( idle.clone() );
    }

    @Override
    public void idle() {
        isMoving = false;
        super.idle();
    }

}