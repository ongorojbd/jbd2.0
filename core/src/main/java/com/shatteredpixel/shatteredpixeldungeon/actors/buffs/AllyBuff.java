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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.levels.ArenaLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Zombie.spwPrize;

//generic class for buffs which convert an enemy into an ally
// There is a decent amount of logic that ties into this, which is why it has its own abstract class
public abstract class AllyBuff extends Buff {

	{
		revivePersists = true;
	}

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)){
			target.alignment = Char.Alignment.ALLY;
			if (target.buff(PinCushion.class) != null){
				target.buff(PinCushion.class).detach();
			}
			return true;
		} else {
			return false;
		}
	}

	//for when applying an ally buff should also cause that enemy to give exp/loot as if they had died
	//consider that chars with the ally alignment do not drop items or award exp on death
	public static void affectAndLoot(Mob enemy, Hero hero, Class<?extends AllyBuff> buffCls){
		boolean wasEnemy = enemy.alignment == Char.Alignment.ENEMY || enemy instanceof Mimic;
		Buff.affect(enemy, buffCls);

		if (enemy.buff(buffCls) != null && wasEnemy){
			enemy.rollToDropLoot();

			Statistics.enemiesSlain++;
			Badges.validateMonstersSlain();
			Statistics.qualifiedForNoKilling = false;
			Bestiary.setSeen(enemy.getClass());
			Bestiary.countEncounter(enemy.getClass());

			AscensionChallenge.processEnemyKill(enemy);

			int exp = hero.lvl <= enemy.maxLvl ? enemy.EXP : 0;
			if (exp > 0) {
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(exp), FloatingText.EXPERIENCE);
			}
			hero.earnExp(exp, enemy.getClass());

			if (hero.subClass == HeroSubClass.MONK){
				Buff.affect(hero, MonkEnergy.class).gainEnergy(enemy);
			}

			// If in arena/tendency mode, trigger clear-reward/key when no enemies remain after conversion
			if (Dungeon.tendencylevel && Dungeon.level instanceof ArenaLevel) {
				boolean mobsAlive = false;
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.isAlive() && mob.alignment == Char.Alignment.ENEMY) {
						mobsAlive = true;
						break;
					}
				}
				if (!mobsAlive && Dungeon.level.entrance == 0 && !Dungeon.level.combatRewardDropped) {
					Dungeon.level.combatRewardDropped = true;
					spwPrize(enemy.pos);
					Dungeon.level.drop(new SkeletonKey(Dungeon.depth), enemy.pos).sprite.drop();
					Dungeon.level.unseal();
					Sample.INSTANCE.play(Assets.Sounds.CHARMS, 1f, 2f);
                    if (Statistics.spw24 > 0) {
                        int chance = Math.min(100, 10 + (Statistics.spw24 * 15));
                        if (Random.Int(100) < chance) {
							Dungeon.level.drop(new Spw().identify(), enemy.pos).sprite.drop(enemy.pos);
							new Flare(5, 32).color(0xFFFF00, true).show(Dungeon.hero.sprite, 2f);
						}
					}
				}
			}
		}
	}

}
