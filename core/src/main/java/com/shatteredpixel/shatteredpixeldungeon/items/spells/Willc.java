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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Val;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willcmob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Willc extends Spell {

    {
        image = ItemSpriteSheet.EBONY_CHEST;
    }

    private Class<? extends Willcmob> summonClass = Willcmob.class;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

    }

    @Override
    protected void onCast(Hero hero) {

        ArrayList<Integer> spawnPoints = new ArrayList<>();

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                spawnPoints.add( p );
            }
        }

        if (!spawnPoints.isEmpty()){

            for (Char ch : Actor.chars()){
                if (ch instanceof Willcmob && ch.buff(SummonElemental.InvisAlly.class) != null){
                    ScrollOfTeleportation.appear( ch, Random.element(spawnPoints) );
                    ((Willcmob) ch).state = ((Willcmob) ch).HUNTING;
                    curUser.spendAndNext(Actor.TICK);
                    return;
                }
            }

            Willcmob elemental = Reflection.newInstance(summonClass);
            GameScene.add( elemental );
            elemental.HP = elemental.HT;
            ScrollOfTeleportation.appear( elemental, Random.element(spawnPoints) );
            curUser.spendAndNext(Actor.TICK);

            Sample.INSTANCE.play(Assets.Sounds.CE3);

            detach(Dungeon.hero.belongings.backpack);

        } else {
            GLog.w(Messages.get(SpiritHawk.class, "no_space"));
        }

        switch(Dungeon.hero.heroClass){
            case WARRIOR:
                GLog.n(Messages.get(this, "1"));
                break;
            case ROGUE:
                GLog.n(Messages.get(this, "3"));
                break;
            case MAGE:
                GLog.n(Messages.get(this, "2"));
                break;
            case HUNTRESS:
                GLog.n(Messages.get(this, "3"));
                break;
            case DUELIST:
                GLog.n(Messages.get(this, "8"));
                break;
        }

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x00CCFF, 1f);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    @Override
    public int value() {
        return 15 * quantity;
    }

}
