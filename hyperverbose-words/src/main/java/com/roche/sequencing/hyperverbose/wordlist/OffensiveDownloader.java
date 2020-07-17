package com.roche.sequencing.hyperverbose.wordlist;

public class OffensiveDownloader extends ADownloader {
	public static final String URL_OFFENSIVE = "https://www.cs.cmu.edu/~biglou/resources/bad-words.txt";

	@Override
	public String getSourceUrl() {
		return URL_OFFENSIVE;
	}
}
