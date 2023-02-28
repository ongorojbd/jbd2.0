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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscB;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KiraSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KousakuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StowerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Kirakira extends Mob {
    private boolean threatened = false;

    {
        spriteClass = KiraSprite.class;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);

        HP = HT = 200;
        defenseSkill = 15;

        EXP = 0;
        maxLvl = 30;

        properties.add(Property.IMMOVABLE);
        state = PASSIVE;

        immunities.add(Terror.class);
        immunities.add(Dread.class );
        immunities.add(Sleep.class );
        immunities.add(Amok.class );
        immunities.add(Blindness.class );
        immunities.add(MagicalSleep.class );

        threatened = false;
    }

    private int targetNeighbor = Random.Int(8);
    public int  Phase = 0;

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

    @Override
    protected boolean act() {
        alerted = false;
        super.act();

        if (threatened == false){
            return true;
        }

        ArrayList<Integer> shockCells = new ArrayList<>();

        shockCells.add(pos + PathFinder.CIRCLE8[targetNeighbor]);

        {
            shockCells.add(pos + PathFinder.CIRCLE8[(targetNeighbor+4)%8]);
        }

        sprite.flash();

        boolean visible = Dungeon.level.heroFOV[pos];
        for (int cell : shockCells){
            if (Dungeon.level.heroFOV[cell]){
                visible = true;
            }
        }

        if (visible) {
            for (int cell : shockCells){
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 15);
                CellEmitter.center(cell).burst(SmokeParticle.FACTORY, 4);
                Sample.INSTANCE.play( Assets.Sounds.BLAST );

            }

        }

        for (int cell : shockCells) {
            shockChar(Actor.findChar(cell));
        }

        targetNeighbor = (targetNeighbor+1)%8;

        return true;
    }

    private void shockChar( Char ch ){
        if (ch != null && !(ch instanceof DM300)){
            ch.sprite.flash();
            ch.damage(Random.NormalIntRange(20, 30), new Electricity());
        }
    }


    @Override
    public int damageRoll() {
        return Random.NormalIntRange(35, 40);
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() { return Random.NormalIntRange(0, 5);}

    @Override
    public void damage(int dmg, Object src) {

        super.damage(dmg, src);

        if (Phase==0 && HP < 200) {
            Phase = 1;
            HP = 199;
            state = HUNTING;
            Music.INSTANCE.play(Assets.Music.CIV, true);
            if (!BossHealthBar.isAssigned()) {
                BossHealthBar.assignBoss(this);
                for (Char ch : Actor.chars()){
                }
            }
            GLog.n(Messages.get(this, "n"));
        }

        threatened = true;

    }

    private static final String PHASE   = "Phase";

    @Override
    public void die(Object cause) {

        super.die(cause);
        GLog.n(Messages.get(this, "d"));
        Dungeon.mboss14 = 0;
        Music.INSTANCE.play(Assets.Music.LABS_1, true);

        new Flare( 5, 32 ).color( 0x00FFFF, true ).show( hero.sprite, 1f );
        Sample.INSTANCE.play(Assets.Sounds.BADGE);
        GLog.p(Messages.get(Pucci4.class, "x"));
    }

    public static void spawn(LabsLevel level) {

        if (Dungeon.depth == 28 && !Dungeon.bossLevel()) {
            Kirakira centinel = new Kirakira();
            do {
                centinel.pos = level.randomRespawnCell(centinel);
            } while (centinel.pos == -1);
            level.mobs.add(centinel);
        }

    }

}
