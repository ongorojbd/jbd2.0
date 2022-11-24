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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ThirdBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Sleepcmoon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDestOrb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
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


        switch (Random.Int(23)){
            case 0:
                Item a = new BossdiscC();
                Dungeon.level.drop(a, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 1:
                Item b = Generator.random( Generator.Category.WEP_T5 );
                Dungeon.level.drop(b, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 2:
                Item c = new PlateArmor();
                Dungeon.level.drop(c, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 3:
                Item d = new Gold().quantity(150 * Dungeon.depth);
                Dungeon.level.drop(d, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 4:
                Item e = new ScrollOfPolymorph();
                Dungeon.level.drop(e, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 5:
                Item f = new PotionOfShielding().identify().quantity(3);
                Dungeon.level.drop(f, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 6:
                Item g = new SummonElemental().quantity(3);
                Dungeon.level.drop(g, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 7:
                Item h = new ThirdBomb();
                Dungeon.level.drop(h, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 8:
                Item i = new ChaosCatalyst();
                Dungeon.level.drop(i, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 9:
                Item j = new AdvancedEvolution();
                Dungeon.level.drop(j, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 10:
                Item k = new Mdisc();
                Dungeon.level.drop(k, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 11:
                Item h2 = new Blandfruit().quantity(3);
                Dungeon.level.drop(h2, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 12:
                Item h3 = new WandOfDestOrb();
                Dungeon.level.drop(h3, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 13:
                Item h4 = new ScrollOfEnchantment().quantity(3);
                Dungeon.level.drop(h4, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 14:
                Item h5 = new Bomb().quantity(6);
                Dungeon.level.drop(h5, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 15:
                Item h6 = new Sleepcmoon();
                Dungeon.level.drop(h6, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 16:
                Item f0 = new FeatherFall().quantity(3);
                Dungeon.level.drop(f0, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 17:
                Item f1 = new AquaBlast().quantity(15);
                Dungeon.level.drop(f1, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 18:
                Item f2 = new PotionOfDivineInspiration().quantity(3);
                Dungeon.level.drop(f2, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 19:
                Item f3 = new PotionOfCorrosiveGas().quantity(3);
                Dungeon.level.drop(f3, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 20:
                Item f4 = new CausticBrew().quantity(3);
                Dungeon.level.drop(f4, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 21:
                Item f5 = new BlizzardBrew().quantity(3);
                Dungeon.level.drop(f5, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
            case 22:
                Item f6 = new InfernalBrew().quantity(3);
                Dungeon.level.drop(f6, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                break;
        }

        new Flare(6, 32).color(0xFFAA00, true).show(hero.sprite, 3.7f);
        Sample.INSTANCE.play(Assets.Sounds.CHARMS, 1f, 3f);
        GLog.p(Messages.get(this, "a"));
        GameScene.flash(0x80FFFFFF);


        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }

    @Override
    public int value() {
        return 100 * quantity;
    }
}
