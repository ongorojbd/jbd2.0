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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Toolbar;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class WndSettings extends WndTabbed {

    private static final int WIDTH_P = 122;
    private static final int WIDTH_L = 223;

    private static final int SLIDER_HEIGHT = 21;
    private static final int BTN_HEIGHT = 16;
    private static final float GAP = 1;

    private DisplayTab display;
    private UITab ui;
    private InputTab input;
    private DataTab data;
    private AudioTab audio;

    public static int last_index = 0;

    public WndSettings() {
        super();

        float height;

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        display = new DisplayTab();
        display.setSize(width, 0);
        height = display.height();
        add(display);

        add(new IconTab(Icons.get(Icons.DISPLAY)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                display.visible = display.active = value;
                if (value) last_index = 0;
            }
        });

        ui = new UITab();
        ui.setSize(width, 0);
        height = Math.max(height, ui.height());
        add(ui);

        add(new IconTab(Icons.get(Icons.PREFS)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                ui.visible = ui.active = value;
                if (value) last_index = 1;
            }
        });

        input = new InputTab();
        input.setSize(width, 0);
        height = Math.max(height, input.height());

        if (DeviceCompat.hasHardKeyboard() || ControllerHandler.isControllerConnected()) {
            add(input);
            Image icon;
            if (ControllerHandler.controllerActive || !DeviceCompat.hasHardKeyboard()) {
                icon = Icons.get(Icons.CONTROLLER);
            } else {
                icon = Icons.get(Icons.KEYBOARD);
            }
            add(new IconTab(icon) {
                @Override
                protected void select(boolean value) {
                    super.select(value);
                    input.visible = input.active = value;
                    if (value) last_index = 2;
                }
            });
        }

        data = new DataTab();
        data.setSize(width, 0);
        height = Math.max(height, data.height());
        add(data);

        add(new IconTab(Icons.get(Icons.DATA)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                data.visible = data.active = value;
                if (value) last_index = 3;
            }
        });

        audio = new AudioTab();
        audio.setSize(width, 0);
        height = Math.max(height, audio.height());
        add(audio);

        add(new IconTab(Icons.get(Icons.AUDIO)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                audio.visible = audio.active = value;
                if (value) last_index = 4;
            }
        });

        resize(width, (int) Math.ceil(height));

        layoutTabs();

        if (tabs.size() == 4 && last_index >= 3) {
            //input tab isn't visible
            select(last_index - 1);
        } else {
            select(last_index);
        }
    }

    @Override
    public void hide() {
        super.hide();
        //resets generators because there's no need to retain chars for languages not selected
        ShatteredPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback() {
            @Override
            public void beforeCreate() {
                Game.platform.resetGenerators();
            }

            @Override
            public void afterCreate() {
                //do nothing
            }
        });
    }

    private static class DisplayTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        CheckBox chkFullscreen;
        CheckBox chkLandscape;
        ColorBlock sep2;
        OptionSlider optBrightness;
        OptionSlider optVisGrid;
        OptionSlider optFollowIntensity;
        OptionSlider optScreenShake;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            chkFullscreen = new CheckBox(Messages.get(this, "fullscreen")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.fullscreen(checked());
                }
            };
            if (Game.platform.supportsFullScreen()){
                chkFullscreen.checked(SPDSettings.fullscreen());
            } else {
                chkFullscreen.checked(true);
                chkFullscreen.enable(false);
            }
            add(chkFullscreen);

            if (DeviceCompat.isAndroid()) {
                chkLandscape = new CheckBox(Messages.get(this, "landscape")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        SPDSettings.landscape(checked());
                    }
                };
                chkLandscape.checked(SPDSettings.landscape());
                add(chkLandscape);
            }

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            optBrightness = new OptionSlider(Messages.get(this, "brightness"),
                    Messages.get(this, "dark"), Messages.get(this, "bright"), -1, 1) {
                @Override
                protected void onChange() {
                    SPDSettings.brightness(getSelectedValue());
                }
            };
            optBrightness.setSelectedValue(SPDSettings.brightness());
            add(optBrightness);

            optVisGrid = new OptionSlider(Messages.get(this, "visual_grid"),
                    Messages.get(this, "off"), Messages.get(this, "high"), -1, 2) {
                @Override
                protected void onChange() {
                    SPDSettings.visualGrid(getSelectedValue());
                }
            };
            optVisGrid.setSelectedValue(SPDSettings.visualGrid());
            add(optVisGrid);

            optFollowIntensity = new OptionSlider(Messages.get(this, "camera_follow"),
                    Messages.get(this, "low"), Messages.get(this, "high"), 1, 4) {
                @Override
                protected void onChange() {
                    SPDSettings.cameraFollow(getSelectedValue());
                }
            };
            optFollowIntensity.setSelectedValue(SPDSettings.cameraFollow());
            add(optFollowIntensity);

            optScreenShake = new OptionSlider(Messages.get(this, "screenshake"),
                    Messages.get(this, "off"), Messages.get(this, "high"), 0, 4) {
                @Override
                protected void onChange() {
                    SPDSettings.screenShake(getSelectedValue());
                }
            };
            optScreenShake.setSelectedValue(SPDSettings.screenShake());
            add(optScreenShake);

        }

        @Override
        protected void layout() {

            float bottom = y;

            title.setPos((width - title.width()) / 2, bottom + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 3 * GAP;

            bottom = sep1.y + 1;

            chkFullscreen.setRect(0, bottom + GAP, width, BTN_HEIGHT);
            bottom = chkFullscreen.bottom();

            if (chkLandscape != null) {
                chkLandscape.setRect(0, bottom + GAP, width, BTN_HEIGHT);
                bottom = chkLandscape.bottom();
            }

            sep2.size(width, 1);
            sep2.y = bottom + GAP;
            bottom = sep2.y + 1;

            if (width > 200) {
                optBrightness.setRect(0, bottom + GAP, width / 2 - GAP / 2, SLIDER_HEIGHT);
                optVisGrid.setRect(optBrightness.right() + GAP, optBrightness.top(), width / 2 - GAP / 2, SLIDER_HEIGHT);

                optFollowIntensity.setRect(0, optVisGrid.bottom() + GAP, width / 2 - GAP / 2, SLIDER_HEIGHT);
                optScreenShake.setRect(optFollowIntensity.right() + GAP, optFollowIntensity.top(), width / 2 - GAP / 2, SLIDER_HEIGHT);
            } else {
                optBrightness.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
                optVisGrid.setRect(0, optBrightness.bottom() + GAP, width, SLIDER_HEIGHT);

                optFollowIntensity.setRect(0, optVisGrid.bottom() + GAP, width, SLIDER_HEIGHT);
                optScreenShake.setRect(0, optFollowIntensity.bottom() + GAP, width, SLIDER_HEIGHT);
            }

            height = optScreenShake.bottom();
        }

    }

    private static class UITab extends Component {

        RenderedTextBlock title;

        ColorBlock sep1;
        OptionSlider optUIMode;
        OptionSlider optUIScale;
        RedButton btnToolbarSettings;
        CheckBox chkFlipTags;
        ColorBlock sep2;
        CheckBox chkFont;
        CheckBox chkVibrate;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            //add slider for UI size only if device has enough space to support it
            float wMin = Game.width / PixelScene.MIN_WIDTH_FULL;
            float hMin = Game.height / PixelScene.MIN_HEIGHT_FULL;
            if (Math.min(wMin, hMin) >= 2 * Game.density) {
                optUIMode = new OptionSlider(
                        Messages.get(this, "ui_mode"),
                        Messages.get(this, "mobile"),
                        Messages.get(this, "full"),
                        0,
                        2
                ) {
                    @Override
                    protected void onChange() {
                        SPDSettings.interfaceSize(getSelectedValue());
                        ShatteredPixelDungeon.seamlessResetScene();
                    }
                };
                optUIMode.setSelectedValue(SPDSettings.interfaceSize());
                add(optUIMode);
            }

            if ((int) Math.ceil(2 * Game.density) < PixelScene.maxDefaultZoom) {
                optUIScale = new OptionSlider(Messages.get(this, "scale"),
                        (int) Math.ceil(2 * Game.density) + "X",
                        PixelScene.maxDefaultZoom + "X",
                        (int) Math.ceil(2 * Game.density),
                        PixelScene.maxDefaultZoom) {
                    @Override
                    protected void onChange() {
                        if (getSelectedValue() != SPDSettings.scale()) {
                            SPDSettings.scale(getSelectedValue());
                            ShatteredPixelDungeon.seamlessResetScene();
                        }
                    }
                };
                optUIScale.setSelectedValue(PixelScene.defaultZoom);
                add(optUIScale);
            }

            if (SPDSettings.interfaceSize() == 0) {
                btnToolbarSettings = new RedButton(Messages.get(this, "toolbar_settings"), 9) {
                    @Override
                    protected void onClick() {
                        ShatteredPixelDungeon.scene().addToFront(new Window() {

                            RenderedTextBlock barDesc;
                            RedButton btnSplit;
                            RedButton btnGrouped;
                            RedButton btnCentered;
                            CheckBox chkQuickSwapper;
                            RenderedTextBlock swapperDesc;
                            CheckBox chkFlipToolbar;
                            CheckBox chkFlipTags;

                            {
                                barDesc = PixelScene.renderTextBlock(Messages.get(WndSettings.UITab.this, "mode"), 9);
                                add(barDesc);

                                btnSplit = new RedButton(Messages.get(WndSettings.UITab.this, "split")) {
                                    @Override
                                    protected void onClick() {
                                        textColor(TITLE_COLOR);
                                        btnGrouped.textColor(WHITE);
                                        btnCentered.textColor(WHITE);
                                        SPDSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
                                        Toolbar.updateLayout();
                                    }
                                };
                                if (SPDSettings.toolbarMode().equals(Toolbar.Mode.SPLIT.name())) {
                                    btnSplit.textColor(TITLE_COLOR);
                                }
                                add(btnSplit);

                                btnGrouped = new RedButton(Messages.get(WndSettings.UITab.this, "group")) {
                                    @Override
                                    protected void onClick() {
                                        btnSplit.textColor(WHITE);
                                        textColor(TITLE_COLOR);
                                        btnCentered.textColor(WHITE);
                                        SPDSettings.toolbarMode(Toolbar.Mode.GROUP.name());
                                        Toolbar.updateLayout();
                                    }
                                };
                                if (SPDSettings.toolbarMode().equals(Toolbar.Mode.GROUP.name())) {
                                    btnGrouped.textColor(TITLE_COLOR);
                                }
                                add(btnGrouped);

                                btnCentered = new RedButton(Messages.get(WndSettings.UITab.this, "center")) {
                                    @Override
                                    protected void onClick() {
                                        btnSplit.textColor(WHITE);
                                        btnGrouped.textColor(WHITE);
                                        textColor(TITLE_COLOR);
                                        SPDSettings.toolbarMode(Toolbar.Mode.CENTER.name());
                                        Toolbar.updateLayout();
                                    }
                                };
                                if (SPDSettings.toolbarMode().equals(Toolbar.Mode.CENTER.name())) {
                                    btnCentered.textColor(TITLE_COLOR);
                                }
                                add(btnCentered);

                                chkQuickSwapper = new CheckBox(Messages.get(WndSettings.UITab.this, "quickslot_swapper")) {
                                    @Override
                                    protected void onClick() {
                                        super.onClick();
                                        SPDSettings.quickSwapper(checked());
                                        Toolbar.updateLayout();
                                    }
                                };
                                chkQuickSwapper.checked(SPDSettings.quickSwapper());
                                add(chkQuickSwapper);

                                swapperDesc = PixelScene.renderTextBlock(Messages.get(WndSettings.UITab.this, "swapper_desc"), 5);
                                swapperDesc.hardlight(0x888888);
                                add(swapperDesc);

                                chkFlipToolbar = new CheckBox(Messages.get(WndSettings.UITab.this, "flip_toolbar")) {
                                    @Override
                                    protected void onClick() {
                                        super.onClick();
                                        SPDSettings.flipToolbar(checked());
                                        Toolbar.updateLayout();
                                    }
                                };
                                chkFlipToolbar.checked(SPDSettings.flipToolbar());
                                add(chkFlipToolbar);

                                chkFlipTags = new CheckBox(Messages.get(WndSettings.UITab.this, "flip_indicators")) {
                                    @Override
                                    protected void onClick() {
                                        super.onClick();
                                        SPDSettings.flipTags(checked());
                                        GameScene.layoutTags();
                                    }
                                };
                                chkFlipTags.checked(SPDSettings.flipTags());
                                add(chkFlipTags);

                                //layout
                                resize(WIDTH_P, 0);

                                barDesc.setPos((width - barDesc.width()) / 2f, GAP);
                                PixelScene.align(barDesc);

                                int btnWidth = (int) (width - 2 * GAP) / 3;
                                btnSplit.setRect(0, barDesc.bottom() + GAP, btnWidth, BTN_HEIGHT - 2);
                                btnGrouped.setRect(btnSplit.right() + GAP, btnSplit.top(), btnWidth, BTN_HEIGHT - 2);
                                btnCentered.setRect(btnGrouped.right() + GAP, btnSplit.top(), btnWidth, BTN_HEIGHT - 2);

                                chkQuickSwapper.setRect(0, btnGrouped.bottom() + GAP, width, BTN_HEIGHT);

                                swapperDesc.maxWidth(width);
                                swapperDesc.setPos(0, chkQuickSwapper.bottom() + 1);

                                if (width > 200) {
                                    chkFlipToolbar.setRect(0, swapperDesc.bottom() + GAP, width / 2 - 1, BTN_HEIGHT);
                                    chkFlipTags.setRect(chkFlipToolbar.right() + GAP, chkFlipToolbar.top(), width / 2 - 1, BTN_HEIGHT);
                                } else {
                                    chkFlipToolbar.setRect(0, swapperDesc.bottom() + GAP, width, BTN_HEIGHT);
                                    chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP, width, BTN_HEIGHT);
                                }

                                resize(WIDTH_P, (int) chkFlipTags.bottom());

                            }
                        });
                    }
                };
                add(btnToolbarSettings);

            } else {

                chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        SPDSettings.flipTags(checked());
                        GameScene.layoutTags();
                    }
                };
                chkFlipTags.checked(SPDSettings.flipTags());
                add(chkFlipTags);

            }

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            chkFont = new CheckBox(Messages.get(this, "system_font")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    ShatteredPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback() {
                        @Override
                        public void beforeCreate() {
                            SPDSettings.systemFont(checked());
                        }

                        @Override
                        public void afterCreate() {
                            //do nothing
                        }
                    });
                }
            };
            chkFont.checked(SPDSettings.systemFont());
            add(chkFont);

            chkVibrate = new CheckBox(Messages.get(this, "vibration")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.vibration(checked());
                    if (checked()) {
                        Game.vibrate(250);
                    }
                }
            };
            chkVibrate.enable(Game.platform.supportsVibration());
            if (chkVibrate.active) {
                chkVibrate.checked(SPDSettings.vibration());
            }
            add(chkVibrate);
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width()) / 2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 3 * GAP;

            height = sep1.y + 1;

            if (optUIMode != null && optUIScale != null && width > 200) {
                optUIMode.setRect(0, height + GAP, width / 2 - 1, SLIDER_HEIGHT);
                optUIScale.setRect(width / 2 + 1, height + GAP, width / 2 - 1, SLIDER_HEIGHT);
                height = optUIScale.bottom();
            } else {
                if (optUIMode != null) {
                    optUIMode.setRect(0, height + GAP, width, SLIDER_HEIGHT);
                    height = optUIMode.bottom();
                }

                if (optUIScale != null) {
                    optUIScale.setRect(0, height + GAP, width, SLIDER_HEIGHT);
                    height = optUIScale.bottom();
                }
            }

            if (btnToolbarSettings != null) {
                btnToolbarSettings.setRect(0, height + GAP, width, BTN_HEIGHT);
                height = btnToolbarSettings.bottom();
            } else {
                chkFlipTags.setRect(0, height + GAP, width, BTN_HEIGHT);
                height = chkFlipTags.bottom();
            }

            sep2.size(width, 1);
            sep2.y = height + GAP;

            if (width > 200) {
                chkFont.setRect(0, sep2.y + 1 + GAP, width / 2 - 1, BTN_HEIGHT);
                chkVibrate.setRect(chkFont.right() + 2, chkFont.top(), width / 2 - 1, BTN_HEIGHT);
                height = chkVibrate.bottom();

            } else {
                chkFont.setRect(0, sep2.y + 1 + GAP, width, BTN_HEIGHT);
                chkVibrate.setRect(0, chkFont.bottom() + GAP, width, BTN_HEIGHT);
                height = chkVibrate.bottom();
            }
        }

    }

    private static class InputTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;

        RedButton btnKeyBindings;
        RedButton btnControllerBindings;

        ColorBlock sep2;

        OptionSlider optControlSens;
        OptionSlider optHoldMoveSens;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            if (DeviceCompat.hasHardKeyboard()) {

                btnKeyBindings = new RedButton(Messages.get(this, "key_bindings")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        ShatteredPixelDungeon.scene().addToFront(new WndKeyBindings(false));
                    }
                };

                add(btnKeyBindings);
            }

            if (ControllerHandler.isControllerConnected()) {
                btnControllerBindings = new RedButton(Messages.get(this, "controller_bindings")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        ShatteredPixelDungeon.scene().addToFront(new WndKeyBindings(true));
                    }
                };

                add(btnControllerBindings);
            }

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);


            optControlSens = new OptionSlider(
                    Messages.get(this, "controller_sensitivity"),
                    "1",
                    "10",
                    1,
                    10
            ) {
                @Override
                protected void onChange() {
                    SPDSettings.controllerPointerSensitivity(getSelectedValue());
                }
            };
            optControlSens.setSelectedValue(SPDSettings.controllerPointerSensitivity());
            add(optControlSens);

            optHoldMoveSens = new OptionSlider(
                    Messages.get(this, "movement_sensitivity"),
                    Messages.get(this, "off"),
                    Messages.get(this, "high"),
                    0,
                    4
            ) {
                @Override
                protected void onChange() {
                    SPDSettings.movementHoldSensitivity(getSelectedValue());
                }
            };
            optHoldMoveSens.setSelectedValue(SPDSettings.movementHoldSensitivity());
            add(optHoldMoveSens);
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width()) / 2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 3 * GAP;

            height = sep1.y + 1;

            if (width > 200 && btnKeyBindings != null && btnControllerBindings != null) {
                btnKeyBindings.setRect(0, height + GAP, width / 2 - 1, BTN_HEIGHT);
                btnControllerBindings.setRect(width / 2 + 1, height + GAP, width / 2 - 1, BTN_HEIGHT);
                height = btnControllerBindings.bottom();
            } else {
                if (btnKeyBindings != null) {
                    btnKeyBindings.setRect(0, height + GAP, width, BTN_HEIGHT);
                    height = btnKeyBindings.bottom();
                }

                if (btnControllerBindings != null) {
                    btnControllerBindings.setRect(0, height + GAP, width, BTN_HEIGHT);
                    height = btnControllerBindings.bottom();
                }
            }

            sep2.size(width, 1);
            sep2.y = height + GAP;

            if (width > 200) {
                optControlSens.setRect(0, sep2.y + 1 + GAP, width / 2 - 1, SLIDER_HEIGHT);
                optHoldMoveSens.setRect(width / 2 + 1, optControlSens.top(), width / 2 - 1, SLIDER_HEIGHT);
            } else {
                optControlSens.setRect(0, sep2.y + 1 + GAP, width, SLIDER_HEIGHT);
                optHoldMoveSens.setRect(0, optControlSens.bottom() + GAP, width, SLIDER_HEIGHT);
            }

            height = optHoldMoveSens.bottom();

        }
    }

    private static class DataTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        CheckBox chkNews;
        CheckBox chkUpdates;
        CheckBox chkWifi;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            chkNews = new CheckBox(Messages.get(this, "news")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.news(checked());
                    News.clearArticles();
                }
            };
            chkNews.checked(SPDSettings.news());
            add(chkNews);

            if (Updates.supportsUpdates() && Updates.supportsUpdatePrompts()) {
                chkUpdates = new CheckBox(Messages.get(this, "updates")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        SPDSettings.updates(checked());
                        Updates.clearUpdate();
                    }
                };
                chkUpdates.checked(SPDSettings.updates());
                add(chkUpdates);
            }

            if (!DeviceCompat.isDesktop()) {
                chkWifi = new CheckBox(Messages.get(this, "wifi")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        SPDSettings.WiFi(checked());
                    }
                };
                chkWifi.checked(SPDSettings.WiFi());
                add(chkWifi);
            }
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width()) / 2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 3 * GAP;

            float pos;
            if (width > 200 && chkUpdates != null) {
                chkNews.setRect(0, sep1.y + 1 + GAP, width / 2 - 1, BTN_HEIGHT);
                chkUpdates.setRect(chkNews.right() + GAP, chkNews.top(), width / 2 - 1, BTN_HEIGHT);
                pos = chkUpdates.bottom();
            } else {
                chkNews.setRect(0, sep1.y + 1 + GAP, width, BTN_HEIGHT);
                pos = chkNews.bottom();
                if (chkUpdates != null) {
                    chkUpdates.setRect(0, chkNews.bottom() + GAP, width, BTN_HEIGHT);
                    pos = chkUpdates.bottom();
                }
            }

            if (chkWifi != null) {
                chkWifi.setRect(0, pos + GAP, width, BTN_HEIGHT);
                pos = chkWifi.bottom();
            }

            height = pos;

        }
    }

    private static class AudioTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        OptionSlider optMusic;
        CheckBox chkMusicMute;
        ColorBlock sep2;
        OptionSlider optSFX;
        CheckBox chkMuteSFX;
        ColorBlock sep3;
        CheckBox chkIgnoreSilent;
        CheckBox chkMusicBG;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            optMusic = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    SPDSettings.musicVol(getSelectedValue());
                }
            };
            optMusic.setSelectedValue(SPDSettings.musicVol());
            add(optMusic);

            chkMusicMute = new CheckBox(Messages.get(this, "music_mute")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.music(!checked());
                }
            };
            chkMusicMute.checked(!SPDSettings.music());
            add(chkMusicMute);

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            optSFX = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    SPDSettings.SFXVol(getSelectedValue());
                    if (Random.Int(100) == 0) {
                        Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                    } else {
                        Sample.INSTANCE.play(Random.oneOf(Assets.Sounds.GOLD,
                                Assets.Sounds.HIT,
                                Assets.Sounds.ITEM,
                                Assets.Sounds.SHATTER,
                                Assets.Sounds.EVOKE,
                                Assets.Sounds.SECRET));
                    }
                }
            };
            optSFX.setSelectedValue(SPDSettings.SFXVol());
            add(optSFX);

            chkMuteSFX = new CheckBox(Messages.get(this, "sfx_mute")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.soundFx(!checked());
                    Sample.INSTANCE.play(Assets.Sounds.CLICK);
                }
            };
            chkMuteSFX.checked(!SPDSettings.soundFx());
            add(chkMuteSFX);

            if (DeviceCompat.isiOS()) {

                sep3 = new ColorBlock(1, 1, 0xFF000000);
                add(sep3);

                chkIgnoreSilent = new CheckBox(Messages.get(this, "ignore_silent")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        SPDSettings.ignoreSilentMode(checked());
                    }
                };
                chkIgnoreSilent.checked(SPDSettings.ignoreSilentMode());
                add(chkIgnoreSilent);

            } else if (DeviceCompat.isDesktop()) {

                sep3 = new ColorBlock(1, 1, 0xFF000000);
                add(sep3);

                chkMusicBG = new CheckBox(Messages.get(this, "music_bg")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        SPDSettings.playMusicInBackground(checked());
                    }
                };
                chkMusicBG.checked(SPDSettings.playMusicInBackground());
                add(chkMusicBG);
            }
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width()) / 2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 3 * GAP;

            if (width > 200) {
                optMusic.setRect(0, sep1.y + 1 + GAP, width / 2 - 1, SLIDER_HEIGHT);
                chkMusicMute.setRect(0, optMusic.bottom() + GAP, width / 2 - 1, BTN_HEIGHT);

                sep2.size(width, 1);
                sep2.y = sep1.y; //just have them overlap

                optSFX.setRect(optMusic.right() + 2, sep2.y + 1 + GAP, width / 2 - 1, SLIDER_HEIGHT);
                chkMuteSFX.setRect(chkMusicMute.right() + 2, optSFX.bottom() + GAP, width / 2 - 1, BTN_HEIGHT);

            } else {
                optMusic.setRect(0, sep1.y + 1 + GAP, width, SLIDER_HEIGHT);
                chkMusicMute.setRect(0, optMusic.bottom() + GAP, width, BTN_HEIGHT);

                sep2.size(width, 1);
                sep2.y = chkMusicMute.bottom() + GAP;

                optSFX.setRect(0, sep2.y + 1 + GAP, width, SLIDER_HEIGHT);
                chkMuteSFX.setRect(0, optSFX.bottom() + GAP, width, BTN_HEIGHT);
            }

            height = chkMuteSFX.bottom();

            if (chkIgnoreSilent != null) {
                sep3.size(width, 1);
                sep3.y = chkMuteSFX.bottom() + GAP;

                chkIgnoreSilent.setRect(0, sep3.y + 1 + GAP, width, BTN_HEIGHT);
                height = chkIgnoreSilent.bottom();
            } else if (chkMusicBG != null) {
                sep3.size(width, 1);
                sep3.y = chkMuteSFX.bottom() + GAP;

                chkMusicBG.setRect(0, sep3.y + 1 + GAP, width, BTN_HEIGHT);
                height = chkMusicBG.bottom();
            }
        }
    }
}