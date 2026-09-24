package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

//a plain Yasu that only ever says the same line when talked to - no quest, no reward
public class Yasu2 extends NPC {

    {
        spriteClass = YasuSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null){
            die(null);
            return true;
        }

        return super.act();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    private void tell( String text ) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show( new WndQuest( Yasu2.this, text ));
            }
        });
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );

        if (c != Dungeon.hero){
            return true;
        }

        tell( Messages.get(this, "tell") );
        return true;
    }
}
