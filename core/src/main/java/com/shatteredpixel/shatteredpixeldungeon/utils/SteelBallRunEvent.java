package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.TimeZone;

//스틸 볼 런 2nd & 3rd STAGE 공개 기념 이벤트: 기간 중 영웅별로 다른 영웅의 보조 직업 하나를 추가로 선택 가능
public class SteelBallRunEvent {

	private static final EnumMap<HeroClass, HeroSubClass> BONUS_SUBCLASSES = new EnumMap<>(HeroClass.class);
	static {
		BONUS_SUBCLASSES.put(HeroClass.WARRIOR,  HeroSubClass.MONK);
		BONUS_SUBCLASSES.put(HeroClass.MAGE,     HeroSubClass.RIDER);
		BONUS_SUBCLASSES.put(HeroClass.ROGUE,    HeroSubClass.PRIEST);
		BONUS_SUBCLASSES.put(HeroClass.DUELIST,  HeroSubClass.GLADIATOR);
		BONUS_SUBCLASSES.put(HeroClass.HUNTRESS, HeroSubClass.FREERUNNER);
		BONUS_SUBCLASSES.put(HeroClass.CLERIC,   HeroSubClass.WARDEN);
		BONUS_SUBCLASSES.put(HeroClass.JOHNNY,   HeroSubClass.WARLOCK);
	}

	//2026-09-24 00:00:00 ~ 2026-12-04 23:59:59 (KST 기준)
	public static boolean isActive() {
		TimeZone kst = TimeZone.getTimeZone("Asia/Seoul");
		long now = System.currentTimeMillis();
		GregorianCalendar start = new GregorianCalendar(kst);
		start.set(2026, Calendar.SEPTEMBER, 24, 0, 0, 0);
		start.set(Calendar.MILLISECOND, 0);
		GregorianCalendar end = new GregorianCalendar(kst);
		end.set(2026, Calendar.DECEMBER, 4, 23, 59, 59);
		end.set(Calendar.MILLISECOND, 999);
		return now >= start.getTimeInMillis() && now <= end.getTimeInMillis();
	}

	public static HeroSubClass bonusSubClass(HeroClass cls) {
		return BONUS_SUBCLASSES.get(cls);
	}

	//클레릭이 아닌 영웅이 이벤트로 선택한 의지의 스탠드사
	public static boolean isEventPriest(Char ch) {
		return ch instanceof Hero
				&& ((Hero) ch).subClass == HeroSubClass.PRIEST
				&& ((Hero) ch).heroClass != HeroClass.CLERIC;
	}

	//스톤 프리 DISC의 무기/방어구 부여가 기존 마법부여를 덮어쓰지 않는 영웅
	public static boolean holyKeepsEnchant(Char ch) {
		return (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.PALADIN)
				|| isEventPriest(ch);
	}

	//이벤트 종료 후에도 이미 선택한 보조 직업은 유지되므로 기간과 무관하게 판정
	public static boolean isEventTalent(HeroClass cls, Talent talent) {
		HeroSubClass bonus = bonusSubClass(cls);
		if (bonus == null) return false;
		ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();
		Talent.initSubclassTalents(bonus, talents);
		return talents.get(2).containsKey(talent);
	}
}
