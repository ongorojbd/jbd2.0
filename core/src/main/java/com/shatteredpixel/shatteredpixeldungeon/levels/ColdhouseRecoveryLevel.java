package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RecoveryWardNurse;
import com.shatteredpixel.shatteredpixeldungeon.items.BossChallengeTester;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Bmap;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
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
        return Assets.Environment.TILES_SEWERS;
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
        map[exit] = Terrain.EXIT;
        transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
        transitions.add(new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT));

        RecoveryWardNurse nurse = new RecoveryWardNurse();
        nurse.pos = WIDTH / 2 + 5 * WIDTH;
        mobs.add(nurse);

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

    //no going back up - the waiting room only opens onto the way down
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
