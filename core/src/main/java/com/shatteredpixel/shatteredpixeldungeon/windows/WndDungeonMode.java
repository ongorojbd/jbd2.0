/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.DeviceCompat;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndDungeonMode extends Window {

    private static final int WIDTH_P = 132;
    private static final int WIDTH_L = 176;
    private static final int MARGIN = 2;
    private static final int BUTTON_HEIGHT = 36;
    private static final int ICON_COLUMN = 28;
    private static final boolean DECKBUILDER_BETA_LIMITS = true;

    private final ArrayList<ModeButton> slots = new ArrayList<>();
    private ScrollPane modeList;
    private Mode chosenMode;
    private float timer;

    public WndDungeonMode() {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        float pos = MARGIN;
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(width - MARGIN * 2);
        title.setPos((width - title.width()) / 2, pos);
        add(title);

        modeList = new ScrollPane(new Component());
        add(modeList);

        pos = title.bottom() + 5 * MARGIN;
        Component content = modeList.content();
        int posItem = 0;

        for (Mode mode : Mode.values()) {
            Image icon = mode.getIcon();
            ModeButton modeButton = new ModeButton(mode.desc(), 6, mode);
            modeButton.icon(icon);
            modeButton.multiline = true;
            modeButton.setSize(width, BUTTON_HEIGHT);
            modeButton.setRect(0, posItem, width, BUTTON_HEIGHT);
            modeButton.enable(true);
            content.add(modeButton);
            slots.add(modeButton);
            posItem += modeButton.height() + MARGIN;
        }
        int contentHeight = posItem + 1;
        content.setSize(width, contentHeight);

        int maxListHeight = (int)(PixelScene.uiCamera.height - title.bottom() - 24);
        int listHeight = Math.min(contentHeight, maxListHeight);
        resize(width, (int)(pos + listHeight + MARGIN));
        modeList.setRect(0, pos, width, listHeight);
    }

    @Override
    public synchronized void update() {
        super.update();
        if (chosenMode != null && (timer += Game.elapsed) > 0.2f) {
            hide();

            if (GamesInProgress.selectedClass == null) return;

            chosenMode.apply();
            Dungeon.hero = null;
            Dungeon.daily = Dungeon.dailyReplay = false;
            Dungeon.initSeed();
            ActionIndicator.clearAction();
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

            Game.switchScene(InterlevelScene.class);
        }
    }

    private enum Mode {
        NORMAL("normal", 0, Dungeon.GameMode.NORMAL) {
            @Override
            public Image getIcon() { return Icons.get(Icons.ENTER); }
        },
        DIO_CASTLE("dio_castle", ItemSpriteSheet.SUNDIAL, Dungeon.GameMode.DIO_CASTLE),
        TENDENCY("tendency", ItemSpriteSheet.TENS, Dungeon.GameMode.TENDENCY),
        DECKBUILDER("deckbuilder", ItemSpriteSheet.DECK, Dungeon.GameMode.DECKBUILDER),
        DECKBUILDER_TUTORIAL("deckbuilder_tutorial", ItemSpriteSheet.DECK, Dungeon.GameMode.DECKBUILDER_TUTORIAL);

        private final String messageKey;
        private final int sprite;
        private final Dungeon.GameMode gameMode;

        Mode(String messageKey, int sprite, Dungeon.GameMode gameMode) {
            this.messageKey = messageKey;
            this.sprite = sprite;
            this.gameMode = gameMode;
        }

        public Image getIcon() { return new ItemSprite(sprite, null); }

        private boolean canStart() {
            switch (this) {
                case DIO_CASTLE:
                    return SPDSettings.getDio() >= 1;
                case TENDENCY:
                case DECKBUILDER:
                    return Badges.isUnlocked(Badges.Badge.VICTORY);
                default:
                    return true;
            }
        }

        private void showLockedMessage() {
            switch (this) {
                case DIO_CASTLE:
                    ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
                            new ItemSprite(ItemSpriteSheet.SUNDIAL, null),
                            Messages.get(WndDungeonMode.class, messageKey),
                            Messages.get(WndDungeonMode.class, "dio_locked")));
                    break;
                case TENDENCY:
                    ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
                            new ItemSprite(ItemSpriteSheet.TENS, null),
                            Messages.get(HeroSelectScene.class, "tendency_mode"),
                            Messages.get(HeroSelectScene.class, "tendency_nowin")));
                    break;
                case DECKBUILDER:
                    ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
                            new ItemSprite(ItemSpriteSheet.DECK, null),
                            Messages.get(HeroSelectScene.class, "deckbuilder_mode"),
                            Messages.get(HeroSelectScene.class, "deckbuilder_nowin")));
                    break;
            }
        }

        private boolean checkStartRequirements() {
            if (!canStart()) {
                showLockedMessage();
                return false;
            }
            if (DECKBUILDER_BETA_LIMITS
                    && (this == DECKBUILDER || this == DECKBUILDER_TUTORIAL)
                    && !deckBuilderClassAvailable(GamesInProgress.selectedClass)) {
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                        "카드 배틀 모드는 아직 베타 버전입니다.\n\n" +
                        "현재는 죠나단, 죠르노만 선택할 수 있습니다."));
                return false;
            }
            if ((this == DECKBUILDER || this == DECKBUILDER_TUTORIAL) && !SPDSettings.landscape() && !DeviceCompat.isDesktop()) {
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(WndDungeonMode.class, "deckbuilder_portrait")));
                return false;
            }
            return true;
        }

        private boolean deckBuilderClassAvailable(HeroClass heroClass) {
            return heroClass == HeroClass.WARRIOR || heroClass == HeroClass.HUNTRESS;
        }

        private void apply() {
            Dungeon.selectedMode = gameMode;
            SPDSettings.setTendency(0);
            SPDSettings.setDeckbuilder(0);

            if (this == TENDENCY) {
                SPDSettings.setTendency(1);
                SPDSettings.customSeed("");
            } else if (this == DECKBUILDER) {
                SPDSettings.setDeckbuilder(1);
                SPDSettings.customSeed("");
            } else if (this == DECKBUILDER_TUTORIAL) {
                SPDSettings.customSeed("");
            }
        }

        private String desc() {
            return "_" + Messages.get(WndDungeonMode.class, messageKey) + "_\n"
                    + Messages.get(WndDungeonMode.class, messageKey + "_desc");
        }
    }

    public class ModeButton extends RedButton {

        private final Mode mode;

        public ModeButton(String label, int size, Mode mode) {
            super(label, size);
            hotArea.blockLevel = PointerArea.NEVER_BLOCK;
            this.mode = mode;
        }

        @Override
        public void onClick() {
            if (chosenMode == null && mode.checkStartRequirements()) {
                bg.brightness(1.2f);
                chosenMode = mode;
            }
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            if (icon != null) {
                icon.x = x + (ICON_COLUMN - icon.width()) / 2f;
                icon.y = y + (height - icon.height()) / 2f;
                PixelScene.align(icon);
            }

            if (text != null) {
                text.maxWidth((int)(width - ICON_COLUMN - 4));
                text.setPos(x + ICON_COLUMN, y + (height - text.height()) / 2f);
                PixelScene.align(text);
            }
        }
    }
}
