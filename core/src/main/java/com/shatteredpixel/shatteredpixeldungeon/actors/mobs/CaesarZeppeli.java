/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CursedBlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ThunderImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillcSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CaesarZeppeli extends DirectableAlly {

    {
        spriteClass = WillcSprite.class;

        HP = HT = 100;
        defenseSkill = 8;
        viewDistance = 8;

        alignment = Alignment.ALLY;
        intelligentAlly = true;

        // Caesar is a human ripple user
        immunities.add(AllyBuff.class);
        immunities.add(Paralysis.class);
    }

    // Caesar's special abilities
    private int bubbleLauncherCooldown = 0;
    private int bubbleCutterCooldown = 0;
    private int bubbleLensCooldown = 0;
    private int hamonKickCooldown = 0;
    private boolean catStanceActive = false;
    private int healthState = 0;

    // Track bubble projectiles for combo attacks
    private ArrayList<Integer> activeBubbles = new ArrayList<>();

    @Override
    public String description() {
        String desc = super.description();
        if (catStanceActive) {
            desc += "\n\n" + Messages.get(this, "cat_stance_desc");
        }
        desc += "\n\n" + Messages.get(this, "stats", HP, HT);
        return desc;
    }

    @Override
    protected boolean act() {
        // Cooldown management
        if (bubbleLauncherCooldown > 0) bubbleLauncherCooldown--;
        if (bubbleCutterCooldown > 0) bubbleCutterCooldown--;
        if (bubbleLensCooldown > 0) bubbleLensCooldown--;
        if (hamonKickCooldown > 0) hamonKickCooldown--;

        if (Dungeon.depth >= 35 && this.buff(ThunderImbue.class) == null) {
            Buff.affect(this, ThunderImbue.class);
        }

        // Cat Stance activation when HP is low
        if (HP < HT / 3 && !catStanceActive) {
            activateCatStance();
        }

        // Simplified combat pattern
        if (enemy != null && enemy.isAlive() && canAttack(enemy)) {
            int dist = Dungeon.level.distance(pos, enemy.pos);
            if (dist > 1) {
                if (bubbleLauncherCooldown == 0) {
                    useBubbleLauncher(enemy);
                    return true;
                }
            } else {
                if (hamonKickCooldown == 0 && Random.Int(4) == 0) {
                    useHamonKick(enemy);
                    return true;
                }
            }
        }

        // Support abilities for hero
        if (Dungeon.hero.HP < Dungeon.hero.HT / 2 && Random.Int(50) == 0) {
            useHamonControl();
        }

        return super.act();
    }

    public boolean skill1() {
        int oldPos = pos;
        int newPos = hero.pos;

        PathFinder.buildDistanceMap(hero.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (PathFinder.distance[pos] == Integer.MAX_VALUE) {
            return true;
        }

        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "skill1"));
        Sample.INSTANCE.play(Assets.Sounds.TENDENCY2);

        pos = newPos;
        hero.pos = oldPos;
        ScrollOfTeleportation.appear(this, newPos);
        ScrollOfTeleportation.appear(hero, oldPos);

        Buff.affect(hero, Invulnerability.class, 5f);
        Buff.affect(this, Invulnerability.class, 5f);

        Dungeon.observe();
        GameScene.updateFog();

        return true;
    }

    public void skill2() {

        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "skill2"));

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                if (!mob.properties().contains(Char.Property.BOSS) && !mob.properties().contains(Char.Property.MINIBOSS) && mob.properties().contains(Char.Property.UNDEAD)) {
                    Buff.affect(mob, SoulMark.class, 10f);
                    Buff.affect(mob, Paralysis.class, 5f);
                    mob.damage(mob.HT / 5, this);
                } else if (mob.properties().contains(Char.Property.BOSS)) {
                    Buff.affect(mob, SoulMark.class, 10f);
                    Buff.affect(mob, Paralysis.class, 2f);
                }
            }
        }

        GameScene.flash(0x99FFFF);

        Sample.INSTANCE.play(Assets.Sounds.CE6);
        Sample.INSTANCE.play(Assets.Sounds.BURNING);

    }

    public void skill3() {
        Ballistica aim;
        int x = pos % Dungeon.level.width();
        int y = pos / Dungeon.level.width();

        Sample.INSTANCE.play(Assets.Sounds.CE1);
        Sample.INSTANCE.play(Assets.Sounds.BURNING);

        if (Math.max(x, Dungeon.level.width() - x) >= Math.max(y, Dungeon.level.height() - y)) {
            if (x > Dungeon.level.width() / 2) {
                aim = new Ballistica(pos, pos - 1, Ballistica.WONT_STOP);
            } else {
                aim = new Ballistica(pos, pos + 1, Ballistica.WONT_STOP);
            }
        } else {
            if (y > Dungeon.level.height() / 2) {
                aim = new Ballistica(pos, pos - Dungeon.level.width(), Ballistica.WONT_STOP);
            } else {
                aim = new Ballistica(pos, pos + Dungeon.level.width(), Ballistica.WONT_STOP);
            }
        }

        int aoeSize = 8;

        int projectileProps = Ballistica.STOP_SOLID | Ballistica.STOP_TARGET;

        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);

        for (Ballistica ray : aoe.outerRays) {
            ((MagicMissile) this.sprite.parent.recycle(MagicMissile.class)).reset(
                    MagicMissile.RAINBOW,
                    this.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        ((MagicMissile) this.sprite.parent.recycle(MagicMissile.class)).reset(
                MagicMissile.RAINBOW,
                this.sprite,
                aim.path.get(Math.min(aoeSize / 2, aim.path.size() - 1)),
                new Callback() {
                    @Override
                    public void call() {

                        int charsHit = 0;
                        for (int cell : aoe.cells) {

                            //### Deal damage ###
                            Char mob = Actor.findChar(cell);

                            if (mob != null && mob.alignment != Char.Alignment.ALLY) {
                                if (Char.hasProp(mob, Char.Property.BOSS) || Char.hasProp(mob, Char.Property.MINIBOSS)) {
                                    mob.damage(HT / 12, this);
                                } else {
                                    mob.damage(HT / 2, this);
                                }

                                charsHit++;
                            }

                            if (mob != null && mob != hero) {
                                if (mob.alignment != Char.Alignment.ALLY) {
                                    Ballistica aim = new Ballistica(pos, mob.pos, Ballistica.WONT_STOP);
                                    int knockback = aoeSize + 1 - (int) Dungeon.level.trueDistance(pos, mob.pos);
                                    knockback *= 3;
                                    WandOfBlastWave.throwChar(mob,
                                            new Ballistica(mob.pos, aim.collisionPos, Ballistica.MAGIC_BOLT),
                                            knockback,
                                            true,
                                            true,
                                            this);
                                }
                            }
                        }
                    }
                }
        );


    }

    private void activateCatStance() {
        catStanceActive = true;
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "cat_stance"));
        Buff.affect(this, Barkskin.class).set(5, 1);
    }

    // Bubble Launcher - basic ranged attack
    private void useBubbleLauncher(Char target) {
        sprite.zap(target.pos);

        // Launch multiple bubbles
        for (int i = 0; i < 3; i++) {
            // Create bubble visual effect
            MagicMissile.boltFromChar(sprite.parent,
                    MagicMissile.FROST,
                    sprite,
                    target.pos,
                    null);
        }

        // Deal damage after short delay
        int damage = Random.NormalIntRange(3, 8) * 3; // 3 bubbles worth
        target.damage(damage, this);

        // Apply weakness to undead targets
        if (target.properties().contains(Property.UNDEAD)) {
            Buff.affect(target, Weakness.class, 5f);
        }

        Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.87f, 1.15f));
        bubbleLauncherCooldown = 3;
        spend(attackDelay());
    }

    // Bubble Cutter removed in simplified pattern

    // Bubble Lens removed in simplified pattern

    // Hamon Kick - powerful melee attack
    private void useHamonKick(Char target) {
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "hamon_kick"));
        Sample.INSTANCE.play(Assets.Sounds.CE2);

        // Damage and knockback
        int damage = Random.NormalIntRange(5, 10);
        target.damage(damage, this);
        Buff.affect(target, SoulMark.class, 3f);

        // Knockback effect
        int oppositePos = target.pos + (target.pos - pos);
        Ballistica trajectory = new Ballistica(target.pos, oppositePos, Ballistica.MAGIC_BOLT);
        int dist = Math.min(trajectory.dist, 3);

        if (dist > 0 && Actor.findChar(trajectory.path.get(dist)) == null) {
            Actor.add(new Pushing(target, target.pos, trajectory.path.get(dist), new Callback() {
                @Override
                public void call() {
                    if (target.properties().contains(Property.UNDEAD)) {
                        Buff.affect(target, Paralysis.class, 2f);
                    }
                }
            }));
            target.pos = trajectory.path.get(dist);
            Dungeon.level.occupyCell(target);
        }

        sprite.attack(target.pos);
        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
        hamonKickCooldown = 10;
        spend(attackDelay());
    }

    // Hamon Control - support ability
    private void useHamonControl() {
        sprite.centerEmitter().start(ElmoParticle.FACTORY, 0.15f, 4);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "hamon_control"));

        // Heal hero
        Buff.affect(hero, Barrier.class).setShield(hero.HT / 10);
        Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);

        // Remove debuffs from hero
        Buff.detach(Dungeon.hero, Poison.class);
        Buff.detach(Dungeon.hero, Cripple.class);
        Buff.detach(Dungeon.hero, Weakness.class);

        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 4, 8 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 18;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 6);
    }


    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        if (Dungeon.level.adjacent(this.pos, enemy.pos)) {
            if (Random.Int(10) == 0) {
                Buff.affect(enemy, SoulMark.class, 5f);
                Sample.INSTANCE.play(Assets.Sounds.CE3);
            } else if (Random.Int(15) == 0) {
                Buff.affect(enemy, Paralysis.class, 2f);
                Sample.INSTANCE.play(Assets.Sounds.CE3);
            }
        }

        return damage;
    }

    @Override
    public void damage(int dmg, Object src) {
        // Cat Stance damage reduction
        if (catStanceActive && dmg > 2) {
            dmg = Math.max(dmg * 2 / 3, 2);
        }

        // Damage cap
        if (dmg >= 30) {
            dmg = 30;
        }

        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        yell(Messages.get(this, "death"));

        // start 300-turn respawn countdown on the hero
        if (Dungeon.hero != null) {
            CaesarRespawn existing = Dungeon.hero.buff(CaesarRespawn.class);
            if (existing == null) {
                Buff.affect(Dungeon.hero, CaesarRespawn.class).set(300);
            } else {
                existing.set(300);
            }
        }
    }

    @Override
    public void defendPos(int cell) {
        super.defendPos(cell);
        yell(Messages.get(this, "defend"));
    }

    @Override
    public void followHero() {
        super.followHero();
        yell(Messages.get(this, "follow"));
    }

    @Override
    public void targetChar(Char ch) {
        super.targetChar(ch);
        yell(Messages.get(this, "target"));
    }

    // 300-turn respawn countdown applied to the hero on Caesar's death
    public static class CaesarRespawn extends Buff {
        private static final String REMAINING = "remaining";
        private static final String TOTAL = "total";
        private int remaining;
        private int total;

        public CaesarRespawn set(int turns) {
            this.remaining = Math.max(0, turns);
            this.total = Math.max(1, turns);
            return this;
        }

        @Override
        public boolean act() {
            // if Caesar already exists in the level, cancel the countdown
            if (Dungeon.level != null) {
                for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (m instanceof CaesarZeppeli && m.isAlive()) {
                        detach();
                        return true;
                    }
                }
            }
            if (remaining > 0) {
                remaining--;
                spend(TICK);
                return true;
            }

            // time to respawn near the hero
            if (Dungeon.level != null && Dungeon.hero != null) {
                CaesarZeppeli caesar = new CaesarZeppeli();
                caesar.pos = hero.pos;
                GameScene.add(caesar);
                Sample.INSTANCE.play(Assets.Sounds.TENDENCY2);
                Dungeon.level.occupyCell(caesar);
                if (caesar.sprite != null) {
                    caesar.sprite.centerEmitter().burst(Speck.factory(Speck.BUBBLE), 10);
                }
                caesar.yell(Messages.get(caesar, "caesar_entrance"));

            }

            detach();
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", remaining);
        }

        @Override
        public void tintIcon(Image icon) {
            // light blue tint for Caesar
            icon.hardlight(0.3f, 0.7f, 1f);
        }

        @Override
        public float iconFadePercent() {
            if (total <= 0) return 1f;
            float progressed = (float) (total - remaining) / (float) total;
            return Math.max(0f, Math.min(1f, progressed));
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(REMAINING, remaining);
            bundle.put(TOTAL, total);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            remaining = bundle.getInt(REMAINING);
            total = Math.max(1, bundle.getInt(TOTAL));
        }
    }

    private static final String BUBBLE_LAUNCHER_CD = "bubble_launcher_cd";
    private static final String BUBBLE_CUTTER_CD = "bubble_cutter_cd";
    private static final String BUBBLE_LENS_CD = "bubble_lens_cd";
    private static final String HAMON_KICK_CD = "hamon_kick_cd";
    private static final String CAT_STANCE = "cat_stance";
    private static final String HEALTH_STATE = "health_state";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BUBBLE_LAUNCHER_CD, bubbleLauncherCooldown);
        bundle.put(BUBBLE_CUTTER_CD, bubbleCutterCooldown);
        bundle.put(BUBBLE_LENS_CD, bubbleLensCooldown);
        bundle.put(HAMON_KICK_CD, hamonKickCooldown);
        bundle.put(CAT_STANCE, catStanceActive);
        bundle.put(HEALTH_STATE, healthState);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bubbleLauncherCooldown = bundle.getInt(BUBBLE_LAUNCHER_CD);
        bubbleCutterCooldown = bundle.getInt(BUBBLE_CUTTER_CD);
        bubbleLensCooldown = bundle.getInt(BUBBLE_LENS_CD);
        hamonKickCooldown = bundle.getInt(HAMON_KICK_CD);
        catStanceActive = bundle.getBoolean(CAT_STANCE);
        healthState = bundle.getInt(HEALTH_STATE);
    }
}
