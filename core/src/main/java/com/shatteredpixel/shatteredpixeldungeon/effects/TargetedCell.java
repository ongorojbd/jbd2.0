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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

public class TargetedCell extends Image implements Bundlable {

	public int pos;
	public float time;

	private float alpha;

	//legacy mode: a one-shot telegraph that fades on its own, not tied to a fire time.
	//kept so the many mobs still using "new TargetedCell(pos, color)" work unchanged.
	private boolean legacy;

	public static SparseArray<TargetedCell> cells = new SparseArray<>();

	//no-arg constructor is required by the recycle pool and by bundle restoration
	public TargetedCell(){
		super();
	}

	//pre-4.0 API: immediate self-fading telegraph with an arbitrary colour, behaviour unchanged.
	//kept so the many mobs still calling "new TargetedCell(pos, color)" keep working as before.
	public TargetedCell( int pos, int color ) {
		super(Icons.get(Icons.TARGET));
		hardlight(color);

		origin.set( width/2f );

		point( DungeonTilemap.tileToWorld( pos ) );

		alpha = 1f;
		legacy = true;
	}

	public void reset( int pos, float time ){
		copy(Icons.get(Icons.TARGET));
		origin.set( width/2f );
		camera = null;

		this.pos = pos;
		point( DungeonTilemap.tileToWorld( pos ) );

		hardlight(0xFF00FF);

		alpha = 1f;
		this.time = time;
		legacy = false;

		alpha(1f);
		scale.set(1f);

		synchronized (cells) {
			cells.put(pos, this);
		}
	}

	@Override
	public void update() {
		if (legacy) {
			if ((alpha -= Game.elapsed/2f) > 0) {
				alpha( alpha );
				scale.set( alpha );
			} else {
				killAndErase();
			}
			return;
		}

		alpha -= Game.elapsed;
		if (time >= Actor.now()){
			alpha = Math.max(alpha, 0.6f);
		}
		if (alpha > 0) {
			alpha( alpha );
			scale.set( (float)Math.pow(alpha, 0.33f) );
		} else {
			time = 0;
			synchronized (cells) {
				cells.remove(pos);
			}
			killAndErase();
		}
	}

	public static void fixTime(float min){
		synchronized (cells){
			for (TargetedCell c : cells.valueList()){
				c.time -= min;
			}
		}
	}

	private static final String POS = "pos";
	private static final String COLOR = "color";
	private static final String TIME = "time";

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(POS, pos);
		bundle.put(TIME, time);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		pos = bundle.getInt(POS);
		time = bundle.getInt(TIME);
	}

}
