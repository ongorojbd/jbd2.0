/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.DuelistArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RogueArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class CloakOfShadows extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_CLOAK;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level()+3, 10);

		defaultAction = AC_STEALTH;

		unique = true;
		bones = false;
	}

	public static final String AC_STEALTH = "STEALTH";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if ((isEquipped( hero ) || hero.hasTalent(Talent.LIGHT_CLOAK))
				&& !cursed
				&& hero.buff(MagicImmune.class) == null
				&& (charge > 0 || activeBuff != null)) {
			actions.add(AC_STEALTH);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals( AC_STEALTH )) {

			if (activeBuff instanceof cloakTimeBubble && hero.buff(Swiftthistle.TimeBubble.class) == null) {
				activeBuff.detach();
				activeBuff = null;
			}

			if (activeBuff == null){
				if (!isEquipped(hero) && !hero.hasTalent(Talent.LIGHT_CLOAK)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
				else if (cursed)       GLog.i( Messages.get(this, "cursed") );
				else if (charge <= 0)  GLog.i( Messages.get(this, "no_charge") );
				else {
					boolean j52TimeBubble = hero.hasTalent(Talent.J52);
					if (j52TimeBubble) {
						activeBuff = new cloakTimeBubble();
						activeBuff.attachTo(hero);
					}

					hero.spend( 1f );
					hero.busy();
					Sample.INSTANCE.play(Assets.Sounds.MELD);

					if (SPDSettings.getSkin3() == 1 && hero.belongings.armor() instanceof ClothArmor || SPDSettings.getSkin3() == 1 && hero.belongings.armor() instanceof RogueArmor) {
						Sample.INSTANCE.play(Assets.Sounds.ZAWARUDO);
					} else {
						Sample.INSTANCE.play(Assets.Sounds.PLATINUM);
					}

					if (!j52TimeBubble) {
						activeBuff = activeBuff();
						activeBuff.attachTo(hero);
					}
					Talent.onArtifactUsed(Dungeon.hero);
					hero.sprite.operate(hero.pos);
				}
			} else {
				activeBuff.detach();
				activeBuff = null;
				if (hero.invisible <= 0 && hero.buff(Preparation.class) != null){
					hero.buff(Preparation.class).detach();
				}
				hero.sprite.operate( hero.pos );
			}

		}
	}

	@Override
	public void activate(Char ch){
		super.activate(ch);
		if (activeBuff != null && activeBuff.target == null){
			activeBuff.attachTo(ch);
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (!collect || !hero.hasTalent(Talent.LIGHT_CLOAK)){
				if (activeBuff != null){
					activeBuff.detach();
					activeBuff = null;
				}
			} else {
				activate(hero);
			}

			return true;
		} else
			return false;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)){
			if (container.owner instanceof Hero
					&& passiveBuff == null
					&& ((Hero) container.owner).hasTalent(Talent.LIGHT_CLOAK)){
				activate((Hero) container.owner);
			}
			return true;
		} else{
			return false;
		}
	}

	@Override
	protected void onDetach() {
		if (passiveBuff != null){
			passiveBuff.detach();
			passiveBuff = null;
		}
		if (activeBuff != null && !isEquipped((Hero) activeBuff.target)){
			activeBuff.detach();
			activeBuff = null;
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new cloakRecharge();
	}

	@Override
	protected ArtifactBuff activeBuff( ) {
		return new cloakStealth();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;

		if (charge < chargeCap) {
			if (!isEquipped(target)) amount *= 0.75f*target.pointsInTalent(Talent.LIGHT_CLOAK)/3f;
			partialCharge += 0.25f*amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
	}

	public void directCharge(int amount){
		charge = Math.min(charge+amount, chargeCap);
		updateQuickslot();
	}
	
	@Override
	public Item upgrade() {
		chargeCap = Math.min(chargeCap + 1, 10);
		return super.upgrade();
	}

	private static final String STEALTHED = "stealthed";
	private static final String BUFF = "buff";
	private static final String TIME_BUBBLE_BUFF = "timeBubbleBuff";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		if (activeBuff != null) {
			bundle.put(BUFF, activeBuff);
			bundle.put(TIME_BUBBLE_BUFF, activeBuff instanceof cloakTimeBubble);
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(BUFF)){
			activeBuff = bundle.getBoolean(TIME_BUBBLE_BUFF) ? new cloakTimeBubble() : new cloakStealth();
			activeBuff.restoreFromBundle(bundle.getBundle(BUFF));
		}
	}

	@Override
	public int value() {
		return 0;
	}

	public class cloakRecharge extends ArtifactBuff{
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
				if (activeBuff == null && Regeneration.regenOn()) {
					float missing = (chargeCap - charge);
					if (level() > 7) missing += 5*(level() - 7)/3f;
					float turnsToCharge = (45 - missing);
					turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
					float chargeToGain = (1f / turnsToCharge);
					if (!isEquipped(Dungeon.hero)){
						chargeToGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.LIGHT_CLOAK)/3f;
					}
					partialCharge += chargeToGain;
				}

				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					if (charge == chargeCap){
						partialCharge = 0;
					}

				}
			} else {
				partialCharge = 0;
			}

			if (cooldown > 0)
				cooldown --;

			updateQuickslot();

			spend( TICK );

			return true;
		}

	}

	public class cloakStealth extends ArtifactBuff{
		
		{
			type = buffType.POSITIVE;
		}
		
		int turnsToCost = 0;

		@Override
		public int icon() {
			return BuffIndicator.INVISIBLE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.brightness(0.6f);
		}

		@Override
		public float iconFadePercent() {
			return (4f - turnsToCost) / 4f;
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(turnsToCost);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", turnsToCost);
		}

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				target.invisible++;
				if (target instanceof Hero && ((Hero) target).subClass == HeroSubClass.ASSASSIN){
					Buff.affect(target, Preparation.class);
				}
				if (target instanceof Hero && ((Hero) target).hasTalent(Talent.PROTECTIVE_SHADOWS)){
					Buff.affect(target, Talent.ProtectiveShadowsTracker.class);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean act(){
			turnsToCost--;
			
			if (turnsToCost <= 0){
				charge--;
				if (charge < 0) {
					charge = 0;
					detach();
					GLog.w(Messages.get(this, "no_charge"));
					((Hero) target).interrupt();
				} else {
					//target hero level is 1 + 2*cloak level
					int lvlDiffFromTarget = ((Hero) target).lvl - (1+level()*2);
					//plus an extra one for each level after 6
					if (level() >= 7){
						lvlDiffFromTarget -= level()-6;
					}
					if (lvlDiffFromTarget >= 0){
						exp += Math.round(10f * Math.pow(1.1f, lvlDiffFromTarget));
					} else {
						exp += Math.round(10f * Math.pow(0.75f, -lvlDiffFromTarget));
					}
					
					if (exp >= (level() + 1) * 50 && level() < levelCap) {
						upgrade();
						Catalog.countUse(CloakOfShadows.class);
						exp -= level() * 50;
						GLog.p(Messages.get(this, "levelup"));
						
					}
					turnsToCost = 4;
				}
				updateQuickslot();
			}

			spend( TICK );

			return true;
		}

		public void dispel(){
			if (turnsToCost <= 0 && charge > 0){
				charge--;
			}
			updateQuickslot();
			detach();
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add( CharSprite.State.INVISIBLE );
			else if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
		}

		@Override
		public void detach() {
			activeBuff = null;

			if (target.invisible > 0)   target.invisible--;

			updateQuickslot();
			super.detach();
		}
		
		private static final String TURNSTOCOST = "turnsToCost";
		private static final String BARRIER_INC = "barrier_inc";
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			
			bundle.put( TURNSTOCOST , turnsToCost);
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			
			turnsToCost = bundle.getInt( TURNSTOCOST );
		}
	}

	public class cloakTimeBubble extends ArtifactBuff{

		{
			type = buffType.POSITIVE;
		}

		float turnsToCost = 0;
		private boolean detachingTimeBubble = false;

		@Override
		public int icon() {
			return BuffIndicator.NONE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1f, 1f, 0);
		}

		@Override
		public float iconFadePercent() {
			return (4f - turnsToCost) / 4f;
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString((int)(turnsToCost + 0.001f));
		}

		@Override
		public String desc() {
			Swiftthistle.TimeBubble bubble = target == null ? null : target.buff(Swiftthistle.TimeBubble.class);
			if (bubble != null) {
				return bubble.desc();
			}
			return Messages.get(cloakStealth.class, "desc", turnsToCost);
		}

		@Override
		public boolean attachTo(Char target) {
			if (super.attachTo(target)) {
				Buff.affect(target, Swiftthistle.TimeBubble.class).reset(Math.max(1, charge * 4));
				charge = Math.max(0, charge - 1);
				if (target instanceof Hero) {
					gainExp((Hero) target);
				}
				turnsToCost = 4;
				updateQuickslot();
				return true;
			} else {
				return false;
			}
		}

		public void processTime(float time) {
			turnsToCost -= time;

			while (turnsToCost < -0.001f){
				if (charge <= 0) {
					charge = 0;
					detach();
					break;
				} else {
					charge--;
					Swiftthistle.TimeBubble bubble = target.buff(Swiftthistle.TimeBubble.class);
					if (bubble != null) {
						bubble.bufftime((charge + 1) * 4f + 1f);
					}
					if (target instanceof Hero) {
						gainExp((Hero) target);
					}
					turnsToCost = 4;
				}
				updateQuickslot();
			}
		}

		private void gainExp(Hero hero) {
			int lvlDiffFromTarget = hero.lvl - (1+level()*2);
			if (level() >= 7){
				lvlDiffFromTarget -= level()-6;
			}
			if (lvlDiffFromTarget >= 0){
				exp += Math.round(10f * Math.pow(1.1f, lvlDiffFromTarget));
			} else {
				exp += Math.round(10f * Math.pow(0.75f, -lvlDiffFromTarget));
			}

			if (exp >= (level() + 1) * 50 && level() < levelCap) {
				upgrade();
				Catalog.countUse(CloakOfShadows.class);
				exp -= level() * 50;
				GLog.p(Messages.get(cloakStealth.class, "levelup"));
			}
		}

		@Override
		public boolean act(){
			spend(TICK);
			return true;
		}

		@Override
		public void detach() {
			detachingTimeBubble = true;
			activeBuff = null;
			Swiftthistle.TimeBubble bubble = target == null ? null : target.buff(Swiftthistle.TimeBubble.class);
			if (bubble != null) {
				bubble.detach();
			}
			updateQuickslot();
			super.detach();
			detachingTimeBubble = false;
		}

		public void onTimeBubbleDetached() {
			if (detachingTimeBubble) {
				return;
			}
			activeBuff = null;
			updateQuickslot();
			super.detach();
		}

		private static final String TURNSTOCOST = "turnsToCost";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TURNSTOCOST, turnsToCost);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			turnsToCost = bundle.getFloat(TURNSTOCOST);
		}
	}
}
