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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb2;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw1;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw10;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw11;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfExtract;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfShock;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NewShopKeeper extends NPC {

    {
        spriteClass = VampireSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    public ArrayList<Item> itemsToSpawn = new ArrayList<>();

    @Override
    public boolean isImmune(Class effect) {
        return true;
    }

    @Override
    protected boolean act() {
        sprite.turnTo(pos, Dungeon.hero.pos);
        spend(TICK);
        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {

    }

    @Override
    public boolean interact(Char c) {

        if (c != Dungeon.hero) {
            return true;
        }

        if (Statistics.zombiecount == 2) {
            itemsToSpawn.clear();
            Statistics.zombiecount = 0;
        }

        if (Statistics.zombiecount == 0) {
            Set<Integer> usedCases = new HashSet<>();

            while (itemsToSpawn.size() < 5) {
                Item newItem = null;
                int randomItem;

                do {
                    randomItem = Random.Int(20);
                } while (usedCases.contains(randomItem));

                usedCases.add(randomItem);

                switch (randomItem) {
                    case 0:
                        newItem = new PotionOfHealing().identify();
                        break;
                    case 1:
                        newItem = new Stylus();
                        break;
                    case 2:
                        newItem = new PotionOfExperience().identify();
                        break;
                    case 3:
                        newItem = new ScrollOfRemoveCurse().identify();
                        break;
                    case 4:
                        newItem = new PotionOfHaste().identify();
                        break;
                    case 5:
                        newItem = Generator.random(Generator.wepTiers[1]).identify();
                        break;
                    case 6:
                        newItem = Generator.random(Generator.misTiers[1]).quantity(2).identify();
                        break;
                    case 7:
                        newItem = new ChaosCatalyst().identify();
                        break;
                    case 8:
                        newItem = new Kingc().identify();
                        break;
                    case 9:
                        newItem = new UnstableBrew().identify();
                        break;
                    case 10:
                        newItem = new ScrollOfRecharging().identify();
                        break;
                    case 11:
                        newItem = new StoneOfShock().identify();
                        break;
                    case 12:
                        newItem = new PotionOfEarthenArmor().identify();
                        break;
                    case 13:
                        newItem = new MagicalInfusion().identify();
                        break;
                    case 14:
                        newItem = Generator.random(Generator.Category.ARMOR).identify();
                        break;
                    case 15:
                        newItem = new Spw10();
                        break;
                    case 16:
                        newItem = new Spw11();
                        break;
                    case 17:
                        newItem = new Tbomb();
                        break;
                    case 18:
                        newItem = new Tbomb().quantity(2);
                        break;
                    case 19:
                        newItem = new Tbomb2();
                        break;
                }

                itemsToSpawn.add(newItem);

            }
        }

        Statistics.zombiecount = 1;

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                String[] options = new String[1 + itemsToSpawn.size()];
                int maxLen = PixelScene.landscape() ? 30 : 25;
                int i = 0;
                options[i++] = Messages.get(NewShopKeeper.this, "talk");
                for (Item item : itemsToSpawn) {
                    options[i] = Messages.get(Heap.class, "for_sale", item.value(), Messages.titleCase(item.title()));
                    if (options[i].length() > maxLen)
                        options[i] = options[i].substring(0, maxLen - 3) + "...";
                    i++;
                }
                GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), Messages.get(NewShopKeeper.class, "gold", Dungeon.gold), options) {

                    @Override
                    public void onBackPressed() {
                        //do nothing
                    }

                    @Override
                    protected void onSelect(int index) {
                        super.onSelect(index);
                        if (index == 0) {

                        } else if (index > 0) {
                            Item returned = itemsToSpawn.remove(index - 1);
                            Dungeon.gold -= returned.value();
                            GLog.i(Messages.get(NewShopKeeper.this, "buy", returned.name()));
                            if (returned instanceof ScrollOfRecharging) {
                                Buff.affect(hero, Recharging.class, Recharging.DURATION);
                            } else if (returned instanceof Spw10) {
                                Spw10.Spw10Ability();
                            } else if (returned instanceof Spw11) {
                                Spw11.Spw11Ability();
                            } else {
                                if (!returned.doPickUp(Dungeon.hero)) {
                                    Dungeon.level.drop(returned, Dungeon.hero.pos);
                                }
                            }
                            interact(hero);
                        }
                    }

                    @Override
                    protected boolean enabled(int index) {
                        if (index > 0) {
                            return Dungeon.gold >= itemsToSpawn.get(index - 1).value();
                        } else {
                            return super.enabled(index);
                        }
                    }

                    @Override
                    protected boolean hasIcon(int index) {
                        return index > 0;
                    }

                    @Override
                    protected Image getIcon(int index) {
                        if (index > 0) {
                            return new ItemSprite(itemsToSpawn.get(index - 1));
                        }
                        return null;
                    }
                });
            }
        });
        return true;
    }


    @Override
    public boolean add(Buff buff) {
        if (super.add(buff)) {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Heap heap : level.heaps.valueList()) {
            if (heap.type == Heap.Type.FOR_SALE) {
                if (ShatteredPixelDungeon.scene() instanceof GameScene) {
                    CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
                }
                if (heap.size() == 1) {
                    heap.destroy();
                } else {
                    heap.items.remove(heap.size() - 1);
                    heap.type = Heap.Type.HEAP;
                }
            }
        }
    }

    @Override
    public boolean reset() {
        return true;
    }

    public static String ITEMSTOSPAWN = "itemsToSpawn";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEMSTOSPAWN, itemsToSpawn);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        itemsToSpawn.clear();
        if (bundle.contains(ITEMSTOSPAWN)) {
            for (Bundlable i : bundle.getCollection(ITEMSTOSPAWN)) {
                itemsToSpawn.add((Item) i);
            }
        }
    }


}