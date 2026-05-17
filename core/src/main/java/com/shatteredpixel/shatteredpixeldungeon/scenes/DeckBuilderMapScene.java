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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderMap;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderRun;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTendencyMap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.RectF;

import java.io.IOException;

public class DeckBuilderMapScene extends PixelScene {

	private static final int NODE_W = 38;
	private static final int NODE_H = 22;
	private static final int CURRENT_W = 58;
	private static final int ROW_H = 31;
	private static final int TOP_PAD = 24;
	private static final int BOTTOM_PAD = 80;
	private static final int MAP_WIDTH = 300;

	public static LevelTransition curTransition;

	private int segmentStart;
	private int segmentEnd;

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		DeckBuilderRun.initIfNeeded();
		if (!DeckBuilderRun.startingRelicChosen) {
			Game.switchScene(DeckRelicChoiceScene.class);
			return;
		}
		ensureTransition();
		DeckBuilderMap.ensureReadyForNextChoice();
		saveMapState();

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();

		add(new ColorBlock(w, h, 0xFF11150F));
		addRunHud(insets);
		addExitButton(insets, w);

		IconTitle title = new IconTitle(Icons.STAIRS.get(), "덱빌딩 경로");
		title.setSize(200, 0);
		title.setPos(insets.left + (w - insets.left - insets.right - title.reqWidth()) / 2f, insets.top + 4);
		align(title);
		add(title);

		RenderedTextBlock desc = renderTextBlock("다음 층으로 향할 경로를 선택합니다.", 6);
		desc.hardlight(0xCCCCCC);
		desc.maxWidth(Math.min(220, w - (int)insets.left - (int)insets.right - 10));
		desc.setPos(insets.left + (w - insets.left - insets.right - desc.width()) / 2f, title.bottom() + 3);
		align(desc);
		add(desc);

		RenderedTextBlock legend = renderTextBlock("전투 / 엘리트 / 보상 / 휴식 / 이벤트", 5);
		legend.hardlight(0xAAAFA4);
		legend.maxWidth(Math.min(230, w - (int)insets.left - (int)insets.right - 10));
		legend.setPos(insets.left + (w - insets.left - insets.right - legend.width()) / 2f, desc.bottom() + 3);
		align(legend);
		add(legend);

		float listTop = legend.bottom() + 5;
		float listHeight = h - listTop - insets.bottom - 4;
		int mapWidth = Math.min(MAP_WIDTH, w - (int)insets.left - (int)insets.right - 14);
		int targetDepth = targetDepth();
		segmentStart = DeckBuilderMap.FIRST_DEPTH;
		segmentEnd = DeckBuilderMap.MAX_DEPTH;
		int contentHeight = (segmentEnd - segmentStart) * ROW_H + TOP_PAD + BOTTOM_PAD;

		SmoothMapScrollPane pane = new SmoothMapScrollPane(new Component());
		add(pane);
		pane.setRect(insets.left + (w - insets.left - insets.right - mapWidth) / 2f, listTop, mapWidth, listHeight);
		MapScrollBar scrollBar = new MapScrollBar(pane, contentHeight);
		scrollBar.setRect(pane.right() + 2, listTop, 4, listHeight);
		add(scrollBar);

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
				if (sceneType(depth, node) == DeckBuilderMap.NONE) continue;
				NodeButton btn = new NodeButton(depth, node);
				btn.setRect(nodeX(node, count, mapWidth) - NODE_W / 2f, nodeY(depth) - NODE_H / 2f, NODE_W, NODE_H);
				content.add(btn);
			}
		}

		addBossLabel(content, mapWidth);

		float focusY = Dungeon.depth < segmentStart
				? (nodeY(targetDepth) + currentY()) / 2f - listHeight / 2f
				: nodeY(Math.min(DeckBuilderMap.MAX_DEPTH, targetDepth)) - listHeight / 2f;
		pane.jumpTo(0, focusY);

		fadeIn();
	}

	private void saveMapState() {
		try {
			Dungeon.saveAll();
		} catch (IOException e) {
			Game.reportException(e);
		}
	}

	private static void ensureTransition() {
		if (curTransition != null || Dungeon.level == null) {
			return;
		}

		curTransition = Dungeon.level.getTransition(LevelTransition.Type.REGULAR_EXIT);
		if (curTransition == null) {
			int cell = Dungeon.hero == null ? Dungeon.level.entrance() : Dungeon.hero.pos;
			curTransition = new LevelTransition(
					Dungeon.level,
					cell,
					LevelTransition.Type.REGULAR_EXIT,
					targetDepth(),
					Dungeon.branch,
					LevelTransition.Type.REGULAR_ENTRANCE);
		}
	}

	private void addRelicButton(RectF insets) {
		IconButton relicButton = new IconButton(Icons.BACKPACK_LRG.get()) {
			@Override
			protected void onClick() {
				addToFront(new WndMessage("유물\n\n" + DeckBuilderRun.relicListText()));
			}
		};
		relicButton.setRect(insets.left + 4, insets.top + 4, 20, 20);
		add(relicButton);
	}

	private void addRunHud(RectF insets) {
		DeckRunHud hud = new DeckRunHud(null);
		hud.setRect(insets.left + 4, insets.top + 4, 150, 20);
		add(hud);
	}

	private void addExitButton(RectF insets, int w) {
		IconButton exit = new IconButton(Icons.EXIT.get()) {
			@Override
			protected void onClick() {
				saveMapState();
				Game.switchScene(TitleScene.class);
			}
		};
		exit.setRect(w - insets.right - 24, insets.top + 4, 20, 20);
		add(exit);
	}

	private void addLinks(Component content, int depth, int mapWidth) {
		int count = sceneCount(depth);
		int nextCount = sceneCount(depth + 1);
		if (count == 0 || nextCount == 0) return;

		for (int node = 0; node < count; node++) {
			int links = sceneLinks(depth, node, count, nextCount);
			if (sceneType(depth, node) == DeckBuilderMap.NONE) continue;
			for (int next = 0; next < nextCount; next++) {
				if ((links & (1 << next)) == 0) continue;
				if (sceneType(depth + 1, next) == DeckBuilderMap.NONE) continue;

				float x1 = nodeX(node, count, mapWidth);
				float y1 = nodeY(depth) - NODE_H / 2f;
				float x2 = nodeX(next, nextCount, mapWidth);
				float y2 = nodeY(depth + 1) + NODE_H / 2f;
				float dx = x2 - x1;
				float dy = y2 - y1;
				float len = (float)Math.sqrt(dx * dx + dy * dy);

				boolean active = depth == Dungeon.depth && node == Statistics.deckBuilderMapPath && selectableNode(depth + 1, next);
				ColorBlock line = new ColorBlock(len, active ? 2 : 1, active ? 0xFFD5F27A : 0xFF4E5848);
				line.am = active ? 0.92f : 0.34f;
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
		int nextDepth = targetDepth();
		int count = sceneCount(nextDepth);
		int mask = (1 << count) - 1;
		for (int node = 0; node < count; node++) {
			if ((mask & (1 << node)) == 0) continue;
			if (sceneType(nextDepth, node) == DeckBuilderMap.NONE) continue;
			float x1 = mapWidth / 2f;
			float y1 = currentY() - NODE_H / 2f;
			float x2 = nodeX(node, count, mapWidth);
			float y2 = nodeY(nextDepth) + NODE_H / 2f;
			float dx = x2 - x1;
			float dy = y2 - y1;
			float len = (float)Math.sqrt(dx * dx + dy * dy);
			ColorBlock line = new ColorBlock(len, 2, 0xFFD5F27A);
			line.am = 0.92f;
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
		return DeckBuilderMap.count(depth);
	}

	private static int sceneType(int depth, int node) {
		return DeckBuilderMap.type(depth, node);
	}

	private static int sceneLinks(int depth, int node, int count, int nextCount) {
		return DeckBuilderMap.links(depth, node);
	}

	private static boolean selectableNode(int depth, int node) {
		if (depth != targetDepth()) return false;
		return DeckBuilderMap.selectable(depth, node);
	}

	private static int targetDepth() {
		if (curTransition != null) {
			return Math.max(DeckBuilderMap.FIRST_DEPTH, Math.min(DeckBuilderMap.MAX_DEPTH, curTransition.destDepth));
		}
		if (Dungeon.depth < DeckBuilderMap.FIRST_DEPTH) {
			return DeckBuilderMap.FIRST_DEPTH;
		}
		return Math.min(DeckBuilderMap.MAX_DEPTH, Dungeon.depth + 1);
	}

	private static float nodeY(int depth) {
		return TOP_PAD + (DeckBuilderMap.MAX_DEPTH - depth) * ROW_H;
	}

	private static float currentY() {
		return nodeY(DeckBuilderMap.FIRST_DEPTH) + ROW_H;
	}

	@Override
	protected void onBackPressed() {
	}

	private static class NodeButton extends Button {

		private final int depth;
		private final int node;
		private Image icon;
		private RenderedTextBlock label;
		private ColorBlock shadow;
		private ColorBlock bg;
		private ColorBlock accent;

		private NodeButton(int depth, int node) {
			this.depth = depth;
			this.node = node;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			shadow = new ColorBlock(1, 1, 0xFF000000);
			shadow.am = 0.38f;
			add(shadow);
			bg = new ColorBlock(1, 1, 0xFF30362F);
			add(bg);
			accent = new ColorBlock(1, 1, 0xFFFFFFFF);
			accent.am = 0.92f;
			add(accent);
			icon = new Image(Icons.SKULL.get());
			add(icon);
			label = PixelScene.renderTextBlock("", 5);
			add(label);
		}

		@Override
		protected void layout() {
			super.layout();
			shadow.x = x + 2;
			shadow.y = y + 2;
			shadow.size(width, height);
			bg.x = x;
			bg.y = y;
			bg.color(color());
			bg.size(width, height);
			bg.am = selectable() || current() ? 0.98f : 0.58f;

			accent.x = x;
			accent.y = y;
			accent.color(accentColor());
			accent.size(3, height);
			accent.visible = current();

			icon.copy(icon());
			icon.x = x + 5;
			icon.y = y + (height - icon.height()) / 2f;
			align(icon);

			label.text(labelText());
			label.hardlight(textColor());
			label.maxWidth((int)(width - 21));
			label.setPos(x + 21, y + (height - label.height()) / 2f);
			align(label);
		}

		@Override
		protected void onClick() {
			if (!selectable()) return;
			Statistics.deckBuilderMapNode = sceneType(depth, node);
			Statistics.deckBuilderMapPath = node;

			if (deckBattleNode()) {
				enterDeckBattle();
				Game.switchScene(DeckBattleScene.class);
				return;
			}

			Level.beforeTransition();
			InterlevelScene.curTransition = curTransition;
			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene(InterlevelScene.class);
		}

		private boolean current() {
			return depth == Dungeon.depth && node == Statistics.deckBuilderMapPath;
		}

		private boolean selectable() {
			return selectableNode(depth, node);
		}

		private boolean deckBattleNode() {
			int type = sceneType(depth, node);
			return type == DeckBuilderMap.COMBAT || type == DeckBuilderMap.ELITE || type == DeckBuilderMap.BOSS;
		}

		private void enterDeckBattle() {
			try {
				Level.beforeTransition();
				Dungeon.saveAll();

				Dungeon.depth = depth;
				Dungeon.branch = curTransition == null ? Dungeon.branch : curTransition.destBranch;

				Level level = Dungeon.levelHasBeenGenerated(Dungeon.depth, Dungeon.branch)
						? Dungeon.loadLevel(com.shatteredpixel.shatteredpixeldungeon.GamesInProgress.curSlot)
						: Dungeon.newLevel();
				Dungeon.switchLevel(level, -1);
				Dungeon.saveAll();
			} catch (IOException e) {
				Game.reportException(e);
			}
		}

		private int color() {
			if (current()) return 0xFF3E5F7A;
			if (selectable()) return 0xFF456830;
			switch (sceneType(depth, node)) {
				case DeckBuilderMap.ELITE:
					return 0xFF5C3030;
				case DeckBuilderMap.SHOP:
					return 0xFF5D522E;
				case DeckBuilderMap.TREASURE:
					return 0xFF665328;
				case DeckBuilderMap.EVENT:
					return 0xFF394357;
				case DeckBuilderMap.REST:
					return 0xFF294D3E;
				case DeckBuilderMap.BOSS:
					return 0xFF7A2528;
				case DeckBuilderMap.COMBAT:
				default:
					return 0xFF343A33;
			}
		}

		private int accentColor() {
			if (current()) return 0xFF9EC8FF;
			switch (sceneType(depth, node)) {
				case DeckBuilderMap.ELITE:
				case DeckBuilderMap.BOSS:
					return 0xFFFF7474;
				case DeckBuilderMap.SHOP:
				case DeckBuilderMap.TREASURE:
					return 0xFFFFD15E;
				case DeckBuilderMap.EVENT:
					return 0xFF9FB6FF;
				case DeckBuilderMap.REST:
					return 0xFF8EE0B6;
				case DeckBuilderMap.COMBAT:
				default:
					return 0xFFD5F27A;
			}
		}

		private int textColor() {
			return selectable() || current() ? Window.TITLE_COLOR : 0xFFB8B8B8;
		}

		private String labelText() {
			return Messages.get(WndTendencyMap.class, "node_" + DeckBuilderMap.key(sceneType(depth, node)));
		}

		private Image icon() {
			switch (sceneType(depth, node)) {
				case DeckBuilderMap.BOSS:
                    return Icons.NEWS.get();
				case DeckBuilderMap.ELITE:
					return Icons.SKULL.get();
				case DeckBuilderMap.SHOP:
					return Icons.GOLD.get();
				case DeckBuilderMap.TREASURE:
					return Icons.TALENT.get();
				case DeckBuilderMap.EVENT:
					return Icons.INVESTIGATE.get();
				case DeckBuilderMap.REST:
					return Icons.WELL_HEALTH.get();
				case DeckBuilderMap.COMBAT:
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

	private static class SmoothMapScrollPane extends ScrollPane {

		private float targetX;
		private float targetY;
		private float lastX;
		private float lastY;

		private SmoothMapScrollPane(Component content) {
			super(content);
		}

		@Override
		public void scrollTo(float x, float y) {
			targetX = clampX(x);
			targetY = clampY(y);
		}

		private void jumpTo(float x, float y) {
			targetX = clampX(x);
			targetY = clampY(y);
			super.scrollTo(targetX, targetY);
			lastX = content.camera.scroll.x;
			lastY = content.camera.scroll.y;
		}

		@Override
		public synchronized void update() {
			super.update();
			if (content.camera == null) return;

			float currentX = content.camera.scroll.x;
			float currentY = content.camera.scroll.y;
			if (Math.abs(currentX - lastX) > 1f || Math.abs(currentY - lastY) > 1f) {
				targetX = clampX(currentX);
				targetY = clampY(currentY);
			}

			float nextX = currentX + (targetX - currentX) * Math.min(1f, Game.elapsed * 14f);
			float nextY = currentY + (targetY - currentY) * Math.min(1f, Game.elapsed * 14f);
			super.scrollTo(nextX, nextY);
			lastX = content.camera.scroll.x;
			lastY = content.camera.scroll.y;
		}

		private float clampX(float value) {
			return Math.max(0, Math.min(value, Math.max(0, content.width() - width())));
		}

		private float clampY(float value) {
			return Math.max(0, Math.min(value, Math.max(0, content.height() - height())));
		}
	}

	private static class MapScrollBar extends Component {

		private final ScrollPane pane;
		private final float contentHeight;
		private ColorBlock track;
		private ColorBlock thumb;

		private MapScrollBar(ScrollPane pane, float contentHeight) {
			this.pane = pane;
			this.contentHeight = contentHeight;
		}

		@Override
		protected void createChildren() {
			track = new ColorBlock(1, 1, 0xFF2D352A);
			track.am = 0.9f;
			add(track);
			thumb = new ColorBlock(1, 1, 0xFFB7E36A);
			thumb.am = 0.95f;
			add(thumb);
		}

		@Override
		protected void layout() {
			track.x = x;
			track.y = y;
			track.size(width, height);
			updateThumb();
		}

		@Override
		public synchronized void update() {
			super.update();
			updateThumb();
		}

		private void updateThumb() {
			if (pane == null || pane.content() == null || contentHeight <= pane.height()) {
				visible = false;
				return;
			}
			visible = true;
			float thumbHeight = Math.max(14, height * height / contentHeight);
			float maxScroll = Math.max(1, contentHeight - pane.height());
			float scroll = pane.content().camera == null ? 0 : pane.content().camera.scroll.y;
			float thumbY = y + (height - thumbHeight) * scroll / maxScroll;
			thumb.x = x;
			thumb.y = thumbY;
			thumb.size(width, thumbHeight);
		}
	}
}
