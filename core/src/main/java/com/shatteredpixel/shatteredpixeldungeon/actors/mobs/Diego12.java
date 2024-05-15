package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Diegohead;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.World21Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Diego12 extends Mob {
    private static final String[] LINE_KEYS = {"invincibility1", "invincibility2", "invincibility3", "invincibility4", "invincibility5"};
    {
        spriteClass = World21Sprite.class;

        HP = HT = 1500;
        defenseSkill = 30;

        state = HUNTING;
        viewDistance = Light.DISTANCE;

        EXP = 0;

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
        immunities.add( ToxicGas.class );
        properties.add(Property.BOSS);

        maxLvl = -9;

    }

    private int phase = 1; // 1~5까지
    private int IgniteCooldown = 5; // 점화 패턴 쿨타임
    private int BurstCooldown = 10; // 화염 폭발 패턴 쿨타임
    private int OverwhelmCooldown = 4; // 압도 패턴 쿨타임
    private int InvincibilityCooldown = 12; // 무적 패턴 쿨타임
    private int InvincibilityTime = 0; // 무적 패턴 지속시간.
    private int BurstPos = -1; // 화염 폭발 패턴의 발동 지점
    private int BurstTime = 0; // 화염 폭발 발동 시간. 2가 되면 발동함
    private int drup = 0; // 방어 상승 상태 지속시간. 있을 경우 받는 피해 50%감소
    private boolean fx = false;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 26, 44 );
    }

    @Override
    public int attackSkill(Char target) { return 50; }


    public int drRoll() {
        int dr;
        if (phase == 5) {
            dr = Random.NormalIntRange(9999, 9999);
        } else {
            dr = Random.NormalIntRange(22, 32);
        }
        return dr;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 150){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 150;
        }

        super.damage(dmg, src);

        if (phase==1 && HP < 1200) {
            phase = 2;
        }
        else if (phase==2 && HP < 900) {
            phase = 3;

        }
        else if (phase==3 && HP < 700) {
            phase = 4;

        }
        else if (phase==4 && HP < 500) {
            phase = 5;
            this.sprite.add(CharSprite.State.SHIELDED);
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        Music.INSTANCE.play(Assets.Music.CIV, true);


     {
            UseAbility();
        }

        if (InvincibilityTime > 0) {
            int evaporatedTiles;
            evaporatedTiles = Random.chances(new float[]{0, 2, 1, 1});
            for (int i = 0; i < evaporatedTiles; i++) {
                int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                if (Dungeon.level.map[cell] == Terrain.WATER) {
                    Level.set(cell, Terrain.EMPTY);
                    GameScene.updateMap(cell);
                    CellEmitter.get(cell).burst(Speck.factory(Speck.STEAM), 10);
                }
            }

            for (int i : PathFinder.NEIGHBOURS9) {
                int vol = Fire.volumeAt(pos+i, Fire.class);
                if (vol < 4 && !Dungeon.level.water[pos + i] && !Dungeon.level.solid[pos + i]){
                    GameScene.add( Blob.seed( pos + i, 4 - vol, Fire.class ) );
                }
            }

        }

        if (IgniteCooldown > 0) IgniteCooldown--;
        if (BurstCooldown > 0) BurstCooldown--;
        if (OverwhelmCooldown > 0) OverwhelmCooldown--;
        if (InvincibilityCooldown > 0) InvincibilityCooldown--;
        if (InvincibilityTime > 0) InvincibilityTime--;
        if (drup > 0) drup--;

        return super.act();
    }


    private boolean UseAbility() {
        // 화염 폭발 > 우르수스의 의지 > 압도 > 점화 순서의 우선도를 가집니다.

        //화염폭발
        if (BurstCooldown <= 0) {
            if (BurstTime == 0) {
                GLog.h(Messages.get(this, "fire_ready"));

                Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);

                Dungeon.hero.interrupt();

                BurstTime++;
                return true;
            }
            else {
                Char Target = hero;

                if (Target.buff(PortableCover.CoverBuff.class) == null) {
                    for (int i : PathFinder.NEIGHBOURS8){
                        int cell = hero.pos+i;
                        ScrollOfTeleportation.appear(this, cell);
                    }

                    Sample.INSTANCE.play( Assets.Sounds.DIEGO);
                    hero.damage(hero.HT/2, this);
                    GLog.n(Messages.get(this, "t"));

                    if (enemy == Dungeon.hero && !enemy.isAlive()) {
                        Dungeon.fail(getClass());
                    }

                }
                else {

                        int i;
                        do {
                            i = Random.Int(Dungeon.level.length());
                        } while (Dungeon.level.heroFOV[i]
                                || Dungeon.level.solid[i]
                                || Actor.findChar(i) != null
                                || PathFinder.getStep(i, Dungeon.level.exit(), Dungeon.level.passable) == -1);
                        ScrollOfTeleportation.appear(this, i);
                        state = WANDERING;

                    damage(100, this);
                    GLog.n(Messages.get(this, Random.element( LINE_KEYS )));
                    Sample.INSTANCE.play( Assets.Sounds.DIEGO2);

                    Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY);
                }

                GameScene.flash(0x80FFFFFF);

                CellEmitter.center(hero.pos).burst(BlastParticle.FACTORY, 3);
                Camera.main.shake(2, 0.5f);
                BurstTime = 0;
                BurstCooldown = Random.NormalIntRange(13,18);
                spend(1f);

                return true;
            }
        }
        // 우르수스의 의지
        else if (InvincibilityCooldown <= 0 && phase >= 4) {
            if (phase == 5) InvincibilityTime = 8;
            else InvincibilityTime = 5;

            Sample.INSTANCE.play(Assets.Sounds.MIMIC);

            GLog.w(Messages.get(this, "4"));

            InvincibilityCooldown = 15;
            return true;
        }
        // 압도
        else if (OverwhelmCooldown <= 0 && phase >= 2) {

            for (int i : PathFinder.NEIGHBOURS8){
                int cell = hero.pos+i;
                ScrollOfTeleportation.appear(this, cell);
            }

            GameScene.flash(0x80FFFFFF);

            if (phase == 5) OverwhelmCooldown = Random.NormalIntRange(7,11);
            else OverwhelmCooldown = Random.NormalIntRange(12,16);

            return true;
        }
        // 점화
        else if (IgniteCooldown <= 0) {
            hero.sprite.emitter().burst( ElmoParticle.FACTORY, 5 );

            for (int i : PathFinder.NEIGHBOURS9){
                if (Dungeon.level.map[hero.pos+i] == Terrain.WATER) {
                    int cell = hero.pos+i;
                    Level.set( cell, Terrain.EMPTY);
                    GameScene.updateMap( cell );
                    CellEmitter.get( hero.pos+i ).burst( Speck.factory( Speck.STEAM ), 10 );
                }else {
                    Buff.affect( hero, Burning.class ).reignite( hero, 5f );
                }

                if (!Dungeon.level.water[hero.pos+i] && !Dungeon.level.solid[hero.pos+i]){
                    int vol = Fire.volumeAt(hero.pos+i, Fire.class);
                    if (vol < 4){
                        GameScene.add( Blob.seed( hero.pos + i, 4 - vol, Fire.class ) );
                    }
                }
            }

            if (phase == 5) {
                Level.set(hero.pos, Terrain.EMPTY);
                GameScene.updateMap(hero.pos);
                CellEmitter.get(hero.pos).burst(Speck.factory(Speck.STEAM), 10);
                IgniteCooldown = 3;
            }
            else IgniteCooldown = Random.NormalIntRange(14, 16);

            Sample.INSTANCE.play( Assets.Sounds.BURNING );
            Sample.INSTANCE.play( Assets.Sounds.SHATTER);
            GLog.w(Messages.get(this, "2"));

            return true;
        }

        return true;
    }

    private void blink( int target ) {

        Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

        //can't occupy the same cell as another char, so move back one.
        if (Actor.findChar( cell ) != null && cell != this.pos)
            cell = route.path.get(route.dist-1);

        if (Dungeon.level.avoid[ cell ]){
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
                return;
            }
        }

        ScrollOfTeleportation.appear( this, cell );
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        return super.attackProc(enemy, damage);
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Sample.INSTANCE.play(Assets.Sounds.BONES, 1f, 0.75f);

        Music.INSTANCE.play(Assets.Music.DIOLOWHP, true);

        new Flare( 5, 32 ).color( 0xFFFF00, true ).show( hero.sprite, 2f );
        Diegohead pick = new Diegohead();
        if (pick.doPickUp( Dungeon.hero )) {
            GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", pick.name()) ));
        } else {
            Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
        }
    }

    private static final String PHASE   = "phase";
    private static final String SKILL1CD   = "IgniteCooldown";
    private static final String SKILL2CD   = "BurstCooldown";
    private static final String SKILL2POS   = "BurstPos";
    private static final String SKILL2TIME   = "BurstTime";
    private static final String SKILL3CD   = "OverwhelmCooldown";
    private static final String SKILL4CD   = "InvincibilityCooldown";
    private static final String SKILL4TIME   = "InvincibilityTime";
    private static final String DRUPTIME   = "drup";


    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
        bundle.put( SKILL1CD, IgniteCooldown );
        bundle.put( SKILL2CD, BurstCooldown );
        bundle.put( SKILL2POS, BurstPos );
        bundle.put( SKILL2TIME, BurstTime );
        bundle.put( SKILL3CD, OverwhelmCooldown );
        bundle.put( SKILL4CD, InvincibilityCooldown );
        bundle.put( SKILL4TIME, InvincibilityTime );
        bundle.put( DRUPTIME, drup );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        IgniteCooldown = bundle.getInt(SKILL1CD);
        BurstCooldown = bundle.getInt(SKILL2CD);
        BurstPos = bundle.getInt(SKILL2POS);
        BurstTime = bundle.getInt(SKILL2TIME);
        OverwhelmCooldown = bundle.getInt(SKILL3CD);
        InvincibilityCooldown = bundle.getInt(SKILL4CD);
        InvincibilityTime = bundle.getInt(SKILL4TIME);
        drup = bundle.getInt(DRUPTIME);
    }
}