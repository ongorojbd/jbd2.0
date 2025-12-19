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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Civil;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Sbr8 extends Spell {

    {
        image = ItemSpriteSheet.MAP0;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        return actions;
    }

    @Override
    public String status() {
        if (this.isIdentified()) return  "8TH" ;
        else return null;}

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

    }

    @Override
    protected void onCast(Hero hero) {

        ArrayList<Integer> spawnPoints = new ArrayList<>();

        if (Dungeon.depth == 26) {
            Sample.INSTANCE.play(Assets.Sounds.CHARMS);
            GameScene.flash(0xFFCC00);

            Sbr9 pick = new Sbr9();
            if (pick.doPickUp( Dungeon.hero )) {
                GLog.h( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", pick.name()) ));
            } else {
                Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
            }

            detach(Dungeon.hero.belongings.backpack);

            updateQuickslot();

            Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);
        }else
        {
            GLog.h(Messages.get(Civil.class, "now"));
            SpellSprite.show( curUser, SpellSprite.MAP );
            Sample.INSTANCE.play(Assets.Sounds.READ);
        }

        hero.sprite.operate(hero.pos);
        hero.spendAndNext( 1f );
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
        return 10 * quantity;
    }

}
