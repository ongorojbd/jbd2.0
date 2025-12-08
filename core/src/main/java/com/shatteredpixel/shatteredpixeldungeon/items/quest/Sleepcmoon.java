/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown7;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Madeinheaven;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Sleepcmoon extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.CM;

        unique = true;

        defaultAction = AC_LIGHT;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_LIGHT);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);

        identify();

        Sleepcmoon pick = Dungeon.hero.belongings.getItem(Sleepcmoon.class);
        if (action.equals(AC_LIGHT)) {
            if (Dungeon.depth > 20) {
                if (hero.buff(ShovelDigCoolDown7.class) != null) {
                    GLog.w(Messages.get(this, "not_ready"));
                } else {
                    int lvl = this.level();
                    Sample.INSTANCE.play(Assets.Sounds.HAHAH);
                    GLog.n(Messages.get(this, "rev"));
                    GameScene.flash(0xFFFF00);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST, 2, Random.Float(0.33f, 0.66f));
                    Camera.main.shake(31, 3f);
                    Music.INSTANCE.play(Assets.Music.HALLS_BOSS, true);

                    Madeinheaven n = new Madeinheaven();

                    pick.detach(Dungeon.hero.belongings.backpack);

                    n.levelKnown = levelKnown;
                    n.cursedKnown = cursedKnown;
                    n.cursed = cursed;

                    n.level(lvl);

                    if (n.doPickUp(Dungeon.hero)) {
                        GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", n.name())));
                    } else {
                        Dungeon.level.drop(n, Dungeon.hero.pos).sprite.drop();
                    }
                }
                Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT), 12);
            } else {
                identify();
                if (hero.buff(ShovelDigCoolDown7.class) != null) {
                    GLog.w(Messages.get(this, "not_ready"));
                } else {
                    Ballistica aim;
                    int x = hero.pos % Dungeon.level.width();
                    int y = hero.pos / Dungeon.level.width();

                    if (Math.max(x, Dungeon.level.width() - x) >= Math.max(y, Dungeon.level.height() - y)) {
                        if (x > Dungeon.level.width() / 2) {
                            aim = new Ballistica(hero.pos, hero.pos - 1, Ballistica.WONT_STOP);
                        } else {
                            aim = new Ballistica(hero.pos, hero.pos + 1, Ballistica.WONT_STOP);
                        }
                    } else {
                        if (y > Dungeon.level.height() / 2) {
                            aim = new Ballistica(hero.pos, hero.pos - Dungeon.level.width(), Ballistica.WONT_STOP);
                        } else {
                            aim = new Ballistica(hero.pos, hero.pos + Dungeon.level.width(), Ballistica.WONT_STOP);
                        }
                    }

                    Class<? extends Wand> wandCls = null;
                    if (hero.belongings.getItem(MagesStaff.class) != null) {
                        wandCls = WandOfBlastWave.class;
                    }

                    int aoeSize = 8;

                    int projectileProps = Ballistica.STOP_SOLID | Ballistica.STOP_TARGET;

                    ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);

                    for (Ballistica ray : aoe.outerRays) {
                        ((MagicMissile) hero.sprite.parent.recycle(MagicMissile.class)).reset(
                                MagicMissile.FORCE_CONE,
                                hero.sprite,
                                ray.path.get(ray.dist),
                                null
                        );
                    }

                    final float effectMulti = 1f;
                    Buff.affect(hero, ShovelDigCoolDown7.class, ShovelDigCoolDown7.DURATION);

                    Class<? extends Wand> finalWandCls = wandCls;
                    ((MagicMissile) hero.sprite.parent.recycle(MagicMissile.class)).reset(
                            MagicMissile.FORCE_CONE,
                            hero.sprite,
                            aim.path.get(Math.min(aoeSize / 2, aim.path.size() - 1)),
                            new Callback() {
                                @Override
                                public void call() {

                                    int charsHit = 0;
                                    for (int cell : aoe.cells) {

                                        //### Deal damage ###
                                        Char mob = Actor.findChar(cell);
                                        int damage = Math.round(Hero.heroDamageIntRange(15, 25));

                                        if (mob != null && damage > 0 && mob.alignment != Char.Alignment.ALLY) {
                                            if (Char.hasProp(mob, Char.Property.BOSS) || Char.hasProp(mob, Char.Property.MINIBOSS)) {
                                                mob.damage(20 / 2, hero);
                                            } else {
                                                mob.damage(mob.HP / 2, hero);
                                            }

                                            charsHit++;
                                        }

                                        if (mob != null && mob != hero) {
                                            if (mob.alignment != Char.Alignment.ALLY) {
                                                Ballistica aim = new Ballistica(hero.pos, mob.pos, Ballistica.WONT_STOP);
                                                int knockback = aoeSize + 1 - (int) Dungeon.level.trueDistance(hero.pos, mob.pos);
                                                knockback *= 3;
                                                WandOfBlastWave.throwChar(mob,
                                                        new Ballistica(mob.pos, aim.collisionPos, Ballistica.MAGIC_BOLT),
                                                        knockback,
                                                        true,
                                                        true,
                                                        hero);
                                            }
                                        }
                                    }

                                    hero.spendAndNext(Actor.TICK);
                                }
                            }
                    );

                    hero.sprite.operate(hero.pos);
                    Invisibility.dispel();
                    hero.busy();

                    Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
                }
            }
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Greenbaby.class, Awakensnake.class};
            inQuantity = new int[]{1, 1};

            cost = 30;

            output = Sleepcmoon.class;
            outQuantity = 1;
        }
    }

}