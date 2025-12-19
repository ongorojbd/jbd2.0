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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.challenges;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Amblance;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bmore;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Civil;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cream;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diego;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diego12;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Fugomob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Jonny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Kakyoin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Val;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Madeinheaven;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CreamTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FdolTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BmoreSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiegoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JosukeDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Map3 extends Spell {

    {
        image = ItemSpriteSheet.MAP3;

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



        if (Dungeon.depth == 15) {
            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            GameScene.flash(0x990000);
            Music.INSTANCE.play(Assets.Music.CIV, true);
            GLog.p(Messages.get(Civil.class, "3"));

            switch(Dungeon.hero.heroClass){
                case WARRIOR:
                case ROGUE:
                case MAGE:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DiegoSprite()},
                            new String[]{"디에고 브란도"},
                            new String[]{
                                    Messages.get(Diego.class, "1")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case HUNTRESS:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DiegoSprite()},
                            new String[]{"디에고 브란도"},
                            new String[]{
                                    Messages.get(Diego.class, "6")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case DUELIST:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DiegoSprite(), new JosukeDialogSprite()},
                            new String[]{"디에고 브란도", "죠스케"},
                            new String[]{
                                    Messages.get(Diego.class, "12"),
                                    Messages.get(Val.class, "9")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    Buff.affect(Dungeon.hero, Adrenaline.class, 1f);
                    break;
                case JOHNNY:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DiegoSprite()},
                            new String[]{"디에고 브란도"},
                            new String[]{
                                    Messages.get(Diego.class, "7")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
            }

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                    spawnPoints.add( p );
                }
            }

            if (!spawnPoints.isEmpty()){

                Diego elemental = new Diego();
                elemental.state = elemental.HUNTING;
                GameScene.add( elemental );
                ScrollOfTeleportation.appear( elemental, Random.element(spawnPoints) );

                detach(Dungeon.hero.belongings.backpack);

            } else {
                GLog.w(Messages.get(SpiritHawk.class, "no_space"));
            }

            updateQuickslot();

            Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);
        }else
        {
            GLog.h(Messages.get(Civil.class, "43"));
            SpellSprite.show( curUser, SpellSprite.MAP );
            Sample.INSTANCE.play(Assets.Sounds.READ);
        }


        hero.sprite.operate(hero.pos);
        hero.spendAndNext( 1f );
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
