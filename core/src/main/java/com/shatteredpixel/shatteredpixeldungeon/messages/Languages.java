package com.shatteredpixel.shatteredpixeldungeon.messages;

import java.util.Locale;

public enum Languages {
	KOREAN("한국어", "ko", Status.O_COMPLETE,
			new String[]{"Cocoa", "Flameblast12", "GameConqueror", "Korean2017"},
			new String[]{"AFS", "N8fall", "WondarRabb1t", "chlrhwnstkd", "ddojin0115", "eeeei", "enjuxx", "hancyel", "linterpreteur", "lemonam", "lsiebnie", "sora0430"});

	public enum Status {
		O_COMPLETE, //complete, 100% reviewed
	}

	private String name;
	private String code;
	private Status status;
	private String[] reviewers;
	private String[] translators;

	Languages(String name, String code, Status status, String[] reviewers, String[] translators) {
		this.name = name;
		this.code = code;
		this.status = status;
		this.reviewers = reviewers;
		this.translators = translators;
	}

	public String nativeName() {
		return name;
	}

	public String code() {
		return code;
	}

	public Status status() {
		return status;
	}

	public String[] reviewers() {
		if (reviewers == null) return new String[]{};
		else return reviewers.clone();
	}

	public String[] translators() {
		if (translators == null) return new String[]{};
		else return translators.clone();
	}

	public static Languages matchLocale(Locale locale) {
		return KOREAN; // 항상 한국어 반환
	}

	public static Languages matchCode(String code) {
		return KOREAN; // 항상 한국어 반환
	}
}