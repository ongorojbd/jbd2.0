package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class Smask3 extends Item {
    public static final String AC_LIGHT	= "LIGHT";
//    public static final String AC_BOSS	= "BOSS";

    {
        image = ItemSpriteSheet.SMASK;

        stackable = true;

        defaultAction = AC_LIGHT;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_LIGHT );
//        actions.add( AC_BOSS );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute( hero, action );
        if (action.equals( AC_LIGHT )) {
            Dungeon.win( Amulet.class );
            Dungeon.deleteGame( GamesInProgress.curSlot, true );
            Game.switchScene( SurfaceScene.class );
            SPDSettings.addSpecialcoin(1);
        }

//        if (action.equals( AC_BOSS )) {
//            SPDSettings.addBrando(1);
//            Statistics.diokilled = true;
//            InterlevelScene.mode = InterlevelScene.Mode.RETURN;
//            InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth +1));
//            InterlevelScene.returnBranch = 0;
//            InterlevelScene.returnPos = -2;
//            Game.switchScene(InterlevelScene.class);
//            detach( curUser.belongings.backpack );
//            updateQuickslot();
//        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xCC0000, 1f);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}
