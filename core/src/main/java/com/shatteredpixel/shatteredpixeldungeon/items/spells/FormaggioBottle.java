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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Civil;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P4mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class FormaggioBottle extends Spell {

	private static final String MOB_CLASS = "mob_class";
	private static final String MOB_BUNDLE = "mob_bundle";
	
	private Mob trappedMob = null;
	
	{
		image = ItemSpriteSheet.FORMAGGIO;
		unique = true;
		stackable = false;
	}

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xCCFFFF, 1f);
    }
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (trappedMob != null) {
			bundle.put(MOB_CLASS, trappedMob.getClass().getName());
			Bundle mobBundle = new Bundle();
			trappedMob.storeInBundle(mobBundle);
			bundle.put(MOB_BUNDLE, mobBundle);
		}
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(MOB_CLASS)) {
			try {
				Class<?> mobClass = Class.forName(bundle.getString(MOB_CLASS));
				trappedMob = (Mob) Reflection.newInstance(mobClass);
				if (trappedMob != null && bundle.contains(MOB_BUNDLE)) {
					trappedMob.restoreFromBundle(bundle.getBundle(MOB_BUNDLE));
				}
			} catch (Exception e) {
				trappedMob = null;
			}
		}
	}
	
	@Override
	protected void onCast(Hero hero) {
		if (trappedMob != null) {
			// 이미 적이 가둬져 있으면 위치 선택 모드로 전환
			GameScene.selectCell(releaseTargeter);
		} else {
			// 타겟 선택 모드로 전환
			GameScene.selectCell(targeter);
		}
	}
	
	private CellSelector.Listener targeter = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer target) {
			if (target == null) {
				return;
			}
			
			// 바로 한 칸 근처에 있는 적만 가둘 수 있음
			if (!Dungeon.level.adjacent(curUser.pos, target)) {
				GLog.w(Messages.get(FormaggioBottle.this, "too_far"));
				return;
			}
			
			Char ch = Actor.findChar(target);
			
			if (ch == null) {
				GLog.w(Messages.get(FormaggioBottle.this, "no_target"));
				return;
			}
			
			if (!(ch instanceof Mob)) {
				GLog.w(Messages.get(FormaggioBottle.this, "no_target"));
				return;
			}
			
			Mob mob = (Mob) ch;
			
			// Mimic 종류는 가둘 수 없음
			if (mob instanceof Mimic) {
				GLog.w(Messages.get(FormaggioBottle.this, "no_mimic"));
				return;
			}
			
			// 보스는 가둘 수 없음
			if (mob.properties().contains(Char.Property.BOSS) || mob.properties().contains(Char.Property.MINIBOSS)) {
				GLog.w(Messages.get(FormaggioBottle.this, "no_boss"));
				return;
			}
			
			// 일반 적만 가둘 수 있음
			if (mob.alignment != Char.Alignment.ENEMY) {
				GLog.w(Messages.get(FormaggioBottle.this, "no_target"));
				return;
			}
			
			curUser.busy();
			curUser.sprite.zap(target);
			
			trapEnemy(mob, curUser);
			
			curUser.spendAndNext(1f);
			Catalog.countUse(FormaggioBottle.this.getClass());
			if (Random.Float() < talentChance) {
				Talent.onScrollUsed(curUser, curUser.pos, talentFactor, FormaggioBottle.this.getClass());
			}
		}
		
		@Override
		public String prompt() {
			return Messages.get(FormaggioBottle.class, "prompt");
		}
	};
	
	private CellSelector.Listener releaseTargeter = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer target) {
			if (target == null) {
				return;
			}
			
			// 플레이어 주변 8방향만 선택 가능
			if (!Dungeon.level.adjacent(curUser.pos, target)) {
				GLog.w(Messages.get(FormaggioBottle.this, "too_far"));
				return;
			}
			
			// 해당 위치에 다른 캐릭터가 있으면 안됨
			if (Actor.findChar(target) != null) {
				GLog.w(Messages.get(FormaggioBottle.this, "cell_occupied"));
				return;
			}
			
			// 통과 불가능한 위치면 안됨
			if (Dungeon.level.solid[target] || Dungeon.level.avoid[target]) {
				GLog.w(Messages.get(FormaggioBottle.this, "invalid_cell"));
				return;
			}
			
			curUser.busy();
			curUser.sprite.zap(target);
			
			releaseTrappedMob(curUser, target);
			
			curUser.spendAndNext(1f);
			Catalog.countUse(FormaggioBottle.this.getClass());
			if (Random.Float() < talentChance) {
				Talent.onScrollUsed(curUser, curUser.pos, talentFactor, FormaggioBottle.this.getClass());
			}
		}
		
		@Override
		public String prompt() {
			return Messages.get(FormaggioBottle.class, "release_prompt");
		}
	};
	
	private void trapEnemy(Mob enemy, Hero hero) {
		// 체력바 제거
		if (TargetHealthIndicator.instance != null && TargetHealthIndicator.instance.target() == enemy) {
			TargetHealthIndicator.instance.target(null);
		}
		
		// 적을 던전에서 제거
		Dungeon.level.mobs.remove(enemy);
		Actor.remove(enemy);
		enemy.sprite.killAndErase();
		
		// 적의 상태 완전 초기화 (나중에 풀려났을 때 깨끗한 상태로 시작)
		enemy.clearEnemy();  // enemy = null, enemySeen = false, state = WANDERING 설정
		
		// 모든 버프 제거 (Amok, 독, 화상 등 이전 전투의 상태 제거)
		for (Buff buff : enemy.buffs().toArray(new Buff[0])) {
			buff.detach();
		}
		
		// target 초기화 (Reflection 사용)
		try {
			java.lang.reflect.Field targetField = Mob.class.getDeclaredField("target");
			targetField.setAccessible(true);
			targetField.setInt(enemy, -1);
		} catch (Exception e) {
			// 실패해도 계속 진행
		}
		
		// 적을 저장
		trappedMob = enemy;
		
		// 이펙트
		CellEmitter.center(enemy.pos).burst(Speck.factory(Speck.DISCOVER), 10);
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		
		GLog.p(Messages.get(this, "trapped", enemy.name()));
		
		// 아이템 소비하지 않음 (다시 사용할 수 있도록)
		updateQuickslot();
	}
	
	private void releaseTrappedMob(Hero hero, int releasePos) {
		if (trappedMob == null) {
			return;
		}
		
		// 적을 다시 배치
		trappedMob.pos = releasePos;
		trappedMob.HP = trappedMob.HT; // HP 복구
		
		// 다른 소환 트랩들과 동일하게 state를 WANDERING으로 설정
		// (GameScene.add 후 적이 자동으로 영웅을 감지하고 HUNTING 상태로 전환)
		if (trappedMob.state != trappedMob.PASSIVE) {
			trappedMob.state = trappedMob.WANDERING;
		}
		
		// 주변에 다른 적이 있는지 확인 (Amok 적용 여부 결정)
		boolean hasOtherEnemies = false;
		for (Mob mob : Dungeon.level.mobs) {
			if (mob != trappedMob && mob.alignment == Char.Alignment.ENEMY && mob.isAlive()) {
				hasOtherEnemies = true;
				break;
			}
		}
		
		// 다른 적이 있으면 Amok 버프 적용 (다른 적들을 공격하도록)
		if (hasOtherEnemies) {
			Buff.affect(trappedMob, Amok.class, 20f);
		}
		
		// 이제 적을 게임에 추가
		GameScene.add(trappedMob);
		
		// 시각 효과 및 셀 점유 (다른 소환 트랩과 동일한 순서)
		ScrollOfTeleportation.appear(trappedMob, releasePos);
		Dungeon.level.occupyCell(trappedMob);
		
		// 이펙트
		CellEmitter.center(releasePos).burst(Speck.factory(Speck.BONE), 10);
		Sample.INSTANCE.play(Assets.Sounds.SHATTER);
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

		GLog.p(Messages.get(this, "released", trappedMob.name()));
		
		// 적 정보 초기화
		trappedMob = null;
		
		// 시야 업데이트 및 영웅이 새로운 적을 인식하도록
		Dungeon.observe();
		hero.checkVisibleMobs();
		
		// 아이템 소비
		detach(hero.belongings.backpack);
		updateQuickslot();
		Invisibility.dispel();
		hero.spendAndNext(1f);
		Catalog.countUse(this.getClass());
		if (Random.Float() < talentChance) {
			Talent.onScrollUsed(hero, hero.pos, talentFactor, this.getClass());
		}
	}
	
	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");
		if (trappedMob != null) {
			desc += "\n\n" + Messages.get(this, "trapped_info", trappedMob.name());
		}
		return desc;
	}

	// 상점용: Cat이 들어있는 병 생성
	public static FormaggioBottle createWithCat() {
		FormaggioBottle bottle = new FormaggioBottle();
		bottle.trappedMob = new Cat();
		bottle.trappedMob.pos = -1;
		return bottle;
	}
	
	// 상점용: P4mob이 들어있는 병 생성
	public static FormaggioBottle createWithP4mob() {
		FormaggioBottle bottle = new FormaggioBottle();
		bottle.trappedMob = new P4mob();
		bottle.trappedMob.pos = -1;
		return bottle;
	}
	
	@Override
	public int value() {
		return 15 * quantity;
	}
}

