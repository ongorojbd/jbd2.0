package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Banshee;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

//a flooded archive: sunken bookshelves and broken wall stumps poke out of the water,
//toxic gas blankets the flood, and only a few dry paths cross to the doors. a pair of
//sleeping banshees lurk beneath the surface.
public class AbyssalMireChamber extends TrialChamber {

    {
        isBuildWithStructure = false;
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.WATER);
        Painter.fill(level, innerRoom, 2, Terrain.EMPTY);
        Painter.drawLine(level, center, topDoor, Terrain.EMPTY);
        Painter.drawLine(level, center, bottomDoor, Terrain.EMPTY);
        Painter.drawLine(level, center, leftDoor, Terrain.EMPTY);
        Painter.drawLine(level, center, rightDoor, Terrain.EMPTY);
        Painter.set(level, center, Terrain.PEDESTAL);

        int[][] shelfOffsets = {{-6, -6}, {6, -6}, {-6, 6}, {6, 6}};
        for (int pos : customOffsetArray(shelfOffsets)) {
            Painter.set(level, pos, Terrain.BOOKSHELF);
        }

        int[][] ruinOffsets = {{-3, -3}, {3, -3}, {-3, 3}, {3, 3}};
        for (int pos : customOffsetArray(ruinOffsets)) {
            Painter.set(level, pos, Terrain.WALL);
        }

        for (Point p : innerRoom.getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.WATER) {
                Blob.seed(cell, 40, ToxicGas.class, level);
            }
        }

        int[][] crabOffsets = {{-5, 0}, {5, 0}};
        for (int pos : customOffsetArray(crabOffsets)) {
            MireCrab crab = new MireCrab();
            crab.pos = pos;
            level.mobs.add(crab);
        }
    }

    public static class MireCrab extends Banshee {
        { properties.add(Property.BOSS_MINION); }
    }
}
