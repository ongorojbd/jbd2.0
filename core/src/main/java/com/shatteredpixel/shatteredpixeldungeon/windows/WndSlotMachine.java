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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/*
    The special vending machine's slot minigame (see SpecialVendingMachine). Pay COST gold for
    one pull of three reels, each landing on one of four symbols. The outcome is rolled up
    front, but the reels are revealed one at a time about a second apart so the result arrives
    in pieces - and when the first two reels match, the last one slows right down before it
    lands.
*/
public class WndSlotMachine extends Window {

    public static final int COST = 500;

    //payouts, easiest to tune from here. JACKPOT also dispenses an upgrade scroll.
    private static final int PAYOUT_JACKPOT = 2500;  //three coins
    private static final int PAYOUT_STAR    = 2000;  //three stars
    private static final int PAYOUT_TRIPLE  = 1500;  //three snacks
    private static final int PAYOUT_TRIPLE2 = 1000;  //three drinks

    private static final int SYMBOLS = 4;
    private static final int DRINK = 0, SNACK = 1, COIN = 2, STAR = 3;
    private static final int[] SYMBOL_ICONS = {
            ItemSpriteSheet.DEWDROP,
            ItemSpriteSheet.RATION,
            ItemSpriteSheet.GOLD,
            ItemSpriteSheet.GOLDEN_KEY
    };
    //a pair's payout depends on which symbol it was - rarer/pricier symbols pay more, same
    //ranking as the triples above
    private static final int[] PAYOUT_PAIR = {150, 200, 400, 300}; //indexed by DRINK/SNACK/COIN/STAR
    private static final String[] SYMBOL_NAME_KEYS = {"symbol_drink", "symbol_snack", "symbol_coin", "symbol_star"};

    private static final float REEL_STOP_INTERVAL = 1.1f;
    private static final float REEL_TENSION_DELAY = 1.6f; //extra wait on reel 3 when 1 and 2 match
    private static final float CYCLE_FAST         = 0.05f;
    private static final float CYCLE_SLOW         = 0.16f; //while the tension build is running

    private static final int REEL_SIZE = 26;
    private static final int REEL_GAP  = 4;

    private enum State { READY, SPINNING, DONE }

    private State state = State.READY;

    private final int WIDTH;
    private final int HEIGHT;

    private final float reelY;
    private final float reelsLeft;
    private final float resultY;
    private final float buttonY;

    private RenderedTextBlock goldText;
    private RenderedTextBlock resultText;
    private RedButton spinButton;

    private final ColorBlock[] reelBoxes = new ColorBlock[3];
    private final ItemSprite[] reelIcons = new ItemSprite[3];

    private final int[] reelSymbols = new int[3];
    private final int[] finalSymbols = new int[3];
    private final boolean[] reelSpinning = new boolean[3];
    private final float[] cycleTimers = new float[3];
    private final float[] stopTimes = new float[3];

    private float spinElapsed = 0f;
    private boolean tensionAnnounced = false;

    public WndSlotMachine() {
        super();

        WIDTH = PixelScene.landscape() ? 144 : 120;
        HEIGHT = 136;
        resize(WIDTH, HEIGHT);

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.setPos((WIDTH - title.width()) / 2, 2);
        add(title);

        goldText = PixelScene.renderTextBlock("", 6);
        add(goldText);

        reelY = 30;
        reelsLeft = (WIDTH - (REEL_SIZE * 3 + REEL_GAP * 2)) / 2f;
        resultY = reelY + REEL_SIZE + 6;
        buttonY = HEIGHT - 20;

        for (int i = 0; i < 3; i++) {
            reelBoxes[i] = new ColorBlock(REEL_SIZE, REEL_SIZE, 0xFF1A1A2E);
            reelBoxes[i].x = reelX(i);
            reelBoxes[i].y = reelY;
            add(reelBoxes[i]);

            reelSymbols[i] = i % SYMBOLS;
            reelIcons[i] = new ItemSprite(SYMBOL_ICONS[reelSymbols[i]]);
            add(reelIcons[i]);
            placeIcon(i);
        }

        resultText = PixelScene.renderTextBlock(Messages.get(this, "prompt", COST), 6);
        add(resultText);
        layoutResult();

        spinButton = new RedButton(Messages.get(this, "spin", COST)) {
            @Override
            protected void onClick() {
                if (state == State.SPINNING) return;
                startSpin();
            }
        };
        spinButton.setRect(10, buttonY, WIDTH - 20, 18);
        add(spinButton);

        refreshGold();
        refreshButton();
    }

    private float reelX(int i) {
        return reelsLeft + i * (REEL_SIZE + REEL_GAP);
    }

    //icons on the sheet aren't all the same size, so re-centre after every symbol change
    private void placeIcon(int i) {
        ItemSprite icon = reelIcons[i];
        icon.x = reelX(i) + (REEL_SIZE - icon.width()) / 2f;
        icon.y = reelY + (REEL_SIZE - icon.height()) / 2f;
        PixelScene.align(icon);
    }

    private void setSymbol(int i, int symbol) {
        reelSymbols[i] = symbol;
        reelIcons[i].view(SYMBOL_ICONS[symbol], null);
        placeIcon(i);
    }

    private void refreshGold() {
        goldText.text(Messages.get(this, "gold", Dungeon.gold));
        goldText.hardlight(Dungeon.gold >= COST ? 0xFFFF44 : 0xFF6666);
        goldText.setPos((WIDTH - goldText.width()) / 2, 16);
    }

    private void layoutResult() {
        resultText.maxWidth(WIDTH - 8);
        resultText.setPos((WIDTH - resultText.width()) / 2, resultY);
    }

    private void refreshButton() {
        if (state == State.SPINNING) {
            spinButton.enable(false);
            return;
        }
        spinButton.text(Messages.get(this, "spin", COST));
        spinButton.enable(Dungeon.gold >= COST);
    }

    private void startSpin() {
        if (Dungeon.gold < COST) {
            return;
        }

        Dungeon.gold -= COST;
        Statistics.goldCollected -= COST;
        Sample.INSTANCE.play(Assets.Sounds.GOLD);
        refreshGold();

        //the whole result is decided here; the reveal below is presentation only
        for (int i = 0; i < 3; i++) {
            finalSymbols[i] = Random.Int(SYMBOLS);
            reelSpinning[i] = true;
            cycleTimers[i] = CYCLE_FAST;
            stopTimes[i] = REEL_STOP_INTERVAL * (i + 1);
        }
        if (finalSymbols[0] == finalSymbols[1]) {
            stopTimes[2] += REEL_TENSION_DELAY;
        }

        spinElapsed = 0f;
        tensionAnnounced = false;
        state = State.SPINNING;

        resultText.text(Messages.get(this, "spinning"));
        resultText.hardlight(0xFFFFFF);
        layoutResult();

        refreshButton();
    }

    @Override
    public void update() {
        super.update();

        if (state != State.SPINNING) {
            return;
        }

        spinElapsed += Game.elapsed;

        for (int i = 0; i < 3; i++) {
            if (!reelSpinning[i]) continue;

            if (spinElapsed >= stopTimes[i]) {
                reelSpinning[i] = false;
                setSymbol(i, finalSymbols[i]);
                Sample.INSTANCE.play(Assets.Sounds.CLICK);

                //first two matched - call it out while the last reel crawls to a stop
                if (i == 1 && finalSymbols[0] == finalSymbols[1] && !tensionAnnounced) {
                    tensionAnnounced = true;
                    resultText.text(Messages.get(this, "tension"));
                    resultText.hardlight(0xFFFF44);
                    layoutResult();
                }

                if (i == 2) {
                    finishSpin();
                }
                continue;
            }

            cycleTimers[i] -= Game.elapsed;
            if (cycleTimers[i] <= 0) {
                cycleTimers[i] = (i == 2 && tensionAnnounced) ? CYCLE_SLOW : CYCLE_FAST;
                setSymbol(i, (reelSymbols[i] + 1) % SYMBOLS);
            }
        }
    }

    private void finishSpin() {
        state = State.DONE;

        boolean allSame = finalSymbols[0] == finalSymbols[1] && finalSymbols[1] == finalSymbols[2];

        //which symbol the pair (if any) landed on - -1 when all three differ. allSame is
        //handled separately below, so by the time this is used it's a clean two-of-three.
        int pairSymbol = -1;
        if (finalSymbols[0] == finalSymbols[1]) pairSymbol = finalSymbols[0];
        else if (finalSymbols[1] == finalSymbols[2]) pairSymbol = finalSymbols[1];
        else if (finalSymbols[0] == finalSymbols[2]) pairSymbol = finalSymbols[0];

        int payout;
        String message;
        int color;

        if (allSame && finalSymbols[0] == COIN) {
            payout = PAYOUT_JACKPOT;
            message = Messages.get(this, "jackpot", payout);
            color = 0xFFFF44;
            Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
            giveItem(new ScrollOfUpgrade());
        } else if (allSame && finalSymbols[0] == STAR) {
            payout = PAYOUT_STAR;
            message = Messages.get(this, "triple", payout);
            color = 0xFFDD44;
            Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
        } else if (allSame && finalSymbols[0] == SNACK) {
            payout = PAYOUT_TRIPLE;
            message = Messages.get(this, "triple", payout);
            color = 0x44FF44;
            Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
        } else if (allSame) {
            payout = PAYOUT_TRIPLE2;
            message = Messages.get(this, "triple", payout);
            color = 0x44FF44;
            Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
        } else if (pairSymbol != -1) {
            payout = PAYOUT_PAIR[pairSymbol];
            message = Messages.get(this, "pair", Messages.get(this, SYMBOL_NAME_KEYS[pairSymbol]), payout);
            color = 0x88FF88;
            Sample.INSTANCE.play(Assets.Sounds.ITEM);
        } else {
            payout = 0;
            message = Messages.get(this, "lose");
            color = 0xFF6666;
        }

        if (payout > 0) {
            Dungeon.gold += payout;
            Statistics.goldCollected += payout;
            Sample.INSTANCE.play(Assets.Sounds.GOLD);
        }

        resultText.text(message);
        resultText.hardlight(color);
        layoutResult();

        refreshGold();
        refreshButton();
    }

    private void giveItem(Item item) {
        if (!item.doPickUp(Dungeon.hero)) {
            Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop();
        } else {
            GLog.i(Messages.get(this, "dispensed", item.name()));
        }
    }

    //no walking away mid-pull
    @Override
    public void onBackPressed() {
        if (state != State.SPINNING) {
            super.onBackPressed();
        }
    }
}
