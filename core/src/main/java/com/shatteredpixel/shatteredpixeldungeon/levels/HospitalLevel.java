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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Alpha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ambulance;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Banshee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Beta;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gamma;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.AmbulanceTurret;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Patient;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTurretWand;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

/**
 * Standalone test level: escort the Ambulance (carrying Patient) down a corridor while
 * fighting off waves of enemies. This is a direct port of Tower Pixel Dungeon's Arena18
 * ("Storm the gates") - same corridor size, same DrillBig/GenDrill step-driven movement and
 * section-building timing (stepcount 32 -> next section + wave++, matching
 * Arena18.GenDrill.moveForward() exactly). The monster line-up is this game's own - see
 * SECTION_SPAWNS. Not wired into
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

    public static final String[] SANCTUM_TRACK_LIST = new String[]{Assets.Music.TG_1};
    public static final float[] SANCTUM_TRACK_CHANCES = new float[]{1f};

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(SANCTUM_TRACK_LIST, SANCTUM_TRACK_CHANCES, false);
    }

    //Arena18 is 500 wide for 15 waves; 6 sections end around column 200, so 250 is enough
    private static final int WIDTH = 250;
    //Arena18 is 30 tall. Everything below is still laid out in its 30-row coordinates and
    //shifted up by ROW_SHIFT through cellAt()/setAt()/fillAt(), which clips whatever falls
    //outside. ROW_SHIFT is derived from HEIGHT so the hero's riding row (original row 15) always
    //lands in the middle of the floor rows: with an odd HEIGHT there are (HEIGHT - 3) / 2 floor
    //rows above and below it (21 tall -> 9 each side). Change HEIGHT alone to resize; the map
    //shrinks or grows equally top and bottom.
    private static final int HEIGHT = 15;
    private static final int ROW_SHIFT = (HEIGHT - 1) / 2 - 15;

    //sections are built in a fixed order (see buildSectionForWave), one of each template;
    //the level completes on the next section trigger after the last one
    private static final int SECTION_COUNT = 6;

    //fixed mob line-up per section, in buildSectionForWave() order. columns follow SPAWN_TYPES.
    //Banshee is placed at even intervals (see placeBanshees), the rest scatter over free floor.
    @SuppressWarnings("unchecked")
    private static final Class<? extends Mob>[] SPAWN_TYPES = new Class[]{
            Banshee.class, Alpha.class, Beta.class, Gamma.class};
    private static final int[][] SECTION_SPAWNS = {
            //Banshee, Alpha, Beta, Gamma
            {4,  0,  0,  0},
            {0, 30,  0,  0},
            {0, 15, 15,  0},
            {0,  0, 30,  0},
            {0,  0, 15, 15},
            {0,  0,  0, 30},
    };
    private static final int BANSHEE = 0;

    private int entranceCell;
    private int exitCell;

    private int wave = 0;

    //matches DrillBig's own "counter"/"stepcount"/"active" fields: counter throttles movement to
    //once every 2 moving turns, stepcount counts real advances and triggers the next section at
    //32, active gates movement. GenDrill starts itself at counter == 101; here active stays
    //false until the hero tells the Patient to depart (see depart).
    //GenDrill sets stepcount = 26 up front, so the very first section arrives after only 6
    //advances instead of a full 32.
    private static final float HERO_WAIT_ON_DEPART = 30f;
    private int counter = 0;
    private int stepcount = 26;
    private boolean active = false;

    private boolean completed = false;

    private Ambulance ambulance;
    private Patient patient;

    //the turret whose wand prompt is open. Deliberately not saved: a game reloaded mid-choice
    //finds the turret still unarmed and asks again.
    private AmbulanceTurret pendingTurret;

    //how many turrets have been armed so far, which picks the patient's next line
    private static final int TURRET_LINES = 6;
    private int turretLine = 0;

    private static final String WAVE = "wave";
    private static final String COUNTER = "counter";
    private static final String STEPCOUNT = "stepcount";
    private static final String ACTIVE = "active";
    private static final String COMPLETED = "completed";
    private static final String AMBULANCE = "ambulance";
    private static final String PATIENT = "patient";
    private static final String TURRET_LINE = "turret_line";

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
        fillAt(1, 1, 15, 28, Terrain.EMPTY);
        paintEdgeRows(1, 15);
        scatterDeco(1, 15);

        //Arena18 puts the amulet pedestal at 7 + WIDTH*15 and the hero at amuletCell + 1
        entranceCell = cellAt(6, 15);

        transitions.add(new LevelTransition(this,
                entranceCell,
                LevelTransition.Type.BRANCH_ENTRANCE,
                Dungeon.depth,
                Dungeon.branch,
                LevelTransition.Type.BRANCH_EXIT));
        map[entranceCell] = Terrain.ENTRANCE;

        //placeholder - completeLevel() moves this next to wherever the ambulance stops
        exitCell = cellAt(WIDTH - 3, 15);
        transitions.add(new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT));

        return true;
    }

    @Override
    protected void createMobs() {
        //seals the level so the run has to be played out, like Arena18.initNpcs()
        seal();

        //GenDrill spawns at "amuletCell - WIDTH - WIDTH - 3" = two rows up, three columns left
        ambulance = new Ambulance();
        ambulance.pos = cellAt(7 - 3, 15 - 2);
        mobs.add(ambulance);

        //rides inside the box stepForward() carries along each step, on the amulet pedestal row
        patient = new Patient();
        patient.pos = ambulance.pos + 3 + 2 * width();
        mobs.add(patient);

        //one turret is already bolted on before the hero sets off; its wand is chosen on the
        //first turn, once there is a scene to show the prompt in (see onAmbulanceTurn)
        installTurret(false);
    }

    //adds a turret to the first free spot on the deck. Turrets ride along on their own because
    //stepForward() shoves the whole deck. The wand is picked separately, by promptPendingWand().
    private void installTurret(boolean levelIsLive) {
        int cell = freeDeckCell();
        if (cell == -1) return;

        AmbulanceTurret turret = new AmbulanceTurret();
        turret.pos = cell;
        //GameScene.add() does the mobs.add() itself, and only works once the scene exists.
        //occupyCell() reads Dungeon.level (via Blob.volumeAt), which is still null while the
        //level is being generated - the ambulance and patient are placed without it too.
        if (levelIsLive) {
            GameScene.add(turret);
            occupyCell(turret);
        } else {
            mobs.add(turret);
        }
    }

    //a turret without a wand is inert, so the choice is offered as soon as one exists and there
    //is a scene to show it in. Driven from onAmbulanceTurn() so it also covers the opening
    //turret, which is built during level generation.
    //GameScene.showingWindow() is no good as a guard here: the prompt is queued onto the render
    //thread, so it does not exist yet on the turns right after it was asked for - and several
    //of those can go by in one frame while autoWaitHero is passing the hero's turns. Tracking
    //the turret waiting on an answer instead is exact, and it self-clears once one is given.
    private void promptPendingWand() {
        if (pendingTurret != null) {
            if (pendingTurret.wand() == null) return;
            pendingTurret = null;

            //the patient works through TURRET_LINES in order, one per turret armed - a run
            //installs exactly that many, so each line is heard once and they escalate
            if (patient != null && patient.isAlive()) {
                patient.yell( Messages.get( Patient.class, "turret_wand_" + (turretLine % TURRET_LINES + 1) ) );
            }
            turretLine++;
        }

        for (Mob mob : mobs.toArray(new Mob[0])) {
            if (mob instanceof AmbulanceTurret && ((AmbulanceTurret) mob).wand() == null) {
                if (chooseWandFor((AmbulanceTurret) mob)) {
                    pendingTurret = (AmbulanceTurret) mob;
                }
                return;
            }
        }
    }

    //the deck is the same 6x5 box stepForward() carries along. Turrets fill the top and bottom
    //rows first so the middle of the deck stays walkable - they are IMMOVABLE, and the hero
    //cannot swap past one.
    private int freeDeckCell() {
        if (ambulance == null) return -1;
        int ax = ambulance.pos % width();
        int ay = ambulance.pos / width();

        //along the deck's two rails, front first and a cell apart (columns 5/3/1 hold one turret
        //each per rail, which is exactly the six a run installs). The columns in between and
        //the inner rows are only a fallback - packing turrets side by side would wall the deck
        //off, since they are IMMOVABLE and the hero cannot swap past one.
        for (int[] rows : new int[][]{{0, 4}, {1, 3, 2}}) {
            for (int dx : new int[]{5, 3, 1, 4, 2, 0}) {
                for (int dy : rows) {
                    int x = ax + dx;
                    int y = ay + dy;
                    if (x < 1 || x > WIDTH - 2 || y < 1 || y > HEIGHT - 2) continue;
                    int cell = x + y * width();
                    if (!passable[cell]) continue;
                    if (Actor.findChar(cell) != null) continue;
                    return cell;
                }
            }
        }
        return -1;
    }

    //true if a prompt was actually put up for this turret
    private boolean chooseWandFor(final AmbulanceTurret turret) {
        final ArrayList<Wand> choices = AmbulanceTurret.rollChoices(3);
        if (choices.isEmpty()) return false;

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndTurretWand(turret, choices));
            }
        });
        return true;
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

    public boolean hasDeparted() {
        return active || completed;
    }

    //the box stepForward() carries along with the ambulance - anything standing in it rides
    //instead of being left behind
    public boolean isRiding(Char ch) {
        if (ambulance == null || ch == null) return false;
        int dx = ch.pos % width() - ambulance.pos % width();
        int dy = ch.pos / width() - ambulance.pos / width();
        return dx >= 0 && dx <= 5 && dy >= 0 && dy <= 4;
    }

    //while the hero is riding along with nothing in sight there is nothing for them to decide,
    //so their turns are passed for them and the stretch between sections plays out at travel
    //speed. Hero.act() re-checks this every turn, so control comes back the moment an enemy
    //comes into view or the hero takes a hit.
    @Override
    public boolean autoWaitHero(Hero hero) {
        if (!active || completed) return false;
        //don't drive on while the hero is still picking a turret's wand. This checks the
        //turret itself rather than pendingTurret, which isn't cleared until the next ambulance
        //turn - and no ambulance turn can happen while this is holding the hero at the prompt
        if (pendingTurret != null && pendingTurret.wand() == null) return false;
        if (!isRiding(hero)) return false;
        if (hero.visibleEnemies() > 0) return false;
        return true;
    }

    //end of the Patient's depart prompt: the ambulance rolls out and the hero stands still for
    //HERO_WAIT_ON_DEPART turns while it does. Departure is one-way, there is no stopping again.
    public void depart() {
        if (hasDeparted()) return;
        active = true;
        Dungeon.hero.spendAndNext(HERO_WAIT_ON_DEPART);
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

        promptPendingWand();

        //parked until the hero says go
        if (!active) return;

        counter++;
        if (counter >= 2) {
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
            stepcount = 0;
            //the trigger after the last section ends the level, so the last section still
            //gets a full 32-step cycle to be fought through
            if (wave >= SECTION_COUNT) {
                completeLevel();
                return;
            }
            //building section N means wave N-1 has been survived, so that clear earns a turret.
            //The first section is the start of wave 0, not the end of anything.
            if (wave > 0) {
                installTurret(true);
            }
            buildSectionForWave(wave);
            wave++;
        }
        //GenDrill re-links heap sprites every move, otherwise dropped items visually lag behind
        for (Heap heap : heaps.valueList()) {
            if (heap.sprite != null) {
                heap.sprite.link(heap);
                heap.sprite.update();
            }
        }

        for (Mob mob : mobs) {
            if (mob == ambulance) continue;
            if (isRiding(mob)) {
                shove(mob);
            }
        }

        if (isRiding(Dungeon.hero)) {
            shove(Dungeon.hero);
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

        //the ambulance (and possibly the hero) just moved outside the hero's own turn, so
        //refresh the shared vision now rather than on the hero's next action
        refreshAmbulanceVision();
    }

    //Ambulance is in the shared-vision lists in Level.updateFieldOfView() and Dungeon.observe()
    //(same as SpiritHawk.HawkAlly), which merge its fieldOfView into the hero's. Mobs normally
    //refresh fieldOfView in Char.act(), but Ambulance.act() doesn't call super.act(), so it has
    //to be recomputed here whenever the ambulance moves or the terrain around it changes.
    private void refreshAmbulanceVision() {
        if (ambulance.fieldOfView == null || ambulance.fieldOfView.length != length()) {
            ambulance.fieldOfView = new boolean[length()];
        }
        updateFieldOfView(ambulance, ambulance.fieldOfView);
        Dungeon.observe();
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
            refreshAmbulanceVision();
            GameScene.updateFog();
            GameScene.updateMap();
        }
    }

    //fixed order instead of Arena18's random pick: the three regular sections first, then the
    //three special ones in the same order the original reaches them (line, golem, gates).
    //x is taken from the ambulance's current column + 10, exactly like the original's
    //"pos % WIDTH + 10" - the section then paints from x+5 onward, i.e. 15+ columns ahead of
    //the ambulance, so the wall in between still has to be ground through and the new area
    //reveals gradually instead of popping in on top of the player.
    private void buildSectionForWave(int index) {
        int x = ambulance.pos % width() + 10;

        switch (index) {
            case 0: buildTown(x); break;
            case 1: buildGraveyard(x); break;
            case 2: buildParade(x); break;
            case 3: buildLine(x); break;
            case 4: buildGolem(x); break;
            case 5: buildGates(x); break;
        }

        paintEdgeRows(x, 30);
        scatterDeco(x, 30);
        buildFlagMaps();
        GameScene.updateMap();

        spawnSection(index, x);
    }

    //Places SECTION_SPAWNS[index]'s mobs over the section's painted area (columns x+5..x+29).
    //Banshees go first at fixed intervals; the others then scatter over the remaining free
    //cells, one mob per cell.
    private void spawnSection(int index, int x) {
        ArrayList<Integer> free = passableCells(x + 5, x + 30, 1, 29);
        int[] counts = SECTION_SPAWNS[index];

        for (int type = 0; type < SPAWN_TYPES.length; type++) {
            if (type == BANSHEE) {
                placeBanshees(free, x, counts[type]);
                continue;
            }
            for (int i = 0; i < counts[type] && !free.isEmpty(); i++) {
                addMob(SPAWN_TYPES[type], free.remove(Random.Int(free.size())));
            }
        }
    }

    //evenly spaced across the section, alternating between an upper and a lower row so they
    //don't form one line. A target cell that is blocked (Town's houses) falls back to the
    //closest free cell.
    private void placeBanshees(ArrayList<Integer> free, int x, int count) {
        for (int i = 0; i < count; i++) {
            int col = x + 5 + Math.round(25 * (i + 0.5f) / count);
            int row = (i % 2 == 0) ? 8 : 22;
            int cell = nearestFree(free, col, row);
            if (cell == -1) continue;
            free.remove((Integer) cell);
            addMob(SPAWN_TYPES[BANSHEE], cell);
        }
    }

    private int nearestFree(ArrayList<Integer> free, int x, int y) {
        int best = -1;
        int bestDist = Integer.MAX_VALUE;
        for (int cell : free) {
            int dx = cell % width() - x;
            int dy = cell / width() - (y + ROW_SHIFT);
            int dist = dx * dx + dy * dy;
            if (dist < bestDist) {
                bestDist = dist;
                best = cell;
            }
        }
        return best;
    }

    private void addMob(Class<? extends Mob> cls, int cell) {
        Mob mob = Reflection.newInstance(cls);
        if (mob == null) return;
        mob.pos = cell;
        mob.state = mob.WANDERING;
        GameScene.add(mob);
        occupyCell(mob);
    }

    //The section templates below keep Arena18's 30-row coordinates. These helpers shift those
    //rows by ROW_SHIFT onto this shorter map and clip anything that lands outside the interior.
    private int cellAt(int x, int y) {
        int row = y + ROW_SHIFT;
        if (x < 1 || x > WIDTH - 2 || row < 1 || row > HEIGHT - 2) return -1;
        return x + row * width();
    }

    private void setAt(int x, int y, int terrain) {
        int cell = cellAt(x, y);
        if (cell != -1) map[cell] = terrain;
    }

    private void fillAt(int x, int y, int w, int h, int terrain) {
        int left = Math.max(1, x);
        int right = Math.min(WIDTH - 2, x + w - 1);
        int top = Math.max(1, y + ROW_SHIFT);
        int bottom = Math.min(HEIGHT - 2, y + ROW_SHIFT + h - 1);
        if (left > right || top > bottom) return;
        Painter.fill(this, left, top, right - left + 1, bottom - top + 1, terrain);
    }

    //passable cells in [x0, x1) x [y0, y1), in template coordinates
    private ArrayList<Integer> passableCells(int x0, int x1, int y0, int y1) {
        ArrayList<Integer> cells = new ArrayList<>();
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                int cell = cellAt(x, y);
                if (cell != -1 && passable[cell]) cells.add(cell);
            }
        }
        return cells;
    }

    //paints the corridor's outermost floor rows with EMPTY_SP, so the route reads as a road
    //with edges instead of one flat expanse. Purely cosmetic: EMPTY_SP copies EMPTY's flags,
    //and grindTerrainAhead() already leaves it alone rather than blasting it to embers. Runs
    //before scatterDeco() so the band doesn't get littered with debris tiles.
    private void paintEdgeRows(int x, int w) {
        for (int x1 = Math.max(1, x); x1 < Math.min(WIDTH - 1, x + w); x1++) {
            for (int y1 : new int[]{1, HEIGHT - 2}) {
                int cell = x1 + y1 * width();
                if (map[cell] == Terrain.EMPTY || map[cell] == Terrain.EMPTY_DECO) {
                    map[cell] = Terrain.EMPTY_SP;
                }
            }
        }
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
        //so it reads as plain wall being bored through
        fillAt(x, 11, 5, 10, Terrain.WALL);
        fillAt(x + 5, 1, 25, 28, Terrain.EMPTY);
        for (int x1 = x; x1 < x + 25; x1 += 5) {
            for (int y1 = 15; y1 < 26; y1 += 10) {
                if (Random.Float() > 0.2f) setAt(x1, y1, Terrain.STATUE);
            }
        }
        buildFlagMaps();
        GameScene.updateMap();
    }

    private void buildParade(int x) {
        fillAt(x, 10, 5, 10, Terrain.EMPTY);
        fillAt(x + 5, 1, 25, 28, Terrain.EMPTY);
        fillAt(x + 5, 10, 25, 10, Terrain.EMPTY);
        buildFlagMaps();
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 10; y1 < 21; y1 += 9) {
                setAt(x1, y1, Terrain.STATUE_SP);
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 23; y1 += 15) {
                if (Random.Float() > 0.5f) setAt(x1, y1, Terrain.HIGH_GRASS);
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 1; y1 < 23; y1 += 10) {
                if (Random.Float() > 0.95f) setAt(x1, y1, Terrain.WALL_DECO);
            }
        }

        buildFlagMaps();
        GameScene.updateMap();
    }

    private void buildLine(int x) {
        fillAt(x, 15, 5, 10, Terrain.WALL);
        fillAt(x + 5, 1, 25, 28, Terrain.EMPTY);
        fillAt(x + 19, 8, 1, 14, Terrain.PEDESTAL);
        buildFlagMaps();
        GameScene.updateMap();

        for (int x1 = x; x1 < x + 13; x1 += 2) {
            for (int y1 = 8; y1 < 28; y1 += 13) {
                setAt(x1, y1, Terrain.BARRICADE);
            }
        }
    }

    private void buildGolem(int x) {
        fillAt(x, 10, 5, 10, Terrain.WALL);
        fillAt(x + 5, 1, 25, 28, Terrain.EMPTY);
        buildFlagMaps();
        for (int x1 = x; x1 < x + 25; x1 += 3) {
            for (int y1 = 8; y1 < 24; y1 += 12) {
                setAt(x1, y1, Terrain.BARRICADE);
            }
        }
        fillAt(x + 19, 8, 1, 15, Terrain.BARRICADE);
        fillAt(x + 15, 12, 1, 15, Terrain.BARRICADE);
        fillAt(x + 11, 8, 1, 15, Terrain.BARRICADE);
        buildFlagMaps();
        GameScene.updateMap();
    }

    private void buildGates(int x) {
        fillAt(x, 10, 5, 10, Terrain.WATER);
        fillAt(x + 5, 1, 25, 23, Terrain.EMPTY);
        fillAt(x + 5, 10, 25, 5, Terrain.EMPTY);
        fillAt(x + 5, 20, 25, 5, Terrain.EMPTY);
        fillAt(x + 25, 10, 5, 10, Terrain.WALL);
        fillAt(x + 25, 13, 5, 4, Terrain.EMPTY);
        buildFlagMaps();

        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 10; y1 < 21; y1 += 10) {
                setAt(x1, y1, Terrain.STATUE_SP);
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 1; y1 < 23; y1 += 10) {
                if (Random.Float() > 0.8f) setAt(x1, y1, Terrain.WATER);
            }
        }
        for (int x1 = x + 5; x1 < x + 25; x1 += 3) {
            for (int y1 = 1; y1 < 23; y1 += 10) {
                if (Random.Float() > 0.95f) setAt(x1, y1, Terrain.STATUE_SP);
            }
        }

        buildFlagMaps();
        GameScene.updateMap();
    }

    private void buildTown(int x) {
        fillAt(x + 5, 1, 25, 28, Terrain.EMPTY);
        for (int x1 = x + 5; x1 < x + 25; x1++) {
            for (int y1 = 1; y1 < 25; y1 += 3) {
                if (Random.Float() > 0.94f) {
                    //original uses Random.Int(lo,hi), which is exclusive of hi
                    int w = Random.Int(6, 8);
                    int h = Random.Int(4, 6);
                    fillAt(x1, y1, w, h, Terrain.WALL);
                    fillAt(x1, y1 + h / 2, w, 1, Terrain.EMPTY);
                    fillAt(x1 + 1, y1 + 1, w - 2, h - 2, Terrain.EMPTY);
                }
            }
        }
        buildFlagMaps();
        GameScene.updateMap();
    }

    private void completeLevel() {
        completed = true;
        unseal();

        //the run ends wherever the ambulance got to, so move the exit just ahead of it and
        //carve a straight path there instead of leaving it at the far end of the corridor
        int ambCol = ambulance.pos % width();
        int exitCol = Math.min(ambCol + 15, WIDTH - 3);
        LevelTransition oldExit = getTransition(LevelTransition.Type.REGULAR_EXIT);
        //getTransition() falls back to the entrance when no match exists - don't remove that
        if (oldExit != null && oldExit.type == LevelTransition.Type.REGULAR_EXIT) {
            transitions.remove(oldExit);
        }
        exitCell = cellAt(exitCol, 15);
        transitions.add(new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT));

        int startCol = Math.max(1, Math.min(ambCol, exitCol));
        fillAt(startCol, 13, exitCol - startCol + 1, 4, Terrain.EMPTY);
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
        bundle.put(TURRET_LINE, turretLine);
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
        turretLine = bundle.getInt(TURRET_LINE);
    }
}
