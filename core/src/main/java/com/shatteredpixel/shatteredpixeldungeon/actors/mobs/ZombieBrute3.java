package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieBrute3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class ZombieBrute3 extends Mob {
    private static final float TIME_TO_ZAP	= 1f;

    {
        spriteClass = ZombieBrute3Sprite.class;

        HP = HT = 20;
        defenseSkill = 9;

        EXP = 5;
        maxLvl = 10;
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 2, 8 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Char.combatRoll(0, 3);
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

    //used so resistances can differentiate between melee and magical attacks
    public static class AcidBolt{}

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {

            int dmg = Char.combatRoll(3, 10);
            enemy.damage( dmg, new AcidBolt());
            if (enemy.isAlive()) {
                Buff.affect(enemy, Ooze.class).set(3f);
            }

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "bolt_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

}
