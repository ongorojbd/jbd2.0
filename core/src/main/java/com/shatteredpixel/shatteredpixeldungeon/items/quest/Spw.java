/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.*;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.D4C;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedWand;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSadGhost;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Spw extends Item {

    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.SUPPLY_RATION;

        defaultAction = AC_LIGHT;
        unique = true;
        stackable = false;
    }

    public static void pickOrDropItem(Item item) {
        if (item.doPickUp(hero)) {
            GLog.i(Messages.capitalize(Messages.get(hero, "you_now_have", item.name())));
        } else {
            Dungeon.level.drop(item, hero.pos).sprite.drop();
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_LIGHT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_LIGHT)) {
            ShatteredPixelDungeon.scene().addToFront(new WndSpw(this));
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    private ArrayList<Item> rolledItem = new ArrayList<>();

    private static final String ROLLED_ITEM = "rolled_item";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (!rolledItem.isEmpty()) {
            bundle.put(ROLLED_ITEM, rolledItem);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        rolledItem.clear();
        if (bundle.contains(ROLLED_ITEM)) {
            rolledItem.addAll((Collection<Item>) ((Collection<?>) bundle.getCollection(ROLLED_ITEM)));
        }
    }

    public static class WndSpw extends Window {

        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 32;
        private static final int BTN_GAP = 5;
        private static final int GAP = 2;
        private static final int NUM_SPW = 3;

        public WndSpw(Spw cata) {

            IconTitle titlebar = new IconTitle();
            Spw spw = new Spw();
            titlebar.icon(new ItemSprite(cata));
            titlebar.label(Messages.titleCase(spw.name()));
            titlebar.setRect(0, 0, WIDTH, 0);
            add(titlebar);

            RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(Spw.class, "window_text"), 6);
            message.maxWidth(WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add(message);

            while (cata.rolledItem.size() < NUM_SPW) {
                Item newItem = null;
                boolean isDuplicate = false;

                int randomItem = Random.Int(12);
                if (spw1 > 7 && randomItem == 0) {
                    continue;
                } else if (spw2 > 7 && randomItem == 1) {
                    continue;
                } else if (spw3 > 7 && randomItem == 2) {
                    continue;
                } else if (spw4 > 7 && randomItem == 3) {
                    continue;
                } else if (spw5 > 7 && randomItem == 4) {
                    continue;
                } else if (spw6 > 7 && randomItem == 5) {
                    continue;
                } else if (spw7 > 7 && randomItem == 6) {
                    continue;
                } else if (wave <= 20 && randomItem == 8) {
                    continue;
                } else if (spw10 > 7 && randomItem == 9) {
                    continue;
                } else if (spw11 > 4 && randomItem == 10) {
                    continue;
                }

                switch (randomItem) {
                    case 0:
                        newItem = new Spw2();
                        break;
                    case 1:
                        newItem = new Spw2();
                        break;
                    case 2:
                        newItem = new Spw2();
                        break;
                    case 3:
                        newItem = new Spw2();
                        break;
                    case 4:
                        newItem = new Spw2();
                        break;
                    case 5:
                        newItem = new Spw2();
                        break;
                    case 6:
                        newItem = new Spw2();
                        break;
                    case 7:
                        newItem = new Spw2();
                        break;
                    case 8:
                        newItem = new Spw9();
                        break;
                    case 9:
                        newItem = new Spw10();
                        break;
                    case 10:
                        newItem = new Spw11();
                        break;
                    case 11:
                        newItem = new Spw12();
                        break;
                }

                for (Item existingItem : cata.rolledItem) {
                    if (existingItem.getClass().equals(newItem.getClass())) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    cata.rolledItem.add(newItem);
                }
            }

            for (int i = 0; i < NUM_SPW; i++) {
                Item item = cata.rolledItem.get(i);
                if ((item instanceof Spw1 && spw1 > 7) ||
                        (item instanceof Spw2 && spw2 > 7) ||
                        (item instanceof Spw3 && spw3 > 7) ||
                        (item instanceof Spw4 && spw4 > 7) ||
                        (item instanceof Spw5 && spw5 > 7) ||
                        (item instanceof Spw6 && spw6 > 7) ||
                        (item instanceof Spw7 && spw7 > 7) ||
                        (item instanceof Spw8 && spw8 > 7) ||
                        (item instanceof Spw9 && spw9 > 7)) {
                } else {
                    ItemButton btnReward = new ItemButton() {
                        @Override
                        protected void onClick() {
                            ShatteredPixelDungeon.scene().addToFront(new SpwRewardWindow(item(), cata));
                        }
                    };
                    if (item instanceof Spw1) {
                        btnReward.item(new Spw1());
                    } else if (item instanceof Spw2) {
                        btnReward.item(new Spw2());
                    } else if (item instanceof Spw4) {
                        btnReward.item(new Spw4());
                    } else if (item instanceof Spw5) {
                        btnReward.item(new Spw5());
                    } else if (item instanceof Spw6) {
                        btnReward.item(new Spw6());
                    } else if (item instanceof Spw7) {
                        btnReward.item(new Spw7());
                    } else if (item instanceof Spw8) {
                        btnReward.item(new Spw8());
                    } else if (item instanceof Spw9) {
                        btnReward.item(new Spw9());
                    }  else if (item instanceof Spw10) {
                        btnReward.item(new Spw10());
                    } else if (item instanceof Spw11) {
                        btnReward.item(new Spw11());
                    } else if (item instanceof Spw12) {
                        btnReward.item(new Spw12());
                    } else {
                        btnReward.item(item);
                    }

                    btnReward.setRect((i + 1) * (WIDTH - BTN_GAP) / NUM_SPW - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE);
                    add(btnReward);
                }
            }

            resize(WIDTH, (int) (message.top() + message.height() + 2 * BTN_GAP + BTN_SIZE));

        }

        @Override
        public void onBackPressed() {
            //do nothing
        }

        private class SpwRewardWindow extends WndInfoItem {

            public SpwRewardWindow(Item item, Spw cata) {
                super(item);

                RedButton btnConfirm = new RedButton(Messages.get(WndSadGhost.class, "confirm")) {
                    @Override
                    protected void onClick() {
                        SpwRewardWindow.this.hide();
                        WndSpw.this.hide();

                        if (item instanceof Spw1) {
                            if (spw1 == 0) {
                                Buff.affect(hero, EnhancedWeapon.class);
                                spw1++;
                            } else if (spw1 >= 1) spw1++;
                        } else if (item instanceof Spw2) {
                            if (spw2 == 0) {
                                Buff.affect(hero, EnhancedWand.class);
                                spw2++;
                            } else if (spw2 >= 1) spw2++;
                        } else if (item instanceof Spw3) {
                            if (spw3 == 0) {
                                Buff.affect(hero, EnhancedArmor.class);
                                spw3++;
                            } else if (spw3 >= 1) spw3++;
                        } else if (item instanceof Spw4) {
                            if (spw4 == 0) {
                                Item a = new RingOfSharpshooting();
                                a.identify();
                                pickOrDropItem(a);
                                spw4++;
                            } else if (spw4 >= 1 && Dungeon.hero.belongings.getItem(RingOfSharpshooting.class) != null) {
                                Spw4.Spw4Ability();
                            } else {
                                GLog.i(Messages.get(Spw.class, "spw4"));
                                return;
                            }
                        } else if (item instanceof Spw5) {
                            Item scrollOfEnchantment = new ScrollOfEnchantment();
                            scrollOfEnchantment.identify();
                            pickOrDropItem(scrollOfEnchantment);
                        } else if (item instanceof Spw6) {
                            pickOrDropItem(new Tbomb().quantity(3));
                            spw6++;
                        } else if (item instanceof Spw7) {
                            spw7++;
                        } else if (item instanceof Spw8) {
                            Spw8.Spw8Ability();
                        } else if (item instanceof Spw9) {
                            spw9++;
                        } else if (item instanceof Spw10) {
                            Spw10.Spw10Ability();
                            if (Statistics.spw10 < 8) Statistics.spw10++;
                        } else if (item instanceof Spw11) {
                            Spw11.Spw11Ability();
                            if (Statistics.spw11 < 8) Statistics.spw11++;
                        } else if (item instanceof Spw12) {
                            Spw12.Spw12Ability();
                        } else if (item.doPickUp(hero)) {
                            GLog.p(Messages.capitalize(Messages.get(hero, "you_now_have", item.name())));
                        } else {
                            Dungeon.level.drop(item, hero.pos).sprite.drop();
                        }
                        cata.detach(Dungeon.hero.belongings.backpack);
                        Sample.INSTANCE.play(Assets.Sounds.TALE);
//                        cata.rolledItem.clear();
                    }
                };
                btnConfirm.setRect(0, height + 2, width / 2 - 1, 16);
                add(btnConfirm);
                RedButton btnCancel = new RedButton(Messages.get(WndSadGhost.class, "cancel")) {
                    @Override
                    protected void onClick() {
                        hide();
                    }
                };
                btnCancel.setRect(btnConfirm.right() + 2, height + 2, btnConfirm.width(), 16);
                add(btnCancel);

                resize(width, (int) btnCancel.bottom());
            }
        }

    }
}
