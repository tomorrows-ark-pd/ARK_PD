package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class ArmorUpKit extends Item {
    private static final String AC_UPGRADE = "UPGRADE";

    {
        image = ItemSpriteSheet.KIT;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_UPGRADE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_UPGRADE)) {
            curUser = hero;
            ArmorUpgard(hero);
            this.detach(hero.belongings.backpack);
        }
    }

    public void ArmorUpgard(Hero hero) {
        if (hero.belongings.armor != null) {
            Armor HeroArmor = hero.belongings.armor;

            if (HeroArmor.checkSeal() != null) {
                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1.22f);
                ArmorUpKit Kit = new ArmorUpKit();
                if (Kit.doPickUp(Dungeon.hero)) {
                    GLog.i(Messages.get(Dungeon.hero, "you_now_have", Kit.name()));
                } else {
                    Dungeon.level.drop(Kit, hero.pos).sprite.drop();
                }
                return;
            } else {
                if (HeroArmor.tier < 3) {
                    ScaleArmor armor = new ScaleArmor();
                    int level = HeroArmor.level();
                    if (HeroArmor.curseInfusionBonus) level--;
                    if (level > 0) {
                        armor.upgrade(level + 1);
                        Catalog.countUse(ScaleArmor.class);
                    } else if (level < 0) {
                        armor.degrade(-level);
                    }

                    armor.inscribe(HeroArmor.glyph != null ? Reflection.newInstance(HeroArmor.glyph.getClass()) : null);
                    armor.curseInfusionBonus = HeroArmor.curseInfusionBonus;
                    armor.levelKnown = HeroArmor.levelKnown;
                    armor.cursedKnown = HeroArmor.cursedKnown;
                    armor.cursed = HeroArmor.cursed;
                    armor.augment = HeroArmor.augment;

                    Dungeon.hero.belongings.armor = armor;
                } else if (HeroArmor.tier < 5) {
                    PlateArmor armor = new PlateArmor();
                    int level = HeroArmor.level();
                    if (HeroArmor.curseInfusionBonus) level--;
                    if (level > 0) {
                        armor.upgrade(level + 1);
                        Catalog.countUse(PlateArmor.class);
                    } else if (level < 0) {
                        armor.degrade(-level);
                    }

                    armor.inscribe(HeroArmor.glyph != null ? Reflection.newInstance(HeroArmor.glyph.getClass()) : null);
                    armor.curseInfusionBonus = HeroArmor.curseInfusionBonus;
                    armor.levelKnown = HeroArmor.levelKnown;
                    armor.cursedKnown = HeroArmor.cursedKnown;
                    armor.cursed = HeroArmor.cursed;
                    armor.augment = HeroArmor.augment;

                    Dungeon.hero.belongings.armor = armor;
                } else {
                    Dungeon.hero.belongings.armor.upgrade();
                    Catalog.countUse(Dungeon.hero.belongings.armor.getClass());
                }
                curUser.sprite.operate(curUser.pos);
                Sample.INSTANCE.play(Assets.Sounds.EVOKE);
            }
        } else {
            if (!new ArmorUpKit().collect()) {
                Dungeon.level.drop(new ArmorUpKit(), curUser.pos).sprite.drop();
            }
        }
    }


    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }
}
