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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Wgas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiegoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Pucci4Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RohanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Callback;
import java.util.ArrayList;

public class Rohan9 extends Mob {

    {
        HP = HT = 500;

        EXP = 0;
        maxLvl = 30;

        defenseSkill = 25;
        spriteClass = RohanSprite.class;
    }

    public int  Phase = 0;

    @Override
    public int damageRoll() {
        int dmg;
        dmg = Random.NormalIntRange( 45, 55 );
        return dmg;
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }


    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 150){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 150;
        }







        BossHealthBar.assignBoss(this);

    }



    @Override
    protected boolean canAttack(Char enemy) {
        return super.canAttack(enemy);
    }
    private Boolean hasadjacent;

    @Override
    protected boolean getCloser(int target) {
        return super.getCloser(target);
    }

    @Override
    protected boolean getFurther(int target) {
        return super.getFurther(target);
    }

    private static final String PHASE   = "Phase";

    @Override
    public void die(Object cause) {

        super.die(cause);
        Sample.INSTANCE.play(Assets.Sounds.P2);

        Dungeon.level.unseal();
        Music.INSTANCE.end();

        GameScene.bossSlain();

        GLog.p(Messages.get(this, "r"));
        yell(Messages.get(this, "defeated"));
    }

    @Override
    public void notice() {
        super.notice();
    }

    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);
        bundle.put( PHASE, Phase );

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

        super.restoreFromBundle(bundle);
        Phase = bundle.getInt(PHASE);
    }


}
