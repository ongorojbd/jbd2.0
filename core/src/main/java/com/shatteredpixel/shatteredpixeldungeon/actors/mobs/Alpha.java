package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpiderMindSprite;
import com.watabou.utils.Random;

/**
 * The plain rock insect of HospitalLevel: no tricks, just the statline the swarm is built on.
 * Individually far weaker than a Labs mob, but they come thirty at a time.
 */
public class Alpha extends Mob {

    {
        spriteClass = SpiderMindSprite.Alpha.class;

        HP = HT = 50;
        defenseSkill = 20;

        EXP = 6;
        maxLvl = 30;

        //a single scroll shouldn't wipe a whole section of the swarm
        immunities.add(ScrollOfRetribution.class);
        immunities.add(ScrollOfPsionicBlast.class);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        damage += enemy.HT / 12;
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(28, 42);
    }

    @Override
    public int attackSkill(Char target) {
        return 48;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 5);
    }

}
