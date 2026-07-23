package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BookFlashShield;
import com.shatteredpixel.shatteredpixeldungeon.items.Snowsants_Coin;
import com.shatteredpixel.shatteredpixeldungeon.items.W0502;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfPurgatory;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfSuzuran;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wrench;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.EX42;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Enfild;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Enfild2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gamzashield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.GoldDogSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MinosFury;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Niansword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PatriotSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RhodesSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SanktaBet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scythe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.PurgatoryKnife;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Nullshield extends Item {
    private static final String AC_USE = "USE";
    {
        image = ItemSpriteSheet.NULL_DEF;

        stackable = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_USE)) {
            GameScene.selectItem(itemSelector, WndBag.Mode.ALL, Messages.get(this, "prompt"));
        }
    }

    private final WndBag.Listener itemSelector = new WndBag.Listener()  {
        @Override
        public void onSelect(final Item item) {
            if (item != null) {
                if (item instanceof Gamza)
                {
                   GLog.h(Messages.get(Nullshield.class, "suc"));
                   Gamzashield gma = new Gamzashield();
                   gma.enchant(Weapon.Enchantment.randomUncommon());
                    Dungeon.level.drop(gma, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                   gma.identify();
                   item.detach( curUser.belongings.backpack );
                    detach(curUser.belongings.backpack);
                }
                else if (item instanceof Nmould)
                {
                    GLog.h(Messages.get(Nullshield.class, "suc"));
                    Niansword nya = new Niansword();
                    nya.enchant(Weapon.Enchantment.randomUncommon());
                    nya.identify();
                    Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                    item.detach( curUser.belongings.backpack );
                    detach(curUser.belongings.backpack);
                }
                else if (item instanceof WornShortsword || item instanceof Dagger || item instanceof MagesStaff || item instanceof EX42 || item instanceof Gloves)
                {
                    if (curUser.belongings.weapon != item) {
                        if (Random.Int(3) < 2) {
                            GLog.h(Messages.get(Nullshield.class, "suc"));
                            RhodesSword nya = new RhodesSword();
                            nya.identify();

                            int level = item.level();
                            if (((MeleeWeapon) item).curseInfusionBonus) level--;
                            if (level > 0) {
                                nya.upgrade(level);
                            } else if (level < 0) {
                                nya.degrade(-level);
                            }

                            Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                            item.detach(curUser.belongings.backpack);
                            detach(curUser.belongings.backpack);
                        }
                        else { 	curUser.sprite.showStatus( CharSprite.NEUTRAL, Messages.get(Hero.class,"wtf") );
                            detach(curUser.belongings.backpack);}
                    }
                    else GLog.h(Messages.get(Nullshield.class, "fail_weapon"));
                }
                else if (item instanceof Enfild)
                {
                    if (curUser.belongings.weapon != item) {
                        GLog.h(Messages.get(Nullshield.class, "suc"));
                        Enfild2 nya = new Enfild2();
                        nya.identify();

                        int level = item.level();
                        if (((Enfild) item).curseInfusionBonus) level--;
                        if (level > 0) {
                            nya.upgrade( level );
                        } else if (level < 0) {
                            nya.degrade( -level );
                        }

                        nya.enchantment = ((Enfild) item).enchantment;
                        nya.curseInfusionBonus = ((Enfild) item).curseInfusionBonus;
                        nya.levelKnown = ((Enfild) item).levelKnown;
                        nya.cursedKnown = ((Enfild) item).cursedKnown;
                        nya.cursed = ((Enfild) item).cursed;
                        nya.augment = ((Enfild) item).augment;
                        Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        item.detach(curUser.belongings.backpack);
                        detach(curUser.belongings.backpack);
                    }
                    else GLog.h(Messages.get(Nullshield.class, "fail_weapon"));
                }
                else if (item instanceof Firebomb)
                {
                    GLog.h(Messages.get(Nullshield.class, "suc"));
                    W0502 nya = new W0502();
                    nya.identify();
                    Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                    item.detach( curUser.belongings.backpack );
                    detach(curUser.belongings.backpack);
                }
                else if (item instanceof Scythe)
                {
                    if (curUser.belongings.weapon != item) {
                        GLog.h(Messages.get(Nullshield.class, "suc"));
                        GoldDogSword nya = new GoldDogSword();
                        nya.identify();

                        int level = item.level();
                        if (((Scythe) item).curseInfusionBonus) level--;
                        if (level > 0) {
                            nya.upgrade( level );
                        } else if (level < 0) {
                            nya.degrade( -level );
                        }

                        nya.enchantment = ((Scythe) item).enchantment;
                        nya.curseInfusionBonus = ((Scythe) item).curseInfusionBonus;
                        nya.levelKnown = ((Scythe) item).levelKnown;
                        nya.cursedKnown = ((Scythe) item).cursedKnown;
                        nya.cursed = ((Scythe) item).cursed;
                        nya.augment = ((Scythe) item).augment;
                        Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        item.detach(curUser.belongings.backpack);
                        detach(curUser.belongings.backpack);
                    }
                    else GLog.h(Messages.get(Nullshield.class, "fail_weapon"));
                }
                else if (item instanceof WandOfWarding)
                {
                    GLog.h(Messages.get(Nullshield.class, "suc"));
                    Wrench nya = new Wrench();
                    nya.identify();

                    int level = item.level();
                    if (((Wand) item).curseInfusionBonus) level--;
                    if (level > 0) {
                        nya.upgrade(level);
                    } else if (level < 0) {
                        nya.degrade(-level);
                    }

                    Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                    item.detach(curUser.belongings.backpack);
                    detach(curUser.belongings.backpack);
                }
                else if (item instanceof Wand)
                {
                    if (Random.Int(4) > 1) {
                        GLog.h(Messages.get(Nullshield.class, "suc"));
                        StaffOfSuzuran nya = new StaffOfSuzuran();
                        nya.identify();

                        int level = item.level();
                        if (((Wand) item).curseInfusionBonus) level--;
                        if (level > 0) {
                            nya.upgrade(level);
                        } else if (level < 0) {
                            nya.degrade(-level);
                        }

                        nya.levelKnown = ((Wand) item).levelKnown;
                        nya.cursedKnown = ((Wand) item).cursedKnown;
                        nya.cursed = ((Wand) item).cursed;

                        Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        item.detach(curUser.belongings.backpack);
                        detach(curUser.belongings.backpack);
                    }
                    else  { curUser.sprite.showStatus( CharSprite.NEUTRAL, Messages.get(Hero.class,"wtf") );
                        detach(curUser.belongings.backpack);}
                }
                else if (item instanceof Pickaxe)
                {
                    if (((Pickaxe) item).sppower) {
                        if (Dungeon.hero.belongings.weapon != item) {
                        GLog.h(Messages.get(Nullshield.class, "suc"));
                        MinosFury gma = new MinosFury();
                        gma.enchant(Weapon.Enchantment.randomUncommon());
                        if(gma.doPickUp(Dungeon.hero)){}
                        else {
                            Dungeon.level.drop( gma, Dungeon.hero.pos ).sprite.drop();
                        }
                        gma.identify();
                        item.detach(curUser.belongings.backpack);
                        detach(curUser.belongings.backpack); }
                    }
                }
                else if (item instanceof PurgatoryKnife)
                {
                    GLog.h(Messages.get(Nullshield.class, "suc"));
                    StaffOfPurgatory nya = new StaffOfPurgatory();
                    nya.identify();
                    Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                    item.detach( curUser.belongings.backpack );
                    detach(curUser.belongings.backpack);
                }
                else if (item instanceof Runestone)
                {
                    GLog.h(Messages.get(Nullshield.class, "suc"));
                    Snowsants_Coin nya = new Snowsants_Coin();
                    Dungeon.level.drop(nya, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                    item.detach( curUser.belongings.backpack );
                    detach(curUser.belongings.backpack);
                }
                else if (item instanceof CorpseDust)
                {
                    GLog.h(Messages.get(Nullshield.class, "suc"));
                    SanktaBet gma = new SanktaBet();
                    Dungeon.level.drop(gma, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                    gma.identify();
                    item.detach( curUser.belongings.backpack );
                    detach(curUser.belongings.backpack);
                }
                else {

                    GLog.h(Messages.get(Nullshield.class, "fail"));}
            }
        }
    };

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
        return 75;
    }
}
