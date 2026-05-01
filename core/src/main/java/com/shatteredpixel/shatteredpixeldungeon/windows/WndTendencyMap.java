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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.TendencyMap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class WndTendencyMap extends WndOptions {

	private static int constructingDepth;

	private final LevelTransition transition;
	private final int[] choices;

	public WndTendencyMap(LevelTransition transition) {
		super(
				Icons.get(Icons.STAIRS),
				Messages.get(WndTendencyMap.class, "title"),
				message(transition.destDepth),
				optionsForConstructor(transition.destDepth));
		this.transition = transition;
		this.choices = TendencyMap.choicesForDepth(transition.destDepth);
	}

	@Override
	protected void onSelect(int index) {
		Statistics.tendencyMapNode = choices[index];
		Statistics.tendencyMapPath = index;

		Level.beforeTransition();
		InterlevelScene.curTransition = transition;
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		Game.switchScene(InterlevelScene.class);
	}

	@Override
	protected boolean hasIcon(int index) {
		return true;
	}

	@Override
	protected Image getIcon(int index) {
		int[] iconChoices = choices == null ? TendencyMap.choicesForDepth(constructingDepth) : choices;
		switch (iconChoices[index]) {
			case TendencyMap.BOSS:
			case TendencyMap.ELITE:
				return Icons.get(Icons.SKULL);
			case TendencyMap.SHOP:
				return Icons.get(Icons.GOLD);
			case TendencyMap.TREASURE:
				return Icons.get(Icons.TALENT);
			case TendencyMap.EVENT:
				return Icons.get(Icons.INVESTIGATE);
			case TendencyMap.REST:
				return Icons.get(Icons.WELL_HEALTH);
			case TendencyMap.COMBAT:
			default:
				return Icons.get(Icons.CHALLENGE_COLOR);
		}
	}

	private static String message(int depth) {
		int[] choices = TendencyMap.choicesForDepth(depth);
		StringBuilder map = new StringBuilder();
		map.append(Messages.get(WndTendencyMap.class, "desc", Dungeon.depth, depth));
		map.append("\n\n");
		for (int i = 0; i < choices.length; i++) {
			if (i > 0) map.append("   ");
			map.append(i + 1).append(": ");
			map.append(Messages.get(WndTendencyMap.class, "node_" + TendencyMap.key(choices[i])));
		}
		return map.toString();
	}

	private static String[] options(int depth) {
		int[] choices = TendencyMap.choicesForDepth(depth);
		String[] options = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			options[i] = Messages.get(WndTendencyMap.class, "choose_" + TendencyMap.key(choices[i]));
		}
		return options;
	}

	private static String[] optionsForConstructor(int depth) {
		constructingDepth = depth;
		return options(depth);
	}
}
