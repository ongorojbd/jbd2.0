/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown8;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bt1mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bt2mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Jolyne;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfWeaponEnhance;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfWeaponUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStamina;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UV;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ChaosSword;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class TendencyTreasureLevel extends Level {

    private static final int WIDTH = 17;
    private static final int HEIGHT = 17;

    private static final int EXIT = 8 + 2 * WIDTH;
    private static final int ENTRANCE = 8 + 14 * WIDTH;
    private static final int LEFT_CHEST = 6 + 8 * WIDTH;
    private static final int RIGHT_CHEST = 10 + 8 * WIDTH;

    private static final String EVENT_TYPE = "treasure_event_type";
    private static final String EVENT_RESOLVED = "treasure_event_resolved";
	private int eventType = Random.Int(13);
    private boolean eventResolved;

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.TENDENCY1},
                new float[]{1},
                false);
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TENDENCY2;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        for (int y = 2; y <= 14; y++) {
            for (int x = 2; x <= 14; x++) {
                map[x + y * WIDTH] = Terrain.EMPTY;
            }
        }

        for (int x = 2; x <= 14; x++) {
            map[x + 2 * WIDTH] = Terrain.WALL_DECO;
            map[x + 14 * WIDTH] = Terrain.WALL_DECO;
        }
        for (int y = 2; y <= 14; y++) {
            map[2 + y * WIDTH] = Terrain.WALL_DECO;
            map[14 + y * WIDTH] = Terrain.WALL_DECO;
        }

        for (int y = 5; y <= 11; y++) {
            map[5 + y * WIDTH] = Terrain.WATER;
            map[11 + y * WIDTH] = Terrain.WATER;
        }
        for (int x = 6; x <= 10; x++) {
            map[x + 7 * WIDTH] = Terrain.EMPTY;
            map[x + 9 * WIDTH] = Terrain.EMPTY;
        }

        map[4 + 4 * WIDTH] = Terrain.STATUE;
        map[12 + 4 * WIDTH] = Terrain.STATUE;
        map[4 + 12 * WIDTH] = Terrain.STATUE;
        map[12 + 12 * WIDTH] = Terrain.STATUE;
        map[8 + 8 * WIDTH] = Terrain.EMPTY;
        map[7 + 6 * WIDTH] = Terrain.BARRICADE;
        map[9 + 6 * WIDTH] = Terrain.BARRICADE;
        map[7 + 10 * WIDTH] = Terrain.BARRICADE;
        map[9 + 10 * WIDTH] = Terrain.BARRICADE;

        map[ENTRANCE] = Terrain.ENTRANCE;
        map[EXIT] = Terrain.EXIT;

        transitions.add(new LevelTransition(this, ENTRANCE, LevelTransition.Type.REGULAR_ENTRANCE));
        transitions.add(new LevelTransition(this, EXIT, LevelTransition.Type.REGULAR_EXIT));

        return true;
    }

    @Override
    protected void createMobs() {
    }

    @Override
    protected void createItems() {
    }

    public void showTreasureEvent() {
        if (eventResolved) return;

		if (eventType == 0) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n1"),
					Messages.get(Jolyne.class, "treasure_n2"),
					Messages.get(Jolyne.class, "treasure_n3"),
					Messages.get(Jolyne.class, "treasure_n4")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						giveItem(Generator.randomMissile().upgrade(2).identify());
					} else {
						giveItem(((Wand) Generator.random(Generator.Category.WAND)).upgrade(2).identify());
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 1) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n5"),
					Messages.get(Jolyne.class, "treasure_n6"),
					Messages.get(Jolyne.class, "treasure_n7"),
					Messages.get(Jolyne.class, "treasure_n8")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						Dungeon.hero.HTBoost += 10;
						Dungeon.hero.updateHT(true);
						GLog.p(Messages.get(Jolyne.class, "treasure_hp_gain"));
					} else {
						Dungeon.hero.HTBoost -= 5;
						Dungeon.hero.updateHT(false);
						drop(new Spw().quantity(1), LEFT_CHEST);
						drop(new Spw().quantity(1), RIGHT_CHEST);
						GLog.p(Messages.get(Jolyne.class, "treasure_chests"));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 2) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n9"),
					Messages.get(Jolyne.class, "treasure_n10"),
					Messages.get(Jolyne.class, "treasure_n11"),
					Messages.get(Jolyne.class, "treasure_n12")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						Weapon weapon = Generator.randomWeapon();
						weapon = (Weapon) weapon.upgrade(3).identify();
						weapon.enchant(Weapon.Enchantment.randomCurse());
						weapon.cursed = true;
						weapon.cursedKnown = true;
						giveItem(weapon);
					} else {
						giveItem(new MagicalInfusion().quantity(1));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 3) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n13"),
					Messages.get(Jolyne.class, "treasure_n14"),
					Messages.get(Jolyne.class, "treasure_n15"),
					Messages.get(Jolyne.class, "treasure_n16")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						giveItem(new Sungrass.Seed().quantity(3));
					} else {
						giveItem(new Earthroot.Seed().quantity(8));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 4) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n17"),
					Messages.get(Jolyne.class, "treasure_n18"),
					Messages.get(Jolyne.class, "treasure_n19"),
					Messages.get(Jolyne.class, "treasure_n20")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						for (int i = 0; i < 6; i++) {
							giveItem(Generator.random(Generator.Category.POTION).identify());
						}
					} else {
						for (int i = 0; i < 3; i++) {
							giveItem(randomEnhancedPotion().identify());
						}
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 5) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n21"),
					Messages.get(Jolyne.class, "treasure_n22"),
					Messages.get(Jolyne.class, "treasure_n23"),
					Messages.get(Jolyne.class, "treasure_n24")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						Buff.affect(Dungeon.hero, Bless.class, 500f);
						GLog.p(Messages.get(Jolyne.class, "treasure_reminiscence_bless"));
					} else {
						Barrier barrier = Buff.affect(Dungeon.hero, Barrier.class);
						barrier.incShield(1000);
						GLog.p(Messages.get(Jolyne.class, "treasure_reminiscence_shield"));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 6) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n25"),
					Messages.get(Jolyne.class, "treasure_n26"),
					Messages.get(Jolyne.class, "treasure_n27"),
					Messages.get(Jolyne.class, "treasure_n28")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						giveItem(new StoneOfBlink().quantity(10));
					} else {
						giveItem(new StoneOfAggression().quantity(5));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 7) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n29"),
					Messages.get(Jolyne.class, "treasure_n30"),
					Messages.get(Jolyne.class, "treasure_n31"),
					Messages.get(Jolyne.class, "treasure_n32")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						giveItem(new ChaosSword().upgrade(3).identify());
					} else {
						giveItem(new CurseInfusion().quantity(4));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 8) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n33"),
					Messages.get(Jolyne.class, "treasure_n34"),
					Messages.get(Jolyne.class, "treasure_n35"),
					Messages.get(Jolyne.class, "treasure_n36")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						Dungeon.gold += 500;
						GLog.p(Messages.get(Jolyne.class, "treasure_supply_gold"));
                        Sample.INSTANCE.play(Assets.Sounds.GOLD);
					} else {
						Buff.affect(Dungeon.hero, MindVision.class, 800f);
						Buff.affect(Dungeon.hero, Foresight.class, 800f);
						GLog.p(Messages.get(Jolyne.class, "treasure_supply_vision"));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 9) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n37"),
					Messages.get(Jolyne.class, "treasure_n38"),
					Messages.get(Jolyne.class, "treasure_n39"),
					Messages.get(Jolyne.class, "treasure_n40")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						Statistics.spw29 += 2;

						ShovelDigCoolDown8 cd = Dungeon.hero.buff(ShovelDigCoolDown8.class);
						if (cd != null) {
							float remaining = cd.cooldown();
							Buff.detach(Dungeon.hero, ShovelDigCoolDown8.class);
							if (remaining > 40f) {
								Buff.affect(Dungeon.hero, ShovelDigCoolDown8.class, remaining - 40f);
							}
						}

						GLog.p(Messages.get(Jolyne.class, "treasure_hamon_training"));
					} else {
						spawnMobNear(new Bt1mob(), LEFT_CHEST);
						spawnMobNear(new Bt2mob(), RIGHT_CHEST);
						GLog.p(Messages.get(Jolyne.class, "treasure_hamon_sparring"));
                        Sample.INSTANCE.play(Assets.Sounds.MIMIC);
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 10) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n41"),
					Messages.get(Jolyne.class, "treasure_n42"),
					Messages.get(Jolyne.class, "treasure_n43"),
					Messages.get(Jolyne.class, "treasure_n44")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
                        giveItem(new Tbomb().quantity(10));
					} else {
						giveItem(new UV().quantity(2).identify());
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else if (eventType == 11) {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n45"),
					Messages.get(Jolyne.class, "treasure_n46"),
					Messages.get(Jolyne.class, "treasure_n47"),
					Messages.get(Jolyne.class, "treasure_n48")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						giveItem(new ElixirOfWeaponEnhance().quantity(3).identify());
					} else {
						giveItem(new ElixirOfWeaponUpgrade().quantity(3).identify());
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		} else {
			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SOMETHING),
					Messages.get(Jolyne.class, "treasure_n49"),
					Messages.get(Jolyne.class, "treasure_n50"),
					Messages.get(Jolyne.class, "treasure_n51"),
					Messages.get(Jolyne.class, "treasure_n52")) {
				@Override
				protected void onSelect(int index) {
					eventResolved = true;
					if (index == 0) {
						Dungeon.hero.HP = 1;
						Item ring = Generator.random(Generator.Category.RING).upgrade(3).identify();
						ring.cursed = true;
						ring.cursedKnown = true;
						giveItem(ring);
						GLog.w(Messages.get(Jolyne.class, "treasure_mask_hp1"));
                        Sample.INSTANCE.play(Assets.Sounds.MIMIC);
					} else {
						giveItem(Generator.random(Generator.Category.RING).upgrade(1));
					}
				}

				@Override
				public void onBackPressed() {
					//do nothing
				}
			});
		}
    }

    private void giveItem(Item item) {
        if (!item.collect()) {
            drop(item, Dungeon.hero.pos).sprite.drop();
        }
        Sample.INSTANCE.play(Assets.Sounds.ITEM);
        GLog.p(Messages.get(Jolyne.class, "treasure_item", item.name()));
    }

	private static final Class<?>[] ENHANCED_POTIONS = new Class<?>[]{
			// 엘릭서들
			ElixirOfArcaneArmor.class,
			ElixirOfAquaticRejuvenation.class,
			ElixirOfDragonsBlood.class,
			ElixirOfHoneyedHealing.class,
			ElixirOfIcyTouch.class,
			ElixirOfToxicEssence.class,
			ElixirOfWeaponEnhance.class,
			ElixirOfWeaponUpgrade.class,

			// 특수 물약들 (이그조틱)
			PotionOfCleansing.class,
			PotionOfCorrosiveGas.class,
			PotionOfDragonsBreath.class,
			PotionOfEarthenArmor.class,
			PotionOfDivineInspiration.class,
			PotionOfMagicalSight.class,
			PotionOfShielding.class,
			PotionOfShroudingFog.class,
			PotionOfSnapFreeze.class,
			PotionOfStamina.class,
			PotionOfStormClouds.class
	};

	private Item randomEnhancedPotion() {
		return ((Item) com.watabou.utils.Reflection.newInstance(Random.element(ENHANCED_POTIONS))).random();
	}

	private void spawnMobNear(Mob mob, int preferredPos) {
		int pos = preferredPos;
		if (!passable[pos] || Actor.findChar(pos) != null) {
			pos = -1;
			for (int n : PathFinder.NEIGHBOURS8) {
				int candidate = preferredPos + n;
				if (candidate < 0 || candidate >= length()) continue;
				if (!passable[candidate]) continue;
				if (Actor.findChar(candidate) != null) continue;
				pos = candidate;
				break;
			}
		}
		if (pos == -1) return;

		mob.pos = pos;
		mob.state = mob.HUNTING;
		GameScene.add(mob);
	}

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) return false;
        return super.activateTransition(hero, transition);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(EVENT_TYPE, eventType);
        bundle.put(EVENT_RESOLVED, eventResolved);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(EVENT_TYPE)) {
            eventType = bundle.getInt(EVENT_TYPE);
        }
        eventResolved = bundle.getBoolean(EVENT_RESOLVED);
    }
}
