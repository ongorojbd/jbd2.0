package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscB;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class WoundsofWar extends Artifact {
    {
        image = ItemSpriteSheet.SKULL;

        levelCap = 10;

        charge = 0;
        partialCharge = 0;
        chargeCap = 1 + level() / 5;
        identify();
        defaultAction = AC_SNAP;
    }

    public static final String AC_SNAP = "SNAP";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && !cursed)
            actions.add(AC_SNAP);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SNAP)) {
            if (activeBuff == null) {
                if (!isEquipped(hero))
                    GLog.w(Messages.get(Artifact.class, "need_to_equip"));
                else if (cursed) GLog.w(Messages.get(this, "cursed"));
                else if (charge < 1) GLog.w(Messages.get(this, "no_charge"));
                else {

                    {
                        {

                           {

                                for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                                    if (Dungeon.level.heroFOV[mob.pos]) {
                                        Buff.affect( mob, MagicalSleep.class );
                                        mob.sprite.centerEmitter().start( Speck.factory( Speck.NOTE ), 0.3f, 5 );
                                    }
                                }
                            }
                        }
                    }
                    charge -= 1;
                    GameScene.flash( 0x000033 );
                    Sample.INSTANCE.play( Assets.Sounds.CURSED, 2, 0.33f );
                    updateQuickslot();
                    GLog.p(Messages.get(this, "req"));

                    exp+=50;
                    if (exp >= 50 + level() * 50 && level() < levelCap) upgrade();

                    curUser.spendAndNext(1f);
                }
            }
        }
    }

    @Override
    public void level(int value) {
        super.level(value);
        chargeCap = 1 + level()/5;
    }

    @Override
    public Item upgrade() {
        super.upgrade();
        chargeCap = 1 + level()/5;
        return this;
    }

    @Override
    protected ArtifactBuff passiveBuff() { return new WoundsofWar.cameraRecharge();
    }

    public class cameraRecharge extends ArtifactBuff {
        public boolean act() {
            LockedFloor lock = target.buff(LockedFloor.class);
            if (activeBuff == null && (lock == null || lock.regenOn()) && !(Dungeon.depth >= 50 && Dungeon.depth <= 55)) {
                if (charge < chargeCap && !cursed) {
                    // 200 턴마다 100%충전 (기본)
                    float chargeGain = 0.005f + level() * 0.0001f;
                    chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                    partialCharge += chargeGain;

                    if (partialCharge > 1 && charge < chargeCap) {
                        partialCharge--;
                        charge++;
                        updateQuickslot();
                    }
                }
            } else partialCharge = 0;

            spend(TICK);
            return true;
        }

        @Override
        public void charge(Hero target, float amount) {
            charge += Math.round(0.1 * amount);
            charge = Math.min(charge, chargeCap);
            updateQuickslot();
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{BossdiscB.class};
            inQuantity = new int[]{1};

            cost = 0;

            output = WoundsofWar.class;
            outQuantity = 1;
        }
    }
}
