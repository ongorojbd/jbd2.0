package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown8;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DoobieTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FancakeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spw23 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.TENS;
        icon = ItemSpriteSheet.Icons.SCROLL_MAGICMAP;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw23);
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_LIGHT );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute( hero, action );
        if (action.equals( AC_LIGHT )) {
            // 가스 생성 기능 제거됨
        }
    }

    /**
     * 현재 위치에 함정을 설치하는 정적 메서드
     * Trapper.java의 setTraps 메서드를 참고하여 구현
     */
    public static void setTrapAtCurrentPosition() {
        // 현재 위치가 함정을 설치할 수 있는지 확인
        if (!Dungeon.level.passable[hero.pos] ||
            Dungeon.level.map[hero.pos] == Terrain.EXIT ||
            Dungeon.level.map[hero.pos] == Terrain.ENTRANCE ||
            Dungeon.level.map[hero.pos] == Terrain.SECRET_TRAP) {
            GLog.w(Messages.get(Spw23.class, "c1"));
            return;
        }

        // 사용 가능한 함정 타입들
        Trap[] trapClasses = new Trap[]{
                new BlazingTrap(),new ConfusionTrap(),new DisintegrationTrap(),new ExplosiveTrap(),
                new FrostTrap(),new GeyserTrap(),
                new StormTrap() ,new ToxicTrap() ,new CorrosionTrap(), new GrimTrap() };

        // 랜덤하게 함정 선택
        Trap selectedTrap = trapClasses[Random.Int(trapClasses.length)];

        // 현재 위치에 함정 설치
        Level.set(hero.pos, Terrain.SECRET_TRAP);
        Dungeon.level.setTrap(selectedTrap, hero.pos);
        Dungeon.level.discover(hero.pos);

        // 시각 효과
        CellEmitter.get(hero.pos).burst(Speck.factory(Speck.LIGHT), 2);

        GLog.p(Messages.get(Spw23.class, "c2"));

        if (hero.heroClass == HeroClass.MAGE) Sample.INSTANCE.play(Assets.Sounds.JOSEPH);
        else Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
} 