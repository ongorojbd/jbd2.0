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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KS4Sprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * KS4 - 카즈의 정예병 (전차)
 * 매우 높은 체력과 방어력을 가진 중장갑 유닛
 * 주기적으로 주변에 범위 공격을 가하고 적을 기절시킴
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

	private int trampleCooldown = 6;
	private int trampleWindup = 0;
	private ArrayList<Integer> trampleCells = new ArrayList<>();

	private static final String TRAMPLE_CD = "trample_cd";
	private static final String TRAMPLE_WIND = "trample_wind";

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
		if (trampleCooldown > 0) trampleCooldown--;

		// 짓밟기 준비중일 때
		if (trampleWindup > 0) {
			trampleWindup--;
			if (trampleWindup == 0 && !trampleCells.isEmpty()) {
				resolveTrample();
				return true;
			} else if (trampleWindup > 0) {
				// 범위 표시
				for (int c : trampleCells) {
					sprite.parent.addToBack(new TargetedCell(c, 0xFF0000));
				}
				spend(TICK);
				return true;
			}
		}

		// 짓밟기 가능 조건: 쿨타임 끝 + 적이 가까이 있음
		if (enemy != null && trampleCooldown <= 0 && Dungeon.level.distance(pos, enemy.pos) <= 4) {
			if (telegraphTrample()) {
				return true;
			}
		}

		return super.act();
	}

	private boolean telegraphTrample() {
		if (enemy == null) return false;

		trampleCells.clear();
		
		// 자신 위치 포함 3x3 범위
		trampleCells.add(pos);
		for (int i : PathFinder.NEIGHBOURS9) {
			int cell = pos + i;
			if (Dungeon.level.insideMap(cell) && !trampleCells.contains(cell)) {
				trampleCells.add(cell);
			}
		}

		if (trampleCells.isEmpty()) return false;

		// 범위 공격 예고
		for (int c : trampleCells) {
			sprite.parent.addToBack(new TargetedCell(c, 0xFF0000));
			CellEmitter.get(c).burst(Speck.factory(Speck.ROCK), 2);
		}

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "trample"));
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		Dungeon.hero.interrupt();
		Camera.main.shake(3, 0.3f);

		trampleWindup = 1;
		spend(TICK);
		return true;
	}

	private void resolveTrample() {
		Sample.INSTANCE.play(Assets.Sounds.BLAST);
		Camera.main.shake(6, 0.5f);

		// 범위 내 모든 적에게 데미지
		for (int c : trampleCells) {
			CellEmitter.get(c).burst(Speck.factory(Speck.ROCK), 8);
			
			Char ch = Actor.findChar(c);
			if (ch != null && ch.alignment != alignment) {
				int dmg = Random.NormalIntRange(35, 50);
				ch.damage(dmg, this);

				// 70% 확률로 기절
				if (Random.Int(10) < 7) {
					Buff.affect(ch, Paralysis.class, 2f);
				}

				if (ch == Dungeon.hero && !ch.isAlive()) {
					Dungeon.fail(getClass());
				}
			}
		}

		trampleCells.clear();
		trampleWindup = 0;
		trampleCooldown = 8; // 짓밟기 후 쿨타임
		spend(1f);
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
		bundle.put(TRAMPLE_CD, trampleCooldown);
		bundle.put(TRAMPLE_WIND, trampleWindup);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		trampleCooldown = bundle.getInt(TRAMPLE_CD);
		trampleWindup = bundle.getInt(TRAMPLE_WIND);
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

