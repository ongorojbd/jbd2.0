package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Dominion;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Triplespeed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.TuskBestiary2;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover2;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover4;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscH;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JojoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JotaroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RebelSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TankSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.HashSet;

public class Rebel extends Mob {
    private static final String[] LINE_KEYS = {"invincibility1", "invincibility2", "invincibility3", "invincibility4", "invincibility5"};

    {
        spriteClass = RebelSprite.class;

        HP = HT = 1500;
        defenseSkill = 25;
        viewDistance = 10;

        EXP = 0;
        maxLvl = 30;

        baseSpeed = 1.7f;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        immunities.add(Terror.class);
        immunities.add(Dread.class);
        immunities.add(Sleep.class);
        immunities.add(Amok.class);
        immunities.add(Blindness.class);
        immunities.add(MagicalSleep.class);
    }

    int cleanCooldown = 9999;
    int summonCooldown = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 9999 : 9999;
    private int charge = 0; // 2이 될경우 강화 사격
    private int LastPos = -1;
    private int Burstcooldown = 0; // 1이 되면 은신 파괴

    public int Phase = 0; // 1~6까지
    private int GasCoolDown = 6;
    private int ACoolDown = 9999;
    private int BurstTime = 0;
    private int BurstTimt = 0; // 화염 폭발 발동 시간. 2가 되면 발동함
    private int Burstpos = -1;
    private static final Rect arena = new Rect(0, 0, 33, 26);
    private static final int bottomDoor = 16 + (arena.bottom + 1) * 33;
    private float abilityCooldown;

    // 중력 역전
    private int gravityCooldown = 0;
    private boolean gravityCharging = false;

    // 무적 기믹
    private boolean invulnerable = false;
    private boolean invulnWarned = false;

    // 데미지 장벽 패턴
    private int barrierCooldown = 20;
    private boolean barrierActive = false;
    private int barrierCurrentRow = 0;
    private int barrierSafeColumn = -1;  // 안전 구역 칸 (매 장벽마다 랜덤 지정)
    private static final int BARRIER_DAMAGE = 35;
    private static final int ARENA_TOP = 1;      // 아레나 시작 행 (맨 위)
    private static final int ARENA_BOTTOM = 26;  // 아레나 끝 행

    // DistortionTrap 패턴
    private int distortionCooldown = 9999;
    private static boolean telling_1 = false;
    private static boolean telling_2 = false;
    private static boolean telling_3 = false;
    private static boolean telling_4 = false;
    private static boolean telling_5 = false;
    private static final String TELLING_1 = "telling_1";
    private static final String TELLING_2 = "telling_2";
    private static final String TELLING_3 = "telling_3";
    private static final String TELLING_4 = "telling_4";
    private static final String TELLING_5 = "telling_5";
    private static final String CLEAN_COOLDOWN = "cleancooldown";
    private static final String SKILL2TIME = "BurstTime";
    private static final String SKILL3TIME = "BurstTimt";
    private static final String SKILL2TPOS = "Burstpos";
    private static final String SKILL2TCD = "ACoolDown";
    private static final String SKILL3TCD = "GasCoolDown";
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private static final String BURST = "Burstcooldown";
    private static final String GRAVITY_COOLDOWN = "gravity_cooldown";
    private static final String GRAVITY_CHARGING = "gravity_charging";
    private static final String INVULNERABLE = "invulnerable";
    private static final String BARRIER_COOLDOWN = "barrier_cooldown";
    private static final String BARRIER_ACTIVE = "barrier_active";
    private static final String BARRIER_CURRENT_ROW = "barrier_current_row";
    private static final String BARRIER_SAFE_COLUMN = "barrier_safe_column";
    private static final String DISTORTION_COOLDOWN = "distortion_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TELLING_1, telling_1);
        bundle.put(TELLING_2, telling_2);
        bundle.put(TELLING_3, telling_3);
        bundle.put(TELLING_4, telling_4);
        bundle.put(TELLING_5, telling_5);
        bundle.put(CLEAN_COOLDOWN, cleanCooldown);
        bundle.put(PHASE, Phase);
        bundle.put(SKILLPOS, LastPos);
        bundle.put(SKILL2TIME, BurstTime);
        bundle.put(SKILL3TIME, BurstTimt);
        bundle.put(SKILL2TCD, ACoolDown);
        bundle.put(SKILL2TPOS, Burstpos);
        bundle.put(SKILL3TCD, GasCoolDown);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
        bundle.put(BURST, Burstcooldown);
        bundle.put(SKILLCD, charge);
        bundle.put(GRAVITY_COOLDOWN, gravityCooldown);
        bundle.put(GRAVITY_CHARGING, gravityCharging);
        bundle.put(INVULNERABLE, invulnerable);
        bundle.put(BARRIER_COOLDOWN, barrierCooldown);
        bundle.put(BARRIER_ACTIVE, barrierActive);
        bundle.put(BARRIER_CURRENT_ROW, barrierCurrentRow);
        bundle.put(BARRIER_SAFE_COLUMN, barrierSafeColumn);
        bundle.put(DISTORTION_COOLDOWN, distortionCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        telling_1 = bundle.getBoolean(TELLING_1);
        telling_2 = bundle.getBoolean(TELLING_2);
        telling_3 = bundle.getBoolean(TELLING_3);
        telling_4 = bundle.getBoolean(TELLING_4);
        telling_5 = bundle.getBoolean(TELLING_5);
        cleanCooldown = bundle.getInt(CLEAN_COOLDOWN);
        Phase = bundle.getInt(PHASE);
        LastPos = bundle.getInt(SKILLPOS);
        BurstTime = bundle.getInt(SKILL2TIME);
        BurstTimt = bundle.getInt(SKILL3TIME);
        ACoolDown = bundle.getInt(SKILL2TCD);
        Burstpos = bundle.getInt(SKILL2TPOS);
        GasCoolDown = bundle.getInt(SKILL3TCD);
        summonCooldown = bundle.getInt(SUMMON_COOLDOWN);
        Burstcooldown = bundle.getInt(BURST);
        charge = bundle.getInt(SKILLCD);
        gravityCooldown = bundle.getInt(GRAVITY_COOLDOWN);
        gravityCharging = bundle.getBoolean(GRAVITY_CHARGING);
        invulnerable = bundle.getBoolean(INVULNERABLE);
        barrierCooldown = bundle.getInt(BARRIER_COOLDOWN);
        barrierActive = bundle.getBoolean(BARRIER_ACTIVE);
        barrierCurrentRow = bundle.getInt(BARRIER_CURRENT_ROW);
        barrierSafeColumn = bundle.getInt(BARRIER_SAFE_COLUMN);
        distortionCooldown = bundle.getInt(DISTORTION_COOLDOWN);
    }

    @Override
    public int damageRoll() {
        int dmg;
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            dmg = Random.NormalIntRange(45, 65);
        } else {
            dmg = Random.NormalIntRange(35, 55);
        }
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 70;
    }

    public static class SummoningBlockDamage2 {
    }

    public static class SummoningBlockDamage3 {
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        if (charge >= 4) {
            enemy.sprite.showStatus(CharSprite.POSITIVE, "5타 추가 피해!", charge + 1);
            SpellSprite.show(this, SpellSprite.PURITY);
            Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
            CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            enemy.damage(20, new SummoningBlockDamage2());
            charge = 0;

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "d"));
            }

        } else {
            damage = super.attackProc(enemy, damage);
            enemy.sprite.showStatus(CharSprite.POSITIVE, "%d..", charge + 1);
            charge++;
        }
        damage = super.attackProc(enemy, damage);

        return damage;
    }

    @Override
    public int drRoll() {
        int dr;
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            dr = Random.NormalIntRange(10, 25);
        } else {
            dr = Random.NormalIntRange(5, 25);
        }
        return dr;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return true;
    }

    @Override
    public String description() {
        String desc = super.description();

        if (Dungeon.mboss4 == 1) {
            desc += "\n" + Messages.get(this, "p1");
        }

        if (Dungeon.mboss9 == 1) {
            desc += "\n" + Messages.get(this, "p2");
        }

        if (Dungeon.mboss14 == 1) {
            desc += "\n" + Messages.get(this, "p3");
        }

        if (Dungeon.mboss19 == 1) {
            desc += "\n" + Messages.get(this, "p4");
        }

        return desc;
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            switch (Dungeon.hero.heroClass) {
                case WARRIOR:
                case MAGE:
                case DUELIST:
                case HUNTRESS:
				case CLERIC:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new RebelSprite()},
                            new String[]{"천국 DIO"},
                            new String[]{
                                    Messages.get(Rebel.class, "n1")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case ROGUE:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new RebelSprite(), new TankSprite()},
                            new String[]{"천국 DIO", "죠타로"},
                            new String[]{
                                    Messages.get(Rebel.class, "n2"),
                                    Messages.get(Rebel.class, "n3")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
            }

            Sample.INSTANCE.play(Assets.Sounds.OH2);

            for (Char ch : Actor.chars()) {
                if (ch instanceof DriedRose.GhostHero) {
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }

            if (Dungeon.isChallenged(Challenges.EOH) && Dungeon.mboss14 == 1) {
                Buff.affect(Dungeon.hero, Doom.class);
            }

        }
    }

    @Override
    protected boolean act() {
        summonCooldown--;
        cleanCooldown--;
        if (gravityCooldown > 0) gravityCooldown--;

        // 중력 역전 차징 중이면 다음 턴에 발동
        if (gravityCharging) {
            activateGravityPull();
            gravityCharging = false;
            return true;
        }

        // 중력 역전: 페이즈 2 이상에서 사용 (장벽 시전 중에는 사용 안함)
        if (Phase >= 2 && gravityCooldown <= 0 && !barrierActive && enemy != null && Dungeon.level.distance(pos, enemy.pos) > 2) {
            chargeGravityPull();
            gravityCooldown = Random.NormalIntRange(12, 18);
            return true;
        }

        // 데미지 장벽 패턴: 페이즈 2 이상에서 사용
        if (barrierCooldown > 0) barrierCooldown--;

        // 장벽이 활성화되어 있으면 진행 (다른 행동도 가능)
        if (barrierActive) {
            progressBarrier();
            // return하지 않고 계속 진행하여 자유롭게 행동
        }

        // 장벽 시작 조건 (Phase 2 이상)
        if (Phase >= 2 && barrierCooldown <= 0 && !barrierActive) {
            startBarrier();
            barrierCooldown = Random.NormalIntRange(25, 35);
            return true;
        }

        // DistortionTrap 패턴: 페이즈 1 이상에서 사용
        if (distortionCooldown > 0) distortionCooldown--;
        if (Phase >= 1 && distortionCooldown <= 0 && enemy != null) {
            activateDistortionTrap();
            distortionCooldown = Random.NormalIntRange(12, 20);
            return true;
        }

        if (cleanCooldown <= 0) {
            if (BurstTimt == 0) {
                GLog.h(Messages.get(this, "fire_ready"));

                Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);

                Dungeon.hero.interrupt();
                spend(1f);
                BurstTimt++;
                return true;
            } else {
                Char Target = hero;

                if (Target.buff(PortableCover4.CoverBuff.class) == null) {
                    Camera.main.shake(24, 2f);
                    GameScene.flash(0xFF00FF);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST);
                    Sample.INSTANCE.play(Assets.Sounds.HAHAH);
                    hero.damage(hero.HP * 3 / 5, this);
                    CellEmitter.center(hero.pos).burst(BlastParticle.FACTORY, 20);
                    GLog.n(Messages.get(this, "o"));

                    if (enemy == Dungeon.hero && !enemy.isAlive()) {
                        Dungeon.fail(getClass());
                    }

                } else {
                    Camera.main.shake(16, 1.5f);
                    Buff.prolong(this, Paralysis.class, 1f);
                    GLog.p(Messages.get(this, "g"));
                    GLog.n(Messages.get(this, Random.element(LINE_KEYS)));

                    Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
                }


                BurstTimt = 0;

                //GLog.w(Messages.get(Rebel.class, "cleaning"));

                for (int i = 0; i < 1122; i++) {
                    if (Dungeon.level.map[i] == Terrain.BARRICADE
                            || Dungeon.level.map[i] == Terrain.HIGH_GRASS
                            || Dungeon.level.map[i] == Terrain.GRASS
                            || Dungeon.level.map[i] == Terrain.FURROWED_GRASS
                            || Dungeon.level.map[i] == Terrain.EMBERS
                            || Dungeon.level.map[i] == Terrain.WATER) {
                        Level.set(i, Terrain.EMPTY);
                        GameScene.updateMap(i);
                    }
                }
                cleanCooldown = (35);

                return true;
            }
        }

        if (!UseAbility()) {
            return true;
        }

        if (ACoolDown > 0) ACoolDown--;
        if (GasCoolDown > 0) GasCoolDown--;

        if (abilityCooldown > 0) abilityCooldown--;

        //extra fast abilities and summons at the final 100 HP

        if (Dungeon.hero.buff(Invisibility.class) != null) {
            if (Burstcooldown == 0) Burstcooldown++;
            else {
                Burstcooldown = 0;
                this.yell(Messages.get(this, "burst"));
                Buff.detach(Dungeon.hero, Invisibility.class);
            }
        } else Burstcooldown = 0;
        return super.act();
    }

    private HashSet<Mob> getSubjects() {
        HashSet<Mob> subjects = new HashSet<>();
        for (Mob m : Dungeon.level.mobs) {
            if (m.alignment == alignment && (m instanceof Soldier || m instanceof Medic || m instanceof Supression || m instanceof Researcher || m instanceof Tank)) {
                subjects.add(m);
            }
        }
        return subjects;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 150) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 150;
        }

        // 무적 상태면 피해 무시
        if (isInvulnerable(src.getClass())) {
            sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
            return;
        }

        BossHealthBar.assignBoss(this);
        super.damage(dmg, src);

        if (Phase == 0 && HP < 1200) {
            Phase = 1;
            GameScene.flash(0x8B00FF);
            new Fadeleaf().activate(this);
            new Fadeleaf().activate(hero);
            distortionCooldown = 6;
            Buff.prolong(this, Haste.class, 1_000_000);
            ACoolDown = 12;
            summonCooldown = 0;

            yell(Messages.get(this, "telling_1"));
            sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);
        } else if (Phase == 1 && HP < 900) {
            Phase = 2;
            GameScene.flash(0x8B00FF);
            Buff.prolong(this, Bless.class, Bless.DURATION * 1_000_000);

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new PucciSprite(), new RebelSprite()},
                    new String[]{"퍼니 발렌타인", "천국 DIO"},
                    new String[]{
                            Messages.get(Rebel.class, "n5"),
                            Messages.get(Rebel.class, "n6")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );

            Pucci Pucci = new Pucci();
            Pucci.state = Pucci.WANDERING;
            Pucci.pos = bottomDoor - 9 * 33;
            GameScene.add(Pucci);
            Pucci.beckon(Dungeon.hero.pos);

            yell(Messages.get(this, "telling_3"));
            sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);
        } else if (Phase == 2 && HP < 600) {
            Phase = 3;
            GameScene.flash(0x8B00FF);
            Buff.prolong(this, BlobImmunity.class, BlobImmunity.DURATION * 1_000_000);
            Buff.detach(this, Doom.class);
            Buff.affect(Dungeon.hero, MindVision.class, 3f);
            immunities.add(Doom.class);
            immunities.add(Grim.class);

            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new RebelSprite()},
                    new String[]{"천국 DIO"},
                    new String[]{
                            Messages.get(Rebel.class, "n4")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE
                    }
            );

            Sample.INSTANCE.play(Assets.Sounds.OH);

            WO WO = new WO();
            WO.state = WO.HUNTING;
            WO.pos = bottomDoor - 12 * 33;
            GameScene.add(WO);
            WO.beckon(Dungeon.hero.pos);

            // 무적 활성화!
            activateInvulnerability();

            sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);

            cleanCooldown = 10;

        } else if (Phase == 3 && HP < 300) {
            Phase = 4;
            GameScene.flash(0x8B00FF);
            Buff.prolong(this, Adrenaline.class, Adrenaline.DURATION * 1_000_000);
            yell(Messages.get(this, "telling_4"));
            immunities.add(Doom.class);
            immunities.add(Grim.class);

            if (hero.heroClass == HeroClass.CLERIC) {
                GameScene.flash(0xFFFFFF);
                Bestiary.setSeen(Jotaro.class);
				Jotaro jojo = new Jotaro();
				jojo.state = jojo.WANDERING;
				jojo.pos = hero.pos;
				GameScene.add(jojo);
				jojo.beckon(Dungeon.hero.pos);

                Sample.INSTANCE.play(Assets.Sounds.ORA);
                for (Char c : Actor.chars()) {
                    if (c instanceof Jotaro) {
                        ((Jotaro) c).jo();
                    }
                }

                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new JotaroSprite(), new JojoSprite(), new JotaroSprite(), new JojoSprite()},
                        new String[]{"죠타로", "죠린", "죠타로", "죠린"},
                        new String[]{
                                Messages.get(Rebel.class, "n9"),
                                Messages.get(Rebel.class, "n10"),
                                Messages.get(Rebel.class, "n11"),
                                Messages.get(Rebel.class, "n12"),
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );

            } else {
                jojo jojo = new jojo();
                jojo.state = jojo.WANDERING;
                jojo.pos = hero.pos;
                GameScene.add(jojo);
                jojo.beckon(Dungeon.hero.pos);

				WndDialogueWithPic.dialogue(
						new CharSprite[]{new JojoSprite()},
						new String[]{"죠린"},
						new String[]{
								Messages.get(Rebel.class, "n7")
						},
						new byte[]{
								WndDialogueWithPic.IDLE
						}
				);
            }

            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);
        } else if (Phase == 4 && HP < 150) {
            Phase = 5;
            Buff.prolong(this, MagicImmune.class, MagicImmune.DURATION * 1_000_000);
            Buff.affect(Dungeon.hero, Blindness.class, 30f);
            immunities.add(Doom.class);
            immunities.add(Grim.class);
            this.sprite.add(CharSprite.State.SHIELDED);

            GameScene.flash(0x8B00FF);
            yell(Messages.get(this, "telling_5"));

            sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);

            GLog.h(Messages.get(Rebel.class, "blood"));
            Music.INSTANCE.play(Assets.Music.DIOLOWHP, true);
        }

    }

    private static final String PHASE = "Phase";
    private static final String SKILLPOS = "LastPos";

    private static final String SKILLCD = "charge";

    @Override
    public void die(Object cause) {

        GameScene.bossSlain();

        super.die(cause);
        SPDSettings.addSpecialcoin(6);

        Dungeon.level.unseal();

        for (Mob m : getSubjects()) {
            m.die(null);
        }

        // Rebel 사망 시 모든 몬스터 제거
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob != this) {
                mob.die(cause);
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.BLAST, 2, Random.Float(0.33f, 0.66f));
        Camera.main.shake(31, 3f);

        Statistics.yorihimes++;
        Badges.validateYorihimes();

        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            Badges.validateBossChallengeCompleted();
        } else {
            Statistics.qualifiedForBossChallengeBadge = false;
        }

        if (Dungeon.hero.buff(Triplespeed.class) != null) {
            Badges.validateMih();
        } else {
            Statistics.qualifiedForBossChallengeBadge = false;
        }

        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && Dungeon.mboss4 == 1 && Dungeon.mboss9 == 1 && Dungeon.mboss14 == 1 && Dungeon.mboss19 == 1) {
            Badges.validateOVERHEAVEN();
        } else {
            Statistics.qualifiedForBossChallengeBadge = false;
        }

        PotionOfHealing.cure(hero);
        PotionOfHealing.heal(hero);
        Buff.detach(Dungeon.hero, Doom.class);

        LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
        if (beacon != null) {
            beacon.upgrade();
        }

        if (Random.Int(10) == 0) {
            GameScene.flash(0xFFFF00);
            Dungeon.level.drop(new BossdiscH().identify(), pos).sprite.drop(pos);
            new Flare(5, 32).color(0xFFFF00, true).show(hero.sprite, 2f);
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
            GLog.p(Messages.get(Kawasiri.class, "rare"));
        }

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new RebelSprite()},
                new String[]{"천국 DIO"},
                new String[]{
                        Messages.get(Rebel.class, "n8")
                },
                new byte[]{
                        WndDialogueWithPic.DIE
                }
        );

        Sample.INSTANCE.play(Assets.Sounds.NANI);

        for (Char c : Actor.chars()) {
            if (c instanceof jojo) {
                ((jojo) c).sayHeroKilled();
            }
        }

        for (Char c : Actor.chars()) {
            if (c instanceof Jotaro) {
                ((Jotaro) c).sayHeroKilled();
            }
        }

    }


    @Override
    public int defenseProc(Char enemy, int damage) {
        damage = super.defenseProc(enemy, damage);

        int newPos;

        LabsBossLevel level = (LabsBossLevel) Dungeon.level;
        if (Dungeon.level instanceof LabsBossLevel) {
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
                if (Random.Int(3) == 0) {
                    do {
                        newPos = level.randomCellPos();
                    } while (level.map[newPos] == Terrain.WALL || Actor.findChar(newPos) != null);

                    if (level.heroFOV[pos])
                        CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

                    sprite.move(pos, newPos);
                    move(newPos);

                    if (level.heroFOV[newPos])
                        CellEmitter.get(newPos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                    Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                    GameScene.flash(0x333333);
                    Sample.INSTANCE.play(Assets.Sounds.HAHAH);
                    Buff.affect(Dungeon.hero, Blindness.class, 3f);

                    if (Dungeon.isChallenged(Challenges.EOH) && Dungeon.mboss9 == 1) {
                        new Fadeleaf().activate(hero);
                    }
                }
            } else {
                if (Random.Int(5) == 0) {
                    do {
                        newPos = level.randomCellPos();
                    } while (level.map[newPos] == Terrain.WALL || Actor.findChar(newPos) != null);

                    if (level.heroFOV[pos])
                        CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

                    sprite.move(pos, newPos);
                    move(newPos);

                    if (level.heroFOV[newPos])
                        CellEmitter.get(newPos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                    Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                    Sample.INSTANCE.play(Assets.Sounds.HAHAH);

                    if (Dungeon.isChallenged(Challenges.EOH) && Dungeon.mboss9 == 1) {
                        new Fadeleaf().activate(hero);
                    }
                }
            }
        }
        return damage;
    }

    private boolean UseAbility() {
        // 폭발 > 국가 순
        if (enemy == null) return true;
        // 폭발 (진실 조작): Phase 1 이상에서만 사용
        if (Phase >= 1 && ACoolDown <= 0) {
            if (Burstpos == -1) {
                // 위치 미지정시, 이번 턴에는 폭발을 일으킬 지점을 정합니다.
                Burstpos = Dungeon.hero.pos;
                sprite.parent.addToBack(new TargetedCell(Burstpos, 0xFF00FF));
                GLog.w(Messages.get(Rebel.class, "skill1"));

                for (int i : PathFinder.NEIGHBOURS9) {
                    int vol = Fire.volumeAt(Burstpos + i, Fire.class);
                    if (vol < 4) {
                        sprite.parent.addToBack(new TargetedCell(Burstpos + i, 0xFF00FF));
                    }
                }

                sprite.centerEmitter().start(Speck.factory(Speck.MASK), 0.05f, 20);
                spend(2f);

                switch (Random.Int(2)) {
                    case 0:
                        Sample.INSTANCE.play(Assets.Sounds.OH);
                        break;
                    case 1:
                        Sample.INSTANCE.play(Assets.Sounds.OH2);
                        break;
                }

                BurstTime++;

                return false;
            } else if (BurstTime == 1) {

                PathFinder.buildDistanceMap(Burstpos, BArray.not(Dungeon.level.solid, null), 1);
                for (int cell = 0; cell < PathFinder.distance.length; cell++) {
                    if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                        CellEmitter.get(cell).burst(SparkParticle.FACTORY, 31);
                        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
                        Char ch = Actor.findChar(cell);

                        Char Target = hero;
                        if (Target.buff(PortableCover2.CoverBuff.class) == null) {
                            // ensure we test hit against the actual character in the cell, if present
                            if (ch != null && !(ch instanceof Rebel)) {
                                if (hit(this, ch, true)) {
                                    ch.damage(Random.NormalIntRange(65, 70), new SummoningBlockDamage3());
                                } else {
                                    ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
                                }
                            }
                        } else {
                            damage(5, this);
                            Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
                        }
                    }
                }

                Camera.main.shake(9, 0.5f);
                Burstpos = -1;
                BurstTime = 0;
                ACoolDown = Random.NormalIntRange(5, 8);

                if (enemy == Dungeon.hero && !enemy.isAlive()) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "d"));
                }

                if (Phase == 5) ACoolDown = Random.NormalIntRange(1, 4);

                Sample.INSTANCE.play(Assets.Sounds.BLAST, 1.5f, 0.67f);

                return true;
            }
        }

        if (GasCoolDown <= 0) {
            ThorwGas(enemy);
            return true;
        }

        return true;
    }

    public void ThorwGas(Char target) {
        Dungeon.hero.interrupt();
        GameScene.add(Blob.seed(target.pos, 250, Dominion.class));
        GLog.w(Messages.get(Rebel.class, "skill2"));
        GasCoolDown = 10;
    }

    // ==================== DistortionTrap 패턴 ====================

    private void activateDistortionTrap() {
        GLog.n(Messages.get(this, "distortion_start"));
        yell(Messages.get(this, "distortion_yell"));

        GameScene.flash(0x660066);

        // 트랩 생성 및 발동
        new DistortionTrap().set(hero.pos).activate();

        Dungeon.hero.interrupt();
        spend(TICK);
    }

    // ==================== 중력 역전 ====================

    private void chargeGravityPull() {
        gravityCharging = true;
        GLog.n(Messages.get(this, "gravity_charge"));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.centerEmitter().start(Speck.factory(Speck.MASK), 0.05f, 20);

        // 끌어당김 범위 표시
        for (int i : PathFinder.NEIGHBOURS8) {
            if (pos + i >= 0 && pos + i < Dungeon.level.length()) {
                sprite.parent.addToBack(new TargetedCell(pos + i, 0x00FFFF));
            }
        }

        Dungeon.hero.interrupt();
        spend(TICK);
    }

    private void activateGravityPull() {
        yell(Messages.get(this, "gravity_pull"));
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        Camera.main.shake(8, 1f);
        GameScene.flash(0x8B00FF);

        // 영웅을 보스 쪽으로 강하게 끌어당김
        if (hero.buff(Roots.class) == null && !hero.rooted) {
            Ballistica trajectory = new Ballistica(hero.pos, pos, Ballistica.STOP_TARGET);

            // 최대 6칸까지 끌어당김 (매우 강력)
            int pullDist = Math.min(6, trajectory.dist);
            int newPos = trajectory.path.get(pullDist);

            // 이동 가능한 위치 찾기
            while (pullDist > 0 && (Dungeon.level.solid[newPos] || Actor.findChar(newPos) != null)) {
                pullDist--;
                newPos = trajectory.path.get(pullDist);
            }

            if (pullDist > 0 && newPos != hero.pos) {
                int finalNewPos = newPos;
                Actor.add(new Pushing(hero, hero.pos, finalNewPos, new com.watabou.utils.Callback() {
                    @Override
                    public void call() {
                        hero.pos = finalNewPos;
                        Dungeon.level.occupyCell(hero);
                        Dungeon.observe();
                        GameScene.updateFog();
                    }
                }));

                // 끌려온 후 강력한 디버프
                Buff.affect(hero, Cripple.class, 5f);
                Buff.affect(hero, Vertigo.class, 4f);
                Buff.affect(hero, Paralysis.class, 1f);
                hero.damage(Random.NormalIntRange(25, 40), this);

                GLog.n(Messages.get(this, "gravity_hit"));
            }
        } else {
            GLog.p(Messages.get(this, "gravity_resist"));
        }

        spend(TICK);
    }

    // ==================== 무적 기믹 ====================

    @Override
    public boolean isInvulnerable(Class effect) {
        if (invulnerable && !invulnWarned) {
            invulnWarned = true;
            GLog.w(Messages.get(this, "invuln_hint"));
        }
        return invulnerable || super.isInvulnerable(effect);
    }

    private void activateInvulnerability() {
        invulnerable = true;
        invulnWarned = false;
        this.sprite.add(CharSprite.State.SHIELDED);
        GLog.n(Messages.get(this, "invuln_start"));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        new Flare(8, 48).color(0x8B00FF, true).show(this.sprite, 3f);
    }

    public void loseInvulnerability() {
        if (invulnerable) {
            invulnerable = false;
            this.sprite.remove(CharSprite.State.SHIELDED);
            GLog.p(Messages.get(this, "invuln_end"));
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Camera.main.shake(5, 1f);
        }
    }

    // 무적 해제 조건 체크 (WO 처치 시 호출)
    public void checkInvulnerabilityCondition() {
        if (!invulnerable) return;

        // WO가 살아있는지 확인
        boolean woAlive = false;
        for (Mob m : Dungeon.level.mobs) {
            if (m instanceof WO) {
                woAlive = true;
                break;
            }
        }

        // WO가 죽으면 무적 해제
        if (!woAlive) {
            loseInvulnerability();
        }
    }

    // ==================== 데미지 장벽 패턴 ====================

    public static class BarrierDamage {}

    // 아레나 실제 빈 공간 범위 (LabsBossLevel 맵 기준: x 12~20이 통로)
    private static final int ARENA_LEFT = 13;   // 빈 공간 시작 (벽 제외)
    private static final int ARENA_RIGHT = 19;  // 빈 공간 끝 (벽 제외)

    private void startBarrier() {
        barrierActive = true;
        barrierCurrentRow = ARENA_TOP;

        // 안전 구역 랜덤 지정 (가로축에서 3칸은 항상 비어있음)
        // 아레나 실제 빈 공간 내에서만 선택 (x: 13~19)
        // 3칸 안전구역의 중앙이므로 양쪽 1칸씩 여유 필요: 14~18
        barrierSafeColumn = Random.Int(ARENA_LEFT + 1, ARENA_RIGHT);  // 14~18 사이

        GLog.n(Messages.get(this, "barrier_start"));
        yell(Messages.get(this, "barrier_yell"));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        Camera.main.shake(3, 0.5f);

        // 첫 줄 예고 표시 (4줄 앞까지 미리 보여줌 - 2칸씩 이동하므로)
        for (int i = 0; i < 4 && barrierCurrentRow + i < ARENA_BOTTOM; i++) {
            showBarrierWarning(barrierCurrentRow + i);
        }

        Dungeon.hero.interrupt();
        spend(TICK);
    }

    // 안전 구역인지 확인 (3칸: barrierSafeColumn-1, barrierSafeColumn, barrierSafeColumn+1)
    private boolean isSafeColumn(int x) {
        return x >= barrierSafeColumn - 1 && x <= barrierSafeColumn + 1;
    }

    private void progressBarrier() {
        // 현재 2줄에 피해 적용 (y축 2칸 범위)
        applyBarrierDamage(barrierCurrentRow);
        if (barrierCurrentRow + 1 < ARENA_BOTTOM) {
            applyBarrierDamage(barrierCurrentRow + 1);
        }

        // 2칸씩 이동
        barrierCurrentRow += 2;

        // 장벽이 아레나 끝에 도달하면 종료
        if (barrierCurrentRow >= ARENA_BOTTOM) {
            barrierActive = false;
            barrierCurrentRow = 0;
            GLog.p(Messages.get(this, "barrier_end"));
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            spend(TICK);
            return;
        }

        // 다음 예고 표시 (4줄 앞까지, 2칸씩 이동하므로)
        if (barrierCurrentRow + 2 < ARENA_BOTTOM) {
            showBarrierWarning(barrierCurrentRow + 2);
            showBarrierWarning(barrierCurrentRow + 3);
        }

        // 현재 장벽 위치 이펙트 (2줄)
        showBarrierEffect(barrierCurrentRow);
        if (barrierCurrentRow + 1 < ARENA_BOTTOM) {
            showBarrierEffect(barrierCurrentRow + 1);
        }

        spend(0.5f); // 빠르게 내려옴
    }

    private void showBarrierWarning(int row) {
        int levelWidth = Dungeon.level.width();

        // 안전 구역이 유효하지 않으면 재설정 (아레나 실제 빈 공간 내에서)
        if (barrierSafeColumn < ARENA_LEFT + 1 || barrierSafeColumn > ARENA_RIGHT - 1) {
            barrierSafeColumn = Random.Int(ARENA_LEFT + 1, ARENA_RIGHT);  // 14~18 사이
        }

        // 아레나 전체 너비 (x: 1~31)
        for (int x = 1; x < levelWidth - 1; x++) {
            int cell = row * levelWidth + x;
            if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
                if (isSafeColumn(x)) {
                    // 안전 구역 표시 - 3칸
                    sprite.parent.addToBack(new TargetedCell(cell, 0x00FFFF));
                } else {
                    // 위험 구역 표시
                    sprite.parent.addToBack(new TargetedCell(cell, 0xFF00FF));
                }
            }
        }
    }

    private void showBarrierEffect(int row) {
        int levelWidth = Dungeon.level.width();
        // 아레나 전체 너비 (x: 1~31), 안전 구역 제외
        for (int x = 1; x < levelWidth - 1; x++) {
            if (isSafeColumn(x)) continue;  // 안전 구역(3칸)은 이펙트 없음

            int cell = row * levelWidth + x;
            if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
                CellEmitter.get(cell).burst(SparkParticle.FACTORY, 8);
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
    }

    private void applyBarrierDamage(int row) {
        int levelWidth = Dungeon.level.width();

        // 아레나 전체 너비 (x: 1~31), 안전 구역 제외
        for (int x = 1; x < levelWidth - 1; x++) {
            // 안전 구역(3칸)은 피해 없음
            if (isSafeColumn(x)) continue;

            int cell = row * levelWidth + x;
            if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
                Char ch = Actor.findChar(cell);
                if (ch != null && ch != this && ch.alignment != alignment) {
                    // 피해 적용
                    ch.damage(BARRIER_DAMAGE, new BarrierDamage());
                    Buff.affect(ch, Cripple.class, 2f);

                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 10);

                    if (ch == Dungeon.hero) {
                        GLog.n(Messages.get(this, "barrier_hit"));
                        Camera.main.shake(5, 0.5f);

                        if (!ch.isAlive()) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(this, "barrier_kill"));
                        }
                    }
                }
            }
        }
    }

}
