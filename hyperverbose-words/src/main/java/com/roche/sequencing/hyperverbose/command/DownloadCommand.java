package com.roche.sequencing.hyperverbose.command;

import com.roche.sequencing.hyperverbose.command.args.OutputArgs;
import com.roche.sequencing.hyperverbose.wordlist.ADownloader;
import com.roche.sequencing.hyperverbose.wordlist.WordList;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(
	name = "download",
	description = "Download a word list"
)
public class DownloadCommand extends ACommand implements Callable<Integer> {
	@CommandLine.Mixin
	OutputArgs outputArgs;

	@CommandLine.Option(
		names = "--source-format",
		description = "Source word list format name",
		required = true
	)
	WordList wordList;

	@Override
	public Integer call() {
		final Path savePath = outputArgs.getDestPath();
		if (!outputArgs.isOverwrite() && savePath.toFile().exists()) {
			log.info("Exists: " + savePath);
			return 0;
		}
		final ADownloader downloader = wordList.buildDownloader();
		final Path downloadedPath = downloader.downloadToPath(savePath);
		log.info("Downloaded: " + downloadedPath);
		return 0;
	}
}
