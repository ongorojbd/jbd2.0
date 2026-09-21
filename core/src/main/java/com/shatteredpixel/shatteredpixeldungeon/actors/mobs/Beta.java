package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpiderMindSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/**
 * A rock insect that burrows, ported from Devoted Pixel Dungeon's Mole. While hunting and not
 * yet next to its target it digs underground: twice as fast, and Actor.findChar() skips it, so
 * nothing can hit or target it and other characters can walk over it. Once adjacent it bursts
 * out, shoving whoever is standing on top of it aside and slowing everything around it.
 * Above ground it is half speed.
 */
public class Beta extends Mob {

    {
        spriteClass = SpiderMindSprite.Beta.class;

        HP = HT = 12;
        defenseSkill = 4;

        EXP = 2;
        maxLvl = 8;

        HUNTING = new Hunting();
    }

    public boolean digging = false;

    private static final String DIGGING = "digging";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DIGGING, digging);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        digging = bundle.getBoolean(DIGGING);
        super.restoreFromBundle(bundle);
    }

    @Override
    public float speed() {
        return super.speed() * (digging ? 2f : 0.5f);
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

    @Override
    protected boolean act() {
        //lost its target while underground - come back up rather than stay buried
        if (state != HUNTING && digging) {
            emerge();
            return true;
        }
        return super.act();
    }

    private void submerge() {
        digging = true;
        if (sprite instanceof SpiderMindSprite.Beta) ((SpiderMindSprite.Beta) sprite).setSubmerge();
        Sample.INSTANCE.play(Assets.Sounds.DIG);
        spend(1f);
    }

    private void emerge() {
        //while digging, findChar() skips this Beta, so this finds whoever walked on top of it
        Char ch = Actor.findChar(pos);

        if (ch != null) {
            int pushPos = pos;
            for (int c : PathFinder.NEIGHBOURS8) {
                if (Actor.findChar(pos + c) == null
                        && Dungeon.level.passable[pos + c]
                        && (Dungeon.level.openSpace[pos + c] || !Char.hasProp(ch, Property.LARGE))
                        && Dungeon.level.trueDistance(pos, pos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
                    pushPos = pos + c;
                }
            }

            //push whoever is on top aside, or share the cell for a turn if there is nowhere to go
            if (pushPos != pos) {
                Actor.add(new Pushing(ch, ch.pos, pushPos));
                ch.pos = pushPos;
                Dungeon.level.occupyCell(ch);
            }
        }

        digging = false;
        if (sprite instanceof SpiderMindSprite.Beta) ((SpiderMindSprite.Beta) sprite).setEmerge();
        Sample.INSTANCE.play(Assets.Sounds.DIG);
        spend(1f);
    }

    private void burst() {
        for (int n : PathFinder.NEIGHBOURS8) {
            CellEmitter.get(pos + n).burst(SmokeParticle.FACTORY, 5);
            Char ch = Actor.findChar(pos + n);
            if (ch != null && ch.isAlive()) {
                Buff.affect(ch, Slow.class, 2f);
            }
        }
    }

    private class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            if (!digging && enemyInFOV && Dungeon.level.distance(enemy.pos, pos) > 1) {
                submerge();
                return true;

            } else if (digging && enemyInFOV && Dungeon.level.distance(enemy.pos, pos) < 2) {
                emerge();
                burst();
                return true;
            }

            return super.act(enemyInFOV, justAlerted);
        }
    }
}
