package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Puccidisc;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Senior extends Mob {
		{
			spriteClass = SeniorSprite.class;

			EXP = 14;
			maxLvl = 23;

			HP = HT = 130;
			defenseSkill = 15;

			properties.add(Property.BOSS);
		}

	public int  Phase = 0;

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( PHASE, Phase );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		Phase = bundle.getInt(PHASE);
	}

	@Override
	public void damage(int dmg, Object src) {

		if (dmg >= 50){
			//takes 20/21/22/23/24/25/26/27/28/29/30 dmg
			// at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
			dmg = 50;
		}

		super.damage(dmg, src);

		if (Phase==0 && HP < 129) {
			Phase = 1;
			HP = 129;
			if (!BossHealthBar.isAssigned()) {
				BossHealthBar.assignBoss(this);
				yell(Messages.get(this, "notice"));
				for (Char ch : Actor.chars()){
				}
			}
		}
		if (Phase==1 && HP < 120) {
			Phase = 2;
			HP = 120;
			GameScene.flash(0x99FFFF);
			new TeleportationTrap().set(target).activate();
			Buff.affect(enemy, Blindness.class, 11f);
			Buff.affect(enemy, Ooze.class ).set( Ooze.DURATION );
			CellEmitter.center(Dungeon.hero.pos).burst(Speck.factory(Speck.QUESTION), 33);
			Sample.INSTANCE.play( Assets.Sounds.CURSED, 2, 0.33f );
			yell(Messages.get(this, "genkaku"));
			Camera.main.shake(31, 0.9f);
		}
		else if (Phase==2 && HP < 100) {
			Phase = 3;
			GameScene.flash(0x99FFFF);
			yell(Messages.get(this, "genkaku2"));
			Buff.affect(enemy, Silence.class, 51f);
			Buff.affect(enemy, Blindness.class, 15f);
			Buff.affect(enemy, Cripple.class, 15f);
			Sample.INSTANCE.play( Assets.Sounds.BLAST, 2, Random.Float(0.33f, 0.66f) );

			CellEmitter.center(Dungeon.hero.pos).burst(Speck.factory(Speck.QUESTION), 33);
			GameScene.flash(0x9999FF);
			Camera.main.shake(9, 0.9f);
		}


	}


	private static final String PHASE   = "Phase";




	@Override
		public int damageRoll() {
			return Random.NormalIntRange( 23, 31 );
		}

		@Override
		public int attackSkill( Char target ) {
			return 27;
		}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 12);
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return true;
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		Dungeon.level.drop( new Puccidisc(), pos ).sprite.drop( pos );

		yell( Messages.get(this, "defeated") );

	}

		@Override
		public int attackProc( Char enemy, int damage ) {
			damage = super.attackProc( enemy, damage );
			if (this.buff(Barkskin.class) == null) {
				if (Random.Int(3) == 0) {
					Buff.affect(enemy, Vertigo.class, 1f);
				}

			}
			return damage;
		}

	}