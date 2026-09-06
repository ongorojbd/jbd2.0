package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

//dropped in 4 of VeiledSanctumLevel's 9 rooms; each fragment reveals one digit of that
//run's 4-digit sanctum code at its correct position (the other 3 positions stay unknown
//until their own fragment is found).
public class SanctumCodeFragment extends Item {

    private static final String POSITION = "sanctum_position";
    private static final String DIGIT = "sanctum_digit";

    private int position; //0-3
    private int digit;    //0-9

    {
        image = ItemSpriteSheet.SEWER_PAGE;
        identify();
        stackable = false;
    }

    public SanctumCodeFragment() {
        this(0, 0);
    }

    public SanctumCodeFragment(int position, int digit) {
        super();
        this.position = position;
        this.digit = digit;
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        boolean result = super.doPickUp(hero, pos);
        if (result) {
            GLog.p("암호 파일을 발견했다: 비밀번호 " + (position + 1) + "번째 자리는 '" + digit + "'.");
        }
        return result;
    }

    @Override
    public String name() {
        return "암호 파일";
    }

    @Override
    public String desc() {
        return "엘리베이터를 열기 위한 4자리 암호 중 " + (position + 1) + "번째 자리 숫자가 적혀 있다.\n\n\"" + digit + "\"";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POSITION, position);
        bundle.put(DIGIT, digit);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        position = bundle.getInt(POSITION);
        digit = bundle.getInt(DIGIT);
    }
}
