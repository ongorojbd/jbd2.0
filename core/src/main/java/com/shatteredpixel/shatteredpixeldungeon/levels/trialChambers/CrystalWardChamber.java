package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalGuardian;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.watabou.utils.Point;

import java.util.ArrayList;

//a square ring of wall guards an inner vault with two guarded gaps (north/south); the east/west
//gaps are open but unguarded, rewarding careful pathing over brute force. the vault interior
//(inside the ring, including the ring plane itself and its gaps) stays clean - the entire outer
//annulus beyond the ring is packed edge-to-edge with hidden ShockingTraps instead.
public class CrystalWardChamber extends TrialChamber {

    private static final int RING_RADIUS = 4;

    {
        isBuildWithStructure = false;
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.EMPTY);

        ArrayList<Integer> vaultFloor = new ArrayList<>();
        for (int dx = -RING_RADIUS; dx <= RING_RADIUS; dx++) {
            for (int dy = -RING_RADIUS; dy <= RING_RADIUS; dy++) {
                int cell = level.pointToCell(new Point(center.x + dx, center.y + dy));
                //cardinal midpoints are left open as the vault's 4 gaps
                if (Math.max(Math.abs(dx), Math.abs(dy)) == RING_RADIUS && dx != 0 && dy != 0) {
                    Painter.set(level, cell, Terrain.WALL);
                }
                //the whole ring plane (including gaps and the wall itself) counts as "inside"
                vaultFloor.add(cell);
            }
        }

        for (int cell : innerRoomPos()) {
            if (vaultFloor.contains(cell)) {
                continue;
            }
            Painter.set(level, cell, Terrain.SECRET_TRAP);
            level.setTrap(new ShockingTrap().hide(), cell);
        }

        Painter.set(level, center, Terrain.PEDESTAL);

        int centerCell = level.pointToCell(center);
        int[][] guardOffsets = {{0, -RING_RADIUS + 1}, {0, RING_RADIUS - 1}};
        for (int pos : customOffsetArray(guardOffsets)) {
            WardGuardian guardian = new WardGuardian();
            guardian.pos = pos;
            level.mobs.add(guardian);
        }

        for (int i = 0; i < 2; i++) {
            level.drop(Generator.randomUsingDefaults(Generator.Category.ARTIFACT), centerCell);
        }
    }

    public static class WardGuardian extends CrystalGuardian {
        { properties.add(Property.BOSS_MINION); }
    }
}
