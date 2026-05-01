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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class StringOcean extends TargetedClericSpell {

	public static final StringOcean INSTANCE = new StringOcean();

	@Override
	public int icon() {
		return HeroIcon.JOLYNE_NEW2;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero)
				&& hero.hasTalent(Talent.JOLYNE_NEW2)
				&& hero.buff(StringOceanCooldown.class) == null;
	}

	@Override
	public int targetingFlags() {
		return Ballistica.PROJECTILE;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", (int)cooldownDuration(Dungeon.hero)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {
		if (target == null) {
			return;
		}

		if (Dungeon.level.solid[target] || !Dungeon.level.visited[target]) {
			GLog.w(Messages.get(ClericSpell.class, "invalid_target"));
			return;
		}

		PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
		if (PathFinder.distance[hero.pos] == Integer.MAX_VALUE) {
			GLog.w(Messages.get(EtherealChains.class, "cant_reach"));
			return;
		}

		Ballistica chain = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);

		boolean success;
		Char ch = Actor.findChar(chain.collisionPos);
		if (ch != null && ch != hero) {
			success = chainEnemy(tome, chain, hero, ch);
		} else if (Dungeon.level.heaps.get(chain.collisionPos) != null) {
			success = chainItem(tome, chain, hero, Dungeon.level.heaps.get(chain.collisionPos));
		} else if (Dungeon.level.traps.get(chain.collisionPos) != null) {
			success = chainTrap(tome, chain, hero);
		} else {
			success = chainLocation(tome, chain, hero);
		}

		if (success) {
			Sword.stonefreeclass();
			Sample.INSTANCE.play(Assets.Sounds.MISS);
		}
	}

	private static boolean chainEnemy(HolyTome tome, Ballistica chain, final Hero hero, final Char enemy) {
		if (enemy.properties().contains(Char.Property.IMMOVABLE)) {
			GLog.w(Messages.get(EtherealChains.class, "cant_pull"));
			return false;
		}

		int bestPos = -1;
		for (int i : chain.subPath(1, chain.dist)) {
			if (!Dungeon.level.solid[i]
					&& Actor.findChar(i) == null
					&& (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[i])) {
				bestPos = i;
				break;
			}
		}

		if (bestPos == -1) {
			GLog.i(Messages.get(EtherealChains.class, "does_nothing"));
			return false;
		}

		QuickSlotButton.target(enemy);
		final int pulledPos = bestPos;

		hero.busy();
		hero.sprite.parent.add(new Chains(hero.sprite.center(), enemy.sprite.center(), Effects.Type.JOLYNE_CHAIN, new Callback() {
			@Override
			public void call() {
				Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
					@Override
					public void call() {
						enemy.pos = pulledPos;
						Dungeon.level.occupyCell(enemy);
						Dungeon.observe();
						GameScene.updateFog();
						hero.spendAndNext(Actor.TICK);
						finishCast(tome, hero);
					}
				}));
				hero.next();
			}
		}));

		return true;
	}

	private static boolean chainLocation(HolyTome tome, Ballistica chain, final Hero hero) {
		if (hero.rooted) {
			GLog.w(Messages.get(EtherealChains.class, "rooted"));
			return false;
		}

		if (Dungeon.level.solid[chain.collisionPos]
				|| !(Dungeon.level.passable[chain.collisionPos] || Dungeon.level.avoid[chain.collisionPos])
				|| Actor.findChar(chain.collisionPos) != null) {
			GLog.i(Messages.get(EtherealChains.class, "inside_wall"));
			return false;
		}

		final int newHeroPos = chain.collisionPos;

		hero.busy();
		hero.sprite.parent.add(new Chains(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(newHeroPos), Effects.Type.JOLYNE_CHAIN, new Callback() {
			@Override
			public void call() {
				Actor.add(new Pushing(hero, hero.pos, newHeroPos, new Callback() {
					@Override
					public void call() {
						hero.pos = newHeroPos;
						Dungeon.level.occupyCell(hero);
						Dungeon.observe();
						GameScene.updateFog();
						hero.spendAndNext(Actor.TICK);
						finishCast(tome, hero);
					}
				}));
				hero.next();
			}
		}));

		return true;
	}

	private static boolean chainItem(HolyTome tome, Ballistica chain, final Hero hero, final Heap heap) {
		if (heap.type != Heap.Type.HEAP) {
			GLog.i(Messages.get(StringOcean.class, "locked_item"));
			return false;
		}

		hero.busy();
		hero.sprite.parent.add(new Chains(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(chain.collisionPos), Effects.Type.JOLYNE_CHAIN, new Callback() {
			@Override
			public void call() {
				Item item = heap.peek();
				if (item != null && item.doPickUp(hero, heap.pos)) {
					heap.pickUp();
					GLog.i(Messages.capitalize(Messages.get(hero, "you_now_have", item.name())));
				} else {
					if (item != null) {
						GLog.w(Messages.capitalize(Messages.get(hero, "you_cant_have", item.name())));
					}
					if (heap.sprite != null) {
						heap.sprite.drop();
					}
					hero.spendAndNext(Actor.TICK);
				}
				finishCast(tome, hero);
			}
		}));

		return true;
	}

	private static boolean chainTrap(HolyTome tome, Ballistica chain, final Hero hero) {
		hero.busy();
		hero.sprite.parent.add(new Chains(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(chain.collisionPos), Effects.Type.JOLYNE_CHAIN, new Callback() {
			@Override
			public void call() {
				Dungeon.level.pressCell(chain.collisionPos);
				hero.spendAndNext(Actor.TICK);
				finishCast(tome, hero);
			}
		}));

		return true;
	}

	private static void finishCast(HolyTome tome, Hero hero) {
		INSTANCE.onSpellCast(tome, hero);
		FlavourBuff.affect(hero, StringOceanCooldown.class, cooldownDuration(hero));
	}

	private static float cooldownDuration(Hero hero) {
		return 120 - 30 * hero.pointsInTalent(Talent.JOLYNE_NEW2);
	}

	public static class StringOceanCooldown extends FlavourBuff {

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0.42f, 0.75f, 0.95f);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, visualcooldown() / cooldownDuration(Dungeon.hero));
		}
	}
}
