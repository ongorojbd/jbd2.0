package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.watabou.utils.PathFinder.*;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PitfallParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WoolParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscA;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM201Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Tusk4Sprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Jonny extends Mob {
    {
        spriteClass = Tusk4Sprite.class;

        HP = HT = 150;
        defenseSkill = 12;

        EXP = 11;
        maxLvl = 17;

        baseSpeed = 1f;

        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);

    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    private int phase = 3;
    private int blastcooldown = 1;
    private int barriercooldown = 3;
    private int volcanocooldown = 14;
    private int volcanotime = 0;
    private int restorecooldown = 993;
    private int summoncooldown = 996;
    private int Burstpos = -1;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(15, 20);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 9);
    }

    @Override
    public int attackSkill( Char target ) {
        return 25;
    }


    @Override
    public void damage(int dmg, Object src) {




        if (phase == 2) {

            return;
        }

        if (this.buff(Barrier.class) != null) {
            dmg /= 10;
        }

        super.damage(dmg, src);

        if (phase==1 && HP < 75) {
            HP = 75;
            phase = 2;
            Buff.detach(this, Barrier.class);
            blastcooldown = 1;
            barriercooldown = 30;
            volcanocooldown = 7;
        }
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (buff(RestorBuff.class) != null && !(enemy instanceof Hero)) {
            damage = 0;
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    protected boolean act() {

        {

            {
                if (UseAbility()) return true;
            }




            if (phase == 1 || phase == 3) {
                if (blastcooldown > 0) blastcooldown--;
                if (barriercooldown > 0) barriercooldown--;
                if (volcanocooldown > 0) volcanocooldown--;
            }
        }
        return super.act();
    }


    private boolean UseAbility() {
        // 화염파 > 리틀 폼페이 소환 > 용암 장갑 > 화산분출 순으로 사용합니다.
        if (FireBlast()){

            return true;
        }

        //용암장갑
        if (barriercooldown <= 0) {
            GLog.w(Messages.get(this, "2"));

            CellEmitter.get(this.pos).burst(PitfallParticle.FACTORY8, 12);
            new Fadeleaf().activate(this);

            barriercooldown = 30;


            return true;
        }


        //화산폭발
        if (volcanocooldown <= 0) {

            if (Burstpos == -1) {
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                // 위치 미지정시, 이번 턴에는 폭발을 일으킬 지점을 정합니다.

                Burstpos = Dungeon.hero.pos;

                yell( Messages.get(this, "1") );

                for (int i : NEIGHBOURS9) {
                    int vol = Fire.volumeAt(Burstpos+i, Fire.class);
                    if (vol < 4){
                        CellEmitter.floor(Burstpos+i).burst(PitfallParticle.FACTORY4, 8);
                    }
                }


                volcanotime++;
                return false;
            }


            else if (volcanotime == 1) {
                Sample.INSTANCE.play(Assets.Sounds.BLAST);

                for (int i : NEIGHBOURS9) {
                    int vol = Fire.volumeAt(Burstpos+i, Fire.class);
                    if (vol < 4){
                        CellEmitter.floor(Burstpos+i).burst(PitfallParticle.FACTORY8, 12);
                    }
                }


                volcanotime++;

                return false;
            }

            else if (volcanotime == 2) {
                boolean isHit = false;
                buildDistanceMap(Burstpos , BArray.not(Dungeon.level.solid, null), 1);
                for (int cell = 0; cell < distance.length; cell++) {
                    if (distance[cell] < Integer.MAX_VALUE) {
                        Char ch = Actor.findChar(cell);
                        int vol = Fire.volumeAt(cell, Fire.class);
                            if (vol < 4){
                                sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
                            }
                        if (ch != null&& !(ch instanceof Jonny)&& !isHit) {
                            if ((ch.alignment != alignment || ch instanceof Bee)) {
                            ch.damage(Random.NormalIntRange(15, 20), this);
                                if (ch.isAlive()) Buff.affect(ch, Vertigo.class, 1f);
                                isHit = true;

                                if (enemy == Dungeon.hero && !enemy.isAlive()) {
                                    Dungeon.fail(getClass());
                                }
                        }

                    }}}


                Sample.INSTANCE.play( Assets.Sounds.BLAST );
                Burstpos = -1;
                volcanotime=0;
                volcanocooldown= 10;

                return true;
            }


            return false;
        }


        return false;
    }

    public class Blast { }
    public class Volcano { }

    private ArrayList<Integer> targetedCells = new ArrayList<>();
    private boolean FireBlast() {
        boolean terrainAffected = false;
        HashSet<Char> affected = new HashSet<>();
        //delay fire on a rooted hero
        if (!Dungeon.hero.rooted) {
            for (int i : targetedCells) {
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                //shoot beams
                for (int p : b.path) {
                    sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                    Char ch = Actor.findChar(p);
                    Sample.INSTANCE.play(Assets.Sounds.EVOKE);
                    if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
                        affected.add(ch);
                    }
                    if (Dungeon.level.flamable[p]) {
                        Dungeon.level.destroy(p);
                        GameScene.updateMap(p);
                        terrainAffected = true;
                    }
                }
            }
            if (terrainAffected) {
                Dungeon.observe();
            }
            for (Char ch : affected) {
               ch.damage(Random.NormalIntRange(11, 20), new Jonny.Blast());

                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }
                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(Char.class, "kill", name()));
                }
            }
            targetedCells.clear();
        }

        if (blastcooldown <= 0){

            int beams = 5;
            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++){

                int targetPos = Dungeon.hero.pos;
                if (i != 0){
                    do {
                        targetPos = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                    } while (Dungeon.level.trueDistance(pos, Dungeon.hero.pos)
                            > Dungeon.level.trueDistance(pos, targetPos));
                }
                targetedCells.add(targetPos);
                Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                affectedCells.addAll(b.path);
            }

            //remove one beam if multiple shots would cause every cell next to the hero to be targeted
            boolean allAdjTargeted = true;
            for (int i : PathFinder.NEIGHBOURS9){
                if (!affectedCells.contains(Dungeon.hero.pos + i) && Dungeon.level.passable[Dungeon.hero.pos + i]){
                    allAdjTargeted = false;
                    break;
                }
            }
            if (allAdjTargeted){
                targetedCells.remove(targetedCells.size()-1);
            }
            for (int i : targetedCells){
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                for (int p : b.path){
                    sprite.parent.addToBack(new TargetedCell(p, 0xFF00FF));
                    affectedCells.add(p);
                }
            }

            //don't want to overly punish players with slow move or attack speed
            Dungeon.hero.interrupt();
            blastcooldown = 6;

            spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2*TICK));
            return true;

        }
        return false;
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
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.drop( new ScrollOfEnchantment().identify(), pos ).sprite.drop( pos );

        yell( Messages.get(this, "3") );

    }

    private static final String PHASE   = "phase";
    private static final String BLAST_CD   = "blastcooldown";
    private static final String BARRIER_CD  = "barriercooldown";
    private static final String VOCAL_CD   = "volcanocooldown";
    private static final String VOCAL_TIME   = "volcanotime";
    private static final String SUMMON_CD   = "summoncooldown";
    private static final String RESTORE_CD   = "restorecooldown";
    private static final String SKILL2TPOS   = "Burstpos";
    private static final String TARGETED_CELLS = "targeted_cells";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
        bundle.put( BLAST_CD, blastcooldown );
        bundle.put( BARRIER_CD, barriercooldown );
        bundle.put( VOCAL_CD, volcanocooldown );
        bundle.put( VOCAL_TIME, volcanotime );
        bundle.put( SUMMON_CD, summoncooldown);
        bundle.put( RESTORE_CD, restorecooldown);
        bundle.put( SKILL2TPOS, Burstpos );

        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        blastcooldown = bundle.getInt(BLAST_CD);
        barriercooldown = bundle.getInt(BARRIER_CD);
        summoncooldown = bundle.getInt(SUMMON_CD);
        volcanocooldown = bundle.getInt(VOCAL_CD);
        volcanotime = bundle.getInt(VOCAL_TIME);
        restorecooldown = bundle.getInt(RESTORE_CD);
        Burstpos = bundle.getInt(SKILL2TPOS);

        for (int i : bundle.getIntArray(TARGETED_CELLS)){
            targetedCells.add(i);
        }

    }


    public static class RestorBuff extends FlavourBuff {
        {
            immunities.add(ToxicGas.class);
            immunities.add(CorrosiveGas.class);
        }
        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.SHIELDED);
            else target.sprite.remove(CharSprite.State.SHIELDED);
        }
    }
}
