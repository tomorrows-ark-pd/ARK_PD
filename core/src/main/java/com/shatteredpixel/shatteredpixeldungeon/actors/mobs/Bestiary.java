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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Bestiary {
	
	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = standardMobRotation( depth );
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}
	
	//returns a rotation of standard mobs, unshuffled.
	private static ArrayList<Class<? extends Mob>> standardMobRotation( int depth ){
		switch(depth){
			
			// Sewers
			case 1: default:
				//3x rat, 1x snake
				return new ArrayList<>(Arrays.asList(
						Slug.class, Slug.class, Slug.class,
						Snake.class));
			case 2:
				//2x rat, 1x snake, 2x gnoll
				return new ArrayList<>(Arrays.asList(Slug.class, Slug.class,
						Snake.class,
						Gnoll.class, Gnoll.class));
			case 3:
				//1x rat, 1x snake, 3x gnoll, 1x swarm, 1x crab
				return new ArrayList<>(Arrays.asList(Slug.class,
						Snake.class,
						Gnoll.class, Gnoll.class, Gnoll.class,
						Swarm.class,
						Hound.class));
			case 4: case 5:
				//1x gnoll, 1x swarm, 2x crab, 2x slime
				return new ArrayList<>(Arrays.asList(Gnoll.class,
						Swarm.class,
						Hound.class, Hound.class,
						Slime.class, Slime.class));
				
			// Prison
			case 6:
				//1x skeleton, 1x thief, 1x swarm, 2x AirborneSoldiers
				return new ArrayList<>(Arrays.asList(Skeleton.class,
						AirborneSoldier.class,AirborneSoldier.class,
						Thief.class,
						Swarm.class));
			case 7:
				//2x skeleton, 1x thief, 1x DM-100, 1x guard, 2x AirborneSoldiers
				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class,
						Thief.class,
						DM100.class,
						AirborneSoldier.class, AirborneSoldier.class,
						Guard.class));
			case 8:
				//1x skeleton, 1x thief, 2x DM-100, 2x guard, 1x necromancer, 2x AirborneSoldiers
				return new ArrayList<>(Arrays.asList(Skeleton.class,
						Thief.class,
						DM100.class, DM100.class,
						Guard.class, Guard.class,
						AirborneSoldier.class, AirborneSoldier.class,
						Necromancer.class));
			case 9: case 10:
				//1x skeleton, 2x DM-100, 2x guard, 2x necromancer, 1x AirborneSoldiers
				return new ArrayList<>(Arrays.asList(Skeleton.class,
						DM100.class, DM100.class,
						Guard.class, Guard.class,
						AirborneSoldier.class,
						Necromancer.class, Necromancer.class));
				
			// Caves
			case 11:
				//3x bat, 1x brute, 1x shaman
				return new ArrayList<>(Arrays.asList(
						Bat.class, Bat.class, Bat.class,
						Brute.class,
						Shaman.random()));
			case 12:
				//2x bat, 2x brute, 1x shaman, 1x spinner
				return new ArrayList<>(Arrays.asList(
						Bat.class, Bat.class,
						Brute.class, Brute.class,
						Shaman.random(),
						ExplodSlug_N.class));
			case 13:
				//1x bat, 2x brute, 2x shaman, 2x spinner, 1x DM-200
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						Brute.class, Brute.class,
						Shaman.random(), Shaman.random(),
						ExplodSlug_N.class, ExplodSlug_N.class,
						MudrockZealot.class,
						DM200.class));
			case 14: case 15:
				//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						Brute.class,
						Shaman.random(), Shaman.random(),
						ExplodSlug_N.class, ExplodSlug_N.class,
						MudrockZealot.class, MudrockZealot.class,
						DM200.class, DM200.class));
				
			// City
			case 16:
				//2x ghoul, 2x elemental, 1x warlock
				return new ArrayList<>(Arrays.asList(
						Ghoul.class, Ghoul.class,
						Elemental.random(), Elemental.random(),
						Warlock.class));
			case 17:
				//1x ghoul, 2x elemental, 1x warlock, 1x monk
				return new ArrayList<>(Arrays.asList(
						Ghoul.class,
						Elemental.random(), Elemental.random(),
						Warlock.class,
						Monk.class));
			case 18:
				//1x ghoul, 1x elemental, 2x warlock, 2x monk, 1x golem
				return new ArrayList<>(Arrays.asList(
						Ghoul.class,
						Elemental.random(),
						Warlock.class, Warlock.class,
						Monk.class, Monk.class,
						Golem.class));
			case 19: case 20:
				//1x elemental, 2x warlock, 2x monk, 3x golem
				return new ArrayList<>(Arrays.asList(
						Elemental.random(),
						Warlock.class, Warlock.class,
						Monk.class, Monk.class,
						Golem.class, Golem.class, Golem.class));
				
			// Halls
			case 21:
				//2x succubus, 1x evil eye
				return new ArrayList<>(Arrays.asList(
						Succubus.class, Succubus.class,
						Eye.class));
			case 22:
				//1x succubus, 1x evil eye
				return new ArrayList<>(Arrays.asList(
						Succubus.class,
						Striker.class,
						Eye.class));
			case 23:
				//1x succubus, 2x evil eye, 1x scorpio
				return new ArrayList<>(Arrays.asList(
						Succubus.class,
						Striker.class, Striker.class,
						Eye.class, Eye.class,
						Scorpio.class));
			case 24: case 25: case 26:
				//1x succubus, 2x evil eye, 3x scorpio
				return new ArrayList<>(Arrays.asList(
						Succubus.class,
						Striker.class, Striker.class,
						Eye.class, Eye.class,
						Scorpio.class, Scorpio.class, Scorpio.class));

			case 31:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhWarrior.class, TiacauhWarrior.class, TiacauhWarrior.class,
							TiacauhFanatic.class, TiacauhFanatic.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							SeaRunner.class, SeaRunner.class,
							FloatingSeaDrifter.class, FloatingSeaDrifter.class));
				}
				//Siesta part1
				return new ArrayList<>(Arrays.asList(
						Infantry.class, Infantry.class, Infantry.class,
						Ergate.class));
			case 32:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhWarrior.class, TiacauhWarrior.class,
							TiacauhFanatic.class, TiacauhFanatic.class,
							TiacauhLancer.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							SeaRunner.class, SeaRunner.class,
							FloatingSeaDrifter.class, FloatingSeaDrifter.class,
							SeaReaper.class));
				}
				return new ArrayList<>(Arrays.asList(
						Infantry.class, Infantry.class,
						Ergate.class, Ergate.class,
						Piersailor.class, Piersailor.class,
						Sniper.class));
			case 33:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhWarrior.class, TiacauhWarrior.class,
							TiacauhFanatic.class, TiacauhFanatic.class,
							TiacauhLancer.class, TiacauhLancer.class,
							TiacauhAddict.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							SeaRunner.class,
							FloatingSeaDrifter.class,
							SeaReaper.class,
							SeaCapsule.class));
				}
				return new ArrayList<>(Arrays.asList(
						Infantry.class, Infantry.class,
						Ergate.class, Ergate.class,
						Sniper.class,
						Piersailor.class, Piersailor.class,
						Agent.class));
			case 34:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhWarrior.class,
							TiacauhFanatic.class, TiacauhFanatic.class,
							TiacauhLancer.class, TiacauhLancer.class,
							TiacauhAddict.class, TiacauhAddict.class, TiacauhAddict.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							FloatingSeaDrifter.class,
							SeaReaper.class, SeaReaper.class,
							SeaCapsule.class, SeaCapsule.class));
				}
				return new ArrayList<>(Arrays.asList(
						Ergate.class,
						Sniper.class, Sniper.class,
						Piersailor.class, Piersailor.class, Piersailor.class,
						Agent.class, Agent.class, Agent.class));
			case 36:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhAddict.class,
							TiacauhRipper.class, TiacauhRipper.class, TiacauhRipper.class,
							TiacauhShredder.class, TiacauhShredder.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							SeaReaper.class, SeaReaper.class,
							SeaCapsule.class,
							Sea_Octo.class, Sea_Octo.class));}
				return new ArrayList<>(Arrays.asList(
						LavaSlug.class, LavaSlug.class,
						MetalCrab.class, MetalCrab.class,
						Rockbreaker.class,
						ExplodeSlug_A.class
				));
			case 37:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhAddict.class,
							TiacauhRipper.class, TiacauhRipper.class,
							TiacauhShredder.class, TiacauhShredder.class,
							TiacauhRitualist.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							SeaReaper.class, SeaReaper.class,
							Sea_Octo.class, Sea_Octo.class,
							SeaLeef.class));}
				return new ArrayList<>(Arrays.asList(
						LavaSlug.class, LavaSlug.class,
						MetalCrab.class, MetalCrab.class,
						Rockbreaker.class, Rockbreaker.class,
						ExplodeSlug_A.class
				));
			case 38:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhAddict.class, TiacauhAddict.class,
							TiacauhRipper.class,
							TiacauhShredder.class, TiacauhShredder.class,
							TiacauhRitualist.class, TiacauhRitualist.class,
							TiacauhBrave.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							SeaReaper.class,SeaReaper.class,
							Sea_Octo.class, Sea_Octo.class,
							SeaLeef.class, SeaLeef.class,
							NetherseaBrandguider.class));}
				return new ArrayList<>(Arrays.asList(
						LavaSlug.class, LavaSlug.class,
						MetalCrab.class,
						Rockbreaker.class, Rockbreaker.class,
						ExplodeSlug_A.class, ExplodeSlug_A.class,
						AcidSlug_A.class
				));
			case 39:
				if (Dungeon.extrastage_Gavial) {
					return new ArrayList<>(Arrays.asList(
							TiacauhAddict.class,
							TiacauhShredder.class,
							TiacauhRitualist.class, TiacauhRitualist.class, TiacauhRitualist.class,
							TiacauhBrave.class, TiacauhBrave.class));
				}
				else if (Dungeon.extrastage_Sea) {
					return new ArrayList<>(Arrays.asList(
							Sea_Octo.class, Sea_Octo.class,
							SeaLeef.class, SeaLeef.class,
							NetherseaBrandguider.class, NetherseaBrandguider.class));}
				return new ArrayList<>(Arrays.asList(
						LavaSlug.class,
						MetalCrab.class, MetalCrab.class,
						Rockbreaker.class, Rockbreaker.class,
						ExplodeSlug_A.class, ExplodeSlug_A.class,
						AcidSlug_A.class, AcidSlug_A.class
				));

		}
		
	}
	
	//has a chance to add a rarely spawned mobs to the rotation
	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){
		
		switch (depth){
			
			// Sewers
			default:
				return;
			case 4:
				if (Random.Float() < 0.025f) rotation.add(Thief.class);
				return;
				
			// Prison
			case 9:
				if (Random.Float() < 0.025f) rotation.add(Bat.class);
				return;
				
			// Caves
			case 14:
				if (Random.Float() < 0.025f) rotation.add(Ghoul.class);
				return;
				
			// City
			case 19:
				if (Random.Float() < 0.025f) rotation.add(Succubus.class);
				return;
		}
	}
	
	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
		for (int i = 0; i < rotation.size(); i++){
			Class<? extends Mob> cl = rotation.get(i);
			if (Random.Int( 33 ) == 0) {
				if (cl == Slug.class) {
					cl = Albino.class;
				} else if (cl == Slime.class) {
					cl = CausticSlime.class;
				} else if (cl == Thief.class) {
					cl = Bandit.class;
				} else if (cl == Brute.class) {
					cl = ArmoredBrute.class;
				} else if (cl == DM200.class) {
					cl = DM201.class;
				} else if (cl == Monk.class) {
					cl = Senior.class;
				} else if (cl == Eye.class) {
					cl = EmpireDrone.class;
				}else if (cl == Succubus.class) {
					cl = Raider.class;
				}
				else if (cl == Striker.class) {
					cl = StrikerElite.class;
				}
				else if (cl == Scorpio.class) {
					cl = Acidic.class;
				}
				else if (cl == Piersailor.class) {
					cl = HeavyBoat.class;
				}
				else if (cl == Sniper.class) {
					cl = WaveCaster.class;
				}
				else if (cl == MetalCrab.class) {
					cl = MutantSpider.class;
				}
				else if (cl == ExplodeSlug_A.class) {
					cl = Originiutant.class;
				}
				else if (cl == TiacauhLancer.class) {
					cl = TiacauhSniper.class;
				}
				else if (cl == TiacauhRitualist.class) {
					cl = TiacauhShaman.class;
				}
				rotation.set(i, cl);
			}
			else if (Random.Int( 1000 ) == 0) {
				cl = Crownslayer_shadow.class;
				rotation.set(i, cl);
			}
		}
	}
}
