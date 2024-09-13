package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GSoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpwSoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Sturo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tboss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TendencyTank;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBossText;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spw11 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.SUPPLY_RATION;

        icon = ItemSpriteSheet.Icons.SCROLL_MIRRORIMG;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw11);
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_LIGHT );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute( hero, action );
        if (action.equals( AC_LIGHT )) {

        }
    }

    @Override
    public String desc() {
        String[] descriptions = {
                Messages.get(this, "desc"),
                Messages.get(Spw11.class, "desc1"),
                Messages.get(Spw11.class, "desc2"),
                Messages.get(Spw11.class, "desc3"),
                Messages.get(Spw11.class, "desc4"),
                Messages.get(Spw11.class, "desc5"),
                Messages.get(Spw11.class, "desc6"),
                Messages.get(Spw11.class, "desc7")
        };

        int index = Math.min(Statistics.spw11, descriptions.length - 1);
        return descriptions[index];
    }

    public static void Spw11Ability() {

        ArrayList<Integer> spawnPoints = new ArrayList<>();

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                spawnPoints.add( p );
            }
        }

        if (!spawnPoints.isEmpty()){

            if (Statistics.spw11 == 7) {
                if (Statistics.neoroca == 0) {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndBossText(new SpwSoldier(), Messages.get(SpwSoldier.class, "t1")) {
                                @Override
                                public void hide() {
                                    super.hide();
                                }
                            });
                        }
                    });

                    TendencyTank tendencyTank = new TendencyTank();
                    tendencyTank.state = tendencyTank.HUNTING;
                    GameScene.add(tendencyTank);
                    ScrollOfTeleportation.appear(tendencyTank, Random.element(spawnPoints));
                    Statistics.neoroca = 1;
                } else {
                    SpwSoldier spwSoldier = new SpwSoldier();
                    if (Statistics.spw11 > 2) Buff.affect(spwSoldier, Barrier.class).setShield(5);
                    spwSoldier.state = spwSoldier.HUNTING;
                    GameScene.add( spwSoldier );
                    ScrollOfTeleportation.appear( spwSoldier, Random.element(spawnPoints) );
                    GLog.n(Messages.get(SpwSoldier.class, "t2"));
                }
            } else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndBossText(new SpwSoldier(), Messages.get(SpwSoldier.class, "t1")) {
                            @Override
                            public void hide() {
                                super.hide();
                            }
                        });
                    }
                });

                SpwSoldier spwSoldier = new SpwSoldier();
                if (Statistics.spw11 > 2) Buff.affect(spwSoldier, Barrier.class).setShield(5);
                spwSoldier.state = spwSoldier.HUNTING;
                GameScene.add( spwSoldier );
                ScrollOfTeleportation.appear( spwSoldier, Random.element(spawnPoints) );
                GLog.n(Messages.get(SpwSoldier.class, "t2"));
                Statistics.neoroca = 1;
            }
        } else {
            GLog.w(Messages.get(SpiritHawk.class, "no_space"));
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 20 * quantity;
    }

}
