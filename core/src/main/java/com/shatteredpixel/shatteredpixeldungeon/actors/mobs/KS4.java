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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KS4Sprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * KS4 - 카즈의 정예병 (전차)
 * 매우 높은 체력과 방어력을 가진 중장갑 유닛
 * 돌진 공격과 충격파 능력을 가짐
 */
public class KS4 extends Mob {

	{
		spriteClass = KS4Sprite.class;

		HP = HT = 250;
		defenseSkill = 30;

		EXP = 20;
		maxLvl = 30;

		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);
	}

	// VampireChariot의 돌진 능력
	private ArrayList<Integer> chargePath;
	private int chargeWindup = 0;
	private int chargeCooldown = Random.NormalIntRange(3, 5);

	// 충격파 능력 (새로운 능력)
	private int shockwaveCooldown = Random.NormalIntRange(5, 8);
	private int shockwaveWindup = 0;

	private static final String CHARGE_PATH = "charge_path";
	private static final String CHARGE_WIND = "charge_wind";
	private static final String CHARGE_CD = "charge_cd";
	private static final String SHOCKWAVE_CD = "shockwave_cd";
	private static final String SHOCKWAVE_WIND = "shockwave_wind";

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 50, 60 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 40;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 20);
	}

	@Override
	protected boolean act() {
		// 돌진 해결 (VampireChariot의 돌진 능력)
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

		// 충격파 해결
		if (shockwaveWindup > 0) {
			shockwaveWindup--;
			if (shockwaveWindup == 0) {
				resolveShockwave();
				return true;
			} else {
				// 충격파 범위 표시
				for (int i : PathFinder.NEIGHBOURS8) {
					int cell = pos + i;
					if (Dungeon.level.insideMap(cell)) {
						sprite.parent.addToBack(new TargetedCell(cell, 0xFFFF00));
					}
				}
				spend(TICK);
				return true;
			}
		}

		// 돌진 시작 시도 (VampireChariot의 돌진 능력)
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

		// 충격파 시작 시도 (새로운 능력)
		if (enemy != null && shockwaveCooldown <= 0 && state != SLEEPING 
				&& Dungeon.level.distance(pos, enemy.pos) <= 2) {
			// 주변에 적이 있을 때 충격파 사용
			boolean enemyNearby = false;
			for (int i : PathFinder.NEIGHBOURS8) {
				int cell = pos + i;
				Char ch = Actor.findChar(cell);
				if (ch != null && ch.alignment != alignment) {
					enemyNearby = true;
					break;
				}
			}
			if (enemyNearby) {
				// 충격파 예고
				for (int i : PathFinder.NEIGHBOURS8) {
					int cell = pos + i;
					if (Dungeon.level.insideMap(cell)) {
						sprite.parent.addToBack(new TargetedCell(cell, 0xFF00FF));
					}
				}
				sprite.showStatus(CharSprite.WARNING, Messages.get(this, "s1"));
				Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
				Dungeon.hero.interrupt();
				Camera.main.shake(2, 0.3f);
				shockwaveWindup = 1;
				spend(TICK);
				return true;
			}
		}

		if (chargeCooldown > 0) chargeCooldown--;
		if (shockwaveCooldown > 0) shockwaveCooldown--;
		return super.act();
	}

	// 충격파 능력 해결
	private void resolveShockwave() {
		Sample.INSTANCE.play(Assets.Sounds.BLAST);
		Camera.main.shake(4, 0.5f);

		// 주변 8칸의 모든 적을 밀쳐내고 데미지
		for (int i : PathFinder.NEIGHBOURS8) {
			int cell = pos + i;
			CellEmitter.get(cell).burst(Speck.factory(Speck.ROCK), 6);
			
			Char ch = Actor.findChar(cell);
			if (ch != null && ch.alignment != alignment) {
				// 데미지
				int dmg = Random.NormalIntRange(25, 35);
				if (ch == Dungeon.hero) {
					ch.damage(dmg, this);
				} else {
					ch.damage(5, this);
				}

				// 밀쳐내기
				Ballistica trajectory = new Ballistica(pos, ch.pos, Ballistica.STOP_TARGET);
				trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
				WandOfBlastWave.throwChar(ch, trajectory, 2, false, true, getClass());

				// 50% 확률로 불구
				if (Random.Int(2) == 0) {
					Buff.affect(ch, Vulnerable.class, 3f);
				}

				if (ch == Dungeon.hero && !ch.isAlive()) {
					Dungeon.fail(getClass());
				}
			}
		}

		shockwaveWindup = 0;
		shockwaveCooldown = Random.NormalIntRange(6, 9);
		spend(1f);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		int dealt = super.attackProc(enemy, damage);
		// 공격 시 체력 회복 (VampireChariot의 능력)
		int heal = Math.max(1, Math.round(dealt * 0.25f));
		if (heal > 0 && HP < HT) {
			HP = Math.min(HT, HP + heal);
			if (sprite != null) sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
		}
		return dealt;
	}

	@Override
	public void damage(int dmg, Object src) {
		// 전차는 단단하여 받는 데미지 감소
		if (dmg > 5) {
			dmg = (int)(dmg * 0.85f);
		}
		super.damage(dmg, src);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (chargePath != null && !chargePath.isEmpty()) {
			int[] pathArray = new int[chargePath.size()];
			for (int i = 0; i < chargePath.size(); i++) {
				pathArray[i] = chargePath.get(i);
			}
			bundle.put(CHARGE_PATH, pathArray);
		}
		bundle.put(CHARGE_WIND, chargeWindup);
		bundle.put(CHARGE_CD, chargeCooldown);
		bundle.put(SHOCKWAVE_CD, shockwaveCooldown);
		bundle.put(SHOCKWAVE_WIND, shockwaveWindup);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(CHARGE_PATH)) {
			int[] pathArray = bundle.getIntArray(CHARGE_PATH);
			chargePath = new ArrayList<>();
			for (int c : pathArray) {
				chargePath.add(c);
			}
		}
		chargeWindup = bundle.getInt(CHARGE_WIND);
		chargeCooldown = bundle.getInt(CHARGE_CD);
		shockwaveCooldown = bundle.getInt(SHOCKWAVE_CD);
		shockwaveWindup = bundle.getInt(SHOCKWAVE_WIND);
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

