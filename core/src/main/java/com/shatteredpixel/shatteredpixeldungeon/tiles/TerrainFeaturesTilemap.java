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

package com.shatteredpixel.shatteredpixeldungeon.tiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastShopLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Platform;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.SeaTerror;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;
import com.watabou.utils.SparseArray;

public class TerrainFeaturesTilemap extends DungeonTilemap {

	private static TerrainFeaturesTilemap instance;

	private SparseArray<Plant> plants;
	private SparseArray<Trap> traps;
    private SparseArray<Platform> platforms;
    private SparseArray<SeaTerror> seaTerrors;

	public TerrainFeaturesTilemap(SparseArray<Plant> plants, SparseArray<Trap> traps, SparseArray<Platform> platforms, SparseArray<SeaTerror> seaTerrors) {
		super(Assets.Environment.TERRAIN_FEATURES);

		this.plants = plants;
		this.traps = traps;
        this.platforms = platforms;
        this.seaTerrors = seaTerrors;

        map( Dungeon.level.map, Dungeon.level.width() );

		instance = this;
	}

	protected int getTileVisual(int pos, int tile, boolean flat){
		if (traps.get(pos) != null){
			Trap trap = traps.get(pos);
			if (!trap.visible)
				return -1;
			else
				return (trap.active ? trap.color : Trap.BLACK) + (trap.shape * 16);
		}

		if (plants.get(pos) != null){
			return plants.get(pos).image + 7*16;
		}

        if (platforms.get(pos) != null){
            return platforms.get(pos).image + 8*16;
        }

        if (seaTerrors.get(pos) != null) {
            int imgPos = Dungeon.extrastage_Sea && Dungeon.depth >= 40 ? 6 : 3;
            return imgPos + (16*8) + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
        }

		int stage = (Dungeon.depth-1)/5;
		if (stage == 6) stage = 3;
		else stage = Math.min(stage,4);
		if (Dungeon.depth == 21 && Dungeon.level instanceof LastShopLevel) stage--;

		if ((Dungeon.depth >= 27 && Dungeon.depth <= 30) || Dungeon.extrastage_Gavial) {
            stage = 3; // 가비알 / 로도스 추가처리
        } else if (Dungeon.extrastage_Sea && Dungeon.depth >= 31) {
            stage = 0;
        }

		if (tile == Terrain.HIGH_GRASS){
			return 9 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.FURROWED_GRASS){
			return 11 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.GRASS) {
			return 13 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.EMBERS) {
			return 9 * (16*5) + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		}
        else if (tile == Terrain.SEA_TERROR) {
            return 3 + (16*8) + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
        }

		return -1;
	}

	public static Image tile(int pos, int tile ) {
		RectF uv = instance.tileset.get( instance.getTileVisual( pos, tile, true ) );
		if (uv == null) return null;
		
		Image img = new Image( instance.texture );
		img.frame(uv);
		return img;
	}

	public void growPlant( final int pos ){
		final Image plant = tile( pos, map[pos] );
		if (plant == null) return;
		
		plant.origin.set( 8, 12 );
		plant.scale.set( 0 );
		plant.point( DungeonTilemap.tileToWorld( pos ) );

		parent.add( plant );

		parent.add( new ScaleTweener( plant, new PointF(1, 1), 0.2f ) {
			protected void onComplete() {
				plant.killAndErase();
				killAndErase();
				updateMapCell(pos);
			}
		} );
	}

    public void createPlatform( final int pos ) {
        final Image platform = tile ( pos, map[pos] );
        if (platform == null) return;

        platform.origin.set( 8, 12 );
        platform.scale.set( 0 );
        platform.point( DungeonTilemap.tileToWorld(pos));

        parent.add( platform );

        parent.add( new ScaleTweener( platform, new PointF(1, 1), 0.2f) {
            protected void onComplete() {
                platform.killAndErase();
                killAndErase();
                updateMapCell(pos);
            }
        } );
    }
}
