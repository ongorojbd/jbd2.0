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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Newro;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HealingDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpeedwagonSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SpeedWagon3 extends DirectableAlly {

    {
        spriteClass = SpeedwagonSprite.class;

        HP = HT = 100;
        defenseSkill = 5;
        viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
        immunities.add(AllyBuff.class);
    }

    private boolean canCauseGameOver = true;
    private static final String CAN_CAUSE_GAME_OVER = "can_cause_game_over";

    @Override
    public String description() {
        String desc = super.description();

            desc += "\n" + Messages.get(this, "p1", this.HP,this.HT);

        return desc;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 50){
            dmg = 50;
        }

        super.damage(dmg, src);

    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

    @Override
    public void defendPos(int cell) {
        Sample.INSTANCE.play(Assets.Sounds.SPW4);
        yell(Messages.get(this, "g" + Random.IntRange(1, 5)));
        super.defendPos(cell);
    }

    @Override
    public void followHero() {
        Sample.INSTANCE.play(Assets.Sounds.SPW5);
        yell(Messages.get(this, "f" + Random.IntRange(1, 5)));
        super.followHero();
    }

    @Override
    public void targetChar(Char ch) {
        Sample.INSTANCE.play(Assets.Sounds.SPW3);
        yell(Messages.get(this, "d" + Random.IntRange(1, 5)));
        super.targetChar(ch);
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 8 );
    }

    @Override
    protected boolean act() {
        int oldPos = pos;
        boolean result = super.act();
        //partially simulates how the hero switches to idle animation
        if ((pos == target || oldPos == pos) && sprite.looping()){
            sprite.idle();
        }

        Dungeon.level.updateFieldOfView( this, fieldOfView );
        GameScene.updateFog(pos, viewDistance+(int)Math.ceil(speed()));

        return result;
    }

    public void sayHeroKilled(){
        yell( Messages.get( this, "z"));
        Sample.INSTANCE.play(Assets.Sounds.SPW5);
        GLog.newLine();
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        Sample.INSTANCE.play(Assets.Sounds.SPW5);

        if (canCauseGameOver) {
            yell(Messages.get(this, "death"));
            Dungeon.hero.die(this);
        }

        if (!Dungeon.hero.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(Char.class, "kill", name()));
        }

        yell( Messages.get(this, "death") );
        GLog.h(Messages.get(SpeedWagon3.class, "death2"));
    }

    public void dieWithoutGameOver(Object cause) {
        canCauseGameOver = false;
        die(cause);
    }

    public static void shareHealingPotion() {
        if (Dungeon.level == null) return;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof SpeedWagon3 && mob.isAlive()) {
                PotionOfHealing.cure(mob);
                Healing healing = Buff.affect(mob, Healing.class);
                healing.setHeal((int)(0.8f * mob.HT + 14), 0.25f, 0);
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CAN_CAUSE_GAME_OVER, canCauseGameOver);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        canCauseGameOver = bundle.getBoolean(CAN_CAUSE_GAME_OVER);
    }

}