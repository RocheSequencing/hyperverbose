package com.roche.sequencing.hyperverbose;

import java.math.BigInteger;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HyperverboseUtilTest {

	@Test
	void bigIntFromUuid() {
		assertNull(HyperverboseUtil.bigIntFromUuid(null), "when called with null");
		assertEquals(BigInteger.ZERO, HyperverboseUtil.bigIntFromUuid(UUID.fromString("00000000-0000-0000-0000-00000000")));
		assertEquals(BigInteger.valueOf(204L), HyperverboseUtil.bigIntFromUuid(UUID.fromString("00000000-0000-0000-0000-000000CC")));
	}

	@Test
	void titleCase() {
		assertNull(HyperverboseUtil.titleCase(null), "when called with null");
		assertEquals("A", HyperverboseUtil.titleCase("a"), "when called with a single character");
		assertEquals("Abc", HyperverboseUtil.titleCase("abc"), "when called with multiple characters");
		assertEquals("0a", HyperverboseUtil.titleCase("0a"), "when called with a non-alpha leading character");
	}
}
