/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Neoro;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdhouseBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GiantSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQteBossGame;
import com.shatteredpixel.shatteredpixeldungeon.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.watabou.utils.Bundle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class RatBeast extends Mob {

	{
		HP = HT = 2000;
		EXP = 15;
		maxLvl = 30;
		spriteClass = GiantSprite.class;
		baseSpeed = 1.25f;

		properties.add(Char.Property.BOSS);
		properties.add(Char.Property.DEMONIC);
		properties.add(Char.Property.ACIDIC);

	}

	private static final int CHASE_SOUND_CHANCE = 8;

	@Override
	public float speed() {
		float speed = super.speed();
		//keep pace with the hero's own Triplespeed - multiply the computed speed, never the
		//baseSpeed field (speed() is called many times per turn, so that compounds forever)
		if (Dungeon.hero.buff(Triplespeed.class) != null) {
			speed *= 2f;
		}
		return speed;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 30, 45 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 35;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 10);
	}

	private boolean bleeding = false;

	public void damage(int dmg, Object src) {

		if ((HP*2 <= HT) && !bleeding){
			bleeding = true;
			BossHealthBar.bleed(true);
			sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enraged"));
		}

		if (dmg >= 200) {
			//takes 20/21/22/23/24/25/26/27/28/29/30 dmg
			// at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
			dmg = 200;
		}

		super.damage(dmg, src);
	}

	public boolean chargingBarf;

	public boolean shoulddoTransition = true;

	//최종 페이즈에서 Roc(등가교환) 없이 처치를 시도했을 때 힌트를 1회만 띄우기 위한 플래그
	private boolean rocHintShown = false;

	//QTE(카운터 찬스) 상태 - 페이즈(1/2/3)별 재사용 대기 턴
	private static final int[] QTE_COOLDOWNS = {9, 6, 4};
	private int qteCooldown = QTE_COOLDOWNS[0];
	private int qteChargeStep = 0;
	public boolean qteGameActive = false;

	//used so resistances can differentiate between melee and magical attacks
	public static class BarfAcid{}

	//QTE 반격 성공 시의 피해원 - 큰 피해 감쇄를 우회한다
	public static class QteCounter{}


	@SuppressWarnings("SuspiciousIndentation")
    @Override
	public boolean act() {

		if (qteGameActive) {
			if (WndQteBossGame.instance == null) {
				showQteGame();
			}
			spend(Actor.TICK);
			return true;
		}

		if (HP <= HT*0.15f && shoulddoTransition)
		{
			GameScene.flash(0x80FFFFFF);

			shoulddoTransition = false;
			HP = (int)(HT*0.15f);

			Buff.affect(this, Barrier.class).setShield(40);

			ScrollOfTeleportation.teleportToLocation(Dungeon.hero, Dungeon.level.randomRespawnCell(Dungeon.hero));
			ScrollOfTeleportation.teleportToLocation(this, Dungeon.level.randomRespawnCell(Dungeon.hero));

			if (Dungeon.level instanceof ColdhouseBossLevel) {
				((ColdhouseBossLevel) Dungeon.level).createFinalArena();

				//a single Neoro appears dead centre of the arena as the final phase begins -
				//drinking it (Roc buff) is the only way to actually land the killing blow
				Point c = ColdhouseBossLevel.mainArena.center();
				int centerCell = c.x + c.y * Dungeon.level.width();
				Dungeon.level.drop(new Neoro(), centerCell).sprite.drop();
			}
		}

		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			GLog.n(Messages.get(this, "notice"));
			for (Char ch : Actor.chars()) {
				if (ch instanceof DriedRose.GhostHero) {
					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
		}


		if (HP * 2 > HT) {
			BossHealthBar.bleed(false);
			HP = Math.min(HP, HT);
		}


		if (state != SLEEPING) {
			Dungeon.level.seal();
		}

		//QTE(카운터 찬스) - 페이즈가 오를수록 더 자주, 더 어렵게 발동
		if (enemy != null && enemy == Dungeon.hero && !chargingBarf) {
			if (qteCooldown <= 0) {
				useQteAbility();
				return true;
			}
			qteCooldown--;
		}

		if (chargingBarf) {
			if (enemy != null) {
				spend(Actor.TICK * 2);
				Ballistica bolt = new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);

				int dist = Random.Int(2, 3);
				if (HP <= HT*0.5f) dist += 1;
                if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) dist += 1;

				ConeAOE cone = new ConeAOE(bolt,
						dist,
						80,
						Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);

				PixelScene.shake( 3, 0.2f );

				for (int cell : cone.cells) {



					if (Dungeon.level.map[cell] == Terrain.WATER) {
						Level.set(cell, Terrain.EMPTY);
						GameScene.updateMap(cell);
					}
                    else {
                        Char ch = Actor.findChar( cell );
                        if (ch != null && ch.alignment != alignment) {
                            if (enemy == Dungeon.hero) {
                                Statistics.bossScores[0] -= 50;
                            }

                            ch.damage(Random.NormalIntRange(2, 9), new BarfAcid());
                        }

                        GameScene.add(Blob.seed(cell, 8, CorrosiveGas.class).setStrength(3));
                    }
				}
			}

			chargingBarf = false;

			return true;
		}


		if (enemy != null &&
				this.distance(enemy) < 3 &&
				Random.Int(6) == 1 &&
				!chargingBarf) {
            spend(1f);
            chargingBarf = true;
            sprite.emitter().start(PoisonParticle.SPLASH, 0.1f, 10);
            return true;
        }

		for (int offset : PathFinder.NEIGHBOURS9) {
			Trap T = Dungeon.level.traps.get(pos + offset);
			if (T != null &&  Random.Int(3) == 1 && T.active) {
				if (T instanceof GeyserTrap) {
					T.reveal();
					CellEmitter.get(pos + offset).burst(Speck.factory(Speck.LIGHT), 2);
				}
				else {
					T.activate();
					T.disarm();
				}
			}
		}

		if (state == HUNTING && Random.Int(CHASE_SOUND_CHANCE) == 0 && Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play(Assets.Sounds.TG2);
		}

		return super.act();
	}

	private void overwhelmAttack() {
		// 플레이어 주변으로 순간이동
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int i : PathFinder.NEIGHBOURS8) {
			int cell = hero.pos + i;
			if (cell >= 0 && cell < Dungeon.level.length() &&
					Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
				candidates.add(cell);
			}
		}

		if (!candidates.isEmpty()) {
			int newPos = Random.element(candidates);
			ScrollOfTeleportation.appear(this, newPos);
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		}
	}

	@Override
	public void die(Object cause) {

		//final phase (after the 15% transition): the beast can only actually be put down while
		//the hero has the Roc buff - otherwise it clings to life at 1 HP
		if (!shoulddoTransition && Dungeon.hero.buff(Roc.class) == null) {
			HP = 1;
			if (sprite != null && Dungeon.level.heroFOV[pos]) {
				sprite.showStatus(CharSprite.WARNING, "죽지 않는다...");
			}
			if (!rocHintShown) {
				rocHintShown = true;
				GLog.w("LOCACACA 6251 없이는 처치할 수 없다.");
			}
			return;
		}

		super.die(cause);

		if (Dungeon.level instanceof ColdhouseBossLevel) {
			((ColdhouseBossLevel) Dungeon.level).postDeath();
		}

		Dungeon.level.unseal();

		GameScene.bossSlain();

		Camera.main.shake( 3, 1f );

		yell(Messages.get(this, "defeated"));
	}

	@Override
	public void notice() {
		super.notice();

	}

	//1: 기본, 2: 격노(HP 50% 이하), 3: 최종 전이 이후(HP 15% 이하)
	private int qtePhase() {
		if (!shoulddoTransition) return 3;
		if (HP * 2 <= HT) return 2;
		return 1;
	}

	private void useQteAbility() {
		if (qteChargeStep == 0) {
			//1턴: 카운터 찬스 예고
			GLog.h(Messages.get(this, "qte_ready"));
			sprite.showStatus(CharSprite.WARNING, Messages.get(this, "qte_warning"));

			Sample.INSTANCE.play(Assets.Sounds.MIMIC);
			SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);
			Dungeon.hero.interrupt();

			qteChargeStep = 1;
			spend(1f);
		} else {
			//2턴: 카운터 찬스 발동 - 미니게임 시작
			GLog.n(Messages.get(this, "qte_activate"));
			Sample.INSTANCE.play(Assets.Sounds.TG1);
			GameScene.flash(0x80228822);

			qteGameActive = true;
			showQteGame();

			qteChargeStep = 0;
			spend(1f);
		}
	}

	private void showQteGame() {
		final RatBeast boss = this;
		final int p = qtePhase();
		//페이즈 = 연속 입력 수 (1 -> 2 -> 3), 제한 시간도 페이즈에 따라 짧아진다
		final int seq = p;

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndQteBossGame(p, seq,
						//성공 - 거대 쥐에게 1턴 Doom + Daze (피해 없음)
						new Callback() {
							@Override
							public void call() {
								qteGameActive = false;

								Camera.main.shake(6, 0.6f);
								GameScene.flash(0x8000FF00);

								GLog.p(Messages.get(RatBeast.class, "qte_success"));
								Sample.INSTANCE.play(Assets.Sounds.PUFF);

								//피해 대신 1턴의 Doom + Daze - 다음 턴에 확정 딜을 넣을 창을 연다
								Buff.affect(boss, Doom.class).setDuration(1f);
								Buff.affect(boss, Daze.class, 1f);
								boss.sprite.showStatus(CharSprite.NEGATIVE, "휘청...");

								qteCooldown = QTE_COOLDOWNS[p - 1];
							}
						},
						//실패 - 거대 쥐 기본 공격력의 1.5배 피해 + 순간이동
						new Callback() {
							@Override
							public void call() {
								qteGameActive = false;

								GameScene.flash(0x66FF0000);
								Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);

								GLog.n(Messages.get(RatBeast.class, "qte_fail"));

								//거대 쥐 기본 공격력의 1.5배, 방어구 감쇄(drRoll) 적용
								int dmg = Math.round(damageRoll() * 1.5f);
								dmg -= Dungeon.hero.drRoll();
								Dungeon.hero.damage(Math.max(dmg, 0), boss);

								overwhelmAttack();

								if (!Dungeon.hero.isAlive()) {
									Dungeon.fail(RatBeast.class);
								}

								qteCooldown = QTE_COOLDOWNS[p - 1];
							}
						}
				));
			}
		});
	}

	{
		immunities.add(StenchGas.class);
		immunities.add(Chill.class);
	}

    public int TimesStolen = 0;

	private static final String CHARGINGBARF     = "chargingbarf";
	private static final String SHOULDDOTRANSITION   = "shoulddoTransition";

    private static final String TIMESSTOLEN     = "TimesStolen";

	private static final String QTE_COOLDOWN     = "qteCooldown";
	private static final String QTE_CHARGE_STEP  = "qteChargeStep";
	private static final String QTE_GAME_ACTIVE  = "qteGameActive";


    @Override
	public void storeInBundle(Bundle bundle) {

		bundle.put( CHARGINGBARF, chargingBarf );
		bundle.put( SHOULDDOTRANSITION, shoulddoTransition );
        bundle.put( TIMESSTOLEN, TimesStolen );
		bundle.put( QTE_COOLDOWN, qteCooldown );
		bundle.put( QTE_CHARGE_STEP, qteChargeStep );
		bundle.put( QTE_GAME_ACTIVE, qteGameActive );

        super.storeInBundle(bundle);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {

		super.restoreFromBundle(bundle);

		chargingBarf = bundle.getBoolean( CHARGINGBARF );
		shoulddoTransition = bundle.getBoolean( SHOULDDOTRANSITION );
        TimesStolen = bundle.getInt( TIMESSTOLEN );
		qteCooldown = bundle.getInt( QTE_COOLDOWN );
		qteChargeStep = bundle.getInt( QTE_CHARGE_STEP );
		qteGameActive = bundle.getBoolean( QTE_GAME_ACTIVE );

		bleeding = (HP * 2 <= HT);
		BossHealthBar.assignBoss(this);
		if (bleeding) BossHealthBar.bleed(true);


	}

}