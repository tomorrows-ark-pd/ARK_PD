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
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Alchemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ActiveOriginium;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChenCombo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CloserangeShot;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fury;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Heat;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HoldFast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.IronSkin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.KnightSKILL;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LanceCharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RadiantKnight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SeethingBurst;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SpikesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StomeCharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Twilight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WildMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WindEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.AnnihilationGear;
import com.shatteredpixel.shatteredpixeldungeon.items.Bonk;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.ChainHook;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.CrimsonCutter;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.ExecutionMode;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Fate;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.FierceGlare;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.FoodPrep;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Hikari;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.LiveStart;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Panorama;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.PhantomMirror;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.PowerfulStrike;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Shinkageryu;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.SoulAbsorption;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.SpreadSpores;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.TacticalChanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Thoughts;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.Whispers;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.WolfSpirit;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.AncientKin;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BenasProtracto;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.BreaktheDawn;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.ChargingPS;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.CoverSmoke;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.DeepHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.DeepSeaPredators;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Dreamland;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.EmergencyDefibrillator;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.FlashShield;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.HotBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Genesis;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Jackinthebox;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.LandingStrike;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.MentalBurst;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Nervous;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.NeverBackDown;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Reflow;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.RockfailHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Spikes;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.WolfPack;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.EveryoneTogether;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.ExtremeSharpness;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.NigetRaid;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.SBurst;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.ShadowAssault;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.ShiningSun;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.SoaringFeather;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.TerminationT;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.TrueSilverSlash;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK3.YourWish;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.Skill;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SkillBook;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemyKit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SealOfLight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAssassin;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfDominate;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SP.StaffOfMudrock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Decapitator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Echeveria;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Enfild2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gluttony;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KRISSVector;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Niansword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PatriotSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SHISHIOH;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SanktaBet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Suffering;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaTerror;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.StatusPane;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndResurrect;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Hero extends Char {

    // 스킨 관련
    public static final int TALULAH = 1;
    public static final int F_NOVA = 2;
    public static final int SKADI = 3;
    public static final int SSR = 4;
    public static final int GRANI = 5;
    public static final int JESSI = 6;
    public static final int LAPPY = 7;
    public static final int LEAF = 8;
    public static final int MUDROCK = 9;
    public static final int AST = 10;
    public static final int SPT = 11;
    public static final int SCHWARZ = 12;
    public static final int ARCH = 13;
    public static final int TOMIMI = 14;
    public static final int FRANKA = 15;
    public static final int WEEDY = 16;
    public static final int GLADIIA = 17;

    {
        actPriority = HERO_PRIO;

        alignment = Alignment.ALLY;
    }

    public static final int MAX_LEVEL = 40;

    //public static final int STARTING_STR = 100000;
    public static final int STARTING_STR = 10;

    private static final float TIME_TO_REST = 1f;
    private static final float TIME_TO_SEARCH = 2f;
    private static final float HUNGER_FOR_SEARCH = 6f;

    public HeroClass heroClass = HeroClass.ROGUE;
    public HeroSubClass subClass = HeroSubClass.NONE;
    public ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();

    private int attackSkill = 10;
    //private int attackSkill = 1000;
    private int defenseSkill = 5;

    public boolean ready = false;
    private boolean damageInterrupt = true;
    public HeroAction curAction = null;
    public HeroAction lastAction = null;

    private Char enemy;

    public boolean resting = false;

    public Belongings belongings;

    public int STR;

    public float awareness;

    public int lvl = 1;
    public int exp = 0;

    public int HTBoost = 0;

    public String named;

    public Skill SK1;
    public Skill SK2;
    public Skill SK3;

    private int SK1num;
    private int SK2num;
    private int SK3num;

    public int CharSkin = 0; // 0은 디폴트 스킨.

    private ArrayList<Mob> visibleEnemies;

    //This list is maintained so that some logic checks can be skipped
    // for enemies we know we aren't seeing normally, resultign in better performance
    public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

    public Hero() {
        super();

        HP = HT = 20;
        //HP = HT = 2000;
        STR = STARTING_STR;

        belongings = new Belongings(this);

        visibleEnemies = new ArrayList<>();
    }

    public void updateHT(boolean boostHP) {
        int curHT = HT;

        if (Dungeon.eazymode == 1) HT = 20 + 10 * (lvl - 1) + (HTBoost * 2);
        else HT = 20 + 5 * (lvl - 1) + HTBoost;
        float multiplier = RingOfMight.HTMultiplier(this);
        HT = Math.round(multiplier * HT);

        if (buff(ElixirOfMight.HTBoost.class) != null) {
            HT += buff(ElixirOfMight.HTBoost.class).boost();
        }

        if (boostHP) {
            HP += Math.max(HT - curHT, 0);
        }
        if (Dungeon.isChallenged(Challenges.SPECIAL_BOSS) && Dungeon.mboss14 == 1 && Dungeon.depth == 25 && Dungeon.bossLevel() && Dungeon.talucount < 4) HT /= 2;

        if (hasTalent(Talent.KNIGHT_BODY)) {
            HT += pointsInTalent(Talent.KNIGHT_BODY) * 10;
        }
        HP = Math.min(HP, HT);
    }

    public int STR() {
        int STR = this.STR;

        STR += RingOfMight.strengthBonus(this);

        AdrenalineSurge buff = buff(AdrenalineSurge.class);
        if (buff != null) {
            STR += buff.boost();
        }

        return STR;
    }

    private static final String ATTACK = "attackSkill";
    private static final String DEFENSE = "defenseSkill";
    private static final String STRENGTH = "STR";
    private static final String LEVEL = "lvl";
    private static final String EXPERIENCE = "exp";
    private static final String HTBOOST = "htboost";
    private static final String RENAME = "named";
    private static final String SKL1 = "sk1num";
    private static final String SKL2 = "sk2num";
    private static final String SKL3 = "sk3num";
    private static final String SKIN = "charskin";

    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        heroClass.storeInBundle(bundle);
        subClass.storeInBundle(bundle);
        Talent.storeTalentsInBundle(bundle, this);

        bundle.put(ATTACK, attackSkill);
        bundle.put(DEFENSE, defenseSkill);

        bundle.put(STRENGTH, STR);

        bundle.put(LEVEL, lvl);
        bundle.put(EXPERIENCE, exp);

        bundle.put(HTBOOST, HTBoost);
        bundle.put(RENAME, named);

        // 스킬 데이터 저장
        bundle.put(SKL1, SK1num);
        bundle.put(SKL2, SK2num);
        bundle.put(SKL3, SK3num);

        // 스킨 코드 저장
        bundle.put(SKIN, CharSkin);

        belongings.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

        lvl = bundle.getInt(LEVEL);
        exp = bundle.getInt(EXPERIENCE);

        HTBoost = bundle.getInt(HTBOOST);

        super.restoreFromBundle(bundle);

        heroClass = HeroClass.restoreInBundle(bundle);
        subClass = HeroSubClass.restoreInBundle(bundle);
        Talent.restoreTalentsFromBundle(bundle, this);

        attackSkill = bundle.getInt(ATTACK);
        defenseSkill = bundle.getInt(DEFENSE);

        named = bundle.getString(RENAME);

        // 스킬 데이터 불러오기
        SK1num = bundle.getInt(SKL1);
        SK2num = bundle.getInt(SKL2);
        SK3num = bundle.getInt(SKL3);

        loadSkill1(SK1num);
        loadSkill2(SK2num);
        loadSkill3(SK3num);

        // 스킨 코드 불러오기
        CharSkin = bundle.getInt(SKIN);

        STR = bundle.getInt(STRENGTH);

        belongings.restoreFromBundle(bundle);
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle) {
        info.level = bundle.getInt(LEVEL);
        info.str = bundle.getInt(STRENGTH);
        info.exp = bundle.getInt(EXPERIENCE);
        info.hp = bundle.getInt(Char.TAG_HP);
        info.ht = bundle.getInt(Char.TAG_HT);
        info.shld = bundle.getInt(Char.TAG_SHLD);
        info.heroClass = HeroClass.restoreInBundle(bundle);
        info.subClass = HeroSubClass.restoreInBundle(bundle);
        Belongings.preview(info, bundle);
    }

    public boolean hasTalent(Talent talent) {
        return pointsInTalent(talent) > 0;
    }

    public int pointsInTalent(Talent talent) {
        for (LinkedHashMap<Talent, Integer> tier : talents) {
            for (Talent f : tier.keySet()) {
                if (f == talent) return tier.get(f);
            }
        }
        return 0;
    }

    public void upgradeTalent(Talent talent) {
        for (LinkedHashMap<Talent, Integer> tier : talents) {
            for (Talent f : tier.keySet()) {
                if (f == talent) tier.put(talent, tier.get(talent) + 1);
            }
        }
        Talent.onTalentUpgraded(this, talent);
    }

    public int talentPointsSpent(int tier) {
        int total = 0;
        for (int i : talents.get(tier - 1).values()) {
            total += i;
        }
        return total;
    }

    public int talentPointsAvailable(int tier) {
        if (lvl < Talent.tierLevelThresholds[tier]
                || (tier == 3 && subClass == HeroSubClass.NONE)) {
            return 0;
        } else if (lvl >= Talent.tierLevelThresholds[tier + 1]) {
            return Talent.tierLevelThresholds[tier + 1] - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier);
        } else {
            return 1 + lvl - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier);
        }
    }

    public String className() {
		/*if (named != null) {
			return  named;
		}
		else*/
        return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
    }

    @Override
    public String name() {
        return className();
    }

    @Override
    public void hitSound(float pitch) {
        if (belongings.weapon != null) {
            belongings.weapon.hitSound(pitch);
        } else if (RingOfForce.getBuffedBonus(this, RingOfForce.Force.class) > 0) {
            //pitch deepens by 2.5% (additive) per point of strength, down to 75%
            super.hitSound(pitch * GameMath.gate(0.75f, 1.25f - 0.025f * STR(), 1f));
        } else {
            super.hitSound(pitch * 1.1f);
        }
    }

    @Override
    public boolean blockSound(float pitch) {
        if (belongings.weapon != null && belongings.weapon.defenseFactor(this) >= 4) {
            Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, pitch);
            return true;
        }
        return super.blockSound(pitch);
    }

    public void live() {
        Buff.affect(this, Regeneration.class);
        Buff.affect(this, Hunger.class);
    }

    public int tier() {
        if (Dungeon.hero.SK3 != null) {
            return 6;
        } else return belongings.armor == null ? 0 : belongings.armor.tier;
    }

    public boolean shoot(Char enemy, MissileWeapon wep) {

        this.enemy = enemy;

        //temporarily set the hero's weapon to the missile weapon being used
        belongings.stashedWeapon = belongings.weapon;
        belongings.weapon = wep;
        boolean hit = attack(enemy);
        Invisibility.dispel();
        belongings.weapon = belongings.stashedWeapon;
        belongings.stashedWeapon = null;

        if (hit && subClass == HeroSubClass.GLADIATOR) {
            Buff.affect(this, Combo.class).hit(enemy);
        }

        return hit;
    }

    @Override
    public int attackSkill(Char target) {
        KindOfWeapon wep = belongings.weapon;

        float accuracy = 1;
        accuracy *= RingOfAccuracy.accuracyMultiplier(this);

        if (wep instanceof MissileWeapon) {
            if (Dungeon.level.adjacent(pos, target.pos)) {
                accuracy *= (0.5f + 0.15f * pointsInTalent(Talent.DURABLE_TIPS));
            } else {
                if (this.subClass == HeroSubClass.SNIPER)
                    accuracy *= 1.75f + (pointsInTalent(Talent.FARSIGHT) * 0.1f);
                else accuracy *= 1.5f;
            }
        }

        if (hasTalent(Talent.PEGASUS_AURA) && buff(RadiantKnight.class) != null) {
            float bouns = 1f;
            bouns += pointsInTalent(Talent.PEGASUS_AURA) / 10;

            accuracy *= bouns;
        }

        if (buff(ExtremeSharpness.SharpnessBuff.class) != null) {
            accuracy *= 2f;
        }

        if (buff(ChenCombo.DoubleSwordBuff.class) != null) {
            accuracy *= 1 + pointsInTalent(Talent.DOUBLE_SWORD) * 0.3f;

            Buff.detach(this, ChenCombo.DoubleSwordBuff.class);
        }

        if (Dungeon.hero.hasTalent(Talent.DRAGONS_SWORD)) {
            float bouns = 1f;
            ChenCombo combo = buff(ChenCombo.class);
            if (combo != null) bouns += combo.getComboCount() * 0.02f;

            accuracy *= bouns;
        }

        if (wep != null) {
            return (int) (attackSkill * accuracy * wep.accuracyFactor(this));
        } else {
            return (int) (attackSkill * accuracy);
        }


    }

    @Override
    public int defenseSkill(Char enemy) {

        if (buff(Combo.ParryTracker.class) != null) {
            if (canAttack(enemy)) {
                Buff.affect(this, Combo.RiposteTracker.class).enemy = enemy;
            }
            return INFINITE_EVASION;
        }

        float evasion = defenseSkill;

        evasion *= RingOfEvasion.evasionMultiplier(this);

        if (paralysed > 0) {
            evasion /= 2;
        }

        if (belongings.armor != null) {
            evasion = belongings.armor.evasionFactor(this, evasion);
        }

        if (buff(StomeCharge.class) != null && hasTalent(Talent.GALEFORCE)) {
            evasion *= 1f + (0.1f * pointsInTalent(Talent.GALEFORCE));
        }

        return Math.round(evasion);
    }

    @Override
    public String defenseVerb() {
        Combo.ParryTracker parry = buff(Combo.ParryTracker.class);
        if (parry == null) {
            return super.defenseVerb();
        } else {
            parry.parried = true;
            if (buff(Combo.class).getComboCount() < 9 || pointsInTalent(Talent.ENHANCED_COMBO) < 2) {
                parry.detach();
            }
            return Messages.get(Monk.class, "parried");
        }
    }

    @Override
    public int drRoll() {
        int dr = 0;

        if (belongings.armor != null) {
            int armDr = Random.NormalIntRange(belongings.armor.DRMin(), belongings.armor.DRMax());
            if (STR() < belongings.armor.STRReq()) {
                armDr -= 2 * (belongings.armor.STRReq() - STR());
            }
            if (armDr > 0) dr += armDr;
        }
        if (belongings.weapon != null) {
            int wepDr = Random.NormalIntRange(0, belongings.weapon.defenseFactor(this));
            if (STR() < ((Weapon) belongings.weapon).STRReq()) {
                wepDr -= 2 * (((Weapon) belongings.weapon).STRReq() - STR());
            }
            if (wepDr > 0) dr += wepDr;
        }
        Barkskin bark = buff(Barkskin.class);
        SeethingBurst Burst = buff(SeethingBurst.class);
        if (bark != null) dr += Random.NormalIntRange(0, bark.level());
        if (Burst != null) dr *= 3;

        Blocking.BlockBuff block = buff(Blocking.BlockBuff.class);
        if (block != null) dr += block.blockingRoll();

        if (buff(HoldFast.class) != null) {
            dr += Random.NormalIntRange(0, 2 * pointsInTalent(Talent.HOLD_FAST));
        }

        if (buff(IronSkin.class) != null) dr += Random.NormalIntRange(0,2);

        if (hasTalent(Talent.TACTICAL_SHIELD)) {
            int drplus = belongings.armor.buffedLvl() * 2;
            drplus = Math.min(drplus, 1 + pointsInTalent(Talent.TACTICAL_SHIELD) * 3);
            dr += Random.NormalIntRange(0,drplus);
        }

        if (hasTalent(Talent.SHIELD_OF_LIGHT)) {
            int drplus_n = pointsInTalent(Talent.SHIELD_OF_LIGHT);
            if (HP <= HT/2) drplus_n*=2;
            if (buff(RadiantKnight.class) != null) drplus_n*=2;
            dr += Random.NormalIntRange(0,drplus_n);
        }

        if (hasTalent(Talent.GUILT)) {
            int kill = (90 - pointsInTalent(Talent.GUILT) * 10);
            int bounsDR;
            bounsDR = Math.min(Statistics.enemiesSlain / kill, 15);
            dr += Random.NormalIntRange(0,bounsDR);
        }

        if (hasTalent(Talent.PARING)) {
            int drplus = pointsInTalent(Talent.PARING);
            dr += Random.NormalIntRange(0,drplus);
        }

        return dr;
    }

    @Override
    public int damageRoll() {
        KindOfWeapon wep = belongings.weapon;
        int dmg;

        if (wep != null) {
            dmg = wep.damageRoll(this);
            if (!(wep instanceof MissileWeapon)) dmg += RingOfForce.armedDamageBonus(this);
        } else {
            dmg = RingOfForce.damageRoll(this);
        }
        if (dmg < 0) dmg = 0;

        Berserk berserk = buff(Berserk.class);
        if (berserk != null) dmg = berserk.damageFactor(dmg);

        if (this.buff(LanceCharge.class) != null) {
            dmg *= 3f;
            Buff.detach(this, LanceCharge.class);
        }

        return buff(Fury.class) != null ? (int) (dmg * 1.5f) : dmg;
    }

    @Override
    public float speed() {

        float speed = super.speed();

        speed *= RingOfHaste.speedMultiplier(this);

        if (belongings.armor != null) {
            speed = belongings.armor.speedFactor(this, speed);
        }

        Momentum momentum = buff(Momentum.class);
        if (momentum != null) {
            ((HeroSprite) sprite).sprint(momentum.freerunning() ? 1.5f : 1f);
            speed *= momentum.speedMultiplier();
        } else {
            ((HeroSprite) sprite).sprint(1f);
        }

        StomeCharge stome = buff(StomeCharge.class);
        if (stome != null) {
            speed *= stome.speedMultiplier();
        }

        float spup = 0f;

        // 광붕이
        Berserk berserk = buff(Berserk.class);
        if (berserk != null)
            spup = berserk.getPower() / 1.3f;

        spup = Math.min(spup, 0.3f + (float) Dungeon.hero.pointsInTalent(Talent.BERSERKING_STAMINA) / 5);



        // 쪽냥이 수호
        AnnihilationGear Gear = this.belongings.getItem(AnnihilationGear.class);
        if (Gear != null) {
            if (Gear.charge > 0) {
                if (this.hasTalent(Talent.SPEED_COMABT)) {
                    speed *= 1f + (float) this.pointsInTalent(Talent.SPEED_COMABT) / 20;
                }
            }
        }

        // 열붕이
        Heat heat = buff(Heat.class);
        if (heat != null && hasTalent(Talent.REDCOMET)) {
            if (heat.state() == Heat.State.OVERHEAT) spup = pointsInTalent(Talent.REDCOMET) * 0.25f;
            else if (heat.power() >= 70) spup = pointsInTalent(Talent.REDCOMET) * 0.05f;
        }

        return speed *= (1 + spup);

    }

    public boolean canSurpriseAttack() {
        if (belongings.weapon == null || !(belongings.weapon instanceof Weapon)) return true;
        if (STR() < ((Weapon) belongings.weapon).STRReq()) return false;
        if (belongings.weapon instanceof Decapitator) return false;
        if (belongings.weapon instanceof SHISHIOH) return false;
        if (belongings.weapon instanceof Enfild2) return false;
        if (RingOfAssassin.Assassin_Curse(this) == true) return false;
        if (belongings.weapon instanceof KRISSVector) return false;
        if (belongings.weapon instanceof Suffering) return false;

        return true;
    }

    public boolean canAttack(Char enemy) {
        if (enemy == null || pos == enemy.pos || !Actor.chars().contains(enemy)) {
            return false;
        }

        //can always attack adjacent enemies
        if (Dungeon.level.adjacent(pos, enemy.pos)) {
            return true;
        }

        KindOfWeapon wep = Dungeon.hero.belongings.weapon;

        if (wep != null) {
            return wep.canReach(this, enemy.pos);
        } else {
            return false;
        }
    }

    public float attackDelay() {
        if (buff(Talent.LethalMomentumTracker.class) != null) {
            buff(Talent.LethalMomentumTracker.class).detach();
            return 0;
        }

        if (hasTalent(Talent.CONTINUOUS_CUT)) {
            if (Random.Int(100) < pointsInTalent(Talent.CONTINUOUS_CUT) * 5) {
                return 0;
            }
        }

        if (belongings.weapon != null) {

            return belongings.weapon.speedFactor(this);

        } else {
            //Normally putting furor speed on unarmed attacks would be unnecessary
            //But there's going to be that one guy who gets a furor+force ring combo
            //This is for that one guy, you shall get your fists of fury!
            return RingOfFuror.attackDelayMultiplier(this);
        }
    }

    @Override
    public void spend(float time) {
        justMoved = false;
        TimekeepersHourglass.timeFreeze freeze = buff(TimekeepersHourglass.timeFreeze.class);
        if (freeze != null) {
            freeze.processTime(time);
            return;
        }

        Swiftthistle.TimeBubble bubble = buff(Swiftthistle.TimeBubble.class);
        if (bubble != null) {
            bubble.processTime(time);
            return;
        }

        CloserangeShot crshot = buff(CloserangeShot.class);
        if (crshot != null) {
            crshot.isActived();
        }

        WildMark mark = buff(WildMark.class);
        if (mark != null) {
            mark.Charged(time);
        }
        else if (subClass == HeroSubClass.WILD) Buff.affect(this, WildMark.class);

        if (Dungeon.depth > 35 && Dungeon.extrastage_Sea && Dungeon.level.seaTerrors.get(pos) != null) {
            Dungeon.level.seaTerrors.get(pos).spendTime(this, time);
        }

        if (belongings.weapon instanceof PatriotSpear) {
            if (belongings.armor instanceof PlateArmor) {
                if (belongings.getItem(RingOfMight.class) != null && belongings.getItem(RingOfTenacity.class) != null) {
                    if (belongings.getItem(RingOfTenacity.class).isEquipped(this) && belongings.getItem(RingOfMight.class).isEquipped(this)){
                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                            if (Dungeon.level.adjacent(mob.pos, pos) && mob.alignment != Char.Alignment.ALLY && !mob.properties().contains(Property.NPC)
                                    && !(mob instanceof Shopkeeper) && !(mob instanceof ImpShopkeeper)) {
                                mob.sprite.emitter().burst( ShadowParticle.CURSE, 15);
                                Buff.affect(mob, Hex.class, 3f);
                                int dmg = mob.HP / 8;
                                dmg = Math.min(dmg, 30);
                                dmg = Math.max(3, dmg);
                                mob.damage( dmg, this );
                            }
                        }
                        this.sprite.emitter().burst( ShadowParticle.UP, 2);
                    }}}}

        if (belongings.weapon instanceof Gluttony) {
            if (Random.Int(6) == 0) ((Gluttony) belongings.weapon).SPCharge(1); }

        if (belongings.weapon instanceof Echeveria && STR() >= ((Echeveria) belongings.weapon).STRReq()) ((Echeveria) belongings.weapon).SPCharge( (int)(1*time));
        if (belongings.weapon instanceof Suffering && STR() >= ((Suffering) belongings.weapon).STRReq()) ((Suffering) belongings.weapon).SPCharge((int)(2*time));

        if (subClass == HeroSubClass.HEAT) {
            Heat heat = buff(Heat.class);
            if (heat == null) {
                Buff.affect(this, Heat.class);
            } else heat.Timeproc(time);
        }
        PathFinder.buildDistanceMap(Dungeon.hero.pos, BArray.not(Dungeon.level.solid, null), 2);
        for (int cell = 0; cell < PathFinder.distance.length; cell++) {
            if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                Char ch = Actor.findChar(cell);
                if (ch != null&& !(ch instanceof Hero) && ch.alignment == Char.Alignment.ENEMY) {
                    Buff.detach(Dungeon.hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Camouflage.class); }}}



        super.spend(time);
    }

    public void spendAndNext(float time) {
        busy();
        spend(time);
        next();
    }

    @Override
    public boolean act() {

        //calls to dungeon.observe will also update hero's local FOV.
        fieldOfView = Dungeon.level.heroFOV;

        if (!ready) {
            //do a full observe (including fog update) if not resting.
            if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null) {
                Dungeon.observe();
            } else {
                //otherwise just directly re-calculate FOV
                Dungeon.level.updateFieldOfView(this, fieldOfView);
            }
        }

        checkVisibleMobs();
        BuffIndicator.refreshHero();

        if (paralysed > 0) {

            curAction = null;

            spendAndNext(TICK);
            return false;
        }

        boolean actResult;
        if (curAction == null) {

            if (resting) {
                spend(TIME_TO_REST);
                next();
            } else {
                ready();
            }

            actResult = false;

        } else {

            resting = false;

            ready = false;

            if (curAction instanceof HeroAction.Move) {
                actResult = actMove((HeroAction.Move) curAction);

            } else if (curAction instanceof HeroAction.Interact) {
                actResult = actInteract((HeroAction.Interact) curAction);

            } else if (curAction instanceof HeroAction.Buy) {
                actResult = actBuy((HeroAction.Buy) curAction);

            } else if (curAction instanceof HeroAction.PickUp) {
                actResult = actPickUp((HeroAction.PickUp) curAction);

            } else if (curAction instanceof HeroAction.OpenChest) {
                actResult = actOpenChest((HeroAction.OpenChest) curAction);

            } else if (curAction instanceof HeroAction.Unlock) {
                actResult = actUnlock((HeroAction.Unlock) curAction);

            } else if (curAction instanceof HeroAction.Descend) {
                actResult = actDescend((HeroAction.Descend) curAction);

            } else if (curAction instanceof HeroAction.Ascend) {
                actResult = actAscend((HeroAction.Ascend) curAction);

            } else if (curAction instanceof HeroAction.Attack) {
                actResult = actAttack((HeroAction.Attack) curAction);

            } else if (curAction instanceof HeroAction.Alchemy) {
                actResult = actAlchemy((HeroAction.Alchemy) curAction);

            } else {
                actResult = false;
            }
        }

        return actResult;
    }

    public void busy() {
        ready = false;
    }

    private void ready() {
        if (sprite.looping()) sprite.idle();
        curAction = null;
        damageInterrupt = true;
        ready = true;

        AttackIndicator.updateState();

        GameScene.ready();
    }

    public void interrupt() {
        if (isAlive() && curAction != null &&
                ((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
                        (curAction instanceof HeroAction.Ascend || curAction instanceof HeroAction.Descend))) {
            lastAction = curAction;
        }
        curAction = null;
        GameScene.resetKeyHold();
    }

    public void resume() {
        curAction = lastAction;
        lastAction = null;
        damageInterrupt = false;
        next();
    }

    private boolean actMove(HeroAction.Move action) {

        if (getCloser(action.dst)) {
            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actInteract(HeroAction.Interact action) {

        Char ch = action.ch;

        if (ch.canInteract(this)) {

            ready();
            sprite.turnTo(pos, ch.pos);
            return ch.interact(this);

        } else {

            if (fieldOfView[ch.pos] && getCloser(ch.pos)) {

                return true;

            } else {
                ready();
                return false;
            }

        }
    }

    private boolean actBuy(HeroAction.Buy action) {
        int dst = action.dst;
        if (pos == dst) {

            ready();

            Heap heap = Dungeon.level.heaps.get(dst);
            if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndTradeItem(heap));
                    }
                });
            }
            else if (heap != null && heap.type == Type.FOR_SALE_28F && heap.size() == 1) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndTradeItem(heap, true));
                    }
                });
            }
            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAlchemy(HeroAction.Alchemy action) {
        int dst = action.dst;
        if (Dungeon.level.distance(dst, pos) <= 1) {

            ready();

            AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
            if (kit != null && kit.isCursed()) {
                GLog.w(Messages.get(AlchemistsToolkit.class, "cursed"));
                return false;
            }

            AlchemyKit.AlchemyBuff AKit = buff(AlchemyKit.AlchemyBuff.class);
            if (AKit != null && AKit.isCursed()) {
                GLog.w(Messages.get(AlchemyKit.class, "cursed"));
                return false;
            }

            Alchemy alch = (Alchemy) Dungeon.level.blobs.get(Alchemy.class);
            if (alch != null) {
                alch.alchPos = dst;
                AlchemyScene.setProvider(alch);
            }
            TomorrowRogueNight.switchScene(AlchemyScene.class);
            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actPickUp(HeroAction.PickUp action) {
        int dst = action.dst;
        if (pos == dst) {

            Heap heap = Dungeon.level.heaps.get(pos);
            if (heap != null) {
                Item item = heap.peek();
                if (item.doPickUp(this)) {
                    heap.pickUp();

                    if (item instanceof Dewdrop
                            || item instanceof TimekeepersHourglass.sandBag
                            || item instanceof DriedRose.Petal
                            || item instanceof Key) {
                        //Do Nothing
                    } else {

                        //TODO make all unique items important? or just POS / SOU?
                        boolean important = item.unique && item.isIdentified() &&
                                (item instanceof Scroll || item instanceof Potion);
                        if (important) {
                            GLog.p(Messages.get(this, "you_now_have", item.name()));
                        } else {
                            GLog.i(Messages.get(this, "you_now_have", item.name()));
                        }
                    }

                    curAction = null;
                } else {

                    if (item instanceof Dewdrop
                            || item instanceof TimekeepersHourglass.sandBag
                            || item instanceof DriedRose.Petal
                            || item instanceof Key) {
                        //Do Nothing
                    } else {
                        GLog.newLine();
                        GLog.n(Messages.get(this, "you_cant_have", item.name()));
                    }

                    heap.sprite.drop();
                    ready();
                }
            } else {
                ready();
            }

            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actOpenChest(HeroAction.OpenChest action) {
        int dst = action.dst;
        if (Dungeon.level.adjacent(pos, dst) || pos == dst) {

            Heap heap = Dungeon.level.heaps.get(dst);
            if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE && heap.type != Type.FOR_SALE_28F)) {

                if ((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.depth)) < 1)
                        || (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.depth)) < 1)) {

                    GLog.w(Messages.get(this, "locked_chest"));
                    ready();
                    return false;

                }

                switch (heap.type) {
                    case TOMB:
                        Sample.INSTANCE.play(Assets.Sounds.TOMB);
                        Camera.main.shake(1, 0.5f);
                        break;
                    case SKELETON:
                    case REMAINS:
                        break;
                    default:
                        Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
                }

                sprite.operate(dst);

            } else {
                ready();
            }

            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actUnlock(HeroAction.Unlock action) {
        int doorCell = action.dst;
        if (Dungeon.level.adjacent(pos, doorCell)) {

            boolean hasKey = false;
            int door = Dungeon.level.map[doorCell];

            if (door == Terrain.LOCKED_DOOR
                    && Notes.keyCount(new IronKey(Dungeon.depth)) > 0) {

                hasKey = true;

            } else if (door == Terrain.LOCKED_EXIT
                    && Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0) {

                hasKey = true;

            }

            if (hasKey) {

                sprite.operate(doorCell);

                Sample.INSTANCE.play(Assets.Sounds.UNLOCK);

            } else {
                GLog.w(Messages.get(this, "locked_door"));
                ready();
            }

            return false;

        } else if (getCloser(doorCell)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actDescend(HeroAction.Descend action) {
        int stairs = action.dst;

        if (rooted) {
            Camera.main.shake(1, 1f);
            ready();
            return false;
            //there can be multiple exit tiles, so descend on any of them
            //TODO this is slightly brittle, it assumes there are no disjointed sets of exit tiles
        } else if (Dungeon.level.locked) {
            ready();
            return false;
        } else if ((Dungeon.level.map[pos] == Terrain.EXIT || Dungeon.level.map[pos] == Terrain.UNLOCKED_EXIT)) {

            curAction = null;

            Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();
            buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (buff != null) buff.detach();


            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene(InterlevelScene.class);

            return false;

        } else if (getCloser(stairs)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAscend(HeroAction.Ascend action) {
        int stairs = action.dst;

        if (rooted) {
            Camera.main.shake(1, 1f);
            ready();
            return false;
            //there can be multiple entrance tiles, so descend on any of them
            //TODO this is slightly brittle, it assumes there are no disjointed sets of entrance tiles
        } else if (Dungeon.level.locked) {
            ready();
            return false;}
        else if (Dungeon.level.map[pos] == Terrain.ENTRANCE) {
            if (Dungeon.depth == 1) {
                curAction = null;

                Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
                if (buff != null) buff.detach();
                buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (buff != null) buff.detach();

                else InterlevelScene.mode = InterlevelScene.Mode.DESCEND_27;
                Game.switchScene(InterlevelScene.class);

                return true;
            } else {
                curAction = null;

                Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
                if (buff != null) buff.detach();
                buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (buff != null) buff.detach();

                if (Dungeon.depth != 27) InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                else InterlevelScene.mode = InterlevelScene.Mode.ASCEND_27;
                Game.switchScene(InterlevelScene.class);

                return true;
            }
        } else if (getCloser(stairs)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAttack(HeroAction.Attack action) {

        enemy = action.target;


        if (enemy.isAlive() && canAttack(enemy) && !isCharmedBy(enemy)) {

            sprite.attack(enemy.pos);

            return false;

        } else {

            if (fieldOfView[enemy.pos] && getCloser(enemy.pos)) {

                return true;

            } else {
                ready();
                return false;
            }

        }
    }

    public Char enemy() {
        return enemy;
    }

    public void rest(boolean fullRest) {
        spendAndNext(TIME_TO_REST);
        if (!fullRest) {
            if (hasTalent(Talent.HOLD_FAST)) {
                Buff.affect(this, HoldFast.class);
            }
            if (sprite != null) {
                sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
            }
        }
        resting = fullRest;
    }

    @Override
    public int attackProc(final Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        float BounsDamage = 0;

        KindOfWeapon wep = belongings.weapon;

        if (wep != null) damage = wep.proc(this, enemy, damage);

        if (buff(Bonk.BonkBuff.class) != null) Buff.detach(this, Bonk.BonkBuff.class);

        damage = Talent.onAttackProc(this, enemy, damage);

        if (enemy instanceof Mob) {
            if (((Mob) enemy).surprisedBy(this)) {
                BounsDamage += damage * (RingOfAssassin.supriseattackbouns(this) - 1f);}
        }

        AnnihilationGear Gear = this.belongings.getItem(AnnihilationGear.class);
        if (hasTalent(Talent.RHODES_CAT)) {
            if (Gear != null)
                if (Gear.charge > 0) {
                    damage *= 1f + (float) this.pointsInTalent(Talent.RHODES_CAT) * 0.12f;
                }
        }
        if (hasTalent(Talent.WEAKNESS_COVER)) {
            int geardmg = Gear.level();
            geardmg *= Random.IntRange(pointsInTalent(Talent.WEAKNESS_COVER) - 1, 2);
            BounsDamage += geardmg;
        }

        if (buff(RadiantKnight.class) != null) {
            if (subClass == HeroSubClass.SAVIOR) damage *= 1.55f;
            else if (subClass == HeroSubClass.FLASH) damage *= 1.25f;
            else damage *= 1.4f;

            // 난입 특성
            if (hasTalent(Talent.PHASERUSH)) {
                SealOfLight Seal = this.belongings.getItem(SealOfLight.class);
                if (Seal != null)
                    Seal.charge(this, pointsInTalent(Talent.PHASERUSH));
                Seal.updateQuickslot();
            }
            // 카시미어의 기사
            if (hasTalent(Talent.KNIGHT_OF_KAZIMIERZ) && Random.Int(5) == 0) {
                if (Dungeon.hero.belongings.getItem(SkillBook.class) != null) {
                    SkillBook Item = Dungeon.hero.belongings.getItem(SkillBook.class);
                    Item.SetCharge(pointsInTalent(Talent.KNIGHT_OF_KAZIMIERZ));
                    Item.updateQuickslot();
                }
            }
        }

        if (enemy.buff(Blindness.class) != null && hasTalent(Talent.FLASH_SPEAR)) {
            BounsDamage += damage * (pointsInTalent(Talent.FLASH_SPEAR) * 0.1f);
        }

        if (hasTalent(Talent.ETERNAL_GLORY) && Random.Int(10) < pointsInTalent(Talent.ETERNAL_GLORY)) {
            Buff.affect(enemy, Blindness.class, 3);
        }

        if (buff(BreaktheDawn.BreakBuff.class) != null) {
            damage *= 2f;
            Buff.detach(this, BreaktheDawn.BreakBuff.class);
        }

        if (hasTalent(Talent.PUSH_ATTACK) && buff(Talent.PushAttackCooldown.class) == null) {
            Ballistica trajectory = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
            WandOfBlastWave.throwChar(enemy, trajectory, pointsInTalent(Talent.PUSH_ATTACK)); // 넉백 효과
            Buff.affect(this, Talent.PushAttackCooldown.class, 80);
        }

        switch (subClass) {
            case SNIPER:
                if (wep instanceof MissileWeapon && !(wep instanceof SpiritBow.SpiritArrow) && enemy != this) {
                    Actor.add(new Actor() {

                        {
                            actPriority = VFX_PRIO;
                        }

                        @Override
                        protected boolean act() {
                            if (enemy.isAlive()) {
                                int bonusTurns = hasTalent(Talent.SHARED_UPGRADES) ? wep.buffedLvl() * 2 : 0;
                                Buff.prolong(Hero.this, SnipersMark.class, SnipersMark.DURATION + bonusTurns).set(enemy.id(), bonusTurns);
                            }
                            Actor.remove(this);
                            return true;
                        }
                    });
                }
                break;
            case KNIGHT:
                if (buff(Haste.class) != null)
                    damage *= 1.2f;
                break;
            default:
        }

        if (hasTalent(Talent.SAVIOR_BELIEF) && enemy.buff(Roots.class) != null || enemy.buff(Paralysis.class) != null) {
            BounsDamage = damage * (pointsInTalent(Talent.SAVIOR_BELIEF) * 0.15f);
        }

        if (hasTalent(Talent.EXORCISM)) {
            if (enemy.properties().contains(Property.SARKAZ)) {
                BounsDamage += 1 + pointsInTalent(Talent.EXORCISM)*2;
            }
        }

        if (Dungeon.hero.hasTalent(Talent.SAVIOR_BELIEF)) {
            int grassCells = 0;
            for (int i : PathFinder.NEIGHBOURS9) {
                if (Dungeon.level.map[pos + i] == Terrain.FURROWED_GRASS
                        || Dungeon.level.map[pos + i] == Terrain.HIGH_GRASS) {
                    grassCells++;
                }
            }
            float Reg = 1f;
            if (grassCells > 0) Reg += 0.025f + grassCells * 0.025f;
            damage *= Reg;
        }

        // 폭풍 스롯 바람의 축복 충전 처리
        if (buff(WindEnergy.class) != null && hasTalent(Talent.WIND_BLESSING)) {
            int getcharge = damage;
            getcharge *= (pointsInTalent(Talent.WIND_BLESSING) * 0.1f);

            buff(WindEnergy.class).GetEnergy(getcharge);
        }

        // 영혼 착취 보호막 처리
        if (buff(SoulAbsorption.SoulBuff.class) != null) {
            Buff.detach(this, SoulAbsorption.SoulBuff.class);

            int barr = Math.min(5 + HT /6, 2 + (damage/3));
            Buff.affect(this, Barrier.class).incShield(barr);
        }

        // 열기 블레이즈

        Heat heat = buff(Heat.class);
        if (heat != null) {
            boolean heatbouns = (heat.power() >= 50f);
            if (heat.state() == Heat.State.OVERHEAT) {
                BounsDamage += damage * 0.5f;
                heatbouns = true;
            }

            if (heatbouns && hasTalent(Talent.HEAT_BLOW)) {
                BounsDamage += damage * (0.05f + (pointsInTalent(Talent.HEAT_BLOW) * 0.05f));
            }

            if (hasTalent(Talent.HEAT_OF_ABSORPTION) && heat.state() == Heat.State.OVERHEAT) {
                int heal = Math.min(10, (int) ((damage + BounsDamage) * (0.01f + (pointsInTalent(Talent.HEAT_OF_ABSORPTION) * 0.01f))));
                if (heal > 0) {
                    HP = Math.min(HP + heal, HT);
                    sprite.showStatus(CharSprite.POSITIVE, "+%dHP", heal);
                }
            }
        }

        if (hasTalent(Talent.WIND_ROAD)) {
            WildMark mark = buff(WildMark.class);
            if (mark != null) {
                mark.Charged(pointsInTalent(Talent.WIND_ROAD) * 7.5f);
            }
        }

        damage += BounsDamage;

        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (RingOfDominate.Dominate_curse(this) == true) {
            if (Random.Int(HT) > HP * 3) {
                Buff.affect(this, Corruption.class);
                Dungeon.hero.die(RingOfDominate.class);

                if (Dungeon.hero.isAlive()) {
                    Buff.detach(this, Corruption.class);
                } else GLog.w(Messages.get(RingOfDominate.class, "soulless"));
            }
        }

        if (damage > 0 && subClass == HeroSubClass.BERSERKER) {
            Berserk berserk = Buff.affect(this, Berserk.class);
            berserk.damage(damage);

            if (this.hasTalent(Talent.ENRAGED_CATALYST)) {
                Talent.BlazeBurstBuff counter = Buff.affect(Dungeon.hero, Talent.BlazeBurstBuff.class);
                if (counter.count() < 10) {
                    counter.countUp(1);
                }
                if (berserk.getPower() <= 0.5f) {
                    counter.countDown(1);
                }
            }
        }

        if (belongings.armor != null) {
            damage = belongings.armor.proc(enemy, this, damage);
        }

        if (belongings.weapon instanceof Niansword) {
            int dmg = Random.IntRange(0, 3 + belongings.weapon.buffedLvl() * 3);
            int dr = Math.max(enemy.drRoll(), enemy.drRoll());
            enemy.damage(dmg - dr, this);
        }

        Earthroot.Armor armor = buff(Earthroot.Armor.class);
        if (armor != null) {
            damage = armor.absorb(damage);
        }

        WandOfLivingEarth.RockArmor rockArmor = buff(WandOfLivingEarth.RockArmor.class);
        StaffOfMudrock.RockArmor MudrockArmor = buff(StaffOfMudrock.RockArmor.class);
        if (MudrockArmor != null) {
            damage = MudrockArmor.absorb(damage);
        } else if (rockArmor != null) {
            damage = rockArmor.absorb(damage);
        }

        AnnihilationGear Gear = Dungeon.hero.belongings.getItem(AnnihilationGear.class);
        if (Gear != null) {
            if (Dungeon.hero.subClass == HeroSubClass.GUARDIAN) {
                if (Gear.charge > 0) {
                    if (buff(Barrier.class) == null) {
                        Buff.affect(this, Barrier.class).setShield(HT / 8);

                        damage *= 0.5f - (float) this.pointsInTalent(Talent.BARRIER_OPERATION) * 0.1f;
                        int redamage = 0;
                        redamage += damageRoll() * (float) pointsInTalent(Talent.BARRIER_OPERATION) * 0.3f;
                        if (Dungeon.hero.hasTalent(Talent.BARRIER_OPERATION)) {
                            enemy.damage(redamage, this);
                            CellEmitter.center(enemy.pos).burst(BlastParticle.FACTORY, 10);
                        }
                        Gear.discharge();
                        }
                }
                if (buff(Barrier.class) == null) {
                if (2 + Dungeon.hero.pointsInTalent(Talent.BARRIER_REPAIR) > Random.Int(20) && Gear.charge < Gear.chargeCap) {
                    Gear.SPCharge(1); }}
            }

        }

        if (buff(SpikesBuff.class) != null) {
            damage -= Math.min(drRoll(), drRoll());
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (Dungeon.level.adjacent(mob.pos, this.pos) && mob.alignment != Char.Alignment.ALLY) {
                    mob.damage(Math.min(drRoll(), mob.HT / 3), this);
                    if (!enemy.isAlive() && enemy instanceof Ghoul == false) {
                        CellEmitter.center(enemy.pos).burst(BlastParticle.FACTORY, 10);
                        enemy.sprite.killAndErase();
                    }
                }
            }
        }

        // 니어 특성 관련

        // 퇴마
        if (hasTalent(Talent.EXORCISM)) {
            if (enemy.properties().contains(Property.SARKAZ)) {
                damage -= pointsInTalent(Talent.EXORCISM);
            }
        }

        if (HP <= HT/2) {
        if(hasTalent(Talent.RADIANTHERO)) {
            if (buff(RadiantKnight.class) == null && buff(Talent.RadiantHeroCooldown.class) == null && hasTalent(Talent.RADIANTHERO)) {
                Buff.affect(this, RadiantKnight.class, RadiantKnight.DURATION);

                float CoolDown = 900 - (pointsInTalent(Talent.RADIANTHERO) * 150);
                Buff.affect(this, Talent.RadiantHeroCooldown.class, CoolDown);

                GameScene.flash( 0x80FFFFFF );
                Sample.INSTANCE.play(Assets.Sounds.SKILL_BABYNIGHT);
            }
        }}

        // 천마의 날개
        if (hasTalent(Talent.PEGASUS_WING)) {
            SealOfLight Seal = this.belongings.getItem(SealOfLight.class);
            if (Seal != null && Random.Int(3) < pointsInTalent(Talent.PEGASUS_WING)) {
                Seal.charge(this, 2);
            Seal.updateQuickslot();}
        }

        // 혼돈 아미야 관련

        if (subClass == HeroSubClass.CHAOS) {
            float bounsdamage = 1.5f;
                if (hasTalent(Talent.CHIMERA)) {
                    bounsdamage += pointsInTalent(Talent.CHIMERA) * 0.1f;
                }

            if (hasTalent(Talent.STABILIZE)) {
                if (HP / HT <= 0.15f + pointsInTalent(Talent.STABILIZE) * 0.05f) {
                    bounsdamage = 1f;
                }}

                damage *= bounsdamage;
            }


        // 첸 특성

        if (hasTalent(Talent.SCOLDING) && buffs(Talent.ScoldingCooldown.class) == null && HT / 2 >= HP) {
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                    Buff.prolong(mob, Amok.class, pointsInTalent(Talent.SCOLDING));
                }
            }
            this.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );

            Buff.affect(this, Talent.ScoldingCooldown.class, 300f);
        }

        return damage;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (buff(TimekeepersHourglass.timeStasis.class) != null)
            return;

        if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
            interrupt();
            resting = false;
        }

        if (this.buff(Drowsy.class) != null) {
            Buff.detach(this, Drowsy.class);
            GLog.w(Messages.get(this, "pain_resist"));
        }

        CapeOfThorns.Thorns thorns = buff(CapeOfThorns.Thorns.class);
        if (thorns != null) {
            dmg = thorns.proc(dmg, (src instanceof Char ? (Char) src : null), this);
        }

        dmg = (int) Math.ceil(dmg * RingOfTenacity.damageMultiplier(this));

        //TODO improve this when I have proper damage source logic
        if (belongings.armor != null && belongings.armor.hasGlyph(AntiMagic.class, this)
                && AntiMagic.RESISTS.contains(src.getClass())) {
            dmg -= AntiMagic.drRoll(belongings.armor.buffedLvl());
        }

        if (buff(Talent.WarriorFoodImmunity.class) != null) {
            if (pointsInTalent(Talent.IRON_STOMACH) == 1) dmg = Math.round(dmg * 0.5f);
            else if (pointsInTalent(Talent.IRON_STOMACH) == 2) dmg = Math.round(dmg * 0.25f);
        }

        if (Dungeon.hero.hasTalent(Talent.INFINITE_RAGE)) {
            Berserk berserk = buff(Berserk.class);
            float ber;
            if (berserk != null) {
                ber = 1.2f - (Dungeon.hero.pointsInTalent(Talent.INFINITE_RAGE) * 0.2f);
                if (berserk.getPower() >= ber) {
                    dmg = Math.round(dmg * 0.8f);
                }
            }
        }

        if (Dungeon.hero.hasTalent(Talent.BARKSKIN) && !(src instanceof Hunger)) {
            int grassCells = 0;
            for (int i : PathFinder.NEIGHBOURS9) {
                if (Dungeon.level.map[pos + i] == Terrain.FURROWED_GRASS
                        || Dungeon.level.map[pos + i] == Terrain.HIGH_GRASS) {
                    grassCells++;
                }
            }

            float Reg = 1f;
            if (grassCells > 0) Reg -= 0.04f + grassCells * 0.04f;
            dmg *= Reg;
        }


        if (src instanceof ChaliceOfBlood) {
            if (buff(Twilight.class) != null) {
                Buff.detach(this, Twilight.class);
                dmg = 0;
            }
        } else {
            if (buff(Twilight.class) != null) {
                dmg = 0;
            }
            if (buff(Bonk.BonkBuff.class) != null) dmg = 0;
        }

        if (buff(RadiantKnight.class) != null && subClass != HeroSubClass.FLASH) {
            float redu = 0.8f;
            if (subClass == HeroSubClass.SAVIOR) {
                redu = 0.6f;
                if (hasTalent(Talent.HOPELIGHT)) {
                    float hope = pointsInTalent(Talent.HOPELIGHT) * 0.05f;
                    redu -= hope;
                }
            }
            dmg *= redu;
            dmg -= 2;
        }

        if (buff(Heat.class) != null) {
            if (buff(Heat.class).state() == Heat.State.OVERHEAT && hasTalent(Talent.HEAT_OF_PROTECTION)) {
                dmg *= 0.95f - (pointsInTalent(Talent.HEAT_OF_PROTECTION) * 0.05f);
            }
        }


        int preHP = HP + shielding();
        super.damage(dmg, src);
        int postHP = HP + shielding();
        int effectiveDamage = preHP - postHP;

        //flash red when hit for serious damage.
        float percentDMG = effectiveDamage / (float) preHP; //percent of current HP that was taken
        float percentHP = 1 - ((HT - postHP) / (float) HT); //percent health after damage was taken
        // The flash intensity increases primarily based on damage taken and secondarily on missing HP.
        float flashIntensity = 0.25f * (percentDMG * percentDMG) / percentHP;
        //if the intensity is very low don't flash at all
        if (flashIntensity >= 0.05f) {
            flashIntensity = Math.min(1 / 3f, flashIntensity); //cap intensity at 1/3
            GameScene.flash((int) (0xFF * flashIntensity) << 16);
            if (isAlive()) {
                if (flashIntensity >= 1 / 6f) {
                    Sample.INSTANCE.play(Assets.Sounds.HEALTH_CRITICAL, 1 / 3f + flashIntensity * 2f);
                } else {
                    Sample.INSTANCE.play(Assets.Sounds.HEALTH_WARN, 1 / 3f + flashIntensity * 4f);
                }
            }
        }
    }

    public void checkVisibleMobs() {
        ArrayList<Mob> visible = new ArrayList<>();

        boolean newMob = false;

        Mob target = null;
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (fieldOfView[m.pos] && m.alignment == Alignment.ENEMY) {
                visible.add(m);
                if (!visibleEnemies.contains(m)) {
                    newMob = true;
                }

                if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1) {
                    if (target == null) {
                        target = m;
                    } else if (distance(target) > distance(m)) {
                        target = m;
                    }
                }
            }
        }

        Char lastTarget = QuickSlotButton.lastTarget;
        if (target != null && (lastTarget == null ||
                !lastTarget.isAlive() ||
                lastTarget.alignment == Alignment.ALLY ||
                !fieldOfView[lastTarget.pos])) {
            QuickSlotButton.target(target);
        }

        if (newMob) {
            interrupt();
            if (resting) {
                Dungeon.observe();
                resting = false;
            }
        }

        visibleEnemies = visible;
    }

    public int visibleEnemies() {
        return visibleEnemies.size();
    }

    public Mob visibleEnemy(int index) {
        return visibleEnemies.get(index % visibleEnemies.size());
    }

    private boolean walkingToVisibleTrapInFog = false;

    //FIXME this is a fairly crude way to track this, really it would be nice to have a short
    //history of hero actions
    public boolean justMoved = false;

    private boolean getCloser(final int target) {

        if (target == pos)
            return false;

        if (rooted) {
            Camera.main.shake(1, 1f);
            return false;
        }

        int step = -1;

        if (Dungeon.level.adjacent(pos, target)) {

            path = null;

            if (Actor.findChar(target) == null) {
                if (Dungeon.level.pit[target] && !flying && !Dungeon.level.solid[target]) {
                    if (!Chasm.jumpConfirmed) {
                        Chasm.heroJump(this);
                        interrupt();
                    } else {
                        Chasm.heroFall(target);
                    }
                    return false;
                }
                if (Dungeon.level.passable[target] || Dungeon.level.avoid[target]) {
                    step = target;
                }
                if (walkingToVisibleTrapInFog
                        && Dungeon.level.traps.get(target) != null
                        && Dungeon.level.traps.get(target).visible) {
                    return false;
                }
            }

        } else {

            boolean newPath = false;
            if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
                newPath = true;
            else if (path.getLast() != target)
                newPath = true;
            else {
                if (!Dungeon.level.passable[path.get(0)] || Actor.findChar(path.get(0)) != null) {
                    newPath = true;
                }
            }

            if (newPath) {

                int len = Dungeon.level.length();
                boolean[] p = Dungeon.level.passable;
                boolean[] v = Dungeon.level.visited;
                boolean[] m = Dungeon.level.mapped;
                boolean[] passable = new boolean[len];
                for (int i = 0; i < len; i++) {
                    passable[i] = p[i] && (v[i] || m[i]);
                }

                PathFinder.Path newpath = Dungeon.findPath(this, target, passable, fieldOfView, true);
                if (newpath != null && path != null && newpath.size() > 2 * path.size()) {
                    path = null;
                } else {
                    path = newpath;
                }
            }

            if (path == null) return false;
            step = path.removeFirst();

        }

        if (step != -1) {

            if (subClass == HeroSubClass.FREERUNNER) {
                Buff.affect(this, Momentum.class).gainStack();
            }

            if (subClass == HeroSubClass.STOME && buff(WindEnergy.class) != null) {
                boolean chack = true;
                if (hasTalent(Talent.ENERGY_STORAGE)) {
                    if (buff(WindEnergy.class).Eneray() <= 25+(pointsInTalent(Talent.ENERGY_STORAGE) * 15)) {
                       chack = false;
                    }
                }

                if (chack) buff(WindEnergy.class).UseEnergy(2);
            }

            float speed = speed();

            sprite.move(pos, step);
            move(step);

            spend(1 / speed);
            justMoved = true;

            search(false);

            return true;

        } else {

            return false;

        }

    }

    public boolean handle(int cell) {

        if (cell == -1) {
            return false;
        }

        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
            fieldOfView = new boolean[Dungeon.level.length()];
            Dungeon.level.updateFieldOfView(this, fieldOfView);
        }

        Char ch = Actor.findChar(cell);
        Heap heap = Dungeon.level.heaps.get(cell);

        if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {

            curAction = new HeroAction.Alchemy(cell);

        } else if (fieldOfView[cell] && ch instanceof Mob) {

            if (ch.alignment != Alignment.ENEMY && ch.buff(Amok.class) == null) {
                curAction = new HeroAction.Interact(ch);
            } else {
                curAction = new HeroAction.Attack(ch);
            }

        } else if (heap != null
                //moving to an item doesn't auto-pickup when enemies are near...
                && (visibleEnemies.size() == 0 || cell == pos ||
                //...but only for standard heaps, chests and similar open as normal.
                (heap.type != Type.HEAP && heap.type != Type.FOR_SALE && heap.type != Type.FOR_SALE_28F))) {

            switch (heap.type) {
                case HEAP:
                    curAction = new HeroAction.PickUp(cell);
                    break;
                case FOR_SALE: case FOR_SALE_28F:
                    curAction = heap.size() == 1 && heap.peek().value() > 0 ?
                            new HeroAction.Buy(cell) :
                            new HeroAction.PickUp(cell);
                    break;
                default:
                    curAction = new HeroAction.OpenChest(cell);
            }

        } else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {

            curAction = new HeroAction.Unlock(cell);

        } else if ((cell == Dungeon.level.exit || Dungeon.level.map[cell] == Terrain.EXIT || Dungeon.level.map[cell] == Terrain.UNLOCKED_EXIT)
                && Dungeon.depth < 41) {

            curAction = new HeroAction.Descend(cell);

        } else if (cell == Dungeon.level.entrance || Dungeon.level.map[cell] == Terrain.ENTRANCE) {

            curAction = new HeroAction.Ascend(cell);

        } else {

            if (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]
                    && Dungeon.level.traps.get(cell) != null && Dungeon.level.traps.get(cell).visible) {
                walkingToVisibleTrapInFog = true;
            } else {
                walkingToVisibleTrapInFog = false;
            }

            curAction = new HeroAction.Move(cell);
            lastAction = null;

        }

        return true;
    }

    public void earnExp(int exp, Class source) {

        this.exp += exp;
        float percent = exp / (float) maxExp();

        EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
        if (chains != null) chains.gainExp(percent);

        HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
        if (horn != null) horn.gainCharge(percent);

        AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
        if (kit != null) kit.gainCharge(percent);

        Berserk berserk = buff(Berserk.class);
        if (berserk != null) berserk.recover(percent);

        if (source != PotionOfExperience.class) {
            for (Item i : belongings) {
                i.onHeroGainExp(percent, this);
            }
        }

        boolean levelUp = false;
        while (this.exp >= maxExp()) {
            this.exp -= maxExp();
            if (lvl < MAX_LEVEL) {
                lvl++;
                levelUp = true;

                if (buff(ElixirOfMight.HTBoost.class) != null) {
                    buff(ElixirOfMight.HTBoost.class).onLevelUp();
                }

                AnnihilationGear Gear = this.belongings.getItem(AnnihilationGear.class);
                if (Gear != null) {
                    if (Gear.charge < Gear.chargeCap) Gear.SPCharge(Gear.chargeCap);
                    Gear.artsused = 0;
                }

                updateHT(true);
                attackSkill++;
                defenseSkill++;

            } else {
                Buff.prolong(this, Bless.class, Bless.DURATION);
                this.exp = 0;

                GLog.newLine();
                GLog.p(Messages.get(this, "level_cap"));
                Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
            }

        }

        if (levelUp) {

            if (sprite != null) {
                GLog.newLine();
                GLog.p(Messages.get(this, "new_level"));
                sprite.showStatus(CharSprite.POSITIVE, Messages.get(Hero.class, "level_up"));
                Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                if (lvl < Talent.tierLevelThresholds[Talent.MAX_TALENT_TIERS + 1]) {
                    GLog.newLine();
                    GLog.p(Messages.get(this, "new_talent"));
                    StatusPane.talentBlink = 10f;
                    WndHero.lastIdx = 1;
                }
            }

            Item.updateQuickslot();

            Badges.validateLevelReached();
        }
    }

    public int maxExp() {
        return maxExp(lvl);
    }

    public static int maxExp(int lvl) {
        return 5 + lvl * 5;
    }

    public boolean isStarving() {
        return Buff.affect(this, Hunger.class).isStarving();
    }

    @Override
    public void add(Buff buff) {

        if (buff(TimekeepersHourglass.timeStasis.class) != null)
            return;

        super.add(buff);

        if (sprite != null) {
            String msg = buff.heroMessage();
            if (msg != null) {
                GLog.w(msg);
            }

            if (buff instanceof Paralysis || buff instanceof Vertigo) {
                interrupt();
            }

        }

        BuffIndicator.refreshHero();
    }

    @Override
    public void remove(Buff buff) {
        super.remove(buff);

        BuffIndicator.refreshHero();
    }

    @Override
    public float stealth() {
        float stealth = super.stealth();

        if (belongings.armor != null) {
            stealth = belongings.armor.stealthFactor(this, stealth);
        }

        return stealth;
    }

    @Override
    public void die(Object cause) {

        curAction = null;

        Ankh ankh = null;

        Heat heat = buff(Heat.class);

        if (heat != null && !(cause instanceof ChaliceOfBlood) && !(cause instanceof SanktaBet)) {
            if (heat.state() == Heat.State.OVERHEAT &&
                    heat.power() >= (60-pointsInTalent(Talent.HEAT_OF_PROTECTION) * 5))
            {
                HP = 1;
                heat.powerDown();
                sprite.showStatus(CharSprite.WARNING, heat.power()+"%");
                return;
            }
        }

        //look for ankhs in player inventory, prioritize ones which are blessed.
        for (Item item : belongings) {
            if (item instanceof Ankh) {
                if (ankh == null || ((Ankh) item).isBlessed()) {
                    ankh = (Ankh) item;
                }
            }
        }

        if (ankh != null && ankh.isBlessed()) {
            int AnkhHP = HT/10;
            int barrior = this.HT/2;
            if (hasTalent(Talent.RESURGENCE)) {
                AnkhHP *= 1 + pointsInTalent(Talent.RESURGENCE) * 3;
                barrior *= 1f + (pointsInTalent(Talent.RESURGENCE) * 0.5f);
                Buff.affect(this, RadiantKnight.class, RadiantKnight.DURATION);
                GameScene.flash( 0x80FFFFFF );
            }

            HP = AnkhHP;

            //ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
            PotionOfHealing.cure(this);
            Buff.detach(this, Paralysis.class);
            Buff.affect(this, Barrier.class).incShield(barrior);
            Buff.prolong(this, BlobImmunity.class, BlobImmunity.DURATION / 4);
            spend(-cooldown());

            new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
            CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

            ankh.detach(belongings.backpack);

            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            GLog.w(Messages.get(this, "revive"));
            Statistics.ankhsUsed++;

            for (Char ch : Actor.chars()) {
                if (ch instanceof DriedRose.GhostHero) {
                    ((DriedRose.GhostHero) ch).sayAnhk();
                    return;
                }
            }

            return;
        }

        Actor.fixTime();
        super.die(cause);

        if (ankh == null) {

            reallyDie(cause);

        } else {

            Dungeon.deleteGame(GamesInProgress.curSlot, false);
            final Ankh finalAnkh = ankh;
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndResurrect(finalAnkh, cause));
                }
            });

        }
    }

    public static void reallyDie(Object cause) {

        int length = Dungeon.level.length();
        int[] map = Dungeon.level.map;
        boolean[] visited = Dungeon.level.visited;
        boolean[] discoverable = Dungeon.level.discoverable;

        for (int i = 0; i < length; i++) {

            int terr = map[i];

            if (discoverable[i]) {

                visited[i] = true;
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
                    Dungeon.level.discover(i);
                }
            }
        }

        Bones.leave();

        Dungeon.observe();
        GameScene.updateFog();

        Dungeon.hero.belongings.identify();

        int pos = Dungeon.hero.pos;

        ArrayList<Integer> passable = new ArrayList<>();
        for (Integer ofs : PathFinder.NEIGHBOURS8) {
            int cell = pos + ofs;
            if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Dungeon.level.heaps.get(cell) == null) {
                passable.add(cell);
            }
        }
        Collections.shuffle(passable);

        ArrayList<Item> items = new ArrayList<>(Dungeon.hero.belongings.backpack.items);
        for (Integer cell : passable) {
            if (items.isEmpty()) {
                break;
            }

            Item item = Random.element(items);
            Dungeon.level.drop(item, cell).sprite.drop(pos);
            items.remove(item);
        }

        GameScene.gameOver();

        if (cause instanceof Hero.Doom) {
            ((Hero.Doom) cause).onDeath();
        }

        Dungeon.deleteGame(GamesInProgress.curSlot, true);


    }

    //effectively cache this buff to prevent having to call buff(Berserk.class) a bunch.
    //This is relevant because we call isAlive during drawing, which has both performance
    //and concurrent modification implications if that method calls buff(Berserk.class)
    private Berserk berserk;

    @Override
    public boolean isAlive() {

        if (HP <= 0) {
            if (berserk == null) berserk = buff(Berserk.class);
            return berserk != null && berserk.berserking();
        } else {
            berserk = null;
            return super.isAlive();
        }
    }

    @Override
    public void move(int step) {
        boolean wasHighGrass = Dungeon.level.map[step] == Terrain.HIGH_GRASS;

        super.move(step);

        if (!flying) {
            if (Dungeon.level.water[pos]) {
                Sample.INSTANCE.play(Assets.Sounds.WATER, 1, Random.Float(0.8f, 1.25f));
            } else if (Dungeon.level.map[pos] == Terrain.EMPTY_SP) {
                Sample.INSTANCE.play(Assets.Sounds.STURDY, 1, Random.Float(0.96f, 1.05f));
            } else if (Dungeon.level.map[pos] == Terrain.GRASS
                    || Dungeon.level.map[pos] == Terrain.EMBERS
                    || Dungeon.level.map[pos] == Terrain.FURROWED_GRASS) {
                if (step == pos && wasHighGrass) {
                    Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float(0.96f, 1.05f));
                } else {
                    Sample.INSTANCE.play(Assets.Sounds.GRASS, 1, Random.Float(0.96f, 1.05f));
                }
            } else {
                Sample.INSTANCE.play(Assets.Sounds.STEP, 1, Random.Float(0.96f, 1.05f));
            }
        }
    }

    @Override
    public void onAttackComplete() {

        AttackIndicator.target(enemy);

        boolean hit = attack(enemy);

        Invisibility.dispel();
        if (this.buff(ActiveOriginium.class) == null) {
            spend(attackDelay());
        } else {
            spend(attackDelay() * 0.5f);
        }

        if (Dungeon.hero.hasTalent(Talent.SPARKOFLIFE)) {
            if (1 + Dungeon.hero.pointsInTalent(Talent.SPARKOFLIFE) > Random.Int(33)) {
                HP = Math.min(HP + HT / 20, HT);
            }
        }

        if (hit && subClass == HeroSubClass.GLADIATOR) {
            Buff.affect(this, Combo.class).hit(enemy);
        }

        if (hit && heroClass == HeroClass.CHEN && subClass != HeroSubClass.SPSHOOTER) {
            Buff.affect(this, ChenCombo.class).hit(enemy);
            if (hasTalent(Talent.BLADE_ART) && Random.Int(20) < 1 + pointsInTalent(Talent.BLADE_ART)) Buff.affect(this, ChenCombo.class).hit(enemy);
        }

        if (hit && subClass == HeroSubClass.KNIGHT) {
            Buff.affect(this, KnightSKILL.class).hit(enemy);
        }

        if (this.buff(ActiveOriginium.class) != null) {
            Buff.affect(this, ActiveOriginium.class).set(HT * 0.1f);
        }

        curAction = null;

        super.onAttackComplete();
    }

    @Override
    public void onMotionComplete() {
        GameScene.checkKeyHold();
    }

    @Override
    public void onOperateComplete() {

        if (curAction instanceof HeroAction.Unlock) {

            int doorCell = ((HeroAction.Unlock) curAction).dst;
            int door = Dungeon.level.map[doorCell];

            if (Dungeon.level.distance(pos, doorCell) <= 1) {
                boolean hasKey = true;
                if (door == Terrain.LOCKED_DOOR) {
                    hasKey = Notes.remove(new IronKey(Dungeon.depth));
                    if (hasKey) Level.set(doorCell, Terrain.DOOR);
                } else {
                    hasKey = Notes.remove(new SkeletonKey(Dungeon.depth));
                    if (hasKey) Level.set(doorCell, Terrain.UNLOCKED_EXIT);
                }

                if (hasKey) {
                    GameScene.updateKeyDisplay();
                    Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
                    GameScene.updateMap(doorCell);
                    spend(Key.TIME_TO_UNLOCK);
                }
            }

        } else if (curAction instanceof HeroAction.OpenChest) {

            Heap heap = Dungeon.level.heaps.get(((HeroAction.OpenChest) curAction).dst);

            if (Dungeon.level.distance(pos, heap.pos) <= 1) {
                boolean hasKey = true;
                if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
                    Sample.INSTANCE.play(Assets.Sounds.BONES);
                } else if (heap.type == Type.LOCKED_CHEST) {
                    hasKey = Notes.remove(new GoldenKey(Dungeon.depth));
                } else if (heap.type == Type.CRYSTAL_CHEST) {
                    hasKey = Notes.remove(new CrystalKey(Dungeon.depth));
                }

                if (hasKey) {
                    GameScene.updateKeyDisplay();
                    heap.open(this);
                    spend(Key.TIME_TO_UNLOCK);
                }
            }

        }
        curAction = null;

        super.onOperateComplete();
    }

    @Override
    public boolean isImmune(Class effect) {
        if (effect == Burning.class
                && belongings.armor != null
                && belongings.armor.hasGlyph(Brimstone.class, this)) {
            return true;
        }
        return super.isImmune(effect);
    }

    public boolean search(boolean intentional) {

        if (!isAlive()) return false;

        boolean smthFound = false;

        boolean circular = pointsInTalent(Talent.WIDE_SEARCH) == 1;
        int distance = heroClass == HeroClass.ROGUE ? 2 : 1;
        if (hasTalent(Talent.WIDE_SEARCH)) distance++;

        boolean foresight = buff(Foresight.class) != null;

        if (foresight) distance++;

        int cx = pos % Dungeon.level.width();
        int cy = pos / Dungeon.level.width();
        int ax = cx - distance;
        if (ax < 0) {
            ax = 0;
        }
        int bx = cx + distance;
        if (bx >= Dungeon.level.width()) {
            bx = Dungeon.level.width() - 1;
        }
        int ay = cy - distance;
        if (ay < 0) {
            ay = 0;
        }
        int by = cy + distance;
        if (by >= Dungeon.level.height()) {
            by = Dungeon.level.height() - 1;
        }

        TalismanOfForesight.Foresight talisman = buff(TalismanOfForesight.Foresight.class);
        boolean cursed = talisman != null && talisman.isCursed();

        for (int y = ay; y <= by; y++) {
            for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {

                if (circular && Math.abs(x - cx) - 1 > ShadowCaster.rounding[distance][distance - Math.abs(y - cy)]) {
                    continue;
                }

                if (fieldOfView[p] && p != pos) {

                    if (intentional) {
                        GameScene.effectOverFog(new CheckedCell(p, pos));
                    }

                    if (Dungeon.level.secret[p]) {

                        Trap trap = Dungeon.level.traps.get(p);
                        float chance;

                        //searches aided by foresight always succeed, even if trap isn't searchable
                        if (foresight) {
                            chance = 1f;

                            //otherwise if the trap isn't searchable, searching always fails
                        } else if (trap != null && !trap.canBeSearched) {
                            chance = 0f;

                            //intentional searches always succeed against regular traps and doors
                        } else if (intentional) {
                            chance = 1f;

                            //unintentional searches always fail with a cursed talisman
                        } else if (cursed) {
                            chance = 0f;

                            //unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
                        } else if (Dungeon.level.map[p] == Terrain.SECRET_TRAP) {
                            chance = 0.4f - (Dungeon.depth / 250f);

                            //unintentional door detection scales from 20% at floor 0 to 0% at floor 20
                        } else {
                            chance = 0.2f - (Dungeon.depth / 100f);
                        }

                        if (Random.Float() < chance) {

                            int oldValue = Dungeon.level.map[p];

                            GameScene.discoverTile(p, oldValue);

                            Dungeon.level.discover(p);

                            ScrollOfMagicMapping.discover(p);

                            smthFound = true;

                            if (talisman != null) {
                                if (oldValue == Terrain.SECRET_TRAP) {
                                    talisman.charge(2);
                                } else if (oldValue == Terrain.SECRET_DOOR) {
                                    talisman.charge(10);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (intentional) {
            sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "search"));
            sprite.operate(pos);
            if (!Dungeon.level.locked) {
                if (cursed) {
                    GLog.n(Messages.get(this, "search_distracted"));
                    Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - (2 * HUNGER_FOR_SEARCH));
                } else {
                    Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
                }
            }
            spendAndNext(TIME_TO_SEARCH);

        }

        if (smthFound) {
            GLog.w(Messages.get(this, "noticed_smth"));
            Sample.INSTANCE.play(Assets.Sounds.SECRET);
            interrupt();
        }

        return smthFound;
    }

    public void resurrect(int resetLevel) {

        HP = HT;
        Dungeon.gold = 0;
        exp = 0;

        belongings.resurrect(resetLevel);

        live();
    }

    @Override
    public void next() {
        if (isAlive())
            super.next();
    }

    public static interface Doom {
        public void onDeath();
    }


    public void SetSkill1Num(int SkillNumber) {
        SK1num = SkillNumber;
    }

    public void SetSkill2Num(int SkillNumber) {
        SK2num = SkillNumber;
    }

    public void SetSkill3Num(int SkillNumber) {
        SK3num = SkillNumber;
    }

    private void loadSkill1(int SkillNumber) {
        switch (SkillNumber) {
            default:
                SK1 = null;
                break;
            case 1:
                SK1 = new TacticalChanting();
                break;
            case 2:
                SK1 = new PowerfulStrike();
                break;
            case 3:
                SK1 = new ExecutionMode();
                break;
            case 4:
                SK1 = new Fate();
                break;
            case 5:
                SK1 = new Panorama();
                break;
            case 6:
                SK1 = new FoodPrep();
                break;
            case 7:
                SK1 = new ChainHook();
                break;
            case 8:
                SK1 = new Whispers();
                break;
            case 9:
                SK1 = new CrimsonCutter();
                break;
            case 10:
                SK1 = new Shinkageryu();
                break;
            case 11:
                SK1 = new FierceGlare();
                break;
            case 12:
                SK1 = new Camouflage();
                break;
            case 13:
                SK1 = new WolfSpirit();
                break;
            case 14:
                SK1 = new Thoughts();
                break;
            case 15:
                SK1 = new HotBlade();
                break;
            case 16:
                SK1 = new SpreadSpores();
                break;
            case 17:
                SK1 = new PhantomMirror();
                break;
            case 18:
                SK1 = new LiveStart();
                break;
            case 19:
                SK1 = new Hikari();
                break;
            case 20:
                SK1 = new SoulAbsorption();
                break;
        }
    }

    private void loadSkill2(int SkillNumber) {
        switch (SkillNumber) {
            default:
                SK2 = null;
                break;
            case 1:
                SK2 = new WolfPack();
                break;
            case 2:
                SK2 = new MentalBurst();
                break;
            case 3:
                SK2 = new Reflow();
                break;
            case 4:
                SK2 = new EmergencyDefibrillator();
                break;
            case 5:
                SK2 = new Jackinthebox();
                break;
            case 6:
                SK2 = new RockfailHammer();
                break;
            case 7:
                SK2 = new ChargingPS();
                break;
            case 8:
                SK2 = new NeverBackDown();
                break;
            case 9:
                SK2 = new CoverSmoke();
                break;
            case 10:
                SK2 = new BenasProtracto();
                break;
            case 11:
                SK2 = new AncientKin();
                break;
            case 12:
                SK2 = new LandingStrike();
                break;
            case 13:
                SK2 = new Dreamland();
                break;
            case 14:
                SK2 = new FlashShield();
                break;
            case 15:
                SK2 = new Nervous();
                break;
            case 16:
                SK2 = new DeepHealing();
                break;
            case 17:
                SK2 = new Spikes();
                break;
            case 18:
                SK2 = new Genesis();
                break;
            case 19:
                SK2 = new DeepSeaPredators();
                break;
            case 20:
                SK2 = new BreaktheDawn();
                break;
        }
    }

    private void loadSkill3(int SkillNumber) {
        switch (SkillNumber) {
            default:
                SK3 = null;
                break;
            case 1:
                SK3 = new ShadowAssault();
                break;
            case 2:
                SK3 = new SoaringFeather();
                break;
            case 3:
                SK3 = new SBurst();
                break;
            case 4:
                SK3 = new NigetRaid();
                break;
            case 5:
                SK3 = new TerminationT();
                break;
            case 6:
                SK3 = new TrueSilverSlash();
                break;
            case 7:
                SK3 = new YourWish();
                break;
            case 8:
                SK3 = new EveryoneTogether();
                break;
            case 9:
                SK3 = new ExtremeSharpness();
                break;
            case 10:
                SK3 = new ShiningSun();
                break;
        }

    }
}
