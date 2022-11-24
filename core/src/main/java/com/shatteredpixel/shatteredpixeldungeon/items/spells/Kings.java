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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Kings extends Spell {

    {
        image = ItemSpriteSheet.KINGS;
    }

    @Override
    protected void onCast(Hero hero) {

        new Flare(6, 32).color(0xFF0000, true).show(hero.sprite, 3.7f);
        Sample.INSTANCE.play(Assets.Sounds.BURNING, 1f);
        Sample.INSTANCE.play(Assets.Sounds.RAY, 1f);

        GLog.p(Messages.get(this, "w"));

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + 1).burst(FlameParticle.FACTORY, 5);
            if (mob.pos == hero.pos + 1){
                mob.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob, Burning.class ).reignite( mob, 5f );
            }
        }
        for (Mob mob2 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - 1).burst(FlameParticle.FACTORY, 5);
            if (mob2.pos == hero.pos - 1){
                mob2.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob2, Burning.class ).reignite( mob2, 5f );
            }
        }
        for (Mob mob3 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + 2).burst(FlameParticle.FACTORY, 5);
            if (mob3.pos == hero.pos + 2){
                mob3.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob3, Burning.class ).reignite( mob3, 5f );
            }
        }
        for (Mob mob4 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - 2).burst(FlameParticle.FACTORY, 5);
            if (mob4.pos == hero.pos - 2){
                mob4.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob4, Burning.class ).reignite( mob4, 5f );
            }
        }
        for (Mob mob5 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + 3).burst(FlameParticle.FACTORY, 5);
            if (mob5.pos == hero.pos + 3){
                mob5.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob5, Burning.class ).reignite( mob5, 5f );
            }
        }
        for (Mob mob6 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - 3).burst(FlameParticle.FACTORY, 5);
            if (mob6.pos == hero.pos - 3){
                mob6.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob6, Burning.class ).reignite( mob6, 5f );
            }
        }
        for (Mob mob7 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + 4).burst(FlameParticle.FACTORY, 5);
            if (mob7.pos == hero.pos + 4) {
                mob7.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob7, Burning.class ).reignite( mob7, 5f );
            }
        }
        for (Mob mob8 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - 4).burst(FlameParticle.FACTORY, 5);
            if (mob8.pos == hero.pos - 4){
                mob8.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob8, Burning.class ).reignite( mob8, 5f );
            }
        }
        for (Mob mob9 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + 5).burst(FlameParticle.FACTORY, 5);
            if (mob9.pos == hero.pos + 5){
                mob9.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob9, Burning.class ).reignite( mob9, 5f );
            }
        }
        for (Mob mob10 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - 5).burst(FlameParticle.FACTORY, 5);
            if (mob10.pos == hero.pos - 5){
                mob10.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob10, Burning.class ).reignite( mob10, 5f );
            }
        }
        for (Mob mob11 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + 6).burst(FlameParticle.FACTORY, 5);
            if (mob11.pos == hero.pos + 6){
                mob11.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob11, Burning.class ).reignite( mob11, 5f );
            }
        }
        for (Mob mob12 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - 6).burst(FlameParticle.FACTORY, 5);
            if (mob12.pos == hero.pos - 6){
                mob12.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob12, Burning.class ).reignite( mob12, 5f );
            }
        }
        for (Mob mob13 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos + 7).burst(FlameParticle.FACTORY, 5);
            if (mob13.pos == hero.pos + 7){
                mob13.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob13, Burning.class ).reignite( mob13, 5f );
            }
        }
        for (Mob mob14 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(hero.pos - 7).burst(FlameParticle.FACTORY, 5);
            if (mob14.pos == hero.pos - 7){
                mob14.damage(5 * Dungeon.depth, hero);
                Buff.affect( mob14, Burning.class ).reignite( mob14, 5f );
            }
        }


        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }

    @Override
    public int value() {
        return 25 * quantity;
    }
}
