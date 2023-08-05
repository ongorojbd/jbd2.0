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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WoolParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.NitoDismantleHammer2;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Diomap;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo1;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo3;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo4;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo5;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo6;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo7;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo8;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo9;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscC;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Newro;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BeeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpeedwagonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class SpeedWagon extends DirectableAlly {

    {
        spriteClass = SpeedwagonSprite.class;

        HP = HT = 85;
        defenseSkill = 5;
        viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
        immunities.add(AllyBuff.class);
    }

    @Override
    public String description() {
        String desc = super.description();

            desc += "\n" + Messages.get(this, "p1", this.HP,this.HT);

        return desc;
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        if (Random.Int(25) == 0) {
            SpellSprite.show(this, SpellSprite.HASTE);
            Sample.INSTANCE.play(Assets.Sounds.SPW2);

            switch (Random.Int(9)) {
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
                    Item g = new ScrollOfChallenge();
                    yell(Messages.get(this, "find", g.name()));
                    Dungeon.level.drop(g, this.pos).sprite.drop();
                    break;
                case 8:
                    Item h = new Newro();
                    yell(Messages.get(this, "find", h.name()));
                    Dungeon.level.drop(h, this.pos).sprite.drop();
                    break;
            }
        }


        return damage;
    }


    @Override
    public void defendPos(int cell) {
        Sample.INSTANCE.play(Assets.Sounds.SPW2);
        yell(Messages.get(this, "g" + Random.IntRange(1, 5)));
        super.defendPos(cell);
    }

    @Override
    public void followHero() {
        Sample.INSTANCE.play(Assets.Sounds.SPW2);
        yell(Messages.get(this, "f" + Random.IntRange(1, 5)));
        super.followHero();
    }

    @Override
    public void targetChar(Char ch) {
        Sample.INSTANCE.play(Assets.Sounds.SPW2);
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
        return result;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        Dungeon.hero.damage(9999, this);

        if (!Dungeon.hero.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(Char.class, "kill", name()));
        }

        yell( Messages.get(this, "death") );
        GLog.h(Messages.get(SpeedWagon.class, "death2"));
    }

    public void sayAppeared(){
            int depth = (Dungeon.depth - 1) / 5;

            //only some lines are said on the first floor of a depth
            int variant = Dungeon.depth % 5 == 1 ? Random.IntRange(1, 3) : Random.IntRange(1, 6);

            switch (depth) {
                case 0:
                    yell( Messages.get( this, "dialogue_sewers_" + variant ));
                    break;
                case 1:
                    yell( Messages.get( this, "dialogue_prison_" + variant ));
                    break;
                case 2:
                    yell( Messages.get( this, "dialogue_caves_" + variant ));
                    break;
                case 3:
                    yell( Messages.get( this, "dialogue_city_" + variant ));
                    break;
                case 4:
                    yell( Messages.get( this, "dialogue_halls_" + variant ));
                    break;
                case 5: default:
                    yell( Messages.get( this, "dialogue_labs_" + variant ));
                    break;
            }

        if (ShatteredPixelDungeon.scene() instanceof GameScene) {
            Sample.INSTANCE.play( Assets.Sounds.GHOST );
        }
    }


}