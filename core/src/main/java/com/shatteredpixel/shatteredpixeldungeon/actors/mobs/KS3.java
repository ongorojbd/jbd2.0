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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
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
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/**
 * KS3 - 카즈의 정예병 (기마병)
 * 빠른 이동속도와 도약 공격을 가진 기동형 유닛
 * 적을 밀쳐낸 후 도약 공격을 사용
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

		HUNTING = new KS3.Hunting();
	}

	private int leapPos = -1;
	private float leapCooldown = 0; // 초기값 0으로 설정하여 평소에도 사용 가능
	private int lastEnemyPos = -1;

	private static final String LEAP_POS = "leap_pos";
	private static final String LEAP_CD = "leap_cd";
	private static final String LAST_ENEMY_POS = "last_enemy_pos";

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
		// 쿨다운 감소
		if (leapCooldown > 0) leapCooldown--;
		return super.act();
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);

		// 1/3 확률로 적을 2칸 밀쳐내기
		if (Random.Int(3) == 0 && enemy.isAlive()) {
			boolean pushed = pushEnemy(enemy);
			if (pushed && enemy.isAlive()) {
				// 밀쳐내기 성공 시 leap 사용
				sprite.showStatus(CharSprite.WARNING, Messages.get(KS3.class, "s1"));
				triggerLeap(enemy);
				Sample.INSTANCE.play(Assets.Sounds.HORSE);
			}
		}

		return damage;
	}

	private boolean pushEnemy(Char enemy) {
		if (enemy == null) return false;

		int direction = enemy.pos - pos;
		Ballistica trajectory = new Ballistica(enemy.pos, enemy.pos + direction, Ballistica.MAGIC_BOLT);
		int knockbackDist = 2;

		int destination = enemy.pos;
		for (int i = 0; i < knockbackDist && trajectory.path.size() > i + 1; i++) {
			int next = trajectory.path.get(i + 1);
			if (Dungeon.level.solid[next] || Actor.findChar(next) != null) {
				// 벽이나 장애물에 부딪힘
				break;
			}
			destination = next;
		}

		// 밀쳐내기 실행
		if (destination != enemy.pos) {
			Actor.add(new Pushing(enemy, enemy.pos, destination));
			enemy.pos = destination;
			Dungeon.level.occupyCell(enemy);
			return true;
		}
		return false;
	}

	private void triggerLeap(Char target) {
		if (target == null || leapCooldown > 0) return;

		int targetPos = target.pos;
		Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
		if (b.collisionPos == targetPos) {
			leapPos = targetPos;
			lastEnemyPos = target.pos;
			
			// 도약 목적지 표시
			if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]) {
				sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF00FF));
				Dungeon.hero.interrupt();
			}
		}
	}

	public class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			if (leapPos != -1){
				leapCooldown = Random.NormalIntRange(4, 8);

				sprite.showStatus(CharSprite.WARNING, Messages.get(KS3.class, "leap"));
				Sample.INSTANCE.play(Assets.Sounds.HORSE);
				Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

				//check if leap pos is not obstructed by terrain
				if (rooted || b.collisionPos != leapPos){
					leapPos = -1;
					return true;
				}

				final Char leapVictim = Actor.findChar(leapPos);
				final int endPos;

				//ensure there is somewhere to land after leaping
				if (leapVictim != null){
					int bouncepos = -1;
					for (int i : PathFinder.NEIGHBOURS8){
						if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
								&& Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
							bouncepos = leapPos+i;
						}
					}
					if (bouncepos == -1) {
						leapPos = -1;
						return true;
					} else {
						endPos = bouncepos;
					}
				} else {
					endPos = leapPos;
				}

				//do leap
				sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos] || Dungeon.level.heroFOV[endPos];
				sprite.jump(pos, leapPos, new Callback() {
					@Override
					public void call() {

						if (leapVictim != null && alignment != leapVictim.alignment){
							Buff.affect(leapVictim, Bleeding.class).set(0.6f*damageRoll());
							leapVictim.sprite.flash();
							Sample.INSTANCE.play(Assets.Sounds.HIT);
						}

						if (endPos != leapPos){
							Actor.addDelayed(new Pushing(KS3.this, leapPos, endPos), -1);
						}

						pos = endPos;
						leapPos = -1;
						sprite.idle();
						Dungeon.level.occupyCell(KS3.this);
						next();
					}
				});
				return false;
			}

			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				} else if (enemy == null) {
					state = WANDERING;
					target = Dungeon.level.randomDestination( KS3.this );
					return true;
				}

				if (leapCooldown <= 0 && enemyInFOV && !rooted
						&& Dungeon.level.distance(pos, enemy.pos) >= 3) {

					int targetPos = enemy.pos;
					if (lastEnemyPos != enemy.pos){
						int closestIdx = 0;
						for (int i = 1; i < PathFinder.CIRCLE8.length; i++){
							if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos)
									< Dungeon.level.trueDistance(lastEnemyPos, enemy.pos)){
								closestIdx = i;
							}
						}
						targetPos = enemy.pos;
					}

					Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
					//try aiming directly at hero if aiming near them doesn't work
					if (b.collisionPos != targetPos && targetPos != enemy.pos){
						targetPos = enemy.pos;
						b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
					}
					if (b.collisionPos == targetPos){
						//get ready to leap
						leapPos = targetPos;
						//don't want to overly punish players with slow move or attack speed
						spend(GameMath.gate(TICK, enemy.cooldown(), 3*TICK));
						if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){

							sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF00FF));
							Dungeon.hero.interrupt();
						}
						return true;
					}
				}

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {

					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {
					spend( TICK );
					if (!enemyInFOV) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination( KS3.this );
					}
					return true;
				}
			}
		}

	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEAP_POS, leapPos);
		bundle.put(LEAP_CD, leapCooldown);
		bundle.put(LAST_ENEMY_POS, lastEnemyPos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		leapPos = bundle.getInt(LEAP_POS);
		leapCooldown = bundle.getFloat(LEAP_CD);
		lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
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

