package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;
import static com.shatteredpixel.shatteredpixeldungeon.levels.TempleLastLevel.bottomDoor;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Keichomob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.TempleLastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KeichoSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Keicho extends NPC {

    private static final String[] LINE_KEYS = {"1", "2", "3", "4", "5", "6", "7"};

    {
        spriteClass = KeichoSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    boolean mine = true;

    private final String MINE = "mine";

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean add( Buff buff ) {
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

    private float spawnCooldown = 5;

    @Override
    protected boolean act() {
        if (mine) {
            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            switch(Dungeon.hero.heroClass){
                case WARRIOR:
                case ROGUE:
                    GLog.n(Messages.get(Keicho.class, "a"));
                    break;
                case MAGE:
                    GLog.n(Messages.get(Keicho.class, "b"));
                    break;
                case HUNTRESS:
                    GLog.n(Messages.get(Keicho.class, "d"));
                    break;
                case DUELIST:
                    GLog.n(Messages.get(Keicho.class, "c"));
                    GLog.newLine();
                    GLog.p(Messages.get(Keicho.class, "e"));
                    break;
            }
            mine = false;
        }

        if (Statistics.bcom == 4) {
            set(bottomDoor, Terrain.EMPTY);
            GameScene.updateMap(bottomDoor);

            Keichomob Kawasiri = new Keichomob();
            Kawasiri.state = Kawasiri.PASSIVE;
            Kawasiri.pos = this.pos;
            GameScene.add(Kawasiri);
            Kawasiri.beckon(Dungeon.hero.pos);

            destroy();
            sprite.killAndErase();
            die(null);
        }

        if (fieldOfView == null || fieldOfView.length != level.length()) {
            fieldOfView = new boolean[level.length()];
        }
        level.updateFieldOfView(this, fieldOfView);

        if (properties().contains(Property.IMMOVABLE)) {
            throwItems();
        }

        if (spawnCooldown > 20) {
            spawnCooldown = 20;
        }

        if (hero != null) {
            if (fieldOfView[hero.pos]
                    && level.map[hero.pos] == Terrain.EMPTY_SP
                    && hero.buff(LostInventory.class) == null) {
                spawnCooldown--;
            } else {
                sprite.idle();
            }
            spawnCooldown--;

            if (spawnCooldown <= 0) {

                //we don't want spawners to store multiple brutes
                if (spawnCooldown < -20) {
                    spawnCooldown = -20;
                }

                ArrayList<Integer> candidates = new ArrayList<>();
                for (int n : PathFinder.NEIGHBOURS8) {
                    if (level.passable[pos + n] && Actor.findChar(pos + n) == null) {
                        candidates.add(pos + n);
                    }
                }

                if (!candidates.isEmpty()) {
                    Mob spawn;
                    switch (Random.Int(3)) {
                        case 0: default:
                            spawn = new TempleLastLevel.TempleGuard();
                            break;
                        case 1:
                            spawn = new TempleLastLevel.TempleBrute();
                            break;
                        case 2:
                            spawn = new TempleLastLevel.TemplePurpleShaman();
                            break;
                    }

                    spawn.pos = 8+15*31;
                    spawn.state = spawn.HUNTING;

//                    if(mine) {
//                        Level.set(4 + 4 * 31, Terrain.SECRET_TRAP);
//                        level.setTrap(t1, 4 + 4 * 31);
//                        level.discover(4 + 4 * 31);
//
//                        Level.set(2 + 4 * 31, Terrain.SECRET_TRAP);
//                        level.setTrap(t1, 2 + 4 * 31);
//                        level.discover(2 + 4 * 31);
//
//                        Level.set(4 + 3 * 31, Terrain.TRAP);
//                        level.setTrap(t1, 4 + 3 * 31);
//                        level.discover(4 + 3 * 31);
//
//                        Level.set(4 + 2 * 31, Terrain.SECRET_TRAP);
//                        level.setTrap(t1, 4 + 2 * 31);
//                        level.discover(4 + 2 * 31);
//
//                        Level.set(3 + 4 * 31, Terrain.SECRET_TRAP);
//                        level.setTrap(t1, 3 + 4 * 31);
//                        level.discover(3 + 4 * 31);
//
//                        mine = false;
//
//                    }

                    GameScene.add(spawn);
                    level.occupyCell(spawn);

                    Mob spawn2;
                    switch (Random.Int(3)) {
                        case 0: default:
                            spawn2 = new TempleLastLevel.TempleGuard();
                            break;
                        case 1:
                            spawn2 = new TempleLastLevel.TempleBrute();
                            break;
                        case 2:
                            spawn2 = new TempleLastLevel.TemplePurpleShaman();
                            break;
                    }

                    spawn2.pos = 22+15*31;
                    spawn2.state = spawn2.HUNTING;

                    GameScene.add(spawn2);
                    level.occupyCell(spawn2);


                    Mob spawn3;
                    switch (Random.Int(3)) {
                        case 0: default:
                            spawn3 = new TempleLastLevel.TempleGuard();
                            break;
                        case 1:
                            spawn3 = new TempleLastLevel.TempleBrute();
                            break;
                        case 2:
                            spawn3 = new TempleLastLevel.TemplePurpleShaman();
                            break;
                    }

                    spawn3.pos = 15+22*31;
                    spawn3.state = spawn3.HUNTING;

                    GameScene.add(spawn3);
                    level.occupyCell(spawn3);

                    ((KeichoSprite)sprite).leapPrep(hero.pos);

                    switch (Random.Int( 3 )) {
                        case 0:
                            Sample.INSTANCE.play( Assets.Sounds.K1 );
                            break;
                        case 1:
                            Sample.INSTANCE.play( Assets.Sounds.K2 );
                            break;
                        case 2:
                            Sample.INSTANCE.play( Assets.Sounds.K3 );
                            break;
                    }

                    yell( Messages.get(this, Random.element( LINE_KEYS )));
                    spawnCooldown += 20;

                }
            }
            alerted = false;
        }
        return super.act();
    }
}