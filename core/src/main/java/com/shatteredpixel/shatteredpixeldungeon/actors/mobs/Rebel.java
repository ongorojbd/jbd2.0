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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RebelSprite;
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

		HP = HT = 1800;
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


	private static boolean telling_1 = false;
	private static boolean telling_2 = false;
	private static boolean telling_3 = false;
	private static boolean telling_4 = false;
	private static boolean telling_5 = false;

	private static final String TELLING_1 = "telling_1";
	private static final String TELLING_2 = "telling_2";
	private static final String TELLING_3 = "telling_3";
	private static final String TELLING_4 = "telling_4";
	private static final String TELLING_5 = "telling_5";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(TELLING_1, telling_1);
		bundle.put(TELLING_2, telling_2);
		bundle.put(TELLING_3, telling_3);
		bundle.put(TELLING_4, telling_4);
		bundle.put(TELLING_5, telling_5);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		telling_1 = bundle.getBoolean( TELLING_1 );
		telling_2 = bundle.getBoolean( TELLING_2 );
		telling_3 = bundle.getBoolean( TELLING_3 );
		telling_4 = bundle.getBoolean( TELLING_4 );
		telling_5 = bundle.getBoolean( TELLING_5 );
	}


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
		return Random.NormalIntRange( 5, 33 );
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

		if (this.HP < 5*this.HT/6 && !telling_1) {
			yell(Messages.get(this, "telling_1"));
			telling_1 = true;
		}
		if (this.HP < 4*this.HT/6 && !telling_2) {
			yell(Messages.get(this, "telling_2"));
			telling_2 = true;
		}
		if (this.HP < 3*this.HT/6 && !telling_3) {
			yell(Messages.get(this, "telling_3"));
			telling_3 = true;
		}
		if (this.HP < 2*this.HT/6 && !telling_4) {
			yell(Messages.get(this, "telling_4"));
			telling_4 = true;
		}
		if (this.HP < this.HT/6 && !telling_5) {
			yell(Messages.get(this, "telling_5"));
			telling_5 = true;
		}

		if (firstSummon == true) {
			if (SummonTurn == 0) {
				this.yell(Messages.get(this, "summon"));
				SummontPos = Dungeon.hero.pos;
				SummonTurn++;}
			else if (SummonTurn > 0) {
				Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

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

				//END


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
		LabsBossLevel level = (LabsBossLevel) Dungeon.level;
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
