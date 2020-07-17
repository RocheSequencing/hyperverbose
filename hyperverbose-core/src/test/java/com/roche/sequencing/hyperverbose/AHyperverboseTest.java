package com.roche.sequencing.hyperverbose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AHyperverboseTest {
	public static final String ALPHA = "alpha";
	public static final String BRAVO = "bravo";
	public static final String CHARLIE = "charlie";
	public static final UUID CS_UUID = UUID.fromString("00000000-0000-0000-0000-CCCCCCCC");
	public static final String CUSTOM_DELIM = "!";
	public static final String DELTA = "delta";
	public static final String ECHO = "echo";
	public static final HyperverboseWords EMPTY_WORDS = new HyperverboseWords(new ArrayList<>());
	public static final String FOXTROT = "foxtrot";
	public static final String GULF = "gulf";
	public static final String HOTEL = "hotel";
	public static final int RANDOM_SEED = 1357;
	public static final List<String> WORD_LIST = Arrays.asList(ALPHA, BRAVO, CHARLIE, DELTA, ECHO, FOXTROT, GULF, HOTEL);
	public static final int WORD_COUNT = WORD_LIST.size();
	public static final HyperverboseWords WORDS = new HyperverboseWords(new ArrayList<>(WORD_LIST));
	public static final UUID ZERO_UUID = UUID.fromString("00000000-0000-0000-0000-00000000");

	protected static long longFromWords(final String... words) {
		return Stream.of(words).map(word -> {
			final int num = WORD_LIST.indexOf(word);
			if (num < 0) {
				throw new IllegalArgumentException("Unknown word: " + word);
			}
			return num;
		}).reduce(0, (prev, cur) -> (prev * WORD_COUNT) + cur);
	}

	protected IHyperverboseGenerator buildCustomGenerator(final Supplier<Integer> random) {
		return HyperverboseIdGenerator.builder()
			.delimiter(CUSTOM_DELIM)
			.titleCase(true)
			.wordCount(5)
			.words(WORDS)
			.random(random == null ? new Random(RANDOM_SEED)::nextInt : random)
			.build();
	}

	protected IHyperverboseGenerator buildDefaultGenerator(final Supplier<Integer> random) {
		return HyperverboseIdGenerator.builder()
			.words(WORDS)
			.random(random)
			.build();
	}

	protected IHyperverboseGenerator buildDefaultGenerator() {
		return HyperverboseIdGenerator.builder()
			.words(WORDS)
			.build();
	}

	protected IHyperverboseGenerator buildHexGenerator() {
		return HyperverboseIdGenerator.builder()
			.delimiter("")
			.wordCount(Integer.MAX_VALUE)
			.words(new HyperverboseWords(new ArrayList<>(IntStream.range(0, 16)
				.mapToObj(n -> "0123456789abcdef".substring(n, n + 1))
				.collect(Collectors.toList()))))
			.build();
	}
}
