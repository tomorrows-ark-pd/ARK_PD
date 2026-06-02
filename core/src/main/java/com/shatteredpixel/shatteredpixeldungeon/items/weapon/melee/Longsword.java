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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Longsword extends MeleeWeapon {

	public static final String AC_ZAP = "ZAP";
	{
		image = ItemSpriteSheet.LONGSWORD;
		hitSound = Assets.Sounds.HIT_SWORD;
		hitSoundPitch = 1f;

		tier = 4;

		usesTargeting = true;
		defaultAction = AC_ZAP;
	}

	protected int collisionProperties = Ballistica.MAGIC_BOLT;

	private int arts = 2;
	private int artscap = 4;

	@Override
	public int max(int lvl) {
		return  4*(tier) + 2 +   //18 + 5
				lvl*(tier+1);
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		SPCharge(1);
		return super.proc(attacker, defender, damage);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_ZAP);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_ZAP) && arts > 0 && isEquipped(hero)) {
			if (this.cursed != true) {
				cursedKnown = true;
				GameScene.selectCell(zapper);
			}
			else {
				Buff.affect(Dungeon.hero, Silence.class, 45f);
				cursedKnown = true;
				arts -= 1;
			}
		}
	}

	public String statsInfo() {
		return Messages.get(this, "stats_desc");
	}

	public void SPCharge(float n) {
		if (Random.Int(11) < 2) {
			arts += (int)n;
			if (artscap < arts) arts = artscap;
			updateQuickslot();
		}
	}

	@Override
	public String status() {
		return arts + "/" + artscap;
	}

	private static final String CHARGE = "arts";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHARGE, arts);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (artscap > 0) arts = Math.min(artscap, bundle.getInt(CHARGE));
		else arts = bundle.getInt(CHARGE);
	}

	protected static CellSelector.Listener zapper = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target != null) {

				final Longsword ss;
				if (curItem instanceof Longsword) {
					ss = (Longsword) Longsword.curItem;

					Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
					int cell = shot.collisionPos;

					if (target == curUser.pos || cell == curUser.pos) {
						GLog.i(Messages.get(Longsword.class, "self_target"));
						return;
					}

					curUser.sprite.zap(cell);

					//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
					if (Actor.findChar(target) != null)
						QuickSlotButton.target(Actor.findChar(target));
					else
						QuickSlotButton.target(Actor.findChar(cell));

					if (ss.tryToZap(curUser, target)) {
						curUser.busy();
						ss.fx(shot, new Callback() {
							public void call() {
								ss.onZap(shot);
							}
						});
					}

				}
			}
		}

		@Override
		public String prompt() {
			return Messages.get(Longsword.class, "prompt");
		}
	};

	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.MAGIC_MISSILE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}
	public boolean tryToZap(Hero owner, int target) {

		if (owner.buff(MagicImmune.class) != null) {
			GLog.w(Messages.get(this, "no_magic"));
			return false;
		}

		if (arts >= 1) {
			return true;
		} else {
			GLog.w(Messages.get(this, "fizzles"));
			return false;
		}
	}


	protected void onZap( Ballistica bolt ) {
		Char ch = Actor.findChar( bolt.collisionPos );
		if (ch != null) {
			float dmg = 4f + (buffedLvl() * 1.5f) - Random.NormalFloat(0, ch.HP / 5);
			dmg = Math.max(dmg, 3f); // 최소 침묵 3턴
			Buff.affect(ch, Silence.class, dmg);
			Sample.INSTANCE.play( Assets.Sounds.LIGHTNING, 1, Random.Float(0.87f, 1.15f) );

			ch.sprite.burst(0xFFFFFFFF, buffedLvl() / 2 + 2);

		} else {
			Dungeon.level.pressCell(bolt.collisionPos);
		}

		arts -=1;
		updateQuickslot();

		Invisibility.dispel();

		if (arts <= 0) curUser.spendAndNext(1f);
		else curUser.spendAndNext(0f);
	}
}
