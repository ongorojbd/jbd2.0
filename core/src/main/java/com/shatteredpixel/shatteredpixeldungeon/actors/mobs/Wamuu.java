/*
 * Wamuu (Full Duel) - 3-phase boss: Chariot → Wind-Sense → Final Mode (혼설삽)
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SkyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask3;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.ArenaBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KarsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuChariot2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuChariotSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuThirdSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Wedding2;
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
        HP = HT = 1600;
        defenseSkill = 30;
        EXP = 40;

        properties.add(Property.BOSS);
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 35, 50 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 20);
    }

    private int phase = 0;            // 0: Chariot, 1: Wind-Sense, 2: Final Mode

    // shared/phase-specific cooldowns
    private int protectorCD = 10;     // 바람의 프로텍터 (utility)
    private int sandstormCD = 8;      // phase 1 only
    private int wireCD = 9;           // all phases
    private int finalModeCD = 18;     // phase 3 opener
    private int windSenseCD = 6;      // phase 2 only: blind wind-sense sweep
    private int windPressureCD = 5;   // phase 3 only: tracking wind pressure

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

    // phase 3: final mode
    private boolean finalEnraged = false;  // becomes true when HP < 15%

    // phase 3: expanding square telegraph based on player's position
    private boolean squareExpanding = false;
    private int squareCenter = -1;
    private int squareRadius = -1; // chebyshev radius: 0=1x1, 1=3x3, ...
    private int squareMaxRadius = 0;
    private int squareTelegraphDelay = 0; // 1 turn per expansion step
    private ArrayList<Integer> squareCells = new ArrayList<>();

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
    private static final String FINAL_ENRAGED = "final_enraged";
    private static final String SENSE_CD = "sense_cd";
    private static final String SENSE_WIND = "sense_wind";
    private static final String SENSE_LAST = "sense_last";
    private static final String SENSE_MODE = "sense_mode";

    private static final String SQ_ACTIVE = "sq_active";
    private static final String SQ_CENTER = "sq_center";
    private static final String SQ_RADIUS = "sq_radius";
    private static final String SQ_MAX = "sq_max";
    private static final String SQ_DELAY = "sq_delay";
    private static final String SQ_CELLS = "sq_cells";

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
		// In final phase, Wamuu only takes one-third damage
		if (phase >= 2) {
			int reduced = Math.round(dmg / 3f);
			// ensure at least 1 damage when non-zero
			dmg = Math.max(1, reduced);
		}
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

            // switch to final phase sprite
            if (sprite != null) sprite.killAndErase();
            spriteClass = WammuThirdSprite.class;
            GameScene.addSprite(this);

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new WammuChariot2Sprite(), new WammuChariot2Sprite(), new WammuChariot2Sprite(), new KarsSprite(), new WammuThirdSprite()},
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
            finalModeCD = 0;
            // reset expanding-square state
            squareExpanding = false;
            squareCells.clear();
            squareCenter = -1;
            squareRadius = -1;
            squareTelegraphDelay = 0;
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
        if (windPressureCD > 0) windPressureCD--;
        if (chariotCD > 0) chariotCD--;

        // Phase 3: No more teleportation - Wamuu stands his ground and unleashes relentless wind pressure
        
        // Check for enraged state (< 15% HP)
        if (phase >= 2 && !finalEnraged && HP < HT * 15 / 100) {
            finalEnraged = true;
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
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
        // Phase 3: Expanding square telegraph 1x1 -> 3x3 -> ... -> full map, then big damage
        if (phase >= 2) {
            if (!squareExpanding && finalModeCD <= 0) {
                startSquareExpansion();
                return true;
            }
            if (squareExpanding) {

                if (squareTelegraphDelay > 0) {
                    squareTelegraphDelay--;
                    if (squareTelegraphDelay == 0) {
                        if (squareRadius >= squareMaxRadius) {
                            resolveSquareBigDamage();
                            return true;
                        } else {
                            expandSquareOnce();
                            return true;
                        }
                    } else {
                        float speedMultiplier = finalEnraged ? 0.7f : 1f;
                        spend(TICK * speedMultiplier);
                        return true;
                    }
                }
            }
            float speedMultiplier = finalEnraged ? 0.7f : 1f;
            spend(TICK * speedMultiplier);
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
                int dmg = Random.NormalIntRange(35, 50);
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        windSenseCells.clear(); windEchoCells.clear();
        windSenseCD = 6;
        spend(1f);
    }

    // Phase 3: Expanding square helpers
    private void startSquareExpansion() {
        squareExpanding = true;
        if (Dungeon.level instanceof ArenaBossLevel) {
            // Arena center as requested: 15 + WIDTH * 15 (WIDTH=31)
            squareCenter = 15 + 31 * 15;
        } else {
            // fallback: current position of player if available
            squareCenter = (enemy != null) ? enemy.pos : pos;
        }
        squareRadius = 0;
        squareCells.clear();
        computeSquareMaxRadius();
        buildSquareCells(squareCenter, squareRadius, squareCells);
        showSquareTelegraph(true);
        squareTelegraphDelay = 1; // 1 turn per step
        float speedMultiplier = finalEnraged ? 0.7f : 1f;
        spend(TICK * speedMultiplier);
    }

    private void expandSquareOnce() {
        squareRadius = Math.min(squareRadius + 1, squareMaxRadius);
        squareCells.clear();
        buildSquareCells(squareCenter, squareRadius, squareCells);
        showSquareTelegraph(false);
        squareTelegraphDelay = 1;
        float speedMultiplier = finalEnraged ? 0.7f : 1f;
        spend(TICK * speedMultiplier);
    }

    private void resolveSquareBigDamage() {
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        int base = finalEnraged ? Random.NormalIntRange(26, 36) : Random.NormalIntRange(18, 28);
        for (int c : squareCells) {
            CellEmitter.center(c).burst(SkyParticle.FACTORY, finalEnraged ? 9 : 6);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int dmg;
                if (ch == Dungeon.hero) {
                    dmg = Math.round(Dungeon.hero.HT * 0.9f);
                } else {
                    dmg = base;
                }
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        // Wamuu also takes fixed self-damage on the explosion
        this.damage(10, this);
        // restart immediately
        squareExpanding = false;
        // brief minimal cooldown to allow sprite/UI update, effectively spams
        finalModeCD = 1;
    }

    private void computeSquareMaxRadius() {
        int w = Dungeon.level.width();
        int cx = squareCenter % w;
        int cy = squareCenter / w;
        int maxDx = Math.max(cx, w - 1 - cx);
        int h = Dungeon.level.length() / w;
        int maxDy = Math.max(cy, h - 1 - cy);
        squareMaxRadius = Math.max(maxDx, maxDy);
        // cap to around 12 expansions if suggested value is smaller than map edge distance
        squareMaxRadius = Math.min(squareMaxRadius, 11);
    }

    private void buildSquareCells(int center, int radius, ArrayList<Integer> out) {
        out.clear();
        int w = Dungeon.level.width();
        int cx = center % w;
        int cy = center / w;
        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                int x = cx + dx;
                int y = cy + dy;
                if (x < 0 || y < 0 || x >= w) continue;
                int c = x + y * w;
                if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
                out.add(c);
            }
        }
    }

    private void showSquareTelegraph(boolean first) {
        int color = 0xFF00FF;
        for (int c : squareCells) sprite.parent.addToBack(new TargetedCell(c, color));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
         if (first) {
            sprite.showStatus(CharSprite.WARNING,"[파이널 모드, 혼설삽!]");
        }
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
                int dmg = Random.NormalIntRange(35, 50);
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
                if (ch == Dungeon.hero) {
                    ch.damage(Dungeon.hero.HT/2, this);
                    Buff.affect(ch, Bleeding.class).set(0.35f * Dungeon.hero.HT/4);
                } else {
                    int dmg = Random.NormalIntRange(16, 22);
                    ch.damage(dmg, this);
                    Buff.affect(ch, Bleeding.class).set(0.35f * dmg);
                }
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
                int dmg = Random.NormalIntRange(35, 50);
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

    // (Removed unused buildFilled helper; not needed for 5x5 pattern)

    // (Removed expanding AoE helpers; replaced by continuous 5x5 pattern)

    // (Removed old wide path-based final-phase telegraph)
    
    // (Removed legacy pending-damage resolver)

    // removed ensureBase (no longer used after flat damage changes)


    @Override
    public void die(Object cause) {
        // On defeating Wamuu within time, safely remove the timed death buff from the hero
        Buff.detach(Dungeon.hero, Wedding2.class);

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new WammuThirdSprite(), new WammuThirdSprite(), new WammuThirdSprite()},
                new String[]{"와무우", "와무우", "와무우"},
                new String[]{
                        Messages.get(Wamuu.class, "t11"),
                        Messages.get(Wamuu.class, "t12"),
                        Messages.get(Wamuu.class, "t13")
                },
                new byte[]{
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE
                }
        );

        Dungeon.level.drop(new Smask3(), pos).sprite.drop(pos);

        super.die( cause );

        Music.INSTANCE.end();

        GameScene.bossSlain();
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
        bundle.put(FINAL_ENRAGED, finalEnraged);
        bundle.put(SENSE_CD, windSenseCD);
        bundle.put(SENSE_WIND, windSenseWindup);
        bundle.put(SENSE_LAST, lastSensePos);
        bundle.put(SENSE_MODE, windSenseMode);

        // Expanding square state
        bundle.put(SQ_ACTIVE, squareExpanding);
        bundle.put(SQ_CENTER, squareCenter);
        bundle.put(SQ_RADIUS, squareRadius);
        bundle.put(SQ_MAX, squareMaxRadius);
        bundle.put(SQ_DELAY, squareTelegraphDelay);
        int[] sc = new int[squareCells.size()];
        for (int i = 0; i < squareCells.size(); i++) sc[i] = squareCells.get(i);
        bundle.put(SQ_CELLS, sc);
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
        finalEnraged = bundle.getBoolean(FINAL_ENRAGED);
        windSenseCD = bundle.getInt(SENSE_CD);
        windSenseWindup = bundle.getInt(SENSE_WIND);
        lastSensePos = bundle.getInt(SENSE_LAST);
        windSenseMode = bundle.getInt(SENSE_MODE);

        // Expanding square state
        squareExpanding = bundle.getBoolean(SQ_ACTIVE);
        squareCenter = bundle.getInt(SQ_CENTER);
        squareRadius = bundle.getInt(SQ_RADIUS);
        squareMaxRadius = bundle.getInt(SQ_MAX);
        squareTelegraphDelay = bundle.getInt(SQ_DELAY);
        squareCells.clear();
        for (int c : bundle.getIntArray(SQ_CELLS)) squareCells.add(c);

        // restore sprite based on phase
        if (phase == 0) spriteClass = WammuChariotSprite.class;
        else if (phase == 1) spriteClass = WammuChariot2Sprite.class;
        else spriteClass = WammuThirdSprite.class;
    }
}
