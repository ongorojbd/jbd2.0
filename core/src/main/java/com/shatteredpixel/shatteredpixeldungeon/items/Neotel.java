package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping.discover;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfFeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

import java.util.ArrayList;
//This is from Elemental PD

public class Neotel extends Item {

    String AC_TELEPORT = "teleport";
    String AC_RETURN = "return";

    {
        defaultAction = AC_TELEPORT;
        image = ItemSpriteSheet.ANKH;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_TELEPORT);
        actions.add(AC_RETURN);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);
        if (action.equals(AC_TELEPORT)) {
            GLog.h(Messages.get(Neotel.class, "s"), SPDSettings.getSkin(), SPDSettings.getSkin2(), SPDSettings.getSkin3(), SPDSettings.getSkin4(), SPDSettings.getSkin5());
            Buff.affect(hero, MagicalSight.class, 50f);
            Buff.affect(hero, MindVision.class, 50f);

            Buff.affect(hero, ElixirOfFeatherFall.FeatherBuff.class, 99f);
            Buff.affect(hero, Awareness.class, 99f);
            SPDSettings.addSpecialcoin(4);
//            Buff.affect(hero, AscensionChallenge.class);

            hero.HP = Math.min(hero.HP + 150, hero.HT);
            Buff.affect(hero, Invisibility.class, 99f);

            Chasm.heroFall(hero.pos);
            int length = Dungeon.level.length();
            int[] map = Dungeon.level.map;
            boolean[] mapped = Dungeon.level.mapped;
            boolean[] discoverable = Dungeon.level.discoverable;

            for (int i = 0; i < length; i++) {

                int terr = map[i];

                if (discoverable[i]) {

                    mapped[i] = true;
                    if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                        Dungeon.level.discover(i);

                        if (Dungeon.level.heroFOV[i]) {
                            GameScene.discoverTile(i, terr);
                            discover(i);
                        }
                    }
                }
            }
            GameScene.updateFog();
        }
        if (action.equals(AC_RETURN)) {
            InterlevelScene.mode = InterlevelScene.Mode.RETURN;
            InterlevelScene.returnDepth = 16;
            InterlevelScene.returnBranch = 0;
            InterlevelScene.returnPos = -2;
            Game.switchScene(InterlevelScene.class);

            for (int lvl = hero.lvl; lvl < 30; lvl++) {
                Potion expPotion = new PotionOfExperience();
                expPotion.apply(hero);
            }

            if (SPDSettings.getTendency() != 0) SPDSettings.addTendency(-1);

            // 디버그: 경쟁 모드 제한 및 기록 제거 (다시 도전 가능하도록)
            long DAY = 24 * 60 * 60 * 1000;
            long time = Game.realTime - (Game.realTime % DAY);
            time = Math.max(time, 20_148 * DAY); // earliest possible daily for v3.0.1
            SPDSettings.lastDaily(time - DAY); // 하루 전으로 설정하여 즉시 도전 가능하게

            // 경쟁 모드 기록도 초기화
            Rankings.INSTANCE.load();
            Rankings.INSTANCE.latestDaily = null;
            Rankings.INSTANCE.dailyScoreHistory.clear();
            Rankings.INSTANCE.save();

            GLog.h("경쟁 모드 제한 및 기록이 초기화되었습니다.");

            // 시크릿 팩터 페이지를 미획득 상태로 되돌림
//            Document.SEWERS_GUARD.deletePage("j1");
//            Document.SEWERS_GUARD.deletePage("j2");
//            Document.SEWERS_GUARD.deletePage("j3");
//            Document.SEWERS_GUARD.deletePage("j4");
//            Document.SEWERS_GUARD.deletePage("j5");
//            Document.SEWERS_GUARD.deletePage("j6");
        }
        int length = Dungeon.level.length();
        int[] map = Dungeon.level.map;
        boolean[] mapped = Dungeon.level.mapped;
        boolean[] discoverable = Dungeon.level.discoverable;

        for (int i = 0; i < length; i++) {

            int terr = map[i];

            if (discoverable[i]) {

                mapped[i] = true;
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover(i);

                    if (Dungeon.level.heroFOV[i]) {
                        GameScene.discoverTile(i, terr);
                        discover(i);
                    }
                }
            }
        }
        GameScene.updateFog();
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }


}

