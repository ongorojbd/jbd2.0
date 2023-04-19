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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BmoreGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yukako;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BmoreSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Fugo2Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Fugomob2 extends Mob {

    {
        spriteClass = Fugo2Sprite.class;

        HP = HT =  30;
        HUNTING = new Mob.Hunting();
        immunities.add(CorrosiveGas.class);
        //only applicable when the bee is charmed with elixir of honeyed healing
        intelligentAlly = true;

    }

    int summonCooldown = 1;
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private boolean threatened = false;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
    }


    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        summonCooldown = bundle.getInt( SUMMON_COOLDOWN );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 15);
    }



    @Override
    protected boolean act() {
        summonCooldown--;
        //in case DM-201 hasn't been able to act yet
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
            fieldOfView = new boolean[Dungeon.level.length()];
            Dungeon.level.updateFieldOfView( this, fieldOfView );
        }

        if (summonCooldown <= 0) {
            threatened = true;
            summonCooldown = (11);
        }

        if (paralysed <= 0 && state == HUNTING && enemy != null && enemySeen
                && threatened && !Dungeon.level.adjacent(pos, enemy.pos) && fieldOfView[enemy.pos]){
            enemySeen = enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        if ((src instanceof Char && !Dungeon.level.adjacent(pos, ((Char)src).pos))
                || enemy == null || !Dungeon.level.adjacent(pos, enemy.pos)){
            threatened = true;
        }
        super.damage(dmg, src);
    }

    public void onZapComplete(){
        zap();
        next();
    }

    @Override
    protected Char chooseEnemy() {

        return super.chooseEnemy();
    }

    @Override
    protected boolean getCloser(int target) {

        target = Dungeon.hero.pos;

        return super.getCloser( target );
    }


    private void zap( ){
        threatened = false;
        spend(TICK);
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "G1"));
        GameScene.add(Blob.seed(enemy.pos, 15, CorrosiveGas.class).setStrength(1+Dungeon.depth/4));
        for (int i : PathFinder.NEIGHBOURS8){
            if (!Dungeon.level.solid[enemy.pos+i]) {
                GameScene.add(Blob.seed(enemy.pos + i, 5, CorrosiveGas.class));
            }
        }

    }


}
