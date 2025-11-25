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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rebel;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Emporio2;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.PortableCover4;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class LabsBossLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	@Override
	public void playLevelMusic() {
		if (locked){
			Music.INSTANCE.play(Assets.Music.LABS_BOSS, true);
			//if top door isn't unlocked
		} else if (map[exit()] != Terrain.EXIT){
			Music.INSTANCE.end();
		} else {
			if (hero.buff(AscensionChallenge.class) != null) {
				Music.INSTANCE.playTracks(
						new String[]{Assets.Music.CIV},
						new float[]{1},
						false);
			} else Music.INSTANCE.playTracks(
					new String[]{Assets.Music.LABS_1},
					new float[]{1},
					false);
		}
	}

	private static final int WIDTH = 33;
	private static final int HEIGHT = 40;

	private static final Rect entry = new Rect(0, 27, 33, 6);
	private static final Rect arena = new Rect(0, 0, 33, 26);
	private static final Rect exitDoor = new Rect(16, 1, 16, 1);

	private static final int topDoor = 16 + 33;
	private static final int bottomDoor = 16 + (arena.bottom+1) * 33;
	private static final int bossPos = bottomDoor-11*33;

	private static boolean isCompleted = false;

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_LABS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_LABS;
	}

	@Override
	protected boolean build() {
		setSize(WIDTH, HEIGHT);

		transitions.add(new LevelTransition(this, 16 + (30)*33, LevelTransition.Type.REGULAR_ENTRANCE));

		int exitCell = topDoor;
		LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
		exit.set(exitDoor);
		transitions.add(exit);

		buildLevel();

//		Emporio2 npc = new Emporio2();
//		npc.pos = 30 * width() + 16;
//		mobs.add( npc );
//
//		Emporio2 npc2 = new Emporio2();
//		npc2.pos = 28 * width() + 14;
//		mobs.add( npc2 );
//
//		Emporio2 npc3 = new Emporio2();
//		npc3.pos = 28 * width() + 18;
//		mobs.add( npc3 );

		return true;
	}

	private static final short n = -1; //used when a tile shouldn't be changed
	private static final short W = Terrain.WALL;
	private static final short e = Terrain.EMPTY;
	private static final short E = Terrain.ENTRANCE;
	private static final short p = Terrain.PEDESTAL;
	private static final short s = Terrain.ALCHEMY;
	private static final short D = Terrain.DOOR;
	private static final short C = Terrain.STATUE_SP;
	private static final short B = Terrain.BARRICADE;
	private static final short i = Terrain.EMPTY_SP;
	private static final short t = Terrain.EMPTY;
	private static final short J = Terrain.STATUE;
	private static final short L = Terrain.LOCKED_EXIT;

	private static short[] level = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, C, i, i, i, i, i, i, i, C, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, C, i, i, i, i, i, e, e, e, i, e, e, e, i, i, i, i, i, C, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, e, i, e, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, i, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, i, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, i, i, i, i, i, i, i, i, i, i, i, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, i, i, i, i, i, i, i, i, i, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, i, i, i, i, i, i, i, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, e, i, i, i, i, i, e, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, i, i, i, e, i, i, i, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, i, i, e, e, e, i, i, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, i, i, e, e, e, e, e, i, i, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, C, i, i, i, i, i, e, e, e, e, e, e, e, i, i, i, i, i, C, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, i, e, e, e, e, e, e, e, i, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, C, i, i, i, i, i, i, i, C, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, C, i, i, i, C, W, W, W, i, i, i, i, i, i, i, i, i, W, W, W, C, i, i, i, C, W, W, W, W,
			W, W, W, W, i, e, e, e, i, W, W, W, i, J, t, e, e, e, t, J, i, W, W, W, i, e, e, e, i, W, W, W, W,
			W, W, W, W, i, e, s, e, i, i, i, D, i, J, t, e, E, e, t, J, i, D, i, i, i, e, s, e, i, W, W, W, W,
			W, W, W, W, i, e, e, e, i, W, W, W, i, J, t, e, e, e, t, J, i, W, W, W, i, e, e, e, i, W, W, W, W,
			W, W, W, W, C, i, i, i, C, W, W, W, i, i, i, i, i, i, i, i, i, W, W, W, C, i, i, i, C, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
	};

	private void buildLevel(){
		int pos = 0 + 0*width(); 								//시작점의 x, y 좌표, width()가 곱해져 있는 것이 y다.

		short[] levelTiles = level; 							//위에서 노가다 한 것을 하나의 변수로 만들고
		for (int i = 0; i < levelTiles.length; i++){ 			//0부터 위 노가다에 포함된 요소의 개수만큼 반복
			if (levelTiles[i] != n) map[pos] = levelTiles[i];	//요소를 n으로 설정한 것이 아닌 경우 위 노가다에 포함
			// 된 요소대로 맵 제작
			pos++; 												//만들고 나서 다음 칸으로 이동하기 위해 필요한 것. ex)벽 -> 벽일 경우 첫번째 벽이 pos고, 두번째 벽이 pos+1.
		}
	}

	@Override
	public void occupyCell(Char ch) {

		super.occupyCell(ch);

		boolean isRebelAlive = false;

		for (int i=0 ; i < Dungeon.level.length ; i++) {
			Char mob = Actor.findChar(i);
			if (mob instanceof Rebel) {
				isRebelAlive = true;
				break;
			}
		}

		if (map[bottomDoor] != Terrain.LOCKED_DOOR && ch.pos < bottomDoor && ch == Dungeon.hero && !isCompleted) {
			seal();
			return;
		}

		if (Dungeon.level.map[topDoor] == Terrain.LOCKED_EXIT && ch.pos < bottomDoor && ch == Dungeon.hero && !isRebelAlive) {
			seal(); //When the boss is not appeared
		}
	}

	@Override
	protected void createMobs() {
	}

	@Override
	protected void createItems() {

		Item prize1 = Random.oneOf(
				new PortableCover4().quantity(1)
		);

		Item prize5 = Random.oneOf(
				new ScrollOfMetamorphosis().quantity(3),
				new Blandfruit().quantity(3),
				new PotionOfShielding().quantity(1)
		);

		drop( prize1, 6+(28)*33 ).type = Heap.Type.CHEST;
		drop( prize5, 26+(28)*33 ).type = Heap.Type.CHEST;

		Random.pushGenerator(Random.Long());
		ArrayList<Item> bonesItems = Bones.get();
		if (bonesItems != null) {
			int pos;
			do {
				pos = randomRespawnCell(null);
			} while (pos == entrance());
			for (Item i : bonesItems) {
				drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
			}
		}
		Random.popGenerator();
	}

	public int randomCellPos() {
		int eC_1 	= Random.IntRange( 7+7*33, 	25+7*33 );
		int eC_2 	= Random.IntRange( 7+8*33, 	25+8*33 );
		int eC_3 	= Random.IntRange( 7+9*33, 	25+9*33 );
		int eC_4 	= Random.IntRange( 7+10*33, 25+10*33 );
		int eC_5 	= Random.IntRange( 7+11*33, 25+11*33 );
		int eC_6 	= Random.IntRange( 7+12*33, 25+12*33 );
		int eC_7 	= Random.IntRange( 7+13*33, 25+13*33 );
		int eC_8 	= Random.IntRange( 7+14*33, 25+14*33 );
		int eC_9 	= Random.IntRange( 7+15*33, 25+15*33 );
		int eC_10 	= Random.IntRange( 7+16*33, 25+16*33 );
		int eC_11 	= Random.IntRange( 7+17*33, 25+17*33 );
		int eC_12 	= Random.IntRange( 7+18*33, 25+18*33 );
		int eC_13 	= Random.IntRange( 7+19*33, 25+19*33 );
		int eC_14 	= Random.IntRange( 7+20*33, 25+20*33 );
		int eC_15 	= Random.IntRange( 7+21*33, 25+21*33 );
		int eC_16 	= Random.IntRange( 7+22*33, 25+22*33 );
		int eC_17 	= Random.IntRange( 7+23*33, 25+23*33 );
		int eC_18 	= Random.IntRange( 7+24*33, 25+24*33 );
		int eC_19 	= Random.IntRange( 7+25*33, 25+25*33 ); //eC = emptyCell
		int randomEmptyCell = Random.oneOf( eC_1, eC_2, eC_3, eC_4, eC_5, eC_6, eC_7, eC_8, eC_9, eC_10, eC_11, eC_12, eC_13, eC_14, eC_15, eC_16, eC_17, eC_18, eC_19 );
		return randomEmptyCell;
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int cell;
		do {
			cell = entrance() + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public void seal() {
		if (!locked) {
			super.seal();

			//moves intelligent allies with the hero, preferring closer pos to entrance door
			int doorPos = bottomDoor;
			Mob.holdAllies(this, doorPos);
			Mob.restoreAllies(this, Dungeon.hero.pos, doorPos);

			Rebel boss = new Rebel();
			boss.state = boss.WANDERING;
			boss.pos = pointToCell(arena.center());
			GameScene.add( boss );
			boss.beckon(Dungeon.hero.pos);

			if (heroFOV[boss.pos]) {
				boss.notice();
				boss.sprite.alpha( 0 );
				boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
			}

			set(bottomDoor, Terrain.LOCKED_DOOR);
			GameScene.updateMap(bottomDoor);
			Dungeon.observe();

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Music.INSTANCE.play(Assets.Music.LABS_BOSS, true);
				}
			});
		}
	}

	@Override
	public void unseal() {
		super.unseal();

		set(topDoor, Terrain.UNLOCKED_EXIT);
		GameScene.updateMap(topDoor);

		set(bottomDoor, Terrain.DOOR);
		GameScene.updateMap(bottomDoor);

		isCompleted = true;

		Dungeon.observe();

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				Music.INSTANCE.end();
			}
		});
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(LabsLevel.class, "water_name");
			case Terrain.WALL_DECO:
				return Messages.get(LabsLevel.class, "wall_deco_name");
			case Terrain.STATUE:
				return Messages.get(LabsLevel.class, "statue_name");
			case Terrain.LOCKED_EXIT:
				return Messages.get(LabsLevel.class, "locked_exit_name");
			case Terrain.UNLOCKED_EXIT:
				return Messages.get(LabsLevel.class, "unlocked_exit_name");
			default:
				return super.tileName( tile );
		}
	}

	private static final String ISCOMPLETED = "iscompleted";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(ISCOMPLETED, isCompleted);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		for (Mob m : mobs){
			if (m instanceof YogDzewa){
				((YogDzewa) m).updateVisibility(this);
			}
		}
		isCompleted = bundle.getBoolean( ISCOMPLETED );
		//pre-1.3.0 saves, modifies exit transition with custom size
		if (bundle.contains("exit")){
			LevelTransition exit = getTransition(LevelTransition.Type.REGULAR_EXIT);
			exit.set(16, 1, 16, 1);
			transitions.add(exit);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(LabsLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(LabsLevel.class, "exit_desc");
			case Terrain.EMPTY_DECO:
				return Messages.get(LabsLevel.class, "empty_deco_desc");
			case Terrain.WALL_DECO:
				return Messages.get(LabsLevel.class, "wall_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(LabsLevel.class, "bookshelf_desc");
			case Terrain.STATUE:
				return Messages.get(LabsLevel.class, "statue_desc");
			case Terrain.LOCKED_EXIT:
				return Messages.get(LabsLevel.class, "locked_exit_desc");
			case Terrain.UNLOCKED_EXIT:
				return Messages.get(LabsLevel.class, "unlocked_exit_desc");
			default:
				return super.tileDesc( tile );
		}
	}

}