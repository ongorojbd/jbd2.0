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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
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
import com.watabou.utils.Random;

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

	// 6레벨 이상일 때 자기 자신 사용 가능 여부
	public boolean canTargetSelf() {
		return level() >= 6;
	}

	// 자기 자신에게 사용 (Fadeleaf 효과)
	private void useSelfEffect(Hero hero) {
		// 충전량 소모 (2 소모)
		charge -= 2;

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
					// 충전량이 2 이상인지 확인
					if (charge < 2) {
						GLog.i(Messages.get(this, "no_charge"));
						return;
					}
					// 6레벨 이상이면 Fadeleaf 효과 발동
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

			// 레벨 0일 때 벽 체크: 벽에 막히면 경고 메시지 표시
			if (level() == 0) {
				Ballistica beam = new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT);
				Char chAtCollision = Actor.findChar(beam.collisionPos);
				// 벽에 막혔거나 타겟이 collisionPos에 없으면 경고
				if (chAtCollision == null || chAtCollision != ch) {
					GLog.w(Messages.get(TuskEquipmentDisc.class, "blocked_by_terrain"));
					return;
				}
			}

			// 레벨 3 이상일 때 관통 기능: 경로상의 모든 적 찾기
			if (level() >= 3) {
				Ballistica beam = new Ballistica(curUser.pos, target, Ballistica.WONT_STOP);
				piercedTargets.clear();
				
				// 경로상의 모든 적 찾기
				for (int c : beam.subPath(1, beam.dist)) {
					Char enemy = Actor.findChar(c);
					if (enemy != null && enemy != curUser 
							&& enemy instanceof Mob 
							&& !(enemy instanceof NPC)
							&& enemy.alignment != Char.Alignment.ALLY
							&& enemy.alignment == Char.Alignment.ENEMY) {
						// PASSIVE 상태의 미발견 적은 제외
						if (enemy instanceof Mob && ((Mob) enemy).state == ((Mob) enemy).PASSIVE
								&& !(Dungeon.level.mapped[c] || Dungeon.level.visited[c])) {
							continue;
						}
						piercedTargets.add(enemy);
					}
				}
				
				// 첫 번째 적에 대해서만 미니게임 실행
				if (!piercedTargets.isEmpty()) {
					GameScene.show(new WndTuskAiming(TuskEquipmentDisc.this, piercedTargets.get(0)));
				} else {
					// 경로상에 적이 없으면 원래 타겟에 대해서만 실행
					GameScene.show(new WndTuskAiming(TuskEquipmentDisc.this, ch));
				}
			} else {
				// 레벨 0~2일 때는 기존처럼 단일 타겟만
				piercedTargets.clear();
				GameScene.show(new WndTuskAiming(TuskEquipmentDisc.this, ch));
			}
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

	// 관통된 적들 저장 (레벨 1 이상일 때, 각 발사마다)
	private ArrayList<Char> piercedTargets = new ArrayList<>();
	
	// 미니게임 결과 저장 (관통된 적들에게 적용하기 위해, 각 발사마다)
	private int lastDamage = 0;
	private String lastHitType = "";
	private float lastDamageRatio = 0f;

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

		// 2. 경로에 빛 파티클 추가 및 관통된 적들 찾기
		// 레벨 0~2일 때는 벽에 막히도록, 레벨 3 이상일 때는 벽을 통과하도록
		int collisionProps = (level() >= 3) ? Ballistica.WONT_STOP : Ballistica.MAGIC_BOLT;
		Ballistica beam = new Ballistica(hero.pos, target.pos, collisionProps);
		
		// 레벨 0일 때 벽에 막혔는지 확인 (WandOfMagicMissile처럼)
		if (level() == 0) {
			Char ch = Actor.findChar(beam.collisionPos);
			// 벽에 막혔거나 타겟이 collisionPos에 없으면 데미지를 주지 않음
			if (ch == null || ch != target) {
				// 이펙트만 표시하고 데미지는 주지 않음
				for (int c : beam.subPath(1, beam.dist)) {
					CellEmitter.center(c).burst(LightParticle.BURST, 8);
				}
				int cell = beam.path.get(Math.min(beam.dist, beam.path.size() - 1));
				hero.sprite.parent.add(new Beam.SuperNovaRay(
					hero.sprite.center(),
					DungeonTilemap.raisedTileCenterToWorld(cell),
					2
				));
				Dungeon.level.pressCell(beam.collisionPos);
				return false; // 데미지 없이 종료
			}
		}
		
		// 레벨 3 이상일 때 각 발사마다 관통된 적들 계산 (2발 모드 대응)
		if (level() >= 3) {
			piercedTargets.clear();
			// 경로상의 모든 적 찾기
			for (int c : beam.subPath(1, beam.dist)) {
				Char enemy = Actor.findChar(c);
				if (enemy != null && enemy != hero 
						&& enemy instanceof Mob 
						&& !(enemy instanceof NPC)
						&& enemy.alignment != Char.Alignment.ALLY
						&& enemy.alignment == Char.Alignment.ENEMY) {
					// PASSIVE 상태의 미발견 적은 제외
					if (enemy instanceof Mob && ((Mob) enemy).state == ((Mob) enemy).PASSIVE
							&& !(Dungeon.level.mapped[c] || Dungeon.level.visited[c])) {
						continue;
					}
					piercedTargets.add(enemy);
				}
			}
		}
		
		// 경로에 빛 파티클 추가
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

        // 디버그 용도 업그레이드
//        upgrade();

		// 데미지 적용
		boolean wasAlive = target.isAlive();
		target.damage(damage, this);
        Sample.INSTANCE.play(Assets.Sounds.EVOKE);

		// provoked_anger talent: 적을 처치했을 때 아드레날린 부여
		if (!wasAlive || !target.isAlive()) {
			if (hero.hasTalent(Talent.PROVOKED_ANGER)) {
				int talentLevel = hero.pointsInTalent(Talent.PROVOKED_ANGER);
				if (talentLevel >= 1) {
					float duration = talentLevel * 4; // 1레벨: 4턴, 2레벨: 8턴
					Buff.prolong(hero, Adrenaline.class, duration);
				}
			}
		}

		// 미니게임 결과 저장 (관통된 적들에게 적용하기 위해)
		lastDamage = damage;
		lastHitType = hitType;
		lastDamageRatio = damageRatio;

		// GLog 메시지 표시 (hitType에 따라 색상 다르게)
		String message = Messages.get("windows.wndtuskaiming." + hitType, damage);
		switch (hitType) {
			case "perfect":
				GLog.p(message);
				break;
			case "great":
				GLog.h(message);
				break;
			case "good":
				GLog.w(message);
				break;
			case "miss":
			default:
				GLog.n(message);
				break;
		}

		// Perfect 판정 체크 (50% 데미지 = Perfect)
		if (damageRatio >= 0.50f) {
			perfectCount++;
			checkLevelUp();
			
			// HorseRiding leap 충전량 획득
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding horseRiding = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding.class);
			if (horseRiding != null) {
				horseRiding.addLeapCharge();
			}
			
			// improvised_projectiles talent: 정확한 공격 시 Doom 부여
			if (hero.hasTalent(Talent.PROVOKED_ANGER)) {
				int talentLevel = hero.pointsInTalent(Talent.PROVOKED_ANGER);
				if (talentLevel >= 1) {
					float duration = talentLevel * 2; // 1레벨: 2턴, 2레벨: 4턴
					Doom doom = Buff.affect(target, Doom.class);
					doom.setDuration(duration);
				}
			}
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
			// 레벨업 전에 현재 레벨이 levelCap에 도달했는지 다시 확인
			if (level() < levelCap) {
				upgrade();
				GLog.p(Messages.get(this, "upgrade", level()));
			}
		}
	}

	@Override
	public Item upgrade() {
		// levelCap을 초과하지 않도록 체크
		if (level() >= levelCap) {
			return this;
		}
		
		chargeCap = Math.min(chargeCap + 1, 10);
		charge = Math.min(charge + 1, chargeCap);
		Item result = super.upgrade();
		
		// 레벨에 따라 이미지 설정 (SandalsOfNature.java 참조)
		int lvl = level();
		if (lvl <= 2) {
			image = ItemSpriteSheet.ARTIFACT_TUSK1;
		} else if (lvl <= 5) {
			image = ItemSpriteSheet.ARTIFACT_TUSK2;
		} else if (lvl <= 9) {
			image = ItemSpriteSheet.ARTIFACT_TUSK3;
		} else { // lvl == 10
			image = ItemSpriteSheet.ARTIFACT_TUSK4;
		}
		
		return result;
	}

	// 각 발사 완료 후 관통된 적들 처리 (2발 모드 대응)
	public void processPiercedTargets() {
		// 레벨 3 이상이고 관통된 적들이 있으면 나머지 적들도 처리
		if (level() >= 3 && !piercedTargets.isEmpty() && lastDamage > 0) {
			// 첫 번째 적은 이미 처리되었으므로 나머지만 처리
			for (int i = 1; i < piercedTargets.size(); i++) {
				Char enemy = piercedTargets.get(i);
				if (enemy != null && enemy.isAlive() && enemy != currentTarget) {
					// 같은 데미지와 hitType 적용
					applyPierceDamage(enemy, lastDamage, lastHitType, lastDamageRatio);
				}
			}
		}
		// 각 발사마다 관통 처리 후 리셋 (다음 발사를 위해)
		piercedTargets.clear();
		lastDamage = 0;
		lastHitType = "";
		lastDamageRatio = 0f;
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
		piercedTargets.clear();
		lastDamage = 0;
		lastHitType = "";
		lastDamageRatio = 0f;
	}

	// 관통된 적에게 데미지 적용 (이펙트 포함)
	private void applyPierceDamage(Char target, int damage, String hitType, float damageRatio) {
		if (target == null || !target.isAlive()) return;

		Hero hero = Dungeon.hero;
		if (hero == null) return;

		// 레이저 이펙트는 첫 번째 타겟에서만 생성되므로 여기서는 제거
		// 타겟에 파티클 및 플래시 효과만 적용
		target.sprite.centerEmitter().burst(LightParticle.BURST, 8);
		target.sprite.flash();

		// 데미지 적용
		boolean wasAlive = target.isAlive();
		target.damage(damage, this);
		
		// provoked_anger talent: 적을 처치했을 때 아드레날린 부여
		if (!wasAlive || !target.isAlive()) {
			if (hero.hasTalent(Talent.PROVOKED_ANGER)) {
				int talentLevel = hero.pointsInTalent(Talent.PROVOKED_ANGER);
				if (talentLevel >= 1) {
					float duration = talentLevel; // 1레벨: 1턴, 2레벨: 2턴
					Buff.prolong(hero, Adrenaline.class, duration);
				}
			}
		}

		// GLog 메시지 표시 (hitType에 따라 색상 다르게)
		String message = Messages.get("windows.wndtuskaiming." + hitType, damage);
		switch (hitType) {
			case "perfect":
				GLog.p(message);
				break;
			case "great":
				GLog.h(message);
				break;
			case "good":
				GLog.w(message);
				break;
			case "miss":
			default:
				GLog.n(message);
				break;
		}

		// Perfect 판정 체크 (50% 데미지 = Perfect)
		if (damageRatio >= 0.50f) {
			perfectCount++;
			checkLevelUp();
			
			// HorseRiding leap 충전량 획득
			if (hero != null) {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding horseRiding = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding.class);
				if (horseRiding != null) {
					horseRiding.addLeapCharge();
				}
			}
		}
	}

	// 업그레이드 여부 확인 (2발 발사 가능 여부)
	// GLADIATOR 서브클래스인 경우에만 2발 발사 가능
	public boolean isUpgraded() {
		Hero hero = Dungeon.hero;
		return hero != null && hero.subClass == HeroSubClass.GLADIATOR;
	}

	// 고정 데미지 계산 (강화 수치 기반)
	public int calculateDamage(String hitType, Char target) {
		int lvl = level();
		
		switch (hitType) {
			case "perfect":
				// 초록 영역: 최대 12+5×level
				int maxDmg = 12 + 5 * lvl;
				// 최소: BOSS/MINIBOSS가 아니면 적의 최대 체력의 절반, 아니면 7+level
				int minDmg;
				if (target != null && !Char.hasProp(target, Char.Property.BOSS) 
						&& !Char.hasProp(target, Char.Property.MINIBOSS)) {
					minDmg = target.HT / 2;
				} else {
					minDmg = 7 + lvl;
				}
				// 최소가 최대를 넘지 않도록 보장
				return Random.NormalIntRange(Math.min(minDmg, maxDmg), maxDmg);
			
			case "great":
				// 노랑 영역: 최소 2+level, 최대 8+2×level
				return Random.NormalIntRange(2 + lvl, 8 + 2 * lvl);
			
			case "good":
				// 주황 영역: 최소 1+level/2, 최대 4+level (더 약하게)
				return Random.NormalIntRange(1 + lvl/2, 4 + lvl);
			
			case "miss":
			default:
				// 빨강 영역: 피해량 없음
				return 0;
		}
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
				
				if (level() >= 3 && level() <= 5) {
					desc += "\n\n" + Messages.get(this, "desc_upgraded2");
				} else if (level() >= 6 && level() <= 8) {
                    desc += "\n\n" + Messages.get(this, "desc_upgraded3");
                } else if (level() >= 9 && level() <= 10) {
                    desc += "\n\n" + Messages.get(this, "desc_upgraded4");
                }

				if (level() < levelCap) {
					int current = perfectsAtCurrentLevel();
					int needed = perfectsForLevel(level());
					int remaining = needed - current;
					desc += "\n\n" + Messages.get(this, "desc_perfect", 
						current, needed, remaining);
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
		
		// 레벨에 따라 이미지 설정 (SandalsOfNature.java 참조)
		int lvl = level();
		if (lvl <= 2) {
			image = ItemSpriteSheet.ARTIFACT_TUSK1;
		} else if (lvl <= 5) {
			image = ItemSpriteSheet.ARTIFACT_TUSK2;
		} else if (lvl <= 9) {
			image = ItemSpriteSheet.ARTIFACT_TUSK3;
		} else { // lvl == 10
			image = ItemSpriteSheet.ARTIFACT_TUSK4;
		}
	}
}
