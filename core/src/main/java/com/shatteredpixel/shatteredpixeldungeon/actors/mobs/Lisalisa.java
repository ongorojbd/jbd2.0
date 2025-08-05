/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LisaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Passione2Sprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

//FIXME the AI for these things is becoming a complete mess, should refactor
public class Lisalisa extends Mob {

    private int level = Dungeon.depth;

    {
        spriteClass = LisaSprite.class;
        HP = HT = (1 + level) * 6;
        EXP = 0;

        state = WANDERING;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
    }

    private static final String LEVEL = "level";
    private static final String ALIGMNENT = "alignment";

    private int starpower = 0;
    private int HealCount = 0;

    @Override
    protected boolean canAttack(Char enemy) {
        if (Dungeon.level.adjacent(pos, enemy.pos)) {
            return true;
        }

        if (Dungeon.level.distance(pos, enemy.pos) <= 3) {
            boolean[] passable = BArray.not(Dungeon.level.solid, null);

            for (Char ch : Actor.chars()) {
                passable[ch.pos] = ch == this;
            }

            PathFinder.buildDistanceMap(enemy.pos, passable, 3);

            if (PathFinder.distance[pos] <= 3) {
                return true;
            }
        }

        return super.canAttack(enemy);
    }
    private static final String POWER = "starpower";
    private static final String HEALCOUNT = "healCount";
    private int starpowercap = 6;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(ALIGMNENT, alignment);
        bundle.put(POWER, starpower);
        bundle.put(HEALCOUNT, HealCount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        if (bundle.contains(ALIGMNENT)) alignment = bundle.getEnum(ALIGMNENT, Alignment.class);
        if (starpowercap > 0) starpower = Math.min(starpowercap, bundle.getInt(POWER));
        else starpower = bundle.getInt(POWER);
        HealCount = bundle.getInt(HEALCOUNT);
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]) {
            Bestiary.setSeen(getClass());
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

        HealCount++;

        if (HealCount == 1) {
            Sample.INSTANCE.play(Assets.Sounds.MISTA1);
            this.sprite.showStatus(CharSprite.POSITIVE, "[넘버 원!]");
        } else if (HealCount == 2) {
            Sample.INSTANCE.play(Assets.Sounds.MISTA2);
            this.sprite.showStatus(CharSprite.POSITIVE, "[넘버 투!]");
        } else if (HealCount == 3) {
            Sample.INSTANCE.play(Assets.Sounds.MISTA3);
            this.sprite.showStatus(CharSprite.POSITIVE, "[넘버 쓰리!]");
        } else if (HealCount == 4) {
            Sample.INSTANCE.play(Assets.Sounds.MISTA4);
            this.sprite.showStatus(CharSprite.POSITIVE, "[넘버 파이브!]");
        } else if (HealCount == 5) {
            Sample.INSTANCE.play(Assets.Sounds.MISTA5);
            this.sprite.showStatus(CharSprite.POSITIVE, "[넘버 식스!]");
        }

        if (HealCount >= 6) {
            starpower++;
            HealCount = 0;
        }

        if (starpower >= 1) {
            GameScene.flash(0x80FFFFFF);
            Camera.main.shake(2, 1.5f);
            Sample.INSTANCE.play(Assets.Sounds.SP, 1.1f, 1f);
            Sample.INSTANCE.play(Assets.Sounds.MISTA6);
            this.sprite.showStatus(CharSprite.POSITIVE, "[넘버 세븐!]");
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.alignment != Alignment.ALLY && Dungeon.level.heroFOV[mob.pos] && alignment != Alignment.ENEMY) {
                    int dmg = this.damageRoll() - mob.drRoll();
                    dmg = Math.round(dmg * 1.2f);

                    mob.damage(dmg, this);
                }
            }

            starpower = 0;
        }

        return damage;
    }

    @Override
    public void die(Object cause) {
        this.yell(Messages.get(this, "die"));
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