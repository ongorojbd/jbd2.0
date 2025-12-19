/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class TacticalScope extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;
        announced = false;
        revivePersists = true; // 죽어도 유지
    }

    private int charges = 0;
    private static final int MAX_CHARGES = 1;
    private float partialCharge = 0f;
    private static final float CHARGE_TIME = 200f; // 200턴마다 충전
    private boolean usedButNotFired = false; // 사용했지만 아직 발사하지 않음

    // 충전된 상태로 시작
    public void set() {
        charges = MAX_CHARGES;
        partialCharge = 0f;
        usedButNotFired = false;
        ActionIndicator.setAction(this);
        BuffIndicator.refreshHero();
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public boolean act() {
        // 사용했지만 아직 발사하지 않았으면 쿨타임 시작하지 않음
        if (charges < MAX_CHARGES && !usedButNotFired) {
            partialCharge += 1f;
            
            // J37 탤런트에 따라 충전 시간 조정
            float chargeTime = CHARGE_TIME;
            Hero hero = Dungeon.hero;
            if (hero != null && hero.hasTalent(Talent.J37)) {
                int talentLevel = hero.pointsInTalent(Talent.J37);
                chargeTime = CHARGE_TIME - (talentLevel * 50f); // 1레벨: 150턴, 2레벨: 100턴, 3레벨: 50턴
            }
            
            if (partialCharge >= chargeTime) {
                charges++;
                partialCharge = 0f;
                GLog.p(Messages.get(this, "charged"));
                ActionIndicator.setAction(this);
                BuffIndicator.refreshHero();
            }
        }
        
        if (charges > 0) {
            ActionIndicator.refresh();
        }
        
        spend(Actor.TICK);
        return true;
    }

    public void useCharge() {
        if (charges > 0) {
            Sample.INSTANCE.play(Assets.Sounds.G1);

            // charges는 줄이지 않고, 플래그만 설정
            usedButNotFired = true;
            // 조준 속도 감소 버프 부여
            Buff.affect(target, ScopeActive.class);
            GLog.p(Messages.get(this, "activated"));
            ActionIndicator.clearAction(this);
            BuffIndicator.refreshHero();
        }
    }

    // 발사 완료 후 쿨타임 시작
    public void resetCharge() {
        if (usedButNotFired) {
            charges--;
            usedButNotFired = false;
            partialCharge = 0f;
            BuffIndicator.refreshHero();
        }
    }

    public int getCharges() {
        return charges;
    }

    public boolean hasCharge() {
        return charges > 0 && !usedButNotFired;
    }

    @Override
    public int icon() {
        // 충전 중일 때만 BuffIndicator 표시
        if (charges == 0 && !usedButNotFired) {
            return BuffIndicator.TIME;
        }
        // 충전 완료 또는 사용 후 발사 전일 때는 숨김
        return BuffIndicator.NONE;
    }

    @Override
    public void tintIcon(Image icon) {
        if (charges > 0 && !usedButNotFired) {
            icon.hardlight(0, 1f, 0);
        } else {
            icon.hardlight(0.0f, 0.6f, 0.0f);
        }
    }

    @Override
    public float iconFadePercent() {
        if (charges >= MAX_CHARGES) {
            return 0f;
        }
        
        // J37 탤런트에 따라 충전 시간 조정
        float chargeTime = CHARGE_TIME;
        Hero hero = Dungeon.hero;
        if (hero != null && hero.hasTalent(Talent.J37)) {
            int talentLevel = hero.pointsInTalent(Talent.J37);
            chargeTime = CHARGE_TIME - (talentLevel * 50f);
        }
        
        return Math.max(0, (chargeTime - partialCharge) / chargeTime);
    }

    @Override
    public String iconTextDisplay() {
        // 충전 중일 때만 남은 턴 표시
        if (charges == 0 && !usedButNotFired) {
            // J37 탤런트에 따라 충전 시간 조정
            float chargeTime = CHARGE_TIME;
            Hero hero = Dungeon.hero;
            if (hero != null && hero.hasTalent(Talent.J37)) {
                int talentLevel = hero.pointsInTalent(Talent.J37);
                chargeTime = CHARGE_TIME - (talentLevel * 50f);
            }
            return Integer.toString((int)(chargeTime - partialCharge));
        }
        return "";
    }

    @Override
    public String desc() {
        if (usedButNotFired) {
            // 사용했지만 발사 전
            return Messages.get(this, "desc_active");
        } else if (charges > 0) {
            // 충전 완료
            return Messages.get(this, "desc_ready", charges);
        } else {
            // 충전 중
            // J37 탤런트에 따라 충전 시간 조정
            float chargeTime = CHARGE_TIME;
            Hero hero = Dungeon.hero;
            if (hero != null && hero.hasTalent(Talent.J37)) {
                int talentLevel = hero.pointsInTalent(Talent.J37);
                chargeTime = CHARGE_TIME - (talentLevel * 50f);
            }
            return Messages.get(this, "desc_charging", (int)(chargeTime - partialCharge));
        }
    }

    private static final String CHARGES = "charges";
    private static final String PARTIAL_CHARGE = "partialCharge";
    private static final String USED_BUT_NOT_FIRED = "usedButNotFired";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGES, charges);
        bundle.put(PARTIAL_CHARGE, partialCharge);
        bundle.put(USED_BUT_NOT_FIRED, usedButNotFired);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charges = bundle.getInt(CHARGES);
        partialCharge = bundle.getFloat(PARTIAL_CHARGE);
        usedButNotFired = bundle.getBoolean(USED_BUT_NOT_FIRED);
        
        // 충전이 완료되어 있고 아직 사용하지 않았으면 ActionIndicator 활성화
        if (charges > 0 && !usedButNotFired) {
            ActionIndicator.setAction(this);
        }
    }

    // ActionIndicator.Action 인터페이스 구현
    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.STANDO_ABILITIES;
    }

    @Override
    public int indicatorColor() {
        return 0x009900;
    }

    @Override
    public void doAction() {
        if (charges > 0 && !usedButNotFired) {
            useCharge();
        } else {
            GLog.w(Messages.get(this, "no_charge"));
        }
    }

    // 조준 속도 감소 효과 버프 (다음 발사까지 유지)
    public static class ScopeActive extends Buff {
        {
            type = buffType.POSITIVE;
            announced = true;
            revivePersists = true; // 죽어도 유지
        }

        @Override
        public int icon() {
            return BuffIndicator.TRINITY_FORM;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.0f, 0.6f, 0.0f);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }
    }
}

