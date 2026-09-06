package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

//a flooded vault: the whole interior is deep water except a one-tile dry walkway around the
//edge and a dry cross running from each door to the reward pedestal at the centre. four
//piranhas patrol the water and can't leave it, so the fight is about staying on the dry paths.
public class PiranhaPoolChamber extends TrialChamber {

    {
        isBuildWithStructure = false;
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, 1, Terrain.EMPTY_SP);
        Painter.fill(level, innerRoom, 2, Terrain.WATER);
        for (int i = 0; i < 4; i++) {
            Painter.drawLine(level, doorPoint(i), center, Terrain.EMPTY_SP);
        }

        Painter.set(level, center, Terrain.PEDESTAL);

        int[][] piranhaOffsets = {{-2, -2}, {2, -2}, {-2, 2}, {2, 2}};
        for (int pos : customOffsetArray(piranhaOffsets)) {
            Piranha piranha = Piranha.random();
            piranha.pos = pos;
            level.mobs.add(piranha);
        }
    }
}
