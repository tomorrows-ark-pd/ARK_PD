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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Fate;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldedLeaderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldedSprite;
import com.watabou.utils.Random;

public class Slime extends Mob {
	
	{
		spriteClass = ShieldedSprite.class;
		
		HP = HT = 22;
		defenseSkill = 5;
		
		EXP = 4;
		maxLvl = 9;

		loot = Generator.Category.SKL_T1;
		lootChance = 0.03f;

		immunities.add(Silence.class);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 6 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public void damage(int dmg, Object src) {
		if (dmg >= 5){
			//takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
			dmg = 4 + (int)(Math.sqrt(8*(dmg - 4) + 1) - 1)/2;
		}
		super.damage(dmg, src);
	}

	@Override
	public void rollToDropLoot() {
		//each drop makes future drops 1/3 as likely
		// so loot chance looks like: 1/5, 1/15, 1/45, 1/135, etc.
		lootChance *= Math.pow(1/3f, Dungeon.LimitedDrops.SLIME_WEP.count);
		super.rollToDropLoot();
	}
	
	@Override
	protected Item createLoot() {
		Dungeon.LimitedDrops.SLIME_WEP.count++;
		MeleeWeapon w = (MeleeWeapon) Generator.random(Generator.Category.WEP_T2);
		w.level(0);
		return w;
	}
}
