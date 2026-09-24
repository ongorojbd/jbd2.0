package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.levels.HospitalLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DoobieSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

/**
 * Doobie Wah!, HospitalLevel's boss. Spawns behind the ambulance once the last section is done
 * and chases the hero down the corridor the ambulance has bored; the deck turrets and the hero
 * wear it down together, and the level clears when it falls. It locks onto the hero's breath,
 * so it always knows where they are - it never loses track and never switches targets.
 */
public class DoobieWah extends Mob {

    {
        spriteClass = DoobieSprite.class;

        HP = HT = 4000;
        defenseSkill = 20;

        EXP = 100;
        maxLvl = 30;

        flying = true;
        viewDistance = 12;

        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);

        immunities.add(Sleep.class);
        immunities.add(MagicalSleep.class);
        immunities.add(Paralysis.class);
        immunities.add(Vertigo.class);
        immunities.add(Cripple.class);
        immunities.add(Dread.class);
        immunities.add(Terror.class);
        immunities.add(Chill.class);
    }

    //the whirlwind winds itself tighter as it is worn down: at full health it is its base size
    //and damage, at death's door it is MAX_SCALE across and hits twice as hard
    public static final float BASE_SCALE = 0.6f;
    public static final float MAX_SCALE = 2.5f;

    //0 at full health, 1 when about to fall
    public float rage() {
        if (HT <= 0) return 0f;
        return 1f - (HP / (float) HT);
    }

    public float sizeScale() {
        return BASE_SCALE + (MAX_SCALE - BASE_SCALE) * rage();
    }

    //30-50 at full health, winding up to 100-130 once it is nearly spent
    @Override
    public int damageRoll() {
        float rage = rage();
        return Random.NormalIntRange(Math.round(30 + 70 * rage), Math.round(50 + 80 * rage));
    }

    //winds up to triple speed as it is worn down: a nearly spent whirlwind cannot be outrun,
    //it takes three steps for each of the hero's
    @Override
    public float speed() {
        return super.speed() * (1f + 3f * rage());
    }

    @Override
    public int attackSkill(Char target) {
        return 45;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(5, 10);
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        //locked onto the hero: re-aimed every turn, so it heads for them even out of sight and
        //never wanders off or turns on the patient instead
        if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
            aggro(Dungeon.hero);
            target = Dungeon.hero.pos;
        }

        return super.act();
    }

    private boolean bleeding = false;

    public void damage(int dmg, Object src) {

        BossHealthBar.assignBoss(this);

        if ((HP*2 <= HT) && !bleeding){
            bleeding = true;
            BossHealthBar.bleed(true);
        }

        if (dmg >= 250) {
            dmg = 250;
        }

        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        if (Dungeon.level instanceof HospitalLevel) {
            ((HospitalLevel) Dungeon.level).onBossDefeated();
        }

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new YasuSprite(), new YasuSprite()},
                new String[]{"히로세 야스호", "히로세 야스호"},
                new String[]{
                        Messages.get(DoobieWah.class, "3"),
                        Messages.get(DoobieWah.class, "4"),
                },
                new byte[]{
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE
                }
        );

        Music.INSTANCE.end();
        GameScene.bossSlain();
    }
}
