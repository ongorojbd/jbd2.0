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

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw4;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ZombieFour extends Mob {

    {
        spriteClass = VampireSprite.class;

        HP = HT = 60 + 30 * spw4;
        defenseSkill = 25;
        viewDistance = Light.DISTANCE;

        EXP = 12;
        maxLvl = 25;
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    public ZombieFour() {
        super();
        switch (Random.Int(4)) {
            case 0:
            default:
                spriteClass = VampireSprite.Blue.class;
                break;
            case 1:
                spriteClass = VampireSprite.Green.class;
                break;
            case 2:
                spriteClass = VampireSprite.Red.class;
                break;
            case 3:
                spriteClass = VampireSprite.Yellow.class;
                break;
        }
    }

    @Override
    public int damageRoll() {
        int minDamage = (int) (25 + spw4 * 1.7);
        int maxDamage = (int) (30 + spw4 * 2);

        return Random.NormalIntRange(minDamage, maxDamage);
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 10);
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        return super.act();
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        Statistics.duwang3++;
        spw4++;

        Item prize = Random.oneOf(
                new Kingt().quantity(1),
                new StoneOfAdvanceguard().quantity(1),
                new Kings().quantity(1),
                new Kingm().quantity(1),
                new Kingw().quantity(1),
                new Willa().quantity(1),
                new PotionOfShielding().quantity(2),
                new Kingc().quantity(1)
        );

        Dungeon.level.drop(prize, pos).sprite.drop(pos);

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BONES, Random.Float(1.2f, 0.9f));
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }
    }
}
