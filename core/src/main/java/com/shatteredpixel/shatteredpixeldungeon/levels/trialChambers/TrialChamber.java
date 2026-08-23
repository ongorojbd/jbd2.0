package com.shatteredpixel.shatteredpixeldungeon.levels.trialChambers;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

//base class for a single high-difficulty room inside VeiledSanctumLevel's 3x3 grid.
//unlike TempleNewLevel's Chamber, the room dimensions are passed into set() instead of being
//statically imported from the owning level, so this package doesn't depend on that level at all.
public class TrialChamber {

    public Level level;
    public Point center;
    public Rect innerRoom;
    private Rect outerRoom;
    public Point topDoor;
    public Point bottomDoor;
    public Point leftDoor;
    public Point rightDoor;
    public int innerWidth;
    public int innerHeight;
    private ArrayList<Integer> innerRoomPos = new ArrayList<>();
    public boolean isBuildWithStructure = false;

    //set by the owning level before placeRewards() is called, if this room was chosen to carry
    //a piece of the sanctum code. null for a normal room.
    public Item bonusReward;

    public void set(Level level, int left, int top, int right, int bottom, Point center, int innerWidth, int innerHeight) {
        this.level = level;
        this.center = center;
        this.innerWidth = innerWidth;
        this.innerHeight = innerHeight;
        this.outerRoom = new Rect(left, top, right, bottom);
        this.innerRoom = new Rect(left + 1, top + 1, right - 1, bottom - 1);

        Point leftTopPoint = new Point(center.x - (innerWidth / 2 - 1), center.y - (innerHeight / 2));
        for (int y = 0; y < innerHeight; y++) {
            for (int x = 0; x < innerWidth; x++) {
                Point p = new Point((leftTopPoint.x - 1) + x, leftTopPoint.y + y);
                innerRoomPos.add(this.level.pointToCell(p));
            }
        }

        this.topDoor    = new Point(center.x, center.y - innerHeight / 2);
        this.bottomDoor = new Point(center.x, center.y + innerHeight / 2);
        this.leftDoor   = new Point(center.x - innerWidth / 2, center.y);
        this.rightDoor  = new Point(center.x + innerWidth / 2, center.y);
    }

    public int[] roomStructure() {
        return new int[]{}; //override when isBuildWithStructure is true
    }

    public void build() {
        Painter.fill(level, outerRoom, Terrain.WALL);
        Painter.fill(level, outerRoom, 1, Terrain.EMPTY);
        Painter.set(level, center, Terrain.PEDESTAL);

        if (isBuildWithStructure) {
            int index = 0;
            try {
                for (int pos : innerRoomPos) {
                    if (roomStructure()[index] != -1) level.map[pos] = roomStructure()[index];
                    index++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    //called by the level once for every room, after build(). Default behaviour drops a small
    //pile of loot on the room's own pedestal, plus bonusReward (if set) as a chest. Rooms whose
    //gimmick is its own reward layout (eg. several separately-locked chests) should override this
    //instead of relying on the generic center-pedestal drop.
    public void placeRewards() {
        int cell = level.pointToCell(center);
        Heap heap = null;
        int n = Random.IntRange(2, 3);
        for (int i = 0; i < n; i++) {
            heap = level.drop(trialPrizeItem(), cell);
        }
        heap = level.drop(eliteTrialPrizeItem(), cell);

        if (bonusReward != null) {
            heap = level.drop(bonusReward, cell);
            heap.type = Heap.Type.CHEST;
        } else {
            heap.setHauntedIfCursed().type = Heap.Type.SKELETON;
        }
    }

    protected static Item trialPrizeItem() {
        return Generator.randomUsingDefaults(Random.oneOf(
                Generator.Category.POTION,
                Generator.Category.SCROLL,
                Generator.Category.FOOD,
                Generator.Category.GOLD
        ));
    }

    protected static Item eliteTrialPrizeItem() {
        Item item;
        do {
            switch (Random.Int(3)) {
                case 0:
                    item = Generator.randomUsingDefaults(Generator.Category.RING);
                    break;
                case 1:
                    item = Generator.randomUsingDefaults(Generator.Category.ARTIFACT);
                    break;
                default:
                    item = Generator.randomUsingDefaults(Random.oneOf(
                            Generator.Category.WEAPON,
                            Generator.Category.ARMOR));
                    break;
            }
        } while (item == null || Challenges.isItemBlocked(item));
        return item;
    }

    public ArrayList<Integer> innerRoomPos() {
        return this.innerRoomPos;
    }

    public ArrayList<Integer> randomRoomPos(int num) {
        ArrayList<Integer> result = new ArrayList<>();
        while (result.size() < num) {
            int randomResult = Random.element(innerRoomPos);
            if (!result.contains(randomResult)) {
                result.add(randomResult);
            }
        }
        return result;
    }

    public ArrayList<Integer> randomRoomPos(int num, ArrayList<Integer> exception) {
        ArrayList<Integer> result = new ArrayList<>();
        while (result.size() < num) {
            int randomResult = Random.element(innerRoomPos);
            if (!result.contains(randomResult) && !exception.contains(randomResult)) {
                result.add(randomResult);
            }
        }
        return result;
    }

    public ArrayList<Integer> customOffsetArray(int[][] offsets) {
        ArrayList<Integer> resultArray = new ArrayList<>();
        for (int[] offset : offsets) {
            resultArray.add(level.pointToCell(new Point(center.x + offset[0], center.y + offset[1])));
        }
        return resultArray;
    }

    public Point doorPoint(int direction) {
        //direction: 0=top, 1=left, 2=bottom, 3=right, other=top (counterclockwise)
        switch (direction) {
            default: case 0:
                return new Point(center.x, center.y - innerHeight / 2);
            case 1:
                return new Point(center.x - innerWidth / 2, center.y);
            case 2:
                return new Point(center.x, center.y + innerHeight / 2);
            case 3:
                return new Point(center.x + innerWidth / 2, center.y);
        }
    }
}
