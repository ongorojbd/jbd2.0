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
import static com.watabou.utils.PathFinder.NEIGHBOURS9;
import static com.watabou.utils.PathFinder.buildDistanceMap;
import static com.watabou.utils.PathFinder.distance;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.IceBlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Diocoffin;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Diobrando2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Diobrando2 extends Mob {

    {
        spriteClass = Diobrando2Sprite.class;

        HP = HT = 200;

        defenseSkill = 8;
        EXP = 0;
        maxLvl = -9;
        HUNTING = new Diobrando2.Hunting();

        immunities.add( Chill.class );
        immunities.add( Frost.class );

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.FIERY);
    }
    private ArrayList<Integer> targetedCells = new ArrayList<>();

    public int  Phase = 0;
    private float leapCooldown = 3;
    private int blastcooldown = 7;

    private int volcanotime = 0;

    private int spw = 0;

    private int volcanocooldown = 9;

    private int Burstpos = -1;

    private int zcooldown = 99999;
    private int charge = 0; // 2이 될경우 강화 사격

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            this.yell(Messages.get(this, "1"));
            GLog.p(Messages.get(this, "10"));
            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            Sample.INSTANCE.play(Assets.Sounds.HAHAH);
            Statistics.duwang3 = 5;
        }
    }

    @Override
    protected boolean getCloser(int target) {

        target = hero.pos;

        return super.getCloser( target );
    }

    @Override
    public void damage(int dmg, Object src) {

        BossHealthBar.assignBoss(this);
        int preHP = HP;
        int dmgTaken = preHP - HP;

        super.damage(dmg, src);

        if (Phase==0 && HP < 90) {
            Phase = 1;
            immunities.add(Grim.class );
            GameScene.flash( 0xFF0000 );
            new Flare( 5, 32 ).color( 0xFF0000, true ).show( this.sprite, 2f );
            Music.INSTANCE.play(Assets.Music.HALLS_BOSS, true);
            Sample.INSTANCE.play(Assets.Sounds.DIO6);
            Sample.INSTANCE.play( Assets.Sounds.BLAST );
            Camera.main.shake(12, 0.5f);
            summonRats(3);
            yell(Messages.get(this, "t"));
            GLog.p(Messages.get(this, "jo"));
            Buff.prolong(hero, Blindness.class, 15f);
            Buff.prolong(hero, Vulnerable.class, 15f);


            zcooldown = 10;
        }

        if (Phase==1 && HP < 31) {
            Phase = 2;
            GameScene.flash( 0xFFCC00 );
            GLog.n(Messages.get(this, "j"));
            Music.INSTANCE.play(Assets.Music.JONATHAN, true);
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
            this.sprite.add(CharSprite.State.MARKED);
            this.sprite.add(CharSprite.State.BURNING);
        }
    }


    private boolean UseAbility() {

        if (volcanocooldown <= 0) {
            if (Burstpos == -1) {
                GLog.w(Messages.get(this, "i"));
                // 위치 미지정시, 이번 턴에는 폭발을 일으킬 지점을 정합니다.

                Burstpos = hero.pos;

                for (int i : NEIGHBOURS9) {
                    int vol = Fire.volumeAt(Burstpos+i, Fire.class);
                    if (vol < 4){
                        sprite.parent.addToBack(new TargetedCell(Burstpos + i, 0xFF00FF));
                    }
                }


                volcanotime++;
                return false;
            }


            else if (volcanotime == 1) {

                for (int i : NEIGHBOURS9) {
                    int vol = Fire.volumeAt(Burstpos+i, Fire.class);
                    if (vol < 4){
                        sprite.parent.addToBack(new TargetedCell(Burstpos + i, 0xFF00FF));
                    }
                }


                volcanotime++;

                return false;
            }

            else if (volcanotime == 2) {
                buildDistanceMap(Burstpos , BArray.not(Dungeon.level.solid, null), 1);
                for (int cell = 0; cell < distance.length; cell++) {
                    if (distance[cell] < Integer.MAX_VALUE) {
                        Char ch = Actor.findChar(cell);
                        int vol = Fire.volumeAt(cell, Fire.class);
                        if (vol < 4){
                            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 15);
                            CellEmitter.center(cell).burst(SmokeParticle.FACTORY, 4);
                            GameScene.add( Blob.seed( cell, 50, Fire.class ) );
//                            CellEmitter.get( cell ).burst( FlameParticle.FACTORY, 15 );
                        }
                        if (ch != null&& !(ch instanceof Diobrando2)) {
                            if ((ch.alignment != alignment || ch instanceof Zombie || ch instanceof Zombiedog)) {
                                ch.damage(Random.NormalIntRange(15, 20), this);

                                if (enemy == hero && !enemy.isAlive()) {
                                    Dungeon.fail(getClass());
                                }
                            }
                        }}}

                Camera.main.shake(9, 0.5f);
                Sample.INSTANCE.play( Assets.Sounds.BLAST );
                Burstpos = -1;
                volcanotime=0;
                volcanocooldown= Random.NormalIntRange(1,6);

                return true;
            }


            return false;
        }

        if (zcooldown <= 0) {
            yell( Messages.get(this, "zomb") );
            CellEmitter.get( this.pos ).burst( MagicMissile.MagicParticle.FACTORY, 12 );
            Sample.INSTANCE.play(Assets.Sounds.DIO6);
            summonRats(3);
            new Flare( 5, 32 ).color( 0xFF0000, true ).show( this.sprite, 2f );
            zcooldown = 10;

            return true;
        }

        if (FireBlast()){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Diobrando2.class, "u"));
            Sample.INSTANCE.play(Assets.Sounds.DIO4);
            return true;
        }

        return false;
    }

    public class Blast { }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        return damage;
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        damage = super.defenseProc( enemy, damage );
        return damage;
    }

    @Override
    public void move( int step, boolean travelling) {
        charge = 0;
        super.move( step, travelling);
    }

    private static final String BLAST_CD   = "blastcooldown";
    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String LEAP_POS = "leap_pos";
    private static final String LEAP_CD = "leap_cd";
    private static final String SKILLCD   = "charge";
    private static final String PHASE   = "Phase";
    private static final String TARGETED_CELLS = "targeted_cells";

    private static final String SKILL2TPOS   = "Burstpos";

    private static final String VOCAL_TIME   = "volcanotime";

    private static final String BARRIER_CD  = "volcanocooldown";
    private static final String Z_CD  = "zcooldown";

    private static final String VOCAL_CD   = "volcanocooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( BLAST_CD, blastcooldown );
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(LEAP_CD, leapCooldown);
        bundle.put( SKILLCD, charge );
        bundle.put( VOCAL_TIME, volcanotime );
        bundle.put( PHASE, Phase );
        bundle.put( BARRIER_CD, volcanocooldown );
        bundle.put( Z_CD, zcooldown );
        bundle.put( SKILL2TPOS, Burstpos );
        bundle.put( VOCAL_CD, volcanocooldown );

        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        blastcooldown = bundle.getInt(BLAST_CD);
        leapCooldown = bundle.getFloat(LEAP_CD);
        charge = bundle.getInt(SKILLCD);
        Phase = bundle.getInt(PHASE);
        volcanotime = bundle.getInt(VOCAL_TIME);
        volcanocooldown = bundle.getInt(BARRIER_CD);
        zcooldown = bundle.getInt(Z_CD);
        volcanocooldown = bundle.getInt(VOCAL_CD);
        Burstpos = bundle.getInt(SKILL2TPOS);

        for (int i : bundle.getIntArray(TARGETED_CELLS)){
            targetedCells.add(i);
        }
    }

    private int lastEnemyPos = -1;

    @Override
    protected boolean act() {

        if (Phase == 1 && HP < 84) {
            HP += 1;
            if (Dungeon.level.heroFOV[pos] ){
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
            }
        }

        if (volcanocooldown == 1)  {
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
            hero.interrupt();
        }

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        {
            if (UseAbility()) return true;
        }

        if (blastcooldown > 0) blastcooldown--;
        if (volcanocooldown > 0) volcanocooldown--;
        if (zcooldown > 0) zcooldown--;

        AiState lastState = state;
        boolean result = super.act();
        if (paralysed <= 0) leapCooldown --;

        //if state changed from wandering to hunting, we haven't acted yet, don't update.
        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = hero.pos;
            }
        }



        return result;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Statistics.duwang3 = 999;

        Badges.validateBrandokill();

        Dungeon.level.drop( new Diocoffin(), pos ).sprite.drop( pos );

        yell( Messages.get(this, "6"));

        Sample.INSTANCE.play(Assets.Sounds.NANI);
        Sample.INSTANCE.play(Assets.Sounds.SPW5);

        GameScene.bossSlain();

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Zombie) {
                mob.die( cause );
            }
        }

        Music.INSTANCE.end();

    }

    @Override
    public int damageRoll() {
        int ice = 1;
        if (this.buff(IceBlow.class) != null) ice = 3;
        return enemy == hero || enemy instanceof Willa2 ?
                Random.NormalIntRange( 2 * ice, 12 * ice) :
                Random.NormalIntRange( 2, 5 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    private boolean FireBlast() {
        boolean terrainAffected = false;
        HashSet<Char> affected = new HashSet<>();
        //delay fire on a rooted hero

        for (int i : targetedCells) {
            Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
            //shoot beams

            for (int p : b.path) {
                sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                Char ch = Actor.findChar(p);
                if (ch != null && (ch.alignment != alignment || ch instanceof Zombie || ch instanceof Zombiedog)) {
                    affected.add(ch);
                }
                if (Dungeon.level.flamable[p]) {
                    Dungeon.level.destroy(p);
                    GameScene.updateMap(p);
                    terrainAffected = true;
                }
            }
        }
        if (terrainAffected) {
            Dungeon.observe();
        }

        for (Char ch : affected) {
            if(ch instanceof SpeedWagon) {

            } else {
                ch.damage(Random.NormalIntRange(20, 25), new Diobrando2.Blast());
            }
            if (Dungeon.level.heroFOV[pos]) {
                ch.sprite.flash();
                CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            }
            if (!ch.isAlive() && ch == hero) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "d"));
            }
        }

        targetedCells.clear();


        if (blastcooldown <= 0){

            int beams = 2;
            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++){

                int targetPos = hero.pos;
                if (i != 0){
                    do {
                        targetPos = hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                    } while (Dungeon.level.trueDistance(pos, hero.pos)
                            > Dungeon.level.trueDistance(pos, targetPos));
                }
                targetedCells.add(targetPos);
                Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                affectedCells.addAll(b.path);
            }

            //remove one beam if multiple shots would cause every cell next to the hero to be targeted
            boolean allAdjTargeted = true;
            for (int i : PathFinder.NEIGHBOURS9){
                if (!affectedCells.contains(hero.pos + i) && Dungeon.level.passable[hero.pos + i]){
                    allAdjTargeted = false;
                    break;
                }
            }
            if (allAdjTargeted){
                targetedCells.remove(targetedCells.size()-1);
            }
            for (int i : targetedCells){
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                for (int p : b.path){
                    sprite.parent.addToBack(new TargetedCell(p, 0xFF00FF));
                    affectedCells.add(p);
                }
            }

            //don't want to overly punish players with slow move or attack speed
            hero.interrupt();
            blastcooldown = 4;

            spend(GameMath.gate(TICK, hero.cooldown(), 2*TICK));
            return true;

        }
        return false;
    }




    public void summonRats(int amount) {


        while (amount > 0) {

            ArrayList<Integer> respawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = this.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add(p);
                }
                int index = Random.index(respawnPoints);
            }

            if (respawnPoints.size() > 0) {
                int type = Random.Int(40);
                Mob mob;

                if (type <= 30) {
                    mob = new Zombie();
                } else if (type <= 38) {
                    mob = new Zombie();
                } else {
                    mob = new Zombie();
                }

                mob.pos = Random.element(respawnPoints);
                GameScene.add(mob, 1);
                mob.state = mob.HUNTING;
                Dungeon.level.occupyCell(mob);
            }


            amount--;
        }


    }


}
