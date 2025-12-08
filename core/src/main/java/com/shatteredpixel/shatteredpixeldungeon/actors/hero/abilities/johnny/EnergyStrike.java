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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.johnny;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LightParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class EnergyStrike extends ArmorAbility {

	{
		baseChargeUse = 50f;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target == null) {
			return;
		}

		hero.busy();

		armor.charge -= chargeUse(hero);
		Item.updateQuickslot();

		Invisibility.dispel(hero);

		// Create visual effects for 3x3 area with TargetedCell
		for (int i : PathFinder.NEIGHBOURS9) {
			int cell = target + i;
			if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
				hero.sprite.parent.addToBack(new TargetedCell(cell, 0xFF00FF));
			}
		}

		Sample.INSTANCE.play(Assets.Sounds.BURNING);

		// Create delayed damage buff
		EnergyStrikeBuff buff = Buff.affect(hero, EnergyStrikeBuff.class, 3f);
		buff.setTargetArea(target);

		hero.spendAndNext(1f);
	}

	@Override
	public int icon() {
		return HeroIcon.NONE;
	}

	@Override
	public Talent[] talents() {
		return new Talent[0];
	}

	public static class EnergyStrikeBuff extends FlavourBuff {

		private int centerCell = -1;
		private ArrayList<Integer> affectedCells;

		{
			type = buffType.NEUTRAL;
		}

		public void setTargetArea(int center) {
			centerCell = center;
			affectedCells = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS9) {
				int cell = center + i;
				if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
					affectedCells.add(cell);
				}
			}
		}

		@Override
		public boolean act() {
			// After 3 turns, deal damage
			if (centerCell != -1 && affectedCells != null) {
				Invisibility.dispel(Dungeon.hero);

				Hero hero = Dungeon.hero;
				
				for (int cell : affectedCells) {
					if (cell < 0 || cell >= Dungeon.level.length() || Dungeon.level.solid[cell]) {
						continue;
					}

					// Visual effects
					// 경로에 빛 파티클 추가
					CellEmitter.center(cell).burst(LightParticle.BURST, 8);
					
					hero.sprite.parent.add(new Beam.SuperNovaRay(
						hero.sprite.center(),
						DungeonTilemap.raisedTileCenterToWorld(cell),
						2
					));
					CellEmitter.center(cell).burst(SmokeParticle.FACTORY, 4);

					Char target = Actor.findChar(cell);
					if (target != null) {
						// Wake up sleeping enemies
						if (target instanceof Mob) {
							Mob mob = (Mob) target;
							if (mob.state == mob.SLEEPING) {
								mob.beckon(hero.pos);
								// If hero is visible, set to HUNTING
								if (mob.fieldOfView[hero.pos] && hero.invisible == 0) {
									mob.state = mob.HUNTING;
								}
							}
						}
						
						// Deal damage to enemies
						if (target.alignment != hero.alignment) {
							int dmg;
							if (target.properties().contains(Char.Property.BOSS)) {
								// Boss: 40~60 damage
								dmg = Random.NormalIntRange(40, 60);
							} else {
								// Normal enemy: 80% of max HP
								dmg = Math.round(target.HT * 0.8f);
							}
							target.damage(dmg, new EnergyBolt());
						}
						
						// Also deal damage to hero if they're in the area
						if (target == hero) {
							int heroDmg = Random.NormalIntRange(hero.HT / 8, hero.HT / 6);
							hero.damage(heroDmg, new EnergyBolt());
						}
					}
				}

				Sample.INSTANCE.play(Assets.Sounds.BURNING);
			}

			detach();
			return super.act();
		}

		private static final String CENTER = "center";
		private static final String CELLS = "cells";

		@Override
		public void storeInBundle(com.watabou.utils.Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CENTER, centerCell);
			if (affectedCells != null) {
				int[] cellsArray = new int[affectedCells.size()];
				for (int i = 0; i < affectedCells.size(); i++) {
					cellsArray[i] = affectedCells.get(i);
				}
				bundle.put(CELLS, cellsArray);
			}
		}

		@Override
		public void restoreFromBundle(com.watabou.utils.Bundle bundle) {
			super.restoreFromBundle(bundle);
			centerCell = bundle.getInt(CENTER);
			if (bundle.contains(CELLS)) {
				int[] cellsArray = bundle.getIntArray(CELLS);
				affectedCells = new ArrayList<>();
				for (int cell : cellsArray) {
					affectedCells.add(cell);
				}
			}
		}
	}

	public static class EnergyBolt {
	}

}

