package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Beta;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TurretSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A turret bolted to the ambulance's deck in HospitalLevel. It carries one wand and fires that
 * wand's effect at the nearest enemy in range, once every FIRE_INTERVAL turns. Indestructible
 * scenery otherwise - it never moves under its own power, it rides along because
 * HospitalLevel.stepForward() shoves everything on the deck forward.
 */
public class AmbulanceTurret extends NPC {

	public static final int RANGE = 6;
	private static final int FIRE_INTERVAL = 2;

	//the wand is always mounted at this level, regardless of what the hero carries
	public static final int WAND_LEVEL = 5;

	//how many wands the pick-a-wand window offers. The window lays itself out from the number of
	//choices it is handed, so this is the only place to change it.
	public static final int WAND_CHOICES = 2;

	//shield a transfusion turret hands out, and the wand levels a warding turret lends
	private static final int SHIELD = 15;
	private static final int WARDING_BONUS = 2;

	//Wands that fire their own effect at whatever is in range. Living Earth is in here: its armor
	//and guardian go to curUser, i.e. the hero, which is a fine thing for a turret to do. Left
	//out entirely is Regrowth (does no damage); Transfusion and Warding get their own behaviour
	//below instead, since Transfusion's own effect would bleed the hero through curUser.
	@SuppressWarnings("unchecked")
	private static final Class<? extends Wand>[] ATTACK_POOL = new Class[]{
			WandOfMagicMissile.class,
			WandOfLightning.class,
			WandOfDisintegration.class,
			WandOfFireblast.class,
			WandOfCorrosion.class,
			WandOfBlastWave.class,
			WandOfFrost.class,
			WandOfPrismaticLight.class,
			WandOfCorruption.class,
			WandOfLivingEarth.class};

	//Wands whose turret does something other than shoot. They keep the wand's name and sprite,
	//but the effect is the turret's own - see supportTurn() and wandLevel().
	@SuppressWarnings("unchecked")
	private static final Class<? extends Wand>[] SUPPORT_POOL = new Class[]{
			WandOfTransfusion.class,
			WandOfWarding.class};

	{
		spriteClass = TurretSprite.class;

		//neutral rather than an ally: enemies only ever pick ALLY chars as targets, so this is
		//what keeps them walking past the turrets instead of stopping to break them down
		alignment = Alignment.NEUTRAL;
		state = PASSIVE;

		properties.add(Property.IMMOVABLE);
		properties.add(Property.INORGANIC);

		viewDistance = RANGE;
	}

	private Wand wand;
	private int cooldown = FIRE_INTERVAL;

	//picks `count` distinct wands from the pool, each already at WAND_LEVEL and identified
	public static ArrayList<Wand> rollChoices(int count) {
		ArrayList<Class<? extends Wand>> remaining = new ArrayList<>();
		Collections.addAll(remaining, ATTACK_POOL);
		Collections.addAll(remaining, SUPPORT_POOL);

		ArrayList<Wand> choices = new ArrayList<>();
		while (choices.size() < count && !remaining.isEmpty()) {
			Wand w = Reflection.newInstance(remaining.remove(Random.Int(remaining.size())));
			if (w == null) continue;
			w.level(WAND_LEVEL);
			w.identify();
			choices.add(w);
		}
		return choices;
	}

	//what a wand will actually do once it is mounted. Support wands ignore their own effect, so
	//their real description would be wrong in the pick-a-wand window.
	public static String turretInfo(Wand wand) {
		if (wand instanceof WandOfTransfusion) {
			return Messages.get(AmbulanceTurret.class, "info_transfusion", RANGE, FIRE_INTERVAL, SHIELD);
		}
		if (wand instanceof WandOfWarding) {
			return Messages.get(AmbulanceTurret.class, "info_warding", WARDING_BONUS);
		}
		return wand.info();
	}

	public void setWand(Wand wand) {
		this.wand = wand;
	}

	public Wand wand() {
		return wand;
	}

	@Override
	public String name() {
		if (wand == null) return Messages.get(this, "name");
		return Messages.get(this, "name_wand", wand.name());
	}

	@Override
	public String description() {
		if (wand == null) return Messages.get(this, "desc");
		if (wand instanceof WandOfTransfusion) {
			return Messages.get(this, "desc_transfusion", wand.name(), RANGE, FIRE_INTERVAL, SHIELD);
		}
		if (wand instanceof WandOfWarding) {
			return Messages.get(this, "desc_warding", wand.name(), WARDING_BONUS);
		}
		return Messages.get(this, "desc_wand", wand.name(), RANGE, FIRE_INTERVAL);
	}

	//a warding turret does nothing itself, it lends its levels to every other turret's wand
	private int wandLevel() {
		int bonus = 0;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob == this || !(mob instanceof AmbulanceTurret)) continue;
			if (((AmbulanceTurret) mob).wand instanceof WandOfWarding) bonus += WARDING_BONUS;
		}
		return WAND_LEVEL + bonus;
	}

	//support turrets don't shoot: they run an effect of their own every FIRE_INTERVAL turns
	private boolean isSupport() {
		return wand instanceof WandOfTransfusion || wand instanceof WandOfWarding;
	}

	private void supportTurn() {
		if (!(wand instanceof WandOfTransfusion)) return;

		for (Char ch : Actor.chars().toArray(new Char[0])) {
			//only the two that matter: the other turrets and the ambulance can't use it, and
			//shielding corrupted enemies would just drag the fight out
			if (ch != Dungeon.hero && !(ch instanceof Patient)) continue;
			if (!ch.isAlive()) continue;
			if (Dungeon.level.distance(pos, ch.pos) > RANGE) continue;

			//tops the barrier back up to SHIELD rather than adding to it - stacking every two
			//turns would leave the hero sitting behind an ever-growing wall of shield
			Barrier barrier = Buff.affect(ch, Barrier.class);
			if (barrier.shielding() < SHIELD) {
				barrier.setShield(SHIELD);
			}
		}
		if (sprite != null) sprite.zap(pos);
	}

	@Override
	protected boolean act() {
		if (isSupport()) {
			if (--cooldown <= 0) {
				cooldown = FIRE_INTERVAL;
				supportTurn();
			}
			spend(TICK);
			return true;
		}

		if (wand != null && --cooldown <= 0) {
			Char target = nearestTarget();
			if (target != null) {
				cooldown = FIRE_INTERVAL;
				spend(TICK);
				//the bolt animation runs on its own time: act() must not advance the turn as
				//well, the callback calls next() once it lands (same as Ward.doAttack)
				return fireAt(target);
			}
			//nothing in range, stay ready so the next arrival is shot immediately
			cooldown = 1;
		}

		spend(TICK);
		return true;
	}

	private Char nearestTarget() {
		Char best = null;
		int bestDist = Integer.MAX_VALUE;

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (!mob.isAlive() || mob.alignment != Alignment.ENEMY) continue;
			//a burrowed Beta can't be hit, a shot at it would just sail through
			if (mob instanceof Beta && ((Beta) mob).digging) continue;

			int dist = Dungeon.level.distance(pos, mob.pos);
			if (dist > RANGE || dist >= bestDist) continue;

			//needs a clear shot, otherwise it would fire into the wall the ambulance is boring
			Ballistica shot = new Ballistica(pos, mob.pos, Ballistica.PROJECTILE);
			if (shot.collisionPos != mob.pos) continue;

			best = mob;
			bestDist = dist;
		}
		return best;
	}

	//true if the shot resolved right away, false if act() should wait for the bolt's callback.
	//Wand.zapFromSource runs the wand's own fx(), so the projectile and sounds are the real
	//ones - it just redirects their origin to this turret instead of the hero.
	private boolean fireAt(Char target) {
		final Ballistica shot = new Ballistica(pos, target.pos, wand.collisionProperties(target.pos));

		//re-applied per shot: nearby warding turrets raise the level this wand fires at
		wand.level(wandLevel());

		if (sprite != null) sprite.zap(shot.collisionPos);

		final boolean[] landed = {false};
		Wand.zapFromSource(wand, this, shot, new Callback() {
			@Override
			public void call() {
				landed[0] = true;
				next();
			}
		});

		//beam wands resolve inside fx() and have already called next() by now
		return landed[0];
	}

	//bolted down and indestructible - the hero should never have to babysit it
	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public boolean add(Buff buff) {
		return false;
	}

	@Override
	public boolean isImmune(Class effect) {
		return true;
	}

	@Override
	public int defenseSkill(Char enemy) {
		return INFINITE_EVASION;
	}

	@Override
	protected Char chooseEnemy() {
		return null;
	}

	@Override
	public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti) {
		return false;
	}

	@Override
	public boolean reset() {
		return true;
	}

	private static final String WAND = "wand";
	private static final String COOLDOWN = "cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
		bundle.put(COOLDOWN, cooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		wand = (Wand) bundle.get(WAND);
		cooldown = bundle.getInt(COOLDOWN);
	}
}
