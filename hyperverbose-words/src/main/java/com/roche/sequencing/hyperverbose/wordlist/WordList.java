package com.roche.sequencing.hyperverbose.wordlist;

import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(value = AccessLevel.PRIVATE)
public enum WordList {
	ALT12DICTS("A large word list compiled by combining 12 other lists", Alt12DictsDownloader::new, Alt12DictsNormalizer::new),
	JARGON("Terms in the Jargon File", JargonDownloader::new, PartsOfSpeechNormalizer::new), // yep, it uses the POS format
	OFFENSIVE("Offensive words", OffensiveDownloader::new, PlainNormalizer::new),
	POS("Parts of speech", PartsOfSpeechDownloader::new, PartsOfSpeechNormalizer::new),
	SWEAR("Swear words", SwearDownloader::new, PlainNormalizer::new),
	;
	@Getter(value = AccessLevel.PUBLIC)
	private final String description;
	private final Supplier<ADownloader> downloaderSupplier;
	private final Supplier<ANormalizer> normalizerSupplier;

	public ADownloader buildDownloader() {
		return getDownloaderSupplier().get();
	}

	public ANormalizer buildNormalizer() {
		return getNormalizerSupplier().get();
	}
}
