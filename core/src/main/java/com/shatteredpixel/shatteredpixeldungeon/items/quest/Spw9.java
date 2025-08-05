package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tendency;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUpgrade;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spw9 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.MAGIC_INFUSE;

        stackable = true;
        levelKnown = true;

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
            Spw9Ability();
        }
    }

    public static void Spw9Ability() {
        GameScene.selectItem(new WndBag.ItemSelector() {
            @Override
            public String textPrompt() {
                return Messages.get(Spw9.class, "prompt");
            }

            @Override
            public Class<?extends Bag> preferredBag(){
                return Belongings.Backpack.class;
            }

            @Override
            public boolean itemSelectable(Item item) {
                return item != null && item.isUpgradable();
            }

            @Override
            public void onSelect(final Item item) {
                if (item != null && item.isUpgradable()) {
                    try {
                        GameScene.show(new WndUpgrade(new Spw9Upgrader(), item, false));
                    } catch (Exception e) {
                        // 오류 발생 시 로그 출력 (디버깅용)
                        GLog.w("SPW9 업그레이드 오류: " + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    /**
     * Internal class to handle the upgrade functionality for SPW9
     */
    public static class Spw9Upgrader extends Item {
        {
            image = ItemSpriteSheet.MAGIC_INFUSE;
        }

        public void useAnimation(){
            hero.spend(1f);
            hero.busy();
            (hero.sprite).operate(hero.pos);

            Sample.INSTANCE.play(Assets.Sounds.READ);
            Invisibility.dispel();

            Catalog.countUse(Spw9.class);
            if (Random.Float() < 0.1f) { // 10% chance for talent
                Talent.onScrollUsed(hero, hero.pos, 2, Spw9.class);
            }
        }

        public Item upgradeItem( Item item ){
            ScrollOfUpgrade.upgrade(hero);

            Degrade.detach( hero, Degrade.class );

            if (item instanceof Weapon && ((Weapon) item).enchantment != null) {
                item = ((Weapon) item).upgrade(true);
            } else if (item instanceof Armor && ((Armor) item).glyph != null) {
                item = ((Armor) item).upgrade(true);
            } else {
                boolean wasCursed = item.cursed;
                boolean wasCurseInfused = item instanceof Wand && ((Wand) item).curseInfusionBonus;
                item = item.upgrade();
                if (wasCursed) item.cursed = true;
                if (wasCurseInfused) ((Wand) item).curseInfusionBonus = true;
            }

            GLog.p( Messages.get(Spw9.class, "upgrade_success") );
            Catalog.countUse(item.getClass());

            // SPW9 카운터 증가
            Statistics.spw9++;

            return item;
        }

        public void reShowSelector(){
            Spw9Ability();
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }
    }
}
