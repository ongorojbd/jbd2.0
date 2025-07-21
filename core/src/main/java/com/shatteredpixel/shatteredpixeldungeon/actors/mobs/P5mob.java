package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Passione3Sprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class P5mob extends Mob {

    private int level = Dungeon.depth;

    {
        spriteClass = Passione3Sprite.Fu.class;
        HP = HT = (1 + level) * 6;
        EXP = 0;

        state = WANDERING;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
    }

    private int summonCooldown = 1;
    private static final String LEVEL = "level";
    private static final String ALIGMNENT = "alignment";
    private static final String SUMMON_COOLDOWN = "summoncooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(ALIGMNENT, alignment);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        if (bundle.contains(ALIGMNENT)) alignment = bundle.getEnum(ALIGMNENT, Alignment.class);
        summonCooldown = bundle.getInt(SUMMON_COOLDOWN);
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]) {
            Bestiary.setSeen(getClass());
        }

        if (summonCooldown > 0) {
            summonCooldown--;
        }

        if (paralysed <= 0 && state == HUNTING && enemy != null && enemySeen
                && !Dungeon.level.adjacent(pos, enemy.pos)
                && fieldOfView[enemy.pos]
                && summonCooldown <= 0) {

            enemySeen = enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap(enemy.pos);
                return false;
            } else {
                zap();
                return true;
            }
        }

        return super.act();
    }

    @Override
    public int attackSkill(Char target) {
        if (target != null && alignment == Alignment.NEUTRAL && target.invisible <= 0) {
            return INFINITE_ACCURACY;
        } else {
            return 8 + level;
        }
    }

    @Override
    public int damageRoll() {
        if (alignment == Alignment.NEUTRAL) {
            return Random.NormalIntRange(2 + 2 * level, 2 + 2 * level);
        } else {
            return Random.NormalIntRange(1 + level, 2 + 2 * level);
        }
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1 + level / 2);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
        }

        return damage;
    }

    private void zap() {
        spend(TICK);
        summonCooldown = 11;
        this.sprite.showStatus(CharSprite.POSITIVE, "[퍼플 헤이즈!]");
        GameScene.add(Blob.seed(enemy.pos, 15, CorrosiveGas.class).setStrength(1 + Dungeon.depth / 4));
        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[enemy.pos + i]) {
                GameScene.add(Blob.seed(enemy.pos + i, 5, CorrosiveGas.class));
            }
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void die(Object cause) {
        sprite.killAndErase();
        yell(Messages.get(Fugomob.class, "0"));

        Fugomob2 Fugomob2 = new Fugomob2();
        Fugomob2.state = Fugomob2.HUNTING;
        Fugomob2.pos = this.pos;
        GameScene.add(Fugomob2);
        super.die(cause);
    }

    @Override
    protected Char chooseEnemy() {
        return super.chooseEnemy();
    }

    @Override
    protected boolean getCloser(int target) {
        target = Dungeon.hero.pos;
        return super.getCloser(target);
    }
}
