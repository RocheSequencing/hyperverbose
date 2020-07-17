package com.roche.sequencing.hyperverbose.wordlist;

public class Alt12DictsDownloader extends ADownloader {
	public static final String URL_2OF12 = "https://github.com/en-wl/wordlist/raw/master/alt12dicts/2of12full.txt";

	@Override
	public String getSourceUrl() {
		return URL_2OF12;
	}
}
