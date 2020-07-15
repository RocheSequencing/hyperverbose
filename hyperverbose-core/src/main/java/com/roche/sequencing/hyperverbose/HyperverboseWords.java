package com.roche.sequencing.hyperverbose;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class HyperverboseWords {
	@Getter
	private final ArrayList<String> words;

	public int count() {
		return words.size();
	}

	public String modWord(final int num) {
		if (words.isEmpty()) {
			return null;
		}
		final int offset = num % words.size();
		return words.get(offset < 0 ? offset + words.size() : offset);
	}
}
