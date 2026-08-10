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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ShieldWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

//offensive charge meter for ShieldWeapon: gained only from mitigated hits, grants no survivability of its own
public class ShieldWeaponTracker extends Buff {

	{
		type = buffType.POSITIVE;
	}

	private static final int   GRACE_TURNS = 3;
	private static final float DECAY_RATE  = 0.25f;
	private static final float DECAY_FLOOR = 0.06f;   //fraction of cap, kills the tail a pure percentage leaves

	private float charge;
	private int   turnsSinceGain;
	//cap of the weapon that last granted charge, kept so decay still has a floor after an unequip
	private int   cap;

	public void gain(float amt, int chargeCap) {
		if (amt <= 0) return;
		cap = chargeCap;
		charge = Math.min(charge + amt, chargeCap);
		turnsSinceGain = 0;
	}

	public float charge() {
		return charge;
	}

	@Override
	public boolean act() {
		turnsSinceGain++;

		if (turnsSinceGain >= GRACE_TURNS) {
			charge -= Math.max(charge * DECAY_RATE, cap * DECAY_FLOOR);
			if (charge <= 0) {
				charge = 0;
				detach();
			}
			Item.updateQuickslot();
		}

		spend(TICK);
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.6f, 0.25f);
	}

	//reads the equipped weapon rather than the stored cap, so a weapon swap shows the same numbers the quickslot does
	@Override
	public String desc() {
		int liveCap = cap;
		int bonus = 0;
		if (target instanceof Hero && ((Hero) target).belongings.weapon() instanceof ShieldWeapon) {
			ShieldWeapon w = (ShieldWeapon) ((Hero) target).belongings.weapon();
			liveCap = w.chargeCap();
			bonus = (int) (w.damageBonus(target) * 100);
		}
		return Messages.get(this, "desc", (int) Math.min(charge, liveCap), liveCap, bonus);
	}

	private static final String CHARGE = "charge";
	private static final String TURNS_SINCE_GAIN = "turns_since_gain";
	private static final String CAP = "cap";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHARGE, charge);
		bundle.put(TURNS_SINCE_GAIN, turnsSinceGain);
		bundle.put(CAP, cap);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		charge = bundle.getFloat(CHARGE);
		turnsSinceGain = bundle.getInt(TURNS_SINCE_GAIN);
		cap = bundle.getInt(CAP);
	}
}
