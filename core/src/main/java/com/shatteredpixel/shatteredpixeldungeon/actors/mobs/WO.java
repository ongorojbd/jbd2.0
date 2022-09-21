/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MudaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TankSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WO extends Mob {

    {
        spriteClass = MudaSprite.class;

        HP = HT = 1200;

        defenseSkill = 25;
        EXP = 0;
        maxLvl = 30;
        flying = true;
        viewDistance = 55;

        state = HUNTING;

        properties.add(Property.BOSS);
        immunities.add(Terror.class);
        immunities.add(Dread.class );
        immunities.add(Amok.class );
        immunities.add(Blindness.class );
        immunities.add(Sleep.class );
        immunities.add(MagicalSleep.class );
    }

    private int spell = 0; // 1 def, 2 knokback, 3 atk
    private int cooldown = 0; // 1 def, 2 knokback, 3 atk

    @Override
    protected boolean act() {
        if (cooldown <= 0) {
            spell = Random.Int(1,4);
            switch (spell) {
                case 1:
                    CellEmitter.get( this.pos ).burst( RainbowParticle.BURST, 99 );
                    sprite.showStatus(CharSprite.WARNING, Messages.get(this, "W1"));
                    Buff.affect(this, Haste.class, 3f);
                    Buff.affect(this, MagicImmune.class, 3f);
                    this.beckon( Dungeon.hero.pos );
                    break;
                case 2:
                    CellEmitter.get( this.pos ).burst( FlameParticle.FACTORY, 99 );
                    sprite.showStatus(CharSprite.WARNING, Messages.get(this, "W2"));
                    Buff.affect(this, Light.class, 3f);
                    Buff.affect(this, FireImbue.class).set(3f);
                    this.beckon( Dungeon.hero.pos );
                    break;
                case 3:
                    CellEmitter.get( this.pos ).burst( MagicMissile.MagicParticle.FACTORY, 99 );
                    sprite.showStatus(CharSprite.WARNING, Messages.get(this, "W3"));
                    Buff.affect(this, Bless.class, 3f);
                    Buff.affect(this, FrostImbue.class, 3f);
                    this.beckon( Dungeon.hero.pos );
                    break;
            }
            cooldown = Random.Int(4,8);
            return super.act();
        }
        else cooldown -= 1;

        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(30, 47);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        int dr;
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            dr = Random.NormalIntRange(5, 19);
        } else {
            dr = Random.NormalIntRange(5, 15);
        }
        return dr;
    }


    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 250){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 250;
        }
        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
    }



}
