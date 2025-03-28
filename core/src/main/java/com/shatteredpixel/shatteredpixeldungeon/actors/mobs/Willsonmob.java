package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DioDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillsonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillsonmobSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Willsonmob extends Mob {

    {
        spriteClass = WillsonmobSprite.class;

        HP = HT = 30;
        defenseSkill = 25;
        viewDistance = Light.DISTANCE;

        maxLvl = 25;
        baseSpeed = 2f;

    }

    public int Phase = 0;
    int summonCooldown = 7;
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private static final String SKILL2TIME = "BurstTime";
    private int BurstTime = 0;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, Phase);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
        bundle.put(SKILL2TIME, BurstTime);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        Phase = bundle.getInt(PHASE);
        summonCooldown = bundle.getInt(SUMMON_COOLDOWN);
        BurstTime = bundle.getInt(SKILL2TIME);
    }

    private static final String PHASE = "Phase";

    @Override
    public int attackSkill( Char target ) {
        return 999;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 25, 30 );
    }

    @Override
    protected boolean getCloser(int target) {

        target = Dungeon.hero.pos;

        return super.getCloser( target );
    }

    @Override
    protected boolean act() {

        if (Phase == 0) {
            WndDialogueWithPic.dialogue(
                    new CharSprite[]{new DioDialogSprite(), new WillsonSprite(), new DioDialogSprite(), new WillsonSprite()},
                    new String[]{"???", "상원의원", "???", "상원의원"},
                    new String[]{
                            Messages.get(this, "1"),
                            Messages.get(this, "2"),
                            Messages.get(this, "3"),
                            Messages.get(this, "4")

                    },
                    new byte[]{
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE,
                            WndDialogueWithPic.IDLE
                    }
            );

            Phase = 1;
        }

        return super.act();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        this.die(null);
        return damage;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        yell(Messages.get(Willsonmob.class, "die"));

    }
}
