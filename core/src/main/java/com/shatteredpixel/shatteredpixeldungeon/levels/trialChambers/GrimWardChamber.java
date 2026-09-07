package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Banshee;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.watabou.utils.Point;

//a lethal vault, three concentric square outlines around the reward pedestal (Chebyshev distance):
//  - inner ring (dist 2) : a REVEALED GrimTrap outline hugging the pedestal
//  - wall ring  (dist 6) : a WALL square with the 4 cardinal midpoints left open as gaps;
//                          the north/south gaps are each held by a guard, east/west are open
//  - outer ring (dist 7) : a REVEALED GrimTrap outline wrapping the wall ring
//so both entering the room (outer ring) and reaching the reward (inner ring) cost one GrimTrap.
//(the wall ring used to belong to CrystalWardChamber.)
public class GrimWardChamber extends TrialChamber {

    private static final int TRAP_RING       = 2; //inner GrimTrap outline (Chebyshev distance from centre)
    private static final int WALL_RING       = 6; //WALL square, cardinal midpoints left open as gaps
    private static final int OUTER_TRAP_RING = 7; //GrimTrap outline wrapping the wall ring

    {
        isBuildWithStructure = false;
    }

    @Override
    public boolean blocksPursuer() {
        return true; //the revealed GrimTrap ring + wall ring wall a walking pursuer in
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.EMPTY);

        for (int cell : innerRoomPos()) {
            Point p = level.cellToPoint(cell);
            int dx = p.x - center.x;
            int dy = p.y - center.y;
            int cheb = Math.max(Math.abs(dx), Math.abs(dy));

            if (cheb == TRAP_RING || cheb == OUTER_TRAP_RING) {
                Painter.set(level, cell, Terrain.TRAP);
                level.setTrap(new GrimTrap().reveal(), cell);
            } else if (cheb == WALL_RING && dx != 0 && dy != 0) {
                //wall ring, with the 4 cardinal midpoints left open as gaps
                Painter.set(level, cell, Terrain.WALL);
            }
        }

        Painter.set(level, center, Terrain.PEDESTAL);

        //one guard just inside each of the wall ring's north/south gaps
        int[][] guardOffsets = {{0, -WALL_RING + 1}, {0, WALL_RING - 1}};
        for (int pos : customOffsetArray(guardOffsets)) {
            GrimWardGuardian guardian = new GrimWardGuardian();
            guardian.pos = pos;
            level.mobs.add(guardian);
        }
    }

    public static class GrimWardGuardian extends Banshee {
        { properties.add(Property.BOSS_MINION); }
    }
}
