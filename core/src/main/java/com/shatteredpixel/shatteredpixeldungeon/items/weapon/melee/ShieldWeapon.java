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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldWeaponTracker;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

//low base damage, high DR. mitigating hits builds a charge meter, and each point of charge boosts outgoing damage
public class ShieldWeapon extends MeleeWeapon {

	public static final float BONUS_PER_STACK = 0.02f;   //+2% damage per point of charge, at every level

	//DR at +0, and DR gained per upgrade
	protected float baseDefenseFactor;
	protected float defenseFactorPerLvl;

	//natural charge ceiling at +0, and ceiling gained per upgrade
	protected int capBase = 30;
	protected int capPerLvl = 7;

	//one charge per point of damage the shield absorbed
	protected float chargePerMitigation() {
		return 1.0f;
	}

	public int chargeCapAt(int lvl) {
		return Math.round((capBase + capPerLvl * lvl) * augmentCapFactor());
	}

	public int chargeCap() {
		return chargeCapAt(buffedLvl());
	}

	public int defenseFactorAt(int lvl) {
		return Math.round((baseDefenseFactor + defenseFactorPerLvl * lvl) * augmentDRFactor());
	}

	@Override
	public int defenseFactor(Char owner) {
		return defenseFactorAt(buffedLvl());
	}

	//credit is the mean of the discarded drRoll, clamped to what was actually thrown at us so weak mobs can't farm it
	public void onMitigated(Char owner, int incomingPreDR) {
		float mitigation = Math.min(incomingPreDR, defenseFactor(owner) / 2f);
		if (mitigation <= 0) return;
		Buff.affect(owner, ShieldWeaponTracker.class).gain(mitigation * chargePerMitigation(), chargeCap());
		updateQuickslot();
	}

	//natural charge is capped to this weapon's ceiling; external shielding stacks on top and goes past it
	public float effectiveCharge(Char owner) {
		if (owner == null) return 0f;
		ShieldWeaponTracker tracker = owner.buff(ShieldWeaponTracker.class);
		float natural = tracker == null ? 0f : Math.min(tracker.charge(), chargeCap());
		return natural + owner.shielding();
	}

	public float damageBonus(Char owner) {
		return effectiveCharge(owner) * BONUS_PER_STACK;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		//read the meter as it stands at swing start; dealing damage grants nothing
		damage = Math.round(damage * (1f + damageBonus(attacker)));
		updateQuickslot();

		return super.proc(attacker, defender, damage);
	}

	//shield weapons reinterpret the three shared augments rather than adding their own enum values,
	//so a transmute in either direction always lands on an augment the destination weapon understands
	public static final Augment DEFENSE = Augment.SPEED;
	public static final Augment ATTACK  = Augment.DAMAGE;
	public static final Augment MAGIC   = Augment.OVERLOAD;

	//the shared damage factors don't apply; these do
	@Override
	public int augmentDamageFactor(int dmg) {
		if (augment == DEFENSE) return Math.round(dmg * 0.80f);
		if (augment == ATTACK)  return Math.round(dmg * 1.45f);
		return dmg;
	}

	//none of the shield augments touch swing speed or accuracy
	@Override
	public float augmentDelayFactor(float dly) {
		return dly;
	}

	@Override
	public float augmentAccFactor(float acc) {
		return acc;
	}

	protected float augmentDRFactor() {
		if (augment == DEFENSE) return 1.30f;
		if (augment == ATTACK)  return 0.70f;
		if (augment == MAGIC)   return 0.85f;
		return 1.00f;
	}

	protected float augmentCapFactor() {
		if (augment == DEFENSE) return 1.35f;
		if (augment == ATTACK)  return 0.80f;
		if (augment == MAGIC)   return 1.10f;
		return 1.00f;
	}

	@Override
	public String augmentKey(Augment aug) {
		if (aug == DEFENSE) return "defense";
		if (aug == ATTACK)  return "attack";
		if (aug == MAGIC)   return "magic";
		return aug.name();
	}

	@Override
	public String augmentDescKey() {
		if (augment == DEFENSE) return "aug_defense";
		if (augment == ATTACK)  return "aug_attack";
		if (augment == MAGIC)   return "aug_magic";
		return null;
	}

	@Override
	public String status() {
		if (Dungeon.hero == null || !isEquipped(Dungeon.hero)) return null;
		return Messages.format("+%d%%", (int) (damageBonus(Dungeon.hero) * 100));
	}

	@Override
	public String statsInfo() {
		int perStack = (int) (BONUS_PER_STACK * 100);
		if (isIdentified()) {
			return Messages.get(this, "stats_desc", defenseFactor(Dungeon.hero), perStack, chargeCap());
		} else {
			return Messages.get(this, "typical_stats_desc", defenseFactorAt(0), perStack, chargeCapAt(0));
		}
	}
}
