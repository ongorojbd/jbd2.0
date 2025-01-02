/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw9;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown6;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.GreatThrowingWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.GreatThrowingWeapon2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tendency;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Bandana;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillcSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class WndJolyne extends Window {

    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 180;

    private static final int MARGIN = 2;
    private static final int GAP = 2;

    private CellSelector.Listener targeter = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            new GreatThrowingWeapon2().activate(hero, target);
        }

        @Override
        public String prompt() {
            return Messages.get(Bandana.class, "prompt");
        }
    };

    public WndJolyne() {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        IconTitle titlebar = new IconTitle();
        titlebar.icon(new WillcSprite());
        titlebar.label(Messages.titleCase(Messages.get(this, "title")));
        titlebar.setRect(0, 0, width, 0);
        add(titlebar);

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title2")), 6);
        title.maxWidth(width);
        title.setPos(0, titlebar.bottom() + GAP);
        add(title);

        float pos = title.bottom() + 3 * MARGIN;

        String text = "_" + Messages.titleCase(Messages.get(this, "ability0", hero.HT / 5));
        RedButton moveBtn = new RedButton(text, 6) {
            @Override
            protected void onClick() {
                GameScene.selectCell(targeter);
                super.onClick();
                hide();
            }
        };
        moveBtn.leftJustify = true;
        moveBtn.multiline = true;
        moveBtn.setSize(width, moveBtn.reqHeight());
        moveBtn.setRect(0, pos, width, moveBtn.reqHeight());
        add(moveBtn);
        pos = moveBtn.bottom() + MARGIN;

        if (spw9 > 0) {
            String text2 = "_" + Messages.titleCase(Messages.get(this, "ability1"));
            RedButton moveBtn2 = new RedButton(text2, 6) {
                @Override
                protected void onClick() {
                    GameScene.selectCell(dasher);
                    super.onClick();
                    hide();
                }
            };
            moveBtn2.leftJustify = true;
            moveBtn2.multiline = true;
            moveBtn2.setSize(width, moveBtn2.reqHeight());
            moveBtn2.setRect(0, pos, width, moveBtn2.reqHeight());
            add(moveBtn2);
            pos = moveBtn2.bottom() + MARGIN;
        }

        if (spw9 > 1) {
            String text3 = "_" + Messages.titleCase(Messages.get(this, "ability2"));
            RedButton moveBtn3 = new RedButton(text3, 6) {
                @Override
                protected void onClick() {
                    for (Char c : Actor.chars()) {
                        if (c instanceof Tendency) {
                            ((Tendency) c).a0();
                        }
                    }
                    Buff.affect(hero, ShovelDigCoolDown6.class, 30f);
                    super.onClick();
                    hide();
                }
            };
            moveBtn3.leftJustify = true;
            moveBtn3.multiline = true;
            moveBtn3.setSize(width, moveBtn3.reqHeight());
            moveBtn3.setRect(0, pos, width, moveBtn3.reqHeight());
            add(moveBtn3);
            pos = moveBtn3.bottom() + MARGIN;
        }

        if (spw9 > 2) {
            String text4 = "_" + Messages.titleCase(Messages.get(this, "ability3"));
            RedButton moveBtn4 = new RedButton(text4, 6) {
                @Override
                protected void onClick() {
                    for (Char c : Actor.chars()) {
                        if (c instanceof Tendency) {
                            ((Tendency) c).barrier();
                        }
                    }
                    super.onClick();
                    hide();
                }
            };
            moveBtn4.leftJustify = true;
            moveBtn4.multiline = true;
            moveBtn4.setSize(width, moveBtn4.reqHeight());
            moveBtn4.setRect(0, pos, width, moveBtn4.reqHeight());
            add(moveBtn4);
            pos = moveBtn4.bottom() + MARGIN;
        }

        if (spw9 > 4) {
            String text5 = "_" + Messages.titleCase(Messages.get(this, "ability4"));
            RedButton moveBtn5 = new RedButton(text5, 6) {
                @Override
                protected void onClick() {
                    for (Char c : Actor.chars()) {
                        if (c instanceof Tendency) {
                            Buff.affect(c, Light.class, 3f);
                        }
                    }
                    super.onClick();
                    hide();
                }
            };
            moveBtn5.leftJustify = true;
            moveBtn5.multiline = true;
            moveBtn5.setSize(width, moveBtn5.reqHeight());
            moveBtn5.setRect(0, pos, width, moveBtn5.reqHeight());
            add(moveBtn5);
            pos = moveBtn5.bottom() + MARGIN;
        }

        if (spw9 > 5) {
            String text6 = "_" + Messages.titleCase(Messages.get(this, "ability5"));
            RedButton moveBtn6 = new RedButton(text6, 6) {
                @Override
                protected void onClick() {
                    for (Char c : Actor.chars()) {
                        if (c instanceof Tendency) {
                            ((Tendency) c).cat();
                        }
                    }
                    super.onClick();
                    hide();
                }
            };
            moveBtn6.leftJustify = true;
            moveBtn6.multiline = true;
            moveBtn6.setSize(width, moveBtn6.reqHeight());
            moveBtn6.setRect(0, pos, width, moveBtn6.reqHeight());
            add(moveBtn6);
            pos = moveBtn6.bottom() + MARGIN;
        }

        if (spw9 > 6) {
            String text7 = "_" + Messages.titleCase(Messages.get(this, "ability6"));
            RedButton moveBtn7 = new RedButton(text7, 6) {
                @Override
                protected void onClick() {
                    for (Char c : Actor.chars()) {
                        if (c instanceof Tendency) {
                            Buff.affect(c, Light.class, 3f);
                        }
                    }
                    super.onClick();
                    hide();
                }
            };
            moveBtn7.leftJustify = true;
            moveBtn7.multiline = true;
            moveBtn7.setSize(width, moveBtn7.reqHeight());
            moveBtn7.setRect(0, pos, width, moveBtn7.reqHeight());
            add(moveBtn7);
            pos = moveBtn7.bottom() + MARGIN;
        }

        resize(width, (int) pos);
    }

    private static CellSelector.Listener dasher = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target == null || target == -1 || (!Dungeon.level.visited[target] && !Dungeon.level.mapped[target])){
                return;
            }

            PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
            if (PathFinder.distance[hero.pos] == Integer.MAX_VALUE){
                GLog.w( Messages.get(MeleeWeapon.class, "dash_bad_position") );
                return;
            }

            if (hero.rooted){
                GLog.w( Messages.get(MeleeWeapon.class, "rooted") );
                return;
            }

            int range = 6;

            if (Dungeon.level.distance(hero.pos, target) > range){
                GLog.w(Messages.get(MeleeWeapon.class, "dash_bad_position"));
                return;
            }

            Ballistica dash = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);

            if (!dash.collisionPos.equals(target)
                    || Actor.findChar(target) != null
                    || (Dungeon.level.solid[target] && !Dungeon.level.passable[target])
                    || Dungeon.level.map[target] == Terrain.CHASM){
                GLog.w(Messages.get(MeleeWeapon.class, "dash_bad_position"));
                return;
            }

            hero.busy();
            Sample.INSTANCE.play(Assets.Sounds.MISS);
            hero.sprite.emitter().start(Speck.factory(Speck.JET), 0.01f, Math.round(4 + 2*Dungeon.level.trueDistance(hero.pos, target)));
            Buff.affect(hero, ShovelDigCoolDown6.class, 10f);
            hero.sprite.jump(hero.pos, target, new Callback() {
                @Override
                public void call() {
                    if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
                        Door.leave( hero.pos );
                    }
                    hero.pos = target;

                    for (int i : PathFinder.NEIGHBOURS8){
                        Char ch = Actor.findChar( target+i );
                        if (ch != null && ch.alignment != Char.Alignment.ALLY){
                            ch.damage(Math.round(hero.belongings.weapon.damageRoll(hero) * 0.5f), Dungeon.hero);
                            Buff.affect(ch, SoulMark.class, 1f);
                        }
                    }

                    Sample.INSTANCE.play(Assets.Sounds.BLAST);
                    WandOfBlastWave.BlastWave.blast(target);
                    Camera.main.shake(0.5f, 0.5f);

                    Dungeon.level.occupyCell(hero);
                    hero.next();
                }
            });

            AttackIndicator.updateState();
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };
}
