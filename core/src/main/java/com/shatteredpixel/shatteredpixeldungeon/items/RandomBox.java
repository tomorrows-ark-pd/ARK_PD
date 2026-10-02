package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class RandomBox extends Item {
    private static final String AC_ADD = "ADD";

    {
        image = ItemSpriteSheet.CHEST;
        stackable = true;

        defaultAction = AC_ADD;

    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ADD);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ADD)) {

            hero.spend(1);
            hero.busy();

            hero.sprite.operate(hero.pos);

            detach(hero.belongings.backpack);
            randomReward(Random.IntRange(0, 9));
        }
    }


    public void randomReward(int i) {
        if (i < 6) {
            if (Random.Int(5) < 3) {
                getGoldLow();
            } else getGoldHigh();
        } else if (i < 8) {
            getSkill();
        } else getWeapon();
    }

    public void getGoldLow() {
        new Gold(Random.IntRange(150, 650)).doPickUp(Dungeon.hero);
    }

    public void getGoldHigh() {
        new Gold(Random.IntRange(300, 1000)).doPickUp(Dungeon.hero);
    }

    public void getSkill() {
        int chance = Random.IntRange(0, 50);

        chance += Random.IntRange(-10, Dungeon.hero.lvl);
        chance += Random.IntRange(0, Dungeon.hero.STR);
        chance += Random.IntRange(0, Dungeon.depth);

        if (chance > 60) {
            Item n = Generator.random(Generator.Category.SKL_T3);
            Dungeon.level.drop(n, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
        } else if (chance > Random.IntRange(20, 60)) {
            Item n = Generator.random(Generator.Category.SKL_T2);
            Dungeon.level.drop(n, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
        } else {
            Item n = Generator.random(Generator.Category.SKL_T1);
            Dungeon.level.drop(n, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
        }
    }

    public void getWeapon() {
        int chance = Random.IntRange(0, 50);
        chance += Random.IntRange(-5, Dungeon.hero.STR);
        if (Dungeon.hero.belongings.weapon != null) {
            chance += Random.IntRange(0, Dungeon.hero.belongings.weapon.buffedLvl() * 8);
        }

        //determine weapon tier based on roll
        int tier;
        if      (chance > 65) tier = 4;
        else if (chance > 50) tier = 3;
        else if (chance > 35) tier = 2;
        else                  tier = 1;

        Generator.Category c = Generator.wepTiers[tier];
        int i = Random.chances(DropTable.mask(c, c.probs));
        if (i == -1) i = Random.chances(c.probs); //backstop: degrade to vanilla rather than crash
        Weapon n = (MeleeWeapon) Reflection.newInstance(c.classes[i]);

        //determine upgrade level based on depth + armor
        int upChance = Random.IntRange(0, 25);
        upChance += Random.IntRange(-10, Dungeon.depth);
        if (Dungeon.hero.belongings.armor != null) {
            upChance += Random.IntRange(-5, Dungeon.hero.belongings.armor.buffedLvl() * 3);
        }

        if      (upChance > 35)  n.level(4);
        else if (upChance > 20)  n.level(3);
        else if (upChance > 12)  n.level(2);
        else if (upChance < -8)  n.level(-2);
        else if (upChance < 0)   n.level(-1);
        else                     n.level(0);

        Dungeon.level.drop(n, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 30;
    }
}
