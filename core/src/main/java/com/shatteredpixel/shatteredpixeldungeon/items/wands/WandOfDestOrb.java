/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Soft;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscD;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfDestOrb extends Wand {

    {
        image = ItemSpriteSheet.WAND_TRANSFUSION;
        icon = ItemSpriteSheet.Icons.POTION_DIVINE;

        collisionProperties = Ballistica.PROJECTILE;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x00CCFF, 3f);
    }

    private boolean freeCharge = false;

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        
        if (action.equals(AC_ZAP)) {
            curUser = hero;
            curItem = this;
            GameScene.selectCell(adjacentTargeter);
        }
    }
    
    private CellSelector.Listener adjacentTargeter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target == null) {
                return;
            }
            
            // 바로 한 칸 근처에 있는 위치만 선택 가능
            if (!Dungeon.level.adjacent(curUser.pos, target)) {
                GLog.w(Messages.get(WandOfDestOrb.this, "too_far"));
                return;
            }
            
            // 해당 위치에 다른 캐릭터가 있으면 안됨
            if (Actor.findChar(target) != null) {
                GLog.w(Messages.get(WandOfDestOrb.this, "cell_occupied"));
                return;
            }
            
            // 통과 불가능한 위치면 안됨
            if (Dungeon.level.solid[target] || Dungeon.level.avoid[target]) {
                GLog.w(Messages.get(WandOfDestOrb.this, "invalid_cell"));
                return;
            }
            
            if (curCharges == 0) {
                GLog.w(Messages.get(Wand.class, "fizzles"));
                return;
            }
            
            curUser.sprite.zap(target);
            Ballistica beam = new Ballistica(curUser.pos, target, collisionProperties);
            onZap(beam);
            
            curCharges--;
            updateQuickslot();
            curUser.spendAndNext(1f);
        }
        
        @Override
        public String prompt() {
            return Messages.get(WandOfDestOrb.class, "prompt");
        }
    };

    @Override
    public void onZap(Ballistica beam) {
        // Calculate damage based on WandOfLightning damage * 1.2
        WandOfLightning lightningWand = new WandOfLightning();
        int minDamage = Math.round(lightningWand.min(buffedLvl()) * 1.2f);
        int maxDamage = Math.round(lightningWand.max(buffedLvl()) * 1.2f);
        
        int spawnPos = beam.collisionPos;
        
        Soft soft = new Soft();
        soft.setDamageRange(minDamage, maxDamage);
        soft.pos = spawnPos;
        soft.state = soft.HUNTING;
        GameScene.add(soft);
        soft.beckon(Dungeon.hero.pos);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //cripples enemy
        Buff.prolong(defender, Cripple.class, Math.round((1 + staff.buffedLvl()) * procChanceMultiplier(attacker)));
    }

    @Override
    public void fx(Ballistica beam, Callback callback) {
        curUser.sprite.parent.add(
                new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
        callback.call();
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x00FFFF);
        particle.am = 0.6f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.PI2), 2f);
        particle.setSize(1f, 2f);
        particle.radiateXY(0.5f);
    }

    @Override
    public String statsDesc() {
        if (Dungeon.hero != null) {
        int selfDMG = Math.round(Dungeon.hero.HT * 0.05f);
            if (levelKnown)
                return Messages.get(this, "stats_desc", selfDMG, selfDMG + 3 * buffedLvl(), 5 + buffedLvl(), 3 + buffedLvl() / 2, 6 + buffedLvl());
            else
                return Messages.get(this, "stats_desc", selfDMG, selfDMG, 5, 3, 6);
        } else return Messages.get(this, "desc2");
    }

    private static final String FREECHARGE = "freecharge";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        freeCharge = bundle.getBoolean(FREECHARGE);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FREECHARGE, freeCharge);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{BossdiscD.class, WandOfTransfusion.class};
            inQuantity = new int[]{1, 1};

            cost = 0;

            output = WandOfDestOrb.class;
            outQuantity = 1;
        }
    }
}
