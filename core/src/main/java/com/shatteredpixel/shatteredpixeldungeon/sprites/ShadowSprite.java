package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Shadow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class ShadowSprite extends MobSprite {

    private static final int FRAME_WIDTH	= 36;
    private static final int FRAME_HEIGHT	= 36;

    @Override
    public void link( Char ch ) {
        super.link( ch );
        updateArmor( 5 );

        add(State.HIKARI);
        add(State.DARKENED);
    }


    public void updateArmor( int tier ) {
        TextureFilm film = new TextureFilm( HeroSprite.tiers(), tier, FRAME_WIDTH, FRAME_HEIGHT );

        idle = new Animation( 1, true );
        idle.frames( film, 0, 0, 0 );

        run = new Animation( 20, true );
        run.frames( film, 1, 2, 3, 4, 5, 6, 7, 8  );

        die = new Animation( 10, false );
        die.frames( film, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36 );

        attack = new Animation( 18, false );
        attack.frames( film, 9, 10, 11, 12, 13, 14, 15, 16, 17 );

        idle();
    }

    public static class ShadowBlaze extends ShadowSprite {
        public ShadowBlaze() {
            super();
            texture(Assets.Sprites.BLAZE);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowAmiya extends ShadowSprite {
        public ShadowAmiya() {
            super();
            texture(Assets.Sprites.AMIYA);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowRed extends ShadowSprite {
        public ShadowRed() {
            super();
            texture(Assets.Sprites.RED);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowGrey extends ShadowSprite {
        public ShadowGrey() {
            super();
            texture(Assets.Sprites.GREY);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowRose extends ShadowSprite {
        public ShadowRose() {
            super();
            texture(Assets.Sprites.ROSEMARY);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowNearl extends ShadowSprite {
        public ShadowNearl() {
            super();
            texture(Assets.Sprites.NEARL);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowJessi extends ShadowSprite {
        public ShadowJessi() {
            super();
            texture(Assets.Sprites.JESSI);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowSkd extends ShadowSprite {
        public ShadowSkd() {
            super();
            texture(Assets.Sprites.SKD);

            updateArmor( 5 );
            idle();
        }
    }

    public static class ShadowSPT extends ShadowSprite {
        public ShadowSPT() {
            super();
            texture(Assets.Sprites.SPT);

            updateArmor( 4 );
            idle();
        }

        @Override
        public void link(Char ch) {
            super.link( ch );
            updateArmor( 4 );
        }
    }

    public static class ShadowGrani extends ShadowSprite {
        public ShadowGrani() {
            super();
            texture(Assets.Sprites.GRN);

            updateArmor( 5 );
            idle();
        }
    }
}
