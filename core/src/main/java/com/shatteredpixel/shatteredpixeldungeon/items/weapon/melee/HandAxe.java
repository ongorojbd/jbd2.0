package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class HandAxe extends MeleeWeapon {
	{
		image = ItemSpriteSheet.HAND_AXE;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 1.17f;

		tier = 2;
		ACC = 1.32f; //32% boost to accuracy
	}

	private int HealCount = 0;

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //12 base, down from 15
				lvl*(tier+1);   //scaling unchanged
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//+(4+1.5*lvl) damage, roughly +55% base dmg, +75% scaling
		int dmgBoost = augment.damageFactor(4 + Math.round(1.5f*buffedLvl()));
		Mace.heavyBlowAbility(hero, target, 1, dmgBoost, this);
	}

	@Override
	public String abilityInfo() {
		int dmgBoost = levelKnown ? 4 + Math.round(1.5f*buffedLvl()) : 4;
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		HealCount++;
		int extratarget = 0;
		if (attacker instanceof Hero) {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (Dungeon.level.adjacent(mob.pos, defender.pos) && mob.alignment != Char.Alignment.ALLY) {
					int dmg = Dungeon.hero.damageRoll() - Math.max(defender.drRoll(), defender.drRoll());
					mob.damage(dmg/3, this);
					extratarget++;
					HealCount++;
				}
			}
		}

		Heal(attacker);

		return super.proc(attacker, defender, damage);
	}

	private void Heal(Char attacker) {
		if (HealCount >= 9) {
			int heal = 3;

			if (attacker instanceof Hero) {
				Buff.affect(hero, Barrier.class).setShield(3);
			}

//			attacker.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2);
			attacker.sprite.showStatusWithIcon(CharSprite.POSITIVE, "3", FloatingText.SHIELDING);
			HealCount = 0;
		}
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

	public String upgradeAbilityStat(int level){
		int dmgBoost = 4 + Math.round(1.5f*level);
		return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
	}
}