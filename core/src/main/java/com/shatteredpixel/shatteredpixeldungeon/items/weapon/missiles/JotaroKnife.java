package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class JotaroKnife extends MissileWeapon{
    private int minDamage = 0;
    private int maxDamage = 0;
    {
        image = ItemSpriteSheet.THROWING_KNIFE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.2f;

        tier = 1;
    }
    public JotaroKnife(int minDamage, int maxDamage){
        final float DAMAGE_SCALE = 0.8f;
        new JotaroKnife(minDamage, maxDamage, DAMAGE_SCALE);
    }
    public JotaroKnife(int minDamage, int maxDamage, float scaleFactor){
        this.minDamage = (int) (minDamage * scaleFactor);
        this.maxDamage = (int)(maxDamage * scaleFactor);
    }
    public int max(int lvl) {
        if (maxDamage != 0){
            return maxDamage;
        }
        else {
            return super.max(lvl);
        }
    }
    public int min(int lvl){
        if (minDamage != 0){
            return minDamage;
        }
        else {
            return super.min(lvl);
        }
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.attackTarget();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                //deals 75% toward max to max on surprise, instead of min to max.
                int diff = max() - min();
                int damage = augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff*0.75f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Random.IntRange(0, exStr);
                }
                return damage;
            }
        }
        return super.damageRoll(owner);
    }
    protected void onThrow( int cell ) {
        Char enemy = Actor.findChar( cell );
        if (enemy == null || enemy == curUser) {
            parent = null;
        } else {
            if (!curUser.shoot( enemy, this )) {
                rangedMiss( cell );
            } else {
                rangedHit( enemy, cell );
            }
        }
    }
    protected void rangedMiss( int cell ) {
        parent = null;
    }
    protected void rangedHit( Char enemy, int cell ){
        parent = null;
    }

}
