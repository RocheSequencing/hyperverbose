package com.roche.sequencing.hyperverbose;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter(value = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class AHyperverboseGenerator implements IHyperverboseGenerator {
	public static final String DELIMITER_DEFAULT = "-";
	public static final boolean TITLE_CASE_DEFAULT = false;
	public static final int WORD_COUNT_DEFAULT = 4;

	/**
	 * This can't be {@code null} but it can be an empty string.
	 */
	@NonNull
	@Builder.Default
	private final String delimiter = DELIMITER_DEFAULT;
	/**
	 * Only used for {@link #randomId()}.
	 * Generally, this will be a method reference to {@link Random#nextInt()}.
	 */
	@Builder.Default
	private final Supplier<Integer> random = null;
	/**
	 * Change words into {@code TitleCase}.
	 * Generally, you would combine this with an empty string delimiter.
	 * Also, if you always want this it would be more efficient to format your word list this way.
	 */
	@Builder.Default
	private final boolean titleCase = TITLE_CASE_DEFAULT;
	/**
	 * The maximum number of words to return.  Note that if the source value
	 * has few bits (say, {@code 4L}) you might get fewer words in your result!
	 */
	@Builder.Default
	private final int wordCount = WORD_COUNT_DEFAULT;

	public String fromBigInt(
		@NonNull final Function<Integer, HyperverboseWords> wordsSupplier,
		final BigInteger source
	) {
		if (source == null) {
			return null;
		}
		BigInteger bigInt = source;
		final LinkedList<String> w = new LinkedList<>();
		final int wordCount = getWordCount();
		for (int n = 0; n < wordCount; n++) {
			if (bigInt.equals(BigInteger.ZERO)) {
				break;
			}
			final HyperverboseWords words = wordsSupplier.apply(n);
			final BigInteger wordListSize = BigInteger.valueOf(words.count());
			final BigInteger[] parts = bigInt.divideAndRemainder(wordListSize);
			bigInt = parts[0];  // quotient
			final BigInteger bigWordNum = parts[1];  // remainder
			final String word = words.modWord(bigWordNum.intValueExact());
			w.addFirst(word);
		}
		return join(w.stream());
	}

	@Override
	public String fromLong(final long source) {
		return fromBigInt(BigInteger.valueOf(source));
	}

	@Override
	public String fromUuid(final UUID uuid) {
		return fromBigInt(HyperverboseUtil.bigIntFromUuid(uuid));
	}

	/**
	 * Helper for abstracting away list to String finalization.
	 */
	protected String join(@NonNull final Stream<String> wordStream) {
		Stream<String> w = wordStream;
		if (isTitleCase()) {
			w = w.map(HyperverboseUtil::titleCase);
		}
		return w.collect(Collectors.joining(getDelimiter()));
	}

	public String randomId(@NonNull final Function<Integer, HyperverboseWords> wordsSupplier) {
		final Supplier<Integer> random = getRandom();
		if (random == null) {
			throw new NullPointerException("No random number generator present");
		}
		return join(IntStream.range(0, getWordCount())
			.mapToObj(n -> wordsSupplier.apply(n).modWord(random.get())));
	}
}
