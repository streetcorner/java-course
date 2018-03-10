package project;

public class FunOp {

	private String name;
	private int arity;
	private int prority;
	
	public FunOp(String name) throws TokenException {
		this.name = name;
		switch(name) {  
		case "(": 
		case ")": arity = 0;
				  prority = 0;
				  break;
		case "+":
		case "-": arity = 2;
				  prority = 2;
				  break;
		case "*":
		case "/": arity = 2;
				  prority = 4;
				  break;
		case "~": 
		case "sin":
		case "cos":
		case "tan":
				  arity = 1;
		  	  	  prority = 6;
		  	  	  break;
		case "^": arity = 2;
		  		  prority = 8;
		  		  break;
		default: throw new TokenException("unknown"); //ÐèÒªÅ×³ö´íÎó£¿
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getArity() {
		return arity;
	}
	
	public int getPrority() {
		return prority;
	}
}
