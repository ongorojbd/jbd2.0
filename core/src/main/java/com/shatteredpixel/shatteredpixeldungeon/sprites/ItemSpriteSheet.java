/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ItemSpriteSheet {

    private static final int WIDTH = 16;
    public static final int SIZE = 16;

    public static TextureFilm film = new TextureFilm(Assets.Sprites.ITEMS, SIZE, SIZE);

    private static int xy(int x, int y) {
        x -= 1;
        y -= 1;
        return x + WIDTH * y;
    }

    private static void assignItemRect(int item, int width, int height) {
        int x = (item % WIDTH) * SIZE;
        int y = (item / WIDTH) * SIZE;
        film.add(item, x, y, x + width, y + height);
    }

    private static final int PLACEHOLDERS = xy(1, 1);   //18 slots
    //SOMETHING is the default item sprite at position 0. May show up ingame if there are bugs.
    public static final int SOMETHING = PLACEHOLDERS + 0;
    public static final int WEAPON_HOLDER = PLACEHOLDERS + 1;
    public static final int ARMOR_HOLDER = PLACEHOLDERS + 2;
    public static final int MISSILE_HOLDER = PLACEHOLDERS + 3;
    public static final int WAND_HOLDER = PLACEHOLDERS + 4;
    public static final int RING_HOLDER = PLACEHOLDERS + 5;
    public static final int ARTIFACT_HOLDER = PLACEHOLDERS + 6;
    public static final int TRINKET_HOLDER = PLACEHOLDERS + 7;
    public static final int FOOD_HOLDER = PLACEHOLDERS + 8;
    public static final int BOMB_HOLDER = PLACEHOLDERS + 9;
    public static final int POTION_HOLDER = PLACEHOLDERS + 10;
    public static final int SEED_HOLDER = PLACEHOLDERS + 11;
    public static final int SCROLL_HOLDER = PLACEHOLDERS + 12;
    public static final int STONE_HOLDER = PLACEHOLDERS + 13;
    public static final int ELIXIR_HOLDER = PLACEHOLDERS + 14;
    public static final int SPELL_HOLDER = PLACEHOLDERS + 15;
    public static final int MOB_HOLDER = PLACEHOLDERS + 16;
    public static final int DOCUMENT_HOLDER = PLACEHOLDERS + 17;

    static {
        assignItemRect(SOMETHING, 8, 13);
        assignItemRect(WEAPON_HOLDER, 14, 14);
        assignItemRect(ARMOR_HOLDER, 12, 10);
        assignItemRect(MISSILE_HOLDER, 13, 13);
        assignItemRect(WAND_HOLDER, 14, 14);
        assignItemRect(RING_HOLDER, 14, 12);
        assignItemRect(ARTIFACT_HOLDER, 14, 14);
        assignItemRect(TRINKET_HOLDER, 11, 12);
        assignItemRect(FOOD_HOLDER, 15, 11);
        assignItemRect(BOMB_HOLDER, 10, 10);
        assignItemRect(POTION_HOLDER, 8, 14);
        assignItemRect(SEED_HOLDER, 10, 10);
        assignItemRect(SCROLL_HOLDER, 14, 14);
        assignItemRect(STONE_HOLDER, 10, 10);
        assignItemRect(ELIXIR_HOLDER, 12, 14);
        assignItemRect(SPELL_HOLDER, 8, 16);
        assignItemRect(MOB_HOLDER, 12, 14);
        assignItemRect(DOCUMENT_HOLDER, 10, 11);
    }

    private static final int UNCOLLECTIBLE = xy(3, 2);   //14 slots
    public static final int GOLD = UNCOLLECTIBLE + 0;
    public static final int ENERGY = UNCOLLECTIBLE + 1;

    public static final int DEWDROP = UNCOLLECTIBLE + 3;
    public static final int PETAL = UNCOLLECTIBLE + 4;
    public static final int SANDBAG = UNCOLLECTIBLE + 5;
    public static final int SPIRIT_ARROW = UNCOLLECTIBLE + 6;

    public static final int TENGU_BOMB = UNCOLLECTIBLE + 8;
    public static final int TENGU_SHOCKER = UNCOLLECTIBLE + 9;
    public static final int GEO_BOULDER = UNCOLLECTIBLE + 10;
    public static final int SHOTGUN = UNCOLLECTIBLE + 11;
    public static final int STURO = UNCOLLECTIBLE + 12;

    static {
        assignItemRect(GOLD, 15, 13);
        assignItemRect(ENERGY, 16, 16);

        assignItemRect(DEWDROP, 10, 9);
        assignItemRect(PETAL, 8, 8);
        assignItemRect(SANDBAG, 6, 10);
        assignItemRect(SPIRIT_ARROW, 6, 6);

        assignItemRect(TENGU_BOMB, 12, 12);
        assignItemRect(TENGU_SHOCKER, 11, 11);
        assignItemRect(GEO_BOULDER, 14, 14);

        assignItemRect(SHOTGUN, 16, 8);
        assignItemRect(STURO, 16, 11);
    }

    private static final int CONTAINERS = xy(1, 3);   //16 slots
    public static final int BONES = CONTAINERS + 0;
    public static final int REMAINS = CONTAINERS + 1;
    public static final int TOMB = CONTAINERS + 2;
    public static final int GRAVE = CONTAINERS + 3;
    public static final int CHEST = CONTAINERS + 4;
    public static final int LOCKED_CHEST = CONTAINERS + 5;
    public static final int CRYSTAL_CHEST = CONTAINERS + 6;
    public static final int EBONY_CHEST = CONTAINERS + 7;
    public static final int KINGN = CONTAINERS + 8;
    public static final int KINGB = CONTAINERS + 9;
    public static final int KINGC = CONTAINERS + 10;
    public static final int KINGW = CONTAINERS + 11;
    public static final int KINGM = CONTAINERS + 12;
    public static final int KINGT = CONTAINERS + 13;
    public static final int KINGA = CONTAINERS + 14;
    public static final int KINGS = CONTAINERS + 15;

    static {
        assignItemRect(BONES, 12, 11);
        assignItemRect(REMAINS, 13, 13);
        assignItemRect(TOMB, 14, 15);
        assignItemRect(GRAVE, 15, 16);
        assignItemRect(CHEST, 16, 14);
        assignItemRect(LOCKED_CHEST, 16, 14);
        assignItemRect(CRYSTAL_CHEST, 16, 14);
        assignItemRect(EBONY_CHEST, 10, 10);
        assignItemRect(KINGN, 16, 9);
        assignItemRect(KINGB, 15, 16);
        assignItemRect(KINGC, 12, 14);
        assignItemRect(KINGW, 15, 14);
        assignItemRect(KINGM, 12, 14);
        assignItemRect(KINGT, 11, 14);
        assignItemRect(KINGA, 15, 16);
        assignItemRect(KINGS, 16, 16);
    }

    private static final int MISC_CONSUMABLE = xy(1, 4);   //32 slots
    public static final int ANKH = MISC_CONSUMABLE + 0;
    public static final int STYLUS = MISC_CONSUMABLE + 1;
    public static final int SEAL = MISC_CONSUMABLE + 2;
    public static final int TORCH = MISC_CONSUMABLE + 3;
    public static final int BEACON = MISC_CONSUMABLE + 4;
    public static final int HONEYPOT = MISC_CONSUMABLE + 5;
    public static final int SHATTPOT = MISC_CONSUMABLE + 6;
    public static final int IRON_KEY = MISC_CONSUMABLE + 7;
    public static final int GOLDEN_KEY = MISC_CONSUMABLE + 8;
    public static final int CRYSTAL_KEY = MISC_CONSUMABLE + 9;
    public static final int SKELETON_KEY = MISC_CONSUMABLE + 10;
    public static final int MASK = MISC_CONSUMABLE + 11;
    public static final int CROWN = MISC_CONSUMABLE + 12;
    public static final int AMULET = MISC_CONSUMABLE + 13;
    public static final int MASTERY = MISC_CONSUMABLE + 14;
    public static final int KIT = MISC_CONSUMABLE + 15;
    public static final int SEAL_SHARD = MISC_CONSUMABLE + 16;
    public static final int BROKEN_STAFF = MISC_CONSUMABLE + 17;
    public static final int CLOAK_SCRAP = MISC_CONSUMABLE + 18;
    public static final int BOW_FRAGMENT = MISC_CONSUMABLE + 19;
    public static final int BROKEN_HILT = MISC_CONSUMABLE + 20;
    public static final int TORN_PAGE       = MISC_CONSUMABLE +21;
    public static final int TRINKET_CATA    = MISC_CONSUMABLE +22;

    static {
        assignItemRect(ANKH, 14, 16);
        assignItemRect(STYLUS, 11, 11);

        assignItemRect(SEAL, 15, 16);
        assignItemRect(TORCH, 12, 14);
        assignItemRect(BEACON, 11, 11);

        assignItemRect(HONEYPOT, 8, 12);
        assignItemRect(SHATTPOT, 11, 15);
        assignItemRect(IRON_KEY, 8, 14);
        assignItemRect(GOLDEN_KEY, 8, 14);
        assignItemRect(CRYSTAL_KEY, 8, 14);
        assignItemRect(SKELETON_KEY, 11, 14);
        assignItemRect(MASK, 13, 13);
        assignItemRect(CROWN, 13, 13);
        assignItemRect(AMULET, 16, 16);
        assignItemRect(MASTERY, 13, 16);
        assignItemRect(KIT, 14, 14);

        assignItemRect(SEAL_SHARD, 10, 11);
        assignItemRect(BROKEN_STAFF, 11, 11);
        assignItemRect(CLOAK_SCRAP, 13, 10);
        assignItemRect(BOW_FRAGMENT, 14, 14);
        assignItemRect(BROKEN_HILT, 12, 13);
        assignItemRect(TORN_PAGE,       16, 11);
        assignItemRect(TRINKET_CATA, 16, 15);
    }

    private static final int BOMBS = xy(1, 6);   //16 slots
    public static final int BOMB = BOMBS + 0;
    public static final int DBL_BOMB = BOMBS + 1;
    public static final int FIRE_BOMB = BOMBS + 2;
    public static final int FROST_BOMB = BOMBS + 3;
    public static final int REGROWTH_BOMB = BOMBS + 4;
    public static final int SMOKE_BOMB = BOMBS + 5;
    public static final int FLASHBANG = BOMBS + 6;
    public static final int HOLY_BOMB = BOMBS + 7;
    public static final int WOOLY_BOMB = BOMBS + 8;
    public static final int NOISEMAKER = BOMBS + 9;
    public static final int ARCANE_BOMB = BOMBS + 10;
    public static final int SHRAPNEL_BOMB = BOMBS + 11;

    static {
        assignItemRect(BOMB, 12, 12);
        assignItemRect(DBL_BOMB, 15, 11);
        assignItemRect(FIRE_BOMB, 11, 11);
        assignItemRect(FROST_BOMB, 11, 11);
        assignItemRect(REGROWTH_BOMB, 11, 11);
        assignItemRect(SMOKE_BOMB, 11, 11);
        assignItemRect(FLASHBANG, 12, 12);
        assignItemRect(HOLY_BOMB, 12, 12);
        assignItemRect(WOOLY_BOMB, 12, 12);
        assignItemRect(NOISEMAKER, 12, 12);
        assignItemRect(ARCANE_BOMB, 12, 12);
        assignItemRect(SHRAPNEL_BOMB, 12, 12);
    }


    //16 free slots

    private static final int WEP_TIER1 = xy(1, 7);   //8 slots
    public static final int WORN_SHORTSWORD = WEP_TIER1 + 0;
    public static final int CUDGEL = WEP_TIER1 + 1;
    public static final int GLOVES = WEP_TIER1 + 2;
    public static final int RAPIER = WEP_TIER1 + 3;
    public static final int DAGGER = WEP_TIER1 + 4;
    public static final int MAGES_STAFF = WEP_TIER1 + 5;

    static {
        assignItemRect(WORN_SHORTSWORD, 16, 16);
        assignItemRect(CUDGEL, 16, 16);
        assignItemRect(GLOVES, 12, 16);
        assignItemRect(RAPIER, 13, 14);
        assignItemRect(DAGGER, 10, 13);
        assignItemRect(MAGES_STAFF, 15, 16);
    }

    private static final int WEP_TIER2 = xy(9, 7);   //8 slots
    public static final int SHORTSWORD = WEP_TIER2 + 0;
    public static final int HAND_AXE = WEP_TIER2 + 1;
    public static final int SPEAR = WEP_TIER2 + 2;
    public static final int QUARTERSTAFF = WEP_TIER2 + 3;
    public static final int DIRK = WEP_TIER2 + 4;
    public static final int SICKLE = WEP_TIER2 + 5;

    static {
        assignItemRect(SHORTSWORD, 13, 13);
        assignItemRect(HAND_AXE, 12, 14);
        assignItemRect(SPEAR, 16, 16);
        assignItemRect(QUARTERSTAFF, 16, 16);
        assignItemRect(DIRK, 14, 14);
        assignItemRect(SICKLE, 13, 14);
    }

    private static final int WEP_TIER3 = xy(1, 8);   //8 slots
    public static final int SWORD = WEP_TIER3 + 0;
    public static final int MACE = WEP_TIER3 + 1;
    public static final int SCIMITAR = WEP_TIER3 + 2;
    public static final int ROUND_SHIELD = WEP_TIER3 + 3;
    public static final int SAI = WEP_TIER3 + 4;
    public static final int WHIP = WEP_TIER3 + 5;

    static {
        assignItemRect(SWORD, 12, 14);
        assignItemRect(MACE, 14, 15);
        assignItemRect(SCIMITAR, 16, 15);
        assignItemRect(ROUND_SHIELD, 14, 16);
        assignItemRect(SAI, 16, 16);
        assignItemRect(WHIP, 10, 14);
    }

    private static final int WEP_TIER4 = xy(9, 8);   //8 slots
    public static final int LONGSWORD = WEP_TIER4 + 0;
    public static final int BATTLE_AXE = WEP_TIER4 + 1;
    public static final int FLAIL = WEP_TIER4 + 2;
    public static final int RUNIC_BLADE = WEP_TIER4 + 3;
    public static final int ASSASSINS_BLADE = WEP_TIER4 + 4;
    public static final int CROSSBOW = WEP_TIER4 + 5;
    public static final int KATANA = WEP_TIER4 + 6;

    static {
        assignItemRect(LONGSWORD, 15, 15);
        assignItemRect(BATTLE_AXE, 16, 16);
        assignItemRect(FLAIL, 14, 14);
        assignItemRect(RUNIC_BLADE, 16, 16);
        assignItemRect(ASSASSINS_BLADE, 14, 15);
        assignItemRect(CROSSBOW, 15, 15);
        assignItemRect(KATANA, 15, 15);
    }

    private static final int WEP_TIER5 = xy(1, 9);   //8 slots
    public static final int GREATSWORD = WEP_TIER5 + 0;
    public static final int WAR_HAMMER = WEP_TIER5 + 1;
    public static final int GLAIVE = WEP_TIER5 + 2;
    public static final int GREATAXE = WEP_TIER5 + 3;
    public static final int GREATSHIELD = WEP_TIER5 + 4;
    public static final int GAUNTLETS = WEP_TIER5 + 5;
    public static final int WAR_SCYTHE = WEP_TIER5 + 6;
    public static final int LSWORD = WEP_TIER5 + 8;
    public static final int DBLADE = WEP_TIER5 + 9;
    public static final int MISTA = WEP_TIER5 + 10;
    public static final int PINK = WEP_TIER5 + 11;
    public static final int AJA = WEP_TIER5 + 12;
    public static final int SPWORLD = WEP_TIER5 + 13;

    static {
        assignItemRect(GREATSWORD, 16, 16);
        assignItemRect(WAR_HAMMER, 13, 16);
        assignItemRect(GLAIVE, 16, 16);
        assignItemRect(GREATAXE, 12, 16);
        assignItemRect(GREATSHIELD, 11, 16);
        assignItemRect(GAUNTLETS, 16, 16);
        assignItemRect(WAR_SCYTHE, 15, 11);
        assignItemRect(LSWORD, 16, 16);
        assignItemRect(DBLADE, 16, 16);
        assignItemRect(MISTA, 15, 14);
        assignItemRect(PINK, 13, 16);
        assignItemRect(AJA, 16, 16);
        assignItemRect(SPWORLD, 10, 13);
    }

    //8 free slots

    private static final int MISSILE_WEP = xy(1, 10);  //16 slots. 3 per tier + bow
    public static final int SPIRIT_BOW = MISSILE_WEP + 0;

    public static final int THROWING_SPIKE = MISSILE_WEP + 1;
    public static final int THROWING_KNIFE = MISSILE_WEP + 2;
    public static final int THROWING_STONE = MISSILE_WEP + 3;

    public static final int FISHING_SPEAR = MISSILE_WEP + 4;
    public static final int SHURIKEN = MISSILE_WEP + 5;
    public static final int THROWING_CLUB = MISSILE_WEP + 6;

    public static final int THROWING_SPEAR = MISSILE_WEP + 7;
    public static final int BOLAS = MISSILE_WEP + 8;
    public static final int KUNAI = MISSILE_WEP + 9;

    public static final int JAVELIN = MISSILE_WEP + 10;
    public static final int TOMAHAWK = MISSILE_WEP + 11;
    public static final int BOOMERANG = MISSILE_WEP + 12;

    public static final int TRIDENT = MISSILE_WEP + 13;
    public static final int THROWING_HAMMER = MISSILE_WEP + 14;
    public static final int FORCE_CUBE = MISSILE_WEP + 15;

    static {
        assignItemRect(SPIRIT_BOW, 12, 16);

        assignItemRect(THROWING_SPIKE, 9, 8);

        assignItemRect(THROWING_KNIFE, 12, 13);
        assignItemRect(THROWING_STONE, 14, 14);

        assignItemRect(FISHING_SPEAR, 6, 6);
        assignItemRect(SHURIKEN, 11, 12);
        assignItemRect(THROWING_CLUB, 10, 10);

        assignItemRect(THROWING_SPEAR, 10, 10);
        assignItemRect(BOLAS, 15, 13);
        assignItemRect(KUNAI, 15, 15);

        assignItemRect(JAVELIN, 11, 14);
        assignItemRect(TOMAHAWK, 14, 14);
        assignItemRect(BOOMERANG, 14, 14);

        assignItemRect(TRIDENT, 13, 9);
        assignItemRect(THROWING_HAMMER, 10, 10);
        assignItemRect(FORCE_CUBE, 11, 12);
    }

    public static final int DARTS = xy(1, 11);  //16 slots
    public static final int DART = DARTS + 0;
    public static final int ROT_DART = DARTS + 1;
    public static final int INCENDIARY_DART = DARTS + 2;
    public static final int ADRENALINE_DART = DARTS + 3;
    public static final int HEALING_DART = DARTS + 4;
    public static final int CHILLING_DART = DARTS + 5;
    public static final int SHOCKING_DART = DARTS + 6;
    public static final int POISON_DART = DARTS + 7;
    public static final int CLEANSING_DART = DARTS + 8;
    public static final int PARALYTIC_DART = DARTS + 9;
    public static final int HOLY_DART = DARTS + 10;
    public static final int DISPLACING_DART = DARTS + 11;
    public static final int BLINDING_DART = DARTS + 12;

    static {
        for (int i = DARTS; i < DARTS + 16; i++)
            assignItemRect(i, 9, 9);

    }

    private static final int ARMOR = xy(1, 12);  //16 slots
    public static final int ARMOR_CLOTH = ARMOR + 0;
    public static final int ARMOR_LEATHER = ARMOR + 1;
    public static final int ARMOR_MAIL = ARMOR + 2;
    public static final int ARMOR_SCALE = ARMOR + 3;
    public static final int ARMOR_PLATE = ARMOR + 4;
    public static final int ARMOR_WARRIOR = ARMOR + 5;
    public static final int ARMOR_MAGE = ARMOR + 6;
    public static final int ARMOR_ROGUE = ARMOR + 7;
    public static final int ARMOR_HUNTRESS = ARMOR + 8;
    public static final int ARMOR_DUELIST = ARMOR + 9;
    public static final int ARMOR_CLERIC = ARMOR+10;
    public static final int MAP0 = ARMOR + 11;
    public static final int HIGHWAY = ARMOR + 12;
    public static final int MAP1 = ARMOR + 13;
    public static final int MAP2 = ARMOR + 14;
    public static final int MAP3 = ARMOR + 15;

    static {
        assignItemRect(ARMOR_CLOTH, 16, 16);
        assignItemRect(ARMOR_LEATHER, 16, 16);
        assignItemRect(ARMOR_MAIL, 16, 16);
        assignItemRect(ARMOR_SCALE, 16, 16);
        assignItemRect(ARMOR_PLATE, 16, 16);
        assignItemRect(ARMOR_WARRIOR, 14, 14);
        assignItemRect(ARMOR_MAGE, 15, 15);
        assignItemRect(ARMOR_ROGUE, 14, 8);
        assignItemRect(ARMOR_HUNTRESS, 13, 13);
        assignItemRect(ARMOR_DUELIST, 16, 16);
        assignItemRect(ARMOR_CLERIC, 14, 13);
        assignItemRect(MAP0, 12, 16);
        assignItemRect(HIGHWAY, 16, 16);
        assignItemRect(MAP1, 12, 16);
        assignItemRect(MAP2, 12, 16);
        assignItemRect(MAP3, 12, 16);
    }

    private static final int BOOK = xy(1, 13);  //16 slots

    public static final int WALL = BOOK + 0;
    public static final int JOJO1 = BOOK + 1;
    public static final int JOJO2 = BOOK + 2;
    public static final int JOJO3 = BOOK + 3;
    public static final int JOJO4 = BOOK + 4;
    public static final int JOJO5 = BOOK + 5;
    public static final int JOJO6 = BOOK + 6;
    public static final int JOJO7 = BOOK + 7;
    public static final int JOJO8 = BOOK + 8;
    public static final int JOJO9 = BOOK + 9;
    public static final int CEN = BOOK + 10;
    public static final int DEMON = BOOK + 11;
    public static final int DANNY = BOOK + 12;
    public static final int SMASK = BOOK + 13;
    public static final int DIOCASTLE = BOOK + 14;
    public static final int DIOCOFFIN = BOOK + 15;

    static {
        assignItemRect(WALL, 16, 16);
        assignItemRect(JOJO1, 12, 16);
        assignItemRect(JOJO2, 12, 16);
        assignItemRect(JOJO3, 12, 16);
        assignItemRect(JOJO4, 12, 16);
        assignItemRect(JOJO5, 12, 16);
        assignItemRect(JOJO6, 12, 16);
        assignItemRect(JOJO7, 12, 16);
        assignItemRect(JOJO8, 12, 16);
        assignItemRect(JOJO9, 12, 16);
        assignItemRect(CEN, 15, 16);
        assignItemRect(DEMON, 16, 12);
        assignItemRect(DANNY, 15, 11);
        assignItemRect(SMASK, 14, 12);
        assignItemRect(DIOCASTLE, 16, 16);
        assignItemRect(DIOCOFFIN, 16, 16);
    }


    private static final int WANDS = xy(1, 14);  //16 slots
    public static final int WAND_MAGIC_MISSILE = WANDS + 0;
    public static final int WAND_FIREBOLT = WANDS + 1;
    public static final int WAND_FROST = WANDS + 2;
    public static final int WAND_LIGHTNING = WANDS + 3;
    public static final int WAND_DISINTEGRATION = WANDS + 4;
    public static final int WAND_PRISMATIC_LIGHT = WANDS + 5;
    public static final int WAND_CORROSION = WANDS + 6;
    public static final int WAND_LIVING_EARTH = WANDS + 7;
    public static final int WAND_BLAST_WAVE = WANDS + 8;
    public static final int WAND_CORRUPTION = WANDS + 9;
    public static final int WAND_WARDING = WANDS + 10;
    public static final int WAND_REGROWTH = WANDS + 11;
    public static final int WAND_TRANSFUSION = WANDS + 12;

    static {
        for (int i = WANDS; i < WANDS + 16; i++)
            assignItemRect(i, 14, 14);
    }

    private static final int RINGS = xy(1, 15);  //16 slots
    public static final int RING_GARNET = RINGS + 0;
    public static final int RING_RUBY = RINGS + 1;
    public static final int RING_TOPAZ = RINGS + 2;
    public static final int RING_EMERALD = RINGS + 3;
    public static final int RING_ONYX = RINGS + 4;
    public static final int RING_OPAL = RINGS + 5;
    public static final int RING_TOURMALINE = RINGS + 6;
    public static final int RING_SAPPHIRE = RINGS + 7;
    public static final int RING_AMETHYST = RINGS + 8;
    public static final int RING_QUARTZ = RINGS + 9;
    public static final int RING_AGATE = RINGS + 10;
    public static final int RING_DIAMOND = RINGS + 11;

    static {
        for (int i = RINGS; i < RINGS + 16; i++)
            assignItemRect(i, 13, 16);
    }

    private static final int ARTIFACTS = xy(1, 16);  //24 slots
    public static final int ARTIFACT_CLOAK = ARTIFACTS + 0;
    public static final int ARTIFACT_ARMBAND = ARTIFACTS + 1;
    public static final int ARTIFACT_CAPE = ARTIFACTS + 2;
    public static final int ARTIFACT_TALISMAN = ARTIFACTS + 3;
    public static final int ARTIFACT_HOURGLASS = ARTIFACTS + 4;
    public static final int ARTIFACT_TOOLKIT = ARTIFACTS + 5;
    public static final int ARTIFACT_SPELLBOOK = ARTIFACTS + 6;
    public static final int ARTIFACT_BEACON = ARTIFACTS + 7;
    public static final int ARTIFACT_CHAINS = ARTIFACTS + 8;
    public static final int ARTIFACT_HORN1 = ARTIFACTS + 9;
    public static final int ARTIFACT_HORN2 = ARTIFACTS + 10;
    public static final int ARTIFACT_HORN3 = ARTIFACTS + 11;
    public static final int ARTIFACT_HORN4 = ARTIFACTS + 12;
    public static final int ARTIFACT_CHALICE1 = ARTIFACTS + 13;
    public static final int ARTIFACT_CHALICE2 = ARTIFACTS + 14;
    public static final int ARTIFACT_CHALICE3 = ARTIFACTS + 15;
    public static final int ARTIFACT_SANDALS = ARTIFACTS + 16;
    public static final int ARTIFACT_SHOES = ARTIFACTS + 17;
    public static final int ARTIFACT_BOOTS = ARTIFACTS + 18;
    public static final int ARTIFACT_GREAVES = ARTIFACTS + 19;
    public static final int ARTIFACT_ROSE1 = ARTIFACTS + 20;
    public static final int ARTIFACT_ROSE2 = ARTIFACTS + 21;
    public static final int ARTIFACT_ROSE3 = ARTIFACTS + 22;
    public static final int ARTIFACT_TOME = ARTIFACTS + 23;

    static {
        assignItemRect(ARTIFACT_CLOAK, 14, 14);
        assignItemRect(ARTIFACT_ARMBAND, 14, 14);
        assignItemRect(ARTIFACT_CAPE, 14, 14);
        assignItemRect(ARTIFACT_TALISMAN, 14, 14);
        assignItemRect(ARTIFACT_HOURGLASS, 14, 14);
        assignItemRect(ARTIFACT_TOOLKIT, 14, 14);
        assignItemRect(ARTIFACT_SPELLBOOK, 14, 14);
        assignItemRect(ARTIFACT_BEACON, 14, 14);
        assignItemRect(ARTIFACT_CHAINS, 14, 14);
        assignItemRect(ARTIFACT_HORN1, 14, 14);
        assignItemRect(ARTIFACT_HORN2, 14, 14);
        assignItemRect(ARTIFACT_HORN3, 14, 14);
        assignItemRect(ARTIFACT_HORN4, 14, 14);
        assignItemRect(ARTIFACT_CHALICE1, 14, 14);
        assignItemRect(ARTIFACT_CHALICE2, 14, 14);
        assignItemRect(ARTIFACT_CHALICE3, 14, 14);
        assignItemRect(ARTIFACT_SANDALS, 14, 14);
        assignItemRect(ARTIFACT_SHOES, 14, 14);
        assignItemRect(ARTIFACT_BOOTS, 14, 14);
        assignItemRect(ARTIFACT_GREAVES, 14, 14);
        assignItemRect(ARTIFACT_ROSE1, 14, 14);
        assignItemRect(ARTIFACT_ROSE2, 14, 14);
        assignItemRect(ARTIFACT_ROSE3, 14, 14);
        assignItemRect(ARTIFACT_TOME, 14, 14);
    }

    private static final int TRINKETS = xy(9, 17);  //24 slots
    public static final int RAT_SKULL = TRINKETS + 0;
    public static final int PARCHMENT_SCRAP = TRINKETS + 1;
    public static final int PETRIFIED_SEED = TRINKETS + 2;
    public static final int EXOTIC_CRYSTALS = TRINKETS + 3;
    public static final int MOSSY_CLUMP = TRINKETS + 4;
    public static final int SUNDIAL = TRINKETS + 5;
    public static final int CLOVER = TRINKETS + 6;
    public static final int TRAP_MECHANISM = TRINKETS + 7;
    public static final int MIMIC_TOOTH = TRINKETS + 8;
    public static final int WONDROUS_RESIN = TRINKETS + 9;
    public static final int EYE_OF_NEWT = TRINKETS + 10;
    public static final int SALT_CUBE = TRINKETS + 11;
    public static final int BLOOD_VIAL = TRINKETS + 12;
    public static final int OBLIVION_SHARD = TRINKETS + 13;
    public static final int CHAOTIC_CENSER = TRINKETS + 14;
    public static final int FERRET_TUFT = TRINKETS+15;
    public static final int TBOMB = TRINKETS + 16;
    public static final int TBOMB2 = TRINKETS + 17;
    public static final int UV = TRINKETS + 18;

    static {
        assignItemRect(RAT_SKULL, 13, 16);
        assignItemRect(PARCHMENT_SCRAP, 13, 15);
        assignItemRect(PETRIFIED_SEED, 15, 15);
        assignItemRect(EXOTIC_CRYSTALS, 14, 11);
        assignItemRect(MOSSY_CLUMP, 16, 16);
        assignItemRect(SUNDIAL, 14, 14);
        assignItemRect(CLOVER, 11, 15);
        assignItemRect(TRAP_MECHANISM, 16, 16);
        assignItemRect(MIMIC_TOOTH, 14, 14);
        assignItemRect(WONDROUS_RESIN, 16, 16);
        assignItemRect(EYE_OF_NEWT, 16, 16);
        assignItemRect(SALT_CUBE, 16, 15);
        assignItemRect(BLOOD_VIAL, 14, 14);
        assignItemRect(OBLIVION_SHARD, 15, 11);
        assignItemRect(CHAOTIC_CENSER, 15, 15);
        assignItemRect(FERRET_TUFT, 11, 16);
        assignItemRect(TBOMB, 12, 11);
        assignItemRect(TBOMB2, 15, 14);
        assignItemRect(UV, 15, 16);
    }

    private static final int SCROLLS = xy(1, 19);  //16 slots
    public static final int SCROLL_KAUNAN = SCROLLS + 0;
    public static final int SCROLL_SOWILO = SCROLLS + 1;
    public static final int SCROLL_LAGUZ = SCROLLS + 2;
    public static final int SCROLL_YNGVI = SCROLLS + 3;
    public static final int SCROLL_GYFU = SCROLLS + 4;
    public static final int SCROLL_RAIDO = SCROLLS + 5;
    public static final int SCROLL_ISAZ = SCROLLS + 6;
    public static final int SCROLL_MANNAZ = SCROLLS + 7;
    public static final int SCROLL_NAUDIZ = SCROLLS + 8;
    public static final int SCROLL_BERKANAN = SCROLLS + 9;
    public static final int SCROLL_ODAL = SCROLLS + 10;
    public static final int SCROLL_TIWAZ = SCROLLS + 11;

    public static final int SCROLL_CATALYST = SCROLLS + 12;
    public static final int ARCANE_RESIN = SCROLLS + 13;

    static {
        for (int i = SCROLLS; i < SCROLLS + 16; i++)
            assignItemRect(i, 14, 14);
        assignItemRect(SCROLL_CATALYST, 12, 11);
        assignItemRect(ARCANE_RESIN, 12, 11);
    }

    private static final int EXOTIC_SCROLLS = xy(1, 20);  //16 slots
    public static final int EXOTIC_KAUNAN = EXOTIC_SCROLLS + 0;
    public static final int EXOTIC_SOWILO = EXOTIC_SCROLLS + 1;
    public static final int EXOTIC_LAGUZ = EXOTIC_SCROLLS + 2;
    public static final int EXOTIC_YNGVI = EXOTIC_SCROLLS + 3;
    public static final int EXOTIC_GYFU = EXOTIC_SCROLLS + 4;
    public static final int EXOTIC_RAIDO = EXOTIC_SCROLLS + 5;
    public static final int EXOTIC_ISAZ = EXOTIC_SCROLLS + 6;
    public static final int EXOTIC_MANNAZ = EXOTIC_SCROLLS + 7;
    public static final int EXOTIC_NAUDIZ = EXOTIC_SCROLLS + 8;
    public static final int EXOTIC_BERKANAN = EXOTIC_SCROLLS + 9;
    public static final int EXOTIC_ODAL = EXOTIC_SCROLLS + 10;
    public static final int EXOTIC_TIWAZ = EXOTIC_SCROLLS + 11;

    static {
        for (int i = EXOTIC_SCROLLS; i < EXOTIC_SCROLLS + 16; i++)
            assignItemRect(i, 14, 14);
    }

    private static final int STONES = xy(1, 21);  //16 slots
    public static final int STONE_AGGRESSION = STONES + 0;
    public static final int STONE_AUGMENTATION = STONES + 1;
    public static final int STONE_FEAR = STONES + 2;
    public static final int STONE_BLAST = STONES + 3;
    public static final int STONE_BLINK = STONES + 4;
    public static final int STONE_CLAIRVOYANCE = STONES + 5;
    public static final int STONE_SLEEP = STONES + 6;
    public static final int STONE_DETECT = STONES + 7;
    public static final int STONE_ENCHANT = STONES + 8;
    public static final int STONE_FLOCK = STONES + 9;
    public static final int STONE_INTUITION = STONES + 10;
    public static final int STONE_SHOCK = STONES + 11;

    static {
        for (int i = STONES; i < STONES + 16; i++)
            assignItemRect(i, 14, 12);
    }

    private static final int POTIONS = xy(1, 22);  //16 slots
    public static final int POTION_CRIMSON = POTIONS + 0;
    public static final int POTION_AMBER = POTIONS + 1;
    public static final int POTION_GOLDEN = POTIONS + 2;
    public static final int POTION_JADE = POTIONS + 3;
    public static final int POTION_TURQUOISE = POTIONS + 4;
    public static final int POTION_AZURE = POTIONS + 5;
    public static final int POTION_INDIGO = POTIONS + 6;
    public static final int POTION_MAGENTA = POTIONS + 7;
    public static final int POTION_BISTRE = POTIONS + 8;
    public static final int POTION_CHARCOAL = POTIONS + 9;
    public static final int POTION_SILVER = POTIONS + 10;
    public static final int POTION_IVORY = POTIONS + 11;
    public static final int LIQUID_METAL = POTIONS + 13;

    static {
        for (int i = POTIONS; i < POTIONS + 16; i++)
            assignItemRect(i, 8, 14);
        assignItemRect(LIQUID_METAL, 8, 15);
    }

    private static final int EXOTIC_POTIONS = xy(1, 23);  //16 slots
    public static final int EXOTIC_CRIMSON = EXOTIC_POTIONS + 0;
    public static final int EXOTIC_AMBER = EXOTIC_POTIONS + 1;
    public static final int EXOTIC_GOLDEN = EXOTIC_POTIONS + 2;
    public static final int EXOTIC_JADE = EXOTIC_POTIONS + 3;
    public static final int EXOTIC_TURQUOISE = EXOTIC_POTIONS + 4;
    public static final int EXOTIC_AZURE = EXOTIC_POTIONS + 5;
    public static final int EXOTIC_INDIGO = EXOTIC_POTIONS + 6;
    public static final int EXOTIC_MAGENTA = EXOTIC_POTIONS + 7;
    public static final int EXOTIC_BISTRE = EXOTIC_POTIONS + 8;
    public static final int EXOTIC_CHARCOAL = EXOTIC_POTIONS + 9;
    public static final int EXOTIC_SILVER = EXOTIC_POTIONS + 10;
    public static final int EXOTIC_IVORY = EXOTIC_POTIONS + 11;

    static {
        for (int i = EXOTIC_POTIONS; i < EXOTIC_POTIONS + 16; i++)
            assignItemRect(i, 12, 13);
    }

    private static final int SEEDS = xy(1, 24);  //16 slots
    public static final int SEED_ROTBERRY = SEEDS + 0;
    public static final int SEED_FIREBLOOM = SEEDS + 1;
    public static final int SEED_SWIFTTHISTLE = SEEDS + 2;
    public static final int SEED_SUNGRASS = SEEDS + 3;
    public static final int SEED_ICECAP = SEEDS + 4;
    public static final int SEED_STORMVINE = SEEDS + 5;
    public static final int SEED_SORROWMOSS = SEEDS + 6;
    public static final int SEED_MAGEROYAL = SEEDS + 7;
    public static final int SEED_EARTHROOT = SEEDS + 8;
    public static final int SEED_STARFLOWER = SEEDS + 9;
    public static final int SEED_FADELEAF = SEEDS + 10;
    public static final int SEED_BLINDWEED = SEEDS + 11;

    static {
        for (int i = SEEDS; i < SEEDS + 16; i++)
            assignItemRect(i, 10, 10);
    }

    private static final int BREWS = xy(1, 25);  //8 slots
    public static final int BREW_INFERNAL = BREWS + 0;
    public static final int BREW_BLIZZARD = BREWS + 1;
    public static final int BREW_SHOCKING = BREWS + 2;
    public static final int BREW_CAUSTIC = BREWS + 3;
    public static final int BREW_AQUA = BREWS + 4;
    public static final int BREW_UNSTABLE = BREWS + 5;
    private static final int ELIXIRS = xy(9, 25);  //8 slots
    public static final int ELIXIR_HONEY = ELIXIRS + 0;
    public static final int ELIXIR_AQUA = ELIXIRS + 1;
    public static final int ELIXIR_MIGHT = ELIXIRS + 2;
    public static final int ELIXIR_DRAGON = ELIXIRS + 3;
    public static final int ELIXIR_TOXIC = ELIXIRS + 4;
    public static final int ELIXIR_ICY = ELIXIRS + 5;
    public static final int ELIXIR_ARCANE = ELIXIRS + 6;
    public static final int ELIXIR_FEATHER = ELIXIRS + 7;

    static {
        for (int i = BREWS; i < BREWS + 16; i++)
            assignItemRect(i, 12, 14);

        assignItemRect(BREW_AQUA, 9, 11);
    }

    //16 free slots

    private static final int SPELLS = xy(1, 27);  //16 slots
    public static final int WILD_ENERGY = SPELLS + 13;
    public static final int PHASE_SHIFT = SPELLS + 1;
    public static final int TELE_GRAB = SPELLS + 2;
    public static final int UNSTABLE_SPELL = SPELLS + 3;
    public static final int CURSE_INFUSE = SPELLS + 5;
    public static final int MAGIC_INFUSE = SPELLS + 6;
    public static final int ALCHEMIZE = SPELLS + 7;
    public static final int RECYCLE = SPELLS + 8;
    public static final int RECLAIM_TRAP = SPELLS + 10;
    public static final int RETURN_BEACON = SPELLS + 11;
    public static final int SUMMON_ELE = SPELLS + 12;


    static {
        assignItemRect(WILD_ENERGY, 12, 11);
        assignItemRect(PHASE_SHIFT, 12, 11);
        assignItemRect(TELE_GRAB, 12, 11);
        assignItemRect(UNSTABLE_SPELL, 13, 15);
        assignItemRect(CURSE_INFUSE, 10, 15);
        assignItemRect(MAGIC_INFUSE, 10, 15);
        assignItemRect(ALCHEMIZE, 10, 13);
        assignItemRect(RECYCLE, 10, 15);
        assignItemRect(WILD_ENERGY, 8, 16);
        assignItemRect(RECLAIM_TRAP, 11, 11);
        assignItemRect(RETURN_BEACON, 8, 16);
        assignItemRect(SUMMON_ELE, 8, 16);
    }

    private static final int FOOD = xy(1, 28);  //16 slots
    public static final int MEAT = FOOD + 0;
    public static final int STEAK = FOOD + 1;
    public static final int STEWED = FOOD + 2;
    public static final int OVERPRICED = FOOD + 3;
    public static final int CARPACCIO = FOOD + 4;
    public static final int RATION = FOOD + 5;
    public static final int PASTY = FOOD + 6;
    public static final int MEAT_PIE = FOOD + 7;
    public static final int BLANDFRUIT = FOOD + 8;
    public static final int BLAND_CHUNKS = FOOD + 9;
    public static final int BERRY = FOOD + 10;
    public static final int PHANTOM_MEAT = FOOD + 11;
    public static final int SUPPLY_RATION = FOOD + 12;

    static {
        assignItemRect(MEAT, 13, 13);
        assignItemRect(STEAK, 13, 13);
        assignItemRect(STEWED, 15, 14);
        assignItemRect(OVERPRICED, 13, 16);
        assignItemRect(CARPACCIO, 13, 13);
        assignItemRect(RATION, 16, 15);
        assignItemRect(PASTY, 16, 11);
        assignItemRect(MEAT_PIE, 16, 16);
        assignItemRect(BLANDFRUIT, 11, 13);
        assignItemRect(BLAND_CHUNKS, 14, 6);
        assignItemRect(BERRY, 14, 15);
        assignItemRect(PHANTOM_MEAT, 12, 16);
        assignItemRect(SUPPLY_RATION, 13, 14);
    }

    private static final int HOLIDAY_FOOD = xy(1, 29);  //16 slots
    public static final int STEAMED_FISH = HOLIDAY_FOOD + 0;
    public static final int FISH_LEFTOVER = HOLIDAY_FOOD + 1;
    public static final int CHOC_AMULET = HOLIDAY_FOOD + 2;
    public static final int EASTER_EGG = HOLIDAY_FOOD + 3;
    public static final int RAINBOW_POTION = HOLIDAY_FOOD + 4;
    public static final int SHATTERED_CAKE = HOLIDAY_FOOD + 5;
    public static final int PUMPKIN_PIE = HOLIDAY_FOOD + 6;
    public static final int VANILLA_CAKE = HOLIDAY_FOOD + 7;
    public static final int CANDY_CANE = HOLIDAY_FOOD + 8;
    public static final int SPARKLING_POTION = HOLIDAY_FOOD + 9;

    static {
        assignItemRect(STEAMED_FISH, 16, 12);
        assignItemRect(FISH_LEFTOVER, 16, 12);
        assignItemRect(CHOC_AMULET, 16, 16);
        assignItemRect(EASTER_EGG, 16, 12);
        assignItemRect(RAINBOW_POTION, 16, 16);
        assignItemRect(SHATTERED_CAKE, 16, 14);
        assignItemRect(PUMPKIN_PIE, 16, 11);
        assignItemRect(VANILLA_CAKE, 15, 11);
        assignItemRect(CANDY_CANE, 16, 15);
        assignItemRect(SPARKLING_POTION, 16, 15);
    }

    private static final int QUEST = xy(1, 30);  //16 slots

    public static final int SKULL = QUEST + 0;
    public static final int DUST = QUEST + 1;
    public static final int CANDLE = QUEST + 2;
    public static final int EMBER = QUEST + 3;
    public static final int PICKAXE = QUEST + 4;
    public static final int ORE = QUEST + 5;
    public static final int TOKEN = QUEST + 6;
    public static final int BLOB = QUEST + 7;
    public static final int SHARD = QUEST + 8;

    static {
        assignItemRect(SKULL, 14, 14);
        assignItemRect(DUST, 13, 8);
        assignItemRect(CANDLE, 10, 12);
        assignItemRect(EMBER, 10, 10);
        assignItemRect(PICKAXE, 14, 14);
        assignItemRect(ORE, 14, 14);
        assignItemRect(TOKEN, 10, 10);
        assignItemRect(BLOB, 8, 8);
        assignItemRect(SHARD, 8, 9);
    }

    private static final int BAGS = xy(1, 31);  //16 slots
    public static final int WATERSKIN = BAGS + 0;
    public static final int BACKPACK = BAGS + 1;
    public static final int POUCH = BAGS + 2;
    public static final int HOLDER = BAGS + 3;
    public static final int BANDOLIER = BAGS + 4;
    public static final int HOLSTER = BAGS + 5;
    public static final int VIAL = BAGS + 6;
    public static final int RO1 = BAGS + 7;
    public static final int RO2 = BAGS + 8;
    public static final int RO3 = BAGS + 9;
    public static final int RAM = BAGS + 10;
    public static final int TUSK = BAGS + 11;
    public static final int SPOH = BAGS + 12;
    public static final int BCOM = BAGS + 13;
    public static final int CM = BAGS + 14;

    static {
        assignItemRect(WATERSKIN, 14, 15);
        assignItemRect(BACKPACK, 16, 16);
        assignItemRect(POUCH, 15, 14);
        assignItemRect(HOLDER, 16, 13);
        assignItemRect(BANDOLIER, 14, 13);
        assignItemRect(HOLSTER, 10, 11);
        assignItemRect(VIAL, 9, 11);
        assignItemRect(RO1, 10, 10);
        assignItemRect(RO2, 10, 11);
        assignItemRect(RO3, 7, 14);
        assignItemRect(RAM, 12, 9);
        assignItemRect(TUSK, 14, 14);
        assignItemRect(SPOH, 14, 14);
        assignItemRect(BCOM, 14, 14);
        assignItemRect(CM, 14, 14);
    }

    private static final int DOCUMENTS = xy(1, 32);  //16 slots
    public static final int GUIDE_PAGE = DOCUMENTS + 0;
    public static final int ALCH_PAGE = DOCUMENTS + 1;
    public static final int SEWER_PAGE = DOCUMENTS + 2;
    public static final int PRISON_PAGE = DOCUMENTS + 3;
    public static final int CAVES_PAGE = DOCUMENTS + 4;
    public static final int CITY_PAGE = DOCUMENTS + 5;
    public static final int HALLS_PAGE = DOCUMENTS + 6;

    static {
        assignItemRect(GUIDE_PAGE, 8, 8);
        assignItemRect(ALCH_PAGE, 12, 15);
        assignItemRect(SEWER_PAGE, 8, 8);
        assignItemRect(PRISON_PAGE, 8, 8);
        assignItemRect(CAVES_PAGE, 8, 8);
        assignItemRect(CITY_PAGE, 8, 8);
        assignItemRect(HALLS_PAGE, 8, 8);
    }

    //for smaller 8x8 icons that often accompany an item sprite
    public static class Icons {

        private static final int WIDTH = 16;
        public static final int SIZE = 8;

        public static TextureFilm film = new TextureFilm(Assets.Sprites.ITEM_ICONS, SIZE, SIZE);

        private static int xy(int x, int y) {
            x -= 1;
            y -= 1;
            return x + WIDTH * y;
        }

        private static void assignIconRect(int item, int width, int height) {
            int x = (item % WIDTH) * SIZE;
            int y = (item / WIDTH) * SIZE;
            film.add(item, x, y, x + width, y + height);
        }

        private static final int RINGS = xy(1, 1);  //16 slots
        public static final int RING_ACCURACY = RINGS + 0;
        public static final int RING_ARCANA = RINGS + 1;
        public static final int RING_ELEMENTS = RINGS + 2;
        public static final int RING_ENERGY = RINGS + 3;
        public static final int RING_EVASION = RINGS + 4;
        public static final int RING_FORCE = RINGS + 5;
        public static final int RING_FUROR = RINGS + 6;
        public static final int RING_HASTE = RINGS + 7;
        public static final int RING_MIGHT = RINGS + 8;
        public static final int RING_SHARPSHOOT = RINGS + 9;
        public static final int RING_TENACITY = RINGS + 10;
        public static final int RING_WEALTH = RINGS + 11;

        static {
            assignIconRect(RING_ACCURACY, 7, 7);
            assignIconRect(RING_ARCANA, 7, 7);
            assignIconRect(RING_ELEMENTS, 7, 7);
            assignIconRect(RING_ENERGY, 7, 5);
            assignIconRect(RING_EVASION, 7, 7);
            assignIconRect(RING_FORCE, 5, 6);
            assignIconRect(RING_FUROR, 7, 6);
            assignIconRect(RING_HASTE, 6, 6);
            assignIconRect(RING_MIGHT, 7, 7);
            assignIconRect(RING_SHARPSHOOT, 7, 7);
            assignIconRect(RING_TENACITY, 6, 6);
            assignIconRect(RING_WEALTH, 7, 6);
        }

        //16 free slots

        private static final int SCROLLS = xy(1, 3);  //16 slots
        public static final int SCROLL_UPGRADE = SCROLLS + 0;
        public static final int SCROLL_IDENTIFY = SCROLLS + 1;
        public static final int SCROLL_REMCURSE = SCROLLS + 2;
        public static final int SCROLL_MIRRORIMG = SCROLLS + 3;
        public static final int SCROLL_RECHARGE = SCROLLS + 4;
        public static final int SCROLL_TELEPORT = SCROLLS + 5;
        public static final int SCROLL_LULLABY = SCROLLS + 6;
        public static final int SCROLL_MAGICMAP = SCROLLS + 7;
        public static final int SCROLL_RAGE = SCROLLS + 8;
        public static final int SCROLL_RETRIB = SCROLLS + 9;
        public static final int SCROLL_TERROR = SCROLLS + 10;
        public static final int SCROLL_TRANSMUTE = SCROLLS + 11;

        static {
            assignIconRect(SCROLL_UPGRADE, 7, 7);
            assignIconRect(SCROLL_IDENTIFY, 4, 7);
            assignIconRect(SCROLL_REMCURSE, 7, 7);
            assignIconRect(SCROLL_MIRRORIMG, 7, 5);
            assignIconRect(SCROLL_RECHARGE, 7, 5);
            assignIconRect(SCROLL_TELEPORT, 7, 7);
            assignIconRect(SCROLL_LULLABY, 7, 6);
            assignIconRect(SCROLL_MAGICMAP, 7, 7);
            assignIconRect(SCROLL_RAGE, 6, 6);
            assignIconRect(SCROLL_RETRIB, 5, 6);
            assignIconRect(SCROLL_TERROR, 5, 7);
            assignIconRect(SCROLL_TRANSMUTE, 7, 7);
        }

        private static final int EXOTIC_SCROLLS = xy(1, 4);  //16 slots
        public static final int SCROLL_ENCHANT = EXOTIC_SCROLLS + 0;
        public static final int SCROLL_DIVINATE = EXOTIC_SCROLLS + 1;
        public static final int SCROLL_ANTIMAGIC = EXOTIC_SCROLLS + 2;
        public static final int SCROLL_PRISIMG = EXOTIC_SCROLLS + 3;
        public static final int SCROLL_MYSTENRG = EXOTIC_SCROLLS + 4;
        public static final int SCROLL_PASSAGE = EXOTIC_SCROLLS + 5;
        public static final int SCROLL_SIREN = EXOTIC_SCROLLS + 6;
        public static final int SCROLL_FORESIGHT = EXOTIC_SCROLLS + 7;
        public static final int SCROLL_CHALLENGE = EXOTIC_SCROLLS + 8;
        public static final int SCROLL_PSIBLAST = EXOTIC_SCROLLS + 9;
        public static final int SCROLL_DREAD = EXOTIC_SCROLLS + 10;
        public static final int SCROLL_METAMORPH = EXOTIC_SCROLLS + 11;

        static {
            assignIconRect(SCROLL_ENCHANT, 7, 7);
            assignIconRect(SCROLL_DIVINATE, 7, 6);
            assignIconRect(SCROLL_ANTIMAGIC, 7, 7);
            assignIconRect(SCROLL_PRISIMG, 5, 7);
            assignIconRect(SCROLL_MYSTENRG, 7, 5);
            assignIconRect(SCROLL_PASSAGE, 5, 7);
            assignIconRect(SCROLL_SIREN, 7, 6);
            assignIconRect(SCROLL_FORESIGHT, 7, 5);
            assignIconRect(SCROLL_CHALLENGE, 7, 7);
            assignIconRect(SCROLL_PSIBLAST, 5, 6);
            assignIconRect(SCROLL_DREAD, 5, 7);
            assignIconRect(SCROLL_METAMORPH, 7, 7);
        }

        //16 free slots

        private static final int POTIONS = xy(1, 6);  //16 slots
        public static final int POTION_STRENGTH = POTIONS + 0;
        public static final int POTION_HEALING = POTIONS + 1;
        public static final int POTION_MINDVIS = POTIONS + 2;
        public static final int POTION_FROST = POTIONS + 3;
        public static final int POTION_LIQFLAME = POTIONS + 4;
        public static final int POTION_TOXICGAS = POTIONS + 5;
        public static final int POTION_HASTE = POTIONS + 6;
        public static final int POTION_INVIS = POTIONS + 7;
        public static final int POTION_LEVITATE = POTIONS + 8;
        public static final int POTION_PARAGAS = POTIONS + 9;
        public static final int POTION_PURITY = POTIONS + 10;
        public static final int POTION_EXP = POTIONS + 11;

        static {
            assignIconRect(POTION_STRENGTH, 7, 7);
            assignIconRect(POTION_HEALING, 6, 7);
            assignIconRect(POTION_MINDVIS, 7, 5);
            assignIconRect(POTION_FROST, 7, 7);
            assignIconRect(POTION_LIQFLAME, 5, 7);
            assignIconRect(POTION_TOXICGAS, 7, 7);
            assignIconRect(POTION_HASTE, 6, 6);
            assignIconRect(POTION_INVIS, 5, 7);
            assignIconRect(POTION_LEVITATE, 6, 7);
            assignIconRect(POTION_PARAGAS, 7, 7);
            assignIconRect(POTION_PURITY, 5, 7);
            assignIconRect(POTION_EXP, 7, 7);
        }

        private static final int EXOTIC_POTIONS = xy(1, 7);  //16 slots
        public static final int POTION_MASTERY = EXOTIC_POTIONS + 0;
        public static final int POTION_SHIELDING = EXOTIC_POTIONS + 1;
        public static final int POTION_MAGISIGHT = EXOTIC_POTIONS + 2;
        public static final int POTION_SNAPFREEZ = EXOTIC_POTIONS + 3;
        public static final int POTION_DRGBREATH = EXOTIC_POTIONS + 4;
        public static final int POTION_CORROGAS = EXOTIC_POTIONS + 5;
        public static final int POTION_STAMINA = EXOTIC_POTIONS + 6;
        public static final int POTION_SHROUDFOG = EXOTIC_POTIONS + 7;
        public static final int POTION_STRMCLOUD = EXOTIC_POTIONS + 8;
        public static final int POTION_EARTHARMR = EXOTIC_POTIONS + 9;
        public static final int POTION_CLEANSE = EXOTIC_POTIONS + 10;
        public static final int POTION_DIVINE = EXOTIC_POTIONS + 11;

        static {
            assignIconRect(POTION_MASTERY, 7, 7);
            assignIconRect(POTION_SHIELDING, 6, 6);
            assignIconRect(POTION_MAGISIGHT, 7, 5);
            assignIconRect(POTION_SNAPFREEZ, 7, 7);
            assignIconRect(POTION_DRGBREATH, 7, 7);
            assignIconRect(POTION_CORROGAS, 7, 7);
            assignIconRect(POTION_STAMINA, 6, 6);
            assignIconRect(POTION_SHROUDFOG, 7, 7);
            assignIconRect(POTION_STRMCLOUD, 7, 7);
            assignIconRect(POTION_EARTHARMR, 6, 6);
            assignIconRect(POTION_CLEANSE, 7, 7);
            assignIconRect(POTION_DIVINE, 7, 7);
        }

        //16 free slots

    }

}
