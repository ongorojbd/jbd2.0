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
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo1;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo3;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo4;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo5;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo6;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo7;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo8;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo9;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.UltimateSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class UltimateFragment extends Spell {

    private static final Item[] jojoItems = {
            new BossdiscA(), new BossdiscB(), new BossdiscC(), new BossdiscD(),
            new BossdiscE(), new BossdiscF(), new BossdiscG(), new BossdiscH() };

    {
        image = ItemSpriteSheet.EVOLUTION;
        unique = true;
    }

    @Override
    protected void onCast(Hero hero) {
        GameScene.show(new WndOptions(new UltimateSprite(),
                Messages.get(UltimateFragment.class, "00"),
                Messages.get(UltimateFragment.class, "0"),
                Messages.get(UltimateFragment.class, "1"),
                Messages.get(UltimateFragment.class, "2"),
                Messages.get(UltimateFragment.class, "3"),
                Messages.get(UltimateFragment.class, "4"),
                Messages.get(UltimateFragment.class, "5"),
                Messages.get(UltimateFragment.class, "6"),
                Messages.get(UltimateFragment.class, "7"),
                Messages.get(UltimateFragment.class, "8")
        ) {
            @Override
            protected void onSelect(int index) {
                if (index >= 0 && index < jojoItems.length) {
                    performJojoAction(jojoItems[index], index + 1);
                }
            }
        });
    }

    private void performJojoAction(Item item, int index) {
        Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
        new Flare(6, 32).color(0xFFAA00, true).show(hero.sprite, 4f);
        Sample.INSTANCE.play(Assets.Sounds.DEBUFF);
        GameScene.flash(0xFFCC00);
        hero.sprite.operate(hero.pos);
        detach(curUser.belongings.backpack);
        updateQuickslot();
        hero.spendAndNext(1f);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFCC33, 1f);
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
        return 100 * quantity;
    }
}

