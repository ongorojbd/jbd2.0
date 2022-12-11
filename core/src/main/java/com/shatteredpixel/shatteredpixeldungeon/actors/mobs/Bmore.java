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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BmoreGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Holy1;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Holy2;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Maga;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BmoreSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Bmore extends Mob {

    {
        spriteClass = BmoreSprite.class;

        HP = HT = 25;
        HUNTING = new Mob.Hunting();
        immunities.add(BmoreGas.class);

        immunities.add( Paralysis.class );
        immunities.add( Roots.class );
        immunities.add( Dread.class );
        immunities.add( Terror.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        immunities.add( Blizzard.class );
        immunities.add( CorrosiveGas.class );
        immunities.add( Electricity.class );
        immunities.add( Fire.class );
        immunities.add( Freezing.class );
        immunities.add( Inferno.class );
        immunities.add( ParalyticGas.class );
        immunities.add( StenchGas.class );
        immunities.add( ToxicGas.class );
        properties.add(Property.BOSS);

        EXP = 0;
        maxLvl = -9;
    }

    int summonCooldown = 1;
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    int sCooldown = 5;
    private static final String SCOOLDOWN = "scooldown";

    private boolean seenBefore = false;
    private boolean threatened = false;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
        bundle.put(SCOOLDOWN, sCooldown);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        summonCooldown = bundle.getInt( SUMMON_COOLDOWN );
        sCooldown = bundle.getInt( SCOOLDOWN );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 15);
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        if (!seenBefore) {
            yell( Messages.get(this, "1") );
        }

        seenBefore = true;
    }

    @Override
    protected boolean act() {
        summonCooldown--;
        sCooldown--;

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        Music.INSTANCE.play(Assets.Music.CIV, true);

        if (sCooldown <= 0) {
            sprite.showStatus(CharSprite.WARNING, Messages.get(Bmore.class, "l"));
            Splash.at( DungeonTilemap.tileCenterToWorld( pos ), -PointF.PI/2, PointF.PI/2, 0x5bc1e3, 100, 0.01f);
            new Fadeleaf().activate(this);
            sCooldown = (9);
        }


        //in case DM-201 hasn't been able to act yet
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
            fieldOfView = new boolean[Dungeon.level.length()];
            Dungeon.level.updateFieldOfView( this, fieldOfView );
        }

        if (summonCooldown <= 0) {
            threatened = true;
            summonCooldown = (6);
        }

        if (paralysed <= 0 && state == HUNTING && enemy != null && enemySeen
                && threatened && !Dungeon.level.adjacent(pos, enemy.pos) && fieldOfView[enemy.pos]){
            enemySeen = enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }


        //if (Dungeon.level.map[hero.pos] == Terrain.WATER) {
         //   int cell = pos;
          //  Dungeon.hero.damage(Dungeon.hero.HT/20, this);
         //   if (!isAlive()) return true;
     //   }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 1){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 1;
        }
        super.damage(dmg, src);
    }

    public void onZapComplete(){
        zap();
        next();
    }

    @Override
    protected Char chooseEnemy() {

        return super.chooseEnemy();
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        GameScene.bossSlain();

        Sample.INSTANCE.play(Assets.Sounds.BONES, 1f, 0.75f);

        Buff.affect(hero, Holy2.class);

        Music.INSTANCE.end();

        yell( Messages.get(this, "2") );
        GLog.h(Messages.get(Civil.class, "45"));

    }

    private void zap( ){
        sprite.showStatus(CharSprite.WARNING, Messages.get(Bmore.class, "z"));
        threatened = false;
        spend(TICK);
        GameScene.add(Blob.seed(enemy.pos, 15, BmoreGas.class));
        for (int i : PathFinder.NEIGHBOURS8){
            if (!Dungeon.level.solid[enemy.pos+i]) {
                GameScene.add(Blob.seed(enemy.pos + i, 5, BmoreGas.class));
            }
        }

    }


}