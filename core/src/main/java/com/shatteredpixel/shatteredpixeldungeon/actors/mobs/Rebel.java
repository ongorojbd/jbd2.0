package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Dominion;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPassage;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.LabsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RebelSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class Rebel extends Mob {
	{
		spriteClass = RebelSprite.class;

		HP = HT = 1800;
		defenseSkill = 25;
		viewDistance = 10;

		EXP = 0;
		maxLvl = 30;

		baseSpeed = 1.7f;

		properties.add(Property.BOSS);
		properties.add(Property.DEMONIC);
		immunities.add(Terror.class);
		immunities.add(Dread.class );
		immunities.add(Sleep.class );
		immunities.add(Amok.class );
		immunities.add(Blindness.class );
		immunities.add(MagicalSleep.class );
	}

	int cleanCooldown = (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) ? 15 : 31;
	int damageTaken = 0;
	int summonCooldown = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 29 : 39;

	private int LastPos = -1;
	private int Burstcooldown = 0; // 1이 되면 은신 파괴
	public int  Phase = 0; // 1~6까지
	private int GasCoolDown = 15;
	private int ACoolDown = 29;
	private int BurstTime = 0;
	private int Burstpos = -1;
	private static final Rect arena = new Rect(0, 0, 33, 26);
	private static final int bottomDoor = 16 + (arena.bottom+1) * 33;

	private float abilityCooldown;

	public boolean isDied = false;
	private static boolean telling_1 = false;
	private static boolean telling_2 = false;
	private static boolean telling_3 = false;
	private static boolean telling_4 = false;
	private static boolean telling_5 = false;

	private static final String ISDIED = "isdied";
	private static final String TELLING_1 = "telling_1";
	private static final String TELLING_2 = "telling_2";
	private static final String TELLING_3 = "telling_3";
	private static final String TELLING_4 = "telling_4";
	private static final String TELLING_5 = "telling_5";
	private static final String CLEAN_COOLDOWN = "cleancooldown";
	private static final String DMGTAKEN = "damagetaken";
	private static final String SKILL2TIME   = "BurstTime";
	private static final String SKILL2TPOS   = "Burstpos";
	private static final String SKILL2TCD   = "ACoolDown";
	private static final String SKILL3TCD   = "GasCoolDown";
	private static final String SUMMON_COOLDOWN = "summoncooldown";
	private static final String BURST = "Burstcooldown";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(ISDIED, isDied);
		bundle.put(TELLING_1, telling_1);
		bundle.put(TELLING_2, telling_2);
		bundle.put(TELLING_3, telling_3);
		bundle.put(TELLING_4, telling_4);
		bundle.put(TELLING_5, telling_5);
		bundle.put(CLEAN_COOLDOWN, cleanCooldown);
		bundle.put(DMGTAKEN, damageTaken);
		bundle.put( PHASE, Phase );
		bundle.put( SKILLPOS, LastPos );
		bundle.put( SKILL2TIME, BurstTime );
		bundle.put( SKILL2TCD, ACoolDown );
		bundle.put( SKILL2TPOS, Burstpos );
		bundle.put( SKILL3TCD, GasCoolDown );
		bundle.put(SUMMON_COOLDOWN, summonCooldown);
		bundle.put( BURST, Burstcooldown);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		isDied = bundle.getBoolean( ISDIED );
		telling_1 = bundle.getBoolean( TELLING_1 );
		telling_2 = bundle.getBoolean( TELLING_2 );
		telling_3 = bundle.getBoolean( TELLING_3 );
		telling_4 = bundle.getBoolean( TELLING_4 );
		telling_5 = bundle.getBoolean( TELLING_5 );
		cleanCooldown = bundle.getInt( CLEAN_COOLDOWN );
		damageTaken = bundle.getInt( DMGTAKEN );
		Phase = bundle.getInt(PHASE);
		LastPos = bundle.getInt(SKILLPOS);
		BurstTime = bundle.getInt(SKILL2TIME);
		ACoolDown = bundle.getInt(SKILL2TCD);
		Burstpos = bundle.getInt(SKILL2TPOS);
		GasCoolDown = bundle.getInt(SKILL3TCD);
		summonCooldown = bundle.getInt( SUMMON_COOLDOWN );
		Burstcooldown = bundle.getInt(BURST);
	}

	public boolean isDied() {
		return isDied;
	}

	@Override
	public int damageRoll() {
		int dmg;
		if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
			dmg = Random.NormalIntRange( 40, 65 );
		} else {
			dmg = Random.NormalIntRange( 35, 55 );
		}
		return dmg;
	}

	@Override
	public int attackSkill( Char target ) {
		return 55;
	}

	@Override
	public int drRoll() {
		int dr;
		if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
			dr = Random.NormalIntRange(9, 19);
		} else {
			dr = Random.NormalIntRange(5, 15);
		}
		return dr;
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return true;
	}

	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			switch(Dungeon.hero.heroClass){
				case WARRIOR:
					this.yell(Messages.get(this, "notice"));
					break;
				case ROGUE:
					this.yell(Messages.get(this, "notice2"));
					break;
				case MAGE:
					GLog.p(Messages.get(Val.class, "6"));
					this.yell(Messages.get(this, "notice3"));
					break;
				case HUNTRESS:
					this.yell(Messages.get(this, "notice4"));
					break;
			}
		}
	}

	@Override
	protected boolean act() {
		summonCooldown--;
		cleanCooldown--;

		if (summonCooldown <= 0 && Dungeon.level instanceof LabsBossLevel) {
			Newgenkaku Newgenkaku = new Newgenkaku();
			Newgenkaku.state = Newgenkaku.HUNTING;
			Newgenkaku.pos = bottomDoor-26*33;
			GameScene.add( Newgenkaku );
			Newgenkaku.beckon(Dungeon.hero.pos);

			Newcmoon Newcmoon = new Newcmoon();
			Newcmoon.state = Newcmoon.HUNTING;
			Newcmoon.pos = bottomDoor-26*33;
			GameScene.add( Newcmoon );
			Newcmoon.beckon(Dungeon.hero.pos);

			Mih Mih = new Mih();
			Mih.state = Mih.HUNTING;
			Mih.pos = bottomDoor-26*33;
			GameScene.add( Mih );
			Mih.beckon(Dungeon.hero.pos);

			GLog.w(Messages.get(Rebel.class, "summon"));

			summonCooldown = (39);

			new Flare( 5, 32 ).color( 0xFFFFFF, true ).show( this.sprite, 3f );
			Sample.INSTANCE.play(Assets.Sounds.CRAZYDIO);
		}
		else if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {

			if (summonCooldown <= 1 && Dungeon.level instanceof LabsBossLevel) {

				Kousaku Kousaku = new Kousaku();
				Kousaku.state = Kousaku.HUNTING;
				Kousaku.pos = bottomDoor-26*33;
				GameScene.add( Kousaku );
				Kousaku.beckon(Dungeon.hero.pos);

				Cmoon Cmoon = new Cmoon();
				Cmoon.state = Cmoon.HUNTING;
				Cmoon.pos = bottomDoor-26*33;
				GameScene.add( Cmoon );
				Cmoon.beckon(Dungeon.hero.pos);

				Genkaku Genkaku = new Genkaku();
				Genkaku.state = Genkaku.HUNTING;
				Genkaku.pos = bottomDoor-26*33;
				GameScene.add( Genkaku );
				Genkaku.beckon(Dungeon.hero.pos);

				GLog.w(Messages.get(Rebel.class, "summon"));

			summonCooldown = (37);

				new Flare( 5, 32 ).color( 0xFFFFFF, true ).show( this.sprite, 3f );
				Sample.INSTANCE.play(Assets.Sounds.CRAZYDIO);

			}
		}
		if (cleanCooldown <= 0) {
			Sample.INSTANCE.play(Assets.Sounds.CHARMS, 1, 1);
			GameScene.flash(0xFFFF00);
			GLog.w(Messages.get(Rebel.class, "cleaning"));
			for (int i = 0; i < 1122; i++) {
				if (Dungeon.level.map[i] == Terrain.BARRICADE
						|| Dungeon.level.map[i] == Terrain.HIGH_GRASS
						|| Dungeon.level.map[i] == Terrain.GRASS
						|| Dungeon.level.map[i] == Terrain.FURROWED_GRASS
						|| Dungeon.level.map[i] == Terrain.EMBERS
						|| Dungeon.level.map[i] == Terrain.WATER  ) {
					Level.set(i, Terrain.EMPTY);
					GameScene.updateMap(i);
				}
			}

			cleanCooldown = (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) ? 15 : 31;
		}

		if (!UseAbility()) {
			return true; }

		if (ACoolDown > 0) ACoolDown--;
		if (GasCoolDown > 0) GasCoolDown--;

		if (abilityCooldown > 0) abilityCooldown--;

		//extra fast abilities and summons at the final 100 HP

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
		if (dmg >= 150){
			//takes 20/21/22/23/24/25/26/27/28/29/30 dmg
			// at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
			dmg = 150;
		}

		int preHP = HP;
		int dmgTaken = preHP - HP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(dmgTaken);

		super.damage(dmg, src);

		if (Phase==0 && HP < 1500) {
			Phase = 1;
			GameScene.flash(0x8B00FF);
			Buff.prolong(this, Haste.class, Haste.DURATION*5000f);
			yell(Messages.get(this, "telling_1"));
			sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);
			BossHealthBar.assignBoss(this);

			Dvdol Dvdol = new Dvdol();
			Dvdol.state = Dvdol.HUNTING;
			Dvdol.pos = bottomDoor-26*33;
			GameScene.add( Dvdol );
			Dvdol.beckon(Dungeon.hero.pos);
		}
		else if (Phase==1 && HP < 1200) {
			Phase = 2;
			GameScene.flash(0x8B00FF);
			Buff.prolong(this, Bless.class, Bless.DURATION*5000f);
			yell(Messages.get(this, "telling_2"));

			WO WO = new WO();
			WO.state = WO.HUNTING;
			WO.pos = bottomDoor-10*33;
			GameScene.add( WO );
			WO.beckon(Dungeon.hero.pos);

			Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
			sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);
			BossHealthBar.assignBoss(this);

		}
		else if (Phase==2 && HP < 900) {
			Phase = 3;
			GameScene.flash(0x8B00FF);
			Buff.prolong(this, BlobImmunity.class, BlobImmunity.DURATION*5000f);
			Buff.detach(this, Doom.class);
			Buff.affect(Dungeon.hero, MindVision.class, 3f);
			immunities.add(Doom.class );
			immunities.add(Grim.class );

			GLog.n(Messages.get(Rebel.class, "val"));

			Pucci Pucci = new Pucci();
			Pucci.state = Pucci.WANDERING;
			Pucci.pos = bottomDoor-9*33;
			GameScene.add( Pucci );
			Pucci.beckon(Dungeon.hero.pos);

			sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);


			yell(Messages.get(this, "telling_3"));

			Music.INSTANCE.play(Assets.Music.HEAVENDIO, true);
			BossHealthBar.assignBoss(this);

		}
		else if (Phase==3 && HP < 600) {
			Phase = 4;
			GameScene.flash(0x8B00FF);
			Buff.prolong(this, Stamina.class, Stamina.DURATION*5000f);
			yell(Messages.get(this, "telling_4"));
			immunities.add(Doom.class );
			immunities.add(Grim.class );

			jojo jojo = new jojo();
			jojo.state = jojo.WANDERING;
			jojo.pos = hero.pos;
			GameScene.add( jojo );
			jojo.beckon(Dungeon.hero.pos);

			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
			sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);
			BossHealthBar.assignBoss(this);
		}
		else if (Phase==4 && HP < 300) {
			Phase = 5;
			Buff.prolong(this, MagicImmune.class, MagicImmune.DURATION*5000f);
			Buff.affect(Dungeon.hero, Blindness.class, 30f);
			immunities.add(Doom.class );
			immunities.add(Grim.class );
			this.sprite.add(CharSprite.State.ILLUMINATED);

			GameScene.flash(0x8B00FF);
			yell(Messages.get(this, "telling_5"));

			sprite.centerEmitter().start(Speck.factory(Speck.UP), 0.4f, 2);

			GLog.h(Messages.get(Rebel.class, "blood"));
			Music.INSTANCE.play(Assets.Music.DIOLOWHP, true);
			BossHealthBar.assignBoss(this);
		}

	}

	private static final String PHASE   = "Phase";
	private static final String SKILLPOS   = "LastPos";

	@Override
	public void die(Object cause) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof WO || mob instanceof Newgenkaku || mob instanceof Newcmoon || mob instanceof Mih || mob instanceof Kousaku || mob instanceof Cmoon || mob instanceof Genkaku || mob instanceof Pucci || mob instanceof Dvdol) {
				mob.die( cause );
			}
		}

		Sample.INSTANCE.play( Assets.Sounds.BLAST, 2, Random.Float(0.33f, 0.66f) );
		Camera.main.shake(31, 3f);

		super.die(cause);
		//Badges.validateBossSlain();
		GameScene.bossSlain();
		Dungeon.level.unseal();

		Statistics.yorihimes++;
		Badges.validateYorihimes();

		if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
			Badges.validateBossChallengeCompleted();
		} else {
			Statistics.qualifiedForBossChallengeBadge = false;
		}

		PotionOfHealing.cure(hero);
		PotionOfHealing.heal(hero);

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
		}

		if (Random.Int( 15 ) == 0) {
			Dungeon.level.drop( new AdvancedEvolution().identify(), pos ).sprite.drop( pos );
			new Flare( 5, 32 ).color( 0xFFFF00, true ).show( hero.sprite, 2f );
			Sample.INSTANCE.play(Assets.Sounds.BADGE);
			GLog.p(Messages.get(Kawasiri.class, "rare"));
		}

		yell( Messages.get(this, "defeated") );

		Sample.INSTANCE.play( Assets.Sounds.NANI );

		isDied = true;
	}


	@Override
	public int defenseProc( Char enemy, int damage ) {
		damage = super.defenseProc( enemy, damage );

		damageTaken += damage;

		int newPos;

		LabsBossLevel level = (LabsBossLevel) Dungeon.level;
		if (Dungeon.level instanceof LabsBossLevel) {
			if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
				if (Random.Int(3) == 0) {
					do {
						newPos = level.randomCellPos();
					} while (level.map[newPos] == Terrain.WALL || Actor.findChar(newPos) != null);

					if (level.heroFOV[pos]) CellEmitter.get( pos ).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

					sprite.move( pos, newPos );
					move( newPos );

					if (level.heroFOV[newPos]) CellEmitter.get( newPos ).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
					Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
					GameScene.flash( 0x333333 );
					Sample.INSTANCE.play( Assets.Sounds.HAHAH );
					Buff.affect(Dungeon.hero, Blindness.class, 3f);
				}
			} else {
				if (Random.Int(5) == 0) {
					do {
						newPos = level.randomCellPos();
					} while (level.map[newPos] == Terrain.WALL || Actor.findChar(newPos) != null);

					if (level.heroFOV[pos]) CellEmitter.get( pos ).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

					sprite.move( pos, newPos );
					move( newPos );

					if (level.heroFOV[newPos]) CellEmitter.get( newPos ).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
					Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
					Sample.INSTANCE.play( Assets.Sounds.HAHAH );

				}
			}
		}
		return damage;
	}

	private boolean UseAbility() {
		// 폭발 > 국가 순
		if (enemy == null) return true;
		//폭발
		if (ACoolDown <= 0) {
			if (Burstpos == -1) {
				// 위치 미지정시, 이번 턴에는 폭발을 일으킬 지점을 정합니다.
				Burstpos = Dungeon.hero.pos;
				sprite.parent.addToBack(new TargetedCell(Burstpos, 0xFF00FF));
				GLog.w(Messages.get(Rebel.class, "skill1"));

				for (int i : PathFinder.NEIGHBOURS9) {
					int vol = Fire.volumeAt(Burstpos+i, Fire.class);
					if (vol < 4){
						sprite.parent.addToBack(new TargetedCell(Burstpos + i, 0xFF00FF));
					}
				}

				sprite.centerEmitter().start(Speck.factory(Speck.MASK), 0.05f, 20);
				spend(2f);
				Sample.INSTANCE.play( Assets.Sounds.OH );
				BurstTime++;

				return false;
			}
			else if (BurstTime == 1) {

				PathFinder.buildDistanceMap(Burstpos, BArray.not(Dungeon.level.solid, null), 1);
				for (int cell = 0; cell < PathFinder.distance.length; cell++) {
					if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
						CellEmitter.get(cell).burst(SparkParticle.FACTORY, 31);
						CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
						Char ch = Actor.findChar(cell);
						if (hit( this, enemy, true )) {
							if (ch != null&& !(ch instanceof Rebel)) {
								ch.damage(Random.NormalIntRange(55, 70), this);
							}
						}
					}}
				Camera.main.shake(9, 0.5f);
				Burstpos = -1;
				BurstTime = 0;
				ACoolDown = Random.NormalIntRange(5,8);
				if (Phase == 5) ACoolDown = Random.NormalIntRange(1,5);

				Sample.INSTANCE.play( Assets.Sounds.BLAST, 1.5f, 0.67f );

				return true;
			}
		}

		if (GasCoolDown <= 0) {
			ThorwGas(enemy);
			return true;
		}

		return true;
	}

	public void ThorwGas(Char target) {
		Dungeon.hero.interrupt();
		GameScene.add(Blob.seed(target.pos, 250, Dominion.class));
		GLog.w(Messages.get(Rebel.class, "skill2"));
		GasCoolDown = 10;

	}

}
