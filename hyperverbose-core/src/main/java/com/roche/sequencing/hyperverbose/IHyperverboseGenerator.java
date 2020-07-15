package com.roche.sequencing.hyperverbose;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Helper to generate or convert human-readable word-based identifiers.
 * Generally, that means given a number or a UUID, build a deterministic
 * representation of that value using words.  For example, {@code 204L}
 * might become {@code cherry-apple-banana}.
 * Generally, since longs and UUIDs have more bits than you'd want to
 * represent with words, this usually has a concept of a word count to limit
 * the generated strings to a reasonable length.  But that also means
 * some of the bit information is lost in the conversion, so you could
 * end up with collisions.  You should never use these strings as keys.
 * Use the source long or UUID as a key instead.
 */
public interface IHyperverboseGenerator {
	/**
	 * Convert from a BigInt, trying to preserve endianness.
	 * This is the same algorithm as {@link #fromLong(long)}.
	 * Presuming that the low order bits have higher entropy than high order, this assembles words from the "right".
	 */
	String fromBigInt(BigInteger source);

	/**
	 * Convert from a long, trying to preserve endianness.
	 * This is the same algorithm as {@link #fromBigInt(BigInteger)}.
	 * Presuming that the low order bits have higher entropy than high order, this assembles words from the "right".
	 */
	String fromLong(long source);

	/**
	 * Similar to {@link #fromBigInt(BigInteger)}, this will start with the low order bits and work "left".
	 */
	String fromUuid(UUID uuid);

	/**
	 * In general, you probably want to just make a random UUID and then use {@link #fromUuid(UUID)} instead.
	 */
	String randomId();
}
