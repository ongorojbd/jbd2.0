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

package com.shatteredpixel.shatteredpixeldungeon.tiles;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.tendencylevel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.levels.ArenaBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdhouseBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdhouseRecoveryLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Dio2Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Dio2bossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DiobossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Emp2Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.EmporioLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HumanVillageBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HumanVillageBossLevel2;
import com.shatteredpixel.shatteredpixeldungeon.levels.JolyneBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastShopLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewLastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.ParallelBrawlLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PhantomLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.ShipbossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.TempleLastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.TendencyEventLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.TendencyTreasureLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VeiledSanctumLevel;
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

	public TerrainFeaturesTilemap(SparseArray<Plant> plants, SparseArray<Trap> traps) {
		super(Assets.Environment.TERRAIN_FEATURES);

		this.plants = plants;
		this.traps = traps;

		if (Dungeon.level != null) {
			map(Dungeon.level.map, Dungeon.level.width());
		}

		instance = this;
	}

	protected int getTileVisual(int pos, int tile, boolean flat){
		if (traps != null && traps.get(pos) != null){
			Trap trap = traps.get(pos);
			if (!trap.visible)
				return -1;
			else
				return (trap.active ? trap.color : Trap.BLACK) + (trap.shape * 16);
		}

		if (plants != null && plants.get(pos) != null){
			return plants.get(pos).image + 7*16;
		}

		int stage = (Dungeon.depth-1)/5;

		if (Dungeon.depth == 21 && Dungeon.level instanceof LastShopLevel) stage--;

		if (Dungeon.level instanceof Dio2Level || Dungeon.level instanceof Dio2bossLevel || tendencylevel) stage = 0; // 전투조류
		stage = Math.min(stage, 4);

		//these levels each draw their own complete decorative terrain art in their own custom
		// tileset (tilesTex()), so the shared depth-region overlay below is skipped for them -
		// otherwise whichever vanilla region their depth happens to fall into gets drawn on top.
		boolean ownDecorAssets = Dungeon.level instanceof VeiledSanctumLevel
				|| Dungeon.level instanceof ColdhouseBossLevel
				|| Dungeon.level instanceof ColdhouseRecoveryLevel
				|| Dungeon.level instanceof ShipbossLevel
				|| Dungeon.level instanceof DiobossLevel
				|| Dungeon.level instanceof Dio2bossLevel
				|| Dungeon.level instanceof LabsLevel
				|| Dungeon.level instanceof LabsBossLevel
				|| Dungeon.level instanceof ArenaBossLevel
				|| Dungeon.level instanceof ParallelBrawlLevel
				|| Dungeon.level instanceof HumanVillageBossLevel
				|| Dungeon.level instanceof HumanVillageBossLevel2
				|| Dungeon.level instanceof NewLastLevel
				|| Dungeon.level instanceof PhantomLevel
				|| Dungeon.level instanceof TempleLastLevel
				|| Dungeon.level instanceof EmporioLevel
				|| Dungeon.level instanceof Emp2Level
				|| Dungeon.level instanceof JolyneBossLevel
				|| Dungeon.level instanceof TendencyEventLevel
				|| Dungeon.level instanceof TendencyTreasureLevel;

		if (tile == Terrain.HIGH_GRASS){
			if (ownDecorAssets) return -1;
			if (DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.RAISED_HIGH_GRASS, pos) == DungeonTileSheet.RAISED_HIGH_GRASS_ALT){
				return 128 + 16*stage + 1;
			} else {
				return 128 + 16*stage;
			}
		} else if (tile == Terrain.FURROWED_GRASS){
			if (ownDecorAssets) return -1;
			if (DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.RAISED_FURROWED_GRASS, pos) == DungeonTileSheet.RAISED_FURROWED_ALT){
				return 130 + 16*stage + 1;
			} else {
				return 130 + 16*stage;
			}
		} else if (tile == Terrain.GRASS) {
			if (ownDecorAssets) return -1;
			if (DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.GRASS, pos) == DungeonTileSheet.GRASS_ALT){
				return 132 + 16*stage + 1;
			} else {
				return 132 + 16*stage;
			}
		} else if (tile == Terrain.BARRICADE) {
			if (ownDecorAssets) return -1;
			return 134 + 16*stage;

		} else if (tile == Terrain.ALCHEMY) {
			if (ownDecorAssets) return -1;
			return 135 + 16*stage;

		} else if (tile == Terrain.STATUE || tile == Terrain.STATUE_SP) {
			if (ownDecorAssets) return -1;
			return 136 + 16*stage;

		} else if (tile == Terrain.REGION_DECO) {
			if (ownDecorAssets) return -1;
			return 137 + 16 * stage;

		} else if (tile == Terrain.REGION_DECO_ALT) {
			if (ownDecorAssets) return -1;
			return 138 + 16 * stage;

		} else if (tile == Terrain.EMBERS) {
			if (DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.EMBERS, pos) == DungeonTileSheet.EMBERS_ALT){
				return 208 + 1;
			} else {
				return 208;
			}
		} else if (tile == Terrain.MINE_CRYSTAL){
			int vis = DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.RAISED_MINE_CRYSTAL_BLUE_1, pos);
			if (vis == DungeonTileSheet.RAISED_MINE_CRYSTAL_RED_2){
				return 210 + 5;
			} else if (vis == DungeonTileSheet.RAISED_MINE_CRYSTAL_RED_1){
				return 210 + 4;
			} else if (vis == DungeonTileSheet.RAISED_MINE_CRYSTAL_GREEN_2){
				return 210 + 3;
			} else if (vis == DungeonTileSheet.RAISED_MINE_CRYSTAL_GREEN_1){
				return 210 + 2;
			} else if (vis == DungeonTileSheet.RAISED_MINE_CRYSTAL_BLUE_2){
				return 210 + 1;
			} else {
				return 210 + 0;
			}
		} else if (tile == Terrain.MINE_BOULDER){
			int vis = DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.RAISED_MINE_BOULDER, pos);
			if (vis == DungeonTileSheet.RAISED_MINE_BOULDER_ALT_2){
				return 216 + 2;
			} else if (vis == DungeonTileSheet.RAISED_MINE_BOULDER_ALT){
				return 216 + 1;
			} else {
				return 216;
			}
		}

		return -1;
	}

	public static Image getTrapVisual( Trap trap ){
		if (instance == null) instance = new TerrainFeaturesTilemap(null, null);

		RectF uv = instance.tileset.get((trap.active ? trap.color : Trap.BLACK) + (trap.shape * 16));
		if (uv == null) return null;

		// TextureCache.get()로 항상 현재 로드된 텍스처를 사용 (오래된 instance의 texture 재사용 방지)
		Image img = new Image( com.watabou.gltextures.TextureCache.get(Assets.Environment.TERRAIN_FEATURES) );
		img.frame(uv);
		return img;
	}

	public static Image getPlantVisual( Plant plant ){
		if (instance == null) instance = new TerrainFeaturesTilemap(null, null);

		RectF uv = instance.tileset.get(plant.image + 7*16);
		if (uv == null) return null;

		Image img = new Image( instance.texture );
		img.frame(uv);
		return img;
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

}
