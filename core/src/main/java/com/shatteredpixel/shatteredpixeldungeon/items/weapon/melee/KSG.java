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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscC;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class KSG extends MeleeWeapon {

    public static final String AC_SHOOT = "SHOOT";
    public static final String AC_RELOAD = "RELOAD";

    public int max_round;
    public int round = 0;
    public float reload_time;
    public boolean silencer = false;
    public boolean short_barrel = false;
    public boolean long_barrel = false;
    public boolean magazine = false;
    public boolean light = false;
    public boolean heavy = false;
    public boolean flash = false;
    private static final String TXT_STATUS = "%d/%d";

    {

        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.SHOTGUN;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;
    }

    private static final String ROUND = "round";
    private static final String MAX_ROUND = "max_round";
    private static final String RELOAD_TIME = "reload_time";
    private static final String SILENCER = "silencer";
    private static final String SHORT_BARREL = "short_barrel";
    private static final String LONG_BARREL = "long_barrel";
    private static final String MAGAZINE = "magazine";
    private static final String LIGHT = "light";
    private static final String HEAVY = "heavy";
    private static final String FLASH = "flash";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_ROUND, max_round);
        bundle.put(ROUND, round);
        bundle.put(RELOAD_TIME, reload_time);
        bundle.put(SILENCER, silencer);
        bundle.put(SHORT_BARREL, short_barrel);
        bundle.put(LONG_BARREL, long_barrel);
        bundle.put(MAGAZINE, magazine);
        bundle.put(LIGHT, light);
        bundle.put(HEAVY, heavy);
        bundle.put(FLASH, flash);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        max_round = bundle.getInt(MAX_ROUND);
        round = bundle.getInt(ROUND);
        reload_time = bundle.getFloat(RELOAD_TIME);
        silencer = bundle.getBoolean(SILENCER);
        short_barrel = bundle.getBoolean(SHORT_BARREL);
        long_barrel = bundle.getBoolean(LONG_BARREL);
        magazine = bundle.getBoolean(MAGAZINE);
        light = bundle.getBoolean(LIGHT);
        heavy = bundle.getBoolean(HEAVY);
        flash = bundle.getBoolean(FLASH);
    }


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero)) {
            actions.add(AC_SHOOT);
            actions.add(AC_RELOAD);
        }
        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {

            identify();

            {
                if (round <= 0) {
                    reload_time = 1f;
                    reload();
                } else {
                    reload_time = 1f;
                    usesTargeting = true;
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                }
            }
        }
        if (action.equals(AC_RELOAD)) {
            max_round = (magazine) ? 2 : 1;
            if (round == max_round) {
                GLog.w(Messages.get(this, "already_loaded"));
            } else {
                reload();
            }
        }
    }

    public void reload() {
        max_round = (magazine) ? 2 : 1;
        curUser.spend(reload_time);
        curUser.busy();
        Sample.INSTANCE.play(Assets.Sounds.RELOAD);
        curUser.sprite.operate(curUser.pos);
        round = Math.max(max_round, round);

        GLog.i(Messages.get(this, "reloading"));

        updateQuickslot();
    }


    public int getRound() {
        return this.round;
    }

    @Override
    public String status() {
        max_round = (magazine) ? 2 : 1;
        return Messages.format(TXT_STATUS, round, max_round);
    }

    @Override
    public int STRReq(int lvl) {
        int needSTR = STRReq(tier, lvl);
        if (heavy) {
            needSTR += 2;
        }
        if (light) {
            needSTR -= 2;
        }
        return needSTR;
    }

    public int min(int lvl) {
        return tier +
                lvl;
    }

    public int max(int lvl) {
        return 2 * (tier) +
                lvl * (tier);
    }

    public int Bulletmin(int lvl) {
        return 1 +
                lvl +
                RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    public int Bulletmax(int lvl) {
        return (tier + 2) +
                lvl * 3 +
                RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public String info() {

        max_round = (magazine) ? 2 : 1;
        reload_time = 1f;
        String info = desc();

        if (Dungeon.hero != null) {
            if (levelKnown) {
                info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
                if (STRReq() > Dungeon.hero.STR()) {
                    info += " " + Messages.get(Weapon.class, "too_heavy");
                } else if (Dungeon.hero.STR() > STRReq()) {
                    info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
                }
                info += "\n\n" + Messages.get(KSG.class, "stats_known",
                        Bulletmin(KSG.this.buffedLvl()),
                        Bulletmax(KSG.this.buffedLvl()),
                        round, max_round, new DecimalFormat("#.##").format(reload_time));
            } else {
                info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
                if (STRReq(0) > Dungeon.hero.STR()) {
                    info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
                }
                info += "\n\n" + Messages.get(KSG.class, "stats_unknown",
                        Bulletmin(0),
                        Bulletmax(0),
                        round, max_round, new DecimalFormat("#.##").format(reload_time));
            }
        }

        String statsInfo = statsInfo();
        if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

        switch (augment) {
            case SPEED:
                info += " " + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += " " + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())) {
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped(Dungeon.hero)) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown) {
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        return info;
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return knockBullet().targetingPos(user, dst);
    }

    private int targetPos;

    @Override
    public int damageRoll(Char owner) {
        int damage = augment.damageFactor(super.damageRoll(owner));

        if (owner instanceof Hero) {
            int exStr = ((Hero) owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange(0, exStr);
            }
        }

        return damage;
    }                           //초과 힘에 따른 추가 데미지

    @Override
    protected float baseDelay(Char owner) {
        float delay = augment.delayFactor(this.DLY);
        if (owner instanceof Hero) {
            int encumbrance = STRReq() - ((Hero) owner).STR();
            if (encumbrance > 0) {
                delay *= Math.pow(1.2, encumbrance);
            }
        }

        return delay;
    }

    public KSG.Bullet knockBullet() {
        return new KSG.Bullet();
    }

    public class Bullet extends MissileWeapon {

        {
            image = ItemSpriteSheet.FISHING_SPEAR;

            tier = 5;
        }

        @Override
        public int defaultQuantity() {
            return 1;
        }

        @Override
        public int buffedLvl() {
            return KSG.this.buffedLvl();
        }

        @Override
        public int damageRoll(Char owner) {
            Hero hero = (Hero) owner;
            int bulletdamage = Random.NormalIntRange(Bulletmin(KSG.this.buffedLvl()),
                    Bulletmax(KSG.this.buffedLvl()));

            if (owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()) {
                bulletdamage = Math.round(bulletdamage * (1f + 0.15f * ((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM)));
            }

            {
                bulletdamage = Math.round(bulletdamage * (1.10f + 0.05f));
            }
            return bulletdamage;
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return KSG.this.hasEnchant(type, owner);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            SpiritBow bow = hero.belongings.getItem(SpiritBow.class);

            if (KSG.this.enchantment == null
                    && Random.Int(3) < hero.pointsInTalent(Talent.SHARED_ENCHANTMENT)
                    && hero.buff(MagicImmune.class) == null
                    && bow != null
                    && bow.enchantment != null) {
                return bow.enchantment.proc(this, attacker, defender, damage);
            } else {
                return KSG.this.proc(attacker, defender, damage);
            }
        }

        @Override
        public float delayFactor(Char user) {
            {
                return KSG.this.delayFactor(user);
            }
        }

        @Override
        public int STRReq(int lvl) {
            return KSG.this.STRReq();
        }

        @Override
        protected void onThrow(int cell) {
            for (int i = 1; i <= 5; i++) {                                                           //i<=n에서 n이 반복하는 횟수, 즉 발사 횟수
                Char enemy = Actor.findChar(cell);
                if (enemy == null || enemy == curUser) {
                    parent = null;
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                } else {
                    if (!curUser.shoot(enemy, this)) {
                        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    }
                    if (Random.Int(20) == 0) {
                        Buff.affect(enemy, Cripple.class, 2f);
                    }
                }
            }
            {
                round--;
            }
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.paralysed <= 0
                        && Dungeon.level.distance(curUser.pos, mob.pos) <= 4
                        && mob.state != mob.HUNTING
                        && !silencer) {
                    mob.beckon(curUser.pos);
                }
            }
            updateQuickslot();

        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play(Assets.Sounds.HIT_SHOTGUN);
        }

        @Override
        public void cast(final Hero user, final int dst) {
            super.cast(user, dst);
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                if (target == curUser.pos) {
                    reload();
                } else {
                    knockBullet().cast(curUser, target);
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{BossdiscC.class};
            inQuantity = new int[]{1};

            cost = 0;

            output = KSG.class;
            outQuantity = 1;
        }
    }

}