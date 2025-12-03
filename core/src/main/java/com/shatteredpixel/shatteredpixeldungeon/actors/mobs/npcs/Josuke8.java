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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.NitoDismantleHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.NitoDismantleHammer2;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo1;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo3;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo4;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo5;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo6;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo7;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo8;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo9;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Josuke8Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Josuke8 extends NPC {

    {
        spriteClass = Josuke8Sprite.class;

        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(LockedFloor.class) != null){
            destroy();
            sprite.killAndErase();
            die(null);
            return true;
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

        sprite.turnTo( pos, c.pos );

        if (c != Dungeon.hero){
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        sprite(),
                        Messages.titleCase(name()),
                        Messages.get(NitoDismantleHammer2.class, "think"),
                        Messages.get(NitoDismantleHammer2.class, "yes"),
                        Messages.get(NitoDismantleHammer2.class, "no")) {
                    @Override
                    protected void onSelect(int index) {
                        switch (index) {
                            case 0:
                                if (Dungeon.gold > 999) {
                                    Dungeon.gold -= 1000;

                                    GLog.n("- 1000골드!");
                                    Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                    CellEmitter.get(Dungeon.hero.pos).burst(Speck.factory(Speck.COIN), 12);

                                    if (Statistics.manga < 3) {
                                        if (Random.Int(33) == 0) {
                                            Statistics.manga += 1;
                                            new Flare(6, 32).color(0xFFAA00, true).show(Dungeon.hero.sprite, 4f);
                                            Sample.INSTANCE.play(Assets.Sounds.MASTERY, 0.7f, 1.2f);
                                            GameScene.flash(0xFFCC00);
                                            switch (Random.Int(9)) {

                                                case 0:
                                                    Item a = new Jojo1();
                                                    Dungeon.level.drop(a, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "1"));
                                                    break;
                                                case 1:
                                                    Item b = new Jojo2();
                                                    Dungeon.level.drop(b, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "2"));
                                                    break;
                                                case 2:
                                                    Item cc = new Jojo3();
                                                    Dungeon.level.drop(cc, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "3"));
                                                    break;
                                                case 3:
                                                    Item d = new Jojo4();
                                                    Dungeon.level.drop(d, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "4"));
                                                    break;
                                                case 4:
                                                    Item e = new Jojo5();
                                                    Dungeon.level.drop(e, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "5"));
                                                    break;
                                                case 5:
                                                    Item f = new Jojo6();
                                                    Dungeon.level.drop(f, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "6"));
                                                    break;
                                                case 6:
                                                    Item g = new Jojo7();
                                                    Dungeon.level.drop(g, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "7"));
                                                    break;
                                                case 7:
                                                    Item h = new Jojo8();
                                                    Dungeon.level.drop(h, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "8"));
                                                    break;
                                                case 8:
                                                    Item i = new Jojo9();
                                                    Dungeon.level.drop(i, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                    GLog.h(Messages.get(NitoDismantleHammer.class, "9"));
                                                    break;
                                            }
                                        }
                                    }

                                } else {
                                    GLog.n(Messages.get(NitoDismantleHammer2.class, "rev"));
                                }
                                break;
                            case 1:
                                break;
                        }
                    }
                });
            }
        });

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
