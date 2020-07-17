package com.roche.sequencing.hyperverbose.command;

import com.roche.sequencing.hyperverbose.command.args.NormalizeArgs;
import com.roche.sequencing.hyperverbose.wordlist.NormalizerConfiguration;
import com.roche.sequencing.hyperverbose.wordlist.PartOfSpeech;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ACommand {
	public static NormalizerConfiguration normalizeConfigFromArgs(final NormalizeArgs args) {
		final NormalizerConfiguration.NormalizerConfigurationBuilder builder = NormalizerConfiguration.builder();
		if (args != null) {
			Optional.ofNullable(args.getDictCountMin()).ifPresent(builder::dictCountMin);
			Optional.ofNullable(args.getLengthMin()).ifPresent(builder::lengthMin);
			Optional.ofNullable(args.getLengthMax()).ifPresent(builder::lengthMax);
			Optional.ofNullable(args.getPosIncludes()).ifPresent(builder::posIncludes);
			Optional.ofNullable(args.getPosExcludes()).ifPresent(builder::posExcludes);
			final String rejectPattern = args.getRejectPattern();
			builder.rejectPattern(rejectPattern == null || rejectPattern.isEmpty() ? null : Pattern.compile(rejectPattern));
			builder.includeVariants(args.isIncludeVariants());
			if (args.isPosIncludesAll()) {
				builder.posIncludes(Stream.of(PartOfSpeech.values()).collect(Collectors.toSet()));
			}
			if (args.isPosExcludesNone()) {
				builder.posExcludes(new HashSet<>());
			}
		}
		final NormalizerConfiguration configuration = builder.build();
		log.debug(configuration.toString());
		return configuration;
	}
}
