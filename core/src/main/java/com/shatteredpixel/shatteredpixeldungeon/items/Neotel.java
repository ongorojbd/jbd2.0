package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing.cleanse;
import static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping.discover;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AnkhInvulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DemonSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Genkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Jonny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mandom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mih;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newcmoon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newgenkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Vitaminc;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Whsnake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Whsnake2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;


import java.util.ArrayList;
//This is from Elemental PD

public class Neotel extends Item {

    String AC_TELEPORT = "teleport";
    String AC_RETURN = "return";

    {
        defaultAction = AC_TELEPORT;
        image = ItemSpriteSheet.ANKH;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_TELEPORT);
        actions.add(AC_RETURN);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);
        if (action.equals(AC_TELEPORT)) {

            Buff.affect(hero, MindVision.class, 99f);
            Buff.affect(hero, MagicalSight.class, 99f);
            Buff.affect(hero, FeatherFall.FeatherBuff.class, 99f);
            Buff.affect(hero, Awareness.class, 99f);



            Buff.affect(hero, PotionOfCleansing.Cleanse.class, 99f);
            Buff.affect(hero, AnkhInvulnerability.class, 99f);
            hero.HP = Math.min(hero.HP + 150, hero.HT);
            Buff.affect(hero, Invisibility.class, 99f);

            Chasm.heroFall(hero.pos);
            int length = Dungeon.level.length();
            int[] map = Dungeon.level.map;
            boolean[] mapped = Dungeon.level.mapped;
            boolean[] discoverable = Dungeon.level.discoverable;

            for (int i=0; i < length; i++) {

                int terr = map[i];

                if (discoverable[i]) {

                    mapped[i] = true;
                    if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                        Dungeon.level.discover( i );

                        if (Dungeon.level.heroFOV[i]) {
                            GameScene.discoverTile( i, terr );
                            discover( i );
                        }
                    }
                }
            }
            GameScene.updateFog();
        }
        if (action.equals(AC_RETURN)) {
            InterlevelScene.mode = InterlevelScene.Mode.RETURN;
            InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1));
            InterlevelScene.returnBranch = 0;
            InterlevelScene.returnPos = -2;
            Game.switchScene( InterlevelScene.class );
        }
        int length = Dungeon.level.length();
        int[] map = Dungeon.level.map;
        boolean[] mapped = Dungeon.level.mapped;
        boolean[] discoverable = Dungeon.level.discoverable;

        for (int i=0; i < length; i++) {

            int terr = map[i];

            if (discoverable[i]) {

                mapped[i] = true;
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover( i );

                    if (Dungeon.level.heroFOV[i]) {
                        GameScene.discoverTile( i, terr );
                        discover( i );
                    }
                }
            }
        }
        GameScene.updateFog();
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

}

