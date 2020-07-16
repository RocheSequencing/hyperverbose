package com.roche.sequencing.hyperverbose.wordlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PartOfSpeech {
	Noun("N"),
	Plural("p"),
	NounPhrase("h"),
	VerbParticiple("V"),
	VerbTransitive("t"),
	VerbIntransitive("i"),
	Adjective("A"),
	Adverb("v"),
	Conjunction("C"),
	Preposition("P"),
	Interjection("!"),
	Pronoun("r"),
	ArticleDefinite("D"),
	ArticleIndefinite("I"),
	Nominative("o"),
	;
	private final String code;
}
