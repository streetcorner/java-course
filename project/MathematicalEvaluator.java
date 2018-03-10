package project;

import java.util.*;

public class MathematicalEvaluator {

	private Tokenizer tokenizer;
	private Stack<Double> value;
	private Stack<FunOp> opeator;
	private double result;
	
	public MathematicalEvaluator(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
		value = new Stack<Double>();
		opeator = new Stack<FunOp>();
	}
	
	public void processExpression() throws TokenException, LexicalErrorException, GeneralErrorException {
		while(tokenizer.hasNextToken()) {
			Token token = tokenizer.readNextToken();
			FunOp thisOp;

			if(token.isNumber()) {
				value.push(token.getNumber());
			}
			else if(token.isIdentifier()) {
				if(token.getIdentifier().equals("last")) {
					value.push(LastManage.getLastValue());
				}
				else if(token.getIdentifier().equals("sin") || token.getIdentifier().equals("cos")
						|| token.getIdentifier().equals("tan")) {
					thisOp = new FunOp(token.getIdentifier());
					while(!isPushOp(thisOp)) {
						compute(opeator.pop());
					}
					opeator.push(thisOp);
				}
				else if(Variable.contains(token.getIdentifier())) {
					value.push(Variable.getValue(token.getIdentifier()));
				}
				else throw new GeneralErrorException(token.getIdentifier() + " is not a variable");
			}
			else if(token.isDelimiter() && token.getDelimiter().equals("(")) {  //压入左括号
				thisOp = new FunOp(token.getDelimiter());
				opeator.push(thisOp);
			}
			else if(token.isDelimiter() && token.getDelimiter().equals(")")) {  //出现右括号，计算
				thisOp = new FunOp(token.getDelimiter());
				compute(thisOp);
			}
			else if(token.isOperator()) {    //出现运算符，判断压入还是计算
				thisOp = new FunOp(token.getOperator());
				while(!isPushOp(thisOp)) {
					compute(opeator.pop());
				}
				opeator.push(thisOp);
			}
//			else if(token.isIdentifier() && (token.getIdentifier().equals("sin") || token.getIdentifier().equals("cos") || token.getIdentifier().equals("tan"))){
//				thisOp = new FunOp(token.getIdentifier());
//				while(!isPushOp(thisOp)) {
//					compute(opeator.pop());
//				}
//				opeator.push(thisOp);
//			}
			else throw new LexicalErrorException("syntax error");
		}
		
		
		//处理最后运算符栈中的运算符
		while(!opeator.isEmpty()) {
			compute(opeator.pop());
		}
		
		if(value.peek() == null) throw new GeneralErrorException("last is null");
			
		result = value.pop();
		
		if(!value.isEmpty()) throw new LexicalErrorException("syntax error: 连续两个数字");
		
		if(LastManage.getState()) {
			LastManage.setLastValue(result);
		}
	}
	
	public boolean isPushOp(FunOp thisOp) {
		return opeator.isEmpty() || opeator.peek().getPrority() < thisOp.getPrority() || thisOp.getName().equals("~")
				||thisOp.getName().equals("sin")||thisOp.getName().equals("cos")||thisOp.getName().equals("tan");
	}
	
	public void pushValue() {
		
	}
	
	public void compute(FunOp thisOp) throws LexicalErrorException,GeneralErrorException {
		double num[] = new double[2];
		
		for(int i= 0; i < thisOp.getArity(); ++i) {
			if(value.isEmpty()) throw new LexicalErrorException("syntax error: 连续两个运算符");
			if(value.peek() == null) throw new GeneralErrorException("last is null");
			num[i] = value.pop();
		}
		
		switch(thisOp.getName()) {
		case "+": value.push(num[1] + num[0]);
				  break;
		case "-": 
		case "~": value.push(num[1] - num[0]);
		  		  break;
		case "*": value.push(num[1] * num[0]);
		  		  break;
		case "/": if(num[0] == 0) throw new GeneralErrorException("division by zero");
		  		  value.push(num[1] / num[0]);
		  		  break;
		case "^": value.push(Math.pow(num[1], num[0]));
				  break;
		case "sin":value.push(Math.sin(Math.toRadians(num[0])));
				  break;
		case "cos":value.push(Math.cos(Math.toRadians(num[0])));
				  break;
		case "tan":value.push(Math.tan(Math.toRadians(num[0])));
				  break;
		case ")": //if(opeator.isEmpty()) throw new LexicalErrorException("Delimiter mismatch");
				  //是否判断括号不匹配问题？只出现右括号默认最前面是左括号？只有左括号在微软计算器可用
				  while(!opeator.isEmpty() && !opeator.peek().getName().equals("(")) {
						compute(opeator.pop());	
				  }
		//其他运算
		default ://抛出错误？
		}
	}
	
	public double getResult() {
		return result;
	}
}
