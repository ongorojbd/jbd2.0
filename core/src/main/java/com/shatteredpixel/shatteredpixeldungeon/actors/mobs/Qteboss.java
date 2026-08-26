package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.World21Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQteBossGame;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Qteboss extends Mob {
    private static final String[] TAUNT_KEYS = {"taunt1", "taunt2", "taunt3"};
    {
        spriteClass = World21Sprite.class;

        HP = HT = 800;
        defenseSkill = 22;

        state = HUNTING;
        viewDistance = Light.DISTANCE;

        EXP = 0;

        immunities.add( Paralysis.class );
        immunities.add( Roots.class );
        immunities.add( Dread.class );
        immunities.add( Terror.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        properties.add(Property.BOSS);

        maxLvl = -9;
    }

    private int phase = 1; // 1~3
    private int qteCooldown = 2;
    private int qteChargeStep = 0;
    private boolean qteGameActive = false;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 22 );
    }

    @Override
    public int attackSkill(Char target) { return 26; }

    public int drRoll() {
        return Random.NormalIntRange(0, 12);
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);

        if (phase == 1 && HP < HT * 0.66f) {
            phase = 2;
        } else if (phase == 2 && HP < HT * 0.33f) {
            phase = 3;
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        // QTE 미니게임이 진행 중이면 대기
        if (qteGameActive) {
            if (WndQteBossGame.instance != null) {
                spend(1f);
                return true;
            }
            // 창이 닫혀있으면 다시 표시
            showQteGame();
            spend(1f);
            return true;
        }

        if (qteCooldown <= 0) {
            useQteAbility();
            return true;
        }

        qteCooldown--;

        return super.act();
    }

    private void useQteAbility() {
        if (qteChargeStep == 0) {
            // 1턴: 카운터 찬스 예고
            GLog.h(Messages.get(this, "qte_ready"));
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "qte_warning"));

            Sample.INSTANCE.play(Assets.Sounds.MIMIC);
            SpellSprite.show(hero, SpellSprite.VISION, 1, 0f, 0f);
            new Flare(8, 32).color(0xFFFF00, true).show(sprite, 2f);

            Dungeon.hero.interrupt();
            qteChargeStep = 1;
            spend(1f);
        } else {
            // 2턴: 카운터 찬스 발동 - 미니게임 시작
            GLog.n(Messages.get(this, "qte_activate"));
            Sample.INSTANCE.play(Assets.Sounds.DIEGO);

            GameScene.flash(0x80FFFF00);

            qteGameActive = true;
            showQteGame();

            qteChargeStep = 0;
            spend(1f);
        }
    }

    private void showQteGame() {
        final Qteboss boss = this;
        final int currentPhase = phase;

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQteBossGame(currentPhase,
                        // 성공 콜백 - 방향 입력 성공, 보스에게 반격 데미지
                        new Callback() {
                            @Override
                            public void call() {
                                qteGameActive = false;

                                Camera.main.shake(6, 0.6f);
                                GameScene.flash(0x8000FF00);

                                GLog.p(Messages.get(Qteboss.class, "qte_success"));
                                GLog.n(Messages.get(Qteboss.class, Random.element(TAUNT_KEYS)));

                                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
                                Sample.INSTANCE.play(Assets.Sounds.DIEGO2);

                                int counterDamage = 25 + phase * 10;
                                boss.damage(counterDamage, hero);
                                boss.sprite.showStatus(CharSprite.NEGATIVE, Integer.toString(counterDamage));

                                qteCooldown = 4;
                            }
                        },
                        // 실패 콜백 - 반응하지 못함
                        new Callback() {
                            @Override
                            public void call() {
                                qteGameActive = false;

                                GameScene.flash(0xFFFF0000);
                                Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                                Sample.INSTANCE.play(Assets.Sounds.DIEGO);

                                GLog.n(Messages.get(Qteboss.class, "qte_fail"));

                                int dmg = Random.NormalIntRange(15 + phase * 5, 25 + phase * 8);
                                hero.damage(dmg, boss);

                                if (hero == Dungeon.hero && !hero.isAlive()) {
                                    Dungeon.fail(Qteboss.class);
                                }

                                qteCooldown = 4;
                            }
                        }
                ));
            }
        });
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        new Flare( 5, 32 ).color( 0xFFFF00, true ).show( hero.sprite, 2f );
    }

    private static final String PHASE = "phase";
    private static final String QTE_COOLDOWN = "qteCooldown";
    private static final String QTE_CHARGE_STEP = "qteChargeStep";
    private static final String QTE_GAME_ACTIVE = "qteGameActive";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, phase );
        bundle.put( QTE_COOLDOWN, qteCooldown );
        bundle.put( QTE_CHARGE_STEP, qteChargeStep );
        bundle.put( QTE_GAME_ACTIVE, qteGameActive );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        qteCooldown = bundle.getInt(QTE_COOLDOWN);
        qteChargeStep = bundle.getInt(QTE_CHARGE_STEP);
        qteGameActive = bundle.getBoolean(QTE_GAME_ACTIVE);
    }
}
