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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BmoreGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BmoreSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM201Sprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Bmore extends Mob {

    {
        spriteClass = BmoreSprite.class;

        HP = HT = 45;
        baseSpeed = 0.7f;

        HUNTING = new Mob.Hunting();
        immunities.add(BmoreGas.class);

        flying=true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 15);
    }

    private boolean threatened = false;

    @Override
    protected boolean act() {
        //in case DM-201 hasn't been able to act yet
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
            fieldOfView = new boolean[Dungeon.level.length()];
            Dungeon.level.updateFieldOfView( this, fieldOfView );
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

    private void zap( ){
        threatened = false;
        spend(TICK);

        GameScene.add(Blob.seed(enemy.pos, 15, BmoreGas.class));
        for (int i : PathFinder.NEIGHBOURS8){
            if (!Dungeon.level.solid[enemy.pos+i]) {
                GameScene.add(Blob.seed(enemy.pos + i, 5, BmoreGas.class));
            }
        }

    }


}
