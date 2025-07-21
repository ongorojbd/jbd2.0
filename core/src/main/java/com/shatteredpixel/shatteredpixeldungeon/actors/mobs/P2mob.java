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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PassioneSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//FIXME the AI for these things is becoming a complete mess, should refactor
public class P2mob extends Mob {

    private int level = Dungeon.depth;

    {
        spriteClass = PassioneSprite.Ab.class;
        HP = HT = (1 + level) * 6;
        EXP = 0;

        state = WANDERING;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
    }

    private static final String LEVEL = "level";
    private static final String ALIGMNENT = "alignment";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(ALIGMNENT, alignment);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        if (bundle.contains(ALIGMNENT)) alignment = bundle.getEnum(ALIGMNENT, Alignment.class);
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
        return super.drRoll() + Random.NormalIntRange(0, 2 + level / 2);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        if (alignment != Alignment.ENEMY) {
            if (Random.Int(3) == 0) {
                this.sprite.showStatus(CharSprite.POSITIVE, "[무디 블루스!]");
                for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
                    int p = pos + PathFinder.NEIGHBOURS9[i];
                    if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                        respawnPoints.add(p);
                    }
                }

                int index = Random.index(respawnPoints);

                MirrorImage mob = new MirrorImage();
                mob.duplicate(hero);
                GameScene.add(mob);
                ScrollOfTeleportation.appear(mob, respawnPoints.get(index));
            }
        }

        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
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

        target = hero.pos;

        return super.getCloser(target);
    }

}