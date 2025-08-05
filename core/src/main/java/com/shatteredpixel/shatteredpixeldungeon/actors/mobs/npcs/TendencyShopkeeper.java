/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Ram2;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Highway;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Speedwagon2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.CurrencyIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTendencyTradeItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;

public class TendencyShopkeeper extends NPC {

    {
        spriteClass = Speedwagon2Sprite.class;

        properties.add(Property.IMMOVABLE);
    }

    public static int MAX_BUYBACK_HISTORY = 3;
    public ArrayList<Item> buybackItems = new ArrayList<>();

    private int turnsSinceHarmed = -1;

    @Override
    protected boolean act() {

        if (turnsSinceHarmed >= 0) {
            turnsSinceHarmed++;
        }

        sprite.turnTo(pos, Dungeon.hero.pos);
        spend(TICK);
        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        processHarm();
    }

    @Override
    public boolean add(Buff buff) {
        if (buff.type == Buff.buffType.NEGATIVE) {
            processHarm();
        }
        return false;
    }

    public void processHarm() {

        //do nothing if the shopkeeper is out of the hero's FOV
        if (!Dungeon.level.heroFOV[pos]) {
            return;
        }

        if (turnsSinceHarmed == -1) {
            turnsSinceHarmed = 0;
            GLog.w(Messages.get(this, "warn"));

            //use a new actor as we can't clear the gas while we're in the middle of processing it
            Actor.add(new Actor() {
                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    //cleanses all harmful blobs in the shop
                    ArrayList<Blob> blobs = new ArrayList<>();
                    for (Class c : new BlobImmunity().immunities()) {
                        Blob b = Dungeon.level.blobs.get(c);
                        if (b != null && b.volume > 0) {
                            blobs.add(b);
                        }
                    }

                    PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 4);

                    for (int i = 0; i < Dungeon.level.length(); i++) {
                        if (PathFinder.distance[i] < Integer.MAX_VALUE) {

                            boolean affected = false;
                            for (Blob blob : blobs) {
                                if (blob.cur[i] > 0) {
                                    blob.clear(i);
                                    affected = true;
                                }
                            }

                            if (affected && Dungeon.level.heroFOV[i]) {
                                CellEmitter.get(i).burst(Speck.factory(Speck.DISCOVER), 2);
                            }

                        }
                    }
                    Actor.remove(this);
                    return true;
                }
            });

            //There is a 1 turn buffer before more damage/debuffs make the shopkeeper flee
            //This is mainly to prevent stacked effects from causing an instant flee
        } else if (turnsSinceHarmed >= 1) {
            flee();
        }
    }

    public void flee() {

        destroy();
        GLog.newLine();

        sprite.killAndErase();

        CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);

    }

    @Override
    public void destroy() {
        super.destroy();
        for (Heap heap : Dungeon.level.heaps.valueList()) {
            if (heap.type == Heap.Type.FOR_SALE) {
                CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
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

    // 판매 물품
    public static int sellPrice(Item item) {
        // 아이템별로 다른 가격 설정 - Tendency 모드에 맞게 조정
        if (item instanceof Potion) {
            return 40; // 포션은 중간 가격
        } else if (item instanceof Scroll) {
            return 40; // 스크롤은 중간 가격
        } else if (item instanceof Runestone) {
            return 30; // 룬석은 저렴
        } else if (item instanceof Ram2 || item instanceof Food) {
            return 40; // 식량은 저렴
        } else if (item instanceof TippedDart) {
            return 20; // 다트는 매우 저렴
        } else if (item instanceof Tbomb || item instanceof Bomb.DoubleTBomb) {
            return 20; // 폭탄은 비싸게
        } else if (item instanceof Alchemize) {
            return 30; // 연금술은 저렴
        } else if (item instanceof Highway) {
            return 10; // 고속도로는 매우 저렴
        } else if (item instanceof MeleeWeapon) {
            return 100; // 무기는 비싸게
        } else if (item instanceof Armor) {
            return 100; // 방어구는 비싸게
        } else if (item instanceof Wand) {
            return 100; // 지팡이는 매우 비싸게
        } else if (item instanceof Artifact) {
            return 100; // 아티팩트는 가장 비싸게
        } else {
            return 50; // 기본 가격
        }

    }

    public static WndBag sell() {
        return GameScene.selectItem(itemSelector);
    }

    public static boolean canSell(Item item) {
        if (item.value() <= 0) return false;
        if (item.unique && !item.stackable) return false;
        if (item instanceof Armor && ((Armor) item).checkSeal() != null) return false;
        if (item.isEquipped(Dungeon.hero) && item.cursed) return false;
        return true;
    }

    private static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(TendencyShopkeeper.class, "sell");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return TendencyShopkeeper.canSell(item);
        }

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                WndBag parentWnd = sell();
                GameScene.show(new WndTendencyTradeItem(item, parentWnd));
            }
        }
    };

    @Override
    public boolean interact(Char c) {

        if (spriteClass == ImpSprite.class) {
            Sample.INSTANCE.play(Assets.Sounds.DARBY);
        }

        if (c != Dungeon.hero) {
            return true;
        }
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                String[] options = new String[1 + buybackItems.size()];
                int maxLen = PixelScene.landscape() ? 30 : 25;
                int i = 0;
                options[i++] = Messages.get(TendencyShopkeeper.this, "sell");
                for (Item item : buybackItems) {
                    options[i] = Messages.get(Heap.class, "for_sale", item.value(), Messages.titleCase(item.title()));
                    if (options[i].length() > maxLen)
                        options[i] = options[i].substring(0, maxLen - 3) + "...";
                    i++;
                }
                CurrencyIndicator.showGold = true;
                GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), description(), options) {
                    @Override
                    protected void onSelect(int index) {
                        super.onSelect(index);
                        if (index == 0) {
                            sell();
                        } else if (index > 0) {
                            GLog.i(Messages.get(TendencyShopkeeper.this, "buyback"));
                            Item returned = buybackItems.remove(index - 1);
                            Dungeon.gold -= returned.value();
                            Statistics.goldCollected -= returned.value();
                            if (!returned.doPickUp(Dungeon.hero)) {
                                Dungeon.level.drop(returned, Dungeon.hero.pos);
                            }
                        }
                    }

                    @Override
                    protected boolean enabled(int index) {
                        if (index > 0) {
                            return Dungeon.gold >= buybackItems.get(index - 1).value();
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
                            return new ItemSprite(buybackItems.get(index - 1));
                        }
                        return null;
                    }

                    @Override
                    public void hide() {
                        super.hide();
                        CurrencyIndicator.showGold = false;
                    }
                });
            }
        });
        return true;
    }

    public static String BUYBACK_ITEMS = "buyback_items";

    public static String TURNS_SINCE_HARMED = "turns_since_harmed";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BUYBACK_ITEMS, buybackItems);
        bundle.put(TURNS_SINCE_HARMED, turnsSinceHarmed);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        buybackItems.clear();
        if (bundle.contains(BUYBACK_ITEMS)) {
            for (Bundlable i : bundle.getCollection(BUYBACK_ITEMS)) {
                buybackItems.add((Item) i);
            }
        }
        turnsSinceHarmed = bundle.contains(TURNS_SINCE_HARMED) ? bundle.getInt(TURNS_SINCE_HARMED) : -1;
    }
}