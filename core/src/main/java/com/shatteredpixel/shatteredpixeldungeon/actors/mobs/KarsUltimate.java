/*
 * Kars Ultimate Life Form - Final Boss
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class KarsUltimate extends Mob {

    {
        // Placeholder sprite
        spriteClass = ShamanSprite.Purple.class;

        HP = HT = 800;
        defenseSkill = 30;
        EXP = 50;
        maxLvl = 30;

        baseSpeed = 1.4f;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);

        // broad immunities befitting the ultimate life form
        immunities.add(Burning.class);
        immunities.add(Paralysis.class);
        immunities.add(Fire.class);
    }

    private int phase = 0;

    // abilities
    private int hamonCD = 10;      // 태양의 파문: 1턴 예고 후 큰 원형 범위 피해
    private int hamonWindup = 0;
    private ArrayList<Integer> hamonCells = new ArrayList<>();

    private int morphCD = 12;      // 생물 창조: 소환/변형 하수인 생성
    private int bladeCD = 6;       // 광휘 절단파: 직선 광선 절단
    private int prismCD = 9;       // 프리즘 스윕: 4방향 광선 예고 후 발동(방향/기점 토글)
    private boolean prismDiagonal = false;
    private boolean prismAtHero = false;
    private int prismWindup = 0;
    private ArrayList<Integer> prismCells = new ArrayList<>();

    // 신규: 태양의 다이얼(회전 안전쐐기 링)
    private int dialCD = 12;
    private boolean dialActive = false;
    private int dialStep = 0;         // 0: 예고1, 1: 예고2/피해
    private int dialRadius = 5;
    private int dialGapDir = 0;       // 0..7 (45도 단위)
    private ArrayList<Integer> dialCells = new ArrayList<>();

    // (moved to Esidisi) 라이트 라티스(격자 광선)

    // 특수 기믹: 화산 대분출 (페이즈2 시작 연출)
    private boolean eruptionActive = false;
    private int eruptionStep = 0;
    private ArrayList<Integer> eruptionCells = new ArrayList<>();

    private static final String PHASE = "phase";
    private static final String HAMON_CD = "hamon_cd";
    private static final String HAMON_WIND = "hamon_wind";
    private static final String HAMON_CELLS = "hamon_cells";
    private static final String MORPH_CD = "morph_cd";
    private static final String BLADE_CD = "blade_cd";
    private static final String PRISM_CD = "prism_cd";
    private static final String PRISM_DIAG = "prism_diag";
    private static final String PRISM_WIND = "prism_wind";
    private static final String PRISM_CELLS = "prism_cells";
    private static final String PRISM_AT_HERO = "prism_at_hero";
    private static final String DIAL_CD = "dial_cd";
    private static final String DIAL_ACTIVE = "dial_active";
    private static final String DIAL_STEP = "dial_step";
    private static final String DIAL_RADIUS = "dial_radius";
    private static final String DIAL_GAP = "dial_gap";
    private static final String DIAL_CELLS = "dial_cells";
    // lattice keys moved to Esidisi
    private static final String ERUPTION_ACTIVE = "eruption_active";
    private static final String ERUPTION_STEP = "eruption_step";

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) BossHealthBar.assignBoss(this);
        sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "notice"));
    }

    @Override
    public void damage(int dmg, Object src) {
        // ultimate resilience: cap incoming damage and regen will counter
        if (dmg > 45) dmg = 45;
        super.damage(dmg, src);
        BossHealthBar.assignBoss(this);

        if (phase == 0 && HP < HT * 2 / 3) {
            phase = 1;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "evolve"));
            hamonCD = Math.min(hamonCD, 8);
            morphCD = Math.min(morphCD, 10);
        }
        if (phase == 1 && HP < HT / 3) {
            phase = 2;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "apex"));
            bladeCD = 4;
            // trigger eruption sequence at phase 2 start
            startEruption();
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) BossHealthBar.assignBoss(this);

        // passive regeneration and senses
        if (HP < HT) HP = Math.min(HT, HP + (phase >= 1 ? 6 : 4));

        if (hamonCD > 0) hamonCD--;
        if (morphCD > 0) morphCD--;
        if (bladeCD > 0) bladeCD--;
        if (prismCD > 0) prismCD--;
        if (dialCD > 0) dialCD--;

        // resolve hamon if telegraphed
        if (hamonWindup > 0) {
            hamonWindup--;
            if (hamonWindup == 0 && !hamonCells.isEmpty()) {
                resolveHamon();
                return true;
            }
        }

        // resolve prism sweep if telegraphed
        if (prismWindup > 0) {
            prismWindup--;
            if (prismWindup == 0 && !prismCells.isEmpty()) {
                resolvePrism();
                return true;
            }
        }

        // eruption sequence progression
        if (eruptionActive) {
            if (advanceEruption()) return true;
        }

        // dial progression if active
        if (dialActive) {
            if (advanceDial()) return true;
        }

        if (enemy != null) {
            if (bladeCD <= 0) {
                if (bladeWave()) return true;
            }
            if (prismCD <= 0 && prismWindup == 0) {
                if (telegraphPrism()) return true;
            }
            // lattice removed from KarsUltimate
            if (dialCD <= 0 && !dialActive) {
                if (startDial()) return true;
            }
            if (hamonCD <= 0) {
                if (telegraphHamon()) return true;
            }
            if (morphCD <= 0) {
                if (morphSpawn()) return true;
            }
        }

        return super.act();
    }

    private boolean telegraphPrism() {
        prismCells.clear();
        int[] dirs;
        int w = Dungeon.level.width();
        if (!prismDiagonal) {
            dirs = new int[]{-w, w, -1, 1};
        } else {
            dirs = new int[]{-w-1, -w+1, w-1, w+1};
        }
        int origin = prismAtHero && enemy != null ? enemy.pos : pos;
        for (int d : dirs) collectLine(prismCells, origin, d);
        for (int c : prismCells) {
            sprite.parent.addToBack(new TargetedCell(c, 0xCCE6FF));
            CellEmitter.center(c).burst(SparkParticle.FACTORY, 1);
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "prism_ready"));
        prismWindup = 1;
        spend(1f);
        return true;
    }

    private void resolvePrism() {
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "prism"));
        for (int c : prismCells) {
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(16, 22) + (phase >= 1 ? 6 : 0);
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        prismCells.clear();
        prismDiagonal = !prismDiagonal; // toggle orientation
        prismAtHero = !prismAtHero;     // toggle origin between self and hero
        prismCD = 7;
        spend(1f);
    }

    private void collectLine(ArrayList<Integer> out, int start, int step) {
        int c = start;
        while (Dungeon.level.insideMap(c) && !Dungeon.level.solid[c]) {
            if (!out.contains(c)) out.add(c);
            c += step;
        }
        c = start;
        while (Dungeon.level.insideMap(c) && !Dungeon.level.solid[c]) {
            if (!out.contains(c)) out.add(c);
            c -= step;
        }
    }

    // lattice removed from KarsUltimate

    // 태양의 다이얼(회전 안전쐐기 링)
    private boolean startDial() {
        dialCells.clear();
        dialRadius = phase >= 2 ? 6 : 5;
        dialGapDir = Random.Int(8);
        dialStep = 0;
        // collect ring cells around Kars
        int w = Dungeon.level.width();
        int len = Dungeon.level.length();
        int origin = (enemy != null ? enemy.pos : pos);
        int cx = origin % w, cy = origin / w;
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
            int x = c % w, y = c / w;
            double dist = Math.hypot(x - cx, y - cy);
            if (Math.abs(dist - dialRadius) <= 1.0) {
                if (!inGap(cx, cy, x, y, dialGapDir)) {
                    if (Random.Int(3) == 0) dialCells.add(c);
                }
            }
        }
        for (int c : dialCells) sprite.parent.addToBack(new TargetedCell(c, 0xFFCC99));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "dial_ready"));
        dialActive = true;
        spend(1f);
        return true;
    }

    private boolean advanceDial() {
        if (dialStep == 0) {
            // rotate the safe wedge and retrace ring
            dialStep = 1;
            dialCells.clear();
            dialGapDir = (dialGapDir + 1) % 8; // rotate 45 deg
            int w = Dungeon.level.width();
            int len = Dungeon.level.length();
            int origin = (enemy != null ? enemy.pos : pos);
            int cx = origin % w, cy = origin / w;
            for (int c = 0; c < len; c++) {
                if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
                int x = c % w, y = c / w;
                double dist = Math.hypot(x - cx, y - cy);
                if (Math.abs(dist - dialRadius) <= 1.0) {
                    if (!inGap(cx, cy, x, y, dialGapDir)) {
                        if (Random.Int(3) == 0) dialCells.add(c);
                    }
                }
            }
            for (int c : dialCells) sprite.parent.addToBack(new TargetedCell(c, 0xFFAA66));
            spend(1f);
            return true;
        } else {
            // detonate ring
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "dial"));
            for (int c : dialCells) {
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != alignment) {
                    ch.damage(Random.NormalIntRange(22, 30) + (phase >= 2 ? 6 : 0), this);
                    if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
                }
            }
            dialActive = false;
            dialStep = 0;
            dialCells.clear();
            dialCD = 12;
            spend(1f);
            return true;
        }
    }

    private boolean inGap(int cx, int cy, int x, int y, int dirIndex) {
        double ang = Math.atan2(y - cy, x - cx); // radians
        double deg = ang * (180.0 / Math.PI);
        if (deg < 0) deg += 360.0;
        double gapCenter = dirIndex * 45.0;
        double diff = Math.abs(((deg - gapCenter + 540) % 360) - 180); // angular difference 0..180
        return diff <= 20.0; // 40-degree safe wedge (harder)
    }

    private boolean telegraphHamon() {
        // choose radius based on phase
        int r = phase >= 2 ? 6 : 5;
        hamonCells.clear();
        int len = Dungeon.level.length();
        int w = Dungeon.level.width();
        int cx = pos % w;
        int cy = pos / w;
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
            int x = c % w;
            int y = c / w;
            double dist = Math.hypot(x - cx, y - cy);
            if (dist <= r) {
                hamonCells.add(c);
                sprite.parent.addToBack(new TargetedCell(c, 0xFFFF66));
                CellEmitter.center(c).burst(SparkParticle.FACTORY, 1);
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "hamon_ready"));
        hamonWindup = 1;
        spend(1f);
        return true;
    }

    private void resolveHamon() {
        // devastate the telegraphed area
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "hamon"));
        for (int c : hamonCells) {
            sprite.parent.addToBack(new TargetedCell(c, 0xFFFFAA));
            CellEmitter.center(c).burst(BlastParticle.FACTORY, 2);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(28, 40) + (phase >= 2 ? 8 : 0);
                ch.damage(dmg, this);
                Buff.affect(ch, Burning.class).reignite(ch);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        hamonCells.clear();
        hamonCD = phase >= 2 ? 8 : 10;
        spend(1f);
    }

    private boolean bladeWave() {
        // high-speed monomolecular cut in a line
        Ballistica beam = new Ballistica(pos, enemy.pos, Ballistica.WONT_STOP);
        boolean hit = false;
        for (int p : beam.path) {
            sprite.parent.addToBack(new TargetedCell(p, 0xFFEECC));
            Char ch = Actor.findChar(p);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(18, 26) + (phase >= 1 ? 6 : 0);
                ch.damage(dmg, this);
                if (Random.Int(2) == 0) Buff.affect(ch, Bleeding.class).set(0.3f * dmg);
                hit = true;
            }
        }
        if (hit) Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
        bladeCD = phase >= 2 ? 4 : 6;
        spend(1f);
        return true;
    }

    private boolean morphSpawn() {
        // spawn up to 2 swift minions around target area
        int summons = 2;
        int placed = 0;
        for (int i : PathFinder.NEIGHBOURS8) {
            int p = enemy.pos + i;
            if (Dungeon.level.insideMap(p) && Dungeon.level.passable[p] && Actor.findChar(p) == null) {
                Mob m = new Niku(); // reuse fast small minion
                m.pos = p;
                m.state = m.HUNTING;
                GameScene.add(m, 1);
                placed++;
                sprite.parent.addToBack(new TargetedCell(p, 0xFF00FF));
                if (placed >= summons) break;
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "morph"));
        morphCD = 12;
        spend(1f);
        return placed > 0;
    }

    private void startEruption() {
        eruptionActive = true;
        eruptionStep = 0;
    }

    private boolean advanceEruption() {
        // 2-step sequence: telegraph -> eruption
        if (eruptionStep == 0) {
            eruptionCells.clear();
            int len = Dungeon.level.length();
            for (int c = 0; c < len; c++) {
                if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
                eruptionCells.add(c);
                sprite.parent.addToBack(new TargetedCell(c, 0xFF9966));
            }
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "eruption_ready"));
            eruptionStep = 1;
            spend(1f);
            return true;
        } else {
            // erupt
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            for (int c : eruptionCells) {
                CellEmitter.center(c).burst(BlastParticle.FACTORY, 2);
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != alignment) {
                    ch.damage(Random.NormalIntRange(18, 26), this);
                }
                GameScene.add(com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob.seed(c, 4, Fire.class));
            }
            eruptionActive = false;
            eruptionStep = 0;
            spend(1f);
            return true;
        }
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        // extreme resilience to projectiles
        if (enemy instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) {
            return (int) Math.ceil(damage * 0.7f);
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public int damageRoll() { return Random.NormalIntRange(18, 28); }

    @Override
    public int attackSkill(Char target) { return 36; }

    @Override
    public int drRoll() { return Random.NormalIntRange(4, 8); }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(HAMON_CD, hamonCD);
        bundle.put(HAMON_WIND, hamonWindup);
        int[] arr = new int[hamonCells.size()];
        for (int i = 0; i < hamonCells.size(); i++) arr[i] = hamonCells.get(i);
        bundle.put(HAMON_CELLS, arr);
        bundle.put(MORPH_CD, morphCD);
        bundle.put(BLADE_CD, bladeCD);
        bundle.put(PRISM_CD, prismCD);
        bundle.put(PRISM_DIAG, prismDiagonal);
        bundle.put(PRISM_WIND, prismWindup);
        int[] pc = new int[prismCells.size()];
        for (int i = 0; i < prismCells.size(); i++) pc[i] = prismCells.get(i);
        bundle.put(PRISM_CELLS, pc);
        bundle.put(PRISM_AT_HERO, prismAtHero);
        bundle.put(ERUPTION_ACTIVE, eruptionActive);
        bundle.put(ERUPTION_STEP, eruptionStep);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        hamonCD = bundle.getInt(HAMON_CD);
        hamonWindup = bundle.getInt(HAMON_WIND);
        hamonCells.clear();
        for (int c : bundle.getIntArray(HAMON_CELLS)) hamonCells.add(c);
        morphCD = bundle.getInt(MORPH_CD);
        bladeCD = bundle.getInt(BLADE_CD);
        prismCD = bundle.getInt(PRISM_CD);
        prismDiagonal = bundle.getBoolean(PRISM_DIAG);
        prismWindup = bundle.getInt(PRISM_WIND);
        prismCells.clear();
        for (int c : bundle.getIntArray(PRISM_CELLS)) prismCells.add(c);
        prismAtHero = bundle.getBoolean(PRISM_AT_HERO);
        eruptionActive = bundle.getBoolean(ERUPTION_ACTIVE);
        eruptionStep = bundle.getInt(ERUPTION_STEP);
    }
}


