package com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.DropTable;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookExecutionMode;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookHikari;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookPowerfulStrike;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookTacticalChanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookThoughts;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookWhispers;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookGenesis;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Gamza;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Nmould;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.SuperAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfTime;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CatGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Firmament;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ImageoverForm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PatriotSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SwordofArtorius;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WintersScar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CrabGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.PurgatoryKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Thunderbolt;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewRhodesLevel2;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Closure_TGBox extends ClosuresBox {

    private static final int[] PRICES = {80, 120, 175, 250, 400};

    private int priceTier = 0;

    {
        image = ItemSpriteSheet.CRYSTAL_CHEST;
        stackable = false;
        OpenLevel = 0;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_OPEN)) {
            if (Dungeon.level instanceof NewRhodesLevel2) {
                curUser = hero;
                curItem = this;
                GameScene.selectItem(itemSelector, WndBag.Mode.TRANMSUTABLE,
                        Messages.get(this, "select"));
            } else {
                    GLog.n(Messages.get(this, "fail"));
            }
        }
    }

    private static WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            if (!(curItem instanceof Closure_TGBox)) return;
            Closure_TGBox box = (Closure_TGBox) curItem;

            if (item == null) {
                curItem.collect(curUser.belongings.backpack);
                return;
            }

            Item result = box.transmute(item);

            if (result == null) {
                GLog.n(Messages.get(ScrollOfTransmutation.class, "nothing"));
                curItem.collect(curUser.belongings.backpack);
                return;
            }

            if (item.isEquipped(Dungeon.hero)) {
                item.cursed = false;
                ((EquipableItem) item).doUnequip(Dungeon.hero, false);
                ((EquipableItem) result).doEquip(Dungeon.hero);
            } else {
                item.detach(Dungeon.hero.belongings.backpack);
                if (!result.collect()) {
                    Dungeon.level.drop(result, curUser.pos).sprite.drop();
                }
            }

            if (result.isIdentified()) {
                Catalog.setSeen(result.getClass());
            }
            Transmuting.show(curUser, item, result);
            curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
            GLog.p(Messages.get(ScrollOfTransmutation.class, "morph"));

            curUser.spend(1f);
            curUser.busy();
            curUser.sprite.operate(curUser.pos);

            box.detach(curUser.belongings.backpack);
            Closure_TGBox next = new Closure_TGBox();
            next.priceTier = Math.min(box.priceTier + 1, PRICES.length - 1);
            Dungeon.level.drop(next, 3832).type = Heap.Type.FOR_SALE_28F;
        }
    };

    private Item transmute(Item item) {
        if (Random.Int(10) == 0) {
            Item avant = tryAvantEffect(item);
            if (avant != null) return avant;
        }

        if (item instanceof MagesStaff) {
            return changeStaff((MagesStaff) item);
        } else if (item instanceof MeleeWeapon || item instanceof MissileWeapon) {
            return changeWeapon((Weapon) item);
        } else if (item instanceof Scroll) {
            return changeScroll((Scroll) item);
        } else if (item instanceof Potion) {
            return changePotion((Potion) item);
        } else if (item instanceof Ring) {
            return changeRing((Ring) item);
        } else if (item instanceof Wand) {
            return changeWand((Wand) item);
        } else if (item instanceof Plant.Seed) {
            return changeSeed((Plant.Seed) item);
        } else if (item instanceof Runestone) {
            return changeStone((Runestone) item);
        } else if (item instanceof Artifact) {
            return changeArtifact((Artifact) item);
        }
        return null;
    }

    private Item tryAvantEffect(Item item) {
        if (item.isEquipped(Dungeon.hero)) return null;

        int level = item.level();

        if (item instanceof Runestone) {
            if (Random.Int(2) == 0) return new SuperAdvanceguard();
            else return new StoneOfAdvanceguard();
        } else if (item instanceof BookPowerfulStrike || item instanceof BookTacticalChanting
                || item instanceof BookExecutionMode || item instanceof BookThoughts
                || item instanceof BookHikari) {
            if (Random.IntRange(0, 21) < 12) return new BookWhispers();
            else return new BookGenesis();
        } else if (item instanceof SwordofArtorius || item instanceof WintersScar) {
            Item result = new PatriotSpear();
            result.level(level);
            result.identify();
            return result;
        } else if (item instanceof Wand) {
            Item result = new StaffOfTime();
            result.level(level);
            result.identify();
            return result;
        } else if (item instanceof Shortsword) {
            Item result = new Firmament();
            result.level(level);
            result.identify();
            return result;
        } else if (item instanceof PurgatoryKnife) {
            Item result = new ImageoverForm();
            result.identify();
            return result;
        } else if (item instanceof Gamza) {
            if (Random.IntRange(0, 100) < 31) return new Nmould();
            else return new Thunderbolt();
        } else if (item instanceof CrabGun) {
            Item result = new CatGun();
            result.level(level);
            result.identify();
            return result;
        }

        return null;
    }

    // --- Transmutation helpers (mirrored from ScrollOfTransmutation) ---

    private MagesStaff changeStaff(MagesStaff staff) {
        Class<? extends Wand> wandClass = staff.wandClass();
        if (wandClass == null) return null;
        Wand n;
        do {
            n = (Wand) Generator.random(Generator.Category.WAND);
        } while (Challenges.isItemBlocked(n) || n.getClass() == wandClass);
        n.level(0);
        n.identify();
        staff.imbueWand(n, null);
        return staff;
    }

    private Weapon changeWeapon(Weapon w) {
        Weapon n;
        Generator.Category c;
        if (w instanceof MeleeWeapon) {
            c = Generator.wepTiers[((MeleeWeapon) w).tier - 1];
        } else {
            c = Generator.misTiers[((MissileWeapon) w).tier - 1];
        }
        do {
            int i = Random.chances(DropTable.mask(c, c.probs));
            if (i == -1) i = Random.chances(c.probs); //backstop: degrade to vanilla rather than crash
            n = (Weapon) Reflection.newInstance(c.classes[i]);
        } while (Challenges.isItemBlocked(n) || n.getClass() == w.getClass());

        int level = w.level();
        if (w.curseInfusionBonus) level--;
        if (level > 0) n.upgrade(level);
        else if (level < 0) n.degrade(-level);

        n.enchantment = w.enchantment;
        n.curseInfusionBonus = w.curseInfusionBonus;
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        n.augment = w.augment;
        return n;
    }

    private Ring changeRing(Ring r) {
        Ring n;
        do {
            n = (Ring) Generator.random(Generator.Category.RING);
        } while (Challenges.isItemBlocked(n) || n.getClass() == r.getClass());
        n.level(0);
        int level = r.level();
        if (level > 0) n.upgrade(level);
        else if (level < 0) n.degrade(-level);
        n.levelKnown = r.levelKnown;
        n.cursedKnown = r.cursedKnown;
        n.cursed = r.cursed;
        return n;
    }

    private Artifact changeArtifact(Artifact a) {
        Artifact n = Generator.randomArtifact();
        if (n != null && !Challenges.isItemBlocked(n)) {
            n.cursedKnown = a.cursedKnown;
            n.cursed = a.cursed;
            n.levelKnown = a.levelKnown;
            n.transferUpgrade(a.visiblyUpgraded());
            return n;
        }
        return null;
    }

    private Wand changeWand(Wand w) {
        Wand n;
        do {
            n = (Wand) Generator.random(Generator.Category.WAND);
        } while (Challenges.isItemBlocked(n) || n.getClass() == w.getClass());
        n.level(0);
        int level = w.level();
        if (w.curseInfusionBonus) level--;
        if (level > 0) n.upgrade(level);
        else if (level < 0) n.degrade(-level);
        n.curChargeKnown = w.curChargeKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        n.curseInfusionBonus = w.curseInfusionBonus;

        n.curCharges = w.curCharges;
        n.updateLevel();
        return n;
    }

    private Plant.Seed changeSeed(Plant.Seed s) {
        Plant.Seed n;
        do {
            n = (Plant.Seed) Generator.random(Generator.Category.SEED);
        } while (n.getClass() == s.getClass());
        return n;
    }

    private Runestone changeStone(Runestone r) {
        Runestone n;
        do {
            n = (Runestone) Generator.random(Generator.Category.STONE);
        } while (n.getClass() == r.getClass());
        return n;
    }

    private Scroll changeScroll(Scroll s) {
        if (s instanceof ExoticScroll) {
            Class<? extends Scroll> mapped = ExoticScroll.exoToReg.get(s.getClass());
            return mapped != null ? Reflection.newInstance(mapped) : s;
        } else {
            Class<? extends ExoticScroll> mapped = ExoticScroll.regToExo.get(s.getClass());
            return mapped != null ? Reflection.newInstance(mapped) : s;
        }
    }

    private Potion changePotion(Potion p) {
        if (p instanceof ExoticPotion) {
            Class<? extends Potion> mapped = ExoticPotion.exoToReg.get(p.getClass());
            return mapped != null ? Reflection.newInstance(mapped) : p;
        } else {
            Class<? extends ExoticPotion> mapped = ExoticPotion.regToExo.get(p.getClass());
            return mapped != null ? Reflection.newInstance(mapped) : p;
        }
    }

    @Override
    public int value() {
        return PRICES[Math.max(0, Math.min(priceTier, PRICES.length - 1))];
    }

    // --- Bundle ---

    private static final String PRICE_TIER = "price_tier";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PRICE_TIER, priceTier);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        priceTier = Math.max(0, Math.min(bundle.getInt(PRICE_TIER), PRICES.length - 1));
    }
}
