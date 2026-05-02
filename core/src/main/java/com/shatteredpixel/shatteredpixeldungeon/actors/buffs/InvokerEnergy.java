/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.PoisonGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WO;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInvokerSpells;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class InvokerEnergy extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;
        revivePersists = true;
    }

    public static float energy = 0;

    public int quasCharges  = 0; // Q (냉기)
    public int wexCharges   = 0; // W (번개)
    public int exortCharges = 0; // E (화염)

    // 최근 사용한 스킬 이력 (스팸 방지용): 최대 5개 유지
    private static final int SPAM_BLOCK_COUNT = 5;
    private LinkedList<String> recentSpells = new LinkedList<>();

    // 타겟 선택이 필요한 스킬 대기 상태 (QQW 영혼 흡수)
    private transient InvokerSpell pendingTargetedSpell = null;

    public enum Element { QUAS, WEX, EXORT }

    // ─────────────────────────────────────────────
    //  스킬 데이터
    // ─────────────────────────────────────────────
    public static class InvokerSpell {
        public final int q, w, e;
        public final String name;
        public final String desc;
        public final int cost;

        InvokerSpell(int q, int w, int e, String name, String desc, int cost) {
            this.q = q; this.w = w; this.e = e;
            this.name = name; this.desc = desc; this.cost = cost;
        }

        public boolean matches(int qC, int wC, int eC) {
            return q == qC && w == wC && e == eC;
        }

        public String descFor(Hero hero) {
            if (q == 3 && w == 0 && e == 0) {
                int pct = 20 + 5 * hero.pointsInTalent(Talent.INVOKER_1);
                return "5턴간 마비에 걸리는 대신, 최대 체력의 " + pct + "%를 서서히 회복합니다.";
            }
            if (q == 2 && w == 1 && e == 0) {
                int pct = 35 + 5 * hero.pointsInTalent(Talent.INVOKER_3);
                return "선택한 적(보스 불가)의 최대 체력 " + pct + "%를 깎고, 그만큼 보호막을 얻습니다.";
            }
            if (q == 0 && w == 0 && e == 3) {
                int range = 5 + 2 * hero.pointsInTalent(Talent.INVOKER_2);
                int invis = hero.pointsInTalent(Talent.INVOKER_2);
                String base = "시야 내 " + range + "칸 이내 위치로 즉시 순간이동합니다.";
                return invis > 0 ? base + " 순간이동 후 " + invis + "턴의 투명화를 얻습니다." : base;
            }
            return desc;
        }

        public String comboLabel() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < q; i++) sb.append("Q");
            for (int i = 0; i < w; i++) sb.append("W");
            for (int i = 0; i < e; i++) sb.append("E");
            return sb.toString();
        }
    }

    public static final InvokerSpell[] SPELLS = {
        new InvokerSpell(3,0,0, "신체 재생",      "5턴간 마비에 걸리는 대신, 최대 체력의 20%를 서서히 회복합니다.",          1),
        new InvokerSpell(2,1,0, "영혼 흡수",      "선택한 적(보스 불가)의 최대 체력 35%를 깎고, 그만큼 보호막을 얻습니다.", 1),
        new InvokerSpell(1,2,0, "타락한 영혼",    "현재 층에 등장하는 무작위 적을 소환하고 아군으로 만듭니다.",                   1),
        new InvokerSpell(2,0,1, "신체 강화",      "12턴간 신속, 무기 +1 강화, 브로치 +1 강화를 얻습니다.",                      1),
        new InvokerSpell(1,0,2, "쇠약",           "선택한 적의 모든 행동이 5턴 동안 2배 느려지게 합니다.",                               1),
        new InvokerSpell(0,3,0, "추적하는 나이프","시야 내 모든 적에게 현재 무기 위력에 비례한 피해를 줍니다.",              1),
        new InvokerSpell(0,2,1, "독안개",         "선택한 위치에 독안개를 소환합니다. 영웅은 독안개에 면역입니다.",                                  1),
        new InvokerSpell(0,1,2, "충격파",          "주위로 충격파를 발사해 적을 넉백하고 현재 체력의 절반의 피해를 줍니다.",           1),
        new InvokerSpell(0,0,3, "순간이동",        "시야 내 5칸 이내 위치로 즉시 순간이동합니다.",                            1),
        new InvokerSpell(1,1,1, "진실조작",       "3x3 영역을 지정합니다. 2턴 간 집중해서 해당 영역에 낙뢰를 떨어트립니다.",       1),
    };

    public static InvokerSpell findSpell(int q, int w, int e) {
        for (InvokerSpell s : SPELLS) {
            if (s.matches(q, w, e)) return s;
        }
        return null;
    }

    // ─────────────────────────────────────────────
    //  에너지 획득 (적 처치 시)
    // ─────────────────────────────────────────────
    public void gainEnergy(Mob enemy) {
        if (target == null) return;
        if (!Regeneration.regenOn()) return; // 보스 하수인 파밍 방지

        float gain;
        if      (Char.hasProp(enemy, Char.Property.BOSS))      gain = 5f;
        else if (Char.hasProp(enemy, Char.Property.MINIBOSS))  gain = 3f;
        else if (enemy instanceof Ghoul)                       gain = 0.5f;
        else if (enemy instanceof RipperDemon)                 gain = 0.5f;
        else if (enemy instanceof YogDzewa.Larva)              gain = 0.5f;
        else if (enemy instanceof Wraith)                      gain = 0.5f;
        else                                                   gain = 1f;

        energy = Math.min(energy + gain, energyCap());
        ActionIndicator.setAction(this);
        BuffIndicator.refreshHero();
    }

    // ─────────────────────────────────────────────
    //  원소 충전
    //  반환값: true = 스킬 발동됨, false = 충전만 되거나 실패
    // ─────────────────────────────────────────────
    public boolean addCharge(Element element) {
        if (totalCharges() >= 3) return false;

        switch (element) {
            case QUAS:  quasCharges++;  break;
            case WEX:   wexCharges++;   break;
            case EXORT: exortCharges++; break;
        }
        BuffIndicator.refreshHero();

        if (totalCharges() == 3) {
            InvokerSpell spell = findSpell(quasCharges, wexCharges, exortCharges);
            if (spell == null) {
                resetCharges();
                return false;
            }
            if (energy < spell.cost) {
                GLog.w(Messages.get(this, "not_enough_energy"));
                resetCharges();
                BuffIndicator.refreshHero();
                return false;
            }
            if (isSpellBlocked(spell)) {
                int remaining = SPAM_BLOCK_COUNT - recentSpells.indexOf(spell.comboLabel());
                GLog.w(Messages.get(this, "spell_blocked", spell.name, remaining));
                resetCharges();
                BuffIndicator.refreshHero();
                return false;
            }
            castSpell(spell);
            return true;
        }
        return false;
    }

    private void castSpell(InvokerSpell spell) {
        Hero hero = (Hero) target;

        String combo = spell.comboLabel();
        if (combo.equals("QQW") || combo.equals("QEE") || combo.equals("WWE")
                || combo.equals("EEE") || combo.equals("QWE")) {
            // 타겟 선택이 필요한 스킬: 자원 소비는 리스너에서 처리
            resetCharges();
            BuffIndicator.refreshHero();
            pendingTargetedSpell = spell;
            GameScene.selectCell(targetingListener);
            return;
        }

        // 비-타겟팅 스킬: 자원 즉시 소비
        energy -= spell.cost;
        resetCharges();
        BuffIndicator.refreshHero();
        ActionIndicator.refresh();

        GLog.p(Messages.get(this, "cast_spell",
                "[" + spell.comboLabel() + "]", spell.name));
        recordSpell(spell);

        switch (combo) {
            case "QQQ": castBodyRegen(hero);     break;
            case "QQE": castBodyBuff(hero);      break;
            case "QWW": castCorruptedSoul(hero); break;
            case "WEE": castShockwave(hero);     break;
            case "WWW": castTrackingKnives(hero);break;
            default:    hero.spendAndNext(1f);
        }
    }

    // ─────────────────────────────────────────────
    //  QQQ: 신체 재생
    //  5턴 기절 + 최대 체력의 20% 서서히 회복
    // ─────────────────────────────────────────────
    private void castBodyRegen(Hero hero) {
        Buff.affect(hero, Paralysis.class, 5f);
        float healRatio = 0.2f + 0.05f * hero.pointsInTalent(Talent.INVOKER_1);
        int healAmount = Math.round(hero.HT * healRatio);
        Buff.affect(hero, Healing.class).setHeal(healAmount, 0.1f, 0);
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE,
                Integer.toString(healAmount), FloatingText.HEALING);
        Sample.INSTANCE.play(Assets.Sounds.D43);
        new Flare(6, 32).color(0x9933FF, true).show(hero.sprite, 2f);
        hero.spendAndNext(1f);
    }

    // ─────────────────────────────────────────────
    //  QQE: 신체 강화
    //  12턴 haste + 무기/방어구 강화
    // ─────────────────────────────────────────────
    private void castBodyBuff(Hero hero) {
        Buff.affect(hero, Haste.class, 12f);
        if (hero.buff(EnhancedWeapon.class) == null) {
            EnhancedWeapon ew = Buff.affect(hero, EnhancedWeapon.class);
            ew.setEnhancementLevel(1);
            ew.setTemporaryDuration(12f);
        }
        if (hero.buff(EnhancedArmor.class) == null) {
            EnhancedArmor ea = Buff.affect(hero, EnhancedArmor.class);
            ea.setEnhancementLevel(1);
            ea.setTemporaryDuration(12f);
        }
        // 버프 적용 후 아이템 슬롯의 파란 "+1" 표시가 즉시 갱신되도록 강제 갱신
        Item.updateQuickslot();
        Sample.INSTANCE.play(Assets.Sounds.D12);
        SpellSprite.show(hero, SpellSprite.HASTE);
        hero.spendAndNext(1f);
    }

    // ─────────────────────────────────────────────
    //  통합 타겟팅 리스너 (QQW, QEE, WWE 공통)
    // ─────────────────────────────────────────────
    private final CellSelector.Listener targetingListener = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            Hero hero = (Hero) target;
            InvokerSpell spell = pendingTargetedSpell;
            pendingTargetedSpell = null;

            if (cell == null || spell == null) {
                hero.next();
                return;
            }

            switch (spell.comboLabel()) {
                case "QQW": handleSoulAbsorb(hero, cell, spell);    break;
                case "QEE": handleWeaken(hero, cell, spell);         break;
                case "WWE": handleToxicCloud(hero, cell, spell);     break;
                case "EEE": handleBlink(hero, cell, spell);          break;
                case "QWE": handleEnergyStrike(hero, cell, spell);   break;
                default:    hero.next();
            }
        }

        @Override
        public String prompt() {
            if (pendingTargetedSpell != null) {
                switch (pendingTargetedSpell.comboLabel()) {
                    case "QQW": return Messages.get(InvokerEnergy.class, "select_target_soul");
                    case "QEE": return Messages.get(InvokerEnergy.class, "select_target_weaken");
                    case "WWE": return Messages.get(InvokerEnergy.class, "select_target_toxic");
                    case "EEE": return Messages.get(InvokerEnergy.class, "select_target_blink");
                    case "QWE": return Messages.get(InvokerEnergy.class, "select_target_energystrike");
                }
            }
            return Messages.get(InvokerEnergy.class, "select_target");
        }
    };

    private void consumeAndLog(InvokerSpell spell) {
        energy -= spell.cost;
        GLog.p(Messages.get(InvokerEnergy.class, "cast_spell",
                "[" + spell.comboLabel() + "]", spell.name));
        recordSpell(spell);
        BuffIndicator.refreshHero();
        ActionIndicator.refresh();
    }

    private void castSoulAbsorb(Hero hero, Char enemy) {
        float drainRatio = 0.35f + 0.05f * hero.pointsInTalent(Talent.INVOKER_3);
        int drain = Math.round(enemy.HT * drainRatio);
        enemy.damage(drain, hero);
        enemy.sprite.showStatusWithIcon(CharSprite.NEGATIVE,
                Integer.toString(drain), FloatingText.CORRUPTION);

        Buff.affect(hero, Barrier.class).setShield(drain);
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE,
                Integer.toString(drain), FloatingText.SHIELDING);

        WO.d3class();
        Sample.INSTANCE.play(Assets.Sounds.CURSED);
        CellEmitter.get(enemy.pos).burst(ShadowParticle.UP, 5);

        hero.spendAndNext(1f);
    }

    // QQW 처리 (targetingListener에서 호출)
    private void handleSoulAbsorb(Hero hero, int cell, InvokerSpell spell) {
        Char enemy = Actor.findChar(cell);
        if (enemy == null || enemy == hero
                || enemy.alignment != Char.Alignment.ENEMY
                || !hero.fieldOfView[cell]) {
            GLog.w(Messages.get(InvokerEnergy.class, "invalid_target"));
            hero.next();
            return;
        }
        if (Char.hasProp(enemy, Char.Property.BOSS)
                || Char.hasProp(enemy, Char.Property.MINIBOSS)) {
            GLog.w(Messages.get(InvokerEnergy.class, "no_boss_target"));
            hero.next();
            return;
        }
        consumeAndLog(spell);
        castSoulAbsorb(hero, enemy);
    }

    // ─────────────────────────────────────────────
    //  QEE: 쇠약
    // ─────────────────────────────────────────────
    private void handleWeaken(Hero hero, int cell, InvokerSpell spell) {
        Char enemy = Actor.findChar(cell);
        if (enemy == null || enemy == hero
                || enemy.alignment != Char.Alignment.ENEMY
                || !hero.fieldOfView[cell]) {
            GLog.w(Messages.get(InvokerEnergy.class, "invalid_target"));
            hero.next();
            return;
        }
        consumeAndLog(spell);
        Buff.affect(enemy, Slow.class, 5f);
        WO.d2class();
        hero.spendAndNext(1f);
    }

    // ─────────────────────────────────────────────
    //  WWE: 독안개
    // ─────────────────────────────────────────────
    private void handleToxicCloud(Hero hero, int cell, InvokerSpell spell) {
        if (!hero.fieldOfView[cell] && cell != hero.pos) {
            GLog.w(Messages.get(InvokerEnergy.class, "invalid_target"));
            hero.next();
            return;
        }
        consumeAndLog(spell);
        GameScene.add(Blob.seed(cell, 300 + 20 * Dungeon.scalingDepth(), PoisonGas.class));
        Sample.INSTANCE.play(Assets.Sounds.GAS);
        WO.d2class();
        hero.spendAndNext(1f);
    }

    // ─────────────────────────────────────────────
    //  QWW: 타락한 영혼
    //  현재 층 몹 소환 + Corruption 부여
    // ─────────────────────────────────────────────
    private void castCorruptedSoul(Hero hero) {
        try {
            ArrayList<Class<? extends Mob>> rotation =
                    MobSpawner.getMobRotation(Dungeon.depth);
            Class<? extends Mob> mobClass = Random.element(rotation);
            Mob mob = mobClass.getDeclaredConstructor().newInstance();

            int spawnPos = -1;
            for (int n : PathFinder.NEIGHBOURS8) {
                int pos = hero.pos + n;
                if (pos >= 0 && pos < Dungeon.level.length()
                        && Dungeon.level.passable[pos]
                        && Actor.findChar(pos) == null) {
                    spawnPos = pos;
                    break;
                }
            }
            if (spawnPos == -1) {
                spawnPos = Dungeon.level.randomRespawnCell(null);
            }
            if (spawnPos == -1) {
                GLog.w(Messages.get(InvokerEnergy.class, "no_summon_pos"));
                hero.spendAndNext(1f);
                return;
            }

            mob.pos = spawnPos;
            GameScene.add(mob);
            Corruption.corruptionHeal(mob);
            new Flare(6, 32).color(0x9933FF, true).show(mob.sprite, 3f);
            Buff.affect(mob, Corruption.class);
            mob.state = mob.HUNTING;

            WO.d4class();
        } catch (Exception e) {
            GLog.w(Messages.get(InvokerEnergy.class, "no_summon_pos"));
        }
        hero.spendAndNext(1f);
    }

    // ─────────────────────────────────────────────
    //  WWW - 추적하는 나이프
    //  시야 내 모든 적에게 무기 위력 비례 피해
    // ─────────────────────────────────────────────
    private void castTrackingKnives(Hero hero) {
        List<Char> targets = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch != hero
                    && ch.alignment == Char.Alignment.ENEMY
                    && hero.fieldOfView[ch.pos]
                    && ch.isAlive()) {
                targets.add(ch);
            }
        }

        if (targets.isEmpty()) {
            GLog.w(Messages.get(this, "no_targets"));
            hero.spendAndNext(1f);
            return;
        }

        // 가장 가까운 적을 hero.sprite.zap 애니메이션 기준으로 사용
        Char nearest = targets.get(0);
        for (Char ch : targets) {
            if (Dungeon.level.trueDistance(hero.pos, ch.pos)
                    < Dungeon.level.trueDistance(hero.pos, nearest.pos)) {
                nearest = ch;
            }
        }

        // ThrowingKnife 스프라이트를 미사일 이펙트로 사용
        Item knifeProto = new Item() {
            { image = ItemSpriteSheet.THROWING_KNIFE; }
        };

        final HashSet<Callback> callbacks = new HashSet<>();

        for (Char ch : targets) {
            final Char victim = ch;
            Callback cb = new Callback() {
                @Override
                public void call() {
                    hero.attack(victim, 1f, 0, Char.INFINITE_ACCURACY);
                    callbacks.remove(this);
                    if (callbacks.isEmpty()) {
                        Invisibility.dispel();
                        hero.spendAndNext(hero.attackDelay());
                    }
                }
            };
            MissileSprite missile = (MissileSprite) hero.sprite.parent.recycle(MissileSprite.class);
            missile.reset(hero.sprite, ch.pos, knifeProto, cb);
            missile.angularSpeed = 0;
            missile.alpha(0.9f);
            callbacks.add(cb);
        }

        WO.d3class();
        hero.sprite.zap(nearest.pos);
        hero.busy();

        Sample.INSTANCE.play( Assets.Sounds.RAY, 2, Random.Float(0.33f, 0.66f) );
    }

    // ─────────────────────────────────────────────
    //  WEE: 충격파 (Sleepcmoon AC_LIGHT 기반)
    // ─────────────────────────────────────────────
    private void castShockwave(final Hero hero) {
        Ballistica aim;
        int x = hero.pos % Dungeon.level.width();
        int y = hero.pos / Dungeon.level.width();

        if (Math.max(x, Dungeon.level.width() - x) >= Math.max(y, Dungeon.level.height() - y)) {
            aim = x > Dungeon.level.width() / 2
                    ? new Ballistica(hero.pos, hero.pos - 1, Ballistica.WONT_STOP)
                    : new Ballistica(hero.pos, hero.pos + 1, Ballistica.WONT_STOP);
        } else {
            aim = y > Dungeon.level.height() / 2
                    ? new Ballistica(hero.pos, hero.pos - Dungeon.level.width(), Ballistica.WONT_STOP)
                    : new Ballistica(hero.pos, hero.pos + Dungeon.level.width(), Ballistica.WONT_STOP);
        }

        final int aoeSize = 8;
        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

        for (Ballistica ray : aoe.outerRays) {
            ((MagicMissile) hero.sprite.parent.recycle(MagicMissile.class)).reset(
                    MagicMissile.FORCE_CONE, hero.sprite, ray.path.get(ray.dist), null);
        }

        final ConeAOE finalAoe = aoe;
        final Ballistica finalAim = aim;
        ((MagicMissile) hero.sprite.parent.recycle(MagicMissile.class)).reset(
                MagicMissile.FORCE_CONE,
                hero.sprite,
                finalAim.path.get(Math.min(aoeSize / 2, finalAim.path.size() - 1)),
                new Callback() {
                    @Override
                    public void call() {
                        for (int cell : finalAoe.cells) {
                            Char mob = Actor.findChar(cell);
                            if (mob != null && mob != hero && mob.alignment != Char.Alignment.ALLY) {
                                int dmg = (Char.hasProp(mob, Char.Property.BOSS)
                                        || Char.hasProp(mob, Char.Property.MINIBOSS))
                                        ? 20 : mob.HP / 2;
                                mob.damage(dmg, hero);
                                Ballistica kb = new Ballistica(hero.pos, mob.pos, Ballistica.WONT_STOP);
                                int knockback = (aoeSize + 1 - (int) Dungeon.level.trueDistance(hero.pos, mob.pos)) * 3;
                                WandOfBlastWave.throwChar(mob,
                                        new Ballistica(mob.pos, kb.collisionPos, Ballistica.MAGIC_BOLT),
                                        knockback, true, true, hero);
                            }
                        }
                        Invisibility.dispel();
                        hero.spendAndNext(Actor.TICK);
                    }
                });

        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.D11);
        hero.busy();
    }

    // 잠긴 문(LOCKED_DOOR, HERO_LKD_DR, CRYSTAL_DOOR, BARRICADE)을 건너지 않고
    // from에서 to까지 도달 가능한지 BFS로 확인
    private boolean isReachableWithoutLockedDoors(int from, int to) {
        if (from == to) return true;
        boolean[] visited = new boolean[Dungeon.level.length()];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(from);
        visited[from] = true;
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            for (int offset : PathFinder.NEIGHBOURS4) {
                int next = curr + offset;
                if (next < 0 || next >= Dungeon.level.length() || visited[next]) continue;
                visited[next] = true;
                int terrain = Dungeon.level.map[next];
                if (terrain == Terrain.LOCKED_DOOR
                        || terrain == Terrain.HERO_LKD_DR
                        || terrain == Terrain.CRYSTAL_DOOR
                        || terrain == Terrain.BARRICADE) continue;
                if (next == to) return true;
                if (Dungeon.level.passable[next]) queue.add(next);
            }
        }
        return false;
    }

    // ─────────────────────────────────────────────
    //  EEE: 순간이동 (5칸 이내 타겟팅)
    // ─────────────────────────────────────────────
    private void handleBlink(Hero hero, int cell, InvokerSpell spell) {
        if (!Dungeon.level.passable[cell] || Actor.findChar(cell) != null) {
            GLog.w(Messages.get(InvokerEnergy.class, "invalid_target"));
            hero.next();
            return;
        }
        int blinkRange = 5 + 2 * hero.pointsInTalent(Talent.INVOKER_2);
        if (Dungeon.level.trueDistance(hero.pos, cell) > blinkRange) {
            GLog.w(Messages.get(InvokerEnergy.class, "blink_too_far"));
            hero.next();
            return;
        }
        if (!isReachableWithoutLockedDoors(hero.pos, cell)) {
            GLog.w(Messages.get(InvokerEnergy.class, "locked_room"));
            hero.next();
            return;
        }

        CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

        consumeAndLog(spell);
        ScrollOfTeleportation.appear(hero, cell);
        Dungeon.observe();
        GameScene.updateFog();

        int invisTurns = hero.pointsInTalent(Talent.INVOKER_2);
        if (invisTurns > 0) {
            Buff.affect(hero, Invisibility.class, invisTurns);
        }

        hero.spendAndNext(1f);

        WO.d2class();
    }

    // ─────────────────────────────────────────────
    //  QWE: 진실조작 (2턴 지연 3x3 피해)
    // ─────────────────────────────────────────────
    private void handleEnergyStrike(Hero hero, int cell, InvokerSpell spell) {
        if (!hero.fieldOfView[cell]) {
            GLog.w(Messages.get(InvokerEnergy.class, "invalid_target"));
            hero.next();
            return;
        }
        consumeAndLog(spell);
        for (int i : PathFinder.NEIGHBOURS9) {
            int areaCell = cell + i;
            if (areaCell >= 0 && areaCell < Dungeon.level.length() && !Dungeon.level.solid[areaCell]) {
                hero.sprite.parent.addToBack(new TargetedCell(areaCell, 0xFF00FF));
            }
        }

        hero.sprite.centerEmitter().start(Speck.factory(Speck.MASK), 0.05f, 20);
        TruthManipulationBuff buff = Buff.append(hero, TruthManipulationBuff.class, 1.5f);
        buff.setTargetArea(cell);
        WO.d3class();
        hero.spendAndNext(2f);
    }

    // 스팸 방지: 최근 SPAM_BLOCK_COUNT개 이력에 동일 스킬이 있으면 차단
    public boolean isSpellBlocked(InvokerSpell spell) {
        return recentSpells.contains(spell.comboLabel());
    }

    // 사용 이력 기록 - 오래된 항목은 자동으로 밀려남, 밀려난 스킬은 쿨타임 해제 알림
    private void recordSpell(InvokerSpell spell) {
        boolean alreadyPresent = recentSpells.contains(spell.comboLabel());
        recentSpells.remove(spell.comboLabel());
        recentSpells.addFirst(spell.comboLabel());
        // 새 스킬을 추가했는데 목록이 넘치면 가장 오래된 스킬이 풀림
        if (!alreadyPresent && recentSpells.size() > SPAM_BLOCK_COUNT) {
            String unblocked = recentSpells.removeLast();
            for (InvokerSpell s : SPELLS) {
                if (s.comboLabel().equals(unblocked)) {
                    GLog.p(Messages.get(InvokerEnergy.class, "spell_ready",
                            "[" + unblocked + "]", s.name));
                    break;
                }
            }
        }
    }

    // 현재 스킬이 다시 사용 가능해지려면 몇 번 더 다른 스킬을 써야 하는지
    public int spellBlockRemaining(InvokerSpell spell) {
        int idx = recentSpells.indexOf(spell.comboLabel());
        if (idx == -1) return 0;
        return SPAM_BLOCK_COUNT - idx;
    }

    private void resetCharges() {
        quasCharges = 0;
        wexCharges  = 0;
        exortCharges = 0;
    }

    public int totalCharges() {
        return quasCharges + wexCharges + exortCharges;
    }

    public int energyCap() {
        // Monk와 동일한 공식: 레벨 30 기준 최대 20
        return Math.max(10, 5 + Dungeon.hero.lvl / 2);
    }

    // ─────────────────────────────────────────────
    //  Buff 오버라이드
    // ─────────────────────────────────────────────
    @Override
    public boolean attachTo(Char target) {
        boolean result = super.attachTo(target);
        ActionIndicator.setAction(this);
        return result;
    }

    @Override
    public boolean act() {
        spend(TICK);
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.BERSERK; // 임시 - 나중에 전용 아이콘으로 교체
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0.2f, 0.6f, 1f); // 파란색 계열로 Monk와 구분
    }

    @Override
    public float iconFadePercent() {
        return GameMath.gate(0, 1f - energy / energyCap(), 1);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int) energy);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc",
                (int) energy, energyCap(),
                quasCharges, wexCharges, exortCharges);
    }

    // ─────────────────────────────────────────────
    //  저장/불러오기
    // ─────────────────────────────────────────────
    private static final String ENERGY       = "energy";
    private static final String QUAS_C       = "quas";
    private static final String WEX_C        = "wex";
    private static final String EXORT_C      = "exort";
    private static final String RECENT_SPELLS = "recent_spells";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ENERGY,  energy);
        bundle.put(QUAS_C,  quasCharges);
        bundle.put(WEX_C,   wexCharges);
        bundle.put(EXORT_C, exortCharges);
        bundle.put(RECENT_SPELLS, recentSpells.toArray(new String[0]));
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        energy       = bundle.getFloat(ENERGY);
        quasCharges  = bundle.getInt(QUAS_C);
        wexCharges   = bundle.getInt(WEX_C);
        exortCharges = bundle.getInt(EXORT_C);
        recentSpells = new LinkedList<>();
        for (String s : bundle.getStringArray(RECENT_SPELLS)) {
            recentSpells.addLast(s);
        }
        ActionIndicator.setAction(this);
    }

    // ─────────────────────────────────────────────
    //  ActionIndicator (버프 클릭 시 스킬 목록 창)
    // ─────────────────────────────────────────────
    @Override
    public String actionName() {
        return Messages.get(this, "action");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.INVOKER_ABILITIES;
    }

    @Override
    public Visual secondaryVisual() {
        BitmapText txt = new BitmapText(
                com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene.pixelFont);
        txt.text(Integer.toString((int) energy));
        txt.hardlight(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.POSITIVE);
        txt.measure();
        return txt;
    }

    @Override
    public int indicatorColor() {
        return 0x0D1B3D;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndInvokerSpells(this));
    }

    // ─────────────────────────────────────────────
    //  QWE 진실조작 버프: 2턴 후 3x3 영역 피해
    // ─────────────────────────────────────────────
    public static class TruthManipulationBuff extends FlavourBuff {

        private int centerCell = -1;
        private ArrayList<Integer> affectedCells;

        { type = buffType.NEUTRAL; }

        public void setTargetArea(int center) {
            centerCell = center;
            affectedCells = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS9) {
                int cell = center + i;
                if (cell >= 0 && cell < Dungeon.level.length() && !Dungeon.level.solid[cell]) {
                    affectedCells.add(cell);
                }
            }
        }

        @Override
        public boolean act() {
            if (centerCell != -1 && affectedCells != null) {
                Invisibility.dispel(Dungeon.hero);
                Hero hero = Dungeon.hero;

                for (int cell : affectedCells) {
                    if (cell < 0 || cell >= Dungeon.level.length() || Dungeon.level.solid[cell]) continue;

                    CellEmitter.get(cell).burst(SparkParticle.FACTORY, 31);
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);

                    Camera.main.shake(9, 0.5f);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST, 1.0f, 0.67f);

                    Char ch = Actor.findChar(cell);
                    if (ch != null) {
                        if (ch instanceof Mob) {
                            Mob mob = (Mob) ch;
                            if (mob.state == mob.SLEEPING) {
                                mob.beckon(hero.pos);
                                if (mob.fieldOfView[hero.pos] && hero.invisible == 0) {
                                    mob.state = mob.HUNTING;
                                }
                            }
                        }
                        if (ch.alignment != hero.alignment) {
                            int dmg = ch.properties().contains(Char.Property.BOSS)
                                    ? Random.NormalIntRange(40, 60)
                                    : Math.round(ch.HT);
                            ch.damage(dmg, new QweDamage());
                        }
                    }
                }

            }
            detach();
            return super.act();
        }

        private static final String CENTER = "tm_center";
        private static final String CELLS  = "tm_cells";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(CENTER, centerCell);
            if (affectedCells != null) {
                int[] arr = new int[affectedCells.size()];
                for (int i = 0; i < affectedCells.size(); i++) arr[i] = affectedCells.get(i);
                bundle.put(CELLS, arr);
            }
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            centerCell = bundle.getInt(CENTER);
            if (bundle.contains(CELLS)) {
                int[] arr = bundle.getIntArray(CELLS);
                affectedCells = new ArrayList<>();
                for (int cell : arr) affectedCells.add(cell);
            }
        }
    }

    public static class QweDamage {
    }
}
