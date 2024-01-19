package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BcopterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Bcopter extends Elemental.FireElemental {

    {
        spriteClass = BcopterSprite.class;

        HP = HT = 90;
        defenseSkill = 15;

        flying = false;

        loot = new StoneOfAggression();
        lootChance = 1/15f;

        EXP = 10;
        maxLvl = 21;

        properties.remove( Property.FIERY );
        harmfulBuffs.remove( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost.class );
        harmfulBuffs.remove( Chill.class );
    }

    private int targetingPos = -1;

    @Override
    protected boolean act() {
        if (targetingPos != -1){
            if (sprite != null && (sprite.visible || Dungeon.level.heroFOV[targetingPos])) {
                sprite.zap( targetingPos );
                return false;
            } else {
                zap();
                return true;
            }
        } else {
            return super.act();
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (super.canAttack(enemy)){
            return true;
        } else {
            return rangedCooldown < 0 && new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET ).collisionPos == enemy.pos;
        }
    }

    protected boolean doAttack( Char enemy ) {

        if (rangedCooldown > 0) {

            return super.doAttack( enemy );

        } else if (new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET ).collisionPos == enemy.pos) {

            //set up an attack for next turn
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8){
                int target = enemy.pos + i;
                if (target != pos && new Ballistica(pos, target, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == target){
                    candidates.add(target);
                }
            }

            if (!candidates.isEmpty()){
                targetingPos = Random.element(candidates);

                for (int i : PathFinder.NEIGHBOURS9){
                    if (!Dungeon.level.solid[targetingPos + i]) {
                        sprite.parent.addToBack(new TargetedCell(targetingPos + i, 0xFF00FF));
                    }
                }

                sprite.showStatus(CharSprite.WARNING, Messages.get(this, "a"));
                spend(GameMath.gate(TICK, (int)Math.ceil(Dungeon.hero.cooldown()), 3*TICK));
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
    public static class DarkBolt{}
    @Override
    protected void zap() {
        if (targetingPos != -1) {
            spend(1f);

            Invisibility.dispel(this);

            for (int i : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[targetingPos + i]) {
                    CellEmitter.center(targetingPos+i).burst(BlastParticle.FACTORY, 15);
                    CellEmitter.center(targetingPos+i).burst(SmokeParticle.FACTORY, 4);

                    Char target = Actor.findChar(targetingPos + i);
                    if (target != null && target.alignment != alignment) {
                        target.damage(Random.NormalIntRange(15, 20), new DarkBolt());
                        if (enemy == hero && !enemy.isAlive()) {
                            Dungeon.fail(getClass());
                        }
                    }
                }
            }
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        targetingPos = -1;
        rangedCooldown = 6;
    }

    @Override
    protected void meleeProc( Char enemy, int damage ) {

    }

    @Override
    protected void rangedProc( Char enemy ) {

    }

    @Override
    public int attackSkill( Char target ) {
        return 28;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 25, 30 );
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 12);
    }

    private static final String TARGETING_POS = "targeting_pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TARGETING_POS, targetingPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        targetingPos = bundle.getInt(TARGETING_POS);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

    }
}