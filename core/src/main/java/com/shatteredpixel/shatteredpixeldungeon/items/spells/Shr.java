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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShrBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Shr extends Spell {

    {
        image = ItemSpriteSheet.SHR_BOMB;
    }

    @Override
    protected void onCast(Hero hero) {
        GameScene.selectCell(targeter);
    }
    
    private CellSelector.Listener targeter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target == null) {
                return;
            }
            
            // 바로 한 칸 근처에 있는 위치만 선택 가능
            if (!Dungeon.level.adjacent(curUser.pos, target)) {
                GLog.w(Messages.get(Shr.this, "too_far"));
                return;
            }
            
            // 해당 위치에 다른 캐릭터가 있으면 안됨
            if (Actor.findChar(target) != null) {
                GLog.w(Messages.get(Shr.this, "cell_occupied"));
                return;
            }
            
            // 통과 불가능한 위치면 안됨
            if (Dungeon.level.solid[target] || Dungeon.level.avoid[target]) {
                GLog.w(Messages.get(Shr.this, "invalid_cell"));
                return;
            }
            
            Sample.INSTANCE.play(Assets.Sounds.SHEER);
            
            ShrBomb bomb = new ShrBomb();
            bomb.pos = target;
            bomb.state = bomb.HUNTING;
            GameScene.add(bomb);
            bomb.beckon(Dungeon.hero.pos);
            
            curUser.busy();
            curUser.sprite.zap(target);
            curUser.spendAndNext(1f);
            
            // 아이템 소비
            detach(curUser.belongings.backpack);
            updateQuickslot();
            
            Catalog.countUse(Shr.this.getClass());
            if (Random.Float() < talentChance) {
                Talent.onScrollUsed(curUser, curUser.pos, talentFactor, Shr.this.getClass());
            }
        }
        
        @Override
        public String prompt() {
            return Messages.get(Shr.class, "prompt");
        }
    };

    @Override
    public String desc() {
        // ShrBomb 폭발 데미지 계산 (ShrapnelBomb보다 75% 강함)
        int minDamage = Math.round((4 + Dungeon.scalingDepth()) * 1.75f);
        int maxDamage = Math.round((12 + 3 * Dungeon.scalingDepth()) * 1.75f);
        return Messages.get(this, "desc", minDamage, maxDamage);
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 50);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs = new Class[]{Bomb.class, MetalShard.class};
            inQuantity = new int[]{1, 2};

            cost = 8;

            output = Shr.class;
            outQuantity = 1;
        }
    }
}

