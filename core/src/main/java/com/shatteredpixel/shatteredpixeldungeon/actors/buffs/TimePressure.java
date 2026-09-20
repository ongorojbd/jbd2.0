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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

//while this is active the hero only gets a limited amount of REAL time to act on each of their
//turns. If they run out of time they are forced to wait, letting everything else act instead.
public class TimePressure extends FlavourBuff {

	public static final float DURATION = 50f;

	//seconds of real time the hero gets for each of their turns
	public static final float DEFAULT_LIMIT = 8f;

	private float secondsPerTurn = DEFAULT_LIMIT;

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	public TimePressure set( float secondsPerTurn ){
		this.secondsPerTurn = secondsPerTurn;
		GameScene.resetTurnTimer();
		return this;
	}

	public float secondsPerTurn(){
		return secondsPerTurn;
	}

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)){
			GameScene.resetTurnTimer();
			return true;
		}
		return false;
	}

	@Override
	public void detach() {
		super.detach();
		GameScene.resetTurnTimer();
	}

	//how many seconds the hero currently gets per turn, 0 if they aren't under any time pressure
	public static float heroTimeLimit(){
		if (Dungeon.hero == null) return 0;
		TimePressure pressure = Dungeon.hero.buff(TimePressure.class);
		return pressure == null ? 0 : pressure.secondsPerTurn;
	}

	@Override
	public int icon() {
		return BuffIndicator.TIME;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.25f, 0.25f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", Messages.decimalFormat("#.##", secondsPerTurn), dispTurns());
	}

	private static final String SECONDS_PER_TURN = "seconds_per_turn";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SECONDS_PER_TURN, secondsPerTurn);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(SECONDS_PER_TURN)) secondsPerTurn = bundle.getFloat(SECONDS_PER_TURN);
	}
}
