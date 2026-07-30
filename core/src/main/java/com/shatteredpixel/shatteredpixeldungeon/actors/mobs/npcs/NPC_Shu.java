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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_ShuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_guardSprite;

public class NPC_Shu extends Mob {
    {
        spriteClass = NPC_ShuSprite.class;
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Property.NPC);
    }

    {
        HP = HT = 100;
        EXP = 0;

        alignment = Alignment.NEUTRAL;
        state = PASSIVE;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);
        sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "say"));

        return true;
    }

    public static void spawn(Level level, int ppos) {
        NPC_Shu npc = new NPC_Shu();
        do {
            npc.pos = ppos;
        } while (npc.pos == -1);
        level.mobs.add(npc);
    }
}