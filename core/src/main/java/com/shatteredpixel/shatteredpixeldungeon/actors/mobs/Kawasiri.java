/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Kawasiribuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Reimi;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscD;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KousakuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ResearcherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SupressionSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Kawasiri extends Mob {


    {
        spriteClass = KousakuSprite.class;

        HP = HT = 150;
        viewDistance = 999;
        baseSpeed = 0.5f;

        EXP = 15;
        defenseSkill = 15;
        immunities.add(Vertigo.class);
        properties.add(Property.INORGANIC);
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
        immunities.add(ShrGas.class);
        immunities.add(Grim.class);
        immunities.add(Dread.class);
        immunities.add(Terror.class);
        immunities.add(Blindness.class);
        immunities.add(Sleep.class);
        immunities.add(Doom.class);
    }

    public int Phase = 0;
    int summonCooldown = 7;
    int amCooldown = 249;
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private static final String AM_COOLDOWN = "amcooldown";
    private static final String SKILL2TIME = "BurstTime";
    private static int WIDTH = 33;
    private int BurstTime = 0;
    public static int[] kira = new int[]{4 + 13 * WIDTH};


    @Override
    public void die(Object cause) {

        super.die(cause);

        GameScene.bossSlain();

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new KousakuSprite(), new ShopkeeperSprite()},
                new String[]{"카와지리 코사쿠", "스기모토 레이미"},
                new String[]{
                        Messages.get(Kawasiri.class, "phase4"),
                        Messages.get(Kawasiri.class, "phase5")
                },
                new byte[]{
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE
                }
        );

        Buff.affect(hero, Kawasiribuff.class);

        if (Random.Int(10) == 0) {
            GameScene.flash(0xFFFF00);
            Dungeon.level.drop(new BossdiscD().identify(), pos).sprite.drop(pos);
            new Flare(5, 32).color(0xFFFF00, true).show(hero.sprite, 2f);
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
            GLog.p(Messages.get(Kawasiri.class, "rare"));
        }
        Music.INSTANCE.end();

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof DestOrb || mob instanceof Amblance) {
                mob.die(cause);
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, Phase);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
        bundle.put(AM_COOLDOWN, amCooldown);
        bundle.put(SKILL2TIME, BurstTime);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        Phase = bundle.getInt(PHASE);
        summonCooldown = bundle.getInt(SUMMON_COOLDOWN);
        amCooldown = bundle.getInt(AM_COOLDOWN);
        BurstTime = bundle.getInt(SKILL2TIME);
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 49) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 49;
        }

        super.damage(dmg, src);

        if (Phase == 0 && HP < 151) {
            Phase = 1;
            HP = 150;
        }

        if (Phase == 1 && HP < 149) {
            Phase = 2;
            HP = 149;
            yell(Messages.get(this, "notice"));
        }

        if (Phase == 2 && HP < 148) {
            Phase = 3;
            HP = 148;

            Viscosity.DeferedDamage deferred = Buff.affect(this, Viscosity.DeferedDamage.class);
            deferred.extend(dmg);
        }
        if (Phase == 3 && HP < 147) {
            Phase = 4;
            baseSpeed = 1f;
            HP = 147;
            Buff.detach(this, Viscosity.DeferedDamage.class);

            if (!BossHealthBar.isAssigned()) {
                BossHealthBar.assignBoss(this);
                for (Char ch : Actor.chars()) {
                }
            }
            switch (Dungeon.hero.heroClass) {
                case WARRIOR:
                case MAGE:
                case HUNTRESS:
                case CLERIC:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new KousakuSprite(), new KousakuSprite()},
                            new String[]{"카와지리 코사쿠", "카와지리 코사쿠"},
                            new String[]{
                                    Messages.get(Kawasiri.class, "n1"),
                                    Messages.get(Kawasiri.class, "n2")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case ROGUE:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new KousakuSprite()},
                            new String[]{"카와지리 코사쿠"},
                            new String[]{
                                    Messages.get(Kawasiri.class, "jotaro")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
                case DUELIST:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new KousakuSprite(), new ResearcherSprite()},
                            new String[]{"카와지리 코사쿠", "죠스케"},
                            new String[]{
                                    Messages.get(Kawasiri.class, "josuke"),
                                    Messages.get(Kawasiri.class, "josuke2")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                    break;
            }

            Music.INSTANCE.play(Assets.Music.CAVES_BOSS, true);
        }
        if (Phase == 4 && HP < 75) {
            Phase = 5;
            HP = 74;
            baseSpeed = 1f;
            yell(Messages.get(this, "phase2"));

        }
        if (Phase == 5 && HP < 54) {
            Phase = 6;
            HP = 150;

            baseSpeed = 1f;

            new Fadeleaf().activate(this);
            Dungeon.hero.damage(Dungeon.hero.HP / 3, this);
            GameScene.flash(0x80FFFFFF);
            Camera.main.shake(11, 3f);
            sprite.emitter().burst(Speck.factory(Speck.HEALING), 15);
            Sample.INSTANCE.play(Assets.Sounds.TBOMB);

            Buff.affect(Dungeon.hero, Blindness.class, 3f);
            Buff.affect(Dungeon.hero, Cripple.class, 7f);
            Buff.affect(Dungeon.hero, Weakness.class, 11f);

            yell(Messages.get(this, "phase3"));
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new KousakuSprite()},
                    new String[]{"카와지리 코사쿠"},
                    new String[]{
                            Messages.get(Kawasiri.class, "bom")
                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE
                    }
            );
        }

        if (Phase == 6 && HP < 52) {
            Phase = 7;
            HP = 51;
            yell(Messages.get(this, "phase7"));
            Music.INSTANCE.play(Assets.Music.KOICHI, true);

            for (int i : kira) {
                Amblance Amblance = new Amblance();
                Amblance.state = Amblance.WANDERING;
                Amblance.pos = i;
                GameScene.add(Amblance, DELAY);
                Amblance.beckon(Dungeon.hero.pos);
            }
        }

    }

    private static final String PHASE = "Phase";
    private static final float DELAY = 2f;
    private static final float DELAY3 = 249f;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(15, 25);
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }


    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public void notice() {
        super.notice();
    }

    @Override
    protected boolean act() {
        summonCooldown--;

        if (Phase == 0 && HP < 151) {
            state = FLEEING;
        }

        if (Phase == 1 && HP < 149) {
            state = FLEEING;
        }

        if (Phase == 2 && HP < 148) {
            if (state == FLEEING) state = SLEEPING;
        }

        if (Phase >= 4 && Phase <= 5) {
            state = HUNTING;
        }

        if (Phase >= 6 && HP < 52) {
            state = FLEEING;
            baseSpeed = 1.1f;
        }


        if (Phase > 3) {
            amCooldown--;
            if (amCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {

                for (int i : PathFinder.NEIGHBOURS2) {
                    Amblance Amblance = new Amblance();
                    Amblance.state = Amblance.WANDERING;
                    Amblance.pos = this.pos + i;
                    GameScene.add(Amblance);
                    Amblance.beckon(Dungeon.hero.pos);
                }

                amCooldown = (249);

            }
        }


        if (Phase == 4) {
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {


                new DestOrbTrap().set(target).activate();


                summonCooldown = (7);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }
        if (Phase == 5) {
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {


                new DestOrbTrap().set(target).activate();


                summonCooldown = (7);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }
        if (Phase == 6) {
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {


                new DestOrbTrap().set(target).activate();


                summonCooldown = (7);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }
        if (Phase == 7) {
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {


                for (int i : PathFinder.NEIGHBOURS1) {
                    DestOrb DestOrb = new DestOrb();
                    DestOrb.state = DestOrb.WANDERING;
                    DestOrb.pos = this.pos + i;
                    GameScene.add(DestOrb);
                    DestOrb.beckon(Dungeon.hero.pos);
                }


                summonCooldown = (7);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }

        return super.act();
    }
}

