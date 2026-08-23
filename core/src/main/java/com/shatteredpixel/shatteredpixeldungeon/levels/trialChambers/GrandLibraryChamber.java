package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

//a pinwheel of 4 collapsed archive wings instead of a plain cross-lattice: each wing is a
//bookshelf run that gives way to a barricade of fallen rubble where the ceiling caved in, with
//a reading chair (Terrain.REGION_DECO) tucked in the nook behind it. The 4 wings are rotations
//of the same arm, so the room reads as a proper spiral of stacks rather than a symmetric grid.
//3 teleporting Golems (the same "library keeper" reused from the old forge chamber) roam the
//outer aisles between wings.
public class GrandLibraryChamber extends TrialChamber {

    {
        isBuildWithStructure = false;
    }

    //one wing's cells, before rotation - a bookshelf run that turns into a barricaded cave-in,
    //with a chair in the pocket it creates
    private static final int[][] WING_BOOKSHELF = {
            {1, -6}, {2, -6}, {3, -6}, {4, -6}, {5, -6},
    };
    private static final int[][] WING_BARRICADE = {
            {5, -5}, {5, -4}, {5, -3},
    };
    private static final int[][] WING_CHAIR = {
            {3, -4},
    };

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, 1, Terrain.BOOKSHELF);
        Painter.fill(level, innerRoom, 2, Terrain.EMPTY);

        //punch a gap through the shelf ring at each of the 4 doors so they actually connect
        //into the stacks
        Painter.set(level, topDoor.x, topDoor.y + 1, Terrain.EMPTY);
        Painter.set(level, bottomDoor.x, bottomDoor.y - 1, Terrain.EMPTY);
        Painter.set(level, leftDoor.x + 1, leftDoor.y, Terrain.EMPTY);
        Painter.set(level, rightDoor.x - 1, rightDoor.y, Terrain.EMPTY);

        for (int rot = 0; rot < 4; rot++) {
            for (int[] cell : WING_BOOKSHELF) {
                int[] p = rotate(cell[0], cell[1], rot);
                Painter.set(level, center.x + p[0], center.y + p[1], Terrain.BOOKSHELF);
            }
            for (int[] cell : WING_BARRICADE) {
                int[] p = rotate(cell[0], cell[1], rot);
                Painter.set(level, center.x + p[0], center.y + p[1], Terrain.BARRICADE);
            }
            for (int[] cell : WING_CHAIR) {
                int[] p = rotate(cell[0], cell[1], rot);
                Painter.set(level, center.x + p[0], center.y + p[1], Terrain.REGION_DECO);
            }
        }

        Painter.set(level, center, Terrain.PEDESTAL);

        int[][] golemSpots = {{7, 7}, {-7, -7}, {7, -7}};
        for (int pos : customOffsetArray(golemSpots)) {
            LibraryGolem golem = new LibraryGolem();
            golem.pos = pos;
            level.mobs.add(golem);
        }
    }

    //rotates (dx,dy) by 90 degrees, `times` times, around the origin
    private int[] rotate(int dx, int dy, int times) {
        for (int i = 0; i < times; i++) {
            int ndx = -dy;
            int ndy = dx;
            dx = ndx;
            dy = ndy;
        }
        return new int[]{dx, dy};
    }

    public static class LibraryGolem extends Golem {
        { properties.add(Property.BOSS_MINION); }
    }
}
