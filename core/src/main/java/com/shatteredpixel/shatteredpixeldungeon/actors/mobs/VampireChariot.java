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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ChariotSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.GameMath;

import java.util.ArrayList;

public class VampireChariot extends Mob {

	private ArrayList<Integer> chargePath;
	private int chargeWindup = 0;
	private int chargeCooldown = Random.NormalIntRange(3, 5);

	{
		spriteClass = ChariotSprite.class;

		HP = HT = 200;
		defenseSkill = 30;

		EXP = 20;
		maxLvl = 27;

		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);
	}

	@Override
	protected boolean act() {
		// resolve charge after telegraph
		if (chargeWindup > 0 && chargePath != null && !chargePath.isEmpty()) {
			sprite.emitter().burst(Speck.factory(Speck.JET), 12);
			int oldPos = pos;
			// choose furthest safe destination from end of path
			int dest = pos;
			for (int i = chargePath.size()-1; i >= 0; i--) {
				int c = chargePath.get(i);
				if (!Dungeon.level.solid[c] && Actor.findChar(c) == null) {
					dest = c;
					break;
				}
			}
			// damage hero if they are anywhere on the path
			for (int c : chargePath) {
				if (Dungeon.hero.pos == c) {
					int dmg = Random.NormalIntRange(35, 50);
					Dungeon.hero.damage(dmg, this);
					if (!Dungeon.hero.isAlive()) {
						Dungeon.fail(getClass());
					}
					break;
				}
			}
			// move the chariot to the chosen destination
			pos = dest;
			Actor.add(new Pushing(this, oldPos, pos));
			spend(TICK);
			chargePath = null;
			chargeWindup = 0;
			chargeCooldown = Random.NormalIntRange(4, 6);
			return true;
		}

		// try to start a charge: telegraph straight line then rush next turn
		if (enemy != null && !rooted && chargeCooldown <= 0 && state != SLEEPING) {
			Ballistica path = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
			if (path.dist > 0) {
				// extend one tile behind the hero if LOS and map allows
				int endIndex = path.dist;
				if (path.collisionPos != null && path.collisionPos == enemy.pos) {
					endIndex = Math.min(path.dist + 1, path.path.size()-1);
				}
				// prepare path excluding current cell
				chargePath = new ArrayList<>(path.subPath(1, endIndex));
				for (int c : chargePath) {
					// show telegraph
					if (sprite != null && (sprite.visible || Dungeon.level.heroFOV[c])) {
						sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
					}
				}
				// spend based on hero speed so slower heroes aren't punished as much
				sprite.showStatus(CharSprite.WARNING, Messages.get(this, "horse"));
				spend(GameMath.gate(TICK, (int)Math.ceil(Dungeon.hero.cooldown()), 3*TICK));
				Dungeon.hero.interrupt();
				chargeWindup = 1;
				Sample.INSTANCE.play(Assets.Sounds.HORSE);
				return true;
			}
		}

		if (chargeCooldown > 0) chargeCooldown--;
		return super.act();
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 35, 50 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 36;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 20);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		int dealt = super.attackProc(enemy, damage);
		int heal = Math.max(1, Math.round(dealt * 0.25f));
		if (heal > 0 && HP < HT) {
			HP = Math.min(HT, HP + heal);
			if (sprite != null) sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
		}
		return dealt;
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		if (Random.Int( 3 ) == 0) {
			Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
		}

		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
		}

	}

}


