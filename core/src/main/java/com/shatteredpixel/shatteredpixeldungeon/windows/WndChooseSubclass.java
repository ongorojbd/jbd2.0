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
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.SteelBallRunEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class WndChooseSubclass extends Window {
	
	private static final int WIDTH		= 130;
	private static final float GAP		= 2;

	private ScrollPane list;

	public WndChooseSubclass(final TengusMask tome, final Hero hero ) {
		
		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH-16, 0 );
		add( titlebar );

		IconButton random = new IconButton(Icons.SHUFFLE.get()){
			@Override
			protected void onClick() {
				super.onClick();
				GameScene.show(new WndOptions(Icons.SHUFFLE.get(),
						Messages.get(WndChooseSubclass.class, "random_title"),
						Messages.get(WndChooseSubclass.class, "random_sure"),
						Messages.get(WndChooseSubclass.class, "yes"),
						Messages.get(WndChooseSubclass.class, "no")){
					@Override
					protected void onSelect(int index) {
						super.onSelect(index);
						if (index == 0){
							WndChooseSubclass.this.hide();
							HeroSubClass cls = Random.oneOf(availableSubClasses(hero));
							tome.choose(cls);
							GameScene.show(new WndInfoSubclass(hero.heroClass, cls));
						}
					}
				});
			}

			@Override
			public void update() {
				if (Statistics.qualifiedForRandomVictoryBadge){
					icon.tint(1, 1, 1, (float)Math.abs(Math.cos(1.5f*Math.PI* Game.timeTotal)/2f));
				}
				super.update();
			}

			@Override
			protected String hoverText() {
				return Messages.get(WndChooseSubclass.class, "random_title");
			}
		};
		random.setRect(WIDTH-16, 0, 16, 16);
		add(random);

		RenderedTextBlock message = PixelScene.renderTextBlock( 6 );
		message.text( Messages.get(this, "message"), WIDTH );
		message.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( message );

		float pos = message.bottom() + 3*GAP;

		list = new ScrollPane(new Component());
		add(list);
		Component content = list.content();

		ArrayList<HeroSubClass> subClasses = selectableSubClasses(hero);
		float listPos = 0;
		float peekHeight = 0;

		for (int i = 0; i < subClasses.size(); i++){
			final HeroSubClass subCls = subClasses.get(i);
			RedButton btnCls = new RedButton( subCls.shortDesc(), 6 ) {
				@Override
				protected void onClick() {
					if (!canChoose(subCls)) {
						if (subCls == HeroSubClass.INVOKER) {
							if (!Badges.isUnlocked(Badges.Badge.YORIHIMES)) {
								GLog.n(Messages.get(WndChooseSubclass.this, "not_enough_badge_invoker"));
							} else {
								GLog.n(Messages.get(WndChooseSubclass.this, "not_enough_coin_invoker"));
							}
						} else {
							GLog.n(Messages.get(WndChooseSubclass.this, "not_enough_token"));
						}
						return;
					}
					GameScene.show(new WndOptions(new HeroIcon(subCls),
							Messages.titleCase(subCls.title()),
							Messages.get(WndChooseSubclass.this, "are_you_sure"),
							Messages.get(WndChooseSubclass.this, "yes"),
							Messages.get(WndChooseSubclass.this, "no")){
						@Override
						protected void onSelect(int index) {
							hide();
							if (index == 0 && WndChooseSubclass.this.parent != null){
								WndChooseSubclass.this.hide();
								tome.choose( subCls );
								Statistics.qualifiedForRandomVictoryBadge = false;
							}
						}
					});
				}
			};
			btnCls.leftJustify = true;
			btnCls.multiline = true;
			btnCls.setSize(WIDTH-20, btnCls.reqHeight()+2);
			btnCls.setRect( 0, listPos, WIDTH-20, btnCls.reqHeight()+2);
			content.add( btnCls );

			if (canChoose(subCls)) {
				IconButton clsInfo = new IconButton(Icons.get(Icons.INFO)){
					@Override
					protected void onClick() {
						GameScene.show(new WndInfoSubclass(Dungeon.hero.heroClass, subCls));
					}
				};
				clsInfo.setRect(WIDTH-20, btnCls.top() + (btnCls.height()-20)/2, 20, 20);
				content.add(clsInfo);
			}

			//4번째 버튼을 절반만 보여줘서 아래에 선택지가 더 있음을 알림
			if (i == 3) peekHeight = btnCls.top() + btnCls.height()/2f;

			listPos = btnCls.bottom() + GAP;
		}
		content.setSize(WIDTH, listPos);

		float maxListHeight = PixelScene.uiCamera.height - pos - 18 - GAP - 24;
		float listHeight = Math.min(listPos, maxListHeight);
		if (subClasses.size() > 4) listHeight = Math.min(listHeight, peekHeight);
		float listTop = pos;
		pos += listHeight + GAP;

		RedButton btnCancel = new RedButton( Messages.get(this, "cancel") ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, pos, WIDTH, 18 );
		add( btnCancel );

		//ScrollPane은 배치 시점의 창 카메라 기준으로 화면 좌표를 계산하므로 창 크기를 먼저 확정해야 함
		resize( WIDTH, (int)btnCancel.bottom() );
		list.setRect(0, listTop, WIDTH, listHeight);
	}

	@Override
	public void offset(int xOffset, int yOffset) {
		super.offset(xOffset, yOffset);
		list.setPos(list.left(), list.top()); //triggers layout
	}

	private static boolean canChoose(HeroSubClass subCls) {
		if (subCls == HeroSubClass.INVOKER) {
			return Badges.isUnlocked(Badges.Badge.YORIHIMES) && SPDSettings.getSpecialcoin() >= 5;
		}
		return subCls != HeroSubClass.SUMMONER || SPDSettings.getToken() >= 2;
	}

	private static ArrayList<HeroSubClass> selectableSubClasses(Hero hero) {
		ArrayList<HeroSubClass> result = new ArrayList<>(Arrays.asList(hero.heroClass.subClasses()));
		HeroSubClass bonus = SteelBallRunEvent.bonusSubClass(hero.heroClass);
		if (bonus != null && SteelBallRunEvent.isActive() && !result.contains(bonus)) {
			result.add(2, bonus);
		}
		return result;
	}

	private static HeroSubClass[] availableSubClasses(Hero hero) {
		ArrayList<HeroSubClass> result = new ArrayList<>();
		for (HeroSubClass subCls : selectableSubClasses(hero)) {
			if (canChoose(subCls)) {
				result.add(subCls);
			}
		}
		return result.toArray(new HeroSubClass[0]);
	}
}
