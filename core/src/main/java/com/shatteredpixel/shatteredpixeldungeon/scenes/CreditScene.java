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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.RectF;

public class CreditScene extends PixelScene {

	private static final int WIDTH = 120;
	private Archs archs;

	@Override
	public void create() {
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_1},
				new float[]{1},
				false);

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();

		archs = new Archs();
		archs.setSize(w, h);
		add(archs);

		// 어두운 배경 레이어
		add(new ColorBlock(w, h, 0x88000000));

		w -= insets.left + insets.right;
		h -= insets.top + insets.bottom;

		// 제목
		IconTitle title = new IconTitle(Icons.SHPX.get(), "개발자 정보");
		title.setSize(200, 0);
		title.setPos(
				insets.left + (w - title.reqWidth()) / 2f,
				insets.top + (20 - title.height()) / 2f
		);
		align(title);
		add(title);

		// 스크롤 가능한 컨텐츠 (스크롤바 숨김)
		ScrollPane list = new ScrollPane(new Component()) {
			@Override
			protected void layout() {
				super.layout();
				// 스크롤바 숨기기
				if (thumb != null) {
					thumb.visible = false;
				}
			}
		};
		add(list);

		Component content = list.content();
		content.clear();

		float pos = 0;

		// 중앙 정렬을 위한 오프셋
		final int OFFSET = 10;

		//*** 죠죠의 기묘한 던전 ***
		CreditsBlock shpx = new CreditsBlock(true, Window.TITLE_COLOR,
				"죠죠의 기묘한 던전",
				Icons.SHPX.get(),
				"Developed by: _Ongoro_\nBased on SPD's open source",
				" ",
				"https://www.youtube.com/watch?v=tLyRpGKWXRs");
		shpx.setRect(OFFSET, pos, WIDTH - 20, 0);
		content.add(shpx);
		pos = shpx.bottom() + 10;

		CreditsBlock alex = new CreditsBlock(false, Window.SHPX_COLOR,
				"Link",
				Icons.KRISTJAN.get(),
				"공식 카톡방",
				"url",
				"https://open.kakao.com/o/gC7ZgGjd");
		alex.setSize((WIDTH - 20) / 2f, 0);
		alex.setPos(OFFSET, pos);
		content.add(alex);

		CreditsBlock charlie = new CreditsBlock(false, Window.SHPX_COLOR,
				"Pixel Design",
				Icons.CELESTI.get(),
				"너구리풀",
				"",
				"https://www.youtube.com/watch?v=FPyIMtXsIcY");
		charlie.setRect(alex.right(), alex.top(), (WIDTH - 20) / 2f, 0);
		content.add(charlie);
		pos = charlie.bottom() + 10;

		addLine(pos - 4, content, OFFSET);
		pos += 4;

		//*** Pixel Dungeon Credits ***
		final int WATA_COLOR = 0x55AAFF;
		CreditsBlock wata = new CreditsBlock(true, WATA_COLOR,
				"To Be Continued..",
				Icons.WATA.get(),
				"Next Update : 3.0f",
				"",
				"");
		wata.setRect(OFFSET, pos, WIDTH - 20, 0);
		content.add(wata);
		pos = wata.bottom() + 10;

		addLine(pos - 4, content, OFFSET);
		pos += 4;

		//*** 감사 인사 ***
		final int GDX_COLOR = 0xE44D3C;
		CreditsBlock gdx = new CreditsBlock(true,
				GDX_COLOR,
				null,
				Icons.LIBGDX.get(),
				"Thank you for playing!",
				null,
				null);
		gdx.setRect(OFFSET, pos, WIDTH - 20, 0);
		content.add(gdx);
		pos = gdx.bottom() + 10;

		content.setSize(WIDTH, pos);

		float listHeight = h - title.bottom() - 60; // 제목 아래 공간
		list.setRect(
				insets.left + (w - WIDTH) / 2f,
				insets.top + title.bottom() + 10,
				WIDTH,
				listHeight
		);
		list.scrollTo(0, 0);

		ExitButton btnExit = new ExitButton() {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchScene(AboutScene.class);
			}
		};
		btnExit.setPos(Camera.main.width - btnExit.width() - insets.right, insets.top);
		add(btnExit);

		fadeIn();
	}

	private void addLine(float y, Component content, int offset) {
		ColorBlock line = new ColorBlock(WIDTH - 20, 1, 0xFF333333);
		line.x = offset;
		line.y = y;
		content.add(line);
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchScene(AboutScene.class);
	}

	private static class CreditsBlock extends Component {

		boolean large;
		RenderedTextBlock title;
		Image avatar;
		Flare flare;
		RenderedTextBlock body;

		RenderedTextBlock link;
		ColorBlock linkUnderline;
		PointerArea linkButton;

		//many elements can be null, but body is assumed to have content.
		private CreditsBlock(boolean large, int highlight, String title, Image avatar, String body, String linkText, String linkUrl) {
			super();

			this.large = large;

			if (title != null) {
				this.title = PixelScene.renderTextBlock(title, large ? 8 : 6);
				if (highlight != -1) this.title.hardlight(highlight);
				add(this.title);
			}

			if (avatar != null) {
				this.avatar = avatar;
				add(this.avatar);
			}

			if (large && highlight != -1 && this.avatar != null) {
				this.flare = new Flare(7, 24).color(highlight, true).show(this.avatar, 0);
				this.flare.angularSpeed = 20;
			}

			this.body = PixelScene.renderTextBlock(body, 6);
			if (highlight != -1) this.body.setHightlighting(true, highlight);
			if (large) this.body.align(RenderedTextBlock.CENTER_ALIGN);
			add(this.body);

			if (linkText != null && linkUrl != null && !linkUrl.isEmpty()) {

				int color = 0xFFFFFFFF;
				if (highlight != -1) color = 0xFF000000 | highlight;
				this.linkUnderline = new ColorBlock(1, 1, color);
				add(this.linkUnderline);

				this.link = PixelScene.renderTextBlock(linkText, 6);
				if (highlight != -1) this.link.hardlight(highlight);
				add(this.link);

				linkButton = new PointerArea(0, 0, 0, 0) {
					@Override
					protected void onClick(PointerEvent event) {
						ShatteredPixelDungeon.platform.openURI(linkUrl);
					}
				};
				add(linkButton);
			}

		}

		@Override
		protected void layout() {
			super.layout();

			float topY = top();

			if (title != null) {
				title.maxWidth((int) width());
				title.setPos(x + (width() - title.width()) / 2f, topY);
				topY += title.height() + (large ? 2 : 1);
			}

			if (large) {

				if (avatar != null) {
					avatar.x = x + (width() - avatar.width()) / 2f;
					avatar.y = topY;
					PixelScene.align(avatar);
					if (flare != null) {
						flare.point(avatar.center());
					}
					topY = avatar.y + avatar.height() + 2;
				}

				body.maxWidth((int) width());
				body.setPos(x + (width() - body.width()) / 2f, topY);
				topY += body.height() + 2;

			} else {

				if (avatar != null) {
					avatar.x = x;
					body.maxWidth((int) (width() - avatar.width - 1));

					float fullAvHeight = Math.max(avatar.height(), 16);
					if (fullAvHeight > body.height()) {
						avatar.y = topY + (fullAvHeight - avatar.height()) / 2f;
						PixelScene.align(avatar);
						body.setPos(avatar.x + avatar.width() + 1, topY + (fullAvHeight - body.height()) / 2f);
						topY += fullAvHeight + 1;
					} else {
						avatar.y = topY + (body.height() - fullAvHeight) / 2f;
						PixelScene.align(avatar);
						body.setPos(avatar.x + avatar.width() + 1, topY);
						topY += body.height() + 2;
					}

				} else {
					topY += 1;
					body.maxWidth((int) width());
					body.setPos(x, topY);
					topY += body.height() + 2;
				}

			}

			if (link != null) {
				if (large) topY += 1;
				link.maxWidth((int) width());
				link.setPos(x + (width() - link.width()) / 2f, topY);
				topY += link.height() + 2;

				linkButton.x = link.left() - 1;
				linkButton.y = link.top() - 1;
				linkButton.width = link.width() + 2;
				linkButton.height = link.height() + 2;

				linkUnderline.size(link.width(), PixelScene.align(0.49f));
				linkUnderline.x = link.left();
				linkUnderline.y = link.bottom() + 1;

			}

			topY -= 2;

			height = Math.max(height, topY - top());
		}
	}
}

