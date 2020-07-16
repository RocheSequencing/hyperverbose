package com.roche.sequencing.hyperverbose.command;

import com.roche.sequencing.hyperverbose.wordlist.ADownloader;
import com.roche.sequencing.hyperverbose.wordlist.WordList;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import picocli.CommandLine;

@CommandLine.Command(
	name = "list-types",
	description = "Print a list of word list types available for use"
)
public class ListTypesCommand implements Callable<Integer> {
	@CommandLine.Option(
		names = "--with-description",
		description = "Include the description, if available",
		defaultValue = "false"
	)
	boolean includeDescription = false;
	@CommandLine.Option(
		names = "--with-url",
		description = "Include the source URL, if available",
		defaultValue = "false"
	)
	boolean includeUrl = false;

	@Override
	public Integer call() {
		Integer padTo = null;
		if (includeDescription || includeUrl) {
			padTo = Stream.of(WordList.values()).map(WordList::name).map(String::length).reduce(0, Math::max);
		}
		for (final WordList format : WordList.values()) {
			final StringBuilder line = new StringBuilder();
			line.append(format.name());
			if (padTo != null && format.name().length() < padTo) {
				line.append(String.join("", Collections.nCopies(padTo - format.name().length(), " ")));
			}
			if (includeDescription) {
				line.append("\t").append(format.getDescription());
			}
			if (includeUrl) {
				final ADownloader downloader = format.buildDownloader();
				final String sourceUrl = downloader.getSourceUrl();
				line.append("\t").append(sourceUrl == null ? "" : sourceUrl);
			}
			System.out.println(line.toString());
		}
		return 0;
	}
}
