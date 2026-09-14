/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ambulance;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Patient;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Standalone test level: escort the Ambulance (carrying Patient) down a corridor while
 * fighting off waves of enemies. This is a direct port of Tower Pixel Dungeon's Arena18
 * ("Storm the gates") - same corridor size, same DrillBig/GenDrill step-driven movement and
 * section-building timing (stepcount 32 -> next section + wave++, matching
 * Arena18.GenDrill.moveForward() exactly), same per-section monster counts. Not wired into
 * real branch/depth progression yet - launched only via the debug button in WndGame.
 */
public class HospitalLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
        //matches Arena18's own override - Dungeon.observe() copies this into hero.viewDistance
        //every turn
        viewDistance = 40;
    }

    public static final String[] SANCTUM_TRACK_LIST = new String[]{Assets.Music.KOICHI};
    public static final float[] SANCTUM_TRACK_CHANCES = new float[]{1f};

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(SANCTUM_TRACK_LIST, SANCTUM_TRACK_CHANCES, false);
    }

    //matches Tower Pixel Dungeon's Arena18 corridor dimensions
    private static final int WIDTH = 500;
    private static final int HEIGHT = 30;

    //matches Arena18's maxWaves - wave 5/10/14 trigger the special sections below
    private static final int MAX_WAVES = 15;

    private int entranceCell;
    private int exitCell;

    //matches Arena18's startLvl/startGold and doStuffEndwave()'s per-wave gold
    private static final int START_LVL = 20;
    private static final int START_GOLD = 3300;
    private static final int WAVE_END_GOLD = 600;

    private int wave = 0;

    //matches DrillBig's own "counter"/"stepcount"/"active" fields exactly: counter throttles
    //movement to once every 2 turns, stepcount counts real advances and triggers the next
    //section at 32, active gates movement until the drill starts up.
    //GenDrill sets stepcount = 26 up front, so the very first section arrives after only 6
    //advances instead of a full 32, and activates itself at counter == 101.
    private int counter = 0;
    private int stepcount = 26;
    private boolean active = false;

    private boolean completed = false;

    private Ambulance ambulance;
    private Patient patient;

    private static final String WAVE = "wave";
    private static final String COUNTER = "counter";
    private static final String STEPCOUNT = "stepcount";
    private static final String ACTIVE = "active";
    private static final String COMPLETED = "completed";
    private static final String AMBULANCE = "ambulance";
    private static final String PATIENT = "patient";

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TG;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected boolean build() {

        setSize(WIDTH, HEIGHT);
        //setSize already fills everything with WALL - only the starting room gets carved,
        //matching Arena18.build()'s "Painter.fill(this, 1, 1, 15, 28, EMPTY)" starting room
        Painter.fill(this, 1, 1, 15, 28, Terrain.EMPTY);
        scatterDeco(1, 15);

        //Arena18 puts the amulet pedestal at 7 + WIDTH*15 and the hero at amuletCell + 1
        entranceCell = 15 * WIDTH + 8;

        transitions.add(new LevelTransition(this,
                entranceCell,
                LevelTransition.Type.BRANCH_ENTRANCE,
                Dungeon.depth,
                Dungeon.branch,
                LevelTransition.Type.BRANCH_EXIT));
        map[entranceCell] = Terrain.ENTRANCE;

        //blocked until completeLevel() carves the final stretch and opens it
        exitCell = 15 * WIDTH + (WIDTH - 3);
        transitions.add(new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT));

        return true;
    }

    @Override
    protected void createMobs() {
        //Arena18.initNpcs() does this: hero.lvl = startLvl, updateHT, gold += startGold,
        //and seals the level so the run has to be played out
        Dungeon.hero.lvl = START_LVL;
        Dungeon.hero.updateHT(true);
        Dungeon.gold += START_GOLD;
        seal();

        //GenDrill spawns at "amuletCell - WIDTH - WIDTH - 3" = two rows up, three columns left
        ambulance = new Ambulance();
        ambulance.pos = (15 * WIDTH + 7) - WIDTH - WIDTH - 3;
        mobs.add(ambulance);

        //rides inside the box stepForward() carries along each step, on the amulet pedestal row
        patient = new Patient();
        patient.pos = ambulance.pos + 3 + 2 * width();
        mobs.add(patient);
    }

    @Override
    protected void createItems() {
        //Arena18 drops two starting weapons here (a tomb and a chest either side of the hero) -
        //left out on purpose, the starting room stays empty
    }

    @Override
    public Actor addRespawner() {
        return null;
    }

    //called every turn by Ambulance.act() - mirrors DrillBig.act(): grind the wall ahead every
    //turn unconditionally, then throttle actual movement to once every 2 turns
    public void onAmbulanceTurn(Ambulance amb) {
        if (completed) return;

        //seal() attaches a LockedFloor, which drains 1/turn from 50 and shuts off HP regen and
        //wand charging once empty (Regeneration.regenOn() -> Wand.Charger.recharge()). Boss
        //floors refill it via addTime() as you damage the boss; here the escort itself is the
        //progress, so keep it topped up - otherwise it runs dry before the ambulance even starts.
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) {
            lock.addTime(1f);
        }

        grindTerrainAhead();

        counter++;
        //GenDrill.act() applies the WaveBuff at counter == 101, which is what flips DrillBig's
        //"active" flag - i.e. the drill sits still for the first 101 turns, then starts up
        if (counter == 101) {
            active = true;
        }
        if (counter >= 2 && active) {
            counter = 0;
            stepForward();
        }
    }

    //mirrors Arena18.GenDrill.moveForward() exactly: stepcount++ first; at stepcount==32, build
    //the next section using the wave value BEFORE incrementing (same order as the original,
    //where startWave()'s wave++ happens after the section is picked), then the box-shove.
    private void stepForward() {
        stepcount++;

        if (stepcount == 32) {
            buildSectionForWave(wave);
            wave++;
            stepcount = 0;
            if (wave > MAX_WAVES) {
                completeLevel();
                return;
            }
        }
        //Arena18's endWave() checkpoint - doStuffEndwave() hands out 600 gold per wave
        if (stepcount == 25) {
            Dungeon.gold += WAVE_END_GOLD;
            GLog.w("+" + WAVE_END_GOLD + " gold");
        }

        //GenDrill re-links heap sprites every move, otherwise dropped items visually lag behind
        for (Heap heap : heaps.valueList()) {
            if (heap.sprite != null) {
                heap.sprite.link(heap);
                heap.sprite.update();
            }
        }

        int xd = ambulance.pos % width();
        int yd = ambulance.pos / width();

        for (Mob mob : mobs) {
            if (mob == ambulance) continue;
            int x = mob.pos % width();
            int y = mob.pos / width();
            if (x - xd >= 0 && x - xd <= 5 && y - yd >= 0 && y - yd <= 4) {
                shove(mob);
            }
        }

        Char hero = Dungeon.hero;
        int xh = hero.pos % width();
        int yh = hero.pos / width();
        boolean heroMoved = xh - xd >= 0 && xh - xd <= 5 && yh - yd >= 0 && yh - yd <= 4;
        if (heroMoved) {
            shove(hero);
        }

        shove(ambulance);

        //GenDrill.moveForward() refreshes exactly the ints2 wedge cells after each move
        int w = width();
        for (int i = 0; i < EMBER_DX.length; i++) {
            int cell = ambulance.pos + EMBER_DX[i] + EMBER_DY[i] * w;
            if (cell < 0 || cell >= length()) continue;
            GameScene.updateMap(cell);
        }
        GameScene.updateMap(ambulance.pos);

        //hero.pos just changed outside of the hero's own turn, so nothing else would recompute
        //FOV/fog for the new position until their next real action - force it now
        if (heroMoved) {
            Dungeon.observe();
        }
    }

    private void shove(Char ch) {
        int newPos = ch.pos + 1;
        if (ch.sprite != null) ch.sprite.move(ch.pos, newPos);
        ch.pos = newPos;
        occupyCell(ch);
    }

    //ported verbatim (as (dx,dy) offsets from the ambulance) from Tower Pixel Dungeon's
    //DrillBig.act() "ints" array - damages/knocks back anything caught in the wedge just ahead.
    private static final int[] HIT_DX = {6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 9};
    private static final int[] HIT_DY = {0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 1, 2, 3, 2};

    //ported verbatim from DrillBig.act()'s "ints2" array - terrain-to-EMBERS destruction wedge
    private static final int[] EMBER_DX = {6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 9};
    private static final int[] EMBER_DY = {-1, 0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 1, 2, 3, 2};

    private void grindTerrainAhead() {
        int w = width();

        for (int i = 0; i < HIT_DX.length; i++) {
            int cell = ambulance.pos + HIT_DX[i] + HIT_DY[i] * w;
            if (cell < 0 || cell >= length()) continue;
            Char ch = Actor.findChar(cell);
            if (ch == null) continue;
            ch.damage(20, ambulance);
            Sample.INSTANCE.play(Assets.Sounds.HIT_CRUSH);
            ch = Actor.findChar(cell);
            if (ch != null) {
                Ballistica line = Random.Float() < 0.5f
                        ? new Ballistica(cell, cell + w * 6 + 3, Ballistica.PROJECTILE)
                        : new Ballistica(cell, cell - w * 6 + 3, Ballistica.PROJECTILE);
                WandOfBlastWave.throwChar(ch, line, 6, false, true, Ambulance.class);
            }
        }

        boolean anyBroken = false;
        for (int i = 0; i < EMBER_DX.length; i++) {
            int cell = ambulance.pos + EMBER_DX[i] + EMBER_DY[i] * w;
            if (cell < 0 || cell >= length()) continue;
            //EMPTY_DECO is just floor litter - the drill shouldn't blast it into embers
            if (map[cell] != Terrain.EMPTY && map[cell] != Terrain.EMPTY_SP
                    && map[cell] != Terrain.EMPTY_DECO && map[cell] != Terrain.EMBERS) {
                if (map[cell] == Terrain.GRASS || map[cell] == Terrain.HIGH_GRASS) {
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    Sample.INSTANCE.play(Assets.Sounds.TRAMPLE);
                } else {
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 20);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST);
                }
                set(cell, Terrain.EMBERS);
                GameScene.updateMap(cell);
                anyBroken = true;
            }
        }

        if (anyBroken) {
            //the original does exactly this after every Level.set(i, Terrain.EMBERS):
            //buildFlagMaps() + cleanWalls(). cleanWalls() is what recomputes discoverable[],
            //and discoverable[] is what updateFieldOfView()/FogOfWar use to decide whether a
            //cell can be seen at all - without it, everything outside the starting room stays
            //permanently black no matter how much terrain is broken open.
            buildFlagMaps();
            cleanWalls();
            Dungeon.observe();
            GameScene.updateFog();
            GameScene.updateMap();
        }
    }

    //mirrors Arena18.GenDrill.moveForward()'s wave-based template choice exactly:
    //wave==5 -> buildLine, wave==10 -> buildGolem, wave==14 -> buildGates, else random.
    //x is taken from the ambulance's current column + 10, exactly like the original's
    //"pos % WIDTH + 10" - the section then paints from x+5 onward, i.e. 15+ columns ahead of
    //the ambulance, so the wall in between still has to be ground through and the new area
    //reveals gradually instead of popping in on top of the player.
    private void buildSectionForWave(int forWave) {
        int x = ambulance.pos % width() + 10;

        if (forWave == 5) {
            buildLine(x);
        } else if (forWave == 10) {
            buildGolem(x);
        } else if (forWave == 14) {
            buildGates(x);
        } else {
            switch (Random.IntRange(1, 3)) {
                case 1: buildTown(x); break;
                case 2: buildGraveyard(x); break;
                case 3: buildParade(x); break;
            }
        }

        scatterDeco(x, 30);
        buildFlagMaps();
        GameScene.updateMap();
    }

    //litters plain floor with EMPTY_DECO (dust/debris tiles). Same flags as EMPTY, so it only
    //changes how the floor looks.
    private void scatterDeco(int x, int w) {
        for (int x1 = Math.max(0, x); x1 < Math.min(WIDTH, x + w); x1++) {
            for (int y1 = 1; y1 < HEIGHT - 1; y1++) {
                int cell = x1 + width() * y1;
                if (map[cell] == Terrain.EMPTY && Random.Float() < 0.07f) {
                    map[cell] = Terrain.EMPTY_DECO;
                }
            }
        }
    }

    private void buildGraveyard(int x) {
        //original uses BARRICADE here, but this block sits directly in the ambulance's path
        //(rows 11-20, it drives along row 13) so it reads as plain wall being bored through
        Painter.fill(this, x, 11, 5, 10, Terrain.WALL);
        Painter.fill(this, x + 5, 1, 25, 28, Terrain.GRASS);
        for (int x1 = x; x1 < x + 25; x1 += 5) {
            for (int y1 = 15; y1 < 26; y1 += 10) {
                if (Random.Float() > 0.2f) map[x1 + width() * y1] = Terrain.STATUE;
            }
        }
        buildFlagMaps();
        GameScene.updateMap();

        ArrayList<Integer> candidates = new ArrayList<>();
        for (int x1 = x + 5; x1 < x + 20; x1++) {
            for (int y1 = 1; y1 < 28; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }

        dropMany(Heap.Type.TOMB, candidates,
                Generator.random(Generator.Category.SCROLL),
                Generator.random(Generator.Category.POTION),
                Generator.random(Generator.Category.SCROLL),
                Generator.random(Generator.Category.POTION),
                Generator.random(Generator.Category.SCROLL),
                Generator.random(Generator.Category.POTION),
                Generator.random(Generator.Category.SCROLL),
                Generator.random(Generator.Category.POTION),
                Generator.random(Generator.Category.SCROLL),
                Generator.random(Generator.Category.POTION));

        dropMany(Heap.Type.TOMB, candidates,
                Generator.random(Generator.Category.WAND).upgrade(3),
                Generator.random(Generator.Category.WAND).upgrade(3),
                Generator.random(Generator.Category.WAND).upgrade(3),
                Generator.random(Generator.Category.RING).upgrade(3),
                Generator.random(Generator.Category.RING).upgrade(3),
                Generator.random(Generator.Category.RING).upgrade(3));

        candidates.clear();
        for (int x1 = x; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 28; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }

        //ported verbatim from Arena18.buildGraveyard(), including the original's own quirk of
        //re-rolling Random.Float() at each check (not reusing one roll) - same (wave+1)*N counts
        if (Random.Float() > 0.66f) {
            spawnWandering(candidates, (wave + 1) * 20, Wraith.class);
        } else if (Random.Float() > 0.66f) {
            //TPD alternates Skeleton/SkeletonArmored every 6th - no SkeletonArmored here, plain
            //Skeleton stands in for it
            spawnWandering(candidates, (wave + 1) * 8, Skeleton.class);
        } else {
            spawnAlternating(candidates, (wave + 1) * 5, 5, Skeleton.class, Warlock.class);
        }
    }

    private void buildParade(int x) {
        Painter.fill(this, x, 10, 5, 10, Terrain.GRASS);
        Painter.fill(this, x + 5, 1, 25, 28, Terrain.EMPTY);
        Painter.fill(this, x + 5, 10, 25, 10, Terrain.EMPTY);
        buildFlagMaps();
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 10; y1 < 21; y1 += 9) {
                map[x1 + width() * y1] = Terrain.STATUE_SP;
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 23; y1 += 15) {
                if (Random.Float() > 0.5f) map[x1 + width() * y1] = Terrain.HIGH_GRASS;
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 1; y1 < 23; y1 += 10) {
                if (Random.Float() > 0.95f) map[x1 + width() * y1] = Terrain.WALL_DECO;
            }
        }

        ArrayList<Integer> candidates = new ArrayList<>();
        for (int x1 = x + 5; x1 < x + 20; x1++) {
            for (int y1 = 1; y1 < 28; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }

        //original also drops 5x SpawnerWall here (tower item, out of scope). BOMB isn't a
        //Generator category in jbd2.0, so plain Bombs stand in for the 6 the original rolls.
        dropMany(candidates,
                Generator.random(Generator.Category.SEED),
                new Bomb(), new Bomb(), new Bomb(), new Bomb(), new Bomb(), new Bomb(),
                Generator.random(Generator.Category.MIS_T3),
                Generator.random(Generator.Category.ARMOR),
                Generator.random(Generator.Category.FOOD),
                Generator.random(Generator.Category.FOOD),
                Generator.random(Generator.Category.FOOD),
                Generator.random(Generator.Category.FOOD),
                Generator.random(Generator.Category.FOOD));

        candidates.clear();
        for (int x1 = x; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 29; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }

        //ported verbatim from Arena18.buildParade() - every 5th is the Frost one, rest Fire
        if (Random.Float() > 0.5f) {
            spawnWandering(candidates, (wave + 1) * 4, Elemental.ShockElemental.class);
        } else {
            spawnAlternating(candidates, (wave + 1) * 4, 5, Elemental.FrostElemental.class, Elemental.FireElemental.class);
        }

        buildFlagMaps();
        GameScene.updateMap();
    }

    private void buildLine(int x) {
        Painter.fill(this, x, 15, 5, 10, Terrain.WALL);
        Painter.fill(this, x + 5, 1, 25, 28, Terrain.EMPTY);
        Painter.fill(this, x + 19, 8, 1, 14, Terrain.PEDESTAL);
        buildFlagMaps();
        GameScene.updateMap();

        //original drops 3 tower spawners here (out of scope), then lays the barricades
        for (int x1 = x; x1 < x + 13; x1 += 2) {
            for (int y1 = 8; y1 < 28; y1 += 13) {
                map[x1 + width() * y1] = Terrain.BARRICADE;
            }
        }

        //TPD lines the lane with 8 fixed LineCannon turrets (a TowerCannon1 subclass) - no
        //tower-defense turret class exists here, so 8 fixed Warlocks hold the line instead,
        //keeping the original's exact count even though the mob type has to differ
        int[] lineCells = new int[8];
        int i = 0;
        for (int yb = 8; yb < 24; yb += 2) {
            lineCells[i++] = x + 20 + yb * width();
        }
        for (int cell : lineCells) {
            if (cell < 0 || cell >= length() || !passable[cell]) continue;
            Warlock guard = new Warlock();
            guard.pos = cell;
            guard.state = guard.HUNTING;
            GameScene.add(guard);
            occupyCell(guard);
        }
    }

    private void buildGolem(int x) {
        Painter.fill(this, x, 10, 5, 10, Terrain.WALL);
        Painter.fill(this, x + 5, 1, 25, 28, Terrain.EMPTY);
        buildFlagMaps();
        for (int x1 = x; x1 < x + 25; x1 += 3) {
            for (int y1 = 8; y1 < 24; y1 += 12) {
                map[x1 + width() * y1] = Terrain.BARRICADE;
            }
        }
        Painter.fill(this, x + 19, 8, 1, 15, Terrain.BARRICADE);
        Painter.fill(this, x + 15, 12, 1, 15, Terrain.BARRICADE);
        Painter.fill(this, x + 11, 8, 1, 15, Terrain.BARRICADE);
        buildFlagMaps();
        GameScene.updateMap();

        //ported verbatim from Arena18.buildGolem(): 8 fixed golem shooters (yb=8..15), no
        //wave scaling in the original either
        for (int yb = 8; yb < 16; yb++) {
            int cell = x + 20 + yb * width();
            if (cell < 0 || cell >= length() || !passable[cell]) continue;
            Golem crossbow = new Golem();
            crossbow.pos = cell;
            crossbow.state = crossbow.HUNTING;
            GameScene.add(crossbow);
            occupyCell(crossbow);
        }
    }

    private void buildGates(int x) {
        Painter.fill(this, x, 10, 5, 10, Terrain.WATER);
        Painter.fill(this, x + 5, 1, 25, 23, Terrain.EMPTY);
        Painter.fill(this, x + 5, 10, 25, 5, Terrain.EMPTY);
        Painter.fill(this, x + 5, 20, 25, 5, Terrain.EMPTY);
        Painter.fill(this, x + 25, 10, 5, 10, Terrain.WALL);
        Painter.fill(this, x + 25, 13, 5, 4, Terrain.EMPTY);
        buildFlagMaps();

        ArrayList<Integer> candidates = new ArrayList<>();
        for (int x1 = x + 5; x1 < x + 20; x1++) {
            for (int y1 = 1; y1 < 29; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }

        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 10; y1 < 21; y1 += 10) {
                map[x1 + width() * y1] = Terrain.STATUE_SP;
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 1; y1 < 23; y1 += 10) {
                if (Random.Float() > 0.8f) map[x1 + width() * y1] = Terrain.WATER;
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 1; y1 < 23; y1 += 10) {
                if (Random.Float() > 0.95f) map[x1 + width() * y1] = Terrain.STATUE_SP;
            }
        }

        candidates.clear();
        for (int x1 = x; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 29; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }
        //ported verbatim from Arena18.buildGates(): wave*2 golems
        spawnWandering(candidates, wave * 2, Golem.class);

        buildFlagMaps();
        GameScene.updateMap();
    }

    private void buildTown(int x) {
        Painter.fill(this, x + 5, 1, 25, 28, Terrain.EMPTY);
        for (int x1 = x + 5; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 25; y1 += 3) {
                if (Random.Float() > 0.94f) {
                    //original uses Random.Int(lo,hi), which is exclusive of hi
                    int w = Random.Int(6, 8);
                    int h = Random.Int(4, 6);
                    Painter.fill(this, x1, y1, w, h, Terrain.WALL);
                    Painter.fill(this, x1, y1 + h / 2, w, 1, Terrain.EMPTY);
                    Painter.fill(this, x1 + 1, y1 + 1, w - 2, h - 2, Terrain.EMPTY);
                }
            }
        }
        buildFlagMaps();
        GameScene.updateMap();

        ArrayList<Integer> candidates = new ArrayList<>();
        for (int x1 = x + 5; x1 < x + 20; x1++) {
            for (int y1 = 5; y1 < 25; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }

        dropMany(Heap.Type.CHEST, candidates,
                Generator.random(Generator.Category.WEAPON),
                Generator.random(Generator.Category.WEAPON),
                Generator.random(Generator.Category.WEAPON),
                Generator.random(Generator.Category.WEAPON),
                Generator.random(Generator.Category.WEAPON),
                Generator.random(Generator.Category.MIS_T1),
                Generator.random(Generator.Category.MIS_T2),
                Generator.random(Generator.Category.MIS_T3),
                Generator.random(Generator.Category.ARMOR));

        drop(new GoldenKey(Dungeon.depth), Random.element(candidates));
        drop(new GoldenKey(Dungeon.depth), Random.element(candidates));
        drop(Generator.random(Generator.Category.ARMOR).identify().upgrade(3), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        drop(Generator.random(Generator.Category.WEAPON).identify().upgrade(3), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;

        candidates.clear();
        for (int x1 = x; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 29; y1++) {
                if (passable[x1 + width() * y1]) candidates.add(x1 + width() * y1);
            }
        }

        //ported verbatim from Arena18.buildTown(), same re-rolled Random.Float() at each check
        if (Random.Float() > 0.66f) {
            spawnWandering(candidates, Math.round((wave / 1.5f + 1) * 4), Monk.class);
        } else if (Random.Float() > 0.66f) {
            spawnWandering(candidates, (wave + 1) * 2, Statue.class);
        } else {
            spawnWandering(candidates, wave / 4 + 1, Senior.class);
        }

        buildFlagMaps();
        GameScene.updateMap();
    }

    private void spawnWandering(ArrayList<Integer> candidates, int count, Class<? extends Mob> cls) {
        if (candidates.isEmpty()) return;
        for (int i = 0; i < count; i++) {
            try {
                Mob mob = cls.newInstance();
                mob.pos = Random.element(candidates);
                mob.state = mob.WANDERING;
                GameScene.add(mob);
                occupyCell(mob);
            } catch (Exception e) {
                //shouldn't happen - all of these have plain no-arg constructors
            }
        }
    }

    //ported pattern from Arena18's "i % n == 0 ? classA : classB" alternating spawn loops
    private void spawnAlternating(ArrayList<Integer> candidates, int count, int everyNth, Class<? extends Mob> mainCls, Class<? extends Mob> altCls) {
        if (candidates.isEmpty()) return;
        for (int i = 0; i < count; i++) {
            Class<? extends Mob> cls = (i % everyNth == 0) ? mainCls : altCls;
            try {
                Mob mob = cls.newInstance();
                mob.pos = Random.element(candidates);
                mob.state = mob.WANDERING;
                GameScene.add(mob);
                occupyCell(mob);
            } catch (Exception e) {
                //shouldn't happen - all of these have plain no-arg constructors
            }
        }
    }

    private void dropMany(ArrayList<Integer> candidates, Item... items) {
        dropMany(Heap.Type.HEAP, candidates, items);
    }

    private void dropMany(Heap.Type type, ArrayList<Integer> candidates, Item... items) {
        if (candidates.isEmpty()) return;
        for (Item item : items) {
            drop(item, Random.element(candidates)).type = type;
        }
    }

    private void completeLevel() {
        completed = true;
        unseal();
        //carve a plain path the rest of the way to the exit in case section-building hasn't
        //reached it yet, so the now-open exit is actually reachable
        int startCol = Math.max(1, Math.min(ambulance.pos % width(), WIDTH - 20));
        Painter.fill(this, startCol, 13, (WIDTH - 2) - startCol, 4, Terrain.EMPTY);
        buildFlagMaps();
        cleanWalls();
        set(exitCell, Terrain.EXIT);
        GameScene.updateMap();
        Dungeon.observe();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WAVE, wave);
        bundle.put(COUNTER, counter);
        bundle.put(STEPCOUNT, stepcount);
        bundle.put(ACTIVE, active);
        bundle.put(COMPLETED, completed);
        bundle.put(AMBULANCE, ambulance);
        bundle.put(PATIENT, patient);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        wave = bundle.getInt(WAVE);
        counter = bundle.getInt(COUNTER);
        stepcount = bundle.getInt(STEPCOUNT);
        active = bundle.getBoolean(ACTIVE);
        completed = bundle.getBoolean(COMPLETED);
        ambulance = (Ambulance) bundle.get(AMBULANCE);
        patient = (Patient) bundle.get(PATIENT);
    }
}
