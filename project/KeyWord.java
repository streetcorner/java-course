package project;

import java.util.HashSet;

public class KeyWord {

	private static HashSet<String> keyWord = new HashSet<String>();
	
	static {
		keyWord.add("setprecision");
		keyWord.add("let");
		keyWord.add("reset");
		keyWord.add("save");
		keyWord.add("load");
		keyWord.add("saved");
		keyWord.add("log");
		keyWord.add("logged");
	}
	
	public KeyWord() {
		
	}
	
	public static boolean contains(String identifier) {
		return keyWord.contains(identifier);
	}
}
