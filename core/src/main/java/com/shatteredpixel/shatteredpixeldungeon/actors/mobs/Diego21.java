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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.D4C;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Holy1;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover2;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscA;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr7;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiegoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiegonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Diego21 extends Mob {

    {
        spriteClass = DiegonSprite.class;

        HP = HT = 1000;
        defenseSkill = 4;
        viewDistance = 4;

        state = HUNTING;

        baseSpeed = 0.4f;

        immunities.add( Fire.class );
        properties.add(Property.BOSS);

        EXP = 0;
        maxLvl = -9;

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 22, 44 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return true;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        GameScene.bossSlain();
        GameScene.flash(0xFFCC00);
        Sample.INSTANCE.play(Assets.Sounds.NANIDI);
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        Music.INSTANCE.end();

        Camera.main.shake(20, 2f);

        Statistics.d4cEnhanced = true;
        Buff.detach(hero, D4C.class);
        Buff.affect(hero, D4C.class);

        GLog.p(Messages.get(Diego12.class, "7"));
        WndDialogueWithPic.dialogue(
                new CharSprite[]{new DiegonSprite()},
                new String[]{"디에고 브란도"},
                new String[]{
                        Messages.get(Diego21.class, "4")
                },
                new byte[]{
                        WndDialogueWithPic.DIE
                }
        );

    }

}