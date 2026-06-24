/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.deckbuilder;

public enum DeckCardKeyword {

	EXHAUST(1, "_소멸_", "사용하면 이번 전투에서 제거됩니다."),
	RETAIN(2, "_보존_", "턴 종료 시 버려지지 않고 손에 남습니다."),
	CAST_ON_DRAW(4, "_뽑을 때 시전_", "이 카드를 뽑으면 자동으로 시전됩니다."),
	AIM(8, "_조준_", "손패의 정중앙에서 사용하면 강화 효과가 발동합니다."),
	THROW(16, "_투척_", "손패의 가장 왼쪽 또는 가장 오른쪽에서 사용하면 강화 효과가 발동합니다."),
	VANGUARD(32, "_선봉_", "전투 시작 시 첫 패에 들어옵니다."),
	TRANSIENT(64, "_일시적_", "내 턴이 끝날 때 손에 들고 있다면 소멸합니다."),
	ZERO_COST(128, "", ""),
	PERMANENT(256, "_영구_", "이 카드는 덱에서 제거하거나 변환할 수 없습니다.");

	public final int bit;
	public final String label;
	public final String description;

	DeckCardKeyword(int bit, String label, String description) {
		this.bit = bit;
		this.label = label;
		this.description = description;
	}
}
