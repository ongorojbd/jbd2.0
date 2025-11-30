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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Passione2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUndertaleGame;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

/**
 * 언더테일 스타일 전투 시스템을 가진 테스트 몬스터
 * - 공격 턴: 타이밍을 맞춰서 적에게 피해
 * - 방어 턴: 탄막을 피해서 생존
 */
public class Utest extends NPC {

	{
		spriteClass = Passione2Sprite.Mi.class;

		HP = HT = 100;
		defenseSkill = 10;

		EXP = 10;
		maxLvl = 15;

		properties.add(Property.IMMOVABLE);
		
		state = PASSIVE; // 먼저 공격하지 않음
	}

	private boolean defeated = false;

	@Override
	protected boolean act() {
		if (defeated) {
			die(null);
			return true;
		}
		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return INFINITE_EVASION; // 일반 공격으로는 피해 불가
	}

	@Override
	public void damage(int dmg, Object src) {
		// 일반 공격으로는 피해를 입지 않음
		// WndUndertaleGame에서만 피해 가능
	}

	@Override
	public boolean add(Buff buff) {
		return false; // 버프/디버프 면역
	}

	@Override
	public boolean interact(Char c) {
		sprite.turnTo(pos, c.pos);

		if (c != Dungeon.hero) {
			return super.interact(c);
		}

		// 전투 시작 대화
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndOptions(
						sprite(),
						Messages.titleCase(name()),
						Messages.get(Utest.class, "greeting"),
						Messages.get(Utest.class, "fight"),
						Messages.get(Utest.class, "leave")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							// 전투 시작
							startBattle();
						}
					}
				});
			}
		});

		return true;
	}

	private void startBattle() {
		final Utest enemy = this;
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndUndertaleGame(enemy, new Callback() {
					@Override
					public void call() {
						// 전투 승리 시 처치
						defeated = true;
					}
				}));
			}
		});
	}

	/**
	 * 언더테일 전투에서 피해 적용 (WndUndertaleGame에서 호출)
	 */
	public void takeDamage(int dmg) {
		HP -= dmg;
		if (HP <= 0) {
			HP = 0;
		}
	}

	private static final String DEFEATED = "defeated";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEFEATED, defeated);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		defeated = bundle.getBoolean(DEFEATED);
	}
}

