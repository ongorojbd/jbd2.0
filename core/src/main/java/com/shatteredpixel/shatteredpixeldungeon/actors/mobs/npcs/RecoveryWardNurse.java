package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.DolomitesTeeth;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfKillerQueen;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

//the receptionist of the recovery ward (ColdhouseRecoveryLevel). talk to her once to pick a
//single parting gift - a Wand of Regrowth or an Ankh - after which she packs up and leaves.
public class RecoveryWardNurse extends NPC {

    {
        spriteClass = YasuSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    private boolean given = false;

    @Override
    public String name() {
        return "히로세 야스호";
    }

    @Override
    public String description() {
        return "모리오초에 사는 대학생이자 죠스케의 가장 든든한 동료입니다. 숨겨진 로카카카의 비밀을 파헤치기 위해 TG 대학병원에 잠입한 상태입니다.";
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
        //cannot be harmed
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQuest(RecoveryWardNurse.this, text));
            }
        });
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        if (given) {
            tell("나는 밑에서 기다리고 있을게!");
            return true;
        }

        //fresh items each time the window opens, so the icons/info always reflect a pristine reward
        final Item[] rewards = { new WandOfKillerQueen().identify(), new DolomitesTeeth().identify(), new Kingt().quantity(15)};

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        sprite(),
                        "히로세 야스호",
                        "휴.. 괜찮아? 진짜 어떻게 되는 줄 알았어..\n\n" +
                                "그동안 나는 위에서 쓸 만한 걸 챙겨뒀어.\n필요한 거 하나만 골라봐!",
                        Messages.titleCase(rewards[0].name()),
                        Messages.titleCase(rewards[1].name()),
                        Messages.titleCase("스페이스 트러킹 x15")) {

                    @Override
                    protected boolean hasIcon(int index) {
                        return true;
                    }

                    @Override
                    protected Image getIcon(int index) {
                        return new ItemSprite(rewards[index]);
                    }

                    @Override
                    protected boolean hasInfo(int index) {
                        return true;
                    }

                    @Override
                    protected void onInfo(int index) {
                        GameScene.show(new WndInfoItem(rewards[index]));
                    }

                    @Override
                    protected void onSelect(int index) {
                        giveReward(rewards[index]);
                    }
                });
            }
        });

        return true;
    }

    private void giveReward(Item reward) {
        given = true;

        reward.identify(false);
        if (!reward.doPickUp(Dungeon.hero)) {
            Dungeon.level.drop(reward, pos).sprite.drop();
        } else {
            GLog.p(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", reward.name())));
        }

        yell("나는 밑에서 기다리고 있을게!");

        destroy();
        sprite.die();
    }

    private static final String GIVEN = "given";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(GIVEN, given);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        given = bundle.getBoolean(GIVEN);
    }
}
