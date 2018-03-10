package project;

/**
 * A class to handle syntax exception
 */
public class SyntaxErrorException extends Exception {

	public SyntaxErrorException(String msg) {
		super(msg);
	}

	public String toString() {
		return "Syntax error: " + getMessage();
	}
}
