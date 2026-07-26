/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.BloodMagister;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Centurion;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.EmperorPursuer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Faust;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Kaltsit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.MagicGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Mon3tr;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Sentinel;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Shadow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.TheEndspeaker;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blackperro;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ceylon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Closure;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dario;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DeliveryDrone;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dobermann;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dummy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Firewall;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FrostLeaf;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Gavial;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GreenCat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Jessica;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.LANCET2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Lens;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MiniShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Gglow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Irene;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Mage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Phantom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_PhantomShadow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Pilot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Shu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Npc_Astesia;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PRTS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PRTS_corpse;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Purestream;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SeaObject;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SkinModel;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Weedy;
import com.shatteredpixel.shatteredpixeldungeon.items.AnnihilationGear;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.AncientKin;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.YourWish;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfMayer;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfMudrock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wrench;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CatGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CrabGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Echeveria;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ImageoverForm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KazemaruWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.HallucinationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OblivionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OriginiumTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TenguDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Dreamfoil;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

//contains all the game's various entities, mostly enemies, NPCS, and allies, but also traps and plants
public enum Bestiary {

    REGIONAL,
    BOSSES,
    UNIVERSAL,
    RARE,
    QUEST,
    NEUTRAL,
    ALLY,
    TRAP,
    PLANT;

    //tracks whether an entity has been encountered
    private final LinkedHashMap<Class<?>, Boolean> seen = new LinkedHashMap<>();
    //tracks enemy kills, trap activations, plant tramples, or just sets to 1 for seen on allies
    private final LinkedHashMap<Class<?>, Integer> encounterCount = new LinkedHashMap<>();

    //should only be used when initializing
    private void addEntities(Class<?>... classes) {
        for (Class<?> cls : classes) {
            if (cls == null) continue;
            seen.put(cls, false);
            encounterCount.put(cls, 0);
        }
    }

    public Collection<Class<?>> entities() {
        return seen.keySet();
    }

    public String title() {
        return Messages.get(this, name() + ".title");
    }

    public int totalEntities() {
        return seen.size();
    }

    public int totalSeen() {
        int seenTotal = 0;
        for (boolean entitySeen : seen.values()) {
            if (entitySeen) seenTotal++;
        }
        return seenTotal;
    }

    static {

        REGIONAL.addEntities(
                //Sewers
                Slug.class, Snake.class, Gnoll.class, Swarm.class, Hound.class, Slime.class,
                //Prison
                Skeleton.class, Thief.class, AirborneSoldier.class, DM100.class, Guard.class, Necromancer.class,
                //Caves
                Bat.class, Brute.class, Shaman.RedShaman.class, Shaman.BlueShaman.class, Shaman.PurpleShaman.class,
                ExplodSlug_N.class, MudrockZealot.class, DM200.class,
                //City
                Ghoul.class, Elemental.FireElemental.class, Elemental.FrostElemental.class, Elemental.ShockElemental.class,
                Warlock.class, Monk.class, Golem.class,
                //Halls
                Succubus.class, Striker.class, Eye.class, Scorpio.class,
                //Siesta (sealed) standard branch
                Infantry.class, Ergate.class, Piersailor.class, Sniper.class, Agent.class,
                LavaSlug.class, MetalCrab.class, Rockbreaker.class, ExplodeSlug_A.class, AcidSlug_A.class,
                //Gavial branch
                TiacauhWarrior.class, TiacauhFanatic.class, TiacauhLancer.class, TiacauhAddict.class,
                TiacauhRipper.class, TiacauhShredder.class, TiacauhRitualist.class, TiacauhBrave.class,
                GiantMushroom.class,
                //Sea branch
                SeaRunner.class, FloatingSeaDrifter.class, SeaReaper.class, SeaCapsule.class,
                Sea_Octo.class, SeaLeef.class, NetherseaBrandguider.class);

        BOSSES.addEntities(Goo.class,
                NewTengu.class,
                Pylon.class, NewDM300.class,
                DwarfKing.class,
                Yog.Larva.class,
                YogFist.BurningFist.class, YogFist.SoiledFist.class, YogFist.RottingFist.class,
                YogFist.RustedFist.class, YogFist.BrightFist.class, YogFist.DarkFist.class,
                Yog.class,
                SiestaBoss.class, SeaBoss1.class,
                //mod minibosses (miniboss/ subpackage)
                Faust.class, Shadow.class, MagicGolem.class, Mon3tr.class,
                Sentinel.class, Kaltsit.class, BloodMagister.class, Centurion.class,
                EmperorPursuer.class, TheEndspeaker.class,
                //mod story bosses
                Talulah.class, Talu_BlackSnake.class, Tomimi.class,
                Isharmla.class, Eunectes.class, Schwarz.class,
                //mod miscellaneous bosses/minibosses
                Cannot.class, DemonSpawner.class, FireCore.class,
                ReunionDefender.class, SandPillar.class,
                Pompeii.class, TheBigUglyThing.class);

        UNIVERSAL.addEntities(Wraith.class, Fanatic.class, Piranha.class,
                Mimic.class, GoldenMimic.class, CrystalMimic.class,
                Statue.class, ArmoredStatue.class,
                GuardianTrap.Guardian.class);

        RARE.addEntities(Albino.class, CausticSlime.class,
                Bandit.class,
                ArmoredBrute.class, DM201.class,
                Senior.class, EmpireDrone.class,
                Raider.class, StrikerElite.class, Acidic.class,
                HeavyBoat.class, WaveCaster.class,
                MutantSpider.class, Originiutant.class,
                TiacauhSniper.class, TiacauhShaman.class,
                Crownslayer_shadow.class,
                Elemental.ChaosElemental.class, RipperDemon.class);

        QUEST.addEntities(FetidSlug.class, GnollTrickster.class,
                Elemental.NewbornFireElemental.class, RotLasher.class, RotHeart.class);

        NEUTRAL.addEntities(Ghost.class, RatKing.class, LANCET2.class,
                Shopkeeper.class, ImpShopkeeper.class, MiniShopkeeper.class,
                Wandmaker.class, Blacksmith.class, Imp.class, Sheep.class, Bee.class,
                //Rhodes Island story NPCs
                Blackperro.class, Ceylon.class, Closure.class, Dario.class, Dobermann.class,
                Dummy.class, Firewall.class, FrostLeaf.class, Gavial.class, GreenCat.class,
                Jessica.class, NPC_Gglow.class, NPC_Irene.class, NPC_Phantom.class,
                NPC_PhantomShadow.class, NPC_Pilot.class, Npc_Astesia.class,
                PRTS.class, PRTS_corpse.class, Weedy.class,
                NPC_Shu.class, NPC_Guard.class, NPC_Mage.class, Purestream.class,
                SkinModel.class, DeliveryDrone.class, SeaObject.class);

        ALLY.addEntities(MirrorImage.class, PrismaticImage.class,
                DriedRose.GhostHero.class,
                WandOfWarding.Ward.class,
                WandOfLivingEarth.EarthGuardian.class,
                Wraith_donut.class, Lens.class,
                //mod staff/wand summons
                StaffOfMayer.Ward.class, StaffOfMudrock.EarthGuardian.class,
                //mod weapon-triggered summons
                CatGun.Mon3tr.class, CrabGun.MetalCrab.class,
                Echeveria.PinkdogDrone.class, ImageoverForm.LittleInstinct.class,
                KazemaruWeapon.KazemaruSummon.class, AnnihilationGear.EX44.class,
                //mod skill summons
                AncientKin.Seaborn.class, YourWish.EX43.class,
                //mod wand summons
                Wrench.Sentry.class);

        TRAP.addEntities(WornDartTrap.class, PoisonDartTrap.class, DisintegrationTrap.class,
                ChillingTrap.class, BurningTrap.class, ShockingTrap.class, AlarmTrap.class, GrippingTrap.class,
                TeleportationTrap.class, OozeTrap.class,
                FrostTrap.class, BlazingTrap.class, StormTrap.class, GuardianTrap.class, FlashingTrap.class, WarpingTrap.class,
                ConfusionTrap.class, ToxicTrap.class, CorrosionTrap.class,
                FlockTrap.class, SummoningTrap.class, WeakeningTrap.class, CursingTrap.class,
                ExplosiveTrap.class, RockfallTrap.class, PitfallTrap.class,
                DistortionTrap.class, DisarmingTrap.class, GrimTrap.class,
                HallucinationTrap.class, OblivionTrap.class, OriginiumTrap.class);

        PLANT.addEntities(Rotberry.class, Sungrass.class, Fadeleaf.class, Icecap.class,
                Firebloom.class, Sorrowmoss.class, Swiftthistle.class, Blindweed.class,
                Stormvine.class, Earthroot.class, Dreamfoil.class, Starflower.class,
                BlandfruitBush.class,
                WandOfRegrowth.Dewcatcher.class, WandOfRegrowth.Seedpod.class, WandOfRegrowth.Lotus.class);

    }

    //some mobs and traps have different internal classes in some cases, so need to convert here
    private static final HashMap<Class<?>, Class<?>> classConversions = new HashMap<>();

    static {
        classConversions.put(Necromancer.NecroSkeleton.class, Skeleton.class);

        classConversions.put(TenguDartTrap.class, PoisonDartTrap.class);

        classConversions.put(DwarfKing.DKGhoul.class, Ghoul.class);
        classConversions.put(DwarfKing.DKWarlock.class, Warlock.class);
        classConversions.put(DwarfKing.DKMonk.class, Monk.class);

        //mod boss segment / minion conversions
        classConversions.put(IsharmlaSeabornHead.class, Isharmla.class);
        classConversions.put(IsharmlaSeabornBody.class, Isharmla.class);
        classConversions.put(IsharmlaSeabornTail.class, Isharmla.class);

        classConversions.put(Centurion.CenturionMinion.class, Centurion.class);

        classConversions.put(Tomimi.TomimiWarrior.class, Tomimi.class);
        classConversions.put(Tomimi.TomimiLancer.class, Tomimi.class);
        classConversions.put(Tomimi.TomimiFanatic.class, Tomimi.class);
        classConversions.put(Tomimi.TomimiTower.class, Tomimi.class);

        //Yog inner-class fists (legacy classes spawned during the Yog fight)
        classConversions.put(Yog.BurningFist.class, YogFist.BurningFist.class);
        classConversions.put(Yog.RottingFist.class, YogFist.RottingFist.class);

        //Talulah summons
        classConversions.put(Talulah.InfectedPatrolCaptain.class, Talulah.class);
        classConversions.put(Talulah.YogRipper.class, Talulah.class);

        //Isharmla summons
        classConversions.put(Isharmla.SummonRunner.class, Isharmla.class);
        classConversions.put(Isharmla.SummonOcto.class, Isharmla.class);
        classConversions.put(Isharmla.SummonLeef.class, Isharmla.class);

        //SiestaBoss summons
        classConversions.put(SiestaBoss.BossAgent.class, SiestaBoss.class);

        //TheBigUglyThing / Pompeii boss reskins of existing regular enemies
        classConversions.put(TheBigUglyThing.BossRipper.class, TiacauhRipper.class);
        classConversions.put(TheBigUglyThing.BossBrave.class, TiacauhBrave.class);
        classConversions.put(Pompeii.BossSlug.class, LavaSlug.class);
    }

    public static boolean isSeen(Class<?> cls) {
        for (Bestiary cat : values()) {
            if (cat.seen.containsKey(cls)) {
                return cat.seen.get(cls);
            }
        }
        return false;
    }

    public static void setSeen(Class<?> cls) {
        if (classConversions.containsKey(cls)) {
            cls = classConversions.get(cls);
        }
        for (Bestiary cat : values()) {
            if (cat.seen.containsKey(cls) && !cat.seen.get(cls)) {
                cat.seen.put(cls, true);
                Journal.saveNeeded = true;
            }
        }
        Badges.validateCatalogBadges();
    }

    public static int encounterCount(Class<?> cls) {
        for (Bestiary cat : values()) {
            if (cat.encounterCount.containsKey(cls)) {
                return cat.encounterCount.get(cls);
            }
        }
        return 0;
    }

    //debug-only: marks every entity in every category as seen, for testing the journal UI
    public static void debugRevealAll(){
        if (!DeviceCompat.isDebug()) return;
        for (Bestiary cat : values()){
            for (Class<?> cls : cat.entities()){
                cat.seen.put(cls, true);
            }
        }
        Badges.validateCatalogBadges();
        Journal.saveNeeded = true;
        Journal.saveGlobal();
    }

    //debug-only: clears seen/encounter-count state across all categories, for testing the journal UI
    public static void debugReset(){
        if (!DeviceCompat.isDebug()) return;
        for (Bestiary cat : values()){
            for (Class<?> cls : cat.entities()){
                cat.seen.put(cls, false);
                cat.encounterCount.put(cls, 0);
            }
        }
        Journal.saveNeeded = true;
        Journal.saveGlobal();
    }

    //used primarily when bosses are killed and need to clean up their minions
    public static boolean skipCountingEncounters = false;

    public static void countEncounter(Class<?> cls) {
        countEncounters(cls, 1);
    }

    public static void countEncounters(Class<?> cls, int encounters) {
        if (skipCountingEncounters) {
            return;
        }
        if (classConversions.containsKey(cls)) {
            cls = classConversions.get(cls);
        }
        for (Bestiary cat : values()) {
            if (cat.encounterCount.containsKey(cls) && cat.encounterCount.get(cls) != Integer.MAX_VALUE) {
                cat.encounterCount.put(cls, cat.encounterCount.get(cls) + encounters);
                if (cat.encounterCount.get(cls) < -1_000_000_000) { //to catch cases of overflow
                    cat.encounterCount.put(cls, Integer.MAX_VALUE);
                }
                Journal.saveNeeded = true;
            }
        }
    }

    private static final String BESTIARY_CLASSES = "bestiary_classes";
    private static final String BESTIARY_SEEN = "bestiary_seen";
    private static final String BESTIARY_ENCOUNTERS = "bestiary_encounters";

    public static void store(Bundle bundle) {

        ArrayList<Class<?>> classes = new ArrayList<>();
        ArrayList<Boolean> seen = new ArrayList<>();
        ArrayList<Integer> encounters = new ArrayList<>();

        for (Bestiary cat : values()) {
            for (Class<?> entity : cat.entities()) {
                if (cat.seen.get(entity) || cat.encounterCount.get(entity) > 0) {
                    classes.add(entity);
                    seen.add(cat.seen.get(entity));
                    encounters.add(cat.encounterCount.get(entity));
                }
            }
        }

        Class<?>[] storeCls = new Class[classes.size()];
        boolean[] storeSeen = new boolean[seen.size()];
        int[] storeEncounters = new int[encounters.size()];

        for (int i = 0; i < storeCls.length; i++) {
            storeCls[i] = classes.get(i);
            storeSeen[i] = seen.get(i);
            storeEncounters[i] = encounters.get(i);
        }

        bundle.put(BESTIARY_CLASSES, storeCls);
        bundle.put(BESTIARY_SEEN, storeSeen);
        bundle.put(BESTIARY_ENCOUNTERS, storeEncounters);

    }

    public static void restore(Bundle bundle) {

        if (bundle.contains(BESTIARY_CLASSES)
                && bundle.contains(BESTIARY_SEEN)
                && bundle.contains(BESTIARY_ENCOUNTERS)) {
            Class<?>[] classes = bundle.getClassArray(BESTIARY_CLASSES);
            boolean[] seen = bundle.getBooleanArray(BESTIARY_SEEN);
            int[] encounters = bundle.getIntArray(BESTIARY_ENCOUNTERS);

            if (classes == null || seen == null || encounters == null
                    || classes.length != seen.length
                    || classes.length != encounters.length) {
                Journal.saveNeeded = true;
            } else {
                for (int i = 0; i < classes.length; i++) {
                    if (classes[i] == null) continue;
                    for (Bestiary cat : values()) {
                        if (cat.seen.containsKey(classes[i])) {
                            cat.seen.put(classes[i], seen[i]);
                            cat.encounterCount.put(classes[i], encounters[i]);
                        }
                    }
                }
            }
        }

    }

}
