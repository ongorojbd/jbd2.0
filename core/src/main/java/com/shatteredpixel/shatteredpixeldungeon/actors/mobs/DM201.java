package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PitfallParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WoolParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscA;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM201Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class DM201 extends Mob {
	{
		spriteClass = DM201Sprite.class;

		HP = HT = 100;
		defenseSkill = 12;

		EXP = 11;
		maxLvl = 17;

		baseSpeed = 1f;

		flying = true;

		viewDistance = 12;

		properties.add(Property.BOSS);
		properties.add(Property.INORGANIC);


	}

	private int phase = 3;
	private int blastcooldown = 990;
	private int summoncooldown = 996;
	private int barriercooldown = 994;
	private int volcanocooldown = 995;
	private int volcanotime = 997;
	private int restorecooldown = 993;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 25);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 9);
	}

	@Override
	public int attackSkill( Char target ) {
		return 25;
	}


	@Override
	public void damage(int dmg, Object src) {

		if (phase == 2) {

			return;
		}

		if (this.buff(Barrier.class) != null) {
			dmg /= 4;
		}

		super.damage(dmg, src);

		if (phase==1 && HP < 75) {
			HP = 75;
			phase = 2;
			Buff.detach(this, Barrier.class);
			summoncooldown = 1;
			blastcooldown = 1;
			barriercooldown = 4;
			volcanocooldown = 7;
			restorecooldown = 10;
		}
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (buff(RestorBuff.class) != null && !(enemy instanceof Hero)) {
			damage = 0;
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	protected boolean act() {

		 {

	 {
				if (UseAbility()) return true;
			}




			if (phase == 1 || phase == 3) {
				if (blastcooldown > 0) blastcooldown--;
				if (barriercooldown > 0) barriercooldown--;
				if (volcanocooldown > 0) volcanocooldown--;
				if (summoncooldown > 0) summoncooldown--;
				if (restorecooldown >0) restorecooldown--;
			}
		}
		return super.act();
	}


	private boolean UseAbility() {
		// 화염파 > 리틀 폼페이 소환 > 용암 장갑 > 화산분출 순으로 사용합니다.

		//화염파
		if (FireBlast()){

			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "tentacle"));
			return true;
		}

		//용암장갑
		if (barriercooldown <= 0) {
			Buff.affect(this, Barrier.class).setShield(15);
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "armad"));
			CellEmitter.center(pos).burst(FlameParticle.FACTORY, 4);
			Sample.INSTANCE.play(Assets.Sounds.BURNING, 2f);
			barriercooldown = 25;
			return true;
		}


		//화산폭발
		if (volcanocooldown <= 0) {
			PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 3);
			if (volcanotime < 3) {
				sprite.parent.addToBack(new TargetedCell(pos, 0xFF00FF));

				if (volcanotime == 0 || volcanotime == 2)
					for (int i = 0; i < PathFinder.distance.length; i++) {
						if (PathFinder.distance[i] < Integer.MAX_VALUE) {
							int vol = Fire.volumeAt(i, Fire.class);
							if (vol < 4){
								sprite.parent.addToBack(new TargetedCell( i, 0xFF00FF));
							}
						}}
				volcanotime+=1;
				spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2*TICK));
				return true;
			}
			else {
				boolean isHit = false;
				for (int i = 0; i < PathFinder.distance.length; i++) {
					if (PathFinder.distance[i] < Integer.MAX_VALUE) {
						Char ch = Actor.findChar(i);
						int vol = Fire.volumeAt(i, Fire.class);
						if (vol < 4){
							CellEmitter.center(i).start( Speck.factory(Speck.FORGE), 0.1f, 3 );
						}
						if (ch != null && !isHit) {
							if ((ch.alignment != alignment || ch instanceof Bee)) {
								 ch.damage(Random.NormalIntRange(13, 15), new DM201.Volcano());
								if (ch.isAlive()) Buff.affect(ch, Paralysis.class, 2f);

								isHit = true;
							}}
					}}}

			Camera.main.shake(2, 0.5f);
			Sample.INSTANCE.play(Assets.Sounds.LIGHTNING, 2f);
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "hamon"));

			volcanotime=0;
			volcanocooldown= 10;
			spend(TICK);
			return true;
		}


		return false;
	}

	public class Blast { }
	public class Volcano { }

	private ArrayList<Integer> targetedCells = new ArrayList<>();
	private boolean FireBlast() {
		boolean terrainAffected = false;
		HashSet<Char> affected = new HashSet<>();
		//delay fire on a rooted hero
		if (!Dungeon.hero.rooted) {
			for (int i : targetedCells) {
				Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
				//shoot beams
				for (int p : b.path) {
					CellEmitter.center(p).burst(WoolParticle.FACTORY, 11);
					Char ch = Actor.findChar(p);
					if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
						affected.add(ch);
					}
					if (Dungeon.level.flamable[p]) {
						Dungeon.level.destroy(p);
						GameScene.updateMap(p);
						terrainAffected = true;
					}
				}

				Sample.INSTANCE.play( Assets.Sounds.HIT_STRONG, 2, Random.Float(3.3f, 3.3f) );
				Camera.main.shake(2, 0.5f);
			}
			if (terrainAffected) {
				Dungeon.observe();
			}
			for (Char ch : affected) {
			ch.damage(Random.NormalIntRange(25, 30), new DM201.Blast());

				if (Dungeon.level.heroFOV[pos]) {
					ch.sprite.flash();
					CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
				}
				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail(getClass());
					GLog.n(Messages.get(Char.class, "kill", name()));
				}
			}
			targetedCells.clear();
		}

		if (blastcooldown <= 0){

			int beams = 3;
			HashSet<Integer> affectedCells = new HashSet<>();
			for (int i = 0; i < beams; i++){

				int targetPos = Dungeon.hero.pos;
				if (i != 0){
					do {
						targetPos = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
					} while (Dungeon.level.trueDistance(pos, Dungeon.hero.pos)
							> Dungeon.level.trueDistance(pos, targetPos));
				}
				targetedCells.add(targetPos);
				Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
				affectedCells.addAll(b.path);
			}

			//remove one beam if multiple shots would cause every cell next to the hero to be targeted
			boolean allAdjTargeted = true;
			for (int i : PathFinder.NEIGHBOURS9){
				if (!affectedCells.contains(Dungeon.hero.pos + i) && Dungeon.level.passable[Dungeon.hero.pos + i]){
					allAdjTargeted = false;
					break;
				}
			}
			if (allAdjTargeted){
				targetedCells.remove(targetedCells.size()-1);
			}
			for (int i : targetedCells){
				Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
				for (int p : b.path){
					sprite.parent.add(new TargetedCell(p, 0xFF00FF));
					affectedCells.add(p);
				}
			}

			//don't want to overly punish players with slow move or attack speed
			Dungeon.hero.interrupt();


			 blastcooldown = 6;

			spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2*TICK));
			return true;

		}
		return false;
	}

	@Override
	public void notice() {


			super.notice();
			if (!BossHealthBar.isAssigned()) {
				BossHealthBar.assignBoss(this);
				for (Char ch : Actor.chars()){
				}
			}

		summoncooldown = 1;
		blastcooldown = 1;
		barriercooldown = 4;
		volcanocooldown = 10;
		restorecooldown = 10;

	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		Dungeon.level.drop( new StoneOfEnchantment().identify(), pos ).sprite.drop( pos );

		GLog.p(Messages.get(this, "defeated"));

	}

	private static final String PHASE   = "phase";
	private static final String BLAST_CD   = "blastcooldown";
	private static final String BARRIER_CD  = "barriercooldown";
	private static final String VOCAL_CD   = "volcanocooldown";
	private static final String VOCAL_TIME   = "volcanotime";
	private static final String SUMMON_CD   = "summoncooldown";
	private static final String RESTORE_CD   = "restorecooldown";
	private static final String TARGETED_CELLS = "targeted_cells";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( PHASE, phase );
		bundle.put( BLAST_CD, blastcooldown );
		bundle.put( BARRIER_CD, barriercooldown );
		bundle.put( VOCAL_CD, volcanocooldown );
		bundle.put( VOCAL_TIME, volcanotime );
		bundle.put( SUMMON_CD, summoncooldown);
		bundle.put( RESTORE_CD, restorecooldown);

		int[] bundleArr = new int[targetedCells.size()];
		for (int i = 0; i < targetedCells.size(); i++){
			bundleArr[i] = targetedCells.get(i);
		}
		bundle.put(TARGETED_CELLS, bundleArr);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		phase = bundle.getInt(PHASE);
		blastcooldown = bundle.getInt(BLAST_CD);
		barriercooldown = bundle.getInt(BARRIER_CD);
		summoncooldown = bundle.getInt(SUMMON_CD);
		volcanocooldown = bundle.getInt(VOCAL_CD);
		volcanotime = bundle.getInt(VOCAL_TIME);
		restorecooldown = bundle.getInt(RESTORE_CD);

		for (int i : bundle.getIntArray(TARGETED_CELLS)){
			targetedCells.add(i);
		}

	}


	public static class RestorBuff extends FlavourBuff {
		{
			immunities.add(ToxicGas.class);
			immunities.add(CorrosiveGas.class);
		}
		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add(CharSprite.State.SHIELDED);
			else target.sprite.remove(CharSprite.State.SHIELDED);
		}
	}
}
