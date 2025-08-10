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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Civil;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P1mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P2mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P3mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P4mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P5mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Castleintro;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.PolpoItem;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FlameKatana;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DoppioDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EmporioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Passione2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PassioneSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PolpoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Polpo extends NPC {

    {
        spriteClass = PolpoSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        if (hero.buff(AscensionChallenge.class) != null) {
            destroy();
            sprite.killAndErase();
            die(null);
            return true;
        }
        return super.act();
    }

    @Override
    public void beckon(int cell) {
        // do nothing: immovable
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean interact(Char c) {
        Sample.INSTANCE.play(Assets.Sounds.POL);
        sprite.turnTo(pos, c.pos);

        if (c != hero) return true;

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        sprite(),
                        Messages.titleCase(name()),
                        Messages.get(Polpo.class, "desc"),
                        Messages.get(Polpo.class, "1"),
                        Messages.get(Polpo.class, "2"),
                        Messages.get(Polpo.class, "3")
                ) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            showSummonOptions();
                        } else if (index == 1) {
                            if (!Statistics.polpoQuest) showPolpoItemOptions();
                            else GLog.n(Messages.get(Polpo.class, "test3"));
                        } else {
                            triggerAmbush();
                        }
                    }
                });
            }
        });

        return true;
    }

    private void showSummonOptions() {
        GameScene.show(new WndOptions(
                sprite(),
                Messages.titleCase(name()),
                Messages.get(Polpo.class, "intro"),
                Messages.get(Polpo.class, "4"),
                Messages.get(Polpo.class, "5"),
                Messages.get(Polpo.class, "6"),
                Messages.get(Polpo.class, "7"),
                Messages.get(Polpo.class, "8")
        ) {
            @Override
            protected void onSelect(int index) {
                switch (index) {
                    case 0:
                        if (Dungeon.gold > 499) {
                            Sample.INSTANCE.play(Assets.Sounds.GOLD);
                            Dungeon.gold -= 500;
                            summon();
                        } else {
                            yellNo();
                        }
                        break;
                    case 1:
                        trySummonWithItem(PotionOfHealing.class);
                        break;
                    case 2:
                        if (Dungeon.energy > 14) {
                            Dungeon.energy -= 15;
                            summon();
                        } else {
                            yellNo();
                        }
                        break;
                    case 3:
                        trySummonWithItem(Pasty.class);
                        break;
                    case 4:
                        trySummonWithItem(ScrollOfIdentify.class);
                        break;
                }
            }
        });
    }

    private void trySummonWithItem(Class<? extends Item> itemClass) {
        Item item = Dungeon.hero.belongings.getItem(itemClass);
        if (item != null) {
            item.detach(hero.belongings.backpack);
            summon();
        } else {
            yellNo();
        }
    }

    private void yellNo() {
        GLog.n(Messages.get(Polpo.class, "no"));
    }

    private void showPolpoItemOptions() {

        GameScene.show(new WndOptions(
                sprite(),
                Messages.titleCase(name()),
                Messages.get(Polpo.class, "test", Statistics.deepestFloor + 3),
                Messages.get(Polpo.class, "test1"),
                Messages.get(Polpo.class, "test2")
        ) {
            @Override
            protected void onSelect(int index) {
                if (index == 0) {
                    if (!Statistics.polpoQuest) {
                        Statistics.polpocount = Statistics.deepestFloor + 3;
                        PolpoItem polpoItem = new PolpoItem();
                        if (polpoItem.doPickUp(Dungeon.hero)) {
                            GLog.i(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", polpoItem.name())));
                        } else {
                            Dungeon.level.drop(polpoItem, Dungeon.hero.pos).sprite.drop();
                        }
                        Statistics.polpoQuest = true;
                    } else {

                    }
                }
            }
        });
    }

    private void triggerAmbush() {
        Sample.INSTANCE.play(Assets.Sounds.MIMIC);
        Music.INSTANCE.play(Assets.Music.PRISON_BOSS, true);

        destroy();
        die(null);
        Dungeon.level.drop(new Gold().identify().quantity(200), pos).sprite.drop(pos);

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new PassioneSprite.Na(), new PassioneSprite.Mi(), new PassioneSprite.Ab()},
                new String[]{"나란차 길가", "귀도 미스타", "레오네 아바키오"},
                new String[]{
                        Messages.get(Polpo.class, "a1"),
                        Messages.get(Polpo.class, "a2"),
                        Messages.get(Polpo.class, "a3")
                },
                new byte[]{
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE
                }
        );

        ArrayList<Integer> spawnPoints = findSpawnPoints();

        if (!spawnPoints.isEmpty()) {
            P1mob mob = new P1mob();
            mob.alignment = Alignment.ENEMY;
            spawnAndTeleport(mob, spawnPoints);

            P2mob mob2 = new P2mob();
            mob2.alignment = Alignment.ENEMY;
            spawnAndTeleport(mob2, spawnPoints);

            P4mob mob4 = new P4mob();
            mob4.alignment = Alignment.ENEMY;
            spawnAndTeleport(mob4, spawnPoints);
        }
    }

    public void summon() {
        GLog.p(Messages.get(Polpo.class, "summon"));

        ArrayList<Integer> spawnPoints = findSpawnPoints();

        if (!spawnPoints.isEmpty()) {
            int roll = Random.Int(5);
            switch (roll) {
                case 0:
                    spawnAndLog(new P3mob(), "p3", "p3j", spawnPoints);
                    break;
                case 1:
                    GameScene.show(
                            new WndOptions(new PassioneSprite.Ab(),
                                    Messages.get(P2mob.class, "w"),
                                    Messages.get(P2mob.class, "prick_warn"),
                                    Messages.get(P2mob.class, "yes"),
                                    Messages.get(P2mob.class, "no")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        GameScene.flash(0x660000);
                                        Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 1);
                                        GLog.n(Messages.get(P2mob.class, "z6"));
                                        hero.HP = 1;}
                                    }

                            }
                    );
                    spawnAndLog(new P2mob(), "p2", "p2j", spawnPoints);
                    break;
                case 2:
                    spawnAndLog(new P1mob(), "p1", "p1j", spawnPoints);
                    break;
                case 3:
                    spawnAndLog(new P4mob(), "p4", "p4j", spawnPoints);
                    break;
                case 4:
                    spawnAndLog(new P5mob(), "p5", "p5j", spawnPoints);
                    break;
            }

            destroy();
            die(null);
        } else {
            GLog.w(Messages.get(SpiritHawk.class, "no_space"));
        }
    }

    private ArrayList<Integer> findSpawnPoints() {
        ArrayList<Integer> spawnPoints = new ArrayList<>();
        for (int offset : PathFinder.NEIGHBOURS8) {
            int cell = hero.pos + offset;
            if (Actor.findChar(cell) == null && Dungeon.level.passable[cell]) {
                spawnPoints.add(cell);
            }
        }
        return spawnPoints;
    }

    private void spawnAndTeleport(Mob mob, ArrayList<Integer> spawnPoints) {
        GameScene.add(mob);
        ScrollOfTeleportation.appear(mob, Random.element(spawnPoints));
    }

    private void spawnAndLog(Mob mob, String logKey, String huntressLogKey, ArrayList<Integer> spawnPoints) {
        spawnAndTeleport(mob, spawnPoints);
        String msg = Messages.get(Polpo.class,
                Dungeon.hero.heroClass == HeroClass.HUNTRESS ? huntressLogKey : logKey);
        GLog.n(msg);
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
        // immune
    }

    @Override
    public boolean reset() {
        return true;
    }
}
