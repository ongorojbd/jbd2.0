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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscC;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BeeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

//FIXME the AI for these things is becoming a complete mess, should refactor
public class Act2 extends Mob {

    {
        spriteClass = Act2Sprite.class;
        HP = HT =  3 + (2 + Dungeon.depth) * 4;
        viewDistance = 5;
        defenseSkill = 5 + Dungeon.depth;
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

    @Override
    public int attackSkill( Char target ) {
        return Dungeon.depth+7;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( Dungeon.depth/2, Dungeon.depth * 2 );
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        enemy.sprite.emitter().burst( Speck.factory( Speck.BUBBLE ), 5);


        if (Random.Int(4) == 0) {
            {
                spell = Random.Int(1, 4);
                switch (spell) {
                    case 1:
                        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "E1"));
                        ScrollOfTeleportation.teleportChar(enemy);
                        this.beckon(Dungeon.hero.pos);
                        break;
                    case 2:
                        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "E2"));
                        new GeyserTrap().set(target).activate();
                        this.beckon(Dungeon.hero.pos);
                        break;
                    case 3:
                        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "E3"));
                        Buff.affect( enemy, Burning.class ).reignite( enemy, 3f );
                        this.beckon(Dungeon.hero.pos);
                        break;
                }

            }
        }


        if (enemy instanceof Mob) {
            ((Mob)enemy).aggro( this );
        }
        return damage;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );


        if (Random.Int( 2 ) == 0) {
            GLog.n(Messages.get(this, "v"));
            Sample.INSTANCE.play( Assets.Sounds.BLAST );
            Camera.main.shake(9, 0.5f);
            new Flare( 5, 32 ).color( 0xFFFF00, true ).show( this.sprite, 2f );
            Act3 Act3 = new Act3();
            Act3.state = Act3.WANDERING;
            Act3.pos = this.pos;
            GameScene.add( Act3 );
            Act3.beckon(Dungeon.hero.pos);
        }

    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return true;
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