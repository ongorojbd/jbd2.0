/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bombification;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Kawasiribuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShrBomb;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

//킬러 퀸의 사격 DISC
//1) 3칸 이내의 보스가 아닌 일반 적을 지정해 폭탄화 상태를 부여한다. (충전 1 소모)
//2) 폭탄화된 적이 존재할 때 다시 시전하면 그 적이 즉시 폭발해 사망하고,
//   주변(인접 8칸)의 적과 영웅에게 Bomb과 동일한 피해를 입힌다. (충전 소모 없음)
//폭탄화는 한 번에 하나의 적만 가능하다.
public class WandOfKillerQueen extends Wand {

	{
		image = ItemSpriteSheet.WAND_KIRA;

		collisionProperties = Ballistica.PROJECTILE;
	}

	@Override
	public int initialCharges() {
		//충전량 1/1 고정
		return 1;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	//현재 층에서 폭탄화 상태인 적을 찾는다.
	public static Char findBombedChar() {
		for (Char ch : Actor.chars()) {
			if (ch.buff(Bombification.class) != null) {
				return ch;
			}
		}
		return null;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_ZAP)) {
			curUser = hero;
			curItem = this;

			Char bombed = findBombedChar();
			if (bombed != null) {
				//이미 폭탄화된 적이 있으면 대상 선택 없이 즉시 기폭
				GameScene.cancelCellSelector();
				identify();
				curUser.sprite.zap(bombed.pos);
				curUser.busy();
				detonate(bombed);
				Invisibility.dispel();
				updateQuickslot();
				curUser.spendAndNext(1f);
				Sample.INSTANCE.play(Assets.Sounds.KIRA5);
				return;
			}

			if (curCharges < chargesPerCast()) {
				GameScene.cancelCellSelector();
				GLog.w(Messages.get(Wand.class, "fizzles"));
				return;
			}

			GameScene.selectCell(targeter);
		}
	}

	private CellSelector.Listener targeter = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer target) {
			if (target == null) {
				return;
			}

			//자기 자신을 시전 대상으로 삼으면 ShrBomb을 소환한다. (Shr 참조)
			if (target == curUser.pos) {
				if (curCharges < chargesPerCast()) {
					GLog.w(Messages.get(Wand.class, "fizzles"));
					return;
				}
				if (!summonShrBomb()) {
					GLog.w(Messages.get(WandOfKillerQueen.this, "no_space"));
					return;
				}
				identify();
				curCharges -= chargesPerCast();
				Invisibility.dispel();
				updateQuickslot();
				curUser.spendAndNext(1f);
				return;
			}

			Char ch = Actor.findChar(target);

			if (ch == null || ch == curUser || ch.alignment != Char.Alignment.ENEMY) {
				GLog.w(Messages.get(WandOfKillerQueen.this, "no_target"));
				return;
			}

			if (ch.properties().contains(Char.Property.BOSS)
					|| ch.properties().contains(Char.Property.MINIBOSS)) {
				GLog.w(Messages.get(WandOfKillerQueen.this, "boss_immune"));
				return;
			}

			if (Dungeon.level.distance(curUser.pos, target) > 3
					|| !Dungeon.level.heroFOV[target]) {
				GLog.w(Messages.get(WandOfKillerQueen.this, "too_far"));
				return;
			}

			if (curCharges < chargesPerCast()) {
				GLog.w(Messages.get(Wand.class, "fizzles"));
				return;
			}

			curUser.sprite.zap(target);
			curUser.busy();

			markTarget(ch);

			Sample.INSTANCE.play(Assets.Sounds.KIRA6);

			identify();
			curCharges -= chargesPerCast();
			Invisibility.dispel();
			updateQuickslot();
			curUser.spendAndNext(1f);
		}

		@Override
		public String prompt() {
			return Messages.get(WandOfKillerQueen.class, "prompt");
		}
	};

	//Shr(사격 DISC 스펠)의 소환 로직을 참조해, 영웅 인접의 빈 칸에 ShrBomb을 소환한다.
	private boolean summonShrBomb() {
		int spawnCell = -1;
		for (int i : PathFinder.NEIGHBOURS8) {
			int c = curUser.pos + i;
			if (!Dungeon.level.solid[c] && !Dungeon.level.avoid[c] && Actor.findChar(c) == null) {
				spawnCell = c;
				break;
			}
		}
		if (spawnCell == -1) {
			return false;
		}

		Sample.INSTANCE.play(Assets.Sounds.SHEER);

		ShrBomb bomb = new ShrBomb();
		bomb.pos = spawnCell;
		bomb.state = bomb.HUNTING;
		GameScene.add(bomb);
		bomb.beckon(Dungeon.hero.pos);

		curUser.busy();
		curUser.sprite.zap(spawnCell);

		GLog.i(Messages.get(this, "shr_summon"));
		return true;
	}

	private void markTarget(Char ch) {
		Buff.affect(ch, Bombification.class);
		if (ch.sprite != null) {
			ch.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT), 6);
		}
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		GLog.i(Messages.get(this, "marked", ch.name()));
	}

	//Bomb.explode() 와 동일한 피해 계산을 사용하되, 지형/아이템은 파괴하지 않는다.
	private void detonate(Char bombed) {
		int cell = bombed.pos;

		Bombification b = bombed.buff(Bombification.class);
		if (b != null) {
			b.detach();
		}

		Sample.INSTANCE.play(Assets.Sounds.BLAST);
		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
		}

		//폭탄화된 적은 폭발과 함께 즉시 사망한다.
		bombed.damage(bombed.HP + bombed.drRoll() + 1000, this);
		if (bombed.isAlive()) {
			bombed.die(this);
		}

		boolean[] explodable = new boolean[Dungeon.level.length()];
		BArray.not(Dungeon.level.solid, explodable);
		BArray.or(Dungeon.level.flamable, explodable, explodable);
		PathFinder.buildDistanceMap(cell, explodable, 1);

		ArrayList<Char> affected = new ArrayList<>();
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] != Integer.MAX_VALUE) {
				if (Dungeon.level.heroFOV[i]) {
					CellEmitter.get(i).burst(SmokeParticle.FACTORY, 4);
				}
				Char ch = Actor.findChar(i);
				if (ch != null && ch != bombed) {
					affected.add(ch);
				}
			}
		}

		for (Char ch : affected) {
			if (!ch.isAlive()) {
				continue;
			}

			int dmg = Random.NormalIntRange(4 + Dungeon.scalingDepth(), 12 + 3 * Dungeon.scalingDepth());

			if (Dungeon.hero.buff(Kawasiribuff.class) != null) {
				dmg = dmg * 3 / 2;
			}

			if (ch instanceof Hero && ((Hero) ch).belongings.getItem(Jojo2.class) != null) {
				dmg = 0;
			}

			dmg -= ch.drRoll();

			if (dmg > 0) {
				ch.damage(dmg, this);
			}

			if (ch == Dungeon.hero && !ch.isAlive()) {
				Badges.validateDeathFromFriendlyMagic();
				GLog.n(Messages.get(Bomb.class, "ondeath"));
				Dungeon.fail(this);
			}
		}
	}

	@Override
	public void onZap(Ballistica bolt) {
		//마도 지팡이에 장착돼 시전될 때의 처리
		Char bombed = findBombedChar();
		if (bombed != null) {
			detonate(bombed);
			return;
		}

		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null && ch != curUser && ch.alignment == Char.Alignment.ENEMY
				&& !ch.properties().contains(Char.Property.BOSS)
				&& !ch.properties().contains(Char.Property.MINIBOSS)) {
			markTarget(ch);
		} else {
			GLog.w(Messages.get(this, "no_target"));
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//적중 시 폭탄화 부여 (한 번에 하나만)
		if (findBombedChar() == null
				&& defender.alignment == Char.Alignment.ENEMY
				&& !defender.properties().contains(Char.Property.BOSS)
				&& !defender.properties().contains(Char.Property.MINIBOSS)) {
			markTarget(defender);
		}
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.FIRE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0xFF3322);
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.speed.polar(Random.Float(PointF.PI2), 2f);
		particle.setSize(1f, 2f);
		particle.radiateXY(0.5f);
	}

	@Override
	public String statsDesc() {
		int depth = Dungeon.hero == null ? 1 : Dungeon.scalingDepth();
		int shrMin = Math.round((4 + depth) * 1.75f);
		int shrMax = Math.round((12 + 3 * depth) * 1.75f);
		return Messages.get(this, "stats_desc", 4 + depth, 12 + 3 * depth, shrMin, shrMax);
	}
}
