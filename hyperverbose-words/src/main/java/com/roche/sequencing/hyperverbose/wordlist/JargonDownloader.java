package com.roche.sequencing.hyperverbose.wordlist;

public class JargonDownloader extends ADownloader {
	public static final String URL_JARGON = "https://raw.githubusercontent.com/en-wl/wordlist/master/jargon-wl/pos.txt";

	@Override
	public String getSourceUrl() {
		return URL_JARGON;
	}
}
