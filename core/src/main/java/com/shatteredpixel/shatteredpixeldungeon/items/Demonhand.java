package com.shatteredpixel.shatteredpixeldungeon.items;


import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Rohan;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Madeinheaven;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Sleepcmoon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SoftTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Demonhand extends Item {
    public static final String AC_DISMANTLE	= "DISMANTLE";
    public static final String AC_DISMANTLE2	= "DISMANTLE2";

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
        actions.add(AC_DISMANTLE2);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_DISMANTLE2))  {
            GameScene.show(new WndOptions(new ItemSprite(curItem),
                    Messages.titleCase(name()),
                    Messages.get(NitoDismantleHammer.class, "think"),
                    Messages.get(NitoDismantleHammer.class, "yes"),
                    Messages.get(NitoDismantleHammer.class, "no")) {
                @Override
                protected void onSelect(int index) {
                    switch (index) {
                        case 0:
                            if (Dungeon.gold > 999) {
                                Dungeon.gold -= 1000;
                            } else{
                                GLog.n( Messages.get(Demonhand.class, "rev") );
                            }
                            break;
                        case 1:

                            break;
                    }
                }
            });
        }


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
            return item instanceof MeleeWeapon && !item.isEquipped(curUser)|| item instanceof Ring && !item.isEquipped(curUser) ||
                    item instanceof Armor && ((Armor) item).checkSeal() == null && !item.isEquipped(curUser);
        }

        @Override
        public void onSelect(Item item) {
            if (item == null) {

            } else
            {
                item.detach(curUser.belongings.backpack);
                if (item.level() > 0) {
                    Dungeon.energy += item.level()*3;
                } else {
                    Dungeon.energy += 2;
                }
                updateQuickslot();

                Buff.affect(curUser, Invisibility.class, Invisibility.DURATION/3f);
                Buff.affect(curUser, Stamina.class, 5f);
                GameScene.add(Blob.seed(Dungeon.hero.pos, 300, StormCloud.class));

                new SoftTrap().set(curUser.pos).activate();
                new SoftTrap().set(curUser.pos).activate();

                int dmg = curUser.HP-1;

                curUser.HP = 1;
                Buff.affect(curUser, Barrier.class).incShield(dmg * 3);
                Buff.affect(curUser, Adrenaline.class, 2 + dmg / 4);

                Camera.main.shake(2, 0.25f);

                CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.ROCK ), 10 );

                Sample.INSTANCE.play(Assets.Sounds.SHEEP);
                curUser.sprite.operate(curUser.pos);

            }
        }
    };
}