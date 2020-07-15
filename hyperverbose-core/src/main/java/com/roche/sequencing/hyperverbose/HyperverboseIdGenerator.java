package com.roche.sequencing.hyperverbose;

import java.math.BigInteger;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A generator which uses a single word list.
 */
@SuperBuilder(toBuilder = true)
@Getter
public class HyperverboseIdGenerator extends AHyperverboseGenerator {
	/**
	 * Word list abstraction.
	 */
	@NonNull
	private final HyperverboseWords words;

	@Override
	public String fromBigInt(final BigInteger source) {
		return fromBigInt(ignored -> words, source);
	}

	@Override
	public String randomId() {
		return randomId(ignored -> words);
	}
}
