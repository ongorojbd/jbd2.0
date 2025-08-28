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
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw1;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw13;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw14;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw15;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw16;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw17;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw18;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw19;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw2;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw20;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw22;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw23;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw24;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw25;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw26;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw27;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw28;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw29;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw3;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw30;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw4;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw31;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw32;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw33;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw34;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw35;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw36;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw37;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw38;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw39;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw40;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw6;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw7;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw9;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedWand;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Holy1;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Holy2;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Holy3;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Triplespeed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TrialOfPillars;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSadGhost;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 특수 파워 무기 (SPW) 아이템 - 영웅에게 다양한 강화 효과를 제공
 * 플레이어가 랜덤으로 생성된 파워업 중에서 선택할 수 있음
 */
public class Spw extends Item {

    // SPW 사용을 위한 액션 상수
    public static final String AC_LIGHT = "LIGHT";

    // 롤된 아이템들을 저장하기 위한 번들 키
    private static final String ROLLED_ITEM = "rolled_item";

    // 롤할 수 있는 최대 아이템 수
    private static final int MAX_ROLLED_ITEMS = 3;

    // 대부분의 SPW 아이템이 사용 불가능해지는 최대 카운트
    private static final int MAX_SPW_COUNT = 7;

    // 특정 아이템에 필요한 최소 웨이브 요구사항
    private static final int MIN_WAVE_FOR_SPECIAL = 20;

    // 이 SPW 인스턴스에 대해 롤된 아이템들의 리스트
    private ArrayList<Item> rolledItems = new ArrayList<>();

    {
        image = ItemSpriteSheet.SUPPLY_RATION;
        defaultAction = AC_LIGHT;
        unique = true;
        stackable = false;
    }

    /**
     * 아이템을 주우거나 인벤토리가 가득 찬 경우 드롭하는 헬퍼 메서드
     */
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

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (!rolledItems.isEmpty()) {
            bundle.put(ROLLED_ITEM, rolledItems);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        rolledItems.clear();
        if (bundle.contains(ROLLED_ITEM)) {
            rolledItems.addAll((Collection<Item>) ((Collection<?>) bundle.getCollection(ROLLED_ITEM)));
        }
    }

    /**
     * SPW 옵션을 표시하고 아이템 선택을 처리하는 윈도우
     */
    public static class WndSpw extends Window {

        // UI 상수들
        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 32;
        private static final int BTN_GAP = 5;
        private static final int GAP = 2;

        // 가독성을 위한 아이템 타입 상수들
        private static final int ITEM_TYPE_SPW1 = 0;
        private static final int ITEM_TYPE_SPW2 = 1;
        private static final int ITEM_TYPE_SPW3 = 2;
        private static final int ITEM_TYPE_SPW4 = 3;
        private static final int ITEM_TYPE_SPW5 = 4;
        private static final int ITEM_TYPE_SPW6 = 5;
        private static final int ITEM_TYPE_SPW7 = 6;
        private static final int ITEM_TYPE_SPW8 = 7;
        private static final int ITEM_TYPE_SPW9 = 8;
        private static final int ITEM_TYPE_SPW10 = 9;
        private static final int ITEM_TYPE_SPW11 = 10;
        private static final int ITEM_TYPE_SPW12 = 11;
        private static final int ITEM_TYPE_SPW13 = 12;
        private static final int ITEM_TYPE_SPW14 = 13;
        private static final int ITEM_TYPE_SPW15 = 14;
        private static final int ITEM_TYPE_SPW16 = 15;
        private static final int ITEM_TYPE_SPW17 = 16;
        private static final int ITEM_TYPE_SPW18 = 17;
        private static final int ITEM_TYPE_SPW19 = 18;
        private static final int ITEM_TYPE_SPW20 = 19;
        private static final int ITEM_TYPE_SPW21 = 20;
        private static final int ITEM_TYPE_SPW22 = 21;
        private static final int ITEM_TYPE_SPW23 = 22;
        private static final int ITEM_TYPE_SPW24 = 23;
        private static final int ITEM_TYPE_SPW25 = 24;
        private static final int ITEM_TYPE_SPW26 = 25;
        private static final int ITEM_TYPE_SPW27 = 26;
        private static final int ITEM_TYPE_SPW28 = 27;
        private static final int ITEM_TYPE_SPW29 = 28;
        private static final int ITEM_TYPE_SPW30 = 29;
        private static final int ITEM_TYPE_SPW31 = 30;
        private static final int ITEM_TYPE_SPW32 = 31;
        private static final int ITEM_TYPE_SPW33 = 32;
        private static final int ITEM_TYPE_SPW34 = 33;
        private static final int ITEM_TYPE_SPW35 = 34;
        private static final int ITEM_TYPE_SPW36 = 35;
        private static final int ITEM_TYPE_SPW37 = 36;
        private static final int ITEM_TYPE_SPW38 = 37;
        private static final int ITEM_TYPE_SPW39 = 38;
        private static final int ITEM_TYPE_SPW40 = 39;

        // 가능한 아이템 타입의 총 개수
        private static final int TOTAL_ITEM_TYPES = 40;

        public WndSpw(Spw spw) {
            IconTitle titlebar = createTitleBar(spw);
            RenderedTextBlock message = createMessage(titlebar);
            generateRolledItems(spw);

            // 아이템이 하나도 없는 경우 처리
            if (spw.rolledItems.isEmpty()) {
                RenderedTextBlock noItemsMessage = PixelScene.renderTextBlock(Messages.get(Spw.class, "no_items_available"), 6);
                noItemsMessage.maxWidth(WIDTH);
                noItemsMessage.setPos(0, message.bottom() + GAP);
                add(noItemsMessage);
                resize(WIDTH, (int) noItemsMessage.bottom() + GAP);
                return;
            }

            createItemButtons(spw, titlebar, message);
            resizeWindow(titlebar, message);
        }

        /**
         * 윈도우의 타이틀 바를 생성
         */
        private IconTitle createTitleBar(Spw spw) {
            IconTitle titlebar = new IconTitle();
            titlebar.icon(new ItemSprite(spw));
            titlebar.label(Messages.titleCase(spw.name()));
            titlebar.setRect(0, 0, WIDTH, 0);
            add(titlebar);
            return titlebar;
        }

        /**
         * 메시지 텍스트 블록을 생성
         */
        private RenderedTextBlock createMessage(IconTitle titlebar) {
            RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(Spw.class, "window_text"), 6);
            message.maxWidth(WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add(message);
            return message;
        }

        /**
         * 아직 생성되지 않은 경우 SPW를 위한 랜덤 아이템들을 생성
         */
        private void generateRolledItems(Spw spw) {
            // 먼저 사용 가능한 모든 아이템들을 수집
            ArrayList<Item> availableItems = new ArrayList<>();
            for (int i = 0; i < TOTAL_ITEM_TYPES; i++) {
                Item newItem = createItemByType(i);
                if (newItem != null && isItemAvailable(newItem)) {
                    availableItems.add(newItem);
                }
            }

            // 사용 가능한 아이템이 없으면 빈 리스트로 남겨둠
            if (availableItems.isEmpty()) {
                return;
            }

            // 사용 가능한 아이템들 중에서 랜덤하게 선택
            int attempts = 0;
            int maxAttempts = availableItems.size() * 10; // 무한 루프 방지

            while (spw.rolledItems.size() < MAX_ROLLED_ITEMS && attempts < maxAttempts) {
                Item randomItem = availableItems.get(Random.Int(availableItems.size()));

                if (!isDuplicate(spw.rolledItems, randomItem)) {
                    spw.rolledItems.add(randomItem);
                }
                attempts++;
            }

            // 충분한 아이템을 생성하지 못한 경우, 사용 가능한 아이템을 반복해서 추가
            while (spw.rolledItems.size() < MAX_ROLLED_ITEMS) {
                boolean addedAny = false;
                for (Item item : availableItems) {
                    if (spw.rolledItems.size() >= MAX_ROLLED_ITEMS) break;
                    if (!isDuplicate(spw.rolledItems, item)) {
                        spw.rolledItems.add(item);
                        addedAny = true;
                    }
                }

                // 더 이상 추가할 수 있는 아이템이 없으면 중단
                if (!addedAny) {
                    break;
                }
            }
        }

        /**
         * 현재 게임 상태를 기반으로 랜덤 아이템을 생성
         * (더 이상 사용되지 않지만 호환성을 위해 유지)
         */
        private Item generateRandomItem() {
            int attempts = 0;
            int maxAttempts = TOTAL_ITEM_TYPES * 5;

            while (attempts < maxAttempts) {
                int randomItem = Random.Int(TOTAL_ITEM_TYPES);

                // Skip items that have reached their maximum count
                if (!shouldSkipItem(randomItem)) {
                    Item item = createItemByType(randomItem);
                    if (item != null && isItemAvailable(item)) {
                        return item;
                    }
                }
                attempts++;
            }

            return null;
        }

        /**
         * 현재 통계를 기반으로 아이템을 건너뛰어야 하는지 확인
         * (아이템 생성 단계에서 사용 - 특정 조건을 만족하지 않으면 아이템 자체를 생성하지 않음)
         */
        private boolean shouldSkipItem(int itemType) {
            switch (itemType) {
                case ITEM_TYPE_SPW1:
                    return spw1 >= 3;
                case ITEM_TYPE_SPW2:
                    return spw2 > MAX_SPW_COUNT;
                case ITEM_TYPE_SPW3:
                    return spw3 >= 3;
                case ITEM_TYPE_SPW4:
                    return spw4 > MAX_SPW_COUNT;
                case ITEM_TYPE_SPW7:
                    return spw7 > MAX_SPW_COUNT;
                case ITEM_TYPE_SPW9:
                    return spw9 >= 3;
                case ITEM_TYPE_SPW13:
                    return spw13 >= 6; // SPW13은 최대 6까지만
                case ITEM_TYPE_SPW14:
                    return spw14 >= 1;
                case ITEM_TYPE_SPW15:
                    return spw15 >= 1;
                case ITEM_TYPE_SPW16:
                    return spw16 >= 1;
                case ITEM_TYPE_SPW17:
                    return spw17 >= 1;
                case ITEM_TYPE_SPW18:
                    return spw18 >= 1;
                case ITEM_TYPE_SPW19:
                    return spw19 >= 1;
                case ITEM_TYPE_SPW22:
                    return spw22 > MAX_SPW_COUNT;
                case ITEM_TYPE_SPW23:
                    return spw23 >= 2;
                case ITEM_TYPE_SPW24:
                    return spw24 >= 6;
                case ITEM_TYPE_SPW26:
                    return spw26 > MAX_SPW_COUNT;
                case ITEM_TYPE_SPW27:
                    return spw27 >= 2;
                case ITEM_TYPE_SPW28:
                    return spw28 >= 2;
                case ITEM_TYPE_SPW29:
                    return spw29 >= 5;
                case ITEM_TYPE_SPW30:
                    return spw30 >= 2;
                case ITEM_TYPE_SPW32:
                    return spw32 >= 2;
                case ITEM_TYPE_SPW33:
                    return spw33 >= 2;
                case ITEM_TYPE_SPW34:
                    return spw34 >= 2;
                case ITEM_TYPE_SPW35:
                    // TrialOfPillars 버프 중에는 출현 금지
                    return Dungeon.hero.buff(TrialOfPillars.class) != null;
                case ITEM_TYPE_SPW36:
                    return spw36 >= 5;
                case ITEM_TYPE_SPW37:
                    return spw37 > MAX_SPW_COUNT;
                case ITEM_TYPE_SPW39:
                    return spw39 > MAX_SPW_COUNT;
                case ITEM_TYPE_SPW40:
                    return spw40 > MAX_SPW_COUNT;
                default:
                    return false;
            }
        }

        /**
         * 주어진 타입에 따라 아이템 인스턴스를 생성(SPW 테스트)
         */
        private Item createItemByType(int itemType) {
            switch (itemType) {
                case ITEM_TYPE_SPW1:
                    return new Spw1();
                case ITEM_TYPE_SPW2:
                    return new Spw2();
                case ITEM_TYPE_SPW3:
                    return new Spw3();
                case ITEM_TYPE_SPW4:
                    return new Spw4();
                case ITEM_TYPE_SPW5:
                    return new Spw5();
                case ITEM_TYPE_SPW6:
                    return new Spw6();
                case ITEM_TYPE_SPW7:
                    return new Spw7();
                case ITEM_TYPE_SPW8:
                    return new Spw8();
                case ITEM_TYPE_SPW9:
                    return new Spw9();
                case ITEM_TYPE_SPW10:
                    return new Spw10();
                case ITEM_TYPE_SPW11:
                    return new Spw11();
                case ITEM_TYPE_SPW12:
                    return new Spw12();
                case ITEM_TYPE_SPW13:
                    return new Spw13();
                case ITEM_TYPE_SPW14:
                    return new Spw14();
                case ITEM_TYPE_SPW15:
                    return new Spw15();
                case ITEM_TYPE_SPW16:
                    return new Spw16();
                case ITEM_TYPE_SPW17:
                    return new Spw17();
                case ITEM_TYPE_SPW18:
                    return new Spw18();
                case ITEM_TYPE_SPW19:
                    return new Spw19();
                case ITEM_TYPE_SPW20:
                    return new Spw20();
                case ITEM_TYPE_SPW21:
                    return new Spw21();
                case ITEM_TYPE_SPW22:
                    return new Spw22();
                case ITEM_TYPE_SPW23:
                    return new Spw23();
                case ITEM_TYPE_SPW24:
                    return new Spw24();
                case ITEM_TYPE_SPW25:
                    return new Spw25();
                case ITEM_TYPE_SPW26:
                    return new Spw26();
                case ITEM_TYPE_SPW27:
                    return new Spw27();
                case ITEM_TYPE_SPW28:
                    return new Spw28();
                case ITEM_TYPE_SPW29:
                    return new Spw29();
                case ITEM_TYPE_SPW30:
                    return new Spw30();
                case ITEM_TYPE_SPW31:
                    return new Spw31();
                case ITEM_TYPE_SPW32:
                    return new Spw32();
                case ITEM_TYPE_SPW33:
                    return new Spw33();
                case ITEM_TYPE_SPW34:
                    return new Spw34();
                case ITEM_TYPE_SPW35:
                    return new Spw35();
                case ITEM_TYPE_SPW36:
                    return new Spw36();
                case ITEM_TYPE_SPW37:
                    return new Spw37();
                case ITEM_TYPE_SPW38:
                    return new Spw38();
                case ITEM_TYPE_SPW39:
                    return new Spw39();
                case ITEM_TYPE_SPW40:
                    return new Spw40();
                default:
                    return null;
            }
        }


        /**
         * 아이템이 기존 아이템들과 중복되는지 확인
         */
        private boolean isDuplicate(ArrayList<Item> existingItems, Item newItem) {
            for (Item item : existingItems) {
                if (item.getClass().equals(newItem.getClass())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 각 롤된 아이템에 대한 버튼들을 생성
         */
        private void createItemButtons(Spw spw, IconTitle titlebar, RenderedTextBlock message) {
            int buttonIndex = 0;
            for (int i = 0; i < spw.rolledItems.size() && buttonIndex < MAX_ROLLED_ITEMS; i++) {
                Item item = spw.rolledItems.get(i);

                if (isItemAvailable(item)) {
                    ItemButton btnReward = createItemButton(item, spw);
                    positionButton(btnReward, buttonIndex, titlebar, message);
                    add(btnReward);
                    buttonIndex++;
                }
            }
        }

        /**
         * 아이템이 선택 가능한지 확인
         * (UI 표시 단계에서 사용 - 이미 생성된 아이템이 화면에 표시되어야 하는지 결정)
         */
        private boolean isItemAvailable(Item item) {
            if (item == null) return false;

            return !((item instanceof Spw1 && spw1 >= 3) ||
                    (item instanceof Spw2 && spw2 > MAX_SPW_COUNT) ||
                    (item instanceof Spw3 && spw3 >= 3) ||
                    (item instanceof Spw4 && spw4 > MAX_SPW_COUNT) ||
                    (item instanceof Spw7 && spw7 > MAX_SPW_COUNT) ||
                    (item instanceof Spw7 && spw9 >= 3) ||
                    (item instanceof Spw13 && spw13 >= 6) ||
                    (item instanceof Spw14 && spw14 >= 1) ||
                    (item instanceof Spw15 && spw15 >= 1) ||
                    (item instanceof Spw16 && spw16 >= 1) ||
                    (item instanceof Spw17 && spw17 >= 1) ||
                    (item instanceof Spw18 && spw18 >= 1) ||
                    (item instanceof Spw19 && spw19 >= 1) ||
                    (item instanceof Spw22 && spw22 > MAX_SPW_COUNT) ||
                    (item instanceof Spw23 && spw23 >= 2) ||
                    (item instanceof Spw24 && spw24 >= 6) ||
                    (item instanceof Spw26 && spw26 > MAX_SPW_COUNT) ||
                    (item instanceof Spw27 && spw27 >= 2) ||
                    (item instanceof Spw28 && spw28 >= 2) ||
                    (item instanceof Spw29 && spw29 >= 5) ||
                    (item instanceof Spw30 && spw30 >= 2) ||
                    (item instanceof Spw32 && spw32 >= 2) ||
                    (item instanceof Spw33 && spw33 >= 2) ||
                    (item instanceof Spw34 && spw34 >= 2) ||
                    (item instanceof Spw35 && Dungeon.hero.buff(TrialOfPillars.class) != null) ||
                    (item instanceof Spw36 && spw36 >= 5) ||
                    (item instanceof Spw37 && spw37 > MAX_SPW_COUNT) ||
                    (item instanceof Spw38 && spw38 > MAX_SPW_COUNT) ||
                    (item instanceof Spw39 && spw39 > MAX_SPW_COUNT) ||
                    (item instanceof Spw40 && spw40 > MAX_SPW_COUNT));
        }

        /**
         * 클릭 핸들러가 있는 아이템 버튼을 생성
         */
        private ItemButton createItemButton(Item item, Spw spw) {
            ItemButton btnReward = new ItemButton() {
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(new SpwRewardWindow(item(), spw));
                }
            };

            btnReward.item(item);
            return btnReward;
        }

        /**
         * 그리드 레이아웃에서 버튼의 위치를 설정
         */
        private void positionButton(ItemButton button, int index, IconTitle titlebar, RenderedTextBlock message) {
            button.setRect(
                    (index + 1) * (WIDTH - BTN_GAP) / MAX_ROLLED_ITEMS - BTN_SIZE,
                    message.top() + message.height() + BTN_GAP,
                    BTN_SIZE,
                    BTN_SIZE
            );
        }

        /**
         * 모든 콘텐츠에 맞게 윈도우 크기를 조정
         */
        private void resizeWindow(IconTitle titlebar, RenderedTextBlock message) {
            resize(WIDTH, (int) (message.top() + message.height() + 2 * BTN_GAP + BTN_SIZE));
        }

        @Override
        public void onBackPressed() {
            // 뒤로가기 버튼으로 닫히는 것을 방지
        }

        /**
         * 아이템 선택을 확인하고 효과를 적용하는 윈도우
         */
        private class SpwRewardWindow extends WndInfoItem {

            public SpwRewardWindow(Item item, Spw spw) {
                super(item);
                createButtons(item, spw);
            }

            /**
             * 확인 및 취소 버튼들을 생성
             */
            private void createButtons(Item item, Spw spw) {
                RedButton btnConfirm = new RedButton(Messages.get(WndSadGhost.class, "confirm")) {
                    @Override
                    protected void onClick() {
                        handleItemSelection(item, spw);
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

            /**
             * 아이템 선택을 처리하고 효과를 적용
             */
            private void handleItemSelection(Item item, Spw spw) {
                SpwRewardWindow.this.hide();
                WndSpw.this.hide();

                applyItemEffect(item);
                spw.detach(Dungeon.hero.belongings.backpack);
                Sample.INSTANCE.play(Assets.Sounds.TALE);
            }

            /**
             * 선택된 아이템의 효과를 적용
             */
            private void applyItemEffect(Item item) {
                if (item instanceof Spw1) {
                    handleSpw1Effect();
                } else if (item instanceof Spw2) {
                    handleSpw2Effect();
                } else if (item instanceof Spw3) {
                    handleSpw3Effect();
                } else if (item instanceof Spw4) {
                    handleSpw4Effect();
                } else if (item instanceof Spw5) {
                    handleSpw5Effect();
                } else if (item instanceof Spw6) {
                    handleSpw6Effect();
                } else if (item instanceof Spw7) {
                    spw7++;
                } else if (item instanceof Spw8) {
                    Spw8.Spw8Ability();
                } else if (item instanceof Spw9) {
                    Spw9.Spw9Ability();
                } else if (item instanceof Spw10) {
                    handleSpw10Effect();
                } else if (item instanceof Spw11) {
                    handleSpw11Effect();
                } else if (item instanceof Spw12) {
                    Spw12.Spw12Ability();
                } else if (item instanceof Spw13) {
                    spw13++;
                } else if (item instanceof Spw14) {
                    spw14++;
                    Buff.affect(hero, Holy1.class);
                } else if (item instanceof Spw15) {
                    spw15++;
                    Buff.affect(hero, Holy2.class);
                } else if (item instanceof Spw16) {
                    spw16++;
                    Buff.affect(hero, Holy3.class);
                } else if (item instanceof Spw17) {
                    spw17++;
                    Jojo1 jojo1 = new Jojo1();
                    if (jojo1.doPickUp(Dungeon.hero)) {
                        GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", jojo1.name())));
                    } else {
                        Dungeon.level.drop(jojo1, Dungeon.hero.pos).sprite.drop();
                    }
                } else if (item instanceof Spw18) {
                    spw18++;
                    Jojo2 jojo2 = new Jojo2();
                    if (jojo2.doPickUp(Dungeon.hero)) {
                        GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", jojo2.name())));
                    } else {
                        Dungeon.level.drop(jojo2, Dungeon.hero.pos).sprite.drop();
                    }
                } else if (item instanceof Spw19) {
                    spw19++;
                    Jojo3 jojo3 = new Jojo3();
                    if (jojo3.doPickUp(Dungeon.hero)) {
                        GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", jojo3.name())));
                    } else {
                        Dungeon.level.drop(jojo3, Dungeon.hero.pos).sprite.drop();
                    }
                } else if (item instanceof Spw20) {
                    handleSpw20Effect();
                } else if (item instanceof Spw21) {
                    Spw21.Spw21Ability();
                } else if (item instanceof Spw25) {
                    Buff.prolong(hero, Triplespeed.class, 100f);
                } else if (item instanceof Spw26) {
                    spw26++;
                } else if (item instanceof Spw31) {
                    spw31++;
                    hero.STR++;
                    hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.STRENGTH);
                } else if (item instanceof Spw35) {
                    // 지옥승주의 시련 시작
                    spw35++;
                    Buff.affect(hero, TrialOfPillars.class);
                    GLog.w(Messages.get(Spw.class, "spw35_start"));
                } else if (item instanceof Spw37) {
                    handleSpw37Effect();
                } else if (item instanceof Spw38) {
                    Item potion = new PotionOfExperience();
                    potion.identify().quantity(2);
                    pickOrDropItem(potion);
                } else if (item instanceof Spw39) {
                    spw39++;
                } else if (item instanceof Spw40) {
                    spw40++;
                } else {
                    handleGenericItem(item);
                }
            }

            // 더 나은 구성을 위한 개별 효과 핸들러들
            private void handleSpw1Effect() {
                if (spw1 == 0) {
                    Buff.affect(hero, EnhancedWeapon.class);
                }
                spw1++;
            }

            private void handleSpw2Effect() {
                if (spw2 == 0) {
                    Buff.affect(hero, EnhancedWand.class);
                }
                spw2++;
            }

            private void handleSpw3Effect() {
                if (spw3 == 0) {
                    Buff.affect(hero, EnhancedArmor.class);
                }
                spw3++;
            }

            private void handleSpw4Effect() {
                if (spw4 == 0) {
                    Item ring = new RingOfSharpshooting();
                    ring.identify();
                    pickOrDropItem(ring);
                    spw4++;
                } else if (spw4 >= 1 && Dungeon.hero.belongings.getItem(RingOfSharpshooting.class) != null) {
                    Spw4.Spw4Ability();
                } else {
                    GLog.i(Messages.get(Spw.class, "spw4"));
                }
            }

            private void handleSpw5Effect() {
                // 스크롤을 직접 주는 대신 인챈트 선택 윈도우를 바로 열 수 있음
                // 예시: 무기나 방어구를 선택해서 인챈트/글리프 적용
                GameScene.selectItem(new WndBag.ItemSelector() {
                    @Override
                    public String textPrompt() {
                        return Messages.get(ScrollOfEnchantment.class, "inv_title");
                    }

                    @Override
                    public Class<? extends Bag> preferredBag() {
                        return Belongings.Backpack.class;
                    }

                    @Override
                    public boolean itemSelectable(Item item) {
                        return (item instanceof MeleeWeapon || item instanceof SpiritBow || item instanceof Armor);
                    }

                    @Override
                    public void onSelect(final Item item) {
                        if (item instanceof Weapon) {
                            final Weapon.Enchantment enchants[] = new Weapon.Enchantment[3];
                            Class<? extends Weapon.Enchantment> existing = ((Weapon) item).enchantment != null ? ((Weapon) item).enchantment.getClass() : null;
                            enchants[0] = Weapon.Enchantment.randomCommon(existing);
                            enchants[1] = Weapon.Enchantment.randomUncommon(existing);
                            enchants[2] = Weapon.Enchantment.random(existing, enchants[0].getClass(), enchants[1].getClass());
                            GameScene.show(new Spw5.SpwEnchantSelect((Weapon) item, enchants[0], enchants[1], enchants[2]));
                        } else if (item instanceof Armor) {
                            final Armor.Glyph glyphs[] = new Armor.Glyph[3];
                            Class<? extends Armor.Glyph> existing = ((Armor) item).glyph != null ? ((Armor) item).glyph.getClass() : null;
                            glyphs[0] = Armor.Glyph.randomCommon(existing);
                            glyphs[1] = Armor.Glyph.randomUncommon(existing);
                            glyphs[2] = Armor.Glyph.random(existing, glyphs[0].getClass(), glyphs[1].getClass());
                            GameScene.show(new Spw5.SpwGlyphSelect((Armor) item, glyphs[0], glyphs[1], glyphs[2]));
                        }
                    }
                });
            }

            private void handleSpw6Effect() {
                // spw6가 0인 경우에도 기본 폭탄 3개 제공
                Tbomb tbomb = new Tbomb();
                tbomb.quantity(3);
                pickOrDropItem(tbomb);
                spw6++;
            }

            private void handleSpw10Effect() {
                Spw10.Spw10Ability();
            }

            private void handleSpw11Effect() {
                Spw11.Spw11Ability();
            }

            private void handleSpw20Effect() {
                UV uv = new UV();
                pickOrDropItem(uv);
            }

            private void handleSpw37Effect() {
                if (spw37 == 0) {
                    Item ring = new RingOfArcana();
                    ring.identify();
                    pickOrDropItem(ring);
                    spw37++;
                } else if (spw37 >= 1 && Dungeon.hero.belongings.getItem(RingOfArcana.class) != null) {
                    Spw37.Spw37Ability();
                } else {
                    GLog.i(Messages.get(Spw.class, "spw37"));
                }
            }


            private void handleGenericItem(Item item) {
                // 아이템을 인벤토리에 추가하지 않고 능력만 발동
                // 현재는 기본적인 통계 증가만 적용
                if (item instanceof Spw20) {
                    spw20++;
                } else if (item instanceof Spw22) {
                    spw22++;
                } else if (item instanceof Spw23) {
                    spw23++;
                } else if (item instanceof Spw24) {
                    spw24++;
                } else if (item instanceof Spw25) {
                    spw25++;
                } else if (item instanceof Spw26) {
                    spw26++;
                } else if (item instanceof Spw27) {
                    spw27++;
                } else if (item instanceof Spw28) {
                    spw28++;
                } else if (item instanceof Spw29) {
                    spw29++;
                } else if (item instanceof Spw30) {
                    spw30++;
                } else if (item instanceof Spw31) {
                    spw31++;
                } else if (item instanceof Spw32) {
                    spw32++;
                } else if (item instanceof Spw33) {
                    spw33++;
                } else if (item instanceof Spw34) {
                    spw34++;
                } else if (item instanceof Spw35) {
                    spw35++;
                } else if (item instanceof Spw36) {
                    spw36++;
                } else if (item instanceof Spw37) {
                    // Grant RingOfArcana first time, then upgrade it on repeats
                    RingOfArcana ring = Dungeon.hero.belongings.getItem(RingOfArcana.class);
                    if (ring == null) {
                        RingOfArcana newRing = new RingOfArcana();
                        newRing.identify();
                        pickOrDropItem(newRing);
                    } else {
                        ring.upgrade();
                    }
                    spw37++;
                } else if (item instanceof Spw38) {
                    spw38++;
                    PotionOfExperience p = new PotionOfExperience();
                    p.quantity(2);
                    pickOrDropItem(p);
                } else if (item instanceof Spw39) {
                    spw39++;
                } else if (item instanceof Spw40) {
                    spw40++;
                }
            }
        }
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

}