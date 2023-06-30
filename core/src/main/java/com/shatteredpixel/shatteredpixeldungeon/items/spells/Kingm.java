/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Kingm extends Spell {

    {
        image = ItemSpriteSheet.KINGM;
    }

    @Override
    protected void onCast(Hero hero) {
        GLog.p(Messages.get(this, "w"));
        new Flare(6, 32).color(0xFFFFFF, true).show(hero.sprite, 3.7f);
        Sample.INSTANCE.play(Assets.Sounds.CHARMS, 1f);

        Buff.affect(Dungeon.hero, Invisibility.class, 10f);

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - Dungeon.level.width()).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob.pos == hero.pos - Dungeon.level.width()){
                mob.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob, Amok.class, 10f );
            }
        }
        for (Mob mob2 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + Dungeon.level.width()).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob2.pos == hero.pos + Dungeon.level.width()){
                mob2.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob2, Amok.class, 10f );
            }
        }
        for (Mob mob3 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - Dungeon.level.width()*2).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob3.pos == hero.pos - Dungeon.level.width()*2){
                mob3.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob3, Amok.class, 10f );
            }
        }
        for (Mob mob4 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + Dungeon.level.width()*2).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob4.pos == hero.pos + Dungeon.level.width()*2){
                mob4.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob4, Amok.class, 10f );
            }
        }
        for (Mob mob5 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - Dungeon.level.width()*3).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob5.pos == hero.pos - Dungeon.level.width()*3){
                mob5.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob5, Amok.class, 10f );
            }
        }
        for (Mob mob6 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + Dungeon.level.width()*3).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob6.pos == hero.pos + Dungeon.level.width()*3){
                mob6.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob6, Amok.class, 10f );
            }
        }
        for (Mob mob7 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - Dungeon.level.width()*4).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob7.pos == hero.pos - Dungeon.level.width()*4){
                mob7.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob7, Amok.class, 10f );
            }
        }
        for (Mob mob8 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + Dungeon.level.width()*4).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob8.pos == hero.pos + Dungeon.level.width()*4){
                mob8.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob8, Amok.class, 10f );
            }
        }
        for (Mob mob9 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - Dungeon.level.width()*5).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob9.pos == hero.pos - Dungeon.level.width()*5){
                mob9.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob9, Amok.class, 10f );
            }
        }
        for (Mob mob10 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + Dungeon.level.width()*5).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob10.pos == hero.pos + Dungeon.level.width()*5){
                mob10.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob10, Amok.class, 10f );
            }
        }
        for (Mob mob11 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - Dungeon.level.width()*6).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob11.pos == hero.pos - Dungeon.level.width()*6){
                mob11.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob11, Amok.class, 10f );
            }
        }
        for (Mob mob12 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + Dungeon.level.width()*6).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob12.pos == hero.pos + Dungeon.level.width()*6){
                mob12.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob12, Amok.class, 10f );
            }
        }
        for (Mob mob13 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - Dungeon.level.width()*7).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob13.pos == hero.pos - Dungeon.level.width()*7){
                mob13.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob13, Amok.class, 10f );
            }
        }
        for (Mob mob14 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + Dungeon.level.width()*7).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            if (mob14.pos == hero.pos + Dungeon.level.width()*7){
                mob14.damage(3 * Dungeon.depth, hero);
                Buff.affect( mob14, Amok.class, 10f );
            }
        }

        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }

    @Override
    public int value() {
        return 10 * quantity;
    }
}
