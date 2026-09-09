package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Triplespeed;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalGuardianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GiantSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/*
    A single relentless pursuer meant for VeiledSanctumLevel ("블루 하와이"). It's faster than the
    hero and, once it sees you, always notices regardless of distance or stealth (see
    RelentlessWandering), so the intended counterplay is still breaking line of sight rather than
    trading blows with it. It can't notice anything at all for its first HUNT_GRACE_TURNS turns,
    so the hero isn't immediately chased on entering the level. While actively HUNTING, there's a
    1-in-CHASE_SOUND_CHANCE chance per turn it plays a chase sting (Assets.Sounds.TG1) as an audio
    warning.

    It CAN be damaged and downed now (no more isInvulnerable()). Like CrystalGuardian, "killing"
    it just knocks it into a recovering state instead of a real death - HP floors at 1 and heals
    back up over a handful of turns, faster than CrystalGuardian's +5/100HP-per-turn regen both in
    turn count and as a % of max HP (see RECOVERY_PER_TURN). While it's down, the hero can walk
    straight through its tile instead of being blocked by the body (see the recovering() checks
    added to Hero.getCloser(), plus heroShouldInteract() below making a direct click swap places
    the same way walking into an ally does, instead of attacking) - otherwise a downed Warden
    sitting in a 1-wide doorway would wall the player out of that room for the whole recovery.

    It also can't be shut out with a Skeleton Key: canBreakThrough() (a new Mob hook added
    alongside this) lets it path through a Terrain.HERO_LKD_DR door as if it were open, and
    move() forces the door back open the moment it steps onto it - otherwise the hero could
    permanently lock it out of any room just by locking a door behind them.
*/
public class UnseenWarden extends Mob {

    {
        spriteClass = GiantSprite.class;

        HP = HT = 300;
        defenseSkill = 20;

        EXP = 0;
        maxLvl = -2;

        viewDistance = 14;

        state = WANDERING;

        properties.add(Property.UNDEAD);
        properties.add(Property.BOSS);
        properties.add(Property.STATIC);

        immunities.add(Sleep.class);
        immunities.add(MagicalSleep.class);
        immunities.add(Paralysis.class);

        WANDERING = new RelentlessWandering();
    }

    private static final int RECOVERY_PER_TURN = 20;

    //it doesn't start actively hunting until this many of its own turns have passed, so the
    //hero gets a breather right after entering the level instead of being chased immediately
    private static final int HUNT_GRACE_TURNS = 10;
    //1 in this many turns while HUNTING to play the chase sting
    private static final int CHASE_SOUND_CHANCE = 8;

    private boolean recovering = false;
    private int turnsAlive = 0;

    public boolean recovering() {
        return recovering;
    }

    //이 거리 안에 있으면 은신을 감지해서 투명화를 풀어버린다
    private static final int INVISIBILITY_SENSE_RANGE = 8;

    @Override
    protected boolean act() {
        if (distance(Dungeon.hero) <= INVISIBILITY_SENSE_RANGE && Dungeon.hero.buff(Invisibility.class) != null) {
            Invisibility.dispel(Dungeon.hero);
            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);
            GLog.w(Messages.get(this, "i"));
            Dungeon.hero.interrupt();
        }

        if (recovering) {
            HP = Math.min(HT, HP + RECOVERY_PER_TURN);
            if (sprite != null && Dungeon.level.heroFOV[pos]) {
                sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(RECOVERY_PER_TURN), FloatingText.HEALING);
            }
            if (HP == HT) {
                recovering = false;
                if (sprite instanceof GiantSprite) ((GiantSprite) sprite).endCrumple();
            }
            spend(TICK);
            return true;
        }

        if (turnsAlive < HUNT_GRACE_TURNS) {
            //유예 기간 동안은 beckon()조차 호출하지 않는다 - beckon()이 target을 플레이어 위치로
            //갱신해버리면 아직 HUNTING이 아니어도 Wandering.continueWandering()이 그 target을
            //향해 슬금슬금 다가가버려서, 결국 유예 기간 내내 가만히 있지 않고 접근해오게 된다
            turnsAlive++;
            spend(TICK);
            if (turnsAlive == 9) {
                SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);
                GLog.n(Messages.get(this, "d"));
                Sample.INSTANCE.play(Assets.Sounds.TG1);
                return true;
            }
            return true;
        }

        if (state == HUNTING && Random.Int(CHASE_SOUND_CHANCE) == 0 && Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.TG2);
        }

        beckon(Dungeon.hero.pos);

        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(35, 55);
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(5, 10);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        damage += enemy.HT / 5;
        return damage;
    }

    //항상 플레이어의 현재 이동 속도(헤이스트, 모멘텀 등 포함)에 비례해서 이 배율만큼 더 빠르다
    private static final float SPEED_MULTIPLIER = 1.15f;

    @Override
    public float speed() {
        float mult = SPEED_MULTIPLIER;
        //Triplespeed는 hero.speed()가 아니라 spend() 오버라이드로 적용되는 효과라
        //hero.speed()만 봐서는 반영되지 않는다 - 따로 체크해서 워든도 2배 빨라지게 한다
        if (Dungeon.hero.buff(Triplespeed.class) != null) {
            mult *= 2f;
        }
        baseSpeed = Dungeon.hero.speed() * mult;
        return super.speed();
    }

    @Override
    protected boolean canBreakThrough(int cell) {
        //otherwise the hero could just Skeleton-Key-lock a door behind them and permanently
        //wall it out of whatever room they're hiding in
        return Dungeon.level.map[cell] == Terrain.HERO_LKD_DR;
    }

    @Override
    public boolean[] modifyPassable(boolean[] passable) {
        //canBreakThrough() alone only covers the final "can I step onto this cell right now"
        //check - the A* route search itself (Dungeon.findPath -> findPassable -> this method)
        //still treats a locked door as a solid wall unless this also marks it passable, or it
        //never even considers a route through it and just stands there with nowhere to go
        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (Dungeon.level.map[i] == Terrain.HERO_LKD_DR) {
                passable[i] = true;
            }
        }
        return super.modifyPassable(passable);
    }

    @Override
    public void move(int step, boolean travelling) {
        super.move(step, travelling);
        if (Dungeon.level.map[pos] == Terrain.HERO_LKD_DR) {
            Level.set(pos, Terrain.DOOR);
            GameScene.updateMap(pos);
            if (Dungeon.level.heroFOV[pos]) {
                Sample.INSTANCE.play( Assets.Sounds.TG1 );
                Sample.INSTANCE.play( Assets.Sounds.BLAST );
                GLog.w("문이 뜯겨나갔다!");
            }
        }
    }

    @Override
    public int defenseSkill(Char enemy) {
        if (recovering) return 0;
        else            return super.defenseSkill(enemy);
    }

    @Override
    public boolean surprisedBy(Char enemy, boolean attacking) {
        if (recovering) return false;
        else            return super.surprisedBy(enemy, attacking);
    }

    @Override
    public boolean heroShouldInteract() {
        //while down, clicking it swaps places (same as walking into an ally) instead of
        //attacking a target that can't actually be finished off anyway
        return recovering || super.heroShouldInteract();
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (recovering) {
            //bypasses block while down, but can't actually finish it off - same trick
            //CrystalGuardian uses to stay "alive" through the recovery window
            if (sprite != null) {
                sprite.showStatusWithIcon(CharSprite.NEGATIVE, Integer.toString(damage), FloatingText.PHYS_DMG_NO_BLOCK);
            }
            HP = Math.max(1, HP - damage);
            damage = -1;
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public boolean isAlive() {
        if (HP <= 0) {
            HP = 1;

            for (Buff b : buffs()) {
                if (!(b instanceof Doom || b instanceof Cripple)) {
                    b.detach();
                }
            }

            if (!recovering) {
                if (sprite != null) sprite.showStatus(CharSprite.WARNING, "쓰러졌다...");
                if (sprite != null) ((GiantSprite) sprite).crumple();
                recovering = true;
            }
        }
        return super.isAlive();
    }

    @Override
    public boolean reset() {
        state = WANDERING;
        return true;
    }

    private static final String RECOVERING = "recovering";
    private static final String TURNS_ALIVE = "turns_alive";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RECOVERING, recovering);
        bundle.put(TURNS_ALIVE, turnsAlive);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        recovering = bundle.getBoolean(RECOVERING);
        turnsAlive = bundle.getInt(TURNS_ALIVE);
    }

    private class RelentlessWandering extends Wandering {
        @Override
        protected float detectionChance(Char enemy) {
            //during the grace period it can't notice anything at all yet
            if (turnsAlive < HUNT_GRACE_TURNS) return 0f;
            //after that, once in FOV, it always notices - stealth and distance don't help here
            return 1f;
        }
    }
}
