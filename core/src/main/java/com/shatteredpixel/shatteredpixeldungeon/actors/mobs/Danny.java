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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DannySprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Danny extends Mob {

	{
		spriteClass = DannySprite.class;
		HP = HT = 3 + (Dungeon.depth) * 3;
		viewDistance = 5;
		defenseSkill = Dungeon.depth;
		EXP = 0;

		state = WANDERING;
		alignment = Alignment.ALLY;
		intelligentAlly = true;
	}

	@Override
	public int attackSkill(Char target) {
		return 99;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(Dungeon.depth / 3, Dungeon.depth / 2);
	}

	@Override
	protected boolean act() {
		boolean result = super.act();
		Dungeon.level.updateFieldOfView(this, fieldOfView);
		GameScene.updateFog(pos, viewDistance + (int)Math.ceil(speed()));
		return result;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);

		// Award 10 gold to the hero on each successful hit
		if (Dungeon.hero != null) {
			int amount = 10;
			Dungeon.gold += amount;
			Statistics.goldCollected += amount;
			Badges.validateGoldCollected();
			Dungeon.hero.sprite.showStatusWithIcon(CharSprite.NEUTRAL, Integer.toString(amount), FloatingText.GOLD);
			Sample.INSTANCE.play(Assets.Sounds.GOLD, 1, 1, Random.Float(0.9f, 1.1f));
		}

		if (enemy instanceof Mob) {
			((Mob)enemy).aggro( this );
		}

		return damage;
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		Dungeon.level.drop( new ChargrilledMeat(), pos ).sprite.drop( pos );

		Sample.INSTANCE.play(Assets.Sounds.BURNING);

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

}


