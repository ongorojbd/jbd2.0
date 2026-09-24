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
import com.watabou.utils.Reflection;

/*
    The special vending machine's slot minigame (see SpecialVendingMachine). Pay COST gold for
    one pull of three reels, each landing on one of four symbols. The outcome is rolled up
    front, but the reels are revealed one at a time about a second apart so the result arrives
    in pieces - and when the first two reels match, the last one slows right down before it
    lands.
*/
public class WndSlotMachine extends Window {

    public static final int COST = 500;
    public static final int MAX_PULLS = 8; //per run, tracked in Statistics.slotPulls

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

        String message;
        int color;
        Item reward = null;

        if (allSame) {
            int symbol = finalSymbols[0];
            switch (symbol) {
                case COIN: //jackpot: a random boss disc
                    message = Messages.get(this, "jackpot_item");
                    color = 0xFFFF44;
                    Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                    reward = giveItem(randomBossdisc());
                    break;
                case STAR: //a random Araki relic, called out same as Araki's own quest drops
                    message = Messages.get(this, "triple_item");
                    color = 0xFFDD44;
                    Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                    reward = giveRandomArakiRelic();
                    break;
                case SNACK: //5 of a random rocacaca reward
                    message = Messages.get(this, "triple_item");
                    color = 0x44FF44;
                    Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                    reward = giveItem(randomRocacacaReward(5));
                    break;
                default: //DRINK: a Magical Infusion
                    message = Messages.get(this, "triple_item");
                    color = 0x44FF44;
                    Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                    reward = giveItem(new MagicalInfusion().identify());
                    break;
            }
        } else if (pairSymbol != -1) {
            color = 0x88FF88;
            Sample.INSTANCE.play(Assets.Sounds.ITEM);
            switch (pairSymbol) {
                case DRINK: //a Stone of Enchantment
                    reward = giveItem(new StoneOfEnchantment().identify());
                    break;
                case SNACK: //1 of a random rocacaca reward
                    reward = giveItem(randomRocacacaReward(1));
                    break;
                case COIN: //a Wild Energy
                    reward = giveItem(new WildEnergy().identify());
                    break;
                default: //STAR: 2 Telekinetic Grabs
                    reward = giveItem(new TelekineticGrab().identify().quantity(2));
                    break;
            }
            message = Messages.get(this, "pair_item", Messages.get(this, SYMBOL_NAME_KEYS[pairSymbol]), rewardName(reward));
            reward = null; //already named in the message
        } else {
            message = Messages.get(this, "lose");
            color = 0xFF6666;
        }

        if (reward != null) {
            message += "\n" + Messages.get(this, "reward", rewardName(reward));
        }

        resultText.text(message);
        resultText.hardlight(color);
        layoutResult();

        refreshGold();
        refreshButton();
    }

    private String rewardName(Item item) {
        return item.quantity() > 1 ? item.name() + " x" + item.quantity() : item.name();
    }

    //every reward goes through here: picked up if there's room, dropped at the hero's feet if not
    private Item giveItem(Item item) {
        if (!item.doPickUp(Dungeon.hero)) {
            Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
        }
        GLog.i(Messages.get(this, "dispensed", item.name()));
        return item;
    }

    private static <T extends Item> T randomOf(Class<? extends T>[] pool) {
        return Reflection.newInstance(pool[Random.Int(pool.length)]);
    }

    //salt cube jackpot's reward pool - one random boss disc, identified same as when mobs drop them
    @SuppressWarnings("unchecked")
    private Item randomBossdisc() {
        Class<? extends Item>[] pool = new Class[]{
                BossdiscA.class, BossdiscB.class, BossdiscC.class, BossdiscD.class,
                BossdiscE.class, BossdiscF.class, BossdiscG.class, BossdiscH.class
        };
        return randomOf(pool).identify();
    }

    //trinket catalyst triple's reward pool - one random Araki relic, plus the same GLog.h
    //call-out Araki's own quest drops use
    @SuppressWarnings("unchecked")
    private Item giveRandomArakiRelic() {
        Class<? extends Item>[] pool = new Class[]{
                Jojo1.class, Jojo2.class, Jojo3.class, Jojo4.class, Jojo5.class,
                Jojo6.class, Jojo7.class, Jojo8.class, Jojo9.class
        };
        int index = Random.Int(pool.length);
        Item relic = giveItem(Reflection.newInstance(pool[index]));
        GLog.h(Messages.get(Araki.class, String.valueOf(index + 1)));
        return relic;
    }

    //rocacaca's reward pool - one random item from the set, at the given quantity
    @SuppressWarnings("unchecked")
    private Item randomRocacacaReward(int quantity) {
        Class<? extends Item>[] pool = new Class[]{
                Kingt.class, StoneOfAdvanceguard.class, Xray.class, Kings.class,
                Kingm.class, Kingw.class, Kingc.class, Kinga.class
        };
        return randomOf(pool).quantity(quantity);
    }

    //no walking away mid-pull
    @Override
    public void onBackPressed() {
        if (state != State.SPINNING) {
            super.onBackPressed();
        }
    }
}
