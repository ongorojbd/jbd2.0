/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.levels.DiobossLevel.itemPlace;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.IceBlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WoolParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Smask3;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NewSantantaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SantanaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SturoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TbossSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Santana extends Mob {

    {
        spriteClass = NewSantantaSprite.class;

        HP = HT = 350;
        EXP = 20;
        defenseSkill = 12;

        HUNTING = new Santana.Hunting();

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    private ArrayList<Integer> targetedCells = new ArrayList<>();

    public int Phase = 0;
    private int zcooldown = 2;
    private int ribBladeCooldown = 10; // 늑골 칼날 공격
    private int bodyControlCooldown = 0; // 신체 조작 능력

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            if (Dungeon.hero.heroClass == HeroClass.MAGE) {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new SturoSprite(), new TrapperSprite(), new NewSantantaSprite(), new SturoSprite()},
                        new String[]{"슈트로하임", "죠셉", "산타나", "슈트로하임"},
                        new String[]{
                                Messages.get(Santana.class, "t4"),
                                Messages.get(Santana.class, "t8"),
                                Messages.get(Santana.class, "t7"),
                                Messages.get(Santana.class, "t9")
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE
                        }
                );
            }
            else {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new NewSantantaSprite(), new SturoSprite(), new SturoSprite()},
                        new String[]{"산타나", "슈트로하임", "슈트로하임"},
                        new String[]{
                                Messages.get(Santana.class, "t3"),
                                Messages.get(Santana.class, "t4"),
                                Messages.get(Santana.class, "t42")
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

        if (dmg >= 100) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 100;
        }

        super.damage(dmg, src);

        if (Phase == 0 && HP < 200) {
            Phase = 1;

            // 2페이즈에서 패턴 강화
            ribBladeCooldown = 0;
        }

        if (Phase == 1 && HP < 150) {
            Phase = 2;
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new NewSantantaSprite(), new NewSantantaSprite()},
                    new String[]{"산타나", "산타나"},
                    new String[]{
                            Messages.get(Santana.class, "t5"),
                            Messages.get(Santana.class, "t6")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );

            zcooldown = 999;
            bodyControlCooldown = 0;
        }
    }

    private boolean UseAbility() {

        if (zcooldown <= 0) {
            sprite.showStatus(CharSprite.WARNING, "[미트 인베이드]");
            CellEmitter.center(this.pos).burst(BloodParticle.BURST, 12);
            summonRats(2);
            zcooldown = 35;
            return true;
        }

        if (ribBladeCooldown == 4) {
            GLog.w(Messages.get(this, "i"));
            Dungeon.hero.interrupt();
        }

        if (ribBladeCooldown == 1) {
            ribBladeAttack();
        }

        // 늑골 칼날 공격 - 십자/엑스자 패턴
        if (ribBladeCooldown <= 0) {
            ribBladeAttack();
            ribBladeCooldown = Phase == 0 ? 8 : 6;
            if (Phase == 2) ribBladeCooldown = 5;
            return true;
        }

        // 신체 조작 능력 (최종 페이즈에서만)
        if (Phase >= 2 && bodyControlCooldown <= 0) {
            bodyControlAbility();
            bodyControlCooldown = 8;
            return true;
        }

        return false;
    }

    // 늑골 칼날 공격 - 십자/엑스자 패턴
    private void ribBladeAttack() {
        Sample.INSTANCE.play(Assets.Sounds.TONIO, 1f, 0.8f);

        // 공격 패턴 결정 (쿨다운 값으로 구분)
        boolean isCrossAttack = (ribBladeCooldown == 1);
        
        if (isCrossAttack) {
            // 십자 공격
            this.rooted = true;
            Dungeon.hero.interrupt();
            sprite.showStatus(CharSprite.WARNING, "[립스 블레이드]");
            int[] crossDirections = {-Dungeon.level.width(), Dungeon.level.width(), -1, 1};
            for (int offset : crossDirections) {
                int targetPos = pos + offset;
                if (Dungeon.level.insideMap(targetPos)) {
                    // 목표 지점 표시와 동시에 공격
                    sprite.parent.addToBack(new TargetedCell(targetPos, 0xFF00FF));
                    CellEmitter.center(targetPos).burst(WoolParticle.FACTORY, 12);

                    Char target = Actor.findChar(targetPos);
                    if (target != null && target.alignment != alignment) {
                        int damage = Random.NormalIntRange(15, 20);
                        target.damage(damage, this);
                        if (!target.isAlive() && target == Dungeon.hero) {
                            Dungeon.fail(getClass());
                        }
                        Buff.affect(target, Bleeding.class).set(0.5f * damage);
                        target.sprite.flash();
                    }
                }
            }
        } else {
            this.rooted = false;

            // 엑스자 공격
            int[] xDirections = {
                -Dungeon.level.width() - 1, -Dungeon.level.width() + 1,
                Dungeon.level.width() - 1, Dungeon.level.width() + 1
            };
            for (int offset : xDirections) {
                int targetPos = pos + offset;
                if (Dungeon.level.insideMap(targetPos)) {
                    // 목표 지점 표시와 동시에 공격
                    sprite.parent.addToBack(new TargetedCell(targetPos, 0xFF00FF));
                    CellEmitter.center(targetPos).burst(WoolParticle.FACTORY, 12);
                    
                    Char target = Actor.findChar(targetPos);
                    if (target != null && target.alignment != alignment) {
                        int damage = Random.NormalIntRange(15, 20);
                        target.damage(damage, this);
                        if (!target.isAlive() && target == Dungeon.hero) {
                            Dungeon.fail(getClass());
                        }
                        Buff.affect(target, Bleeding.class).set(0.5f * damage);
                        target.sprite.flash();
                    }
                }
            }
        }
    }

    // 신체 조작 능력 - 빠른 이동과 회피
    private void bodyControlAbility() {
        sprite.showStatus(CharSprite.WARNING, "[신체 조작]");
        CellEmitter.center(this.pos).burst(PurpleParticle.BURST, 10);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

        // 빠른 이동으로 플레이어 주변으로 이동
        if (enemy != null) {
            ArrayList<Integer> positions = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8) {
                int p = enemy.pos + i;
                if (Dungeon.level.passable[p] && Actor.findChar(p) == null) {
                    positions.add(p);
                }
            }
            
            if (!positions.isEmpty()) {
                int targetPos = Random.element(positions);
                // 빠른 이동 효과
                sprite.jump(pos, targetPos, new Callback() {
                    @Override
                    public void call() {
                        pos = targetPos;
                        Dungeon.level.occupyCell(Santana.this);
                        sprite.idle();
                        next();
                    }
                });
                return;
            }
        }
        
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        
        // 기본 공격에도 출혈 효과 추가
        if (Random.Int(4) == 0) {
            Buff.affect(enemy, Bleeding.class).set(0.25f * damage);
        }
        
        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        damage = super.defenseProc(enemy, damage);

        return damage;
    }

    @Override
    public void move(int step, boolean travelling) {
        super.move(step, travelling);
    }

    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String PHASE = "Phase";
    private static final String TARGETED_CELLS = "targeted_cells";
    private static final String Z_CD = "zcooldown";
    private static final String RIB_BLADE_CD = "ribbladecooldown";
    private static final String BODY_CONTROL_CD = "bodycontrolcooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(PHASE, Phase);
        bundle.put(Z_CD, zcooldown);
        bundle.put(RIB_BLADE_CD, ribBladeCooldown);
        bundle.put(BODY_CONTROL_CD, bodyControlCooldown);

        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++) {
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        Phase = bundle.getInt(PHASE);
        zcooldown = bundle.getInt(Z_CD);
        ribBladeCooldown = bundle.getInt(RIB_BLADE_CD);
        bodyControlCooldown = bundle.getInt(BODY_CONTROL_CD);

        for (int i : bundle.getIntArray(TARGETED_CELLS)) {
            targetedCells.add(i);
        }
    }

    private int lastEnemyPos = -1;

    @Override
    protected boolean act() {

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        {
            if (UseAbility()) return true;
        }

        if (zcooldown > 0) zcooldown--;
        if (ribBladeCooldown > 0) ribBladeCooldown--;
        if (bodyControlCooldown > 0) bodyControlCooldown--;

        AiState lastState = state;
        boolean result = super.act();

        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = Dungeon.hero.pos;
            }
        }

        return result;
    }



    @Override
    public void die(Object cause) {

        super.die(cause);

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Niku || mob instanceof ZombieSoldier) {
                mob.die(cause);
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new SantanaSprite()},
                new String[]{"산타나"},
                new String[]{
                        Messages.get(Santana.class, "die")
                },
                new byte[]{
                        WndDialogueWithPic.DIE
                }
        );

        Dungeon.level.drop(new Spw().identify(), pos).sprite.drop(pos);
        Dungeon.level.drop( new TengusMask(), pos ).sprite.drop( pos );

        Badges.validateTendency();

        GameScene.bossSlain();

        Music.INSTANCE.end();

        Dungeon.level.unseal();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 6, 15 );
    }

    @Override
    public int attackSkill(Char target) {
        return 15;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 5);
    }


    public void summonRats(int amount) {

        while (amount > 0) {

            ArrayList<Integer> respawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = this.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add(p);
                }
                int index = Random.index(respawnPoints);
            }

            if (respawnPoints.size() > 0) {
                Mob mob;
                mob = new Niku();
                mob.pos = Random.element(respawnPoints);
                GameScene.add(mob, 1);
                mob.state = mob.HUNTING;
                Dungeon.level.occupyCell(mob);
            }
            amount--;
        }
    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy)) {

                return doAttack(enemy);

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination(Santana.this);
                    return true;
                }

                int oldPos = pos;
                if (target != -1 && getCloser(target)) {

                    spend(1 / speed());
                    return moveSprite(oldPos, pos);

                } else {
                    spend(TICK);
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination(Santana.this);
                    }
                    return true;
                }
            }
        }
    }
}
