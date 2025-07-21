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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BmoreGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Fugo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yukako;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscG;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BmoreSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Fugo2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Fugomob2 extends Mob {

    private int level = Dungeon.depth;

    {
        spriteClass = Fugo2Sprite.class;
        HP = HT = (1 + level) * 6;
        EXP = 0;

        HUNTING = new Mob.Hunting();
        immunities.add(CorrosiveGas.class);
        immunities.add(Vertigo.class);
        //only applicable when the bee is charmed with elixir of honeyed healing
        intelligentAlly = true;

    }

    @Override
    public int attackSkill(Char target) {
        if (target != null && alignment == Alignment.NEUTRAL && target.invisible <= 0) {
            return INFINITE_ACCURACY;
        } else {
            return 8 + level;
        }
    }

    int summonCooldown = 1;
    private static final String LEVEL = "level";
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private boolean threatened = false;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        summonCooldown = bundle.getInt(SUMMON_COOLDOWN);
    }

    @Override
    public int damageRoll() {
        if (alignment == Alignment.NEUTRAL) {
            return Random.NormalIntRange(2 + 2 * level, 2 + 2 * level);
        } else {
            return Random.NormalIntRange(1 + level, 2 + 2 * level);
        }
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1 + level / 2);
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]) {
            Bestiary.setSeen(getClass());
        }

        if (summonCooldown > 0) {
            summonCooldown--;
        }

        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
            fieldOfView = new boolean[Dungeon.level.length()];
            Dungeon.level.updateFieldOfView( this, fieldOfView );
        }

        if (paralysed <= 0 && state == HUNTING && enemy != null && enemySeen
                && !Dungeon.level.adjacent(pos, enemy.pos)
                && fieldOfView[enemy.pos]
                && summonCooldown <= 0) {

            enemySeen = enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap(enemy.pos);
                return false;
            } else {
                zap();
                return true;
            }
        }

        return super.act();
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


    private void zap() {
        spend(TICK);
        summonCooldown = 11;
        sprite.showStatus(CharSprite.POSITIVE, "[퍼플 헤이즈!]");
        GameScene.add(Blob.seed(enemy.pos, 15, CorrosiveGas.class).setStrength(1 + Dungeon.depth / 4));
        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[enemy.pos + i]) {
                GameScene.add(Blob.seed(enemy.pos + i, 5, CorrosiveGas.class));
            }
        }
    }

    @Override
    public void die( Object cause ) {
        yell(Messages.get(Fugomob2.class, "0"));
        super.die( cause );
        Dungeon.level.drop( new PotionOfCorrosiveGas().identify(), pos ).sprite.drop( pos );
    }
}
