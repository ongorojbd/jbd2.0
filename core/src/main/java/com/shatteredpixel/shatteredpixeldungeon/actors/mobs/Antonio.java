package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Retonio;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AntonioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EnemytonioSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Antonio extends Mob {

    {
        spriteClass = AntonioSprite.class;

        HP = HT = 299;

        if (Dungeon.depth == 13){
            HP = HT = 250;
        }

        if (Dungeon.depth == 18){
            HP = HT = 400;
        }

        defenseSkill = 25;
        EXP = 10;
        maxLvl = 25;

        properties.add(Property.BOSS);

    }
    private boolean seenBefore = false;
    public int  Phase = 0;
    int summonCooldown = 7;
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private static final String SKILL2TIME   = "BurstTime";
    private static int WIDTH = 33;
    private int BurstTime = 0;
    public static int[] kira = new int[]{ 4 + 13*WIDTH };

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, Phase );
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
        bundle.put( SKILL2TIME, BurstTime );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        Phase = bundle.getInt(PHASE);
        summonCooldown = bundle.getInt( SUMMON_COOLDOWN );
        BurstTime = bundle.getInt(SKILL2TIME);
    }

    private static final String PHASE   = "Phase";
    private static final float DELAY = 29f;

    @Override
    public int attackSkill( Char target ) {
        return 9999;
    }

    @Override
    public int damageRoll() {
        if (Dungeon.depth == 13) {
            return Random.NormalIntRange(17, 26);
        }else
        if (Dungeon.depth == 18) {
            return Random.NormalIntRange(28, 28);
        }
        return Random.NormalIntRange(15, 19);
    }

    @Override
    protected boolean act() {

        if (Dungeon.hero.buff(AscensionChallenge.class) != null){
            flee();
            return true;
        }

        return super.act();
    }

    public void flee() {
        destroy();

        CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);


                yell(Messages.get(Antonio.class, "2"));
                Dungeon.level.drop( new Kingt().identify(), pos ).sprite.drop( pos );


        }


}
