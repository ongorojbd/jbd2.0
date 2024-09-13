/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Zombie.spwPrize;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CursedBlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Keicho;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Keicho2;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.levels.TendencyLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Zombie2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ZombieTwoTboss extends Mob {

    {
        spriteClass = Zombie2Sprite.class;

        HP = HT = 10;
        defenseSkill = 2;

        maxLvl = 5;
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);

    }

    private int moving;

    @Override
    protected boolean getCloser( int target ) {
        if (moving > 1) {
            moving-=2;
            return super.getCloser( target );
        } else if (moving==1) {
            moving+=3;
            return true;
        }
        else {
            moving++;
            return true;
        }

    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos] && hero.armorAbility instanceof Ratmogrify){
            alignment = Alignment.ALLY;
            if (state == SLEEPING) state = WANDERING;
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 5 );
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 3 ) == 0) {
            if(enemy instanceof Hero || enemy instanceof SpeedWagon || enemy instanceof Willa2) Buff.affect(enemy, Roots.class, 2f);
        }
        return damage;
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }
}