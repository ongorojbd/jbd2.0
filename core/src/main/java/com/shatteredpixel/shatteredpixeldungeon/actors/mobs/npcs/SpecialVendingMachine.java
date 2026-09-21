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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlotSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSlotMachine;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

//the odd machine at the back of the hospital lounge's vending corner (ColdhouseLoungeLevel).
//feed it WndSlotMachine.COST gold and it plays a three reel slot pull instead of dropping a can.
public class SpecialVendingMachine extends NPC {

    {
        spriteClass = SlotSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.INORGANIC);
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
        //it's a machine, and a sturdy one
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero) {
            return true;
        }

        if (Statistics.slotPulls >= WndSlotMachine.MAX_PULLS) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(SpecialVendingMachine.this,
                            Messages.get(SpecialVendingMachine.class, "exhausted", Statistics.slotPulls, WndSlotMachine.MAX_PULLS)));
                }
            });
            return true;
        }

        if (Dungeon.gold < WndSlotMachine.COST) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(SpecialVendingMachine.this,
                            Messages.get(SpecialVendingMachine.class, "broke", WndSlotMachine.COST)));
                }
            });
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndSlotMachine());
            }
        });
        return true;
    }
}
