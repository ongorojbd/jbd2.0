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
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Diegohead;
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
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDiegoDodgeGame;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
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
    private int IgniteCooldown = 5;
    private int TimeStopCooldown = 12;
    private int OverwhelmCooldown = 4;
    private int KnifeBarrageCooldown = 8;
    private int TimeStopChargeTime = 0;
    private boolean timeStopGameActive = false;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 26, 44 );
    }

    @Override
    public int attackSkill(Char target) { return 50; }

    public int drRoll() {
        return Random.NormalIntRange(22, 32);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 150){
            dmg = 150;
        }

        super.damage(dmg, src);

        if (phase==1 && HP < 1200) {
            phase = 2;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "phase_up"));
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        }
        else if (phase==2 && HP < 900) {
            phase = 3;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "phase_up"));
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        }
        else if (phase==3 && HP < 700) {
            phase = 4;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "phase_up"));
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        }
        else if (phase==4 && HP < 500) {
            phase = 5;
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "final_phase"));
            Sample.INSTANCE.play(Assets.Sounds.BOSS);
            Music.INSTANCE.play(Assets.Music.DIOLOWHP, true);
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        Music.INSTANCE.play(Assets.Music.CIV, true);

        // 시간 정지 미니게임이 진행 중이면 대기
        if (timeStopGameActive) {
            if (WndDiegoDodgeGame.instance != null) {
                spend(1f);
                return true;
            }
            // 창이 닫혀있으면 다시 표시
            showTimeStopGame();
            spend(1f);
            return true;
        }

        UseAbility();

        // 쿨다운 감소
        if (IgniteCooldown > 0) IgniteCooldown--;
        if (TimeStopCooldown > 0) TimeStopCooldown--;
        if (OverwhelmCooldown > 0) OverwhelmCooldown--;
        if (KnifeBarrageCooldown > 0) KnifeBarrageCooldown--;

        return super.act();
    }

    private boolean UseAbility() {

        // 시간 정지 - 나이프 회피 미니게임 (리메이크된 메인 패턴)
        if (TimeStopCooldown <= 0 && phase >= 1) {
            if (TimeStopChargeTime == 0) {
                // 1턴: 시간 정지 준비 경고
                GLog.h(Messages.get(this, "timestop_ready"));
                sprite.showStatus(CharSprite.WARNING, Messages.get(this, "timestop_warning"));

                Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);

                // 플레이어 주변 이펙트
                for (int i : PathFinder.NEIGHBOURS8) {
                    int cell = hero.pos + i;
                    if (cell >= 0 && cell < Dungeon.level.length()) {
                        sprite.parent.addToBack(new TargetedCell(cell, 0xFFFF00));
                    }
                }

                new Flare(8, 32).color(0xFFFF00, true).show(sprite, 2f);

                Dungeon.hero.interrupt();
                TimeStopChargeTime++;
                spend(1f);
                return true;
            }
            else {
                // 2턴: 시간 정지 발동 - 미니게임 시작
                GLog.n(Messages.get(this, "timestop_activate"));
                Sample.INSTANCE.play(Assets.Sounds.DIEGO);

                GameScene.flash(0x80FFFF00);
                Camera.main.shake(5, 0.5f);

                // 미니게임 시작
                timeStopGameActive = true;
                showTimeStopGame();

                TimeStopChargeTime = 0;
                spend(1f);
                return true;
            }
        }

        // 나이프 탄막 (새로운 패턴) - phase 2부터 사용
        else if (KnifeBarrageCooldown <= 0 && phase >= 2) {
            knifeBarrageAttack();

            if (phase >= 5) KnifeBarrageCooldown = Random.NormalIntRange(4, 6);
            else if (phase >= 4) KnifeBarrageCooldown = Random.NormalIntRange(6, 8);
            else KnifeBarrageCooldown = Random.NormalIntRange(8, 10);

            return true;
        }

        // 압도 (순간이동 후 근접) - phase 2부터 사용
        else if (OverwhelmCooldown <= 0 && phase >= 2) {
            overwhelmAttack();

            if (phase == 5) OverwhelmCooldown = Random.NormalIntRange(5, 8);
            else OverwhelmCooldown = Random.NormalIntRange(10, 14);

            return true;
        }

        // 점화 (화염 공격) - 모든 페이즈에서 사용
        else if (IgniteCooldown <= 0) {
            igniteAttack();

            if (phase == 5) IgniteCooldown = Random.NormalIntRange(2, 4);
            else IgniteCooldown = Random.NormalIntRange(10, 14);

            return true;
        }

        return true;
    }

    private void showTimeStopGame() {
        final Diego12 boss = this;
        final int currentPhase = phase;

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndDiegoDodgeGame(currentPhase,
                        // 성공 콜백 - 회피 성공, 보스에게 반격 데미지
                        new Callback() {
                            @Override
                            public void call() {
                                timeStopGameActive = false;

                                Camera.main.shake(6, 0.8f);
                                GameScene.flash(0x8000FF00);

                                GLog.p(Messages.get(Diego12.class, "dodge_success"));
                                GLog.n(Messages.get(Diego12.class, Random.element(LINE_KEYS)));

                                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
                                Sample.INSTANCE.play(Assets.Sounds.DIEGO2);

                                // 보스에게 반격 데미지
                                int counterDamage = Random.NormalIntRange(80, 120);
                                boss.damage(counterDamage, hero);
                                boss.sprite.showStatus(CharSprite.NEGATIVE, Integer.toString(counterDamage));

                                // 보스 텔레포트 (도망)
                                teleportAway();

                                TimeStopCooldown = Random.NormalIntRange(15, 20);
                                if (phase >= 5) TimeStopCooldown = Random.NormalIntRange(10, 14);
                            }
                        },
                        // 실패 콜백 - 나이프에 맞음
                        new Callback() {
                            @Override
                            public void call() {
                                timeStopGameActive = false;

                                GameScene.flash(0xFFFF0000);
                                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                                Sample.INSTANCE.play(Assets.Sounds.DIEGO);

                                // 큰 피해
                                int damage = Math.max(50, hero.HT / 3);
                                if (phase >= 4) damage = Math.max(70, hero.HT * 2 / 5);

                                hero.damage(damage, boss);
                                GLog.n(Messages.get(Diego12.class, "timestop_hit"));

                                CellEmitter.center(hero.pos).burst(BlastParticle.FACTORY, 15);

                                if (hero == Dungeon.hero && !hero.isAlive()) {
                                    Dungeon.fail(Diego12.class);
                                }

                                TimeStopCooldown = Random.NormalIntRange(12, 16);
                                if (phase >= 5) TimeStopCooldown = Random.NormalIntRange(8, 12);
                            }
                        }
                ));
            }
        });
    }

    // 나이프 탄막 패턴 (새로운 패턴)
    private void knifeBarrageAttack() {
        GLog.w(Messages.get(this, "knife_barrage"));
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "knife_warning"));

        Sample.INSTANCE.play(Assets.Sounds.MISS, 1.5f);

        // 플레이어 주변 셀에 나이프 예고
        ArrayList<Integer> targetCells = new ArrayList<>();

        int range = phase >= 4 ? 2 : 1;

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                int cell = hero.pos + dx + dy * Dungeon.level.width();
                if (cell >= 0 && cell < Dungeon.level.length() &&
                        !Dungeon.level.solid[cell] && cell != pos) {
                    targetCells.add(cell);
                    sprite.parent.addToBack(new TargetedCell(cell, 0xFFDDDD));
                }
            }
        }

        // 다음 턴에 데미지 적용 (현재는 경고만)
        // 플레이어가 이동할 시간을 줌

        Dungeon.hero.interrupt();
        spend(TICK);

        // 실제 데미지는 1턴 후 해당 위치에 있는 캐릭터에게
        Actor.add(new Actor() {
            { actPriority = VFX_PRIO; }

            @Override
            protected boolean act() {
                for (int cell : targetCells) {
                    CellEmitter.get(cell).burst(SparkParticle.FACTORY, 8);

                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch == hero) {
                        int dmg = Random.NormalIntRange(15, 25);
                        if (phase >= 4) dmg = Random.NormalIntRange(20, 35);

                        ch.damage(dmg, Diego12.this);

                        if (ch == Dungeon.hero && !ch.isAlive()) {
                            Dungeon.fail(Diego12.class);
                        }
                    }
                }
                Sample.INSTANCE.play(Assets.Sounds.HIT_STAB, 1.2f);

                Actor.remove(this);
                return true;
            }
        });
    }

    // 압도 패턴 (순간이동 후 공격)
    private void overwhelmAttack() {
        // 플레이어 주변으로 순간이동
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = hero.pos + i;
            if (cell >= 0 && cell < Dungeon.level.length() &&
                    Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
                candidates.add(cell);
            }
        }

        if (!candidates.isEmpty()) {
            int newPos = Random.element(candidates);
            ScrollOfTeleportation.appear(this, newPos);

            GameScene.flash(0x80FFFFFF);
            Camera.main.shake(2, 0.3f);

            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "overwhelm"));

            // 즉시 추가 공격
            if (Random.Int(2) == 0 && phase >= 3) {
                int dmg = Random.NormalIntRange(20, 30);
                hero.damage(dmg, this);
                Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
            }
        }

        spend(TICK);
    }

    // 점화 패턴 (화염 공격)
    private void igniteAttack() {
        hero.sprite.emitter().burst(ElmoParticle.FACTORY, 5);

        for (int i : PathFinder.NEIGHBOURS9) {
            int cell = hero.pos + i;
            if (cell >= 0 && cell < Dungeon.level.length()) {
                if (Dungeon.level.map[cell] == Terrain.WATER) {
                    Level.set(cell, Terrain.EMPTY);
                    GameScene.updateMap(cell);
                    CellEmitter.get(cell).burst(Speck.factory(Speck.STEAM), 10);
                } else {
                    Buff.affect(hero, Burning.class).reignite(hero, 5f);
                }

                if (!Dungeon.level.water[cell] && !Dungeon.level.solid[cell]) {
                    int vol = Fire.volumeAt(cell, Fire.class);
                    if (vol < 4) {
                        GameScene.add(Blob.seed(cell, 4 - vol, Fire.class));
                    }
                }
            }
        }

        // phase 5에서는 플레이어 위치의 물도 증발
        if (phase == 5) {
            Level.set(hero.pos, Terrain.EMPTY);
            GameScene.updateMap(hero.pos);
            CellEmitter.get(hero.pos).burst(Speck.factory(Speck.STEAM), 10);
        }

        Sample.INSTANCE.play(Assets.Sounds.BURNING);
        Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        GLog.w(Messages.get(this, "ignite"));

        spend(TICK);
    }

    // 보스 텔레포트 (회피 성공 시)
    private void teleportAway() {
        int newPos;
        int attempts = 0;
        do {
            newPos = Random.Int(Dungeon.level.length());
            attempts++;
        } while (attempts < 100 && (
                Dungeon.level.heroFOV[newPos] ||
                        Dungeon.level.solid[newPos] ||
                        Actor.findChar(newPos) != null ||
                        PathFinder.getStep(newPos, Dungeon.level.exit(), Dungeon.level.passable) == -1
        ));

        if (attempts < 100) {
            ScrollOfTeleportation.appear(this, newPos);
            state = WANDERING;
        }
    }

    private void blink( int target ) {
        Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

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
    private static final String SKILL2CD   = "TimeStopCooldown";
    private static final String SKILL2TIME   = "TimeStopChargeTime";
    private static final String SKILL3CD   = "OverwhelmCooldown";
    private static final String SKILL4CD   = "KnifeBarrageCooldown";
    private static final String TIMESTOP_GAME_ACTIVE = "timeStopGameActive";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
        bundle.put( SKILL1CD, IgniteCooldown );
        bundle.put( SKILL2CD, TimeStopCooldown );
        bundle.put( SKILL2TIME, TimeStopChargeTime );
        bundle.put( SKILL3CD, OverwhelmCooldown );
        bundle.put( SKILL4CD, KnifeBarrageCooldown );
        bundle.put( TIMESTOP_GAME_ACTIVE, timeStopGameActive );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        IgniteCooldown = bundle.getInt(SKILL1CD);
        TimeStopCooldown = bundle.getInt(SKILL2CD);
        TimeStopChargeTime = bundle.getInt(SKILL2TIME);
        OverwhelmCooldown = bundle.getInt(SKILL3CD);
        KnifeBarrageCooldown = bundle.getInt(SKILL4CD);
        timeStopGameActive = bundle.getBoolean(TIMESTOP_GAME_ACTIVE);
    }
}
