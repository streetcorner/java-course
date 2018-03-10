package project;

import java.util.*;

/**
 * This class is the model in the MVC architecture.
 * The model performs computations and notifies the view
 * with the result. The model should use your code to
 * evaluate SMAC commands and compute the result
 */
public class SMACmodel extends Observable {


	/**
	 * evaluates the input and notify
	 * the view with the result (a String)
	 */
   	public void eval(String input) {
   		////// CHANGE THIS PART //////
   		String r;
   		
   		if(LogManage.getLoggedStatus()) {
   			r = ">> " + input + "\n";
   		}
   		else {
   			r = "> " + input + "\n";
   		}
   		
   		try {
   			Evaluator evaluator = new Evaluator(input);
   	   		r += evaluator.getOutPut();
   		}
   		catch(Exception e) {
   			r += e.getMessage();
//   			e.printStackTrace();
   		}
   		finally {
   			if(LastManage.getState()) {
   				LastManage.changeState();
   			}
   			
   			if(LogManage.getLoggedStatus()) {
   				if(LogManage.getWriteInLogStatus()) {
   					try {
   						LogManage.writeLog(r);
   					}
   					catch(Exception e) {
   						r += "\nlog writing failed";
   					}
   				}
   				else {
   					LogManage.changeWriteInLog();
   				}
   			}
   			else if(LogManage.getWriteInLogStatus()) {
   				LogManage.changeWriteInLog();
   			}
   		}
   		
   		/*
   		try {

			Tokenizer t = new Tokenizer(input);
			while ( t.hasNextToken() ) {
				r += t.peekNextToken() + " ";
				t.readNextToken();
			}

   		}
   		catch ( Exception e ) {
   			r += "ERROR: " + e.getMessage();
   		}
   		*/
   		//////////////////////////////
   		// this notify the view and send the result
   		setChanged();
		notifyObservers(r);
   	}
}
