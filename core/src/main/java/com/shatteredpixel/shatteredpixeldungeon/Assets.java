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
 * This program is distributed in the hope that it will be useful
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon;

public class Assets {

	public static class Effects {
		public static final String EFFECTS		= "effects/effects.png";
		public static final String FIREBALL		= "effects/fireball.png";
		public static final String SPECKS		= "effects/specks.png";
		public static final String SPELL_ICONS	= "effects/spell_icons.png";
	}

	public static class Environment {
		public static final String TERRAIN_FEATURES	= "environment/terrain_features.png";

		public static final String VISUAL_GRID	= "environment/visual_grid.png";
		public static final String WALL_BLOCKING= "environment/wall_blocking.png";

		public static final String TILES_SEWERS	= "environment/tiles_sewers.png";
		public static final String TILES_PRISON	= "environment/tiles_prison.png";
		public static final String TILES_CAVES	= "environment/tiles_caves.png";
		public static final String TILES_CITY	= "environment/tiles_city.png";
		public static final String TILES_HALLS	= "environment/tiles_halls.png";
		public static final String TILSE_RHODES	= "environment/tiles_rhodos.png";
		public static final String TILSE_SIESTA	= "environment/tiles_siesta.png";
		public static final String TILSE_SIESTA2	= "environment/tiles_siesta2.png";
		public static final String TILSE_IBERIA	= "environment/tiles_iberia.png";
		public static final String TILSE_IBERIA2	= "environment/tiles_iberia2.png";
		public static final String TILSE_SARGON	= "environment/tiles_sargon.png";
        public static final String TILSE_SARGON2	= "environment/tiles_sargon2.png";

		public static final String WATER_SEWERS	= "environment/water0.png";
		public static final String WATER_PRISON	= "environment/water1.png";
		public static final String WATER_CAVES	= "environment/water2.png";
		public static final String WATER_CITY	= "environment/water3.png";
		public static final String WATER_HALLS	= "environment/water4.png";
		public static final String WATER_SIESTA	= "environment/water5.png";
		public static final String WATER_SIESTA2	= "environment/water6.png";
		public static final String WATER_IBERIA	= "environment/water7.png";
		public static final String WATER_IBERIA2	= "environment/water7.png";

		public static final String WEAK_FLOOR       = "environment/custom_tiles/weak_floor.png";
		public static final String SEWER_BOSS       = "environment/custom_tiles/sewer_boss.png";
		public static final String PRISON_QUEST     = "environment/custom_tiles/prison_quests.png";
		public static final String PRISON_EXIT_OLD  = "environment/custom_tiles/prison_exit_old.png";
		public static final String PRISON_EXIT_NEW  = "environment/custom_tiles/prison_exit_new.png";
		public static final String CAVES_BOSS       = "environment/custom_tiles/caves_boss.png";
		public static final String CITY_BOSS        = "environment/custom_tiles/city_boss.png";
		public static final String HALLS_SP         = "environment/custom_tiles/halls_special.png";

		//명픽던 커스텀타일

		public static final String RHODES_27F         = "environment/custom_tiles/rhodes_27f.png";
		public static final String RHODES_28F         = "environment/custom_tiles/rhodes_28f.png";
		public static final String RHODES_29F         = "environment/custom_tiles/rhodes_29f.png";
		public static final String RHODES_30F         = "environment/custom_tiles/rhodes_30f.png";
		public static final String GAVIAL_BOSS2         = "environment/custom_tiles/gavial_boss2.png";
		public static final String IBERIA_BOSS1_1         = "environment/custom_tiles/iberia_boss1_1.png";
		public static final String IBERIA_BOSS1_2         = "environment/custom_tiles/iberia_boss1_2.png";
		public static final String IBERIA_BOSS2         = "environment/custom_tiles/iberia_boss2.png";
	}
	
	//TODO include other font assets here? Some are platform specific though...
	public static class Fonts {
		public static final String PIXELFONT= "fonts/pixel_font.png";
	}

	public static class Interfaces {
		public static final String ARCS_BG  = "interfaces/arcs1.png";
		public static final String ARCS_FG  = "interfaces/arcs2.png";

		public static final String BANNERS	= "interfaces/banners.png";
		public static final String BADGES	= "interfaces/badges.png";
		public static final String LOCKED	= "interfaces/locked_badge.png";

		public static final String CHROME	= "interfaces/chrome.png";
		public static final String ICONS	= "interfaces/icons.png";
		public static final String STATUS	= "interfaces/status_pane.png";
		public static final String MENU		= "interfaces/menu_button.png";
		public static final String HP_BAR	= "interfaces/hp_bar.png";
		public static final String SHLD_BAR = "interfaces/shield_bar.png";
		public static final String XP_BAR	= "interfaces/exp_bar.png";
		public static final String TOOLBAR	= "interfaces/toolbar.png";
		public static final String SHADOW   = "interfaces/shadow.png";
		public static final String BOSSHP   = "interfaces/boss_hp.png";

		public static final String SURFACE	= "interfaces/surface.png";

		public static final String LOADING_SEWERS	= "interfaces/loading_sewers.png";
		public static final String LOADING_PRISON	= "interfaces/loading_prison.png";
		public static final String LOADING_CAVES	= "interfaces/loading_caves.png";
		public static final String LOADING_CITY	    = "interfaces/loading_city.png";
		public static final String LOADING_HALLS	= "interfaces/loading_halls.png";
		public static final String LOADING_RHODOS	= "interfaces/loading_rhodos.png";
		public static final String LOADING_SIESTA	= "interfaces/loading_siesta.png";

		public static final String BUFFS_SMALL	= "interfaces/buffs.png";
		public static final String BUFFS_LARGE	= "interfaces/large_buffs.png";

		public static final String TALENT_ICONS	 = "interfaces/talent_icons.png";
		public static final String TALENT_BUTTON = "interfaces/talent_button.png";
	}

	//these points to resource bundles, not raw asset files
	public static class Messages {
		public static final String ACTORS   = "messages/actors/actors";
		public static final String ITEMS    = "messages/items/items";
		public static final String JOURNAL  = "messages/journal/journal";
		public static final String LEVELS   = "messages/levels/levels";
		public static final String MISC     = "messages/misc/misc";
		public static final String PLANTS   = "messages/plants/plants";
		public static final String SCENES   = "messages/scenes/scenes";
		public static final String UI       = "messages/ui/ui";
		public static final String WINDOWS  = "messages/windows/windows";
	}

	public static class Music {
		public static final String GAME		= "music/game.ogg";
		public static final String GAME2		= "music/game2.ogg";
		public static final String GAME3		= "music/game3.ogg";
		public static final String GAME4		= "music/game4.ogg";
		public static final String GAME5		= "music/game5.ogg";
		public static final String GAME6_SIESTA		= "music/game_siesta1.ogg";
		public static final String GAME6_SIESTA2		= "music/game_siesta2.ogg";
		public static final String GAME6_SARGON1		= "music/game_sargon1.ogg";
		public static final String GAME6_SARGON2		= "music/game_sargon2.ogg";
		public static final String GAME6_IBERIA1		= "music/game_iberia1.ogg";
		public static final String GAME6_IBERIA2		= "music/game_iberia2.ogg";
		public static final String BOSS		= "music/boss.ogg";
		public static final String BOSS2		= "music/boss2.ogg";
		public static final String BOSS3		= "music/boss3.ogg";
		public static final String BOSS3_MUDROCKSP	= "music/mudrock.ogg";
		public static final String BOSS4		= "music/boss4.ogg";
		public static final String BOSS5		= "music/boss5.ogg";
		public static final String BOSS6_SIESTA		= "music/boss_siesta1.ogg";
		public static final String BOSS6_SIESTA2		= "music/boss_siesta2.ogg";
		public static final String BOSS6_SARGON		= "music/boss_sargon1.ogg";
		public static final String BOSS6_SARGON2		= "music/boss_sargon2.ogg";
		public static final String BOSS6_IBERIA1		= "music/boss_iberia1.ogg";
		public static final String BOSS6_IBERIA2		= "music/boss_iberia2.ogg";
		public static final String SURFACE	= "music/surface.ogg";
		public static final String THEME	= "music/theme.ogg";
		public static final String RHODOS	= "music/rhodos.ogg";
		public static final String BOSS_KAL		= "music/boss_kalt.ogg";
	}

	public static class Sounds {


		public static final String CLICK	= "sounds/click.mp3";
		public static final String BADGE	= "sounds/badge.mp3";
		public static final String GOLD		= "sounds/gold.mp3";

		public static final String OPEN		= "sounds/door_open.mp3";
		public static final String UNLOCK	= "sounds/unlock.mp3";
		public static final String ITEM		= "sounds/item.mp3";
		public static final String DEWDROP	= "sounds/dewdrop.mp3";
		public static final String STEP		= "sounds/step.mp3";
		public static final String WATER	= "sounds/water.mp3";
		public static final String GRASS	= "sounds/grass.mp3";
		public static final String TRAMPLE	= "sounds/trample.mp3";
		public static final String STURDY	= "sounds/sturdy.mp3";

		public static final String HIT		        = "sounds/hit.mp3";
		public static final String MISS		        = "sounds/miss.mp3";
		public static final String HIT_SLASH        = "sounds/hit_slash.mp3";
		public static final String HIT_SLASH2        = "sounds/hit_slash2.mp3";
		public static final String HIT_PUNCH        = "sounds/hit_punch.mp3";
		public static final String HIT_SPEAR        = "sounds/hit_spear.mp3";


		public static final String HIT_STAB         = "sounds/hit_stab.mp3";
		public static final String HIT_CRUSH        = "sounds/hit_crush.mp3";
		public static final String HIT_MAGIC        = "sounds/hit_magic.mp3";
		public static final String HIT_STRONG       = "sounds/hit_strong.mp3";
		public static final String HIT_PARRY        = "sounds/hit_parry.mp3";
		public static final String HIT_ARROW        = "sounds/hit_arrow.mp3";
		public static final String ATK_SPIRITBOW    = "sounds/atk_spiritbow.mp3";
		public static final String ATK_CROSSBOW     = "sounds/atk_crossbow.mp3";
		public static final String HEALTH_WARN      = "sounds/health_warn.mp3";
		public static final String HEALTH_CRITICAL  = "sounds/health_critical.mp3";

		public static final String DESCEND	= "sounds/descend.mp3";
		public static final String EAT		= "sounds/eat.mp3";
		public static final String READ		= "sounds/read.mp3";
		public static final String LULLABY	= "sounds/lullaby.mp3";
		public static final String DRINK	= "sounds/drink.mp3";
		public static final String SHATTER	= "sounds/shatter.mp3";
		public static final String ZAP		= "sounds/zap.mp3";
		public static final String ZAP_GUN		= "sounds/zap_gun.mp3";
		public static final String LIGHTNING= "sounds/lightning.mp3";
		public static final String LEVELUP	= "sounds/levelup.mp3";
		public static final String DEATH	= "sounds/death.mp3";
		public static final String CHALLENGE= "sounds/challenge.mp3";
		public static final String CURSED	= "sounds/cursed.mp3";
		public static final String TRAP		= "sounds/trap.mp3";
		public static final String EVOKE	= "sounds/evoke.mp3";
		public static final String TOMB		= "sounds/tomb.mp3";
		public static final String ALERT	= "sounds/alert.mp3";
		public static final String MELD		= "sounds/meld.mp3";
		public static final String BOSS		= "sounds/boss.mp3";
		public static final String BLAST	= "sounds/blast.mp3";
		public static final String PLANT	= "sounds/plant.mp3";
		public static final String RAY		= "sounds/ray.mp3";
		public static final String BEACON	= "sounds/beacon.mp3";
		public static final String TELEPORT	= "sounds/teleport.mp3";
		public static final String CHARMS	= "sounds/charms.mp3";
		public static final String MASTERY	= "sounds/mastery.mp3";
		public static final String PUFF		= "sounds/puff.mp3";
		public static final String ROCKS	= "sounds/rocks.mp3";
		public static final String BURNING	= "sounds/burning.mp3";
		public static final String FALLING	= "sounds/falling.mp3";
		public static final String GHOST	= "sounds/ghost.mp3";
		public static final String SECRET	= "sounds/secret.mp3";
		public static final String BONES	= "sounds/bones.mp3";
		public static final String BEE      = "sounds/bee.mp3";
		public static final String DEGRADE  = "sounds/degrade.mp3";
		public static final String MIMIC    = "sounds/mimic.mp3";
		public static final String DEBUFF   = "sounds/debuff.mp3";
		public static final String CHARGEUP = "sounds/chargeup.mp3";
		public static final String GAS      = "sounds/gas.mp3";
		public static final String CHAINS   = "sounds/chains.mp3";
		public static final String SCAN     = "sounds/scan.mp3";
		public static final String SHEEP    = "sounds/sheep.mp3";

		//명픽던에서만 추가된  효과음들↓
		public static final String TIMESTOP    = "sounds/timestop.mp3";

		public static final String FROST    = "sounds/frost.mp3";
		public static final String SHINNING    = "sounds/shining.mp3";
		public static final String AJIMU   = "sounds/ajimu.mp3";

		public static final String HIT_KNIFE        = "sounds/hit_knife.mp3";
		public static final String HIT_CHAINSAW        = "sounds/hit_chainsaw.mp3";
		public static final String HIT_CHAINSAW2        = "sounds/hit_chainsaw2.mp3";
		public static final String HIT_SHOTGUN        = "sounds/hit_shotgun.mp3";
		public static final String HIT_BIRD        = "sounds/hit_bird.mp3";
		public static final String HIT_WALL1        = "sounds/hit_wall1.mp3";
		public static final String HIT_WALL2        = "sounds/hit_wall2.mp3";
		public static final String HIT_GUNLANCE        = "sounds/hit_gunlance.mp3";
		public static final String HIT_REVOLVER        = "sounds/hit_revolver.mp3";
		public static final String HIT_SNIPER        = "sounds/hit_sniping.mp3";
		public static final String HIT_RINGOUT        = "sounds/hit_ringout.mp3";
		public static final String HIT_BREAK        = "sounds/hit_break.mp3";
		public static final String HIT_GUN        = "sounds/hit_gun.mp3";
		public static final String HIT_GUN2        = "sounds/hit_gun.mp3";
		public static final String HIT_GUN3        = "sounds/hit_gun3.mp3";
		public static final String HIT_SNIPER2        = "sounds/hit_sniper.mp3";
		public static final String HIT_AR        = "sounds/hit_ar.mp3";
		public static final String HIT_PISTOL        = "sounds/hit_pistol.mp3";
		public static final String HIT_BONK        = "sounds/hit_bonk.mp3";
		public static final String HIT_SWORD        = "sounds/hit_sword.mp3";
		public static final String HIT_SWORD2        = "sounds/hit_sword2.mp3";
		public static final String HIT_DUALSTRIKE        = "sounds/hit_dualstrike.mp3";
		public static final String HIT_SPLASH       = "sounds/hit_splash.mp3";
		public static final String HIT_GLUTONY       = "sounds/hit_glutony.mp3";
		public static final String HIT_STRIKE       = "sounds/hit_strike.mp3";
		public static final String HIT_DUSK       = "sounds/hit_dusk.mp3";
		public static final String HIT_WHIP       = "sounds/hit_whip.mp3";
		public static final String CHAINS2   = "sounds/chains2.mp3";
		public static final String RELOAD   = "sounds/reload.mp3";
        public static final String SPLASH   = "sounds/splash.mp3";

		public static final String SKILL_BASIC       = "sounds/skill_basic.mp3";
		public static final String SKILL_TEXAS       = "sounds/skill_texas.mp3";
		public static final String SKILL_SURTR       = "sounds/skill_surtr.mp3";
		public static final String SKILL_BABYNIGHT       = "sounds/skill_babynight.mp3";
		public static final String SKILL_MON1       = "sounds/skill_mon1.mp3";
		public static final String SKILL_MON2       = "sounds/skill_mon2.mp3";
		public static final String SKILL_BEEP       = "sounds/skill_beep.mp3";
		public static final String SKILL_CHAINSAW        = "sounds/skill_chainsaw.mp3";
		public static final String SKILL_WOLFPACK        = "sounds/skill_wolfpack.mp3";
		public static final String SKILL_CROSSBOW       = "sounds/skill_crossbow.mp3";
		public static final String SKILL_YOUWISH   = "sounds/skill_youwish.mp3";
		public static final String SKILL_SILVERSLASH   = "sounds/skill_silverslash.mp3";
		public static final String SKILL_SORA   = "sounds/skill_sora.mp3";


		public static final String[] all = new String[]{
				CLICK, BADGE, GOLD,

				OPEN, UNLOCK, ITEM, DEWDROP, STEP, WATER, GRASS, TRAMPLE, STURDY,

				HIT_PUNCH, HIT_SPEAR, HIT, MISS, HIT_SLASH, HIT_SLASH2, HIT_STAB, HIT_CRUSH, HIT_MAGIC, HIT_STRONG, HIT_PARRY, HIT_SWORD, HIT_SWORD2, HIT_DUALSTRIKE,
				HIT_ARROW, HIT_WHIP, ATK_SPIRITBOW, ATK_CROSSBOW, HEALTH_WARN, HEALTH_CRITICAL, HIT_RINGOUT, HIT_BREAK, HIT_BONK, HIT_SPLASH, HIT_DUSK, HIT_REVOLVER,

				DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, ZAP_GUN, LIGHTNING, LEVELUP, DEATH,
				CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
				TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
				BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, CHAINS, SCAN, SHEEP, RELOAD,

				//명픽던에서만 추가된  효과음들↓
				TIMESTOP,
				HIT_KNIFE, HIT_CHAINSAW, HIT_CHAINSAW2, HIT_BIRD, HIT_WALL1, HIT_WALL2, HIT_SHOTGUN, HIT_GUNLANCE, HIT_GLUTONY,
				HIT_SNIPER, HIT_GUN, HIT_GUN2, HIT_GUN3, HIT_PISTOL, HIT_SNIPER2, HIT_AR, FROST, SHINNING, AJIMU, HIT_STRIKE,
				SKILL_BASIC, SKILL_BEEP, SKILL_CROSSBOW, SKILL_WOLFPACK, SKILL_CHAINSAW, SKILL_SILVERSLASH, SKILL_YOUWISH,
				SKILL_MON1, SKILL_MON2, SKILL_TEXAS , SKILL_SURTR, SKILL_BABYNIGHT, SKILL_SORA, SPLASH,
				CHAINS2,
		};
	}

	public static class Splashes {
		public static final String WARRIOR	= "splashes/warrior.jpg";
		public static final String MAGE		= "splashes/mage.jpg";
		public static final String ROGUE	= "splashes/rogue.jpg";
		public static final String HUNTRESS	= "splashes/huntress.jpg";
		public static final String ROSECAT	= "splashes/rosemary.png";
		public static final String NEARL	= "splashes/nearl.png";
		public static final String CHEN	= "splashes/chen.jpg";
	}

	public static class Sprites {
		public static final String ITEMS	    = "sprites/items.png";
		public static final String ITEM_ICONS   = "sprites/item_icons.png";

		public static final String BLAZE	= "sprites/blaze.png";
		public static final String AMIYA		= "sprites/amiya.png";
		public static final String RED	= "sprites/red.png";
		public static final String GREY	= "sprites/greythroat.png";
		public static final String ROSEMARY	= "sprites/rosemary.png";
		public static final String NEARL	= "sprites/nearl.png";
		public static final String NEARL_SPEAR	= "sprites/nearl_spear.png";
		public static final String CHEN	= "sprites/chen.png";
		public static final String RISKARM	= "sprites/riskarm.png";
		public static final String AVATARS	= "sprites/avatars.png";
		public static final String PET		= "sprites/pet.png";
		public static final String DONUT		= "sprites/Wraith_donut.png";
		public static final String AMULET	= "sprites/amulet.png";

		public static final String KEEPER	= "sprites/shopkeeper.png";
		public static final String TENTACLE	= "sprites/tentacle.png";
		public static final String CIVILIAN	= "sprites/civilian.png";
		public static final String GUARD	= "sprites/guard.png";
		public static final String VULCAN	= "sprites/blacksmith.png";

		public static final String BABYBUG	= "sprites/babybug.png";
		public static final String BUG		= "sprites/bug.png";
		public static final String BUG_A		= "sprites/bug_a.png";
		public static final String SPITTER		= "sprites/spitter.png";
		public static final String INFANTRY	= "sprites/Infantry.png";
		public static final String CROSSBOWMAN	= "sprites/crossbowman.png";
		public static final String SHILDED	= "sprites/Shielded.png";
		public static final String SHILDED_L	= "sprites/ShieldedLeader.png";
		public static final String HAUND		= "sprites/haund.png";
		public static final String DIFENDER		= "sprites/difender.png";
		public static final String YOMA	= "sprites/yoma.png";
		public static final String HANDCLAP		= "sprites/handclap.png";

		public static final String BRUTE	= "sprites/brute.png";
		public static final String CASTER	= "sprites/caster.png";
		public static final String SPINNER	= "sprites/spinner.png";
		public static final String MUD	= "sprites/mudrock.png";
		public static final String S_GOLEM	= "sprites/Stone_golem.png";
		public static final String I_GOLEM	= "sprites/iron_golem.png";


		public static final String LURKER	= "sprites/lurker.png";
		public static final String MEPHISTO		= "sprites/mephisto.png";
		public static final String A_MASTER2	= "sprites/arts_master2.png";

		public static final String BOMBTAIL	= "sprites/bombtail.png";
		public static final String RIVENGER	= "sprites/rivenger.png";
		public static final String AVENGER	= "sprites/avenger.png";
		public static final String THIEF	= "sprites/thief.png";
		public static final String W	= "sprites/w.png";

		public static final String BREAKER		= "sprites/breaker.png";
		public static final String ELEMENTAL= "sprites/elemental.png";
		public static final String MONK		= "sprites/monk.png";
		public static final String THROWER	= "sprites/thrower.png";
		public static final String GOLEM	= "sprites/junkman.png";
		public static final String ENRAGED	= "sprites/enraged.png";
		public static final String DRONE	= "sprites/drone.png";
		public static final String GUERRILLA	= "sprites/guerrilla.png";
		public static final String SUCCUBUS	= "sprites/succubus.png";
		public static final String SCORPIO	= "sprites/scorpio.png";
		public static final String FISTS	= "sprites/tongvengers.png";
		public static final String YOG		= "sprites/yog.png";
		public static final String LARVA	= "sprites/larva.png";
		public static final String ACE	= "sprites/ace.png";

		public static final String IMP		= "sprites/cannot.png";
		public static final String AJIMU	= "sprites/ajimu.png";
		public static final String BEE      = "sprites/bee.png";
		public static final String MIMIC    = "sprites/mimic.png";
		public static final String ROT_LASH = "sprites/rot_lasher.png";
		public static final String ROT_HEART= "sprites/rot_heart.png";
		public static final String S_CASTER    = "sprites/Sarkaz_Caster.png";
		public static final String WARDS    = "sprites/wards.png";
		public static final String GUARDIAN	= "sprites/guardian.png";
		public static final String SCOUT	= "sprites/scout.png";
		public static final String GHOUL	= "sprites/ghoul.png";
		public static final String RIPPER	= "sprites/ripper.png";
		public static final String SPAWNER	= "sprites/spawner.png";
		public static final String A_MASTER1	= "sprites/arts_master1.png";
		public static final String PYLON	= "sprites/pylon.png";
		public static final String POSSESSED	= "sprites/possessed.png";
		public static final String UNDEAD	= "sprites/possessed.png";

		//통벤저스H
		public static final String BLACK_SNAKE	= "sprites/black_snake.png"; //72x44
		public static final String BOSS_2	= "sprites/boss_2.png"; //44x40
		public static final String BOSS_3	= "sprites/boss_3.png"; //46x44
		public static final String PATRIOT	= "sprites/patriot.png"; //72x48
		public static final String MEPHI_SINGER	= "sprites/mephi_singer.png"; //58x42
		public static final String EMPEROR_BLADE	= "sprites/emperor_blade.png"; //72x44

		public static final String LOTUS	= "sprites/lotus.png";

		//변종
		public static final String STRIKER	= "sprites/striker.png";
		public static final String IMPERIAL_ARTILLERY	= "sprites/imperial_artillery.png";
		public static final String RAIDER	= "sprites/ursus_raider.png";
		public static final String RAPTOR	= "sprites/raptor.png";
		public static final String RAPTOR_FIRE	= "sprites/raptor_fire.png";
		public static final String CS	= "sprites/crownslayer.png";

		//시에스타
		public static final String SIESTA_INFANTRY	= "sprites/siesta_infantry.png";
		public static final String SIESTA_SNIPER	= "sprites/siesta_sniper.png";
		public static final String SIESTA_AGENT	= "sprites/siesta_agent.png";
		public static final String ERGATE	= "sprites/ergate.png";
		public static final String TANK	= "sprites/tank.png";
		public static final String TANK_MAN	= "sprites/tank_man.png";
		public static final String WAVE_CASTER	= "sprites/wave_caster.png";
		public static final String CRONIN	= "sprites/cronin.png";
		public static final String SCHWARZ	= "sprites/schwarz.png";
		public static final String CEYLON	= "sprites/ceylon.png";

		public static final String POMPEII	= "sprites/pompeii.png";
		public static final String ROCKBREAKER	= "sprites/rockbreaker.png";
		public static final String BIG_BUG	= "sprites/big_bug.png";
		public static final String RED_SPITTER	= "sprites/red_spitter.png";
		public static final String METAL_CRAB	= "sprites/metal_crab.png";
		public static final String VOLCANO	= "sprites/volcano.png";
		public static final String ORIGINIUTANT	= "sprites/originiutant.png";
		public static final String MUTANT_SPIDER	= "sprites/mutant_spider.png";

		//가비알
		public static final String TIACAUH_WARRIOR	= "sprites/tiacauhwarrior.png";
		public static final String TIACAUH_LANCER	= "sprites/tiacauhlancer.png";
		public static final String TIACAUH_FANATIC	= "sprites/tiacauhfanatic.png";
		public static final String TIACAUH_RIPPER	= "sprites/tiacauhripper.png";
		public static final String TIACAUH_BRAVE	= "sprites/tiacauh_brave.png";
		public static final String TIACAUH_RITUA	= "sprites/tiacauh_ritualist.png";
		public static final String TIACAUH_SHAMAN	= "sprites/tiacauh_shaman.png";
		public static final String TIACAUH_DRUGS	= "sprites/tiacauh_drugs.png";
		public static final String TIACAUH_IMPALER	= "sprites/tiacauhimpaler.png";
		public static final String TIACAUH_SHREDDER	= "sprites/tiacauhshredder.png";
		public static final String GIANT_MUSHROOM	= "sprites/giant_mushroom.png";
		public static final String TOMIMI	= "sprites/tomimi.png";
		public static final String PILLAR	= "sprites/pillar.png";
		public static final String BIG_UGLY	= "sprites/big_ugly.png";
		public static final String JUMAMA	= "sprites/jumama.png";
		public static final String HELP_LANCET	= "sprites/lancet.png";

		//이베리아-에기르
		public static final String SEA_DRIFTER	= "sprites/sea_drifter.png";
		public static final String SEA_RUNNER	= "sprites/sea_runner.png";
		public static final String SEA_REAPER	= "sprites/sea_reaper.png";
		public static final String SEA_CRAWLER	= "sprites/sea_crawler.png";
		public static final String FIRST_TALK	= "sprites/first_talk.png";

		public static final String BELFLY	= "sprites/belfry.png";

		public static final String SEA_SPEWER	= "sprites/sea_spewer.png";
		public static final String SEA_PREDATOR	= "sprites/sea_predator.png";
		public static final String SEA_BRANDGUIDER	= "sprites/sea_brandguider.png";
		public static final String SEA_LEEF	= "sprites/sea_reef.png";

        public static final String ENDSPEAKER1	= "sprites/endspeaker1.png";
        public static final String ENDSPEAKER2	= "sprites/endspeaker2.png";
        public static final String ENDSPEAKER3	= "sprites/endspeaker3.png";
        public static final String ENDSPEAKER4	= "sprites/endspeaker4.png";


		public static final String SKADI_MULA	= "sprites/skadi_mula.png";
		public static final String MULA_HEAD	= "sprites/mula_1.png";
		public static final String MULA_BODY	= "sprites/mula_2.png";
		public static final String MULA_TAIL	= "sprites/mula_3.png";

		public static final String LAST_KNIGHT	= "sprites/last_knights.png";
		public static final String LAST_KNIGHT2	= "sprites/last_knights2.png";




		//명픽던에서만 추가된 것들↓
		public static final String KALTSIT	= "sprites/kaltsit.png";
		public static final String KALTSIT_NPC	= "sprites/kaltsit_npc.png";
		public static final String GREEN_CAT_HEAD	= "sprites/green_cat_head.png";
		public static final String CLOSURE	= "sprites/closure.png";
		public static final String DOBERMANN	= "sprites/Dobermann.png";
		public static final String PRTS1	= "sprites/PRTS.png";
		public static final String PRTS2	= "sprites/PRTS_corpse.png";
		public static final String PRTS3	= "sprites/PRTS_END.png";
		public static final String WEEDY	= "sprites/weedy.png";
		public static final String KEEPER2	= "sprites/keeper.png";
		public static final String NPC_JESSI	= "sprites/npc_jessi.png";
		public static final String NPC_FROST	= "sprites/npc_frost.png";
		public static final String NPC_DUMMY	= "sprites/dummy.png";
		public static final String NPC_PILOT	= "sprites/pilot.png";
		public static final String NPC_PHANTOM	= "sprites/phantom.png";
		public static final String NPC_ASTESIA	= "sprites/astesia_npc.png";
		public static final String NPC_GAVIAL	= "sprites/gavial.png";
		public static final String NPC_PINK	= "sprites/pink_doggi.png";
		public static final String NPC_IRENE	= "sprites/npc_irene.png";
		public static final String NPC_DARIO	= "sprites/npc_dario.png";
        public static final String NPC_MAGE	= "sprites/npc_mage.png";
        public static final String NPC_GUARD	= "sprites/npc_guard.png";
		public static final String TEXAS_SHOPKEEPER	= "sprites/texas_shopkeeper.png";
        public static final String DELIVERY_DRON	= "sprites/delivery_dron.png";

		public static final String SARKAZ_SENTINEL	= "sprites/Sarkaz_Sentinel.png";
		public static final String SARKAZ_CENTURION	= "sprites/Sarkaz_Centurion.png";
		public static final String BLOOD_SHAMAN	= "sprites/Blood_Shaman.png";
		public static final String FAUST	= "sprites/Faust.png";
		public static final String ZEALOT	= "sprites/zealot.png";

        public static final String SENTRY	= "sprites/sentry.png";

		public static final String LENS	= "sprites/lens.png";
		public static final String ROCK_CRAB	= "sprites/rock_crab.png";
		public static final String SEABORN	= "sprites/seaborn.png";
		public static final String MON3TER	= "sprites/mon3ter.png";
		public static final String ZUZAI	= "sprites/zuzai.png";
		public static final String KAZE	= "sprites/kazemaru.png";

		public static final String FANATIC	= "sprites/fanatic.png";
		public static final String AIRBORNE	= "sprites/airborne.png";
		public static final String URSUS_INFANTRI	= "sprites/Ursus_Infantri.png";
		public static final String SARKAZ_SWARDMAN	= "sprites/Sarkaz_Swordsman.png";
		public static final String SARKAZ_SNIPER	= "sprites/Sarkaz_Sniper.png";
		public static final String SARKAZ_SNIPER_E	= "sprites/Sarkaz_Sniper_E.png";
		public static final String LANCER	= "sprites/Lancer.png";
		public static final String GHOST1	= "sprites/ghost_1.png";
		public static final String OBELISK	= "sprites/Obelisk.png";
		public static final String GOPNIK	= "sprites/gopnik.png";



		// 방벽
		public static final String EX42	= "sprites/ex42.png";
		public static final String GLOW_DRON	= "sprites/glow_dron.png";
		public static final String RED_GOLEM	= "sprites/red_golem.png";

		//스킨
		public static final String TALRU_FIGHT	= "sprites/talru.png";
		public static final String FNOVA	= "sprites/nova.png";
		public static final String GRN	= "sprites/grani.png";
		public static final String SKD	= "sprites/skadi.png";
		public static final String SSR	= "sprites/sussurro.png";
		public static final String JESSI	= "sprites/jessica.png";
		public static final String LAPPY	= "sprites/lappy.png";
		public static final String FROST	= "sprites/frost.png";
		public static final String SPT	= "sprites/specter.png";
		public static final String AST	= "sprites/astesia.png";
		public static final String MDR	= "sprites/mudrock_skin.png";
		public static final String SCH	= "sprites/schwarz_skin.png";
		public static final String ARCT	= "sprites/archetto.png";
		public static final String TMM	= "sprites/tomimi_skin.png";
		public static final String FRK	= "sprites/franka.png";
		public static final String WED	= "sprites/weedy_skin.png";
		public static final String GLD	= "sprites/gladiia.png";
	}
}