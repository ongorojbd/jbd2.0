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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Fugomob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Kakyoin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Val;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
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

public class Willa extends Spell {

    {
        image = ItemSpriteSheet.EBONY_CHEST;
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

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                spawnPoints.add( p );
            }
        }

        if (!spawnPoints.isEmpty()){

            for (Char ch : Actor.chars()){
                if (ch instanceof Willamob && ch.buff(SummonElemental.InvisAlly.class) != null){
                    ScrollOfTeleportation.appear( ch, Random.element(spawnPoints) );
                    ((Willamob) ch).state = ((Willamob) ch).HUNTING;
                    curUser.spendAndNext(Actor.TICK);
                    return;
                }
            }

            GameScene.show(
                    new WndOptions(new WillaSprite(),
                            Messages.get(Willamob.class, "w"),
                            Messages.get(Willamob.class, "prick_warn"),
                            Messages.get(Willamob.class, "yes"),
                            Messages.get(Willamob.class, "no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0)

                                switch (Random.Int( 2 )) {
                                    case 0:
                                        GLog.p(Messages.get(Willamob.class, "s"));
                                        Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                                        Buff.affect(hero, Barrier.class).setShield((int) (0.6f * hero.HT + 10));
                                        Buff.affect(hero, Bless.class, 30f);
                                        new Flare( 5, 32 ).color( 0xFFFF33, true ).show( hero.sprite, 3f );
                                        break;
                                    case 1:
                                        Sample.INSTANCE.play(Assets.Sounds.HIT);
                                        hero.damage(hero.HP/2, this);
                                        Camera.main.shake(9, 0.5f);
                                        GLog.n(Messages.get(Willamob.class, "fail"));
                                        break;
                                }


                        }

                    }
            );

            Willamob elemental = Reflection.newInstance(summonClass);
            GameScene.add( elemental );
            elemental.HP = elemental.HT;
            ScrollOfTeleportation.appear( elemental, Random.element(spawnPoints) );
            curUser.spendAndNext(Actor.TICK);













            detach(Dungeon.hero.belongings.backpack);






        } else {
            GLog.w(Messages.get(SpiritHawk.class, "no_space"));
        }

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFF9966, 1f);
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
        return 15 * quantity;
    }

}
