/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 *  Shattered Pixel Dungeon
 *  Copyright (C) 2014-2022 Evan Debenham
 *
 * Summoning Pixel Dungeon
 * Copyright (C) 2019-2022 TrashboxBobylev
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
// import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
// import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.LoopBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.ArenaPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossExitRoom2;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.TendencyShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

// imports for trial effects
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArenaTrialMarker;
// import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
// import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob; // used for champion trials
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.watabou.utils.Reflection;

public class ArenaLevel extends RegularLevel {

    // Trial state (persisted)
    private int trialType = 0;
    private int trialTrapCount = 0;

    {
        color1 = 0x48763c;
        color2 = 0x48763c;
        // Random trial selection for Arena depths 28-35 (inclusive)
        if (Dungeon.tendencylevel && Dungeon.depth >= 1 && Dungeon.depth <= 35) {
            // 5개 시련: 1 어둠, 2 함정, 3 대형, 5 약화, 6 챔피언, 7 전갈 (4 제거), 강화 저하 추가 예정
            trialType = 1 + Random.Int(6);
            if (trialType == 4) trialType = 7;

            switch (trialType) {
                case 1: // Darkness
                    feeling = Level.Feeling.DARK;
                    // reduce vision now so it's in effect on enter
                    viewDistance = 2;
                    break;
                case 2: // Trap-heavy
                    // Add a noticeable number of traps to make the arena hazardous
                    trialTrapCount = 50 + Random.Int(6);
                    break;
                case 3: // Large (more mobs/items)
                    feeling = Level.Feeling.LARGE;
                    break;
                case 5: // Combat debuff
                    // create()에서 버프 적용
                    break;
                case 6: // Champion enemies
                    break;
                case 7: // Scorpio incursion
                    break;
                default:
                    break;
            }
        }
    }

    public void playLevelMusic(){
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.TENDENCY1},
                new float[]{1},
                false);
    }

    protected Painter painter() {
        return new ArenaPainter()
                .setWater( 0.35f, 4 )
                .setGrass( 0.25f, 3 )
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    private int arenaDoor;
    private boolean enteredArena = false;
    private boolean keyDropped = false;
    private int stairs = 0;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TENDENCY2;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                FrostTrap.class, SummoningTrap.class, StormTrap.class, CorrosionTrap.class, CorrosionTrap.class, ConfusionTrap.class,
                RockfallTrap.class, FlashingTrap.class, WornDartTrap.class, GuardianTrap.class, GuardianTrap.class, ExplosiveTrap.class,
                DisarmingTrap.class, DisarmingTrap.class, WarpingTrap.class, CursingTrap.class, GrimTrap.class, PitfallTrap.class, DistortionTrap.class };
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
                1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1 };
    }

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap setTrap(com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap trap, int pos) {
        com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap placed = super.setTrap(trap, pos);
        // 함정 시련에서는 모든 함정을 항상 보이도록 강제
        if (Dungeon.tendencylevel && trialType == 2) {
            if (placed != null) placed.visible = true;
            map[pos] = Terrain.TRAP;
        }
        return placed;
    }

    private static final String DOOR	= "door";
    private static final String ENTERED	= "entered";
    private static final String DROPPED	= "droppped";
    private static final String STAIRS	= "stairs";
    private static final String TRIAL_TYPE = "trial_type";
    private static final String TRIAL_TRAPS = "trial_traps";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DOOR, arenaDoor );
        bundle.put( ENTERED, enteredArena );
        bundle.put( DROPPED, keyDropped );
        bundle.put( STAIRS, stairs );
        bundle.put( TRIAL_TYPE, trialType );
        bundle.put( TRIAL_TRAPS, trialTrapCount );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        arenaDoor = bundle.getInt( DOOR );
        enteredArena = bundle.getBoolean( ENTERED );
        keyDropped = bundle.getBoolean( DROPPED );
        stairs = bundle.getInt( STAIRS );
        if (bundle.contains(TRIAL_TYPE)) trialType = bundle.getInt(TRIAL_TYPE);
        if (bundle.contains(TRIAL_TRAPS)) trialTrapCount = bundle.getInt(TRIAL_TRAPS);
        if (trialType == 1) {
            viewDistance = Math.round(5 * viewDistance / 8f);
            feeling = Level.Feeling.DARK;
        } else if (trialType == 3) {
            feeling = Level.Feeling.LARGE;
        }
        roomExit = roomEntrance;
    }

    @Override
    protected int nTraps() {
        return trialTrapCount;
    }

    public int nMobs() {
        int mobs = Math.round(4 + Dungeon.depth % 8 + Random.Int(4));
        if (feeling == Feeling.LARGE){
            mobs = (int)Math.ceil(mobs * 2f);
        }
        return (int) (mobs/1.5f);
    }

    @Override
    protected Builder builder() {
        return new LoopBuilder()
                .setLoopShape( 2 ,
                        Random.Float(0f, 0.6f),
                        Random.Float(0f, 0.6f));
    }

    public Actor addRespawner() {
        return null;
    }

    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();
        initRooms.add ( roomEntrance = new com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance.EntranceRoom());

        if (Dungeon.depth % 2 == 0) initRooms.add( new TendencyShopRoom());

        initRooms.add( roomExit = new SewerBossExitRoom2());

        for (int i = 0; i < 2 + Dungeon.depth * 2 / 8; i++) {
            StandardRoom s = StandardRoom.createRoom();
            initRooms.add(s);
        }

        return initRooms;
    }

    @Override
    public void pressCell(int cell) {
        super.pressCell(cell);

        for (Heap heap : Dungeon.level.heaps.valueList().toArray(new Heap[0])){
            if (heap.pos == cell){
                for (Item item : heap.items){
                    if (item instanceof SkeletonKey){
                        keyDropped = true;
                    }
                }
            }
        }
    }

    @Override
    public void create() {

//        if (Dungeon.depth % 2 == 0) addItemToSpawn(new Food());

        super.create();

        // 추가 패널티 적용 처리 (hazard 제거, 함정 공개 지연 제거)
        if (Dungeon.tendencylevel && Dungeon.depth >= 1 && Dungeon.depth <= 35) {
            if (trialType == 5) {
                // 전투 디버프 시련: 허약/취약/절름발이 중 하나 적용 (이 층에서만 유지)
                Buff.affect(Dungeon.hero, ArenaTrialMarker.class);
                int roll = Random.Int(3);
                if (roll == 0) Buff.prolong(Dungeon.hero, Weakness.class, Float.MAX_VALUE);
                else if (roll == 1) Buff.prolong(Dungeon.hero, Vulnerable.class, Float.MAX_VALUE);
                else Buff.prolong(Dungeon.hero, Cripple.class, Float.MAX_VALUE);
            } else if (trialType == 6) {
                // 챔피언의 위협: 일부 기존 몹에게 챔피언 버프 부여 (전갈과 분리)
                int champions = 2 + Random.Int(3); // 2~4마리 정도로 증가
                for (Mob m : mobs.toArray(new Mob[0])) {
                    if (champions <= 0) break;
                    if (m.alignment == com.shatteredpixel.shatteredpixeldungeon.actors.Char.Alignment.ENEMY && m.buff(ChampionEnemy.class) == null) {
                        // 강제 챔피언화: 무작위 챔피언 버프 직접 부여
                        Class<? extends ChampionEnemy> buffCls;
                        switch (Random.Int(6)){
                            case 0: default:    buffCls = ChampionEnemy.Blazing.class;      break;
                            case 1:             buffCls = ChampionEnemy.Projecting.class;   break;
                            case 2:             buffCls = ChampionEnemy.AntiMagic.class;    break;
                            case 3:             buffCls = ChampionEnemy.Giant.class;        break;
                            case 4:             buffCls = ChampionEnemy.Blessed.class;      break;
                            case 5:             buffCls = ChampionEnemy.Growing.class;      break;
                        }
                        Buff.affect(m, buffCls);
                        champions--;
                    }
                }
            } else if (trialType == 7) {
                // 전갈 난입: Scorpio 추가 소환
                Mob sc = Reflection.newInstance(Scorpio.class);
                if (sc != null) {
                    sc.state = sc.WANDERING;
                    int tries = 30;
                    do {
                        sc.pos = randomRespawnCell(sc);
                        tries--;
                    } while (sc.pos == -1 && tries > 0);
                    if (sc.pos != -1) {
                        mobs.add(sc);
                    }
                }
            }
        }

        if (Dungeon.tendencylevel && Dungeon.depth >= 2 && Dungeon.depth <= 35) {
            switch (trialType) {
                case 1:
                    GLog.p("파문의 수행: 어둠 - 시야가 감소합니다.");
                    break;
                case 2:
                    GLog.p("파문의 수행: 함정 - 함정이 대량으로 배치되며, 모든 함정이 항상 보입니다.");
                    break;
                case 3:
                    GLog.p("파문의 수행: 전투 - 적이 더 많이 등장합니다.");
                    break;
                case 5:
                    GLog.p("파문의 수행: 약화 - 무작위 디버프가 (이 층에서만) 적용됩니다.");
                    break;
                case 6:
                    GLog.p("파문의 수행: 강화 - 일부 적이 강화됩니다.");
                    break;
                case 7:
                    GLog.p("파문의 수행: 전갈 난입 - 전갈이 추가로 등장합니다.");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void createItems() {
        for (Item item : itemsToSpawn) {
            int cell = randomDropCell();
            drop( item, cell ).type = Heap.Type.HEAP;
            if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
        }

        //use a separate generator for this to prevent held items and meta progress from affecting levelgen
        Random.pushGenerator( Dungeon.seedCurDepth() );


        Random.popGenerator();
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int cell;
        do {
            cell = pointToCell( roomEntrance.random() );
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    public void seal() {
        if (entrance != 0) {

            set( entrance, Terrain.CHASM );
            GameScene.updateMap( entrance );
            GameScene.ripple( entrance );

            stairs = entrance;
            entrance = 0;
        }
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);

        if (ch == Dungeon.hero && !roomEntrance.inside(cellToPoint(ch.pos)) && !locked && !keyDropped){
            seal();
        }
    }

    @Override
    public void unseal() {
        if (stairs != 0) {

            super.unseal();

            entrance = stairs;
            stairs = 0;

            set( entrance, Terrain.ENTRANCE );
            GameScene.updateMap( entrance );
        }
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(CityLevel.class, "water_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(CityLevel.class, "high_grass_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.ENTRANCE:
                return Messages.get(CityLevel.class, "entrance_desc");
            case Terrain.EXIT:
                return Messages.get(CityLevel.class, "exit_desc");
            case Terrain.WALL_DECO:
            case Terrain.EMPTY_DECO:
                return Messages.get(CityLevel.class, "deco_desc");
            case Terrain.EMPTY_SP:
                return Messages.get(CityLevel.class, "sp_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(CityLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CityLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {

        if (transition.type == LevelTransition.Type.SURFACE){
            if (hero.belongings.getItem( Amulet.class ) == null) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show( new WndMessage( Messages.get(hero, "tendency") ) );
                    }
                });
                return false;
            } else {

                return true;
            }
        } else if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show( new WndMessage( Messages.get(hero, "tendency2") ) );
                }
            });
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        // hazard trial removed
        if (map[exit()-1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit()-1));
        if (map[exit()+1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit()+1));
        return visuals;
    }

    @Override
    public Group addWallVisuals() {
        Group g = super.addWallVisuals();
        // no special trap handling (reverted to previous behavior)
        return g;
    }

    // addVisualsAfter 훅이 없으므로 이 추가 코드는 제거

}
