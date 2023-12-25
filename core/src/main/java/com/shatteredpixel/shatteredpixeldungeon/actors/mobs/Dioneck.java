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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.IceBlow;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DioneckSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DioneckSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Dioneck extends Mob {

    {
        spriteClass = DioneckSprite.class;

        HP = HT = 200;

        defenseSkill = 8;
        EXP = 0;
        maxLvl = -9;
        HUNTING = new Dioneck.Hunting();

        immunities.add( Chill.class );
        immunities.add( Frost.class );

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }
    private ArrayList<Integer> targetedCells = new ArrayList<>();

    public int  Phase = 0;
    private float leapCooldown = 3;
    private int blastcooldown = 14;

    private int spw = 0;

    private int barriercooldown = 9;

    private int zcooldown = 99999;
    private int charge = 0; // 2이 될경우 강화 사격

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            switch(Dungeon.hero.heroClass){
                case WARRIOR:
                    this.yell(Messages.get(this, "1"));
                    GLog.p(Messages.get(this, "10"));
                    Sample.INSTANCE.play(Assets.Sounds.DIO1);
                    Statistics.duwang3 = 5;
                    break;
                case MAGE:
                    this.yell(Messages.get(this, "12"));
                    Sample.INSTANCE.play(Assets.Sounds.DIO1);
                    Statistics.duwang3 = 5;
                    break;
                case ROGUE:
                    this.yell(Messages.get(this, "13"));
                    Sample.INSTANCE.play(Assets.Sounds.DIO1);
                    Statistics.duwang3 = 5;
                    break;
                case DUELIST:
                    this.yell(Messages.get(this, "14"));
                    GLog.p(Messages.get(Val.class, "9"));
                    Sample.INSTANCE.play(Assets.Sounds.DIO1);
                    Sample.INSTANCE.play(Assets.Sounds.HEALTH_CRITICAL);
                    GameScene.flash(0xFF0000);
                    Camera.main.shake(9, 0.5f);
                    Buff.affect(Dungeon.hero, Adrenaline.class, 1f);
                    Statistics.duwang3 = 5;
                    break;
                case HUNTRESS:
                    this.yell(Messages.get(this, "15"));
                    Sample.INSTANCE.play(Assets.Sounds.DIO1);
                    Statistics.duwang3 = 5;
                    break;
            }

        }
    }

    @Override
    protected boolean getCloser(int target) {

        target = Dungeon.hero.pos;

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
            Sample.INSTANCE.play(Assets.Sounds.DIO6);
            Sample.INSTANCE.play( Assets.Sounds.BLAST );
            Camera.main.shake(12, 0.5f);
            summonRats(2);
            yell(Messages.get(this, "t"));
            zcooldown = 26;
        }

        if (Phase==1 && HP < 31) {
            Phase = 2;
            GameScene.flash( 0xFFCC00 );
            GLog.n(Messages.get(this, "j"));
            GLog.p(Messages.get(this, "t5"));
            Music.INSTANCE.play(Assets.Music.JONATHAN, true);
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
            this.sprite.add(CharSprite.State.MARKED);
            this.sprite.add(CharSprite.State.BURNING);
        }
    }


    private boolean UseAbility() {

        if (barriercooldown <= 0) {
            Buff.affect(this, IceBlow.class, 1f);
            CellEmitter.get( this.pos ).burst( MagicMissile.MagicParticle.FACTORY, 12 );
            Sample.INSTANCE.play(Assets.Sounds.DIO1);
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
            barriercooldown = 14;
            return true;
        }

        if (zcooldown <= 0) {
            if(spw == 2){
                GLog.n(Messages.get(SpeedWagon.class, "spw2"));
                spw++;
            }
            yell( Messages.get(this, "zomb") );
            CellEmitter.get( this.pos ).burst( MagicMissile.MagicParticle.FACTORY, 12 );
            Sample.INSTANCE.play(Assets.Sounds.DIO6);
            summonRats(Random.Int(2, 3));
            new Flare( 5, 32 ).color( 0xFF0000, true ).show( this.sprite, 2f );
            zcooldown = 30;

            return true;
        }

        if (FireBlast()){
            sprite.showStatus(CharSprite.WARNING, Messages.get(Dioneck.class, "u"));
            Sample.INSTANCE.play(Assets.Sounds.DIO4);
            return true;
        }

        return false;
    }


    @Override
    public int attackProc(Char enemy, int damage) {

        if (this.buff(IceBlow.class) != null) {

            GameScene.flash(0x33CCFF);
            Sample.INSTANCE.play(Assets.Sounds.DIO3);
            new ChillingTrap().set(enemy.pos).activate();
            Buff.affect(enemy, Frost.class, Frost.DURATION);
            yell( Messages.get(this, "armad") );
            Buff.detach(this, IceBlow.class);

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "d"));
            }
        }

        damage = super.attackProc(enemy, damage);

        return damage;
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        damage = super.defenseProc( enemy, damage );
        if (this.buff(IceBlow.class) != null) {

            if(enemy == hero) {
                for (int i : PathFinder.NEIGHBOURS8){
                    int cell = enemy.pos+i;
                    ScrollOfTeleportation.appear(this, cell);
                }
                enemy.damage(Random.NormalIntRange(15, 20), this);
            }
            GameScene.flash(0x33CCFF);
            Sample.INSTANCE.play(Assets.Sounds.DIO3);
            new ChillingTrap().set(enemy.pos).activate();
            Buff.affect(enemy, Frost.class, Frost.DURATION);
            yell( Messages.get(this, "armad") );
            Buff.detach(this, IceBlow.class);
            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "d"));
            }
        }
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

    private static final String BARRIER_CD  = "barriercooldown";
    private static final String Z_CD  = "zcooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( BLAST_CD, blastcooldown );
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(LEAP_POS, leapPos);
        bundle.put(LEAP_CD, leapCooldown);
        bundle.put( SKILLCD, charge );
        bundle.put( PHASE, Phase );
        bundle.put( BARRIER_CD, barriercooldown );
        bundle.put( Z_CD, zcooldown );

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
        leapPos = bundle.getInt(LEAP_POS);
        leapCooldown = bundle.getFloat(LEAP_CD);
        charge = bundle.getInt(SKILLCD);
        Phase = bundle.getInt(PHASE);
        barriercooldown = bundle.getInt(BARRIER_CD);
        zcooldown = bundle.getInt(Z_CD);

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

        if (barriercooldown == 1)  {

            if(blastcooldown < 3){
                blastcooldown += 4;
            }

            GLog.w(Messages.get(this, "i"));
            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);
            Dungeon.hero.interrupt();
            if(spw == 0){
                GLog.n(Messages.get(SpeedWagon.class, "spw1"));
                spw++;
            }
        }

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        {
            if (UseAbility()) return true;
        }

        if (blastcooldown > 0) blastcooldown--;
        if (barriercooldown > 0) barriercooldown--;
        if (zcooldown > 0) zcooldown--;


        AiState lastState = state;
        boolean result = super.act();
        if (paralysed <= 0) leapCooldown --;

        //if state changed from wandering to hunting, we haven't acted yet, don't update.
        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = Dungeon.hero.pos;
            }
        }



        return result;
    }

    private int leapPos = -1;


    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if (leapPos != -1){
                charge = 0;
                leapCooldown = Random.NormalIntRange(5, 9);

                if(spw == 1){
                    GLog.n(Messages.get(SpeedWagon.class, "spw0"));
                    spw++;
                }

                switch (Random.Int( 2 )) {
                    case 0:
                        Sample.INSTANCE.play( Assets.Sounds.DIO2);
                        sprite.showStatus(CharSprite.WARNING, Messages.get(Dioneck.class, "3"));
                        break;
                    case 1:
                        Sample.INSTANCE.play( Assets.Sounds.DIO5);
                        sprite.showStatus(CharSprite.WARNING, Messages.get(Dioneck.class, "4"));
                        break;
                }

                Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

                //check if leap pos is not obstructed by terrain
                if (rooted || b.collisionPos != leapPos){
                    leapPos = -1;
                    return true;
                }

                final Char leapVictim = Actor.findChar(leapPos);
                final int endPos;

                //ensure there is somewhere to land after leaping
                if (leapVictim != null){
                    int bouncepos = -1;
                    for (int i : PathFinder.NEIGHBOURS8){
                        if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
                                && Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
                            bouncepos = leapPos+i;
                        }
                    }
                    if (bouncepos == -1) {
                        leapPos = -1;
                        return true;
                    } else {
                        endPos = bouncepos;
                    }
                } else {
                    endPos = leapPos;
                }

                //do leap
                sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos] || Dungeon.level.heroFOV[endPos];
                sprite.jump(pos, leapPos, new Callback() {
                    @Override
                    public void call() {

                        if (leapVictim != null && alignment != leapVictim.alignment){
                            Buff.affect(leapVictim, Bleeding.class).set(0.75f*damageRoll());
                            leapVictim.sprite.flash();
                            Sample.INSTANCE.play(Assets.Sounds.HIT);
                        }

                        if (endPos != leapPos){
                            Actor.addDelayed(new Pushing(Dioneck.this, leapPos, endPos), -1);
                        }

                        pos = endPos;
                        leapPos = -1;
                        sprite.idle();
                        Dungeon.level.occupyCell(Dioneck.this);
                        next();
                    }
                });
                return false;
            }

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination( Dioneck.this );
                    return true;
                }

                if (leapCooldown <= 0 && enemyInFOV && !rooted
                        && Dungeon.level.distance(pos, enemy.pos) >= 3) {

                    int targetPos = enemy.pos;
                    if (lastEnemyPos != enemy.pos){
                        int closestIdx = 0;
                        for (int i = 1; i < PathFinder.CIRCLE8.length; i++){
                            if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos)
                                    < Dungeon.level.trueDistance(lastEnemyPos, enemy.pos)){
                                closestIdx = i;
                            }
                        }
                        targetPos = enemy.pos;
                    }

                    Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    //try aiming directly at hero if aiming near them doesn't work
                    if (b.collisionPos != targetPos && targetPos != enemy.pos){
                        targetPos = enemy.pos;
                        b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    }
                    if (b.collisionPos == targetPos){
                        //get ready to leap
                        leapPos = targetPos;
                        //don't want to overly punish players with slow move or attack speed
                        spend(GameMath.gate(TICK, enemy.cooldown(), 3*TICK));
                        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){

                            sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF00FF));
                            ((DioneckSprite)sprite).leapPrep( leapPos );
                            Dungeon.hero.interrupt();
                        }
                        return true;
                    }
                }

                int oldPos = pos;
                if (target != -1 && getCloser( target )) {

                    spend( 1 / speed() );
                    return moveSprite( oldPos,  pos );

                } else {
                    spend( TICK );
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination( Dioneck.this );
                    }
                    return true;
                }
            }
        }

    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Statistics.duwang3 = 999;

        Badges.validateBrandokill();

        Dungeon.level.drop( new Smask(), pos ).sprite.drop( pos );

        yell( Messages.get(this, "6") );
        GLog.n(Messages.get(this, "t6"));

        Sample.INSTANCE.play(Assets.Sounds.NANI);
        Sample.INSTANCE.play(Assets.Sounds.SPW5);

        GameScene.bossSlain();

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Zombie || mob instanceof Zombiedog) {
                mob.die( cause );
            }
        }

        Music.INSTANCE.end();

    }

    @Override
    public int damageRoll() {
        int ice = 1;
        if (this.buff(IceBlow.class) != null) ice = 3;
        return enemy == Dungeon.hero || enemy instanceof Willa2 ?
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

    public class Blast { }

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
                ch.damage(Random.NormalIntRange(20, 25), new Dioneck.Blast());
            }
            if (Dungeon.level.heroFOV[pos]) {
                ch.sprite.flash();
                CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            }
            if (!ch.isAlive() && ch == Dungeon.hero) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "d"));
            }
        }

        targetedCells.clear();


        if (blastcooldown <= 0){

            int beams = 2;
            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++){

                int targetPos = Dungeon.hero.pos;
                if (i != 0){
                    do {
                        targetPos = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                    } while (Dungeon.level.trueDistance(pos, Dungeon.hero.pos)
                            > Dungeon.level.trueDistance(pos, targetPos));
                }
                targetedCells.add(targetPos);
                Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                affectedCells.addAll(b.path);
            }

            //remove one beam if multiple shots would cause every cell next to the hero to be targeted
            boolean allAdjTargeted = true;
            for (int i : PathFinder.NEIGHBOURS9){
                if (!affectedCells.contains(Dungeon.hero.pos + i) && Dungeon.level.passable[Dungeon.hero.pos + i]){
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
            Dungeon.hero.interrupt();
            blastcooldown = 8;

            spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2*TICK));
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
                    mob = new Zombiedog();
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
