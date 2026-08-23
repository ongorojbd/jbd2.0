package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

import java.util.ArrayList;

//self-contained version of the same "endless vent" trick TempleNewLevel's ConfusionChamber
//uses: dozens of decorative, permanently-inactive FrostVent traps each seed a BlizzardSeed
//blob, which re-triggers real Blizzard at its own vent tile every evolve tick forever - so
//unlike an ordinary blizzard that eventually clears, this room never stops being a whiteout.
//No monsters here; the blizzard itself is the obstacle.
public class BlizzardVentChamber extends TrialChamber {

    {
        isBuildWithStructure = false;
    }

    @Override
    public void build() {
        super.build();

        Painter.fill(level, innerRoom, Terrain.EMPTY);
        Painter.set(level, center, Terrain.PEDESTAL);

        int[][] shrineOffsets = {{-7, -7}, {7, -7}, {-7, 7}, {7, 7}};
        for (int pos : customOffsetArray(shrineOffsets)) {
            Painter.set(level, pos, Terrain.STATUE_SP);
        }

        //keep the 4 doors, the shrine statues, and the reward pedestal clear - everything else
        //is fair game for a vent
        int[][] exceptionOffsets = {
                {-1, -8}, {0, -8}, {1, -8}, {-1, -7}, {0, -7}, {1, -7},
                {-1, 8}, {0, 8}, {1, 8}, {-1, 7}, {0, 7}, {1, 7},
                {-8, -1}, {-8, 0}, {-8, 1}, {-7, -1}, {-7, 0}, {-7, 1},
                {8, -1}, {8, 0}, {8, 1}, {7, -1}, {7, 0}, {7, 1},
                {-1, -1}, {0, -1}, {1, -1},
                {-1, 0}, {0, 0}, {1, 0},
                {-1, 1}, {0, 1}, {1, 1},
                {-7, -7}, {7, -7}, {-7, 7}, {7, 7},
        };
        ArrayList<Integer> exceptions = new ArrayList<>(customOffsetArray(exceptionOffsets));

        final int VENT_COUNT = 55;
        for (int pos : randomRoomPos(VENT_COUNT, exceptions)) {
            level.setTrap(new FrostVent().reveal(), pos);
            Blob.seed(pos, 20, BlizzardSeed.class, level);
            Painter.set(level, pos, Terrain.INACTIVE_TRAP);
        }
    }

    public static class BlizzardSeed extends Blob {

        @Override
        protected void evolve() {
            int cell;
            ToxicGas bliz = (ToxicGas) Dungeon.level.blobs.get(ToxicGas.class);
            for (int i = area.top - 1; i <= area.bottom; i++) {
                for (int j = area.left - 1; j <= area.right; j++) {
                    cell = j + i * Dungeon.level.width();
                    if (Dungeon.level.insideMap(cell)) {
                        if (Dungeon.level.map[cell] != Terrain.INACTIVE_TRAP) {
                            off[cell] = 0;
                            continue;
                        }

                        off[cell] = cur[cell];
                        volume += off[cell];

                        if (bliz == null || bliz.volume == 0) {
                            GameScene.add(Blob.seed(cell, off[cell], ToxicGas.class));
                        } else if (bliz.cur[cell] <= 9 * off[cell]) {
                            GameScene.add(Blob.seed(cell, off[cell], ToxicGas.class));
                        }
                    }
                }
            }
        }
    }

    public static class FrostVent extends Trap {

        {
            color = WHITE;
            shape = GRILL;

            canBeHidden = false;
            active = false;
        }

        @Override
        public void activate() {
            //does nothing, this trap is just decoration and is always deactivated
        }
    }
}
