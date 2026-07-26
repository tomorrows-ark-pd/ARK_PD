package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class RingKit extends Item {
    private static final String AC_APPLY = "APPLY";
    private static final float TIME_TO_UPGRADE = 2;

    {
        image = ItemSpriteSheet.RINGKIT;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_APPLY);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_APPLY)) {
            curUser = hero;
            detachaa();
            GameScene.selectItem(itemSelector, WndBag.Mode.RING, Messages.get(this, "prompt"));

        }
    }

        private final WndBag.Listener itemSelector = new WndBag.Listener() {
            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    if (item instanceof Ring && item != Dungeon.hero.belongings.ring && item != Dungeon.hero.belongings.misc) {

                        RingKit.this.upgrade((Ring) item);

                        final Ring rings[] = new Ring[2];

                        do {
                            rings[0] = (Ring) Generator.random(Generator.Category.RING);
                        }
                        while (rings[0].getClass() == item.getClass());
                        do {
                            rings[1] = (Ring) Generator.random(Generator.Category.RING);
                        }
                        while (rings[1].getClass() == item.getClass());


                        rings[0].identify();
                        rings[1].identify();

                        GameScene.show(new WndOptions(Messages.titleCase(RingKit.this.name()),
                                Messages.get(RingKit.class, "ring") +
                                        "\n\n" +
                                        Messages.get(RingKit.class, "cancel_warn"),
                                rings[0].name(),
                                rings[1].name(),
                                Messages.get(RingKit.class, "upgrade")) {

                            @Override
                            protected void onSelect(int index) {
                                if (index < 2) {
                                    Ring n = rings[index];
                                    n.level(0);

                                    int level = item.level();
                                    if (level > 0) {
                                        n.upgrade(level);
                                    } else if (level < 0) {
                                        n.degrade(-level);
                                    }

                                    n.levelKnown = item.levelKnown;
                                    n.cursedKnown = item.cursedKnown;
                                    n.cursed = item.cursed;

                                    if (item.isEquipped(Dungeon.hero)) {
                                        item.cursed = false; //to allow it to be unequipped
                                        ((EquipableItem) item).doUnequip(Dungeon.hero, false);
                                        ((EquipableItem) n).doEquip(Dungeon.hero);
                                    } else {
                                        item.detach(Dungeon.hero.belongings.backpack);
                                        if (!n.collect()) {
                                            Dungeon.level.drop(n, curUser.pos).sprite.drop();
                                        }

                                        n.identify();
                                        Sample.INSTANCE.play(Assets.Sounds.EVOKE);
                                    }
                                }
                            }

                            @Override
                            public void onBackPressed() {
                            }
                        });

                    } else {
                        if (!new RingKit().collect()){
                            Dungeon.level.drop(new RingKit(), curUser.pos).sprite.drop();
                        }
                    }
                } else {
                    if (!new RingKit().collect()){
                        Dungeon.level.drop(new RingKit(), curUser.pos).sprite.drop();
                    }
                }
            }
        };


    private void upgrade(Ring item) {
        item.upgrade();
        Catalog.countUse(item.getClass());

        curUser.sprite.operate(curUser.pos);
        Sample.INSTANCE.play(Assets.Sounds.EVOKE);
        Dungeon.hero.spendAndNext(TIME_TO_UPGRADE);
    }

    private void detachaa() {
        this.detach(Dungeon.hero.belongings.backpack);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
