/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.NitoDismantleHammer2;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Castleintro;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscF;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DioDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HighdioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JojoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LarvaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ResearcherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SoldierSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SupressionSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TankSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class YogDzewa extends Mob {

    {

        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            spriteClass = HighdioSprite.class;
        } else {
            spriteClass = YogSprite.class;
        }

        HP = HT = 1000;

        EXP = 50;

        //so that allies can attack it. States are never actually used.
        state = HUNTING;

        viewDistance = 12;

        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.DEMONIC);
        properties.add(Property.STATIC);
    }

    private int phase = 0;

    private float abilityCooldown;
    private static final int MIN_ABILITY_CD = 10;
    private static final int MAX_ABILITY_CD = 15;

    private float summonCooldown;
    private static final int MIN_SUMMON_CD = 10;
    private static final int MAX_SUMMON_CD = 15;

    private static Class getPairedFist(Class fist) {
        if (fist == YogFist.BurningFist.class) return YogFist.SoiledFist.class;
        if (fist == YogFist.SoiledFist.class) return YogFist.BurningFist.class;
        if (fist == YogFist.RottingFist.class) return YogFist.RustedFist.class;
        if (fist == YogFist.RustedFist.class) return YogFist.RottingFist.class;
        if (fist == YogFist.BrightFist.class) return YogFist.DarkFist.class;
        if (fist == YogFist.DarkFist.class) return YogFist.BrightFist.class;
        return null;
    }

    private ArrayList<Class> fistSummons = new ArrayList<>();
    private ArrayList<Class> challengeSummons = new ArrayList<>();

    {
        //offset seed slightly to avoid output patterns
        Random.pushGenerator(Dungeon.seedCurDepth() + 1);
        fistSummons.add(Random.Int(2) == 0 ? YogFist.BurningFist.class : YogFist.SoiledFist.class);
        fistSummons.add(Random.Int(2) == 0 ? YogFist.RottingFist.class : YogFist.RustedFist.class);
        fistSummons.add(Random.Int(2) == 0 ? YogFist.BrightFist.class : YogFist.DarkFist.class);
        Random.shuffle(fistSummons);
        //randomly place challenge summons so that two fists of a pair can never spawn together
        if (Random.Int(2) == 0) {
            challengeSummons.add(getPairedFist(fistSummons.get(1)));
            challengeSummons.add(getPairedFist(fistSummons.get(2)));
            challengeSummons.add(getPairedFist(fistSummons.get(0)));
        } else {
            challengeSummons.add(getPairedFist(fistSummons.get(2)));
            challengeSummons.add(getPairedFist(fistSummons.get(0)));
            challengeSummons.add(getPairedFist(fistSummons.get(1)));
        }
        Random.popGenerator();
    }

    private ArrayList<Class> regularSummons = new ArrayList<>();

    {
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            for (int i = 0; i < 6; i++) {
                if (i >= 4) {
                    regularSummons.add(YogRipper.class);
                } else if (i >= Statistics.spawnersAlive) {
                    regularSummons.add(Larva.class);
                } else {
                    regularSummons.add(i % 2 == 0 ? YogEye.class : YogScorpio.class);
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (i >= Statistics.spawnersAlive) {
                    regularSummons.add(Larva.class);
                } else {
                    regularSummons.add(YogRipper.class);
                }
            }
        }
        Random.shuffle(regularSummons);
    }

    private ArrayList<Integer> targetedCells = new ArrayList<>();

    @Override
    public int attackSkill(Char target) {
        return INFINITE_ACCURACY;
    }

    @Override
    protected boolean act() {
        //char logic
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
            fieldOfView = new boolean[Dungeon.level.length()];
        }
        Dungeon.level.updateFieldOfView(this, fieldOfView);

        throwItems();

        sprite.hideAlert();
        sprite.hideLost();

        //mob logic
        enemy = chooseEnemy();

        enemySeen = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
        //end of char/mob logic

        if (phase == 0) {
            if (Dungeon.hero.viewDistance >= Dungeon.level.distance(pos, Dungeon.hero.pos)) {
                Dungeon.observe();
            }
            if (Dungeon.level.heroFOV[pos]) {
                notice();
            }
        }

        if (phase == 0) {
            spend(TICK);
            return true;
        } else {

            boolean terrainAffected = false;
            HashSet<Char> affected = new HashSet<>();
            //delay fire on a rooted hero
            if (!Dungeon.hero.rooted) {
                for (int i : targetedCells) {
                    Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                    //shoot beams
                    sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                    for (int p : b.path) {
                        Char ch = Actor.findChar(p);
                        if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
                            affected.add(ch);
                        }
                        if (Dungeon.level.flamable[p]) {
                            Dungeon.level.destroy(p);
                            GameScene.updateMap(p);
                            terrainAffected = true;
                        }
                    }
                }
                if (terrainAffected) {
                    Dungeon.observe();
                }
                Invisibility.dispel(this);
                for (Char ch : affected) {

                    if (ch == Dungeon.hero) {
                        Statistics.bossScores[4] -= 500;
                    }

                    if (hit(this, ch, true)) {
                        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
                            ch.damage(Random.NormalIntRange(30, 50), new Eye.DeathGaze());
                        } else {
                            ch.damage(Random.NormalIntRange(20, 30), new Eye.DeathGaze());
                        }
                        if (Dungeon.level.heroFOV[pos]) {
                            ch.sprite.flash();
                            CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                        }
                        if (!ch.isAlive() && ch == Dungeon.hero) {
                            Badges.validateDeathFromEnemyMagic();
                            Dungeon.fail(this);
                            GLog.n(Messages.get(Char.class, "kill", name()));
                        }
                    } else {
                        ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
                    }
                }
                targetedCells.clear();
            }

            if (abilityCooldown <= 0) {

                int beams = 1 + (HT - HP) / 400;
                HashSet<Integer> affectedCells = new HashSet<>();
                for (int i = 0; i < beams; i++) {

                    int targetPos = Dungeon.hero.pos;
                    if (i != 0) {
                        do {
                            targetPos = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                        } while (Dungeon.level.trueDistance(pos, Dungeon.hero.pos)
                                > Dungeon.level.trueDistance(pos, targetPos));
                    }
                    targetedCells.add(targetPos);
                    Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                    affectedCells.addAll(b.path);
                }

                //remove one beam if multiple shots would cause every cell next to the hero to be targeted
                boolean allAdjTargeted = true;
                for (int i : PathFinder.NEIGHBOURS9) {
                    if (!affectedCells.contains(Dungeon.hero.pos + i) && Dungeon.level.passable[Dungeon.hero.pos + i]) {
                        allAdjTargeted = false;
                        break;
                    }
                }
                if (allAdjTargeted) {
                    targetedCells.remove(targetedCells.size() - 1);
                }
                for (int i : targetedCells) {
                    Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                    for (int p : b.path) {
                        sprite.parent.add(new TargetedCell(p, 0xFF00FF));
                        affectedCells.add(p);
                    }
                }

                //don't want to overly punish players with slow move or attack speed
                spend(GameMath.gate(TICK, (int) Math.ceil(Dungeon.hero.cooldown()), 3 * TICK));
                Dungeon.hero.interrupt();

                abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
                abilityCooldown -= (phase - 1);

            } else {
                spend(TICK);
            }

            while (summonCooldown <= 0) {

                Class<? extends Mob> cls = regularSummons.remove(0);
                Mob summon = Reflection.newInstance(cls);
                regularSummons.add(cls);

                int spawnPos = -1;
                for (int i : PathFinder.NEIGHBOURS8) {
                    if (Actor.findChar(pos + i) == null) {
                        if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, pos + i)) {
                            spawnPos = pos + i;
                        }
                    }
                }

                //if no other valid spawn spots exist, try to kill an adjacent sheep to spawn anyway
                if (spawnPos == -1) {
                    for (int i : PathFinder.NEIGHBOURS8) {
                        if (Actor.findChar(pos + i) instanceof Sheep) {
                            if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, pos + i)) {
                                spawnPos = pos + i;
                            }
                        }
                    }
                    if (spawnPos != -1) {
                        Actor.findChar(spawnPos).die(null);
                    }
                }

                if (spawnPos != -1) {
                    summon.pos = spawnPos;
                    GameScene.add(summon);
                    Actor.add(new Pushing(summon, pos, summon.pos));
                    summon.beckon(Dungeon.hero.pos);
                    Dungeon.level.occupyCell(summon);

                    summonCooldown += Random.NormalFloat(MIN_SUMMON_CD, MAX_SUMMON_CD);
                    summonCooldown -= (phase - 1);
                    if (findFist() != null) {
                        summonCooldown += MIN_SUMMON_CD - (phase - 1);
                    }
                } else {
                    break;
                }
            }

        }

        if (summonCooldown > 0) summonCooldown--;
        if (abilityCooldown > 0) abilityCooldown--;

        //extra fast abilities and summons at the final 100 HP
        if (phase == 5 && abilityCooldown > 2) {
            abilityCooldown = 2;
        }
        if (phase == 5 && summonCooldown > 3) {
            summonCooldown = 3;
        }

        return true;
    }

    public void processFistDeath() {
        //normally Yog has no logic when a fist dies specifically
        //but the very last fist to die does trigger the final phase
        if (phase == 4 && findFist() == null) {
            yell(Messages.get(this, "hope"));
            summonCooldown = -15; //summon a burst of minions!
            phase = 5;
            BossHealthBar.bleed(true);
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Music.INSTANCE.play(Assets.Music.DIOLOWHP, true);
                }
            });
        }
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() || phase != 5;
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return phase == 0 || findFist() != null || super.isInvulnerable(effect);
    }

    @Override
    public void damage(int dmg, Object src) {

        int preHP = HP;
        super.damage(dmg, src);

        if (phase == 0 || findFist() != null) return;

        if (phase < 4) {
            HP = Math.max(HP, HT - 300 * phase);
        } else if (phase == 4) {
            HP = Math.max(HP, 100);
        }
        int dmgTaken = preHP - HP;

        if (dmgTaken > 0) {
            abilityCooldown -= dmgTaken / 10f;
            summonCooldown -= dmgTaken / 10f;
        }

        if (phase < 4 && HP <= HT - 300 * phase) {

            phase++;

            updateVisibility(Dungeon.level);

            if (HP == 700) {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new DioDialogSprite()},
                        new String[]{"DIO"},
                        new String[]{
                                Messages.get(YogDzewa.class, "fist")
                        },
                        new byte[]{
                                WndDialogueWithPic.IDLE
                        }
                );
            } else {
                GLog.n(Messages.get(this, "darkness"));
            }

            sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));

            addFist((YogFist) Reflection.newInstance(fistSummons.remove(0)));

            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
                addFist((YogFist) Reflection.newInstance(challengeSummons.remove(0)));
            }

            CellEmitter.get(Dungeon.level.exit() - 1).burst(ShadowParticle.UP, 25);
            CellEmitter.get(Dungeon.level.exit()).burst(ShadowParticle.UP, 100);
            CellEmitter.get(Dungeon.level.exit() + 1).burst(ShadowParticle.UP, 25);

            if (abilityCooldown < 5) abilityCooldown = 5;
            if (summonCooldown < 5) summonCooldown = 5;

        }

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass()) && !isInvulnerable(src.getClass())) {
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) lock.addTime(dmgTaken / 3f);
            else lock.addTime(dmgTaken / 2f);
        }

    }

    public void addFist(YogFist fist) {
        fist.pos = Dungeon.level.exit();

        CellEmitter.get(Dungeon.level.exit() - 1).burst(ShadowParticle.UP, 25);
        CellEmitter.get(Dungeon.level.exit()).burst(ShadowParticle.UP, 100);
        CellEmitter.get(Dungeon.level.exit() + 1).burst(ShadowParticle.UP, 25);

        if (abilityCooldown < 5) abilityCooldown = 5;
        if (summonCooldown < 5) summonCooldown = 5;

        int targetPos = Dungeon.level.exit() + Dungeon.level.width();

        if (!Dungeon.isChallenged(Challenges.STRONGER_BOSSES)
                && (Actor.findChar(targetPos) == null || Actor.findChar(targetPos) instanceof Sheep)) {
            fist.pos = targetPos;
        } else if (Actor.findChar(targetPos - 1) == null || Actor.findChar(targetPos - 1) instanceof Sheep) {
            fist.pos = targetPos - 1;
        } else if (Actor.findChar(targetPos + 1) == null || Actor.findChar(targetPos + 1) instanceof Sheep) {
            fist.pos = targetPos + 1;
        } else if (Actor.findChar(targetPos) == null || Actor.findChar(targetPos) instanceof Sheep) {
            fist.pos = targetPos;
        }

        if (Actor.findChar(fist.pos) instanceof Sheep) {
            Actor.findChar(fist.pos).die(null);
        }

        GameScene.add(fist, 4);

        Actor.add(new Pushing(fist, Dungeon.level.exit(), fist.pos));

        Sample.INSTANCE.play(Assets.Sounds.ZAWARUDO);
        GameScene.flash(0x666666);

        Dungeon.level.occupyCell(fist);

    }

    public void updateVisibility(Level level) {
        int viewDistance = 4;
        if (phase > 1 && isAlive()) {
            viewDistance = Math.max(4 - (phase - 1), 1);
        }
        if (Dungeon.isChallenged(Challenges.DARKNESS)) {
            viewDistance = Math.min(viewDistance, 2);
        }
        level.viewDistance = viewDistance;
        if (Dungeon.hero != null) {
            if (Dungeon.hero.buff(Light.class) == null) {
                Dungeon.hero.viewDistance = level.viewDistance;
            }
            Dungeon.observe();
        }
    }

    private YogFist findFist() {
        for (Char c : Actor.chars()) {
            if (c instanceof YogFist) {
                return (YogFist) c;
            }
        }
        return null;
    }

    @Override
    public void beckon(int cell) {
    }

    @Override
    public void clearEnemy() {
        //do nothing
    }

    @Override
    public void aggro(Char ch) {
        if (ch != null && ch.alignment != alignment || !(ch instanceof Larva || ch instanceof YogRipper || ch instanceof YogEye || ch instanceof YogScorpio)) {
            for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
                if (mob != ch && Dungeon.level.distance(pos, mob.pos) <= 4 && mob.alignment == alignment &&
                        (mob instanceof Larva || mob instanceof YogRipper || mob instanceof YogEye || mob instanceof YogScorpio)) {
                    mob.aggro(ch);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void die(Object cause) {

        Bestiary.skipCountingEncounters = true;
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Larva || mob instanceof YogRipper || mob instanceof YogEye || mob instanceof YogScorpio) {
                mob.die(cause);
            }
        }
        Bestiary.skipCountingEncounters = false;

        Item pick = new NitoDismantleHammer2();
        if (pick.doPickUp(Dungeon.hero)) {

        } else {
            Dungeon.level.drop(pick, Dungeon.hero.pos).sprite.drop();
        }

        updateVisibility(Dungeon.level);

        GameScene.bossSlain();

        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && Statistics.spawnersAlive == 4) {
            Badges.validateBossChallengeCompleted();
        } else {
            Statistics.qualifiedForBossChallengeBadge = false;
        }
        Statistics.bossScores[4] += 5000 + 1250 * Statistics.spawnersAlive;

        Badges.validateTakingTheMick(cause);

        Dungeon.level.unseal();
        super.die(cause);
        Music.INSTANCE.end();

        Statistics.zombiecount = 2;

        if (Random.Int(10) == 0) {
            GameScene.flash(0xFFFF00);
            Dungeon.level.drop(new BossdiscF().identify(), pos).sprite.drop(pos);
            new Flare(5, 32).color(0xFF00FF, true).show(hero.sprite, 2f);
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
            GLog.p(Messages.get(Kawasiri.class, "rare"));
        }

        if (Badges.isUnlocked(Badges.Badge.VICTORY) && !Dungeon.isChallenged(Challenges.EOH)) {
            Dungeon.level.drop(new Castleintro().identify(), pos).sprite.drop(pos);
        }

        switch (Dungeon.hero.heroClass) {
            case WARRIOR:
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new YogSprite(), new SupressionSprite()},
                        new String[]{"DIO", "죠나단"},
                        new String[]{
                                Messages.get(YogDzewa.class, "defeated"),
                                Messages.get(YogDzewa.class, "d1")
                        },
                        new byte[]{
                                WndDialogueWithPic.DIE,
                                WndDialogueWithPic.IDLE
                        }
                );
                break;
            case MAGE:
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new YogSprite(), new TrapperSprite()},
                        new String[]{"DIO", "죠셉"},
                        new String[]{
                                Messages.get(YogDzewa.class, "defeated"),
                                Messages.get(YogDzewa.class, "d2")
                        },
                        new byte[]{
                                WndDialogueWithPic.DIE,
                                WndDialogueWithPic.IDLE
                        }
                );
                break;
            case ROGUE:
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new YogSprite(), new TankSprite()},
                        new String[]{"DIO", "죠타로"},
                        new String[]{
                                Messages.get(YogDzewa.class, "defeated"),
                                Messages.get(YogDzewa.class, "d3")
                        },
                        new byte[]{
                                WndDialogueWithPic.DIE,
                                WndDialogueWithPic.IDLE
                        }
                );
                break;
            case DUELIST:
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new YogSprite(), new ResearcherSprite()},
                        new String[]{"DIO", "죠스케"},
                        new String[]{
                                Messages.get(YogDzewa.class, "defeated"),
                                Messages.get(YogDzewa.class, "d4")
                        },
                        new byte[]{
                                WndDialogueWithPic.DIE,
                                WndDialogueWithPic.IDLE
                        }
                );
                break;
            case HUNTRESS:
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new YogSprite(), new SoldierSprite()},
                        new String[]{"DIO", "죠르노"},
                        new String[]{
                                Messages.get(YogDzewa.class, "defeated"),
                                Messages.get(YogDzewa.class, "d5")
                        },
                        new byte[]{
                                WndDialogueWithPic.DIE,
                                WndDialogueWithPic.IDLE
                        }
                );
                break;
			case CLERIC:
				WndDialogueWithPic.dialogue(
						new CharSprite[]{new YogSprite(), new JojoSprite()},
						new String[]{"DIO", "죠린"},
						new String[]{
								Messages.get(YogDzewa.class, "defeated"),
								Messages.get(YogDzewa.class, "d6")
						},
						new byte[]{
								WndDialogueWithPic.DIE,
								WndDialogueWithPic.IDLE
						}
				);
				break;
        }

        Sample.INSTANCE.play(Assets.Sounds.NANI);
        SPDSettings.addSpecialcoin(3);
    }

    @Override
    public void notice() {

        Sample.INSTANCE.play(Assets.Sounds.CRAZYDIO);

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            switch (Dungeon.hero.heroClass) {
                case WARRIOR:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new SupressionSprite(), new DioDialogSprite()},
                            new String[]{"죠나단", "DIO"},
                            new String[]{
                                    Messages.get(YogDzewa.class, "n1"),
                                    Messages.get(YogDzewa.class, "n2")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case MAGE:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DioDialogSprite(), new TrapperSprite()},
                            new String[]{"DIO", "죠셉"},
                            new String[]{
                                    Messages.get(YogDzewa.class, "n3"),
                                    Messages.get(YogDzewa.class, "n4")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case ROGUE:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DioDialogSprite(), new TankSprite()},
                            new String[]{"DIO", "죠타로"},
                            new String[]{
                                    Messages.get(YogDzewa.class, "n5"),
                                    Messages.get(YogDzewa.class, "n6")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case DUELIST:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new ResearcherSprite(), new DioDialogSprite()},
                            new String[]{"죠스케", "DIO"},
                            new String[]{
                                    Messages.get(YogDzewa.class, "n7"),
                                    Messages.get(YogDzewa.class, "n8")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case HUNTRESS:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DioDialogSprite(), new SoldierSprite()},
                            new String[]{"DIO", "죠르노"},
                            new String[]{
                                    Messages.get(YogDzewa.class, "n9"),
                                    Messages.get(YogDzewa.class, "n10")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case CLERIC:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DioDialogSprite(), new JojoSprite()},
                            new String[]{"DIO", "죠린"},
                            new String[]{
                                    Messages.get(YogDzewa.class, "n11"),
                                    Messages.get(YogDzewa.class, "n12")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
            }
            for (Char ch : Actor.chars()) {
                if (ch instanceof DriedRose.GhostHero) {
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Music.INSTANCE.play(Assets.Music.HALLS_BOSS, true);
                }
            });
            if (phase == 0) {
                phase = 1;
                summonCooldown = Random.NormalFloat(MIN_SUMMON_CD, MAX_SUMMON_CD);
                abilityCooldown = Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
            }
        }
    }

    @Override
    public String description() {
        String desc = super.description();

        if (Statistics.spawnersAlive > 0) {
            desc += "\n\n" + Messages.get(this, "desc_spawners");
        }

        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            desc += "\n\n" + Messages.get(this, "high");
        }

        return desc;
    }

    private static final String PHASE = "phase";

    private static final String ABILITY_CD = "ability_cd";
    private static final String SUMMON_CD = "summon_cd";

    private static final String FIST_SUMMONS = "fist_summons";
    private static final String REGULAR_SUMMONS = "regular_summons";
    private static final String CHALLENGE_SUMMONS = "challenges_summons";

    private static final String TARGETED_CELLS = "targeted_cells";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);

        bundle.put(ABILITY_CD, abilityCooldown);
        bundle.put(SUMMON_CD, summonCooldown);

        bundle.put(FIST_SUMMONS, fistSummons.toArray(new Class[0]));
        bundle.put(CHALLENGE_SUMMONS, challengeSummons.toArray(new Class[0]));
        bundle.put(REGULAR_SUMMONS, regularSummons.toArray(new Class[0]));

        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++) {
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        if (phase != 0) {
            BossHealthBar.assignBoss(this);
            if (phase == 5) BossHealthBar.bleed(true);
        }

        abilityCooldown = bundle.getFloat(ABILITY_CD);
        summonCooldown = bundle.getFloat(SUMMON_CD);

        fistSummons.clear();
        Collections.addAll(fistSummons, bundle.getClassArray(FIST_SUMMONS));
        challengeSummons.clear();
        Collections.addAll(challengeSummons, bundle.getClassArray(CHALLENGE_SUMMONS));
        regularSummons.clear();
        Collections.addAll(regularSummons, bundle.getClassArray(REGULAR_SUMMONS));

        for (int i : bundle.getIntArray(TARGETED_CELLS)) {
            targetedCells.add(i);
        }
    }

    public static class Larva extends Mob {

        {
            spriteClass = LarvaSprite.class;

            HP = HT = 20;
            defenseSkill = 12;
            viewDistance = Light.DISTANCE;

            EXP = 5;
            maxLvl = -2;

            properties.add(Property.DEMONIC);
            properties.add(Property.BOSS_MINION);
        }

        @Override
        public int attackSkill(Char target) {
            return 30;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(15, 25);
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange(0, 4);
        }

    }

    //used so death to yog's ripper demons have their own rankings description
    public static class YogRipper extends RipperDemon {
        {
            maxLvl = -2;
            properties.add(Property.BOSS_MINION);
        }
    }

    public static class YogEye extends Eye {
        {
            maxLvl = -2;
            properties.add(Property.BOSS_MINION);
        }
    }

    public static class YogScorpio extends Scorpio {
        {
            maxLvl = -2;
            properties.add(Property.BOSS_MINION);
        }
    }
}
