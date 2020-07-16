package com.roche.sequencing.hyperverbose.wordlist;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;

import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.Adjective;
import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.ArticleDefinite;
import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.ArticleIndefinite;
import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.Conjunction;
import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.Interjection;
import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.Noun;
import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.Preposition;
import static com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech.Pronoun;

/**
 * This is an aggregation of all possible configuration toggles for all normalizers.
 */
@Value
@Getter
@NonFinal
@ToString
@Builder(toBuilder = true)
public class NormalizerConfiguration {
	public static final int DICT_COUNT_MIN_DEFAULT = 7;
	public static final boolean INCLUDE_VARIANTS_DEFAULT = false;
	public static final int LENGTH_MAX_DEFAULT = 7;
	public static final int LENGTH_MIN_DEFAULT = 3;
	public static final String NON_ALPHA = "[^a-zA-Z]";
	public static final Set<PartOfSpeech> POS_EXCLUDE_DEFAULT = Stream.of(ArticleDefinite,
		ArticleIndefinite, Pronoun, Interjection, Preposition, Conjunction)
		.collect(Collectors.toSet());
	public static final Set<PartOfSpeech> POS_INCLUDE_DEFAULT = Stream.of(Noun, Adjective)
		.collect(Collectors.toSet());
	public static final Pattern REJECT_PATTERN_DEFAULT = Pattern.compile(NON_ALPHA);

	/**
	 * For aggregated word lists, how many dictionaries should the word appear in?
	 */
	@Builder.Default
	int dictCountMin = DICT_COUNT_MIN_DEFAULT;

	/**
	 * For aggregated word lists, should we exclude variant spellings?
	 */
	@Builder.Default
	boolean includeVariants = INCLUDE_VARIANTS_DEFAULT;

	/**
	 * How many characters should the word be, at most?
	 */
	@Builder.Default
	int lengthMax = LENGTH_MAX_DEFAULT;

	/**
	 * How many characters should the word be, at least?
	 */
	@Builder.Default
	int lengthMin = LENGTH_MIN_DEFAULT;

	/**
	 * Which parts of speech should we consider blacklisted?
	 */
	@Builder.Default
	Set<PartOfSpeech> posExcludes = POS_EXCLUDE_DEFAULT;

	/**
	 * Which parts of speech should we consider preferred?
	 */
	@Builder.Default
	Set<PartOfSpeech> posIncludes = POS_INCLUDE_DEFAULT;

	/**
	 * Catch-all regular expression which will cause the word to be rejected if present.
	 * Generally used to check for non-alpha characters.
	 */
	@Builder.Default
	Pattern rejectPattern = REJECT_PATTERN_DEFAULT;
}
