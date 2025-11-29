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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.levels.ArenaBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KS1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KarsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Lisa2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Lisa3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LisaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSoldierNewSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class FakeKars extends Mob {

    {
        spriteClass = KarsSprite.class;

        HP = HT = 100;
        defenseSkill = 20;
        EXP = 0; // No EXP since this is a fake boss

        baseSpeed = 1.2f;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new KarsSprite(), new KarsSprite(), new KarsSprite(), new LisaSprite()},
                    new String[]{"카즈", "카즈", "카즈", "리사리사"},
                    new String[]{
                            Messages.get(FakeKars.class, "t1"),
                            Messages.get(FakeKars.class, "t2"),
                            Messages.get(FakeKars.class, "t3"),
                            Messages.get(FakeKars.class, "t4")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(12, 18);
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Bleeding.class).set(damage * 0.5f);
        }
        return damage;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        // Lisa를 찾아서 게임오버 없이 사망시키기
        for (Char ch : Actor.chars()) {
            if (ch instanceof Lisa) {
                ((Lisa) ch).dieWithoutGameOver(this);
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BONES, Random.Float(1.2f, 0.9f));
        }

        Music.INSTANCE.play(Assets.Music.TENDENCY3, true);

        // Show death dialogue
        WndDialogueWithPic.dialogue(
                new CharSprite[]{new Lisa3Sprite(), new Lisa2Sprite(), new KarsSprite(), new KarsSprite(), new KarsSprite(), new KarsSprite(), new KS1Sprite()},
                new String[]{"카즈?", "리사리사", "카즈", "카즈", "카즈", "카즈", "흡혈귀 병사들"},
                new String[]{
                        Messages.get(FakeKars.class, "t5"),
                        Messages.get(FakeKars.class, "t6"),
                        Messages.get(FakeKars.class, "t7"),
                        Messages.get(FakeKars.class, "t8"),
                        Messages.get(FakeKars.class, "t9"),
                        Messages.get(FakeKars.class, "t10"),
                        Messages.get(FakeKars.class, "t11")
                },
                new byte[]{
                        WndDialogueWithPic.DIE,
                        WndDialogueWithPic.RUN,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE
                }
        );

        // Spawn 8 zombies - handled by ArenaBossLevel
        if (Dungeon.level instanceof ArenaBossLevel) {
            ((ArenaBossLevel) Dungeon.level).onFakeKarsDefeated();
        }
    }
}

