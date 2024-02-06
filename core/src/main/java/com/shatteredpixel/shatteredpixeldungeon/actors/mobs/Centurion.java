package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CenturionSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Centurion extends Mob {
    {
        spriteClass = CenturionSprite.class;

        properties.add(Property.BOSS);

        HP = HT = 200;
        defenseSkill = 15;

        EXP = 0;
        maxLvl = 30;

        properties.add(Property.IMMOVABLE);
        state = PASSIVE;
    }

    private int skillcooldown = 999;
    public int  Phase = 0;

    @Override
    public void damage(int dmg, Object src) {

        super.damage(dmg, src);

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

    @Override
    public float speed() {
        return super.speed() * 0.5f;
    }

    private static final String COOLDOWN = "skillcooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN, skillcooldown);
        bundle.put( PHASE, Phase );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        skillcooldown = bundle.getInt(COOLDOWN);
        Phase = bundle.getInt(PHASE);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (skillcooldown > 0) {
            skillcooldown -= 1;
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public boolean act() {
        while (skillcooldown <= 0) {

            ArrayList<Integer> respawnPoints = new ArrayList<>();
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add( p );
                }
            }

            if (respawnPoints.size() > 0) {
                int index = Random.index( respawnPoints );

                Mob summon = Reflection.newInstance(Centurion.CenturionMinion.class);
                GameScene.add(summon);
                ScrollOfTeleportation.appear( summon, respawnPoints.get( index ) );
                Actor.addDelayed(new Pushing(summon, pos, summon.pos), -1);
                CellEmitter.get(summon.pos).burst(ShadowParticle.CURSE, 4);
                summon.beckon(Dungeon.hero.pos);

                Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);

                skillcooldown = 8;

            }
            else break;
        }

        if (skillcooldown > 0) {
            skillcooldown -= 1;
        }

        return super.act();
    }

    private static final String PHASE   = "Phase";

    @Override
    public void die(Object cause) {
        GLog.n(Messages.get(this, "d"));

        Dungeon.mboss19 = 0;
        super.die(cause);

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof CenturionMinion ) {
                mob.die( cause );
            }
        }

        new Flare( 5, 32 ).color( 0x00FFFF, true ).show( hero.sprite, 1f );
        Sample.INSTANCE.play(Assets.Sounds.BADGE);
        GLog.p(Messages.get(Pucci4.class, "x"));
    }

    public static class CenturionMinion extends Mob {

        {
            state = WANDERING;

            spriteClass = GhoulSprite.class;

            maxLvl = -15;
            EXP = 5;

            HP=HT=50;
            baseSpeed = 1f;
            properties.add(Property.UNDEAD);
        }

        public CenturionMinion(){
            super();
            switch (Random.Int(3)){
                case 0: default:
                    spriteClass = GhoulSprite.Blue.class;
                    break;
                case 1:
                    spriteClass = GhoulSprite.Green.class;
                    break;
                case 2:
                    spriteClass = GhoulSprite.Red.class;
                    break;
            }
        }

        @Override
        public void die(Object cause) {

            if (Dungeon.level.heroFOV[pos]) {
                Sample.INSTANCE.play( Assets.Sounds.BONES );
            }

            super.die(cause);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(35, 45);
        }

        @Override
        public int attackSkill(Char target) { return 10; }

        @Override
        public int drRoll() { return 0; }

        @Override
        public float speed() {
            return super.speed() * 1.5f;
        }
    }

    public static void spawn(LabsLevel level) {

        if (Dungeon.depth == 29 && !Dungeon.bossLevel()) {
            Centurion centinel = new Centurion();
            do {
                centinel.pos = level.randomRespawnCell(centinel);
            } while (centinel.pos == -1);
            level.mobs.add(centinel);
        }

    }
}