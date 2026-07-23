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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.AnnihilationGear;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.DewVial;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SkillBook;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SealOfLight;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatCutlet;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfWarp;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wrench;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ChenSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.EX42;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Heamyo;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.NEARL_AXE;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.LightKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Reflection;

import java.util.Collection;

public enum HeroClass {

    WARRIOR("warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR, HeroSubClass.HEAT),
    MAGE("mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK, HeroSubClass.CHAOS),
    ROGUE("rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER, HeroSubClass.WILD),
    HUNTRESS("huntress", HeroSubClass.SNIPER, HeroSubClass.WARDEN, HeroSubClass.STOME),
    ROSECAT("rosecat", HeroSubClass.DESTROYER, HeroSubClass.GUARDIAN, HeroSubClass.WAR),
    NEARL("nearl", HeroSubClass.KNIGHT, HeroSubClass.SAVIOR, HeroSubClass.FLASH),
    CHEN("chen", HeroSubClass.SWORDMASTER, HeroSubClass.SPSHOOTER);

    private String title;
    private HeroSubClass[] subClasses;

    HeroClass(String title, HeroSubClass... subClasses) {
        this.title = title;
        this.subClasses = subClasses;
    }

    public void initHero(Hero hero) {

        hero.heroClass = this;
        Talent.initClassTalents(hero);

        initCommon(hero);

        switch (this) {
            case WARRIOR:
                initWarrior(hero);
                break;

            case MAGE:
                initMage(hero);
                break;

            case ROGUE:
                initRogue(hero);
                break;

            case HUNTRESS:
                initHuntress(hero);
                break;

            case ROSECAT:
                initRosecat(hero);
                break;

            case NEARL:
                initNearl(hero);
                break;

            case CHEN:
                initChen(hero);
                break;
        }

    }

    private static void initCommon(Hero hero) {
        Item i = new ClothArmor().identify();
        if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor) i;

        i = new Food();
        if (!Challenges.isItemBlocked(i)) i.collect();

        new ScrollOfIdentify().identify();
        new FoodBag().collect();

        if (DeviceCompat.isDebug()) {
            new VelvetPouch().collect();
            new MagicalHolster().collect();

            new PotionOfStrength().identify().quantity(40).collect();
            new PotionOfExperience().identify().quantity(40).collect();
            new PotionOfHealing().identify().quantity(40).collect();
            new ScrollOfUpgrade().identify().quantity(40).collect();
            new ScrollOfMagicMapping().identify().quantity(40).collect();

            new Amulet().collect();
            new Heamyo().collect();

            Ankh ankh = new Ankh();
            ankh.collect();

            new Wrench().identify().collect();

            // Catalog collection test fixtures. Uncomment one section at a time to avoid flooding inventory.

            // collectCatalogItems(Catalog.POTIONS.items());
            // collectCatalogItems(Catalog.SCROLLS.items());
            // collectCatalogItems(Catalog.SEEDS.items());
            // collectCatalogItems(Catalog.STONES.items());
            // collectCatalogItems(Catalog.FOOD.items());
            // collectCatalogItems(Catalog.EXOTIC_POTIONS.items());
            // collectCatalogItems(Catalog.EXOTIC_SCROLLS.items());
            // collectCatalogItems(Catalog.BOMBS.items());
            // collectCatalogItems(Catalog.TIPPED_DARTS.items());
            // collectCatalogItems(Catalog.BREWS_ELIXIRS.items());
            // collectCatalogItems(Catalog.SPELLS.items());
            // collectCatalogItems(Catalog.MISC_CONSUMABLES.items());

            // These catalog sections are effects rather than inventory items.
            // markCatalogSeen(Catalog.ENCHANTMENTS.items());
            // markCatalogSeen(Catalog.GLYPHS.items());
        }

        Dungeon.LimitedDrops.FOOD_BAG.drop();
    }

    private static void collectCatalogItems(Collection<Class<?>> itemClasses) {
        for (Class<?> itemClass : itemClasses) {
            if (!Item.class.isAssignableFrom(itemClass)) {
                continue;
            }

            Item item = (Item) Reflection.newInstance(itemClass);
            setupCatalogTestItem(item).collect();
        }
    }

    private static Item setupCatalogTestItem(Item item) {
        if (item instanceof Weapon || item instanceof Armor || item instanceof Wand || item instanceof Ring || item instanceof Artifact) {
            item.identify();
        } else if (item instanceof Potion || item instanceof Scroll) {
            item.identify().quantity(5);
        } else {
            item.quantity(5);
        }

        return item;
    }

    private static void markCatalogSeen(Collection<Class<?>> classes) {
        for (Class<?> cls : classes) {
            Catalog.setSeen(cls);
        }
    }

    public Badges.Badge masteryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.MASTERY_WARRIOR;
            case MAGE:
                return Badges.Badge.MASTERY_MAGE;
            case ROGUE:
                return Badges.Badge.MASTERY_ROGUE;
            case HUNTRESS:
                return Badges.Badge.MASTERY_HUNTRESS;
            case ROSECAT:
                return Badges.Badge.MASTERY_ROSECAT;
            case NEARL:
                return Badges.Badge.MASTERY_NEARL;
            case CHEN:
                return Badges.Badge.MASTERY_CHEN;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void initWarrior(Hero hero) {
        (hero.belongings.weapon = new WornShortsword()).identify();
        ThrowingStone stones = new ThrowingStone();

        stones.quantity(3).collect();
        Dungeon.quickslot.setSlot(0, stones);

        if (hero.belongings.armor != null) {
            hero.belongings.armor.affixSeal(new BrokenSeal());
        }

        SkillBook skillB;
        skillB = new SkillBook();
        skillB.quantity(1).collect();
        Dungeon.quickslot.setSlot(1, skillB);

        new PotionBandolier().collect();
        Dungeon.LimitedDrops.POTION_BANDOLIER.drop();

        new VelvetPouch().collect();
        Dungeon.LimitedDrops.VELVET_POUCH.drop();

        new PotionOfHealing().identify();
        new ScrollOfRage().identify();
        new ScrollOfWarp().identify();

        new DewVial().collect();
    }

    private static void initMage(Hero hero) {
        MagesStaff staff;

        staff = new MagesStaff(new WandOfMagicMissile());

        (hero.belongings.weapon = staff).identify();
        hero.belongings.weapon.activate(hero);

        Dungeon.quickslot.setSlot(0, staff);

        new ScrollHolder().collect();
        Dungeon.LimitedDrops.SCROLL_HOLDER.drop();

        new PotionBandolier().collect();
        Dungeon.LimitedDrops.POTION_BANDOLIER.drop();

        new ScrollOfUpgrade().identify();
        new PotionOfLiquidFlame().identify();
        new ScrollOfWarp().identify();

        SkillBook skillB;
        skillB = new SkillBook();
        skillB.quantity(1).collect();
        Dungeon.quickslot.setSlot(1, skillB);

        new DewVial().collect();
    }

    private static void initRogue(Hero hero) {
        (hero.belongings.weapon = new Dagger()).identify();

        CloakOfShadows cloak = new CloakOfShadows();
        (hero.belongings.artifact = cloak).identify();
        hero.belongings.artifact.activate(hero);

        ThrowingKnife knives = new ThrowingKnife();
        knives.quantity(1).collect();
        Dungeon.quickslot.setSlot(0, cloak);
        Dungeon.quickslot.setSlot(1, knives);

        new VelvetPouch().collect();
        Dungeon.LimitedDrops.VELVET_POUCH.drop();

        new ScrollHolder().collect();
        Dungeon.LimitedDrops.SCROLL_HOLDER.drop();

        new ScrollOfMagicMapping().identify();
        new PotionOfInvisibility().identify();
        new ScrollOfWarp().identify();

        SkillBook skillB;
        skillB = new SkillBook();
        skillB.quantity(1).collect();
        Dungeon.quickslot.setSlot(2, skillB);

        new DewVial().collect();
    }

    private static void initHuntress(Hero hero) {

        (hero.belongings.weapon = new Gloves()).identify();
        SpiritBow bow = new SpiritBow();
        bow.identify().collect();

        Dungeon.quickslot.setSlot(0, bow);

        new VelvetPouch().collect();
        Dungeon.LimitedDrops.VELVET_POUCH.drop();

        new MagicalHolster().collect();
        Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

        new PotionOfMindVision().identify();
        new ScrollOfLullaby().identify();
        new ScrollOfWarp().identify();

        SkillBook skillB;
        skillB = new SkillBook();
        skillB.quantity(1).collect();
        Dungeon.quickslot.setSlot(1, skillB);

        new DewVial().collect();
    }

    private static void initRosecat(Hero hero) {
        (hero.belongings.weapon = new EX42()).identify();

        AnnihilationGear Gear = new AnnihilationGear();
        Gear.identify().collect();

        Dungeon.quickslot.setSlot(0, Gear);

        new VelvetPouch().collect();
        Dungeon.LimitedDrops.VELVET_POUCH.drop();

        new MagicalHolster().collect();
        Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

        new PotionOfLevitation().identify();
        new ScrollOfWarp().identify();

        SkillBook skillB;
        skillB = new SkillBook();
        skillB.quantity(1).collect();
        Dungeon.quickslot.setSlot(1, skillB);

        Dungeon.quickslot.setSlot(3, hero.belongings.weapon);

        new DewVial().collect();
        new MeatCutlet().quantity(2).collect();
    }

    private static void initNearl(Hero hero) {
        (hero.belongings.weapon = new NEARL_AXE()).identify();

        SealOfLight SEAL = new SealOfLight();
        (hero.belongings.artifact = SEAL).identify();
        hero.belongings.artifact.activate(hero);
        Dungeon.quickslot.setSlot(0, SEAL);

        LightKnife knives = new LightKnife();
        knives.quantity(2).collect();
        Dungeon.quickslot.setSlot(1, knives);

        new ScrollHolder().collect();
        Dungeon.LimitedDrops.SCROLL_HOLDER.drop();

        new MagicalHolster().collect();
        Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

        new PotionOfExperience().identify();
        new ScrollOfRemoveCurse().identify();
        new ScrollOfWarp().identify();

        SkillBook skillB;
        skillB = new SkillBook();
        skillB.quantity(1).collect();
        Dungeon.quickslot.setSlot(2, skillB);
        new DewVial().collect();
    }

    private static void initChen(Hero hero) {
        (hero.belongings.weapon = new ChenSword()).identify();

        new PotionBandolier().collect();
        Dungeon.LimitedDrops.POTION_BANDOLIER.drop();

        new MagicalHolster().collect();
        Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

        new PotionOfStrength().identify();
        new PotionOfParalyticGas().identify();
        new ScrollOfWarp().identify();

        SkillBook skillB;
        skillB = new SkillBook();
        skillB.quantity(1).collect();
        Dungeon.quickslot.setSlot(0, skillB);
        new DewVial().collect();
    }

    public String title() {
        return Messages.get(HeroClass.class, title);
    }

    public String desc() {
        return Messages.get(HeroClass.class, name() + "_desc");
    }

    public HeroSubClass[] subClasses() {
        return subClasses;
    }

    public String spritesheet() {
        if (Dungeon.hero != null) {

            switch (Dungeon.hero.CharSkin) {
                case 1:
                    return Assets.Sprites.TALRU_FIGHT;
                case 2:
                    return Assets.Sprites.FNOVA;
                case 3:
                    return Assets.Sprites.SKD;
                case 4:
                    return Assets.Sprites.SSR;
                case 5:
                    return Assets.Sprites.GRN;
                case 6:
                    return Assets.Sprites.JESSI;
                case 7:
                    return Assets.Sprites.LAPPY;
                case 8:
                    return Assets.Sprites.FROST;
                case 9:
                    return Assets.Sprites.MDR;
                case 10:
                    return Assets.Sprites.AST;
                case 11:
                    return Assets.Sprites.SPT;
                case 12:
                    return Assets.Sprites.SCH;
                case 13:
                    return Assets.Sprites.ARCT;
                case 14:
                    return Assets.Sprites.TMM;
                case 15:
                    return Assets.Sprites.FRK;
                case 16:
                    return Assets.Sprites.WED;
                case 17:
                    return Assets.Sprites.GLD;
            }
        }
        switch (this) {
            case WARRIOR:
            default:
                return Assets.Sprites.BLAZE;
            case MAGE:
                return Assets.Sprites.AMIYA;
            case ROGUE:
                return Assets.Sprites.RED;
            case HUNTRESS:
                return Assets.Sprites.GREY;
            case ROSECAT:
                return Assets.Sprites.ROSEMARY;
            case NEARL:
                return Assets.Sprites.NEARL;
            case CHEN:
                return Assets.Sprites.CHEN;
        }
    }

    public String spritesheet_de() {
        switch (this) {
            case WARRIOR:
            default:
                return Assets.Sprites.BLAZE;
            case MAGE:
                return Assets.Sprites.AMIYA;
            case ROGUE:
                return Assets.Sprites.RED;
            case HUNTRESS:
                return Assets.Sprites.GREY;
            case ROSECAT:
                return Assets.Sprites.ROSEMARY;
            case NEARL:
                return Assets.Sprites.NEARL;
            case CHEN:
                return Assets.Sprites.CHEN;
        }
    }

    public Image icon() {
        switch (this) {
            case WARRIOR:
            default:
                return new Image(Icons.BLAZE.get());
            case MAGE:
                return new Image(Icons.AMIYA.get());
            case ROGUE:
                return new Image(Icons.P_RED.get());
            case HUNTRESS:
                return new Image(Icons.GREY.get());
            case ROSECAT:
                return new Image(Icons.ROSEMARI.get());
            case NEARL:
                return new Image(Icons.NEARL.get());
            case CHEN: //첸포인트
                return new Image(Icons.CHEN.get());
        }
    }

    public String splashArt() {
        switch (this) {
            case WARRIOR:
            default:
                return Assets.Splashes.WARRIOR;
            case MAGE:
                return Assets.Splashes.MAGE;
            case ROGUE:
                return Assets.Splashes.ROGUE;
            case HUNTRESS:
                return Assets.Splashes.HUNTRESS;
            case ROSECAT:
                return Assets.Splashes.ROSECAT;
            case NEARL:
                return Assets.Splashes.NEARL;
            case CHEN:
                return Assets.Splashes.CHEN;
        }
    }

    public String[] perks() {
        switch (this) {
            case WARRIOR:
            default:
                return new String[]{
                        Messages.get(HeroClass.class, "warrior_perk1"),
                        Messages.get(HeroClass.class, "warrior_perk2"),
                        Messages.get(HeroClass.class, "warrior_perk3"),
                        Messages.get(HeroClass.class, "warrior_perk4"),
                        Messages.get(HeroClass.class, "warrior_perk5"),
                };
            case MAGE:
                return new String[]{
                        Messages.get(HeroClass.class, "mage_perk1"),
                        Messages.get(HeroClass.class, "mage_perk2"),
                        Messages.get(HeroClass.class, "mage_perk3"),
                        Messages.get(HeroClass.class, "mage_perk4"),
                        Messages.get(HeroClass.class, "mage_perk5"),
                };
            case ROGUE:
                return new String[]{
                        Messages.get(HeroClass.class, "rogue_perk1"),
                        Messages.get(HeroClass.class, "rogue_perk2"),
                        Messages.get(HeroClass.class, "rogue_perk3"),
                        Messages.get(HeroClass.class, "rogue_perk4"),
                        Messages.get(HeroClass.class, "rogue_perk5"),
                };
            case HUNTRESS:
                return new String[]{
                        Messages.get(HeroClass.class, "huntress_perk1"),
                        Messages.get(HeroClass.class, "huntress_perk2"),
                        Messages.get(HeroClass.class, "huntress_perk3"),
                        Messages.get(HeroClass.class, "huntress_perk4"),
                        Messages.get(HeroClass.class, "huntress_perk5"),
                };
        }
    }

    public boolean isUnlocked() {
        //always unlock on debug builds
        if (DeviceCompat.isDebug()) return true;

        switch (this) {
            case WARRIOR:
            default:
                return true;
            case MAGE:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
            case ROGUE:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
            case HUNTRESS:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
            case ROSECAT:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_ROSECAT);
            case NEARL:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_NEARL);
            case CHEN:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_CHEN);
        }
    }

    public String unlockMsg() {
        switch (this) {
            case WARRIOR:
            default:
                return "";
            case MAGE:
                return Messages.get(HeroClass.class, "mage_unlock");
            case ROGUE:
                return Messages.get(HeroClass.class, "rogue_unlock");
            case HUNTRESS:
                return Messages.get(HeroClass.class, "huntress_unlock");
            case ROSECAT:
                return Messages.get(HeroClass.class, "rosecat_unlock");
            case NEARL:
                return Messages.get(HeroClass.class, "nearl_unlock");
            case CHEN:
                return Messages.get(HeroClass.class, "chen_unlock");
        }
    }

    private static final String CLASS = "class";

    public void storeInBundle(Bundle bundle) {
        bundle.put(CLASS, toString());
    }

    public static HeroClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(CLASS);
        return !value.isEmpty() ? valueOf(value) : ROGUE;
    }

}
