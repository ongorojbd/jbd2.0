/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
        public static final String EFFECTS = "effects/effects.png";
        public static final String FIREBALL = "effects/fireball.png";
        public static final String SPECKS = "effects/specks.png";
        public static final String SPELL_ICONS = "effects/spell_icons.png";
        public static final String TEXT_ICONS = "effects/text_icons.png";
    }

    public static class Environment {
        public static final String TERRAIN_FEATURES = "environment/terrain_features.png";

        public static final String VISUAL_GRID = "environment/visual_grid.png";
        public static final String WALL_BLOCKING = "environment/wall_blocking.png";

        public static final String TILES_SEWERS = "environment/tiles_sewers.png";
        public static final String TILES_PRISON = "environment/tiles_prison.png";
        public static final String TILES_CAVES = "environment/tiles_caves.png";
        public static final String TILES_CITY = "environment/tiles_city.png";
        public static final String TILES_HALLS = "environment/tiles_halls.png";
        public static final String TILES_CAVES_CRYSTAL = "environment/tiles_caves_crystal.png";
        public static final String TILES_CAVES_GNOLL = "environment/tiles_caves_gnoll.png";
        public static final String TILES_LABS = "environment/tiles_labs.png";
        public static final String TILES_EMPO = "environment/tiles_empo.png";
        public static final String TILES_DIO = "environment/tiles_dio.png";
        public static final String TILES_SHIP = "environment/tiles_ship.png";

        public static final String TILES_KEICHO = "environment/tiles_keicho.png";
        public static final String TILES_TENDENCY = "environment/tiles_tendency.png";

        public static final String WATER_SEWERS = "environment/water0.png";
        public static final String WATER_PRISON = "environment/water1.png";
        public static final String WATER_CAVES = "environment/water2.png";
        public static final String WATER_CITY = "environment/water3.png";
        public static final String WATER_HALLS = "environment/water4.png";
        public static final String WATER_LABS = "environment/water5.png";

        public static final String WEAK_FLOOR = "environment/custom_tiles/weak_floor.png";
        public static final String SEWER_BOSS = "environment/custom_tiles/sewer_boss.png";
        public static final String PRISON_QUEST = "environment/custom_tiles/prison_quest.png";
        public static final String PRISON_EXIT = "environment/custom_tiles/prison_exit.png";
        public static final String CAVES_QUEST = "environment/custom_tiles/caves_quest.png";
        public static final String CAVES_BOSS = "environment/custom_tiles/caves_boss.png";
        public static final String CITY_BOSS = "environment/custom_tiles/city_boss.png";
        public static final String HALLS_SP = "environment/custom_tiles/halls_special.png";
    }

    //TODO include other font assets here? Some are platform specific though...
    public static class Fonts {
        public static final String PIXELFONT = "fonts/pixel_font.png";
    }

    public static class Interfaces {
        public static final String ARCS_BG = "interfaces/arcs1.png";
        public static final String ARCS_FG = "interfaces/arcs2.png";

        public static final String BANNERS = "interfaces/banners.png";
        public static final String BADGES = "interfaces/badges.png";
        public static final String LOCKED = "interfaces/locked_badge.png";

        public static final String CHROME = "interfaces/chrome.png";
        public static final String ICONS = "interfaces/icons.png";
        public static final String STATUS = "interfaces/status_pane.png";
        public static final String MENU = "interfaces/menu_pane.png";
        public static final String MENU_BTN = "interfaces/menu_button.png";
        public static final String TOOLBAR = "interfaces/toolbar.png";
        public static final String SHADOW = "interfaces/shadow.png";
        public static final String BOSSHP = "interfaces/boss_hp.png";

        public static final String SURFACE = "interfaces/surface.png";

        public static final String LOADING_SEWERS = "interfaces/loading_sewers.png";
        public static final String LOADING_PRISON = "interfaces/loading_prison.png";
        public static final String LOADING_CAVES = "interfaces/loading_caves.png";
        public static final String LOADING_CITY = "interfaces/loading_city.png";
        public static final String LOADING_HALLS = "interfaces/loading_halls.png";

        public static final String LOADING_LABS = "interfaces/loading_labs.png";

        public static final String LOADING_DIO = "interfaces/loading_dio.png";

        public static final String BUFFS_SMALL = "interfaces/buffs.png";
        public static final String BUFFS_LARGE = "interfaces/large_buffs.png";

        public static final String TALENT_ICONS = "interfaces/talent_icons.png";
        public static final String TALENT_BUTTON = "interfaces/talent_button.png";

        public static final String HERO_ICONS = "interfaces/hero_icons.png";

        public static final String RADIAL_MENU = "interfaces/radial_menu.png";
    }

    //these points to resource bundles, not raw asset files
    public static class Messages {
        public static final String ACTORS = "messages/actors/actors";
        public static final String ITEMS = "messages/items/items";
        public static final String JOURNAL = "messages/journal/journal";
        public static final String LEVELS = "messages/levels/levels";
        public static final String MISC = "messages/misc/misc";
        public static final String PLANTS = "messages/plants/plants";
        public static final String SCENES = "messages/scenes/scenes";
        public static final String UI = "messages/ui/ui";
        public static final String WINDOWS = "messages/windows/windows";
    }

    public static class Music {
        public static final String THEME_1 = "music/theme_1.ogg";
        public static final String THEME_2 = "music/theme_2.ogg";

        public static final String THEME_3 = "music/theme_3.ogg";

        public static final String CIV = "music/civ.ogg";

        public static final String SEWERS_1 = "music/sewers_1.ogg";
        public static final String SEWERS_2 = "music/sewers_2.ogg";

        public static final String SEWERS_TENSE = "music/sewers_tense.ogg";
        public static final String DRAGON = "music/dragon.ogg";
        public static final String SEWERS_BOSS = "music/sewers_boss.ogg";

        public static final String PRISON_1 = "music/prison_1.ogg";
        public static final String PRISON_2 = "music/prison_2.ogg";
        public static final String PRISON_BOSS = "music/prison_boss.ogg";
        public static final String PRISON_TENSE = "music/prison_tense.ogg";

        public static final String CAVES_1 = "music/caves_1.ogg";
        public static final String CAVES_2 = "music/caves_2.ogg";
        public static final String KIRA = "music/kira.ogg";
        public static final String CAVES_BOSS = "music/caves_boss.ogg";
        public static final String KOICHI = "music/koichi.ogg";

        public static final String CITY_1 = "music/city_1.ogg";
        public static final String CITY_2 = "music/city_2.ogg";
        public static final String ENYA = "music/enya.ogg";
        public static final String CITY_BOSS = "music/city_boss.ogg";

        public static final String HALLS_1 = "music/halls_1.ogg";
        public static final String HALLS_2 = "music/halls_2.ogg";
        public static final String HALLS_BOSS = "music/halls_boss.ogg";
        public static final String DIOLOWHP = "music/diolowhp.ogg";

        public static final String LABS_1 = "music/labs_1.ogg";
        public static final String LABS_BOSS = "music/labs_boss.ogg";
        public static final String HEAVENDIO = "music/heavendio.ogg";

        public static final String EMPO = "music/empo.ogg";

        public static final String DIO_1 = "music/dio_1.ogg";

        public static final String YUUKI = "music/yuuki.ogg";

        public static final String JONATHAN = "music/jonathan.ogg";
        public static final String TENDENCY1 = "music/tendency1.ogg";
        public static final String TENDENCY2 = "music/tendency2.ogg";
    }

    public static class Sounds {
        public static final String CLICK = "sounds/click.mp3";
        public static final String BADGE = "sounds/badge.mp3";
        public static final String GOLD = "sounds/gold.mp3";

        public static final String OPEN = "sounds/door_open.mp3";
        public static final String UNLOCK = "sounds/unlock.mp3";
        public static final String ITEM = "sounds/item.mp3";
        public static final String DEWDROP = "sounds/dewdrop.mp3";
        public static final String STEP = "sounds/step.mp3";
        public static final String WATER = "sounds/water.mp3";
        public static final String GRASS = "sounds/grass.mp3";
        public static final String TRAMPLE = "sounds/trample.mp3";
        public static final String STURDY = "sounds/sturdy.mp3";

        public static final String HIT = "sounds/hit.mp3";
        public static final String MISS = "sounds/miss.mp3";
        public static final String HIT_SLASH = "sounds/hit_slash.mp3";
        public static final String HIT_STAB = "sounds/hit_stab.mp3";
        public static final String HIT_CRUSH = "sounds/hit_crush.mp3";
        public static final String HIT_MAGIC = "sounds/hit_magic.mp3";
        public static final String HIT_STRONG = "sounds/hit_strong.mp3";
        public static final String HIT_PARRY = "sounds/hit_parry.mp3";
        public static final String HIT_ARROW = "sounds/hit_arrow.mp3";
        public static final String HIT_SHOTGUN = "sounds/hit_shotgun.mp3";
        public static final String HIT_ICE = "sounds/hit_ice.mp3";
        public static final String HEI = "sounds/hei.mp3";
        public static final String RELOAD = "sounds/reload.mp3";
        public static final String ATK_SPIRITBOW = "sounds/atk_spiritbow.mp3";
        public static final String ATK_CROSSBOW = "sounds/atk_crossbow.mp3";
        public static final String HEALTH_WARN = "sounds/health_warn.mp3";
        public static final String HEALTH_CRITICAL = "sounds/health_critical.mp3";

        public static final String DESCEND = "sounds/descend.mp3";
        public static final String EAT = "sounds/eat.mp3";
        public static final String READ = "sounds/read.mp3";
        public static final String LULLABY = "sounds/lullaby.mp3";
        public static final String DRINK = "sounds/drink.mp3";
        public static final String SHATTER = "sounds/shatter.mp3";
        public static final String ZAP = "sounds/zap.mp3";
        public static final String LIGHTNING = "sounds/lightning.mp3";
        public static final String LEVELUP = "sounds/levelup.mp3";
        public static final String DEATH = "sounds/death.mp3";
        public static final String CHALLENGE = "sounds/challenge.mp3";
        public static final String CURSED = "sounds/cursed.mp3";
        public static final String TRAP = "sounds/trap.mp3";
        public static final String EVOKE = "sounds/evoke.mp3";
        public static final String TOMB = "sounds/tomb.mp3";
        public static final String ALERT = "sounds/alert.mp3";
        public static final String MELD = "sounds/meld.mp3";
        public static final String BOSS = "sounds/boss.mp3";
        public static final String BLAST = "sounds/blast.mp3";
        public static final String PLANT = "sounds/plant.mp3";
        public static final String RAY = "sounds/ray.mp3";
        public static final String BEACON = "sounds/beacon.mp3";
        public static final String TELEPORT = "sounds/teleport.mp3";
        public static final String CHARMS = "sounds/charms.mp3";
        public static final String MASTERY = "sounds/mastery.mp3";
        public static final String PUFF = "sounds/puff.mp3";
        public static final String ROCKS = "sounds/rocks.mp3";
        public static final String BURNING = "sounds/burning.mp3";
        public static final String FALLING = "sounds/falling.mp3";
        public static final String GHOST = "sounds/ghost.mp3";
        public static final String SECRET = "sounds/secret.mp3";
        public static final String BONES = "sounds/bones.mp3";
        public static final String BEE = "sounds/bee.mp3";
        public static final String DEGRADE = "sounds/degrade.mp3";
        public static final String MIMIC = "sounds/mimic.mp3";
        public static final String DEBUFF = "sounds/debuff.mp3";
        public static final String CHARGEUP = "sounds/chargeup.mp3";
        public static final String GAS = "sounds/gas.mp3";
        public static final String CHAINS = "sounds/chains.mp3";
        public static final String SCAN = "sounds/scan.mp3";
        public static final String SHEEP = "sounds/sheep.mp3";
        public static final String GUITAR = "sounds/guitar.mp3";
        public static final String FF = "sounds/ff.mp3";
        public static final String OH = "sounds/oh.mp3";
        public static final String OH1 = "sounds/oh1.mp3";
        public static final String OH2 = "sounds/oh2.mp3";
        public static final String OVERDRIVE = "sounds/overdrive.mp3";
        public static final String SP = "sounds/sp.mp3";
        public static final String HAHAH = "sounds/hahah.mp3";
        public static final String ZAWARUDO = "sounds/zawarudo.mp3";
        public static final String NANI = "sounds/nani.mp3";
        public static final String TBOMB = "sounds/tbomb.mp3";
        public static final String SHEER = "sounds/sheer.mp3";
        public static final String DIAVOLO = "sounds/diavolo.mp3";
        public static final String CREAM = "sounds/cream.mp3";
        public static final String DIAVOLO1 = "sounds/diavolo1.mp3";
        public static final String DIAVOLO2 = "sounds/diavolo2.mp3";

        public static final String DIAVOLO3 = "sounds/diavolo3.mp3";
        public static final String STANDO = "sounds/stando.mp3";
        public static final String CRAZYDIO = "sounds/crazydio.mp3";
        public static final String YAREYARE = "sounds/yareyare.mp3";
        public static final String PLATINUM = "sounds/platinum.mp3";
        public static final String ORA = "sounds/ora.mp3";
        public static final String DORA = "sounds/dora.mp3";
        public static final String NANIDI = "sounds/nanidi.mp3";
        public static final String D1 = "sounds/d1.mp3";
        public static final String D2 = "sounds/d2.mp3";
        public static final String DIEGO = "sounds/diego.mp3";
        public static final String DIEGO2 = "sounds/diego2.mp3";
        public static final String JOSEPH = "sounds/joseph.mp3";
        public static final String RO1 = "sounds/ro1.mp3";
        public static final String RO2 = "sounds/ro2.mp3";
        public static final String RO3 = "sounds/ro3.mp3";
        public static final String RO4 = "sounds/ro4.mp3";
        public static final String RO5 = "sounds/ro5.mp3";
        public static final String YUKAK = "sounds/yukak.mp3";
        public static final String TALE = "sounds/tale.mp3";
        public static final String NITOH = "sounds/nitoh.mp3";
        public static final String TONIO = "sounds/tonio.mp3";
        public static final String HAMON = "sounds/hamon.mp3";
        public static final String MINE = "sounds/mine.mp3";

        public static final String P1 = "sounds/p1.mp3";
        public static final String P2 = "sounds/p2.mp3";

        public static final String EMP = "sounds/emp.mp3";

        public static final String ANNA = "sounds/anna.mp3";

        public static final String SHEER2 = "sounds/sheer2.mp3";

        public static final String A1 = "sounds/a1.mp3";

        public static final String B1 = "sounds/b1.mp3";
        public static final String B2 = "sounds/b2.mp3";
        public static final String DIO1 = "sounds/dio1.mp3";
        public static final String DIO2 = "sounds/dio2.mp3";
        public static final String DIO3 = "sounds/dio3.mp3";
        public static final String DIO4 = "sounds/dio4.mp3";
        public static final String DIO5 = "sounds/dio5.mp3";
        public static final String DIO6 = "sounds/dio6.mp3";

        public static final String SPW1 = "sounds/spw1.mp3";
        public static final String SPW2 = "sounds/spw2.mp3";

        public static final String SPW3 = "sounds/spw3.mp3";
        public static final String SPW4 = "sounds/spw4.mp3";
        public static final String SPW5 = "sounds/spw5.mp3";
        public static final String SPW6 = "sounds/spw6.mp3";
        public static final String WILLA = "sounds/willa.mp3";
        public static final String MIH = "sounds/mih.mp3";

        public static final String ENYA = "sounds/enya1.mp3";
        public static final String ENYA2 = "sounds/enya2.mp3";
        public static final String DARBY = "sounds/darby.mp3";
        public static final String JUDGE = "sounds/judge.mp3";
        public static final String NUKESAKU = "sounds/nukesaku.mp3";
        public static final String REIMI = "sounds/reimi.mp3";
        public static final String TONIO2 = "sounds/tonio2.mp3";
        public static final String TONIO3 = "sounds/tonio3.mp3";
        public static final String YOSHIHIRO = "sounds/yoshihiro.mp3";
        public static final String WS1 = "sounds/ws1.mp3";
        public static final String WS2 = "sounds/ws2.mp3";
        public static final String WS3 = "sounds/ws3.mp3";

        public static final String ROLLERDA = "sounds/roller.mp3";
        public static final String WOU = "sounds/wou.mp3";
        public static final String WOU2 = "sounds/wou2.mp3";
        public static final String K1 = "sounds/k1.mp3";
        public static final String K2 = "sounds/k2.mp3";

        public static final String K3 = "sounds/k3.mp3";
        public static final String JONATHAN1 = "sounds/jonathan1.mp3";
        public static final String JONATHAN2 = "sounds/jonathan2.mp3";
        public static final String JONATHAN3 = "sounds/jonathan3.mp3";
        public static final String JONNY = "sounds/jonny.mp3";
        public static final String JONNY2 = "sounds/jonny2.mp3";
        public static final String JONNY3 = "sounds/jonny3.mp3";
        public static final String SO1 = "sounds/so1.mp3";
        public static final String SO2 = "sounds/so2.mp3";
        public static final String EVO1 = "sounds/evo1.mp3";
        public static final String EVO2 = "sounds/evo2.mp3";
        public static final String EVO3 = "sounds/evo3.mp3";
        public static final String JOSEPH0 = "sounds/joseph0.mp3";
        public static final String JOSEPH1 = "sounds/joseph1.mp3";
        public static final String JOSEPH2 = "sounds/joseph2.mp3";
        public static final String JOSEPH3 = "sounds/joseph3.mp3";
        public static final String JOSEPH4 = "sounds/joseph4.mp3";
        public static final String JOSEPH5 = "sounds/joseph5.mp3";
        public static final String JOSEPH6 = "sounds/joseph6.mp3";
        public static final String JOSEPH7 = "sounds/joseph7.mp3";
        public static final String JOSEPH8 = "sounds/joseph8.mp3";
        public static final String CE1 = "sounds/ce1.mp3";
        public static final String CE2 = "sounds/ce2.mp3";
        public static final String CE3 = "sounds/ce3.mp3";
        public static final String CE4 = "sounds/ce4.mp3";
        public static final String CE5 = "sounds/ce5.mp3";
        public static final String CE6 = "sounds/ce6.mp3";
        public static final String TENDENCY1 = "sounds/tendency1.mp3";
        public static final String TENDENCY2 = "sounds/tendency2.mp3";
        public static final String TENDENCY3 = "sounds/tendency3.mp3";
        public static final String JOSUKE0 = "sounds/josuke0.mp3";
        public static final String JOSUKE1 = "sounds/josuke1.mp3";
        public static final String JOSUKE2 = "sounds/josuke2.mp3";
        public static final String JOSUKE3 = "sounds/josuke3.mp3";
        public static final String JOSUKE4 = "sounds/josuke4.mp3";
        public static final String JOSUKE5 = "sounds/josuke5.mp3";
        public static final String KIRA1 = "sounds/kira1.mp3";
        public static final String KIRA2 = "sounds/kira2.mp3";
        public static final String KIRA3 = "sounds/kira3.mp3";
        public static final String KIRA4 = "sounds/kira4.mp3";
        public static final String KIRA5 = "sounds/kira5.mp3";
        public static final String KIRA6 = "sounds/kira6.mp3";
        public static final String GIORNO1 = "sounds/giorno1.mp3";
        public static final String GIORNO2 = "sounds/giorno2.mp3";
        public static final String GIORNO3 = "sounds/giorno3.mp3";
        public static final String GIORNO4 = "sounds/giorno4.mp3";
        public static final String GIORNO5 = "sounds/giorno5.mp3";
        public static final String GIORNO6 = "sounds/giorno6.mp3";
        public static final String MISTA1 = "sounds/mista1.mp3";
        public static final String MISTA2 = "sounds/mista2.mp3";
        public static final String MISTA3 = "sounds/mista3.mp3";
        public static final String MISTA4 = "sounds/mista4.mp3";
        public static final String MISTA5 = "sounds/mista5.mp3";
        public static final String MISTA6 = "sounds/mista6.mp3";

        public static final String JSL1 = "sounds/jsl1.mp3";
        public static final String JSL2 = "sounds/jsl2.mp3";
        public static final String JSL3 = "sounds/jsl3.mp3";
        public static final String JSL4 = "sounds/jsl4.mp3";
        public static final String JSF1 = "sounds/jsf1.mp3";
        public static final String JSF2 = "sounds/jsf2.mp3";
        public static final String JSF3 = "sounds/jsf3.mp3";
        public static final String JSF4 = "sounds/jsf4.mp3";
        public static final String JSF5 = "sounds/jsf5.mp3";
        public static final String JT1 = "sounds/jt1.mp3";
        public static final String JT2 = "sounds/jt2.mp3";
        public static final String JT3 = "sounds/jt3.mp3";
        public static final String JT4 = "sounds/jt4.mp3";
        public static final String JT5 = "sounds/jt5.mp3";
        public static final String JT6 = "sounds/jt6.mp3";
        public static final String JT7 = "sounds/jt7.mp3";

        public static final String[] all = new String[]{
                CLICK, BADGE, GOLD,

                OPEN, UNLOCK, ITEM, DEWDROP, STEP, WATER, GRASS, TRAMPLE, STURDY,

                HIT, MISS, HIT_SLASH, HIT_STAB, HIT_CRUSH, HIT_MAGIC, HIT_STRONG, HIT_PARRY,
                HIT_ARROW, HIT_SHOTGUN, HIT_ICE, HEI, RELOAD, ATK_SPIRITBOW, ATK_CROSSBOW, HEALTH_WARN, HEALTH_CRITICAL,

                DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, LIGHTNING, LEVELUP, DEATH,
                CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
                TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
                BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, CHAINS, SCAN, SHEEP, GUITAR, FF, OH, OH1, OH2, OVERDRIVE, SP,
                HAHAH, ZAWARUDO, NANI, TBOMB, SHEER, CREAM, DIAVOLO, DIAVOLO1, DIAVOLO2, DIAVOLO3, STANDO, CRAZYDIO, YAREYARE, PLATINUM, ORA, DORA, NANIDI, D1, D2, DIEGO, DIEGO2,
                JONATHAN1, JONATHAN2, JONATHAN3,
                JOSEPH, JOSEPH0, JOSEPH1, JOSEPH2, JOSEPH3, JOSEPH4, JOSEPH5, JOSEPH6, JOSEPH7, JOSEPH8,
                CE1, CE2, CE3, CE4, CE5, CE6, TENDENCY1, TENDENCY2, TENDENCY3,
                JOSUKE0, JOSUKE1, JOSUKE2, JOSUKE3, JOSUKE4, JOSUKE5,
                KIRA1, KIRA2, KIRA3, KIRA4, KIRA5, KIRA6,
                GIORNO1, GIORNO2, GIORNO3, GIORNO4, GIORNO5, GIORNO6,
                MISTA1, MISTA2, MISTA3, MISTA4, MISTA5, MISTA6,
                RO1, RO2, RO3, RO4, RO5, YUKAK, TALE, NITOH, TONIO, HAMON, MINE, P1, P2, EMP, ANNA, SHEER2, A1, B1, B2, DIO1, DIO2, DIO3, DIO4, DIO5, DIO6, WOU, WOU2, K1, K2, K3, JONNY, JONNY2, JONNY3, SO1, SO2, EVO1, EVO2, EVO3,
                SPW1, SPW2, SPW3, SPW4, SPW5, SPW6, WILLA, MIH, ENYA, ENYA2, DARBY, JUDGE, NUKESAKU, REIMI, TONIO2, TONIO3, YOSHIHIRO, WS1, WS2, WS3, ROLLERDA,
                JSL1, JSL2, JSL3, JSL4, JSF1, JSF2, JSF3, JSF4, JSF5, JT1, JT2, JT3, JT4, JT5, JT6, JT7
        };
    }

    public static class Splashes {
        public static final String WARRIOR = "splashes/warrior.jpg";
        public static final String MAGE = "splashes/mage.jpg";
        public static final String ROGUE = "splashes/rogue.jpg";
        public static final String HUNTRESS = "splashes/huntress.jpg";
        public static final String DUELIST = "splashes/duelist.jpg";
        public static final String CLERIC = "splashes/cleric.jpg";
        public static final String BRANDO = "splashes/brando.jpg";

        public static final String SEWERS = "splashes/sewers.jpg";
        public static final String PRISON = "splashes/prison.jpg";
        public static final String CAVES = "splashes/caves.jpg";
        public static final String CITY = "splashes/city.jpg";
        public static final String HALLS = "splashes/halls.jpg";
        public static final String LABS = "splashes/labs.jpg";
        public static final String SO = "splashes/so.jpg";
    }

    public static class Sprites {
        public static final String ITEMS = "sprites/items.png";
        public static final String ITEM_ICONS = "sprites/item_icons.png";

        public static final String WARRIOR = "sprites/warrior.png";
        public static final String WARRIOR2 = "sprites/warrior2.png";
        public static final String MAGE = "sprites/mage.png";
        public static final String MAGE2 = "sprites/mage2.png";
        public static final String ROGUE = "sprites/rogue.png";
        public static final String ROGUE2 = "sprites/rogue2.png";
        public static final String HUNTRESS = "sprites/huntress.png";
        public static final String HUNTRESS2 = "sprites/huntress2.png";
        public static final String DUELIST = "sprites/duelist.png";
        public static final String DUELIST2 = "sprites/duelist2.png";
        public static final String CLERIC = "sprites/cleric.png";
        public static final String CLERIC2 = "sprites/cleric2.png";
        public static final String AVATARS = "sprites/avatars.png";
        public static final String PET = "sprites/pet.png";
        public static final String AMULET = "sprites/amulet.png";

        public static final String RAT = "sprites/rat.png";
        public static final String BRUTE = "sprites/brute.png";
        public static final String SPINNER = "sprites/spinner.png";
        public static final String DM300 = "sprites/dm300.png";
        public static final String WRAITH = "sprites/wraith.png";
        public static final String UNDEAD = "sprites/undead.png";
        public static final String KING = "sprites/king.png";
        public static final String PIRANHA = "sprites/piranha.png";
        public static final String EYE = "sprites/eye.png";
        public static final String GNOLL = "sprites/gnoll.png";
        public static final String GNOLL2 = "sprites/gnoll2.png";
        public static final String GNOLL3 = "sprites/gnoll3.png";
        public static final String HARVEST = "sprites/harvest.png";
        public static final String SO1 = "sprites/so1.png";
        public static final String SO2 = "sprites/so2.png";
        public static final String CRAB = "sprites/crab.png";
        public static final String GOO = "sprites/goo.png";
        public static final String SWARM = "sprites/swarm.png";
        public static final String SKELETON = "sprites/skeleton.png";
        public static final String SHAMAN = "sprites/shaman.png";
        public static final String THIEF = "sprites/thief.png";
        public static final String TENGU = "sprites/tengu.png";
        public static final String SHEEP = "sprites/sheep.png";
        public static final String KEEPER = "sprites/shopkeeper.png";
        public static final String BAT = "sprites/bat.png";
        public static final String ELEMENTAL = "sprites/elemental.png";

        public static final String MONK = "sprites/monk.png";
        public static final String EVO = "sprites/evo.png";
        public static final String WARLOCK = "sprites/warlock.png";
        public static final String GOLEM = "sprites/golem.png";
        public static final String STATUE = "sprites/statue.png";
        public static final String SUCCUBUS = "sprites/succubus.png";
        public static final String SCORPIO = "sprites/scorpio.png";
        public static final String SCORPIO2 = "sprites/scorpio2.png";
        public static final String SCORPIO3 = "sprites/scorpio3.png";
        public static final String FISTS = "sprites/yog_fists.png";
        public static final String YOG = "sprites/yog.png";
        public static final String LARVA = "sprites/larva.png";
        public static final String GHOST = "sprites/ghost.png";
        public static final String MAKER = "sprites/wandmaker.png";
        public static final String TROLL = "sprites/blacksmith.png";
        public static final String IMP = "sprites/demon.png";
        public static final String RATKING = "sprites/ratking.png";
        public static final String BEE = "sprites/bee.png";
        public static final String MIMIC = "sprites/mimic.png";
        public static final String ROT_LASH = "sprites/rot_lasher.png";
        public static final String ROT_HEART = "sprites/rot_heart.png";
        public static final String GUARD = "sprites/guard.png";
        public static final String WARDS = "sprites/wards.png";
        public static final String WARDS2 = "sprites/wards2.png";
        public static final String WARDS4 = "sprites/wards4.png";
        public static final String GUARDIAN = "sprites/guardian.png";
        public static final String SLIME = "sprites/slime.png";
        public static final String SNAKE = "sprites/snake.png";
        public static final String NECRO = "sprites/necromancer.png";
        public static final String GHOUL = "sprites/ghoul.png";
        public static final String GHOUL2 = "sprites/ghoul2.png";
        public static final String GHOUL3 = "sprites/ghoul3.png";
        public static final String RIPPER = "sprites/ripper.png";
        public static final String SPAWNER = "sprites/spawner.png";
        public static final String DM100 = "sprites/dm100.png";
        public static final String PYLON = "sprites/pylon.png";
        public static final String DM200 = "sprites/dm200.png";
        public static final String DM201 = "sprites/dm201.png";
        public static final String LOTUS = "sprites/lotus.png";

        public static final String NINJA_LOG = "sprites/ninja_log.png";
        public static final String SPIRIT_HAWK = "sprites/spirit_hawk.png";
        public static final String RED_SENTRY = "sprites/red_sentry.png";
        public static final String CRYSTAL_WISP = "sprites/crystal_wisp.png";
        public static final String CRYSTAL_GUARDIAN = "sprites/crystal_guardian.png";
        public static final String CRYSTAL_SPIRE = "sprites/crystal_spire.png";
        public static final String GNOLL_GUARD = "sprites/gnoll_guard.png";
        public static final String GNOLL_SAPPER = "sprites/gnoll_sapper.png";
        public static final String GNOLL_GEOMANCER = "sprites/gnoll_geomancer.png";
        public static final String GNOLL_EXILE = "sprites/gnoll_exile.png";
        public static final String HERMIT = "sprites/hermit.png";
        public static final String FUNGAL_SPINNER = "sprites/fungal_spinner.png";
        public static final String FUNGAL_SENTRY = "sprites/fungal_sentry.png";
        public static final String FUNGAL_CORE = "sprites/fungal_core.png";
        public static final String SOLDIER = "sprites/soldier.png";
        public static final String RESEARCHER = "sprites/researcher.png";
        public static final String TANK = "sprites/tank.png";
        public static final String TANK2 = "sprites/tank2.png";
        public static final String SUPRESSION = "sprites/supression.png";
        public static final String MEDIC = "sprites/medic.png";
        public static final String REBEL = "sprites/rebel.png";
        public static final String MUDA = "sprites/muda.png";
        public static final String PUCCI = "sprites/pucci.png";
        public static final String PUCCI4 = "sprites/pucci4.png";
        public static final String KOUSAKU = "sprites/kousaku.png";
        public static final String JOJO = "sprites/jojo.png";
        public static final String TONIO = "sprites/tonio.png";
        public static final String HIGHDIO = "sprites/highdio.png";
        public static final String MANDOM = "sprites/mandom.png";
        public static final String KHAN = "sprites/khan.png";
        public static final String VITAMINC = "sprites/vitaminc.png";
        public static final String HEAVYW = "sprites/heavyw.png";
        public static final String CIVIL = "sprites/civil.png";
        public static final String BMORE = "sprites/bmore.png";
        public static final String DOPPIO = "sprites/doppio.png";
        public static final String BOYTWO = "sprites/boytwo.png";
        public static final String ACT1 = "sprites/act1.png";
        public static final String ACT2 = "sprites/act2.png";
        public static final String ACT3 = "sprites/act3.png";
        public static final String WILLA = "sprites/willa.png";
        public static final String STOWER = "sprites/stower.png";
        public static final String TUSK1 = "sprites/tusk1.png";
        public static final String TUSK3 = "sprites/tusk3.png";
        public static final String TUSK4 = "sprites/tusk4.png";
        public static final String YUKAKO = "sprites/yukako.png";
        public static final String RETONIO = "sprites/retonio.png";
        public static final String POLPO = "sprites/polpo.png";
        public static final String DIEGO = "sprites/diego.png";
        public static final String WEZA = "sprites/weza.png";
        public static final String WORLD21 = "sprites/world21.png";
        public static final String DIEGON = "sprites/diegon.png";
        public static final String CMOON = "sprites/cmoon.png";
        public static final String BCOM = "sprites/bcom.png";
        public static final String KIRA = "sprites/kira.png";
        public static final String BCOMG = "sprites/bcomg.png";
        public static final String BTANK = "sprites/btank.png";
        public static final String BCOPTER = "sprites/bcopter.png";
        public static final String KEICHO = "sprites/keicho.png";
        public static final String ANTONIO = "sprites/antonio.png";
        public static final String ROHAN = "sprites/rohan.png";
        public static final String KAKYOIN = "sprites/kakyoin.png";
        public static final String CRAZYDIAMOND = "sprites/crazydiamond.png";
        public static final String ATOM = "sprites/atom.png";
        public static final String FUGO = "sprites/fugo.png";
        public static final String PIAN = "sprites/pian.png";
        public static final String COM = "sprites/com.png";
        public static final String YASU = "sprites/yasu.png";
        public static final String FF = "sprites/ff.png";
        public static final String EMPORIO = "sprites/emporio.png";
        public static final String ANNASUI = "sprites/annasui.png";
        public static final String JOTARO= "sprites/jotaro.png";
        public static final String CREAM = "sprites/cream.png";

        public static final String ZOMBIE = "sprites/zombie.png";
        public static final String ZOMBIEDOG = "sprites/zombiedog.png";

        public static final String ZOMBIED = "sprites/zombied.png";

        public static final String ZOMBIEZ = "sprites/zombiez.png";
        public static final String ZOMBIEP = "sprites/zombiep.png";

        public static final String ZOMBIET = "sprites/zombiet.png";
        public static final String ZOMBIET2 = "sprites/zombiet2.png";

        public static final String ZOMBIEB = "sprites/zombieb.png";

        public static final String SPEEDWAGON = "sprites/speedwagon.png";
        public static final String SPEEDWAGON2 = "sprites/speedwagon2.png";
        public static final String DIOBRANDO = "sprites/diobrando.png";
        public static final String DIOBRANDO2 = "sprites/diobrando2.png";

        public static final String INA = "sprites/ina.png";
        public static final String GREEN_CAT_HEAD = "sprites/green_cat_head.png";
        public static final String ROLLER = "sprites/roller.png";
        public static final String ZOMBIE2 = "sprites/zombie2.png";
        public static final String TBOSS = "sprites/tboss.png";
        public static final String SPWSOLDIER = "sprites/spwsoldier.png";
        public static final String GSOLDIER = "sprites/gsoldier.png";
        public static final String ZOMBIEBRUTE = "sprites/zombiebrute.png";
        public static final String ZOMBIEBRUTE2 = "sprites/zombiebrute2.png";
        public static final String ZOMBIEBRUTE3 = "sprites/zombiebrute3.png";
        public static final String STURO = "sprites/sturo.png";
        public static final String WILLSON = "sprites/willson.png";
        public static final String WILLSONMOB = "sprites/willsonmob.png";
        public static final String HORSE = "sprites/horse.png";
        public static final String ZOMBIEG = "sprites/zombieg.png";

        public static final String ZOMBIE3 = "sprites/zombie3.png";
        public static final String VAMPIRE = "sprites/vampire.png";
        public static final String LISA = "sprites/lisa.png";
        public static final String DOPPIODIALOG = "sprites/doppiodialog.png";
        public static final String JOSUKEDIALOG = "sprites/josukedialog.png";
        public static final String DIODIALOG = "sprites/diodialog.png";
    }
}
