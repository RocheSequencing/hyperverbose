package com.roche.sequencing.hyperverbose;

import java.math.BigInteger;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HyperverbosePhraseGeneratorTest extends AHyperverboseTest {
	public static final HyperverboseWords ADJECTIVES = HyperverboseWordFile.wordsFromResource("/adjectives.txt");
	public static final HyperverboseWords ADVERBS = HyperverboseWordFile.wordsFromResource("/adverbs.txt");
	public static final HyperverboseWords NOUNS = HyperverboseWordFile.wordsFromResource("/nouns.txt");
	public static final HyperverboseWords VERBS = HyperverboseWordFile.wordsFromResource("/verbs.txt");

	@Test
	void builderIncludesParent() {
		// this is just here to make sure we don't screw up the builder
		final HyperverbosePhraseGenerator generator = HyperverbosePhraseGenerator.builder()
			.delimiter("!")
			.random(null)
			.wordCount(7)
			.titleCase(true)
			.wordList(ADJECTIVES)
			.wordList(ADVERBS)
			.wordList(NOUNS)
			.wordList(VERBS)
			.build();
		assertNotNull(generator, "should have gotten an instance");
		assertEquals(4, generator.getWordLists().size(), "should see two word lists");
	}

	@Test
	void fromBigIntExtendsCorrectly() {
		final HyperverbosePhraseGenerator phrases = HyperverbosePhraseGenerator.builder()
			.wordCount(4)
			.extraStrategy(HyperverbosePhraseGenerator.ExtraStrategy.Extend)
			.wordList(NOUNS)
			.wordList(ADJECTIVES)
			.wordList(ADVERBS)
			.build();
		assertNull(phrases.fromBigInt(null), "when called with null");
		assertEquals("curiously-feverishly-bright-cherry", phrases.fromBigInt(BigInteger.valueOf(1234)));
	}

	@Test
	void fromBigIntThrowsForTooMany() {
		final HyperverbosePhraseGenerator phrases = HyperverbosePhraseGenerator.builder()
			.wordCount(4)
			.extraStrategy(HyperverbosePhraseGenerator.ExtraStrategy.Throw)
			.wordList(NOUNS)
			.wordList(ADJECTIVES)
			.wordList(ADVERBS)
			.build();
		assertThrows(IndexOutOfBoundsException.class, () -> phrases.fromBigInt(BigInteger.valueOf(1234)));
	}

	@Test
	void fromBigIntWrapsCorrectly() {
		final HyperverbosePhraseGenerator phrases = HyperverbosePhraseGenerator.builder()
			.wordCount(4)
			.extraStrategy(HyperverbosePhraseGenerator.ExtraStrategy.Wrap)
			.wordList(NOUNS)
			.wordList(ADJECTIVES)
			.build();
		assertNull(phrases.fromBigInt(null), "when called with null");
		assertEquals("heliocentric-eggplant-allowed-banana", phrases.fromBigInt(BigInteger.valueOf(4321)));
	}

	@Test
	void randomId() {
		final Random random = new Random(RANDOM_SEED);
		final HyperverbosePhraseGenerator phrases = HyperverbosePhraseGenerator.builder()
			.random(random::nextInt)
			.wordCount(3)
			.wordList(ADJECTIVES)
			.wordList(ADJECTIVES)
			.wordList(NOUNS)
			.build();
		assertEquals("bright-delectable-honeydew", phrases.randomId());
	}
}
