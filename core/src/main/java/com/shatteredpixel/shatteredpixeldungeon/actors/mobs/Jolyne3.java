/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Jolyne;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Jolyne4;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JojoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JosukeDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhsnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBossText;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Jolyne3 extends DirectableAlly {

    {
        spriteClass = JojoSprite.class;

        HP = HT = 50;
        defenseSkill = 5;
        viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
        immunities.add(AllyBuff.class);
    }

    private int timer = 3;

    @Override
    public void defendPos(int cell) {
        Sample.INSTANCE.play(Assets.Sounds.B2);
        yell(Messages.get(this, "d"));
        super.defendPos(cell);
    }

    @Override
    public void followHero() {
        Sample.INSTANCE.play(Assets.Sounds.JT5);
        yell(Messages.get(this, "f"));
        super.followHero();
    }

    @Override
    public void targetChar(Char ch) {
        Sample.INSTANCE.play(Assets.Sounds.JSF1);
        yell(Messages.get(this, "t"));
        super.targetChar(ch);
    }

    @Override
    public String description() {
        String desc = super.description();

        desc += "\n" + Messages.get(this, "p1", this.HP, this.HT);

        return desc;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 25) {
            dmg = 25;
        }
        super.damage(dmg, src);
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

    @Override
    public int attackSkill(Char target) {
        if (enemy instanceof Piranha || enemy instanceof Mimic || enemy instanceof Wraith || enemy instanceof Thief) {
            return 30;
        } else {
            return 11;
        }
    }

    @Override
    public int damageRoll() {
        if (enemy instanceof Piranha || enemy instanceof Mimic || enemy instanceof Thief) {
            return Random.NormalIntRange(10, 12);
        } else {
            return Random.NormalIntRange(4, 6);
        }
    }

    private void tell( String text ) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
//                GameScene.show( new WndQuest(Jolyne.class, text ));
            }
        });
    }

    @Override
    protected boolean act() {
        int oldPos = pos;
        boolean result = super.act();
        //partially simulates how the hero switches to idle animation
        if ((pos == target || oldPos == pos) && sprite.looping()) {
            sprite.idle();
        }

        Dungeon.level.updateFieldOfView(this, fieldOfView);
        GameScene.updateFog(pos, viewDistance + (int) Math.ceil(speed()));

        if (timer == 0) {
            Dungeon.fail(Jolyne.class);
            Dungeon.deleteGame( GamesInProgress.curSlot, true );
            Game.switchScene( RankingsScene.class );
        }

        if (Statistics.spw6 == 5) {
            if (timer == 2) {
                WndDialogueWithPic.dialogue(
                        new CharSprite[]{new JosukeDialogSprite(), new JosukeDialogSprite(), new WhsnakeSprite(), new WhsnakeSprite(), new JojoSprite(),
                        new JosukeDialogSprite(), new JojoSprite(), new JojoSprite(), new JosukeDialogSprite(), new JojoSprite(), new JosukeDialogSprite(), new JojoSprite()},
                        new String[]{"죠타로", "죠타로", "???", "화이트 스네이크", "죠린", "죠타로", "죠린", "죠린", "죠타로", "죠린", "죠타로", "죠린"},
                        new String[]{
                                Messages.get(jojo.class, "e1"),
                                Messages.get(jojo.class, "e2"),
                                Messages.get(jojo.class, "e3"),
                                Messages.get(jojo.class, "e4"),
                                Messages.get(jojo.class, "e5"),
                                Messages.get(jojo.class, "e6"),
                                Messages.get(jojo.class, "e7"),
                                Messages.get(jojo.class, "e8"),
                                Messages.get(jojo.class, "e9"),
                                Messages.get(jojo.class, "e10"),
                                Messages.get(jojo.class, "e12"),
                                Messages.get(jojo.class, "e13")
                        },
                        new byte[]{
                                WndDialogueWithPic.RUN,
                                WndDialogueWithPic.RUN,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.RUN,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.RUN,
                                WndDialogueWithPic.IDLE,
                                WndDialogueWithPic.RUN,
                                WndDialogueWithPic.IDLE
                        }
                );
            } else if (timer == 1) {
                Music.INSTANCE.play(Assets.Music.THEME_1, true);

                Statistics.spw6 = 6;

                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndBossText(new Jolyne4(), Messages.get(Jolyne4.class, "title")) {
                            @Override
                            public void hide() {
                                super.hide();
                            }
                        });
                    }
                });

//                tell( Messages.get(jojo.class, "jolyne") );
                Badges.validateClericUnlock();
            }

            timer--;
            spend(TICK);
        }

        return result;
    }

    public void sayHeroKilled() {
        yell(Messages.get(this, "z"));
        GLog.newLine();
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.hero.damage(9999, this);

        if (!Dungeon.hero.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(Char.class, "kill", name()));
        }

        GLog.h(Messages.get(Jolyne3.class, "death2"));

    }
}