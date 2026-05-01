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
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Dioprize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UltimateFragment;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.So2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class So2 extends NPC {

    {
        spriteClass = So2Sprite.class;

        properties.add(Property.IMMOVABLE);
    }

    private boolean maskBought  = false;
    private boolean arakiBought = false;

    private static final String MASK_BOUGHT  = "mask_bought";
    private static final String ARAKI_BOUGHT = "araki_bought";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MASK_BOUGHT,  maskBought);
        bundle.put(ARAKI_BOUGHT, arakiBought);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        maskBought  = bundle.getBoolean(MASK_BOUGHT);
        arakiBought = bundle.getBoolean(ARAKI_BOUGHT);
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
        Sample.INSTANCE.play(Assets.Sounds.SO2);

        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                String maskOption  = maskBought
                        ? Messages.get(So2.class, "sold_mask")
                        : Messages.get(So2.class, "buy_mask");
                String arakiOption = arakiBought
                        ? Messages.get(So2.class, "sold_araki")
                        : Messages.get(So2.class, "buy_araki");

                GameScene.show(new WndOptions(
                        sprite(),
                        Messages.titleCase(name()),
                        Messages.get(So2.class, "shop", SPDSettings.getToken()),
                        maskOption,
                        arakiOption,
                        Messages.get(So2.class, "close")
                ) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            buyMask();
                        } else if (index == 1) {
                            buyAraki();
                        }
                    }
                });
            }
        });

        return true;
    }

    private void buyMask() {
        if (maskBought) {
            GLog.w(Messages.get(So2.class, "already_bought"));
            return;
        }
        if (SPDSettings.getToken() < 1) {
            GLog.w(Messages.get(So2.class, "nc"));
            return;
        }
        SPDSettings.addToken(-1);
        maskBought = true;
        Item item = new UltimateFragment();
        GLog.p(Messages.get(So2.class, "purchased"));
        if (item.doPickUp(Dungeon.hero)) {
            GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", item.name())));
        } else {
            Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop();
        }
        Sample.INSTANCE.play(Assets.Sounds.BADGE);
    }

    private void buyAraki() {
        if (arakiBought) {
            GLog.w(Messages.get(So2.class, "already_bought"));
            return;
        }
        if (SPDSettings.getToken() < 1) {
            GLog.w(Messages.get(So2.class, "nc"));
            return;
        }
        SPDSettings.addToken(-1);
        arakiBought = true;
        Item item = new Dioprize();
        GLog.p(Messages.get(So2.class, "purchased"));
        if (item.doPickUp(Dungeon.hero)) {
            GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", item.name())));
        } else {
            Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop();
        }
        Sample.INSTANCE.play(Assets.Sounds.BADGE);
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
