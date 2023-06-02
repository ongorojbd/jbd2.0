package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.NitoDismantleHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ThirdBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscE;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscH;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Highway;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Map3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sleepcmoon3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDestOrb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MissileButton;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FdolTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Araki extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.ALCH_PAGE;

        stackable = true;

        defaultAction = AC_LIGHT;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_LIGHT );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute( hero, action );
        if (action.equals( AC_LIGHT )) {
        switch (Random.Int(9)){

            case 0:
                Item a = new Jojo1();
                Dungeon.level.drop(a, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "1"));
                break;
            case 1:
                Item b = new Jojo2();
                Dungeon.level.drop(b, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "2"));
                break;
            case 2:
                Item c = new Jojo3();
                Dungeon.level.drop(c, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "3"));
                break;
            case 3:
                Item d = new Jojo4();
                Dungeon.level.drop(d, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "4"));
                break;
            case 4:
                Item e = new Jojo5();
                Dungeon.level.drop(e, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "5"));
                break;
            case 5:
                Item f = new Jojo6();
                Dungeon.level.drop(f, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "6"));
                break;
            case 6:
                Item g = new Jojo7();
                Dungeon.level.drop(g, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "7"));
                break;
            case 7:
                Item h = new Jojo8();
                Dungeon.level.drop(h, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "8"));
                break;
            case 8:
                Item i = new Jojo9();
                Dungeon.level.drop(i, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                GLog.h(Messages.get(Araki.class, "9"));
                break;
        }

        new Flare(6, 32).color(0xFFAA00, true).show(hero.sprite, 4f);
        Sample.INSTANCE.play(Assets.Sounds.MASTERY, 0.7f, 1.2f);
        GameScene.flash(0xFFCC00);

        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x99FFFF, 1f);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 50 * quantity;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{BossdiscH.class};
            inQuantity = new int[]{1};

            cost = 0;

            output = Araki.class;
            outQuantity = 1;
        }
    }
}
