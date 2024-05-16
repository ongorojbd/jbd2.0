package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class PINK extends MeleeWeapon {
    {
        image = ItemSpriteSheet.PINK;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1.03f;

        tier = 5;
        ACC = 1.20f; //20% boost to accuracy
    }

    @Override
    public int max(int lvl) {
        return  4*(tier) +    		//12 base
                lvl*(tier-1);     	//+3 per level
    }

    private void MindBrack(Hero hero) {
        PathFinder.buildDistanceMap(hero.pos, BArray.not(Dungeon.level.solid, null), 2);
        for (int cell = 0; cell < PathFinder.distance.length; cell++) {
            if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                CellEmitter.get( cell ).burst( Speck.factory( Speck.MASK ), 10 );
                Char ch = Actor.findChar(cell);
                if (ch != null&& !(ch instanceof Hero) && ch instanceof Mob && ch.alignment != Char.Alignment.ALLY) {
                    if (!ch.isImmune(Corruption.class)) {
                        boolean chance = true;

                        if (chance)Buff.affect(ch, Corruption.class);

                        boolean droppingLoot = ch.alignment != Char.Alignment.ALLY;

                        if (ch.buff(Corruption.class) != null){
                            if (droppingLoot) ((Mob)ch).rollToDropLoot();
                            Statistics.enemiesSlain++;
                            Badges.validateMonstersSlain();
                            Statistics.qualifiedForNoKilling = false;
                            if (((Mob)ch).EXP > 0 && curUser.lvl <= ((Mob)ch).maxLvl) {
                                curUser.sprite.showStatus(CharSprite.POSITIVE, Messages.get(((Mob)ch), "exp", ((Mob)ch).EXP));
                                curUser.earnExp(((Mob)ch).EXP, ((Mob)ch).getClass());
                            } else {
                                curUser.earnExp(0, ((Mob)ch).getClass());
                            }
                        }
                    }

                }}}
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        switch (Random.Int(15)) {
            case 0: case 1: default:
                Buff.affect( defender, Weakness.class, 3f );
                break;
            case 2: case 3:
                Buff.affect( defender, Vulnerable.class, 3f );
                break;
            case 4:
                Buff.affect( defender, Cripple.class, 3f );
                break;
            case 5:
                Buff.affect( defender, Blindness.class, 3f );
                break;
            case 6:
                Buff.affect( defender, Terror.class, 3f );
                break;
            case 7: case 8: case 9:
                Buff.affect( defender, Amok.class, 3f );
                break;
            case 10: case 11:
                Buff.affect( defender, Slow.class, 3f );
                break;
            case 12: case 13:
                Buff.affect( defender, Hex.class, 3f );
                break;
            case 14:
                Buff.affect( defender, Paralysis.class, 3f );
                break;
        }
        if (Random.Int(100) <= Math.min(buffedLvl(), 9)) {					//1% base, +1% per lvl, max 10%
            Buff.affect( defender, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom.class);
        }

        if (attacker instanceof Hero) {
            if (hero.belongings.getItem(RingOfHaste.class) != null) {
                if (hero.belongings.getItem(RingOfHaste.class).isEquipped(hero)) {
                    if (Random.Int( 100 ) == 0) {
                        MindBrack(hero);
                        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                        attacker.sprite.showStatus(CharSprite.POSITIVE, "[세이프티 록]");
                    }
                }
            }
        }

        return damage;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{WarHammer.class, WandOfCorruption.class};
            inQuantity = new int[]{1, 1};

            cost = 15;

            output = PINK.class;
            outQuantity = 1;
        }
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero.belongings.getItem(RingOfHaste.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfHaste.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( PINK.class, "setbouns");}

        return info;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(6+1.5*lvl) damage, roughly +40% base dmg, +45% scaling
        int dmgBoost = augment.damageFactor(6 + Math.round(1.5f*buffedLvl()));
        Mace.heavyBlowAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 6 + Math.round(1.5f*buffedLvl()) : 6;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

}