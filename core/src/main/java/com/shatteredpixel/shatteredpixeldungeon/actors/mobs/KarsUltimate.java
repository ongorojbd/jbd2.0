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
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask3;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SturoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.UltimateSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKarsMashGame;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class KarsUltimate extends Mob {

    {
        spriteClass = UltimateSprite.class;

        HP = HT = 2500;
        defenseSkill = 30;
        EXP = 60;
        maxLvl = 30;

        baseSpeed = 1.15f;
        flying = true;
        viewDistance = 12;

        immunities.add(ShrGas.class);
        properties.add(Property.BOSS);
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    private static final int NONE = 0;
    private static final int GENOME_SPIRAL = 1;
    private static final int WING_DIVE = 2;
    private static final int LIFE_CREATION = 3;
    private static final int PRISM_HAMON = 4;
    private static final int AXIS_SWEEP = 6;
    private static final int VERTICAL_SWEEP = 7;

    private int phase = 0;
    private int abilityCooldown = 3;
    private int lastAbility = NONE;
    private int pendingAbility = NONE;
    private int windup = 0;
    private int solarCooldown = 16;
    private int solarWindup = 0;
    private int solarSafeCell = -1;
    private int finalGameCooldown = 0;
    private boolean finalGameActive = false;
    private boolean invulnWarned = false;
    private final ArrayList<Integer> targetCells = new ArrayList<>();
    private final ArrayList<Emitter> flameEmitters = new ArrayList<>();
    private int regenCounter = 0;

    private static final String PHASE = "phase";
    private static final String ABILITY_COOLDOWN = "ability_cooldown";
    private static final String LAST_ABILITY = "last_ability";
    private static final String PENDING_ABILITY = "pending_ability";
    private static final String WINDUP = "windup";
    private static final String SOLAR_COOLDOWN = "solar_cooldown";
    private static final String SOLAR_WINDUP = "solar_windup";
    private static final String SOLAR_SAFE = "solar_safe";
    private static final String FINAL_GAME_COOLDOWN = "final_game_cooldown";
    private static final String FINAL_GAME_ACTIVE = "final_game_active";
    private static final String INVULN_WARNED = "invuln_warned";
    private static final String TARGET_CELLS = "target_cells";
    private static final String REGEN_COUNTER = "regen_counter";

    private boolean finalBattle() {
        return true;
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new UltimateSprite(), new UltimateSprite(), new UltimateSprite()},
                        new String[]{"완전생물 카즈", "완전생물 카즈", "완전생물 카즈"},
                        new String[]{
                                Messages.get(KarsUltimate.class, "t1"),
                                Messages.get(KarsUltimate.class, "t2"),
                                Messages.get(KarsUltimate.class, "t3")
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        if (abilityCooldown > 0) abilityCooldown--;
        if (solarCooldown > 0) solarCooldown--;
        if (finalGameCooldown > 0) finalGameCooldown--;

        // 체력 재생 (최종 페이즈 제외, 2턴에 1 HP)
        if (phase < 4 && HP < HT) {
            regenCounter++;
            if (regenCounter >= 2) {
                regenCounter = 0;
                HP++;
            }
        }

        if (phase >= 4) {
            return actFinalPhase();
        }

        if (windup > 0) {
            windup--;
            if (windup == 0) {
                boolean freeSweep = (pendingAbility == AXIS_SWEEP || pendingAbility == VERTICAL_SWEEP);
                resolveAbility();
                if (freeSweep) {
                    return super.act();
                }
            } else {
                telegraphTargets(abilityColor());
                spend(TICK);
            }
            return true;
        }

        if (solarWindup > 0) {
            resolveSolarFall();
            return true;
        }

        if (enemy != null && enemy.isAlive()) {
            if (phase >= 2 && solarCooldown <= 0 && startSolarFall()) {
                return true;
            }
            if (abilityCooldown <= 0 && startAbility()) {
                if (lastAbility == AXIS_SWEEP || lastAbility == VERTICAL_SWEEP) {
                    return super.act();
                }
                return true;
            }
        }

        return super.act();
    }

    private boolean actFinalPhase() {
        if (WndKarsMashGame.instance == null && !finalGameActive && finalGameCooldown <= 0) {
            startFinalMashGame();
        }
        spend(TICK);
        return true;
    }

    private void startFinalMashGame() {
        finalGameActive = true;
        finalGameCooldown = 4;
        Dungeon.hero.interrupt();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        new Flare(12, 72).color(0xFFFFFF, true).show(sprite, 2.5f);

        final KarsUltimate kars = this;
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                if (!kars.isAlive() || WndKarsMashGame.instance != null) return;
                GameScene.show(new WndKarsMashGame(
                        new Callback() {
                            @Override
                            public void call() {
                                kars.finalGameActive = false;
                                GLog.p(Messages.get(KarsUltimate.class, "final_game_success"));
                                GameScene.flash(0xFFFFFF);
                                Camera.main.shake(8, 1f);
                                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
                                kars.die(Dungeon.hero);
                            }
                        },
                        new Callback() {
                            @Override
                            public void call() {
                                kars.finalGameActive = false;
                                kars.finalGameCooldown = 3;
                                GLog.n(Messages.get(KarsUltimate.class, "final_game_fail"));
                                int damage = Math.max(10, Dungeon.hero.HP / 2);
                                Dungeon.hero.damage(damage, kars);
                                CellEmitter.center(Dungeon.hero.pos).burst(BlastParticle.FACTORY, 20);
                                if (!Dungeon.hero.isAlive()) {
                                    Dungeon.fail(KarsUltimate.class);
                                }
                            }
                        }
                ));
            }
        });
    }

    private boolean startAbility() {
        if (enemy == null) return false;

        int nextAbility = nextAbility();
        for (int i = 0; i < 4; i++) {
            if (nextAbility == GENOME_SPIRAL && startGenomeSpiral()) {
                return true;
            } else if (nextAbility == WING_DIVE && startWingDive()) {
                return true;
            } else if (nextAbility == LIFE_CREATION && startLifeCreation()) {
                return true;
            } else if (nextAbility == PRISM_HAMON && startPrismHamon()) {
                return true;
            } else if (nextAbility == AXIS_SWEEP && startAxisSweep()) {
                return true;
            } else if (nextAbility == VERTICAL_SWEEP && startVerticalSweep()) {
                return true;
            }
            nextAbility = nextAbilityAfter(nextAbility);
        }
        return false;
    }

    private int nextAbility() {
        if (lastAbility == NONE) {
            return Dungeon.level.distance(pos, enemy.pos) >= 5 ? WING_DIVE : GENOME_SPIRAL;
        }
        return nextAbilityAfter(lastAbility);
    }

    private int nextAbilityAfter(int ability) {
        if (phase >= 3) {
            if (ability == GENOME_SPIRAL) return WING_DIVE;
            if (ability == WING_DIVE) return LIFE_CREATION;
            if (ability == LIFE_CREATION) return PRISM_HAMON;
            if (ability == PRISM_HAMON) return AXIS_SWEEP;
            if (ability == AXIS_SWEEP) return VERTICAL_SWEEP;
            return GENOME_SPIRAL;
        } else if (phase >= 1) {
            if (ability == GENOME_SPIRAL) return WING_DIVE;
            if (ability == WING_DIVE) return LIFE_CREATION;
            if (ability == LIFE_CREATION) return PRISM_HAMON;
            if (ability == PRISM_HAMON) return AXIS_SWEEP;
            if (ability == AXIS_SWEEP) return VERTICAL_SWEEP;
            return GENOME_SPIRAL;
        } else {
            if (ability == GENOME_SPIRAL) return PRISM_HAMON;
            if (ability == PRISM_HAMON) return WING_DIVE;
            if (ability == WING_DIVE) return AXIS_SWEEP;
            if (ability == AXIS_SWEEP) return VERTICAL_SWEEP;
            return GENOME_SPIRAL;
        }
    }

    private boolean startGenomeSpiral() {
        if (enemy == null) return false;

        targetCells.clear();
        int center = enemy.pos;
        int width = Dungeon.level.width();
        int range = phase >= 3 ? 5 : phase >= 1 ? 4 : 3;

        for (int r = 1; r <= range; r++) {
            addOffsetCell(center, r, -(r - 1), width);
            addOffsetCell(center, r - 1, r, width);
            addOffsetCell(center, -r, r - 1, width);
            addOffsetCell(center, -(r - 1), -r, width);
            if (phase >= 2) {
                addOffsetCell(center, r, r, width);
                addOffsetCell(center, -r, -r, width);
            }
        }
        addTargetCell(center);

        if (targetCells.isEmpty()) return false;

        pendingAbility = GENOME_SPIRAL;
        windup = 1;
        lastAbility = GENOME_SPIRAL;
        setAbilityCooldown();
        telegraphTargets(0xFF00FF);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "genome_ready"));
        spend(TICK);
        return true;
    }

    private boolean startWingDive() {
        if (enemy == null) return false;

        targetCells.clear();
        Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID);
        int limit = Math.min(aim.dist, phase >= 3 ? 9 : 7);
        for (int i = 1; i <= limit && i < aim.path.size(); i++) {
            int cell = aim.path.get(i);
            if (!Dungeon.level.insideMap(cell) || Dungeon.level.solid[cell]) break;
            addTargetCell(cell);
        }

        if (targetCells.size() < 2) return false;

        pendingAbility = WING_DIVE;
        windup = 1;
        lastAbility = WING_DIVE;
        setAbilityCooldown();
        telegraphTargets(0xFF00FF);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "wing_ready"));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        spend(TICK);
        return true;
    }

    private boolean startLifeCreation() {
        this.sprite.emitter().burst( RainbowParticle.BURST, 12 );

        if (enemy == null || phase < 1) return false;

        ArrayList<Integer> spots = new ArrayList<>();
        for (int offset : PathFinder.NEIGHBOURS8) {
            int cell = pos + offset;
            if (Dungeon.level.insideMap(cell) && Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
                spots.add(cell);
            }
        }
        if (spots.isEmpty()) return false;

        lastAbility = LIFE_CREATION;
        setAbilityCooldown();
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "creation_ready"));
        Sample.INSTANCE.play(Assets.Sounds.MIMIC);

        int creation = Random.Int(4);
        if (creation == 0) {
            int count = Math.min(2, spots.size());
            for (int i = 0; i < count; i++) {
                int spawnPos = spots.remove(Random.Int(spots.size()));
                KarsMawTentacle tentacle = new KarsMawTentacle();
                summonCreation(tentacle, spawnPos, 0xFF00FF);
            }
        } else if (creation == 1) {
            int spawnPos = spots.remove(Random.Int(spots.size()));
            KarsCorrosiveButterfly butterfly = new KarsCorrosiveButterfly();
            summonCreation(butterfly, spawnPos, 0xFF00FF);
        } else if (creation == 2) {
            int spawnPos = spots.remove(Random.Int(spots.size()));
            KarsSquirrel squirrel = new KarsSquirrel();
            summonCreation(squirrel, spawnPos, 0xFF00FF);
        } else {
            int count = Math.min(4, spots.size());
            for (int i = 0; i < count; i++) {
                int spawnPos = spots.remove(Random.Int(spots.size()));
                KarsPiranha piranha = new KarsPiranha();
                summonCreation(piranha, spawnPos, 0xFF00FF);
            }
        }

        spend(TICK);
        return true;
    }

    private void summonCreation(Mob mob, int spawnPos, int color) {
        mob.pos = spawnPos;
        mob.state = mob.HUNTING;
        GameScene.add(mob);
        mob.beckon(Dungeon.hero.pos);
        Dungeon.level.occupyCell(mob);
        warnCell(spawnPos, color);
    }

    private boolean startPrismHamon() {
        if (enemy == null) return false;

        targetCells.clear();
        int width = Dungeon.level.width();
        int dir = directionTo(enemy.pos);
        int[] dirs = new int[]{-width - 1, -width, -width + 1, 1, width + 1, width, width - 1, -1};
        int range = phase >= 3 ? 8 : phase >= 1 ? 7 : 6;
        for (int offset = -1; offset <= 1; offset++) {
            collectRay(pos, dirs[(dir + offset + dirs.length) % dirs.length], range);
        }

        if (targetCells.isEmpty()) return false;

        pendingAbility = PRISM_HAMON;
        windup = 1;
        lastAbility = PRISM_HAMON;
        setAbilityCooldown();
        telegraphTargets(0xFF00FF);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "prism_ready"));
        spend(TICK);
        return true;
    }

    private boolean startAxisSweep() {
        targetCells.clear();
        int width = Dungeon.level.width();
        int height = Dungeon.level.height();
        int parity = Random.Int(2);

        for (int y = 0; y < height; y++) {
            for (int x = parity; x < width; x += 2) {
                addTargetCell(x + y * width);
            }
        }

        if (targetCells.isEmpty()) return false;

        pendingAbility = AXIS_SWEEP;
        windup = 1;
        lastAbility = AXIS_SWEEP;
        setAbilityCooldown();
        telegraphTargets(0xFF00FF);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "axis_ready"));
        Dungeon.hero.interrupt();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        return true;
    }

    private boolean startVerticalSweep() {
        targetCells.clear();
        int width = Dungeon.level.width();
        int height = Dungeon.level.height();
        int parity = Random.Int(2);

        for (int x = 0; x < width; x++) {
            for (int y = parity; y < height; y += 2) {
                addTargetCell(x + y * width);
            }
        }

        if (targetCells.isEmpty()) return false;

        pendingAbility = VERTICAL_SWEEP;
        windup = 1;
        lastAbility = VERTICAL_SWEEP;
        setAbilityCooldown();
        telegraphTargets(0xFF00FF);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "vertical_ready"));
        Dungeon.hero.interrupt();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        return true;
    }

    private void resolveAbility() {
        if (pendingAbility == GENOME_SPIRAL) {
            resolveGenomeSpiral();
        } else if (pendingAbility == WING_DIVE) {
            resolveWingDive();
        } else if (pendingAbility == PRISM_HAMON) {
            resolvePrismHamon();
        } else if (pendingAbility == AXIS_SWEEP) {
            resolveAxisSweep();
        } else if (pendingAbility == VERTICAL_SWEEP) {
            resolveVerticalSweep();
        }
        pendingAbility = NONE;
        targetCells.clear();
        spend(TICK);
    }

    private void resolveGenomeSpiral() {
        Sample.INSTANCE.play(Assets.Sounds.TONIO);

        for (int cell : targetCells) {
            if (!validTargetCell(cell)) continue;
            CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 8);
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(35, 45);
                ch.damage(dmg, this);
                Buff.affect(ch, Bleeding.class).set(dmg * 0.25f);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
    }

    private void resolveWingDive() {
        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "wing_dive"));

        int landing = pos;
        for (int cell : targetCells) {
            if (!validTargetCell(cell)) continue;
            CellEmitter.center(cell).burst(Speck.factory(Speck.LIGHT), 8);
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(35, 45);
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
            if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
                landing = cell;
            }
        }

        if (landing != pos) {
            int oldPos = pos;
            pos = landing;
            Dungeon.level.occupyCell(this);
            sprite.move(oldPos, pos);
        }
    }

    private void resolvePrismHamon() {
        Sample.INSTANCE.play( Assets.Sounds.HIT_STRONG, 2, Random.Float(3.3f, 3.3f) );

        for (int cell : targetCells) {
            if (!validTargetCell(cell)) continue;
            CellEmitter.center(cell).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            CellEmitter.center(cell).burst( Speck.factory( Speck.WOOL ), 6 );
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(35, 45);
                ch.damage(dmg, this);
                Buff.affect(ch, Cripple.class, 2f);
                Buff.affect(ch, Poison.class).set(6 + phase * 2);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
    }

    private void resolveAxisSweep() {
        Sample.INSTANCE.play(Assets.Sounds.RAY);

        for (int cell : targetCells) {
            if (!validTargetCell(cell)) continue;
            CellEmitter.center(cell).burst(Speck.factory(Speck.ROCK), 3);
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(35, 45);
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }

        abilityCooldown = 0;
    }

    private void resolveVerticalSweep() {
        Sample.INSTANCE.play(Assets.Sounds.RAY);

        for (int cell : targetCells) {
            if (!validTargetCell(cell)) continue;
            CellEmitter.center(cell).burst(Speck.factory(Speck.ROCK), 3);
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(35, 45);
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
    }

    private int abilityColor() {
        return 0xFF00FF;
    }


    private void setAbilityCooldown() {
        abilityCooldown = phase >= 3 ? 3 : phase >= 1 ? 4 : 5;
    }

    private void addOffsetCell(int center, int dx, int dy, int width) {
        int x = center % width + dx;
        int y = center / width + dy;
        if (x < 0 || x >= width || y < 0 || y >= Dungeon.level.height()) return;
        addTargetCell(x + y * width);
    }

    private int directionTo(int target) {
        int width = Dungeon.level.width();
        int dx = Integer.compare(target % width, pos % width);
        int dy = Integer.compare(target / width, pos / width);

        if (dx < 0 && dy < 0) return 0;
        if (dx == 0 && dy < 0) return 1;
        if (dx > 0 && dy < 0) return 2;
        if (dx > 0 && dy == 0) return 3;
        if (dx > 0) return 4;
        if (dx == 0 && dy > 0) return 5;
        if (dx < 0 && dy > 0) return 6;
        return 7;
    }

    private void collectLine(int center, int step, int range) {
        addTargetCell(center);
        collectRay(center, step, range);
        collectRay(center, -step, range);
    }

    private void collectRay(int start, int step, int range) {
        int width = Dungeon.level.width();
        int previous = start;
        for (int i = 0; i < range; i++) {
            int cell = previous + step;
            if (!Dungeon.level.insideMap(cell) || Dungeon.level.solid[cell]) break;
            if (Math.abs(cell % width - previous % width) > 1) break;
            addTargetCell(cell);
            previous = cell;
        }
    }

    private void addTargetCell(int cell) {
        if (validTargetCell(cell) && !targetCells.contains(cell)) {
            targetCells.add(cell);
        }
    }

    private boolean validTargetCell(int cell) {
        return Dungeon.level.insideMap(cell) && !Dungeon.level.solid[cell];
    }

    private void telegraphTargets(int color) {
        for (int cell : targetCells) {
            warnCell(cell, color);
        }
    }

    private boolean startSolarFall() {
        solarSafeCell = chooseSafeCell();
        solarWindup = 10;
        invulnWarned = false;
        telegraphSolarFall();
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "solar"));
        GLog.p(Messages.get(this, "solar_ready"));
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        new Flare(10, 64).color(0xFFFF66, true).show(sprite, 2.5f);
        sprite.add(CharSprite.State.SHIELDED);
        spend(TICK);
        return true;
    }

    private int chooseSafeCell() {
        ArrayList<Integer> candidates = new ArrayList<>();
        int len = Dungeon.level.length();

        for (int cell = 0; cell < len; cell++) {
            if (!Dungeon.level.insideMap(cell)) continue;
            if (Dungeon.level.distance(cell, Dungeon.hero.pos) > 12) continue;
            if (Dungeon.level.map[cell] == Terrain.EMPTY && Actor.findChar(cell) == null) {
                candidates.add(cell);
            }
        }

        if (candidates.isEmpty()) {
            for (int cell = 0; cell < len; cell++) {
                if (!Dungeon.level.insideMap(cell)) continue;
                if (Dungeon.level.distance(cell, Dungeon.hero.pos) > 12) continue;
                if (Dungeon.level.passable[cell] && !Dungeon.level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }
        }

        // 12칸 이내 후보가 없으면 거리 제한 없이 선택
        if (candidates.isEmpty()) {
            for (int cell = 0; cell < len; cell++) {
                if (!Dungeon.level.insideMap(cell)) continue;
                if (Dungeon.level.passable[cell] && !Dungeon.level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }
        }

        return candidates.isEmpty() ? pos : candidates.get(Random.Int(candidates.size()));
    }

    private void telegraphSolarFall() {
        for (int cell = 0; cell < Dungeon.level.length(); cell++) {
            if (!Dungeon.level.insideMap(cell) || Dungeon.level.solid[cell]) continue;
            if (cell == solarSafeCell) {
                warnCell(cell, 0x00FFFF);
                CellEmitter.center(cell).burst(SparkParticle.FACTORY, 4);
            } else {
                warnCell(cell, 0xFFFF33);
            }
        }
    }

    private void resolveSolarFall() {
        solarWindup--;
        telegraphSolarFall();

        if (solarWindup > 0) {
            // 안전지대에 카운트다운 표시
            if (solarSafeCell != -1) {
                com.watabou.utils.PointF p = DungeonTilemap.raisedTileCenterToWorld(solarSafeCell);
                FloatingText.show(p.x, p.y, solarSafeCell, solarWindup + "...", CharSprite.NEUTRAL);
            }

            // 안전지대를 향해 안개가 밀려오는 효과 (거리 > solarWindup 인 셀에 Smog 추가)
            for (int cell = 0; cell < Dungeon.level.length(); cell++) {
                if (!Dungeon.level.insideMap(cell) || Dungeon.level.solid[cell] || cell == solarSafeCell) continue;
                if (Dungeon.level.distance(cell, solarSafeCell) > solarWindup) {
                    GameScene.add(Blob.seed(cell, 2, ShrGas.class));
                }
            }

            spend(TICK);
            return;
        }

        Camera.main.shake(4, 0.8f);
        GameScene.flash(0xFFFF99);
        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        for (int cell = 0; cell < Dungeon.level.length(); cell++) {
            if (!Dungeon.level.insideMap(cell) || Dungeon.level.solid[cell] || cell == solarSafeCell) continue;

            // 폭발 시 안개 제거
            ShrGas shrGas = (ShrGas) Dungeon.level.blobs.get(ShrGas.class);
            if (shrGas != null) shrGas.clear(cell);

            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(55, 75);
                if (ch == Dungeon.hero) {
                    ch.damage(dmg, this);
                } else {
                    ch.damage(25, this);
                }

                Buff.affect(ch, Burning.class).reignite(ch, 3f);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }

        solarSafeCell = -1;
        solarCooldown = phase >= 3 ? 12 : 18;
        invulnWarned = false;
        sprite.remove(CharSprite.State.SHIELDED);
        spend(TICK);
    }

    private void warnCell(int cell, int color) {
        if (sprite != null && sprite.parent != null) {
            sprite.parent.addToBack(new TargetedCell(cell, color));
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        if (phase >= 4) {
            if (!invulnWarned && sprite != null) {
                invulnWarned = true;
                sprite.showStatus(CharSprite.WARNING, Messages.get(this, "invulnerable"));
            }
            return;
        }

        int finalPhaseHP = Math.max(1, HT / 10);
        if (phase == 3 && HP <= finalPhaseHP) {
            enterFinalPhase();
            return;
        }
        if (phase < 4 && HP - dmg <= finalPhaseHP) {
            dmg = Math.max(0, HP - finalPhaseHP);
        }

        int damageCap = finalBattle() ? 300 : 100;
        if (dmg > damageCap) {
            dmg = damageCap;
        }

        if (buff(Barrier.class) != null) {
            dmg = Math.max(1, dmg / 3);
        }

        super.damage(dmg, src);

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && src != null && !isImmune(src.getClass()) && !isInvulnerable(src.getClass())) {
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) lock.addTime(dmg);
            else lock.addTime(dmg * 1.5f);
        }

        if (phase == 0 && HP < HT * 2 / 3) {
            phase = 1;
            baseSpeed = 1.25f;
            abilityCooldown = 1;
            Music.INSTANCE.play(Assets.Music.TENDENCY3, true);
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new UltimateSprite(), new UltimateSprite()},
                    new String[]{"완전생물 카즈", "완전생물 카즈"},
                    new String[]{
                            Messages.get(KarsUltimate.class, "t12"),
                            Messages.get(KarsUltimate.class, "t13")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );
        } else if (phase == 1 && HP < HT / 3) {
            phase = 2;
            baseSpeed = 1.35f;
            abilityCooldown = 1;
            solarCooldown = 2;
            BossHealthBar.bleed(true);
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new UltimateSprite(), new UltimateSprite()},
                    new String[]{"완전생물 카즈", "완전생물 카즈"},
                    new String[]{
                            Messages.get(KarsUltimate.class, "t14"),
                            Messages.get(KarsUltimate.class, "t15")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );
        } else if (phase == 2 && HP < HT / 5) {
            phase = 3;
            baseSpeed = 1.50f;
            abilityCooldown = 1;
            solarCooldown = 1;
        }

        if (phase == 3 && HP <= finalPhaseHP) {
            Music.INSTANCE.play(Assets.Music.TENDENCY1, true);
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new SturoSprite(), new UltimateSprite(), new UltimateSprite()},
                    new String[]{"슈트로하임", "완전생물 카즈", "완전생물 카즈"},
                    new String[]{
                            Messages.get(KarsUltimate.class, "t9"),
                            Messages.get(KarsUltimate.class, "t10"),
                            Messages.get(KarsUltimate.class, "t11")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );

            enterFinalPhase();
        }
    }

    private void enterFinalPhase() {
        phase = 4;
        HP = Math.max(1, HT / 10);
        baseSpeed = 1f;
        targetCells.clear();
        pendingAbility = NONE;
        windup = 0;
        solarWindup = 0;
        solarSafeCell = -1;
        abilityCooldown = 9999;
        solarCooldown = 9999;
        finalGameCooldown = 5;
        finalGameActive = false;
        invulnWarned = false;
        sprite.add(CharSprite.State.SHIELDED);
        BossHealthBar.bleed(true);

        flameEmitters.clear();
        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (!Dungeon.level.solid[i] && Dungeon.level.insideMap(i)) {
                Emitter e = CellEmitter.get(i);
                e.pour(FlameParticle.FACTORY, 0.03f);
                flameEmitters.add(e);
            }
        }
    }

    private void phaseShift(String message, int color) {
        targetCells.clear();
        pendingAbility = NONE;
        windup = 0;

        yell(message);
        sprite.showStatus(CharSprite.WARNING, message);
        GameScene.flash(color);
        Camera.main.shake(5, 0.8f);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        new Flare(8, 48).color(color, true).show(sprite, 2f);
        CellEmitter.center(pos).burst(Speck.factory(Speck.UP), 20);
        Buff.affect(this, Barrier.class).setShield(finalBattle() ? 120 + phase * 40 : 35 + phase * 15);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Bleeding.class).set(damage * 0.25f);
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 55, 60 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(5, 20);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        if (solarWindup > 0 || phase >= 4) {
            if (!invulnWarned && sprite != null) {
                invulnWarned = true;
                sprite.showStatus(CharSprite.WARNING, Messages.get(this, "invulnerable"));
            }
            return true;
        }
        return super.isInvulnerable(effect);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        for (Emitter e : flameEmitters) {
            e.on = false;
        }
        flameEmitters.clear();

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof KarsMawTentacle || mob instanceof KarsCorrosiveButterfly || mob instanceof KarsPiranha || mob instanceof KarsSquirrel) {
                mob.die( cause );
            }
        }

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new UltimateSprite(), new UltimateSprite(), new UltimateSprite(), new UltimateSprite(), new UltimateSprite(), new UltimateSprite()},
                new String[]{"완전생물 카즈", "완전생물 카즈", "완전생물 카즈", "완전생물 카즈", "완전생물 카즈", "완전생물 카즈"},
                new String[]{
                        Messages.get(KarsUltimate.class, "t4"),
                        Messages.get(KarsUltimate.class, "t42"),
                        Messages.get(KarsUltimate.class, "t5"),
                        Messages.get(KarsUltimate.class, "t6"),
                        Messages.get(KarsUltimate.class, "t7"),
                        Messages.get(KarsUltimate.class, "t8")
                },
                new byte[]{
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.DIE
                }
        );

        Statistics.diospawned = false;

        GameScene.bossSlain();

        Music.INSTANCE.end();

        Badges.validateKarsKill();

        Dungeon.level.drop(new Smask3().identify(), pos).sprite.drop(pos);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(ABILITY_COOLDOWN, abilityCooldown);
        bundle.put(LAST_ABILITY, lastAbility);
        bundle.put(PENDING_ABILITY, pendingAbility);
        bundle.put(WINDUP, windup);
        bundle.put(SOLAR_COOLDOWN, solarCooldown);
        bundle.put(SOLAR_WINDUP, solarWindup);
        bundle.put(SOLAR_SAFE, solarSafeCell);
        bundle.put(FINAL_GAME_COOLDOWN, finalGameCooldown);
        bundle.put(FINAL_GAME_ACTIVE, finalGameActive);
        bundle.put(INVULN_WARNED, invulnWarned);
        bundle.put(REGEN_COUNTER, regenCounter);

        int[] cells = new int[targetCells.size()];
        for (int i = 0; i < targetCells.size(); i++) {
            cells[i] = targetCells.get(i);
        }
        bundle.put(TARGET_CELLS, cells);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        abilityCooldown = bundle.getInt(ABILITY_COOLDOWN);
        lastAbility = bundle.getInt(LAST_ABILITY);
        pendingAbility = bundle.getInt(PENDING_ABILITY);
        windup = bundle.getInt(WINDUP);
        solarCooldown = bundle.getInt(SOLAR_COOLDOWN);
        solarWindup = bundle.getInt(SOLAR_WINDUP);
        solarSafeCell = bundle.getInt(SOLAR_SAFE);
        finalGameCooldown = bundle.getInt(FINAL_GAME_COOLDOWN);
        finalGameActive = bundle.getBoolean(FINAL_GAME_ACTIVE);
        invulnWarned = bundle.getBoolean(INVULN_WARNED);
        regenCounter = bundle.getInt(REGEN_COUNTER);

        targetCells.clear();
        for (int cell : bundle.getIntArray(TARGET_CELLS)) {
            targetCells.add(cell);
        }
    }
}
