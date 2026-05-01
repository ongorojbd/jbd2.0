/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Butterfly2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Butterfly3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class KarsCorrosiveButterfly extends Mob {

    private static final float TIME_TO_ZAP = 1f;

    {
        spriteClass = Butterfly2Sprite.class;

        HP = HT = 30;
        defenseSkill = 10;
        EXP = 0;
        maxLvl = -9;

        viewDistance = 6;
        flying = true;

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
        immunities.add( ShrGas.class );
    }

    @Override
    public boolean act() {
        sprite.add(CharSprite.State.KARS);
        if (enemy == null || !enemy.isAlive()) {
            enemy = Dungeon.hero;
        }
        beckon(Dungeon.hero.pos);

        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 50, 60 );
    }
    @Override
    public int attackSkill( Char target ) {
        return 45;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(5, 20);
    }

    // Ballistica.MAGIC_BOLT로 시야 내 어느 거리든 공격 가능
    @Override
    protected boolean canAttack(Char enemy) {
        return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    // Teq 패턴: 인접이면 근접, 원거리면 zap
    @Override
    protected boolean doAttack(Char enemy) {
        if (Dungeon.level.adjacent(pos, enemy.pos)) {
            return super.doAttack(enemy);
        } else {
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap(enemy.pos);
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    // 독침 클래스 (저항 구분용)
    public static class PoisonBolt {}

    // 비시각 경로: 직접 데미지 + 독 적용
    private void zap() {
        spend(TIME_TO_ZAP);

        Invisibility.dispel(this);
        Char enemy = this.enemy;
        if (hit(this, enemy, true)) {

            if (enemy == Dungeon.hero && Random.Int(2) == 0) {
                Buff.affect(enemy, Poison.class).set(6f);
            }

            int dmg = Random.NormalIntRange(15, 25);
            enemy.damage(dmg, new PoisonBolt());

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail(getClass());
            }
        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }
    }

    // ButterflySprite는 onAttackComplete를 호출하므로 attackProc에서 독 처리
    // onZapComplete는 비시각 경로 미사용 시 일관성을 위해 유지
    public void onZapComplete() {
        zap();
        next();
    }

    // 시각 경로(onAttackComplete → attack)에서도 독 부여
    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        Buff.affect(enemy, Poison.class).set(6f);
        return damage;
    }

    // 근접 공격을 받을 때 1/3 확률로 날개 가루 독 반격
    @Override
    public int defenseProc(Char enemy, int damage) {
        damage = super.defenseProc(enemy, damage);
        if (Dungeon.level.adjacent(pos, enemy.pos) && Random.Int(3) == 0) {
            Buff.affect(enemy, Poison.class).set(4f);
        }
        return damage;
    }

    // 사망 시 ToxicTrap처럼 독가스 분출
    @Override
    public void die(Object cause) {
        sprite.remove(CharSprite.State.KARS);

        GameScene.add(Blob.seed(pos, 300, ToxicGas.class));

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        super.die(cause);
    }
}
