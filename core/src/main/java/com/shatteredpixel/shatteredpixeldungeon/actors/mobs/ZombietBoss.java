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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.D4C;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Willabuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Zombiet2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ZombietBoss extends Mob {
    private boolean chainsUsed = true;

    {
        spriteClass = Zombiet2Sprite.class;
        HP = HT = 360;
        defenseSkill = 25;
        viewDistance = Light.DISTANCE;

        EXP = 12;
        maxLvl = 25;
        HUNTING = new ZombietBoss.Hunting();

        properties.add(Property.BOSS);
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);

    }
    private int cooldown = 6;

    @Override
    public boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        if (cooldown > 0) cooldown--;

        if (cooldown == 4) {
            chainsUsed = false;
        }

        if (Dungeon.level.distance(hero.pos, pos) < 4 && cooldown < 1 && this.state != SLEEPING) {
            cooldown = 20;
            Buff.affect(this, Adrenaline.class, 4f);
            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
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
        if (enemy == hero) {
            hero.interrupt();
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
        return Random.NormalIntRange( 40, 50 );
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 10);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Buff.affect(hero, Willabuff.class);
        yell(Messages.get(this, "n4"));

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        Item prize = Random.oneOf(
                new Kingt().quantity(1),
                new StoneOfAdvanceguard().quantity(1),
                new Kings().quantity(1),
                new Kingm().quantity(1),
                new Kingw().quantity(1),
                new Willa().quantity(1),
                new PotionOfShielding().quantity(2),
                new Kingc().quantity(1)
        );

        Dungeon.level.drop(prize, pos).sprite.drop(pos);

        GameScene.bossSlain();

        Music.INSTANCE.end();

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

}