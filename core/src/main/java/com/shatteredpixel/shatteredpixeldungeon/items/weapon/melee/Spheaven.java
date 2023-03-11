package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spheaven extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SPWORLD;
        hitSound = Assets.Sounds.HIT;
        identify();
        tier = 5;
        RCH = 3;    //extra reach
    }

    private boolean swiching = false;

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //15+3
                lvl*(tier);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x33CCFF, 1f);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        return actions;
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero) owner;
            Char enemy = hero.enemy();
            if (Dungeon.hero.belongings.weapon() instanceof Spheaven && (Random.Int(10) == 0)) {
                Buff.prolong(owner, Haste.class, 3f);
                Buff.prolong(owner, Bless.class, 3f);
                Buff.prolong(owner, BlobImmunity.class, 3f);
                Buff.prolong(owner, Adrenaline.class, 3f);
                Buff.prolong(owner, MagicImmune.class, 3f);
            }
        }
        return super.damageRoll(owner);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Sample.INSTANCE.play(Assets.Sounds.ORA);
        if (!swiching) {
            Ballistica trajectory = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_TARGET);
            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
            moveChar(attacker, trajectory, 1, defender.pos, false, false); // 자신이 이동효과
            CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.LIGHT ), 3 );
            Sample.INSTANCE.play( Assets.Sounds.PUFF );
        }
        else if (swiching) {
            final Ballistica chain = new Ballistica(defender.pos, attacker.pos, Ballistica.STOP_TARGET);
            if (Actor.findChar( chain.collisionPos ) != null) chainEnemy(chain,defender);
        }

        if (Dungeon.hero.belongings.getItem(TimekeepersHourglass.class) != null) {
            if (Dungeon.hero.belongings.getItem(TimekeepersHourglass.class).isEquipped(Dungeon.hero)) {
                if (attacker.buff(Levitation.class) != null) {
                    damage *= 1.35f;
                    attacker.sprite.showStatus(CharSprite.NEUTRAL, "[스탠드 파워전개]");
                }
            }
        }

        return super.proc(attacker, defender, damage);
    }

    private void moveChar(final Char ch, final Ballistica trajectory, int power, int enemypos,
                          boolean closeDoors, boolean collideDmg){
        if (ch.properties().contains(Char.Property.BOSS)) {
            power /= 2;
        }

        int dist = Math.min(trajectory.dist, power);

        boolean collided = dist == trajectory.dist;

        if (dist == 0 || ch.properties().contains(Char.Property.IMMOVABLE)) return;

        //large characters cannot be moved into non-open space
        if (Char.hasProp(ch, Char.Property.LARGE)) {
            for (int i = 1; i <= dist; i++) {
                if (!Dungeon.level.openSpace[trajectory.path.get(i)]){
                    dist = i-1;
                    collided = true;
                    break;
                }
            }
        }

        if (Actor.findChar(trajectory.path.get(dist)) != null){
            dist--;
            collided = true;
        }

        if (dist < 0) return;

        final int newPos = trajectory.path.get(dist);

        if (newPos == enemypos) return;

        final int finalDist = dist;
        final boolean finalCollided = collided && collideDmg;
        final int initialpos = ch.pos;

        Actor.addDelayed(new Pushing(ch, ch.pos, newPos, new Callback() {
            public void call() {
                if (initialpos != ch.pos) {
                    //something caused movement before pushing resolved, cancel to be safe.
                    ch.sprite.place(ch.pos);
                    return;
                }
                int oldPos = ch.pos;
                ch.pos = newPos;
                if (finalCollided && ch.isAlive()) {
                    ch.damage(Random.NormalIntRange(finalDist, 2*finalDist), this);
                    Paralysis.prolong(ch, Paralysis.class, 1 + finalDist/2f);
                }
                if (closeDoors && Dungeon.level.map[oldPos] == Terrain.OPEN_DOOR){
                    Door.leave(oldPos);
                }
                Dungeon.level.occupyCell(ch);
                if (ch == hero){
                    //FIXME currently no logic here if the throw effect kills the hero
                    Dungeon.observe();
                }
            }
        }), -1);
    }

    public static void chainEnemy( Ballistica chain, final Char enemy ){

        if (enemy.properties().contains(Char.Property.IMMOVABLE)) {
            return;
        }

        int bestPos = -1;
        for (int i : chain.subPath(1, chain.dist)){
            //prefer to the earliest point on the path
            if (!Dungeon.level.solid[i]
                    && Actor.findChar(i) == null
                    && (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[i])){
                bestPos = i;
                break;
            }
        }

        if (bestPos == -1) {
            return;
        }

        final int pulledPos = bestPos;

        Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
            public void call() {
                enemy.sprite.move(enemy.pos, pulledPos);
                enemy.pos = pulledPos;
                Dungeon.level.occupyCell(enemy);
                Dungeon.observe();
                GameScene.updateFog();
            }
        }));

    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero.belongings.getItem(TimekeepersHourglass.class) != null) {
            if (Dungeon.hero.belongings.getItem(TimekeepersHourglass.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( Spheaven.class, "setbouns");}

        return info;
    }

    private static final String SWICH = "swiching";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SWICH, swiching);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        swiching = bundle.getBoolean(SWICH);
    }


    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Dagger.class, Greatshield.class};
            inQuantity = new int[]{1, 1};

            cost = 50;

            output = SnowHunter.class;
            outQuantity = 1;
        }
    }





}
