package com.roche.sequencing.hyperverbose;

import java.math.BigInteger;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * A generator which uses a sequence of word lists defined by you.
 * Useful if you want to set up a pattern such as {@code adverb-adjective-noun}.
 * Note that because the {@code from*} methods work from low order bits to high,
 * you will want the word lists in reverse order.  That is, to set up the
 * pattern above you will want the list to be {@code [NOUNS, ADJECTIVES, ADVERBS]}.
 * The last word list will be used for any additional words beyond the list count.
 * That is, if {@link #getWordCount()} was {@code 4}, the {@code ADVERBS} list
 * would get used twice in succession.
 */
@SuperBuilder(toBuilder = true)
@Getter
public class HyperverbosePhraseGenerator extends AHyperverboseGenerator {
	@NonNull
	@Builder.Default
	private final HyperverbosePhraseGenerator.ExtraStrategy extraStrategy = ExtraStrategy.Wrap;
	@Singular
	@NonNull
	private final List<com.roche.sequencing.hyperverbose.HyperverboseWords> wordLists;

	protected HyperverboseWords findList(@NonNull final Integer n) {
		switch (extraStrategy) {
			case Wrap:
				return wordLists.get(n % wordLists.size());
			case Extend:
				return wordLists.get(Math.min(n, wordLists.size() - 1));
			case Throw:
				return wordLists.get(n);
			default:
				throw new IllegalStateException("Unhandled ExtraStrategy type: " + extraStrategy);
		}
	}

	@Override
	public String fromBigInt(final BigInteger source) {
		return fromBigInt(this::findList, source);
	}

	@Override
	public String randomId() {
		return randomId(this::findList);
	}

	/**
	 * How to handle a request for a wordlist beyond the end of {@link #wordLists}.
	 */
	public enum ExtraStrategy {
		/**
		 * Use the last item in the list repeatedly.
		 */
		Extend,
		/**
		 * Rotate through the lists.
		 */
		Wrap,
		/**
		 * Throw an error.
		 */
		Throw,
		;
	}
}
