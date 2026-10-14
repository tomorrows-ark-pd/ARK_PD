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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Sandvich;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.cooking.FRY_EGG;
import com.shatteredpixel.shatteredpixeldungeon.items.food.cooking.FRY_GAMZA;
import com.shatteredpixel.shatteredpixeldungeon.items.food.cooking.Glassate;
import com.shatteredpixel.shatteredpixeldungeon.items.food.cooking.HoneyBread;
import com.shatteredpixel.shatteredpixeldungeon.items.food.cooking.NotBarFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.cooking.SmokeEgg;
import com.shatteredpixel.shatteredpixeldungeon.items.food.cooking.Yukjeon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirsOfIronSkin;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirsOfSoulBreak;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirsOfSoulProtection;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfWarp;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Avantgardeform;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BlastSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ForceCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.InstantRecharge;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalArmord;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.OathofFire;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SaltBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WeaponTransform;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KollamSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.NormalMagazine;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.UpMagazine;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public abstract class Recipe {

    public abstract boolean testIngredients(ArrayList<Item> ingredients);

    public abstract int cost(ArrayList<Item> ingredients);

    public abstract Item brew(ArrayList<Item> ingredients);

    public abstract Item sampleOutput(ArrayList<Item> ingredients);

    //subclass for the common situation of a recipe with static inputs and outputs
    public static abstract class SimpleRecipe extends Recipe {

        //*** These elements must be filled in by subclasses
        protected Class<? extends Item>[] inputs; //each class should be unique
        protected int[] inQuantity;

        protected int cost;

        protected Class<? extends Item> output;
        protected int outQuantity;
        //***

        //gets a simple list of items based on inputs
        public ArrayList<Item> getIngredients() {
            ArrayList<Item> result = new ArrayList<>();
            for (int i = 0; i < inputs.length; i++) {
                Item ingredient = Reflection.newInstance(inputs[i]);
                ingredient.quantity(inQuantity[i]);
                result.add(ingredient);
            }
            return result;
        }

        @Override
        public final boolean testIngredients(ArrayList<Item> ingredients) {

            int[] needed = inQuantity.clone();

            for (Item ingredient : ingredients) {
                if (!ingredient.isIdentified()) return false;
                for (int i = 0; i < inputs.length; i++) {
                    if (ingredient.getClass() == inputs[i]) {
                        needed[i] -= ingredient.quantity();
                        break;
                    }
                }
            }

            for (int i : needed) {
                if (i > 0) {
                    return false;
                }
            }

            return true;
        }

        public final int cost(ArrayList<Item> ingredients) {
            return cost;
        }

        @Override
        public final Item brew(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            int[] needed = inQuantity.clone();

            for (Item ingredient : ingredients) {
                for (int i = 0; i < inputs.length; i++) {
                     if (ingredient.getClass() == inputs[i] && needed[i] > 0) {
                        if (needed[i] <= ingredient.quantity()) {
                            ingredient.quantity(ingredient.quantity() - needed[i]);
                            needed[i] = 0;
                        } else {
                            needed[i] -= ingredient.quantity();
                            ingredient.quantity(0);
                        }
                    }
                }
            }

            //sample output and real output are identical in this case.
            return sampleOutput(null);
        }

        //ingredients are ignored, as output doesn't vary
        public final Item sampleOutput(ArrayList<Item> ingredients) {
            try {
                Item result = Reflection.newInstance(output);
                result.quantity(outQuantity);
                return result;
            } catch (Exception e) {
                TomorrowRogueNight.reportException(e);
                return null;
            }
        }
    }


    //*******
    // Static members
    //*******

    private static Recipe[] oneIngredientRecipes = new Recipe[]{
            new Scroll.ScrollToStone(),
            new StewedMeat.oneMeat(),
            new SmokeEgg.Recipe()
    };

    private static Recipe[] twoIngredientRecipes = new Recipe[]{
            new Blandfruit.CookFruit(),
            new Bomb.EnhanceBomb(),
            new AlchemicalCatalyst.Recipe(),
            new ArcaneCatalyst.Recipe(),
            new ElixirOfArcaneArmor.Recipe(),
            new ElixirOfAquaticRejuvenation.Recipe(),
            new ElixirOfDragonsBlood.Recipe(),
            new ElixirOfIcyTouch.Recipe(),
            new ElixirOfMight.Recipe(),
            new ElixirOfHoneyedHealing.Recipe(),
            new ElixirOfToxicEssence.Recipe(),
            new ElixirsOfSoulBreak.Recipe(),
            new BlizzardBrew.Recipe(),
            new InfernalBrew.Recipe(),
            new ShockingBrew.Recipe(),
            new CausticBrew.Recipe(),
            new Alchemize.Recipe(),
            new AquaBlast.Recipe(),
            new BeaconOfReturning.Recipe(),
            new CurseInfusion.Recipe(),
            new FeatherFall.Recipe(),
            new MagicalInfusion.Recipe(),
            new MagicalArmord.Recipe(),
            new PhaseShift.Recipe(),
            new ReclaimTrap.Recipe(),
            new Recycle.Recipe(),
            new WildEnergy.Recipe(),
            new StewedMeat.twoMeat(),
            // 명픽던 추가
            new StaffKit.Recipe(),
            new ScrollOfWarp.Recipe(),
            new NormalMagazine.Recipe(),
            new UpMagazine.Recipe(),
            new HoneyBread.Recipe(),
            new WeaponTransform.Recipe(),
            new OathofFire.Recipe(),
            new ForceCatalyst.Recipe(),
            new ChaosCatalyst.Recipe(),
            new FRY_EGG.Recipe(),
            new FRY_GAMZA.Recipe(),
            new SaltBlast.Recipe(),
            new BlastSpell.Recipe(),
            new InstantRecharge.Recipe(),
            new PortableCover.Recipe(),
    };

    private static Recipe[] threeIngredientRecipes = new Recipe[]{
            new Potion.SeedToPotion(),
            new ExoticPotion.PotionToExotic(),
            new ExoticScroll.ScrollToExotic(),
            new StewedMeat.threeMeat(),
            new MeatPie.Recipe(),
            new Sandvich.Recipe(),
            new NotBarFood.Recipe(),
            new Yukjeon.Recipe(),
            new Avantgardeform.Recipe(),
            new Glassate.Recipe(),
            new ElixirsOfSoulProtection.Recipe(),
            new ElixirsOfIronSkin.Recipe(),
            new KollamSword.Recipe()
    };

    public static Recipe findRecipe(ArrayList<Item> ingredients) {

        if (ingredients.size() == 1) {
            for (Recipe recipe : oneIngredientRecipes) {
                if (recipe.testIngredients(ingredients)) {
                    return recipe;
                }
            }

        } else if (ingredients.size() == 2) {
            for (Recipe recipe : twoIngredientRecipes) {
                if (recipe.testIngredients(ingredients)) {
                    return recipe;
                }
            }

        } else if (ingredients.size() == 3) {
            for (Recipe recipe : threeIngredientRecipes) {
                if (recipe.testIngredients(ingredients)) {
                    return recipe;
                }
            }
        }

        return null;
    }

    public static boolean usableInRecipe(Item item) {
        if ((item instanceof  NormalMagazine)) return true;
        return !item.isEquipped(Dungeon.hero);
    }
}


