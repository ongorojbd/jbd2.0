package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.levels.VeiledSanctumLevel;

//simple 4-digit password prompt shown when the hero tries to descend from VeiledSanctumLevel
//before entering the correct code found from its 4 hint chests.
public class WndSanctumCode extends WndTextInput {

    private final VeiledSanctumLevel level;

    public WndSanctumCode(VeiledSanctumLevel level) {
        super("TG 대학병원 엘리베이터",
                "아래로 내려가려면 4자리 비밀번호가 필요하다.\n방 안의 상자들에 흩어진 힌트를 모아보자.",
                "", 4, false, "확인", "취소");
        this.level = level;
    }

    @Override
    public void onSelect(boolean positive, String text) {
        if (positive) {
            level.tryCode(text.trim());
        }
    }
}
