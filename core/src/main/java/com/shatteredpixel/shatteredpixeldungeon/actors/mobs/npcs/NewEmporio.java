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
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AnnasuiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EmporioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IrineSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.So2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBossText;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class NewEmporio extends NPC {

    {
        spriteClass = EmporioSprite.class;

        properties.add(Property.IMMOVABLE);
    }
    private int gravityCD = 10;

    private static final String GRAVITY_CD = "gravity_cd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(GRAVITY_CD, gravityCD);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        gravityCD = bundle.getInt(GRAVITY_CD);
    }

    @Override
    protected boolean act() {
        // 쿨다운 감소
        if (gravityCD > 0) gravityCD--;

        if (gravityCD == 6 && Dungeon.hero != null) {
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new EmporioSprite(), new So2Sprite(), new AnnasuiSprite(),
                            new IrineSprite(), new IrineSprite(), new EmporioSprite(),
                            new IrineSprite(), new EmporioSprite()
                    },
                    new String[]{"엠포리오", "???", "아나키스", "아이린", "아이린", "엠포리오", "아이린", "엠포리오"},
                    new String[]{
                            Messages.get(this, "s"),
                            Messages.get(this, "s1"),
                            Messages.get(this, "s2"),
                            Messages.get(this, "s3"),
                            Messages.get(this, "s4"),
                            Messages.get(this, "s5"),
                            Messages.get(this, "s6"),
                            Messages.get(this, "s7"),
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                    }
            );

            Music.INSTANCE.play(Assets.Music.THEME_1, true);

        }

        if (gravityCD == 4 && Dungeon.hero != null) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndBossText(new Johnny(), Messages.get(Johnny.class, "title")) {
                        @Override
                        public void hide() {
                            super.hide();
                        }
                    });
                }
            });

            Badges.validateJohnnyUnlock();
        }

        if (gravityCD == 2 && Dungeon.hero != null) {
            Dungeon.win( Amulet.class );
            Dungeon.deleteGame( GamesInProgress.curSlot, true );
            Game.switchScene( SurfaceScene.class );
        }

        return super.act();
    }

    @Override
    public void beckon (int cell) {
        //do nothing
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
    public boolean interact(Char c) {
        Sample.INSTANCE.play(Assets.Sounds.EMP);

        sprite.turnTo( pos, c.pos );

        if (c != Dungeon.hero){
            return true;
        }

        Dungeon.hero.spendAndNext(1f);

        return true;
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
