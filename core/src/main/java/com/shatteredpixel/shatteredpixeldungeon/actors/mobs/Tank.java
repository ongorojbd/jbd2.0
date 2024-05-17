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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TankSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Tank extends Mob {

	{
		spriteClass = TankSprite.class;

		HP = HT = 190;
		defenseSkill = 15;
		baseSpeed = 3f;

		EXP = 15;
		maxLvl = 30;

		loot = new ShockingBrew();
		lootChance = 0.15f;
	}
	int damageTaken = 0;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 35, 50 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 40;
	}

	@Override
	public int drRoll() { return Random.NormalIntRange(0, 5);}

	@Override
	public int attackProc( Char hero, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (hero == Dungeon.hero) {
			if (Random.Int(3) == 0) {
				Invisibility.dispel(this);
				Sample.INSTANCE.play(Assets.Sounds.ORA);
				Ballistica trajectory = new Ballistica(this.pos, enemy.pos, Ballistica.STOP_TARGET);
				//trim it to just be the part that goes past them
				trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
				//knock them back along that ballistica
				WandOfBlastWave.throwChar(enemy, trajectory, 6, false, true, getClass());
				return damage;
			}
		}

		return damage;
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {
		damage = super.defenseProc( enemy, damage );
		damageTaken += damage;
		if (damageTaken >= 100) {
			Buff.prolong(this, Invisibility.class, Invisibility.DURATION*5000f);
			Sample.INSTANCE.play(Assets.Sounds.PLATINUM);
			damageTaken = 0;
		}
		return damage;
	}

	@Override
	protected boolean act() {


		if (this.HP < 1*this.HT/2) {
			Buff.prolong(this, Invisibility.class, Invisibility.DURATION*5000f);
		}
		return super.act();
	}

	@Override
	public float lootChance() {
		return super.lootChance() * ((10f - Dungeon.LimitedDrops.NECRO_HP.count) / 10f);
	}

	@Override
	public Item createLoot(){
		Dungeon.LimitedDrops.NECRO_HP.count++;
		return super.createLoot();
	}

}