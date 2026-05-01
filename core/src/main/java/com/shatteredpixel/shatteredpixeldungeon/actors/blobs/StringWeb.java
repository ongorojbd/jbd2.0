/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WebParticle;
import com.watabou.noosa.particles.Emitter;

public class StringWeb extends Web {

	@Override
	public void use(BlobEmitter emitter) {
		this.emitter = emitter;
		emitter.pour(SkyWebParticle.FACTORY, 0.25f);
	}

	public static void affectChar(Char ch) {
		if (ch.alignment != Char.Alignment.ENEMY) {
			return;
		}

		int points = Dungeon.hero.pointsInTalent(Talent.JOLYNE_NEW3);
		Buff.prolong(ch, Cripple.class, 5f);
		if (points >= 2) {
			Buff.prolong(ch, Roots.class, 5f);
		}
		if (points >= 3) {
            Buff.prolong(ch, Hex.class, 5f);
		}
	}

	public static class SkyWebParticle extends WebParticle {

		public static final Emitter.Factory FACTORY = new Emitter.Factory() {
			@Override
			public void emit(Emitter emitter, int index, float x, float y) {
				for (int i = 0; i < 3; i++) {
					((SkyWebParticle) emitter.recycle(SkyWebParticle.class)).reset(x, y);
				}
			}
		};

		public SkyWebParticle() {
			super();
			color(0x66FFFF);
		}
	}
}
