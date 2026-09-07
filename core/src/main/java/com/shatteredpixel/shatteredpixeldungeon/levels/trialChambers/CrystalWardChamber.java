package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.watabou.utils.Point;

//the reward pedestal sits in a clean square at the centre; the whole surrounding floor out to
//the room edge is packed edge-to-edge with REVEALED ShockingTraps - visible, but with no gap.
//four Acidics stand at the cardinal edges of the clean vault. (the old enclosing wall ring lives
//in GrimWardChamber now.)
public class CrystalWardChamber extends TrialChamber {

    private static final int VAULT_RADIUS = 4; //clean square around the pedestal (Chebyshev distance)

    {
        isBuildWithStructure = false;
    }

    @Override
    public boolean blocksPursuer() {
        return true; //revealed traps everywhere outside the vault - don't strand the pursuer here
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.EMPTY);

        for (int cell : innerRoomPos()) {
            Point p = level.cellToPoint(cell);
            int cheb = Math.max(Math.abs(p.x - center.x), Math.abs(p.y - center.y));

            if (cheb <= VAULT_RADIUS) {
                continue; //clean vault floor
            }
            Painter.set(level, cell, Terrain.TRAP);
            level.setTrap(new ShockingTrap().reveal(), cell);
        }

        Painter.set(level, center, Terrain.PEDESTAL);

        int g = VAULT_RADIUS - 1;
        int[][] acidicOffsets = {{0, -g}, {0, g}, {-g, 0}, {g, 0}};
        for (int pos : customOffsetArray(acidicOffsets)) {
            Acidic acidic = new Acidic();
            acidic.pos = pos;
            level.mobs.add(acidic);
        }
    }
}
