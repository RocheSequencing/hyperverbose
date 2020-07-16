package com.roche.sequencing.hyperverbose;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HyperverboseWordFileTest extends AHyperverboseTest {
	@Test
	public void wordsFromResourceGzipped() {
		final HyperverboseWords words = HyperverboseWordFile.wordsFromResource("/phonetic.txt.gz");
		assertEquals(WORD_LIST, words.getWords());
	}

	@Test
	public void wordsFromResourcePlain() {
		final HyperverboseWords words = HyperverboseWordFile.wordsFromResource("/phonetic.txt");
		assertEquals(WORD_LIST, words.getWords());
	}

	@Test
	public void writeAndReadPathGz() throws IOException {
		final Path tempFile = Files.createTempFile(getClass().getSimpleName(), ".txt");
		tempFile.toFile().deleteOnExit();
		HyperverboseWordFile.writeWordsToPath(WORDS, tempFile, true);
		final HyperverboseWords readWords = HyperverboseWordFile.wordsFromPath(tempFile);
		assertEquals(WORD_LIST, readWords.getWords());
	}

	@Test
	public void writeAndReadPathNoGz() throws IOException {
		final Path tempFile = Files.createTempFile(getClass().getSimpleName(), ".txt");
		tempFile.toFile().deleteOnExit();
		HyperverboseWordFile.writeWordsToPath(WORDS, tempFile, false);
		final HyperverboseWords readWords = HyperverboseWordFile.wordsFromPath(tempFile);
		assertEquals(WORD_LIST, readWords.getWords());
	}
}
