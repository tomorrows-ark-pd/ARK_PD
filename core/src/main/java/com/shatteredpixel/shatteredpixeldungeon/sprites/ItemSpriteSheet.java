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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ItemSpriteSheet {

	private static final int WIDTH = 32;
	public static final int SIZE = 32;

	public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEMS, SIZE, SIZE );

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

	private static void assignItemRect( int item, int width, int height ){
		int x = (item % WIDTH) * SIZE;
		int y = (item / WIDTH) * SIZE;
		film.add( item, x, y, x+width * 2, y+height * 2);
	}

	private static final int PLACEHOLDERS   =                               xy(1, 1);   //16 slots
	//SOMETHING is the default item sprite at position 0. May show up ingame if there are bugs.
	public static final int SOMETHING       = PLACEHOLDERS+0;
	public static final int WEAPON_HOLDER   = PLACEHOLDERS+1;
	public static final int ARMOR_HOLDER    = PLACEHOLDERS+2;
	public static final int MISSILE_HOLDER  = PLACEHOLDERS+3;
	public static final int WAND_HOLDER     = PLACEHOLDERS+4;
	public static final int RING_HOLDER     = PLACEHOLDERS+5;
	public static final int ARTIFACT_HOLDER = PLACEHOLDERS+6;
	public static final int FOOD_HOLDER     = PLACEHOLDERS+7;
	public static final int BOMB_HOLDER     = PLACEHOLDERS+8;
	public static final int POTION_HOLDER   = PLACEHOLDERS+9;
	public static final int SCROLL_HOLDER   = PLACEHOLDERS+11;
	public static final int SEED_HOLDER     = PLACEHOLDERS+10;
	public static final int STONE_HOLDER    = PLACEHOLDERS+12;
	public static final int CATA_HOLDER     = PLACEHOLDERS+13;
	public static final int ELIXIR_HOLDER   = PLACEHOLDERS+14;
	public static final int SPELL_HOLDER    = PLACEHOLDERS+15;
	static{
		assignItemRect(SOMETHING,       16,  16);
		assignItemRect(WEAPON_HOLDER,   16,  16);
		assignItemRect(ARMOR_HOLDER,    16,  16);
		assignItemRect(MISSILE_HOLDER,  16,  16);
		assignItemRect(WAND_HOLDER,     16,  16);
		assignItemRect(RING_HOLDER,     16,  16);
		assignItemRect(ARTIFACT_HOLDER, 16,  16);
		assignItemRect(FOOD_HOLDER,     16,  16);
		assignItemRect(BOMB_HOLDER,     16,  16);
		assignItemRect(POTION_HOLDER,   16,  16);
		assignItemRect(SEED_HOLDER,     16,  16);
		assignItemRect(SCROLL_HOLDER,   16,  16);
		assignItemRect(STONE_HOLDER,    16,  16);
		assignItemRect(CATA_HOLDER,     16,  16);
		assignItemRect(ELIXIR_HOLDER,   16,  16);
		assignItemRect(SPELL_HOLDER,    16,  16);
	}

	private static final int UNCOLLECTIBLE  =                               xy(1, 2);   //16 slots
	public static final int GOLD            = UNCOLLECTIBLE+0;
	public static final int DEWDROP         = UNCOLLECTIBLE+1;
	public static final int PETAL           = UNCOLLECTIBLE+2;
	public static final int SANDBAG         = UNCOLLECTIBLE+3;
	public static final int SPIRIT_ARROW    = UNCOLLECTIBLE+4;
	
	public static final int GUIDE_PAGE      = UNCOLLECTIBLE+6;
	public static final int ALCH_PAGE       = UNCOLLECTIBLE+7;

	public static final int TENGU_MINE      = UNCOLLECTIBLE+8;
	public static final int TENGU_BOMB      = UNCOLLECTIBLE+9;
	public static final int TENGU_SHOCKER   = UNCOLLECTIBLE+10;
	public static final int INFO_CERTI   = UNCOLLECTIBLE+11;
	static{
		assignItemRect(GOLD,        16, 16);
		assignItemRect(DEWDROP,     10, 10);
		assignItemRect(PETAL,       16,  16);
		assignItemRect(SANDBAG,     16, 16);
		assignItemRect(SPIRIT_ARROW,11, 11);
		
		assignItemRect(GUIDE_PAGE,  10, 11);
		assignItemRect(ALCH_PAGE,   10, 11);
		
		assignItemRect(TENGU_BOMB,      16, 16);
		assignItemRect(TENGU_SHOCKER,   16, 16);
		assignItemRect(INFO_CERTI,   16, 16);
	}

	private static final int CONTAINERS     =                               xy(1, 3);   //16 slots
	public static final int BONES           = CONTAINERS+0;
	public static final int REMAINS         = CONTAINERS+1;
	public static final int TOMB            = CONTAINERS+2;
	public static final int GRAVE           = CONTAINERS+3;
	public static final int CHEST           = CONTAINERS+4;
	public static final int LOCKED_CHEST    = CONTAINERS+5;
	public static final int CRYSTAL_CHEST   = CONTAINERS+6;
	public static final int EBONY_CHEST     = CONTAINERS+7;
	public static final int SKILL_BOOK     = CONTAINERS+8;
	public static final int SKILL_CHIP1     = CONTAINERS+9;
	public static final int SKILL_CHIP2     = CONTAINERS+10;
	public static final int SKILL_CHIP3     = CONTAINERS+11;
	static{
		assignItemRect(BONES,           16, 16);
		assignItemRect(REMAINS,         16, 16);
		assignItemRect(TOMB,            16, 16);
		assignItemRect(GRAVE,           16, 16);
		assignItemRect(CHEST,           16, 14);
		assignItemRect(LOCKED_CHEST,    16, 14);
		assignItemRect(CRYSTAL_CHEST,   16, 14);
		assignItemRect(EBONY_CHEST,     16, 14);
		assignItemRect(SKILL_BOOK,     16, 16);
		assignItemRect(SKILL_CHIP1,     16, 16);
		assignItemRect(SKILL_CHIP2,     16, 16);
		assignItemRect(SKILL_CHIP3,     16, 16);


	}

	private static final int SINGLE_USE     =                               xy(1, 4);   //16 slots
	public static final int ANKH            = SINGLE_USE+0;
	public static final int STYLUS          = SINGLE_USE+1;
	public static final int ORIGINIUM          = SINGLE_USE+2;
	public static final int SEAL            = SINGLE_USE+3;
	public static final int TORCH           = SINGLE_USE+4;
	public static final int BEACON          = SINGLE_USE+5;

	public static final int HONEYPOT        = SINGLE_USE+7;
	public static final int SHATTPOT        = SINGLE_USE+8;
	public static final int IRON_KEY        = SINGLE_USE+9;
	public static final int GOLDEN_KEY      = SINGLE_USE+10;
	public static final int CRYSTAL_KEY     = SINGLE_USE+11;
	public static final int SKELETON_KEY    = SINGLE_USE+12;
	public static final int MASTERY         = SINGLE_USE+13;
	public static final int KIT             = SINGLE_USE+14;
	public static final int AMULET          = SINGLE_USE+15;
	public static final int RINGKIT          = SINGLE_USE+16;
	static{
		assignItemRect(ANKH,            10, 16);
		assignItemRect(STYLUS,          16, 16);
		
		assignItemRect(SEAL,            16,  16);
		assignItemRect(TORCH,           16, 16);
		assignItemRect(BEACON,          16, 15);
		
		assignItemRect(HONEYPOT,        14, 12);
		assignItemRect(SHATTPOT,        14, 12);
		assignItemRect(IRON_KEY,        16,  16);
		assignItemRect(GOLDEN_KEY,      16,  16);
		assignItemRect(CRYSTAL_KEY,     16,  16);
		assignItemRect(SKELETON_KEY,    16,  16);
		assignItemRect(MASTERY,         16, 16);
		assignItemRect(KIT,             16, 15);
		assignItemRect(AMULET,          16, 16);
		assignItemRect(RINGKIT,          16, 16);
	}
	
	private static final int BOMBS          =                               xy(1, 5);   //16 slots
	public static final int BOMB            = BOMBS+0;
	public static final int DBL_BOMB        = BOMBS+1;
	public static final int FIRE_BOMB       = BOMBS+2;
	public static final int FROST_BOMB      = BOMBS+3;
	public static final int REGROWTH_BOMB   = BOMBS+4;
	public static final int FLASHBANG       = BOMBS+5;
	public static final int SHOCK_BOMB      = BOMBS+6;
	public static final int HOLY_BOMB       = BOMBS+7;
	public static final int WOOLY_BOMB      = BOMBS+8;
	public static final int NOISEMAKER      = BOMBS+9;
	public static final int ARCANE_BOMB     = BOMBS+10;
	public static final int SHRAPNEL_BOMB   = BOMBS+11;
	public static final int LENS_BOMB   = BOMBS+12;
	
	static{
		assignItemRect(BOMB,            16, 16);
		assignItemRect(DBL_BOMB,        16, 16);
		assignItemRect(FIRE_BOMB,       16, 16);
		assignItemRect(FROST_BOMB,      16, 16);
		assignItemRect(REGROWTH_BOMB,   16, 16);
		assignItemRect(FLASHBANG,       16,  16);
		assignItemRect(SHOCK_BOMB,      16,  16);
		assignItemRect(HOLY_BOMB,       16,  16);
		assignItemRect(WOOLY_BOMB,      16,  16);
		assignItemRect(NOISEMAKER,      16,  16);
		assignItemRect(ARCANE_BOMB,     16,  16);
		assignItemRect(SHRAPNEL_BOMB,   16,  16);
		assignItemRect(LENS_BOMB,   16,  16);
	}

	
	                                                                                    //16 free slots

	private static final int WEP_TIER1      =                               xy(1, 7);   //8 slots
	public static final int WORN_SHORTSWORD = WEP_TIER1+0;
	public static final int EX41          = WEP_TIER1+1;
	public static final int GLOVES          = WEP_TIER1+2;
	public static final int CHAINSAW          = WEP_TIER1+3;
	public static final int DAGGER          = WEP_TIER1+4;
	public static final int MAGES_STAFF     = WEP_TIER1+5;
	public static final int HEAMYO     = WEP_TIER1+6;
	public static final int YATO     = WEP_TIER1+7;
	public static final int NEARL_AXE     = WEP_TIER1+8;
	public static final int SIG553     = WEP_TIER1+9;
	static{
		assignItemRect(WORN_SHORTSWORD, 16, 16);
		assignItemRect(EX41, 			16, 16);
		assignItemRect(GLOVES,          16, 16);
		assignItemRect(CHAINSAW, 			16, 16);
		assignItemRect(DAGGER,          16, 16);
		assignItemRect(MAGES_STAFF,     16, 16);
		assignItemRect(HEAMYO,     16, 16);
		assignItemRect(YATO,     16, 16);
		assignItemRect(NEARL_AXE,     16, 16);
		assignItemRect(SIG553,     16, 16);
	}

	private static final int WEP_TIER2      =                               xy(17, 7);   //8 slots
	public static final int SHORTSWORD      = WEP_TIER2+0;
	public static final int HAND_AXE        = WEP_TIER2+1;
	public static final int SPEAR           = WEP_TIER2+2;
	public static final int VANILLA_AXE = WEP_TIER2+3;
	public static final int DIRK            = WEP_TIER2+4;
	public static final int ENFILD           = WEP_TIER2+5;
	public static final int MIDSWORD           = WEP_TIER2+6;
	public static final int FIRMAMENT           = WEP_TIER2+7;
	public static final int FIRE_KATANA           = WEP_TIER2+8;
	public static final int USG           = WEP_TIER2+9;

	static{
		assignItemRect(SHORTSWORD,      16, 16);
		assignItemRect(HAND_AXE,        16, 16);
		assignItemRect(SPEAR,           16, 16);
		assignItemRect(VANILLA_AXE,    16, 16);
		assignItemRect(DIRK,            16, 16);
		assignItemRect(ENFILD,            16, 16);
		assignItemRect(MIDSWORD,            16, 16);
		assignItemRect(FIRMAMENT,            16, 16);
		assignItemRect(FIRE_KATANA,            16, 16);
		assignItemRect(USG,            16, 16);

	}

	private static final int WEP_TIER3      =                               xy(1, 8);   //8 slots
	public static final int SWORD           = WEP_TIER3+0;
	public static final int MACE            = WEP_TIER3+1;
	public static final int SCIMITAR        = WEP_TIER3+2;
	public static final int ROUND_SHIELD    = WEP_TIER3+3;
	public static final int SAI             = WEP_TIER3+4;
	public static final int WHIP            = WEP_TIER3+5;
	public static final int GAMZA_SHIELD     = WEP_TIER3+6;
	public static final int BEENS     = WEP_TIER3+7;
	public static final int ANDREANA     = WEP_TIER3+8;
	public static final int SHISHIOH     = WEP_TIER3+9;
	public static final int FLAG     = WEP_TIER3+10;
	public static final int DP27     = WEP_TIER3+11;
	public static final int C1     = WEP_TIER3+12;

	static{
		assignItemRect(SWORD,           16, 16);
		assignItemRect(MACE,            16, 16);
		assignItemRect(SCIMITAR,        16, 16);
		assignItemRect(ROUND_SHIELD,    16, 16);
		assignItemRect(SAI,             16, 16);
		assignItemRect(WHIP,            16, 16);
		assignItemRect(GAMZA_SHIELD,            16, 16);
		assignItemRect(BEENS,            16, 16);
		assignItemRect(ANDREANA,            16, 16);
		assignItemRect(SHISHIOH,            16, 16);
		assignItemRect(FLAG,            16, 16);
		assignItemRect(DP27,            16, 16);
		assignItemRect(C1,            16, 16);


	}

	private static final int WEP_TIER4      =                               xy(1, 9);   //8 slots
	public static final int LONGSWORD       = WEP_TIER4+0;
	public static final int BATTLE_AXE      = WEP_TIER4+1;
	public static final int FLAIL           = WEP_TIER4+2;
	public static final int RUNIC_BLADE     = WEP_TIER4+3;
	public static final int ASSASSINS_BLADE = WEP_TIER4+4;
	public static final int CROSSBOW        = WEP_TIER4+5;
	public static final int M1887       = WEP_TIER4+6;
	public static final int NAGINATA       = WEP_TIER4+7;
	public static final int SICKEL       = WEP_TIER4+8;
	public static final int GOLDDOG       = WEP_TIER4+9;
	public static final int SPECTER       = WEP_TIER4+10;
	public static final int CARNEL       = WEP_TIER4+11;
	public static final int CLIFF       = WEP_TIER4+12;
	public static final int BLADE_DEMON       = WEP_TIER4+13;
	public static final int FLAMETAIL       = WEP_TIER4+14;
	public static final int PANDA       = WEP_TIER4+15;
	public static final int SCENE       = WEP_TIER4+16;
	public static final int REVOLVER       = WEP_TIER4+17;
	public static final int KAZEMARU       = WEP_TIER4+18;
	public static final int OTS03       = WEP_TIER4+19;
	public static final int SG_CQB       = WEP_TIER4+20;
	public static final int BEOWULF       = WEP_TIER4+21;
	static{
		assignItemRect(LONGSWORD,       16, 16);
		assignItemRect(BATTLE_AXE,      16, 16);
		assignItemRect(FLAIL,           16, 16);
		assignItemRect(RUNIC_BLADE,     16, 16);
		assignItemRect(ASSASSINS_BLADE, 16, 16);
		assignItemRect(CROSSBOW,        16, 16);
		assignItemRect(M1887,        16, 16);
		assignItemRect(NAGINATA,        16, 16);
		assignItemRect(SICKEL,        16, 16);
		assignItemRect(GOLDDOG,        16, 16);
		assignItemRect(SPECTER,        16, 16);
		assignItemRect(CARNEL,        16, 16);
		assignItemRect(CLIFF,        16, 16);
		assignItemRect(BLADE_DEMON,        16, 16);
		assignItemRect(FLAMETAIL,        16, 16);
		assignItemRect(PANDA,        16, 16);
		assignItemRect(SCENE,        16, 16);
		assignItemRect(REVOLVER,        16, 16);
		assignItemRect(KAZEMARU,        16, 16);
		assignItemRect(OTS03,        16, 16);
		assignItemRect(SG_CQB,        16, 16);
		assignItemRect(BEOWULF,        16, 16);
	}

	private static final int WEP_TIER5      =                               xy(1, 10);   //8 slots
	public static final int GREATSWORD      = WEP_TIER5+0;
	public static final int WAR_HAMMER      = WEP_TIER5+1;
	public static final int GLAIVE          = WEP_TIER5+2;
	public static final int GREATAXE        = WEP_TIER5+3;
	public static final int GREATSHIELD     = WEP_TIER5+4;
	public static final int GAUNTLETS       = WEP_TIER5+5;
	public static final int NIANSWORD       = WEP_TIER5+6;
	public static final int ARTORIUS      = WEP_TIER5+7;
	public static final int WINTER       = WEP_TIER5+8;
	public static final int REQUIEM       = WEP_TIER5+9;
	public static final int CATGUN       = WEP_TIER5+10;
	public static final int BABY_KNIGHT       = WEP_TIER5+11;
	public static final int R4C       = WEP_TIER5+12;
	public static final int DUSK       = WEP_TIER5+13;
	public static final int VULCAN       = WEP_TIER5+14;
	public static final int NEARL_SPEAR       = WEP_TIER5+15;
	public static final int SAKURA_FUBUKI       = WEP_TIER5+16;
	public static final int KRISS_V       = WEP_TIER5+17;
	public static final int LONE       = WEP_TIER5+18;
	public static final int ECHEVERIA       = WEP_TIER5+19;
	public static final int DONKEY_SWORD       = WEP_TIER5+20;
	public static final int FLAMMETTA       = WEP_TIER5+21;
	public static final int PKP       = WEP_TIER5+22;

	static{
		assignItemRect(GREATSWORD,  16, 16);
		assignItemRect(WAR_HAMMER,  16, 16);
		assignItemRect(GLAIVE,      16, 16);
		assignItemRect(GREATAXE,    16, 16);
		assignItemRect(GREATSHIELD, 16, 16);
		assignItemRect(GAUNTLETS,   16, 16);
		assignItemRect(NIANSWORD,   16, 16);
		assignItemRect(ARTORIUS,   16, 16);
		assignItemRect(WINTER,   16, 16);
		assignItemRect(REQUIEM,   16, 16);
		assignItemRect(CATGUN,   16, 16);
		assignItemRect(BABY_KNIGHT,   16, 16);
		assignItemRect(R4C,   16, 16);
		assignItemRect(DUSK,   16, 16);
		assignItemRect(VULCAN,   16, 16);
		assignItemRect(NEARL_SPEAR,   16, 16);
		assignItemRect(SAKURA_FUBUKI,   16, 16);
		assignItemRect(KRISS_V,   16, 16);
		assignItemRect(LONE,   16, 16);
		assignItemRect(ECHEVERIA,   16, 16);
		assignItemRect(DONKEY_SWORD,   16, 16);
		assignItemRect(FLAMMETTA,   16, 16);
		assignItemRect(PKP,   16, 16);


	}

	                                                                                    //8 free slots

	private static final int MISSILE_WEP    =                               xy(1, 11);  //16 slots. 3 per tier + boomerang
	public static final int SPIRIT_BOW      = MISSILE_WEP+0;
	
	public static final int DART            = MISSILE_WEP+1;
	public static final int THROWING_KNIFE  = MISSILE_WEP+2;
	public static final int THROWING_STONE  = MISSILE_WEP+3;
	
	public static final int FISHING_SPEAR   = MISSILE_WEP+4;
	public static final int SHURIKEN        = MISSILE_WEP+5;
	public static final int THROWING_CLUB   = MISSILE_WEP+6;
	
	public static final int THROWING_SPEAR  = MISSILE_WEP+7;
	public static final int BOLAS           = MISSILE_WEP+8;
	public static final int KUNAI           = MISSILE_WEP+9;
	
	public static final int JAVELIN         = MISSILE_WEP+10;
	public static final int TOMAHAWK        = MISSILE_WEP+11;
	public static final int BOOMERANG       = MISSILE_WEP+12;
	
	public static final int TRIDENT         = MISSILE_WEP+13;
	public static final int THROWING_HAMMER = MISSILE_WEP+14;
	public static final int FORCE_CUBE      = MISSILE_WEP+15;
	public static final int FLINT  		= MISSILE_WEP+16;
	public static final int LAVA  		= MISSILE_WEP+17;
	public static final int AMMO1  		= MISSILE_WEP+18;
	public static final int AMMO2 		= MISSILE_WEP+19;
	public static final int LISKARM_DOLL 		= MISSILE_WEP+20;
	public static final int HOLY_KNIFE 		= MISSILE_WEP+21;

    public static final int SAINT_HAND 		= MISSILE_WEP+22;
	public static final int EX44 		= MISSILE_WEP+30;
	
	static{
		assignItemRect(SPIRIT_BOW,      16, 16);
		
		assignItemRect(DART,            16, 16);
		assignItemRect(THROWING_KNIFE,  16, 16);
		assignItemRect(THROWING_STONE,  16, 16);
		
		assignItemRect(FISHING_SPEAR,   16, 16);
		assignItemRect(SHURIKEN,        16, 16);
		assignItemRect(THROWING_CLUB,   16, 16);
		
		assignItemRect(THROWING_SPEAR,  16, 16);
		assignItemRect(BOLAS,           16, 16);
		assignItemRect(KUNAI,           16, 16);
		
		assignItemRect(JAVELIN,         16, 16);
		assignItemRect(TOMAHAWK,        16, 16);
		assignItemRect(BOOMERANG,       16, 16);
		
		assignItemRect(TRIDENT,         16, 16);
		assignItemRect(THROWING_HAMMER, 16, 16);
		assignItemRect(FORCE_CUBE,      16, 16);
		assignItemRect(FLINT,      16, 16);
		assignItemRect(LAVA,      16, 16);
		assignItemRect(AMMO1,      16, 16);
		assignItemRect(AMMO2,      16, 16);
		assignItemRect(LISKARM_DOLL,      16, 16);
		assignItemRect(HOLY_KNIFE,      16, 16);
        assignItemRect(SAINT_HAND,      16, 16);
		assignItemRect(EX44,         24, 24);
	}
	
	public static final int TIPPED_DARTS    =                               xy(1, 12);  //16 slots
	public static final int ROT_DART        = TIPPED_DARTS+0;
	public static final int INCENDIARY_DART = TIPPED_DARTS+1;
	public static final int ADRENALINE_DART = TIPPED_DARTS+2;
	public static final int HEALING_DART    = TIPPED_DARTS+3;
	public static final int CHILLING_DART   = TIPPED_DARTS+4;
	public static final int SHOCKING_DART   = TIPPED_DARTS+5;
	public static final int POISON_DART     = TIPPED_DARTS+6;
	public static final int SLEEP_DART      = TIPPED_DARTS+7;
	public static final int PARALYTIC_DART  = TIPPED_DARTS+8;
	public static final int HOLY_DART       = TIPPED_DARTS+9;
	public static final int DISPLACING_DART = TIPPED_DARTS+10;
	public static final int BLINDING_DART   = TIPPED_DARTS+11;
	static {
		for (int i = TIPPED_DARTS; i < TIPPED_DARTS+31; i++)
			assignItemRect(i, 16, 16);
	}
	
	private static final int ARMOR          =                               xy(1, 13);  //16 slots
	public static final int ARMOR_CLOTH     = ARMOR+0;
	public static final int ARMOR_LEATHER   = ARMOR+1;
	public static final int ARMOR_MAIL      = ARMOR+2;
	public static final int ARMOR_SCALE     = ARMOR+3;
	public static final int ARMOR_PLATE     = ARMOR+4;
	public static final int ARMOR_WARRIOR   = ARMOR+5;
	public static final int ARMOR_MAGE      = ARMOR+6;
	public static final int ARMOR_ROGUE     = ARMOR+7;
	public static final int ARMOR_HUNTRESS  = ARMOR+8;
	static{
		assignItemRect(ARMOR_CLOTH,     16, 16);
		assignItemRect(ARMOR_LEATHER,   16, 16);
		assignItemRect(ARMOR_MAIL,      16, 16);
		assignItemRect(ARMOR_SCALE,     16, 16);
		assignItemRect(ARMOR_PLATE,     16, 16);
		assignItemRect(ARMOR_WARRIOR,   16, 16);
		assignItemRect(ARMOR_MAGE,      16, 16);
		assignItemRect(ARMOR_ROGUE,     16, 16);
		assignItemRect(ARMOR_HUNTRESS,  16, 16);
	}

	                                                                                    //16 free slots

	private static final int WANDS              =                           xy(1, 14);  //16 slots
	public static final int WAND_MAGIC_MISSILE  = WANDS+0;
	public static final int WAND_FIREBOLT       = WANDS+1;
	public static final int WAND_FROST          = WANDS+2;
	public static final int WAND_LIGHTNING      = WANDS+3;
	public static final int WAND_DISINTEGRATION = WANDS+4;
	public static final int WAND_PRISMATIC_LIGHT= WANDS+5;
	public static final int WAND_CORROSION      = WANDS+6;
	public static final int WAND_LIVING_EARTH   = WANDS+7;
	public static final int WAND_BLAST_WAVE     = WANDS+8;
	public static final int WAND_CORRUPTION     = WANDS+9;
	public static final int WAND_WARDING        = WANDS+10;
	public static final int WAND_REGROWTH       = WANDS+11;
	public static final int WAND_TRANSFUSION    = WANDS+12;
	public static final int WAND_SNOWSANT    = WANDS+13;
	public static final int WAND_SUSSURRO    = WANDS+14;
	public static final int WAND_SUZRAN    = WANDS+15;
	public static final int WAND_MOSTIMA    = WANDS+16;
	public static final int WAND_LAVA    = WANDS+17;
	public static final int WAND_PODENCO    = WANDS+18;
	static {
		for (int i = WANDS; i < WANDS+31; i++)
			assignItemRect(i, 16, 16);
	}

	private static final int RINGS          =                               xy(1, 15);  //17 slots
	public static final int RING_GARNET     = RINGS+0;
	public static final int RING_RUBY       = RINGS+1;
	public static final int RING_TOPAZ      = RINGS+2;
	public static final int RING_EMERALD    = RINGS+3;
	public static final int RING_ONYX       = RINGS+4;
	public static final int RING_OPAL       = RINGS+5;
	public static final int RING_TOURMALINE = RINGS+6;
	public static final int RING_SAPPHIRE   = RINGS+7;
	public static final int RING_AMETHYST   = RINGS+8;
	public static final int RING_QUARTZ     = RINGS+9;
	public static final int RING_AGATE      = RINGS+10;
	public static final int RING_DIAMOND    = RINGS+11;
	public static final int RING_HYDROGEN    = RINGS+12;
	public static final int RING_HELIUM    = RINGS+13;
	public static final int RING_LITHIUM    = RINGS+14;
	public static final int RING_MINT   = RINGS+15;
	public static final int RING_ARGON   = RINGS+16;
	public static final int RING_METAL   = RINGS+17;
	public static final int RING_IRON   = RINGS+18;
	public static final int RING_BRONS   = RINGS+19;
	public static final int RING_MASTER   = RINGS+20;

	static {
		for (int i = RINGS; i < RINGS+31; i++)
			assignItemRect(i, 16, 16);
	}

	private static final int ARTIFACTS          =                            xy(1, 16);  //32 slots
	public static final int ARTIFACT_CLOAK      = ARTIFACTS+0;
	public static final int ARTIFACT_ARMBAND    = ARTIFACTS+1;
	public static final int ARTIFACT_ROSEMARY = ARTIFACTS+2;
	public static final int ARTIFACT_TALISMAN   = ARTIFACTS+3;
	public static final int ARTIFACT_HOURGLASS  = ARTIFACTS+4;
	public static final int ARTIFACT_TOOLKIT    = ARTIFACTS+5;
	public static final int ARTIFACT_SPELLBOOK  = ARTIFACTS+6;
	public static final int ARTIFACT_BEACON     = ARTIFACTS+7;
	public static final int ARTIFACT_CHAINS     = ARTIFACTS+8;
	public static final int ARTIFACT_HORN1      = ARTIFACTS+9;
	public static final int ARTIFACT_HORN2      = ARTIFACTS+10;
	public static final int ARTIFACT_HORN3      = ARTIFACTS+11;
	public static final int ARTIFACT_HORN4      = ARTIFACTS+12;
	public static final int ARTIFACT_CHALICE1   = ARTIFACTS+13;
	public static final int ARTIFACT_CHALICE2   = ARTIFACTS+14;
	public static final int ARTIFACT_CHALICE3   = ARTIFACTS+15;
	public static final int ARTIFACT_SANDALS    = ARTIFACTS+32;
	public static final int ARTIFACT_SHOES      = ARTIFACTS+33;
	public static final int ARTIFACT_BOOTS      = ARTIFACTS+34;
	public static final int ARTIFACT_GREAVES    = ARTIFACTS+35;
	public static final int ARTIFACT_ROSE1      = ARTIFACTS+36;
	public static final int ARTIFACT_ROSE2      = ARTIFACTS+37;
	public static final int ARTIFACT_ROSE3      = ARTIFACTS+38;
	public static final int ARTIFACT_THORNS      = ARTIFACTS+39;
	public static final int ARTIFACT_ASH      = ARTIFACTS+40;
	public static final int ARTIFACT_NEARL      = ARTIFACTS+41;
	public static final int ARTIFACT_CAMERA      = ARTIFACTS+42;
	public static final int ARTIFACT_WALLET      = ARTIFACTS+43;
	static{
		assignItemRect(ARTIFACT_CLOAK,      16, 16);
		assignItemRect(ARTIFACT_ARMBAND,    16, 16);
		assignItemRect(ARTIFACT_ROSEMARY,       16, 16);
		assignItemRect(ARTIFACT_TALISMAN,   16, 16);
		assignItemRect(ARTIFACT_HOURGLASS,  16, 16);
		assignItemRect(ARTIFACT_TOOLKIT,    16, 16);
		assignItemRect(ARTIFACT_SPELLBOOK,  16, 16);
		assignItemRect(ARTIFACT_BEACON,     16, 16);
		assignItemRect(ARTIFACT_CHAINS,     16, 16);
		assignItemRect(ARTIFACT_HORN1,      16, 16);
		assignItemRect(ARTIFACT_HORN2,      16, 16);
		assignItemRect(ARTIFACT_HORN3,      16, 16);
		assignItemRect(ARTIFACT_HORN4,      16, 16);
		assignItemRect(ARTIFACT_CHALICE1,   16, 16);
		assignItemRect(ARTIFACT_CHALICE2,   16, 16);
		assignItemRect(ARTIFACT_CHALICE3,   16, 16);
		assignItemRect(ARTIFACT_SANDALS,    16, 16);
		assignItemRect(ARTIFACT_SHOES,      16, 16);
		assignItemRect(ARTIFACT_BOOTS,      16, 16);
		assignItemRect(ARTIFACT_GREAVES,    16, 16);
		assignItemRect(ARTIFACT_ROSE1,      16, 16);
		assignItemRect(ARTIFACT_ROSE2,      16, 16);
		assignItemRect(ARTIFACT_ROSE3,      16, 16);
		assignItemRect(ARTIFACT_THORNS,      16, 16);
		assignItemRect(ARTIFACT_ASH,      16, 16);
		assignItemRect(ARTIFACT_NEARL,      16, 16);
		assignItemRect(ARTIFACT_CAMERA,      16, 16);
		assignItemRect(ARTIFACT_WALLET,      16, 16);
	}

	                                                                                    //16 free slots

	private static final int SCROLLS        =                               xy(1, 19);  //16 slots
	public static final int SCROLL_KAUNAN   = SCROLLS+0;
	public static final int SCROLL_SOWILO   = SCROLLS+1;
	public static final int SCROLL_LAGUZ    = SCROLLS+2;
	public static final int SCROLL_YNGVI    = SCROLLS+3;
	public static final int SCROLL_GYFU     = SCROLLS+4;
	public static final int SCROLL_RAIDO    = SCROLLS+5;
	public static final int SCROLL_ISAZ     = SCROLLS+6;
	public static final int SCROLL_MANNAZ   = SCROLLS+7;
	public static final int SCROLL_NAUDIZ   = SCROLLS+8;
	public static final int SCROLL_BERKANAN = SCROLLS+9;
	public static final int SCROLL_ODAL     = SCROLLS+10;
	public static final int SCROLL_ROSE    = SCROLLS+11;
	public static final int SCROLL_TIWAZ    = SCROLLS+12;
	
	public static final int SCROLL_CATALYST = SCROLLS+13;
	public static final int FORCE_CATALYST = SCROLLS+14;
	static {
		for (int i = SCROLLS; i < SCROLLS+31; i++)
			assignItemRect(i, 15, 14);
		assignItemRect(SCROLL_CATALYST, 16, 16);
		assignItemRect(FORCE_CATALYST, 16, 16);
	}
	
	private static final int EXOTIC_SCROLLS =                               xy(1, 20);  //16 slots
	public static final int EXOTIC_KAUNAN   = EXOTIC_SCROLLS+0;
	public static final int EXOTIC_SOWILO   = EXOTIC_SCROLLS+1;
	public static final int EXOTIC_LAGUZ    = EXOTIC_SCROLLS+2;
	public static final int EXOTIC_YNGVI    = EXOTIC_SCROLLS+3;
	public static final int EXOTIC_GYFU     = EXOTIC_SCROLLS+4;
	public static final int EXOTIC_RAIDO    = EXOTIC_SCROLLS+5;
	public static final int EXOTIC_ISAZ     = EXOTIC_SCROLLS+6;
	public static final int EXOTIC_MANNAZ   = EXOTIC_SCROLLS+7;
	public static final int EXOTIC_NAUDIZ   = EXOTIC_SCROLLS+8;
	public static final int EXOTIC_BERKANAN = EXOTIC_SCROLLS+9;
	public static final int EXOTIC_ODAL     = EXOTIC_SCROLLS+10;
	public static final int EXOTIC_TIWAZ    = EXOTIC_SCROLLS+11;
	public static final int EXOTIC_BLAZE    = EXOTIC_SCROLLS+12;
	static {
		for (int i = EXOTIC_SCROLLS; i < EXOTIC_SCROLLS+31; i++)
			assignItemRect(i, 16, 16);
	}
	
	private static final int STONES             =                           xy(1, 21);  //16 slots
	public static final int STONE_AGGRESSION    = STONES+0;
	public static final int STONE_AUGMENTATION  = STONES+1;
	public static final int STONE_AFFECTION     = STONES+2;
	public static final int STONE_BLAST         = STONES+3;
	public static final int STONE_BLINK         = STONES+4;
	public static final int STONE_CLAIRVOYANCE  = STONES+5;
	public static final int STONE_SLEEP         = STONES+6;
	public static final int STONE_DISARM        = STONES+7;
	public static final int STONE_ENCHANT       = STONES+8;
	public static final int STONE_FLOCK         = STONES+9;
	public static final int STONE_INTUITION     = STONES+10;
	public static final int STONE_SHOCK         = STONES+11;
	static {
		for (int i = STONES; i < STONES+31; i++)
			assignItemRect(i, 14, 12);
	}

	private static final int POTIONS        =                               xy(1, 22);  //16 slots
	public static final int POTION_CRIMSON  = POTIONS+0;
	public static final int POTION_AMBER    = POTIONS+1;
	public static final int POTION_GOLDEN   = POTIONS+2;
	public static final int POTION_JADE     = POTIONS+3;
	public static final int POTION_TURQUOISE= POTIONS+4;
	public static final int POTION_AZURE    = POTIONS+5;
	public static final int POTION_INDIGO   = POTIONS+6;
	public static final int POTION_MAGENTA  = POTIONS+7;
	public static final int POTION_BISTRE   = POTIONS+8;
	public static final int POTION_CHARCOAL = POTIONS+9;
	public static final int POTION_SILVER   = POTIONS+10;
	public static final int POTION_IVORY    = POTIONS+11;
	public static final int POTION_CATALYST = POTIONS+13;
	static {
		for (int i = POTIONS; i < POTIONS+31; i++)
			assignItemRect(i, 16, 16);
		assignItemRect(POTION_CATALYST, 16, 16);
	}
	
	private static final int EXOTIC_POTIONS =                               xy(1, 23);  //16 slots
	public static final int EXOTIC_CRIMSON  = EXOTIC_POTIONS+0;
	public static final int EXOTIC_AMBER    = EXOTIC_POTIONS+1;
	public static final int EXOTIC_GOLDEN   = EXOTIC_POTIONS+2;
	public static final int EXOTIC_JADE     = EXOTIC_POTIONS+3;
	public static final int EXOTIC_TURQUOISE= EXOTIC_POTIONS+4;
	public static final int EXOTIC_AZURE    = EXOTIC_POTIONS+5;
	public static final int EXOTIC_INDIGO   = EXOTIC_POTIONS+6;
	public static final int EXOTIC_MAGENTA  = EXOTIC_POTIONS+7;
	public static final int EXOTIC_BISTRE   = EXOTIC_POTIONS+8;
	public static final int EXOTIC_CHARCOAL = EXOTIC_POTIONS+9;
	public static final int EXOTIC_SILVER   = EXOTIC_POTIONS+10;
	public static final int EXOTIC_IVORY    = EXOTIC_POTIONS+11;
	static {
		for (int i = EXOTIC_POTIONS; i < EXOTIC_POTIONS+31; i++)
			assignItemRect(i, 16, 16);
	}

	private static final int SEEDS              =                           xy(1, 24);  //16 slots
	public static final int SEED_ROTBERRY       = SEEDS+0;
	public static final int SEED_FIREBLOOM      = SEEDS+1;
	public static final int SEED_SWIFTTHISTLE   = SEEDS+2;
	public static final int SEED_SUNGRASS       = SEEDS+3;
	public static final int SEED_ICECAP         = SEEDS+4;
	public static final int SEED_STORMVINE      = SEEDS+5;
	public static final int SEED_SORROWMOSS     = SEEDS+6;
	public static final int SEED_DREAMFOIL      = SEEDS+7;
	public static final int SEED_EARTHROOT      = SEEDS+8;
	public static final int SEED_STARFLOWER     = SEEDS+9;
	public static final int SEED_FADELEAF       = SEEDS+10;
	public static final int SEED_BLINDWEED      = SEEDS+11;
	static{
		for (int i = SEEDS; i < SEEDS+31; i++)
			assignItemRect(i, 10, 10);
	}
	
	private static final int BREWS          =                               xy(1, 25);  //8 slots
	public static final int BREW_INFERNAL   = BREWS+0;
	public static final int BREW_BLIZZARD   = BREWS+1;
	public static final int BREW_SHOCKING   = BREWS+2;
	public static final int BREW_CAUSTIC    = BREWS+3;
	
	private static final int ELIXIRS        =                               xy(9, 25);  //8 slots
	public static final int ELIXIR_HONEY    = ELIXIRS+0;
	public static final int ELIXIR_AQUA     = ELIXIRS+1;
	public static final int ELIXIR_MIGHT    = ELIXIRS+2;
	public static final int ELIXIR_DRAGON   = ELIXIRS+3;
	public static final int ELIXIR_TOXIC    = ELIXIRS+4;
	public static final int ELIXIR_ICY      = ELIXIRS+5;
	public static final int ELIXIR_ARCANE   = ELIXIRS+6;
	public static final int ELIXIR_IRON   = ELIXIRS+7;
	public static final int ELIXIR_SOUL1   = ELIXIRS+8;
	public static final int ELIXIR_SOUL2   = ELIXIRS+9;
	static{
		for (int i = BREWS; i < BREWS+31; i++)
			assignItemRect(i, 12, 14);
	}


	private static final int TACTICAL        =                               xy(1, 26);  //8 slots
	public static final int IRON_SIGHT    = TACTICAL+0;
	public static final int HOLO_SIGHT      = TACTICAL+1;
	public static final int TELESCOPE     = TACTICAL+2;
	public static final int MUZZLE_BRAKE    = TACTICAL+3;
	public static final int C_MAG    = TACTICAL+4;
	static{
		for (int i = BREWS; i < BREWS+16; i++)
			assignItemRect(i, 16, 16);
	}
	                                                                                    //16 free slots
	
	private static final int SPELLS         =                               xy(1, 27);  //16 slots
	public static final int MAGIC_ARMOR = SPELLS+0;
	public static final int PHASE_SHIFT     = SPELLS+1;
	public static final int WILD_ENERGY = SPELLS+2;
	public static final int RETURN_BEACON   = SPELLS+3;
	
	public static final int AQUA_BLAST      = SPELLS+5;
	public static final int FEATHER_FALL    = SPELLS+6;
	public static final int RECLAIM_TRAP    = SPELLS+7;
	public static final int OATH_FIRE    = SPELLS+8;
	
	public static final int CURSE_INFUSE    = SPELLS+9;
	public static final int MAGIC_INFUSE    = SPELLS+10;
	public static final int ALCHEMIZE       = SPELLS+11;
	public static final int RECYCLE         = SPELLS+12;
	public static final int WEAPON_TRANS         = SPELLS+13;
	public static final int AVANT_TRANS         = SPELLS+14;
	static{
		assignItemRect(MAGIC_ARMOR,    12, 11);
		assignItemRect(PHASE_SHIFT,     12, 11);
		assignItemRect(WILD_ENERGY,      8, 16);
		assignItemRect(RETURN_BEACON,    8, 16);
		
		assignItemRect(AQUA_BLAST,      11, 11);
		assignItemRect(FEATHER_FALL,    11, 11);
		assignItemRect(RECLAIM_TRAP,    11, 11);
		assignItemRect(OATH_FIRE,    11, 11);
		
		assignItemRect(CURSE_INFUSE,    10, 15);
		assignItemRect(MAGIC_INFUSE,    10, 15);
		assignItemRect(ALCHEMIZE,       10, 15);
		assignItemRect(RECYCLE,         10, 15);
		assignItemRect(WEAPON_TRANS,         10, 15);
		assignItemRect(AVANT_TRANS,         10, 15);
	}
	
	private static final int FOOD       =                                   xy(1, 28);  //16 slots
	public static final int MEAT        = FOOD+0;
	public static final int STEAK       = FOOD+1;
	public static final int STEWED      = FOOD+2;
	public static final int OVERPRICED  = FOOD+3;
	public static final int CARPACCIO   = FOOD+4;
	public static final int RATION      = FOOD+5;
	public static final int PASTY       = FOOD+6;
	public static final int PUMPKIN_PIE = FOOD+7;
	public static final int CANDY_CANE  = FOOD+8;
	public static final int BLANDFRUIT  = FOOD+9;
	public static final int BLAND_CHUNKS= FOOD+10;
	public static final int BERRY =       FOOD+11;
	public static final int CUTLET =       FOOD+12;
	public static final int BONK =       FOOD+13;
	public static final int POMBBAY =       FOOD+14;
	public static final int PEPPER =       FOOD+15;
	public static final int SALT =       FOOD+16;
	public static final int HONEY_FLOWER =       FOOD+17;
	public static final int POTATO =       FOOD+18;
	public static final int EGG =       FOOD+19;
	static{
		assignItemRect(MEAT,        16, 16);
		assignItemRect(STEAK,       16, 16);
		assignItemRect(STEWED,      16, 16);
		assignItemRect(OVERPRICED,  16, 16);
		assignItemRect(CARPACCIO,   16, 16);
		assignItemRect(RATION,      16, 16);
		assignItemRect(PASTY,       16, 16);
		assignItemRect(PUMPKIN_PIE, 16, 16);
		assignItemRect(CANDY_CANE,  16, 16);
		assignItemRect(BLANDFRUIT,  16, 16);
		assignItemRect(BLAND_CHUNKS,16, 16);
		assignItemRect(BERRY,       16, 16);
		assignItemRect(CUTLET,       16, 16);
		assignItemRect(BONK,       16, 16);
		assignItemRect(POMBBAY,       16, 16);
		assignItemRect(PEPPER,       16, 16);
		assignItemRect(SALT,       16, 16);
		assignItemRect(HONEY_FLOWER,       16, 16);
		assignItemRect(POTATO,       16, 16);
		assignItemRect(EGG,       16, 16);
	}

	private static final int FOOD2  =                                       xy(1, 29);  //32 slots
	public static final int MEAT_PIE    = FOOD2+0;
	public static final int SANDBITCH =       FOOD2+1;
	public static final int PAN_CAKE =       FOOD2+2;
	public static final int MEAT_SRICK =       FOOD2+3;
	public static final int POTATO_FRY =       FOOD2+4;
	public static final int EGG_FRY =       FOOD2+5;
	public static final int YUKJEON =       FOOD2+6;
	public static final int SMOKEEGG =       FOOD2+7;
	public static final int NUNEDDINE =       FOOD2+8;
	static{
		assignItemRect(MEAT_PIE,    16, 16);
		assignItemRect(SANDBITCH,       16, 16);
		assignItemRect(PAN_CAKE,       16, 16);
		assignItemRect(MEAT_SRICK,       16, 16);
		assignItemRect(POTATO_FRY,       16, 16);
		assignItemRect(EGG_FRY,       16, 16);
		assignItemRect(YUKJEON,       16, 16);
		assignItemRect(SMOKEEGG,       16, 16);
		assignItemRect(NUNEDDINE,       16, 16);
	}

	private static final int QUEST  =                                       xy(1, 30);  //32 slots
	public static final int SKULL   = QUEST+0;
	public static final int DUST    = QUEST+1;
	public static final int CANDLE  = QUEST+2;
	public static final int EMBER   = QUEST+3;
	public static final int PICKAXE = QUEST+4;
	public static final int ORE     = QUEST+5;
	public static final int TOKEN   = QUEST+6;
	public static final int BLOB    = QUEST+7;
	public static final int SHARD   = QUEST+8;
	public static final int CAUTUS   = QUEST+9;
	public static final int MOULD   = QUEST+10;
	public static final int NULL_DEF   = QUEST+11;
	public static final int GAMZA   = QUEST+12;
	public static final int MYOSOTIS   = QUEST+13;
	public static final int W0520   = QUEST+14;
	public static final int BOSS_TOKEN   = QUEST+15;
	public static final int OBSIDIAN   = QUEST+16;
	public static final int TEA   = QUEST+17;
	public static final int KETTLE   = QUEST+18;
	public static final int CAT   = QUEST+19;
	public static final int WALKIE   = QUEST+20;
	public static final int MUSH   = QUEST+21;
	public static final int BARRI   = QUEST+22;
	public static final int TYLENOL   = QUEST+23;
	public static final int SIESTA   = QUEST+24;
	public static final int GAVIAL   = QUEST+25;
	public static final int IBERIA   = QUEST+26;
	static{
		assignItemRect(SKULL,   16, 16);
		assignItemRect(DUST,    16, 16);
		assignItemRect(CANDLE,  16, 16);
		assignItemRect(EMBER,   16, 16);
		assignItemRect(PICKAXE, 16, 16);
		assignItemRect(ORE,     16, 16);
		assignItemRect(TOKEN,   16, 16);
		assignItemRect(BLOB,    16, 16);
		assignItemRect(SHARD,    16, 16);
		assignItemRect(CAUTUS,    16, 17);
		assignItemRect(MOULD,    16, 16);
		assignItemRect(NULL_DEF,    16, 16);
		assignItemRect(GAMZA,    16, 16);
		assignItemRect(MYOSOTIS,    16, 16);
		assignItemRect(W0520,    16, 16);
		assignItemRect(BOSS_TOKEN,    16, 16);
		assignItemRect(OBSIDIAN,    16, 16);
		assignItemRect(TEA,    16, 16);
		assignItemRect(KETTLE,    16, 16);
		assignItemRect(CAT,    16, 16);
		assignItemRect(WALKIE,    16, 16);
		assignItemRect(MUSH,    16, 16);
		assignItemRect(BARRI,    16, 16);
		assignItemRect(TYLENOL,    16, 16);
		assignItemRect(SIESTA,    16, 16);
		assignItemRect(GAVIAL,    16, 16);
		assignItemRect(IBERIA,    16, 16);

	}

	private static final int BAGS       =                                   xy(1, 31);  //16 slots
	public static final int VIAL        = BAGS+0;
	public static final int POUCH       = BAGS+1;
	public static final int HOLDER      = BAGS+2;
	public static final int BANDOLIER   = BAGS+3;
	public static final int HOLSTER     = BAGS+4;
	public static final int ICON_1     = BAGS+5;
	public static final int ICON_2     = BAGS+6;
	public static final int ICON_3     = BAGS+7;
	public static final int ICON_4     = BAGS+8;
	public static final int ICON_5     = BAGS+9;
	public static final int ICON_6     = BAGS+10;
	public static final int ICON_7     = BAGS+11;
	public static final int ICON_8     = BAGS+12;
	public static final int ICON_9     = BAGS+13;
	public static final int ICON_10     = BAGS+14;
	public static final int ICE_BOX     = BAGS+15;


	static{
		assignItemRect(VIAL,        14, 14);
		assignItemRect(POUCH,       14, 15);
		assignItemRect(HOLDER,      16, 16);
		assignItemRect(BANDOLIER,   15, 16);
		assignItemRect(HOLSTER,     15, 16);
		assignItemRect(ICON_1,   	  16, 16);
		assignItemRect(ICON_2,   	  16, 16);
		assignItemRect(ICON_3,   	  16, 16);
		assignItemRect(ICON_4,   	  16, 16);
		assignItemRect(ICON_5,   	  16, 16);
		assignItemRect(ICON_6,   	  16, 16);
		assignItemRect(ICON_7,   	  16, 16);
		assignItemRect(ICON_8,   	  16, 16);
		assignItemRect(ICON_9,   	  16, 16);
		assignItemRect(ICON_10,   	  16, 16);
		assignItemRect(ICE_BOX,   	  16, 16);
	}

	//for smaller 8x8 icons that often accompany an item sprite
	public static class Icons {

		private static final int WIDTH = 16;
		public static final int SIZE = 8;

		public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEM_ICONS, SIZE, SIZE );

		private static int xy(int x, int y){
			x -= 1; y -= 1;
			return x + WIDTH*y;
		}

		private static void assignIconRect( int item, int width, int height ){
			int x = (item % WIDTH) * SIZE;
			int y = (item / WIDTH) * SIZE;
			film.add( item, x, y, x+width, y+height);
		}

		private static final int RINGS          =                            xy(1, 1);  //16 slots
		public static final int RING_ACCURACY   = RINGS+0;
		public static final int RING_ELEMENTS   = RINGS+1;
		public static final int RING_ENERGY     = RINGS+2;
		public static final int RING_EVASION    = RINGS+3;
		public static final int RING_FORCE      = RINGS+4;
		public static final int RING_FUROR      = RINGS+5;
		public static final int RING_HASTE      = RINGS+6;
		public static final int RING_MIGHT      = RINGS+7;
		public static final int RING_SHARPSHOOT = RINGS+8;
		public static final int RING_TENACITY   = RINGS+9;
		public static final int RING_WEALTH     = RINGS+10;
		public static final int RING_SUNLIGHT     = RINGS+11;
		public static final int RING_AMPLIFIED     = RINGS+12;
		public static final int RING_DOMINATE     = RINGS+13;
		public static final int RING_SURPRISE     = RINGS+14;
		public static final int RING_MISTRESS     = RINGS+15;

		static {
			assignIconRect( RING_ACCURACY,      7, 7 );
			assignIconRect( RING_ELEMENTS,      7, 7 );
			assignIconRect( RING_ENERGY,        7, 5 );
			assignIconRect( RING_EVASION,       7, 7 );
			assignIconRect( RING_FORCE,         5, 6 );
			assignIconRect( RING_FUROR,         7, 6 );
			assignIconRect( RING_HASTE,         6, 6 );
			assignIconRect( RING_MIGHT,         7, 7 );
			assignIconRect( RING_SHARPSHOOT,    7, 7 );
			assignIconRect( RING_TENACITY,      6, 6 );
			assignIconRect( RING_WEALTH,        7, 6 );
			assignIconRect( RING_SUNLIGHT,        7, 7 );
			assignIconRect( RING_AMPLIFIED,        7, 7 );
			assignIconRect( RING_DOMINATE,        7, 7 );
			assignIconRect( RING_SURPRISE,        8, 8 );
			assignIconRect( RING_MISTRESS,        8, 8 );
		}

		                                                                                //16 free slots

		private static final int SCROLLS        =                            xy(1, 3);  //16 slots
		public static final int SCROLL_UPGRADE  = SCROLLS+0;
		public static final int SCROLL_IDENTIFY = SCROLLS+1;
		public static final int SCROLL_REMCURSE = SCROLLS+2;
		public static final int SCROLL_MIRRORIMG= SCROLLS+3;
		public static final int SCROLL_RECHARGE = SCROLLS+4;
		public static final int SCROLL_TELEPORT = SCROLLS+5;
		public static final int SCROLL_LULLABY  = SCROLLS+6;
		public static final int SCROLL_MAGICMAP = SCROLLS+7;
		public static final int SCROLL_RAGE     = SCROLLS+8;
		public static final int SCROLL_RETRIB   = SCROLLS+9;
		public static final int SCROLL_TERROR   = SCROLLS+10;
		public static final int SCROLL_TRANSMUTE= SCROLLS+11;
		public static final int SCROLL_WARP= SCROLLS+12;
		static {
			assignIconRect( SCROLL_UPGRADE,     7, 7 );
			assignIconRect( SCROLL_IDENTIFY,    4, 7 );
			assignIconRect( SCROLL_REMCURSE,    7, 7 );
			assignIconRect( SCROLL_MIRRORIMG,   7, 5 );
			assignIconRect( SCROLL_RECHARGE,    7, 5 );
			assignIconRect( SCROLL_TELEPORT,    7, 7 );
			assignIconRect( SCROLL_LULLABY,     7, 6 );
			assignIconRect( SCROLL_MAGICMAP,    7, 7 );
			assignIconRect( SCROLL_RAGE,        6, 6 );
			assignIconRect( SCROLL_RETRIB,      5, 6 );
			assignIconRect( SCROLL_TERROR,      5, 7 );
			assignIconRect( SCROLL_TRANSMUTE,   7, 7 );
			assignIconRect( SCROLL_WARP,   7, 7 );
		}

		private static final int EXOTIC_SCROLLS =                            xy(1, 4);  //16 slots
		public static final int SCROLL_ENCHANT  = EXOTIC_SCROLLS+0;
		public static final int SCROLL_DIVINATE = EXOTIC_SCROLLS+1;
		public static final int SCROLL_ANTIMAGIC= EXOTIC_SCROLLS+2;
		public static final int SCROLL_PRISIMG  = EXOTIC_SCROLLS+3;
		public static final int SCROLL_MYSTENRG = EXOTIC_SCROLLS+4;
		public static final int SCROLL_PASSAGE  = EXOTIC_SCROLLS+5;
		public static final int SCROLL_AFFECTION= EXOTIC_SCROLLS+6;
		public static final int SCROLL_FORESIGHT= EXOTIC_SCROLLS+7;
		public static final int SCROLL_CONFUSION= EXOTIC_SCROLLS+8;
		public static final int SCROLL_PSIBLAST = EXOTIC_SCROLLS+9;
		public static final int SCROLL_PETRIF   = EXOTIC_SCROLLS+10;
		public static final int SCROLL_POLYMORPH= EXOTIC_SCROLLS+11;
		public static final int SCROLL_DOMINATE= EXOTIC_SCROLLS+12;
		static {
			assignIconRect( SCROLL_ENCHANT,     7, 7 );
			assignIconRect( SCROLL_DIVINATE,    7, 6 );
			assignIconRect( SCROLL_ANTIMAGIC,   7, 7 );
			assignIconRect( SCROLL_PRISIMG,     5, 7 );
			assignIconRect( SCROLL_MYSTENRG,    7, 5 );
			assignIconRect( SCROLL_PASSAGE,     5, 7 );
			assignIconRect( SCROLL_AFFECTION,   7, 6 );
			assignIconRect( SCROLL_FORESIGHT,   7, 5 );
			assignIconRect( SCROLL_CONFUSION,   7, 7 );
			assignIconRect( SCROLL_PSIBLAST,    5, 6 );
			assignIconRect( SCROLL_PETRIF,      7, 5 );
			assignIconRect( SCROLL_POLYMORPH,   7, 6 );
			assignIconRect( SCROLL_DOMINATE,   7, 7 );
		}

		                                                                                //16 free slots

		private static final int POTIONS        =                            xy(1, 6);  //16 slots
		public static final int POTION_STRENGTH = POTIONS+0;
		public static final int POTION_HEALING  = POTIONS+1;
		public static final int POTION_MINDVIS  = POTIONS+2;
		public static final int POTION_FROST    = POTIONS+3;
		public static final int POTION_LIQFLAME = POTIONS+4;
		public static final int POTION_TOXICGAS = POTIONS+5;
		public static final int POTION_HASTE    = POTIONS+6;
		public static final int POTION_INVIS    = POTIONS+7;
		public static final int POTION_LEVITATE = POTIONS+8;
		public static final int POTION_PARAGAS  = POTIONS+9;
		public static final int POTION_PURITY   = POTIONS+10;
		public static final int POTION_EXP      = POTIONS+11;
		static {
			assignIconRect( POTION_STRENGTH,    7, 7 );
			assignIconRect( POTION_HEALING,     6, 7 );
			assignIconRect( POTION_MINDVIS,     7, 5 );
			assignIconRect( POTION_FROST,       7, 7 );
			assignIconRect( POTION_LIQFLAME,    5, 7 );
			assignIconRect( POTION_TOXICGAS,    7, 7 );
			assignIconRect( POTION_HASTE,       6, 6 );
			assignIconRect( POTION_INVIS,       5, 7 );
			assignIconRect( POTION_LEVITATE,    6, 7 );
			assignIconRect( POTION_PARAGAS,     7, 7 );
			assignIconRect( POTION_PURITY,      5, 7 );
			assignIconRect( POTION_EXP,         7, 7 );
		}

		private static final int EXOTIC_POTIONS =                            xy(1, 7);  //16 slots
		public static final int POTION_ARENSURGE= EXOTIC_POTIONS+0;
		public static final int POTION_SHIELDING= EXOTIC_POTIONS+1;
		public static final int POTION_MAGISIGHT= EXOTIC_POTIONS+2;
		public static final int POTION_SNAPFREEZ= EXOTIC_POTIONS+3;
		public static final int POTION_DRGBREATH= EXOTIC_POTIONS+4;
		public static final int POTION_CORROGAS = EXOTIC_POTIONS+5;
		public static final int POTION_STAMINA  = EXOTIC_POTIONS+6;
		public static final int POTION_SHROUDFOG= EXOTIC_POTIONS+7;
		public static final int POTION_STRMCLOUD= EXOTIC_POTIONS+8;
		public static final int POTION_EARTHARMR= EXOTIC_POTIONS+9;
		public static final int POTION_CLEANSE  = EXOTIC_POTIONS+10;
		public static final int POTION_HOLYFUROR= EXOTIC_POTIONS+11;
		static {
			assignIconRect( POTION_ARENSURGE,   7, 7 );
			assignIconRect( POTION_SHIELDING,   6, 6 );
			assignIconRect( POTION_MAGISIGHT,   7, 5 );
			assignIconRect( POTION_SNAPFREEZ,   7, 7 );
			assignIconRect( POTION_DRGBREATH,   7, 7 );
			assignIconRect( POTION_CORROGAS,    7, 7 );
			assignIconRect( POTION_STAMINA,     6, 6 );
			assignIconRect( POTION_SHROUDFOG,   7, 7 );
			assignIconRect( POTION_STRMCLOUD,   7, 7 );
			assignIconRect( POTION_EARTHARMR,   6, 6 );
			assignIconRect( POTION_CLEANSE,     7, 7 );
			assignIconRect( POTION_HOLYFUROR,   5, 7 );
		}
	}

}
