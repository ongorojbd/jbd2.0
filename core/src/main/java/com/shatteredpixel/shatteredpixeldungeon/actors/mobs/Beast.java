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
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Wgas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DoppioDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EmporioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Pucci4Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Beast extends Mob {

    {
        HP = HT = 1000;

        EXP = 0;
        maxLvl = 30;

        defenseSkill = 23;
        spriteClass = Pucci4Sprite.class;
        baseSpeed = 12f;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);

        FLEEING = new Fleeing();
        HUNTING = new Hunting();
    }

    public int  Phase = 0;

    private int leapPos = -1;
    private float leapCooldown = 3;

    private int lastEnemyPos = -1;

    @Override
    public int damageRoll() {
        int dmg;
        dmg = Random.NormalIntRange( 25, 45 );
        return dmg;
    }

    @Override
    public int attackSkill( Char target ) {
        return 45;
    }


    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 150){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 150;
        }
        BossHealthBar.assignBoss(this);
        int preHP = HP;
        int dmgTaken = preHP - HP;

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmgTaken);

        super.damage(dmg, src);

        if (Phase==0 && HP < 450) {
            Phase = 1;
            GameScene.flash( 0xFFCCFF );
            new Fadeleaf().activate(this);
            state = HUNTING;
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new Pucci4Sprite()},
                    new String[]{"엔리코 푸치"},
                    new String[]{
                            Messages.get(Beast.class, "q")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE
                    }
            );
        }
        else if (Phase==1 && HP < 151) {
            Phase = 2;
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new EmporioSprite(), new Pucci4Sprite()},
                    new String[]{"엠포리오 아르니뇨", "엔리코 푸치"},
                    new String[]{
                            Messages.get(Beast.class, "n6"),
                            Messages.get(Beast.class, "n7")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );
            Music.INSTANCE.play(Assets.Music.DIOLOWHP, true);
        }
    }


    @Override
    public boolean act() {

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Music.INSTANCE.play(Assets.Music.LABS_BOSS, true);

        }

        if (Phase == 0) {

            AiState lastState = state;
            boolean result = super.act();
            if (leapCooldown > 0) leapCooldown--;

            //if state changed from wandering to hunting, we haven't acted yet, don't update.
            if (!(lastState == WANDERING && state == HUNTING)) {
                if (enemy != null) {
                    lastEnemyPos = enemy.pos;
                } else {
                    lastEnemyPos = hero.pos;
                }
            }

        }

        if (Phase == 2) {
            state = FLEEING;
            baseSpeed = 0.8f;
        }

        if (state != SLEEPING) {
            Dungeon.level.seal();
        }

        return super.act();
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return super.canAttack(enemy);
    }
    private Boolean hasadjacent;
    @Override
    public int attackProc(Char enemy, int damage) {
        hasadjacent=false;
        damage = super.attackProc(enemy, damage);

        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Bleeding.class).set(damage/0.75f);
            if (Phase == 0) {
                state = FLEEING;
                summonRats(Random.Int(2, 5));
            }
            else {
                Ballistica route = new Ballistica(this.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                int cell = route.collisionPos;

                for (int i : PathFinder.NEIGHBOURS8) {
                    hasadjacent = true;
                    Char mob = Actor.findChar(this.pos + i);
                    if (mob != null) {
                        if (mob.pos == this.pos + i) {
                            new Fadeleaf().activate(this);
                        }
                    }
                }
            }
        }

        return damage;
    }

    public void summonRats(int amount) {


        while (amount > 0) {

            ArrayList<Integer> respawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS2.length; i++) {
                int p = this.pos + PathFinder.NEIGHBOURS2[i];
                if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add(p);
                }
                int index = Random.index(respawnPoints);
            }

            if (respawnPoints.size() > 0) {
                int type = Random.Int(40);
                Mob mob;

                if (type <= 30) {
                    mob = new RipperDemon();
                } else if (type <= 38) {
                    mob = new RipperDemon();
                } else {
                    mob = new RipperDemon();
                }


                mob.pos = Random.element(respawnPoints);
                GameScene.add(mob, 1);
                mob.state = mob.HUNTING;
                Dungeon.level.occupyCell(mob);
            }


            amount--;
        }


    }

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

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof RipperDemon) {
                mob.die( cause );
            }
        }

        SPDSettings.addSpecialcoin(4);

        Camera.main.shake( 3, 1f );
        WndDialogueWithPic.dialogue(
                new CharSprite[]{new Pucci4Sprite(), new EmporioSprite()},
                new String[]{"엔리코 푸치", "엠포리오 아르니뇨"},
                new String[]{
                        Messages.get(Beast.class, "n8"),
                        Messages.get(Beast.class, "n9")
                },
                new byte[]{
                        WndDialogueWithPic.DIE,
                        WndDialogueWithPic.IDLE
                }
        );

        Statistics.duwang = 34;
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

    private class Fleeing extends Mob.Fleeing {


        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            if(Phase == 2){
                GameScene.add(Blob.seed(pos, 60, Wgas.class));
            }

            if (Random.Int(2) == 0) {

                int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                if (!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos])
                        || (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[newPos])
                        || Actor.findChar(newPos) != null) return super.act(enemyInFOV, justAlerted);
                else {
                    sprite.move(pos, newPos);
                    pos = newPos;
                 }
            } else if (Random.Int(5) == 2) {
                if(Phase != 2){
                state = HUNTING;
                }
            }
            return super.act(enemyInFOV, justAlerted);
        }


    }


    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            if (Phase == 1) {
                if (leapPos != -1) {
                    Sample.INSTANCE.play(Assets.Sounds.P1);

                    leapCooldown = 4;
                    Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

                    //check if leap pos is not obstructed by terrain
                    if (rooted || b.collisionPos != leapPos) {
                        leapPos = -1;
                        return true;
                    }

                    final Char leapVictim = Actor.findChar(leapPos);
                    final int endPos;

                    //ensure there is somewhere to land after leaping
                    if (leapVictim != null) {
                        int bouncepos = -1;
                        for (int i : PathFinder.NEIGHBOURS8) {
                            if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos + i) < Dungeon.level.trueDistance(pos, bouncepos))
                                    && Actor.findChar(leapPos + i) == null && Dungeon.level.passable[leapPos + i]) {
                                bouncepos = leapPos + i;
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

                            if (leapVictim != null && alignment != leapVictim.alignment) {
                                Buff.affect(leapVictim, Bleeding.class).set(0.4f * damageRoll());
                                leapVictim.sprite.flash();
                                Sample.INSTANCE.play(Assets.Sounds.HIT);
                            }

                            if (endPos != leapPos) {
                                Actor.addDelayed(new Pushing(Beast.this, leapPos, endPos), -1);
                            }

                            pos = endPos;
                            leapPos = -1;
                            sprite.idle();
                            Dungeon.level.occupyCell(Beast.this);
                            next();
                        }
                    });
                    return false;
                }

                enemySeen = enemyInFOV;
                if (enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy)) {

                    return doAttack(enemy);

                } else {

                    if (enemyInFOV) {
                        target = enemy.pos;
                    } else if (enemy == null) {
                        state = WANDERING;
                        target = Dungeon.level.randomDestination(Beast.this);
                        return true;
                    }

                    if (enemyInFOV && !rooted
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
                        if (b.collisionPos == targetPos) {
                            //get ready to leap
                            leapPos = targetPos;
                            //don't want to overly punish players with slow move or attack speed
                            spend(GameMath.gate(TICK, enemy.cooldown(), 3 * TICK));
                            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]) {
                                sprite.showStatus(CharSprite.WARNING, Messages.get(Beast.this, "leap"));
                                sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF00FF));
                                ((Pucci4Sprite)sprite).leapPrep( leapPos );
                                hero.interrupt();
                            }
                            return true;
                        }
                    }

                    int oldPos = pos;
                    if (target != -1 && getCloser(target)) {

                        spend(1 / speed());
                        return moveSprite(oldPos, pos);

                    } else {
                        spend(TICK);
                        if (!enemyInFOV) {
                            sprite.showLost();
                            state = WANDERING;
                            target = Dungeon.level.randomDestination(Beast.this);
                        }
                        return true;
                    }
                }
            }
            else return super.act(enemyInFOV, justAlerted);

        }



    }
}