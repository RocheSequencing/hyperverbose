package com.roche.sequencing.hyperverbose.wordlist;

public class PartsOfSpeechDownloader extends ADownloader {
	public static final String URL_POS = "https://raw.githubusercontent.com/en-wl/wordlist/master/pos/part-of-speech.txt";

	@Override
	public String getSourceUrl() {
		return URL_POS;
	}
}
