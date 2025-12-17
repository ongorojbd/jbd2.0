/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class D4C extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 1_000_000;
    
    private float left = DURATION;

    @Override
    public void fx(boolean on) {
        if (on) {
            // Statistics.d4cEnhanced가 true면 강화된 아우라
            if (Statistics.d4cEnhanced) {
                target.sprite.aura( 0xFF0099, 6 ); // 더 강력한 보라색 아우라, 더 큰 크기
            } else {
                target.sprite.aura( 0xFF9900, 4 ); // 기본 주황색 아우라
            }
        } else {
            target.sprite.clearAura();
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.RAGE;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    
    public void oneTurn() {
        left = 1f;
    }
    
    @Override
    public boolean act() {
        if (left < DURATION) {
            // 시간 제한이 있는 경우 (1턴 버프)
            left -= 1f;
            if (left <= 0) {
                detach();
                return true;
            }
        }
        return super.act();
    }
    
    @Override
    public String desc() {
        String desc;
        if (left < DURATION) {
            desc = Messages.get(this, "desc", dispTurns(Math.max(0, left)));
        } else {
            desc = Messages.get(this, "desc");
        }
        
        // Statistics.d4cEnhanced가 true면 강화 효과 설명 추가
        if (Statistics.d4cEnhanced) {
            desc += "\n\n" + Messages.get(this, "enhanced_desc");
        }
        
        return desc;
    }
    
    // 피격 시 상태이상을 적에게 떠넘기는 메서드
    public void transferDebuffsToAttacker(Char attacker) {
        if (!Statistics.d4cEnhanced) {
            return; // 강화되지 않았으면 아무것도 하지 않음
        }
        
        if (attacker == null || attacker == target || !attacker.isAlive()) {
            return;
        }
        
        boolean transferred = false;
        
        // 중독
        Poison poison = target.buff(Poison.class);
        if (poison != null) {
            Buff.affect(attacker, Poison.class).set(poison.left);
            poison.detach();
            transferred = true;
        }
        
        // 불구
        Cripple cripple = target.buff(Cripple.class);
        if (cripple != null) {
            Buff.affect(attacker, Cripple.class, cripple.cooldown());
            cripple.detach();
            transferred = true;
        }
        
        // 공격력 저하
        Weakness weakness = target.buff(Weakness.class);
        if (weakness != null) {
            Buff.affect(attacker, Weakness.class, weakness.cooldown());
            weakness.detach();
            transferred = true;
        }
        
        // 방어력 저하
        Vulnerable vulnerable = target.buff(Vulnerable.class);
        if (vulnerable != null) {
            Buff.affect(attacker, Vulnerable.class, vulnerable.cooldown());
            vulnerable.detach();
            transferred = true;
        }
        
        // 무력화
        Amok amok = target.buff(Amok.class);
        if (amok != null) {
            Buff.affect(attacker, Amok.class, amok.cooldown());
            amok.detach();
            transferred = true;
        }
        
        // 출혈
        Bleeding bleeding = target.buff(Bleeding.class);
        if (bleeding != null) {
            Buff.affect(attacker, Bleeding.class).set((int)bleeding.level());
            bleeding.detach();
            transferred = true;
        }
        
        // 현기증
        Vertigo vertigo = target.buff(Vertigo.class);
        if (vertigo != null) {
            Buff.affect(attacker, Vertigo.class, vertigo.cooldown());
            vertigo.detach();
            transferred = true;
        }
        
        // 발화
        Burning burning = target.buff(Burning.class);
        if (burning != null) {
            Buff.affect(attacker, Burning.class).reignite(attacker, burning.cooldown());
            burning.detach();
            transferred = true;
        }
        
        // 한기
        Chill chill = target.buff(Chill.class);
        if (chill != null) {
            Buff.affect(attacker, Chill.class, chill.cooldown());
            chill.detach();
            transferred = true;
        }
        
        // 산성
        Ooze ooze = target.buff(Ooze.class);
        if (ooze != null) {
            Buff.affect(attacker, Ooze.class).set(Ooze.DURATION);
            ooze.detach();
            transferred = true;
        }
        
        // 속박
        Roots roots = target.buff(Roots.class);
        if (roots != null) {
            Buff.affect(attacker, Roots.class, roots.cooldown());
            roots.detach();
            transferred = true;
        }
        
        // 실명
        Blindness blind = target.buff(Blindness.class);
        if (blind != null) {
            Buff.affect(attacker, Blindness.class, blind.cooldown());
            blind.detach();
            transferred = true;
        }
        
        if (transferred) {
            GLog.p(Messages.get(this, "transferred"));
            attacker.sprite.showStatus(0xFF0099, Messages.get(this, "debuff_icon"));
        }
    }
    
    private static final String LEFT = "left";
    
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }
    
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(LEFT)) {
            left = bundle.getFloat(LEFT);
        } else {
            left = DURATION;
        }
    }

}
