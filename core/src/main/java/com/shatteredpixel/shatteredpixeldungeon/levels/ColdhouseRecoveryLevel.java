package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ColdhouseExitGuide;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RecoveryWardNurse;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Bmap;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

/*
    The floor right after ColdhouseBossLevel (branch 0, depth 3): a quiet hospital waiting room.
    No monsters and no way to fall - the interior is plain EMPTY floor speckled with EMPTY_DECO
    tiles, with furniture scattered around it (rows of waiting chairs, potted plants, marble
    pillars). A RecoveryWardNurse stands between the entrance and the way down; talk to her once
    to pick a single parting gift - a Wand of Regrowth or an Ankh - then head down into the sewers.
*/
public class ColdhouseRecoveryLevel extends Level {

    private static final int WIDTH = 21;
    private static final int HEIGHT = 25;

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
        Music.INSTANCE.play(Assets.Music.TG_1, true);
    }

    //hand-built arena: never take part in the random level feeling roll (a CHASM feeling would
    //turn every un-painted wall into a pit - see Level.assignLevelFeeling)
    @Override
    protected boolean assignLevelFeeling() {
        return false;
    }

    private int entranceCell() {
        return WIDTH / 2 + (HEIGHT - 2) * WIDTH;
    }

    private int exitCell() {
        return WIDTH / 2 + 1 * WIDTH;
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
        //no visible stairs here - the LevelTransition is still registered below (so the compass
        //and level.exit() still resolve normally), it's just no longer tied to a stair tile.
        //ColdhouseExitGuide, standing on this cell, fires it herself when talked to.
        map[exit] = Terrain.EMPTY;
        transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
        transitions.add(new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT));

        RecoveryWardNurse nurse = new RecoveryWardNurse();
        nurse.pos = WIDTH / 2 + 5 * WIDTH;
        mobs.add(nurse);

        ColdhouseExitGuide exitGuide = new ColdhouseExitGuide();
        exitGuide.pos = exit;
        mobs.add(exitGuide);

        return true;
    }

    //rows of waiting chairs on either side of a central aisle, pillars along the walls, and
    //potted plants in the corners - everything solid so the hero walks around it, nothing that
    //blocks the entrance<->exit path
    private void decorate() {
        //marble pillars down both side walls
        for (int y : new int[]{3, 8, 13, 18, 22}) {
            Painter.set(this, 2 + y * WIDTH, Terrain.STATUE_SP);
            Painter.set(this, WIDTH - 3 + y * WIDTH, Terrain.STATUE_SP);
        }

        //potted plants in the four interior corners
        Painter.set(this, 1 + 1 * WIDTH, Terrain.EMPTY);
        Painter.set(this, WIDTH - 2 + 1 * WIDTH, Terrain.EMPTY);
        Painter.set(this, 1 + (HEIGHT - 2) * WIDTH, Terrain.EMPTY);
        Painter.set(this, WIDTH - 2 + (HEIGHT - 2) * WIDTH, Terrain.EMPTY);

        //three blocks of seating, a left bank and a right bank each, facing the central aisle
        for (int rowTop : new int[]{10, 14, 18}) {
            for (int dy = 0; dy < 2; dy++) {
                int y = rowTop + dy;
                for (int x = 4; x <= 8; x++) {
                    Painter.set(this, x + y * WIDTH, Terrain.REGION_DECO);
                }
                for (int x = WIDTH - 9; x <= WIDTH - 5; x++) {
                    Painter.set(this, x + y * WIDTH, Terrain.REGION_DECO);
                }
            }
        }
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.REGION_DECO:
                return Messages.get(ColdhouseRecoveryLevel.class, "region_deco_name");
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.REGION_DECO:
                return Messages.get(ColdhouseRecoveryLevel.class, "region_deco_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    //no going back up - the waiting room only opens onto the way down. The exit used to end
    //the branch here with a "next floor still under construction" prompt (see
    //ColdhouseLoungeLevel, which now carries that prompt instead) - a plain REGULAR_EXIT now
    //just descends normally into depth 23.
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
