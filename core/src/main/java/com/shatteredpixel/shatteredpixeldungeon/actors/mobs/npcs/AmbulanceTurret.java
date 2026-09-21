package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TurretSprite;
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

	//attack wands only. The rest of Generator's pool either does no damage (Regrowth), hurts
	//the hero through curUser (Transfusion), or nests turrets inside turrets (Warding).
	@SuppressWarnings("unchecked")
	private static final Class<? extends Wand>[] POOL = new Class[]{
			WandOfMagicMissile.class,
			WandOfLightning.class,
			WandOfDisintegration.class,
			WandOfFireblast.class,
			WandOfCorrosion.class,
			WandOfBlastWave.class,
			WandOfFrost.class,
			WandOfPrismaticLight.class};

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
		Collections.addAll(remaining, POOL);

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
		return Messages.get(this, "desc_wand", wand.name(), RANGE, FIRE_INTERVAL);
	}

	@Override
	protected boolean act() {
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
