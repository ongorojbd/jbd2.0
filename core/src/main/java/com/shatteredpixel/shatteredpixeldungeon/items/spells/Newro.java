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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDestOrb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Newro extends Spell {

    {
        image = ItemSpriteSheet.RO2;
    }

    @Override
    protected void onCast(Hero hero) {
        Statistics.neoroca += 1;
        if (Statistics.neoroca > 3) {
            GLog.n( Messages.get(this, "z") );
        }
        if (Statistics.neoroca < 4) {

            switch (Random.Int(8)){
                case 0:
                    Item a = new Kingt();
                    if (a.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", a.name()) ));
                    } else {
                        Dungeon.level.drop(a, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
                case 1:
                    Item b = new StoneOfAdvanceguard();
                    if (b.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", b.name()) ));
                    } else {
                        Dungeon.level.drop(b, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
                case 2:
                    Item c = new Xray();
                    if (c.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", c.name()) ));
                    } else {
                        Dungeon.level.drop(c, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
                case 3:
                    Item d = new Kings();
                    if (d.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", d.name()) ));
                    } else {
                        Dungeon.level.drop(d, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
                case 4:
                    Item e = new Kingm();
                    if (e.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", e.name()) ));
                    } else {
                        Dungeon.level.drop(e, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
                case 5:
                    Item f = new Kingw();
                    if (f.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", f.name()) ));
                    } else {
                        Dungeon.level.drop(f, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
                case 6:
                    Item g = new Kingc();
                    if (g.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", g.name()) ));
                    } else {
                        Dungeon.level.drop(g, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
                case 7:
                    Item h = new Kinga();
                    if (h.doPickUp( Dungeon.hero )) {
                        GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", h.name()) ));
                    } else {
                        Dungeon.level.drop(h, Dungeon.hero.pos ).sprite.drop();
                    }
                    break;
            }



            new Flare(6, 32).color(0xFF66FF, true).show(hero.sprite, 1.5f);
            Sample.INSTANCE.play(Assets.Sounds.EAT, 1f);
            Sample.INSTANCE.play(Assets.Sounds.LEVELUP, 0.7f, 1.2f);
            SpellSprite.show(hero, SpellSprite.FOOD);

            hero.sprite.operate(hero.pos);
            detach( curUser.belongings.backpack );
            updateQuickslot();
            hero.spendAndNext( 1f );
        }
    }
    @Override
    public int value() {
        return 15 * quantity;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{
        {
            inputs =  new Class[]{StoneOfEnchantment.class};
            inQuantity = new int[]{1};

            cost = 5;

            output = Newro.class;
            outQuantity = 1;
        }
    }
}