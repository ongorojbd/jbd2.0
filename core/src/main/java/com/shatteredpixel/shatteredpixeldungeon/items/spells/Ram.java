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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
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
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Ram extends Spell {

    {
        image = ItemSpriteSheet.RAM;
    }

    private Class<? extends Willamob> summonClass = Willamob.class;

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
                    new WndOptions(new StatueSprite(),
                            Messages.get(Willamob.class, "i"),
                            Messages.get(Willamob.class, "y"),
                            Messages.get(Willamob.class, "s1"),
                            Messages.get(Willamob.class, "s3"),
                            Messages.get(Willamob.class, "s4"),
                            Messages.get(Willamob.class, "s5"),
                            Messages.get(Willamob.class, "s6")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                GameScene.flash(0xFFCC00);
                                Sample.INSTANCE.play(Assets.Sounds.BADGE, 1, 1);

                            GLog.w(Messages.get(Willamob.class, "z4"));
                            GLog.p(Messages.get(Willamob.class, "ap"));
                                Item a = new SmallRation();
                                Dungeon.level.drop(a, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                detach(Dungeon.hero.belongings.backpack);
                            }
                            else if (index == 1){
                                GameScene.flash(0xFFCC00);
                                Sample.INSTANCE.play(Assets.Sounds.BADGE, 1, 1);
                                hero.HP = Math.min(hero.HP + 25, hero.HT);
                                GLog.w(Messages.get(Willamob.class, "z4"));
                                GLog.p(Messages.get(Willamob.class, "ap2"));
                                detach(Dungeon.hero.belongings.backpack);
                          }
                            else if (index == 2){
                                GameScene.flash(0xFFCC00);
                                Sample.INSTANCE.play(Assets.Sounds.BADGE, 1, 1);
                                GLog.w(Messages.get(Willamob.class, "z4"));
                                GLog.p(Messages.get(Willamob.class, "ap3"));
                                Buff.affect(hero, Swiftthistle.TimeBubble.class).reset();;
                                detach(Dungeon.hero.belongings.backpack);
                               }
                            else if (index == 3){
                                GLog.n(Messages.get(Willamob.class, "z5"));
                                new FdolTrap().set(curUser.pos).activate();
                                detach(Dungeon.hero.belongings.backpack);}
                            else  {
                                GameScene.flash(0x660000);
                                Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 1);
                                Sample.INSTANCE.play(Assets.Sounds.HAHAH, 1, 1);
                                GLog.n(Messages.get(Willamob.class, "z6"));
                                detach(Dungeon.hero.belongings.backpack);

                                hero.HP = 1;}
                        }

                    }
            );

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xCC9900, 1f);
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
        return 12 * quantity;
    }

}
