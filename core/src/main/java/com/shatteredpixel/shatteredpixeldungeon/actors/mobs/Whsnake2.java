package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhsnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Whsnake2 extends Mob {
    {
        spriteClass = WhsnakeSprite.class;

        HP = HT = 60;
        defenseSkill = 10;

        EXP = 10;
        maxLvl = 15;

        properties.add(Property.BOSS);
    }

    public int  Phase = 0;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, Phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        Phase = bundle.getInt(PHASE);
    }

    @Override
    public void notice() {


        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            for (Char ch : Actor.chars()){
            }
        }
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 25){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 25;
        }

        super.damage(dmg, src);

        if (Phase==0 && HP < 26) {
            Phase = 1;
            HP = 25;

            GameScene.flash(0x99FFFF);
            Sample.INSTANCE.play( Assets.Sounds.CURSED, 2, 0.33f );
            Buff.affect(Dungeon.hero, Blindness.class, 15f);
            Buff.affect( hero, Ooze.class ).set( Ooze.DURATION );
            Buff.affect(Dungeon.hero, Silence.class,  30f);

            yell(Messages.get(this, "3"));
        }
    }


    private static final String PHASE   = "Phase";



    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 12);
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 7);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return true;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        yell( Messages.get(this, "4") );

    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (this.buff(Barkskin.class) == null) {
            if (Random.Int(2) == 0) {
                sprite.showStatus(CharSprite.WARNING, Messages.get(this, "1"));
                Buff.affect( enemy, Burning.class ).reignite( enemy, 2f );
                hero.sprite.emitter().burst(Speck.factory(Speck.STEAM), 10);
            }

        }
        return damage;
    }

}