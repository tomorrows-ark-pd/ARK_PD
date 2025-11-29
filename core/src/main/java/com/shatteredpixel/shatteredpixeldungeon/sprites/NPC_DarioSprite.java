/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NPC_DarioSprite extends MobSprite {

    public NPC_DarioSprite() {
        super();

        texture( Assets.Sprites.NPC_DARIO );

        TextureFilm frames = new TextureFilm( texture, 38, 34 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5 );

        run = new Animation( 12, true );
        run.frames( frames, 0 );

        attack = new Animation( 15, false );
        attack.frames( frames, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 24, 24, 24, 24, 24);

        play( idle );
    }
}
