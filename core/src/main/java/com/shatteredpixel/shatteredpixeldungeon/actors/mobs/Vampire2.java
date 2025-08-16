/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 *  Shattered Pixel Dungeon
 *  Copyright (C) 2014-2022 Evan Debenham
 *
 * Summoning Pixel Dungeon
 * Copyright (C) 2019-2022 TrashboxBobylev
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Vampire2Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Vampire2 extends Mob {

    {
        spriteClass = Vampire2Sprite.class;

        HP = HT = 90;
        defenseSkill = 12;
        baseSpeed = 2f;

        EXP = 7;
        maxLvl = 15;

        properties.add(Property.DEMONIC);
        properties.add(Property.UNDEAD);
    }

    private boolean bloodFrenzy = false;

    @Override
    public int damageRoll() {
        if (bloodFrenzy) return Random.NormalIntRange(14, 22);
        return Random.NormalIntRange(12, 20);
    }

    @Override
    public int attackSkill( Char target ) {
        return bloodFrenzy ? 24 : 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, bloodFrenzy ? 8 : 6);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        // 33% chance to inflict bleeding on hit (stronger than VampireTest)
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Bleeding.class).set(0.5f * damage);
            if (Dungeon.level.heroFOV[pos]) {
                CellEmitter.center(this.pos).burst(BloodParticle.BURST, 3);
            }
        }

        return damage;
    }

    private static final String BLOOD_FRENZY = "blood_frenzy";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BLOOD_FRENZY, bloodFrenzy);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bloodFrenzy = bundle.getBoolean(BLOOD_FRENZY);
    }

    private boolean anyBleedingPresent() {
        // check hero
        if (Dungeon.hero != null && Dungeon.hero.buff(Bleeding.class) != null) return true;
        // check mobs
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (m != null && m != this && m.buff(Bleeding.class) != null) return true;
        }
        return false;
    }

    @Override
    protected boolean act() {
        boolean frenzyNow = anyBleedingPresent();
        if (frenzyNow && !bloodFrenzy) {
            bloodFrenzy = true;
            for (Mob mob : Dungeon.level.mobs) {
                mob.beckon( pos );
            }
            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            Buff.affect(this, Adrenaline.class, 50f);
            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus(CharSprite.WARNING, Messages.get(this, "frenzy"));
            }
        } else if (!frenzyNow && bloodFrenzy) {
            bloodFrenzy = false;
        }
        return super.act();
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (Random.Int( 3 ) == 0) {
            Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }

}