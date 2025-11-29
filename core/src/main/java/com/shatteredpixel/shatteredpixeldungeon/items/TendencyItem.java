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

package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw23;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw27;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw28;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw29;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw30;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw32;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw33;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw34;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw6;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hamon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown8;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CaesarZeppeli;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Lisa;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw23;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TendencyItem extends Item {

    private static String AC_USE = "USE";

    {
        image = ItemSpriteSheet.TENS;
        defaultAction = AC_USE;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_USE)) {
            if (hero.buff(ShovelDigCoolDown8.class) != null) {
                GLog.w(Messages.get(this, "not_ready"));
            } else if (spw23 == 0 && spw27 == 0 && spw28 == 0 && spw32 == 0 && spw33 == 0 && spw34 == 0) {
                GLog.w(Messages.get(this, "not_ready2"));
            } else {
                for (Char c : Actor.chars()) {
                    if (c instanceof CaesarZeppeli) {
                        switch (Random.Int(3)) {
                            case 0:
                                ((CaesarZeppeli) c).skill1();
                                break;
                            case 1:
                                ((CaesarZeppeli) c).skill2();
                                break;
                            case 2:
                                ((CaesarZeppeli) c).skill3();
                        }
                    }
                    // Lisa의 TendencyItem 스킬
                    if (c instanceof Lisa) {
                        ((Lisa) c).tendencySkill();
                    }
                }

                if (spw23 > 1) Buff.affect(hero, Invulnerability.class, 5f);
                else if (spw23 > 0) Spw23.setTrapAtCurrentPosition();

                if (spw27 > 1) {
                    hero.sprite.showStatus(HeroSprite.POSITIVE, "도망치는 거지롱~!");
                    Buff.prolong(hero, Haste.class, 30f);
                    Buff.affect(Dungeon.hero, GreaterHaste.class).set(5);
                } else if (spw27 > 0) {
                    hero.sprite.showStatus(HeroSprite.POSITIVE, "도망치는 거지롱~!");
                    Buff.prolong(hero, Haste.class, 20f);
                }

                if (spw28 > 1) {
                    Buff.affect(hero, Hamon.class).set(5, 2);
                } else if (spw28 > 0) {
                    Buff.affect(hero, Hamon.class).set(5, 1);
                }

                // SPW32: 파문 배리어
                if (spw32 > 0) {
                    int percent = (spw32 > 1) ? 75 : 50;
                    int shield = Math.round(hero.HT * (percent / 100f));
                    Buff.affect(hero, Barrier.class).setShield(shield);
                }

                // SPW33: 리버프 오버 드라이브 (보스 피해)
                if (spw33 > 0) {
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob.properties().contains(Char.Property.BOSS)) {
                            if (spw33 > 1) {
                                mob.damage((int) (mob.HT * 0.15), hero);
                                mob.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);
                            } else if (spw33 > 0) {
                                mob.damage(mob.HT/10, hero);
                                mob.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);
                            }
                        }
                    }
                }

                // SPW34: 파문의 호흡 (충전 버프)
                if (spw34 > 0) {
                    float turns = (spw34 > 1) ? 20f : 10f;
                    SpellSprite.show( curUser, SpellSprite.CHARGE );
                    Buff.affect(hero, Recharging.class, turns);
                    Buff.affect(hero, ArtifactRecharge.class).set(turns);
                }

                new Flare(6, 32).color(0xFFCC00, true).show(hero.sprite, 2f);
                Sample.INSTANCE.play(Assets.Sounds.HAMON);

                int cooldown = 119 - spw29 * 20;
                Buff.affect(hero, ShovelDigCoolDown8.class, cooldown);

                if (spw6 <= 1) {
                    if (spw30 > 1) {
                        spw6++;
                        hero.HTBoost += 2;
                        hero.updateHT(true);
                        GLog.p(Messages.get(this, "hp_boost"));
                    } else if (spw30 > 0) {
                        spw6++;
                        hero.HTBoost++;
                        hero.updateHT(true);
                        GLog.p(Messages.get(this, "hp_boost"));
                    }
                }

                hero.sprite.operate(hero.pos);
            }
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 30 * quantity;
    }

    @Override
    public String desc() {
        // 기본 쿨타임 120턴에서 spw29 값에 따라 감소
        int cooldown = 120 - spw29 * 20;
        return "몇억 번이고 빛을 반사, 증폭시켜 한 점으로 방출하는 신기한 광물.\n\n던전 보상으로 붉은 돌에 특수 능력을 부여할 수 있습니다. 능력 사용 후 " + cooldown + "턴의 재사용 대기시간이 적용됩니다.";
    }

}