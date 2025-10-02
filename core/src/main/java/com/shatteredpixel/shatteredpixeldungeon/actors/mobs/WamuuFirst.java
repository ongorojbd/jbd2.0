/*
 * Wamuu (First Encounter) - Wind Mode boss without Final Mode
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Wedding;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SkyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EsidisiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KarsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Speedwagon2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WamuuSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WamuuFirst extends Mob {

    {
        spriteClass = WamuuSprite.class;
        HP = HT = 700;

        EXP = 30;
        defenseSkill = 15;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 32 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 12);
    }

    private int phase = 0;
    private int protectorCD = 10;  // 바람의 프로텍터
    private int sandstormCD = 999; // 2페이즈에서만 활성화
    private int wireCD = 10;       // 와이어 (1페이즈 전에도 사용)

    private int sandWindup = 0;
    private ArrayList<Integer> sandCells = new ArrayList<>();
    private int wireWindup = 0;
    private ArrayList<Integer> wireCells = new ArrayList<>(); // legacy combined list
    private boolean wireActive = false;
    private int wireStage = 0; // 0: small, 1: mid, 2: large
    private ArrayList<Integer> wireRing1 = new ArrayList<>();
    private ArrayList<Integer> wireRing2 = new ArrayList<>();
    private ArrayList<Integer> wireRing3 = new ArrayList<>();
    // legacy field removed (not used in ring-style wire)
    private boolean sandstormPending = false;

    private static final String PHASE = "phase";
    private static final String PROT_CD = "prot_cd";
    private static final String SAND_CD = "sand_cd";
    private static final String WIRE_CD = "wire_cd";
    private static final String SAND_WIND = "sand_wind";
    private static final String WIRE_WIND = "wire_wind";

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            if (Dungeon.hero.heroClass == HeroClass.WARRIOR || Dungeon.hero.heroClass == HeroClass.MAGE) {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new WamuuSprite(), new WamuuSprite(), new KarsSprite()},
                        new String[]{"와무우", "와무우", "???"},
                        new String[]{
                                Messages.get(WamuuFirst.class, "s1"),
                                Messages.get(WamuuFirst.class, "s2"),
                                Messages.get(WamuuFirst.class, "t8")
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                        }
                );
            }
            else {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new WamuuSprite(), new WamuuSprite(), new WamuuSprite(), new KarsSprite()},
                        new String[]{"와무우", "와무우", "와무우", "???"},
                        new String[]{
                                Messages.get(WamuuFirst.class, "t5"),
                                Messages.get(WamuuFirst.class, "t6"),
                                Messages.get(WamuuFirst.class, "t7"),
                                Messages.get(WamuuFirst.class, "t8")
                        },
                        new byte[]{
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
        if (dmg >= 200) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 200;
        }
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src);
        if (phase == 0 && HP < HT / 2) {
            phase = 1;

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new WamuuSprite(), new WamuuSprite(), new Speedwagon2Sprite(), new Speedwagon2Sprite(), new Speedwagon2Sprite()},
                    new String[]{"와무우", "와무우", "스피드왜건", "스피드왜건", "스피드왜건"},
                    new String[]{
                            Messages.get(WamuuFirst.class, "t9"),
                            Messages.get(WamuuFirst.class, "t10"),
                            Messages.get(WamuuFirst.class, "t11"),
                            Messages.get(WamuuFirst.class, "t12"),
                            Messages.get(WamuuFirst.class, "t12s"),
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );

            Music.INSTANCE.play(Assets.Music.TENDENCY3, true);

            // keep WamuuSprite in phase 2 as well (no sprite swap)

            // enable sandstorm in next phase segment
            sandstormCD = 3;
        }
    }

    @Override
    public void die( Object cause ) {

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new WamuuSprite(), new WamuuSprite(), new EsidisiSprite()},
                new String[]{"와무우", "와무우", "에시디시"},
                new String[]{
                        Messages.get(WamuuFirst.class, "t14"),
                        Messages.get(WamuuFirst.class, "t15"),
                        Messages.get(WamuuFirst.class, "t16")
                },
                new byte[]{
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE
                }
        );

        super.die( cause );

        Dungeon.level.drop(new Spw().identify(), pos).sprite.drop(pos);
        Buff.affect(Dungeon.hero, Wedding.class);

        sprite.killAndErase();

        Music.INSTANCE.end();

        GameScene.bossSlain();

        Dungeon.level.unseal();
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) BossHealthBar.assignBoss(this);

        if (protectorCD > 0) protectorCD--;
        if (sandstormCD > 0) sandstormCD--;
        if (wireCD > 0) wireCD--;

        // resolve windups first
        if (sandWindup > 0) {
            // re-show telegraph during both aiming turns
            if (!sandCells.isEmpty()) {
                for (int c : sandCells) {
                    sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
                }
            }
            sandWindup--;
            if (sandWindup == 0 && !sandCells.isEmpty()) {
                resolveSandstorm();
                return true;
            } else {
                // channeling: no attacks or movement while aiming
                spend(1f);
                return true;
            }
        }
        if (wireWindup > 0) {
            wireWindup--;
            if (wireWindup == 0 && wireActive) {
                resolveWireTick();
                // do not consume the turn; allow other actions/movement this act
                // avoid immediately starting new telegraphs after tick
                return super.act();
            }
        }

        if (enemy != null) {
            // 2페이즈: sandstorm available
            if (phase >= 1 && sandstormCD <= 0) {
                if (!wireActive) {
                    // don't re-telegraph while aiming
                    if (sandWindup == 0) {
                        if (telegraphSandstorm()) return true;
                    }
                } else {
                    sandstormPending = true;
                }
            }
            // 와이어: 모든 페이즈에서 사용 (요청사항)
            if (wireCD <= 0 && !wireActive) {
                if (telegraphWire()) return true;
            }
            // wind protector (defense/utility) - disabled during second phase
            if (phase == 0 && protectorCD <= 0 && !wireActive) {
                if (windProtector()) return true;
            }
        }

        return super.act();
    }

    // 바람의 프로텍터
    private boolean windProtector() {
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "protector"));
        Buff.affect(this, Invisibility.class, 3f);
        Buff.affect(this, Haste.class, 3f);
        for (int i : PathFinder.NEIGHBOURS9) {
            int p = pos + i;
            if (Dungeon.level.insideMap(p) && !Dungeon.level.solid[p]) {
                CellEmitter.center(p).burst(SkyParticle.FACTORY, 1);
            }
        }
        protectorCD = 10;
        spend(1f);
        return true;
    }

    // 신사폭풍 (예고 후 1턴)
    private boolean telegraphSandstorm() {
        if (enemy == null) return false;
        sandCells.clear();
        Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.WONT_STOP);
        ConeAOE cone = new ConeAOE(aim, Float.POSITIVE_INFINITY, 70, Ballistica.STOP_SOLID);
        for (int c : cone.cells) {
            if (!Dungeon.level.insideMap(c)) continue;
            sandCells.add(c);
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            // wind-up visuals on floor
            CellEmitter.center(c).burst(SkyParticle.FACTORY, 1);
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "sandstorm"));
        sandWindup = 2; // add 1 extra turn before resolving
        spend(1f);
        return true;
    }

    private void resolveSandstorm() {
        // 바닥 이펙트 강화 및 효과음 추가
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        Camera.main.shake(12, 0.8f);

        boolean terrainAffected = false;
        for (int c : sandCells) {
            // 더 강한 시각 효과: 스파크 + 작은 폭발
            CellEmitter.center(c).burst(SkyParticle.FACTORY, 6);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {

                if (ch == Dungeon.hero) {
                    ch.damage(Dungeon.hero.HT/2, this);
                    Buff.affect(ch, Bleeding.class).set(0.35f * Dungeon.hero.HT/4);
                }
                else {
                    int dmg = Random.NormalIntRange(16, 22);
                    ch.damage(dmg, this);
                    Buff.affect(ch, Bleeding.class).set(0.35f * dmg);
                }
                Buff.prolong( ch, Vulnerable.class, Vulnerable.DURATION );
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

        if (terrainAffected) {
            Dungeon.observe();
        }
        sandCells.clear();
        sandstormCD = 8;
        spend(1f);
    }

    // 와이어(회오리 바람) - 도넛 3단계(작은→중간→큰)로 3턴 연속 타격
    private boolean telegraphWire() {
        wireCells.clear(); wireRing1.clear(); wireRing2.clear(); wireRing3.clear();
        int w = Dungeon.level.width();
        int cx = pos % w, cy = pos / w;
        // build three rings with tight thickness for better readability
        buildRing(cx, cy, 1.0, 0.6, wireRing1);
        buildRing(cx, cy, 2.0, 0.6, wireRing2);
        buildRing(cx, cy, 3.0, 0.6, wireRing3);
        // show telegraph for first (small) ring only
        for (int c : wireRing1) {
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "wire_ready"));
        wireActive = true;
        wireStage = 0;
        wireWindup = 1; // resolve small ring next turn
        spend(1f);
        return true;
    }

    private void resolveWireTick() {
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        Dungeon.hero.interrupt();
        ArrayList<Integer> ring = (wireStage == 0 ? wireRing1 : wireStage == 1 ? wireRing2 : wireRing3);
        for (int c : ring) {
            CellEmitter.center( c ).burst(SkyParticle.FACTORY, 2);
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != alignment) {
                int bonus = (wireStage == 2 ? 2 : 0);

                if (ch == Dungeon.hero) {
                    int dmg = Random.NormalIntRange(12, 26) + bonus;
                    ch.damage(dmg, this);
                }

                int dmg = Random.NormalIntRange(12, 16) + bonus;
                ch.damage(dmg, this);

                Buff.prolong( ch, Vulnerable.class, Vulnerable.DURATION );

                if (Random.Int(2) == 0) Buff.affect(ch, Bleeding.class).set(0.25f * dmg);
                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        // next stage or finish
        if (wireStage < 2) {
            wireStage++;
            // show next ring telegraph
            ArrayList<Integer> nextRing = (wireStage == 1 ? wireRing2 : wireRing3);
            for (int c : nextRing) {
                sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            }
            wireWindup = 1;
        } else {
            wireActive = false;
            wireRing1.clear(); wireRing2.clear(); wireRing3.clear();
            wireCD = 9;
            // if sandstorm was pending, start it now
            if (sandstormPending && sandstormCD <= 0) {
                sandstormPending = false;
                telegraphSandstorm();
            }
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

    @Override
    public int defenseProc(Char enemy, int damage) {
        return super.defenseProc(enemy, damage);
    }

    @Override
    public float speed() {
        float s = super.speed();
        if (buff(Invisibility.class) != null) s *= 1.4f; // faster while wind protector active
        return s;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(PROT_CD, protectorCD);
        bundle.put(SAND_CD, sandstormCD);
        bundle.put(WIRE_CD, wireCD);
        bundle.put(SAND_WIND, sandWindup);
        bundle.put(WIRE_WIND, wireWindup);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        protectorCD = bundle.getInt(PROT_CD);
        sandstormCD = bundle.getInt(SAND_CD);
        wireCD = bundle.getInt(WIRE_CD);
        sandWindup = bundle.getInt(SAND_WIND);
        wireWindup = bundle.getInt(WIRE_WIND);

        // keep WamuuSprite after load regardless of phase
    }
}


