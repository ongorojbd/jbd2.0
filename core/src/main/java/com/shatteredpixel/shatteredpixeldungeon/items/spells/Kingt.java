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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Lighter;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ThirdBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfFeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Castleintro;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Cen;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Drago;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Sleepcmoon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDestOrb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Kingt extends Spell {

    {
        image = ItemSpriteSheet.KINGT;
    }

    @Override
    protected void onCast(Hero hero) {


        switch (Random.Int(39)){
            case 0:
                Item a = new Sbr3();
                Dungeon.level.drop(a, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", a.name()) ));
                break;
            case 1:
                Item b = Generator.random( Generator.Category.WEP_T5 );
                Dungeon.level.drop(b, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", b.name()) ));
                break;
            case 2:
                Item c = new PlateArmor();
                Dungeon.level.drop(c, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", c.name()) ));
                break;
            case 3:
                Item d = new Gold().quantity(150 * Dungeon.depth);
                Dungeon.level.drop(d, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", d.name()) ));
                break;
            case 4:
                Item e = new ScrollOfPolymorph();
                Dungeon.level.drop(e, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", e.name()) ));
                break;
            case 5:
                Item f = new PotionOfShielding().identify().quantity(3);
                Dungeon.level.drop(f, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f.name()) ));
                break;
            case 6:
                Item g = new Willa().quantity(2);
                Dungeon.level.drop(g, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", g.name()) ));
                break;
            case 7:
                Item h = new ThirdBomb();
                Dungeon.level.drop(h, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", h.name()) ));
                break;
            case 8:
                Item i = new ChaosCatalyst();
                Dungeon.level.drop(i, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", i.name()) ));
                break;
            case 9:
                Item j = new AdvancedEvolution();
                Dungeon.level.drop(j, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", j.name()) ));
                break;
            case 10:
                Item h1 = new ReclaimTrap().quantity(5);
                Dungeon.level.drop(h1, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", h1.name()) ));
                break;
            case 11:
                Item h2 = new Blandfruit().quantity(3);
                Dungeon.level.drop(h2, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", h2.name()) ));
                break;
            case 12:
                Item h3 = new WandOfDestOrb();
                Dungeon.level.drop(h3, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", h3.name()) ));
                break;
            case 13:
                Item h4 = new ScrollOfMetamorphosis().quantity(5);
                Dungeon.level.drop(h4, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", h4.name()) ));
                break;
            case 14:
                Item h5 = new Bomb().quantity(10);
                Dungeon.level.drop(h5, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", h5.name()) ));
                break;
            case 15:
                Item h6 = new Sleepcmoon();
                Dungeon.level.drop(h6, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", h6.name()) ));
                break;
            case 16:
                Item f0 = new ElixirOfFeatherFall().quantity(3);
                Dungeon.level.drop(f0, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f0.name()) ));
                break;
            case 17:
                Item f1 = new AquaBrew().quantity(15);
                Dungeon.level.drop(f1, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f1.name()) ));
                break;
            case 18:
                Item f2 = new PotionOfDivineInspiration().quantity(3);
                Dungeon.level.drop(f2, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f2.name()) ));
                break;
            case 19:
                Item f3 = new PotionOfCorrosiveGas().quantity(3);
                Dungeon.level.drop(f3, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f3.name()) ));
                break;
            case 20:
                Item f5 = new BlizzardBrew().quantity(3);
                Dungeon.level.drop(f5, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f5.name()) ));
                break;
            case 21:
                Item f7 = new MagicalInfusion();
                Dungeon.level.drop(f7, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f7.name()) ));
                break;
            case 22:
                Item f6 = new InfernalBrew().quantity(3);
                Dungeon.level.drop(f6, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", f6.name()) ));
                break;
            case 23:
                Item z = new StoneOfBlink().quantity(5);
                Dungeon.level.drop(z, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", z.name()) ));
                break;
            case 24:
                Item zz = new ScrollOfTransmutation().quantity(3);
                Dungeon.level.drop(zz, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", zz.name()) ));
                break;
            case 25:
                Item we = new Starflower.Seed().quantity(3);;
                Dungeon.level.drop(we, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", we.name()) ));
                break;
            case 26:
                Item we2 = new Highway().quantity(10);
                Dungeon.level.drop(we2, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", we2.name()) ));
                break;
            case 27:
                Item we4 = new ScrollOfChallenge();
                Dungeon.level.drop(we4, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", we4.name()) ));
                break;
            case 28:
                Item we6 = new BossdiscE();
                Dungeon.level.drop(we6, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", we6.name()) ));
                break;
            case 29:
                Item wq = new Castleintro();
                Dungeon.level.drop(wq, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", wq.name()) ));
                break;
            case 30:
                Item wq1 = new Drago().quantity(5);
                Dungeon.level.drop(wq1, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", wq1.name()) ));
                break;
            case 31:
                Item wq3 = new Cen();
                Dungeon.level.drop(wq3, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", wq3.name()) ));
                break;
            case 32:
                Item wq5 = new Araki();
                Dungeon.level.drop(wq5, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", wq5.name()) ));
                break;
            case 33:
                Item qw1 = new PhantomMeat();
                Dungeon.level.drop(qw1, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", qw1.name()) ));
                break;
            case 34:
                Item qw2 = new StoneOfAugmentation().quantity(3);
                Dungeon.level.drop(qw2, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", qw2.name()) ));
                break;
            case 35:
                Item qw3 = Generator.random( Generator.Category.RING );
                Dungeon.level.drop(qw3, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", qw3.name()) ));
                break;
            case 36:
                Item qw4 = new Lighter().quantity(4);
                Dungeon.level.drop(qw4, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", qw4.name()) ));
                break;
            case 37:
                Item neoro = new Neoro().quantity(4);
                Dungeon.level.drop(neoro, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", neoro.name()) ));
                break;
            case 38:
                Item qwe = new UnstableSpell().quantity(3);
                Dungeon.level.drop(qwe, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", qwe.name()) ));
                break;
            case 39:
                Item qwe2 = new UnstableBrew().quantity(3);
                Dungeon.level.drop(qwe2, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have2", qwe2.name()) ));
                break;

        }
        new Flare(6, 32).color(0xFFAA00, true).show(hero.sprite, 3.7f);
        Sample.INSTANCE.play(Assets.Sounds.CHARMS, 1f, 3f);
        GameScene.flash(0x80FFFFFF);
        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }

    @Override
    public int value() {
        return 45 * quantity;
    }
}
