package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CmoonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Pucci4 extends Mob {

    {
        spriteClass = CmoonSprite.class;

        EXP = 0;
        maxLvl = 30;

        state = PASSIVE;

        HP = HT = 200;
        defenseSkill = 15;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    int damageTaken = 0;
    private int skillcooldown = 9999;
    public int  Phase = 0;

    private static final String DMGTAKEN = "damagetaken";
    private static final String COOLDOWN = "skillcooldown";
    private static final String PHASE   = "Phase";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(DMGTAKEN, damageTaken);
        bundle.put(COOLDOWN, skillcooldown);
        bundle.put( PHASE, Phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        damageTaken = bundle.getInt( DMGTAKEN );
        skillcooldown = bundle.getInt(COOLDOWN);
        Phase = bundle.getInt(PHASE);
    }


    @Override
    public int damageRoll() {
        return Random.NormalIntRange(35, 40);
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() { return Random.NormalIntRange(0, 5);}

    private int moving = 0;

    @Override
    protected boolean getCloser( int target ) {
        //this is used so that the crab remains slower, but still detects the player at the expected rate.
        moving++;
        if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && skillcooldown <= 0) {

            blink( target );
            spend( -1 / speed() );
            return true;

        } else {

            skillcooldown--;

        }

        if (moving < 3) {

        } else {
            moving = 0;
            return true;
        }

        return super.getCloser( target );

    }


    private void blink( int target ) {

        Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

        //can't occupy the same cell as another char, so move back one.
        if (Actor.findChar( cell ) != null && cell != this.pos)
            cell = route.path.get(route.dist-1);

        if (Dungeon.level.avoid[ cell ] && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])){
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                cell = route.collisionPos + n;
                if (Dungeon.level.passable[cell]
                        && Actor.findChar( cell ) == null
                        && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])) {
                    candidates.add( cell );
                }
            }
            if (candidates.size() > 0)
                cell = Random.element(candidates);
            else {
                skillcooldown = Random.IntRange(4, 6);
                return;
            }
        }

        ScrollOfTeleportation.appear( this, cell );
        Sample.INSTANCE.play( Assets.Sounds.DIAVOLO );
        GameScene.flash( 0xCC3366 );
        GLog.n(Messages.get(this, "q"));


        skillcooldown = Random.IntRange(4, 6);
    }

    @Override
    public void damage( int dmg, Object src ){
        //crab blocks all wand damage from the hero if it sees them.
        //Direct damage is negated, but add-on effects and environmental effects go through as normal.

        if (Phase==0 && HP < 200) {

            Phase = 1;
            HP = 199;
            state = HUNTING;
            skillcooldown = 2;
            if (!BossHealthBar.isAssigned()) {
                BossHealthBar.assignBoss(this);
                for (Char ch : Actor.chars()){
                }
            }
            GLog.n(Messages.get(this, "n"));
        }

        if (enemySeen
                && state != SLEEPING
                && paralysed == 0
                && src instanceof Wand
                && enemy == Dungeon.hero
                && enemy.invisible == 0){
            GLog.w( Messages.get(this, "noticed") );
            sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "def_verb") );
            Sample.INSTANCE.play( Assets.Sounds.DIAVOLO );
            GameScene.flash( 0xCC3366 );
        } else {
            super.damage( dmg, src );
        }


    }

    @Override
    public int defenseSkill( Char enemy ) {
        //crab blocks all melee attacks from its current target


        if (Phase==2) {
        if (enemySeen
                && state != SLEEPING
                && paralysed == 0
                && enemy == this.enemy
                && enemy.invisible == 0){
            if (sprite != null && sprite.visible) {
                Sample.INSTANCE.play( Assets.Sounds.DIAVOLO );
                GameScene.flash( 0xCC3366 );
                GLog.w( Messages.get(this, "noticed") );
            }
            return INFINITE_EVASION;
        }}
        return super.defenseSkill( enemy );
    }

    @Override
    public void die(Object cause) {
        GLog.n( Messages.get(this, "d") );

        new Flare( 5, 32 ).color( 0x00FFFF, true ).show( hero.sprite, 1f );
        Sample.INSTANCE.play(Assets.Sounds.BADGE);
        GLog.p(Messages.get(Pucci4.class, "x"));
        Dungeon.mboss9 = 0;
        super.die(cause);
    }

    public static void spawn(LabsLevel level) {

        if (Dungeon.depth == 27 && !Dungeon.bossLevel()) {
            Pucci4 centinel = new Pucci4();
            do {
                centinel.pos = level.randomRespawnCell(centinel);
            } while (centinel.pos == -1);
            level.mobs.add(centinel);
        }

    }

}
