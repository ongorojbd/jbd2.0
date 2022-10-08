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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SupressionSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class Pucci extends Mob {

    {
        spriteClass = PucciSprite.class;

        alignment = Alignment.ALLY;
        state = WANDERING;
        intelligentAlly = true;
        properties.add(Property.INORGANIC);
        HP = HT = 5;
        defenseSkill = 15;
        EXP = 0;
        Buff.affect(this, Barrier.class).setShield(171);

        immunities.add(Blindness.class );

    }
    private boolean seenBefore = false;
    private static final Rect arena = new Rect(0, 0, 33, 26);
    private static final int bottomDoor = 16 + (arena.bottom+1) * 33;
    int summonCooldown = 7;
    private static final String SUMMON_COOLDOWN = "summoncooldown";

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
    protected boolean act() {

        summonCooldown--;

        if (!seenBefore) {
            switch(Dungeon.hero.heroClass){
                case WARRIOR:
                    this.yell(Messages.get(this, "notice"));
                    break;
                case ROGUE:
                    this.yell(Messages.get(this, "notice"));
                    break;
                case MAGE:
                    this.yell(Messages.get(this, "notice"));
                    break;
                case HUNTRESS:
                    this.yell(Messages.get(this, "notice"));
                    break;
            }
        }
        seenBefore = true;

        if (summonCooldown <= 0 && Dungeon.level instanceof LabsBossLevel) {


            for (int i : PathFinder.NEIGHBOURS4){
                    Val Val = new Val();
                    Val.state = Val.HUNTING;
                    Val.pos = this.pos+i;
                    GameScene.add( Val );
                    Val.beckon(Dungeon.hero.pos);

                summonCooldown = (7);
                sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "skill"));

            }
        }

        return super.act();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 15 );
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 25){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 25;
        }

        super.damage(dmg, src);


    }

    @Override
    public int attackSkill( Char target ) {
        return 9999;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(3, 5);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {

        if (enemy instanceof Mob) {
            ((Mob)enemy).aggro( this );
        }

        damage = super.attackProc( enemy, damage );

        return damage;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        yell(Messages.get(this, "defeated"));

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Val ) {
                mob.die( cause );
            }
        }

    }


}