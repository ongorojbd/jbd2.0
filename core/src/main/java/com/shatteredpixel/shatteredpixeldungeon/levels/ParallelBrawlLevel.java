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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DailyChampion;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.ParallelBrawlEscapeCrystal;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.services.rankings.DailyRankingEntry;
import com.shatteredpixel.shatteredpixeldungeon.services.rankings.Ranking;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Comparator;

public class ParallelBrawlLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	private static final int WIDTH = 31;
	private static final int HEIGHT = 31;
	private static final int CHAMPION_POS = 15 + WIDTH * 10;

	private ArrayList<DailyRankingEntry> challengers = new ArrayList<>();
	private int wave = 0;
	private int challengerIndex = 0;
	private boolean started = false;

	private static final String CHALLENGER_NAMES = "challenger_names";
	private static final String CHALLENGER_SCORES = "challenger_scores";
	private static final String CHALLENGER_DATES = "challenger_dates";
	private static final String CHALLENGER_DEPTHS = "challenger_depths";
	private static final String CHALLENGER_LEVELS = "challenger_levels";
	private static final String CHALLENGER_CLASSES = "challenger_classes";
	private static final String CHALLENGER_ARMOR_TIERS = "challenger_armor_tiers";
	private static final String CHALLENGER_WINS = "challenger_wins";
	private static final String CHALLENGER_ASCENDING = "challenger_ascending";
	private static final String CHALLENGER_GAME_DATA = "challenger_game_data";
	private static final String WAVE = "wave";
	private static final String CHALLENGER_INDEX = "challenger_index";
	private static final String STARTED = "started";

	@Override
	public void playLevelMusic() {
		Music.INSTANCE.playTracks(new String[]{Assets.Music.YUUKI}, new float[]{1}, false);
	}

	@Override
	public String tilesTex() {
	return Assets.Environment.TILES_SHIP;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_SEWERS;
	}

	@Override
	protected boolean build() {
		setSize(WIDTH, HEIGHT);
		transitions.add(new LevelTransition(this, 15 + WIDTH * 15, LevelTransition.Type.REGULAR_ENTRANCE));
		buildLevel();
		prepareChallengers();
		return true;
	}

	private static final short W = Terrain.WALL;
	private static final short e = Terrain.EMPTY;
	private static final short h = Terrain.EMPTY_SP;
	private static final short S = Terrain.STATUE_SP;
	private static final short L = Terrain.LOCKED_EXIT;
	private static final short E = Terrain.ENTRANCE_SP;
	private static final short d = Terrain.WALL_DECO;
	private static final short w = Terrain.EMPTY_DECO;

	private static final short[] level = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, h, h, h, h, S, h, h, h, h, h, S, h, h, h, h, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, h, h, e, e, e, w, e, h, h, h, e, w, e, e, e, h, h, W, W, W, W, W, W, W,
			W, W, W, W, W, W, h, h, e, e, e, e, e, e, h, h, h, e, e, e, e, e, e, h, h, W, W, W, W, W, W,
			W, W, W, W, W, h, h, e, e, e, e, e, e, e, h, h, h, e, e, e, e, e, e, e, h, h, W, W, W, W, W,
			W, W, W, W, h, h, e, e, e, e, e, e, w, e, h, h, h, e, w, e, e, e, e, e, e, h, h, W, W, W, W,
			W, W, W, W, h, e, e, e, e, e, e, e, e, e, h, h, h, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
			W, W, W, W, h, e, e, e, e, e, e, e, e, e, h, h, h, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
			W, W, W, W, h, e, e, e, e, e, e, e, w, e, h, h, h, e, w, e, e, e, e, e, e, e, h, W, W, W, W,
			W, W, W, W, S, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, S, W, W, W, W,
			W, W, W, W, h, e, e, e, e, e, e, e, h, h, h, h, h, h, h, e, e, e, e, e, e, e, h, W, W, W, W,
			W, W, W, W, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, W, W, W, W,
			W, W, W, W, h, h, h, h, h, h, h, h, h, h, h, E, h, h, h, h, h, h, h, h, h, h, h, W, W, W, W,
			W, W, W, W, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, h, W, W, W, W,
			W, W, W, W, h, e, e, e, e, e, e, e, h, h, h, h, h, h, h, e, e, e, e, e, e, e, h, W, W, W, W,
			W, W, W, W, S, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, S, W, W, W, W,
			W, W, W, W, h, e, e, e, e, e, e, e, w, e, h, h, h, e, w, e, e, e, e, e, e, e, h, W, W, W, W,
			W, W, W, W, h, e, e, e, e, e, e, e, e, e, h, h, h, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
			W, W, W, W, h, h, e, e, e, e, e, e, e, e, h, h, h, e, e, e, e, e, e, e, e, h, h, W, W, W, W,
			W, W, W, W, W, h, h, e, e, e, e, e, w, e, h, h, h, e, w, e, e, e, e, e, h, h, W, W, W, W, W,
			W, W, W, W, W, W, h, h, e, e, e, e, e, e, h, h, h, e, e, e, e, e, e, h, h, W, W, W, W, W, W,
			W, W, W, W, W, W, W, h, h, e, e, e, e, e, h, h, h, e, e, e, e, e, h, h, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, h, h, e, e, w, e, h, h, h, e, w, e, e, h, h, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, h, h, h, S, h, h, h, h, h, S, h, h, h, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
	};

	private void buildLevel() {
		for (int i = 0; i < level.length; i++) {
			map[i] = level[i];
		}
	}

	private void prepareChallengers() {
		challengers = new ArrayList<>(Ranking.monthlyTopRankings());
		challengers.sort(new Comparator<DailyRankingEntry>() {
			@Override
			public int compare(DailyRankingEntry a, DailyRankingEntry b) {
				String aDate = a.date == null ? "" : a.date;
				String bDate = b.date == null ? "" : b.date;
				return bDate.compareTo(aDate);
			}
		});
	}

	@Override
	protected void createMobs() {
	}

	@Override
	protected void createItems() {
		drop(new ParallelBrawlEscapeCrystal(), entrance());
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		String[] names = new String[challengers.size()];
		int[] scores = new int[challengers.size()];
		String[] dates = new String[challengers.size()];
		int[] depths = new int[challengers.size()];
		int[] levels = new int[challengers.size()];
		String[] classes = new String[challengers.size()];
		int[] armorTiers = new int[challengers.size()];
		boolean[] wins = new boolean[challengers.size()];
		boolean[] ascending = new boolean[challengers.size()];
		String[] gameData = new String[challengers.size()];
		for (int i = 0; i < challengers.size(); i++) {
			DailyRankingEntry entry = challengers.get(i);
			names[i] = entry.playerName;
			scores[i] = entry.score;
			dates[i] = entry.date;
			depths[i] = entry.depth;
			levels[i] = entry.level;
			classes[i] = entry.heroClass;
			armorTiers[i] = entry.armorTier;
			wins[i] = entry.win;
			ascending[i] = entry.ascending;
			gameData[i] = entry.gameData;
		}
		bundle.put(CHALLENGER_NAMES, names);
		bundle.put(CHALLENGER_SCORES, scores);
		bundle.put(CHALLENGER_DATES, dates);
		bundle.put(CHALLENGER_DEPTHS, depths);
		bundle.put(CHALLENGER_LEVELS, levels);
		bundle.put(CHALLENGER_CLASSES, classes);
		bundle.put(CHALLENGER_ARMOR_TIERS, armorTiers);
		bundle.put(CHALLENGER_WINS, wins);
		bundle.put(CHALLENGER_ASCENDING, ascending);
		bundle.put(CHALLENGER_GAME_DATA, gameData);
		bundle.put(WAVE, wave);
		bundle.put(CHALLENGER_INDEX, challengerIndex);
		bundle.put(STARTED, started);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		challengers = new ArrayList<>();
		String[] names = bundle.getStringArray(CHALLENGER_NAMES);
		int[] scores = bundle.getIntArray(CHALLENGER_SCORES);
		String[] dates = bundle.getStringArray(CHALLENGER_DATES);
		int[] depths = bundle.getIntArray(CHALLENGER_DEPTHS);
		int[] levels = bundle.getIntArray(CHALLENGER_LEVELS);
		String[] classes = bundle.getStringArray(CHALLENGER_CLASSES);
		int[] armorTiers = bundle.getIntArray(CHALLENGER_ARMOR_TIERS);
		boolean[] wins = bundle.getBooleanArray(CHALLENGER_WINS);
		boolean[] ascending = bundle.getBooleanArray(CHALLENGER_ASCENDING);
		String[] gameData = bundle.getStringArray(CHALLENGER_GAME_DATA);
		for (int i = 0; i < names.length; i++) {
			DailyRankingEntry entry = new DailyRankingEntry();
			entry.rank = 1;
			entry.playerName = names[i];
			entry.score = i < scores.length ? scores[i] : 0;
			entry.date = i < dates.length ? dates[i] : "";
			entry.depth = i < depths.length ? depths[i] : 0;
			entry.level = i < levels.length ? levels[i] : 0;
			entry.heroClass = i < classes.length ? classes[i] : "";
			entry.armorTier = i < armorTiers.length ? armorTiers[i] : 0;
			entry.win = i < wins.length && wins[i];
			entry.ascending = i < ascending.length && ascending[i];
			entry.gameData = i < gameData.length ? gameData[i] : null;
			challengers.add(entry);
		}
		wave = bundle.getInt(WAVE);
		challengerIndex = bundle.contains(CHALLENGER_INDEX) ? bundle.getInt(CHALLENGER_INDEX) : wave;
		started = bundle.getBoolean(STARTED);
		if (locked) {
			unseal();
		}
	}

	@Override
	public void occupyCell(Char ch) {
		super.occupyCell(ch);
		if (ch == hero && !started) {
			started = true;
			spawnNextChampion();
		}
	}

	private void spawnNextChampion() {
		DailyChampion champion = null;
		if (challengers.isEmpty()) {
			finishBrawl(false);
			return;
		}

		int attempts = 0;
		while (champion == null && attempts < challengers.size()) {
			DailyRankingEntry entry = challengers.get(challengerIndex % challengers.size());
			challengerIndex++;
			champion = DailyChampion.createFromRankingEntry(entry, wave);
			attempts++;
		}

		if (champion == null) {
			finishBrawl(false);
			return;
		}

		wave++;
		champion.pos = CHAMPION_POS;
		champion.state = champion.HUNTING;
		GameScene.add(champion);
		BossHealthBar.assignBoss(champion);
		champion.beckon(Dungeon.hero.pos);

		if (heroFOV[champion.pos]) {
			champion.notice();
			champion.sprite.alpha(0);
			champion.sprite.parent.add(new AlphaTweener(champion.sprite, 1, 0.1f));
		}

		String weaponName = champion.weapon == null ? Messages.get(ParallelBrawlLevel.class, "unknown_equipment") : champion.weapon.name();
		String armorName = champion.armor == null ? Messages.get(ParallelBrawlLevel.class, "unknown_equipment") : champion.armor.name();
		showMessage(Messages.get(ParallelBrawlLevel.class, "wave", wave, champion.name(), champion.sourceDateText(), champion.sourceScore(), weaponName, armorName));
		Dungeon.observe();
	}

	public void onChampionDefeated(int pos) {
		spawnNextChampion();
	}

	private void finishBrawl(boolean won) {
		unseal();
		set(15 + WIDTH * 3, Terrain.UNLOCKED_EXIT);
		transitions.add(new LevelTransition(this, 15 + WIDTH * 3, LevelTransition.Type.REGULAR_EXIT));
		GameScene.updateMap(15 + WIDTH * 3);
		if (won) {
			showMessage(Messages.get(ParallelBrawlLevel.class, "clear"));
		} else {
			showMessage(Messages.get(ParallelBrawlLevel.class, "empty"));
		}
		Dungeon.observe();
	}

	private void showMessage(final String message) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				if (Dungeon.level == ParallelBrawlLevel.this) {
					GameScene.show(new WndMessage(message));
				}
			}
		});
	}

	@Override
	public int randomRespawnCell(Char ch) {
		int cell;
		do {
			cell = entrance() + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public boolean activateTransition(Hero hero, LevelTransition transition) {

		if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					GameScene.show(new WndMessage(Messages.get(hero, "tendency2")));
				}
			});
			return false;
		} else {
			return super.activateTransition(hero, transition);
		}
	}

	public static void returnToDepth31(Hero hero) {
		if (hero != null) {
			hero.HP = Math.max(1, hero.HP);
			ParallelBrawlEscapeCrystal rope = hero.belongings.getItem(ParallelBrawlEscapeCrystal.class);
			if (rope != null) {
				rope.detachAll(hero.belongings.backpack);
			}
		}
		Level.beforeTransition();
		InterlevelScene.mode = InterlevelScene.Mode.RETURN;
		InterlevelScene.returnDepth = 31;
		InterlevelScene.returnBranch = 0;
		InterlevelScene.returnPos = -1;
		Game.switchScene(InterlevelScene.class);
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		addArenaVisuals(this, visuals);
		return visuals;
	}

	public static void addArenaVisuals(Level level, Group group) {
		for (int i = 0; i < level.length(); i++) {
			if (level.map[i] == Terrain.EMPTY_DECO) {
				group.add(new Torch(i));
			}
		}
	}

	public static class Torch extends Emitter {

		private int pos;

		public Torch(int pos) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld(pos);
			pos(p.x - 1, p.y + 2, 2, 0);

			pour(FlameParticle.FACTORY, 0.15f);

			add(new Halo(12, 0xFFFFCC, 0.4f).point(p.x, p.y + 1));
		}

		@Override
		public void update() {
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				super.update();
			}
		}
	}
}
