package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpiritHorseSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HorseRiding extends Buff implements ActionIndicator.Action, Hero.Doom {

    {
        revivePersists = true;

        announced = true;
    }

    private HorseAlly horse = null;
    private int horseHP = 0;
    private int horseHT = 0;

    public void set() {
        horseHT = (15+ hero.lvl*5);
        horseHP = horseHT;
    }

    public void set(int HP) {
        horseHT = (15+ hero.lvl*5);
        horseHP = HP;
    }

    public void onLevelUp() {
        horseHT = (15+ hero.lvl*5);
        BuffIndicator.refreshHero();
    }

    public void healHorse(int amount) {
        this.horseHP = Math.min(HorseRiding.this.horseHP + amount, HorseRiding.this.horseHT);;
    }

    @Override
    public int icon() {
        return BuffIndicator.SACRIFICE;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", horseHP, horseHT);
    }

    public void onDamage(int damage) {
        damage -= drRoll();
        damage = Math.max(damage, 0); //최소 0
        horseHP -= damage;
        if (horseHP <= 0) {
            detach();
            PixelScene.shake( 2, 1f );
            GLog.n(Messages.get(this, "fall"));
            float dmgMulti = 1;

            //The lower the hero's HP, the more bleed and the less upfront damage.
            //Hero has a 50% chance to bleed out at 66% HP, and begins to risk instant-death at 25%
            int bleedAmt = Math.round(target.HT / (6f + (6f*(target.HP/(float)target.HT))) * dmgMulti);
            int fallDmg = Math.round(Math.max( target.HP / 2, Random.NormalIntRange( target.HP / 2, target.HT / 4 ))*dmgMulti);
            Buff.affect( target, Bleeding.class).set( bleedAmt, RideFall.class);
            target.damage( fallDmg, new RideFall() );
            Buff.affect(target, RidingCooldown.class).set();
        }
    }

    public static int drRoll() {
        int baseDr = Random.NormalIntRange(2, 16); //기본 방어력: 2~16
        return baseDr; //추가 방어력: 특성 레벨~8*특성 레벨
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (horseHT - horseHP)/(float)horseHT);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(horseHP);
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.GLADIATOR;
    }

    @Override
    public int indicatorColor() {
        return 0x26058C;
    }

    @Override
    public void doAction() {
        spawnHorse();
    }

    @Override
    public boolean attachTo(Char target) {
        ActionIndicator.setAction(this);
        return super.attachTo(target);
    }

    @Override
    public void detach() {
        ActionIndicator.clearAction();
        super.detach();
    }

    private void spawnHorse() {
        Hero hero = (Hero) target;
        ArrayList<Integer> spawnPoints = new ArrayList<>();
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                spawnPoints.add(p);
            }
        }

        if (!spawnPoints.isEmpty()) {
            this.horse = new HorseAlly(hero, this.horseHP);

            horse.pos = Random.element(spawnPoints);

            GameScene.add(horse, 1f);
            Dungeon.level.occupyCell(horse);

            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            CellEmitter.get(horse.pos).start( Speck.factory(Speck.LIGHT), 0.2f, 3 );

            hero.spend(1f);
            hero.busy();
            hero.sprite.operate(hero.pos);
            Buff.affect(hero, Cripple2.class);

            detach();
        } else
            GLog.i( Messages.get(this, "no_space") );
    }

    public CellSelector.Listener dashDirector = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            if (cell == hero.pos) {
                spawnHorse();
            } else {
                spawnHorse();
            }
        }


        @Override
        public String prompt() {
            return Messages.get(HorseRiding.class, "direct_prompt");
        }
    };

    @Override
    public void onDeath() {
        Dungeon.fail( this );
        GLog.n( Messages.get(this, "ondeath") );
    }

    private static final String HORSE_HP = "horseHP";
    private static final String HORSE_HT = "horseHT";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(HORSE_HP, horseHP);
        bundle.put(HORSE_HT, horseHT);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        horseHP = bundle.getInt(HORSE_HP);
        horseHT = bundle.getInt(HORSE_HT);
    }

    public static class HorseAlly extends DirectableAlly {
        {
            spriteClass = SpiritHorseSprite.class;

            alignment = Alignment.ALLY;

            //before other mobs
            actPriority = MOB_PRIO + 1;

            followHero();
        }

        private float partialCharge = 0f;
        private int heroLvl = 0;

        public HorseAlly() {
            super();
        }

        public HorseAlly(Hero hero, int HP) {
            this.HT = (15+hero.lvl*5);
            this.defenseSkill = (hero.lvl+4);
            this.HP = HP;
            this.heroLvl = hero.lvl;
        }

        @Override
        protected boolean act() {
            if (this.HP < this.HT && Regeneration.regenOn()) {
                partialCharge += 0.1f;
                if (Dungeon.level.map[this.pos] == Terrain.GRASS) {
                    partialCharge += 0.4f; //풀 위에 있으면 회복 속도 5배
                }
                while (partialCharge > 1) {
                    this.HP++;
                    partialCharge--;
                }
            } else {
                partialCharge = 0;
            }
            if (hero != null && hero.lvl != this.heroLvl) updateHorse(hero);
            return super.act();
        }

        @Override
        public boolean canInteract(Char c) {
            return super.canInteract(c); //can use ALLY_WARP talent
        }

        @Override
        public boolean interact(Char c) {
            if (c instanceof Hero) {
                Buff.affect(c, HorseRiding.class).set(this.HP);
                Buff.detach(hero, Cripple2.class );
                destroy();
                sprite.die();
            }
            return true;
        }

        @Override
        public void die(Object cause) {
            Buff.affect(hero, RidingCooldown.class).set();
            super.die(cause);
        }

        @Override
        protected boolean canAttack(Char enemy) { //can't attack
            return false;
        }

        @Override
        public int damageRoll() {
            return 0;
        }

        public void updateHorse(Hero hero){
            //same dodge as the hero
            defenseSkill = (hero.lvl+4);
            HT = (15+hero.lvl*5);
            this.heroLvl = hero.lvl;
        }

        @Override
        public float speed() {
            float speed = super.speed();

            //moves 2 tiles at a time when returning to the hero
            if (state == WANDERING
                    && defendingPos == -1
                    && Dungeon.level.distance(pos, hero.pos) > 1){
                speed *= 2;
            }

            return speed;
        }

        @Override
        public int drRoll() {
            return HorseRiding.drRoll();
        }
    }

    public static class RideFall implements Hero.Doom {
        @Override
        public void onDeath() {
            Dungeon.fail( this );
            GLog.n( Messages.get(this, "ondeath") );
        }
    }

    public static class RidingCooldown extends Buff {

        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        private int coolDown;
        private int maxCoolDown;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x808080);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (maxCoolDown - coolDown)/(float)maxCoolDown);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(coolDown);
        }

        public void kill() {
            coolDown --;
            if (coolDown <= 0) {
                detach();
            }
            BuffIndicator.refreshHero();    //영웅의 버프창 갱신
        }

        public void set() {
            maxCoolDown = 5;
            coolDown = maxCoolDown;
        }

        @Override
        public void detach() {
            Buff.affect(target, HorseRiding.class).set();
            super.detach();
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", coolDown, maxCoolDown);
        }

        private static final String MAXCOOLDOWN = "maxCoolDown";
        private static final String COOLDOWN  = "cooldown";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(MAXCOOLDOWN, maxCoolDown);
            bundle.put(COOLDOWN, coolDown);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            maxCoolDown = bundle.getInt( MAXCOOLDOWN );
            coolDown = bundle.getInt( COOLDOWN );
        }
    }
}
