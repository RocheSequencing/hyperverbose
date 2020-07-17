package com.roche.sequencing.hyperverbose;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import lombok.NonNull;
import lombok.SneakyThrows;

public final class HyperverboseUtil {
	public static BigInteger bigIntFromUuid(final UUID uuid) {
		if (uuid == null) {
			return null;
		}
		final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());
		return new BigInteger(1, byteBuffer.array());
	}

	@SneakyThrows
	public static InputStream gunzipIfNeeded(@NonNull final InputStream inputStream) {
		final PushbackInputStream pushback = new PushbackInputStream(inputStream, 2);
		final byte[] bytes = new byte[2];
		final int bytesRead = pushback.read(bytes);
		final boolean isGzipped = bytesRead == 2 && (byte) (GZIPInputStream.GZIP_MAGIC % 256) == bytes[0] && (byte) (GZIPInputStream.GZIP_MAGIC / 256) == bytes[1];
		pushback.unread(bytes);
		return isGzipped ? new GZIPInputStream(pushback) : pushback;
	}

	public static String titleCase(final String s) {
		return s == null ? null : s.length() < 2 ? s.toUpperCase() : s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
