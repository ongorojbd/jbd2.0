package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.wave;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieFour;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombietBoss;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Zombiet2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;

public class Keicho3 extends NPC {

    {
        spriteClass = VampireSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    public Keicho3(){
        super();
        switch (Random.Int(4)){
            case 0: default:
                spriteClass = VampireSprite.Blue.class;
                break;
            case 1:
                spriteClass = VampireSprite.Green.class;
                break;
            case 2:
                spriteClass = VampireSprite.Red.class;
                break;
            case 3:
                spriteClass = VampireSprite.Yellow.class;
                break;
        }
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

            mobsToSpawn = 1;

            if (spawnCooldown <= 0) {

                Statistics.duwang3 = 0;
                Statistics.zombiecount = 2;

                if (spawnCooldown < -12) {
                    spawnCooldown = -12;
                }

                int thisWaveMobSpawn = 1;
                Statistics.duwang2 = thisWaveMobSpawn;
                GLog.h(Messages.get(Keicho.class, "t1", wave));
                ArrayList<Class<? extends Mob>> spawnClasses = getSpawnForWave(wave);
                for (int i = 0; i < thisWaveMobSpawn; i++) {
                    Mob spawn = Reflection.newInstance(spawnClasses.remove(0));
                    spawn.pos = 15 + 9 * 31;
                    spawn.state = spawn.HUNTING;
                    GameScene.add(spawn);
                    level.occupyCell(spawn);
                }

                if (wave == 10) {
                    Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new Zombiet2Sprite(), new Zombiet2Sprite()},
                            new String[]{"타커스", "타커스"},
                            new String[]{
                                    Messages.get(ZombietBoss.class, "n1"),
                                    Messages.get(ZombietBoss.class, "n2")
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
            case 10:
                return new ArrayList<>(Collections.singletonList(ZombietBoss.class));
        }

    }
}
