package com.roche.sequencing.hyperverbose.command;

import com.roche.sequencing.hyperverbose.HyperverboseUtil;
import com.roche.sequencing.hyperverbose.HyperverboseWordFile;
import com.roche.sequencing.hyperverbose.HyperverboseWords;
import com.roche.sequencing.hyperverbose.command.args.NormalizeArgs;
import com.roche.sequencing.hyperverbose.command.args.OutputArgs;
import com.roche.sequencing.hyperverbose.command.args.ReReplaceArgs;
import com.roche.sequencing.hyperverbose.command.args.SourceArgs;
import com.roche.sequencing.hyperverbose.wordlist.ANormalizer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(
	name = "transform",
	description = "Transform word list from source form to one usable by hyperverbose",
	subcommandsRepeatable = true
)
public class TransformCommand extends ACommand implements Callable<Integer> {
	@CommandLine.Mixin
	NormalizeArgs normalizeArgs;

	@CommandLine.Mixin
	SourceArgs sourceArgs;

	TreeSet<String> words;

	private void assertWorkingWords() {
		if (words == null) {
			throw new NullPointerException("No working word list!");
		}
	}

	@Override
	public Integer call() {
		final Path sourcePath = sourceArgs.getSourcePath();
		if (!sourcePath.toFile().exists()) {
			throw new IllegalArgumentException("Does not exist: " + sourcePath);
		}
		words = new TreeSet<>(loadWords(sourceArgs, normalizeArgs));
		return 0;
	}

	@CommandLine.Command(
		name = "intersect",
		description = "Load a local file and keep only the words in both it and the working set"
	)
	public void intersect(
		@CommandLine.Mixin final SourceArgs intersectSource,
		@CommandLine.Mixin final NormalizeArgs intersectNormalize
	) {
		assertWorkingWords();
		final TreeSet<String> rightWords = new TreeSet<>(loadWords(intersectSource, intersectNormalize));
		words.removeIf(word -> !rightWords.contains(word));
		log.debug("Words kept after intersect: " + words.size());
	}

	private ArrayList<String> loadWords(
		@CommandLine.Mixin final SourceArgs loadSource,
		@CommandLine.Mixin final NormalizeArgs loadNormalize
	) {
		log.debug("Loading: " + loadSource.getWordList() + " " + loadSource.getSourcePath());
		final ANormalizer normalizer = loadSource.getWordList().buildNormalizer();
		normalizer.setConfiguration(normalizeConfigFromArgs(loadNormalize));
		return normalizer.normalize(loadSource.getSourcePath()).getWords();
	}

	@CommandLine.Command(
		name = "lowercase",
		description = "Lowercase all words in the working list"
	)
	public void lowercase() {
		assertWorkingWords();
		final Counting updated = new Counting(String::toLowerCase);
		words = words.stream().map(updated).collect(Collectors.toCollection(TreeSet::new));
		log.debug("Lowercased: " + updated);
	}

	@CommandLine.Command(
		name = "re-replace",
		description = "Use a regular expression to replace parts of words in the working list"
	)
	public void reReplace(@CommandLine.Mixin @NonNull final ReReplaceArgs replaceArgs) {
		assertWorkingWords();
		final Counting updated = new Counting(word -> word.replaceAll(replaceArgs.getReFind(), replaceArgs.getReUpdate()));
		words = words.stream().map(updated).collect(Collectors.toCollection(TreeSet::new));
		log.debug("REReplace: " + updated);
	}

	@CommandLine.Command(
		name = "remove",
		description = "Load a local file remove its words from the working set"
	)
	public void remove(
		@CommandLine.Mixin final SourceArgs removeSource,
		@CommandLine.Mixin final NormalizeArgs removeNormalize
	) {
		assertWorkingWords();
		final TreeSet<String> rightWords = new TreeSet<>(loadWords(removeSource, removeNormalize));
		words.removeIf(rightWords::contains);
		log.debug("Words kept after remove: " + words.size());
	}

	@CommandLine.Command(
		name = "ucfirst",
		description = "Uppercase the first letter of all words in the working list"
	)
	public void ucfirst() {
		assertWorkingWords();
		final Counting updated = new Counting(HyperverboseUtil::titleCase);
		words = words.stream().map(updated).collect(Collectors.toCollection(TreeSet::new));
		log.debug("UC First: " + updated);
	}

	@CommandLine.Command(
		name = "union",
		description = "Load a local file and add its words to the working list"
	)
	public void union(
		@CommandLine.Mixin final SourceArgs unionSource,
		@CommandLine.Mixin final NormalizeArgs unionNormalize
	) {
		assertWorkingWords();
		words.addAll(loadWords(unionSource, unionNormalize));
		log.debug("Words kept after union: " + words.size());
	}

	@CommandLine.Command(
		name = "uppercase",
		description = "Uppercase all words in the working list"
	)
	public void uppercase() {
		assertWorkingWords();
		final Counting updated = new Counting(String::toUpperCase);
		words = words.stream().map(updated).collect(Collectors.toCollection(TreeSet::new));
		log.debug("Uppercased: " + updated);
	}

	@CommandLine.Command(
		name = "write",
		description = "Save the working set to a local file"
	)
	public void write(@CommandLine.Mixin final OutputArgs outputArgs) {
		assertWorkingWords();
		final Path destPath = outputArgs.getDestPath();
		if (!outputArgs.isOverwrite() && destPath.toFile().exists()) {
			log.info("Exists: " + destPath);
			return;
		}
		boolean gzip = outputArgs.isGzip();
		if (destPath.getFileName().toString().endsWith(".gz")) {
			gzip = true;
		}
		HyperverboseWordFile.writeWordsToPath(new HyperverboseWords(new ArrayList<>(this.words)), destPath, gzip);
		log.debug("Wrote: " + destPath);
	}

	@RequiredArgsConstructor
	private static class Counting implements Function<String, String> {
		private final Function<String, String> block;
		private int total = 0;
		private int updated = 0;

		@Override
		public String apply(final String t) {
			final String u = block.apply(t);
			total++;
			if (!Objects.equals(t, u)) {
				updated++;
			}
			return u;
		}

		public String toString() {
			return updated + "/" + total;
		}
	}
}
