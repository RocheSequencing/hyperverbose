package com.roche.sequencing.hyperverbose.wordlist;

import lombok.NonNull;

public class PlainNormalizer extends ANormalizer {
	@Override
	protected String wordFromSourceLine(@NonNull final String sourceLine) {
		if (sourceLine == null) {
			return null;
		}
		final String word = sourceLine.trim().replace(" ", "");
		if (shouldReject(word)) {
			return null;
		}
		return word.toLowerCase();
	}
}
