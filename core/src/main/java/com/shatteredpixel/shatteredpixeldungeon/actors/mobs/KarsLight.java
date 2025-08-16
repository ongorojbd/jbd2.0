/*
 * Light Mode Kars - agile blade boss focused on high-speed cutting and ranged cone slashes.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM201Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EsidisiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KarsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class KarsLight extends Mob {

    {
        // placeholder sprite; replace when custom sprite is available
        spriteClass = KarsSprite.class;

        HP = HT = 450;
        defenseSkill = 24;
        EXP = 30;
        maxLvl = 30;

        baseSpeed = 1.3f;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    private int phase = 0;
    private int netCD = 8;       // 블레이드 넷 (십자/대각 교차 절단, 예고 1턴)
    private int danceCD = 8;     // 블레이드 댄스 (근접 다단 히트)
    private int stepCD = 10;     // 라이트스텝 (짧은 은신/이동)
    private int flashCD = 12;    // 플래시 세버 (순간이동 절단)

    private int netWindup = 0;
    private ArrayList<Integer> netCells = new ArrayList<>();

    private static final String PHASE = "phase";
    // SWEEP_CD deprecated, kept for backward compatibility with older saves
    private static final String SWEEP_CD = "sweep_cd";
    private static final String DANCE_CD = "dance_cd";
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

            if (Dungeon.hero.heroClass == HeroClass.MAGE) {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new KarsSprite(), new KarsSprite(), new KarsSprite()},
                        new String[]{"카즈", "카즈", "카즈"},
                        new String[]{
                                Messages.get(KarsLight.class, "t1"),
                                Messages.get(KarsLight.class, "t2"),
                                Messages.get(KarsLight.class, "t3")
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );
            }
            else {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new KarsSprite(), new KarsSprite(), new KarsSprite()},
                        new String[]{"카즈", "카즈", "카즈"},
                        new String[]{
                                Messages.get(KarsLight.class, "t1"),
                                Messages.get(KarsLight.class, "t2"),
                                Messages.get(KarsLight.class, "t3")
                        },
                        new byte[]{
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
        super.damage(dmg, src);

        if (phase == 0 && HP < HT * 2 / 3) {
            phase = 1;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "sharper"));
            netCD = Math.min(netCD, 5);
            danceCD = Math.min(danceCD, 6);
        }
        if (phase == 1 && HP < HT / 3) {
            phase = 2;
            Music.INSTANCE.play(Assets.Music.TENDENCY3, true);
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "ultimate"));
            stepCD = 6;
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) BossHealthBar.assignBoss(this);

        if (netCD > 0) netCD--;
        if (danceCD > 0) danceCD--;
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
            if (danceCD <= 0 && Dungeon.level.adjacent(pos, enemy.pos)) {
                if (bladeDance()) return true;
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
            sprite.parent.addToBack(new TargetedCell(c, 0xFFFF66));
            CellEmitter.center(c).burst(SparkParticle.FACTORY, 1);
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "net_ready"));
        netWindup = 1;
        spend(1f);
        return true;
    }

    private void resolveBladeNet() {
        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "net"));
        for (int c : netCells) {
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(16, 22) + (phase >= 1 ? 4 : 0);
                ch.damage(dmg, this);
                if (Random.Int(2) == 0) Buff.affect(ch, Bleeding.class).set(0.3f * dmg);
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

    private boolean bladeDance() {
        // three rapid adjacent strikes around the enemy
        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "dance"));
        int hits = 3;
        for (int i = 0; i < hits; i++) {
            int targetPos = enemy.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
            if (!Dungeon.level.insideMap(targetPos)) targetPos = enemy.pos;
            sprite.parent.addToBack(new TargetedCell(targetPos, 0xFFEECC));
            Char ch = Actor.findChar(targetPos);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(10, 14) + (phase >= 2 ? 4 : 0);
                ch.damage(dmg, this);
                if (Random.Int(2) == 0) Buff.affect(ch, Bleeding.class).set(0.25f * dmg);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        danceCD = phase >= 1 ? 6 : 8;
        spend(1f);
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
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "flash"));
        for (int dst : route) {
            Ballistica ray = new Ballistica(cur, dst, Ballistica.WONT_STOP);
            for (int c : ray.path) {
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != alignment) {
                    int dmg = Random.NormalIntRange(10, 14) + (phase >= 2 ? 5 : 0);
                    ch.damage(dmg, this);
                    if (Random.Int(3) == 0) Buff.affect(ch, Bleeding.class).set(0.25f * dmg);
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
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "step"));
        Buff.affect(this, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility.class, 2f);
        int old = pos;
        pos = dst;
        Dungeon.level.occupyCell(this);
        sprite.move(old, pos);
        spend(1f);
        stepCD = 10;
        return true;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        // cut incoming missiles: greatly reduce damage from projectile attacks
        if (enemy instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
                && ((com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) enemy).belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon) {
            sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "cut_bullets"));
            return Math.max(0, (int)(damage * 0.2f));
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public int damageRoll() { return Random.NormalIntRange(14, 22); }

    @Override
    public int attackSkill(Char target) { return 32; }

    @Override
    public int drRoll() { return Random.NormalIntRange(1, 5); }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(NET_CD, netCD);
        bundle.put(DANCE_CD, danceCD);
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
        danceCD = bundle.getInt(DANCE_CD);
        stepCD = bundle.getInt(STEP_CD);
        flashCD = bundle.getInt(FLASH_CD);
        netWindup = bundle.getInt(NET_WINDUP);
        netCells.clear();
        for (int c : bundle.getIntArray(NET_CELLS)) netCells.add(c);
    }
}


