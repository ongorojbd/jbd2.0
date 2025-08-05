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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AbominationSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Zombie2Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;

public class Abomination extends Mob {

    {
        spriteClass = AbominationSprite.class;

        HP = HT = 36;
        defenseSkill = 10;

        EXP = 7;
        maxLvl = 14;

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
        immunities.add(CorrosiveGas.class);
    }

    private int moving;
    private int abilityCooldown = 0;
    private boolean berserkMode = false;
    private int berserkTurns = 0;
    
    // 능력 사용 관련 변수들
    private static final int CORROSIVE_SPRAY_COOLDOWN = 6;
    private static final int BERSERK_TRIGGER_HP = 3; // HP가 3 이하일 때 광분 모드
    private static final int BERSERK_DURATION = 5; // 광분 지속 턴

    @Override
    protected boolean act() {
        // 능력 사용 체크
        if (canUseAbility()) {
            useAbility();
        }
        
        // 광분 모드 체크
        checkBerserkMode();
        
        return super.act();
    }
    
    private boolean canUseAbility() {
        if (abilityCooldown > 0) {
            abilityCooldown--;
            return false;
        }
        
        // HP가 낮을수록 능력 사용 빈도 증가
        if (HP <= HT/2) {
            return Random.Int(2) == 0; // 50% 확률 (더 자주 사용)
        } else {
            return Random.Int(4) == 0; // 25% 확률
        }
    }
    
    private void useAbility() {
        if (berserkMode) {
            // 광분 모드에서는 강화된 부식성 분사 사용
            enhancedCorrosiveSpray();
        } else {
            // 일반 모드에서는 부식성 분사 사용
            corrosiveSpray();
        }
        abilityCooldown = CORROSIVE_SPRAY_COOLDOWN;
    }
    
    private void corrosiveSpray() {
        
        // 전방 3칸에 부식성 가스 생성 (전략적 요소 강화)
        int targetPos = pos + (hero.pos - pos) / Math.max(1, Dungeon.level.distance(pos, hero.pos));
        for (int i = -1; i <= 1; i++) {
            int sprayPos = targetPos + i;
            if (!Dungeon.level.solid[sprayPos]) {
                GameScene.add(Blob.seed(sprayPos, 4, CorrosiveGas.class));
            }
        }
        
        // 주변 8칸에도 약간의 부식성 가스 생성 (주변 몹들에게도 피해)
        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[pos + i] && Random.Int(3) == 0) {
                GameScene.add(Blob.seed(pos + i, 2, CorrosiveGas.class));
            }
        }
    }
    
    private void enhancedCorrosiveSpray() {
        if (Dungeon.level.heroFOV[pos]) {
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enhanced_corrosive_spray"));
            Sample.INSTANCE.play(Assets.Sounds.GAS);
        }
        
        // 광분 모드에서는 더 강력한 부식성 분사
        // 전방 5칸에 강화된 부식성 가스 생성
        int targetPos = pos + (hero.pos - pos) / Math.max(1, Dungeon.level.distance(pos, hero.pos));
        for (int i = -2; i <= 2; i++) {
            int sprayPos = targetPos + i;
            if (!Dungeon.level.solid[sprayPos]) {
                GameScene.add(Blob.seed(sprayPos, 6, CorrosiveGas.class));
            }
        }
        
        // 주변 8칸에도 강화된 부식성 가스 생성
        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[pos + i]) {
                GameScene.add(Blob.seed(pos + i, 4, CorrosiveGas.class));
            }
        }
    }
    
    private void checkBerserkMode() {
        if (HP <= BERSERK_TRIGGER_HP && !berserkMode) {
            berserkMode = true;
            berserkTurns = BERSERK_DURATION;
            
            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "berserk"));
                Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
            }
            
            // 광분 모드에서 스탯 증가
            defenseSkill += 3;
        }
        
        if (berserkMode) {
            berserkTurns--;
            if (berserkTurns <= 0) {
                berserkMode = false;
                defenseSkill -= 3;

            }
        }
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 10);
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 6);
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
        
        // 사망 시 최후의 부식성 폭발
        if (Random.Int(2) == 0) {
            finalDeathCorrosiveBurst();
        }

    }
    
    private void finalDeathCorrosiveBurst() {
        if (Dungeon.level.heroFOV[pos]) {
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "final_corrosive_burst"));
            Sample.INSTANCE.play(Assets.Sounds.GAS);
        }
        
        // 사망 시 주변에 강력한 부식성 가스 생성
        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[pos + i]) {
                GameScene.add(Blob.seed(pos + i, 8, CorrosiveGas.class));
            }
        }
        GameScene.add(Blob.seed(pos, 10, CorrosiveGas.class));
    }
    
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("abilityCooldown", abilityCooldown);
        bundle.put("berserkMode", berserkMode);
        bundle.put("berserkTurns", berserkTurns);
    }
    
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        abilityCooldown = bundle.getInt("abilityCooldown");
        berserkMode = bundle.getBoolean("berserkMode");
        berserkTurns = bundle.getInt("berserkTurns");
    }
}