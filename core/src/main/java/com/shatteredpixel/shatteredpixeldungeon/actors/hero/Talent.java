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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Rose_Force;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WildMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.AnnihilationGear;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SealOfLight;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.GunWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public enum Talent {

    GENIUS(97, 1),

    //Warrior T1
    HEARTY_MEAL(0), ARMSMASTERS_INTUITION(1), TEST_SUBJECT(2), IRON_WILL(3),
    //Warrior T2
    IRON_STOMACH(4), RESTORED_WILLPOWER(5), RUNIC_TRANSFERENCE(6), LETHAL_MOMENTUM(7), IMPROVISED_PROJECTILES(8),
    //Warrior T3
    HOLD_FAST(9, 3), STRONGMAN(10, 3),
    //Berserker T3
    ENDLESS_RAGE(11, 3), BERSERKING_STAMINA(12, 3), ENRAGED_CATALYST(13, 3),
    //Gladiator T3
    CLEAVE(14, 3), FINALBLOW(15, 3), ENHANCED_COMBO(16, 3),
    //HEAT T3
    HEAT_BLOW(201, 3), HEAT_OF_PROTECTION(202, 3), HEAT_OF_RECOVERY(203, 3),
    //Warrior T4
    TACTICAL_SHIELD(17, 3), CHAINSAW_EXTEND(18, 3),
    //Berserker T4
    INFINITE_RAGE(11, 4), INFINITE_BATTLE(12, 4),
    //Gladiator T4
    SPARKOFLIFE(13, 4), DEADLY_REACH(15, 4),
    // HEAT T4
    HEAT_OF_ABSORPTION(204, 4), REDCOMET(205, 4),

    //Mage T1
    EMPOWERING_MEAL(32), SCHOLARS_INTUITION(33), TESTED_HYPOTHESIS(34), BACKUP_BARRIER(35),
    //Mage T2
    ENERGIZING_MEAL(36), ENERGIZING_UPGRADE(37), WAND_PRESERVATION(38), ARCANE_SNIPE(39), SHIELD_BATTERY(40),
    //Mage T3
    PSYCHOANALYSIS(41, 3), ALLY_WARP(42, 3),
    //Battlemage T3
    EMPOWERED_STRIKE(43, 3), MYSTICAL_CHARGE(44, 3), EXCESS_CHARGE(45, 3),
    //Warlock T3
    SOUL_EATER(46, 3), SOUL_SIPHON(47, 3), NECROMANCERS_MINIONS(48, 3),
    //Chaos T3
    CHIMERA(196, 3), RESTRICTION(197, 3), EMOTION_ABSORPTION(198, 3),
    //Mage T4
    LIBERATION(49, 3), RHODES(50, 3),
    //Battlemage T4
    AZURE_FURY(51, 4), SWORDOFLORD(43, 4),
    //Warlokc T4
    EMOTION(46, 4), LORD(47, 4),
    //Chaos T4
    STABILIZE(199, 4), MIND_CRASH(200, 4),

    //Rogue T1
    CACHED_RATIONS(64), THIEFS_INTUITION(65), SUCKER_PUNCH(66), PROTECTIVE_SHADOWS(67),
    //Rogue T2
    MYSTICAL_MEAL(68), TRACKER(80), WIDE_SEARCH(70), SILENT_STEPS(71), ROGUES_FORESIGHT(72),
    //Rogue T3
    ENHANCED_RINGS(73, 3), LIGHT_CLOAK(74, 3),
    //Assassin T3
    ENHANCED_LETHALITY(75, 3), ASSASSINS_REACH(76, 3), BOUNTY_HUNTER(77, 3),
    //Freerunner T3
    EVASIVE_ARMOR(78, 3), PROJECTILE_MOMENTUM(79, 3), SPEEDY_STEALTH(80, 3),
    //WILD T3
    WIND_SCAR(214, 3), OPPORTUNIST(215, 3), WIND_ROAD(116, 3),
    //Rogue T4
    ASSASSINSCREED(81, 3), CLOCK_UPGRADE(74, 3),
    //Assassin T4
    SWEEP(76, 4), HOWLING(75, 4),
    //Freerunner T4
    SPECIAL_OPERATIONS(83, 4), BLOODBATH_OPERATIONS(78, 4),
    // WILD T4
    SWORD_WIND_UPGRADE(216, 4), SHADOW_HUNTER(67, 4),

    //Huntress T1
    NATURES_BOUNTY(96), SURVIVALISTS_INTUITION(97), FOLLOWUP_STRIKE(98), NATURES_AID(99),
    //Huntress T2
    INVIGORATING_MEAL(100), RESTORED_NATURE(101), REJUVENATING_STEPS(102), BOMB_ARROW(103), DURABLE_PROJECTILES(104),
    //Huntress T3
    ARTS_FOCUS(105, 3), SEER_SHOT(106, 3),
    //Sniper T3
    FARSIGHT(107, 3), SHARPSHOOTER(108, 3), SHARED_UPGRADES(109, 3),
    //Warden T3
    DURABLE_TIPS(110, 3), BARKSKIN(111, 3), SAVIOR_PRAY(112, 3),
    //Stome T3
    SONG_OF_STOME(210, 3), ENERGY_STORAGE(211, 3), WIND_BLESSING(212, 3),
    //Huntress T4
    POINT_BLANK(106, 3), IMPROVED_CROSSBOW(113, 3), PUSH_ATTACK(98, 4),
    //Sniper T4
    SNIPING(115, 4),
    //Warden T4
    SAVIOR_BELIEF(116, 4),
    // Stome T4
    GALEFORCE(213, 4),

    //RoseCat T1
    LIGHTNESSMEAL(128), SMARTMEALS(129), GOODMEAT(130), NYANGING(131),
    //RoseCat T2
    FASTMEAL(132), RECOVERY_UPGRADE(133), DELICIOUS_FOOD(134), LOVEMEAT(135), POWERGEAR(136),
    //RoseCat T3
    MYWISH(137, 3), CUTLET(138, 3),
    //RoseCat De_T3
    FOCUSED_ATTACK(139, 3), PHYSICAL_ATTACK(140, 3), BATTLEFLOW(141, 3),
    //RoseCat Gu_T3
    BARRIER_OPERATION(142, 3), BARRIER_REPAIR(143, 3), RHODES_CAT(144, 3),
    //RoseCat WAR_T3
    OBLIVION(192, 3), WEAKNESS_COVER(193, 3), MENTALAMPLIFICATION(194, 3),
    //RossCat T4
    RHODES_WEAPON(145, 3), RECALL_MEMORY(146, 3), GUILT(137, 4),
    //RoseCat De_T4
    ESTHESIA(148, 4),
    //RoseCat Gu_T4
    SPEED_COMABT(147, 4),
    //RoseCat WAR_TR
    CRYSTALLIZE(195, 4),

    //Nearl T1
    SHINING_MEAL(160), EXPERIENCE(161), PROTECTIONOFLIGHT(162), KNIGTS_OATH(163),
    //Nearl T2
    COMBAT_MEAL(164), PUERLIGHT(165), RESURGENCE(166), PHASERUSH(167), EXORCISM(168),
    //Nearl T3
    PEGASUS_AURA(169, 3), RADIANTHERO(170, 3),
    //Nearl T3 - KNIGHT
    INTO_FRAY(171, 3), BLITZKRIEG(172, 3), SKILL_MASTERY(173, 3),
    //Nearl T3 - SAVIER
    SHIELD_OF_LIGHT(174, 3), PEGASUS_WING(175, 3), HOPELIGHT(176, 3),
    // Flash T3
    FLASH_SPEAR(206, 3), KNIGHT_GLORY(207, 3), LIGHT_OF_GLORY(208, 3),
    //Nearl T4
    GRAND_ORDER(177, 3), KNIGHT_OF_KAZIMIERZ(178, 3), KNIGHT_BODY(179, 4),
    //Nearl T4 - KNIGHT
    QUICK_TACTICS(180, 4),
    //Nearl T4 - SAVIER
    BLESSED_CHAMPION(181, 4),
    //Flast T4
    ETERNAL_GLORY(209, 4),

    //Chen T1
    PARING(288), POLICE_SENSE(289), CONTINUOUS_CUT(290), ZANTETSUKEN(291),
    //Chen T2
    LATENT_MEAL(292), SCOLDING(293), DRAGONS_SWORD(294), GALLOP(295), TARGET_FOCUSING(296),
    //Chen T3-SMASTER
    UP_SP1(297, 3), UP_SP2(298, 3), UP_SP3(299, 3), BLADE_ART(300, 3), DOUBLE_SWORD(301, 3),
    //Chen T3-SPSHOT
    GORGEOUS_VACATION(302, 3), TAC_DEF(303, 3), PINPOINT(304, 3), FRUGALITY(305, 3), WATER_PLAY(306, 3),
    //Chen T4-SMASTER
    UP_EX1(307, 4), UP_EX2(308, 4), UP_EX3(309, 4), AWAKE(310, 3),
    //Chen T4-SPSHOT
    TAC_SHOT(311, 4), TECHNICAL(312, 4), ZERO_RANGE_SHOT(313, 4), BF_RULL(314, 3);


    public static class ImprovisedProjectileCooldown extends FlavourBuff {
    }

    ;

    public static class LethalMomentumTracker extends FlavourBuff {
    }

    ;

    public static class WandPreservationCounter extends CounterBuff {
        {revivePersists = true;}
    }

    ;

    public static class EmpoweredStrikeTracker extends FlavourBuff {
    }

    ;

    public static class BountyHunterTracker extends FlavourBuff {
    }

    ;

    public static class SWEEPTraker extends FlavourBuff {
    }

    ;

    public static class RejuvenatingStepsCooldown extends FlavourBuff {
    }

    ;

    public static class bombshotooldown extends FlavourBuff {
    }

    ;

    public static class SeerShotCooldown extends FlavourBuff {
    }

    ;

    public static class SilShotCooldown extends FlavourBuff {
    }

    ;

    public static class PushAttackCooldown extends FlavourBuff {
    }

    ;

    public static class foodIdentify extends CounterBuff {
    }

    ;

    public static class BlazeBurstBuff extends CounterBuff {
    }

    ;

    public static class ScoldingCooldown extends FlavourBuff {
    }

    ;

    public static class GALLOPbuff extends Buff {
        {
            immunities.add(Cripple.class);
        }
    }

    public static class MindCrashMark extends FlavourBuff {
        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.MARK;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }

    ;

    public static class RadiantHeroCooldown extends FlavourBuff {
        @Override
        public int icon() {
            return BuffIndicator.COOL_TIME;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }

    ;

    public static class MyWishCooldown extends FlavourBuff {
        @Override
        public int icon() {
            return BuffIndicator.COOL_TIME;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }

    ;

    public static class LightClockCooldown extends FlavourBuff {
        @Override
        public int icon() {
            return BuffIndicator.COOL_TIME;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }
    }

    ;

    int icon;
    int maxPoints;

    // tiers 1/2/3/4 start at levels 2/7/13/21
    public static int[] tierLevelThresholds = new int[]{0, 2, 7, 13, 21, 29};

    Talent(int icon) {
        this(icon, 2);
    }

    Talent(int icon, int maxPoints) {
        this.icon = icon;
        this.maxPoints = maxPoints;
    }

    public int icon() {
        return icon;
    }

    public int maxPoints() {
        return maxPoints;
    }

    public String title() {
        return Messages.get(this, name() + ".title");
    }

    public String desc() {
        return Messages.get(this, name() + ".desc");
    }

    public static void onTalentUpgraded(Hero hero, Talent talent) {
        if (talent == GENIUS) {
            new ScrollOfIdentify().execute(hero);
        }

        if (talent == NATURES_BOUNTY) {
            if (hero.pointsInTalent(NATURES_BOUNTY) == 1)
                Buff.count(hero, NatureBerriesAvailable.class, 4);
            else Buff.count(hero, NatureBerriesAvailable.class, 2);
        }

        if (talent == ARMSMASTERS_INTUITION && hero.pointsInTalent(ARMSMASTERS_INTUITION) == 2) {
            if (hero.belongings.weapon != null) hero.belongings.weapon.identify();
            if (hero.belongings.armor != null) hero.belongings.armor.identify();
        }
        if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 2) {
            if (hero.belongings.ring instanceof Ring) hero.belongings.ring.identify();
            if (hero.belongings.misc instanceof Ring) hero.belongings.misc.identify();
            for (Item item : Dungeon.hero.belongings) {
                if (item instanceof Ring) {
                    ((Ring) item).setKnown();
                }
            }
        }
        if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 1) {
            if (hero.belongings.ring instanceof Ring) hero.belongings.ring.setKnown();
            if (hero.belongings.misc instanceof Ring) ((Ring) hero.belongings.misc).setKnown();
        }

        if (talent == LIBERATION && Dungeon.hero.belongings.getItem(MagesStaff.class) != null) {
            MagesStaff Item = Dungeon.hero.belongings.getItem(MagesStaff.class);
            Item.updateWand(false);
        }

        if (talent == FARSIGHT) {
            Dungeon.observe();
        }

        if (talent == EXPERIENCE && hero.pointsInTalent(EXPERIENCE) == 2) {
            for (Item item : Dungeon.hero.belongings) {
                if (item instanceof Armor) {
                    ((Armor) item).cursedKnown = true;
                }
            }
        }

        if (talent == POLICE_SENSE && hero.pointsInTalent(POLICE_SENSE) == 2) {
            for (Item item : Dungeon.hero.belongings) {
                if (item instanceof Weapon) {
                    ((Weapon) item).cursedKnown = true;
                }
            }
        }

        if (talent == GALLOP && hero.pointsInTalent(GALLOP) == 2) {
            Buff.affect(hero, GALLOPbuff.class);
        }

        if (talent == PROTECTIONOFLIGHT) {
            if (hero.pointsInTalent(PROTECTIONOFLIGHT) == 1) {
                Armor heroArmor = hero.belongings.armor;
                if (heroArmor != null) {
                    heroArmor.curseInfusionBonus = false;
                    ScrollOfRemoveCurse.uncurse(hero, heroArmor);
                }
            } else if (hero.pointsInTalent(PROTECTIONOFLIGHT) == 2) {
                Buff.affect(hero, ProtectionInsurance.class);
            }
        }

        if (talent == KNIGHT_BODY) {
            hero.updateHT(false);
        }
    }

    public static class CachedRationsDropped extends CounterBuff {
        {revivePersists = true;}
    }

    ;

    public static class NatureBerriesAvailable extends CounterBuff {
        {revivePersists = true;}
    }

    ;

    public static class ProtectionInsurance extends Buff {
    }

    ;

    public static void onFoodEaten(Hero hero, float foodVal, Item foodSource) {
        Quests.onFoodEaten(foodSource);
        if (hero.hasTalent(HEARTY_MEAL)) {
            //3/5 HP healed, when hero is below 25% health
            if (hero.HP <= hero.HT / 4) {
                hero.HP = Math.min(hero.HP + 1 + 2 * hero.pointsInTalent(HEARTY_MEAL), hero.HT);
                hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 + hero.pointsInTalent(HEARTY_MEAL));
                //2/3 HP healed, when hero is below 50% health
            } else if (hero.HP <= hero.HT / 2) {
                hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(HEARTY_MEAL), hero.HT);
                hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(HEARTY_MEAL));
            }
        }
        if (hero.hasTalent(IRON_STOMACH)) {
            if (hero.cooldown() > 0) {
                Buff.affect(hero, WarriorFoodImmunity.class, hero.cooldown());
            }
        }
        if (hero.hasTalent(EMPOWERING_MEAL)) {
            //2/3 bonus wand damage for next 3 zaps
            Buff.affect(hero, WandEmpower.class).set(2 + hero.pointsInTalent(EMPOWERING_MEAL), 3);
            ScrollOfRecharging.charge(hero);
        }
        if (hero.hasTalent(ENERGIZING_MEAL)) {
            //5/8 turns of recharging
            Buff.prolong(hero, Recharging.class, 2 + 3 * (hero.pointsInTalent(ENERGIZING_MEAL)));
            ScrollOfRecharging.charge(hero);
        }
        if (hero.hasTalent(MYSTICAL_MEAL)) {
            //3/5 turns of recharging
            Buff.affect(hero, ArtifactRecharge.class).set(1.5f * (hero.pointsInTalent(MYSTICAL_MEAL))).ignoreHornOfPlenty = true;
            ScrollOfRecharging.charge(hero);
        }
        if (hero.hasTalent(INVIGORATING_MEAL)) {
            //effectively 1/2 turns of haste
            Buff.prolong(hero, Haste.class, 0.67f + hero.pointsInTalent(INVIGORATING_MEAL));
        }

        if (hero.hasTalent(LIGHTNESSMEAL)) {
            if (foodSource instanceof HornOfPlenty == false) {
                Buff.prolong(hero, Levitation.class, 5 * hero.pointsInTalent(LIGHTNESSMEAL));
            }
        }

        if (hero.hasTalent(FASTMEAL)) {
            Buff.affect(hero, Barrier.class).incShield(hero.HT * hero.pointsInTalent(FASTMEAL) / 33);
        }

        if (hero.hasTalent(SHINING_MEAL)) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos] && !mob.properties().contains(Char.Property.NPC) && !(mob instanceof Shopkeeper)) {
                    Buff.affect(mob, Blindness.class, 1 + hero.pointsInTalent(SHINING_MEAL));
                    if (mob.properties().contains(Char.Property.INFECTED) || mob.properties().contains(Char.Property.SARKAZ)) {
                        mob.damage(3 + (hero.pointsInTalent(SHINING_MEAL) * 3), hero);
                    }
                }
            }
        }

        if (hero.hasTalent(COMBAT_MEAL)) {
            SealOfLight Seal = hero.belongings.getItem(SealOfLight.class);
            if (hero.belongings.getItem(SealOfLight.class) != null) {
                Seal.charge(hero, 1 + hero.pointsInTalent(COMBAT_MEAL) * 3);
            }
        }

        if (hero.hasTalent(LATENT_MEAL) && !(foodSource instanceof HornOfPlenty)) {
            Buff.affect(hero, Invisibility.class, hero.pointsInTalent(LATENT_MEAL));
        }

        if (hero.subClass == HeroSubClass.DESTROYER)
            Buff.affect(hero, Rose_Force.class, 10f);
    }

    public static class WarriorFoodImmunity extends FlavourBuff {
        {
            actPriority = HERO_PRIO + 1;
        }
    }

    public static float itemIDSpeedFactor(Hero hero, Item item) {
        // 1.75x/2.5x speed with huntress talent
        float factor = 1f + hero.pointsInTalent(SURVIVALISTS_INTUITION);

        // 2x/instant for Warrior (see onItemEquipped)
        if (item instanceof MeleeWeapon || item instanceof Armor) {
            factor *= 1f + hero.pointsInTalent(ARMSMASTERS_INTUITION) * 2;
        }
        // 3x/instant for mage (see Wand.wandUsed())
        if (item instanceof Wand) {
            factor *= 1f + 2 * hero.pointsInTalent(SCHOLARS_INTUITION);
        }
        // 2x/instant for rogue (see onItemEqupped), also id's type on equip/on pickup
        if (item instanceof Ring) {
            factor *= 1f + hero.pointsInTalent(THIEFS_INTUITION);
        }

        //니어전용 감정특
        if (item instanceof Armor) {
            factor *= 1f + hero.pointsInTalent(EXPERIENCE) * 2;
        }

        // 첸
        if (item instanceof Weapon) {
            factor *= 1f + hero.pointsInTalent(EXPERIENCE) * 2;
        }
        return factor;
    }

    public static void onHealingPotionUsed(Hero hero) {
        if (hero.hasTalent(RESTORED_WILLPOWER)) {
            BrokenSeal.WarriorShield shield = hero.buff(BrokenSeal.WarriorShield.class);
            if (shield != null) {
                int shieldToGive = Math.round(shield.maxShield() * 0.33f * (1 + hero.pointsInTalent(RESTORED_WILLPOWER)));
                shield.supercharge(shieldToGive);
            }
            Buff.detach(hero, Burning.class);
        }
        if (hero.hasTalent(RESTORED_NATURE)) {
            ArrayList<Integer> grassCells = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8) {
                grassCells.add(hero.pos + i);
            }
            Random.shuffle(grassCells);
            for (int cell : grassCells) {
                Char ch = Actor.findChar(cell);
                if (ch != null) {
                    Buff.affect(ch, Roots.class, 1f + hero.pointsInTalent(RESTORED_NATURE));
                }
                if (Dungeon.level.map[cell] == Terrain.EMPTY ||
                        Dungeon.level.map[cell] == Terrain.EMBERS ||
                        Dungeon.level.map[cell] == Terrain.EMPTY_DECO) {
                    Level.set(cell, Terrain.GRASS);
                    GameScene.updateMap(cell);
                }
                CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
            }
            if (hero.pointsInTalent(RESTORED_NATURE) == 1) {
                grassCells.remove(0);
                grassCells.remove(0);
                grassCells.remove(0);
            }
            for (int cell : grassCells) {
                int t = Dungeon.level.map[cell];
                if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
                        || t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
                        && Dungeon.level.plants.get(cell) == null) {
                    Level.set(cell, Terrain.HIGH_GRASS);
                    GameScene.updateMap(cell);
                }
            }
            Dungeon.observe();
        }
    }

    public static void onUpgradeScrollUsed(Hero hero) {
        if (hero.hasTalent(RECOVERY_UPGRADE)) {
            AnnihilationGear Gear = hero.belongings.getItem(AnnihilationGear.class);
            if (hero.belongings.getItem(AnnihilationGear.class) != null) {
                Gear.SPCharge(hero.pointsInTalent(RECOVERY_UPGRADE) * 2);
            }
        }
    }

    public static void onArtifactUsed(Hero hero) {
        if (hero.hasTalent(ENHANCED_RINGS)) {
            Buff.prolong(hero, EnhancedRings.class, 2.5f * hero.pointsInTalent(ENHANCED_RINGS));
        }
    }


    public static void onSkillUsed(Hero hero) {
        if (hero.hasTalent(ENERGIZING_UPGRADE)) {
            MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
            if (staff != null) {
                staff.gainCharge(hero.pointsInTalent(ENERGIZING_UPGRADE), true);
                ScrollOfRecharging.charge(Dungeon.hero);
                SpellSprite.show(hero, SpellSprite.CHARGE);
            }
        }

        if (hero.hasTalent(TRACKER)) {
            Buff.affect(hero, Haste.class, hero.pointsInTalent(TRACKER) - 1);
            Buff.detach(hero, Cripple.class);
            Buff.detach(hero, Blindness.class);
            Buff.detach(hero, Roots.class);
        }

        if (hero.hasTalent(PUERLIGHT)) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                    Ballistica trajectory = new Ballistica(hero.pos, mob.pos, Ballistica.STOP_TARGET);
                    trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                    WandOfBlastWave.throwChar(mob, trajectory, hero.pointsInTalent(PUERLIGHT)); // 넉백 효과
                    Buff.affect(mob, Blindness.class, 1f + hero.pointsInTalent(PUERLIGHT));
                }
            }
            Buff.affect(hero, Light.class, hero.pointsInTalent(PUERLIGHT) * 50);
        }
    }

    public static void onItemEquipped(Hero hero, Item item) {
        if (hero.pointsInTalent(ARMSMASTERS_INTUITION) == 2 && (item instanceof Weapon || item instanceof Armor)) {
            item.identify();
        }
        if (hero.hasTalent(THIEFS_INTUITION) && item instanceof Ring) {
            if (hero.pointsInTalent(THIEFS_INTUITION) == 2) {
                item.identify();
            } else {
                ((Ring) item).setKnown();
            }
        }
        // Protection of Light insurance: auto-uncurse the next cursed equipment equipped
        if (hero.buff(ProtectionInsurance.class) != null && item instanceof EquipableItem && item.cursed) {
            if (item instanceof Armor) ((Armor) item).curseInfusionBonus = false;
            ScrollOfRemoveCurse.uncurse(hero, item);
            Buff.detach(hero, ProtectionInsurance.class);
            GLog.p(Messages.get(ProtectionInsurance.class, "proc"));
        }

    }

    public static void onItemCollected(Hero hero, Item item) {
        if (hero.pointsInTalent(THIEFS_INTUITION) == 2) {
            if (item instanceof Ring) ((Ring) item).setKnown();
        }
    }

    //note that IDing can happen in alchemy scene, so be careful with VFX here
    public static void onItemIdentified(Hero hero, Item item) {
        if (hero.hasTalent(TEST_SUBJECT)) {
            //heal for 4 / 6
            hero.HP = Math.min(hero.HP + 2 + (hero.pointsInTalent(TEST_SUBJECT) * 2), hero.HT);
            Emitter e = hero.sprite.emitter();
            if (e != null) e.burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(TEST_SUBJECT));
        }
        if (hero.hasTalent(TESTED_HYPOTHESIS)) {
            //2/3 turns of wand recharging
            Buff.affect(hero, Recharging.class, 3f + hero.pointsInTalent(TESTED_HYPOTHESIS));
            ScrollOfRecharging.charge(hero);
        }

        if (hero.hasTalent(NYANGING)) {
            AnnihilationGear Gear = hero.belongings.getItem(AnnihilationGear.class);
            if (Gear != null) {
                Gear.charge = Math.min(hero.pointsInTalent(NYANGING) + Gear.charge, Gear.chargeCap + 4);
                Gear.updateQuickslot();
            }
        }

        if (hero.hasTalent(KNIGTS_OATH)) {
            SealOfLight Seal = hero.belongings.getItem(SealOfLight.class);
            if (hero.belongings.getItem(SealOfLight.class) != null) {
                Seal.charge(hero, 5 + hero.pointsInTalent(KNIGTS_OATH) * 5);
                Seal.updateQuickslot();
            }
        }
    }

    public static int onAttackProc(Hero hero, Char enemy, int dmg) {
        if (hero.hasTalent(Talent.ASSASSINSCREED)
                && enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
                && enemy.buff(SuckerPunchTracker.class) == null) {
            dmg *= 1f + hero.pointsInTalent(Talent.ASSASSINSCREED) * 0.05;
            Buff.affect(enemy, SuckerPunchTracker.class);
        }

        if (hero.hasTalent(Talent.SUCKER_PUNCH)
                && enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
                && enemy.buff(SuckerPunchTracker.class) == null) {
            dmg += Random.IntRange(hero.pointsInTalent(Talent.SUCKER_PUNCH), 3);
            Buff.affect(enemy, SuckerPunchTracker.class);
        }

        if (hero.hasTalent(Talent.SHADOW_HUNTER)
                && enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
            if (Random.Int(20) < hero.pointsInTalent(SHADOW_HUNTER)) {
                WildMark mark = hero.buff(WildMark.class);
                if (mark != null) mark.gainCharge();
            }
        }

        if (hero.hasTalent(Talent.FOLLOWUP_STRIKE)) {
            if (hero.belongings.attackingWeapon() instanceof MissileWeapon
                    || hero.buff(GunWeapon.RangedAttackTracker.class) != null) {
                Buff.affect(enemy, FollowupStrikeTracker.class);
            } else if (enemy.buff(FollowupStrikeTracker.class) != null) {
                dmg += 1 + hero.pointsInTalent(FOLLOWUP_STRIKE);
                if (!(enemy instanceof Mob) || !((Mob) enemy).surprisedBy(hero)) {
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
                }
                enemy.buff(FollowupStrikeTracker.class).detach();
            }
        }

        return dmg;
    }

    public static class SuckerPunchTracker extends Buff {
    }

    ;

    public static class FollowupStrikeTracker extends Buff {
    }

    ;

    public static final int MAX_TALENT_TIERS = 4;

    public static void initClassTalents(Hero hero) {
        initClassTalents(hero.heroClass, hero.talents);
    }

    public static void initClassTalents(HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents) {
        while (talents.size() < MAX_TALENT_TIERS) {
            talents.add(new LinkedHashMap<>());
        }

        ArrayList<Talent> tierTalents = new ArrayList<>();

        //tier 1
        switch (cls) {
            case WARRIOR:
            default:
                Collections.addAll(tierTalents, HEARTY_MEAL, ARMSMASTERS_INTUITION, TEST_SUBJECT, IRON_WILL, GENIUS);
                break;
            case MAGE:
                Collections.addAll(tierTalents, EMPOWERING_MEAL, SCHOLARS_INTUITION, TESTED_HYPOTHESIS, BACKUP_BARRIER, GENIUS);
                break;
            case ROGUE:
                Collections.addAll(tierTalents, CACHED_RATIONS, THIEFS_INTUITION, SUCKER_PUNCH, PROTECTIVE_SHADOWS, GENIUS);
                break;
            case HUNTRESS:
                Collections.addAll(tierTalents, NATURES_BOUNTY, SURVIVALISTS_INTUITION, FOLLOWUP_STRIKE, NATURES_AID, GENIUS);
                break;
            case ROSECAT:
                Collections.addAll(tierTalents, LIGHTNESSMEAL, SMARTMEALS, GOODMEAT, NYANGING, GENIUS);
                break;
            case NEARL:
                Collections.addAll(tierTalents, SHINING_MEAL, EXPERIENCE, PROTECTIONOFLIGHT, KNIGTS_OATH, GENIUS);
                break;
            case CHEN:
                Collections.addAll(tierTalents, PARING, POLICE_SENSE, CONTINUOUS_CUT, ZANTETSUKEN, GENIUS);
                break;
        }
        for (Talent talent : tierTalents) {
            talents.get(0).put(talent, 0);
        }
        tierTalents.clear();

        //tier 2
        switch (cls) {
            case WARRIOR:
            default:
                Collections.addAll(tierTalents, IRON_STOMACH, RESTORED_WILLPOWER, RUNIC_TRANSFERENCE, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES);
                break;
            case MAGE:
                Collections.addAll(tierTalents, ENERGIZING_MEAL, ENERGIZING_UPGRADE, WAND_PRESERVATION, ARCANE_SNIPE, SHIELD_BATTERY);
                break;
            case ROGUE:
                Collections.addAll(tierTalents, MYSTICAL_MEAL, TRACKER, WIDE_SEARCH, SILENT_STEPS, ROGUES_FORESIGHT);
                break;
            case HUNTRESS:
                Collections.addAll(tierTalents, INVIGORATING_MEAL, RESTORED_NATURE, REJUVENATING_STEPS, BOMB_ARROW, DURABLE_PROJECTILES);
                break;
            case ROSECAT:
                Collections.addAll(tierTalents, FASTMEAL, RECOVERY_UPGRADE, DELICIOUS_FOOD, LOVEMEAT, POWERGEAR);
                break;
            case NEARL:
                Collections.addAll(tierTalents, COMBAT_MEAL, PUERLIGHT, RESURGENCE, PHASERUSH, EXORCISM);
                break;
            case CHEN:
                Collections.addAll(tierTalents, LATENT_MEAL, SCOLDING, DRAGONS_SWORD, GALLOP, TARGET_FOCUSING);
                break;
        }
        for (Talent talent : tierTalents) {
            talents.get(1).put(talent, 0);
        }
        tierTalents.clear();

        //tier 3
        switch (cls) {
            case WARRIOR:
            default:
                Collections.addAll(tierTalents, HOLD_FAST, STRONGMAN);
                break;
            case MAGE:
                Collections.addAll(tierTalents, PSYCHOANALYSIS, ALLY_WARP);
                break;
            case ROGUE:
                Collections.addAll(tierTalents, ENHANCED_RINGS, LIGHT_CLOAK);
                break;
            case HUNTRESS:
                Collections.addAll(tierTalents, ARTS_FOCUS, SEER_SHOT);
                break;
            case ROSECAT:
                Collections.addAll(tierTalents, MYWISH, CUTLET);
                break;
            case NEARL:
                Collections.addAll(tierTalents, PEGASUS_AURA, RADIANTHERO);
                break;
            case CHEN:
                Collections.addAll(tierTalents);
                break;
        }
        for (Talent talent : tierTalents) {
            talents.get(2).put(talent, 0);
        }
        tierTalents.clear();

        //tier4
        switch (cls) {
            case WARRIOR:
            default:
                Collections.addAll(tierTalents, TACTICAL_SHIELD, CHAINSAW_EXTEND);
                break;
            case MAGE:
                Collections.addAll(tierTalents, LIBERATION, RHODES);
                break;
            case ROGUE:
                Collections.addAll(tierTalents, ASSASSINSCREED, CLOCK_UPGRADE);
                break;
            case HUNTRESS:
                Collections.addAll(tierTalents, POINT_BLANK, IMPROVED_CROSSBOW, PUSH_ATTACK);
                break;
            case ROSECAT:
                Collections.addAll(tierTalents, RHODES_WEAPON, RECALL_MEMORY, GUILT);
                break;
            case NEARL:
                Collections.addAll(tierTalents, GRAND_ORDER, KNIGHT_OF_KAZIMIERZ, KNIGHT_BODY);
                break;
            case CHEN:
                Collections.addAll(tierTalents);
                break;
        }
        for (Talent talent : tierTalents) {
            talents.get(3).put(talent, 0);
        }
        tierTalents.clear();
    }

    public static void initSubclassTalents(Hero hero) {
        initSubclassTalents(hero.subClass, hero.talents);
    }

    public static void initSubclassTalents(HeroSubClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents) {
        if (cls == HeroSubClass.NONE) return;

        while (talents.size() < MAX_TALENT_TIERS) {
            talents.add(new LinkedHashMap<>());
        }

        ArrayList<Talent> tierTalents = new ArrayList<>();

        //tier 3
        switch (cls) {
            case BERSERKER:
            default:
                Collections.addAll(tierTalents, ENDLESS_RAGE, BERSERKING_STAMINA, ENRAGED_CATALYST);
                break;
            case GLADIATOR:
                Collections.addAll(tierTalents, CLEAVE, FINALBLOW, ENHANCED_COMBO);
                break;
            case HEAT:
                Collections.addAll(tierTalents, HEAT_BLOW, HEAT_OF_PROTECTION, HEAT_OF_RECOVERY);
                break;
            case BATTLEMAGE:
                Collections.addAll(tierTalents, EMPOWERED_STRIKE, MYSTICAL_CHARGE, EXCESS_CHARGE);
                break;
            case WARLOCK:
                Collections.addAll(tierTalents, SOUL_EATER, SOUL_SIPHON, NECROMANCERS_MINIONS);
                break;
            case CHAOS:
                Collections.addAll(tierTalents, CHIMERA, RESTRICTION, EMOTION_ABSORPTION);
                break;
            case ASSASSIN:
                Collections.addAll(tierTalents, ENHANCED_LETHALITY, ASSASSINS_REACH, BOUNTY_HUNTER);
                break;
            case FREERUNNER:
                Collections.addAll(tierTalents, EVASIVE_ARMOR, PROJECTILE_MOMENTUM, SPEEDY_STEALTH);
                break;
            case WILD:
                Collections.addAll(tierTalents, WIND_SCAR, OPPORTUNIST, WIND_ROAD);
                break;
            case SNIPER:
                Collections.addAll(tierTalents, FARSIGHT, SHARPSHOOTER, SHARED_UPGRADES);
                break;
            case WARDEN:
                Collections.addAll(tierTalents, DURABLE_TIPS, BARKSKIN, SAVIOR_PRAY);
                break;
            case STOME:
                Collections.addAll(tierTalents, SONG_OF_STOME, ENERGY_STORAGE, WIND_BLESSING);
                break;
            case DESTROYER:
                Collections.addAll(tierTalents, FOCUSED_ATTACK, PHYSICAL_ATTACK, BATTLEFLOW);
                break;
            case GUARDIAN:
                Collections.addAll(tierTalents, BARRIER_OPERATION, BARRIER_REPAIR, RHODES_CAT);
                break;
            case WAR:
                Collections.addAll(tierTalents, OBLIVION, WEAKNESS_COVER, MENTALAMPLIFICATION);
                break;
            case KNIGHT:
                Collections.addAll(tierTalents, INTO_FRAY, BLITZKRIEG, SKILL_MASTERY);
                break;
            case SAVIOR:
                Collections.addAll(tierTalents, SHIELD_OF_LIGHT, PEGASUS_WING, HOPELIGHT);
                break;
            case FLASH:
                Collections.addAll(tierTalents, FLASH_SPEAR, KNIGHT_GLORY, LIGHT_OF_GLORY);
                break;
            case SWORDMASTER:
                Collections.addAll(tierTalents, UP_SP1, UP_SP2, UP_SP3, BLADE_ART, DOUBLE_SWORD);
                break;
            case SPSHOOTER:
                Collections.addAll(tierTalents, GORGEOUS_VACATION, TAC_DEF, PINPOINT, FRUGALITY, WATER_PLAY);
                break;
        }
        for (Talent talent : tierTalents) {
            talents.get(2).put(talent, 0);
        }
        tierTalents.clear();

        //tier4
        switch (cls) {
            case BERSERKER:
            default:
                Collections.addAll(tierTalents, INFINITE_RAGE, INFINITE_BATTLE);
                break;
            case GLADIATOR:
                Collections.addAll(tierTalents, SPARKOFLIFE, DEADLY_REACH);
                break;
            case HEAT:
                Collections.addAll(tierTalents, HEAT_OF_ABSORPTION, REDCOMET);
                break;
            case BATTLEMAGE:
                Collections.addAll(tierTalents, AZURE_FURY, SWORDOFLORD);
                break;
            case WARLOCK:
                Collections.addAll(tierTalents, EMOTION, LORD);
                break;
            case CHAOS:
                Collections.addAll(tierTalents, STABILIZE, MIND_CRASH);
                break;
            case ASSASSIN:
                Collections.addAll(tierTalents, SWEEP, HOWLING);
                break;
            case FREERUNNER:
                Collections.addAll(tierTalents, SPECIAL_OPERATIONS, BLOODBATH_OPERATIONS);
                break;
            case WILD:
                Collections.addAll(tierTalents, SWORD_WIND_UPGRADE, SHADOW_HUNTER);
                break;
            case SNIPER:
                Collections.addAll(tierTalents, SNIPING);
                break;
            case WARDEN:
                Collections.addAll(tierTalents, SAVIOR_BELIEF);
                break;
            case STOME:
                Collections.addAll(tierTalents, GALEFORCE);
                break;
            case DESTROYER:
                Collections.addAll(tierTalents, ESTHESIA);
                break;
            case GUARDIAN:
                Collections.addAll(tierTalents, SPEED_COMABT);
                break;
            case WAR:
                Collections.addAll(tierTalents, CRYSTALLIZE);
                break;
            case KNIGHT:
                Collections.addAll(tierTalents, QUICK_TACTICS);
                break;
            case SAVIOR:
                Collections.addAll(tierTalents, BLESSED_CHAMPION);
                break;
            case FLASH:
                Collections.addAll(tierTalents, ETERNAL_GLORY);
                break;
            case SWORDMASTER:
                Collections.addAll(tierTalents, UP_EX1, UP_EX2, UP_EX3, AWAKE);
                break;
            case SPSHOOTER:
                Collections.addAll(tierTalents, TAC_SHOT, TECHNICAL, ZERO_RANGE_SHOT, BF_RULL);
                break;
        }
        for (Talent talent : tierTalents) {
            talents.get(3).put(talent, 0);
        }
        tierTalents.clear();
    }

    private static final String TALENT_TIER = "talents_tier_";

    public static void storeTalentsInBundle(Bundle bundle, Hero hero) {
        for (int i = 0; i < MAX_TALENT_TIERS; i++) {
            LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
            Bundle tierBundle = new Bundle();

            for (Talent talent : tier.keySet()) {
                if (tier.get(talent) > 0) {
                    tierBundle.put(talent.name(), tier.get(talent));
                }
                if (tierBundle.contains(talent.name())) {
                    tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
                }
            }
            bundle.put(TALENT_TIER + (i + 1), tierBundle);
        }
    }

    public static void restoreTalentsFromBundle(Bundle bundle, Hero hero) {
        if (hero.heroClass != null) initClassTalents(hero);
        if (hero.subClass != null) initSubclassTalents(hero);

        for (int i = 0; i < MAX_TALENT_TIERS; i++) {
            LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
            Bundle tierBundle = bundle.contains(TALENT_TIER + (i + 1)) ? bundle.getBundle(TALENT_TIER + (i + 1)) : null;
            //pre-0.9.1 saves
            if (tierBundle == null && i == 0 && bundle.contains("talents")) {
                tierBundle = bundle.getBundle("talents");
            }

            if (tierBundle != null) {
                for (Talent talent : tier.keySet()) {
                    if (tierBundle.contains(talent.name())) {
                        tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
                    }
                }
            }
        }
    }

}
