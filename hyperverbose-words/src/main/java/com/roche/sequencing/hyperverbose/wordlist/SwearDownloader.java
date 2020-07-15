package com.roche.sequencing.hyperverbose.wordlist;

public class SwearDownloader extends ADownloader {
	public static final String URL_SWEAR = "http://www.bannedwordlist.com/lists/swearWords.txt";

	@Override
	public String getSourceUrl() {
		return URL_SWEAR;
	}
}
