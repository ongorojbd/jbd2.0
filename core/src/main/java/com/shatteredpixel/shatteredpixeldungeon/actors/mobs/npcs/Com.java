/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Castleintro;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Cfree;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SeedAnalysisScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SeedFindScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ComSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YukakoSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBlacksmith;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Com extends NPC {

    {
        spriteClass = ComSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    private static final String SPAWNED		= "spawned";
    private static boolean spawned;
    private boolean seenBefore = false;

    @Override
    protected boolean act() {

        if(SPDSettings.getJojo() == 1 && !seenBefore){
            Sample.INSTANCE.play( Assets.Sounds.SECRET, 2,  0.66f );
            this.yell(Messages.get(this, "notice"));
            seenBefore = true;
        } else if(SPDSettings.getJojo() == 2 && !seenBefore){
            Sample.INSTANCE.play( Assets.Sounds.SECRET, 2,  0.66f );
            this.yell(Messages.get(this, "notice"));
            seenBefore = true;
        }

        return super.act();
    }

    @Override
    public void beckon (int cell) {
        //do nothing
    }

    private void tell( String text ) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show( new WndQuest( Com.this, text ));
            }
        });
    }

    @Override
    public boolean interact(Char c) {
        Sample.INSTANCE.play( Assets.Sounds.SECRET, 2,  0.66f );

        sprite.turnTo( pos, c.pos );

        if (c != Dungeon.hero){
            return true;
        }

        if (SPDSettings.getJojo() == 1) {
            Item q = new Cfree().quantity(3);
            GLog.h(Messages.get(Jolyne.class, "3"));
            if (q.doPickUp(Dungeon.hero)) {
                GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", q.name())));
            } else {
                Dungeon.level.drop(q, Dungeon.hero.pos).sprite.drop();
            }
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
            SPDSettings.addJojo(2);
        }

        if (SPDSettings.getJojo() == 2) {
            Item z = new Cfree().quantity(3);
            GLog.h(Messages.get(Jolyne.class, "3"));
            if (z.doPickUp(Dungeon.hero)) {
                GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", z.name())));
            } else {
                Dungeon.level.drop(z, Dungeon.hero.pos).sprite.drop();
            }
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
            SPDSettings.addJojo(1);
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {

                GameScene.show(new WndOptions(
                        sprite(),
                        Messages.titleCase(name()),
                        Messages.get(Com.class, "0", SPDSettings.getSid()),
                        Messages.get(Com.class, "1"),
                        Messages.get(Com.class, "2"),
                        Messages.get(Com.class, "3")
//                        Messages.get(Com.class, "4"),
//                        Messages.get(Com.class, "5")
                ){
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            tell(Messages.get(Com.class, "q", SPDSettings.getSpecialcoin()));
                        } else if (index == 1) {
                            tell(Messages.get(Com.class, "w", SPDSettings.getDio()));
                        } else if (index == 2) {
                            tell(Messages.get(Com.class, "w", SPDSettings.getDio()));
                        } else if (index == 3) {
                            if (SPDSettings.getSid() >= 1) {
                                ShatteredPixelDungeon.switchScene(SeedFindScene.class);
                            }  else {
                                GLog.p(Messages.get(Annasui.class, "sid"));
                            }
                        } else {
                            if (SPDSettings.getSid() >= 1) {
                                ShatteredPixelDungeon.switchScene( SeedAnalysisScene.class );
                            }  else {
                                GLog.p(Messages.get(Annasui.class, "sid"));
                            }
                        }

                    }
                });
            }
        });


        return true;
    }


    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }


    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }


    @Override
    public boolean reset() {
        return true;
    }

}
