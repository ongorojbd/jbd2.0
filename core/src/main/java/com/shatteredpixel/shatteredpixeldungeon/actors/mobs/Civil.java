package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Holy1;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roc;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Reimi;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Kirafood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscD;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CivilSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Civil extends Mob {

    {
        spriteClass = CivilSprite.class;

        HP = HT = 100 + Statistics.enemiesSlain*10;
        defenseSkill = 4;

        EXP = 0;
        maxLvl = -9;

        state = HUNTING;

        immunities.add( Paralysis.class );
        immunities.add( Roots.class );
        immunities.add( Dread.class );
        immunities.add( Terror.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        immunities.add( Blizzard.class );
        immunities.add( CorrosiveGas.class );
        immunities.add( Electricity.class );
        immunities.add( Fire.class );
        immunities.add( Freezing.class );
        immunities.add( Inferno.class );
        immunities.add( ParalyticGas.class );
        immunities.add( StenchGas.class );
        immunities.add( ToxicGas.class );
        immunities.addAll(AntiMagic.RESISTS);

        properties.add(Property.BOSS);
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    public static class Potion{}

    protected void zap() {
        spend( 3f );

        int randompot = Random.Int(4);

        switch (randompot) {
            case 0:
            default:
                PotionOfFrost pf = new PotionOfFrost();
                pf.shatter(enemy.pos);
                break;
            case 1:
                PotionOfLiquidFlame pl = new PotionOfLiquidFlame();
                pl.shatter(enemy.pos);
                break;
            case 2:
                PotionOfToxicGas pt = new PotionOfToxicGas();
                pt.shatter(enemy.pos);
                break;
            case 3:
                PotionOfCorrosiveGas ps = new PotionOfCorrosiveGas();
                ps.shatter(enemy.pos);
                break;
        }

        Invisibility.dispel(this);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( hero.HT/11, hero.HT/6 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(999,999);
    }

    @Override
    public boolean act() {

        boolean result = super.act();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        Music.INSTANCE.play(Assets.Music.CIV, true);

        if (Dungeon.level.map[pos] == Terrain.WATER){
            Level.set( pos, Terrain.EMPTY);
            GameScene.updateMap( pos );
            CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 10 );
        }

        if (Dungeon.level.map[hero.pos] == Terrain.WATER) {
            int cell = hero.pos;
            damage(HT/50, this);
            Level.set( cell, Terrain.EMPTY);
            GameScene.updateMap( cell );
            CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
            if (!isAlive()) return true;
        }

        return result;
    }

    @Override
    protected boolean getCloser(int target) {

        target = Dungeon.hero.pos;

        return super.getCloser( target );
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        GameScene.bossSlain();

        Sample.INSTANCE.play(Assets.Sounds.BONES, 1f, 0.75f);

        Buff.affect(hero, Holy1.class);

        Music.INSTANCE.end();

        yell( Messages.get(this, "2") );
        GLog.h(Messages.get(Civil.class, "44"));

    }
}
