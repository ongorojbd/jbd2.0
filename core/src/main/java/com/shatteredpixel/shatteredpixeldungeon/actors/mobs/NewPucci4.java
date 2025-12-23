package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GravityChaosTracker;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Pucci5Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NewPucci4 extends Mob {

    {
        spriteClass = Pucci5Sprite.class;

        EXP = 50;
        maxLvl = 30;
        HP = HT = 800;
        defenseSkill = 23;
        properties.add(Property.BOSS);
    }

    int damageTaken = 0;
    private int skillcooldown = 8;
    public int  Phase = 0;
    
    // 새로운 능력: 순간이동과 GravityChaosTracker 쿨다운
    private int teleportCD = 8;  // 순간이동 쿨다운
    private int gravityCD = 3;   // GravityChaosTracker 쿨다운

    private static final String DMGTAKEN = "damagetaken";
    private static final String COOLDOWN = "skillcooldown";
    private static final String PHASE   = "Phase";
    private static final String TELEPORT_CD = "teleport_cd";
    private static final String GRAVITY_CD = "gravity_cd";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(DMGTAKEN, damageTaken);
        bundle.put(COOLDOWN, skillcooldown);
        bundle.put( PHASE, Phase );
        bundle.put(TELEPORT_CD, teleportCD);
        bundle.put(GRAVITY_CD, gravityCD);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        damageTaken = bundle.getInt( DMGTAKEN );
        skillcooldown = bundle.getInt(COOLDOWN);
        Phase = bundle.getInt(PHASE);
        teleportCD = bundle.getInt(TELEPORT_CD);
        gravityCD = bundle.getInt(GRAVITY_CD);
    }


    @Override
    public int damageRoll() {
        // Beast 수준의 강화된 데미지
        return Random.NormalIntRange(25, 45);
    }

    @Override
    public int attackSkill( Char target ) {
        // Beast 수준의 강화된 공격 스킬
        return 45;
    }

    @Override
    public int drRoll() { return Random.NormalIntRange(0, 15);}

    private int moving = 0;

    @Override
    protected boolean act() {

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            if (HP <= HT/2) BossHealthBar.bleed(true);
        }

        // 쿨다운 감소
        if (teleportCD > 0) teleportCD--;
        if (gravityCD > 0) gravityCD--;
        
        // 일정 시간마다 순간이동
        if (teleportCD <= 0 && enemy != null) {
            doTeleport();
            teleportCD = Random.IntRange(6, 10);
        }
        
        // 일정 시간마다 영웅에게 GravityChaosTracker 적용
        if (gravityCD <= 0 && enemy != null && enemy == Dungeon.hero) {
            GravityChaosTracker tracker = Buff.affect(Dungeon.hero, GravityChaosTracker.class);
            tracker.positiveOnly = false;
            tracker.left = 3;  // 3턴만 지속
            gravityCD = Random.IntRange(10, 12);
        }
        
        return super.act();
    }

    @Override
    protected boolean getCloser( int target ) {
        //this is used so that the crab remains slower, but still detects the player at the expected rate.
        moving++;
        if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && skillcooldown <= 0) {

            blink( target );
            spend( -1 / speed() );
            return true;

        } else {

            skillcooldown--;

        }

        if (moving < 3) {

        } else {
            moving = 0;
            return true;
        }

        return super.getCloser( target );

    }

    // 주기적 순간이동 능력
    private void doTeleport() {
        if (enemy == null) return;
        
        // 영웅 주변의 빈 공간으로 순간이동
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = enemy.pos + i;
            if (Dungeon.level.insideMap(cell) 
                    && Dungeon.level.passable[cell]
                    && Actor.findChar(cell) == null
                    && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])) {
                candidates.add(cell);
            }
        }
        
        if (candidates.isEmpty()) {
            // 영웅 주변에 공간이 없으면 랜덤 위치로
            candidates = new ArrayList<>();
            for (int i = 0; i < Dungeon.level.length(); i++) {
                if (Dungeon.level.passable[i] 
                        && Actor.findChar(i) == null
                        && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[i])) {
                    candidates.add(i);
                }
            }
        }
        
        if (!candidates.isEmpty()) {
            int targetCell = Random.element(candidates);
            ScrollOfTeleportation.appear(this, targetCell);
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            GameScene.flash(0xCC3366);
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "teleport"));
        }
    }


    private void blink( int target ) {

        Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

        //can't occupy the same cell as another char, so move back one.
        if (Actor.findChar( cell ) != null && cell != this.pos)
            cell = route.path.get(route.dist-1);

        if (Dungeon.level.avoid[ cell ] && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])){
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                cell = route.collisionPos + n;
                if (Dungeon.level.passable[cell]
                        && Actor.findChar( cell ) == null
                        && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])) {
                    candidates.add( cell );
                }
            }
            if (candidates.size() > 0)
                cell = Random.element(candidates);
            else {
                skillcooldown = Random.IntRange(4, 6);
                return;
            }
        }

        ScrollOfTeleportation.appear( this, cell );
        Sample.INSTANCE.play( Assets.Sounds.DIAVOLO );
        GameScene.flash( 0xCC3366 );
        GLog.n(Messages.get(this, "q"));

        skillcooldown = Random.IntRange(4, 6);
    }

    @Override
    public void notice() {
        super.notice();
    }

    @Override
    public void damage( int dmg, Object src ){

        if (dmg >= 300) {
            dmg = 300;
        }

        super.damage( dmg, src );

    }

    @Override
    public void die(Object cause) {

        hero.HP = hero.HT;

        InterlevelScene.mode = InterlevelScene.Mode.RETURN;
        InterlevelScene.returnDepth = 3;
        InterlevelScene.returnBranch = 1;
        InterlevelScene.returnPos = -2;
        Game.switchScene(InterlevelScene.class);

        GravityChaosTracker tracker = Buff.affect(Dungeon.hero, GravityChaosTracker.class);
        tracker.positiveOnly = false;
        tracker.left = 0;
        
        super.die(cause);
    }

}

