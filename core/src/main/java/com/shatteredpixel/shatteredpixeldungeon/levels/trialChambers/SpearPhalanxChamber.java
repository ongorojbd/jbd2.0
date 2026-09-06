package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Banshee;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

//a training yard: tall grass floor with a bare drill circle at the center, a broken low
//fence ringing it with gaps at the guard posts, and banner statues at the four edges.
public class SpearPhalanxChamber extends TrialChamber {

    {
        isBuildWithStructure = false;
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.HIGH_GRASS);
        Painter.fill(level, innerRoom, 3, Terrain.EMPTY);
        Painter.set(level, center, Terrain.PEDESTAL);

        int[][] statueOffsets = {{0, -7}, {0, 7}, {-7, 0}, {7, 0}};
        for (int pos : customOffsetArray(statueOffsets)) {
            Painter.set(level, pos, Terrain.STATUE_SP);
        }

        //low fence ring with gaps left open at the guard posts
        int[][] fenceOffsets = {
                {-6, -2}, {-6, 2}, {-2, -6}, {2, -6},
                {6, -2}, {6, 2}, {-2, 6}, {2, 6},
        };
        for (int pos : customOffsetArray(fenceOffsets)) {
            Painter.set(level, pos, Terrain.WALL);
        }

        //one guard at each of the four fence gaps: west, east, north, south
        int[][] guardOffsets = {
                {-6, 0}, {6, 0}, {0, -6}, {0, 6},
        };
        for (int pos : customOffsetArray(guardOffsets)) {
            PhalanxGuard guard = new PhalanxGuard();
            guard.pos = pos;
            level.mobs.add(guard);
        }
    }

    public static class PhalanxGuard extends Banshee {
        { properties.add(Property.BOSS_MINION); }
    }
}
