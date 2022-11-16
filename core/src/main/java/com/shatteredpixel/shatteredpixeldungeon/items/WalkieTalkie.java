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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Act1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Act2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pucci;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Darby;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscC;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KSG;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WalkieTalkie extends Item {

    public static final String AC_SHATTER	= "SHATTER";

    {
        image = ItemSpriteSheet.BLANDFRUIT;

        defaultAction = AC_THROW;
        usesTargeting = true;

        stackable = true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x00FF00, 1f);
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_SHATTER );
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_SHATTER )) {

            hero.sprite.zap( hero.pos );

            detach( hero.belongings.backpack );

            Item item = shatter( hero, hero.pos );

            hero.next();

        }
    }

    @Override
    protected void onThrow( int cell ) {
        if (Dungeon.level.pit[cell]) {
            super.onThrow( cell );
        } else {
            Dungeon.level.drop(shatter( null, cell ), cell);
        }
    }

    public Item shatter( Char owner, int pos ) {

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.SHEEP );
            Splash.at( pos, 0xffd500, 5 );
        }

        int newPos = pos;
        if (Actor.findChar( pos ) != null) {
            ArrayList<Integer> candidates = new ArrayList<>();

            for (int n : PathFinder.NEIGHBOURS4) {
                int c = pos + n;
                if (!Dungeon.level.solid[c] && Actor.findChar( c ) == null) {
                    candidates.add( c );
                }
            }

            newPos = candidates.size() > 0 ? Random.element( candidates ) : -1;
        }

        if (newPos != -1) {
            Act1 bee = new Act1();
            bee.HP = bee.HT;
            bee.pos = newPos;

            GameScene.add( bee );
            if (newPos != pos) Actor.addDelayed( new Pushing( bee, pos, newPos ), -1f );

            bee.sprite.alpha( 0 );
            bee.sprite.parent.add( new AlphaTweener( bee.sprite, 1, 0.15f ) );

            Sample.INSTANCE.play( Assets.Sounds.BEACON );
            return new Blandfruit.Chunks();
        } else {
            return this;
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 100 * quantity;
    }


    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Blandfruit.class, Darby.class};
            inQuantity = new int[]{1, 1};

            cost = 6;

            output = WalkieTalkie.class;
            outQuantity = 1;
        }
    }

}
