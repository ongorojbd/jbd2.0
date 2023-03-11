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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Weza;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Goo2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Goo2 extends Mob {

    {
        HP = HT = 200;
        spriteClass = Goo2Sprite.class;
        defenseSkill = 15;

        EXP = 0;
        maxLvl = 30;

        state = PASSIVE;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
    }

    private int pumpedUp = 0;
    private int healInc = 1;

    public int  Phase = 0;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(35, 40);
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() { return Random.NormalIntRange(0, 5);}


    @Override
    public boolean act() {

        if (state != HUNTING){
            pumpedUp = 0;
        }

        if (Dungeon.level.water[pos] && HP < HT) {
            HP += healInc;

            LockedFloor lock = hero.buff(LockedFloor.class);
            if (lock != null) lock.removeTime(healInc*2);

            if (Dungeon.level.heroFOV[pos] ){
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), healInc );
            }

            if (HP*2 > HT) {
                BossHealthBar.bleed(false);
                ((Goo2Sprite)sprite).spray(false);
                HP = Math.min(HP, HT);
            }
        } else {
            healInc = 1;
        }
        return super.act();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (pumpedUp > 0){
            //we check both from and to in this case as projectile logic isn't always symmetrical.
            //this helps trim out BS edge-cases
            return Dungeon.level.distance(enemy.pos, pos) <= 2
                    && new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
                    && new Ballistica( enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 3 ) == 0) {
            Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
            enemy.sprite.burst( 0x000000, 5 );
        }

        if (pumpedUp > 0) {
            Camera.main.shake( 3, 0.2f );
        }

        return damage;
    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();

        if (pumpedUp > 0){
            ((Goo2Sprite)sprite).pumpUp( pumpedUp );
        }
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        if (pumpedUp == 1) {
            pumpedUp++;
            ((Goo2Sprite)sprite).pumpUp( pumpedUp );

            spend( attackDelay() );

            return true;
        } else if (pumpedUp >= 2 || Random.Int( (HP*2 <= HT) ? 2 : 5 ) > 0) {

            boolean visible = Dungeon.level.heroFOV[pos];

            if (visible) {
                if (pumpedUp >= 2) {
                    ((Goo2Sprite) sprite).pumpAttack();
                } else {
                    sprite.attack(enemy.pos);
                }
            } else {
                if (pumpedUp >= 2){
                    ((Goo2Sprite)sprite).triggerEmitters();
                }
                attack( enemy );
                Invisibility.dispel(this);
                spend( attackDelay() );
            }

            return !visible;

        } else {

            if (Dungeon.isChallenged(Challenges.EOH)){
                pumpedUp += 2;
                //don't want to overly punish players with slow move or attack speed
                spend(GameMath.gate(attackDelay(), hero.cooldown(), 3*attackDelay()));
            } else {
                pumpedUp++;
                spend( attackDelay() );
            }

            ((Goo2Sprite)sprite).pumpUp( pumpedUp );

            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "!!!") );
                GLog.n( Messages.get(this, "pumpup") );
            }

            return true;
        }
    }

    @Override
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
        boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        if (pumpedUp > 0) {
            pumpedUp = 0;
        }
        return result;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (pumpedUp != 0) {
            pumpedUp = 0;
            sprite.idle();
        }
        return super.getCloser( target );
    }

    @Override
    protected boolean getFurther(int target) {
        if (pumpedUp != 0) {
            pumpedUp = 0;
            sprite.idle();
        }
        return super.getFurther( target );
    }

    @Override
    public void damage(int dmg, Object src) {

        super.damage(dmg, src);


        if (Phase==0 && HP < 200) {
            Phase = 1;
            HP = 199;
            state = HUNTING;

            if (!BossHealthBar.isAssigned()) {
                BossHealthBar.assignBoss(this);
                for (Char ch : Actor.chars()){
                }
            }
            GLog.n(Messages.get(this, "n"));
        }

        boolean bleeding = (HP*2 <= HT);


        if ((HP*2 <= HT) && !bleeding){
            BossHealthBar.bleed(true);
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enraged"));
            ((Goo2Sprite)sprite).spray(true);
            GLog.n(Messages.get(this, "gluuurp"));
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        GLog.n(Messages.get(this, "d"));

        new Flare( 5, 32 ).color( 0x00FFFF, true ).show( hero.sprite, 1f );
        Sample.INSTANCE.play(Assets.Sounds.BADGE);
        GLog.p(Messages.get(Pucci4.class, "x"));

        Dungeon.mboss4 = 0;
    }

    private final String PUMPEDUP = "pumpedup";
    private final String HEALINC = "healinc";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( PUMPEDUP , pumpedUp );
        bundle.put( HEALINC, healInc );
        bundle.put( PHASE, Phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        pumpedUp = bundle.getInt( PUMPEDUP );
        Phase = bundle.getInt(PHASE);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

        healInc = bundle.getInt(HEALINC);
    }
    private static final String PHASE   = "Phase";


    public static void spawn(LabsLevel level) {

        if ((Dungeon.isChallenged(Challenges.EOH)) && Dungeon.depth == 26 && !Dungeon.bossLevel()) {
            Goo2 centinel = new Goo2();
            do {
                centinel.pos = level.randomRespawnCell(centinel);
            } while (centinel.pos == -1);
            level.mobs.add(centinel);
        }

    }

}