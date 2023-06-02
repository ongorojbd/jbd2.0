package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Madeinheaven;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ThirdBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo1;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo3;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo4;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo5;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo6;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo7;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo8;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo9;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Sleepcmoon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscE;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Highway;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Map3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sleepcmoon3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDestOrb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SoftTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.World21Sprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NitoDismantleHammer extends Item {
    public static final String AC_DISMANTLE	= "DISMANTLE";

    {
        image = ItemSpriteSheet.WALL;

        defaultAction = AC_DISMANTLE;

        stackable = true;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DISMANTLE);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);

        NitoDismantleHammer pick = Dungeon.hero.belongings.getItem(  NitoDismantleHammer.class );

        if (Dungeon.depth > 20) {
            Sample.INSTANCE.play(Assets.Sounds.HAHAH);
            GLog.n( Messages.get(this, "rev") );
            GameScene.flash(0xFFFF00);
            Sample.INSTANCE.play( Assets.Sounds.BLAST, 2, Random.Float(0.33f, 0.66f) );
            Camera.main.shake(10, 3f);

            Madeinheaven n = new Madeinheaven();

            pick.detach( Dungeon.hero.belongings.backpack );

            if (n.doPickUp( Dungeon.hero )) {
                GLog.i( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", n.name()) ));
            } else {
                Dungeon.level.drop(n, Dungeon.hero.pos ).sprite.drop();
            }
            Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);
        } else if (action.equals(AC_DISMANTLE)) {
        GameScene.selectItem(itemSelector);
    }

    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(NitoDismantleHammer.class, "prompt");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof MeleeWeapon && !item.isEquipped(curUser)||
                    item instanceof Armor && ((Armor) item).checkSeal() == null && !item.isEquipped(curUser) || item instanceof Ring && !item.isEquipped(curUser);
        }

        @Override
        public void onSelect(Item item) {
            if (item == null) {

            } else
            {

                item.detach(curUser.belongings.backpack);
                if (item.level() > 0) {
                    GLog.p(  "+" + item.level()*3 + " 죠죠 포인트!");
                    Dungeon.energy += item.level()*3;
                } else {
                    Dungeon.energy += 2;
                    GLog.p(  "+2 죠죠 포인트!");
                }
                updateQuickslot();

                if (Statistics.manga < 3) {
                   if (Random.Int( 33 ) == 0) {
                   Statistics.manga += 1;
                   new Flare(6, 32).color(0xFFAA00, true).show(hero.sprite, 4f);
                   Sample.INSTANCE.play(Assets.Sounds.MASTERY, 0.7f, 1.2f);
                   GameScene.flash(0xFFCC00);
                switch (Random.Int(9)){

                    case 0:
                        Item a = new Jojo1();
                        Dungeon.level.drop(a, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "1"));
                        break;
                    case 1:
                        Item b = new Jojo2();
                        Dungeon.level.drop(b, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "2"));
                        break;
                    case 2:
                        Item c = new Jojo3();
                        Dungeon.level.drop(c, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "3"));
                        break;
                    case 3:
                        Item d = new Jojo4();
                        Dungeon.level.drop(d, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "4"));
                        break;
                    case 4:
                        Item e = new Jojo5();
                        Dungeon.level.drop(e, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "5"));
                        break;
                    case 5:
                        Item f = new Jojo6();
                        Dungeon.level.drop(f, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "6"));
                        break;
                    case 6:
                        Item g = new Jojo7();
                        Dungeon.level.drop(g, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "7"));
                        break;
                    case 7:
                        Item h = new Jojo8();
                        Dungeon.level.drop(h, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "8"));
                        break;
                    case 8:
                        Item i = new Jojo9();
                        Dungeon.level.drop(i, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                        GLog.h(Messages.get(NitoDismantleHammer.class, "9"));
                        break;
                }}}

                if (hero.belongings.getItem(Jojo7.class) != null) {
                    GameScene.flash(0x80FFFFFF);
                    Buff.affect(Dungeon.hero, Swiftthistle.TimeBubble.class).bufftime(5f);
                }

                if (hero.belongings.getItem(Jojo8.class) != null) {
                int dmg = curUser.HP / 5;
                curUser.HP -= dmg;
                Buff.affect(curUser, Barrier.class).incShield(dmg * 4);
                Camera.main.shake(2, 0.25f);
                }

                if (hero.belongings.getItem(Jojo9.class) != null) {
                Splash.at( DungeonTilemap.tileCenterToWorld( hero.pos ), -PointF.PI/2, PointF.PI/2, 0x5bc1e3, 100, 0.01f);
                GameScene.add(Blob.seed(Dungeon.hero.pos, 300, StormCloud.class));
                Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);
                hero.sprite.showStatus(CharSprite.POSITIVE, "[노멤버 레인]");
                }

                Sample.INSTANCE.play(Assets.Sounds.NITOH);
                curUser.sprite.operate(curUser.pos);
                Camera.main.shake(2, 0.25f);

                Statistics.duwang3++;

                CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.ROCK ), 10 );

            }
        }
    };







    public static class Mon3tr extends Mob {
        {
            spriteClass = World21Sprite.class;
            baseSpeed = 3f;

            state = HUNTING;
            alignment = Alignment.ALLY;

            WANDERING = new Wandering();
        }

        private int blinkCooldown = 0;

        @Override
        protected boolean getCloser( int target ) {
            if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && blinkCooldown <= 0 && target != Dungeon.hero.pos) {

                blink( target );
                spend( -1 / speed() );
                return true;

            } else {

                blinkCooldown--;
                return super.getCloser( target );

            }
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
                    target = Dungeon.hero.pos;
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

        private void blink( int target ) {

            Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
            int cell = route.collisionPos;

            //can't occupy the same cell as another char, so move back one.
            if (Actor.findChar( cell ) != null && cell != this.pos)
                cell = route.path.get(route.dist-1);

            if (Dungeon.level.avoid[ cell ]){
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int n : PathFinder.NEIGHBOURS8) {
                    cell = route.collisionPos + n;
                    if (Dungeon.level.passable[cell] && Actor.findChar( cell ) == null) {
                        candidates.add( cell );
                    }
                }
                if (candidates.size() > 0)
                    cell = Random.element(candidates);
                else {
                    blinkCooldown = 1;
                    return;
                }
            }

            ScrollOfTeleportation.appear( this, cell );
            Sample.INSTANCE.play( Assets.Sounds.DIEGO );

            blinkCooldown = 1;
        }

        @Override
        protected boolean act() {
            if (this.buff(StoneOfAggression.Aggression.class) == null) {
                Buff.prolong(this, StoneOfAggression.Aggression.class, StoneOfAggression.Aggression.DURATION);}

            if (isAlive() || HP <= 1) {

                    int adddamage = HT /50;
                    if (adddamage < 1) adddamage = 1;
                    if (state == WANDERING && adddamage > 1) HP -= adddamage / 2;
                    else  HP -= adddamage;

            }

            if (HP < 1) {
                this.die(this);
                return true;
            }
            return super.act();
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            HP -= HT/20;

            return super.attackProc(enemy, damage);
        }

        @Override
        public void die(Object cause) {

            super.die(cause);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 18 + maxLvl * 4, 24 + maxLvl * 6 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 50;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(maxLvl / 2, 2 + maxLvl);
        }

        public void setting(int setlvl)
        {
            HP=HT=120 + depth * 5;
            defenseSkill = 10 + setlvl * 2;
            maxLvl = setlvl + depth / 2;
        }

        private static final String BLINK = "blinkcooldown";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BLINK, blinkCooldown);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            blinkCooldown = bundle.getInt(BLINK);
            enemySeen = true;
        }
    }

}