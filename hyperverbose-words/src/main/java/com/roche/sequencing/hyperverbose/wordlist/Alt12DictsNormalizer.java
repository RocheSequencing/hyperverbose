package com.roche.sequencing.hyperverbose.wordlist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;

public class Alt12DictsNormalizer extends ANormalizer {
	public static final String DICT_COUNT_GROUP = "dictCount";
	public static final Pattern LINE_PATTERN = Pattern.compile(
		"^" +
			"\\s*" +
			"(?<dictCount>\\d+|-)" +
			"(?<dictFlag>[:&#=]*)" +
			"\\s+" +
			"(?<nonVariantCount>\\d+|-)" +
			"(?<nonVariantFlag>[:&#=]*)" +
			"\\s+" +
			"(?<variantCount>\\d+|-)" +
			"(?<variantFlag>[:&#=]*)" +
			"\\s+" +
			"(?<nonAmericanCount>\\d+|-)" +
			"(?<nonAmericanFlag>[:&#=]*)" +
			"\\s+" +
			"(?<secondClassCount>\\d+|-)" +
			"(?<secondClassFlag>[:&#=]*)" +
			"\\s+" +
			"\\s*(?<word>.+)\\s*" +  // trim
			"$"
	);
	public static final String NON_VARIANT_COUNT_GROUP = "nonVariantCount";
	public static final String VARIANT_COUNT_GROUP = "variantCount";
	public static final String WORD_GROUP = "word";

	protected int countFromGroup(final String group) {
		return group == null || group.equals("-") ? 0 : Integer.parseInt(group, 10);
	}

	@Override
	protected String wordFromSourceLine(@NonNull final String sourceLine) {
		final Matcher matcher = LINE_PATTERN.matcher(sourceLine);
		if (!matcher.find()) {
			return null;
		}
		final int dictCount = countFromGroup(matcher.group(DICT_COUNT_GROUP));
		final int nonVariantCount = countFromGroup(matcher.group(NON_VARIANT_COUNT_GROUP));
		final int variantCount = countFromGroup(matcher.group(VARIANT_COUNT_GROUP));
		final NormalizerConfiguration config = getConfiguration();
		final int dictCountMin = config.getDictCountMin();
		if (dictCount < dictCountMin) {
			// we don't want esoteric words
			return null;
		}
		if (!config.isIncludeVariants() && (nonVariantCount < dictCountMin || variantCount > dictCountMin)) {
			return null;
		}
		final String word = matcher.group(WORD_GROUP);
		if (shouldReject(word)) {
			return null;
		}
		return word.toLowerCase();
	}
}
