/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roc;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BanditSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BansheeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SupressionSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Banshee extends Mob {

	//체력이 0이 되어도 이 확률로 죽지 않고 아래 체력으로 부활한다
	private static final float REVIVE_CHANCE = 0.8f;
	private static final int REVIVE_HP = 30;

	//플레이어가 Roc 버프 상태면 이 확률로 부활이 봉쇄되어 그대로 사망한다
	private static final float ROC_REVIVE_SUPPRESS_CHANCE = 0.8f;

	private int regenCounter = 0;
	private static final String REGEN_COUNTER = "regen_counter";

	{
		spriteClass = BansheeSprite.class;

		HP = HT = 150;
		defenseSkill = 15;

		EXP = 15;
		maxLvl = 30;

		baseSpeed = 1.5f;

	}

	@Override
	protected boolean act() {
		if (HP < HT) {
			if (Dungeon.level.heroFOV[pos] ){
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
			regenCounter++;
			if (regenCounter >= 1) {
				regenCounter = 0;
				HP = HP + 5;
			}
		}
		return super.act();
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		damage += enemy.HT / 8;
		return damage;
	}

	@Override
	public void die(Object cause) {
		boolean rocSuppressed = Dungeon.hero != null
				&& Dungeon.hero.buff(Roc.class) != null
				&& Random.Float() < ROC_REVIVE_SUPPRESS_CHANCE;

		if (!rocSuppressed && Random.Float() < REVIVE_CHANCE) {
			HP = REVIVE_HP;
			if (Dungeon.level.heroFOV[pos]) {
				SpellSprite.show(this, SpellSprite.BERSERK);
				CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 8);
			}
			spend(1f);
			Buff.affect(this, Roots.class, 2f);
			return;
		} else {
			Sample.INSTANCE.play(Assets.Sounds.TG1);
		}

		if (Random.Int( 3 ) == 0) {
			Dungeon.level.drop( new Gold().quantity(300), pos ).sprite.drop( pos );
		}

		Dungeon.level.drop( new StewedMeat(), pos ).sprite.drop( pos );

		super.die(cause);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(REGEN_COUNTER, regenCounter);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		regenCounter = bundle.getInt(REGEN_COUNTER);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 15, 35 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 35;
	}

    @Override
    public int drRoll() {
        return Random.NormalIntRange(5, 10);
    }



}
