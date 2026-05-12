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
import com.shatteredpixel.shatteredpixeldungeon.items.P2;
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
import java.util.Arrays;

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
        // ?꾩빟 異⑹쟾?됱? ?좎? (?대? 議댁옱?섎뒗 寃쎌슦)
        // leapCharges???꾩옱 媛??좎?
    }

    public void onLevelUp() {
        horseHT = (15+Dungeon.hero.lvl*5);
        BuffIndicator.refreshHero();
    }

    public void healHorse(int amount) {
        this.horseHP = Math.min(HorseRiding.this.horseHP + amount, HorseRiding.this.horseHT);
        ActionIndicator.refresh();
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
        damage = Math.max(damage, 0); //理쒖냼 0
        horseHP -= damage;
        ActionIndicator.refresh();
        if (horseHP <= 0) {
            detach();
            PixelScene.shake( 2, 1f );
            GLog.n(Messages.get(this, "fall"));
            float dmgMulti = 1;
            Sample.INSTANCE.play(Assets.Sounds.HORSE);

            //The lower the hero's HP, the more bleed and the less upfront damage.
            //Hero has a 50% chance to bleed out at 66% HP, and begins to risk instant-death at 25%
            int bleedAmt = Math.round(target.HT / (6f + (6f*(target.HP/(float)target.HT))) * dmgMulti);
            int fallDmg = Math.round(Math.max( target.HP / 2, Random.NormalIntRange( target.HP / 2, target.HT / 4 )) * dmgMulti * 0.7f); // 利됱떆 ?쇳빐 30% 媛먯냼
            Buff.affect( target, Bleeding.class).set( bleedAmt, RideFall.class);
            target.damage( fallDmg, new RideFall() );
            Buff.affect(target, RidingCooldown.class, 150f);
        }
    }

    public static int drRoll() {
        int baseDR = Random.NormalIntRange(2, 16); //湲곕낯 諛⑹뼱?? 2~16
        
        // J33 ?ㅻ윴?? ?щ줈 ?꾩꽌 諛⑹뼱??利앷?
        if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.J33)) {
            int talentLevel = Dungeon.hero.pointsInTalent(Talent.J33);
            int minBonus = talentLevel; // 1, 2, 3
            int maxBonus = talentLevel * 6; // 6, 12, 18
            baseDR += Random.NormalIntRange(minBonus, maxBonus);
        }

        if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.J53)) {
            baseDR *= 2;
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
        // 留먯쓽 泥대젰 ?쒖떆 (?꾩そ) - ?꾩옱 泥대젰留??쒖떆
        BitmapText hpText = new BitmapText(PixelScene.pixelFont);
        hpText.text(Integer.toString(horseHP));
        hpText.hardlight(CharSprite.NEUTRAL);
        hpText.measure();
        
        // ?꾩빟 異⑹쟾???쒖떆 (?꾨옒履?
        BitmapText chargeText = new BitmapText(PixelScene.pixelFont);
        chargeText.text(leapCharges + "/" + MAX_LEAP_CHARGES);
        chargeText.hardlight(CharSprite.POSITIVE);
        chargeText.measure();
        
        // ???띿뒪?몃? ?ы븿?섎뒗 Visual 洹몃９ ?앹꽦
        return new HorseRidingVisual(hpText, chargeText);
    }
    
    private static class HorseRidingVisual extends Visual {
        private BitmapText hpText;
        private BitmapText chargeText;
        
        public HorseRidingVisual(BitmapText hpText, BitmapText chargeText) {
            super(0, 0, Math.max(hpText.width(), chargeText.width()), 
                  hpText.height() + chargeText.height() + 1);
            this.hpText = hpText;
            this.chargeText = chargeText;
        }
        
        @Override
        public void update() {
            super.update();
            // ?먯떇 ?붿냼?ㅼ쓽 ?꾩튂? camera瑜?遺紐⑥뿉 留욊쾶 ?낅뜲?댄듃
            if (hpText != null) {
                hpText.x = this.x + this.width - hpText.width();
                hpText.y = this.y;
                hpText.camera = this.camera;
                hpText.update();
            }
            if (chargeText != null) {
                chargeText.x = this.x + this.width - chargeText.width();
                chargeText.y = this.y + hpText.height() + 1;
                chargeText.camera = this.camera;
                chargeText.update();
            }
        }
        
        @Override
        public void draw() {
            super.draw();
            if (hpText != null && hpText.exists && hpText.visible) {
                hpText.draw();
            }
            if (chargeText != null && chargeText.exists && chargeText.visible) {
                chargeText.draw();
            }
        }
        
        @Override
        public void destroy() {
            super.destroy();
            if (hpText != null) {
                hpText.destroy();
                hpText = null;
            }
            if (chargeText != null) {
                chargeText.destroy();
                chargeText = null;
            }
        }
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
            // 湲곗〈 泥대젰怨??꾩빟 異⑹쟾?됱쓣 洹몃?濡??ъ슜 (0?대㈃ 理쒕? 泥대젰?쇰줈)
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

    private void j53AutoDash(Hero hero) {
        if (leapCharges <= 0) {
            GLog.w(Messages.get(HorseRiding.class, "no_leap_charge"));
            return;
        }

        Char nearest = null;
        float nearDist = Float.MAX_VALUE;
        for (Char ch : Dungeon.level.mobs) {
            if (ch instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC) continue;
            if (ch.isAlive() && ch.alignment == Char.Alignment.ENEMY && Dungeon.level.heroFOV[ch.pos]) {
                float dist = Dungeon.level.trueDistance(hero.pos, ch.pos);
                if (dist < nearDist) {
                    nearDist = dist;
                    nearest = ch;
                }
            }
        }

        if (nearest == null) {
            GLog.w(Messages.get(P2.class, "no_target"));
            return;
        }

        PathFinder.Path pathToEnemy = Dungeon.findPath(hero, nearest.pos,
                Dungeon.level.passable, Dungeon.level.heroFOV, false);

        if (pathToEnemy == null || pathToEnemy.isEmpty()) {
            GLog.w(Messages.get(P2.class, "no_path"));
            return;
        }

        Integer[] segment = pathToEnemy.toArray(new Integer[0]);
        int enemyCell = segment[segment.length - 1];
        int prevCell = segment.length >= 2 ? segment[segment.length - 2] : hero.pos;
        int w = Dungeon.level.width();
        int dCol = Integer.signum((enemyCell % w) - (prevCell % w));
        int dRow = Integer.signum((enemyCell / w) - (prevCell / w));
        int extendDir = dRow * w + dCol;

        ArrayList<Integer> combined = new ArrayList<>(Arrays.asList(segment));
        int extendTarget = enemyCell + extendDir;
        if (extendDir != 0 && extendTarget >= 0 && extendTarget < Dungeon.level.length()) {
            Ballistica extension = new Ballistica(enemyCell, extendTarget, Ballistica.STOP_SOLID);
            for (int i = 1; i <= extension.dist; i++) {
                combined.add(extension.path.get(i));
            }
        }

        leapCharges--;
        BuffIndicator.refreshHero();
        ActionIndicator.refresh();
        Sample.INSTANCE.play(Assets.Sounds.HORSE);

        hero.busy();
        j53DashStep(hero, combined.toArray(new Integer[0]), 0);
    }

    private void j53TargetedDash(Hero hero, int target) {
        if (leapCharges <= 0) {
            GLog.w(Messages.get(HorseRiding.class, "no_leap_charge"));
            return;
        }

        Char selected = Actor.findChar(target);
        if (selected == null || selected == hero || selected.alignment != Char.Alignment.ENEMY || !Dungeon.level.heroFOV[selected.pos]) {
            GLog.w(Messages.get(P2.class, "no_target"));
            return;
        }

        PathFinder.Path pathToEnemy = Dungeon.findPath(hero, selected.pos,
                Dungeon.level.passable, Dungeon.level.heroFOV, false);

        if (pathToEnemy == null || pathToEnemy.isEmpty()) {
            GLog.w(Messages.get(P2.class, "no_path"));
            return;
        }

        Integer[] segment = pathToEnemy.toArray(new Integer[0]);
        int enemyCell = segment[segment.length - 1];
        int prevCell = segment.length >= 2 ? segment[segment.length - 2] : hero.pos;
        int w = Dungeon.level.width();
        int dCol = Integer.signum((enemyCell % w) - (prevCell % w));
        int dRow = Integer.signum((enemyCell / w) - (prevCell / w));
        int extendDir = dRow * w + dCol;

        ArrayList<Integer> combined = new ArrayList<>(Arrays.asList(segment));
        int extendTarget = enemyCell + extendDir;
        if (extendDir != 0 && extendTarget >= 0 && extendTarget < Dungeon.level.length()) {
            Ballistica extension = new Ballistica(enemyCell, extendTarget, Ballistica.STOP_SOLID);
            for (int i = 1; i <= extension.dist; i++) {
                combined.add(extension.path.get(i));
            }
        }

        leapCharges--;
        BuffIndicator.refreshHero();
        ActionIndicator.refresh();
        Sample.INSTANCE.play(Assets.Sounds.HORSE);

        hero.busy();
        j53DashStep(hero, combined.toArray(new Integer[0]), 0);
    }

    private void j53DashStep(final Hero hero, final Integer[] pathArr, final int counter) {
        if (counter < pathArr.length) {
            final int cell = pathArr[counter];

            PixelScene.shake(0.8f, 0.125f);
            hero.sprite.jump(hero.pos, cell, 0f, 0.07f, new Callback() {
                @Override
                public void call() {
                    if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
                        com.shatteredpixel.shatteredpixeldungeon.levels.features.Door.leave(hero.pos);
                    }

                    CellEmitter.get(hero.pos).start(Speck.factory(Speck.JET), 0.01f, 2);
                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch != hero && ch.alignment == Char.Alignment.ENEMY) {
                        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                        int dmg = Math.round(hero.damageRoll() * 3f);
                        dmg = Math.max(0, dmg - ch.drRoll());
                        if (dmg > 0) {
                            ch.damage(dmg, hero);
                            ch.sprite.flash();
                        }
                    }

                    hero.pos = cell;
                    Dungeon.level.occupyCell(hero);

                    hero.sprite.jump(hero.pos, hero.pos, 0f, 0.04f, new Callback() {
                        @Override
                        public void call() {
                            j53DashStep(hero, pathArr, counter + 1);
                        }
                    });
                }
            });
        } else {
            PixelScene.shake(1.1f, 0.3f);
            WandOfBlastWave.BlastWave.blast(hero.pos);
            Buff.prolong(hero, Paralysis.class, 2f);
            Invisibility.dispel();
            Dungeon.observe();
            GameScene.updateFog();
            hero.checkVisibleMobs();
            hero.spendAndNext(Actor.TICK);
        }
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
            
            // ?먭린 ?먯떊?먭쾶 ?꾩빟?섎㈃ 留??뚰솚 (異⑹쟾???뚮え ?놁쓬, 異⑹쟾??泥댄겕???놁쓬)
            if (target == hero.pos) {
                spawnHorse();
                return;
            }
            
            // 異⑹쟾??泥댄겕 (?먭린 ?먯떊???꾨땶 寃쎌슦?먮쭔)
            if (leapCharges <= 0) {
                GLog.w(Messages.get(HorseRiding.class, "no_leap_charge"));
                return;
            }
            
            // 異⑹쟾??泥댄겕 (?먭린 ?먯떊???꾨땶 寃쎌슦?먮쭔)
            if (leapCharges <= 0) {
                GLog.w(Messages.get(HorseRiding.class, "no_leap_charge"));
                return;
            }
            
            // 理쒕? 5移?嫄곕━ ?쒗븳
            if (hero.hasTalent(Talent.J53)) {
                j53TargetedDash(hero, target);
                return;
            }

            int dist = Dungeon.level.distance(hero.pos, target);
            if (dist > 5) {
                GLog.w(Messages.get(HorseRiding.class, "too_far"));
                return;
            }
            
            Ballistica route = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
            int cell = route.collisionPos;
            
            // ?ㅻⅨ 罹먮┃?곌? ?덈뒗 ??먮뒗 ?꾩빟?????놁쓬
            int backTrace = route.dist - 1;
            while (Actor.findChar(cell) != null && cell != hero.pos) {
                if (backTrace < 0) {
                    GLog.w(Messages.get(HorseRiding.class, "blocked"));
                    return;
                }
                cell = route.path.get(backTrace);
                backTrace--;
            }
            
            // ?꾩빟?????녿뒗 吏?뺤씤吏 ?뺤씤
            if (!Dungeon.level.passable[cell] && !(hero.flying && Dungeon.level.avoid[cell])) {
                GLog.w(Messages.get(HorseRiding.class, "blocked"));
                return;
            }
            
            // 紐⑺몴 ?μ냼??TargetedCell ?쒖떆
            if (hero.sprite != null && hero.sprite.parent != null) {
                hero.sprite.parent.addToBack(new TargetedCell(cell, 0xFF00FF));
            }
            
            final int dest = cell;
            final Ballistica finalRoute = route;
            hero.busy();
            
            // ?꾩빟 ?댄럺??            hero.sprite.emitter().start(Speck.factory(Speck.JET), 0.01f, Math.round(4 + 2*Dungeon.level.trueDistance(hero.pos, cell)));
            
            // ?꾩빟 ?띾룄瑜?鍮좊Ⅴ寃?(湲곕낯 duration???덈컲)
            float distance = Math.max(1f, Dungeon.level.trueDistance(hero.pos, cell));
            float height = distance * 2;
            float duration = distance * 0.05f; // 湲곕낯 0.1f?먯꽌 0.05f濡?以꾩뿬??2諛?鍮좊Ⅴ寃?            
            hero.sprite.jump(hero.pos, cell, height, duration, new Callback() {
                @Override
                public void call() {
                    // 寃쎈줈?곸쓽 ? 泥섎━ (吏???뺣컯, ???곕?吏)
                    ArrayList<Char> pathEnemies = new ArrayList<>();
                    for (int c : finalRoute.subPath(1, finalRoute.dist)) {
                        // 吏???뺣컯
                        if (!hero.flying) {
                            Dungeon.level.pressCell(c);
                        }
                        
                        // 寃쎈줈?곸쓽 ??李얘린
                        Char enemy = Actor.findChar(c);
                        if (enemy != null && enemy != hero && enemy.alignment == Char.Alignment.ENEMY) {
                            pathEnemies.add(enemy);
                        }
                    }
                    
                    // 寃쎈줈?곸쓽 ?곸뿉寃??곕?吏 (?뚮젅?댁뼱 ?덈꺼??2諛?3諛?
                    for (Char enemy : pathEnemies) {
                        if (enemy.isAlive()) {
                            int minDmg = hero.lvl * 2;
                            int maxDmg = hero.lvl * 3;
                            int dmg = Random.NormalIntRange(minDmg, maxDmg);
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
                    
                    if (hero.hasTalent(Talent.J33)) {
                        int talentLevel = hero.pointsInTalent(Talent.J33);
                        int pushDistance = 1 + talentLevel; // 2, 3, 4 ???                        
                        for (int i : PathFinder.NEIGHBOURS8) {
                            Char ch = Actor.findChar(dest + i);
                            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                                Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                                int pushDest = ch.pos;
                                
                                // 諛?대궡湲?嫄곕━ 怨꾩궛 (trajectory.path ?ъ슜)
                                for (int push = 0; push < pushDistance && trajectory.path.size() > push + 1; push++) {
                                    int next = trajectory.path.get(push + 1);
                                    if (Dungeon.level.solid[next] || Actor.findChar(next) != null) {
                                        // 踰쎌씠???ㅻⅨ 罹먮┃?곗뿉 遺?ろ엳硫?以묐떒
                                        break;
                                    }
                                    pushDest = next;
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
                    
                    // 異⑹쟾???뚮え
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
        private int savedLeapCharges = 0; // ?꾩빟 異⑹쟾?????
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
                int beforeHeal = this.HP;
                partialCharge += 0.2f;
                if (Dungeon.level.map[this.pos] == Terrain.GRASS) {
                    partialCharge += 0.8f;
                }
                while (partialCharge > 1) {
                    this.HP = Math.min(this.HP + 1, this.HT);
                    partialCharge--;
                }
                if (this.HP > beforeHeal && this.sprite != null) {
                    Buff.affect(this, HorseHealingFX.class, 1.1f);
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
                // 留?泥대젰 ?ㅼ젙
                HorseRiding riding = Buff.affect(c, HorseRiding.class);
                riding.set(this.HP);
                
                // 留먯씠 ??ν븯怨??덈뜕 ?꾩빟 異⑹쟾??蹂듭썝
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
            Buff.affect(Dungeon.hero, RidingCooldown.class, 150f);
            Sample.INSTANCE.play(Assets.Sounds.HORSE);
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

    public static class HorseHealingFX extends FlavourBuff {

        @Override
        public void fx(boolean on) {
            if (target == null || target.sprite == null) {
                return;
            }
            if (on) {
                target.sprite.add(CharSprite.State.HEALING);
            } else {
                target.sprite.remove(CharSprite.State.HEALING);
            }
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
            revivePersists = true; // 二쎌뼱???좎?
        }

        public static final float DURATION = 150f;

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
            Sample.INSTANCE.play(Assets.Sounds.HORSE);
            GLog.p( Messages.get(HorseRiding.class, "sd") );
            CellEmitter.get(hero.pos).start( Speck.factory(Speck.LIGHT), 0.2f, 3 );
            super.detach();
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", (int)Math.ceil(visualcooldown()));
        }
    }
}
