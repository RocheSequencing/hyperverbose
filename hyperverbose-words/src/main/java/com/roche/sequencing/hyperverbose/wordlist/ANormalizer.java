package com.roche.sequencing.hyperverbose.wordlist;

import com.roche.sequencing.hyperverbose.HyperverboseWords;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Base for classes which take a public word list, which probably has an amount of word metadata,
 * and filters and transforms it down to a set of words without metadata.
 */
@Slf4j
public abstract class ANormalizer {

	@Setter
	@Getter
	private NormalizerConfiguration configuration = NormalizerConfiguration.builder().build();

	protected boolean hasGoodLength(final String word) {
		if (word == null || word.isEmpty()) return false;
		final NormalizerConfiguration config = getConfiguration();
		return word.length() >= config.getLengthMin() && word.length() <= config.getLengthMax();
	}

	public HyperverboseWords normalize(@NonNull final Path sourcePath) {
		try (final FileInputStream sourceIn = new FileInputStream(sourcePath.toFile())) {
			final BufferedReader sourceReader = new BufferedReader(new InputStreamReader(sourceIn));
			String sourceLine;
			final Set<String> words = new HashSet<>();
			long wordsAdded = 0;
			long linesTotal = 0;
			do {
				sourceLine = sourceReader.readLine();
				if (sourceLine != null && !sourceLine.isEmpty()) {
					linesTotal++;
					final String word = wordFromSourceLine(sourceLine);
					if (word != null) {
						words.add(word);
						wordsAdded++;
					}
				}
			} while (sourceLine != null);
			log.debug("Lines: " + linesTotal);
			log.debug("Words kept: " + wordsAdded);
			return new HyperverboseWords(new ArrayList<>(words.stream().sorted(String::compareTo).collect(Collectors.toList())));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Source path not found: " + sourcePath, e);
		} catch (IOException e) {
			throw new IllegalStateException("Could not read source path: " + sourcePath, e);
		}
	}

	protected boolean shouldReject(final String word) {
		final Pattern rejectPattern = getConfiguration().getRejectPattern();
		return word == null || word.isEmpty() || !hasGoodLength(word) || (rejectPattern != null && rejectPattern.matcher(word).find());
	}

	protected abstract String wordFromSourceLine(@NonNull final String sourceLine);
}
