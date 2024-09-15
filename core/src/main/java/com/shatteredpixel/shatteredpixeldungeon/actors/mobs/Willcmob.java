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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willc;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillcSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//FIXME the AI for these things is becoming a complete mess, should refactor
public class Willcmob extends Mob {

    {
        spriteClass = WillcSprite.class;
        HP = HT =  10 + (Dungeon.depth) * 3;
        viewDistance = 5;
        defenseSkill = Dungeon.depth;
        EXP = 0;

        state = WANDERING;
        alignment = Alignment.ALLY;
        //only applicable when the bee is charmed with elixir of honeyed healing
        intelligentAlly = true;
    }

    //-1 refers to a pot that has gone missing.
    private int potPos;
    //-1 for no owner
    private int potHolder;
    private int spell = 0; // 1 def, 2 knokback, 3 atk

    private static final String POTPOS	    = "potpos";
    private static final String POTHOLDER	= "potholder";
    private static final String ALIGMNENT   = "alignment";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( POTPOS, potPos );
        bundle.put( POTHOLDER, potHolder );
        bundle.put( ALIGMNENT, alignment);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        potPos = bundle.getInt( POTPOS );
        potHolder = bundle.getInt( POTHOLDER );
        if (bundle.contains(ALIGMNENT)) alignment = bundle.getEnum( ALIGMNENT, Alignment.class);
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]){
            Bestiary.setSeen(getClass());
        }
        return super.act();
    }

    public int potHolderID(){
        return potHolder;
    }

    @Override
    public int attackSkill( Char target ) {
        return 999;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( Dungeon.depth/2, Dungeon.depth  );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth/2);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );


        if (Random.Int(5) == 0) {

            Buff.affect(enemy, Paralysis.class, 1f);

        }

        if (enemy instanceof Mob) {
            ((Mob)enemy).aggro( this );
        }
        return damage;
    }



    @Override
    protected boolean canAttack( Char enemy ) {
        return true;
    }

    @Override
    public void die( Object cause ) {

        GLog.n(Messages.get(Willc.class, "5"));

        switch(Dungeon.hero.heroClass){
            case MAGE:
                GLog.p(Messages.get(Willc.class, "6"));
                break;
        }

        GameScene.flash(0xFFFF00);
        new Flare( 5, 32 ).color( 0xFFFF00, true ).show( hero.sprite, 3f );
        Sample.INSTANCE.play(Assets.Sounds.TENDENCY3);

        Buff.affect(hero, Barrier.class).setShield((int) (0.5f * hero.HT));
        Buff.affect(hero, Bless.class, 15f);

        super.die( cause );

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

}