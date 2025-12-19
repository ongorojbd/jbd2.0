/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Val;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.FigureEightBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.SewerPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.RatKingRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.GooBossRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DvdolSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JojoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JosukeDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SupressionSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TankSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapperSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerBossLevel extends SewerLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	@Override
	public void playLevelMusic() {
		if (locked){
			Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
			return;
		}

		boolean gooAlive = false;
		for (Mob m : mobs){
			if (m instanceof Goo) {
				gooAlive = true;
				break;
			}
		}

		if (gooAlive){
			Music.INSTANCE.end();
		} else {
			if (hero.buff(AscensionChallenge.class) != null) {
				Music.INSTANCE.playTracks(
						new String[]{Assets.Music.CIV},
						new float[]{1},
						false);
			} else Music.INSTANCE.playTracks(SewerLevel.SEWER_TRACK_LIST, SewerLevel.SEWER_TRACK_CHANCES, false);
		}

	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		
		initRooms.add( roomEntrance = new SewerBossEntranceRoom() );
		initRooms.add( roomExit = new SewerBossExitRoom() );
		
		int standards = standardRooms(true);
		for (int i = 0; i < standards; i++) {
			StandardRoom s = StandardRoom.createRoom();
			//force to normal size
			s.setSizeCat(0, 0);
			initRooms.add(s);
		}
		
		GooBossRoom gooRoom = GooBossRoom.randomGooRoom();
		initRooms.add(gooRoom);
		((FigureEightBuilder)builder).setLandmarkRoom(gooRoom);
		initRooms.add(new RatKingRoom());
		return initRooms;
	}
	
	@Override
	protected int standardRooms(boolean forceMax) {
		if (forceMax) return 3;
		//2 to 3, average 2.5
		return 2+Random.chances(new float[]{1, 1});
	}
	
	protected Builder builder(){
		return new FigureEightBuilder()
				.setLoopShape( 2 , Random.Float(0.3f, 0.8f), 0f)
				.setPathLength(1f, new float[]{1})
				.setTunnelLength(new float[]{1, 2}, new float[]{1});
	}
	
	@Override
	protected Painter painter() {
		return new SewerPainter()
				.setWater(0.50f, 5)
				.setGrass(0.20f, 4)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}
	
	protected int nTraps() {
		return 0;
	}

	@Override
	protected void createMobs() {
	}
	
	public Actor addRespawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Random.pushGenerator(Random.Long());
			ArrayList<Item> bonesItems = Bones.get();
			if (bonesItems != null) {
				int pos;
				do {
					pos = pointToCell(roomEntrance.random());
				} while (pos == entrance() || solid[pos]);
				for (Item i : bonesItems) {
					drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
				}
			}
		Random.popGenerator();
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (Point p : roomEntrance.getPoints()){
			int cell = pointToCell(p);
			if (passable[cell]
					&& roomEntrance.inside(p)
					&& Actor.findChar(cell) == null
					&& (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])){
				candidates.add(cell);
			}
		}

		if (candidates.isEmpty()){
			return -1;
		} else {
			return Random.element(candidates);
		}
	}

	
	public void seal() {
		if (!locked) {

			super.seal();

			Statistics.qualifiedForBossChallengeBadge = true;

			switch(hero.heroClass){
				case WARRIOR:
					WndDialogueWithPic.dialogue(
							new CharSprite[]{new DvdolSprite(), new GhostSprite(), new GooSprite(), new SupressionSprite()},
							new String[]{"무함마드 압둘", "무함마드 압둘", "크림", "죠나단"},
							new String[]{
									Messages.get(Goo.class, "n1"),
									Messages.get(Goo.class, "n2"),
									Messages.get(Goo.class, "n3"),
									Messages.get(Goo.class, "n32")
							},
							new byte[]{
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.DIE,
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.IDLE
							}
					);
					break;
				case HUNTRESS:
					WndDialogueWithPic.dialogue(
							new CharSprite[]{new DvdolSprite(), new GhostSprite(), new GooSprite()},
							new String[]{"무함마드 압둘", "무함마드 압둘", "크림"},
							new String[]{
									Messages.get(Goo.class, "n1"),
									Messages.get(Goo.class, "n2"),
									Messages.get(Goo.class, "n3")
							},
							new byte[]{
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.DIE,
									WndDialogueWithPic.IDLE
							}
					);
					break;
				case ROGUE:
					WndDialogueWithPic.dialogue(
							new CharSprite[]{new DvdolSprite(), new GhostSprite(), new GooSprite(), new TankSprite()},
							new String[]{"무함마드 압둘", "무함마드 압둘", "크림", "죠타로"},
							new String[]{
									Messages.get(Goo.class, "n1"),
									Messages.get(Goo.class, "n2"),
									Messages.get(Goo.class, "n4"),
									Messages.get(Goo.class, "n5")
							},
							new byte[]{
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.DIE,
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.IDLE
							}
					);
					break;
				case MAGE:
					WndDialogueWithPic.dialogue(
							new CharSprite[]{new TrapperSprite(), new GooSprite()},
							new String[]{"죠셉", "크림"},
							new String[]{
									Messages.get(Val.class, "v1"),
									Messages.get(Val.class, "v2"),
							},
							new byte[]{
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.IDLE
							}
					);
					break;
				case DUELIST:
					WndDialogueWithPic.dialogue(
							new CharSprite[]{new GooSprite(), new JosukeDialogSprite()},
							new String[]{"크림", "죠스케"},
							new String[]{
									Messages.get(Goo.class, "notice5"),
									Messages.get(Val.class, "9"),
							},
							new byte[]{
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.IDLE
							}
					);
					Buff.affect(Dungeon.hero, Adrenaline.class, 1f);
					break;
				case CLERIC:
					WndDialogueWithPic.dialogue(
							new CharSprite[]{new DvdolSprite(), new GhostSprite(), new GooSprite(), new JojoSprite()},
							new String[]{"무함마드 압둘", "무함마드 압둘", "크림", "죠린"},
							new String[]{
									Messages.get(Goo.class, "n1"),
									Messages.get(Goo.class, "n2"),
									Messages.get(Goo.class, "n3"),
									Messages.get(Goo.class, "n6")
							},
							new byte[]{
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.DIE,
									WndDialogueWithPic.IDLE,
									WndDialogueWithPic.IDLE
							}
					);
					break;
                case JOHNNY:
                    WndDialogueWithPic.dialogue(
                            new CharSprite[]{new DvdolSprite(), new GhostSprite(), new GooSprite(), new BlacksmithSprite()},
                            new String[]{"무함마드 압둘", "무함마드 압둘", "크림", "죠니"},
                            new String[]{
                                    Messages.get(Goo.class, "n1"),
                                    Messages.get(Goo.class, "n2"),
                                    Messages.get(Goo.class, "n3"),
                                    Messages.get(Goo.class, "n7")
                            },
                            new byte[]{
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.DIE,
                                    WndDialogueWithPic.IDLE,
                                    WndDialogueWithPic.RUN
                            }
                    );
                    break;
			}

			set( entrance(), Terrain.WATER );
			GameScene.updateMap( entrance() );
			GameScene.ripple( entrance() );

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
				}
			});
		}
	}
	
	public void unseal() {
		if (locked) {

			super.unseal();

			set( entrance(), Terrain.ENTRANCE );
			GameScene.updateMap( entrance() );

			Game.runOnRenderThread(new Callback() {

						@Override
						public void call() {
							Music.INSTANCE.end();
						}


			});
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		if (map[exit()-1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit()-1));
		if (map[exit()+1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit()+1));
		return visuals;
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		roomExit = roomEntrance;
	}
}
