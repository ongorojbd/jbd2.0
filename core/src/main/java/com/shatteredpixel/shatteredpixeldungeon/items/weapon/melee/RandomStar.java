/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class RandomStar extends MeleeWeapon {

    {
        image = ItemSpriteSheet.RAPIER;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        ACC = 1.20f; //20% boost to accuracy

        tier = 1;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        Sword.doraclass();
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(attacker.pos + 1).burst( Speck.factory( Speck.ROCK ), 1 );
            if (mob.pos == attacker.pos + 1){
                mob.damage(damageRoll(attacker), attacker);
            }
        }
        for (Mob mob2 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(attacker.pos - 1).burst( Speck.factory( Speck.ROCK ), 1 );
            if (mob2.pos == attacker.pos - 1){
                mob2.damage(damageRoll(attacker), attacker);
            }
        }
        for (Mob mob3 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(attacker.pos - Dungeon.level.width()).burst( Speck.factory( Speck.ROCK ), 1 );
            if (mob3.pos == attacker.pos - Dungeon.level.width()){
                mob3.damage(damageRoll(attacker), attacker);
            }
        }
        for (Mob mob4 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(attacker.pos + Dungeon.level.width() + 1).burst( Speck.factory( Speck.ROCK ), 1 );
            if (mob4.pos == attacker.pos + Dungeon.level.width() + 1){
                mob4.damage(damageRoll(attacker), attacker);
            }
        }
        for (Mob mob5 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(attacker.pos + Dungeon.level.width() - 1).burst( Speck.factory( Speck.ROCK ), 1 );
            if (mob5.pos == attacker.pos + Dungeon.level.width() - 1){
                mob5.damage(damageRoll(attacker), attacker);
            }
        }

        return damage;
    }



    @Override
    public int max(int lvl) {
        return  4*(tier+1) +
                lvl*(tier+1);
    }

}