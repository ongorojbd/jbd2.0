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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Zombie.spwPrize;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieGSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ZombieG extends Mob {

    {
        spriteClass = ZombieGSprite.class;

        HP = HT = 40;
        defenseSkill = 9;

        EXP = 7;
        maxLvl = 14;

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 8);
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 6);
    }

    public void damage(int dmg, Object src) {
        int newdmg = dmg;

        if (src instanceof Char) {
            Char enemy = (Char)src;
            if (Dungeon.level.distance(enemy.pos, pos) > 3) newdmg = 0;
            else if (Dungeon.level.distance(enemy.pos, pos) == 2) newdmg = (int)(dmg/0.67);
            else if (Dungeon.level.distance(enemy.pos, pos) == 3) newdmg = (int)(dmg/0.34);

        }

        super.damage(newdmg, src);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        Statistics.duwang3++;

        if (Statistics.duwang3 == Statistics.duwang2) {
            spwPrize(pos);
        }

        if (Statistics.wave == 12) Dungeon.level.drop( new PotionOfStrength().identify(), pos ).sprite.drop();
        if (Random.Int( 8 ) == 0) Dungeon.level.drop( new Gold().quantity(20), pos ).sprite.drop();

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BONES, Random.Float(1.2f, 0.9f));
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }


}
