package com.roche.sequencing.hyperverbose.command.args;

import com.roche.sequencing.hyperverbose.wordlist.WordList;
import java.nio.file.Path;
import lombok.Getter;
import picocli.CommandLine;

@Getter
public class SourceArgs {
	@CommandLine.Option(
		names = "--source-path",
		description = "Path to word list in source format",
		required = true
	)
	Path sourcePath;

	@CommandLine.Option(
		names = "--source-format",
		description = "Word list source format name",
		required = true,
		defaultValue = "${env:WORDLIST}"
	)
	WordList wordList;
}
