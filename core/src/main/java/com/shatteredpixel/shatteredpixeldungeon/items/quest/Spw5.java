package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw2;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.SpwItem;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Spw5 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.EXOTIC_GYFU;

        icon = ItemSpriteSheet.Icons.SCROLL_ENCHANT;

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
            // SPW5는 무기나 방어구를 선택해서 인챈트/글리프 적용
            GameScene.selectItem(new WndBag.ItemSelector() {
                @Override
                public String textPrompt() {
                    return Messages.get(ScrollOfEnchantment.class, "inv_title");
                }

                @Override
                public Class<? extends Bag> preferredBag() {
                    return Belongings.Backpack.class;
                }

                @Override
                public boolean itemSelectable(Item item) {
                    return (item instanceof MeleeWeapon || item instanceof SpiritBow || item instanceof Armor);
                }

                @Override
                public void onSelect(final Item item) {
                    if (item instanceof Weapon) {
                        final Weapon.Enchantment enchants[] = new Weapon.Enchantment[3];
                        Class<? extends Weapon.Enchantment> existing = ((Weapon) item).enchantment != null ? ((Weapon) item).enchantment.getClass() : null;
                        enchants[0] = Weapon.Enchantment.randomCommon(existing);
                        enchants[1] = Weapon.Enchantment.randomUncommon(existing);
                        enchants[2] = Weapon.Enchantment.random(existing, enchants[0].getClass(), enchants[1].getClass());
                        GameScene.show(new SpwEnchantSelect((Weapon) item, enchants[0], enchants[1], enchants[2]));
                    } else if (item instanceof Armor) {
                        final Armor.Glyph glyphs[] = new Armor.Glyph[3];
                        Class<? extends Armor.Glyph> existing = ((Armor) item).glyph != null ? ((Armor) item).glyph.getClass() : null;
                        glyphs[0] = Armor.Glyph.randomCommon(existing);
                        glyphs[1] = Armor.Glyph.randomUncommon(existing);
                        glyphs[2] = Armor.Glyph.random(existing, glyphs[0].getClass(), glyphs[1].getClass());
                        GameScene.show(new SpwGlyphSelect((Armor) item, glyphs[0], glyphs[1], glyphs[2]));
                    }
                }
            });
        }
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
     * SPW용 무기 인챈트 선택 윈도우
     */
    public static class SpwEnchantSelect extends WndOptions {

        private Weapon weapon;
        private Weapon.Enchantment[] enchantments;

        public SpwEnchantSelect(Weapon weapon, Weapon.Enchantment ench1,
                                Weapon.Enchantment ench2, Weapon.Enchantment ench3) {
            super(new ItemSprite(new ScrollOfEnchantment()),
                    Messages.titleCase(new ScrollOfEnchantment().name()),
                    Messages.get(ScrollOfEnchantment.class, "weapon"),
                    ench1.name(),
                    ench2.name(),
                    ench3.name(),
                    Messages.get(ScrollOfEnchantment.class, "cancel"));
            this.weapon = weapon;
            enchantments = new Weapon.Enchantment[3];
            enchantments[0] = ench1;
            enchantments[1] = ench2;
            enchantments[2] = ench3;
        }

        @Override
        protected void onSelect(int index) {
            if (index < 3) {
                weapon.enchant(enchantments[index]);
                GLog.p(Messages.get(ScrollOfEnchantment.class, "weapon_success"));
                Sample.INSTANCE.play(Assets.Sounds.READ);
            }
        }

        @Override
        protected boolean hasInfo(int index) {
            return index < 3;
        }

        @Override
        protected void onInfo(int index) {
            GameScene.show(new WndTitledMessage(
                    Icons.get(Icons.INFO),
                    Messages.titleCase(enchantments[index].name()),
                    enchantments[index].desc()));
        }

        @Override
        public void onBackPressed() {
            // 뒤로가기 버튼으로 닫히는 것을 방지
        }
    }

    /**
     * SPW용 방어구 글리프 선택 윈도우
     */
    public static class SpwGlyphSelect extends WndOptions {

        private Armor armor;
        private Armor.Glyph[] glyphs;

        public SpwGlyphSelect(Armor armor, Armor.Glyph glyph1,
                              Armor.Glyph glyph2, Armor.Glyph glyph3) {
            super(new ItemSprite(new ScrollOfEnchantment()),
                    Messages.titleCase(new ScrollOfEnchantment().name()),
                    Messages.get(ScrollOfEnchantment.class, "armor"),
                    glyph1.name(),
                    glyph2.name(),
                    glyph3.name(),
                    Messages.get(ScrollOfEnchantment.class, "cancel"));
            this.armor = armor;
            glyphs = new Armor.Glyph[3];
            glyphs[0] = glyph1;
            glyphs[1] = glyph2;
            glyphs[2] = glyph3;
        }

        @Override
        protected void onSelect(int index) {
            if (index < 3) {
                armor.inscribe(glyphs[index]);
                GLog.p(Messages.get(ScrollOfEnchantment.class, "armor_success"));
                Sample.INSTANCE.play(Assets.Sounds.READ);
            }
        }

        @Override
        protected boolean hasInfo(int index) {
            return index < 3;
        }

        @Override
        protected void onInfo(int index) {
            GameScene.show(new WndTitledMessage(
                    Icons.get(Icons.INFO),
                    Messages.titleCase(glyphs[index].name()),
                    glyphs[index].desc()));
        }

        @Override
        public void onBackPressed() {
            // 뒤로가기 버튼으로 닫히는 것을 방지
        }
    }
}
