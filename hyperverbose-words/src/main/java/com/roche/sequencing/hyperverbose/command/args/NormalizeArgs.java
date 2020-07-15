package com.roche.sequencing.hyperverbose.command.args;

import com.roche.sequencing.hyperverbose.wordlist.NormalizerConfiguration;
import com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech;
import java.util.Set;
import lombok.Getter;
import picocli.CommandLine;

import static com.roche.sequencing.hyperverbose.wordlist.NormalizerConfiguration.NON_ALPHA;

@Getter
public class NormalizeArgs {
	@CommandLine.Option(
		names = "--dict-count-min",
		description = "For aggregated word lists, how many dictionaries should the word appear in?",
		defaultValue = "" + NormalizerConfiguration.DICT_COUNT_MIN_DEFAULT
	)
	Integer dictCountMin;

	@CommandLine.Option(
		names = "--include-variants",
		description = "For aggregated word lists, should we exclude variant spellings?",
		defaultValue = "false"
	)
	boolean includeVariants = false;

	@CommandLine.Option(
		names = "--length-max",
		description = "How many characters should the word be, at most?",
		defaultValue = "" + NormalizerConfiguration.LENGTH_MAX_DEFAULT
	)
	Integer lengthMax;

	@CommandLine.Option(
		names = "--length-min",
		description = "How many characters should the word be, at least?",
		defaultValue = "" + NormalizerConfiguration.LENGTH_MIN_DEFAULT
	)
	Integer lengthMin;

	@CommandLine.Option(
		names = "--pos-exclude",
		description = "Part of speech to be excluded",
		type = PartOfSpeech.class
	)
	Set<PartOfSpeech> posExcludes;

	@CommandLine.Option(
		names = "--pos-exclude-none",
		description = "Don't exclude any parts of speech",
		defaultValue = "false"
	)
	boolean posExcludesNone = false;

	@CommandLine.Option(
		names = "--pos-include",
		description = "Part of speech to be considered preferred",
		type = PartOfSpeech.class
	)
	Set<PartOfSpeech> posIncludes;

	@CommandLine.Option(
		names = "--pos-include-all",
		description = "Include all parts of speech (don't filter any)",
		defaultValue = "false"
	)
	boolean posIncludesAll = false;

	@CommandLine.Option(
		names = "--reject-pattern",
		description = "Catch-all regular expression which will cause the word to be rejected if present",
		defaultValue = NON_ALPHA
	)
	String rejectPattern;
}
