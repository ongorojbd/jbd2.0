package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LightParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;

public class HamonAmplification extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;
        announced = false;
        revivePersists = true;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            ActionIndicator.setAction(this);
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        ActionIndicator.setAction(this);
    }

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0xFF9933);
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.HAMON_ABILITIES;
    }

    @Override
    public int indicatorColor() {
        return 0x996600;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(trigger);
    }

    private final CellSelector.Listener trigger = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) {
                return;
            }

            Char target = Actor.findChar(cell);
            if (target == null
                    || target == HamonAmplification.this.target
                    || target.alignment != Char.Alignment.ENEMY
                    || !Dungeon.level.heroFOV[target.pos]
                    || target.buff(SoulMark.class) == null) {
                GLog.w(Messages.get(HamonAmplification.class, "bad_target"));
                return;
            }

            Dungeon.hero.busy();

            target.sprite.centerEmitter().burst(LightParticle.BURST, 8);
            Sword.seph();

            if (Dungeon.hero.sprite != null && Dungeon.hero.sprite.parent != null && target.sprite != null) {
                Dungeon.hero.sprite.parent.add(new Beam.SuperNovaPerfectRay(
                        Dungeon.hero.sprite.center(),
                        target.sprite.center(),
                        1.5f));
            }
            Dungeon.hero.sprite.zap(target.pos, new Callback() {
                @Override
                public void call() {
                    triggerExplosion(target);
                }
            });
        }

        @Override
        public String prompt() {
            return Messages.get(HamonAmplification.class, "prompt");
        }
    };

    public static class DarkBolt{}

    private void triggerExplosion(Char target) {
        SoulMark mark = target.buff(SoulMark.class);
        if (mark == null) {
            Dungeon.hero.spendAndNext(0f);
            return;
        }

        int targetPos = target.pos;
        mark.detach();

        int damage = Math.max(5, 10 + Math.round((target.HT - target.HP) * 0.5f));
        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        int[] cells = new int[PathFinder.NEIGHBOURS8.length + 1];
        cells[0] = targetPos;
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            cells[i + 1] = targetPos + PathFinder.NEIGHBOURS8[i];
        }

        for (int affectedCell : cells) {
            if (affectedCell < 0 || affectedCell >= Dungeon.level.length()) {
                continue;
            }

            if (Dungeon.level.heroFOV[affectedCell]) {
                CellEmitter.center(affectedCell).burst(BlastParticle.FACTORY, 30);
                CellEmitter.center(affectedCell).burst(SmokeParticle.FACTORY, 30);
            }

            Char ch = Actor.findChar(affectedCell);
            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                if (ch.sprite != null) {
                    Splash.at(ch.sprite.center(), 0xFFFFD05A, 8);
                }
                ch.damage(damage, new DarkBolt());
            }
        }

        if (!target.isAlive()) {
            ScrollOfTeleportation.teleportToLocation(Dungeon.hero, targetPos);
        }

        Dungeon.hero.spendAndNext(Actor.TICK);
    }
}
