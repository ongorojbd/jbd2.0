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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Fugomob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Kakyoin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Val;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FdolTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Mdisc extends Spell {

    {
        image = ItemSpriteSheet.EMBER;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

    }

    @Override
    protected void onCast(Hero hero) {

        ArrayList<Integer> spawnPoints = new ArrayList<>();

        GameScene.show(
                new WndOptions(new SeniorSprite(),
                        Messages.get(Mdisc.class, "00"),
                        Messages.get(Mdisc.class, "0"),
                        Messages.get(Mdisc.class, "1"),
                        Messages.get(Mdisc.class, "2"),
                        Messages.get(Mdisc.class, "3"),
                        Messages.get(Mdisc.class, "4"),
                        Messages.get(Mdisc.class, "5"),
                        Messages.get(Mdisc.class, "6"),
                        Messages.get(Mdisc.class, "7")
                ) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                            GLog.p(Messages.get(Mdisc.class, "8"));
                            Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
                        } else if (index == 1) {
                            Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                            GLog.p(Messages.get(Mdisc.class, "9"));
                            Music.INSTANCE.play(Assets.Music.PRISON_BOSS, true);
                        } else if (index == 2) {
                            Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                            GLog.p(Messages.get(Mdisc.class, "10"));
                            Music.INSTANCE.play(Assets.Music.CAVES_BOSS, true);
                        } else if (index == 3) {
                            Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                            GLog.p(Messages.get(Mdisc.class, "11"));
                            Music.INSTANCE.play(Assets.Music.CITY_BOSS, true);
                        } else if (index == 4) {
                            Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                            GLog.p(Messages.get(Mdisc.class, "12"));
                            Music.INSTANCE.play(Assets.Music.HALLS_BOSS, true);
                        } else if (index == 5) {
                            Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                            GLog.p(Messages.get(Mdisc.class, "13"));
                            Music.INSTANCE.play(Assets.Music.LABS_BOSS, true);
                        } else {
                            Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                            GLog.p(Messages.get(Mdisc.class, "14"));
                            Music.INSTANCE.play(Assets.Music.YUUKI, true);
                        }
                    }

                }
        );


        curUser.spendAndNext(Actor.TICK);


    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x00FF00, 0.7f);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

}
