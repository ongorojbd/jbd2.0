package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomsoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcopter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Btank;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

//inspired by the vanilla SegmentedRoom: the interior is recursively split in half by straight
//WALL partitions with a 2-tile gap each (same width vanilla uses), instead of
//IronGauntletChamber's open colosseum floor. The same trio of elite grunts IronGauntletChamber
//used still converges on the entrant, but the partition walls turn it into a maze skirmish -
//breaking line of sight between the elites is now possible - instead of an open brawl.
public class PartitionedGauntletChamber extends TrialChamber {

    {
        isBuildWithStructure = false;
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.EMPTY);

        //createWalls() (like vanilla's) expects area.right/area.bottom to be the LAST FREE
        //FLOOR column/row, with the wall sitting one cell beyond that (area.right+1 etc). Since
        //innerRoom is a plain exclusive Rect, innerRoom.right/.bottom already land exactly ON
        //the room's true outer wall (not one before it, the way vanilla's inclusive Room.right
        //does before its own "-1" adjustment) - so this needs the same "-1" vanilla applies, or
        //every border check looks one cell too far out and never finds a wall, silently
        //produces zero partitions
        createWalls(level, new Rect(innerRoom.left, innerRoom.top, innerRoom.right - 1, innerRoom.bottom - 1));

        //the split above might have drawn a wall through the reward pedestal - make sure it's
        //still standing open
        Painter.set(level, center, Terrain.PEDESTAL);

        ArrayList<Integer> openCells = openFloorCells();
        openCells.remove(Integer.valueOf(level.pointToCell(center)));

        Mob[] elites = {new GauntletGuard(), new GauntletShaman(), new GauntletBrute()};
        for (Mob elite : elites) {
            if (openCells.isEmpty()) break;
            int pos = Random.element(openCells);
            openCells.remove(Integer.valueOf(pos));
            elite.pos = pos;
            elite.state = elite.HUNTING;
            level.mobs.add(elite);
        }
    }

    //cells inside this room that ended up as open floor after the recursive split - needed
    //since the split is randomized and might put a wall anywhere
    private ArrayList<Integer> openFloorCells() {
        ArrayList<Integer> cells = new ArrayList<>();
        for (int cell : innerRoomPos()) {
            if (level.map[cell] == Terrain.EMPTY) {
                cells.add(cell);
            }
        }
        return cells;
    }

    //ported from SegmentedRoom.createWalls(): recursively halves area with a single straight
    //wall, leaving a 2-tile gap
    private void createWalls(Level level, Rect area) {
        if (Math.max(area.width() + 1, area.height() + 1) < 5
                || Math.min(area.width() + 1, area.height() + 1) < 3) {
            return;
        }

        int tries = 10;

        if (area.width() > area.height() || (area.width() == area.height() && Random.Int(2) == 0)) {

            do {
                int splitX = Random.IntRange(area.left + 2, area.right - 2);

                if (level.map[splitX + level.width() * (area.top - 1)] == Terrain.WALL
                        && level.map[splitX + level.width() * (area.bottom + 1)] == Terrain.WALL) {
                    tries = 0;

                    Painter.drawLine(level, new Point(splitX, area.top), new Point(splitX, area.bottom), Terrain.WALL);

                    int spaceTop = Random.IntRange(area.top, area.bottom - 1);
                    Painter.set(level, splitX, spaceTop, Terrain.EMPTY);
                    Painter.set(level, splitX, spaceTop + 1, Terrain.EMPTY);

                    createWalls(level, new Rect(area.left, area.top, splitX - 1, area.bottom));
                    createWalls(level, new Rect(splitX + 1, area.top, area.right, area.bottom));
                }

            } while (--tries > 0);

        } else {

            do {
                int splitY = Random.IntRange(area.top + 2, area.bottom - 2);

                if (level.map[area.left - 1 + level.width() * splitY] == Terrain.WALL
                        && level.map[area.right + 1 + level.width() * splitY] == Terrain.WALL) {
                    tries = 0;

                    Painter.drawLine(level, new Point(area.left, splitY), new Point(area.right, splitY), Terrain.WALL);

                    int spaceLeft = Random.IntRange(area.left, area.right - 1);
                    Painter.set(level, spaceLeft, splitY, Terrain.EMPTY);
                    Painter.set(level, spaceLeft + 1, splitY, Terrain.EMPTY);

                    createWalls(level, new Rect(area.left, area.top, area.right, splitY - 1));
                    createWalls(level, new Rect(area.left, splitY + 1, area.right, area.bottom));
                }

            } while (--tries > 0);

        }
    }

    public static class GauntletGuard extends Btank {
        { properties.add(Property.BOSS_MINION); }
    }

    public static class GauntletShaman extends Bcomsoldier {
        { properties.add(Property.BOSS_MINION); }
    }

    public static class GauntletBrute extends Bcopter {
        { properties.add(Property.BOSS_MINION); }
    }
}
