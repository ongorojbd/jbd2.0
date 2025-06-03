package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SeedFinder {
    enum Condition {ANY, ALL}
    enum FINDING {STOP,CONTINUE}

    public static FINDING findingStatus = FINDING.STOP;

    public static class Options {
        public static int floors;
        public static Condition condition;
        public static long seed;
    }

    static class HeapItem {
        public Item item;
        public Heap heap;

        public HeapItem(Item item, Heap heap) {
            this.item = item;
            this.heap = heap;
        }
    }

    List<Class<? extends Item>> blacklist;
    ArrayList<String> itemList;

    private void addTextItems(String caption, ArrayList<HeapItem> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");

            for (HeapItem item : items) {
                Item i = item.item;
                Heap h = item.heap;

                if (((i instanceof Armor && ((Armor) i).hasGoodGlyph()) ||
                        (i instanceof Weapon && ((Weapon) i).hasGoodEnchant()) ||
                        (i instanceof Ring) || (i instanceof Wand)) && i.cursed)
                    builder.append("- 저주받은 ").append(i.title());

                else
                    builder.append("- ").append(i.title());

                if (h.type != Type.HEAP)
                    builder.append(" (").append(h.title()).append(")");

                builder.append("\n");
            }

            builder.append("\n");
        }
    }

    private void addTextQuest(String caption, ArrayList<Item> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");

            for (Item i : items) {
                if (i.cursed)
                    builder.append("- 저주받은 ").append(i.title().toLowerCase()).append("\n");

                else
                    builder.append("- ").append(i.title().toLowerCase()).append("\n");
            }

            builder.append("\n");
        }
    }



    public void findSeed(boolean stop){
        if(!stop){
            findingStatus = FINDING.STOP;
        }
    }

    public void stopFindSeed(){
        findingStatus = FINDING.STOP;
    }

    public String findSeed(String[] wanted, int floor) {
        // 찾고자 하는 아이템 목록을 ArrayList로 변환
        itemList = new ArrayList<>(Arrays.asList(wanted));

        // 시드 검색 시작 시점
        String seedDigits = Integer.toString(Random.Int(500000));

        findingStatus = FINDING.CONTINUE;
        Options.condition = Condition.ALL;
        String result = "NONE";

        // 시작 시간(밀리초)
        long startTime = System.currentTimeMillis();
        // 12초 이후에 자동 타임아웃
        long timeLimit = 12000;

        // 실제 시드 탐색
        for (int i = Random.Int(9_999_999);
             i < DungeonSeed.TOTAL_SEEDS && findingStatus == FINDING.CONTINUE;
             i++)
        {
            // 시간 제한 체크
            if (System.currentTimeMillis() - startTime > timeLimit) {
                // 12초를 초과하면 반복을 멈추고 메시지 반환
                result = "잘못된 조건 또는 너무 복잡한 조건이기 때문에 검색할 수 없습니다.";
                break;
            }

            // testSeedALL 로 조건 검사
            if (testSeedALL(seedDigits + i, floor)) {
                // 조건에 부합하는 시드를 찾았다면 해당 시드 아이템 목록 반환
                result = logSeedItems(seedDigits + i, floor);
                break;
            }
        }

        // 반복을 모두 마쳤는데도 result가 NONE이면, 시간 내 검색 실패 메시지 반환
        if ("NONE".equals(result)) {
            result = "잘못된 조건 또는 너무 복잡한 조건이기 때문에 검색할 수 없습니다.";
        }

        findingStatus = FINDING.STOP;
        return result;
    }


    private ArrayList<Heap> getMobDrops(Level l) {
        ArrayList<Heap> heaps = new ArrayList<>();

        for (Mob m : l.mobs) {
            if (m instanceof Statue) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                h.items.add(((Statue) m).weapon.identify());
                h.type = Type.STATUE;
                heaps.add(h);
            }

            else if (m instanceof ArmoredStatue) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                h.items.add(((ArmoredStatue) m).armor.identify());
                h.items.add(((ArmoredStatue) m).weapon.identify());
                h.type = Type.STATUE;
                heaps.add(h);
            }

            else if (m instanceof Mimic) {
                Heap h = new Heap();
                h.items = new LinkedList<>();

                for (Item item : ((Mimic) m).items)
                    h.items.add(item.identify());

                if (m instanceof GoldenMimic) h.type = Type.GOLDEN_MIMIC;
                else if (m instanceof CrystalMimic) h.type = Type.CRYSTAL_MIMIC;
                else h.type = Type.MIMIC;
                heaps.add(h);
            }
        }

        return heaps;
    }

    private ArrayList<HeapItem> getTrinkets() {
        TrinketCatalyst cata = new TrinketCatalyst();
        int NUM_TRINKETS = TrinketCatalyst.WndTrinket.NUM_TRINKETS;

        // roll new trinkets if trinkets were not already rolled
        while (cata.rolledTrinkets.size() < NUM_TRINKETS) {
            cata.rolledTrinkets.add((Trinket) Generator.random(Generator.Category.TRINKET));
        }

        ArrayList<HeapItem> trinkets = new ArrayList<>();

        for (int i = 0; i < NUM_TRINKETS; i++) {
            Heap h = new Heap();
            h.type = Heap.Type.TrinketCatalyst;
            trinkets.add(new HeapItem(cata.rolledTrinkets.get(i), h));
        }

        return trinkets;
    }

    private boolean testSeed(String seed, int floors) {
        SPDSettings.customSeed(seed);
        GamesInProgress.selectedClass = HeroClass.WARRIOR;
        Dungeon.init();

        boolean[] itemsFound = new boolean[itemList.size()];

        for (int i = 0; i < floors; i++) {
            Level l = Dungeon.newLevel();

            ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
            heaps.addAll(getMobDrops(l));

            if(Ghost.Quest.armor != null){
                for (int j = 0; j < itemList.size(); j++) {
                    if (Ghost.Quest.armor.identify().title().toUpperCase().replaceAll(" ","").contains(itemList.get(j).toUpperCase().replaceAll(" ",""))) {
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                }
            }
            if(Wandmaker.Quest.wand1 != null){
                for (int j = 0; j < itemList.size(); j++) {
                    if (Wandmaker.Quest.wand1.identify().title().toUpperCase().replaceAll(" ","").contains(itemList.get(j).toUpperCase().replaceAll(" ","")) || Wandmaker.Quest.wand2.identify().title().toUpperCase().replaceAll(" ","").contains(itemList.get(j).toUpperCase().replaceAll(" ",""))) {
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                    if(Wandmaker.Quest.type == 1 && "서바이버".toUpperCase().contains(itemList.get(j).toUpperCase().replaceAll(" ",""))){
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }else if(Wandmaker.Quest.type == 2 && "킬러 퀸의 DISC".toUpperCase().contains(itemList.get(j).toUpperCase().replaceAll(" ",""))){
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }else if(Wandmaker.Quest.type == 3 && "스트레이 캣의 씨앗".toUpperCase().contains(itemList.get(j).toUpperCase().replaceAll(" ",""))){
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                }
            }
            if(Imp.Quest.reward != null){
                for (int j = 0; j < itemList.size(); j++) {
                    if (Imp.Quest.reward.identify().title().toUpperCase().replaceAll(" ","").contains(itemList.get(j).toUpperCase().replaceAll(" ",""))) {
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                }
            }

            for (Heap h : heaps) {
                for (Item item : h.items) {
                    item.identify();

                    for (int j = 0; j < itemList.size(); j++) {
                        if (item.title().toUpperCase().replaceAll(" ","").contains(itemList.get(j).toUpperCase().replaceAll(" ",""))) {
                            if (itemsFound[j] == false) {
                                itemsFound[j] = true;
                                break;
                            }
                        }
                    }
                }
            }

            Dungeon.depth++;
        }

        if (Options.condition == Condition.ANY) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemsFound[i] == true)
                    return true;
            }

            return false;
        }

        else {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemsFound[i] == false)
                    return false;
            }

            return true;
        }
    }

    private boolean testSeedALL(String seed, int floors) {
        SPDSettings.customSeed(seed);
        Dungeon.initSeed();
        GamesInProgress.selectedClass = HeroClass.WARRIOR;
        Dungeon.init();

        boolean[] itemsFound = new boolean[itemList.size()];
        Arrays.fill(itemsFound, false);

        ArrayList<HeapItem> trinkets = getTrinkets();
        for (int k = 0; k < trinkets.size(); k++) {
            for (int j = 0; j < itemList.size(); j++) {
                String wantingItem = itemList.get(j);
                boolean precise = wantingItem.startsWith("\"")&&wantingItem.endsWith("\"");
                if(precise){
                    wantingItem = wantingItem.replaceAll("\"","").toUpperCase();
                }else{
                    wantingItem = wantingItem.replaceAll(" ", "").toUpperCase();
                }

                String trinketTitle = trinkets.get(k).item.title().toUpperCase();
                if (!precise && trinketTitle.replaceAll(" ","").contains(wantingItem) ||
                        precise && trinketTitle.equals(wantingItem)) {
                    if (!itemsFound[j]) {
                        itemsFound[j] = true;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < floors; i++) {
            Level l = Dungeon.newLevel();

            ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
            heaps.addAll(getMobDrops(l));

            if(Ghost.Quest.armor != null){
                for (int j = 0; j < itemList.size(); j++) {
                    String wantingItem = itemList.get(j);
                    boolean precise = wantingItem.startsWith("\"")&&wantingItem.endsWith("\"");
                    if(precise){
                        wantingItem = wantingItem.replaceAll("\"","").toUpperCase();
                    }else{
                        wantingItem = wantingItem.replaceAll(" ", "").toUpperCase();
                    }

                    String armorTitle = Ghost.Quest.armor.identify().title().toUpperCase();
                    if (!precise && armorTitle.replaceAll(" ","").contains(wantingItem) ||
                            precise && armorTitle.equals(wantingItem)) {
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                }
            }
            if(Wandmaker.Quest.wand1 != null){
                for (int j = 0; j < itemList.size(); j++) {
                    String wantingItem = itemList.get(j);
                    String wand1 = Wandmaker.Quest.wand1.identify().title().toUpperCase();
                    String wand2 = Wandmaker.Quest.wand2.identify().title().toUpperCase();
                    boolean precise = wantingItem.startsWith("\"")&&wantingItem.endsWith("\"");
                    if(precise){
                        wantingItem = wantingItem.replaceAll("\"","").toUpperCase();
                        if (wand1.equals(wantingItem) || wand2.equals(wantingItem)) {
                            if (itemsFound[j] == false) {
                                itemsFound[j] = true;
                                break;
                            }
                        }
                    }else{
                        wantingItem = wantingItem.replaceAll(" ", "").toUpperCase();
                        wand1 = wand1.replaceAll(" ","");
                        wand2 = wand2.replaceAll(" ","");
                        if (wand1.contains(wantingItem) || wand2.contains(wantingItem)) {
                            if (itemsFound[j] == false) {
                                itemsFound[j] = true;
                                break;
                            }
                        }
                    }

                    String lowerWantingItem = wantingItem.toUpperCase().replaceAll(" ","");
                    if(Wandmaker.Quest.type == 1 && "서바이버".toUpperCase().contains(lowerWantingItem)){
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }else if(Wandmaker.Quest.type == 2 && "킬러 퀸의 DISC".toUpperCase().contains(lowerWantingItem)){
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }else if(Wandmaker.Quest.type == 3 && "스트레이 캣의 씨앗".toUpperCase().contains(lowerWantingItem)){
                        if (itemsFound[j] == false) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                }
            }
            if(Imp.Quest.reward != null){
                for (int j = 0; j < itemList.size(); j++) {
                    String wantingItem = itemList.get(j);
                    boolean precise = wantingItem.startsWith("\"")&&wantingItem.endsWith("\"");
                    String ring = Imp.Quest.reward.identify().title().toUpperCase();
                    if(precise){
                        wantingItem = wantingItem.replaceAll("\"","").toUpperCase();
                        if (ring.equals(wantingItem)) {
                            if (itemsFound[j] == false) {
                                itemsFound[j] = true;
                                break;
                            }
                        }
                    }else{
                        wantingItem = wantingItem.replaceAll(" ", "").toUpperCase();
                        if (ring.replaceAll(" ","").contains(wantingItem)) {
                            if (itemsFound[j] == false) {
                                itemsFound[j] = true;
                                break;
                            }
                        }
                    }
                }
            }

            for (Heap h : heaps) {
                for (Item item : h.items) {
                    item.identify();
                    String itemName = item.title().toUpperCase();

                    for (int j = 0; j < itemList.size(); j++) {
                        String wantingItem = itemList.get(j);
                        boolean precise = wantingItem.startsWith("\"")&&wantingItem.endsWith("\"");
                        if(precise){
                            wantingItem = wantingItem.replaceAll("\"", "").toUpperCase();
                            if (itemName.equals(wantingItem)) {
                                if (itemsFound[j] == false) {
                                    itemsFound[j] = true;
                                    break;
                                }
                            }
                        }else{
                            wantingItem = wantingItem.replaceAll(" ", "").toUpperCase();
                            if (itemName.replaceAll(" ","").contains(wantingItem)) {
                                if (itemsFound[j] == false) {
                                    itemsFound[j] = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if(areAllTrue(itemsFound)){
                return true;
            }
            Dungeon.depth++;
        }
        return false;
    }

    private static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }

    public String logSeedItems(String seed, int floors) {

        SPDSettings.customSeed(seed);
        Dungeon.initSeed();
        GamesInProgress.selectedClass = HeroClass.WARRIOR;
        Dungeon.init();
        StringBuilder result = new StringBuilder("시드 " + DungeonSeed.convertToCode(Dungeon.seed) + " (" + Dungeon.seed + ") 의 아이템들:\n\n");

        blacklist = Arrays.asList(Gold.class, Dewdrop.class, IronKey.class, GoldenKey.class, CrystalKey.class, EnergyCrystal.class,
                CorpseDust.class, Embers.class, CeremonialCandle.class, Pickaxe.class);
        ArrayList<HeapItem> trinkets = getTrinkets();
        addTextItems("[ 위험한 물건 ]", trinkets, result);

        for (int i = 0; i < floors; i++) {
            result.append("\n_----- ").append(Long.toString(Dungeon.depth)).append("층 -----_\n\n");

            Level l = Dungeon.newLevel();
            ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
            StringBuilder builder = new StringBuilder();
            ArrayList<HeapItem> scrolls = new ArrayList<>();
            ArrayList<HeapItem> potions = new ArrayList<>();
            ArrayList<HeapItem> equipment = new ArrayList<>();
            ArrayList<HeapItem> rings = new ArrayList<>();
            ArrayList<HeapItem> artifacts = new ArrayList<>();
            ArrayList<HeapItem> wands = new ArrayList<>();
            ArrayList<HeapItem> others = new ArrayList<>();
            ArrayList<HeapItem> forSales = new ArrayList<>();

            // list quest rewards
            if (Ghost.Quest.armor != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Ghost.Quest.armor.identify());
                rewards.add(Ghost.Quest.weapon.identify());
                Ghost.Quest.complete();

                addTextQuest("[ 압둘 퀘스트 보상 ]", rewards, builder);
            }

            if (Wandmaker.Quest.wand1 != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Wandmaker.Quest.wand1.identify());
                rewards.add(Wandmaker.Quest.wand2.identify());
                Wandmaker.Quest.complete();

                builder.append("[ 화이트 스네이크의 요구 아이템 ]:\n ");


                switch (Wandmaker.Quest.type) {
                    case 1: default:
                        builder.append("서바이버\n\n");
                        break;
                    case 2:
                        builder.append("킬러 퀸의 DISC\n\n");
                        break;
                    case 3:
                        builder.append("스트레이 캣의 씨앗\n\n");
                }

                addTextQuest("[ 화이트 스네이크의 퀘스트 보상 ]", rewards, builder);
            }

            if (Imp.Quest.reward != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Imp.Quest.reward.identify());
                Imp.Quest.complete();

                addTextQuest("[ 오시리스신 퀘스트 보상 ]", rewards, builder);
            }

            heaps.addAll(getMobDrops(l));

            // list items
            for (Heap h : heaps) {
                for (Item item : h.items) {
                    item.identify();

                    if (h.type == Type.FOR_SALE) forSales.add(new HeapItem(item, h));
                    else if (blacklist.contains(item.getClass())) continue;
                    else if (item instanceof Scroll) scrolls.add(new HeapItem(item, h));
                    else if (item instanceof Potion) potions.add(new HeapItem(item, h));
                    else if (item instanceof MeleeWeapon || item instanceof Armor) equipment.add(new HeapItem(item, h));
                    else if (item instanceof Ring) rings.add(new HeapItem(item, h));
                    else if (item instanceof Artifact) artifacts.add(new HeapItem(item, h));
                    else if (item instanceof Wand) wands.add(new HeapItem(item, h));
                    else others.add(new HeapItem(item, h));
                }
            }

            addTextItems("[ 기억 DISC ]", scrolls, builder);
            addTextItems("[ 물약 ]", potions, builder);
            addTextItems("[ 장비 ]", equipment, builder);
            addTextItems("[ 석가면 ]", rings, builder);
            addTextItems("[ 장비 DISC ]", artifacts, builder);
            addTextItems("[ 사격 DISC ]", wands, builder);
            addTextItems("[ 상점 ]", forSales, builder);
            addTextItems("[ 그 외 ]", others, builder);

            result.append("\n").append(builder);

            Dungeon.depth++;
        }
        return result.toString();
    }

}