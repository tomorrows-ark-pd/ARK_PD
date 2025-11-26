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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ceylon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dario;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FrostLeaf;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Jessica;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC_Phantom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.GavialBossLevel1;
import com.shatteredpixel.shatteredpixeldungeon.levels.GavialBossLevel2;
import com.shatteredpixel.shatteredpixeldungeon.levels.GavialLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewRhodesLevel1;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewRhodesLevel2;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewRhodesLevel3;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewRhodesLevel4;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastShopLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewHallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewPrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaBossLevel1;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaBossLevel2;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaLevel_part1;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaLevel_part2;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SiestaBossLevel_part1;
import com.shatteredpixel.shatteredpixeldungeon.levels.SiestaBossLevel_part2;
import com.shatteredpixel.shatteredpixeldungeon.levels.SiestaLevel_part1;
import com.shatteredpixel.shatteredpixeldungeon.levels.SiestaLevel_part2;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Dungeon {

	//enum of items which have limited spawns, records how many have spawned
	//could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
	public static enum LimitedDrops {
		//limited world drops
		STRENGTH_POTIONS,
		UPGRADE_SCROLLS,
		ARCANE_STYLI,

		//Health potion sources
		//enemies
		SWARM_HP,
		NECRO_HP,
		BAT_HP,
		WARLOCK_HP,
		SNIPER_HP,
		//Demon spawners are already limited in their spawnrate, no need to limit their health drops
		//alchemy
		COOKING_HP,
		BLANDFRUIT_SEED,

		//Other limited enemy drops
		SLIME_WEP,
		SKELE_WEP,
		THEIF_MISC,
		GUARD_ARM,
		SHAMAN_WAND,
		DM200_EQUIP,
		GOLEM_EQUIP,

		//containers
		DEW_VIAL,
		VELVET_POUCH,
		SCROLL_HOLDER,
		POTION_BANDOLIER,
		MAGICAL_HOLSTER,
		FOOD_BAG;

		public int count = 0;

		//for items which can only be dropped once, should directly access count otherwise.
		public boolean dropped(){
			return count != 0;
		}
		public void drop(){
			count = 1;
		}

		public static void reset(){
			for (LimitedDrops lim : values()){
				lim.count = 0;
			}
		}

		public static void store( Bundle bundle ){
			for (LimitedDrops lim : values()){
				bundle.put(lim.name(), lim.count);
			}
		}

		public static void restore( Bundle bundle ){
			for (LimitedDrops lim : values()){
				if (bundle.contains(lim.name())){
					lim.count = bundle.getInt(lim.name());
				} else {
					lim.count = 0;
				}
				
			}
		}

	}

	public static int challenges;
	public static int mobsToChampion;

	public static Hero hero;
	public static Level level;

	public static QuickSlot quickslot = new QuickSlot();
	
	public static int depth;
	public static int gold;
	public static int guardquest;
	public static int acequest;
	public static int cautusquset;

	public static int eazymode;
	public static int skin_ch;

	public static int mboss4;
	public static int mboss9;
	public static int mboss14;
	public static int mboss19;

	public static int talucount;
	public static int siesta1_bosspower;

	public static int mulaCount;

	public static boolean extrastage_Gavial; // true라면 가비알 스테이지 실행
	public static boolean extrastage_Sea;

	public static boolean isPray; // 프리스티스를 위한 기도를 하였는가?
	public static boolean killcat; // 엔딩 씬에서 켈시 하극상 출현용.

	public static int QuestCatPoint;

	public static boolean buyFoodbox;
	public static boolean buyPotionbox;
	public static boolean buyScrollbox;
	public static boolean buyIdentifybox;
	public static boolean buyHealbox;
	public static boolean buyWandbox;
	public static boolean buyTransbox;
	public static boolean buyRingbox;
	
	public static HashSet<Integer> chapters;

	public static SparseArray<ArrayList<Item>> droppedItems;
	public static SparseArray<ArrayList<Item>> portedItems;

	public static int version;
	public static long seed;
	
	public static void init() {

		version = Game.versionCode;
		challenges = SPDSettings.challenges();
		mobsToChampion = -1;

		seed = DungeonSeed.randomSeed();

		Actor.clear();
		Actor.resetNextID();
		
		Random.pushGenerator( seed );

			Scroll.initLabels();
			Potion.initColors();
			Ring.initGems();

			SpecialRoom.initForRun();
			SecretRoom.initForRun();

		Random.resetGenerators();
		
		Statistics.reset();
		Notes.reset();

		quickslot.reset();
		QuickSlotButton.reset();
		
		depth = 0; //@
		gold = 300;
		cautusquset = -1;
		guardquest = -1;
		acequest = -1;

		eazymode = -1;
		skin_ch = 0;

		mboss4 = 1;
		mboss9 = 1;
		mboss14 = 1;
		mboss19 = 1;

		talucount = 0;
		siesta1_bosspower = 4;

		isPray = false;
		killcat = false;
		extrastage_Gavial = false;
		extrastage_Sea = false;
		mulaCount = 0;

		Jessica.QuestClear = false;
		NPC_Phantom.QuestClear = false;
		FrostLeaf.QuestClear = false;

		buyFoodbox = false;
		buyPotionbox = false;
		buyScrollbox = false;
		buyIdentifybox = false;
		buyHealbox = false;
		buyWandbox = false;
		buyTransbox = false;
		buyRingbox = false;

		QuestCatPoint = Random.Int(2);

		droppedItems = new SparseArray<>();
		portedItems = new SparseArray<>();

		LimitedDrops.reset();
		
		chapters = new HashSet<>();
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
        Imp.Quest.reset();
        Dario.Quest.reset();

		Generator.fullReset();
		hero = new Hero();
		hero.live();
		
		Badges.reset();
		
		GamesInProgress.selectedClass.initHero( hero );
	}

	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}
	
	public static Level newLevel() {
		
		Dungeon.level = null;
		Actor.clear();
		
		depth++;
		if (depth > Statistics.deepestFloor) {
			if (depth != 27 && depth != 28) Statistics.deepestFloor = depth;
			if (Statistics.deepestFloor <= 1) Statistics.deepestFloor = 1;
			
			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}
		
		Level level;
		switch (depth) {
		case 1:
		case 2:
		case 3:
		case 4:
			level = new SewerLevel();
			break;
		case 5:
			level = new SewerBossLevel();
			break;
		case 6:
		case 7:
		case 8:
		case 9:
			level = new PrisonLevel();
			break;
		case 10:
			level = new NewPrisonBossLevel();
			break;
		case 11:
		case 12:
		case 13:
		case 14:
			level = new CavesLevel();
			break;
		case 15:
			level = new NewCavesBossLevel();
			break;
		case 16:
		case 17:
		case 18:
		case 19:
			level = new CityLevel();
			break;
		case 20:
			level = new NewCityBossLevel();
			break;
		case 21:
			//logic for old city boss levels, need to spawn a shop on floor 21
			try {
				Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.depthFile(GamesInProgress.curSlot, 20));
				Class cls = bundle.getBundle(LEVEL).getClass("__className");
				if (cls == NewCityBossLevel.class) {
					level = new HallsLevel();
				} else {
					level = new LastShopLevel();
				}
			} catch (Exception e) {
				TomorrowRogueNight.reportException(e);
				level = new HallsLevel();
			}
			break;
		case 22:
		case 23:
		case 24:
			level = new HallsLevel();
			break;
		case 25:
			level = new NewHallsBossLevel();
			break;
		case 26:
			level = new LastLevel();
			break;
		case 27:
			level = new NewRhodesLevel1();
			break;
		case 28:
			level = new NewRhodesLevel2();
			break;
		case 29:
			level = new NewRhodesLevel3();
			break;
		case 30:
			level = new NewRhodesLevel4();
			break;
		case 31:
			case 32:
			case 33:
			case 34:
				if (extrastage_Gavial) level = new GavialLevel();
				else if (extrastage_Sea) level = new SeaLevel_part1();
				else level = new SiestaLevel_part1();
				break;
			case 35:
				if (extrastage_Gavial) level = new GavialBossLevel1();
				else if (extrastage_Sea) level = new SeaBossLevel1();
				else level = new SiestaBossLevel_part1();
				break;
			case 36:
			case 37:
			case 38:
			case 39:
				if (extrastage_Gavial) level = new GavialLevel();
				else if (extrastage_Sea) level = new SeaLevel_part2();
				else level = new SiestaLevel_part2();
				break;
			case 40:
				if (extrastage_Gavial) level = new GavialBossLevel2();
				else if (extrastage_Sea) level = new SeaBossLevel2();
				else level = new SiestaBossLevel_part2();
				break;
		default:
			level = new DeadEndLevel();
			Statistics.deepestFloor--;
		}
		
		level.create();
		
		Statistics.qualifiedForNoKilling = !bossLevel();
		
		return level;
	}
	
	public static void resetLevel() {
		
		Actor.clear();
		
		level.reset();
		switchLevel( level, level.entrance );
	}

	public static long seedCurDepth(){
		return seedForDepth(depth);
	}

	public static long seedForDepth(int depth){
		Random.pushGenerator( seed );

			for (int i = 0; i < depth; i ++) {
				Random.Long(); //we don't care about these values, just need to go through them
			}
			long result = Random.Long();

		Random.popGenerator();
		return result;
	}
	
	public static boolean shopOnLevel() {
		return depth == 6 || depth == 11 || depth == 16 || depth == 31 || depth == 36;
	}
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}
	
	public static boolean bossLevel( int depth ) {
		return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25 || depth == 35 || depth == 40;
	}
	
	public static void switchLevel( final Level level, int pos ) {
		
		if (pos == -2){
			pos = level.exit;
		} else if (pos < 0 || pos >= level.length() || (!level.passable[pos] && !level.avoid[pos])){
			pos = level.entrance;
		}
		
		PathFinder.setMapSize(level.width(), level.height());
		
		Dungeon.level = level;
		Mob.restoreAllies( level, pos );
		Actor.init();

		level.addRespawner();

		hero.pos = pos;
		
		for(Mob m : level.mobs){
			if (m.pos == hero.pos){
				//displace mob
				for(int i : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(m.pos+i) == null && level.passable[m.pos + i]){
						m.pos += i;
						break;
					}
				}
			}
		}
		
		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		
		hero.curAction = hero.lastAction = null;
		
		observe();
		try {
			saveAll();
		} catch (IOException e) {
			TomorrowRogueNight.reportException(e);
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
		}
	}

	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<>() );
		}
		dropped.add( item );
	}

	public static boolean posNeeded() {
		//2 POS each floor set
		if (Dungeon.depth < 26) {
			int posLeftThisSet = 2 - (LimitedDrops.STRENGTH_POTIONS.count - (depth / 5) * 2);
			if (posLeftThisSet <= 0) return false;

			int floorThisSet = (depth % 5);

			//pos drops every two floors, (numbers 1-2, and 3-4) with a 50% chance for the earlier one each time.
			int targetPOSLeft = 2 - floorThisSet / 2;
			if (floorThisSet % 2 == 1 && Random.Int(2) == 0) targetPOSLeft--;

			if (targetPOSLeft < posLeftThisSet) return true;
			else return false;
		}
		else return false;

	}
	
	public static boolean souNeeded() {
		int souLeftThisSet;
		//3 SOU each floor set, 1.5 (rounded) on forbidden runes challenge
		if (Dungeon.depth < 26) {
			if (isChallenged(Challenges.NO_SCROLLS)) {
				souLeftThisSet = Math.round(1.5f - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 1.5f));
			} else {
				souLeftThisSet = 3 - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 3);
			}
			if (souLeftThisSet <= 0) return false;

			int floorThisSet = (depth % 5);
			//chance is floors left / scrolls left
			return Random.Int(5 - floorThisSet) < souLeftThisSet;
		}
		return false;
	}
	
	public static boolean asNeeded() {
		//1 AS each floor set
		int asLeftThisSet = 1 - (LimitedDrops.ARCANE_STYLI.count - (depth / 5));
		if (asLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < asLeftThisSet;
	}
	
	private static final String VERSION		= "version";
	private static final String SEED		= "seed";
	private static final String CHALLENGES	= "challenges";
	private static final String MOBS_TO_CHAMPION	= "mobs_to_champion";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String DROPPED     = "dropped%d";
	private static final String PORTED      = "ported%d";
	private static final String LEVEL		= "level";
	private static final String LIMDROPS    = "limited_drops";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	private static final String GUARD_Q		= "guardquest";
	private static final String ACE_Q		= "acequest";
	private static final String cautus		= "cautusquset";
	private static final String MBOSS4		= "mboss4";
	private static final String MBOSS9		= "mboss9";
	private static final String MBOSS14		= "mboss14";
	private static final String MBOSS19		= "mboss19";
	private static final String EAZYMODE    = "eazymode";
	private static final String SKIN    = "skin_ch";
	private static final String PRAY    = "isPray";
	private static final String END_CAT    = "killcat";
	private static final String TALU    = "talucount";
	private static final String SIEBOSS1    = "siesta1_bosspower";
	private static final String GAVIAL    = "extrastage_Gavial";
	private static final String SEA    = "extrastage_SeA";
	private static final String CATQUEST    = "QuestCatPoint";
	private static final String PHANTOM_QUESTCLEAR    = "NPC_Phantom.QuestClear";
	private static final String JESI_QUESTCLEAR    = "Jessica.QuestClear";
	private static final String LEAF_QUESTCLEAR    = "FrostLeaf.QuestClear";

	private static final String BUY_FOOD = "buyFoodbox";
	private static final String BUY_POTION = "buyPotionbox";
	private static final String BUY_SCROLL = "buyScrollbox";
	private static final String BUY_IDENTIFY = "buyIdentifybox";
	private static final String BUY_HEAL = "buyHealbox";
	private static final String BUY_WAND = "buyWandbox";
	private static final String BUY_TRANS = "buyTransbox";
	private static final String BUY_RING = "buyRingbox";

	private static final String MULA_COUNT = "mulaCount";

	public static void saveGame(int save ) {
		try {
			Bundle bundle = new Bundle();
			version = Game.versionCode;
			bundle.put( VERSION, version );
			bundle.put( SEED, seed );
			bundle.put( CHALLENGES, challenges );
			bundle.put( MOBS_TO_CHAMPION, mobsToChampion );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );
			bundle.put( cautus, cautusquset);
			bundle.put( GUARD_Q, guardquest);
			bundle.put( ACE_Q, acequest);
			bundle.put (MBOSS4, mboss4);
			bundle.put (MBOSS9, mboss9);
			bundle.put (MBOSS14, mboss14);
			bundle.put (MBOSS19, mboss19);
			bundle.put (EAZYMODE, eazymode);
			bundle.put (SKIN, skin_ch);
			bundle.put (PRAY, isPray);
			bundle.put (END_CAT, killcat);
			bundle.put (TALU, talucount);
			bundle.put (SIEBOSS1, siesta1_bosspower);
			bundle.put (GAVIAL, extrastage_Gavial);
			bundle.put (SEA, extrastage_Sea);
			bundle.put (MULA_COUNT, mulaCount);

			bundle.put (PHANTOM_QUESTCLEAR, NPC_Phantom.QuestClear);
			bundle.put (JESI_QUESTCLEAR, Jessica.QuestClear);
			bundle.put (LEAF_QUESTCLEAR, FrostLeaf.QuestClear);

			bundle.put (CATQUEST, QuestCatPoint);

			bundle.put (BUY_FOOD, buyFoodbox);
			bundle.put (BUY_POTION, buyPotionbox);
			bundle.put (BUY_SCROLL, buyScrollbox);
			bundle.put (BUY_IDENTIFY, buyIdentifybox);
			bundle.put (BUY_HEAL, buyHealbox);
			bundle.put (BUY_WAND, buyWandbox);
			bundle.put (BUY_TRANS, buyTransbox);
			bundle.put (BUY_RING, buyRingbox);


			for (int d : droppedItems.keyArray()) {
				bundle.put(Messages.format(DROPPED, d), droppedItems.get(d));
			}
			
			for (int p : portedItems.keyArray()){
				bundle.put(Messages.format(PORTED, p), portedItems.get(p));
			}

			quickslot.storePlaceholders( bundle );

			Bundle limDrops = new Bundle();
			LimitedDrops.store( limDrops );
			bundle.put ( LIMDROPS, limDrops );
			
			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			Ceylon      .Quest.storeInBundle( quests );
            Dario       .Quest.storeInBundle( quests );
            bundle.put( QUESTS, quests );
			
			SpecialRoom.storeRoomsInBundle( bundle );
			SecretRoom.storeRoomsInBundle( bundle );
			
			Statistics.storeInBundle( bundle );
			Notes.storeInBundle( bundle );
			Generator.storeInBundle( bundle );
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Ring.save( bundle );

			Actor.storeNextID( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );
			
			FileUtils.bundleToFile( GamesInProgress.gameFile(save), bundle);
			
		} catch (IOException e) {
			GamesInProgress.setUnknown( save );
			TomorrowRogueNight.reportException(e);
		}
	}
	
	public static void saveLevel( int save ) throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		FileUtils.bundleToFile(GamesInProgress.depthFile( save, depth), bundle);
	}
	
	public static void saveAll() throws IOException {
		if (hero != null && hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( GamesInProgress.curSlot );
			saveLevel( GamesInProgress.curSlot );

			GamesInProgress.set( GamesInProgress.curSlot, depth, challenges, hero );

		}
	}
	
	public static void loadGame( int save ) throws IOException {
		loadGame( save, true );
	}
	
	public static void loadGame( int save, boolean fullLoad ) throws IOException {
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.gameFile( save ) );

		version = bundle.getInt( VERSION );

		seed = bundle.contains( SEED ) ? bundle.getLong( SEED ) : DungeonSeed.randomSeed();

		Actor.restoreNextID( bundle );

		quickslot.reset();
		QuickSlotButton.reset();

		Dungeon.challenges = bundle.getInt( CHALLENGES );
		Dungeon.mobsToChampion = bundle.getInt( MOBS_TO_CHAMPION );
		
		Dungeon.level = null;
		Dungeon.depth = -1;
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Ring.restore( bundle );

		quickslot.restorePlaceholders( bundle );
		
		if (fullLoad) {
			
			LimitedDrops.restore( bundle.getBundle(LIMDROPS) );

			chapters = new HashSet<>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
				Ceylon.Quest.restoreFromBundle( quests );
                Dario.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
				Ceylon.Quest.reset();
                Dario.Quest.reset();
			}
			
			SpecialRoom.restoreRoomsFromBundle(bundle);
			SecretRoom.restoreRoomsFromBundle(bundle);
		}
		
		Bundle badges = bundle.getBundle(BADGES);
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		
		Notes.restoreFromBundle( bundle );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );
		
		gold = bundle.getInt( GOLD );
		depth = bundle.getInt( DEPTH );

		guardquest = bundle.getInt(GUARD_Q);
		acequest = bundle.getInt(ACE_Q);
		cautusquset = bundle.getInt(cautus);

		mboss4 = bundle.getInt(MBOSS4);
		mboss9 = bundle.getInt(MBOSS9);
		mboss14 = bundle.getInt(MBOSS14);
		mboss19 = bundle.getInt(MBOSS19);

		eazymode = bundle.getInt(EAZYMODE);
		skin_ch = bundle.getInt(SKIN);

		isPray = bundle.getBoolean(PRAY);
		killcat = bundle.getBoolean(END_CAT);
		talucount = bundle.getInt(TALU);
		siesta1_bosspower = bundle.getInt(SIEBOSS1);
		extrastage_Gavial = bundle.getBoolean(GAVIAL);
		extrastage_Sea = bundle.getBoolean(SEA);

		mulaCount = bundle.getInt(MULA_COUNT);

		QuestCatPoint = bundle.getInt(CATQUEST);

		NPC_Phantom.QuestClear = bundle.getBoolean(PHANTOM_QUESTCLEAR);
		Jessica.QuestClear = bundle.getBoolean(JESI_QUESTCLEAR);
		FrostLeaf.QuestClear = bundle.getBoolean(LEAF_QUESTCLEAR);

		buyFoodbox = bundle.getBoolean(BUY_FOOD);
		buyPotionbox = bundle.getBoolean(BUY_POTION);
		buyScrollbox = bundle.getBoolean(BUY_SCROLL);
		buyIdentifybox = bundle.getBoolean(BUY_IDENTIFY);
		buyHealbox = bundle.getBoolean(BUY_HEAL);
		buyWandbox = bundle.getBoolean(BUY_WAND);
		buyTransbox = bundle.getBoolean(BUY_TRANS);
		buyRingbox = bundle.getBoolean(BUY_RING);
		
		Statistics.restoreFromBundle( bundle );
		Generator.restoreFromBundle( bundle );

		droppedItems = new SparseArray<>();
		portedItems = new SparseArray<>();
		for (int i=1; i <= 26; i++) {
			
			//dropped items
			ArrayList<Item> items = new ArrayList<>();
			if (bundle.contains(Messages.format( DROPPED, i )))
				for (Bundlable b : bundle.getCollection( Messages.format( DROPPED, i ) ) ) {
					items.add( (Item)b );
				}
			if (!items.isEmpty()) {
				droppedItems.put( i, items );
			}
			
			//ported items
			items = new ArrayList<>();
			if (bundle.contains(Messages.format( PORTED, i )))
				for (Bundlable b : bundle.getCollection( Messages.format( PORTED, i ) ) ) {
					items.add( (Item)b );
				}
			if (!items.isEmpty()) {
				portedItems.put( i, items );
			}
		}
	}
	
	public static Level loadLevel( int save ) throws IOException {
		
		Dungeon.level = null;
		Actor.clear();
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.depthFile( save, depth)) ;
		
		Level level = (Level)bundle.get( LEVEL );
		
		if (level == null){
			throw new IOException();
		} else {
			return level;
		}
	}
	
	public static void deleteGame( int save, boolean deleteLevels ) {
		
		FileUtils.deleteFile(GamesInProgress.gameFile(save));
		
		if (deleteLevels) {
			FileUtils.deleteDir(GamesInProgress.gameFolder(save));
		}
		
		GamesInProgress.delete( save );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.version = bundle.getInt( VERSION );
		info.challenges = bundle.getInt( CHALLENGES );
		Hero.preview( info, bundle.getBundle( HERO ) );
		Statistics.preview( info, bundle );
	}
	
	public static void fail( Class cause ) {
		if (hero.belongings.getItem( Ankh.class ) == null) {
			Rankings.INSTANCE.submit( false, cause );
		}
	}
	
	public static void win( Class cause ) {

		hero.belongings.identify();

		Rankings.INSTANCE.submit( true, cause );
	}

	public static void observe(){
		int dist = 8 + 2*Dungeon.hero.pointsInTalent(Talent.FARSIGHT);
		observe( dist+1 );
	}
	
	public static void observe( int dist ) {

		if (level == null) {
			return;
		}
		
		level.updateFieldOfView(hero, level.heroFOV);

		int x = hero.pos % level.width();
		int y = hero.pos / level.width();
	
		//left, right, top, bottom
		int l = Math.max( 0, x - dist );
		int r = Math.min( x + dist, level.width() - 1 );
		int t = Math.max( 0, y - dist );
		int b = Math.min( y + dist, level.height() - 1 );
	
		int width = r - l + 1;
		int height = b - t + 1;
		
		int pos = l + t * level.width();
	
		for (int i = t; i <= b; i++) {
			BArray.or( level.visited, level.heroFOV, pos, width, level.visited );
			pos+=level.width();
		}
	
		GameScene.updateFog(l, t, width, height);
		
		if (hero.buff(MindVision.class) != null){
			for (Mob m : level.mobs.toArray(new Mob[0])){
				BArray.or( level.visited, level.heroFOV, m.pos - 1 - level.width(), 3, level.visited );
				BArray.or( level.visited, level.heroFOV, m.pos, 3, level.visited );
				BArray.or( level.visited, level.heroFOV, m.pos - 1 + level.width(), 3, level.visited );
				//updates adjacent cells too
				GameScene.updateFog(m.pos, 2);
			}
		}
		
		if (hero.buff(Awareness.class) != null){
			for (Heap h : level.heaps.valueList()){
				BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
				GameScene.updateFog(h.pos, 2);
			}
		}

		for (TalismanOfForesight.CharAwareness c : hero.buffs(TalismanOfForesight.CharAwareness.class)){
			if (Dungeon.depth != c.depth) continue;
			Char ch = (Char) Actor.findById(c.charID);
			if (ch == null) continue;
			BArray.or( level.visited, level.heroFOV, ch.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, ch.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, ch.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(ch.pos, 2);
		}

		for (TalismanOfForesight.HeapAwareness h : hero.buffs(TalismanOfForesight.HeapAwareness.class)){
			if (Dungeon.depth != h.depth) continue;
			BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(h.pos, 2);
		}

		for (RevealedArea a : hero.buffs(RevealedArea.class)){
			if (Dungeon.depth != a.depth) continue;
			BArray.or( level.visited, level.heroFOV, a.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, a.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, a.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(a.pos, 2);
		}

		GameScene.afterObserve();
	}

	//we store this to avoid having to re-allocate the array with each pathfind
	private static boolean[] passable;

	private static void setupPassable(){
		if (passable == null || passable.length != Dungeon.level.length())
			passable = new boolean[Dungeon.level.length()];
		else
			BArray.setFalse(passable);
	}

	public static PathFinder.Path findPath(Char ch, int to, boolean[] pass, boolean[] vis, boolean chars) {

		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		if (chars && Char.hasProp(ch, Char.Property.LARGE)){
			BArray.and( passable, Dungeon.level.openSpace, passable );
		}

		if (chars) {
			for (Char c : Actor.chars()) {
				if (vis[c.pos]) {
					passable[c.pos] = false;
				}
			}
		}

		return PathFinder.find( ch.pos, to, passable );

	}
	
	public static int findStep(Char ch, int to, boolean[] pass, boolean[] visible, boolean chars ) {

		if (Dungeon.level.adjacent( ch.pos, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Dungeon.level.avoid[to]) ? to : -1;
		}

		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		if (Char.hasProp(ch, Char.Property.LARGE)){
			BArray.and( passable, Dungeon.level.openSpace, passable );
		}

		if (chars){
			for (Char c : Actor.chars()) {
				if (visible[c.pos]) {
					passable[c.pos] = false;
				}
			}
		}
		
		return PathFinder.getStep( ch.pos, to, passable );

	}
	
	public static int flee( Char ch, int from, boolean[] pass, boolean[] visible, boolean chars ) {

		setupPassable();
		if (ch.flying) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		if (Char.hasProp(ch, Char.Property.LARGE)){
			BArray.and( passable, Dungeon.level.openSpace, passable );
		}

		passable[ch.pos] = true;

		//only consider chars impassable if our retreat path runs into them
		int step = PathFinder.getStepBack( ch.pos, from, passable );
		while (step != -1 && Actor.findChar(step) != null){
			passable[step] = false;
			step = PathFinder.getStepBack( ch.pos, from, passable );
		}
		return step;
		
	}

}
