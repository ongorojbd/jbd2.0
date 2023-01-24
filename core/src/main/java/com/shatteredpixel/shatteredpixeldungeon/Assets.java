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

package com.shatteredpixel.shatteredpixeldungeon;

public class Assets {

	public static class Effects {
		public static final String EFFECTS      = "effects/effects.png";
		public static final String FIREBALL     = "effects/fireball.png";
		public static final String SPECKS       = "effects/specks.png";
		public static final String SPELL_ICONS  = "effects/spell_icons.png";
	}

	public static class Environment {
		public static final String TERRAIN_FEATURES = "environment/terrain_features.png";

		public static final String VISUAL_GRID  = "environment/visual_grid.png";
		public static final String WALL_BLOCKING= "environment/wall_blocking.png";

		public static final String TILES_SEWERS	= "environment/tiles_sewers.png";
		public static final String TILES_PRISON	= "environment/tiles_prison.png";
		public static final String TILES_CAVES	= "environment/tiles_caves.png";
		public static final String TILES_CITY	= "environment/tiles_city.png";
		public static final String TILES_HALLS	= "environment/tiles_halls.png";
		public static final String TILES_LABS	= "environment/tiles_labs.png";

		public static final String WATER_SEWERS	= "environment/water0.png";
		public static final String WATER_PRISON	= "environment/water1.png";
		public static final String WATER_CAVES	= "environment/water2.png";
		public static final String WATER_CITY	= "environment/water3.png";
		public static final String WATER_HALLS	= "environment/water4.png";
		public static final String WATER_LABS	= "environment/water5.png";

		public static final String WEAK_FLOOR       = "environment/custom_tiles/weak_floor.png";
		public static final String SEWER_BOSS       = "environment/custom_tiles/sewer_boss.png";
		public static final String PRISON_QUEST     = "environment/custom_tiles/prison_quests.png";
		public static final String PRISON_EXIT      = "environment/custom_tiles/prison_exit.png";
		public static final String CAVES_BOSS       = "environment/custom_tiles/caves_boss.png";
		public static final String CITY_BOSS        = "environment/custom_tiles/city_boss.png";
		public static final String HALLS_SP         = "environment/custom_tiles/halls_special.png";
	}
	
	//TODO include other font assets here? Some are platform specific though...
	public static class Fonts {
		public static final String PIXELFONT= "fonts/pixel_font.png";
	}

	public static class Interfaces {
		public static final String ARCS_BG  = "interfaces/arcs1.png";
		public static final String ARCS_FG  = "interfaces/arcs2.png";

		public static final String BANNERS  = "interfaces/banners.png";
		public static final String BADGES   = "interfaces/badges.png";
		public static final String LOCKED   = "interfaces/locked_badge.png";

		public static final String CHROME   = "interfaces/chrome.png";
		public static final String ICONS    = "interfaces/icons.png";
		public static final String STATUS   = "interfaces/status_pane.png";
		public static final String MENU     = "interfaces/menu_pane.png";
		public static final String MENU_BTN = "interfaces/menu_button.png";
		public static final String TOOLBAR  = "interfaces/toolbar.png";
		public static final String SHADOW   = "interfaces/shadow.png";
		public static final String BOSSHP   = "interfaces/boss_hp.png";

		public static final String SURFACE  = "interfaces/surface.png";

		public static final String LOADING_SEWERS   = "interfaces/loading_sewers.png";
		public static final String LOADING_PRISON   = "interfaces/loading_prison.png";
		public static final String LOADING_CAVES    = "interfaces/loading_caves.png";
		public static final String LOADING_CITY     = "interfaces/loading_city.png";
		public static final String LOADING_HALLS    = "interfaces/loading_halls.png";

		public static final String BUFFS_SMALL      = "interfaces/buffs.png";
		public static final String BUFFS_LARGE      = "interfaces/large_buffs.png";

		public static final String TALENT_ICONS     = "interfaces/talent_icons.png";
		public static final String TALENT_BUTTON    = "interfaces/talent_button.png";

		public static final String HERO_ICONS       = "interfaces/hero_icons.png";

		public static final String RADIAL_MENU      = "interfaces/radial_menu.png";
	}

	//these points to resource bundles, not raw asset files
	public static class Messages {
		public static final String ACTORS   = "messages/actors/actors";
		public static final String ITEMS    = "messages/items/items";
		public static final String JOURNAL  = "messages/journal/journal";
		public static final String LEVELS   = "messages/levels/levels";
		public static final String MISC     = "messages/misc/misc";
		public static final String PLANTS   = "messages/plants/plants";
		public static final String SCENES   = "messages/scenes/scenes";
		public static final String UI       = "messages/ui/ui";
		public static final String WINDOWS  = "messages/windows/windows";
	}

	public static class Music {
		public static final String THEME_1      = "music/theme_1.ogg";
		public static final String THEME_2      = "music/theme_2.ogg";
		public static final String CIV          = "music/civ.ogg";

		public static final String SEWERS_1     = "music/sewers_1.ogg";
		public static final String SEWERS_2     = "music/sewers_2.ogg";
		public static final String DRAGON       = "music/dragon.ogg";
		public static final String SEWERS_BOSS  = "music/sewers_boss.ogg";

		public static final String PRISON_1     = "music/prison_1.ogg";
		public static final String PRISON_2     = "music/prison_2.ogg";
		public static final String PRISON_BOSS  = "music/prison_boss.ogg";

		public static final String CAVES_1      = "music/caves_1.ogg";
		public static final String CAVES_2      = "music/caves_2.ogg";
		public static final String KIRA         = "music/kira.ogg";
		public static final String CAVES_BOSS   = "music/caves_boss.ogg";
		public static final String KOICHI       = "music/koichi.ogg";

		public static final String CITY_1       = "music/city_1.ogg";
		public static final String CITY_2       = "music/city_2.ogg";
		public static final String ENYA         = "music/enya.ogg";
		public static final String CITY_BOSS    = "music/city_boss.ogg";

		public static final String HALLS_1      = "music/halls_1.ogg";
		public static final String HALLS_2      = "music/halls_2.ogg";
		public static final String HALLS_BOSS   = "music/halls_boss.ogg";
		public static final String DIOLOWHP     = "music/diolowhp.ogg";

		public static final String LABS_1     = "music/labs_1.ogg";
		public static final String LABS_BOSS   = "music/labs_boss.ogg";
		public static final String HEAVENDIO   = "music/heavendio.ogg";

	}

	public static class Sounds {
		public static final String CLICK    = "sounds/click.mp3";
		public static final String BADGE    = "sounds/badge.mp3";
		public static final String GOLD     = "sounds/gold.mp3";

		public static final String OPEN     = "sounds/door_open.mp3";
		public static final String UNLOCK   = "sounds/unlock.mp3";
		public static final String ITEM     = "sounds/item.mp3";
		public static final String DEWDROP  = "sounds/dewdrop.mp3";
		public static final String STEP     = "sounds/step.mp3";
		public static final String WATER    = "sounds/water.mp3";
		public static final String GRASS    = "sounds/grass.mp3";
		public static final String TRAMPLE  = "sounds/trample.mp3";
		public static final String STURDY   = "sounds/sturdy.mp3";

		public static final String HIT              = "sounds/hit.mp3";
		public static final String MISS             = "sounds/miss.mp3";
		public static final String HIT_SLASH        = "sounds/hit_slash.mp3";
		public static final String HIT_STAB         = "sounds/hit_stab.mp3";
		public static final String HIT_CRUSH        = "sounds/hit_crush.mp3";
		public static final String HIT_MAGIC        = "sounds/hit_magic.mp3";
		public static final String HIT_STRONG       = "sounds/hit_strong.mp3";
		public static final String HIT_PARRY        = "sounds/hit_parry.mp3";
		public static final String HIT_ARROW        = "sounds/hit_arrow.mp3";
		public static final String HIT_SHOTGUN      = "sounds/hit_shotgun.mp3";
		public static final String HEI              = "sounds/hei.mp3";
		public static final String RELOAD           = "sounds/reload.mp3";
		public static final String ATK_SPIRITBOW    = "sounds/atk_spiritbow.mp3";
		public static final String ATK_CROSSBOW     = "sounds/atk_crossbow.mp3";
		public static final String HEALTH_WARN      = "sounds/health_warn.mp3";
		public static final String HEALTH_CRITICAL  = "sounds/health_critical.mp3";

		public static final String DESCEND  = "sounds/descend.mp3";
		public static final String EAT      = "sounds/eat.mp3";
		public static final String READ     = "sounds/read.mp3";
		public static final String LULLABY  = "sounds/lullaby.mp3";
		public static final String DRINK    = "sounds/drink.mp3";
		public static final String SHATTER  = "sounds/shatter.mp3";
		public static final String ZAP      = "sounds/zap.mp3";
		public static final String LIGHTNING= "sounds/lightning.mp3";
		public static final String LEVELUP  = "sounds/levelup.mp3";
		public static final String DEATH    = "sounds/death.mp3";
		public static final String CHALLENGE= "sounds/challenge.mp3";
		public static final String CURSED   = "sounds/cursed.mp3";
		public static final String TRAP     = "sounds/trap.mp3";
		public static final String EVOKE    = "sounds/evoke.mp3";
		public static final String TOMB     = "sounds/tomb.mp3";
		public static final String ALERT    = "sounds/alert.mp3";
		public static final String MELD     = "sounds/meld.mp3";
		public static final String BOSS     = "sounds/boss.mp3";
		public static final String BLAST    = "sounds/blast.mp3";
		public static final String PLANT    = "sounds/plant.mp3";
		public static final String RAY      = "sounds/ray.mp3";
		public static final String BEACON   = "sounds/beacon.mp3";
		public static final String TELEPORT = "sounds/teleport.mp3";
		public static final String CHARMS   = "sounds/charms.mp3";
		public static final String MASTERY  = "sounds/mastery.mp3";
		public static final String PUFF     = "sounds/puff.mp3";
		public static final String ROCKS    = "sounds/rocks.mp3";
		public static final String BURNING  = "sounds/burning.mp3";
		public static final String FALLING  = "sounds/falling.mp3";
		public static final String GHOST    = "sounds/ghost.mp3";
		public static final String SECRET   = "sounds/secret.mp3";
		public static final String BONES    = "sounds/bones.mp3";
		public static final String BEE      = "sounds/bee.mp3";
		public static final String DEGRADE  = "sounds/degrade.mp3";
		public static final String MIMIC    = "sounds/mimic.mp3";
		public static final String DEBUFF   = "sounds/debuff.mp3";
		public static final String CHARGEUP = "sounds/chargeup.mp3";
		public static final String GAS      = "sounds/gas.mp3";
		public static final String CHAINS   = "sounds/chains.mp3";
		public static final String SCAN     = "sounds/scan.mp3";
		public static final String SHEEP    = "sounds/sheep.mp3";
		public static final String GUITAR    = "sounds/guitar.mp3";
		public static final String FF    = "sounds/ff.mp3";
		public static final String OH    = "sounds/oh.mp3";
		public static final String OH1    = "sounds/oh1.mp3";
		public static final String OH2    = "sounds/oh2.mp3";
		public static final String OVERDRIVE   = "sounds/overdrive.mp3";
		public static final String SP   = "sounds/sp.mp3";
		public static final String HAHAH    = "sounds/hahah.mp3";
		public static final String ZAWARUDO   = "sounds/zawarudo.mp3";
		public static final String NANI   = "sounds/nani.mp3";
		public static final String TBOMB   = "sounds/tbomb.mp3";
		public static final String SHEER = "sounds/sheer.mp3";
		public static final String DIAVOLO = "sounds/diavolo.mp3";
		public static final String STANDO = "sounds/stando.mp3";
		public static final String CRAZYDIO = "sounds/crazydio.mp3";
		public static final String YAREYARE = "sounds/yareyare.mp3";
		public static final String PLATINUM = "sounds/platinum.mp3";
		public static final String ORA = "sounds/ora.mp3";
		public static final String DORA = "sounds/dora.mp3";
		public static final String D4C = "sounds/d4c.mp3";
		public static final String NANIDI = "sounds/nanidi.mp3";
		public static final String D1 = "sounds/d1.mp3";
		public static final String D2 = "sounds/d2.mp3";
		public static final String DIEGO = "sounds/diego.mp3";
		public static final String DIEGO2 = "sounds/diego2.mp3";

		public static final String[] all = new String[]{
				CLICK, BADGE, GOLD,

				OPEN, UNLOCK, ITEM, DEWDROP, STEP, WATER, GRASS, TRAMPLE, STURDY,

				HIT, MISS, HIT_SLASH, HIT_STAB, HIT_CRUSH, HIT_MAGIC, HIT_STRONG, HIT_PARRY,
				HIT_ARROW, HIT_SHOTGUN, HEI, RELOAD, ATK_SPIRITBOW, ATK_CROSSBOW, HEALTH_WARN, HEALTH_CRITICAL,

				DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, LIGHTNING, LEVELUP, DEATH,
				CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
				TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
				BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, CHAINS, SCAN, SHEEP, GUITAR, FF, OH, OH1, OH2, OVERDRIVE, SP,
				HAHAH, ZAWARUDO, NANI, TBOMB, SHEER, DIAVOLO, STANDO, CRAZYDIO ,YAREYARE, PLATINUM, ORA, DORA, D4C, NANIDI, D1, D2, DIEGO, DIEGO2
		};
	}

	public static class Splashes {
		public static final String WARRIOR  = "splashes/warrior.jpg";
		public static final String MAGE     = "splashes/mage.jpg";
		public static final String ROGUE    = "splashes/rogue.jpg";
		public static final String HUNTRESS = "splashes/huntress.jpg";
	}

	public static class Sprites {
		public static final String ITEMS        = "sprites/items.png";
		public static final String ITEM_ICONS   = "sprites/item_icons.png";

		public static final String WARRIOR  = "sprites/warrior.png";
		public static final String MAGE     = "sprites/mage.png";
		public static final String ROGUE    = "sprites/rogue.png";
		public static final String HUNTRESS = "sprites/huntress.png";
		public static final String AVATARS  = "sprites/avatars.png";
		public static final String PET      = "sprites/pet.png";
		public static final String AMULET   = "sprites/amulet.png";

		public static final String RAT      = "sprites/rat.png";
		public static final String BRUTE    = "sprites/brute.png";
		public static final String SPINNER  = "sprites/spinner.png";
		public static final String DM300    = "sprites/dm300.png";
		public static final String WRAITH   = "sprites/wraith.png";
		public static final String UNDEAD   = "sprites/undead.png";
		public static final String KING     = "sprites/king.png";
		public static final String PIRANHA  = "sprites/piranha.png";
		public static final String EYE      = "sprites/eye.png";
		public static final String GNOLL    = "sprites/gnoll.png";
		public static final String CRAB     = "sprites/crab.png";
		public static final String GOO      = "sprites/goo.png";
		public static final String SWARM    = "sprites/swarm.png";
		public static final String SKELETON = "sprites/skeleton.png";
		public static final String SHAMAN   = "sprites/shaman.png";
		public static final String THIEF    = "sprites/thief.png";
		public static final String TENGU    = "sprites/tengu.png";
		public static final String SHEEP    = "sprites/sheep.png";
		public static final String KEEPER   = "sprites/shopkeeper.png";
		public static final String BAT      = "sprites/bat.png";
		public static final String ELEMENTAL= "sprites/elemental.png";
		public static final String MONK     = "sprites/monk.png";
		public static final String WARLOCK  = "sprites/warlock.png";
		public static final String GOLEM    = "sprites/golem.png";
		public static final String STATUE   = "sprites/statue.png";
		public static final String SUCCUBUS = "sprites/succubus.png";
		public static final String SCORPIO  = "sprites/scorpio.png";
		public static final String FISTS    = "sprites/yog_fists.png";
		public static final String YOG      = "sprites/yog.png";
		public static final String LARVA    = "sprites/larva.png";
		public static final String GHOST    = "sprites/ghost.png";
		public static final String MAKER    = "sprites/wandmaker.png";
		public static final String TROLL    = "sprites/blacksmith.png";
		public static final String IMP      = "sprites/demon.png";
		public static final String RATKING  = "sprites/ratking.png";
		public static final String BEE      = "sprites/bee.png";
		public static final String MIMIC    = "sprites/mimic.png";
		public static final String ROT_LASH = "sprites/rot_lasher.png";
		public static final String ROT_HEART= "sprites/rot_heart.png";
		public static final String GUARD    = "sprites/guard.png";
		public static final String WARDS    = "sprites/wards.png";
		public static final String GUARDIAN = "sprites/guardian.png";
		public static final String SLIME    = "sprites/slime.png";
		public static final String SNAKE    = "sprites/snake.png";
		public static final String NECRO    = "sprites/necromancer.png";
		public static final String GHOUL    = "sprites/ghoul.png";
		public static final String GHOUL2    = "sprites/ghoul2.png";
		public static final String GHOUL3    = "sprites/ghoul3.png";
		public static final String RIPPER   = "sprites/ripper.png";
		public static final String SPAWNER  = "sprites/spawner.png";
		public static final String DM100    = "sprites/dm100.png";
		public static final String PYLON    = "sprites/pylon.png";
		public static final String DM200    = "sprites/dm200.png";
		public static final String LOTUS    = "sprites/lotus.png";
		public static final String NINJA_LOG= "sprites/ninja_log.png";
		public static final String SPIRIT_HAWK= "sprites/spirit_hawk.png";
		public static final String SOLDIER= "sprites/soldier.png";
		public static final String RESEARCHER= "sprites/researcher.png";
		public static final String TANK= "sprites/tank.png";
		public static final String SUPRESSION= "sprites/supression.png";
		public static final String MEDIC= "sprites/medic.png";
		public static final String REBEL= "sprites/rebel.png";
		public static final String MUDA= "sprites/muda.png";
		public static final String PUCCI= "sprites/pucci.png";
		public static final String KOUSAKU= "sprites/kousaku.png";
		public static final String JOJO= "sprites/jojo.png";
		public static final String TONIO= "sprites/tonio.png";
		public static final String HIGHDIO= "sprites/highdio.png";
		public static final String MANDOM= "sprites/mandom.png";
		public static final String KHAN= "sprites/khan.png";
		public static final String VITAMINC= "sprites/vitaminc.png";
		public static final String HEAVYW= "sprites/heavyw.png";
		public static final String CIVIL= "sprites/civil.png";
		public static final String BMORE= "sprites/bmore.png";
		public static final String DOPPIO= "sprites/doppio.png";
		public static final String BOYTWO= "sprites/boytwo.png";
		public static final String ACT1= "sprites/act1.png";
		public static final String ACT2= "sprites/act2.png";
		public static final String ACT3= "sprites/act3.png";
		public static final String WILLA= "sprites/willa.png";
		public static final String STOWER= "sprites/stower.png";
		public static final String TUSK1= "sprites/tusk1.png";
		public static final String TUSK3= "sprites/tusk3.png";
		public static final String TUSK4= "sprites/tusk4.png";
		public static final String YUKAKO= "sprites/yukako.png";
		public static final String RETONIO= "sprites/retonio.png";
		public static final String POLPO= "sprites/polpo.png";
		public static final String DIEGO= "sprites/diego.png";
		public static final String WEZA= "sprites/weza.png";
		public static final String WORLD21= "sprites/world21.png";
		public static final String DIEGON= "sprites/diegon.png";
		public static final String CMOON= "sprites/cmoon.png";
		public static final String BCOM= "sprites/bcom.png";
		public static final String RED_SENTRY= "sprites/red_sentry.png";
	}
}
