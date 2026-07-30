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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Foliage;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ceylon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GreenCat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MiniShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.LostBackpack;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AceSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CannotSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CeylonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GopnikSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreenCatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Guard_operSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Texas_shopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Notes {
	
	public static abstract class Record implements Comparable<Record>, Bundlable {
		
		protected int depth;

		public int depth(){
			return depth;
		}

		public Image icon() { return Icons.DEPTH.get(); }

		public Visual secondIcon() { return null; }

		public int quantity() { return 1; }

		//short label; defaults to desc() so subclasses can override incrementally
		public String title() { return desc(); }

		//sort order within a floor
		protected abstract int order();

		public abstract String desc();

		@Override
		public abstract boolean equals(Object obj);
		
		@Override
		public int compareTo( Record another ) {
			return another.depth() - depth();
		}
		
		private static final String DEPTH	= "depth";
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			depth = bundle.getInt( DEPTH );
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			bundle.put( DEPTH, depth );
		}
	}
	
	public enum Landmark {
		WELL_OF_HEALTH,
		WELL_OF_AWARENESS,
		WELL_OF_TRANSMUTATION,
		WELL_OF_ADVANCEGUARD,
		ALCHEMY,
		GARDEN,
		STATUE,
		SHOP,
		MINI_SHOP,
		
		GHOST,
		WANDMAKER,
		TROLL,
		IMP,
		GREENCAT,
		CEYLON,

		LOST_PACK;
		
		public String desc() {
			return Messages.get(this, name());
		}
	}
	
	public static class LandmarkRecord extends Record {
		
		protected Landmark landmark;
		
		public LandmarkRecord() {}
		
		public LandmarkRecord(Landmark landmark, int depth ) {
			this.landmark = landmark;
			this.depth = depth;
		}

		@Override
		public Image icon() {
			switch (landmark) {
				default:
					return Icons.DEPTH.get();

				//water features -> a dewdrop stands in for the well
				case WELL_OF_HEALTH:
				case WELL_OF_AWARENESS:
				case WELL_OF_TRANSMUTATION:
				case WELL_OF_ADVANCEGUARD:
					return new ItemSprite(ItemSpriteSheet.DEWDROP);
				case ALCHEMY:
					return new ItemSprite(ItemSpriteSheet.ALCH_PAGE);
				case GARDEN:
					return new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS);
				case LOST_PACK:
					return Icons.BACKPACK.get();

				//NPC landmarks -> the NPC's own sprite
				case SHOP:
					return new Image(new ShopkeeperSprite());
				case MINI_SHOP:
					return new Image(new Texas_shopkeeperSprite());
				case STATUE:
					return new Image(new GopnikSprite());
				case GHOST:
					return new Image(new Guard_operSprite());
				case WANDMAKER:
					return new Image(new AceSprite());
				case TROLL:
					return new Image(new BlacksmithSprite());
				case IMP:
					return new Image(new CannotSprite());
				case GREENCAT:
					return new Image(new GreenCatSprite());
				case CEYLON:
					return new Image(new CeylonSprite());
			}
		}

		@Override
		protected int order() {
			return landmark.ordinal();
		}

		@Override
		public String title() {
			return landmark.desc();
		}

		//falls back to the landmark's name if the linked entity has no desc of its own
		@Override
		public String desc() {
			switch (landmark) {
				case WELL_OF_HEALTH:        return Messages.get(WaterOfHealth.class, "desc");
				case WELL_OF_AWARENESS:     return Messages.get(WaterOfAwareness.class, "desc");
				case WELL_OF_TRANSMUTATION: return Messages.get(WaterOfTransmutation.class, "desc");
				case WELL_OF_ADVANCEGUARD:  return Messages.get(WaterOfAdvanceguard.class, "desc");
				case ALCHEMY:                return Messages.get(Level.class, "alchemy_desc");
				case GARDEN:                 return Messages.get(Foliage.class, "desc");
				case STATUE:                 return Messages.get(Statue.class, "desc");
				case SHOP:                   return Messages.get(Shopkeeper.class, "desc");
				case MINI_SHOP:              return Messages.get(MiniShopkeeper.class, "desc");
				case GHOST:                  return Messages.get(Ghost.class, "desc");
				case WANDMAKER:              return Messages.get(Wandmaker.class, "desc");
				case TROLL:                  return Messages.get(Blacksmith.class, "desc");
				case IMP:                    return Messages.get(Imp.class, "desc");
				case GREENCAT:               return Messages.get(GreenCat.class, "desc");
				case CEYLON:                 return Messages.get(Ceylon.class, "desc");
				case LOST_PACK:              return Messages.get(LostBackpack.class, "desc");
				default:                     return landmark.desc();
			}
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof LandmarkRecord)
					&& landmark == ((LandmarkRecord) obj).landmark
					&& depth() == ((LandmarkRecord) obj).depth();
		}
		
		private static final String LANDMARK	= "landmark";
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			landmark = Landmark.valueOf(bundle.getString(LANDMARK));
		}
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( LANDMARK, landmark.toString() );
		}
	}
	
	public static class KeyRecord extends Record {
		
		protected Key key;
		
		public KeyRecord() {}
		
		public KeyRecord( Key key ){
			this.key = key;
		}
		
		@Override
		public int depth() {
			return key.depth;
		}

		@Override
		public Image icon() {
			return new ItemSprite(key);
		}

		@Override
		public Visual secondIcon() {
			if (quantity() > 1) {
				BitmapText text = new BitmapText(Integer.toString(quantity()), PixelScene.pixelFont);
				text.measure();
				return text;
			} else {
				return null;
			}
		}

		@Override
		public String title() {
			return key.title();
		}

		@Override
		public String desc() {
			return key.desc();
		}

		public Class<? extends Key> type(){
			return key.getClass();
		}

		@Override
		protected int order() {
			return 1000 + Generator.Category.order(key);
		}

		@Override
		public int quantity(){
			return key.quantity();
		}
		
		public void quantity(int num){
			key.quantity(num);
		}
		
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof KeyRecord)
					&& key.isSimilar(((KeyRecord) obj).key);
		}
		
		private static final String KEY	= "key";
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			key = (Key) bundle.get(KEY);
		}
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( KEY, key );
		}
	}
	
	private static ArrayList<Record> records;
	
	public static void reset() {
		records = new ArrayList<>();
	}
	
	private static final String RECORDS	= "records";
	
	public static void storeInBundle( Bundle bundle ) {
		bundle.put( RECORDS, records );
	}
	
	public static void restoreFromBundle( Bundle bundle ) {
		records = new ArrayList<>();
		for (Bundlable rec : bundle.getCollection( RECORDS ) ) {
			records.add( (Record) rec );
		}
	}
	
	public static boolean add( Landmark landmark ) {
		return add( landmark, Dungeon.depth );
	}

	public static boolean add( Landmark landmark, int depth ) {
		LandmarkRecord l = new LandmarkRecord( landmark, depth );
		if (!records.contains(l)) {
			boolean result = records.add(l);
			Collections.sort(records, comparator);
			return result;
		}
		return false;
	}

	public static boolean contains( Landmark landmark ){
		return contains( landmark, Dungeon.depth );
	}

	public static boolean contains( Landmark landmark, int depth ){
		return records.contains( new LandmarkRecord( landmark, depth ) );
	}

	public static boolean remove( Landmark landmark ) {
		return remove( landmark, Dungeon.depth );
	}

	public static boolean remove( Landmark landmark, int depth ) {
		return records.remove( new LandmarkRecord(landmark, depth) );
	}

	public static boolean add( Key key ){
		KeyRecord k = new KeyRecord(key);
		if (!records.contains(k)){
			boolean result = records.add(k);
			Collections.sort(records, comparator);
			return result;
		} else {
			k = (KeyRecord) records.get(records.indexOf(k));
			k.quantity(k.quantity() + key.quantity());
			return true;
		}
	}
	
	public static boolean remove( Key key ){
		KeyRecord k = new KeyRecord( key );
		if (records.contains(k)){
			k = (KeyRecord) records.get(records.indexOf(k));
			k.quantity(k.quantity() - key.quantity());
			if (k.quantity() <= 0){
				records.remove(k);
			}
			return true;
		}
		return false;
	}
	
	public static int keyCount( Key key ){
		KeyRecord k = new KeyRecord( key );
		if (records.contains(k)){
			k = (KeyRecord) records.get(records.indexOf(k));
			return k.quantity();
		} else {
			return 0;
		}
	}
	
	public static ArrayList<Record> getRecords(){
		return getRecords(Record.class);
	}
	
	public static <T extends Record> ArrayList<T> getRecords( Class<T> recordType ){
		ArrayList<T> filtered = new ArrayList<>();
		for (Record rec : records){
			if (recordType.isInstance(rec)){
				filtered.add((T)rec);
			}
		}
		return filtered;
	}

	public static ArrayList<Record> getRecords( int depth ){
		ArrayList<Record> filtered = new ArrayList<>();
		for (Record rec : records){
			if (rec.depth() == depth){
				filtered.add(rec);
			}
		}
		Collections.sort(filtered, comparator);
		return filtered;
	}

	public static void remove( Record rec ){
		records.remove(rec);
	}

	private static final Comparator<Record> comparator = new Comparator<Record>() {
		@Override
		public int compare(Record r1, Record r2) {
			return r1.order() - r2.order();
		}
	};

}
