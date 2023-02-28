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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Antonio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RetonioSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBlacksmith;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Retonio extends NPC {

    {
        spriteClass = RetonioSprite.class;

        properties.add(Property.IMMOVABLE);
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
    protected boolean act() {
        if (hero.buff(AscensionChallenge.class) != null){
            die(null);
            return true;
        }
        return super.act();
    }

    @Override
    public boolean interact(Char c) {


        sprite.turnTo( pos, c.pos );

        if (c != hero){
            return true;
        }

        {

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(name()),
                            Messages.get(Retonio.class, "0"),
                            Messages.get(Retonio.class, "1"),
                            Messages.get(Retonio.class, "2"),
                            Messages.get(Retonio.class, "3"),
                            Messages.get(Retonio.class, "4"),
                            Messages.get(Retonio.class, "5"),
                            Messages.get(Retonio.class, "6")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){

                                yell(Messages.get(Retonio.class, "11"));
                                GameScene.flash(0xFFFF00);
                                Sample.INSTANCE.play(Assets.Sounds.EAT);
                                GLog.p(Messages.get(Retonio.class, "7"));

                                Buff.affect(hero, MagicalSight.class, 50f);

                                destroy();
                                sprite.killAndErase();
                                die(null);

                            } else if (index == 1) {
                                yell(Messages.get(Retonio.class, "11"));
                                GameScene.flash(0xFFFF00);
                                Sample.INSTANCE.play(Assets.Sounds.EAT);
                                GLog.p(Messages.get(Retonio.class, "8"));

                                Buff.affect(hero, Recharging.class, 15f);
                                Buff.affect(hero, ArtifactRecharge.class).prolong( 15 ).ignoreHornOfPlenty = false;

                                destroy();
                                sprite.killAndErase();
                                die(null);
                            } else if (index == 2) {
                                yell(Messages.get(Retonio.class, "11"));
                                GameScene.flash(0xFFFF00);

                                Item a = new Food();
                                Dungeon.level.drop(a, pos).sprite.drop(pos);

                                destroy();
                                sprite.killAndErase();
                                die(null);
                            } else if (index == 3) {
                                yell(Messages.get(Retonio.class, "11"));
                                GameScene.flash(0xFFFF00);
                                Sample.INSTANCE.play(Assets.Sounds.EAT);
                                GLog.p(Messages.get(Retonio.class, "10"));

                                Buff.affect(hero, FireImbue.class).set(50f);
                                hero.sprite.emitter().burst(FlameParticle.FACTORY, 20);

                                destroy();
                                sprite.killAndErase();
                                die(null);
                            } else if (index == 4) {
                                yell(Messages.get(Retonio.class, "11"));
                                GameScene.flash(0xFFFF00);
                                Sample.INSTANCE.play(Assets.Sounds.EAT);
                                GLog.p(Messages.get(Retonio.class, "15"));

                                hero.HP = Math.min(hero.HP + 30, hero.HT);

                                destroy();
                                sprite.killAndErase();
                                die(null);
                            } else {
                                Sample.INSTANCE.play(Assets.Sounds.HIT);
                                Sample.INSTANCE.play(Assets.Sounds.ALERT);
                                yell(Messages.get(Retonio.class, "12"));

                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Antonio Antonio = new Antonio();
                                Antonio.state = Antonio.HUNTING;
                                Antonio.pos = pos;
                                GameScene.add( Antonio );
                                Antonio.beckon(hero.pos);
                            }
                        }
                    });
                }
            });

        }
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
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

}
