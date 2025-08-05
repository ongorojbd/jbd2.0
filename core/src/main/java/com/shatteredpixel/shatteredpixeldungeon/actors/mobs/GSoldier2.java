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
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw10;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.wave;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GSoldierSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GSoldier2 extends Mob {

    {
        spriteClass = GSoldierSprite.class;
        HP = HT = 15;
        EXP = 0;

        viewDistance = 7;
        alignment = Alignment.ALLY;
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * 1.5f;
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 5);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        Sample.INSTANCE.play(Assets.Sounds.HIT_STAB, 1f, 0.8f);
        CellEmitter.get(enemy.pos).burst(SmokeParticle.FACTORY, 2);
        CellEmitter.center(enemy.pos).burst(BlastParticle.FACTORY, 2);

        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
        }

        return damage;
    }

    @Override
    public void die(Object cause) {

        GLog.n(Messages.get(this, "die"));

        Sample.INSTANCE.play(Assets.Sounds.MIMIC);
        new Flare( 5, 32 ).color( 0xCC0000, true ).show( this.sprite, 1f );
        ZombieSoldier zombieSoldier = new ZombieSoldier();
        zombieSoldier.state = zombieSoldier.HUNTING;
        zombieSoldier.pos = this.pos;
        GameScene.add(zombieSoldier);
        zombieSoldier.beckon(Dungeon.hero.pos);
        super.die(cause);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return true;
    }

}