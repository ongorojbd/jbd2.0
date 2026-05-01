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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.TendencyMap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTendencyMap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.RectF;

public class TendencyMapScene extends PixelScene {

	private static final int NODE_W = 34;
	private static final int NODE_H = 20;
	private static final int CURRENT_W = 58;
	private static final int ROW_H = 30;
	private static final int TOP_PAD = 24;
	private static final int BOTTOM_PAD = 80;
	private static final int MAP_WIDTH = 170;

	public static LevelTransition curTransition;

	private int segmentStart;
	private int segmentEnd;

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		TendencyMap.init();

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();

		add(new ColorBlock(w, h, 0xFF151812));

		IconTitle title = new IconTitle(Icons.STAIRS.get(), Messages.get(WndTendencyMap.class, "title"));
		title.setSize(200, 0);
		title.setPos(insets.left + (w - insets.left - insets.right - title.reqWidth()) / 2f, insets.top + 4);
		align(title);
		add(title);

		RenderedTextBlock desc = renderTextBlock(Messages.get(WndTendencyMap.class, "desc", Dungeon.depth, curTransition == null ? Dungeon.depth + 1 : curTransition.destDepth), 6);
		desc.hardlight(0xCCCCCC);
		desc.maxWidth(Math.min(220, w - (int)insets.left - (int)insets.right - 10));
		desc.setPos(insets.left + (w - insets.left - insets.right - desc.width()) / 2f, title.bottom() + 3);
		align(desc);
		add(desc);

		RenderedTextBlock legend = renderTextBlock(Messages.get(WndTendencyMap.class, "legend"), 5);
		legend.hardlight(0xAAAFA4);
		legend.maxWidth(Math.min(230, w - (int)insets.left - (int)insets.right - 10));
		legend.setPos(insets.left + (w - insets.left - insets.right - legend.width()) / 2f, desc.bottom() + 3);
		align(legend);
		add(legend);

		float listTop = legend.bottom() + 5;
		float listHeight = h - listTop - insets.bottom - 4;
		int mapWidth = Math.min(MAP_WIDTH, w - (int)insets.left - (int)insets.right - 8);
		int targetDepth = curTransition == null ? Dungeon.depth + 1 : curTransition.destDepth;
		segmentEnd = ((targetDepth + 8) / 9) * 9;
		segmentEnd = Math.min(TendencyMap.MAX_DEPTH, Math.max(9, segmentEnd));
		segmentStart = segmentEnd == 9 ? 2 : segmentEnd - 8;
		int contentHeight = (segmentEnd - segmentStart) * ROW_H + TOP_PAD + BOTTOM_PAD;

		ScrollPane pane = new ScrollPane(new Component());
		add(pane);
		pane.setRect(insets.left + (w - insets.left - insets.right - mapWidth) / 2f, listTop, mapWidth, listHeight);

		Component content = pane.content();
		content.clear();
		content.setSize(mapWidth, contentHeight);

		if (Dungeon.depth < segmentStart) {
			addCurrentMarker(content, mapWidth);
		}

		for (int depth = segmentStart; depth < segmentEnd; depth++) {
			addLinks(content, depth, mapWidth);
		}

		addCurrentLinks(content, mapWidth);

		for (int depth = segmentStart; depth <= segmentEnd; depth++) {
			int count = sceneCount(depth);
			for (int node = 0; node < count; node++) {
				NodeButton btn = new NodeButton(depth, node);
				btn.setRect(nodeX(node, count, mapWidth) - NODE_W / 2f, nodeY(depth) - NODE_H / 2f, NODE_W, NODE_H);
				content.add(btn);
			}
		}

		addBossLabel(content, mapWidth);

		float focusY = Dungeon.depth < segmentStart
				? (nodeY(targetDepth) + currentY()) / 2f - listHeight / 2f
				: nodeY(targetDepth) - listHeight / 2f;
		pane.scrollTo(0, focusY);

		fadeIn();
	}

	private void addLinks(Component content, int depth, int mapWidth) {
		int count = sceneCount(depth);
		int nextCount = sceneCount(depth + 1);
		if (count == 0 || nextCount == 0) return;

		for (int node = 0; node < count; node++) {
			int links = sceneLinks(depth, node, count, nextCount);
			for (int next = 0; next < nextCount; next++) {
				if ((links & (1 << next)) == 0) continue;

				float x1 = nodeX(node, count, mapWidth);
				float y1 = nodeY(depth) - NODE_H / 2f;
				float x2 = nodeX(next, nextCount, mapWidth);
				float y2 = nodeY(depth + 1) + NODE_H / 2f;
				float dx = x2 - x1;
				float dy = y2 - y1;
				float len = (float)Math.sqrt(dx * dx + dy * dy);

				boolean active = depth == Dungeon.depth && node == Statistics.tendencyMapPath && selectableNode(depth + 1, next);
				ColorBlock line = new ColorBlock(len, active ? 2 : 1, active ? 0xFFB7E36A : 0xFF59604F);
				line.am = active ? 0.85f : 0.28f;
				line.x = x1;
				line.y = y1;
				line.angle = (float)(Math.atan2(dy, dx) * 180 / Math.PI);
				content.add(line);
			}
		}
	}

	private void addCurrentMarker(Component content, int mapWidth) {
		CurrentMarker current = new CurrentMarker();
		current.setRect((mapWidth - CURRENT_W) / 2f, currentY() - NODE_H / 2f, CURRENT_W, NODE_H);
		content.add(current);
	}

	private void addCurrentLinks(Component content, int mapWidth) {
		if (Dungeon.depth >= segmentStart) return;
		int nextDepth = curTransition == null ? Dungeon.depth + 1 : curTransition.destDepth;
		int count = sceneCount(nextDepth);
		int mask = (1 << count) - 1;
		for (int node = 0; node < count; node++) {
			if ((mask & (1 << node)) == 0) continue;
			float x1 = mapWidth / 2f;
			float y1 = currentY() - NODE_H / 2f;
			float x2 = nodeX(node, count, mapWidth);
			float y2 = nodeY(nextDepth) + NODE_H / 2f;
			float dx = x2 - x1;
			float dy = y2 - y1;
			float len = (float)Math.sqrt(dx * dx + dy * dy);
			ColorBlock line = new ColorBlock(len, 2, 0xFFB7E36A);
			line.am = 0.85f;
			line.x = x1;
			line.y = y1;
			line.angle = (float)(Math.atan2(dy, dx) * 180 / Math.PI);
			content.add(line);
		}
	}

	private void addBossLabel(Component content, int mapWidth) {
		RenderedTextBlock boss = PixelScene.renderTextBlock(Messages.get(WndTendencyMap.class, "node_boss"), 7);
		boss.hardlight(0xFFFF6666);
		boss.setPos((mapWidth - boss.width()) / 2f, Math.max(0, nodeY(segmentEnd) - NODE_H / 2f - boss.height() - 2));
		content.add(boss);
	}

	private static float nodeX(int node, int count, int width) {
		if (count == 1) return width / 2f;
		float left = NODE_W / 2f + 8;
		float right = width - NODE_W / 2f - 8;
		return left + (right - left) * node / (count - 1);
	}

	private static int sceneCount(int depth) {
		return TendencyMap.count(depth);
	}

	private static int sceneType(int depth, int node) {
		return TendencyMap.type(depth, node);
	}

	private static int sceneLinks(int depth, int node, int count, int nextCount) {
		return TendencyMap.links(depth, node);
	}

	private static boolean selectableNode(int depth, int node) {
		if (curTransition == null || depth != curTransition.destDepth) return false;
		return TendencyMap.selectable(depth, node);
	}

	private static float nodeY(int depth) {
		int end = ((depth + 8) / 9) * 9;
		end = Math.min(TendencyMap.MAX_DEPTH, Math.max(9, end));
		return TOP_PAD + (end - depth) * ROW_H;
	}

	private static float currentY() {
		return TOP_PAD + 8 * ROW_H + BOTTOM_PAD / 2f;
	}

	@Override
	protected void onBackPressed() {
	}

	private static class NodeButton extends Button {

		private final int depth;
		private final int node;
		private Image icon;
		private RenderedTextBlock label;
		private ColorBlock bg;

		private NodeButton(int depth, int node) {
			this.depth = depth;
			this.node = node;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			bg = new ColorBlock(1, 1, 0xFF5A3030);
			add(bg);
			icon = new Image(Icons.SKULL.get());
			add(icon);
			label = PixelScene.renderTextBlock("", 5);
			add(label);
		}

		@Override
		protected void layout() {
			super.layout();
			bg.x = x;
			bg.y = y;
			bg.color(color());
			bg.size(width, height);
			bg.am = selectable() || current() ? 0.95f : 0.45f;

			icon.copy(icon());
			icon.x = x + 2;
			icon.y = y + (height - icon.height()) / 2f;
			align(icon);

			label.text(labelText());
			label.hardlight(textColor());
			label.maxWidth((int)(width - 18));
			label.setPos(x + 18, y + (height - label.height()) / 2f);
			align(label);
		}

		@Override
		protected void onClick() {
			if (!selectable()) return;
			Statistics.tendencyMapNode = sceneType(depth, node);
			Statistics.tendencyMapPath = node;

			Level.beforeTransition();
			InterlevelScene.curTransition = curTransition;
			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene(InterlevelScene.class);
		}

		private boolean current() {
			return depth == Dungeon.depth && node == Statistics.tendencyMapPath;
		}

		private boolean selectable() {
			return selectableNode(depth, node);
		}

		private int color() {
			if (sceneType(depth, node) == TendencyMap.BOSS) return 0xFF8A2020;
			if (current()) return 0xFF496C9A;
			if (selectable()) return 0xFF4F6D32;
			switch (sceneType(depth, node)) {
				case TendencyMap.ELITE:
					return 0xFF6A3434;
				case TendencyMap.SHOP:
					return 0xFF6A5B2E;
				case TendencyMap.TREASURE:
					return 0xFF72592A;
				case TendencyMap.EVENT:
					return 0xFF40475C;
				case TendencyMap.REST:
					return 0xFF315B48;
				case TendencyMap.COMBAT:
				default:
					return 0xFF5E5E5E;
			}
		}

		private int textColor() {
			return selectable() || current() ? Window.TITLE_COLOR : 0xFFB8B8B8;
		}

		private String labelText() {
			return Messages.get(WndTendencyMap.class, "node_" + TendencyMap.key(sceneType(depth, node)));
		}

		private Image icon() {
			switch (sceneType(depth, node)) {
				case TendencyMap.BOSS:
                    return Icons.NEWS.get();
				case TendencyMap.ELITE:
					return Icons.SKULL.get();
				case TendencyMap.SHOP:
					return Icons.GOLD.get();
				case TendencyMap.TREASURE:
					return Icons.TALENT.get();
				case TendencyMap.EVENT:
					return Icons.INVESTIGATE.get();
				case TendencyMap.REST:
					return Icons.WELL_HEALTH.get();
				case TendencyMap.COMBAT:
				default:
					return Icons.SKULL.get();
			}
		}
	}

	private static class CurrentMarker extends Button {

		private ColorBlock bg;
		private RenderedTextBlock label;

		@Override
		protected void createChildren() {
			super.createChildren();
			bg = new ColorBlock(1, 1, 0xFF3E5D8A);
			add(bg);
			label = PixelScene.renderTextBlock(Messages.get(WndTendencyMap.class, "current"), 6);
			label.hardlight(Window.TITLE_COLOR);
			add(label);
		}

		@Override
		protected void layout() {
			super.layout();
			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			bg.am = 0.95f;
			label.setPos(x + (width - label.width()) / 2f, y + (height - label.height()) / 2f);
			align(label);
		}
	}
}
