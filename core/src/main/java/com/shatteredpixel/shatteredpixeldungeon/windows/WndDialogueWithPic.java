package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.badlogic.gdx.utils.Timer;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class WndDialogueWithPic extends Window {
    private CharSprite[] icons;
    private String[] titles;
    private RenderedTextBlock ttl;
    private RenderedTextBlock tf;
    private static final int MARGIN = 2;

    ArrayList<Runnable> runnableArrayList;

    private int textNum = 0;
    public boolean lastDialogue = false;

    private String[] texts;

    private String curText;
    private int letterNum = 0;
    private boolean typing = false;

    public byte spriteActionIndexes[];

    public static final byte IDLE = 0;
    public static final byte RUN = 1;
    public static final byte ATTACK = 2;
    public static final byte DIE = 3;

    public enum WndType {
        NORMAL,
        FINAL,
        CUSTOM
    }

    public static void dialogue(CharSprite[] icons, String[] titles, String[] text, byte[] spriteActionIndexes) {
        dialogue(icons, titles, text, spriteActionIndexes, WndType.NORMAL, new ArrayList<>());
    }

    public static void dialogue(CharSprite[] icons, String[] titles, String[] text, byte[] spriteActionIndexes, WndType type, ArrayList<Runnable> runnableArrayList) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                WndDialogueWithPic wnd = new WndDialogueWithPic(icons, titles, text, spriteActionIndexes);
                if (type == WndType.FINAL) wnd.lastDialogue = true;
                wnd.runnableArrayList = runnableArrayList;
                GameScene.show(wnd);
            }
        });
    }

    public WndDialogueWithPic(CharSprite[] icons, String[] titles, String[] text, byte spriteActionIndexes[]) {
        super(0, 0, Chrome.get(Chrome.Type.TOAST_TR));

        shadow.visible = false;
        resize(PixelScene.uiCamera.width, PixelScene.uiCamera.height);
        texts = text;
        this.titles = titles;
        textNum = 0;

        this.icons = icons;
        this.spriteActionIndexes = spriteActionIndexes;

        int scale = 8;
        int chromeWidth = PixelScene.landscape() ? PixelScene.uiCamera.width / 2 : PixelScene.uiCamera.width - 4;
        int chromeHeight = Math.round(PixelScene.uiCamera.height * 0.3f);
        chrome.x = (PixelScene.uiCamera.width - chromeWidth) * 0.5f;
        chrome.y = (PixelScene.uiCamera.height - chromeHeight - 2);
        chrome.size(chromeWidth, chromeHeight);
        addToFront(chrome);

        float y = chrome.y + MARGIN;

        // 모든 스프라이트 초기에 투명하게 설정
        for (CharSprite icon : icons) {
            icon.alpha(0f);
            icon.scale.set(scale);
            while (icon.width() > PixelScene.uiCamera.width / 2) {
                scale--;
                icon.scale.set(scale);
            }
            icon.x = chrome.x;
            icon.y = chrome.y - icon.height * (scale / 1.5f);
            addToBack(icon);
        }

        ttl = PixelScene.renderTextBlock(titles[0], 11);
        ttl.maxWidth(chromeWidth - 4 * MARGIN);
        ttl.setPos(chrome.x + icons[0].width() + 2 * MARGIN, chrome.y - 2 * MARGIN - ttl.height());
        add(ttl);

        tf = PixelScene.renderTextBlock("", 9);
        tf.maxWidth(chromeWidth - 4 * MARGIN);
        tf.setPos(chrome.x + 2 * MARGIN, y + 2 * MARGIN);
        add(tf);

        PointerArea blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height) {
            @Override
            protected void onClick(PointerEvent event) {
                onBackPressed();
            }
        };
        blocker.camera = PixelScene.uiCamera;
        addToBack(blocker);

        startText(texts[0]);
    }

    private Timer timer = new Timer();

    @Override
    public void onBackPressed() {
        if (typing) {
            typing = false;
            timer.stop();
            timer.clear();
            tf.text(texts[textNum]);
        } else {
            textNum++;
            if (textNum >= texts.length) {
                hide();
            } else {
                startText(texts[textNum]);
            }
        }
        update();
    }

    private void startText(String text) {
        // 모든 스프라이트 초기에 투명하게 설정
        for (CharSprite icon : icons) {
            icon.alpha(0f);
        }

        // 현재 말하는 캐릭터의 스프라이트만 표시
        icons[textNum].alpha(1f);
        ttl.text(titles[textNum]);

        // 해당 스프라이트의 액션 처리
        if (textNum < spriteActionIndexes.length) {
            switch (spriteActionIndexes[textNum]) {
                case IDLE:
                default:
                    icons[textNum].play(icons[textNum].idle);
                    break;
                case RUN:
                    icons[textNum].play(icons[textNum].run);
                    break;
                case ATTACK:
                    icons[textNum].play(icons[textNum].attack);
                    break;
                case DIE:
                    icons[textNum].play(icons[textNum].die);
                    break;
            }
        }

        curText = "";
        tf.text(curText);
        update();
        letterNum = 0;
        typing = true;
        timer.clear();
        timer.start();

        try {
            runnableArrayList.get(textNum).run();
        } catch (Exception ignored) {
        }

        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (typing) nextLetter(text);
                WndDialogueWithPic.this.update();
            }
        }, 0f, 0.04f, text.length());
    }

    private void nextLetter(String text) {
        curText += text.charAt(letterNum);
        tf.text(curText);
        letterNum++;
        if (letterNum >= text.length()) {
            typing = false;
            timer.stop();
            timer.clear();
        }
    }
}