package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.UnseenWarden;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yasuho;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SanctumCodeFragment;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.AbyssalMireChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.BlizzardVentChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.CrystalWardChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.GrandLibraryChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.LabyrinthChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.PartitionedGauntletChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.SpearPhalanxChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.TrialChamber;
import com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers.VaultOfKeysChamber;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSanctumCode;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;
import com.watabou.utils.Reflection;

import java.util.HashMap;

/*
    A compact, maximum-difficulty cousin of TempleNewLevel: a 3x3 grid of self-contained trial
    rooms (instead of TempleNewLevel's 2x6 grid) where 8 of the 9 slots are guaranteed to get one
    of six hard encounters built from the mod's own elite mobs.

    The 9th slot - the dead center of the grid - is left empty on purpose and doubles as the hub:
    the entrance and the locked way down both sit inside it, side by side. There's no separate
    "reward corridor" outside the grid like TempleNewLevel has; you land in the hub, fan out into
    the 8 surrounding rooms to fight/loot/find code fragments, and come back to the same room to
    leave once you have the code.

    A single unkillable UnseenWarden roams the 8 outer rooms; it can't be fought, so getting past
    it is about breaking line of sight and using the room/door layout, not combat. It never starts
    in the hub, so the hub itself is always safe.

    The way down is locked behind a 4-digit code that is randomized every time the level is
    built. 4 of the 8 outer rooms are secretly chosen to hold a "code chest" containing a
    SanctumCodeFragment that reveals one digit at its correct position; the other 4 outer rooms
    just hold normal loot. The code and whether it's been solved are persisted across save/load.

    Placement in the game's progression (which depth/branch this hooks into) is not decided yet -
    the LevelTransition depth offsets below are placeholders to be wired up later.
*/
public class VeiledSanctumLevel extends Level {

    //cool, sterile tones for the overall university-hospital concept
    {
        color1 = 0x6b8a99;
        color2 = 0xdfe8ea;
    }

    public static final String[] SANCTUM_TRACK_LIST = new String[]{Assets.Music.TG_1};
    public static final float[] SANCTUM_TRACK_CHANCES = new float[]{1f};

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(SANCTUM_TRACK_LIST, SANCTUM_TRACK_CHANCES, false);
    }

    public static final int GRID_TOP_MARGIN    = 2; //distance between the top wall and the room grid
    public static final int GRID_BOTTOM_MARGIN = 2; //distance between the room grid and the bottom wall
    public static final int GRID_X             = 3; //room grid width, at least 1
    public static final int GRID_Y             = 3; //room grid height, at least 1
    public static final int ROOM_WIDTH         = 17; //room's horizontal length (excluding wall), must be odd
    public static final int ROOM_HEIGHT        = 17; //room's vertical length (excluding wall), must be odd
    public static final int GRID_FIRST_X       = 0;
    public static final int GRID_FIRST_Y       = GRID_TOP_MARGIN;
    public static final int WIDTH  = 1 + GRID_X * (ROOM_WIDTH + 1);
    public static final int HEIGHT = GRID_FIRST_Y + GRID_Y * (ROOM_HEIGHT + 1) + GRID_BOTTOM_MARGIN + 1;
    public static final int GATE_SPACING = 4; //how far the entrance and the locked exit sit from the hub's center
    public static final int CODE_LENGTH = 4;
    public static final int CODE_ROOMS  = 4; //how many of the 8 outer rooms hold a code fragment
    Rect rect = new Rect(0, 0, WIDTH, HEIGHT);

    private int[] sanctumCode = new int[CODE_LENGTH];
    private boolean sanctumCodeSolved = false;

    private int hubIndex() {
        return (GRID_X / 2) * GRID_Y + (GRID_Y / 2);
    }

    private Point hubCenterPoint() {
        int x = GRID_X / 2;
        int y = GRID_Y / 2;
        int left = GRID_FIRST_X + x * (ROOM_WIDTH + 1);
        int top = GRID_FIRST_Y + y * (ROOM_HEIGHT + 1);
        return new Point(left + Math.round(ROOM_WIDTH / 2f), top + Math.round(ROOM_HEIGHT / 2f));
    }

    //both the entrance and the locked way down live inside the hub room, next to each other
    private int entranceCell() {
        Point c = hubCenterPoint();
        return this.pointToCell(new Point(c.x - GATE_SPACING, c.y));
    }

    private Point entrancePoint() {
        return this.cellToPoint(entranceCell());
    }

    private int exitCell() {
        Point c = hubCenterPoint();
        return this.pointToCell(new Point(c.x + GATE_SPACING, c.y));
    }

    private Point exitPoint() {
        return this.cellToPoint(exitCell());
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        transitions.add(new LevelTransition(this,
                entranceCell(),
                LevelTransition.Type.BRANCH_ENTRANCE,
                Dungeon.depth,
                0,
                LevelTransition.Type.BRANCH_EXIT));

        transitions.add(new LevelTransition(this,
                exitCell(),
                LevelTransition.Type.BRANCH_EXIT,
                Dungeon.depth,
                3,
                LevelTransition.Type.BRANCH_ENTRANCE));

        buildLevel();
        return true;
    }

    //trial room build logic - every one of the 8 outer grid slots always gets a hard encounter,
    //the 9th (center/hub) slot never does.
    //8 room types for 8 outer slots, each with weight 1 - every type is guaranteed to show up
    //exactly once per run, just shuffled into a different room each time.
    Class<?>[] trialClasses = {
            PartitionedGauntletChamber.class,
            CrystalWardChamber.class,
            GrandLibraryChamber.class,
            AbyssalMireChamber.class,
            BlizzardVentChamber.class,
            SpearPhalanxChamber.class,
            VaultOfKeysChamber.class,
            LabyrinthChamber.class,
    };
    float[] trialDeck = {1, 1, 1, 1, 1, 1, 1, 1}; //sums to 8, one for each outer room

    private TrialChamber placeTrial(int left, int top, int right, int bottom, Point center) {
        int index = Random.chances(trialDeck);
        if (index == -1) return null; //shouldn't happen since the deck sum matches the outer room count
        trialDeck[index] -= 1; //remove this pick from the pool so it can't be drawn again

        Class<?> trialClass;
        try {
            trialClass = trialClasses[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            trialClass = TrialChamber.class;
        }
        TrialChamber chamber = (TrialChamber) Reflection.newInstance(trialClass);
        if (chamber != null) {
            chamber.set(this, left, top, right, bottom, center, ROOM_WIDTH, ROOM_HEIGHT);
            chamber.build();
        }
        return chamber;
    }

    //the center room: no fights, no loot, dressed as a hospital lobby/atrium. the entrance and
    //the locked way down sit here side by side, with a receptionist standing where the old
    //fountain centerpiece used to be.
    private void buildHubRoom(int left, int top, int right, int bottom, Point center) {
        Rect outer = new Rect(left, top, right, bottom);
        Painter.fill(this, outer, Terrain.WALL);
        Painter.fill(this, outer, 1, Terrain.EMPTY);

        //two rows of archive shelving (records wing), with a gap in the middle for the
        //north/south doors
        int[][] shelves = {
                {-5, -6}, {-4, -6}, {-3, -6}, {-2, -6}, {2, -6}, {3, -6}, {4, -6}, {5, -6},
                {-5, 6}, {-4, 6}, {-3, 6}, {-2, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6},
        };
        for (int[] o : shelves) {
            Painter.set(this, center.x + o[0], center.y + o[1], Terrain.BOOKSHELF);
        }

        //a colonnade of statues ringing the lobby, framing the approach to all 4 doors
        int[][] columns = {
                {-7, -2}, {-7, 2}, {7, -2}, {7, 2},
                {-2, -7}, {2, -7}, {-2, 7}, {2, 7},
        };
        for (int[] o : columns) {
            Painter.set(this, center.x + o[0], center.y + o[1], Terrain.STATUE_SP);
        }

        //two waiting-room benches (Terrain.REGION_DECO = chairs) facing the reception desk,
        //with an open aisle down the middle
        int[][] chairs = {
                {-3, -3}, {-2, -3}, {-1, -3}, {1, -3}, {2, -3}, {3, -3},
                {-3, 3}, {-2, 3}, {-1, 3}, {1, 3}, {2, 3}, {3, 3},
        };
        for (int[] o : chairs) {
            Painter.set(this, center.x + o[0], center.y + o[1], Terrain.REGION_DECO);
        }

        //the receptionist, standing where the old fountain centerpiece used to be
        Yasuho receptionist = new Yasuho();
        receptionist.pos = this.pointToCell(center);
        this.mobs.add(receptionist);
    }

    private void buildLevel() {
        Painter.fill(this, rect, Terrain.WALL);
        Painter.fill(this, rect, 1, Terrain.EMPTY);

        for (int i = 0; i < CODE_LENGTH; i++) {
            sanctumCode[i] = Random.Int(10);
        }

        int hubIndex = hubIndex();

        //randomly choose CODE_ROOMS of the 8 outer rooms to each hold one code fragment,
        //one fragment per code position
        int[] outerRoomIndices = new int[GRID_X * GRID_Y - 1];
        int oi = 0;
        for (int i = 0; i < GRID_X * GRID_Y; i++) {
            if (i != hubIndex) outerRoomIndices[oi++] = i;
        }
        Random.shuffle(outerRoomIndices);
        HashMap<Integer, Integer> codeRoomPositions = new HashMap<>();
        for (int i = 0; i < CODE_ROOMS; i++) {
            codeRoomPositions.put(outerRoomIndices[i], i);
        }
        //the pursuer starts in one of the outer rooms - never in the hub
        int wardenRoomIndex = outerRoomIndices[Random.Int(outerRoomIndices.length)];

        Point[] roomCenters = new Point[GRID_X * GRID_Y];

        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                int left = GRID_FIRST_X + x * (ROOM_WIDTH + 1);
                int top = GRID_FIRST_Y + y * (ROOM_HEIGHT + 1);
                int right = left + ROOM_WIDTH + 2;
                int bottom = top + ROOM_HEIGHT + 2;
                Point center = new Point(left + Math.round(ROOM_WIDTH / 2f), top + Math.round(ROOM_HEIGHT / 2f));

                int roomIndex = x * GRID_Y + y;
                roomCenters[roomIndex] = center;
                boolean isHub = roomIndex == hubIndex;

                TrialChamber chamber = null;
                if (isHub) {
                    buildHubRoom(left, top, right, bottom, center);
                } else {
                    chamber = placeTrial(left, top, right, bottom, center);
                }

                int door = Terrain.DOOR;

                int doorX;
                doorX = center.x - Math.round(ROOM_WIDTH / 2f);
                if (doorX != 0 && doorX != WIDTH - 1) {
                    Painter.set(this, doorX, center.y, door);
                }
                doorX = center.x + Math.round(ROOM_WIDTH / 2f);
                if (doorX != 0 && doorX != WIDTH - 1) {
                    Painter.set(this, doorX, center.y, door);
                }
                Painter.set(this, center.x, center.y - Math.round(ROOM_HEIGHT / 2f), door);
                Painter.set(this, center.x, center.y + Math.round(ROOM_HEIGHT / 2f), door);

                if (isHub || chamber == null) {
                    continue;
                }

                Integer codePosition = codeRoomPositions.get(roomIndex);
                if (codePosition != null) {
                    chamber.bonusReward = new SanctumCodeFragment(codePosition, sanctumCode[codePosition]);
                }
                chamber.placeRewards();
            }
        }

        UnseenWarden warden = new UnseenWarden();
        warden.pos = this.pointToCell(roomCenters[wardenRoomIndex]);
        this.mobs.add(warden);

        Painter.set(this, entrancePoint(), Terrain.ENTRANCE);
        Painter.set(this, exitPoint(), Terrain.EXIT);

        //wall the way-down tile in on all 8 sides except directly to its south, where a door
        //takes its place - so the locked stairwell sits in its own little alcove instead of
        //just standing in the open. the two wall tiles flanking the door use WALL_DECO instead
        //of a plain WALL to frame it.
        Point exitPt = exitPoint();
        int[][] exitNeighbors = {
                {-1, -1}, {0, -1}, {1, -1},
                {-1, 0}, {1, 0},
                {-1, 1}, {0, 1}, {1, 1},
        };
        for (int[] o : exitNeighbors) {
            int terrain;
            if (o[0] == 0 && o[1] == 1) {
                terrain = Terrain.DOOR;
            } else if ((o[0] == -1 || o[0] == 1) && o[1] == 1) {
                terrain = Terrain.WALL_DECO;
            } else {
                terrain = Terrain.WALL;
            }
            Painter.set(this, exitPt.x + o[0], exitPt.y + o[1], terrain);
        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_EXIT && !sanctumCodeSolved) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndSanctumCode(VeiledSanctumLevel.this));
                }
            });
            return false;
        }
        return super.activateTransition(hero, transition);
    }

    public void tryCode(String input) {
        StringBuilder sb = new StringBuilder();
        for (int digit : sanctumCode) sb.append(digit);

        if (sb.toString().equals(input)) {
            sanctumCodeSolved = true;
            GLog.p("찰칵 - 자물쇠가 풀렸다.");
        } else {
            GLog.w("비밀번호가 일치하지 않는다.");
        }
    }

    private static final String SANCTUM_CODE = "sanctum_code";
    private static final String SANCTUM_CODE_SOLVED = "sanctum_code_solved";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SANCTUM_CODE, sanctumCode);
        bundle.put(SANCTUM_CODE_SOLVED, sanctumCodeSolved);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        sanctumCode = bundle.getIntArray(SANCTUM_CODE);
        sanctumCodeSolved = bundle.getBoolean(SANCTUM_CODE_SOLVED);
    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }
}
