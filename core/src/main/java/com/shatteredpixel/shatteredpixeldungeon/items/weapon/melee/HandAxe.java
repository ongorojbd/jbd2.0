package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

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
	public int proc(Char attacker, Char defender, int damage) {
		HealCount++;
		int extratarget = 0;
		if (attacker instanceof Hero) {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (Dungeon.level.adjacent(mob.pos, defender.pos) && mob.alignment != Char.Alignment.ALLY) {
					int dmg = Dungeon.hero.damageRoll() - Math.max(defender.drRoll(), defender.drRoll());
					mob.damage(dmg, this);
					extratarget++;
					HealCount++;
				}
			}
		}

		float bounsdmg = Math.min(1.5f, 1f+(extratarget*0.1f));

//		if (Dungeon.hero.belongings.getItem(HornOfPlenty.class) != null) {
//			if (Dungeon.hero.belongings.getItem(HornOfPlenty.class).isEquipped(Dungeon.hero) && defender.properties().contains(Char.Property.BOSS))
//				bounsdmg += 0.1f;
//		}

		damage = Math.round(damage * bounsdmg);

		Heal(attacker);

		return super.proc(attacker, defender, damage);
	}

	private void Heal(Char attacker) {
		if (HealCount >= 9) {
			int heal = 3;

			if (attacker instanceof Hero) {
				hero.HP += heal;
			}
			attacker.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2);
			attacker.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", heal);
			HealCount = 0;
		}
	}

	@Override
	public String status() {
		return HealCount + "/" + 9;}

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
}