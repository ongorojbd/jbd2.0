package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CustomeSet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AnnihilationGear extends Item {

    public static final String AC_ACTIVE	= "active";

    {
        image = ItemSpriteSheet.JOJO7;

        defaultAction = AC_ACTIVE;
        unique = true;
    }

    public int charge = 4;
    public int chargeCap = 4;
    public int arts = 0; // 0 = false, 1,2 = true 파괴1=부유 파괴2=충격 파괴3=저격 수호1=면역 수호2=압살 수호3=제압
    public int artsused = 0; // 일정 횟수 이상이면 마법부여 발동안함.

    public int min() {
        return 6 + buffedLvl(); }

    public int max() {
        return 10 + Dungeon.hero.lvl + buffedLvl();}

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ACTIVE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ACTIVE)) {
            if (charge > 0) GameScene.selectCell(Shot);
        }
    }

    public void SPCharge(int n) {
        charge += n;
        if (chargeCap < charge) charge = chargeCap;
        updateQuickslot();
    }

    public void discharge() {
        charge--;
        updateQuickslot();
    }

    @Override
    public String status() {
        return charge + "/" + chargeCap;
    }

    @Override
    public Item upgrade() {
        charge++; chargeCap++;
        chargeCap = Math.min(chargeCap,10);
        return super.upgrade();
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    private static final String CHARGE = "charge";
    private static final String MAGICARTS = "arts";
    private static final String ARTSUSED = "artsused";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
        bundle.put(MAGICARTS, arts);
        bundle.put(ARTSUSED, artsused);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt(CHARGE);
        arts = bundle.getInt(MAGICARTS);
        artsused = bundle.getInt(ARTSUSED);
    }


    protected CellSelector.Listener Shot = new  CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null && target != curUser.pos) {
                int targetCell = target;

                final int finalTargetCell = targetCell;

                final AnnihilationGear.Spriteex Arrow = new AnnihilationGear.Spriteex();

                curUser.sprite.zap(targetCell);

                if (Dungeon.hero.buff(Levitation.class) != null) {
                    Buff.detach(Dungeon.hero, Levitation.class);
                }
                else charge--;
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(curUser.sprite,
                                finalTargetCell,
                                Arrow,
                                new Callback() {
                                    @Override
                                    public void call() {
                                        Arrow.onThrow(target);
                                        updateQuickslot();
                                    }
                                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(AnnihilationGear.class, "prompt");
        }


    };


    public class Spriteex extends MissileWeapon {

        {
            image = ItemSpriteSheet.JAVELIN;

            hitSound = Assets.Sounds.HIT_SLASH;
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            parent = null;
            Splash.at( cell, 0xCC99FFFF, 1 );
            isHit(cell);

        }

        protected void isHit(int target)
        {
            Char mob = Actor.findChar(target);
            if (mob != null) {
                if (mob instanceof EX44 && arts == 3) {
                    CellEmitter.center(target).burst(BlastParticle.FACTORY, 10);
                    Sample.INSTANCE.play(Assets.Sounds.HIT);
                    mob.die(new WarCatArts3());
                }
                else {dohit(mob);
                    CellEmitter.center(target).burst(BlastParticle.FACTORY, 10);
                    Sample.INSTANCE.play(Assets.Sounds.HIT);}
            }
            else {
                if (Dungeon.hero.subClass == HeroSubClass.PALADIN) SpawnEX44(target);

                CellEmitter.center(target).burst(BlastParticle.FACTORY, 10);
                Sample.INSTANCE.play(Assets.Sounds.HIT);
            }

            Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();
            buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (buff != null) buff.detach();

            Invisibility.dispel(); curUser.spendAndNext(1);
        }

    }

    public void dohit(final Char enemy) {

        int dmg = Random.NormalIntRange(min(), max());
            dmg *= 1.08f;


        if (curUser.subClass == HeroSubClass.PALADIN){
            switch (arts) {
                case 0: default: break;
                case 1:
                    if (5 + buffedLvl() > Random.Int(100)) Buff.affect(curUser, Levitation.class, 3f);
                    break;
                case 2:
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (Dungeon.level.adjacent(mob.pos, enemy.pos) && mob.alignment != Char.Alignment.ALLY && mob != enemy) {
                            mob.damage(5 + buffedLvl() * 3, curUser);
                        }}
                    break;
                case 3:
                    int distance = Dungeon.level.distance(curUser.pos, enemy.pos) - 1;
                    float DamageLevel = 1.1f + (0.008f * buffedLvl());
                    if (distance < 3) break;
                    else if (distance < 5) dmg = Math.round(dmg * DamageLevel);
                    else dmg = Math.round(dmg * (DamageLevel * 1.2f));
                    break;
            }}

        enemy.damage(dmg, curUser);
    }


    private void SpawnEX44(int point) {
        if (Actor.findChar(point) == null && Dungeon.level.passable[point]) {
            int augtype;
            switch (WeaponAug()) {
                case NONE: default:
                    augtype = 0;
                    break;
                case DAMAGE:
                    augtype = 1;
                    break;
                case SPEED:
                    augtype = 2;
                    break;
            }

            EX44 w = new EX44();
            w.pos = point;
            w.setting(Dungeon.hero, this.level(), augtype);

            if (arts == 1) Buff.affect(w, WarCatBuff1.class);
            else if (arts == 2) Buff.affect(w, WarCatBuff2.class);

            GameScene.add( w );

        }
    }

    public Weapon.Augment WeaponAug() {
        if (Dungeon.hero.belongings.weapon == null) return Weapon.Augment.NONE;
        if (Dungeon.hero.belongings.weapon instanceof Pickaxe) return Weapon.Augment.NONE;
        Weapon.Augment wep = ((MeleeWeapon)curUser.belongings.weapon).augment;
        return wep;
    }

    public static class WarCatBuff1 extends Buff {}
    public static class WarCatBuff2 extends Buff {}
    public static class WarCatArts3{};

    public static class EX44 extends Mob {
        {
            spriteClass = RatSprite.class;

            state = HUNTING;

            properties.add(Property.IMMOVABLE);
            alignment = Alignment.ALLY;

            immunities.add(WandOfCorruption.class);
            immunities.add(Terror.class);
            immunities.add(Amok.class);
        }

        private int lifecount = 30;
        private int weaponAug;

        public void setting(Hero hero, int GearLevel, int AugType)
        {
            CustomeSet.CustomSetBuff setBuff = Dungeon.hero.buff( CustomeSet.CustomSetBuff.class);
            int itembuff = 0;
            if (setBuff != null) itembuff = setBuff.itemLevel();

            int armorlevel = 0;
            if (hero.belongings.armor != null) armorlevel = hero.belongings.armor.level();

            if (AugType != 3) HP=HT=30 + (armorlevel*6) + (itembuff*3);
            else HP=HT=2 + (armorlevel / 2) + (itembuff/5);

            maxLvl = GearLevel + (itembuff/3);

            if (AugType == 1) lifecount = 40;

            weaponAug = AugType;
        }

        @Override
        public int damageRoll() {
            int dmg = Random.NormalIntRange( 3, 5+(maxLvl*3));
            if (weaponAug == 1) dmg *= 1.35f;
            else if (weaponAug == 2) dmg *= 0.7f;

            return dmg; }

        @Override
        public void damage(int dmg, Object src) {

            if (buff(WarCatBuff2.class) != null) {
                dmg /= 4;
                Buff.detach(this, WarCatBuff2.class);
            }

            if (weaponAug == 3) dmg = 1;
            super.damage(dmg, src);
        }

        @Override
        public int drRoll() { return Random.NormalIntRange( 0, maxLvl ); }

        @Override
        public int attackSkill(Char target) {
            return 15 + maxLvl; }

        @Override
        protected boolean canAttack(Char enemy) {
            if (weaponAug == 2) return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 2;
            else return super.canAttack(enemy);
        }

        @Override
        public int defenseSkill(Char enemy) { return 0; }

        @Override
        protected boolean act() {
            lifecount--;
            if (buff(WarCatBuff1.class) != null && lifecount > 15) lifecount--;
            if (lifecount < 1) {
                this.die(this);
                return true;
            }

            return super.act();
        }

        @Override
        public void die(Object cause) {
            if (cause == this) {
                if (Dungeon.hero.belongings.getItem(AnnihilationGear.class) != null) {
                    AnnihilationGear Gear = Dungeon.hero.belongings.getItem(AnnihilationGear.class);
                    Gear.SPCharge(1);
                }
            }
            else if(cause instanceof WarCatArts3) {
                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
                    if (ch != null && ch.isAlive() && !(ch instanceof Hero) && !(ch instanceof EX44)) {
                        int damage = damageRoll() * 2;
                        ch.damage( damage, this );
                        Buff.affect(ch, Weakness.class, 5f);
                    }
                }
                if (Dungeon.hero.belongings.getItem(AnnihilationGear.class) != null) {
                    AnnihilationGear Gear = Dungeon.hero.belongings.getItem(AnnihilationGear.class);
                    Gear.SPCharge(1);
                }
            }
            super.die(cause);
        }

        @Override
        public int attackProc(Char enemy, int damage) {

            return super.attackProc(enemy, damage);
        }

        {
            immunities.add( Paralysis.class );
            immunities.add( Amok.class );
            immunities.add( Sleep.class );
            immunities.add( Terror.class );
            immunities.add( Vertigo.class );
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }

        private static final String LIFE = "lifecount";
        private static final String AUG = "weaponAug";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LIFE, lifecount);
            bundle.put(AUG, weaponAug);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            lifecount = bundle.getInt(LIFE);
            weaponAug = bundle.getInt(AUG);
            enemySeen = true;
        }
    }
}
