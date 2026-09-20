package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.GuidingLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HallowedGround;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyLance;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWard;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.MnemonicPrayer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JojoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//죠타로가 이벤트로 의지의 스탠드사를 선택하면 소환되는 동료. 스톤 프리 DISC 주문을 쿨다운마다 대신 시전한다
public class JolyneStoneFree extends DirectableAlly {

	{
		spriteClass = JojoSprite.class;

		HP = HT = 80;
		defenseSkill = 5;
		viewDistance = 8;

		alignment = Alignment.ALLY;
		intelligentAlly = true;
		immunities.add(AllyBuff.class);
	}

	public static final int RESPAWN_TURNS = 200;

	private static final int PUNCH_COOLDOWN    = 20;
	private static final int RADIANCE_COOLDOWN = 60;
	private static final int LANCE_COOLDOWN    = 100;
	private static final int RAIN_COOLDOWN     = 60;
	private static final int PRAYER_COOLDOWN   = 15;
	private static final int WARD_COOLDOWN     = 100;
	private static final int WEAPON_COOLDOWN   = 130;

	private int depthBonus = 0;

	private int punchCooldown = 0;
	private int radianceCooldown = 0;
	private int lanceCooldown = 0;
	private int rainCooldown = 0;
	private int prayerCooldown = 0;
	private int wardCooldown = 0;
	private int weaponCooldown = 0;

	private Char directedEnemy = null;

	//화살의 선택: 모든 능력의 재사용 대기시간이 1/3이 되는 대신, 죠린이 쓰러지면 죠타로의 체력이 1이 된다
	private static boolean arrowChoice() {
		return Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.J54);
	}

	private static int cooldown(int base) {
		return arrowChoice() ? Math.max(1, base/3) : base;
	}

	@Override
	public String description() {
		return super.description() + "\n\n" + Messages.get(this, "stats", HP, HT);
	}

	@Override
	protected boolean act() {
		updateDepthScaling();

		if (punchCooldown > 0) punchCooldown--;
		if (radianceCooldown > 0) radianceCooldown--;
		if (lanceCooldown > 0) lanceCooldown--;
		if (rainCooldown > 0) rainCooldown--;
		if (prayerCooldown > 0) prayerCooldown--;
		if (wardCooldown > 0) wardCooldown--;
		if (weaponCooldown > 0) weaponCooldown--;

		if (directedEnemy != null && !directedEnemy.isAlive()) directedEnemy = null;

		Hero hero = Dungeon.hero;
		if (hero != null) syncCloakStealth(hero);

		if (hero != null && castSpell(hero)) {
			spend(TICK);
			return true;
		}

		return super.act();
	}

	//죠타로가 스타 플라티나 DISC로 은신 중이면 죠린도 함께 모습을 감춘다
	private void syncCloakStealth(Hero hero) {
		if (hero.buff(CloakOfShadows.cloakStealth.class) != null) {
			Buff.prolong(this, Invisibility.class, 2f);
		} else if (buff(Invisibility.class) != null) {
			Invisibility.dispel(this);
		}
		//숨어 있는 동안에는 적에게 달려들지 않는다
		attacksAutomatically = !hidden();
	}

	private boolean hidden() {
		return buff(Invisibility.class) != null;
	}

	@Override
	public boolean canAttack(Char enemy) {
		return !hidden() && super.canAttack(enemy);
	}

	private boolean castSpell(Hero hero) {
		ArrayList<Char> visibleEnemies = visibleEnemies();

		if (lanceCooldown == 0 && hero.hasTalent(Talent.HOLY_LANCE) && !visibleEnemies.isEmpty()) {
			castHolyLance(hero, lanceTarget(visibleEnemies));
			return true;
		}

		if (radianceCooldown == 0 && !visibleEnemies.isEmpty()) {
			castRadiance(hero, visibleEnemies);
			return true;
		}

		if (rainCooldown == 0 && hero.hasTalent(Talent.HALLOWED_GROUND) && hero.HP < hero.HT/2) {
			castRain(hero);
			return true;
		}

		if (prayerCooldown == 0 && hero.hasTalent(Talent.MNEMONIC_PRAYER)) {
			castPrayer(hero, visibleEnemies);
			return true;
		}

		if (wardCooldown == 0 && !visibleEnemies.isEmpty() && hero.buff(HolyWard.HolyArmBuff.class) == null) {
			castHolyWard(hero);
			return true;
		}

		if (weaponCooldown == 0 && !visibleEnemies.isEmpty() && hero.buff(HolyWeapon.HolyWepBuff.class) == null) {
			castHolyWeapon(hero);
			return true;
		}

		if (punchCooldown == 0 && !visibleEnemies.isEmpty()) {
			castStringPunch(hero, Random.element(visibleEnemies));
			return true;
		}

		return false;
	}

	private ArrayList<Char> visibleEnemies() {
		ArrayList<Char> enemies = new ArrayList<>();
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) return enemies;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.alignment == Alignment.ENEMY && mob.isAlive() && fieldOfView[mob.pos]) {
				enemies.add(mob);
			}
		}
		return enemies;
	}

	//죠타로가 지목한 적을 우선하고, 없으면 가장 가까운 적
	private Char lanceTarget(ArrayList<Char> visibleEnemies) {
		if (directedEnemy != null && visibleEnemies.contains(directedEnemy)) {
			return directedEnemy;
		}
		Char closest = visibleEnemies.get(0);
		for (Char ch : visibleEnemies) {
			if (Dungeon.level.distance(pos, ch.pos) < Dungeon.level.distance(pos, closest.pos)) {
				closest = ch;
			}
		}
		return closest;
	}

	private void castStringPunch(Hero hero, Char target) {
		punchCooldown = cooldown(PUNCH_COOLDOWN);

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "punch"));
		sprite.zap(target.pos);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
		MagicMissile.boltFromChar(sprite.parent, MagicMissile.LIGHT_MISSILE, sprite, target.pos, null);

		//피해 출처를 주문으로 두어 마법 저항(안티매직) 등이 클레릭의 주문과 똑같이 적용되게 한다
		target.damage(Random.NormalIntRange(2, 8), GuidingLight.INSTANCE);
		Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.87f, 1.15f));
		target.sprite.burst(0x80DEF7, 3);
		if (target.isAlive()) {
			Buff.affect(target, GuidingLight.Illuminated.class);
			Buff.affect(target, GuidingLight.WasIlluminatedTracker.class);
		}
	}

	private void castRadiance(Hero hero, ArrayList<Char> visibleEnemies) {
		radianceCooldown = cooldown(RADIANCE_COOLDOWN);

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "radiance"));
		GameScene.flash(0x80DEF7);
		Sample.INSTANCE.play(Assets.Sounds.BLAST);

		for (Char ch : visibleEnemies) {
			if (ch.buff(GuidingLight.Illuminated.class) != null) {
				ch.damage(hero.lvl + 5, GuidingLight.INSTANCE);
			} else {
				Buff.affect(ch, GuidingLight.Illuminated.class);
				Buff.affect(ch, GuidingLight.WasIlluminatedTracker.class);
			}
			if (ch.isActive()) {
				Buff.affect(ch, Paralysis.class, 3f);
			}
		}
	}

	private void castHolyLance(Hero hero, Char target) {
		lanceCooldown = cooldown(LANCE_COOLDOWN);

		int points = hero.pointsInTalent(Talent.HOLY_LANCE);
		int min = 15 + 15*points;
		int max = Math.round(27.5f + 27.5f*points);
		if (Char.hasProp(target, Char.Property.UNDEAD) || Char.hasProp(target, Char.Property.DEMONIC)) {
			min = max;
		}

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "lance"));
		sprite.zap(target.pos);
		Sample.INSTANCE.play(Assets.Sounds.JT1);
		((MissileSprite) sprite.parent.recycle(MissileSprite.class))
				.reset(sprite, target.sprite, new HolyLance.HolyLanceVFX(), null);

		target.damage(Random.NormalIntRange(min, max), HolyLance.INSTANCE);
		Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.8f, 1f));
		if (target.isAlive()) {
			Buff.affect(target, GuidingLight.Illuminated.class);
			Buff.affect(target, GuidingLight.WasIlluminatedTracker.class);
		}
		target.sprite.burst(0xFFFFFFFF, 10);
	}

	private void castRain(Hero hero) {
		rainCooldown = cooldown(RAIN_COOLDOWN);

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "rain"));
		Sample.INSTANCE.play(Assets.Sounds.B1);
		Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);

		PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null),
				hero.pointsInTalent(Talent.HALLOWED_GROUND));
		for (int i = 0; i < Dungeon.level.length(); i++) {
			if (PathFinder.distance[i] == Integer.MAX_VALUE) continue;

			int terr = Dungeon.level.map[i];
			if (terr == Terrain.EMPTY || terr == Terrain.EMBERS || terr == Terrain.EMPTY_DECO) {
				Level.set(i, Terrain.GRASS);
				GameScene.updateMap(i);
				CellEmitter.get(i).burst(LeafParticle.LEVEL_SPECIFIC, 2);
			}
			GameScene.add(Blob.seed(i, 20, HallowedGround.HallowedTerrain.class));

			Char ch = Actor.findChar(i);
			if (ch != null) {
				HallowedGround.INSTANCE.affectChar(ch);
			}
		}
		Dungeon.observe();
	}

	private void castPrayer(Hero hero, ArrayList<Char> visibleEnemies) {
		prayerCooldown = cooldown(PRAYER_COOLDOWN);

		float extension = 2 + hero.pointsInTalent(Talent.MNEMONIC_PRAYER);
		MnemonicPrayer.INSTANCE.affectChar(hero, extension);
		MnemonicPrayer.INSTANCE.affectChar(this, extension);
		for (Char ch : visibleEnemies) {
			MnemonicPrayer.INSTANCE.affectChar(ch, extension);
		}

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "prayer"));
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		sprite.emitter().start(Speck.factory(Speck.UP), 0.15f, 4);
	}

	private void castHolyWard(Hero hero) {
		wardCooldown = cooldown(WARD_COOLDOWN);

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "ward"));
		Buff.affect(hero, HolyWard.HolyArmBuff.class, HolyWard.HolyArmBuff.DURATION);
		Item.updateQuickslot();
		if (hero.belongings.armor() != null) Enchanting.show(hero, hero.belongings.armor());
	}

	private void castHolyWeapon(Hero hero) {
		weaponCooldown = cooldown(WEAPON_COOLDOWN);

		sprite.showStatus(CharSprite.WARNING, Messages.get(this, "holy_weapon"));
		Buff.affect(hero, HolyWeapon.HolyWepBuff.class, HolyWeapon.HolyWepBuff.DURATION);
		Item.updateQuickslot();
		if (hero.belongings.weapon() != null) Enchanting.show(hero, hero.belongings.weapon());
	}

	private void updateDepthScaling() {
		int currentBonus = Dungeon.depth / 5;
		if (currentBonus != depthBonus) {
			int bonusDiff = currentBonus - depthBonus;
			depthBonus = currentBonus;

			int hpBonus = bonusDiff * 20;
			HT += hpBonus;
			HP = Math.min(HP + hpBonus, HT);

			defenseSkill += bonusDiff * 2;
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(4 + depthBonus, 6 + depthBonus*2);
	}

	@Override
	public int attackSkill(Char target) {
		return 15 + depthBonus*3;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(depthBonus, 2 + depthBonus*2);
	}

	@Override
	public void damage(int dmg, Object src) {
		//후반 층에서 한 방에 쓰러지지 않도록
		if (dmg > 25) dmg = 25;
		super.damage(dmg, src);
	}

	@Override
	public void targetChar(Char ch) {
		super.targetChar(ch);
		directedEnemy = ch;
	}

	@Override
	public void followHero() {
		super.followHero();
		directedEnemy = null;
	}

	@Override
	public void defendPos(int cell) {
		super.defendPos(cell);
		directedEnemy = null;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);

		yell(Messages.get(this, "death"));

		Hero hero = Dungeon.hero;
		if (hero != null) {
			if (arrowChoice() && hero.isAlive()) {
				hero.HP = 1;
				hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "arrow_choice_penalty"));
			}
			JolyneRespawn respawn = hero.buff(JolyneRespawn.class);
			if (respawn == null) {
				Buff.affect(hero, JolyneRespawn.class).set(RESPAWN_TURNS);
			} else {
				respawn.set(RESPAWN_TURNS);
			}
		}
	}

	public static void summon(Hero hero) {
		ArrayList<Integer> spawnPoints = new ArrayList<>();
		for (int offset : PathFinder.NEIGHBOURS8) {
			int cell = hero.pos + offset;
			if (Actor.findChar(cell) == null && (Dungeon.level.passable[cell] || Dungeon.level.avoid[cell])) {
				spawnPoints.add(cell);
			}
		}

		JolyneStoneFree jolyne = new JolyneStoneFree();
		jolyne.pos = spawnPoints.isEmpty() ? hero.pos : Random.element(spawnPoints);
		GameScene.add(jolyne);
		Dungeon.level.occupyCell(jolyne);
		if (jolyne.sprite != null) {
			jolyne.sprite.centerEmitter().burst(Speck.factory(Speck.LIGHT), 10);
		}
	}

	private static final String DEPTH_BONUS = "depth_bonus";
	private static final String PUNCH_CD    = "punch_cooldown";
	private static final String RADIANCE_CD = "radiance_cooldown";
	private static final String LANCE_CD    = "lance_cooldown";
	private static final String RAIN_CD     = "rain_cooldown";
	private static final String PRAYER_CD   = "prayer_cooldown";
	private static final String WARD_CD     = "ward_cooldown";
	private static final String WEAPON_CD   = "weapon_cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH_BONUS, depthBonus);
		bundle.put(PUNCH_CD, punchCooldown);
		bundle.put(RADIANCE_CD, radianceCooldown);
		bundle.put(LANCE_CD, lanceCooldown);
		bundle.put(RAIN_CD, rainCooldown);
		bundle.put(PRAYER_CD, prayerCooldown);
		bundle.put(WARD_CD, wardCooldown);
		bundle.put(WEAPON_CD, weaponCooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		depthBonus = bundle.getInt(DEPTH_BONUS);
		punchCooldown = bundle.getInt(PUNCH_CD);
		radianceCooldown = bundle.getInt(RADIANCE_CD);
		lanceCooldown = bundle.getInt(LANCE_CD);
		rainCooldown = bundle.getInt(RAIN_CD);
		prayerCooldown = bundle.getInt(PRAYER_CD);
		wardCooldown = bundle.getInt(WARD_CD);
		weaponCooldown = bundle.getInt(WEAPON_CD);
	}

	//죠린이 쓰러진 뒤 재소환까지의 대기시간
	public static class JolyneRespawn extends Buff {

		private static final String REMAINING = "remaining";

		private int remaining;

		public JolyneRespawn set(int turns) {
			remaining = Math.max(0, turns);
			return this;
		}

		@Override
		public boolean act() {
			if (Dungeon.level != null) {
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob instanceof JolyneStoneFree && mob.isAlive()) {
						detach();
						return true;
					}
				}
			}

			if (remaining > 0) {
				remaining--;
				spend(TICK);
				return true;
			}

			if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
				summon(Dungeon.hero);
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(REMAINING, remaining);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			remaining = bundle.getInt(REMAINING);
		}
	}
}
