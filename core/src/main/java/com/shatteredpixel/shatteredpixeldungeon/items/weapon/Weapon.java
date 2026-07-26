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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Guardoper_ItermUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SeethingBurst;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Twilight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.IsekaiItem;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfDominate;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Annoying;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Displacing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.contamination;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Exhausting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Fragile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Friendly;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Polarized;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Wayward;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Dragonkill;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Overeating;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Penetrate;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ThermiteBlade;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

abstract public class Weapon extends KindOfWeapon {

	protected float ACC = 1f;	// Accuracy modifier
	protected float	DLY	= 1f;	// Speed modifier
	protected int	RCH = 1;    // Reach modifier (only applies to melee hits)

	public float getAcc() {
		return ACC;
	}

	public float getDelay() {
		return DLY;
	}

	public int getReach() {
		return RCH;
	}

	public enum Augment {
		SPEED   (0.7f, 0.6667f, 1f),
		DAMAGE  (1.5f, 1.6667f, 1f),
		OVERLOAD (1.25f, 0.9f, 0.65f),
		NONE	(1.0f, 1.0000f, 1f);

		private float damageFactor;
		private float delayFactor;
		private float accFactor;


		Augment(float dmg, float dly, float acc){
			damageFactor = dmg;
			delayFactor = dly;
			accFactor = acc;
		}

		public int damageFactor(int dmg){
			return Math.round(dmg * damageFactor);
		}

		public float delayFactor(float dly){
			return dly * delayFactor;
		}

		public float accFactor(float acc){
			return acc * accFactor;
		}
	}

	public Augment augment = Augment.NONE;

	private static final int USES_TO_ID = 20;
	private float usesLeftToID = USES_TO_ID;
	private float availableUsesToID = USES_TO_ID/2f;

	public Enchantment enchantment;
	public boolean curseInfusionBonus = false;

	@Override
	public int proc( Char attacker, Char defender, int damage ) {

		if (attacker instanceof Hero) {
            curUser = Dungeon.hero;
            if (Dungeon.hero.belongings.ring instanceof RingOfDominate) {
                float enemyResist = 1;
                enemyResist *= 1 + 2*Math.pow(defender.HP/(float)defender.HT, 2);

                float Resists = 100 * enemyResist;

                if (Random.Int((int)Resists) < RingOfDominate.Dominate(Dungeon.hero)) {
                    if (defender instanceof Mob && defender.isAlive() && !defender.isImmune(Corruption.class)
                            && defender.buff(Corruption.class) == null && defender.alignment != Char.Alignment.ALLY) {
                        defender.HP = defender.HT;
                        damage = 0;
                        AllyBuff.affectAndLoot((Mob) defender, curUser, Corruption.class);
                    }
                }
			}
		}


		int conservedDamage = 0;
		if (attacker.buff(Kinetic.ConservedDamage.class) != null) {
			conservedDamage = attacker.buff(Kinetic.ConservedDamage.class).damageBonus();
			attacker.buff(Kinetic.ConservedDamage.class).detach();

			damage += conservedDamage;
		}
		if (enchantment != null && attacker.buff(MagicImmune.class) == null) {
			damage = enchantment.proc( this, attacker, defender, damage );
			}

		if (!levelKnown && attacker == Dungeon.hero) {
			float uses = Math.min( availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this) );
			availableUsesToID -= uses;
			usesLeftToID -= uses;
			if (usesLeftToID <= 0) {
				identify();
				GLog.p( Messages.get(Weapon.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}

		if (Dungeon.hero.hasTalent(Talent.ENRAGED_CATALYST) && attacker instanceof Hero) {
			Talent.BlazeBurstBuff counter = Buff.affect(Dungeon.hero, Talent.BlazeBurstBuff.class);
			if (counter.count() >= 10){
				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
						int dmg = damageRoll(curUser);
						dmg = dmg - mob.drRoll();
						dmg /= (4-Dungeon.hero.pointsInTalent(Talent.ENRAGED_CATALYST));
						mob.damage(dmg, this);
						mob.sprite.burst(0xFFFFFFFF, 15);

						if (mob.isAlive() == true) Buff.affect(mob, Paralysis.class, 1f);
					}
				}
				Sample.INSTANCE.play( Assets.Sounds.HIT_CHAINSAW2, 1.33f, 1.65f );
				Camera.main.shake(2, 1.5f);
				counter.countDown(10);
			}
		}

		return damage;
	}

	public void onHeroGainExp( float levelPercent, Hero hero ){
		levelPercent *= Talent.itemIDSpeedFactor(hero, this);
		if (!levelKnown && isEquipped(hero) && availableUsesToID <= USES_TO_ID/2f) {
			//gains enough uses to ID over 0.5 levels
			availableUsesToID = Math.min(USES_TO_ID/2f, availableUsesToID + levelPercent * USES_TO_ID);
		}
	}

	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String ENCHANTMENT	    = "enchantment";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	private static final String AUGMENT	        = "augment";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( ENCHANTMENT, enchantment );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
		bundle.put( AUGMENT, augment );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		usesLeftToID = bundle.getFloat( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getFloat( AVAILABLE_USES );
		enchantment = (Enchantment)bundle.get( ENCHANTMENT );
		curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );

		augment = bundle.getEnum(AUGMENT, Augment.class);
	}

	@Override
	public void reset() {
		super.reset();
		usesLeftToID = USES_TO_ID;
		availableUsesToID = USES_TO_ID/2f;
	}

	@Override
	public float accuracyFactor( Char owner ) {

		int encumbrance = 0;

		if( owner instanceof Hero ){
			encumbrance = STRReq() - ((Hero)owner).STR();
		}

		if (hasEnchant(Wayward.class, owner))
			encumbrance = Math.max(2, encumbrance+2);

		float ACC = augment.accFactor(this.ACC);
		return encumbrance > 0 ? (float)(ACC / Math.pow( 1.5, encumbrance )) : ACC;
	}

	@Override
	public float speedFactor( Char owner ) {

		int encumbrance = 0;
		if (owner instanceof Hero) {
			encumbrance = STRReq() - ((Hero)owner).STR();
		}

		float DLY = augment.delayFactor(this.DLY);

		DLY *= RingOfFuror.attackDelayMultiplier(owner);
		return (encumbrance > 0 ? (float)(DLY * Math.pow( 1.2, encumbrance )) : DLY);
	}

	@Override
	public int reachFactor(Char owner) {
		int RCHmath = getReach();
		if (Dungeon.hero.hasTalent(Talent.CHAINSAW_EXTEND) && owner instanceof Hero) {
			RCHmath +=1;
		}
		if (Dungeon.hero.buff(Twilight.class) != null) RCHmath +=1;
		if (owner.buff(SeethingBurst.class) != null) {
			return hasEnchant(Projecting.class, owner) ? RCHmath + 2 : RCHmath + 1;
		}
		else {
			return hasEnchant(Projecting.class, owner) ? RCHmath + 1 : RCHmath;
		}
	}

	public int STRReq(){
		return STRReq(level());
	}

	public abstract int STRReq(int lvl);

	protected static int STRReq(int tier, int lvl){
		lvl = Math.max(0, lvl);

		//strength req decreases at +1,+3,+6,+10,etc.
		int req = (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;

		if (Dungeon.hero.pointsInTalent(Talent.STRONGMAN) + 1 >= 2) req--;
		if (Dungeon.hero.pointsInTalent(Talent.STRONGMAN) + 1 >= 4) req--;

		return req;
	}

	@Override
	public int level() {
		return super.level() + (curseInfusionBonus ? 1 : 0);
	}
	
	//overrides as other things can equip these
	@Override
	public int buffedLvl() {
		DriedRose rose = Dungeon.hero.belongings.getItem(DriedRose.class);
		if (rose != null) {
			if (this == rose.ghostWeapon())
				return Guardoper_ItermUpgrade.bounslevel(level());
		}

		if (isEquipped( Dungeon.hero ) || Dungeon.hero.belongings.contains( this )){
			return super.buffedLvl();
		} else {
			return level();
		}
	}

	@Override
	public boolean doPickUp(Hero hero) {
		if (hero.hasTalent(Talent.POLICE_SENSE)) this.cursedKnown=true;
		return super.doPickUp(hero);
	}

	@Override
	public Item upgrade() {
		return upgrade(false);
	}
	
	public Item upgrade(boolean enchant ) {

		if (enchant){
			if (enchantment == null || hasCurseEnchant()){
				enchant(Enchantment.random());
			}
		} else {
			if (hasCurseEnchant()){
				if (Random.Int(3) == 0) enchant(null);
			} else if (level() >= 4 && Random.Float(10) < Math.pow(2, level()-4)){
				enchant(null);
			}
		}
		
		cursed = false;
		
		return super.upgrade();
	}

	public static class PlaceHolder extends Weapon {

		{
			image = ItemSpriteSheet.WEAPON_HOLDER;
		}

		@Override
		public boolean isSimilar(Item item) {
			return !item.isEquipped(Dungeon.hero);
		}

		@Override
		public int STRReq(int lvl) {
			return 0;
		}

		@Override
		public int max(int lvl) {
			return 0;
		}

		@Override
		public int min(int lvl) {
			return 0;
		}

		@Override
		public String info() {
			return "";
		}
	}
	
	@Override
	public String name() {
		return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name( super.name() ) : super.name();
	}
	
	@Override
	public Item random() {
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		int n = 0;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}
		level(n);
		
		//30% chance to be cursed
		//10% chance to be enchanted
		float effectRoll = Random.Float();
		if (effectRoll < 0.3f) {
			enchant(Enchantment.randomCurse());
			cursed = true;
		} else if (effectRoll >= 0.9f){
			enchant();
		}

		return this;
	}
	
	@Override
	public boolean collect(Bag container) {
		if (super.collect(container)) {
			if (Dungeon.hero != null && Dungeon.hero.isAlive() && isIdentified() && enchantment != null) {
				Catalog.setSeen(enchantment.getClass());
				Statistics.itemTypesDiscovered.add(enchantment.getClass());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Item identify(boolean byHero) {
		if (enchantment != null && byHero && Dungeon.hero != null && Dungeon.hero.isAlive()) {
			Catalog.setSeen(enchantment.getClass());
			Statistics.itemTypesDiscovered.add(enchantment.getClass());
		}
		return super.identify(byHero);
	}

	public Weapon enchant( Enchantment ench ) {
		if (ench == null || !ench.curse()) curseInfusionBonus = false;
		enchantment = ench;
		updateQuickslot();
		if (ench != null && isIdentified() && Dungeon.hero != null
				&& Dungeon.hero.isAlive() && Dungeon.hero.belongings.contains(this)) {
			Catalog.setSeen(ench.getClass());
			Statistics.itemTypesDiscovered.add(ench.getClass());
		}
		return this;
	}

	public Weapon enchant() {

		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random( oldEnchantment );

		return enchant( ench );
	}

	public boolean hasEnchant(Class<?extends Enchantment> type, Char owner) {
		return enchantment != null && enchantment.getClass() == type && owner.buff(MagicImmune.class) == null;
	}
	
	//these are not used to process specific enchant effects, so magic immune doesn't affect them
	public boolean hasGoodEnchant(){
		return enchantment != null && !enchantment.curse();
	}

	public boolean hasCurseEnchant(){
		return enchantment != null && enchantment.curse();
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.glowing() : null;
	}

	public static abstract class Enchantment implements Bundlable {

		public static final Class<?>[] common = new Class<?>[]{
				Blazing.class, Chilling.class, Kinetic.class, Shocking.class,};

		public static final Class<?>[] uncommon = new Class<?>[]{
				Blocking.class, Blooming.class, Elastic.class,
				Lucky.class, Projecting.class, Unstable.class, Penetrate.class, Overeating.class};

		public static final Class<?>[] rare = new Class<?>[]{
				Corrupting.class, Grim.class, Vampiric.class, Dragonkill.class};

		private static final float[] typeChances = new float[]{
				50, //12.5% each
				40, //6.67% each
				10  //3.33% each
		};

		public static final Class<?>[] curses = new Class<?>[]{
				Annoying.class, Displacing.class, Exhausting.class, Fragile.class,
				Sacrificial.class, Wayward.class, Polarized.class, Friendly.class,
				contamination.class
		};


		public abstract int proc( Weapon weapon, Char attacker, Char defender, int damage );

		protected float procChanceMultiplier( Char attacker ){
			float multi = 1f;
			if (Dungeon.hero.belongings.weapon instanceof ThermiteBlade && Dungeon.hero.belongings.getItem(IsekaiItem.class) != null) {
				if (Dungeon.hero.belongings.getItem(IsekaiItem.class).isEquipped(Dungeon.hero))
					multi *= 3;
			}
			return multi;
		}

		public String name() {
			if (!curse())
				return name( Messages.get(this, "enchant"));
			else
				return name( Messages.get(Item.class, "curse"));
		}

		public String name( String weaponName ) {
			return Messages.get(this, "name", weaponName);
		}

		public String desc() {
			return Messages.get(this, "desc");
		}

		public boolean curse() {
			return false;
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public abstract ItemSprite.Glowing glowing();
		
		@SuppressWarnings("unchecked")
		public static Enchantment random( Class<? extends Enchantment> ... toIgnore ) {
			switch(Random.chances(typeChances)){
				case 0: default:
					return randomCommon( toIgnore );
				case 1:
					return randomUncommon( toIgnore );
				case 2:
					return randomRare( toIgnore );
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomCommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(common));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomUncommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(uncommon));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomRare( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(rare));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}

		@SuppressWarnings("unchecked")
		public static Enchantment randomCurse( Class<? extends Enchantment> ... toIgnore ){
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(curses));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
	}
}
