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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Cat extends Mob {

	{
		spriteClass = CatSprite.class;
		
		HP = HT = 10;
		defenseSkill = 15;
		
		EXP = 2;
		maxLvl = 10;
		
		baseSpeed = 2f; // 빠른 이동 속도
		
		// MagicImmune 속성 - 마법 공격에 면역
		immunities.addAll(AntiMagic.RESISTS);
		
		// 기본 상태는 도망 상태
		state = FLEEING;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(1, 3);
	}
	
	@Override
	public int attackSkill(Char target) {
		return 10;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 2);
	}
	
	@Override
	public void damage(int dmg, Object src) {
		// 투척 무기에 면역
		if (src instanceof MissileWeapon) {
			GLog.w(Messages.get(this, "missile_immune"));
			return; // 데미지를 받지 않음
		}
		
		// 영웅이 투척 무기로 공격할 때 체크
		if (src instanceof Hero) {
			Hero hero = (Hero) src;
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				GLog.w(Messages.get(this, "missile_immune"));
				return;
			}
		}
		
		super.damage(dmg, src);
	}
	
	// 플레이어를 발견하면 항상 도망
	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos] && enemy == Dungeon.hero) {
			state = FLEEING;
		}
		return super.act();
	}
	
	// Hunting 상태를 오버라이드하여 도망가도록 함
	private class Hunting extends Mob.Hunting {
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV) {
				// 플레이어를 발견하면 도망
				state = FLEEING;
				target = enemy.pos;
			}
			return true;
		}
	}
	
	// Fleeing 상태를 오버라이드하여 절대 싸우지 않도록 함
	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			// 도망갈 곳이 없어도 싸우지 않고 제자리에 있음
			// 기본 행동을 무시하고 아무것도 하지 않음
		}
		
		@Override
		protected void escaped() {
			// 탈출해도 계속 도망 상태 유지
			state = WANDERING;
			target = Dungeon.level.randomDestination(Cat.this);
		}
	}
	
	{
		// Hunting과 Fleeing 상태 초기화
		HUNTING = new Hunting();
		FLEEING = new Fleeing();
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		Dungeon.level.drop( new StewedMeat(), pos ).sprite.drop( pos );

	}
}

