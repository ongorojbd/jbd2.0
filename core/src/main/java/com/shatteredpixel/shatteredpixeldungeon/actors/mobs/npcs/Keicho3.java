package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.wave;
import static com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw.pickOrDropItem;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tboss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tendency;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieBrute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieBrute2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieFour;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieThree;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieTwo;
import com.shatteredpixel.shatteredpixeldungeon.items.Bandana;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Tmap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TbossSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombietSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBossText;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Keicho3 extends NPC {

    {
        spriteClass = VampireSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    boolean mine = true;

    private final String MINE = "mine";
    private int mobsToSpawn;

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
        //do nothing
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

    private static final String SPAWN_COOLDOWN = "spawnCooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPAWN_COOLDOWN, spawnCooldown);
        bundle.put(MINE, mine);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spawnCooldown = bundle.getFloat(SPAWN_COOLDOWN);
        mine = bundle.getBoolean(SPAWN_COOLDOWN);
    }

    public static float spawnCooldown = 6;

    @Override
    protected boolean act() {
        if (fieldOfView == null || fieldOfView.length != level.length()) {
            fieldOfView = new boolean[level.length()];
        }
        level.updateFieldOfView(this, fieldOfView);

        if (properties().contains(Property.IMMOVABLE)) {
            throwItems();
        }

        if (hero != null) {


            if (Statistics.duwang3 >= mobsToSpawn) {
                spawnCooldown--;
            }

            yell(String.valueOf(spawnCooldown));

            if (wave == 21 && spawnCooldown == 4) {
                Sample.INSTANCE.play(Assets.Sounds.TENDENCY2);
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndBossText(new Tendency(), Messages.get(Tendency.class, "t1")) {
                            @Override
                            public void hide() {
                                super.hide();
                                Item a = new Tmap();
                                pickOrDropItem(a);
                                Item b = new Bandana();
                                b.identify();
                                pickOrDropItem(b);
                                Tendency tendency = new Tendency();
                                tendency.pos = hero.pos;
                                GameScene.add( tendency );
                                tendency.beckon(Dungeon.hero.pos);
                                GLog.n(Messages.get(Tendency.class, "t2"));
                                Dungeon.quickslot.clearSlot(4);
                                Dungeon.quickslot.clearSlot(5);
                                Dungeon.quickslot.setSlot(4, a);
                                Dungeon.quickslot.setSlot(5, b);
                            }
                        });
                    }
                });
            }

            if (Statistics.duwang == 2 && spawnCooldown == 9 && wave >= 3 && (wave & 1) == 1) {
                for (Char c : Actor.chars()) {
                    if (c instanceof NewShopKeeper) {
                        c.interact(hero);
                    }
                }
                Statistics.duwang = 0;
            }

            if (Statistics.spw6 > 0 && spawnCooldown == 6) {
                Item a = new Tbomb();
                a.identify();
                pickOrDropItem(a);
            }

            mobsToSpawn = 1;

            if (spawnCooldown <= 0) {

                Statistics.duwang3 = 0;
                Statistics.zombiecount = 2;

                if (spawnCooldown < -12) {
                    spawnCooldown = -12;
                }

                int thisWaveMobSpawn = 1;
                Statistics.duwang2 = thisWaveMobSpawn;
                GLog.h(Messages.get(Keicho.class, "t1", wave, thisWaveMobSpawn));
                ArrayList<Class<? extends Mob>> spawnClasses = getSpawnForWave(wave);
                for (int i = 0; i < thisWaveMobSpawn; i++) {
                    Mob spawn = Reflection.newInstance(spawnClasses.remove(0));
                    spawn.pos = 15 + 9 * 31;
                    spawn.state = spawn.HUNTING;
                    GameScene.add(spawn);
                    level.occupyCell(spawn);
                }

                if (wave == 20) {
                    Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new ZombietSprite(), new ZombietSprite()},
                            new String[]{"타커스", "타커스"},
                            new String[]{
                                    Messages.get(Tboss.class, "t1"),
                                    Messages.get(Tboss.class, "t2")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.IDLE
                            }
                    );
                }

                wave++;
                spawnCooldown += 12;
            }

            alerted = false;
        }
        return super.act();
    }

    private ArrayList<Class<? extends Mob>> getSpawnForWave(int wave) {

        switch (wave) {
            default:
                return new ArrayList<>(Collections.singletonList(ZombieFour.class));
        }

    }
}
