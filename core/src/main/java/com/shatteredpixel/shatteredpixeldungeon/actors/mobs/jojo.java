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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Dominion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fury;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
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
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JojoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SupressionSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class jojo extends Mob {

    {
        spriteClass = JojoSprite.class;

        alignment = Alignment.ALLY;
        state = WANDERING;
        intelligentAlly = true;
        properties.add(Property.INORGANIC);
        viewDistance = 99;
        HP = HT = 220;
        defenseSkill = 35;
        EXP = 0;

        immunities.add(Blindness.class );
        immunities.add(Dominion.class );
    }

    private boolean seenBefore = false;

    public void sayHeroKilled(){
        this.yell(Messages.get(this, "v"));

        destroy();
        sprite.killAndErase();
    }

    @Override
    protected boolean act() {

        if (!seenBefore) {
            switch(Dungeon.hero.heroClass){
                case WARRIOR:
                    this.yell(Messages.get(this, "notice"));
                    break;
                case ROGUE:
                    this.yell(Messages.get(this, "notice2"));
                    break;
                case MAGE:
                    this.yell(Messages.get(this, "notice3"));
                    break;
                case HUNTRESS:
                    this.yell(Messages.get(this, "notice4"));
                    break;
            }

            new Flare( 5, 32 ).color( 0x00FFFF, true ).show( this.sprite, 3f );
            Sample.INSTANCE.play( Assets.Sounds.YAREYARE, 3 );
        }
        seenBefore = true;
        return super.act();
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
    public int damageRoll() {
        return Random.NormalIntRange( 10, 16 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 99;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(15, 21);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        switch(Dungeon.hero.heroClass){
            case WARRIOR:
                this.yell(Messages.get(this, "defeated1"));
                break;
            case ROGUE:
                this.yell(Messages.get(this, "defeated"));
                GLog.p(Messages.get(jojo.class, "kisama"));
                Buff.affect(Dungeon.hero, Adrenaline.class, 7f);
                break;
            case MAGE:
                this.yell(Messages.get(this, "defeated2"));
                break;
            case HUNTRESS:
                this.yell(Messages.get(this, "defeated3"));
                break;
        }

    }

}