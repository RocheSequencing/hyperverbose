package com.roche.sequencing.hyperverbose.command;

import com.roche.sequencing.hyperverbose.wordlist.NormalizerConfiguration;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
	name = "defaults",
	description = "Show the default word list normalization parameters"
)
public class DefaultsCommand implements Callable<Integer> {
	@Override
	public Integer call() {
		final NormalizerConfiguration normalizerConfiguration = NormalizerConfiguration.builder().build();
		System.out.println(normalizerConfiguration.toString());
		return 0;
	}
}
