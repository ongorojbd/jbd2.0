package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LarvaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RebelSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Rebel extends Mob {
	{
		spriteClass = RebelSprite.class;

		HP = HT = (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) ? 2000 : 1700;
		EXP = 100;
		maxLvl = 30;
		defenseSkill = 25;
		viewDistance = 12;

		properties.add(Property.BOSS);
		properties.add(Property.DEMONIC);
		immunities.add(Terror.class);
		immunities.add(Dread.class );
		immunities.add(Sleep.class );
		immunities.add(Amok.class );
		immunities.add(Blindness.class );
		immunities.add(MagicalSleep.class );
	}
	private boolean firstSummon = true;
	private int SummontPos = 0;
	private int SummonTurn = 0; // 1이 되면 소환
	private int Burstcooldown = 0; // 1이 되면 은신 파괴
	private int phase = 0;
	private int blinkCooldown = 0;

	private float abilityCooldown;
	private static final int MIN_ABILITY_CD = 10;
	private static final int MAX_ABILITY_CD = 15;

	private float summonCooldown;
	private static final int MIN_SUMMON_CD = 10;
	private static final int MAX_SUMMON_CD = 15;

	private ArrayList<Class> fistSummons = new ArrayList<>();

	private ArrayList<Class> challengeSummons = new ArrayList<>();

	private ArrayList<Class> regularSummons = new ArrayList<>();

	private ArrayList<Integer> targetedCells = new ArrayList<>();




	@Override
	public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
		if (enemy == null) return false;

		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

		if (enemy.isInvulnerable(getClass())) {

			if (visibleFight) {
				enemy.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));

				Sample.INSTANCE.play(Assets.Sounds.HIT, 1f, Random.Float(0.96f, 1.05f));
			}

			return false;
		}
		else if (hit( this, enemy, true )) {

			int dmg = damageRoll();
			enemy.damage( dmg, this );
			enemy.sprite.bloodBurstA( sprite.center(), dmg );
			enemy.sprite.flash();

			if (Dungeon.level.heroFOV[pos]) Sample.INSTANCE.play(Assets.Sounds.HIT, 1.35f, Random.Float(0.65f, 1.76f));

			if (enemy == Dungeon.hero && !enemy.isAlive()) {
				Dungeon.fail( getClass() );
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}

		return true;

	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 5, 37 );
	}

	@Override
	public int attackSkill( Char target ) {
		return (Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && Random.Int(10) == 0) ? 70 : 35;
	}

	@Override
	public int drRoll() {
		int dr;
		if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
			dr = Random.NormalIntRange(10, 30);
		} else {
			dr = Random.NormalIntRange(5, 30);
		}
		return dr;
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (this.fieldOfView[enemy.pos]){ return true; }
		return false;
	}


	@Override
	protected boolean getCloser( int target ) {
		if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && blinkCooldown <= 0) {

			blink( target );
			spend( -1 / speed() );
			return true;

		} else {

			blinkCooldown--;
			return super.getCloser( target );

		}
	}

	private void blink( int target ) {

		Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
		int cell = route.collisionPos;

		//can't occupy the same cell as another char, so move back one.
		if (Actor.findChar( cell ) != null && cell != this.pos)
			cell = route.path.get(route.dist-1);

		if (Dungeon.level.avoid[ cell ] && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])){
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8) {
				cell = route.collisionPos + n;
				if (Dungeon.level.passable[cell]
						&& Actor.findChar( cell ) == null
						&& (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])) {
					candidates.add( cell );
				}
			}
			if (candidates.size() > 0)
				cell = Random.element(candidates);
			else {
				blinkCooldown = Random.IntRange(4, 6);
				return;
			}
		}

		ScrollOfTeleportation.appear( this, cell );

		blinkCooldown = Random.IntRange(4, 6);
	}

	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			for (Char ch : Actor.chars()){
				if (ch instanceof DriedRose.GhostHero){
					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
		}
	}

	@Override
	protected boolean act() {
		if (firstSummon == true) {
			if (SummonTurn == 0) {
				this.yell(Messages.get(this, "summon"));
				SummontPos = Dungeon.hero.pos;
				SummonTurn++;}
			else if (SummonTurn > 0) {
				Sample.INSTANCE.play(Assets.Sounds.TOMB);

				if (SummontPos == Dungeon.hero.pos) {
					Dungeon.hero.damage(Dungeon.hero.HP/2, this);
					Dungeon.hero.sprite.burst(CharSprite.NEGATIVE, 10);

					Camera.main.shake(9, 0.9f);
				}
				WO ter = new WO();
				ter.pos = 537;
				GameScene.add(ter);

				GameScene.flash(0x8B00FF);

				ter.beckon( Dungeon.hero.pos );

				firstSummon = false;

				if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
					fieldOfView = new boolean[Dungeon.level.length()];
				}
				Dungeon.level.updateFieldOfView( this, fieldOfView );

				throwItems();

				//mob logic
				enemy = chooseEnemy();

				enemySeen = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
				//end of char/mob logic

				if (phase == 0){
					if (Dungeon.hero.viewDistance >= Dungeon.level.distance(pos, Dungeon.hero.pos)) {
						Dungeon.observe();
					}
					if (Dungeon.level.heroFOV[pos]) {
						notice();
					}
				}

				if (phase == 4 && findFist() == null){
					yell(Messages.get(this, "hope"));
					summonCooldown = -15; //summon a burst of minions!
					phase = 5;
				}

				if (phase == 0){
					spend(TICK);
					return true;
				} else {

					boolean terrainAffected = false;
					HashSet<Char> affected = new HashSet<>();
					//delay fire on a rooted hero
					if (!Dungeon.hero.rooted) {
						for (int i : targetedCells) {
							Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
							//shoot beams
							sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
							for (int p : b.path) {
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
						}
						if (terrainAffected) {
							Dungeon.observe();
						}
						for (Char ch : affected) {

							if (hit( this, ch, true )) {
								if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
									ch.damage(Random.NormalIntRange(30, 50), new Eye.DeathGaze());
								} else {
									ch.damage(Random.NormalIntRange(20, 30), new Eye.DeathGaze());
								}
								if (ch == Dungeon.hero) {
									Statistics.bossScores[4] -= 500;
								}
								if (Dungeon.level.heroFOV[pos]) {
									ch.sprite.flash();
									CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
								}
								if (!ch.isAlive() && ch == Dungeon.hero) {
									Badges.validateDeathFromEnemyMagic();
									Dungeon.fail(getClass());
									GLog.n(Messages.get(Char.class, "kill", name()));
								}
							} else {
								ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
							}
						}
						targetedCells.clear();
					}

					if (abilityCooldown <= 0){

						int beams = 1 + (HT - HP)/400;
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
								sprite.parent.add(new TargetedCell(p, 0xFF0000));
								affectedCells.add(p);
							}
						}

						//don't want to overly punish players with slow move or attack speed
						spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 3*TICK));
						Dungeon.hero.interrupt();

						abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
						abilityCooldown -= (phase - 1);

					} else {
						spend(TICK);
					}

					while (summonCooldown <= 0){

						Class<?extends Mob> cls = regularSummons.remove(0);
						Mob summon = Reflection.newInstance(cls);
						regularSummons.add(cls);

						int spawnPos = -1;
						for (int i : PathFinder.NEIGHBOURS8){
							if (Actor.findChar(pos+i) == null){
								if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, pos+i)){
									spawnPos = pos + i;
								}
							}
						}

						//if no other valid spawn spots exist, try to kill an adjacent sheep to spawn anyway
						if (spawnPos == -1){
							for (int i : PathFinder.NEIGHBOURS8){
								if (Actor.findChar(pos+i) instanceof Sheep){
									if (spawnPos == -1 || Dungeon.level.trueDistance(Dungeon.hero.pos, spawnPos) > Dungeon.level.trueDistance(Dungeon.hero.pos, pos+i)){
										spawnPos = pos + i;
									}
								}
							}
							if (spawnPos != -1){
								Actor.findChar(spawnPos).die(null);
							}
						}

						if (spawnPos != -1) {
							summon.pos = spawnPos;
							GameScene.add( summon );
							Actor.addDelayed( new Pushing( summon, pos, summon.pos ), -1 );
							summon.beckon(Dungeon.hero.pos);

							summonCooldown += Random.NormalFloat(MIN_SUMMON_CD, MAX_SUMMON_CD);
							summonCooldown -= (phase - 1);
							if (findFist() != null){
								summonCooldown += MIN_SUMMON_CD - (phase - 1);
							}
						} else {
							break;
						}
					}
			}


		}

			if (summonCooldown > 0) summonCooldown--;
			if (abilityCooldown > 0) abilityCooldown--;

			//extra fast abilities and summons at the final 100 HP
			if (phase == 5 && abilityCooldown > 2){
				abilityCooldown = 2;
			}
			if (phase == 5 && summonCooldown > 3){
				summonCooldown = 3;
			}

			return true;
		}



		if (Dungeon.hero.buff(Invisibility.class) != null) {
			if (Burstcooldown == 0) Burstcooldown++;
			else {
				Burstcooldown = 0;
				this.yell(Messages.get(this, "burst"));
				Buff.detach(Dungeon.hero, Invisibility.class);
			}
		}
		else Burstcooldown = 0;
		return super.act();
	}

	@Override
	public void damage(int dmg, Object src) {
		if (dmg >= 250){
			//takes 20/21/22/23/24/25/26/27/28/29/30 dmg
			// at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
			dmg = 250;
		}

		int preHP = HP;
		int dmgTaken = preHP - HP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(dmgTaken);

		super.damage(dmg, src);
	}

	@Override
	public void beckon( int cell ) {
	}


	private YogFist findFist(){
		for ( Char c : Actor.chars() ){
			if (c instanceof YogFist){
				return (YogFist) c;
			}
		}
		return null;
	}

	private static final String PHASE = "phase";

	private static final String ABILITY_CD = "ability_cd";
	private static final String SUMMON_CD = "summon_cd";

	private static final String FIST_SUMMONS = "fist_summons";
	private static final String REGULAR_SUMMONS = "regular_summons";
	private static final String CHALLENGE_SUMMONS = "challenges_summons";

	private static final String TARGETED_CELLS = "targeted_cells";

	private static final String FIRST_SUMMON = "first_summon";
	private static final String TURN = "SummonTurn";
	private static final String POS = "SummontPos";
	private static final String BURST = "Burstcooldown";


	@Override
	public void die(Object cause) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof WO || mob instanceof Soldier || mob instanceof Tank || mob instanceof Researcher || mob instanceof Supression || mob instanceof Medic || mob instanceof SWAT) {
				mob.die( cause );
			}
		}

		super.die(cause);
		//Badges.validateBossSlain(); //TODO 뱃지 추가 필요
		GameScene.bossSlain();
		Dungeon.level.unseal();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
		}

		yell( Messages.get(this, "defeated") );
	}


	@Override
	public int defenseProc(Char enemy, int damage) {
		if (this.buff(Barkskin.class) == null) {
			if (Dungeon.hero.belongings.weapon() instanceof MeleeWeapon) {
				if (Random.Int(7) == 0) {
					new WarpingTrap().set(target).activate();
				}
			}
		}
		return super.defenseProc(enemy, damage);
	}












}
