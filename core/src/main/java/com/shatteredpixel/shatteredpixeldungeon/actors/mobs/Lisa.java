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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LisaSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Lisa extends Mob {

	{
		spriteClass = LisaSprite.class;

		HP = HT = 350;
		defenseSkill = 30;

		alignment = Alignment.ALLY;
		intelligentAlly = true;
		state = HUNTING;
	}

	private boolean canCauseGameOver = true;

	// 레이저 스킬 관련 변수
	private int laserCooldown = 5;
	private int laserWindup = 0;
	private Char laserTarget = null;

	private static final String CAN_CAUSE_GAME_OVER = "can_cause_game_over";
	private static final String LASER_CD = "laser_cd";
	private static final String LASER_WIND = "laser_wind";

	@Override
	protected boolean act() {
		// 레이저 쿨다운 감소
		if (laserCooldown > 0) {
			laserCooldown--;
			// 쿨타임이 방금 끝났을 때 충전 이펙트 표시
			if (laserCooldown == 0) {
				SpellSprite.show(this, SpellSprite.CHARGE);
			}
		}

		// 레이저 충전 중
		if (laserWindup > 0) {
			// 충전 중 타겟이 죽었는지 확인
			if (laserTarget == null || !laserTarget.isAlive()) {
				// 타겟이 죽었으면 충전 취소
				laserWindup = 0;
				laserTarget = null;
				laserCooldown = 3; // 취소 시 짧은 쿨다운
			} else {
				laserWindup--;
				if (laserWindup == 0) {
					fireLaser();
					return true;
				} else {
					// 충전 중 타겟 표시
					CellEmitter.get(laserTarget.pos).burst(SparkParticle.FACTORY, 3);
					spend(TICK);
					return true;
				}
			}
		}

		// 적이 있고 레이저 쿨다운이 끝났으면 레이저 발사 준비
		if (enemy != null && laserCooldown <= 0 && Dungeon.level.distance(pos, enemy.pos) >= 2) {
			if (prepareLaser()) {
				return true;
			}
		}

		boolean result = super.act();
		Dungeon.level.updateFieldOfView(this, fieldOfView);
		GameScene.updateFog(pos, viewDistance + (int)Math.ceil(speed()));
		return result;
	}

	// 레이저 발사 준비 (1턴 충전)
	private boolean prepareLaser() {
		if (enemy == null) return false;

		// 시야 확인
		Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
		if (!Dungeon.level.heroFOV[pos] || !Dungeon.level.heroFOV[enemy.pos] || aim.collisionPos != enemy.pos) {
			return false;
		}

		laserTarget = enemy;
		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "laser_charge"));
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		CellEmitter.get(enemy.pos).burst(SparkParticle.FACTORY, 5);
		this.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
		laserWindup = 1;
		spend(TICK);
		return true;
	}

	// 레이저 발사
	private void fireLaser() {
		if (laserTarget == null || !laserTarget.isAlive()) {
			laserTarget = null;
			return;
		}

		// 레이저 빔 이펙트
		sprite.parent.add(new Beam.LightRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(laserTarget.pos)));
		Sample.INSTANCE.play(Assets.Sounds.RAY);

		// 경로상의 모든 적 확인
		Ballistica beam = new Ballistica(pos, laserTarget.pos, Ballistica.PROJECTILE);
		for (int cell : beam.path) {
			Char ch = Actor.findChar(cell);
			if (ch != null && ch.alignment != alignment && ch != Dungeon.hero) {
				// 레이저 피해
				int damage = Random.NormalIntRange(15, 25);
				ch.damage(damage, this);

				// 1턴 마비
				Buff.affect(ch, Paralysis.class, 1f);

				// 어그로를 Lisa에게 끌기
				if (ch instanceof Mob) {
					Mob mob = (Mob) ch;
					mob.aggro(this);
					mob.target = this.pos;
				}

				// 이펙트
				CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 8);
				
				break; // 첫 번째 적에게만 적중
			}
		}

		laserTarget = null;
		laserWindup = 0;
		laserCooldown = 25; // 25턴 쿨타임
		spend(1f);
	}

	// TendencyItem 사용 시 발동하는 스킬 - 시야의 모든 적에게 빔 발사 + 마비 (어그로 끌리지 않음)
	public void tendencySkill() {
		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "tendency_skill"));
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		sprite.centerEmitter().burst(EnergyParticle.FACTORY, 20);

		// 시야 내의 모든 적 찾기
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.alignment != alignment && Dungeon.level.heroFOV[mob.pos]) {
				// 레이저 빔 이펙트
				sprite.parent.add(new Beam.LightRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(mob.pos)));
				
				// 1턴 마비 부여 (어그로는 끌리지 않음)
				Buff.affect(mob, Paralysis.class, 1f);
				
				// 이펙트
				CellEmitter.get(mob.pos).burst(Speck.factory(Speck.LIGHT), 6);
			}
		}

		Sample.INSTANCE.play(Assets.Sounds.RAY);
	}

	@Override
	public String description() {
		String desc = super.description();

		desc += "\n" + Messages.get(this, "p1", this.HP,this.HT);

		return desc;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(10, 20);
	}

	@Override
	public int attackSkill( Char target ) {
		return 60;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 20);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		
		// 적을 공격하면 해당 적의 배리어를 즉시 전부 제거
		Barrier barrier = enemy.buff(Barrier.class);
		if (barrier != null) {
			barrier.detach();
		}
		
		return damage;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		
		// canCauseGameOver가 true일 때만 게임오버
		if (canCauseGameOver) {
			yell(Messages.get(this, "death"));
			Dungeon.hero.die(this);
		}
	}

	// 게임오버 없이 사망시키는 메서드
	public void dieWithoutGameOver(Object cause) {
		canCauseGameOver = false;
		die(cause);
	}

	@Override
	protected Char chooseEnemy() {
		return super.chooseEnemy();
	}

	@Override
	protected boolean getCloser(int target) {
		target = Dungeon.hero.pos;
		return super.getCloser( target );
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CAN_CAUSE_GAME_OVER, canCauseGameOver);
		bundle.put(LASER_CD, laserCooldown);
		bundle.put(LASER_WIND, laserWindup);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		canCauseGameOver = bundle.getBoolean(CAN_CAUSE_GAME_OVER);
		laserCooldown = bundle.getInt(LASER_CD);
		laserWindup = bundle.getInt(LASER_WIND);
	}

}
