package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map.Entry;

public class LogManage {

	private static boolean logged = false;
	private static boolean writeInLog = false;
	private static String logName = "";
	private static String logged_folder = "logged";
	private static String logged_path = "logged/";
	private static String suffix = ".txt";
	
	public LogManage() {
		
	}
	
	public static void changeLogged() {
		logged = !logged;
	}
	
	public static boolean getLoggedStatus() {
		return logged;
	}
	
	public static void changeWriteInLog() {
		writeInLog = !writeInLog;
	}
	
	public static boolean getWriteInLogStatus() {
		return writeInLog;
	}
	
	public static void setLogName(String log_name) {
		logName = log_name;
	}
	
	public static String getLogName() {
		return logName;
	}
	
	public static String log(Tokenizer tokenizer) throws TokenException, LexicalErrorException, GeneralErrorException {
		if(!tokenizer.hasNextToken()) {
			if(getLoggedStatus()) return getLogName();
			else throw new LexicalErrorException("syntax error");
		}
		
		String outPut = "";
		Token token = tokenizer.readNextToken();
		
		if(token.isString() && !tokenizer.hasNextToken()) {
			if(getLoggedStatus()) throw new GeneralErrorException("logging is being logged");
			
			logName = token.getString();
			changeLogged();
			outPut = "logging session to " + logName;
		}
		else if(token.isIdentifier() && token.getIdentifier().equals("end") && !tokenizer.hasNextToken()) {
			if(!getLoggedStatus()) throw new GeneralErrorException("no log is being logged");
			
			outPut = "session was logged to " + logName;
			logName = "";
			changeLogged();
		}
		else throw new LexicalErrorException("syntax error");
			
		return outPut;
	}
	
	public static void writeLog(String conent) throws IOException{
		BufferedWriter out = null;
		File file = new File(logged_path + logName + suffix);

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent + "\n");
			
			out.close();
	}
	
	public static String logged(Tokenizer tokenizer) throws LexicalErrorException {
		if(tokenizer.hasNextToken()) throw new LexicalErrorException("syntax error");
		
		String outPut = "";

		File dir = new File(logged_folder);
		String[] file_list = dir.list();
		
		if(file_list.length == 0) return "no logged files";
		
		for(int i = 0; i < file_list.length - 1; ++i) {
			outPut += file_list[i].substring(0, file_list[i].length() - 4) + "\n";
		}
		outPut += file_list[file_list.length - 1].substring(0, file_list[file_list.length - 1].length() - 4);

		return outPut;
	}
}
