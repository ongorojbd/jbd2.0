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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CaesarZeppeli;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Danny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Supression2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DannySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LisaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SupressionSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillcSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YukakoSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBlacksmith;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
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

public class Dannynpc extends NPC {

    {
        spriteClass = DannySprite.class;

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
                            Messages.get(Dannynpc.class, "0"),
                            Messages.get(Dannynpc.class, "1"),
                            Messages.get(Dannynpc.class, "2")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                GLog.p(Messages.get(Dannynpc.class, "5"));

                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Danny danny = new Danny();
                                danny.state = danny.HUNTING;
                                danny.pos = pos;
                                GameScene.add(danny);
                                danny.beckon(Dungeon.hero.pos);

                            } else {
                                if (hero.heroClass == HeroClass.WARRIOR) {
                                    GLog.p(Messages.get(Dannynpc.class, "6"));
                                } else {
                                    Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                                    Sample.INSTANCE.play(Assets.Sounds.JONATHAN2);

                                    WndDialogueWithPic.dialogue(
                                            new CharSprite[]{new SupressionSprite()},
                                            new String[]{"죠나단 죠스타"},
                                            new String[]{
                                                    Messages.get(Supression2.class, "t3")
                                            },
                                            new byte[]{
                                                    WndDialogueWithPic.IDLE
                                            }
                                    );

                                    destroy();
                                    sprite.killAndErase();
                                    die(null);

                                    Supression2 supression2 = new Supression2();
                                    supression2.state = supression2.HUNTING;
                                    supression2.pos = pos;
                                    GameScene.add(supression2);
                                    supression2.beckon(Dungeon.hero.pos);
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
