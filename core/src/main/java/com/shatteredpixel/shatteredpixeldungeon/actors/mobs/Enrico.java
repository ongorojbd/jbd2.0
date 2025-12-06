package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Puccidisc;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Enrico extends Mob {
    {
        spriteClass = SeniorSprite.class;

        EXP = 14;
        maxLvl = 23;

        HP = HT = 130;
        defenseSkill = 15;

        properties.add(Property.BOSS);
    }

    public int  Phase = 0;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, Phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        Phase = bundle.getInt(PHASE);
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 50){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 50;
        }

        super.damage(dmg, src);

        if (Phase==0 && HP < 129) {
            Phase = 1;
            HP = 129;
            if (!BossHealthBar.isAssigned()) {
                BossHealthBar.assignBoss(this);
                yell(Messages.get(this, "notice"));
            }
        }
        if (Phase==1 && HP < 120) {
            Phase = 2;
            HP = 120;
            GameScene.flash(0x99FFFF);
            new TeleportationTrap().set(target).activate();
            Buff.affect(Dungeon.hero, Blindness.class, 11f);
            Buff.affect(Dungeon.hero, Ooze.class ).set( Ooze.DURATION );
            CellEmitter.center(Dungeon.hero.pos).burst(Speck.factory(Speck.QUESTION), 33);
            Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
            yell(Messages.get(this, "genkaku"));
            Camera.main.shake(31, 0.9f);
        }
        else if (Phase==2 && HP < 100) {
            Phase = 3;
            GameScene.flash(0x99FFFF);
            yell(Messages.get(this, "genkaku2"));
            Buff.affect(Dungeon.hero, Silence.class, 51f);
            Buff.affect(Dungeon.hero, Blindness.class, 15f);
            Buff.affect(Dungeon.hero, Cripple.class, 15f);
            Sample.INSTANCE.play( Assets.Sounds.BLAST, 2, Random.Float(0.33f, 0.66f) );

            CellEmitter.center(Dungeon.hero.pos).burst(Speck.factory(Speck.QUESTION), 33);
            GameScene.flash(0x9999FF);
            Camera.main.shake(9, 0.9f);
        }


    }

    private static final String PHASE   = "Phase";


    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 23, 31 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 27;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 12);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return true;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.drop( new StoneOfEnchantment(), pos ).sprite.drop( pos );

        yell( Messages.get(this, "defeated") );

    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
            if (Random.Int(3) == 0) {
                Buff.affect(enemy, Vertigo.class, 1f);
            }
        return damage;
    }

    public static void spawn(CityLevel level) {
        // 독립적인 시드 오프셋을 사용
        Random.pushGenerator(Dungeon.seedCurDepth() + 999991L);
        int max = 4;
        boolean shouldSpawn = Random.Int( max ) == 0;
        Random.popGenerator();
        
        if (shouldSpawn) {
            if (Dungeon.depth == 19 && !Dungeon.bossLevel()) {

                Enrico npc = new Enrico();
                do {
                    npc.pos = level.randomRespawnCell( npc );
                } while (
                        npc.pos == -1 ||
                                level.heaps.get( npc.pos ) != null ||
                                level.traps.get( npc.pos) != null ||
                                level.findMob( npc.pos ) != null ||
                                level.map[npc.pos] == Terrain.GRASS ||
                                level.map[npc.pos] == Terrain.HIGH_GRASS ||
                                level.map[npc.pos] == Terrain.FURROWED_GRASS ||
                                //The imp doesn't move, so he cannot obstruct a passageway
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
                level.mobs.add( npc );
                
                // 풀 타일을 EMPTY로 변경하여 겹침 방지 (do-while에서 이미 제외했지만 안전을 위해)
                if (level.map[npc.pos] == Terrain.GRASS ||
                        level.map[npc.pos] == Terrain.HIGH_GRASS ||
                        level.map[npc.pos] == Terrain.FURROWED_GRASS) {
                    Level.set(npc.pos, Terrain.EMPTY, level);
                } else if (level.map[npc.pos] != Terrain.EMPTY_DECO) {
                    Level.set(npc.pos, Terrain.EMPTY, level);
                }
            }
        }
    }

}