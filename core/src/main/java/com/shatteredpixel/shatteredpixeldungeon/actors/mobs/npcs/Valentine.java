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
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.services.rankings.Ranking;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class Valentine extends NPC {

	{
		spriteClass = PucciSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	@Override
	protected boolean act() {
		if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
			die(null);
			return true;
		}
		return super.act();
	}

	@Override
	public void beckon(int cell) {
		//do nothing
	}

	@Override
	protected boolean getCloser(int target) {
		return true;
	}

	@Override
	protected boolean getFurther(int target) {
		return true;
	}

	@Override
	public boolean add(Buff buff) {
		return false;
	}

	@Override
	public int defenseSkill(Char enemy) {
		return INFINITE_EVASION;
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean interact(Char c) {
		sprite.turnTo(pos, Dungeon.hero.pos);

		if (c != Dungeon.hero) {
			return true;
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				Ranking.checkForMonthlyRankings();
				if (!Ranking.monthlyRankingsAvailable()) {
					GameScene.show(new WndOptions(
							sprite(),
							Messages.titleCase(name()),
							Messages.get(Valentine.class, "loading"),
							Messages.get(Valentine.class, "leave")
					));
					return;
				}

				GameScene.show(new WndOptions(
						sprite(),
						Messages.titleCase(name()),
						Messages.get(Valentine.class, "intro"),
						Messages.get(Valentine.class, "enter"),
						Messages.get(Valentine.class, "leave")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							enterArena();
							die(null);
						}
					}
				});
			}
		});

		return true;
	}

	private void enterArena() {
		Ranking.checkForMonthlyRankings();
		if (!Ranking.monthlyRankingsAvailable()) {
			yell(Messages.get(Valentine.class, "no_monthly_rankings"));
			return;
		}

		InterlevelScene.mode = InterlevelScene.Mode.RETURN;
		InterlevelScene.returnDepth = 31;
		InterlevelScene.returnBranch = 1;
		InterlevelScene.returnPos = -2;
		Game.switchScene(InterlevelScene.class);
	}

}
