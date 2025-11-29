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
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KS3Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * KS3 - 카즈의 정예병 (기마병)
 * 빠른 이동속도와 돌진 공격을 가진 기동형 유닛
 * 일정 쿨타임마다 영웅을 향해 돌진하여 큰 데미지를 가함
 */
public class KS3 extends Mob {

	{
		spriteClass = KS3Sprite.class;

		HP = HT = 230;
		defenseSkill = 30;

		EXP = 20;
		maxLvl = 30;
		baseSpeed = 1.5f; // 빠른 이동속도

		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);
	}

	private int chargeCooldown = 5;
	private int chargeWindup = 0;
	private ArrayList<Integer> chargePath = new ArrayList<>();

	private static final String CHARGE_CD = "charge_cd";
	private static final String CHARGE_WIND = "charge_wind";

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 50, 55 );
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
		if (chargeCooldown > 0) chargeCooldown--;

		// 돌진 준비중일 때
		if (chargeWindup > 0) {
			chargeWindup--;
			if (chargeWindup == 0 && !chargePath.isEmpty()) {
				resolveCharge();
				return true;
			} else if (chargeWindup > 0) {
				// 돌진 경로 표시
				for (int c : chargePath) {
					sprite.parent.addToBack(new TargetedCell(c, 0xFF8800));
				}
				spend(TICK);
				return true;
			}
		}

		// 돌진 가능 조건: 쿨타임 끝 + 적이 3칸 이상 떨어져 있음
		if (enemy != null && chargeCooldown <= 0 && Dungeon.level.distance(pos, enemy.pos) >= 3) {
			if (telegraphCharge()) {
				return true;
			}
		}

		return super.act();
	}

	private boolean telegraphCharge() {
		if (enemy == null) return false;
		
		chargePath.clear();
		Ballistica path = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
		
		if (path.dist <= 1) return false;
		
		// 경로 설정 (최소 2칸, 최대 5칸)
		int chargeDistance = Math.min(5, path.dist);
		for (int i = 1; i <= chargeDistance; i++) {
			if (i < path.path.size()) {
				chargePath.add(path.path.get(i));
			}
		}
		
		if (chargePath.isEmpty()) return false;

		// 돌진 예고
		for (int c : chargePath) {
			sprite.parent.addToBack(new TargetedCell(c, 0xFF8800));
		}

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "charge"));
		Sample.INSTANCE.play(Assets.Sounds.HORSE);
		Dungeon.hero.interrupt();

		chargeWindup = 1;
		spend(TICK);
		return true;
	}

	private void resolveCharge() {
		Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
		
		// 경로상의 모든 캐릭터에게 데미지
		for (int c : chargePath) {
			Char ch = Actor.findChar(c);
			if (ch != null && ch.alignment != alignment) {
				int dmg = Random.NormalIntRange(30, 45);
				ch.damage(dmg, this);
				
				// 50% 확률로 다리 부상
				if (Random.Int(2) == 0) {
					Buff.affect(ch, Cripple.class, 3f);
				}
				
				if (ch == Dungeon.hero && !ch.isAlive()) {
					Dungeon.fail(getClass());
				}
			}
		}

		// 말이 최종 위치로 이동
		int destination = pos;
		for (int i = chargePath.size() - 1; i >= 0; i--) {
			int cell = chargePath.get(i);
			if (!Dungeon.level.solid[cell] && Actor.findChar(cell) == null) {
				destination = cell;
				break;
			}
		}

		if (destination != pos) {
			int old = pos;
			pos = destination;
			Actor.add(new Pushing(this, old, pos));
			Dungeon.level.occupyCell(this);
			sprite.emitter().burst(Speck.factory(Speck.DUST), 8);
		}

		chargePath.clear();
		chargeWindup = 0;
		chargeCooldown = 6; // 돌진 후 쿨타임
		spend(1f);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHARGE_CD, chargeCooldown);
		bundle.put(CHARGE_WIND, chargeWindup);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		chargeCooldown = bundle.getInt(CHARGE_CD);
		chargeWindup = bundle.getInt(CHARGE_WIND);
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

