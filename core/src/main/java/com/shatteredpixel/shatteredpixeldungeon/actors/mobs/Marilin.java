/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BanditSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Marilin extends Mob {

    {
        spriteClass = BanditSprite.class;

        HP = HT = 50;
        defenseSkill = 10;

        EXP = 7;
        maxLvl = 14;
    }

    @Override
    public void notice() {

        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 12);
    }

    @Override
    public int attackSkill(Char target) {
        return 15;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        super.damage(dmg, src);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 6);
    }

    @Override
    public int attackProc(Char hero, int damage) {
        damage = super.attackProc(enemy, damage);
        if (this.buff(Doom.class) == null) {
            if (hero instanceof Hero) {
                if (Dungeon.gold > 101) {
                    Dungeon.gold -= 100;
                    Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                    CellEmitter.get(pos).burst(Speck.factory(Speck.COIN), 12);
                    GLog.w(Messages.get(this, "losemoney"));
                }
                return damage;
            }
        }
        return damage;
    }


    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.drop(new Gold().identify().quantity(1000), pos).sprite.drop(pos);
    }

    public static void spawn(PrisonLevel level) {
        int max = 4;
        if (Dungeon.isChallenged(Challenges.RAPSO)) max = 1;
        if (Random.Int(max) == 0) {
            if (Dungeon.depth == 8 && !Dungeon.bossLevel()) {

                Marilin npc = new Marilin();
                do {
                    npc.pos = level.randomRespawnCell(npc);
                } while (
                        npc.pos == -1 ||
                                level.heaps.get(npc.pos) != null ||
                                level.traps.get(npc.pos) != null ||
                                level.findMob(npc.pos) != null ||
                                //The imp doesn't move, so he cannot obstruct a passageway
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
                level.mobs.add(npc);
            }
        }
    }
}
