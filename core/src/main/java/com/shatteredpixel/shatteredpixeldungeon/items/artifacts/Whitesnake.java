/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Awakensnake;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Whitesnake extends Artifact {

    {
        image = ItemSpriteSheet.SWORD;

        levelCap = 14;

        unique = true;

    }

    public static final String AC_PRICK = "PRICK";

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xCC0000, 1f);
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero )
                && !cursed
                && !hero.isInvulnerable(getClass())
                && hero.buff(MagicImmune.class) == null)
            actions.add(AC_PRICK);
        return actions;
    }

    @Override
    public String status() {
        return level() + "/" + 14;
        }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);

        if (action.equals(AC_PRICK)){

            int damage = 100;

            if (damage > hero.HP*0.75) {

                GameScene.show(
                        new WndOptions(new ItemSprite(this),
                                Messages.titleCase(name()),
                                Messages.get(this, "prick_warn"),
                                Messages.get(this, "yes"),
                                Messages.get(this, "no")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0)
                                    prick(Dungeon.hero);
                            }
                        }
                );

            } else {
                prick(hero);
            }
        }

    }

    private void prick(Hero hero){
        Whitesnake pick = Dungeon.hero.belongings.getItem(  Whitesnake.class );
        int damage = 100;

        Earthroot.Armor armor = hero.buff(Earthroot.Armor.class);
        if (armor != null) {
            damage = armor.absorb(damage);
        }

        WandOfLivingEarth.RockArmor rockArmor = hero.buff(WandOfLivingEarth.RockArmor.class);
        if (rockArmor != null) {
            damage = rockArmor.absorb(damage);
        }

        damage -= hero.drRoll();

        hero.sprite.operate( hero.pos );
        hero.busy();
        hero.spend(1f);

        if (level() == 0){
            GLog.p( Messages.get(this, "onprick") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 1){
            GLog.p( Messages.get(this, "onprick2") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 2){
            GLog.p( Messages.get(this, "onprick3") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 3){
            GLog.p( Messages.get(this, "onprick4") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 4){
            GLog.p( Messages.get(this, "onprick2") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 5){
            GLog.p( Messages.get(this, "onprick5") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 6){
            GLog.p( Messages.get(this, "onprick2") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 7){
            GLog.p( Messages.get(this, "onprick6") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 8){
            GLog.p( Messages.get(this, "onprick7") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 9){
            GLog.p( Messages.get(this, "onprick8") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 10){
            GLog.p( Messages.get(this, "onprick9") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 11){
            GLog.p( Messages.get(this, "onprick2") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 12){
            GLog.p( Messages.get(this, "onprick6") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
        }

        if (level() == 13)
            if (pick.isEquipped( Dungeon.hero )) {
            GLog.p( Messages.get(this, "onprick10") );
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
                pick.doUnequip( Dungeon.hero, false );
                pick.detach( Dungeon.hero.belongings.backpack );
                GLog.n( Messages.get(this, "onprick11") );

                Sample.INSTANCE.play( Assets.Sounds.HAHAH);
                GameScene.flash(0xFFFF00);

                Awakensnake n = new Awakensnake();


                n.cursedKnown = cursedKnown;
                n.cursed = cursed;

                Dungeon.quickslot.clearItem(curItem);
                curItem.updateQuickslot();

                if (n.doPickUp( Dungeon.hero )) {
                    GLog.h( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", n.name()) ));
                } else {
                    Dungeon.level.drop(n, Dungeon.hero.pos ).sprite.drop();
                }

                Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);

        }

        if (damage <= 0){
            damage = 1;
        } else {
            hero.sprite.emitter().burst( ShadowParticle.CURSE, 4+(damage/10) );
        }

        hero.damage(damage, this);

        if (!hero.isAlive()) {
            Badges.validateDeathFromFriendlyMagic();
            Dungeon.fail( getClass() );
            GLog.n( Messages.get(this, "ondeath") );
        } else {
            upgrade();
        }
    }

    @Override
    public Item upgrade() {

        return super.upgrade();
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Whitesnake.chaliceRegen();
    }

    @Override
    public void charge(Hero target, float amount) {

        target.HP = Math.min( target.HT, target.HP + 1);
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped (Dungeon.hero)){
            desc += "\n\n";
            if (cursed)
                desc += Messages.get(this, "desc_cursed");
            else
                desc += Messages.get(this, "desc_3");
        }

        return desc;
    }

    public class chaliceRegen extends ArtifactBuff {


    }


    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{ScrollOfAntiMagic.class};
            inQuantity = new int[]{1};

            cost = 30;

            output = Whitesnake.class;
            outQuantity = 1;
        }
    }

}
