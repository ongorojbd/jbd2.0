package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.HospitalLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DoobieSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Random;

/**
 * Doobie Wah!, HospitalLevel's boss. Spawns behind the ambulance once the last section is done
 * and chases the hero down the corridor the ambulance has bored; the deck turrets and the hero
 * wear it down together, and the level clears when it falls. It locks onto the hero's breath,
 * so it always knows where they are - it never loses track and never switches targets.
 */
public class DoobieWah extends Mob {

    {
        spriteClass = DoobieSprite.class;

        HP = HT = 1500;
        defenseSkill = 20;

        EXP = 100;
        maxLvl = 30;

        flying = true;
        viewDistance = 12;

        properties.add(Property.BOSS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(30, 50);
    }

    @Override
    public int attackSkill(Char target) {
        return 45;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(5, 10);
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        //locked onto the hero: re-aimed every turn, so it heads for them even out of sight and
        //never wanders off or turns on the patient instead
        if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
            aggro(Dungeon.hero);
            target = Dungeon.hero.pos;
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        if (Dungeon.level instanceof HospitalLevel) {
            ((HospitalLevel) Dungeon.level).onBossDefeated();
        }
    }
}
