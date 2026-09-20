package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpiderMindSprite;
import com.watabou.utils.Random;

public class Alpha extends Mob {

    {
        spriteClass = SpiderMindSprite.Alpha.class;

        HP = HT = 12;
        defenseSkill = 4;

        EXP = 2;
        maxLvl = 8;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 6);
    }

    @Override
    public int attackSkill(Char target) {
        return 10;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

}
