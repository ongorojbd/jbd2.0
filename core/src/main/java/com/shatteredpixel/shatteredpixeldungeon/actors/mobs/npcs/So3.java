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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.So3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRhythmGame;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class So3 extends NPC {

    private static final String PLAYED = "played";

    private boolean played = false;

    {
        spriteClass = So3Sprite.class;

        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
            die(null);
            return true;
        }
        return super.act();
    }

    @Override
    public void beckon(int cell) {
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
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean interact(Char c) {
        Sample.INSTANCE.play(Assets.Sounds.SO1);

        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        // 이미 플레이했으면 메시지 표시 후 종료
        if (played) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(name()),
                            Messages.get(So3.class, "already_played"),
                            Messages.get(So3.class, "close")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            // 닫기만 가능
                        }
                    });
                }
            });
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        sprite(),
                        Messages.titleCase(name()),
                        hero.heroClass == HeroClass.CLERIC ? Messages.get(So3.class, "02") : Messages.get(So3.class, "0"),
                        Messages.get(So3.class, "rhythm_game")
                ) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            // 리듬게임 시작
                            if (WndRhythmGame.instance != null) {
                                return; // 이미 열려있으면 무시
                            }
                            
                            // 플레이 여부를 true로 설정
                            played = true;
                            
                            GameScene.show(new WndRhythmGame(
                                    // 성공 콜백
                                    new Callback() {
                                        @Override
                                        public void call() {
                                        }
                                    },
                                    // 실패 콜백
                                    new Callback() {
                                        @Override
                                        public void call() {
                                        }
                                    }
                            ));
                        }
                    }
                });
            }
        });

        return true;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PLAYED, played);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        played = bundle.getBoolean(PLAYED);
    }

}

