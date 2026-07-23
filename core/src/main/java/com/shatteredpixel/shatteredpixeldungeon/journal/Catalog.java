/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.LensBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatCutlet;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Sandvich;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalArmord;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Enfild;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Enfild2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Firmament;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gamzashield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.GoldDogSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gluttony;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ImageoverForm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ChenSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KollamSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MinosFury;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Niansword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PatriotSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RhodesSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SakuraSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SanktaBet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CatGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfAbsinthe;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfAngelina;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfBreeze;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfCorrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfGreyy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfLeaf;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfLena;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfMayer;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfMudrock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfPodenco;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfPurgatory;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfShining;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfSkyfire;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfSnowsant;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfSussurro;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfSuzuran;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfTime;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfVigna;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfWeedy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wrench;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookCamouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookChainHook;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookCrimsonCutter;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookExecutionMode;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookFate;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookFierceGlare;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookFoodPrep;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookHikari;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookHotBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookLive;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookPhantomMirror;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookPowerfulStrike;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookShinkageryu;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookSpreadSpores;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookTacticalChanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookThoughts;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookWhispers;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookWolfSpirit;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Bookpanorama;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookBenasProtracto;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookChargingPS;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookCoverSmoke;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookDawn;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookDeepHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookDreamland;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookEmergencyDefibrillator;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookFlashShield;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookGenesis;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookJackinthebox;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookLandingStrike;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookMentalBurst;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookNervous;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookNeverBackDown;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookPredators;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookReflow;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookRockfailHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookSpikes;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookWolfPack;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Bookancientkin;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookEveryone;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookNigetRaid;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookSBurst;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookShadowAssault;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookSharpness;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookSoaringFeather;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookSun;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookTerminationT;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookTrueSilverSlash;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.BookYourWish;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

//For items, but includes a few item-like effects, such as enchantments
public enum Catalog {

	//EQUIPMENT
	MELEE_WEAPONS,
	ARMOR,
	ENCHANTMENTS,
	GLYPHS,
	THROWN_WEAPONS,
	WANDS,
	RINGS,
	ARTIFACTS,
	MISC_EQUIPMENT,
	SKILLBOOK,

	//CONSUMABLES
	POTIONS,
	SEEDS,
	SCROLLS,
	STONES,
	FOOD,
	EXOTIC_POTIONS,
	EXOTIC_SCROLLS,
	BOMBS,
	TIPPED_DARTS,
	BREWS_ELIXIRS,
	SPELLS,
	MISC_CONSUMABLES;

	//tracks whether an item has been collected while identified
	private final LinkedHashMap<Class<?>, Boolean> seen = new LinkedHashMap<>();
	//tracks upgrades spent for equipment, uses for consumables
	private final LinkedHashMap<Class<?>, Integer> useCount = new LinkedHashMap<>();

	public Collection<Class<?>> items(){
		return seen.keySet();
	}

	//should only be used when initializing
	private void addItems( Class<?>... items){
		for (Class<?> item : items){
			if (item == null) continue;
			seen.put(item, false);
			useCount.put(item, 0);
		}
	}

	public String title(){
		return Messages.get(this, name() + ".title");
	}

	public int totalItems(){
		return seen.size();
	}

	public int totalSeen(){
		int seenTotal = 0;
		for (boolean itemSeen : seen.values()){
			if (itemSeen) seenTotal++;
		}
		return seenTotal;
	}

	public boolean allSeen(){
		for (boolean itemSeen : seen.values()){
			if (!itemSeen) return false;
		}
		return true;
	}

	static {

		MELEE_WEAPONS.addItems(Generator.Category.WEP_T1.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T2.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T3.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T4.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T5.classes);
		//mod-only weapons not in tier rotations
		MELEE_WEAPONS.addItems(
				Enfild.class, SakuraSword.class,
				RhodesSword.class, Firmament.class, Gamzashield.class, Enfild2.class,
				GoldDogSword.class, Gluttony.class, SanktaBet.class, Niansword.class,
				PatriotSpear.class, CatGun.class, MinosFury.class, ImageoverForm.class,
				KollamSword.class, ChenSword.class
		);

		ARMOR.addItems(Generator.Category.ARMOR.classes);

		ENCHANTMENTS.addItems(Weapon.Enchantment.common);
		ENCHANTMENTS.addItems(Weapon.Enchantment.uncommon);
		ENCHANTMENTS.addItems(Weapon.Enchantment.rare);
		ENCHANTMENTS.addItems(Weapon.Enchantment.curses);

		GLYPHS.addItems(Armor.Glyph.common);
		GLYPHS.addItems(Armor.Glyph.uncommon);
		GLYPHS.addItems(Armor.Glyph.rare);
		GLYPHS.addItems(Armor.Glyph.curses);

		THROWN_WEAPONS.addItems(Generator.Category.MIS_T1.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T2.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T3.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T4.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T5.classes);

		WANDS.addItems(Generator.Category.WAND.classes);
		//mod-only operator staffs
		WANDS.addItems(
				StaffOfAbsinthe.class, StaffOfGreyy.class, StaffOfVigna.class,
				StaffOfSkyfire.class, StaffOfBreeze.class, StaffOfWeedy.class,
				StaffOfMudrock.class, StaffOfLeaf.class, StaffOfShining.class,
				StaffOfMayer.class, StaffOfAngelina.class, StaffOfCorrupting.class,
				StaffOfLena.class, StaffOfSnowsant.class, StaffOfSussurro.class,
				StaffOfPodenco.class, StaffOfTime.class, StaffOfSuzuran.class,
				StaffOfPurgatory.class, Wrench.class
		);

		RINGS.addItems(Generator.Category.RING.classes);

		ARTIFACTS.addItems(Generator.Category.ARTIFACT.classes);

		MISC_EQUIPMENT.addItems(BrokenSeal.class, SpiritBow.class, VelvetPouch.class,
				PotionBandolier.class, ScrollHolder.class, MagicalHolster.class,
				FoodBag.class, Amulet.class);

		SKILLBOOK.addItems(Generator.Category.SKL_T1.classes);
		SKILLBOOK.addItems(Generator.Category.SKL_T2.classes);
		SKILLBOOK.addItems(Generator.Category.SKL_T3.classes);
		//mod skill books not in Generator (legacy/special)
		SKILLBOOK.addItems(
				BookPowerfulStrike.class, BookTacticalChanting.class, BookExecutionMode.class,
				BookThoughts.class, BookHikari.class, BookWhispers.class,
				BookWolfPack.class, BookMentalBurst.class, BookReflow.class,
				BookNervous.class, BookDawn.class, BookEmergencyDefibrillator.class,
				BookGenesis.class,
				BookSBurst.class, BookShadowAssault.class, BookNigetRaid.class,
				BookSoaringFeather.class, BookYourWish.class, BookSun.class
		);

		POTIONS.addItems(Generator.Category.POTION.classes);

		SCROLLS.addItems(Generator.Category.SCROLL.classes);

		SEEDS.addItems(Generator.Category.SEED.classes);

		STONES.addItems(Generator.Category.STONE.classes);

		FOOD.addItems(Food.class, Pasty.class, MysteryMeat.class, ChargrilledMeat.class,
				StewedMeat.class, FrozenCarpaccio.class, SmallRation.class, Berry.class,
				Blandfruit.class, MeatPie.class, MeatCutlet.class, Sandvich.class);

		EXOTIC_POTIONS.addItems(Generator.Category.EXOTIC_POTION.classes);

		EXOTIC_SCROLLS.addItems(Generator.Category.EXOTIC_SCROLL.classes);

		BOMBS.addItems(Bomb.class, FrostBomb.class, Firebomb.class, RegrowthBomb.class,
				WoollyBomb.class, Noisemaker.class, HolyBomb.class, ArcaneBomb.class,
				ShrapnelBomb.class, LensBomb.class, ShockBomb.class, Flashbang.class);

		TIPPED_DARTS.addItems(TippedDart.types.values().toArray(new Class[0]));

		BREWS_ELIXIRS.addItems(Generator.Category.ELIXIR_BREW.classes);

		SPELLS.addItems(WildEnergy.class, PhaseShift.class, Alchemize.class,
				CurseInfusion.class, MagicalInfusion.class, Recycle.class,
				ReclaimTrap.class, BeaconOfReturning.class, FeatherFall.class,
				MagicalArmord.class);

		MISC_CONSUMABLES.addItems(Gold.class, Dewdrop.class,
				IronKey.class, GoldenKey.class, CrystalKey.class, SkeletonKey.class,
				Stylus.class, Torch.class, Honeypot.class, Ankh.class);

	}

	//old badges for pre-2.5
	public static LinkedHashMap<Catalog, Badges.Badge> catalogBadges = new LinkedHashMap<>();
	static {
		catalogBadges.put(MELEE_WEAPONS, Badges.Badge.ALL_WEAPONS_IDENTIFIED);
		catalogBadges.put(SKILLBOOK, Badges.Badge.ALL_SKILLBOOK_IDENTIFIED);
		catalogBadges.put(WANDS, Badges.Badge.ALL_WANDS_IDENTIFIED);
		catalogBadges.put(RINGS, Badges.Badge.ALL_RINGS_IDENTIFIED);
		catalogBadges.put(ARTIFACTS, Badges.Badge.ALL_ARTIFACTS_IDENTIFIED);
		catalogBadges.put(POTIONS, Badges.Badge.ALL_POTIONS_IDENTIFIED);
		catalogBadges.put(SCROLLS, Badges.Badge.ALL_SCROLLS_IDENTIFIED);
	}

	public static ArrayList<Catalog> equipmentCatalogs = new ArrayList<>();
	static {
		equipmentCatalogs.add(MELEE_WEAPONS);
		equipmentCatalogs.add(ARMOR);
		equipmentCatalogs.add(ENCHANTMENTS);
		equipmentCatalogs.add(GLYPHS);
		equipmentCatalogs.add(THROWN_WEAPONS);
		equipmentCatalogs.add(WANDS);
		equipmentCatalogs.add(RINGS);
		equipmentCatalogs.add(ARTIFACTS);
		equipmentCatalogs.add(MISC_EQUIPMENT);
		equipmentCatalogs.add(SKILLBOOK);
	}

	public static ArrayList<Catalog> consumableCatalogs = new ArrayList<>();
	static {
		consumableCatalogs.add(POTIONS);
		consumableCatalogs.add(SCROLLS);
		consumableCatalogs.add(SEEDS);
		consumableCatalogs.add(STONES);
		consumableCatalogs.add(FOOD);
		consumableCatalogs.add(EXOTIC_POTIONS);
		consumableCatalogs.add(EXOTIC_SCROLLS);
		consumableCatalogs.add(BOMBS);
		consumableCatalogs.add(TIPPED_DARTS);
		consumableCatalogs.add(BREWS_ELIXIRS);
		consumableCatalogs.add(SPELLS);
		consumableCatalogs.add(MISC_CONSUMABLES);
	}

	public static boolean isSeen(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(cls)) {
				return cat.seen.get(cls);
			}
		}
		return false;
	}

	public static void setSeen(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(cls) && !cat.seen.get(cls)) {
				cat.seen.put(cls, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateItemsIdentified();
	}

	public static int useCount(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.useCount.containsKey(cls)) {
				return cat.useCount.get(cls);
			}
		}
		return 0;
	}

	public static void countUse(Class<?> cls){
		countUses(cls, 1);
	}

	public static void countUses(Class<?> cls, int uses){
		//uses in vault tester / deep branches don't count
		if (Dungeon.depth > 15 && Dungeon.branch > 0){
			return;
		}
		for (Catalog cat : values()) {
			if (cat.useCount.containsKey(cls) && cat.useCount.get(cls) != Integer.MAX_VALUE) {
				cat.useCount.put(cls, cat.useCount.get(cls) + uses);
				if (cat.useCount.get(cls) < -1_000_000_000){ //catch overflow
					cat.useCount.put(cls, Integer.MAX_VALUE);
				}
				Journal.saveNeeded = true;
			}
		}
	}

	private static final String CATALOG_CLASSES = "catalog_classes";
	private static final String CATALOG_SEEN    = "catalog_seen";
	private static final String CATALOG_USES    = "catalog_uses";

	public static void store( Bundle bundle ){

		ArrayList<Class<?>> classes = new ArrayList<>();
		ArrayList<Boolean> seen = new ArrayList<>();
		ArrayList<Integer> uses = new ArrayList<>();

		for (Catalog cat : values()) {
			for (Class<?> item : cat.items()) {
				if (cat.seen.get(item) || cat.useCount.get(item) > 0){
					classes.add(item);
					seen.add(cat.seen.get(item));
					uses.add(cat.useCount.get(item));
				}
			}
		}

		Class<?>[] storeCls = new Class[classes.size()];
		boolean[] storeSeen = new boolean[seen.size()];
		int[] storeUses = new int[uses.size()];

		for (int i = 0; i < storeCls.length; i++){
			storeCls[i] = classes.get(i);
			storeSeen[i] = seen.get(i);
			storeUses[i] = uses.get(i);
		}

		bundle.put( CATALOG_CLASSES, storeCls );
		bundle.put( CATALOG_SEEN, storeSeen );
		bundle.put( CATALOG_USES, storeUses );

	}

	//pre-v2.5 (mod's old format)
	private static final String CATALOG_ITEMS = "catalog_items";
	//pre-0.8.2 (string-array format)
	private static final String CATALOG_LEGACY_ITEMS = "catalogs";

	public static void restore( Bundle bundle ){

		//old logic for pre-2.5 catalog-specific badges
		Badges.loadGlobal();

		//if ALL_ITEMS_IDENTIFIED was unlocked, every item in every category is seen
		if (Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)){
			for (Catalog cat : values()){
				for (Class<?> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
		} else {
			for (Catalog cat : values()){
				if (Badges.isUnlocked(catalogBadges.get(cat))){
					for (Class<?> item : cat.items()){
						cat.seen.put(item, true);
					}
				}
			}
		}

		//legacy CATALOG_ITEMS migration (mod's old format)
		if (bundle.contains(CATALOG_ITEMS)) {
			Journal.saveNeeded = true; //rewrite to new format
			Class<?>[] legacy = bundle.getClassArray(CATALOG_ITEMS);
			if (legacy != null) {
				for (Class<?> cls : legacy) {
					if (cls == null) continue;
					for (Catalog cat : values()) {
						if (cat.seen.containsKey(cls)) {
							cat.seen.put(cls, true);
						}
					}
				}
			}
		}

		//pre-0.8.2 string-array format — match by simple class name
		if (bundle.contains(CATALOG_LEGACY_ITEMS)) {
			Journal.saveNeeded = true; //rewrite to new format
			String[] legacyNames = bundle.getStringArray(CATALOG_LEGACY_ITEMS);
			if (legacyNames != null) {
				List<String> seenNames = Arrays.asList(legacyNames);
				for (Catalog cat : values()) {
					for (Class<?> item : cat.items()) {
						if (seenNames.contains(item.getSimpleName())) {
							cat.seen.put(item, true);
						}
					}
				}
			}
		}

		//new format
		if (bundle.contains(CATALOG_CLASSES)){
			Class<?>[] classes = bundle.getClassArray(CATALOG_CLASSES);
			boolean[] seen = bundle.contains(CATALOG_SEEN) ? bundle.getBooleanArray(CATALOG_SEEN) : null;
			int[] uses = bundle.contains(CATALOG_USES) ? bundle.getIntArray(CATALOG_USES) : null;

			if (classes == null || seen == null || uses == null
					|| classes.length != seen.length
					|| classes.length != uses.length) {
				Journal.saveNeeded = true;
			} else {
				for (int i = 0; i < classes.length; i++){
					if (classes[i] == null) continue;
					for (Catalog cat : values()) {
						if (cat.seen.containsKey(classes[i])) {
							cat.seen.put(classes[i], seen[i]);
							cat.useCount.put(classes[i], uses[i]);
						}
					}
				}
			}
		}

	}

}
