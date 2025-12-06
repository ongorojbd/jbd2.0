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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LightParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTuskAiming;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TuskEquipmentDisc extends Artifact {

	public static final String AC_SHOOT = "SHOOT";

	{
		image = ItemSpriteSheet.ARTIFACT_TUSK1;

        exp = 0;
        levelCap = 10;
        charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level()+3, 10);

		defaultAction = AC_SHOOT;
		usesTargeting = true;

        unique = true;
        bones = false;
	}

	// Perfect 횟수 카운터 (영구 저장)
	private int perfectCount = 0;

	// 레벨업에 필요한 Perfect 횟수 (레벨별)
	private int perfectsForLevel(int level) {
		// 레벨 0→1: 2회, 레벨 1→2: 4회, ..., 레벨 9→10: 20회
		return (level + 1) * 2;
	}

	// 현재 레벨에서 얻은 Perfect 횟수
	public int perfectsAtCurrentLevel() {
		int spent = 0;
		for (int i = 0; i < level(); i++) {
			spent += perfectsForLevel(i);
		}
		return perfectCount - spent;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge > 0 && !cursed && hero.buff(MagicImmune.class) == null) {
			actions.add(AC_SHOOT);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_SHOOT)) {
			curUser = hero;

			if (!isEquipped(hero)) {
				GLog.i(Messages.get(Artifact.class, "need_to_equip"));
				usesTargeting = false;

			} else if (charge <= 0) {
				GLog.i(Messages.get(this, "no_charge"));
				usesTargeting = false;

			} else if (cursed) {
				GLog.w(Messages.get(this, "cursed"));
				usesTargeting = false;

			} else {
				usesTargeting = true;
				GameScene.selectCell(targeter);
			}
		}
	}

	// 3레벨 이상일 때 자기 자신 사용 가능 여부
	public boolean canTargetSelf() {
		return level() >= 3;
	}

	// 자기 자신에게 사용 (Fadeleaf 효과)
	private void useSelfEffect(Hero hero) {
		// 충전량 소모
		charge--;

		// Fadeleaf 효과 발동
		new Fadeleaf().activate(hero);

		// 버프 처리
		Invisibility.dispel(hero);
		Talent.onArtifactUsed(hero);
		updateQuickslot();

		// 턴 소모
		hero.spendAndNext(1f);

		GLog.i(Messages.get(this, "self_use"));
	}

	// 적 선택용 CellSelector.Listener
	private CellSelector.Listener targeter = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer target) {
			if (target == null) return;

			Char ch = Actor.findChar(target);

			// 타겟이 없는 경우
			if (ch == null) {
				GLog.w(Messages.get(TuskEquipmentDisc.class, "no_target"));
				return;
			}

			// 자기 자신을 선택한 경우
			if (ch == curUser) {
				if (canTargetSelf()) {
					// 3레벨 이상이면 Fadeleaf 효과 발동
					useSelfEffect((Hero) curUser);
				} else {
					GLog.w(Messages.get(TuskEquipmentDisc.class, "cannot_target_self"));
				}
				return;
			}

			// NPC인 경우 조준 불가
			if (ch instanceof NPC) {
				GLog.w(Messages.get(TuskEquipmentDisc.class, "not_enemy"));
				return;
			}

			// 적(Mob)이 아닌 경우 조준 불가
			if (!(ch instanceof Mob)) {
				GLog.w(Messages.get(TuskEquipmentDisc.class, "not_enemy"));
				return;
			}

			// 아군인 경우 조준 불가
			if (ch.alignment == Char.Alignment.ALLY) {
				GLog.w(Messages.get(TuskEquipmentDisc.class, "ally_target"));
				return;
			}

			// 시야 체크
			if (!Dungeon.level.heroFOV[target]) {
				GLog.w(Messages.get(TuskEquipmentDisc.class, "out_of_sight"));
				return;
			}

			// 타이밍 미니게임 창 열기
			GameScene.show(new WndTuskAiming(TuskEquipmentDisc.this, ch));
		}

		@Override
		public String prompt() {
			if (canTargetSelf()) {
				return Messages.get(TuskEquipmentDisc.class, "prompt_with_self");
			}
			return Messages.get(TuskEquipmentDisc.class, "prompt");
		}
	};

	// 첫 발사 여부 추적
	private boolean isFirstShot = true;

	// 현재 타겟 저장 (미니게임에서 사용)
	private Char currentTarget = null;

	// 각 발사마다 호출되는 메서드 (즉시 데미지 적용 및 GLog 표시)
	// 반환값: 타겟이 살아있으면 true, 죽었으면 false
	public boolean onSingleShotComplete(Char target, float damageRatio, String hitType, int damage) {
		if (target == null || !target.isAlive()) return false;

		Hero hero = Dungeon.hero;
		if (hero == null) return false;

		// 타겟 저장
		currentTarget = target;

		// 첫 발사 시 충전량 소모
		if (isFirstShot) {
			charge--;
			isFirstShot = false;
		}

		// SuperNova 스타일 이펙트
		// 1. 히어로 스프라이트 zap 애니메이션
		hero.sprite.zap(target.pos);

		// 2. 경로에 빛 파티클 추가
		Ballistica beam = new Ballistica(hero.pos, target.pos, Ballistica.WONT_STOP);
		for (int c : beam.subPath(1, beam.dist)) {
			CellEmitter.center(c).burst(LightParticle.BURST, 8);
		}

		// 3. SuperNovaRay 빔 이펙트 (두께 2)
		int cell = beam.path.get(Math.min(beam.dist, beam.path.size() - 1));
		hero.sprite.parent.add(new Beam.SuperNovaRay(
			hero.sprite.center(),
			DungeonTilemap.raisedTileCenterToWorld(cell),
			2
		));

		// 4. 타겟에 파티클 및 플래시 효과
		target.sprite.centerEmitter().burst(LightParticle.BURST, 8);
		target.sprite.flash();

		// 데미지 적용
		target.damage(damage, this);

		// GLog 메시지 표시
		GLog.i(Messages.get("windows.wndtuskaiming." + hitType, damage));

		// Perfect 판정 체크 (50% 데미지 = Perfect)
		if (damageRatio >= 0.50f) {
			perfectCount++;
			checkLevelUp();
		}

		// 타겟이 살아있는지 반환
		return target.isAlive();
	}

	// 레벨업 체크
	private void checkLevelUp() {
		if (level() >= levelCap) return;

		int totalNeeded = 0;
		for (int i = 0; i <= level(); i++) {
			totalNeeded += perfectsForLevel(i);
		}

		if (perfectCount >= totalNeeded) {
			upgrade();
			GLog.p(Messages.get(this, "upgrade", level()));
		}
	}

	@Override
	public Item upgrade() {
		chargeCap = Math.min(chargeCap + 1, 10);
		charge = Math.min(charge + 1, chargeCap);
		return super.upgrade();
	}

	// 모든 발사 완료 후 호출되는 메서드
	public void onAllShotsComplete() {
		Hero hero = Dungeon.hero;
		if (hero == null) return;

		// 버프 처리
		Invisibility.dispel(hero);
		Talent.onArtifactUsed(hero);
		updateQuickslot();

		// 턴 소모
		hero.spendAndNext(1f);

		// 아티팩트 proc
		if (currentTarget != null) {
			artifactProc(currentTarget, visiblyUpgraded(), 1);
		}

		// 첫 발사 플래그 및 타겟 리셋
		isFirstShot = true;
		currentTarget = null;
	}

	// 업그레이드 여부 확인 (2발 발사 가능 여부)
	public boolean isUpgraded() {
		return level() >= 6;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new tuskRecharge();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;

		if (charge < chargeCap) {
			partialCharge += 0.25f * amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (isEquipped(Dungeon.hero)) {
			desc += "\n\n";
			if (cursed) {
				desc += Messages.get(this, "desc_cursed");
			} else {
				desc += Messages.get(this, "desc_equipped");
				if (isUpgraded()) {
					desc += "\n\n" + Messages.get(this, "desc_upgraded");
				}
				if (level() < levelCap) {
					int current = perfectsAtCurrentLevel();
					int needed = perfectsForLevel(level());
					int remaining = needed - current;
					desc += "\n\n" + Messages.get(this, "desc_perfect", 
						current, needed, remaining);
				} else {
					desc += "\n\n" + Messages.get(this, "desc_max_level");
				}
			}
		}
		return desc;
	}


	public class tuskRecharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
				if (Regeneration.regenOn()) {
					float missing = (chargeCap - charge);
					if (level() > 7) missing += 5*(level() - 7)/3f;
					float turnsToCharge = (45 - missing);
					turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
					float chargeToGain = (1f / turnsToCharge);
					partialCharge += chargeToGain;
				}

				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					if (charge == chargeCap){
						partialCharge = 0;
					}
				}
			} else {
				partialCharge = 0;
			}

			updateQuickslot();
			spend(TICK);
			return true;
		}
	}

	private static final String PERFECT_COUNT = "perfectCount";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PERFECT_COUNT, perfectCount);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		perfectCount = bundle.getInt(PERFECT_COUNT);
	}
}
