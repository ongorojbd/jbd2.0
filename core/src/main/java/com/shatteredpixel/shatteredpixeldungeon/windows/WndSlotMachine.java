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
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo1;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo3;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo4;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo5;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo6;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo7;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo8;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo9;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscA;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscB;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscC;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscD;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscE;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscF;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscG;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscH;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
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
    public static final int MAX_PULLS = 10; //per run, tracked in Statistics.slotPulls

    //all four symbols now dispense items instead of gold on a triple/pair - see finishSpin().
    private static final int SYMBOLS = 4;
    private static final int DRINK = 0, SNACK = 1, COIN = 2, STAR = 3;
    private static final int[] SYMBOL_ICONS = {
            ItemSpriteSheet.MAGIC_INFUSE,
            ItemSpriteSheet.RO1,
            ItemSpriteSheet.SALT_CUBE,
            ItemSpriteSheet.TRINKET_CATA
    };
    private static final String[] SYMBOL_NAME_KEYS = {"symbol_topaz", "symbol_rocacaca", "symbol_saltcube", "symbol_catalyst"};

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

        spinButton = new RedButton("") {
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
        boolean pullsLeft = Statistics.slotPulls < MAX_PULLS;
        spinButton.text(Messages.get(this, pullsLeft ? "spin" : "spin_out", COST, Statistics.slotPulls, MAX_PULLS));
        spinButton.enable(pullsLeft && Dungeon.gold >= COST);
    }

    private void startSpin() {
        if (Dungeon.gold < COST || Statistics.slotPulls >= MAX_PULLS) {
            return;
        }

        Statistics.slotPulls++;
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
            //salt cube triple (jackpot) dispenses a random boss disc instead of gold
            payout = 0;
            message = Messages.get(this, "jackpot_item");
            color = 0xFFFF44;
            Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
            giveItem(randomBossdisc());
        } else if (allSame && finalSymbols[0] == STAR) {
            //trinket catalyst triple drops a random Araki relic instead of gold
            payout = 0;
            message = Messages.get(this, "triple_item");
            color = 0xFFDD44;
            Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
            giveRandomArakiRelic();
        } else if (allSame && finalSymbols[0] == SNACK) {
            //rocacaca triple dispenses 5 of a random reward instead of gold
            payout = 0;
            message = Messages.get(this, "triple_item");
            color = 0x44FF44;
            Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
            giveRandomRocacacaReward(5);
        } else if (allSame) {
            //magic infuse triple dispenses an actual Magical Infusion instead of gold
            payout = 0;
            message = Messages.get(this, "triple_item");
            color = 0x44FF44;
            Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
            giveItem(new MagicalInfusion().identify());
        } else if (pairSymbol == DRINK) {
            //same deal for the pair, but a lesser Stone of Enchantment instead
            payout = 0;
            message = Messages.get(this, "pair_item", Messages.get(this, SYMBOL_NAME_KEYS[pairSymbol]));
            color = 0x88FF88;
            Sample.INSTANCE.play(Assets.Sounds.ITEM);
            giveItem(new StoneOfEnchantment().identify());
        } else if (pairSymbol == SNACK) {
            //same random reward pool as the triple, but just 1 instead of 5
            payout = 0;
            message = Messages.get(this, "pair_item", Messages.get(this, SYMBOL_NAME_KEYS[pairSymbol]));
            color = 0x88FF88;
            Sample.INSTANCE.play(Assets.Sounds.ITEM);
            giveRandomRocacacaReward(1);
        } else if (pairSymbol == COIN) {
            //salt cube pair dispenses a Wild Energy instead of gold
            payout = 0;
            message = Messages.get(this, "pair_item", Messages.get(this, SYMBOL_NAME_KEYS[pairSymbol]));
            color = 0x88FF88;
            Sample.INSTANCE.play(Assets.Sounds.ITEM);
            giveItem(new WildEnergy().identify());
        } else if (pairSymbol == STAR) {
            //trinket catalyst pair dispenses 2 Telekinetic Grabs instead of gold
            payout = 0;
            message = Messages.get(this, "pair_item", Messages.get(this, SYMBOL_NAME_KEYS[pairSymbol]));
            color = 0x88FF88;
            Sample.INSTANCE.play(Assets.Sounds.ITEM);
            giveItem(new TelekineticGrab().identify().quantity(2));
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

    //salt cube jackpot's reward pool - one random boss disc, identified same as when mobs drop them
    private Item randomBossdisc() {
        switch (Random.Int(8)) {
            case 0:
                return new BossdiscA().identify();
            case 1:
                return new BossdiscB().identify();
            case 2:
                return new BossdiscC().identify();
            case 3:
                return new BossdiscD().identify();
            case 4:
                return new BossdiscE().identify();
            case 5:
                return new BossdiscF().identify();
            case 6:
                return new BossdiscG().identify();
            case 7: default:
                return new BossdiscH().identify();
        }
    }

    //trinket catalyst triple's reward pool - always drops on the floor rather than trying to
    //pick up first, and always calls out the relic via GLog.h, same as Araki's own quest drops
    private void giveRandomArakiRelic() {
        Item item;
        String key;
        switch (Random.Int(9)) {
            case 0:
                item = new Jojo1();
                key = "1";
                break;
            case 1:
                item = new Jojo2();
                key = "2";
                break;
            case 2:
                item = new Jojo3();
                key = "3";
                break;
            case 3:
                item = new Jojo4();
                key = "4";
                break;
            case 4:
                item = new Jojo5();
                key = "5";
                break;
            case 5:
                item = new Jojo6();
                key = "6";
                break;
            case 6:
                item = new Jojo7();
                key = "7";
                break;
            case 7:
                item = new Jojo8();
                key = "8";
                break;
            case 8: default:
                item = new Jojo9();
                key = "9";
                break;
        }
        Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
        GLog.h(Messages.get(Araki.class, key));
    }

    //rocacaca's reward pool - one random item from the set, at the given quantity
    private void giveRandomRocacacaReward(int quantity) {
        Item item;
        switch (Random.Int(8)) {
            case 0:
                item = new Kingt();
                break;
            case 1:
                item = new StoneOfAdvanceguard();
                break;
            case 2:
                item = new Xray();
                break;
            case 3:
                item = new Kings();
                break;
            case 4:
                item = new Kingm();
                break;
            case 5:
                item = new Kingw();
                break;
            case 6:
                item = new Kingc();
                break;
            case 7: default:
                item = new Kinga();
                break;
        }
        item.quantity(quantity);

        if (item.doPickUp(Dungeon.hero)) {
            GLog.p(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", item.name())));
        } else {
            Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop();
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
