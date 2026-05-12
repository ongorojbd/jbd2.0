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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DailyChampion;
import com.watabou.noosa.TextureFilm;

public class DailyChampionSprite extends MobSprite {

	private static final int FRAME_WIDTH = 12;
	private static final int FRAME_HEIGHT = 15;

	private HeroClass heroClass = HeroClass.WARRIOR;
	private int armorTier = 0;

	public DailyChampionSprite() {
		super();
		updateSprite();
	}

	public DailyChampionSprite(HeroClass heroClass, int armorTier) {
		super();
		this.heroClass = heroClass == null ? HeroClass.WARRIOR : heroClass;
		this.armorTier = Math.max(0, Math.min(6, armorTier));
		updateSprite();
	}

	@Override
	public void linkVisuals(Char ch) {
		super.linkVisuals(ch);
		if (ch instanceof DailyChampion) {
			DailyChampion champion = (DailyChampion)ch;
			heroClass = champion.heroClass();
			armorTier = Math.max(0, Math.min(6, champion.armorTier()));
			updateSprite();
		}
	}

	private void updateSprite() {
		texture(heroClass.spritesheet());

		// Same frame mapping HeroSprite uses: choose the hero class sheet, then slice the armor-tier row.
		TextureFilm film = new TextureFilm(HeroSprite.tiers(), armorTier, FRAME_WIDTH, FRAME_HEIGHT);

		idle = new Animation(1, true);
		idle.frames(film, 0, 0, 0, 1, 0, 0, 1, 1);

		run = new Animation(20, true);
		run.frames(film, 2, 3, 4, 5, 6, 7);

		die = new Animation(20, false);
		die.frames(film, 8, 9, 10, 11, 12, 11);

		attack = new Animation(15, false);
		attack.frames(film, 13, 14, 15, 0);

		zap = attack.clone();

		play(idle);
	}
}
