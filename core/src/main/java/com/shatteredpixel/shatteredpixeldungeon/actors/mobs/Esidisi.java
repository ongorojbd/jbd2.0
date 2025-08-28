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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EsidisiParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask3;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EsidisiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;


/**
 * Esidisi - Flame King styled boss with heat control and blood-vein attacks.
 * Implements two special abilities:
 * - Vein Jet: extends veins from nails/toes to jet blood needles at the enemy, applying Bleeding and heat.
 * - Grand Carriage Hell Mode: opens holes over the body and emits many blood needles in all directions.
 */
public class Esidisi extends Mob {

    {
        // Using existing Pillar Man sprite as a placeholder
        spriteClass = EsidisiSprite.class;

        HP = HT = 1000;
        EXP = 40;
        defenseSkill = 22;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.UNDEAD);

        // fire theme
        immunities.add(Burning.class);
        immunities.add(Fire.class);
    }

    private int phase = 0;
    private int veinJetCooldown = 4;
    private int veinWindup = 0;
    private java.util.ArrayList<Integer> veinCells = new java.util.ArrayList<>();
    private int grandHellCooldown = 14;
    private int latticeCD = 10;
    private int latticeWindup = 0;
    private int latticeSpacing = 4;
    private int latticeOffset = 0;
    private java.util.ArrayList<Integer> latticeCells = new java.util.ArrayList<>();
    private int lasersCD = 12;
    private int lasersWindup = 0;
    private java.util.ArrayList<Integer> laserCells = new java.util.ArrayList<>();
    private boolean latticeOpenerPending = false;
    private boolean latticeOpenerUsed = false;
    private int latticeOpenerDelay = 0;

    private static final String PHASE = "phase";
    private static final String VEIN_CD = "vein_cd";
    private static final String HELL_CD = "hell_cd";

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            if (Dungeon.hero.heroClass == HeroClass.MAGE) {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new EsidisiSprite(), new TrapperSprite(), new TrapperSprite()},
                        new String[]{"에시디시", "죠셉", "죠셉"},
                        new String[]{
                                Messages.get(Esidisi.class, "t7"),
                                Messages.get(Esidisi.class, "t8"),
                                Messages.get(Esidisi.class, "t9")
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );
            }
            else {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new EsidisiSprite(), new EsidisiSprite(), new EsidisiSprite()},
                        new String[]{"에시디시", "에시디시", "에시디시"},
                        new String[]{
                                Messages.get(Esidisi.class, "t1"),
                                Messages.get(Esidisi.class, "t2"),
                                Messages.get(Esidisi.class, "t3")
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );
            }
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 200) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 200;
        }
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src);

        if (phase == 0 && HP < HT * 2 / 3) {
            phase = 1; // heats up, shorter cooldowns
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "heat_up"));
            // Heat-up visual burst
            CellEmitter.center(this.pos).burst(EsidisiParticle.FACTORY, 8);
            veinJetCooldown = Math.min(veinJetCooldown, 2);
            grandHellCooldown = Math.max(10, grandHellCooldown - 2);
        }
        if (phase == 1 && HP < HT / 2) {
            phase = 2; // unlocks grand hell more often

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new EsidisiSprite(), new EsidisiSprite(), new EsidisiSprite()},
                    new String[]{"에시디시", "에시디시", "에시디시"},
                    new String[]{
                            Messages.get(Esidisi.class, "t4"),
                            Messages.get(Esidisi.class, "t5"),
                            Messages.get(Esidisi.class, "t6")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );

            Music.INSTANCE.play(Assets.Music.TENDENCY3, true);
            // Phase transition visual burst
            CellEmitter.center(this.pos).burst(EsidisiParticle.FACTORY, 12);

            grandHellCooldown = 6;
            // Phase 2 opener: Light Lattice telegraph (after 2 turns)
            latticeOpenerPending = true;
            latticeOpenerUsed = false;
            latticeOpenerDelay = 2;
            latticeCD = 3;
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        if (veinJetCooldown > 0) veinJetCooldown--;
        if (grandHellCooldown > 0) grandHellCooldown--;
        if (latticeCD > 0) latticeCD--;
        if (lasersCD > 0) lasersCD--;

        // Phase 2 opener: force Light Lattice telegraph first
        if (phase >= 2 && latticeOpenerPending && !latticeOpenerUsed) {
            if (latticeOpenerDelay > 0) {
                latticeOpenerDelay--;
            } else if (latticeWindup == 0) {
                if (telegraphLattice()) {
                    latticeOpenerUsed = true;
                    latticeOpenerPending = false;
                    return true;
                }
            }
        }

        // Ability priority by phase
        if (phase >= 2) {
            if (grandHellCooldown <= 0 && enemy != null) {
                if (grandCarriageHell()) return true;
            }
        }
        if (veinWindup > 0) {
            veinWindup--;
            if (veinWindup == 0 && !veinCells.isEmpty()) {
                resolveVeinJet();
                return true;
            }
        }
        if (latticeWindup > 0) {
            latticeWindup--;
            if (latticeWindup == 0 && !latticeCells.isEmpty()) {
                resolveLattice();
                return true;
            }
        }
        if (lasersWindup > 0) {
            lasersWindup--;
            if (lasersWindup == 0 && !laserCells.isEmpty()) {
                resolveLasers();
                return true;
            }
        }
        // Start new telegraphs by phase
        if (phase < 2) {
            if (lasersCD <= 0 && enemy != null) {
                if (telegraphLasers()) return true;
            }
            if (veinJetCooldown <= 0 && enemy != null) {
                if (telegraphVeinJet()) return true;
            }
        } else {
            if (latticeCD <= 0 && enemy != null) {
                if (telegraphLattice()) return true;
            }
            // also use vein jet in phase 2
            if (veinJetCooldown <= 0 && enemy != null) {
                if (telegraphVeinJet()) return true;
            }
        }

        return super.act();
    }

    // Light Lattice for Esidisi (burning grid)
    private boolean telegraphLattice() {
        latticeCells.clear();
        int w = Dungeon.level.width();
        int len = Dungeon.level.length();
        latticeSpacing = 4;
        latticeOffset = com.watabou.utils.Random.Int(latticeSpacing);
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
            int x = c % w;
            int y = c / w;
            if (x % latticeSpacing == latticeOffset || y % latticeSpacing == latticeOffset) {
                latticeCells.add(c);
                sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "lattice_ready"));
        // Telegraphing lattice: charge effect at caster
        CellEmitter.center(this.pos).burst(EsidisiParticle.FACTORY, 6);
        latticeWindup = 1;
        spend(1f);
        return true;
    }

    private void resolveLattice() {
        for (int c : latticeCells) {
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(40, 56);
                if (ch.alignment == Char.Alignment.ALLY && ch != Dungeon.hero) {
                    dmg = Math.round(dmg * 0.2f);
                }
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
            // Esidisi-specific particle burst per lattice cell
            CellEmitter.center(c).burst(EsidisiParticle.FACTORY, 2);
            GameScene.add(com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob.seed(c, 3, Fire.class));
        }
        // Resolution burst at caster
        CellEmitter.center(this.pos).burst(EsidisiParticle.FACTORY, 8);
        latticeCells.clear();
        latticeCD = 10;
        spend(1f);
    }

    private boolean telegraphVeinJet() {
        if (enemy == null) return false;
        veinCells.clear();
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "vein_ready"));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        CellEmitter.center(this.pos).burst(BloodParticle.BURST, 8);
        // Additional flame burst for Esidisi theme
        CellEmitter.center(this.pos).burst(EsidisiParticle.FACTORY, 4);
        int shots = phase >= 1 ? 3 : 2;
        for (int s = 0; s < shots; s++) {
            int target = enemy.pos;
            int off = PathFinder.NEIGHBOURS9[Random.Int(PathFinder.NEIGHBOURS9.length)];
            if (off != 0 && Dungeon.level.insideMap(target + off)) target += off;
            sprite.parent.addToBack(new TargetedCell(target, 0xFF00FF));
            veinCells.add(target);
        }
        veinWindup = 1;
        spend(1f);
        return true;
    }

    private void resolveVeinJet() {
        int damageMin = 16;
        int damageMax = 25;
        for (int target : veinCells) {
            for (int i : PathFinder.NEIGHBOURS9) {
                int p = target + i;
                if (!Dungeon.level.insideMap(p) || Dungeon.level.solid[p]) continue;
                Char ch = Actor.findChar(p);
                if (ch != null && ch.alignment != alignment) {
                    int dmg = Random.NormalIntRange(damageMin, damageMax);
                    if (ch.alignment == Char.Alignment.ALLY && ch != Dungeon.hero) {
                        dmg = Math.round(dmg * 0.25f);
                    }
                    ch.damage(dmg, this);
                    Buff.affect(ch, Bleeding.class).set(0.3f * dmg);
                    Buff.affect(ch, Burning.class).reignite(ch, 3f);
                    if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
                }
                // Impact spark at each hit cell
                CellEmitter.center(p).burst(EsidisiParticle.FACTORY, 1);
                GameScene.add(Blob.seed(p, 4, Fire.class));
            }
        }
        veinCells.clear();
        veinJetCooldown = phase >= 1 ? 6 : 8;
        spend(1f);
    }

    private boolean grandCarriageHell() {
        // Prepare
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        Invisibility.dispel(this);
        // Big cast burst at caster
        CellEmitter.center(this.pos).burst(EsidisiParticle.FACTORY, 12);

        // Emit radial needles, 8 directions, a short range each
        int range = 4;
        for (int dir : PathFinder.NEIGHBOURS8) {
            int cur = pos;
            for (int r = 1; r <= range; r++) {
                cur += dir;
                if (!Dungeon.level.insideMap(cur) || Dungeon.level.solid[cur]) break;
                sprite.parent.addToBack(new TargetedCell(cur, 0xFF00FF));
                CellEmitter.center(cur).burst(BlastParticle.FACTORY, 6);
                // Add Esidisi flame on each radial cell
                CellEmitter.center(cur).burst(EsidisiParticle.FACTORY, 2);
                Char ch = Actor.findChar(cur);
                if (ch != null && ch.alignment != alignment) {
                    int dmg = Random.NormalIntRange(25, 36);
                    if (ch.alignment == Char.Alignment.ALLY && ch != Dungeon.hero) {
                        dmg = Math.round(dmg * 0.25f);
                    }
                    ch.damage(dmg, this);
                    Buff.affect(ch, Burning.class).reignite(ch, 3f);
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                }
                GameScene.add(Blob.seed(cur, 6, Fire.class));
            }
        }

        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "gc_cast"));
        grandHellCooldown = 10;
        return true;
    }

    // lasersAttack from Tboss adapted for Esidisi (heat lasers)
    private boolean telegraphLasers() {
        laserCells.clear();
        int cell = Dungeon.level.randomCell();
        java.util.HashSet<Integer> set = new java.util.HashSet<>();
        for (int i = 0; i < 6; i++) {
            Ballistica beam = new Ballistica(pos, cell, Ballistica.WONT_STOP);
            beam = beam.targetAtAngle(60 * i);
            beam = new Ballistica(pos, beam.collisionPos, Ballistica.WONT_STOP);
            for (int p : beam.subPath(1, beam.dist)) {
                if (set.add(p)) {
                    laserCells.add(p);
                    sprite.parent.addToBack(new TargetedCell(p, 0xFF00FF));
                }
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "lasers_ready"));
        // Laser telegraph charge at caster
        CellEmitter.center(this.pos).burst(EsidisiParticle.FACTORY, 6);
        lasersWindup = 1;
        spend(1f);
        return true;
    }

    private void resolveLasers() {
        for (int p : laserCells) {
            // Laser impact spark
            CellEmitter.center(p).burst(EsidisiParticle.FACTORY, 2);
            GameScene.add(Blob.seed(p, 3, Fire.class));
            Char ch = Actor.findChar(p);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(25, 36);
                if (ch.alignment == Char.Alignment.ALLY && ch != Dungeon.hero) {
                    dmg = Math.round(dmg * 0.25f);
                }
                ch.damage(dmg, this);
                Buff.affect(ch, Burning.class).reignite(ch, 3f);
                if (!ch.isAlive() && ch == Dungeon.hero) Dungeon.fail(getClass());
            }
        }
        laserCells.clear();
        lasersCD = 12;
        spend(1f);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Burning.class).reignite(enemy, 2f);
        }
        return damage;
    }
    
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 25, 36 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 35;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 13);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(VEIN_CD, veinJetCooldown);
        bundle.put(HELL_CD, grandHellCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        veinJetCooldown = bundle.getInt(VEIN_CD);
        grandHellCooldown = bundle.getInt(HELL_CD);
    }

    @Override
    public void die( Object cause ) {

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new EsidisiSprite()},
                new String[]{"에시디시"},
                new String[]{
                        Messages.get(Esidisi.class, "die")
                },
                new byte[]{
                        WndDialogueWithPic.DIE
                }
        );

        super.die( cause );

        Dungeon.level.drop(new Smask3(), pos).sprite.drop(pos);
        
        Music.INSTANCE.end();

        GameScene.bossSlain();
    }

}


