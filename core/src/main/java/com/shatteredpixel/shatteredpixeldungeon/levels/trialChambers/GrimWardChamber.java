package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Banshee;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.watabou.utils.Point;

//a far more lethal cousin of CrystalWardChamber: two concentric square outlines centred on the
//reward pedestal, each one tile thick, measured by Chebyshev distance from the centre.
//  - small ring (distance 2)        : a REVEALED GrimTrap outline, 16 tiles, guarding the pedestal
//  - large ring (distance 6)        : a REVEALED GrimTrap outline guarding the way in
//everything else - the pedestal, the gap out to the small ring, the safe band between the rings
//(where two guards stand), and the outer strip by the doors - is clean floor.
//unlike CrystalWard the traps are drawn (not SECRET_TRAP) and are the instant-kill GrimTrap kind
//instead of ShockingTrap. neither ring has a gap, so crossing either one - the doors inward, or
//the safe band to the pedestal - means stepping onto a single GrimTrap.
public class GrimWardChamber extends TrialChamber {

    private static final int SMALL_RING = 2; //inner GrimTrap outline: Chebyshev distance from centre
    private static final int LARGE_RING = 6; //outer GrimTrap outline: Chebyshev distance from centre

    {
        isBuildWithStructure = false;
    }

    @Override
    public boolean blocksPursuer() {
        return true; //the revealed GrimTrap rings wall a walking pursuer in
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.EMPTY);

        for (int cell : innerRoomPos()) {
            Point p = level.cellToPoint(cell);
            int cheb = Math.max(Math.abs(p.x - center.x), Math.abs(p.y - center.y));

            if (cheb == SMALL_RING || cheb == LARGE_RING) {
                Painter.set(level, cell, Terrain.TRAP);
                level.setTrap(new GrimTrap().reveal(), cell);
            }
        }

        Painter.set(level, center, Terrain.PEDESTAL);

        int[][] guardOffsets = {{0, -4}, {0, 4}};
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
