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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.johnny;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedThrownWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedWand;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PinkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class BlessArena extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target == null) {
			return;
		}

		hero.busy();

		armor.charge -= chargeUse(hero);
		Item.updateQuickslot();

		// Create arena buff
		BlessArenaBuff buff = Buff.affect(hero, BlessArenaBuff.class);
		buff.setup(target);

		hero.spendAndNext(1f);
	}

	@Override
	public int icon() {
		return HeroIcon.NONE;
	}

	@Override
	public Talent[] talents() {
		return new Talent[0];
	}

	public static class BlessArenaBuff extends Buff {

		private ArrayList<Integer> arenaPositions = new ArrayList<>();
		private ArrayList<Emitter> arenaEmitters = new ArrayList<>();

		private static final float DURATION = 10f;
		int left = 0;

		{
			type = buffType.POSITIVE;
			announced = true;
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1f, 0.8f, 0.2f); // 주황색이 약간 섞인 노란색 (황금색)
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - left) / DURATION);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", left);
		}

		public void setup(int pos) {
			// 3x3 범위 설정
			arenaPositions.clear();
			for (int i : PathFinder.NEIGHBOURS9) {
				int cell = pos + i;
				if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
					arenaPositions.add(cell);
				}
			}

			if (target != null) {
				fx(false);
				fx(true);
			}

			left = (int) DURATION;
		}

		@Override
		public boolean act() {
			// 영웅이 Arena 안에 있으면 Enhanced 버프들 부여 (강화 수치 1로 설정)
			if (target instanceof Hero && arenaPositions.contains(target.pos)) {
				EnhancedWeapon weaponBuff = Buff.affect(target, EnhancedWeapon.class);
				weaponBuff.setEnhancementLevel(1);
				EnhancedArmor armorBuff = Buff.affect(target, EnhancedArmor.class);
				armorBuff.setEnhancementLevel(1);
				EnhancedWand wandBuff = Buff.affect(target, EnhancedWand.class);
				wandBuff.setEnhancementLevel(1);
				EnhancedThrownWeapon thrownBuff = Buff.affect(target, EnhancedThrownWeapon.class);
				thrownBuff.setEnhancementLevel(1);
			} else {
				// Arena 밖으로 나가면 Enhanced 버프들만 제거 (Arena는 유지)
				Buff.detach(target, EnhancedWeapon.class);
				Buff.detach(target, EnhancedArmor.class);
				Buff.detach(target, EnhancedWand.class);
				Buff.detach(target, EnhancedThrownWeapon.class);
			}

			// Arena는 밖으로 나가도 계속 유지됨 (제거하지 않음)

			left--;
			BuffIndicator.refreshHero();
			if (left <= 0) {
				// 시간이 끝나면 Enhanced 버프들도 제거
				Buff.detach(target, EnhancedWeapon.class);
				Buff.detach(target, EnhancedArmor.class);
				Buff.detach(target, EnhancedWand.class);
				Buff.detach(target, EnhancedThrownWeapon.class);
				detach();
			}

			spend(TICK);
			return true;
		}

		@Override
		public void detach() {
			// Enhanced 버프들 제거
			if (target != null) {
				Buff.detach(target, EnhancedWeapon.class);
				Buff.detach(target, EnhancedArmor.class);
				Buff.detach(target, EnhancedWand.class);
				Buff.detach(target, EnhancedThrownWeapon.class);
			}
			super.detach();
		}

		@Override
		public void fx(boolean on) {
			if (on) {
				for (int i : arenaPositions) {
					Emitter e = CellEmitter.get(i);
					e.pour(PinkParticle.FACTORY, 0.05f);
					arenaEmitters.add(e);
				}
			} else {
				for (Emitter e : arenaEmitters) {
					e.on = false;
				}
				arenaEmitters.clear();
			}
		}

		private static final String ARENA_POSITIONS = "arena_positions";
		private static final String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			int[] values = new int[arenaPositions.size()];
			for (int i = 0; i < values.length; i++) {
				values[i] = arenaPositions.get(i);
			}
			bundle.put(ARENA_POSITIONS, values);

			bundle.put(LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			if (bundle.contains(ARENA_POSITIONS)) {
				int[] values = bundle.getIntArray(ARENA_POSITIONS);
				arenaPositions.clear();
				for (int value : values) {
					arenaPositions.add(value);
				}
			}

			left = bundle.getInt(LEFT);
		}
	}

}

