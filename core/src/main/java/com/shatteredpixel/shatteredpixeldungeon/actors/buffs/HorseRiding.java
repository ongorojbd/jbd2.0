package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpiritHorseSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HorseRiding extends Buff implements ActionIndicator.Action, Hero.Doom {

    {
        revivePersists = true;

        announced = true;
    }

    private HorseAlly horse = null;
    private int horseHP = 0;
    private int horseHT = 0;
    private int leapCharges = 0;
    private static final int MAX_LEAP_CHARGES = 5;

    public void set() {
        horseHT = (15+Dungeon.hero.lvl*5);
        horseHP = horseHT;
    }
    
    public void addLeapCharge() {
        if (leapCharges < MAX_LEAP_CHARGES) {
            leapCharges++;
            BuffIndicator.refreshHero();
            ActionIndicator.refresh();
        }
    }
    
    public int getLeapCharges() {
        return leapCharges;
    }
    
    public void setLeapCharges(int charges) {
        this.leapCharges = Math.min(charges, MAX_LEAP_CHARGES);
    }
    
    public int getMaxLeapCharges() {
        return MAX_LEAP_CHARGES;
    }

    public void set(int HP) {
        horseHT = (15+Dungeon.hero.lvl*5);
        horseHP = HP;
        // 도약 충전량은 유지 (이미 존재하는 경우)
        // leapCharges는 현재 값 유지
    }

    public void onLevelUp() {
        horseHT = (15+Dungeon.hero.lvl*5);
        BuffIndicator.refreshHero();
    }

    public void healHorse(int amount) {
        this.horseHP = Math.min(HorseRiding.this.horseHP + amount, HorseRiding.this.horseHT);;
    }

    @Override
    public int icon() {
        return BuffIndicator.SACRIFICE;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", horseHP, horseHT);
    }

    public void onDamage(int damage) {
        damage -= drRoll();
        damage = Math.max(damage, 0); //최소 0
        horseHP -= damage;
        if (horseHP <= 0) {
            detach();
            PixelScene.shake( 2, 1f );
            GLog.n(Messages.get(this, "fall"));
            float dmgMulti = 1;

            //The lower the hero's HP, the more bleed and the less upfront damage.
            //Hero has a 50% chance to bleed out at 66% HP, and begins to risk instant-death at 25%
            int bleedAmt = Math.round(target.HT / (6f + (6f*(target.HP/(float)target.HT))) * dmgMulti);
            int fallDmg = Math.round(Math.max( target.HP / 2, Random.NormalIntRange( target.HP / 2, target.HT / 4 )) * dmgMulti * 0.7f); // 즉시 피해 30% 감소
            Buff.affect( target, Bleeding.class).set( bleedAmt, RideFall.class);
            target.damage( fallDmg, new RideFall() );
            Buff.affect(target, RidingCooldown.class, 200f);
        }
    }

    public static int drRoll() {
        int baseDR = Random.NormalIntRange(2, 16); //기본 방어력: 2~16
        
        // J33 탤런트: 슬로 댄서 방어력 증가
        if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.J33)) {
            int talentLevel = Dungeon.hero.pointsInTalent(Talent.J33);
            int minBonus = talentLevel; // 1, 2, 3
            int maxBonus = talentLevel * 6; // 6, 12, 18
            baseDR += Random.NormalIntRange(minBonus, maxBonus);
        }
        
        return baseDR;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (horseHT - horseHP)/(float)horseHT);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(horseHP);
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.RIDER_ABILITIES;
    }

    @Override
    public Visual secondaryVisual() {
        BitmapText txt = new BitmapText(PixelScene.pixelFont);
        txt.text(leapCharges + "/" + MAX_LEAP_CHARGES);
        txt.hardlight(CharSprite.POSITIVE);
        txt.measure();
        return txt;
    }

    @Override
    public int indicatorColor() {
        return 0x87CEEB;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(leapSelector);
    }

    @Override
    public boolean attachTo(Char target) {
        ActionIndicator.setAction(this);
        return super.attachTo(target);
    }

    @Override
    public void detach() {
        ActionIndicator.clearAction();
        super.detach();
    }

    private void spawnHorse() {
        Hero hero = (Hero) target;
        ArrayList<Integer> spawnPoints = new ArrayList<>();
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                spawnPoints.add(p);
            }
        }

        if (!spawnPoints.isEmpty()) {
            // 기존 체력과 도약 충전량을 그대로 사용 (0이면 최대 체력으로)
            int spawnHP = this.horseHP > 0 ? this.horseHP : this.horseHT;
            int currentLeapCharges = this.leapCharges;
            this.horse = new HorseAlly(hero, spawnHP, currentLeapCharges);

            horse.pos = Random.element(spawnPoints);

            GameScene.add(horse, 1f);
            Dungeon.level.occupyCell(horse);

            Sword.horsesound();
            Sample.INSTANCE.play(Assets.Sounds.HORSE);
            CellEmitter.get(horse.pos).start( Speck.factory(Speck.LIGHT), 0.2f, 3 );

            hero.spend(1f);
            hero.busy();
            hero.sprite.operate(hero.pos);

            detach();
        } else
            GLog.i( Messages.get(this, "no_space") );
    }

    private CellSelector.Listener leapSelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target == null) return;
            
            Hero hero = (Hero) HorseRiding.this.target;
            
            if (hero.rooted) {
                PixelScene.shake(1, 1f);
                return;
            }
            
            // 자기 자신에게 도약하면 말 소환 (충전량 소모 없음, 충전량 체크도 없음)
            if (target == hero.pos) {
                spawnHorse();
                return;
            }
            
            // 충전량 체크 (자기 자신이 아닌 경우에만)
            if (leapCharges <= 0) {
                GLog.w(Messages.get(HorseRiding.class, "no_leap_charge"));
                return;
            }
            
            // 충전량 체크 (자기 자신이 아닌 경우에만)
            if (leapCharges <= 0) {
                GLog.w(Messages.get(HorseRiding.class, "no_leap_charge"));
                return;
            }
            
            // 최대 5칸 거리 제한
            int dist = Dungeon.level.distance(hero.pos, target);
            if (dist > 5) {
                GLog.w(Messages.get(HorseRiding.class, "too_far"));
                return;
            }
            
            Ballistica route = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
            int cell = route.collisionPos;
            
            // 다른 캐릭터가 있는 셀에는 도약할 수 없음
            int backTrace = route.dist - 1;
            while (Actor.findChar(cell) != null && cell != hero.pos) {
                if (backTrace < 0) {
                    GLog.w(Messages.get(HorseRiding.class, "blocked"));
                    return;
                }
                cell = route.path.get(backTrace);
                backTrace--;
            }
            
            // 도약할 수 없는 지형인지 확인
            if (!Dungeon.level.passable[cell] && !(hero.flying && Dungeon.level.avoid[cell])) {
                GLog.w(Messages.get(HorseRiding.class, "blocked"));
                return;
            }
            
            // 목표 장소에 TargetedCell 표시
            if (hero.sprite != null && hero.sprite.parent != null) {
                hero.sprite.parent.addToBack(new TargetedCell(cell, 0xFF00FF));
            }
            
            final int dest = cell;
            final Ballistica finalRoute = route;
            hero.busy();
            
            // 도약 이펙트
            hero.sprite.emitter().start(Speck.factory(Speck.JET), 0.01f, Math.round(4 + 2*Dungeon.level.trueDistance(hero.pos, cell)));
            
            // 도약 속도를 빠르게 (기본 duration의 절반)
            float distance = Math.max(1f, Dungeon.level.trueDistance(hero.pos, cell));
            float height = distance * 2;
            float duration = distance * 0.05f; // 기본 0.1f에서 0.05f로 줄여서 2배 빠르게
            
            hero.sprite.jump(hero.pos, cell, height, duration, new Callback() {
                @Override
                public void call() {
                    // 경로상의 셀 처리 (지형 압박, 적 데미지)
                    ArrayList<Char> pathEnemies = new ArrayList<>();
                    for (int c : finalRoute.subPath(1, finalRoute.dist)) {
                        // 지형 압박
                        if (!hero.flying) {
                            Dungeon.level.pressCell(c);
                        }
                        
                        // 경로상의 적 찾기
                        Char enemy = Actor.findChar(c);
                        if (enemy != null && enemy != hero && enemy.alignment == Char.Alignment.ENEMY) {
                            pathEnemies.add(enemy);
                        }
                    }
                    
                    // 경로상의 적에게 고정 데미지 (4~16)
                    for (Char enemy : pathEnemies) {
                        if (enemy.isAlive()) {
                            int dmg = Random.NormalIntRange(4, 16);
                            dmg -= enemy.drRoll();
                            dmg = Math.max(0, dmg);
                            if (dmg > 0) {
                                enemy.damage(dmg, hero);
                                enemy.sprite.flash();
                            }
                        }
                    }
                    
                    hero.move(dest);
                    Dungeon.level.occupyCell(hero);
                    Dungeon.observe();
                    GameScene.updateFog();
                    
                    WandOfBlastWave.BlastWave.blast(dest);
                    Sword.horsesound();
                    Sample.INSTANCE.play(Assets.Sounds.HORSE);
                    
                    // J33 탤런트: 도약 착지 시 근접한 적들을 밀어내기
                    if (hero.hasTalent(Talent.J33)) {
                        int talentLevel = hero.pointsInTalent(Talent.J33);
                        int pushDistance = 1 + talentLevel; // 2, 3, 4 타일
                        
                        for (int i : PathFinder.NEIGHBOURS8) {
                            Char ch = Actor.findChar(dest + i);
                            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                                Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                                int pushDest = trajectory.collisionPos;
                                
                                // 밀어내기 거리 계산
                                for (int push = 0; push < pushDistance - 1; push++) {
                                    trajectory = new Ballistica(pushDest, pushDest + i, Ballistica.MAGIC_BOLT);
                                    pushDest = trajectory.collisionPos;
                                }
                                
                                if (pushDest != ch.pos) {
                                    Actor.add(new Pushing(ch, ch.pos, pushDest));
                                    ch.pos = pushDest;
                                    Dungeon.level.occupyCell(ch);
                                    ch.sprite.place(ch.pos);
                                }
                            }
                        }
                    }
                    
                    Invisibility.dispel();
                    hero.spendAndNext(Actor.TICK);
                    
                    // 충전량 소모
                    leapCharges--;
                    BuffIndicator.refreshHero();
                    ActionIndicator.refresh();
                }
            });
        }
        
        @Override
        public String prompt() {
            return Messages.get(HorseRiding.class, "leap_prompt");
        }
    };

    @Override
    public void onDeath() {
        Dungeon.fail( this );
        GLog.n( Messages.get(this, "ondeath") );
    }

    private static final String HORSE_HP = "horseHP";
    private static final String HORSE_HT = "horseHT";
    private static final String LEAP_CHARGES = "leapCharges";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(HORSE_HP, horseHP);
        bundle.put(HORSE_HT, horseHT);
        bundle.put(LEAP_CHARGES, leapCharges);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        horseHP = bundle.getInt(HORSE_HP);
        horseHT = bundle.getInt(HORSE_HT);
        if (bundle.contains(LEAP_CHARGES)) {
            leapCharges = bundle.getInt(LEAP_CHARGES);
        } else {
            leapCharges = 0;
        }
        // 말 체력이 0이거나 최대 체력이 0이면 초기화
        if (horseHT == 0 || horseHP == 0) {
            set();
        }
    }

    public static class HorseAlly extends DirectableAlly {
        {
            spriteClass = SpiritHorseSprite.class;

            alignment = Alignment.ALLY;

            //before other mobs
            actPriority = MOB_PRIO + 1;

            followHero();
        }

        private float partialCharge = 0f;
        private int heroLvl = 0;
        private int savedLeapCharges = 0; // 도약 충전량 저장

        public HorseAlly() {
            super();
        }

        public HorseAlly(Hero hero, int HP, int leapCharges) {
            this.HT = (15+hero.lvl*5);
            this.defenseSkill = (hero.lvl+4);
            this.HP = HP;
            this.heroLvl = hero.lvl;
            this.savedLeapCharges = leapCharges;
        }

        @Override
        protected boolean act() {
            if (this.HP < this.HT && Regeneration.regenOn()) {
                partialCharge += 0.1f;
                if (Dungeon.level.map[this.pos] == Terrain.GRASS) {
                    partialCharge += 0.4f; //풀 위에 있으면 회복 속도 5배
                }
                while (partialCharge > 1) {
                    this.HP++;
                    partialCharge--;
                }
            } else {
                partialCharge = 0;
            }
            if (hero != null && hero.lvl != this.heroLvl) updateHorse(hero);
            return super.act();
        }

        @Override
        public boolean canInteract(Char c) {
            return super.canInteract(c);
        }

        @Override
        public boolean interact(Char c) {
            if (c instanceof Hero) {
                // 말 체력 설정
                HorseRiding riding = Buff.affect(c, HorseRiding.class);
                riding.set(this.HP);
                
                // 말이 저장하고 있던 도약 충전량 복원
                riding.setLeapCharges(this.savedLeapCharges);
                BuffIndicator.refreshHero();
                ActionIndicator.refresh();
                
                Sample.INSTANCE.play(Assets.Sounds.HORSE);
                destroy();
                sprite.die();
            }
            return true;
        }

        @Override
        public void die(Object cause) {
            Buff.affect(Dungeon.hero, RidingCooldown.class, 200f);
            super.die(cause);
        }

        @Override
        protected boolean canAttack(Char enemy) { //can't attack
            return false;
        }

        @Override
        public int damageRoll() {
            return 0;
        }

        public void updateHorse(Hero hero){
            //same dodge as the hero
            defenseSkill = (hero.lvl+4);
            HT = (15+hero.lvl*5);
            this.heroLvl = hero.lvl;
        }

        @Override
        public String description() {
            return Messages.get(this, "desc", HP, HT);
        }

        @Override
        public float speed() {
            float speed = super.speed();

            //moves 2 tiles at a time when returning to the hero
            if (state == WANDERING
                    && defendingPos == -1
                    && Dungeon.level.distance(pos, hero.pos) > 1){
                speed *= 2;
            }

            return speed;
        }

        @Override
        public int drRoll() {
            return HorseRiding.drRoll();
        }

        private static final String SAVED_LEAP_CHARGES = "savedLeapCharges";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(SAVED_LEAP_CHARGES, savedLeapCharges);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            savedLeapCharges = bundle.getInt(SAVED_LEAP_CHARGES);
        }
    }

    public static class RideFall implements Hero.Doom {
        @Override
        public void onDeath() {
            Dungeon.fail( this );
            GLog.n( Messages.get(this, "ondeath") );
        }
    }

    public static class RidingCooldown extends FlavourBuff {

        {
            type = buffType.NEUTRAL;
            announced = false;
            revivePersists = true; // 죽어도 유지
        }

        public static final float DURATION = 200f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x808080);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void detach() {
            Buff.affect(target, HorseRiding.class).set();
            super.detach();
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", (int)Math.ceil(visualcooldown()));
        }
    }
}
