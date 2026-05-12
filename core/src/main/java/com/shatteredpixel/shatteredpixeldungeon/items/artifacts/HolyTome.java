/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.GuidingLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Act1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P1mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P2mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P3mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P4mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.P5mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TendencyTank;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willcmob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willgmob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndClericSpells;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HolyTome extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TOME;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level()+3, 10);

		defaultAction = AC_CAST;

		unique = true;
		bones = false;
	}

	public static final String AC_CAST = "CAST";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if ((isEquipped( hero ) || hero.hasTalent(Talent.LIGHT_READING))
				&& !cursed
				&& hero.buff(MagicImmune.class) == null) {
			actions.add(AC_CAST);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_CAST)) {

			if (!isEquipped(hero) && !hero.hasTalent(Talent.LIGHT_READING)) GLog.i(Messages.get(Artifact.class, "need_to_equip"));
			else if (cursed)       GLog.i( Messages.get(this, "cursed") );
			else if (usesRandomQuickSpell(hero)) {
				castQuickSpell(hero);
			}
			else {

				GameScene.show(new WndClericSpells(this, hero, false));

			}

		}
	}

	//used to ensure tome has variable targeting logic for whatever spell is being case
	public ClericSpell targetingSpell = null;

	@Override
	public int targetingPos(Hero user, int dst) {
		if (targetingSpell == null || targetingSpell.targetingFlags() == -1) {
			return super.targetingPos(user, dst);
		} else {
			return new Ballistica( user.pos, dst, targetingSpell.targetingFlags() ).collisionPos;
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (collect && hero.hasTalent(Talent.LIGHT_READING)){
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
					&& ((Hero) container.owner).hasTalent(Talent.LIGHT_READING)){
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
	}

	public boolean canCast( Hero hero, ClericSpell spell ){
		return (isEquipped(hero) || (Dungeon.hero.hasTalent(Talent.LIGHT_READING) && hero.belongings.contains(this)))
				&& hero.buff(MagicImmune.class) == null
				&& charge >= spell.effectiveChargeUse(hero)
				&& spell.canCast(hero);
	}

	public void spendCharge( float chargesSpent ){
		partialCharge -= chargesSpent;
		while (partialCharge < 0){
			charge--;
			partialCharge++;
		}

		//target hero level is 1 + 2*tome level
		int lvlDiffFromTarget = Dungeon.hero.lvl - (1+level()*2);
		//plus an extra one for each level after 6
		if (level() >= 7){
			lvlDiffFromTarget -= level()-6;
		}

		if (lvlDiffFromTarget >= 0){
			exp += Math.round(chargesSpent * 10f * Math.pow(1.1f, lvlDiffFromTarget));
		} else {
			exp += Math.round(chargesSpent * 10f * Math.pow(0.75f, -lvlDiffFromTarget));
		}

		if (exp >= (level() + 1) * 50 && level() < levelCap) {
			upgrade();
			Catalog.countUse(HolyTome.class);
			exp -= level() * 50;
			GLog.p(Messages.get(this, "levelup"));

		}

		updateQuickslot();
	}

	public void onSpellCast(ClericSpell castSpell, Hero hero) {
		if (hero.hasTalent(Talent.J65)) {
			onJ65SpellUsed(hero);
		}
		if (usesRandomQuickSpell(hero)) {
			setRandomQuickSpell(hero, castSpell);
		}
	}

	public void directCharge(float amount){
		if (charge < chargeCap) {
			partialCharge += amount;
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
		updateQuickslot();
	}

	@Override
	public Item upgrade() {
		chargeCap = Math.min(chargeCap + 1, 10);
		return super.upgrade();
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new TomeRecharge();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;

		if (charge < chargeCap) {
			if (!isEquipped(target)) amount *= 0.75f*target.pointsInTalent(Talent.LIGHT_READING)/3f;
			if (target.hasTalent(Talent.J54)) amount *= 2.5f;
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

	private ClericSpell quickSpell = null;
	private int j65Uses = 0;

	public void setQuickSpell(ClericSpell spell){
		if (quickSpell == spell){
			quickSpell = null; //re-assigning the same spell clears the quick spell
			if (passiveBuff != null){
				ActionIndicator.clearAction((ActionIndicator.Action) passiveBuff);
			}
		} else {
			quickSpell = spell;
			if (passiveBuff != null){
				ActionIndicator.setAction((ActionIndicator.Action) passiveBuff);
			}
		}
	}

	private void setQuickSpellDirect(ClericSpell spell) {
		quickSpell = spell;
		if (passiveBuff != null) {
			if (quickSpell == null) {
				ActionIndicator.clearAction((ActionIndicator.Action) passiveBuff);
			} else {
				ActionIndicator.setAction((ActionIndicator.Action) passiveBuff);
			}
		}
	}

	private void setRandomQuickSpell(Hero hero, ClericSpell previousSpell) {
		ArrayList<ClericSpell> spells = availableJ54Spells(hero);
		if (spells.isEmpty()) {
			setQuickSpellDirect(null);
			return;
		}

		if (spells.size() > 1 && previousSpell != null) {
			spells.remove(previousSpell);
		}
		setQuickSpellDirect(Random.element(spells));
	}

	private void ensureJ54QuickSpell(Hero hero) {
		if (quickSpell == null || !availableJ54Spells(hero).contains(quickSpell)) {
			setRandomQuickSpell(hero, null);
		}
	}

	private boolean usesRandomQuickSpell(Hero hero) {
		return hero.hasTalent(Talent.J54) || hero.hasTalent(Talent.J65);
	}

	private ArrayList<ClericSpell> availableJ54Spells(Hero hero) {
		ArrayList<ClericSpell> spells = new ArrayList<>();
		for (int i = 1; i <= Talent.MAX_TALENT_TIERS; i++) {
			spells.addAll(ClericSpell.getSpellList(hero, i));
		}
		return spells;
	}

	private void castQuickSpell(Hero hero) {
		ensureJ54QuickSpell(hero);

		if (quickSpell == null || !canCast(hero, quickSpell)) {
			GLog.w(Messages.get(HolyTome.this, "no_spell"));
			return;
		}

		ClericSpell spell = quickSpell;

		if (QuickSlotButton.targetingSlot != -1 &&
				Dungeon.quickslot.getItem(QuickSlotButton.targetingSlot) == HolyTome.this) {
			targetingSpell = spell;
			int cell = QuickSlotButton.autoAim(QuickSlotButton.lastTarget, HolyTome.this);

			if (cell != -1){
				GameScene.handleCell(cell);
			} else {
				GameScene.handleCell( QuickSlotButton.lastTarget.pos );
			}
		} else {
			spell.onCast(HolyTome.this, hero);

			if (spell.targetingFlags() != -1 && Dungeon.quickslot.contains(HolyTome.this)){
				targetingSpell = spell;
				QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(HolyTome.this));
			}
		}
	}

	private void onJ65SpellUsed(Hero hero) {
		j65Uses++;
		if (hero.sprite != null) {
			hero.sprite.showStatus(CharSprite.WARNING, j65Uses + "/5");
		}
		if (j65Uses < 5) {
			return;
		}

		if (summonJ65Ally(hero)) {
			j65Uses = 0;
		} else {
			j65Uses = 4;
		}
	}

	private boolean summonJ65Ally(Hero hero) {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int offset : PathFinder.NEIGHBOURS8) {
			int cell = hero.pos + offset;
			if (Dungeon.level.insideMap(cell)
					&& Dungeon.level.passable[cell]
					&& Actor.findChar(cell) == null) {
				candidates.add(cell);
			}
		}

		if (candidates.isEmpty()) {
			return false;
		}

		Mob ally = randomJ65Ally();
		ally.alignment = Char.Alignment.ALLY;
		ally.state = ally.WANDERING;
		ally.pos = Random.element(candidates);
		GameScene.add(ally);
		Dungeon.level.occupyCell(ally);
		ally.beckon(hero.pos);
		ScrollOfTeleportation.appear(ally, ally.pos);
		return true;
	}

	private Mob randomJ65Ally() {
		switch (Random.Int(10)) {
			case 0:
				return new Act1();
			case 1:
				return new P1mob();
			case 2:
				return new P2mob();
			case 3:
				return new P3mob();
			case 4:
				return new P4mob();
			case 5:
				return new P5mob();
			case 6:
				return new Willamob();
			case 7:
				return new Willcmob();
			case 8:
				return new Willgmob();
			case 9:
			default:
				return new TendencyTank();
		}
	}

	private static final String QUICK_CLS = "quick_cls";
	private static final String J65_USES = "j65_uses";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (quickSpell != null) {
			bundle.put(QUICK_CLS, quickSpell.getClass());
		}
		bundle.put(J65_USES, j65Uses);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(QUICK_CLS)){
			Class quickCls = bundle.getClass(QUICK_CLS);
			for (ClericSpell spell : ClericSpell.getAllSpells()){
				if (spell.getClass() == quickCls){
					quickSpell = spell;
				}
			}
		}
		j65Uses = bundle.getInt(J65_USES);
	}

	public class TomeRecharge extends ArtifactBuff implements ActionIndicator.Action {

		@Override
		public boolean attachTo(Char target) {
			if (super.attachTo(target)) {
				if (target instanceof Hero && usesRandomQuickSpell((Hero) target)) {
					ensureJ54QuickSpell((Hero) target);
				}
				if (quickSpell != null) ActionIndicator.setAction(this);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void detach() {
			super.detach();
			ActionIndicator.clearAction(this);
		}

		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
				if (Regeneration.regenOn()) {
					float missing = (chargeCap - charge);
					if (level() > 7) missing += 5*(level() - 7)/3f;
					float turnsToCharge = (45 - missing);
					turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
					float chargeToGain = (1f / turnsToCharge);
					if (!isEquipped(Dungeon.hero)){
						chargeToGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.LIGHT_READING)/3f;
					}
					if (Dungeon.hero.hasTalent(Talent.J54)) {
						chargeToGain *= 1.5f;
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

			updateQuickslot();

			spend( TICK );

			return true;
		}

		@Override
		public String actionName() {
			if (usesRandomQuickSpell(Dungeon.hero)) ensureJ54QuickSpell(Dungeon.hero);
			return quickSpell.name();
		}

		@Override
		public int actionIcon() {
			if (usesRandomQuickSpell(Dungeon.hero)) ensureJ54QuickSpell(Dungeon.hero);
			return quickSpell.icon() + HeroIcon.SPELL_ACTION_OFFSET;
		}

		@Override
		public int indicatorColor() {
			if (quickSpell == GuidingLight.INSTANCE && quickSpell.effectiveChargeUse(Dungeon.hero) == 0){
				return 0x0063ff;
			} else {
				return 0x212E4C;
			}
		}

		@Override
		public void doAction() {
			if (cursed){
				GLog.w(Messages.get(HolyTome.this, "cursed"));
				return;
			}

			if (usesRandomQuickSpell(Dungeon.hero)) {
				castQuickSpell(Dungeon.hero);
				return;
			}

			if (!canCast(Dungeon.hero, quickSpell)){
				GLog.w(Messages.get(HolyTome.this, "no_spell"));
				return;
			}

			if (QuickSlotButton.targetingSlot != -1 &&
					Dungeon.quickslot.getItem(QuickSlotButton.targetingSlot) == HolyTome.this) {
				targetingSpell = quickSpell;
				int cell = QuickSlotButton.autoAim(QuickSlotButton.lastTarget, HolyTome.this);

				if (cell != -1){
					GameScene.handleCell(cell);
				} else {
					//couldn't auto-aim, just target the position and hope for the best.
					GameScene.handleCell( QuickSlotButton.lastTarget.pos );
				}
			} else {
				quickSpell.onCast(HolyTome.this, Dungeon.hero);

				if (quickSpell.targetingFlags() != -1 && Dungeon.quickslot.contains(HolyTome.this)){
					targetingSpell = quickSpell;
					QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(HolyTome.this));
				}
			}
		}
	}

}
