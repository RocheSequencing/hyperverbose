package com.roche.sequencing.hyperverbose.wordlist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import lombok.NonNull;

public abstract class ADownloader {
	public Path downloadToPath(@NonNull final Path path) {
		final String sourceUrl = getSourceUrl();
		if (sourceUrl == null) {
			throw new NullPointerException("Missing sourceUrl in " + getClass().getSimpleName());
		}
		try {
			final URL url = new URL(sourceUrl);
			final InputStream in = url.openStream();
			final ReadableByteChannel inChannel = Channels.newChannel(in);
			final File outFile = path.toFile();
			final FileOutputStream out = new FileOutputStream(outFile);
			final long copiedBytes = out.getChannel().transferFrom(inChannel, 0, Long.MAX_VALUE);
			if (copiedBytes < 1) {
				throw new IllegalStateException("No bytes were copied, there may have been an error");
			}
			return path;
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Bad URL: " + sourceUrl, e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not open URL: " + sourceUrl, e);
		}
	}

	public Path downloadToTemp() {
		try {
			final File tempFile = File.createTempFile(getClass().getSimpleName(), ".txt");
			tempFile.deleteOnExit();
			return downloadToPath(tempFile.toPath());
		} catch (IOException e) {
			throw new IllegalStateException("Could not create temp file", e);
		}
	}

	public abstract String getSourceUrl();
}
