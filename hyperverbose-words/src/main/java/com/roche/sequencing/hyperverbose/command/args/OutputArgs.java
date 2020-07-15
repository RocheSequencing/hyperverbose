package com.roche.sequencing.hyperverbose.command.args;

import java.nio.file.Path;
import lombok.Getter;
import picocli.CommandLine;

@Getter
public class OutputArgs {
	public static final boolean GZIP_DEFAULT = false;
	public static final boolean OVERWRITE_DEFAULT = false;

	@CommandLine.Option(
		names = "--dest-path",
		description = "Where to save the normalized intersection words file",
		required = true
	)
	Path destPath;

	@CommandLine.Option(
		names = "--gzip",
		description = "Gzip output",
		defaultValue = "" + GZIP_DEFAULT
	)
	boolean gzip = GZIP_DEFAULT;

	@CommandLine.Option(
		names = "--overwrite",
		description = "Overwrite the file if it already exists",
		defaultValue = "" + OVERWRITE_DEFAULT
	)
	boolean overwrite = OVERWRITE_DEFAULT;
}
