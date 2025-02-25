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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rebel;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AnnasuiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JojoSprite;
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

public class Annasui extends NPC {

    {
        spriteClass = AnnasuiSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    private boolean seenBefore = false;

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
            die(null);
            return true;
        }

        if (SPDSettings.getSkin() == 0 || SPDSettings.getSkin2() == 0 || SPDSettings.getSkin3() == 0 || SPDSettings.getSkin4() == 0 || SPDSettings.getSkin5() == 0) {
            if (SPDSettings.getBrando() >= 3 && !seenBefore) {
                Sample.INSTANCE.play(Assets.Sounds.ANNA);
                this.yell(Messages.get(this, "notice"));
                seenBefore = true;
            }
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
        Sample.INSTANCE.play(Assets.Sounds.ANNA);

        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        {

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {

                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(name()),
                            Messages.get(Annasui.class, "0", SPDSettings.getBrando()),
                            Messages.get(Annasui.class, "1"),
                            Messages.get(Annasui.class, "2"),
                            Messages.get(Annasui.class, "3"),
                            Messages.get(Annasui.class, "4"),
                            Messages.get(Annasui.class, "5"),
                            Messages.get(Annasui.class, "6i")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                if (SPDSettings.getSkin() == 0) {
                                    if (SPDSettings.getBrando() >= 3) {
                                        SPDSettings.addSkin(1);
                                        SPDSettings.addBrando(-3);
                                        Emporio.retu();
                                        GLog.p(Messages.get(Annasui.class, "g"));
                                        Sample.INSTANCE.play(Assets.Sounds.BADGE);
                                    } else {
                                        GLog.p(Messages.get(Annasui.class, "nc"));
                                    }
                                } else {
                                    GLog.p(Messages.get(Annasui.class, "r"));
                                }
                            } else if (index == 1) {
                                if (SPDSettings.getSkin2() == 0) {
                                    if (SPDSettings.getBrando() >= 3) {
                                        SPDSettings.addSkin2(1);
                                        SPDSettings.addBrando(-3);
                                        Emporio.retu();
                                        GLog.p(Messages.get(Annasui.class, "g"));
                                        Sample.INSTANCE.play(Assets.Sounds.BADGE);
                                    } else {
                                        GLog.p(Messages.get(Annasui.class, "nc"));
                                    }
                                } else {
                                    GLog.p(Messages.get(Annasui.class, "r"));
                                }
                            } else if (index == 2) {
                                if (SPDSettings.getSkin3() == 0) {
                                    if (SPDSettings.getBrando() >= 3) {
                                        SPDSettings.addSkin3(1);
                                        SPDSettings.addBrando(-3);
                                        Emporio.retu();
                                        GLog.p(Messages.get(Annasui.class, "g"));
                                        Sample.INSTANCE.play(Assets.Sounds.BADGE);
                                    } else {
                                        GLog.p(Messages.get(Annasui.class, "nc"));
                                    }
                                } else {
                                    GLog.p(Messages.get(Annasui.class, "r"));
                                }
                            } else if (index == 3) {
                                if (SPDSettings.getSkin4() == 0) {
                                    if (SPDSettings.getBrando() >= 3) {
                                        SPDSettings.addSkin4(1);
                                        SPDSettings.addBrando(-3);
                                        Emporio.retu();
                                        GLog.p(Messages.get(Annasui.class, "g"));
                                        Sample.INSTANCE.play(Assets.Sounds.BADGE);
                                    } else {
                                        GLog.p(Messages.get(Annasui.class, "nc"));
                                    }
                                } else {
                                    GLog.p(Messages.get(Annasui.class, "r"));
                                }
                            } else if (index == 4) {
                                if (SPDSettings.getSkin5() == 0) {
                                    if (SPDSettings.getBrando() >= 3) {
                                        SPDSettings.addSkin5(1);
                                        SPDSettings.addBrando(-3);
                                        Emporio.retu();
                                        GLog.p(Messages.get(Annasui.class, "g"));
                                        Sample.INSTANCE.play(Assets.Sounds.BADGE);
                                    } else {
                                        GLog.p(Messages.get(Annasui.class, "nc"));
                                    }
                                } else {
                                    GLog.p(Messages.get(Annasui.class, "r"));
                                }
                            } else {
                                if (SPDSettings.getSkin6() == 0) {
                                    if (SPDSettings.getBrando() >= 3) {
                                        SPDSettings.addSkin6(1);
                                        SPDSettings.addBrando(-3);
                                        Emporio.retu();
                                        GLog.p(Messages.get(Annasui.class, "g"));
                                        Sample.INSTANCE.play(Assets.Sounds.BADGE);
                                    } else {
                                        GLog.p(Messages.get(Annasui.class, "nc"));
                                    }
                                } else {
                                    GLog.p(Messages.get(Annasui.class, "r"));
                                }
                            }
                        }
                    });
                }
            });

        }
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

}
