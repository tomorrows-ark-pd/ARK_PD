package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class ChaosCatalyst extends InventorySpell{
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing( 0xE6E6FA );
    {
        image = ItemSpriteSheet.FORCE_CATALYST;
        mode = WndBag.Mode.UPGRADEABLE;
        stackable = true;
    }

    public ItemSprite.Glowing glowing() {
        return COL;
    }

    @Override
    protected void onItemSelected(Item item) {

        item.level(0);
        item.upgrade();
        Catalog.countUse(item.getClass());
        Sample.INSTANCE.play(Assets.Sounds.EVOKE);
        Dungeon.hero.spendAndNext(1f);
        updateQuickslot();
    }

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return quantity * 25;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ForceCatalyst.class};
            inQuantity = new int[]{2};

            cost = 20;

            output = ChaosCatalyst.class;
            outQuantity = 1;
        }

    }
}
