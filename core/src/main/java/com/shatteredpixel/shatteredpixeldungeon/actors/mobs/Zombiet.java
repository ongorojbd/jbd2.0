/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ZombietParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.DioLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombietSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Zombiet extends Mob {
    private boolean chainsUsed = false;

    {
        spriteClass = ZombietSprite.class;
        HP = HT = 25;
        defenseSkill = 5;

        EXP = 4;
        maxLvl = 9;
        HUNTING = new Zombiet.Hunting();

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);

        lootChance = 0.2f;
    }
    private int cooldown = 0;

    @Override
    public boolean act() {

        if (cooldown > 0) cooldown--;

        if (Dungeon.level.distance(Dungeon.hero.pos, pos) < 4 && cooldown < 1 && this.state != SLEEPING) {
            cooldown = 20;

            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof Slime) mob.beckon(this.pos);
            }

            Buff.affect(Dungeon.hero, Zombiet.LeaderArena.class).setup(this.pos);


        }
        return super.act();
    }


    private boolean chain(int target){
        if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
            return false;

        Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

        if (chain.collisionPos != enemy.pos
                || chain.path.size() < 2
                || Dungeon.level.pit[chain.path.get(1)])
            return false;
        else {
            int newPos = -1;
            for (int i : chain.subPath(1, chain.dist)){
                if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
                    newPos = i;
                    break;
                }
            }

            if (newPos == -1){
                return false;
            } else {
                final int newPosFinal = newPos;
                this.target = newPos;

                if (sprite.visible || enemy.sprite.visible) {
                    yell(Messages.get(this, "scorpion"));
                    new Item().throwSound();
                    Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                    Sample.INSTANCE.play(Assets.Sounds.SHEEP, 1f, 3.2f);
                    sprite.parent.add(new Chains(sprite.center(),
                            enemy.sprite.destinationCenter(),
                            Effects.Type.RIPPLE,
                            new Callback() {
                                public void call() {
                                    Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal, new Callback() {
                                        public void call() {
                                            pullEnemy(enemy, newPosFinal);
                                        }
                                    }), -1);
                                    next();
                                }
                            }));
                } else {
                    pullEnemy(enemy, newPos);
                }
            }
        }
        chainsUsed = true;
        return true;
    }

    private void pullEnemy( Char enemy, int pullPos ){
        enemy.pos = pullPos;
        enemy.sprite.place(pullPos);
        Dungeon.level.occupyCell(enemy);
        Buff.prolong(enemy, StoneOfAggression.Aggression.class, 4f);
        if (enemy == Dungeon.hero) {
            Dungeon.hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }

    private final String CHAINSUSED = "chainsused";
    private static final String COOLDOWN = "COOLDOWN";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHAINSUSED, chainsUsed);
        bundle.put(COOLDOWN, cooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        chainsUsed = bundle.getBoolean(CHAINSUSED);
        cooldown = bundle.getInt( COOLDOWN );
    }

    @Override
    public int damageRoll() {
        return enemy == Dungeon.hero ?
                Random.NormalIntRange( 2, 5 ) :
                Random.NormalIntRange( 4, 10 );
    }

    @Override
    public void damage(int dmg, Object src) {
        float scaleFactor = AscensionChallenge.statModifier(this);
        int scaledDmg = Math.round(dmg/scaleFactor);
        if (scaledDmg >= 5){
            //takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
            scaledDmg = 4 + (int)(Math.sqrt(8*(scaledDmg - 4) + 1) - 1)/2;
        }
        dmg = (int)(scaledDmg*AscensionChallenge.statModifier(this));
        super.damage(dmg, src);
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        if (Random.Int( 10 ) == 0) {
            Dungeon.level.drop( new ChaosCatalyst().identify(), pos ).sprite.drop( pos );
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;

            if (!chainsUsed
                    && enemyInFOV
                    && !isCharmedBy( enemy )
                    && !canAttack( enemy )
                    && Dungeon.level.distance( pos, enemy.pos ) < 5


                    && chain(enemy.pos)){
                return !(sprite.visible || enemy.sprite.visible);
            } else {
                return super.act( enemyInFOV, justAlerted );
            }

        }
    }

    public static class LeaderArena extends Buff {

        private ArrayList<Integer> arenaPositions = new ArrayList<>();
        private ArrayList<Emitter> arenaEmitters = new ArrayList<>();

        private static final float DURATION = 20;
        int left = 0;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0f, 1f);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - left) / DURATION);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(left);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", left);
        }

        public void setup(int pos){

            int dist;
            if (Dungeon.level instanceof DioLevel){
                dist = 2; //smaller boss arenas
            } else {

                boolean[] visibleCells = new boolean[Dungeon.level.length()];
                Point c = Dungeon.level.cellToPoint(pos);
                ShadowCaster.castShadow(c.x, c.y, visibleCells, Dungeon.level.losBlocking, 8);
                int count=0;
                for (boolean b : visibleCells){
                    if (b) count++;
                }

                if (count < 30){
                    dist = 3;
                } else if (count >= 100) {
                    dist = 5;
                } else {
                    dist = 4;
                }
            }

            PathFinder.buildDistanceMap( pos, BArray.or( Dungeon.level.passable, Dungeon.level.avoid, null ), dist );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE && !arenaPositions.contains(i)) {
                    arenaPositions.add(i);
                }
            }
            if (target != null) {
                fx(false);
                fx(true);
            }

            left = (int) DURATION;

        }

        @Override
        public boolean act() {

            if (!arenaPositions.contains(target.pos)){
                detach();
            }

            left--;
            BuffIndicator.refreshHero();
            if (left <= 0){
                detach();
            }

            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on){
                for (int i : arenaPositions){
                    Emitter e = CellEmitter.get(i);
                    e.pour(ZombietParticle.FACTORY, 0.05f);
                    arenaEmitters.add(e);
                }
            } else {
                for (Emitter e : arenaEmitters){
                    e.on = false;
                }
                arenaEmitters.clear();
            }
        }

        private static final String ARENA_POSITIONS = "arena_positions";
        private static final String LEFT = "left";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);

            int[] values = new int[arenaPositions.size()];
            for (int i = 0; i < values.length; i ++)
                values[i] = arenaPositions.get(i);
            bundle.put(ARENA_POSITIONS, values);

            bundle.put(LEFT, left);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);

            int[] values = bundle.getIntArray( ARENA_POSITIONS );
            for (int value : values) {
                arenaPositions.add(value);
            }

            left = bundle.getInt(LEFT);
        }
    }

    @Override
    public float lootChance(){
        //each drop makes future drops 1/3 as likely
        // so loot chance looks like: 1/5, 1/15, 1/45, 1/135, etc.
        return super.lootChance() * (float)Math.pow(1/3f, Dungeon.LimitedDrops.SLIME_WEP.count);
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.SLIME_WEP.count++;
        Generator.Category c = Generator.Category.WEP_T2;
        MeleeWeapon w = (MeleeWeapon)Generator.randomUsingDefaults(Generator.Category.WEP_T2);
        w.level(0);
        return w;
    }

}