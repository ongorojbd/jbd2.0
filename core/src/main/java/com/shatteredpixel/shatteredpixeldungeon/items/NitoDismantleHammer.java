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
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Map3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sleepcmoon3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
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

            if (action.equals(AC_DISMANTLE)) {
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
                    GLog.p(  "+" + item.level()*2 + " 죠죠 포인트!");
                    Dungeon.energy += item.level()*2;
                } else {
                    Dungeon.energy += 1;
                    GLog.p(  "+1 죠죠 포인트!");
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

                if (hero.belongings.getItem(Jojo7.class) != null && hero.belongings.getItem(Jojo8.class) != null || hero.belongings.getItem(Jojo7.class) != null && hero.belongings.getItem(Jojo9.class) != null || hero.belongings.getItem(Jojo8.class) != null && hero.belongings.getItem(Jojo9.class) != null) {
                int dmg = curUser.HP / 5;
                curUser.HP -= dmg;
                Buff.affect(curUser, Barrier.class).incShield(dmg * 4);
                Camera.main.shake(2, 0.25f);
                }

                if (hero.belongings.getItem(Jojo8.class) != null) {
                    if (Random.Int( 5 ) == 0) {
                        new Flare(6, 32).color(0x9966FF, true).show(hero.sprite, 4f);
                        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                        GameScene.flash(0x80FFFFFF);
                        switch (Random.Int(8)){
                            case 0:
                                Item a = new Kingt();
                                if (a.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", a.name()) ));
                                } else {
                                    Dungeon.level.drop(a, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                            case 1:
                                Item b = new StoneOfAdvanceguard();
                                if (b.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", b.name()) ));
                                } else {
                                    Dungeon.level.drop(b, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                            case 2:
                                Item c = new Xray();
                                if (c.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", c.name()) ));
                                } else {
                                    Dungeon.level.drop(c, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                            case 3:
                                Item d = new Kings();
                                if (d.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", d.name()) ));
                                } else {
                                    Dungeon.level.drop(d, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                            case 4:
                                Item e = new Kingm();
                                if (e.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", e.name()) ));
                                } else {
                                    Dungeon.level.drop(e, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                            case 5:
                                Item f = new Kingw();
                                if (f.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", f.name()) ));
                                } else {
                                    Dungeon.level.drop(f, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                            case 6:
                                Item g = new Kingc();
                                if (g.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", g.name()) ));
                                } else {
                                    Dungeon.level.drop(g, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                            case 7:
                                Item h = new Kinga();
                                if (h.doPickUp( Dungeon.hero )) {
                                    GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", h.name()) ));
                                } else {
                                    Dungeon.level.drop(h, Dungeon.hero.pos ).sprite.drop();
                                }
                                break;
                        }

                    }

                }

                if (hero.belongings.getItem(Jojo9.class) != null) {
                Splash.at( DungeonTilemap.tileCenterToWorld( hero.pos ), -PointF.PI/2, PointF.PI/2, 0x5bc1e3, 100, 0.01f);
                GameScene.add(Blob.seed(Dungeon.hero.pos, 300, StormCloud.class));
                Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);
                hero.sprite.showStatus(CharSprite.POSITIVE, "[노멤버 레인]");
                }

                if (hero.belongings.getItem(Jojo7.class) != null) {
                    Dungeon.gold += 100;
                    GLog.h(  "+100골드 획득!");
                    Sample.INSTANCE.play(Assets.Sounds.GOLD);
                    CellEmitter.get(curUser.pos).burst(Speck.factory(Speck.COIN), 12);
                }


                Sample.INSTANCE.play(Assets.Sounds.NITOH);
                curUser.sprite.operate(curUser.pos);
                Camera.main.shake(2, 0.25f);

                Statistics.duwang3++;

                CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.ROCK ), 10 );

            }
        }
    };

}