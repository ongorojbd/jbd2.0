/*
 * Light Mode Kars - agile blade boss focused on high-speed cutting and ranged cone slashes.
 * Enhanced version with Beast-level stats
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask3;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GSoldierSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KarsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Lisa2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Lisa3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Speedwagon2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SturoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.UltimateSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NewKarslight extends Mob {

    {
        // placeholder sprite; replace when custom sprite is available
        spriteClass = KarsSprite.class;

        // Beast 수준의 강화된 스펙
        HP = HT = 800;
        defenseSkill = 23;

        EXP = 50;
        maxLvl = 30;

        properties.add(Property.BOSS);
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    private int phase = 0;
    private int netCD = 8;       // 블레이드 넷 (십자/대각 교차 절단, 예고 1턴)
    private int kickCD = 7;      // 크러싱 킥 (적을 벽에 찍어버림)
    private int stepCD = 10;     // 라이트스텝 (짧은 은신/이동)
    private int flashCD = 12;    // 플래시 세버 (순간이동 절단)

    private int netWindup = 0;
    private ArrayList<Integer> netCells = new ArrayList<>();

    private static final String PHASE = "phase";
    private static final String KICK_CD = "kick_cd";
    private static final String STEP_CD = "step_cd";
    private static final String FLASH_CD = "flash_cd";
    private static final String NET_CD = "net_cd";
    private static final String NET_WINDUP = "net_windup";
    private static final String NET_CELLS = "net_cells";

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            Music.INSTANCE.play(Assets.Music.TENDENCY2, true);

            if (Dungeon.hero.heroClass == HeroClass.MAGE) {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new Lisa2Sprite(), new KarsSprite(), new KarsSprite(), new KarsSprite(), new KarsSprite()},
                        new String[]{"리사리사", "카즈", "카즈", "카즈", "카즈"},
                        new String[]{
                                Messages.get(KarsLight.class, "t1"),
                                Messages.get(KarsLight.class, "t2"),
                                Messages.get(KarsLight.class, "t3"),
                                Messages.get(KarsLight.class, "t4"),
                                Messages.get(KarsLight.class, "t5"),
                        },
                        new byte[]{
                                WndDialogueWithPic.RUN,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );
            }
            else {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new Lisa2Sprite(), new KarsSprite(), new KarsSprite(), new KarsSprite(), new KarsSprite()},
                        new String[]{"리사리사", "카즈", "카즈", "카즈", "카즈"},
                        new String[]{
                                Messages.get(KarsLight.class, "t1"),
                                Messages.get(KarsLight.class, "t2"),
                                Messages.get(KarsLight.class, "t3"),
                                Messages.get(KarsLight.class, "t4"),
                                Messages.get(KarsLight.class, "t5"),
                        },
                        new byte[]{
                                WndDialogueWithPic.RUN,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );
            }
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        BossHealthBar.assignBoss(this);

        if (dmg >= 300) {
            dmg = 300;
        }

        super.damage(dmg, src);

        // LockedFloor 시간 연장 (다른 보스들처럼)
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass()) && !isInvulnerable(src.getClass())){
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg);
            else                                                    lock.addTime(dmg*1.5f);
        }

        if (phase == 0 && HP < HT * 2 / 3) {
            phase = 1;
            netCD = Math.min(netCD, 5);
            kickCD = Math.min(kickCD, 4);
        }
        if (phase == 1 && HP < HT / 3) {
            phase = 2;
            stepCD = 6;
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) BossHealthBar.assignBoss(this);

        if (netCD > 0) netCD--;
        if (kickCD > 0) kickCD--;
        if (stepCD > 0) stepCD--;
        if (flashCD > 0) flashCD--;

        if (netWindup > 0) {
            netWindup--;
            if (netWindup == 0 && !netCells.isEmpty()) {
                resolveBladeNet();
                return true;
            }
        }

        if (enemy != null) {
            if (flashCD <= 0) {
                if (flashSever()) return true;
            }
            if (stepCD <= 0 && Dungeon.level.distance(pos, enemy.pos) > 2) {
                if (lightStep()) return true;
            }
            if (kickCD <= 0 && Dungeon.level.adjacent(pos, enemy.pos)) {
                if (crushingKick()) return true;
            }
            if (netCD <= 0) {
                if (telegraphBladeNet()) return true;
            }
        }

        return super.act();
    }

    private boolean telegraphBladeNet() {
        if (enemy == null) return false;
        netCells.clear();
        // create crossing lines centered on hero: + and X (if phase >= 1 includes diagonals)
        int[] dirs4 = new int[]{-Dungeon.level.width(), Dungeon.level.width(), -1, 1};
        for (int d : dirs4) collectLine(enemy.pos, d);
        if (phase >= 1) {
            int w = Dungeon.level.width();
            int[] dirsDiag = new int[]{-w-1, -w+1, w-1, w+1};
            for (int d : dirsDiag) collectLine(enemy.pos, d);
        }
        for (int c : netCells) {
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            CellEmitter.center(c).burst(SparkParticle.FACTORY, 1);
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(KarsLight.class, "s1"));
        netWindup = 1;
        spend(1f);
        return true;
    }

    private void resolveBladeNet() {
        Sample.INSTANCE.play(Assets.Sounds.TONIO);
        for (int c : netCells) {
            // Speck.LIGHT 이펙트 추가
            CellEmitter.get(c).burst(Speck.factory(Speck.LIGHT), 8);
            
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                // 강화된 블레이드 넷 데미지
                int dmg = Random.NormalIntRange(25, 35);
                ch.damage(dmg, this);
                if (Random.Int(2) == 0) Buff.affect(ch, Bleeding.class).set(0.3f * dmg);
                Buff.affect(ch, Hex.class, 2f);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        netCells.clear();
        netCD = phase >= 1 ? 6 : 8;
        spend(1f);
    }

    private void collectLine(int start, int step) {
        int c = start;
        while (Dungeon.level.insideMap(c) && !Dungeon.level.solid[c]) {
            if (!netCells.contains(c)) netCells.add(c);
            c += step;
        }
        c = start;
        while (Dungeon.level.insideMap(c) && !Dungeon.level.solid[c]) {
            if (!netCells.contains(c)) netCells.add(c);
            c -= step;
        }
    }

    private boolean crushingKick() {
        if (enemy == null) return false;
        
        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
        sprite.attack(enemy.pos);
        sprite.showStatus(CharSprite.WARNING, Messages.get(KarsLight.class, "s2"));
        
        // 강화된 킥 데미지
        int kickDmg = Random.NormalIntRange(15, 20) + (phase >= 2 ? 5 : 0);
        enemy.damage(kickDmg, this);
        
        // 적을 밀어내기
        int direction = enemy.pos - pos;
        Ballistica trajectory = new Ballistica(enemy.pos, enemy.pos + direction, Ballistica.MAGIC_BOLT);
        int knockbackDist = 2;
        
        int destination = enemy.pos;
        for (int i = 0; i < knockbackDist && trajectory.path.size() > i + 1; i++) {
            int next = trajectory.path.get(i + 1);
            if (Dungeon.level.solid[next] || Actor.findChar(next) != null) {
                // 벽이나 장애물에 부딪힘!
                Sample.INSTANCE.play(Assets.Sounds.HIT_CRUSH);
                CellEmitter.get(destination).burst(Speck.factory(Speck.ROCK), 8);
                
                // 강화된 벽 충돌 추가 데미지
                int wallDmg = Random.NormalIntRange(25, 35) + (phase >= 1 ? 10 : 0);
                enemy.damage(wallDmg, this);
                GLog.w(Messages.get(KarsLight.class, "wall_hit"));

                // 벽에 부딪혔을 때 스턴 또는 불구
                if (phase >= 1) {
                    Buff.affect(enemy, Paralysis.class, 2f);
                } else {
                    Buff.affect(enemy, Cripple.class, 3f);
                }
                break;
            }
            destination = next;
        }
        
        // 밀려남 이펙트
        if (destination != enemy.pos) {
            Actor.add(new Pushing(enemy, enemy.pos, destination));
            enemy.pos = destination;
            Dungeon.level.occupyCell(enemy);
            CellEmitter.get(destination).burst(Speck.factory(Speck.LIGHT), 5);
        }
        
        if (enemy == Dungeon.hero && !enemy.isAlive()) {
            Dungeon.fail(getClass());
        }
        
        kickCD = phase >= 1 ? 5 : 7;
        return true;
    }

    private boolean flashSever() {
        // instant multi-dash slices through 3 points around the target
        ArrayList<Integer> points = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8) {
            int p = enemy.pos + i;
            if (Dungeon.level.insideMap(p) && Dungeon.level.passable[p] && Actor.findChar(p) == null) points.add(p);
        }
        if (points.isEmpty()) return false;
        // pick up to 3 unique points
        ArrayList<Integer> route = new ArrayList<>();
        for (int i = 0; i < Math.min(3, points.size()); i++) {
            int pick = points.remove(Random.Int(points.size()));
            route.add(pick);
        }
        int cur = pos;
        sprite.showStatus(CharSprite.WARNING, Messages.get(KarsLight.class, "s3"));
        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        Sample.INSTANCE.play(Assets.Sounds.HIT);
        for (int dst : route) {
            Ballistica ray = new Ballistica(cur, dst, Ballistica.WONT_STOP);
            for (int c : ray.path) {
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != alignment) {
                    // 강화된 플래시 세버 데미지
                    int dmg = Random.NormalIntRange(18, 25) + (phase >= 2 ? 8 : 0);
                    Buff.affect(ch, Hex.class, 1f);
                    ch.damage(dmg, this);
                    if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
                }
            }
            // blink
            int old = pos;
            pos = dst;
            Dungeon.level.occupyCell(this);
            sprite.move(old, pos);
            cur = dst;
        }
        flashCD = 12;
        spend(1f);
        return true;
    }

    private boolean lightStep() {
        // short blink near enemy
        ArrayList<Integer> spots = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8) {
            int p = enemy.pos + i;
            if (Dungeon.level.insideMap(p) && Dungeon.level.passable[p] && Actor.findChar(p) == null) spots.add(p);
        }
        if (spots.isEmpty()) return false;
        int dst = spots.get(Random.Int(spots.size()));
        sprite.showStatus(CharSprite.WARNING, Messages.get(KarsLight.class, "s4"));
        Buff.affect(this, Invisibility.class, 2f);
        int old = pos;
        pos = dst;
        Dungeon.level.occupyCell(this);
        sprite.move(old, pos);
        spend(1f);
        stepCD = 10;
        return true;
    }

    @Override
    public int damageRoll() {
        // Beast 수준의 강화된 데미지
        return Random.NormalIntRange( 25, 40 );
    }

    @Override
    public int attackSkill( Char target ) {
        // Beast 수준의 강화된 공격 스킬
        return 45;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 15);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(NET_CD, netCD);
        bundle.put(KICK_CD, kickCD);
        bundle.put(STEP_CD, stepCD);
        bundle.put(FLASH_CD, flashCD);
        bundle.put(NET_WINDUP, netWindup);
        int[] arr = new int[netCells.size()];
        for (int i = 0; i < netCells.size(); i++) arr[i] = netCells.get(i);
        bundle.put(NET_CELLS, arr);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        netCD = bundle.getInt(NET_CD);
        kickCD = bundle.getInt(KICK_CD);
        stepCD = bundle.getInt(STEP_CD);
        flashCD = bundle.getInt(FLASH_CD);
        netWindup = bundle.getInt(NET_WINDUP);
        netCells.clear();
        for (int c : bundle.getIntArray(NET_CELLS)) netCells.add(c);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        hero.HP = hero.HT;

        InterlevelScene.mode = InterlevelScene.Mode.RETURN;
        InterlevelScene.returnDepth = 25;
        InterlevelScene.returnBranch = 0;
        InterlevelScene.returnPos = -2;
        Game.switchScene(InterlevelScene.class);

    }
}

