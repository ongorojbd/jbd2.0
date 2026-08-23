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

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.effects.ShadowBox;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;

//Full-screen splash shown over GameScene when entering a floor: a background image
//plus a sequence of messages the player pages through one at a time before returning to play.
public class WndFloorIntro extends Window {

	private static final int MARGIN = 10;
	private static final int SIDE_MARGIN = 12;
	private static final int BOTTOM_MARGIN = 12;

	private final String[] messages;
	private int index = 0;

	private final Image background;
	private final RenderedTextBlock messageText;
	private final RenderedTextBlock continueHint;
	private final ShadowBox messageBG;

	public static void show(String splashAsset, String... messages) {
		if (messages == null || messages.length == 0) {
			return;
		}
		GameScene.show(new WndFloorIntro(splashAsset, messages));
	}

	public WndFloorIntro(String splashAsset, String... messages) {
		super(0, 0, Chrome.get(Chrome.Type.TOAST_TR));

		this.messages = messages;

		shadow.visible = false;
		resize(PixelScene.uiCamera.width, PixelScene.uiCamera.height);
		chrome.visible = false;

		//the window's own camera (created by the Window constructor) is scaled/positioned
		//for a chrome-sized box, not the full screen. Every visual added below is pinned to
		//the real screen-filling uiCamera instead, so the splash actually covers the screen.
		int w = width;
		int h = height;

		background = new Image(splashAsset);
		background.camera = PixelScene.uiCamera;
		background.scale.set(h / background.height);
		background.x = (w - background.width()) / 2f;
		background.y = 0;
		PixelScene.align(background);
		addToBack(background);

		messageText = PixelScene.renderTextBlock("", 6);
		messageText.maxWidth(w - SIDE_MARGIN * 2 - MARGIN * 2);
		messageText.invert();
		messageText.camera = PixelScene.uiCamera;

		messageBG = new ShadowBox();
		messageBG.alpha(0.8f);
		messageBG.camera = PixelScene.uiCamera;
		add(messageBG);
		add(messageText);

		continueHint = PixelScene.renderTextBlock(Messages.get(this, "continue_hint"), 6);
		continueHint.invert();
		continueHint.camera = PixelScene.uiCamera;
		add(continueHint);

		//an explicit full-screen blocker (rather than relying on the inherited one) so a
		//tap anywhere advances the page, regardless of where the (invisible) chrome sits
		PointerArea blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height) {
			@Override
			protected void onClick(PointerEvent event) {
				onBackPressed();
			}
		};
		blocker.camera = PixelScene.uiCamera;
		addToBack(blocker);

		showPage();
	}

	private void showPage() {
		messageText.text(messages[index]);
		messageText.setPos(
				SIDE_MARGIN + MARGIN,
				height - BOTTOM_MARGIN - MARGIN - messageText.height()
		);
		PixelScene.align(messageText);

		messageBG.boxRect(
				SIDE_MARGIN, messageText.top() - MARGIN,
				width - SIDE_MARGIN * 2, messageText.height() + MARGIN * 2
		);

		continueHint.setPos(
				(width - SIDE_MARGIN) - 4 - continueHint.width(),
				(messageText.bottom() + MARGIN) - 4 - continueHint.height()
		);
		PixelScene.align(continueHint);
	}

	@Override
	public void onBackPressed() {
		index++;
		if (index >= messages.length) {
			hide();
		} else {
			showPage();
		}
	}
}
