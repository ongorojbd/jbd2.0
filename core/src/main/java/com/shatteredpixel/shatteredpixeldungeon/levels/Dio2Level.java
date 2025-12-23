/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Willa4;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Zombieb;
import com.shatteredpixel.shatteredpixeldungeon.effects.Ripple;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Bmap;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Gmap;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.HallsPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MimicTooth;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.MachineTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Dio2Level extends RegularLevel {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    public void playLevelMusic(){
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.DIO_1},
                new float[]{1},
                false);
    }

    @Override
    protected boolean build() {
        boolean result = super.build();

        // 모든 잠긴 문을 열림 상태로 변경
        if (result && rooms != null) {
            for (Room r : rooms) {
                for (Room n : r.connected.keySet()) {
                    Room.Door door = r.connected.get(n);
                    if (door != null) {
                        // LOCKED 또는 CRYSTAL 문을 UNLOCKED로 변경
                        if (door.type == Room.Door.Type.LOCKED || door.type == Room.Door.Type.CRYSTAL) {
                            door.type = Room.Door.Type.UNLOCKED;
                            // 맵의 지형도 변경
                            int doorCell = door.x + door.y * width();
                            if (map[doorCell] == Terrain.LOCKED_DOOR || map[doorCell] == Terrain.CRYSTAL_DOOR) {
                                map[doorCell] = Terrain.DOOR;
                            }
                        }
                    }
                }
            }

            // 맵 전체에서 잠긴 문 찾아서 열기
            for (int i = 0; i < length(); i++) {
                if (map[i] == Terrain.LOCKED_DOOR || map[i] == Terrain.CRYSTAL_DOOR) {
                    map[i] = Terrain.DOOR;
                }
            }
        }

        return result;
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();
        initRooms.add(roomEntrance = EntranceRoom.createEntrance());
        initRooms.add(roomExit = ExitRoom.createExit());

        // 일반 방만 생성 (특수 방과 비밀 방 제외)
        int standards = standardRooms(feeling == Feeling.LARGE);
        if (feeling == Feeling.LARGE) {
            standards = (int) Math.ceil(standards * 1.5f);
        }
        standards *= 3; // Dio2Level은 일반 방을 3배로 생성
        for (int i = 0; i < standards; i++) {
            StandardRoom s;
            do {
                s = StandardRoom.createRoom();
            } while (!s.setSizeCat(standards - i));
            i += s.sizeFactor() - 1;
            initRooms.add(s);
        }

        // 상점도 제외 (열쇠가 필요할 수 있으므로)
        // if (Dungeon.shopOnLevel())
        //     initRooms.add(new ShopRoom());

        // 특수 방 생성하지 않음
        // int specials = specialRooms(feeling == Feeling.LARGE);
        // ...

        // 비밀 방도 생성하지 않음
        // int secrets = SecretRoom.secretsForFloor(Dungeon.depth);
        // ...

        return initRooms;
    }

    @Override
    protected void createMobs() {
        super.createMobs();
    }

    @Override
    public void addItemToSpawn(Item item) {
        // Dio2Level에서는 열쇠를 스폰하지 않음
        if (item != null && !(item instanceof Key)) {
            super.addItemToSpawn(item);
        }
    }

    @Override
    protected void createItems() {
        // drops 3/4/5 items 60%/30%/10% of the time
        int nItems = 3 + Random.chances(new float[]{6, 3, 1});

        if (feeling == Feeling.LARGE) {
            nItems += 2;
        }

        for (int i = 0; i < nItems; i++) {

            Item toDrop = Generator.random();
            if (toDrop == null) continue;

            int cell = randomDropCell();
            if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }

            Heap.Type type = null;
            switch (Random.Int(20)) {
                case 0:
                    type = Heap.Type.SKELETON;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    //base mimic chance is 1/20, regular chest is 4/20
                    // so each +1x mimic spawn rate converts to a 25% chance here
                    if (Random.Float() < (MimicTooth.mimicChanceMultiplier() - 1f) / 4f && findMob(cell) == null) {
                        mobs.add(Mimic.spawnAt(cell, toDrop));
                        continue;
                    }

                    type = Heap.Type.CHEST;
                    break;
                case 5:
                    if (Dungeon.depth > 1 && findMob(cell) == null) {
                        mobs.add(Mimic.spawnAt(cell, toDrop));
                        continue;
                    }
                    type = Heap.Type.CHEST;
                    break;
                default:
                    type = Heap.Type.HEAP;
                    break;
            }

            // Dio2Level에서는 잠긴 상자와 열쇠를 생성하지 않음
            if ((toDrop instanceof Artifact && Random.Int(2) == 0) ||
                    (toDrop.isUpgradable() && Random.Int(4 - toDrop.level()) == 0)) {

                float mimicChance = 1 / 10f * MimicTooth.mimicChanceMultiplier();
                if (Dungeon.depth > 1 && Random.Float() < mimicChance && findMob(cell) == null) {
                    mobs.add(Mimic.spawnAt(cell, GoldenMimic.class, toDrop));
                } else {
                    // 일반 상자로 생성 (잠긴 상자 아님)
                    Heap dropped = drop(toDrop, cell);
                    if (heaps.get(cell) == dropped) {
                        dropped.type = Heap.Type.CHEST; // LOCKED_CHEST 대신 CHEST 사용
                        // 열쇠 생성하지 않음
                    }
                }
            } else {
                Heap dropped = drop(toDrop, cell);
                dropped.type = type;
                if (type == Heap.Type.SKELETON) {
                    dropped.setHauntedIfCursed();
                }
            }

        }

        // itemsToSpawn 처리 (열쇠는 제외하고, TrinketCatalyst도 잠긴 상자로 만들지 않음)
        ArrayList<Item> itemsToProcess = new ArrayList<>(itemsToSpawn);
        itemsToSpawn.clear(); // 원본 리스트 비우기

        for (Item item : itemsToProcess) {
            // 열쇠는 스킵
            if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.keys.Key) {
                continue;
            }

            int cell = randomDropCell();
            if (item instanceof TrinketCatalyst) {
                // 일반 상자로 생성 (잠긴 상자 아님)
                drop(item, cell).type = Heap.Type.CHEST; // LOCKED_CHEST 대신 CHEST 사용
                // 열쇠 생성하지 않음
            } else {
                drop(item, cell).type = Heap.Type.HEAP;
            }
            if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
        }
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 6;
        //5 to 6, average 5.5
        return 5+Random.chances(new float[]{1, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        // Dio2Level에서는 특수 방을 생성하지 않음
        return 0;
    }

    @Override
    protected Painter painter() {
        return new HallsPainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
                .setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_DIO;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }


    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                FrostTrap.class, StormTrap.class, CorrosionTrap.class, BlazingTrap.class, DisintegrationTrap.class,
                RockfallTrap.class, FlashingTrap.class, WeakeningTrap.class, MachineTrap.class,
                DisarmingTrap.class, SummoningTrap.class, WarpingTrap.class, CursingTrap.class, PitfallTrap.class, GatewayTrap.class, GeyserTrap.class};
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
                4, 4, 4, 4, 4,
                2, 2, 2, 2,
                1, 1, 1, 1, 1, 1, 1};
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addSewerVisuals(this, visuals);
        return visuals;
    }

    public static void addSewerVisuals( Level level, Group group ) {
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new Sink( i ) );
            }
        }
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(SewerLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(SewerLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(SewerLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    private static class Sink extends Emitter {

        private int pos;
        private float rippleDelay = 0;

        private static final Factory factory = new Factory() {

            @Override
            public void emit( Emitter emitter, int index, float x, float y ) {
                WaterParticle p = (WaterParticle)emitter.recycle( WaterParticle.class );
                p.reset( x, y );
            }
        };

        public Sink( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
            pos( p.x - 2, p.y + 3, 4, 0 );

            pour( factory, 0.1f );
        }

        @Override
        public void update() {
            if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {

                super.update();

                if (!isFrozen() && (rippleDelay -= Game.elapsed) <= 0) {
                    Ripple ripple = GameScene.ripple( pos + Dungeon.level.width() );
                    if (ripple != null) {
                        ripple.y -= DungeonTilemap.SIZE / 2;
                        rippleDelay = Random.Float(0.4f, 0.6f);
                    }
                }
            }
        }
    }

    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.MAP0),
                            Messages.titleCase(Messages.get(Bmap.class, "name")),
                            Messages.get(Bmap.class, "1"),
                            Messages.get(Bmap.class, "yes"),
                            Messages.get(Bmap.class, "no")){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                                InterlevelScene.returnDepth = 22;
                                InterlevelScene.returnBranch = 0;
                                InterlevelScene.returnPos = -1;
                                Game.switchScene(InterlevelScene.class);
                            }
                        }
                    } );
                }
            });
            return false;

        } else {
            return super.activateTransition(hero, transition);
        }
    }

    public static final class WaterParticle extends PixelParticle {

        public WaterParticle() {
            super();

            acc.y = 50;
            am = 0.5f;

            color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
            size( 2 );
        }

        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            speed.set( Random.Float( -2, +2 ), 0 );

            left = lifespan = 0.4f;
        }
    }
}
