package project;

public class LastManage {

	private static Double lastValue;
	private static boolean changeable = false;
	
	public LastManage() {
		
	}
	
	public static void changeState() {
		changeable = !changeable;
	}
	
	public static boolean getState() {
		return changeable;
	}
	
	public static void setLastValue(Double last) {
		lastValue = last;
	}
	
	public static Double getLastValue() {
		return lastValue;
	}
}
