package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.D4CParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

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
	public String status() {
		if (this.isIdentified()) return HealCount + "/" + 11;
		else return null;}

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
}