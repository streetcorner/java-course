package project;

public class PrecisionManage {
	private static int precision = 6;
	private static String precisionFormat = "#.######";
	
	public PrecisionManage() {
		
	}
	
	public static void setPrecision(int pre) {
		precision = pre;
		precisionFormat = "#.";
		
		for(int i = 0; i < precision; ++i) {
			precisionFormat += "#";
		}
	}
	
	public static int getPrecision() {
		return precision;
	}
	
	public static String getPrecisionFormat() {
		return precisionFormat;
	}
}
