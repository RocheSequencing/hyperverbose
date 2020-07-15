package com.roche.sequencing.hyperverbose;

import com.roche.sequencing.hyperverbose.command.DefaultsCommand;
import com.roche.sequencing.hyperverbose.command.DownloadCommand;
import com.roche.sequencing.hyperverbose.command.ListTypesCommand;
import com.roche.sequencing.hyperverbose.command.TransformCommand;
import java.util.concurrent.Callable;
import lombok.NoArgsConstructor;
import picocli.CommandLine;

@CommandLine.Command(
	name = "wordlist",
	description = "Work with word list files",
	mixinStandardHelpOptions = true,
	subcommands = {
		DownloadCommand.class,
		ListTypesCommand.class,
		TransformCommand.class,
		DefaultsCommand.class
	},
	versionProvider = WordListCmd.VersionProvider.class
)
@NoArgsConstructor
public class WordListCmd implements Callable<Integer> {
	@CommandLine.Spec
	private CommandLine.Model.CommandSpec commandSpec;

	public static void main(String[] args) {
		final int exitCode = new CommandLine(new WordListCmd())
			.setExecutionStrategy(new CommandLine.RunAll())
			.execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		if (commandSpec.commandLine().getParseResult().subcommand() == null) {
			commandSpec.commandLine().usage(System.out);
			return 1;
		} else {
			return commandSpec.commandLine().getExecutionResult();
		}
	}

	public static class VersionProvider implements CommandLine.IVersionProvider {
		@Override
		public String[] getVersion() {
			return new String[]{getClass().getPackage().getImplementationVersion()};
		}
	}
}
