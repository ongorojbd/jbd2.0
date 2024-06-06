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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vitam;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Newro;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HealingDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpeedwagonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillcSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Tendency extends DirectableAlly {

    {
        spriteClass = WillcSprite.class;

        HP = HT = 100;
        defenseSkill = 5;
        viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
        immunities.add(AllyBuff.class);
    }

    private int sph = 1;

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

        if (HP < 86 && sph == 1) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h1"));
            GLog.h(Messages.get(SpeedWagon.class, "t1", this.HP,this.HT));
            sph++;
        }
        else if (HP < 66 && sph == 2) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h2", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t2", this.HP,this.HT));
            sph++;
        }
        else if (HP < 46 && sph == 3) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h3", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t3", this.HP,this.HT));
            sph++;
        }
        else if (HP < 26 && sph == 4) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h4", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t4", this.HP,this.HT));
            sph++;
        }
        else if (HP < 11 && sph == 5) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h5", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t5", this.HP,this.HT));
            sph++;
        }

    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        return damage;
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
        return 11;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 3 );
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

    public void a2() {
        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = this.pos+i;
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                //trace a ballistica to our target (which will also extend past them
                Ballistica trajectory = new Ballistica(this.pos, ch.pos, Ballistica.STOP_TARGET);
                //trim it to just be the part that goes past them
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                //knock them back along that ballistica
                WandOfBlastWave.throwChar(ch, trajectory, 3, false, true, this);

            }
            if (Dungeon.level.map[cell] == Terrain.DOOR) {
                Level.set(cell, Terrain.OPEN_DOOR);
                Dungeon.observe();
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        WandOfBlastWave.BlastWave.blast(this.pos);
    }

    public void a3() {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos + 1).burst(EnergyParticle.FACTORY, 5);
            if (mob.pos == this.pos + 1){
                mob.damage(5 * Dungeon.depth, this);
                Buff.affect( mob, SoulMark.class, 5f );
            }
        }
        for (Mob mob2 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos - 1).burst(EnergyParticle.FACTORY, 5);
            if (mob2.pos == this.pos - 1){
                mob2.damage(5 * Dungeon.depth, this);
                Buff.affect( mob2, SoulMark.class, 5f );
            }
        }
        for (Mob mob3 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos + 2).burst(EnergyParticle.FACTORY, 5);
            if (mob3.pos == this.pos + 2){
                mob3.damage(5 * Dungeon.depth, this);
                Buff.affect( mob3, SoulMark.class, 5f );
            }
        }
        for (Mob mob4 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos - 2).burst(EnergyParticle.FACTORY, 5);
            if (mob4.pos == this.pos - 2){
                mob4.damage(5 * Dungeon.depth, this);
                Buff.affect( mob4, SoulMark.class, 5f );
            }
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        Sample.INSTANCE.play(Assets.Sounds.SPW5);
        Dungeon.hero.damage(9999, this);

        if (!Dungeon.hero.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(Char.class, "kill", name()));
        }

        yell( Messages.get(this, "death") );
        GLog.h(Messages.get(SpeedWagon.class, "death2"));
    }
}