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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RollerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Rollermob extends Mob {

    {
        spriteClass = RollerSprite.class;

        HP = HT = 120;
        defenseSkill = 0;

        EXP = 0;

        state = PASSIVE;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    public float lifespan;
    public int bombPos = -1;

    private int timer = 10;

    @Override
    protected boolean act() {
        if (timer==0) {
            HP = 0;

            Dungeon.hero.damage(Dungeon.hero.HP/2, this);

            Camera.main.shake(5, 1.5f);

            for (int i : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[this.pos + i]) {
                    CellEmitter.center(this.pos+i).burst(BlastParticle.FACTORY, 15);
                    CellEmitter.center(this.pos+i).burst(SmokeParticle.FACTORY, 4);
                }
            }

            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            Sample.INSTANCE.play(Assets.Sounds.HAHAH);

            destroy();
            sprite.die();
            GLog.newLine();
            GLog.n(Messages.get(Rollermob.class, "notice"));
        }

        if (timer == 9) {
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "1"));
            Dungeon.hero.interrupt();
        } else if (timer == 8){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "2"));
            Dungeon.hero.interrupt();
        } else if (timer == 7){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "3"));
            Dungeon.hero.interrupt();
        } else if (timer == 6){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "4"));
            Dungeon.hero.interrupt();
        } else if (timer == 5){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "5"));
            Dungeon.hero.interrupt();
        } else if (timer == 4){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "6"));
            Dungeon.hero.interrupt();
        } else if (timer == 3){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "7"));
            Dungeon.hero.interrupt();
        } else if (timer == 2){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "8"));
            Dungeon.hero.interrupt();
        } else if (timer == 1){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Rollermob.class, "9"));
            Dungeon.hero.interrupt();
        }

        timer--;
        spend(TICK);
        return true;
    }


    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 12);
    }

    @Override
    public void beckon (int cell) {
        //do nothing
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 20){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 19 + (int)(Math.sqrt(8*(dmg - 19) + 1) - 1)/2;
        }
        super.damage(dmg, src);
    }

    @Override
    public void die( Object cause ) {
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        for (int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[this.pos + i]) {
                CellEmitter.center(this.pos+i).burst(BlastParticle.FACTORY, 15);
                CellEmitter.center(this.pos+i).burst(SmokeParticle.FACTORY, 4);
            }
        }

        GLog.newLine();
        GLog.n(Messages.get(Rollermob.class, "10"));

        super.die( cause );
    }

    private static final String LIFESPAN = "lifespan";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LIFESPAN, lifespan);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lifespan = bundle.getInt(LIFESPAN);
    }
}