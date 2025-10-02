/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WiredSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
// import com.watabou.utils.PathFinder;

public class Wired extends Mob {

    {
        spriteClass = WiredSprite.class;

        HP = HT = 160;
        defenseSkill = 24;

        EXP = 14;
        maxLvl = 27;

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    // Cross spike telegraph/resolve state
    private int spikeWindup = 0;
    private int spikeCooldown = 0;
    private java.util.ArrayList<Integer> spikeCells = new java.util.ArrayList<>();

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 40 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 16);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int dealt = super.attackProc(enemy, damage);
        
        // Spike attack - chance to cause bleeding with body hair spikes
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Bleeding.class).set(Random.IntRange(3, 6));
        }

        return dealt;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        int taken = super.defenseProc(enemy, damage);
        // Thorny Retaliation: if struck in melee range, Wired's spikes lash out
        if (enemy != null && Dungeon.level.adjacent(pos, enemy.pos)) {
            if (Random.Int(2) == 0) {
                int thorns = Random.NormalIntRange(4, 7);
                enemy.damage(thorns, this);
                Buff.affect(enemy, Bleeding.class).set(Random.IntRange(2, 3));
                if (enemy.sprite != null) enemy.sprite.emitter().burst(Speck.factory(Speck.BONE), 4);
            }
        }
        return taken;
    }

    @Override
    protected boolean act() {
        if (spikeCooldown > 0) spikeCooldown--;
        if (spikeWindup > 0) {
            spikeWindup--;
            if (spikeWindup == 0 && !spikeCells.isEmpty()) {
                if (state != SLEEPING) {
                    resolveCrossSpikes();
                    return true;
                } else {
                    // cancel if asleep when it would resolve
                    spikeCells.clear();
                    spikeCooldown = Random.NormalIntRange(2, 4);
                }
            }
        }

        // Start cross spike telegraph when enemy present and off cooldown (does not consume turn)
        if (enemy != null && spikeCooldown <= 0 && spikeWindup == 0 && state != SLEEPING) {
            telegraphCrossSpikes();
        }

        return super.act();
    }

    private boolean telegraphCrossSpikes() {
        if (sprite == null) return false;
        spikeCells.clear();

        int w = Dungeon.level.width();
        int len = Dungeon.level.length();
        int cx = pos % w;
        int cy = pos / w;

        // Left
        for (int x = cx - 1; x >= 0; x--) {
            int c = cy * w + x;
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) break;
            spikeCells.add(c);
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }
        // Right
        for (int x = cx + 1; x < w; x++) {
            int c = cy * w + x;
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) break;
            spikeCells.add(c);
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }
        // Up
        for (int y = cy - 1; y >= 0; y--) {
            int c = y * w + cx;
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) break;
            spikeCells.add(c);
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }
        // Down
        for (int y = cy + 1; y < len / w; y++) {
            int c = y * w + cx;
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) break;
            spikeCells.add(c);
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }

        if (!spikeCells.isEmpty()) {
            if (sprite != null) sprite.emitter().burst(Speck.factory(Speck.ROCK), 4);
            spikeWindup = 1; // 1턴 예고
            return false; // no time spent, continue normal act
        }
        return false;
    }

    private void resolveCrossSpikes() {
        for (int c : spikeCells) {
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(35, 45);
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
            if (Dungeon.level.heroFOV[c]) CellEmitter.center(c).burst(Speck.factory(Speck.WOOL), 2);
        }
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "skill"));
        Sample.INSTANCE.play(Assets.Sounds.TONIO, 1f, 0.8f);
        spikeCells.clear();
        spikeCooldown = Random.NormalIntRange(3, 5);
        spend(1f);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (Random.Int( 3 ) == 0) {
            Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }
}