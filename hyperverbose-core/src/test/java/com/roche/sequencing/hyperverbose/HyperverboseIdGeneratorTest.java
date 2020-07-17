package com.roche.sequencing.hyperverbose;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.roche.sequencing.hyperverbose.HyperverboseUtil.titleCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HyperverboseIdGeneratorTest extends AHyperverboseTest {

	@Test
	void fromBigInt() {
		assertNull(buildDefaultGenerator().fromBigInt(null), "when called with null");
		final long num = longFromWords(DELTA, BRAVO, ECHO);
		assertEquals(((3 * WORD_COUNT) + 1) * WORD_COUNT + 4, num);
		final BigInteger bigInt = BigInteger.valueOf(num);
		assertEquals("delta-bravo-echo", buildDefaultGenerator().fromBigInt(bigInt), "default generator");
		assertEquals("Delta!Bravo!Echo", buildCustomGenerator(null).fromBigInt(bigInt), "custom generator");
	}

	@Test
	void fromLong() {
		final long num = longFromWords(DELTA, BRAVO, ECHO);
		assertEquals(((3 * WORD_COUNT) + 1) * WORD_COUNT + 4, num);
		assertEquals(DELTA + "-" + BRAVO + "-" + ECHO, buildDefaultGenerator().fromLong(num), "default generator");
		assertEquals(titleCase(DELTA) + CUSTOM_DELIM + titleCase(BRAVO) + CUSTOM_DELIM + titleCase(ECHO), buildCustomGenerator(null).fromLong(num), "custom generator");
	}

	@Test
	void fromUuid() {
		assertEquals("gulf-delta-bravo-echo", buildDefaultGenerator().fromUuid(CS_UUID));
		assertEquals("Echo!Gulf!Delta!Bravo!Echo", buildCustomGenerator(null).fromUuid(CS_UUID));
	}

	@Test
	void fromUuidEmpty() {
		assertEquals("", buildDefaultGenerator().fromUuid(ZERO_UUID));
	}

	@Test
	void fullUuid() {
		// because leading zeros are truncated
		final UUID uuid = UUID.fromString(UUID.randomUUID().toString().replace("0", "1"));
		assertEquals(uuid.toString().replace("-", ""), buildHexGenerator().fromUuid(uuid));
	}

	@Test
	void randomId1248() {
		final Random random = new Random(1248);
		final IHyperverboseGenerator generator = buildDefaultGenerator(random::nextInt);
		assertEquals("delta-alpha-alpha-gulf", generator.randomId());
	}

	@Test
	void randomId1357() {
		final Random random = new Random(1357);
		final IHyperverboseGenerator generator = buildDefaultGenerator(random::nextInt);
		assertEquals("delta-gulf-hotel-alpha", generator.randomId());
	}

	@Test
	void randomNull() {
		final IHyperverboseGenerator generator = buildDefaultGenerator(null);
		assertThrows(NullPointerException.class, generator::randomId);
	}
}
