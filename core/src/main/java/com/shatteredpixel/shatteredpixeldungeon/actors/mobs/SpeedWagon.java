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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Newro;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HealingDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpeedwagonSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class SpeedWagon extends DirectableAlly {

    {
        spriteClass = SpeedwagonSprite.class;

        HP = HT = 100;
        defenseSkill = 5;
        viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
        immunities.add(AllyBuff.class);
    }

    private int sph = 1;

    @Override
    public String description() {
        String desc = super.description();

            desc += "\n" + Messages.get(this, "p1", this.HP,this.HT);

        return desc;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 50){
            dmg = 50;
        }

        super.damage(dmg, src);

        if (HP < 86 && sph == 1) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h1"));
            GLog.h(Messages.get(SpeedWagon.class, "t1", this.HP,this.HT));
            sph++;
        }
        else if (HP < 66 && sph == 2) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h2", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t2", this.HP,this.HT));
            sph++;
        }
        else if (HP < 46 && sph == 3) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h3", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t3", this.HP,this.HT));
            sph++;
        }
        else if (HP < 26 && sph == 4) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h4", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t4", this.HP,this.HT));
            sph++;
        }
        else if (HP < 11 && sph == 5) {
            Sample.INSTANCE.play(Assets.Sounds.SPW2);
            yell(Messages.get(this, "h5", this.HP,this.HT));
            GLog.h(Messages.get(SpeedWagon.class, "t5", this.HP,this.HT));
            sph++;
        }

    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        if (Random.Int(25) == 0) {
            SpellSprite.show(this, SpellSprite.MAP);
            Sample.INSTANCE.play(Assets.Sounds.SPW5);

            switch (Random.Int(8)) {
                case 0:
                    Item pick = new ScrollOfIdentify();
                    yell(Messages.get(this, "find", pick.name()));
                    Dungeon.level.drop(pick, this.pos).sprite.drop();
                    break;
                case 1:
                    Item a = new PotionOfHealing();
                    yell(Messages.get(this, "find", a.name()));
                    Dungeon.level.drop(a, this.pos).sprite.drop();
                    break;
                case 2:
                    Item b = new ScrollOfTransmutation();
                    yell(Messages.get(this, "find", b.name()));
                    Dungeon.level.drop(b, this.pos).sprite.drop();
                    break;
                case 3:
                    Item c = new ScrollOfMetamorphosis();
                    yell(Messages.get(this, "find", c.name()));
                    Dungeon.level.drop(c, this.pos).sprite.drop();
                    break;
                case 4:
                    Item d = new ScrollOfRemoveCurse();
                    yell(Messages.get(this, "find", d.name()));
                    Dungeon.level.drop(d, this.pos).sprite.drop();
                    break;
                case 5:
                    Item e = new Blandfruit();
                    yell(Messages.get(this, "find", e.name()));
                    Dungeon.level.drop(e, this.pos).sprite.drop();
                    break;
                case 6:
                    Item f = new PotionOfExperience();
                    yell(Messages.get(this, "find", f.name()));
                    Dungeon.level.drop(f, this.pos).sprite.drop();
                    break;
                case 7:
                    Item h = new Newro();
                    yell(Messages.get(this, "find", h.name()));
                    Dungeon.level.drop(h, this.pos).sprite.drop();
                    break;
                case 8:
                    Item g = new HealingDart();
                    yell(Messages.get(this, "find", g.name()));
                    Dungeon.level.drop(g, this.pos).sprite.drop();
                    break;
            }
        }

        return damage;
    }


    @Override
    public void defendPos(int cell) {
        Sample.INSTANCE.play(Assets.Sounds.SPW4);
        yell(Messages.get(this, "g" + Random.IntRange(1, 5)));
        super.defendPos(cell);
    }

    @Override
    public void followHero() {
        Sample.INSTANCE.play(Assets.Sounds.SPW5);
        yell(Messages.get(this, "f" + Random.IntRange(1, 5)));
        super.followHero();
    }

    @Override
    public void targetChar(Char ch) {
        Sample.INSTANCE.play(Assets.Sounds.SPW3);
        yell(Messages.get(this, "d" + Random.IntRange(1, 5)));
        super.targetChar(ch);
    }

    @Override
    public int attackSkill( Char target ) {
        return 11;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 3 );
    }

    @Override
    protected boolean act() {
        int oldPos = pos;
        boolean result = super.act();
        //partially simulates how the hero switches to idle animation
        if ((pos == target || oldPos == pos) && sprite.looping()){
            sprite.idle();
        }

        Dungeon.level.updateFieldOfView( this, fieldOfView );
        GameScene.updateFog(pos, viewDistance+(int)Math.ceil(speed()));

        return result;
    }

    public void sayHeroKilled(){
        yell( Messages.get( this, "z"));
        Sample.INSTANCE.play(Assets.Sounds.SPW5);
        GLog.newLine();
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        Sample.INSTANCE.play(Assets.Sounds.SPW5);
        Dungeon.hero.damage(9999, this);

        if (!Dungeon.hero.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(Char.class, "kill", name()));
        }

        yell( Messages.get(this, "death") );
        GLog.h(Messages.get(SpeedWagon.class, "death2"));
    }
}