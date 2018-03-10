package project;

/**
 * A class to handle general exception
 */
public class GeneralErrorException extends Exception {

	public GeneralErrorException(String msg) {
		super(msg);
	}

	public String toString() {
		return "Error: " + getMessage();
	}
}
