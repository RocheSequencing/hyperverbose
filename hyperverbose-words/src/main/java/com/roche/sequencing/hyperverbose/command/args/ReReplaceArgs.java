package com.roche.sequencing.hyperverbose.command.args;

import lombok.Getter;
import picocli.CommandLine;

@Getter
public class ReReplaceArgs {
	@CommandLine.Option(
		names = "--re-find",
		description = "Regular expression pattern to find",
		required = true
	)
	String reFind;

	@CommandLine.Option(
		names = "--re-update",
		description = "Replacement/update expression",
		required = true
	)
	String reUpdate;
}
