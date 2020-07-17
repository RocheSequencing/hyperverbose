package com.roche.sequencing.hyperverbose;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import lombok.NonNull;
import lombok.SneakyThrows;

import static com.roche.sequencing.hyperverbose.HyperverboseUtil.gunzipIfNeeded;

public final class HyperverboseWordFile {
	@SneakyThrows
	public static HyperverboseWords wordsFromInputStream(@NonNull final InputStream inputStream) {
		try (final BufferedReader sourceIn = new BufferedReader(new InputStreamReader(gunzipIfNeeded(inputStream)))) {
			final List<String> words = new LinkedList<>();
			while (sourceIn.ready()) {
				final String word = sourceIn.readLine();
				if (word != null && !word.isEmpty()) {
					words.add(word);
				}
			}
			return new HyperverboseWords(new ArrayList<>(words));
		}
	}

	@SneakyThrows
	public static HyperverboseWords wordsFromPath(@NonNull final Path path) {
		try (final FileInputStream sourceIn = new FileInputStream(path.toFile())) {
			return wordsFromInputStream(sourceIn);
		}
	}

	@SneakyThrows
	public static HyperverboseWords wordsFromResource(@NonNull final String path) {
		return wordsFromResource(HyperverboseWordFile.class, path);
	}

	@SneakyThrows
	public static HyperverboseWords wordsFromResource(
		@NonNull final Class<?> type,
		@NonNull final String path
	) {
		try (final InputStream sourceIn = type.getResourceAsStream(path)) {
			return wordsFromInputStream(sourceIn);
		}
	}

	@SneakyThrows
	public static void writeWordsToPath(
		@NonNull final HyperverboseWords wordFile,
		@NonNull final Path path,
		final boolean gzip
	) {
		final FileOutputStream fileOut = new FileOutputStream(path.toFile());
		final OutputStream maybeGzipped = gzip ? new GZIPOutputStream(fileOut) : fileOut;
		try (final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(maybeGzipped))) {
			for (final String word : wordFile.getWords()) {
				out.write(word);
				out.newLine();
			}
		}
	}
}
