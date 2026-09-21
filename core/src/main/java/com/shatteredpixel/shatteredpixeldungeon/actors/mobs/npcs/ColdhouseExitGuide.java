package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AmblanceSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CivilSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

//stands where the stairs down used to be in ColdhouseRecoveryLevel. Talking to her takes over the
//tile-triggered activateTransition effect the old EXIT terrain used to provide - the LevelTransition
//itself is untouched (still registered in Level.transitions, still what the compass points at),
//only the way it gets fired changes from "step on the tile" to "talk to the guide".
public class ColdhouseExitGuide extends NPC {

    {
        spriteClass = AmblanceSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
        //cannot be harmed
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        new YasuSprite(),
                        Messages.get(Yasu.class, "name"),
                        Messages.get(ColdhouseExitGuide.class, "0"),
                        Messages.get(ColdhouseExitGuide.class, "1"),
                        Messages.get(ColdhouseExitGuide.class, "2")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            LevelTransition transition = Dungeon.level.getTransition(LevelTransition.Type.REGULAR_EXIT);
                            if (transition != null) {
                                Dungeon.level.activateTransition(Dungeon.hero, transition);
                            }
                        }
                    }
                });
            }
        });

        return true;
    }
}
