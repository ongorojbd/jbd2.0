package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JadeWardenSprite;
import com.watabou.utils.Random;

/**
 * The rock insect that keeps its distance and spits at range - the same reach as an ambulance
 * turret, so the two trade shots on even terms. Fragile and hard to hit rather than sturdy.
 */
public class Gamma extends Mob {

    private static final int RANGE = 6;

    {
        spriteClass = JadeWardenSprite.class;

        HP = HT = 35;
        defenseSkill = 25;

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
        return Random.NormalIntRange(12, 25);
    }

    @Override
    public int attackSkill(Char target) {
        return 50;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 4);
    }

    private boolean canRangedAttack(Char enemy) {
        return Dungeon.level.distance(pos, enemy.pos) <= RANGE
                && new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return super.canAttack(enemy) || canRangedAttack(enemy);
    }

    @Override
    protected boolean doAttack(Char enemy) {
        if (Dungeon.level.adjacent(pos, enemy.pos) || !canRangedAttack(enemy)) {
            return super.doAttack(enemy);
        }

        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
            //the bolt's callback calls onZapComplete(), so the turn waits for it to land
            sprite.zap(enemy.pos);
            return false;
        } else {
            zap();
            return true;
        }
    }

    private void zap() {
        spend(attackDelay());

        Char enemy = this.enemy;
        if (enemy == null || !enemy.isAlive()) return;

        //the spit counts as magic, so armour glyphs can turn it aside - its own damage source
        //keeps that from covering the melee bite as well
        if (hit(this, enemy, true)) {
            int dmg = Random.NormalIntRange(15, 25);
            enemy.damage(dmg, new StoneBolt());

            if (!enemy.isAlive() && enemy == Dungeon.hero) {
                Dungeon.fail(this);
            }
        } else {
            enemy.sprite.showStatus(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.NEUTRAL,
                    enemy.defenseVerb());
        }
    }

    //damage source for the ranged spit, so AntiMagic and friends can resist it
    public static class StoneBolt {}

    public void onZapComplete() {
        zap();
        next();
    }

}
