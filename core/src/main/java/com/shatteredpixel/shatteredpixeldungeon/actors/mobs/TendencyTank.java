package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TendencyTankSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TendencyTank extends Mob {

    private int level = Dungeon.depth / 2;

    {
        spriteClass = TendencyTankSprite.class;

        HP = HT = (2 + level) * 15;
        defenseSkill = 0;
        EXP = 0;

        viewDistance = 6;
        alignment = Alignment.ALLY;
        intelligentAlly = true;

        properties.add(Property.INORGANIC);
    }

    private int targetingPos = -1;

    @Override
    protected boolean act() {
        if (state == HUNTING) {
            rangedCooldown--;
        }

        if (targetingPos != -1) {
            if (sprite != null && (sprite.visible || Dungeon.level.heroFOV[targetingPos])) {
                sprite.zap(targetingPos);
                return false;
            } else {
                zap();
                return true;
            }
        } else {
            return super.act();
        }
    }

    protected int rangedCooldown = 3;

    @Override
    protected boolean canAttack(Char enemy) {
        if (super.canAttack(enemy)) {
            return true;
        } else {
            return rangedCooldown < 0 && new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == enemy.pos;
        }
    }

    protected boolean doAttack(Char enemy) {

        if (rangedCooldown > 0) {

            return super.doAttack(enemy);

        } else if (new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == enemy.pos) {

            //set up an attack for next turn
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8) {
                int target = enemy.pos + i;
                if (target != pos && new Ballistica(pos, target, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == target) {
                    candidates.add(target);
                }
            }

            if (!candidates.isEmpty()) {
                targetingPos = Random.element(candidates);

                for (int i : PathFinder.NEIGHBOURS9) {
                    if (!Dungeon.level.solid[targetingPos + i]) {
                        sprite.parent.addToBack(new TargetedCell(targetingPos + i, 0xFF00FF));
                    }
                }

                sprite.showStatus(CharSprite.WARNING, Messages.get(TendencyTank.class, "a"));
                spend(GameMath.gate(TICK, (int) Math.ceil(Dungeon.hero.cooldown()), 3 * TICK));
                Dungeon.hero.interrupt();
                return true;
            } else {
                rangedCooldown = 1;
                return super.doAttack(enemy);
            }

        } else {
            rangedCooldown = 1;
            return super.doAttack(enemy);
        }
    }

    public static class DarkBolt {
    }

    protected void zap() {
        if (targetingPos != -1) {

            Invisibility.dispel(this);

            for (int i : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[targetingPos + i]) {
                    sprite.parent.add(new Beam.SPWRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(targetingPos + i)));
                    CellEmitter.center(targetingPos + i).burst(SmokeParticle.FACTORY, 4);
                    Char target = Actor.findChar(targetingPos + i);
                    if (target != null && target.alignment != alignment) {
                        int dmgboss = Random.NormalIntRange(enemy.HT / 6, enemy.HT / 5);
                        int dmg = Random.NormalIntRange(enemy.HT / 2, enemy.HT);

                        if (target.properties().contains(Char.Property.BOSS)) target.damage(dmgboss, new DarkBolt());
                        else target.damage(dmg, new DarkBolt());
                        Buff.affect(target, Paralysis.class, 2f);
                        Buff.affect(target, Burning.class).reignite(enemy, 2f);
                    }
                }
            }
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        targetingPos = -1;
        rangedCooldown = 20;
    }

    @Override
    public int attackSkill(Char target) {
        if (target != null && alignment == Alignment.NEUTRAL && target.invisible <= 0) {
            return INFINITE_ACCURACY;
        } else {
            return (int) (8 + level * 1.5);
        }
    }

    @Override
    public int damageRoll() {
        if (alignment == Alignment.NEUTRAL) {
            return Random.NormalIntRange(2 + 2 * level, 2 + 2 * level);
        } else {
            return Random.NormalIntRange(level, 2 * level);
        }
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(2, 2 + level / 2);
    }

    @Override
    protected boolean getCloser(int target) {

        target = Dungeon.hero.pos;

        return super.getCloser(target);
    }

    public void onZapComplete() {
        zap();
        next();
    }

    private static final String COOLDOWN = "cooldown";
    private static final String TARGETING_POS = "targeting_pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN, rangedCooldown);
        bundle.put(TARGETING_POS, targetingPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(COOLDOWN)) {
            rangedCooldown = bundle.getInt(COOLDOWN);
        }
        targetingPos = bundle.getInt(TARGETING_POS);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

    }
}