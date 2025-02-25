package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Jolyne3;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Jolynemap extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.MAP0;

        stackable = true;

        defaultAction = AC_LIGHT;

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
        super.execute(hero, action);
        if (action.equals(AC_LIGHT)) {

            GameScene.selectCell(thrower);

        }
    }

    protected static CellSelector.Listener thrower = new CellSelector.Listener() {


        @Override
        public void onSelect( Integer target ) {
            Jolyne3 ally = getwagon();
            if (target == null) return;

            SpellSprite.show( curUser, SpellSprite.MAP );
            hero.sprite.operate(hero.pos);

            ally.directTocell(target);

        }


        @Override
        public String prompt() {
            return Messages.get(Jolyne3.class, "prompt");
        }
    };

    private static Jolyne3 getwagon(){
        for (Char ch : Actor.chars()){
            if (ch instanceof Jolyne3){
                return (Jolyne3) ch;
            }
        }
        return null;
    }


    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x9999CC, 1f);
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
        return 50 * quantity;
    }

}
