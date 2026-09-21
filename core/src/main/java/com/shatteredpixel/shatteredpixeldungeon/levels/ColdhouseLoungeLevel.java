package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SpecialVendingMachine;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yasu;
import com.shatteredpixel.shatteredpixeldungeon.items.BossChallengeTester;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.VendingMachineShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

/*
    The floor right after ColdhouseRecoveryLevel (branch 2, depth 23): the hospital's lounge,
    one hall down from the waiting room. Laid out symmetrically around a central aisle running
    entrance to exit - windows spaced evenly along all four walls, marble pillars framing both
    doorways, planters in each corner, a U of seating on the left, and the vending corner on the
    right: machines lined up along the wall with the kiosk's stock (see VendingMachineShopRoom)
    laid out in front of them, in plain view of the entrance. No monsters, same as the floor
    above. This is currently the end of the branch's built content, so the exit reuses
    ColdhouseRecoveryLevel's old "next floor still under construction" prompt to send the hero
    back to depth 26 of the main dungeon.
*/
public class ColdhouseLoungeLevel extends Level {

    private static final int WIDTH = 19;
    private static final int HEIGHT = 17;

    {
        color1 = 0x535353;
        color2 = 0xa8a8a8;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TG;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.play(Assets.Music.EMPO, true);
    }

    //hand-built arena: never take part in the random level feeling roll (a CHASM feeling would
    //turn every un-painted wall into a pit - see Level.assignLevelFeeling)
    @Override
    protected boolean assignLevelFeeling() {
        return false;
    }

    private int entranceCell() {
        return WIDTH / 2 + 2 * WIDTH;
    }

    private int exitCell() {
        return WIDTH / 2 + (HEIGHT - 3) * WIDTH;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        Painter.fill(this, new Rect(0, 0, WIDTH, HEIGHT), Terrain.WALL);
        Painter.fill(this, new Rect(0, 0, WIDTH, HEIGHT), 1, Terrain.EMPTY);

        decorate();

        int entrance = entranceCell();
        int exit = exitCell();
        map[entrance] = Terrain.ENTRANCE;
        map[exit] = Terrain.EXIT;
        transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
        transitions.add(new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT));

        placeYasu(exit);

        return true;
    }

    //Yasu, standing just off the exit stairs, one tile up and one tile to the left of them
    private void placeYasu(int exit) {
        Yasu npc = new Yasu();
        npc.pos = exit - WIDTH - 1;
        mobs.add(npc);
    }

    //everything solid here is furniture the hero walks around; the central aisle from the
    //entrance straight down to the exit is deliberately left clear
    private void decorate() {
        paintWindows();
        paintPillars();
        paintSeating();
        paintVendingCorner();
        paintFloor();
    }

    //a sparse, irregular scatter of clutter over whatever plain floor is left, the kiosk's
    //included. Runs last so the kiosk's own floor pass doesn't wipe it out.
    private void paintFloor() {
        for (int cell = 0; cell < length(); cell++) {
            if (map[cell] == Terrain.EMPTY && Random.Float() < 0.04f) {
                map[cell] = Terrain.EMPTY_DECO;
            }
        }
    }

    //evenly spaced windows down all four walls - the main thing making the room read as a
    //built interior rather than a carved-out box
    private void paintWindows() {
        for (int x : new int[]{4, WIDTH / 2, WIDTH - 5}) {
            Painter.set(this, x, Terrain.WALL_DECO);
            Painter.set(this, x + (HEIGHT - 1) * WIDTH, Terrain.WALL_DECO);
        }
        //clear of the vending machines along the right wall, so neither side covers a window
        for (int y : new int[]{3, HEIGHT - 4}) {
            Painter.set(this, y * WIDTH, Terrain.WALL_DECO);
            Painter.set(this, WIDTH - 1 + y * WIDTH, Terrain.WALL_DECO);
        }
    }

    //a pair of pillars framing each doorway
    private void paintPillars() {
        for (int x : new int[]{WIDTH / 2 - 2, WIDTH / 2 + 2}) {
            Painter.set(this, x + 2 * WIDTH, Terrain.STATUE_SP);
            Painter.set(this, x + (HEIGHT - 3) * WIDTH, Terrain.STATUE_SP);
        }
    }

    //two identical seating pods down the left side, mirrored about the room's midline: a pair
    //of facing rows with a low planter between them standing in for the table
    private void paintSeating() {
        for (int top : new int[]{4, HEIGHT - 7}) {
            for (int x = 3; x <= 6; x++) {
                Painter.set(this, x + top * WIDTH, Terrain.REGION_DECO);
                Painter.set(this, x + (top + 2) * WIDTH, Terrain.REGION_DECO);
            }
            Painter.set(this, 4 + (top + 1) * WIDTH, Terrain.GRASS);
            Painter.set(this, 5 + (top + 1) * WIDTH, Terrain.GRASS);
        }
    }

    //vending machines lined up against the right wall, with the kiosk's stock spread out on
    //the open floor in front of them - no walls around any of it, so it's visible on entry
    private void paintVendingCorner() {
        for (int y = 4; y <= HEIGHT - 5; y++) {
            Painter.set(this, WIDTH - 2 + y * WIDTH, Terrain.BOOKSHELF);
        }

        //the odd one out stands at the head of the row, on open floor so it can be talked to
        SpecialVendingMachine slots = new SpecialVendingMachine();
        slots.pos = WIDTH - 7 + 8 * WIDTH;
        mobs.add(slots);

        //sized so the stock forms a ring one tile in from the edges with the shopkeeper standing
        //in the hollow at its centre - i.e. behind a counter of goods, machines at their back.
        //ShopRoom.placeItems() only ever fills that one ring before giving up and scattering the
        //rest, so the rect has to be big enough for the whole stock list to fit around it.
//        VendingMachineShopRoom kiosk = new VendingMachineShopRoom();
//        kiosk.set(11, 3, WIDTH - 3, HEIGHT - 4);
//        kiosk.paint(this);
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.REGION_DECO:
                return Messages.get(ColdhouseLoungeLevel.class, "region_deco_name");
            case Terrain.BOOKSHELF:
                return Messages.get(ColdhouseLoungeLevel.class, "bookshelf_name");
            case Terrain.STATUE_SP:
                return Messages.get(ColdhouseLoungeLevel.class, "statue_sp_name");
            case Terrain.GRASS:
            case Terrain.HIGH_GRASS:
                return Messages.get(ColdhouseLoungeLevel.class, "grass_name");
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.REGION_DECO:
                return Messages.get(ColdhouseLoungeLevel.class, "region_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(ColdhouseLoungeLevel.class, "bookshelf_desc");
            case Terrain.STATUE_SP:
                return Messages.get(ColdhouseLoungeLevel.class, "statue_sp_desc");
            case Terrain.GRASS:
            case Terrain.HIGH_GRASS:
                return Messages.get(ColdhouseLoungeLevel.class, "grass_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    //no going back up - same one-way rule as the rest of this branch
    @Override
    public boolean activateTransition(final Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE
                || transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndMessage(Messages.get(hero, "tendency2")));
                }
            });
            return false;
        }
        if (transition.type == LevelTransition.Type.REGULAR_EXIT) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.TG),
                            Messages.get(BossChallengeTester.class, "1"),
                            Messages.get(BossChallengeTester.class, "2"),
                            Messages.get(BossChallengeTester.class, "3"),
                            Messages.get(BossChallengeTester.class, "4")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                                InterlevelScene.returnDepth = 26;
                                InterlevelScene.returnBranch = 0;
                                InterlevelScene.returnPos = -1;
                                Game.switchScene(InterlevelScene.class);
                            }
                        }
                    });
                }
            });
            //기본 하강 전환을 막는다 - 실제 이동은 위 창의 onSelect가 담당
            return false;
        }
        return super.activateTransition(hero, transition);
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
    }

    @Override
    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos = entrance() - width(); //one tile in from the entrance, always open floor here
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    @Override
    public int randomRespawnCell(Char ch) {
        int cell;
        int tries = 30;
        do {
            cell = entrance() + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while ((!passable[cell] || Actor.findChar(cell) != null) && tries-- > 0);
        return passable[cell] ? cell : entrance();
    }
}
