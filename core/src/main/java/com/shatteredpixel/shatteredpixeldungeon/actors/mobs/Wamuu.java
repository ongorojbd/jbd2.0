/*
 * Wamuu (Full Duel) - 3-phase boss: Chariot → Wind-Sense → Final Mode (혼설삽)
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SkyParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KarsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuChariot2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuChariotSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Wamuu extends Mob {

    {
        // Phase 1: Wamuu riding a chariot
        spriteClass = WammuChariotSprite.class;
        HP = HT = 900;
        EXP = 40;
        defenseSkill = 20;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(18, 30);
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 10);
    }

    private int phase = 0;            // 0: Chariot, 1: Wind-Sense, 2: Final Mode

    // shared/phase-specific cooldowns
    private int protectorCD = 10;     // 바람의 프로텍터 (utility)
    private int sandstormCD = 8;      // phase 1 only
    private int wireCD = 9;           // all phases
    private int finalModeCD = 18;     // phase 3 opener
    private int windSenseCD = 6;      // phase 2 only: blind wind-sense sweep
    private int blinkCD = 4;          // phase 3 only: periodic blink

    // sandstorm windup/targeting (phase 1 only)
    private int sandWindup = 0;
    private ArrayList<Integer> sandCells = new ArrayList<>();

    // wire pattern (ring waves), reused across phases
    private int wireWindup = 0;
    private boolean wireActive = false;
    private int wireStage = 0; // 0: small, 1: mid, 2: large
    private ArrayList<Integer> wireRing1 = new ArrayList<>();
    private ArrayList<Integer> wireRing2 = new ArrayList<>();
    private ArrayList<Integer> wireRing3 = new ArrayList<>();

    // phase 3: final mode spiral state
    private boolean finalActive = false;
    private int finalStage = 0;            // used as cycle counter
    private int finalWindup = 0;
    private ArrayList<Integer> finalCells = new ArrayList<>();
    private double finalAngle = 0;         // degrees, rotating clock hand
    private double finalWidth = 60;        // sector width in degrees (narrow sweep)
    private int finalRadius = Integer.MAX_VALUE; // effectively whole map
    private int finalSpinDir = 1;          // 1 or -1
    private double finalSpinStep = 35;     // degrees per cycle
    // approaching-band state (Tengu fire-like)
    private int finalDir = -1;             // direction index along path to hero (0..7)
    private int finalStepIndex = 0;        // current step along ballistica path

    private boolean sandstormPending = false;
    // Phase 1: chariot charge
    private int chariotWindup = 0;
    private ArrayList<Integer> chariotPath = new ArrayList<>();
    private int chariotCD = 4;
    private int windSenseWindup = 0;
    private ArrayList<Integer> windSenseCells = new ArrayList<>();
    private int lastSensePos = -1;
    private int windSenseMode = 0;         // 0: rays, 1: echo ring
    private ArrayList<Integer> windEchoCells = new ArrayList<>();

    private static final String PHASE = "phase";
    private static final String PROT_CD = "prot_cd";
    private static final String SAND_CD = "sand_cd";
    private static final String WIRE_CD = "wire_cd";
    private static final String FINAL_CD = "final_cd";
    private static final String SAND_WIND = "sand_wind";
    private static final String WIRE_WIND = "wire_wind";
    private static final String FINAL_ACTIVE = "final_active";
    private static final String FINAL_STAGE = "final_stage";
    private static final String FINAL_WINDUP = "final_windup";
    private static final String FINAL_CELLS = "final_cells";
    private static final String FINAL_ANGLE = "final_angle";
    private static final String FINAL_DIR = "final_dir";
    private static final String SENSE_CD = "sense_cd";
    private static final String SENSE_WIND = "sense_wind";
    private static final String SENSE_LAST = "sense_last";
    private static final String SENSE_MODE = "sense_mode";

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new WammuChariotSprite(), new WammuChariotSprite(), new WammuChariotSprite()},
                    new String[]{"와무우", "와무우", "와무우"},
                    new String[]{
                            Messages.get(Wamuu.class, "t1"),
                            Messages.get(Wamuu.class, "t2"),
                            Messages.get(Wamuu.class, "t3")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                    }
            );
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 250) dmg = 250;
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src);

        // Phase changes: 0 -> 1 at < 2/3 HP, 1 -> 2 at < 1/3 HP
        if (phase == 0 && HP < HT * 2 / 3) {
            phase = 1;

            // dismount: swap to Wamuu on foot
            if (sprite != null) sprite.killAndErase();
            spriteClass = WammuChariot2Sprite.class;
            GameScene.addSprite(this);

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new WammuChariotSprite(), new WammuChariot2Sprite()},
                    new String[]{"와무우", "와무우"},
                    new String[]{
                            Messages.get(Wamuu.class, "t4"),
                            Messages.get(Wamuu.class, "t5")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );

            Music.INSTANCE.play(Assets.Music.TENDENCY3, true);
            Buff.affect(this, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WamuuWindBlind.class, 999999f);

            // seal sandstorm after this point
            sandstormCD = 999;
            sandWindup = 0;
            sandCells.clear();
            protectorCD = Math.min(protectorCD, 8);

            // ensure chariot charge won't occur immediately upon phase 1 start
            chariotCD = Math.max(chariotCD, 2);
            chariotWindup = 3;
            chariotPath.clear();
        }
        if (phase == 1 && HP < HT / 3) {
            phase = 2;
            Music.INSTANCE.play(Assets.Music.TENDENCY2, true);

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new WammuChariot2Sprite(), new WammuChariot2Sprite(), new WammuChariot2Sprite(), new KarsSprite(), new WammuChariot2Sprite()},
                    new String[]{"와무우", "와무우", "와무우", "카즈", "와무우"},
                    new String[]{
                            Messages.get(Wamuu.class, "t6"),
                            Messages.get(Wamuu.class, "t7"),
                            Messages.get(Wamuu.class, "t8"),
                            Messages.get(Wamuu.class, "t9"),
                            Messages.get(Wamuu.class, "t10")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );


            // visually accentuate and apply visual-only blind state to self
            if (sprite != null) sprite.aura(0x66CCFF, 4);
            finalModeCD = 8;
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) BossHealthBar.assignBoss(this);

        if (protectorCD > 0) protectorCD--;
        if (sandstormCD > 0) sandstormCD--;
        if (wireCD > 0) wireCD--;
        if (finalModeCD > 0) finalModeCD--;
        if (windSenseCD > 0) windSenseCD--;
        if (blinkCD > 0) blinkCD--;
        if (chariotCD > 0) chariotCD--;

        // Phase 3: allow blink even during final pattern; if blinking mid-cast, re-telegraph to new origin
        if (phase >= 2 && blinkCD <= 0) {
            new com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf().activate(this);
            blinkCD = 999;
            if (finalActive) {
                finalActive = false;
                finalWindup = 0;
                finalCells.clear();
                if (telegraphFinalWave()) return true;
            }
        }

        // resolve windups
        // Phase 0: sandstorm windup resolution
        if (phase == 0 && sandWindup > 0) {
            if (!sandCells.isEmpty()) {
                for (int c : sandCells) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            }
            sandWindup--;
            if (sandWindup == 0 && !sandCells.isEmpty()) {
                resolveSandstorm();
                return true;
            } else {
                spend(1f);
                return true;
            }
        }

        // Phase 1: chariot windup resolution
        if (phase == 1 && chariotWindup > 0) {
            chariotWindup--;
            if (chariotWindup == 0 && !chariotPath.isEmpty()) {
                resolveChariot();
                return true;
            } else if (chariotWindup > 0) {
                for (int c : chariotPath) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
                spend(TICK);
                return true;
            }
        }

        if (wireWindup > 0) {
            wireWindup--;
            if (wireWindup == 0 && wireActive) {
                if (resolveWireTick()) return true;
                return super.act();
            }
        }

        if (windSenseWindup > 0) {
            windSenseWindup--;
            if (windSenseWindup == 0 && !windSenseCells.isEmpty()) {
                resolveWindSense();
                return true;
            } else if (windSenseWindup > 0) {
                // keep telegraph visible
                for (int c : windSenseCells) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
                spend(1f);
                return true;
            }
        }
        // final mode sequencing
        if (finalActive) {
            if (finalWindup > 0) {
                finalWindup--;
                if (finalWindup == 0 && !finalCells.isEmpty()) {
                    resolveFinalWave();
                    return true;
                } else if (finalWindup > 0) {
                    for (int c : finalCells) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
                    spend(1f);
                    return true;
                }
            } else {
                // safety: if windup ended but no cells were queued, reset state so we can re-telegraph
                finalActive = false;
            }
        }

        if (phase < 2 && enemy != null) {
            // Phase 0: allow sandstorm
            if (phase == 0 && sandstormCD <= 0) {
                if (!wireActive && sandWindup == 0) {
                    if (telegraphSandstorm()) return true;
                } else sandstormPending = true;
            }
            // Phase 1: allow chariot
            if (phase == 1 && chariotCD <= 0) {
                if (!wireActive && chariotWindup == 0) {
                    if (telegraphChariot()) return true;
                }
            }
            // Wire is always allowed
            if (wireCD <= 0 && !wireActive) {
                if (telegraphWire()) return true;
            }
            // Phase 1 only: echo ring (no rays)
            if (phase == 1 && windSenseCD <= 0 && windSenseWindup == 0 && !wireActive) {
                if (telegraphWindSense()) return true;
            }
        }
        // Phase 3: only final mode, continuously
        if (phase >= 2) {
            // short-circuit other actions; run only final mode
            if (!finalActive && finalWindup == 0) {
                if (telegraphFinalWave()) return true;
            } else if (finalActive && finalWindup == 0) {
                // safety fallback: if active but no windup/cells, re-telegraph
                if (telegraphFinalWave()) return true;
            }
            spend(TICK);
            return true;
        }

        return super.act();
    }

    // Phase 1: Wind-Sense – echo ring only
    private boolean telegraphWindSense() {
        windSenseCells.clear(); windEchoCells.clear();
        windSenseMode = 1; // echo ring only
        // echo ring: 2-tile thick ring at radius 2 and 3 around Wamuu
        buildRing(pos % Dungeon.level.width(), pos / Dungeon.level.width(), 2.0, 0.6, windEchoCells);
        buildRing(pos % Dungeon.level.width(), pos / Dungeon.level.width(), 3.0, 0.6, windEchoCells);
        windSenseCells.addAll(windEchoCells);
        for (int c : windSenseCells) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "wind"));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        windSenseWindup = 1;
        spend(1f);
        return true;
    }

    private void resolveWindSense() {
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        // echo: 2-stage pulse, inner ring then outer ring next turn (here applied in one go as before)
        for (int c : windEchoCells) {
            CellEmitter.center(c).burst(SkyParticle.FACTORY, 2);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(10, 18);
                ch.damage(dmg, this);
                if (ch == Dungeon.hero) Buff.prolong(Dungeon.hero, Blindness.class, 3f);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        windSenseCells.clear(); windEchoCells.clear();
        windSenseCD = 6;
        spend(1f);
    }

    // Phase 0: 신사폭풍 (원형 부채꼴) 예고
    private boolean telegraphSandstorm() {
        if (enemy == null) return false;
        sandCells.clear();
        Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.WONT_STOP);
        ConeAOE cone = new ConeAOE(aim, Float.POSITIVE_INFINITY, 70, Ballistica.STOP_SOLID);
        for (int c : cone.cells) {
            if (!Dungeon.level.insideMap(c)) continue;
            sandCells.add(c);
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            CellEmitter.center(c).burst(SkyParticle.FACTORY, 1);
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "sandstorm"));
        sandWindup = 2;
        spend(1f);
        return true;
    }

    // Phase 1: 전차 돌진 예고/해결
    private boolean telegraphChariot() {
        if (enemy == null) return false;
        chariotPath.clear();
        Ballistica path = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
        if (path.dist <= 0) return false;
        int endIndex = path.dist;
        if (path.collisionPos != null && path.collisionPos == enemy.pos) {
            endIndex = Math.min(path.dist + 1, path.path.size() - 1);
        }
        chariotPath.addAll(path.subPath(1, endIndex));
        if (chariotPath.isEmpty()) return false;
        for (int c : chariotPath) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));

        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "horse"));
        Dungeon.hero.interrupt();
        Sample.INSTANCE.play(Assets.Sounds.HORSE);

        chariotWindup = 1;
        // strictly one turn of windup to avoid occasional extra wait
        spend(TICK);
        return true;
    }

    private void resolveChariot() {
        for (int c : chariotPath) {
            if (Dungeon.hero.pos == c) {
                int dmg = Random.NormalIntRange(18, 28);
                Dungeon.hero.damage(dmg, this);
                if (!Dungeon.hero.isAlive()) Dungeon.fail(getClass());
                break;
            }
        }
        sprite.emitter().burst(Speck.factory(Speck.JET), 12);
        int dest = pos;
        for (int i = chariotPath.size() - 1; i >= 0; i--) {
            int cell = chariotPath.get(i);
            if (!Dungeon.level.solid[cell] && Actor.findChar(cell) == null) { dest = cell; break; }
        }
        int old = pos;
        pos = dest;
        Actor.add(new Pushing(this, old, pos));
        chariotPath.clear();
        chariotWindup = 0;
        chariotCD = 4;
        spend(1f);
    }

    private void resolveSandstorm() {
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        boolean terrainAffected = false;
        for (int c : sandCells) {
            CellEmitter.center(c).burst(SkyParticle.FACTORY, 6);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(16, 22);
                ch.damage(dmg, this);
                Buff.affect(ch, Bleeding.class).set(0.35f * dmg);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        int len = Dungeon.level.length();
        for (int c = 0; c < len; c++) {
            if (Dungeon.level.map[c] == Terrain.WALL_DECO) {
                Level.set(c, Terrain.EMBERS);
                GameScene.updateMap(c);
                terrainAffected = true;
            }
        }
        if (terrainAffected) Dungeon.observe();
        sandCells.clear();
        sandstormCD = 8;
        spend(1f);
    }

    // 와이어(회오리 바람): 도넛 3연격, 1턴 간격으로 확장
    private boolean telegraphWire() {
        wireRing1.clear(); wireRing2.clear(); wireRing3.clear();
        int w = Dungeon.level.width();
        int cx = pos % w, cy = pos / w;
        buildRing(cx, cy, 1.0, 0.6, wireRing1);
        buildRing(cx, cy, 2.0, 0.6, wireRing2);
        buildRing(cx, cy, 3.0, 0.6, wireRing3);
        for (int c : wireRing1) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "wire_ready"));
        wireActive = true;
        wireStage = 0;
        wireWindup = 1;
        spend(1f);
        return true;
    }

    private boolean resolveWireTick() {
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        ArrayList<Integer> ring = (wireStage == 0 ? wireRing1 : wireStage == 1 ? wireRing2 : wireRing3);
        for (int c : ring) {
            CellEmitter.center(c).burst(SkyParticle.FACTORY, 2);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int bonus = (wireStage == 2 ? 2 : 0);
                int dmg = Random.NormalIntRange(12, 16) + bonus;
                ch.damage(dmg, this);
                Buff.affect(ch, Bleeding.class).set(0.25f * dmg);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        if (wireStage < 2) {
            wireStage++;
            ArrayList<Integer> nextRing = (wireStage == 1 ? wireRing2 : wireRing3);
            for (int c : nextRing) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            wireWindup = 1;
            return false;
        } else {
            wireActive = false;
            wireRing1.clear(); wireRing2.clear(); wireRing3.clear();
            wireCD = 9;
            if (sandstormPending && sandstormCD <= 0 && phase == 0) {
                sandstormPending = false;
                // telegraph spends time itself, so signal caller to stop further acting
                telegraphSandstorm();
                return true;
            } else if (phase == 1 && chariotCD <= 0 && chariotWindup == 0) {
                telegraphChariot();
                return true;
            }
            return false;
        }
    }

    private void buildRing(int cx, int cy, double radius, double thickness, ArrayList<Integer> out) {
        int w = Dungeon.level.width();
        int len = Dungeon.level.length();
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
            int x = c % w, y = c / w;
            double dist = Math.hypot(x - cx, y - cy);
            if (Math.abs(dist - radius) <= thickness) out.add(c);
        }
    }

    // Phase 3 Final: approaching band towards the hero (Tengu fire-like)
    private boolean telegraphFinalWave() {
        if (enemy == null) return false;
        finalCells.clear();

        Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.WONT_STOP);
        if (aim.path.size() < 2) return false;

        // reset or continue progression
        if (finalStepIndex <= 0 || finalStepIndex >= aim.path.size()) {
            finalStepIndex = 1;
        }

        // get current position along path
        int centerCell = aim.path.get(Math.min(finalStepIndex, aim.path.size()-1));
        
        // add band cells (7-wide: center + 3 on each side perpendicular to path)
        addWideBandCells(centerCell, aim, finalCells);

        for (int c : finalCells) sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        finalWindup = 1;
        finalActive = true;
        spend(1f);
        return true;
    }

    private void resolveFinalWave() {
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        // self drain while sustaining final mode
        int selfDmg = Random.NormalIntRange(2, 4);
        this.damage(selfDmg, this);
        for (int c : finalCells) {
            CellEmitter.center(c).burst(SkyParticle.FACTORY, 6);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(22, 34) + 6;
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        finalCells.clear();
        
        // advance the band 3 steps closer to hero (faster approach)
        finalStepIndex += 3;
        finalStage++;
        finalActive = false;
        
        // immediately telegraph next wave
        telegraphFinalWave();
    }

    private void addWideBandCells(int center, Ballistica aim, ArrayList<Integer> out){
        if (!Dungeon.level.insideMap(center)) return;
        
        // add center cell
        if (!Dungeon.level.solid[center]) out.add(center);
        
        // determine direction from previous to current cell to get perpendicular
        int prevIdx = Math.max(0, finalStepIndex - 1);
        int prev = aim.path.get(prevIdx);
        int dx = (center % Dungeon.level.width()) - (prev % Dungeon.level.width());
        int dy = (center / Dungeon.level.width()) - (prev / Dungeon.level.width());
        
        // perpendicular offsets (rotate 90 degrees)
        int perpX = -dy;
        int perpY = dx;
        
        // add cells perpendicular to path (3 on each side for 7-wide band)
        for (int i = -3; i <= 3; i++) {
            if (i == 0) continue; // center already added
            int offsetX = i * perpX;
            int offsetY = i * perpY;
            int cell = center + offsetX + offsetY * Dungeon.level.width();
            if (Dungeon.level.insideMap(cell) && !Dungeon.level.solid[cell]) {
                out.add(cell);
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(PROT_CD, protectorCD);
        bundle.put(SAND_CD, sandstormCD);
        bundle.put(WIRE_CD, wireCD);
        bundle.put(FINAL_CD, finalModeCD);
        bundle.put(SAND_WIND, sandWindup);
        bundle.put(WIRE_WIND, wireWindup);
        bundle.put(FINAL_ACTIVE, finalActive);
        bundle.put(FINAL_STAGE, finalStage);
        bundle.put(FINAL_WINDUP, finalWindup);
        int[] fc = new int[finalCells.size()];
        for (int i = 0; i < finalCells.size(); i++) fc[i] = finalCells.get(i);
        bundle.put(FINAL_CELLS, fc);
        bundle.put(FINAL_ANGLE, (float) finalAngle);
        bundle.put(FINAL_DIR, finalSpinDir);
        bundle.put(SENSE_CD, windSenseCD);
        bundle.put(SENSE_WIND, windSenseWindup);
        bundle.put(SENSE_LAST, lastSensePos);
        bundle.put(SENSE_MODE, windSenseMode);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        protectorCD = bundle.getInt(PROT_CD);
        sandstormCD = bundle.getInt(SAND_CD);
        wireCD = bundle.getInt(WIRE_CD);
        finalModeCD = bundle.getInt(FINAL_CD);
        sandWindup = bundle.getInt(SAND_WIND);
        wireWindup = bundle.getInt(WIRE_WIND);
        finalActive = bundle.getBoolean(FINAL_ACTIVE);
        finalStage = bundle.getInt(FINAL_STAGE);
        finalWindup = bundle.getInt(FINAL_WINDUP);
        finalCells.clear();
        for (int c : bundle.getIntArray(FINAL_CELLS)) finalCells.add(c);
        finalAngle = bundle.getFloat(FINAL_ANGLE);
        finalSpinDir = bundle.getInt(FINAL_DIR);
        windSenseCD = bundle.getInt(SENSE_CD);
        windSenseWindup = bundle.getInt(SENSE_WIND);
        lastSensePos = bundle.getInt(SENSE_LAST);
        windSenseMode = bundle.getInt(SENSE_MODE);

        // restore sprite based on phase
        if (phase == 0) spriteClass = WammuChariotSprite.class;
        else spriteClass = WammuChariot2Sprite.class;
    }
}
