package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//클레릭이 이벤트로 생명 창조의 스탠드사를 선택한 경우 사용 가능: 주변 8타일 중 2타일에 무작위 식물, 나머지에 수풀 생성
public class GreenBirth extends ClericSpell {

	public static final GreenBirth INSTANCE = new GreenBirth();

	private static final int PLANTS = 2;

	@Override
	public int icon() {
		return HeroIcon.WARDEN;
	}

	@Override
	public float chargeUse(Hero hero) {
		return 2;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero) && hero.subClass == HeroSubClass.WARDEN;
	}

	@Override
	public void onCast(HolyTome tome, Hero hero) {

		ArrayList<Integer> cells = new ArrayList<>();
		for (int offset : PathFinder.NEIGHBOURS8) {
			int cell = hero.pos + offset;
			int terr = Dungeon.level.map[cell];
			if (!(terr == Terrain.EMPTY || terr == Terrain.EMBERS || terr == Terrain.EMPTY_DECO
					|| terr == Terrain.GRASS || terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS)) {
				continue;
			}
			if (Char.hasProp(Actor.findChar(cell), Char.Property.IMMOVABLE)) continue;
			if (Dungeon.level.plants.get(cell) != null) continue;
			cells.add(cell);
		}
		Random.shuffle(cells);

		for (int i = 0; i < cells.size(); i++) {
			int cell = cells.get(i);
			if (i < PLANTS) {
				Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), cell);
			} else {
				Level.set(cell, Terrain.HIGH_GRASS);
				GameScene.updateMap(cell);
			}
			CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
		}
		Dungeon.observe();

		Sample.INSTANCE.play(Assets.Sounds.PLANT);

		hero.spend( 1f );
		hero.busy();
		hero.sprite.operate(hero.pos);

		onSpellCast(tome, hero);
	}
}
