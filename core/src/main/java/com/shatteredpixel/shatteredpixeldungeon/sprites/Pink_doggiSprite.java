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
import com.watabou.utils.Random;

public class Pink_doggiSprite extends MobSprite {

    public Pink_doggiSprite() {
        super();

        texture( Assets.Sprites.NPC_PINK );

        TextureFilm frames = new TextureFilm( texture, 36, 40 );

        idle = new Animation( 5, true );
        idle.frames( frames, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 );

        run = idle.clone();
        attack = idle.clone();

        die = new Animation( 20, false );
        die.frames( frames, 5 );

        play( idle );
        curFrame = Random.Int( curAnim.frames.length );
    }
}