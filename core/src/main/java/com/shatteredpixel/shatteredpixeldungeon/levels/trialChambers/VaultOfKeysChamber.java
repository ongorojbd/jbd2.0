package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Banshee;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Neoro;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

//inspired by the vanilla CrystalPathRoom: 4 small vaults, one on each cardinal side of the
//room, each sealed behind its own Terrain.CRYSTAL_DOOR, with one CrystalKey lying loose per
//vault, so a thorough sweep of the room opens all 4. A single warden stands on the room's
//centre; the main challenge is still finding all the keys. One of the 4 chests (picked at
//random) is the one that carries the sanctum code fragment when this room is chosen to hold one.
public class VaultOfKeysChamber extends TrialChamber {

    private final ArrayList<Integer> vaultCells = new ArrayList<>();

    {
        isBuildWithStructure = false;
    }

    @Override
    public void build() {
        super.build();

        //the base pedestal isn't used by this room - the reward is in the vaults instead
        Painter.set(level, center, Terrain.EMPTY);

        //every non-door cell touching the pedestal (including diagonals) has to be a wall -
        //leaving a diagonal gap lets the hero cut straight to the chest around the door
        buildVault(0, -6, new int[][]{{-1, -7}, {0, -7}, {1, -7}, {-1, -6}, {1, -6}, {-1, -5}, {1, -5}}, 0, -5);
        buildVault(0, 6, new int[][]{{-1, 5}, {1, 5}, {-1, 6}, {1, 6}, {-1, 7}, {0, 7}, {1, 7}}, 0, 5);
        buildVault(6, 0, new int[][]{{5, -1}, {5, 1}, {6, -1}, {6, 1}, {7, -1}, {7, 0}, {7, 1}}, 5, 0);
        buildVault(-6, 0, new int[][]{{-5, -1}, {-5, 1}, {-6, -1}, {-6, 1}, {-7, -1}, {-7, 0}, {-7, 1}}, -5, 0);

        int[][] keySpots = {{-2, -2}, {2, -2}, {2, 2}, {-2, 2}};
        for (int pos : customOffsetArray(keySpots)) {
            level.drop(new CrystalKey(Dungeon.depth), pos);
        }

        //a single warden standing on the room's centre, between the four vaults
        VaultWarden warden = new VaultWarden();
        warden.pos = level.pointToCell(center);
        level.mobs.add(warden);
    }

    public static class VaultWarden extends Banshee {
        { properties.add(Property.BOSS_MINION); }
    }

    //carves a small pocket at (ox,oy) walled in on wallOffsets, with a crystal door at
    //(doorOx,doorOy) sealing it off from the room's open floor, and a pedestal inside
    private void buildVault(int ox, int oy, int[][] wallOffsets, int doorOx, int doorOy) {
        int pedestalCell = level.pointToCell(new Point(center.x + ox, center.y + oy));
        Painter.set(level, center.x + ox, center.y + oy, Terrain.PEDESTAL);
        vaultCells.add(pedestalCell);

        for (int[] w : wallOffsets) {
            Painter.set(level, center.x + w[0], center.y + w[1], Terrain.WALL);
        }

        Painter.set(level, center.x + doorOx, center.y + doorOy, Terrain.CRYSTAL_DOOR);
    }

    @Override
    public void placeRewards() {
        ArrayList<Integer> cells = new ArrayList<>(vaultCells);

        if (bonusReward != null) {
            int fragmentCell = Random.element(cells);
            cells.remove(Integer.valueOf(fragmentCell));
            level.drop(bonusReward, fragmentCell).type = Heap.Type.CHEST;
        }

        for (int cell : cells) {
            level.drop(new Neoro(), cell).type = Heap.Type.CHEST;
        }
    }
}
