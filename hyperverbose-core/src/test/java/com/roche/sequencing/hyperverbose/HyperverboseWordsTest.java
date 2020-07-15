package com.roche.sequencing.hyperverbose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HyperverboseWordsTest extends AHyperverboseTest {

	@Test
	void count() {
		assertEquals(0, EMPTY_WORDS.count());
		assertEquals(WORD_COUNT, WORDS.count());
	}

	@Test
	void modWord() {
		assertNull(EMPTY_WORDS.modWord(0), "null from empty word list");
		assertNull(EMPTY_WORDS.modWord(1234), "null from empty word list");
		for (int n = 0; n < WORD_COUNT; n++) {
			final String word = WORD_LIST.get(n);
			assertEquals(word, WORDS.modWord(n));
			assertEquals(word, WORDS.modWord(n + WORD_COUNT), n + " plus " + WORD_COUNT);
			assertEquals(word, WORDS.modWord(n - WORD_COUNT), n + " minus " + WORD_COUNT);
		}
	}
}
