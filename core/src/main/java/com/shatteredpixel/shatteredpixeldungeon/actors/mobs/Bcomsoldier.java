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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfShock;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BcomTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DoobieTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FancakeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BcomSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BcopterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Bcomsoldier extends Mob {

    {
        spriteClass = BcomSprite.class;

        HP = HT = 60;
        defenseSkill = 15;

        loot = new StoneOfBlast();
        lootChance = 1/15f;

        EXP = 10;
        maxLvl = 21;
    }

    private int left(int direction){
        return direction == 0 ? 7 : direction-1;
    }

    private int right(int direction){
        return direction == 7 ? 0 : direction+1;
    }

    private int trapcooldown = 0;
    private int lastEnemyPos = -1;

    @Override
    public int attackProc( Char enemy, int damage ) {
        Sample.INSTANCE.play(Assets.Sounds.HIT_STAB, 1f, 0.8f);
        return damage;
    }

    public void setTraps(){


        Trap[] trapClasses = new Trap[]{
                new BcomTrap()};

        int trapPos = trapPos();
        if (trapPos != -1) {
            int i;
            for (i = 0; i < PathFinder.CIRCLE8.length; i++) {
                if ((enemy.pos + PathFinder.CIRCLE8[i]) == trapPos) {
                    break;
                }
            }

            if (Dungeon.level.passable[trapPos] && Dungeon.level.map[trapPos] != Terrain.EXIT&& Dungeon.level.map[trapPos] != Terrain.ENTRANCE) {

                if(trapPos != Terrain.EMPTY_SP){
                    Level.set(trapPos, Terrain.SECRET_TRAP);

                    Trap t1 = trapClasses[Random.Int(trapClasses.length)];

                    Dungeon.level.setTrap(t1, trapPos);

                    Dungeon.level.discover(trapPos);
                    CellEmitter.get(trapPos).burst(Speck.factory(Speck.LIGHT), 12);

                    sprite.showStatus(CharSprite.WARNING, Messages.get(this, "a"));

                    Sample.INSTANCE.play(Assets.Sounds.MINE);

                    Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                } else {

                }

            }

            trapcooldown = 12;



            if (Dungeon.level.heroFOV[enemy.pos]) {
                Dungeon.hero.interrupt();
            }
        }
        next();
    }

    @Override
    protected boolean act() {
        trapcooldown--;

        AiState lastState = state;
        boolean result = super.act();


        if (enemy != null && enemySeen) {
            lastEnemyPos = enemy.pos;
        } else {
            lastEnemyPos = Dungeon.hero.pos;
        }


        return result;
    }

    public int trapPos(){

        Char enemy = this.enemy;
        if (enemy == null) return -1;

        Ballistica b;
        //aims web in direction enemy is moving, or between self and enemy if they aren't moving
        if (lastEnemyPos == enemy.pos){
            b = new Ballistica( enemy.pos, pos, Ballistica.WONT_STOP );
        } else {
            b = new Ballistica( lastEnemyPos, enemy.pos, Ballistica.WONT_STOP );
        }

        int collisionIndex = 0;
        for (int i = 0; i < b.path.size(); i++){
            if (b.path.get(i) == enemy.pos){
                collisionIndex = i;
                break;
            }
        }

        //in case target is at the edge of the map and there are no more cells in the path
        if (b.path.size() <= collisionIndex+1){
            return -1;
        }

        int trapPos = b.path.get( collisionIndex+1 );

        //ensure we aren't shooting the web through walls
        int projectilePos = new Ballistica( pos, trapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID).collisionPos;

        if (trapPos != enemy.pos && projectilePos == trapPos && Dungeon.level.passable[trapPos]){
            return trapPos;
        } else {
            return -1;
        }

    }

    @Override
    public void move(int step, boolean travelling) {
        if (travelling && enemySeen && trapcooldown <= 0 && lastEnemyPos != -1){
            if (trapPos() != -1){

                setTraps();

            }
        }
        super.move(step, travelling);
    }


    private static final String TRAPCOOLDOWN = "TRAPCOOLDOWN";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TRAPCOOLDOWN, trapcooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        trapcooldown = bundle.getInt( TRAPCOOLDOWN );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 16, 22 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 24;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 4);
    }
}