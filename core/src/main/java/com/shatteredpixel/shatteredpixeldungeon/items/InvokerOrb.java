/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InvokerEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.U3;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

/**
 * 원소술사(Invoker)의 원소 오브 베이스 클래스.
 * Q/W/E 사용은 턴 소모 없이 즉발로 원소를 충전하며,
 * 3번째 충전 시 에너지를 소모하고 스킬을 발동한다.
 */
public abstract class InvokerOrb extends Item {

    public static final String AC_CHARGE = "CHARGE";

    {
        stackable = false;
        unique    = true;
        bones     = false;
        defaultAction = AC_CHARGE;
    }

    protected abstract InvokerEnergy.Element element();

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_CHARGE);
        return actions;
    }

    @Override
    public String defaultAction() {
        return AC_CHARGE;
    }

    public static class DarkBolt {}

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CHARGE)) {
            if (InvokerEnergy.energy == 0) {
                GLog.w(Messages.get(InvokerEnergy.class, "not_enough_energy"));
            } else {
                InvokerEnergy buff = Buff.affect(hero, InvokerEnergy.class);

                Sample.INSTANCE.play(Assets.Sounds.BURNING);

                hero.damage( 1, new DarkBolt());
                if (!hero.isAlive()) {
                    Badges.validateDeathFromFriendlyMagic();
                    Dungeon.fail( this );
                }

                if (buff.totalCharges() >= 3) {
                    // 이미 3개 충전됨 - 이 경로는 addCharge 내부에서도 막히지만 방어 처리
                    GLog.w(Messages.get(InvokerOrb.class, "full_charges"));
                    hero.next();
                    return;
                }

                int chargesBefore = buff.totalCharges();
                boolean spellFired = buff.addCharge(element());

                if (!spellFired) {
                    // 충전이 실제로 추가된 경우에만 피드백 표시
                    if (buff.totalCharges() > chargesBefore) {
                        GLog.h(name());
                    }
                    hero.next();
                }
                // 스킬 발동 시: castSpell 내부에서 hero.spendAndNext 혹은 hero.busy()를 처리함
            }
        }
    }

    @Override
    public boolean isUpgradable() { return false; }

    @Override
    public boolean isIdentified() { return true; }

    // ─────────────────────────────────────────────
    //  Q - 퀘이스 오브 (냉기)
    // ─────────────────────────────────────────────
    public static class QuasOrb extends InvokerOrb {
        {
            image = ItemSpriteSheet.H1;
        }

        @Override
        protected InvokerEnergy.Element element() { return InvokerEnergy.Element.QUAS; }
    }

    // ─────────────────────────────────────────────
    //  W - 웩스 오브 (번개)
    // ─────────────────────────────────────────────
    public static class WexOrb extends InvokerOrb {
        {
            image = ItemSpriteSheet.H2;
        }

        @Override
        protected InvokerEnergy.Element element() { return InvokerEnergy.Element.WEX; }
    }

    // ─────────────────────────────────────────────
    //  E - 엑소트 오브 (화염)
    // ─────────────────────────────────────────────
    public static class ExortOrb extends InvokerOrb {
        {
            image = ItemSpriteSheet.H3;
        }

        @Override
        protected InvokerEnergy.Element element() { return InvokerEnergy.Element.EXORT; }
    }
}
