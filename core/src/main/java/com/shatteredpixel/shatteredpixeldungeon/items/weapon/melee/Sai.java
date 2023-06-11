package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Sai extends MeleeWeapon {
	{
		image = ItemSpriteSheet.SAI;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.17f;

		tier = 3;
	}

	private int HealCount = 0;

	@Override
	public int max(int lvl) {
		return  5*(tier) - 2 +   //14 + 4
				lvl*(tier); }

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		HealCount++;

		Heal(attacker);

		if (hero.belongings.getItem(SandalsOfNature.class) != null) {
			if (hero.belongings.getItem(SandalsOfNature.class).isEquipped(hero) && (Random.Int(10) == 0)) {
				{
					Buff.affect( defender, Blindness.class, 3f );
				}
			}
		}

		return super.proc(attacker, defender, damage);
	}

	private void Heal(Char attacker) {
		if (HealCount >= 11) {

			Sample.INSTANCE.play(Assets.Sounds.RAY, 1f);

			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {

					for (int i : PathFinder.NEIGHBOURS8) {
					CellEmitter.get( hero.pos+i ).burst(BloodParticle.FACTORY, 9 );
					}

					Buff.affect( mob, Blindness.class, 3f );
				}
			}

			attacker.sprite.showStatus(CharSprite.POSITIVE, "[체온 흡수]");
			HealCount = 0;
		}
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if (hero.belongings.getItem(SandalsOfNature.class) != null) {
			if (hero.belongings.getItem(SandalsOfNature.class).isEquipped(hero))
				info += "\n\n" + Messages.get( Sai.class, "setbouns");}

		return info;
	}

	private static final String HEALPOINT = "HealCount";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HEALPOINT, HealCount);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		HealCount = bundle.getInt(HEALPOINT);
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Sai.comboStrikeAbility(hero, target, 0.40f, this);
	}

	public static void comboStrikeAbility(Hero hero, Integer target, float boostPerHit, MeleeWeapon wep){
		if (target == null) {
			return;
		}

		Char enemy = Actor.findChar(target);
		if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
			GLog.w(Messages.get(wep, "ability_no_target"));
			return;
		}

		hero.belongings.abilityWeapon = wep;
		if (!hero.canAttack(enemy)){
			GLog.w(Messages.get(wep, "ability_bad_position"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				wep.beforeAbilityUsed(hero, enemy);
				AttackIndicator.target(enemy);

				int recentHits = 0;
				ComboStrikeTracker buff = hero.buff(ComboStrikeTracker.class);
				if (buff != null){
					recentHits = buff.totalHits();
					buff.detach();
				}

				boolean hit = hero.attack(enemy, 1f + boostPerHit*recentHits, 0, Char.INFINITE_ACCURACY);
				if (hit && !enemy.isAlive()){
					wep.onAbilityKill(hero, enemy);
				}

				Invisibility.dispel();
				Sample.INSTANCE.play( Assets.Sounds.DORA );
				hero.spendAndNext(hero.attackDelay());
				if (recentHits >= 2 && hit){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				}

				wep.afterAbilityUsed(hero);
			}
		});
	}

	public static class ComboStrikeTracker extends Buff {

		{
			type = buffType.POSITIVE;
		}

		public static int DURATION = 6; //to account for the turn the attack is made in
		public int[] hits = new int[DURATION];

		@Override
		public int icon() {
			//pre-v2.1 saves
			if (totalHits() == 0) return BuffIndicator.NONE;

			if (Dungeon.hero.belongings.weapon() instanceof Gloves
					|| Dungeon.hero.belongings.weapon() instanceof Sai
					|| Dungeon.hero.belongings.weapon() instanceof Gauntlet
					|| Dungeon.hero.belongings.secondWep() instanceof Gloves
					|| Dungeon.hero.belongings.secondWep() instanceof Sai
					|| Dungeon.hero.belongings.secondWep() instanceof Gauntlet) {
				return BuffIndicator.DUEL_COMBO;
			} else {
				return BuffIndicator.NONE;
			}
		}

		@Override
		public boolean act() {

			//shuffle all hits down one turn
			for (int i = 0; i < DURATION; i++){
				if (i == DURATION-1){
					hits[i] = 0;
				} else {
					hits[i] =  hits[i+1];
				}
			}

			if (totalHits() == 0){
				detach();
			}

			spend(TICK);
			return true;
		}

		public void addHit(){
			hits[DURATION-1]++;
		}

		public int totalHits(){
			int sum = 0;
			for (int i = 0; i < DURATION; i++){
				sum += hits[i];
			}
			return sum;
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(totalHits());
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", totalHits());
		}

		public static String RECENT_HITS = "recent_hits";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(RECENT_HITS, hits);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			if (bundle.contains(RECENT_HITS)) {
				hits = bundle.getIntArray(RECENT_HITS);
			}
		}
	}

}