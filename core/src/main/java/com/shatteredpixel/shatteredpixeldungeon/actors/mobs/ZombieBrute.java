package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Zombie.spwPrize;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieBruteSprite;
import com.shatteredpixel.shatteredpixeldungeon.levels.TendencyLevel;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ZombieBrute extends Mob {
    {
        spriteClass = ZombieBruteSprite.class;

        HP = HT = 16;
        defenseSkill = 5;
        baseSpeed = 1.3f;

        EXP = 4;
        maxLvl = 9;
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    private int attackpower = 0;
    private boolean powerdown = false;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1 + attackpower, 4 + attackpower);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        attackpower += 1;
        return super.attackProc(enemy, damage);
    }

    @Override
    public boolean act() {
        if (this.HP < this.HT && buff(rage.class) == null) {
            Buff.affect(this, rage.class);
        } else if (buff(rage.class) != null) {
            if (HP <= 16 && HP > 1) damage(1, this);
            else powerdown = true;
        }
        return super.act();
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        Buff.detach(this, rage.class);

        if (Random.Int( 3 ) == 0) {
            Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BONES, Random.Float(1.2f, 0.9f));
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }

    private static final String POWER = "attackpower";
    private static final String PDOWN = "powerdown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POWER, attackpower);
        bundle.put(PDOWN, powerdown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        attackpower = bundle.getInt(POWER);
        powerdown = bundle.getBoolean(PDOWN);

    }

    public static class rage extends Buff {

        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.STARVATION;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }
    }
}
