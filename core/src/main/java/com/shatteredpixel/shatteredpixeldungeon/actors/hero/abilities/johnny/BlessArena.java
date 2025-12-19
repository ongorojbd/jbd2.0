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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PinkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
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
		baseChargeUse = 25f;
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

        Sword.tp();

		// Create arena buff
		BlessArenaBuff buff = Buff.affect(hero, BlessArenaBuff.class);
		buff.setup(target, hero);

		hero.spendAndNext(1f);
	}

	@Override
	public int icon() {
		return HeroIcon.BLESS_ARENA;
	}

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.J44, Talent.J45, Talent.J46, Talent.HEROIC_ENERGY};
    }

	public static class BlessArenaBuff extends Buff {

		private ArrayList<Integer> arenaPositions = new ArrayList<>();
		private ArrayList<Emitter> arenaEmitters = new ArrayList<>();

		private static final float BASE_DURATION = 10f;
		private float duration = BASE_DURATION;
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
			return Math.max(0, (duration - left) / duration);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", left);
		}

		public void setup(int pos, Hero hero) {
			arenaPositions.clear();
			
			// J46 +4: 5x5 범위, 그 외: 3x3 범위
			boolean is5x5 = hero != null && hero.hasTalent(Talent.J46) && hero.pointsInTalent(Talent.J46) >= 4;
			
			if (is5x5) {
				// 5x5 범위 설정
				for (int dx = -2; dx <= 2; dx++) {
					for (int dy = -2; dy <= 2; dy++) {
						int cell = pos + dx + dy * Dungeon.level.width();
						if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
							arenaPositions.add(cell);
						}
					}
				}
			} else {
				// 3x3 범위 설정
				for (int i : PathFinder.NEIGHBOURS9) {
					int cell = pos + i;
					if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
						arenaPositions.add(cell);
					}
				}
			}

			// J44 탤런트: 지속 시간 증가 (10 + talentLevel * 5)
			if (hero != null && hero.hasTalent(Talent.J44)) {
				int talentLevel = hero.pointsInTalent(Talent.J44);
				duration = BASE_DURATION + (talentLevel * 5f); // +1: 15턴, +2: 20턴, +3: 25턴, +4: 30턴
			} else {
				duration = BASE_DURATION;
			}

			if (target != null) {
				fx(false);
				fx(true);
			}

			left = (int) duration;
		}

		@Override
		public boolean act() {
			Hero hero = target instanceof Hero ? (Hero) target : null;
			
			// 영웅이 Arena 안에 있으면 Enhanced 버프들 부여
			if (hero != null && arenaPositions.contains(hero.pos)) {
				// J45 탤런트: 강화 수치 증가 (1 + talentLevel)
				int enhancementLevel = 1;
				if (hero.hasTalent(Talent.J45)) {
					enhancementLevel = 1 + hero.pointsInTalent(Talent.J45); // +1: 2, +2: 3, +3: 4, +4: 5
				}
				
				EnhancedWeapon weaponBuff = Buff.affect(hero, EnhancedWeapon.class);
				weaponBuff.setEnhancementLevel(enhancementLevel);
				EnhancedArmor armorBuff = Buff.affect(hero, EnhancedArmor.class);
				armorBuff.setEnhancementLevel(enhancementLevel);
			} else if (hero != null) {
				// Arena 밖으로 나가면 Enhanced 버프들만 제거 (Arena는 유지)
				Buff.detach(hero, EnhancedWeapon.class);
				Buff.detach(hero, EnhancedArmor.class);
			}

			// J46 탤런트: Arena 안에 있는 적들에게 디버프 부여
			if (hero != null && hero.hasTalent(Talent.J46)) {
				int talentLevel = hero.pointsInTalent(Talent.J46);
				for (int cell : arenaPositions) {
					Char ch = Actor.findChar(cell);
					if (ch != null && ch != hero && ch.alignment == Char.Alignment.ENEMY) {
						// +1: 불구
						if (talentLevel >= 1) {
							Buff.prolong(ch, Cripple.class, Cripple.DURATION);
						}
						// +2: 실명
						if (talentLevel >= 2) {
							Buff.prolong(ch, Blindness.class, Blindness.DURATION);
						}
						// +3: 무력화
						if (talentLevel >= 3) {
							Buff.prolong(ch, Hex.class, Hex.DURATION);
						}
					}
				}
			}

			// Arena는 밖으로 나가도 계속 유지됨 (제거하지 않음)

			left--;
			BuffIndicator.refreshHero();
			if (left <= 0) {
				// 시간이 끝나면 Enhanced 버프들도 제거
				if (hero != null) {
					Buff.detach(hero, EnhancedWeapon.class);
					Buff.detach(hero, EnhancedArmor.class);
				}
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
		private static final String DURATION = "duration";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			int[] values = new int[arenaPositions.size()];
			for (int i = 0; i < values.length; i++) {
				values[i] = arenaPositions.get(i);
			}
			bundle.put(ARENA_POSITIONS, values);

			bundle.put(LEFT, left);
			bundle.put(DURATION, duration);
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
			if (bundle.contains(DURATION)) {
				duration = bundle.getFloat(DURATION);
			} else {
				duration = BASE_DURATION;
			}
		}
	}

}

