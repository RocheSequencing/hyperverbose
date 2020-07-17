package com.roche.sequencing.hyperverbose.wordlist;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;

public class PartsOfSpeechNormalizer extends ANormalizer {
	public static final Pattern LINE_PATTERN = Pattern.compile(
		"^" +
			"\\s*" +
			"(?<word>[^\t]+)" +
			"\t" +
			"(?<pos>\\S+)" +
			"\\s*" +
			"$"
	);
	public static Map<String, PartOfSpeech> POS_BY_CODE = Stream.of(PartOfSpeech.values())
		.collect(Collectors.toMap(PartOfSpeech::getCode, p -> p));

	@Override
	protected String wordFromSourceLine(@NonNull final String sourceLine) {
		final Matcher matcher = LINE_PATTERN.matcher(sourceLine);
		if (!matcher.find()) {
			return null;
		}
		final String word = matcher.group("word");
		final String pos = matcher.group("pos");
		if (shouldReject(word) || pos == null || pos.isEmpty()) {
			return null;
		}
		final Set<PartOfSpeech> blacklist = getConfiguration().getPosExcludes();
		final Set<PartOfSpeech> preferred = getConfiguration().getPosIncludes();
		final PosFilterState decision = pos.chars()
			.mapToObj(Character::toChars)
			.map(String::new)
			.map(POS_BY_CODE::get)
			.filter(Objects::nonNull)
			.reduce(PosFilterState.INDIFFERENT, (state, cpos) -> {
				if (blacklist.contains(cpos)) {
					return PosFilterState.REJECT;
				} else if (preferred.contains(cpos)) {
					return PosFilterState.PREFER;
				} else {
					return PosFilterState.INDIFFERENT;
				}
			}, (prev, cur) -> {
				if (prev == PosFilterState.REJECT || cur == PosFilterState.REJECT) {
					return PosFilterState.REJECT;
				} else if (prev == PosFilterState.PREFER || cur == PosFilterState.PREFER) {
					return PosFilterState.PREFER;
				} else {
					return PosFilterState.INDIFFERENT;
				}
			});
		if (PosFilterState.PREFER != decision) {
			return null;
		}
		return word.toLowerCase();
	}

	private enum PosFilterState {
		INDIFFERENT,
		REJECT,
		PREFER,
	}
}
