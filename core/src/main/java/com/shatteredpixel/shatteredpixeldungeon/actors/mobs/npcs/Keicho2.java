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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieThree;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieTwo;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Tmap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TbossSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
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

public class Keicho2 extends NPC {

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

    final String msg1Final = Messages.get(Tboss.class, "t1");
    final String msg2Final = Messages.get(Tboss.class, "t2");

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
                                Tendency tendency = new Tendency();
                                tendency.pos = hero.pos;
                                GameScene.add( tendency );
                                tendency.beckon(Dungeon.hero.pos);
                                GLog.n(Messages.get(Tendency.class, "t2"));
                                Dungeon.quickslot.clearSlot(4);
                                Dungeon.quickslot.clearSlot(5);
                                Dungeon.quickslot.setSlot(4, a);
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

            mobsToSpawn = 3;  // 여기에 mobsToSpawn 값을 설정
            if (wave == 2) mobsToSpawn = 2; // 조건문에 적힌 웨이브로 넘어가기 위해 잡아야할 몹의 수
            if (wave == 4) mobsToSpawn = 4;
            if (wave == 7) mobsToSpawn = 6;
            if (wave == 8) mobsToSpawn = 2;
            if (wave == 9) mobsToSpawn = 5;
            if (wave == 11) mobsToSpawn = 4;
            if (wave == 13) mobsToSpawn = 4;
            if (wave == 14) mobsToSpawn = 4;
            if (wave == 15) mobsToSpawn = 8;
            if (wave == 16) mobsToSpawn = 4;
            if (wave == 17) mobsToSpawn = 4;
            if (wave == 18) mobsToSpawn = 5;
            if (wave == 20) mobsToSpawn = 8;

            if (spawnCooldown <= 0) {

                Statistics.duwang3 = 0;
                Statistics.zombiecount = 2;

                if (spawnCooldown < -12) {
                    spawnCooldown = -12;
                }

                int[] spawnPositions = {8 + 15 * 31, 22 + 15 * 31, 15 + 22 * 31, 10 + 20 * 31, 18 + 18 * 31, 12 + 17 * 31, 20 + 19 * 31, 14 + 16 * 31};
                int thisWaveMobSpawn = 3;
                if (wave == 1) thisWaveMobSpawn = 2; // 이번에 소환할 몹의 수
                if (wave == 3) thisWaveMobSpawn = 4;
                if (wave == 6) thisWaveMobSpawn = 6;
                if (wave == 7) thisWaveMobSpawn = 2;
                if (wave == 8) thisWaveMobSpawn = 5;
                if (wave == 10) thisWaveMobSpawn = 4;
                if (wave == 12) thisWaveMobSpawn = 4;
                if (wave == 13) thisWaveMobSpawn = 4;
                if (wave == 14) thisWaveMobSpawn = 8;
                if (wave == 15) thisWaveMobSpawn = 4;
                if (wave == 16) thisWaveMobSpawn = 4;
                if (wave == 17) thisWaveMobSpawn = 5;
                if (wave == 19) thisWaveMobSpawn = 8;
                Statistics.duwang2 = thisWaveMobSpawn;
                GLog.h(Messages.get(Keicho.class, "t1", wave, thisWaveMobSpawn));
                ArrayList<Class<? extends Mob>> spawnClasses = getSpawnForWave(wave);
                for (int i = 0; i < thisWaveMobSpawn; i++) {
                    Mob spawn = Reflection.newInstance(spawnClasses.remove(0));
                    spawn.pos = spawnPositions[i];
                    spawn.state = spawn.HUNTING;
                    GameScene.add(spawn);
                    level.occupyCell(spawn);
                }

                if (wave == 20) {
                    Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new TbossSprite(), new TbossSprite()},
                            new String[]{"스트레이초", "스트레이초"},
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
            case 1:
            default:
                return new ArrayList<>(Arrays.asList(ZombieTwo.class, ZombieTwo.class));
            case 2:
                return new ArrayList<>(Arrays.asList(ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 3:
                return new ArrayList<>(Arrays.asList(ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 4:
                return new ArrayList<>(Arrays.asList(ZombieThree.class, ZombieTwo.class, ZombieTwo.class));
            case 5:
                return new ArrayList<>(Arrays.asList(ZombieThree.class, ZombieThree.class, ZombieTwo.class));
            case 6:
                return new ArrayList<>(Arrays.asList(ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 7:
                return new ArrayList<>(Arrays.asList(ZombieBrute.class, ZombieTwo.class));
            case 8:
                return new ArrayList<>(Arrays.asList(ZombieThree.class, ZombieThree.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 9:
                return new ArrayList<>(Arrays.asList(ZombieBrute.class, ZombieTwo.class, ZombieTwo.class));
            case 10:
                return new ArrayList<>(Arrays.asList(ZombieBrute.class, ZombieThree.class, ZombieTwo.class, ZombieTwo.class));
            case 11:
                return new ArrayList<>(Arrays.asList(ZombieBrute.class, ZombieBrute2.class, ZombieTwo.class));
            case 12:
                return new ArrayList<>(Arrays.asList(ZombieBrute2.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 13:
                return new ArrayList<>(Arrays.asList(ZombieBrute2.class, ZombieBrute2.class, ZombieTwo.class, ZombieTwo.class));
            case 14:
                return new ArrayList<>(Arrays.asList(ZombieBrute.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieBrute.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 15:
                return new ArrayList<>(Arrays.asList(ZombieBrute2.class, ZombieBrute2.class, ZombieBrute.class, ZombieTwo.class));
            case 16:
                return new ArrayList<>(Arrays.asList(ZombieBrute2.class, ZombieBrute.class, ZombieTwo.class, ZombieTwo.class));
            case 17:
                return new ArrayList<>(Arrays.asList(ZombieBrute2.class, ZombieBrute2.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 18:
                return new ArrayList<>(Arrays.asList(ZombieBrute.class, ZombieBrute.class, ZombieBrute.class));
            case 19:
                return new ArrayList<>(Arrays.asList(ZombieBrute.class, ZombieBrute2.class, ZombieBrute2.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieTwo.class));
            case 20:
                return new ArrayList<>(Arrays.asList(Tboss.class, ZombieTwo.class, ZombieTwo.class));
        }

    }
}
