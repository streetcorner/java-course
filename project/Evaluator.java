package project;

import java.io.FileNotFoundException;
import java.text.*;
import java.util.*;

public class Evaluator {
	
	private Tokenizer tokenizer;
	private String outPut = "";
	
	public Evaluator(String input) throws TokenException, LexicalErrorException, GeneralErrorException, FileNotFoundException {
		tokenizer = new Tokenizer(input);
		classify();
	}
	
	public void classify() throws TokenException, LexicalErrorException, GeneralErrorException, FileNotFoundException  {
		if(tokenizer.hasNextToken()) {
			Token token = tokenizer.peekNextToken();
			
			if(isMathematicalExpression(token)) {
				LastManage.changeState();
				processMathematicalExpression();
			}
			else if(isCommand(token)) {
				processCommand();
			}
			else {
				token = tokenizer.readNextToken();
				if(token.isIdentifier()) {
					throw new GeneralErrorException(token.getIdentifier() + " is not a variable");
				}
				else throw new LexicalErrorException("syntax error");
			}
		}	
	}
	
	public static boolean isMathematicalExpression(Token token) throws TokenException {
		return token.isNumber() || (token.isDelimiter() && token.getDelimiter().equals("("))
				|| token.isOperator() && token.getOperator().equals("~")
				|| token.isIdentifier() && (token.getIdentifier().equals("last")
				|| token.getIdentifier().equals("sin")
				|| token.getIdentifier().equals("cos")
				|| token.getIdentifier().equals("tan")
				|| Variable.contains(token.getIdentifier()));
	}
	
	public boolean isCommand(Token token) throws TokenException {
		return token.isIdentifier() && KeyWord.contains(token.getIdentifier());
	}
	
	public void processMathematicalExpression() throws TokenException, LexicalErrorException, GeneralErrorException {
		MathematicalEvaluator mathe = new MathematicalEvaluator(tokenizer);
		mathe.processExpression();
		
		double result = mathe.getResult();
		outPut += new DecimalFormat(PrecisionManage.getPrecisionFormat()).format(result);
	}
	
	public void processCommand() throws TokenException, LexicalErrorException, GeneralErrorException, FileNotFoundException {
		Token token = tokenizer.readNextToken();
		
		if(token.getIdentifier().equals("setprecision")){  //set precision
			
			if(!tokenizer.hasNextToken()) {
				outPut += "current precision is " + PrecisionManage.getPrecision();
			}
			else {
				token = tokenizer.readNextToken();
				if(!token.isNumber() || token.getNumber() % 1 != 0 || tokenizer.hasNextToken()) throw new LexicalErrorException("syntax error");
				
				PrecisionManage.setPrecision((int)token.getNumber());
				outPut += "precision set to " + PrecisionManage.getPrecision();
			}
		}
		else if(token.getIdentifier().equals("let")) {
			outPut += Variable.let(tokenizer);
		}
		else if(token.getIdentifier().equals("reset")) {
			outPut += Variable.reset(tokenizer);
		}
		else if(token.getIdentifier().equals("save")) {
			outPut += Variable.save(tokenizer);
		}
		else if(token.getIdentifier().equals("load")) {
			outPut += Variable.load(tokenizer);
		}
		else if(token.getIdentifier().equals("saved")) {
			outPut += Variable.saved(tokenizer);
		}
		else if(token.getIdentifier().equals("log")) {
			outPut += LogManage.log(tokenizer);
		}
		else {                                      //logged
			outPut += LogManage.logged(tokenizer);
		}
	}
	
	public String getOutPut() {
		return outPut;
	}
}
