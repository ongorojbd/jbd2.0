/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Dominion;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Abomination2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Abomination3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AbominationSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButterflySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FishSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KS3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NikuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SquirrelSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TentacleSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class HereticSummon extends NPC {

	private boolean canzap;

	{
		HP = HT = 60;
		defenseSkill = 20;

		alignment = Alignment.ALLY;
		state = HUNTING;

		//before other mobs
		actPriority = MOB_PRIO + 1;

		intelligentAlly = true;

		properties.add( Property.DEMONIC );
		WANDERING = new Wandering();
	}

	@Override
	public int damageRoll() {
		return Math.max(1, Math.round(Random.NormalIntRange(
				(int) (Dungeon.depth*0.75f),
				Dungeon.depth
		) * damageMultiplier()));
	}

	@Override
	public int attackSkill( Char target ){
		return (int) (Dungeon.depth*1.5f);
	}

	@Override
	public int drRoll() {
		if (this instanceof EarthSummon){
			return Random.NormalIntRange(
					(int) (Dungeon.depth*0.33f),
					(int) (Dungeon.depth*0.66f)
			);
		} else return Random.NormalIntRange(
				(int) (Dungeon.depth*0.25f),
				(int) (Dungeon.depth*0.5f)
		);
	}

	@Override
	public float attackDelay() {
		if (this instanceof BloodSummon){
		    return super.attackDelay()*0.5f;
		} else return super.attackDelay();
	}

	private int rangedCooldown = Random.NormalIntRange( 3, 5 );

	@Override
	protected boolean act() {
		if (state == HUNTING){
			rangedCooldown--;
		}

		return super.act();
	}

	public void summon(int HT) {
		this.HT = Math.max(1, Math.round(HT * healthMultiplier()));
		this.HP = this.HT;
		state = HUNTING;
		target = Dungeon.hero.pos;
		Buff.append(Dungeon.hero, TalismanOfForesight.CharAwareness.class).charID = this.id();
		canzap = true;
	}

	public static int maxSummons() {
		int points = summonTalentPoints(Talent.CURSED_CLAW);
		return points > 0 ? points + 1 : 1;
	}

	public static ArrayList<HereticSummon> activeSummons() {
		ArrayList<HereticSummon> summons = new ArrayList<>();
		if (Dungeon.level == null) return summons;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob instanceof HereticSummon && mob.isAlive()) {
				summons.add((HereticSummon) mob);
			}
		}
		return summons;
	}

	public static void shareHealingPotion() {
		if (!hasPact(1)) return;
		for (HereticSummon summon : activeSummons()) {
			PotionOfHealing.cure(summon);
			PotionOfHealing.heal(summon);
		}
	}

	public static void shareShieldingPotion() {
		if (!hasPact(1)) return;
		for (HereticSummon summon : activeSummons()) {
			int shield = (int) (0.6f * summon.HT + 10);
			Buff.affect(summon, Barrier.class).setShield(shield);
			summon.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shield), FloatingText.SHIELDING);
		}
	}

	public static void sharePurityPotion() {
		if (!hasPact(1)) return;
		for (HereticSummon summon : activeSummons()) {
			Buff.prolong(summon, BlobImmunity.class, BlobImmunity.DURATION);
		}
	}

	public static void shareCleansingPotion() {
		if (!hasPact(1)) return;
		for (HereticSummon summon : activeSummons()) {
			PotionOfCleansing.cleanse(summon);
		}
	}

	public static void shareDew(int quantity) {
		if (!hasPact(2)) return;
		for (HereticSummon summon : activeSummons()) {
			int heal = Math.min(summon.HT - summon.HP, Math.round(summon.HT * 0.05f * quantity));
			if (heal > 0) {
				summon.HP += heal;
				summon.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
			}
		}
	}

	public static void shareSungrass(Char ch) {
		if (ch != Dungeon.hero || !hasPact(2)) return;
		for (HereticSummon summon : activeSummons()) {
			Buff.affect(summon, Sungrass.Health.class).boost(summon.HT);
		}
	}

	public static void shareMageroyal(Char ch) {
		if (ch != Dungeon.hero || !hasPact(2)) return;
		for (HereticSummon summon : activeSummons()) {
			PotionOfHealing.cure(summon);
		}
	}

	public static boolean sacrificeForPact() {
		if (!hasPact(3)
				|| Dungeon.hero.buff(Talent.PactOfKnotCooldown.class) != null) {
			return false;
		}
		ArrayList<HereticSummon> summons = activeSummons();
		if (summons.isEmpty()) return false;
		HereticSummon summon = summons.get(0);
		summon.die(Talent.PactOfKnotCooldown.class);
		Buff.affect(Dungeon.hero, Talent.PactOfKnotCooldown.class, 100);

        Buff.affect(hero, Invulnerability.class, 1f);
        GameScene.flash(0xFFFF00);
        GLog.n(Messages.get(Keicho.class, "qwe"));

		return true;
	}

	private static boolean hasPact(int level) {
		return summonTalentPoints(Talent.PACT_OF_KNOT) >= level;
	}

	private static int summonTalentPoints(Talent talent) {
		return Dungeon.hero != null && Dungeon.hero.subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.SUMMONER
				? Dungeon.hero.pointsInTalent(talent)
				: 0;
	}

	private static float damageMultiplier() {
		switch (summonTalentPoints(Talent.CURSED_CLAW)) {
			case 1: return 0.75f;
			case 2: return 0.60f;
			case 3: return 0.55f;
			default: return 1f;
		}
	}

	private static float healthMultiplier() {
		switch (summonTalentPoints(Talent.CURSED_CLAW)) {
            case 1: return 0.75f;
            case 2: return 0.60f;
            case 3: return 0.55f;
			default: return 1f;
		}
	}

	public void cantzap() {
		canzap = false;
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		if (rangedCooldown <= 0 && canzap) {
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ).collisionPos == enemy.pos;
		} else {
			return super.canAttack( enemy );
		}
	}

	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos ) && !canzap) {
			return super.doAttack( enemy );
		}

		if (Dungeon.level.adjacent( pos, enemy.pos ) || rangedCooldown > 0 || canzap) {
			return super.doAttack( enemy );

		} else {

			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		meleeProc( enemy, damage );

		return damage;
	}

	private void zap() {
		spend( 1f );

		if (hit( this, enemy, true )) {

			rangedProc( enemy );

		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}

		rangedCooldown = Random.NormalIntRange( 3, 5 );
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public boolean add( Buff buff ) {
		if (harmfulBuffs.contains( buff.getClass() )) {
			damage( Random.NormalIntRange( HT/2, HT * 3/5 ), buff );
			return false;
		} else {
			return super.add( buff );
		}
	}

	protected abstract void meleeProc( Char enemy, int damage );
	protected abstract void rangedProc( Char enemy );

	protected ArrayList<Class<? extends Buff>> harmfulBuffs = new ArrayList<>();

	private static final String COOLDOWN = "cooldown";
	private static final String CANZAP   = "canzap";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( COOLDOWN, rangedCooldown );
		bundle.put( CANZAP, canzap );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (bundle.contains( COOLDOWN )){
			rangedCooldown = bundle.getInt( COOLDOWN );
		}
		canzap = bundle.getBoolean( CANZAP );
	}

	private class Wandering extends Mob.Wandering {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if ( enemyInFOV ) {

				enemySeen = true;

				notice();
				alerted = true;
				state = HUNTING;
				target = enemy.pos;

			} else {

				enemySeen = false;

				int oldPos = pos;
				target =  Dungeon.hero.pos;
				//always move towards the hero when wandering
				if (getCloser( target )) {
					//moves 2 tiles at a time when returning to the hero
					if (!Dungeon.level.adjacent(target, pos)){
						getCloser( target );
					}
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					spend( TICK );
				}

			}
			return true;
		}

	}

	@Override
	protected Char chooseEnemy() {
		Char enemy = super.chooseEnemy();

		//will never attack something far from their target
		if (enemy != null
				&& Dungeon.level.mobs.contains(enemy)
				&& (Dungeon.level.distance(enemy.pos, Dungeon.hero.pos) <= 8)){
			return enemy;
		}

		return null;
	}

	public static class BloodSummon extends HereticSummon {

		{
			spriteClass = SquirrelSprite.class;
			baseSpeed = 2f;
		}

		@Override
		public void summon(int HT) {
			super.summon(HT);
			cantzap();
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 2 ) == 0) {
				Buff.affect(enemy, Bleeding.class).set(Math.round(damage * 0.33f));
				Splash.at( enemy.sprite.center(), enemy.sprite.blood(), 5);
			}

			if (enemy.HP <= damage) {
				HP = Math.max(HT, HP+enemy.HP);
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			//cant zap
		}
	}

	public static class PoisonSummon extends HereticSummon {

		{
			spriteClass = ButterflySprite.class;
			properties.add( Property.ACIDIC );
			immunities.add( Poison.class );
			immunities.add( ToxicGas.class );
			immunities.add( StenchGas.class );
			immunities.add( CorrosiveGas.class );
			immunities.add( Dominion.class );

            flying = true;
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 3 ) == 0) {
				Buff.affect(enemy, Poison.class).set(Random.Int(3, 5) );
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			Buff.affect(enemy, Poison.class).set(Random.Int(2, 3) );
		}
	}

	public static class FrostSummon extends HereticSummon {

		{
			spriteClass = TentacleSprite.class;
			properties.add( Property.ICY );
			harmfulBuffs.add( Burning.class );
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 3 ) == 0 || Dungeon.level.water[enemy.pos]) {
				Freezing.freeze( enemy.pos );
				Splash.at( enemy.sprite.center(), sprite.blood(), 5);
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			Buff.affect(enemy, Chill.class, 2f);
			Splash.at( enemy.sprite.center(), sprite.blood(), 5);
		}
	}

	public static class ArcaneSummon extends HereticSummon {

		{
			spriteClass = Abomination3Sprite.class;
			immunities.add( DisintegrationTrap.class );
			immunities.add( WandOfDisintegration.class );
			resistances.add( Eye.DeathGaze.class );
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
            if (Random.Int( 1 ) == 0 || Dungeon.level.water[enemy.pos]) {
                Doom doom = Buff.affect(enemy, Doom.class);
                doom.setDuration(2f);
            }
		}

		@Override
		protected void rangedProc( Char enemy ) {
			int zap = damageRoll();
			if (enemy instanceof Eye) zap /= 2;
			enemy.sprite.emitter().burst(PurpleParticle.BURST, 5);
		}
	}

	public static class FireSummon extends HereticSummon {

		{
			spriteClass = AbominationSprite.class;
			properties.add( Property.FIERY );
			harmfulBuffs.add( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost.class );
			harmfulBuffs.add( Chill.class );
			harmfulBuffs.add( Frost.class );
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 2 ) == 0 && !Dungeon.level.water[enemy.pos]) {
				Buff.affect( enemy, Burning.class ).reignite( enemy );
				Splash.at( enemy.sprite.center(), sprite.blood(), 5);
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			if (!Dungeon.level.water[enemy.pos]) {
				Buff.affect( enemy, Burning.class ).reignite( enemy, 4f );
			}
			Splash.at( enemy.sprite.center(), sprite.blood(), 5);
		}
	}

	public static class DarkSummon extends HereticSummon {

		{
			spriteClass = NikuSprite.class;
			immunities.add( Weakness.class );
			immunities.add( Vulnerable.class );
			immunities.add( Hex.class );
			resistances.add( Shaman.RedShaman.EarthenBolt.class );
			resistances.add( Shaman.BlueShaman.EarthenBolt.class );
			resistances.add( Shaman.PurpleShaman.EarthenBolt.class );
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			switch (Random.Int(2)){
				case 0: Buff.affect(enemy, Weakness.class, Random.Int(3, 5) );
				case 1: Buff.affect(enemy, Vulnerable.class, Random.Int(3, 5) );
				case 2: Buff.affect(enemy, Hex.class, Random.Int(3, 5) );
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			switch (Random.Int(2)){
				case 0: Buff.affect(enemy, Weakness.class, Random.Int(3, 5) );
				case 1: Buff.affect(enemy, Vulnerable.class, Random.Int(3, 5) );
				case 2: Buff.affect(enemy, Hex.class, Random.Int(3, 5) );
			}
		}
	}

	public static class EarthSummon extends HereticSummon {

		{
			spriteClass = FishSprite.class;
		}

		@Override
		public void summon(int HT) {
			super.summon(HT);
			cantzap();
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 3 ) == 0) {
				Buff.affect( enemy, Paralysis.class, 2f );
				Buff.affect( enemy, Cripple.class, 2f );
				enemy.sprite.emitter().burst(MagicMissile.EarthParticle.BURST, 5);
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			// cant zap
		}
	}
}
