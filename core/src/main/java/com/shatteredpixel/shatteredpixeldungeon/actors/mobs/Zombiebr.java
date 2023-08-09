/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CursedBlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Lpsword;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Newro;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfExtract;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreatCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiebSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiebrSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombietSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Zombiebr extends Mob {

    {
        spriteClass = ZombiebrSprite.class;

        HP = HT = 30;
        defenseSkill = 5;

        EXP = 6;

        state = WANDERING;

        properties.add(Property.MINIBOSS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 7 );
    }


    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (enemy.buff(CursedBlow.class) != null){
            HP += 3;
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 2 );
            Sample.INSTANCE.play( Assets.Sounds.DRINK );
            sprite.showStatus(CharSprite.WARNING, Messages.get(Zombiebr.class, "d"));
            GLog.n(Messages.get(Zombiebr.class, "d2"));
        }

        if (Random.Int( 2 ) == 0) {
            Buff.affect(enemy, CursedBlow.class, 3f);
        }

        return damage;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );


        this.yell(Messages.get(this, "0"));

        Music.INSTANCE.play(Assets.Music.JONATHAN, true);

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        switch (Random.Int(2)) {
            case 0:
                Item qwe = Generator.random( Generator.Category.WEP_T2 );
                Dungeon.level.drop(qwe.identify(), this.pos).sprite.drop();
                break;
            case 1:
                Item a = new LeatherArmor();
                Dungeon.level.drop(a.identify(), this.pos).sprite.drop();
                break;
        }

        Dungeon.level.drop(new Lpsword(), this.pos).sprite.drop();

    }
}
