package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
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
		DLY = 0.5f; //2x speed
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

		if (defender.buff(Blindness.class) != null){
			damage *= 1.25f;
			attacker.sprite.showStatus(CharSprite.POSITIVE, Messages.get(Sai.class, "1"));
		}

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
		if (Dungeon.hero != null && hero.belongings.getItem(SandalsOfNature.class) != null) {
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
		//+(4+lvl) damage, roughly +60% base damage, +67% scaling
		int dmgBoost = augment.damageFactor(4 + buffedLvl());
		Sai.comboStrikeAbility(hero, target, 0, dmgBoost, this);
	}

	@Override
	public String abilityInfo() {
		int dmgBoost = levelKnown ? 4 + buffedLvl() : 4;
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(dmgBoost));
		} else {
			return Messages.get(this, "typical_ability_desc", augment.damageFactor(dmgBoost));
		}
	}

	public String upgradeAbilityStat(int level){
		return "+" + augment.damageFactor(4 + level);
	}

	public static void comboStrikeAbility(Hero hero, Integer target, float multiPerHit, int boostPerHit, MeleeWeapon wep){
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
			GLog.w(Messages.get(wep, "ability_target_range"));
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
					recentHits = buff.hits;
					buff.detach();
				}

				boolean hit = hero.attack(enemy, 1f + multiPerHit*recentHits, boostPerHit*recentHits, Char.INFINITE_ACCURACY);
				if (hit && !enemy.isAlive()){
					wep.onAbilityKill(hero, enemy);
				}

				Invisibility.dispel();
				Sword.doraclass();
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

		public static int DURATION = 5;
		private float comboTime = 0f;
		public int hits = 0;

		@Override
		public int icon() {
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
			comboTime-=TICK;
			spend(TICK);
			if (comboTime <= 0) {
				detach();
			}
			return true;
		}

		public void addHit(){
			hits++;
			comboTime = 5f;

			if (hits >= 2 && icon() != BuffIndicator.NONE){
				GLog.p( Messages.get(Combo.class, "combo", hits) );
			}
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - comboTime)/ DURATION);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString((int)comboTime);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", hits, dispTurns(comboTime));
		}

		private static final String TIME  = "combo_time";
		public static String RECENT_HITS = "recent_hits";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TIME, comboTime);
			bundle.put(RECENT_HITS, hits);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			comboTime = bundle.getInt(TIME);
			hits = bundle.getInt(RECENT_HITS);
		}
	}

}