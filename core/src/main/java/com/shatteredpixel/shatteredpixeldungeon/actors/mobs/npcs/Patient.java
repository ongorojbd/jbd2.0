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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.HospitalLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LisaSprite; //placeholder sprite, swap out later
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

/**
 * The escorted NPC riding on the Ambulance in HospitalLevel. If this dies for real, the hero
 * dies too (same pattern as Lisa.die()). HospitalLevel keeps its position synced to the
 * Ambulance's position each time the ambulance advances - this class has no movement logic
 * of its own.
 */
public class Patient extends Mob {

    {
        spriteClass = YasuSprite.class; //TODO placeholder, replace with a dedicated sprite later

        HP = HT = 60;
        defenseSkill = 0;

        alignment = Alignment.ALLY;
        state = PASSIVE;
        properties.add(Property.IMMOVABLE);
    }

    private boolean canCauseGameOver = true;

    private static final String CAN_CAUSE_GAME_OVER = "can_cause_game_over";

    @Override
    protected boolean act() {
        spend(TICK);
        return true;
    }

    //talking to the patient before the ambulance has left is how the hero starts it
    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero || !(Dungeon.level instanceof HospitalLevel)) {
            return true;
        }

        final HospitalLevel level = (HospitalLevel) Dungeon.level;
        if (level.hasDeparted()) {
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(sprite(),
                        Messages.titleCase(name()),
                        Messages.get(Patient.this, "depart_prompt"),
                        Messages.get(Patient.this, "depart_yes"),
                        Messages.get(Patient.this, "depart_no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            level.depart();
                            Music.INSTANCE.play(Assets.Music.TENDENCY1, true);
                        }
                    }
                });
            }
        });
        return true;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti) {
        return false;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        if (canCauseGameOver) {
            yell(Messages.get(this, "death"));
            Dungeon.hero.die(this);
        }
    }

    //used by HospitalLevel to clear the patient without ending the run (e.g. on level completion)
    public void dieWithoutGameOver(Object cause) {
        canCauseGameOver = false;
        die(cause);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CAN_CAUSE_GAME_OVER, canCauseGameOver);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        canCauseGameOver = bundle.getBoolean(CAN_CAUSE_GAME_OVER);
    }
}
