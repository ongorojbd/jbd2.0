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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BcomTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BcomSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Bcom extends Mob {

    {
        spriteClass = BcomSprite.class;

        HP = HT = 10;
        defenseSkill = 5;

        EXP = 3;
        maxLvl = 9;

        loot = new PotionOfHealing();
        lootChance = 0.1667f; //by default, see lootChance()
    }

    private boolean seenBefore = false;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 4 );
    }


    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 3;
    }

    @Override
    public void notice() {
        super.notice();

        if (!seenBefore) {
        for (int i : PathFinder.NEIGHBOURS4) {

        Gnoll jojo = new Gnoll();
        jojo.state = jojo.WANDERING;
        jojo.pos = this.pos+i;
        GameScene.add( jojo );
        jojo.beckon(Dungeon.hero.pos);
        }}


        seenBefore = true;
    }









    @Override
    public int attackProc( Char enemy, int damage ) {
        Sample.INSTANCE.play(Assets.Sounds.HIT_STAB, 1f, 0.8f);
        return damage;
    }

    @Override
    public float lootChance(){
        return super.lootChance() * ((5f - Dungeon.LimitedDrops.SWARM_HP.count) / 5f);
    }

    @Override
    public Item createLoot(){
        Dungeon.LimitedDrops.SWARM_HP.count++;
        return super.createLoot();
    }

}