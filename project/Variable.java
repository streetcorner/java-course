package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;  
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;  

public class Variable {

	private static Map<String, Double> variable = new HashMap<String, Double>();
	private static String saved_folder = "saved";
	private static String saved_path = "saved/";
	private static String suffix = ".txt";
	
	public Variable() {
		
	}
	
	public static boolean contains(String key) {
		return variable.containsKey(key);
	}
	
	public static double getValue(String key) {
		return variable.get(key);
	}
	
	public static String let(Tokenizer tokenizer) throws TokenException, LexicalErrorException, GeneralErrorException {
		String outPut = "";
		if(!tokenizer.hasNextToken()) {
			if(variable.isEmpty()) {
				outPut += "no variable defined";
			}
			else {
				Iterator<Entry<String, Double>> it = variable.entrySet().iterator();  
				while(it.hasNext()) {
					Entry<String, Double> itEntry = it.next();

					outPut += itEntry.getKey() + " = "
							+ new DecimalFormat(PrecisionManage.getPrecisionFormat()).format(itEntry.getValue());
					if(it.hasNext()) {
						outPut += "\n";
					}
				}
//				for(Map.Entry<String, Double> entry : variable.entrySet()) {  
//					outPut += entry.getKey() + " = " + new DecimalFormat(PrecisionManage.getPrecisionFormat()).format(entry.getValue())+ "\n";
//				}
			}
		}
		else {
			Token token = tokenizer.readNextToken();
			if(token.isIdentifier()&&!KeyWord.contains(token.getIdentifier())){
				if(tokenizer.hasNextToken()&&tokenizer.readNextToken().isEqual()){
					if(tokenizer.hasNextToken() && Evaluator.isMathematicalExpression(tokenizer.peekNextToken())){
						MathematicalEvaluator exp = new MathematicalEvaluator(tokenizer);
						exp.processExpression();
						
						double result = exp.getResult();
						variable.put(token.getIdentifier(), result);
						
						outPut += token.getIdentifier() + " = "
								+ new DecimalFormat(PrecisionManage.getPrecisionFormat()).format(result);
					}
					else throw new LexicalErrorException("syntax error");
				}
				else throw new LexicalErrorException("syntax error");
			}
			else throw new GeneralErrorException("变量名不合法");
		}
		return outPut;
	}
	
	public static String reset(Tokenizer tokenizer) throws TokenException, LexicalErrorException, GeneralErrorException{
		String outPut = "";
		if(!tokenizer.hasNextToken()) {
			if(!variable.isEmpty()){
				Iterator<Entry<String, Double>> it = variable.entrySet().iterator();  
				while(it.hasNext()) {
					Entry<String, Double> itEntry = it.next();
					it.remove();
					outPut += itEntry.getKey() + " has been reset";
					if(it.hasNext()) {
						outPut += "\n";
					}
				}					
			}
			else throw new GeneralErrorException("no variable");
		}
		else {
			while(tokenizer.hasNextToken()){
				Token token = tokenizer.readNextToken();
				if(token.isIdentifier()){
					if(variable.containsKey(token.getIdentifier())){
						variable.remove(token.getIdentifier());
						outPut += token.getIdentifier() + " has been reset";
					}
					else throw new LexicalErrorException(outPut + token.getIdentifier() + " is not a variable");
				}	
				else throw new LexicalErrorException("syntax error");
			}
		}
		return outPut;
	}
	
	public static String save(Tokenizer tokenizer) throws TokenException, LexicalErrorException, GeneralErrorException, FileNotFoundException {
		if(!tokenizer.hasNextToken()) throw new LexicalErrorException("syntax error");
		
		String file_name;
		Token token = tokenizer.readNextToken();
		
		if(token.isString()) {
			file_name = token.getString();
			File file = new File(saved_path + file_name + suffix);  
			
			try {
				if(!file.exists()) {
					file.createNewFile();
				}
	        }
			catch (IOException e) {  
	            throw new GeneralErrorException("文件创建失败");
	        } 
			
			if(!tokenizer.hasNextToken()) {
				if(variable.isEmpty()) {
					return "no variable defined";
				}
				else {
					try {
						FileOutputStream out = new FileOutputStream(file);
						Iterator<Entry<String, Double>> it = variable.entrySet().iterator();
						
						while(it.hasNext()) {
							Entry<String, Double> itEntry = it.next();
							String buffer = "let "+ itEntry.getKey() + " = "
									+ new DecimalFormat(PrecisionManage.getPrecisionFormat()).format(itEntry.getValue()) + "\n";
							byte bt[] = new byte[1024];
							bt = buffer.getBytes();
							
							out.write(bt, 0, bt.length);
						}
						
						out.close();
					}
					catch (FileNotFoundException e) {
						throw new GeneralErrorException("待写文件不存在");
					}
					catch (IOException e) {
						throw new GeneralErrorException("写入文件失败");
					}
				}
			}
			else {
				try {
					FileOutputStream out = new FileOutputStream(file);
					
					while(tokenizer.hasNextToken()) {
						token = tokenizer.readNextToken();
						
						if(token.isIdentifier()) {
							if(variable.containsKey(token.getIdentifier())) {
								String buffer = "let "+ token.getIdentifier() + " = "
										+ new DecimalFormat(PrecisionManage.getPrecisionFormat()).format(variable.get(token.getIdentifier())) + "\n";
								byte bt[] = new byte[1024];
								bt = buffer.getBytes();
								
								out.write(bt, 0, bt.length);
							}
							else {
								out.close();
								throw new GeneralErrorException(token.getIdentifier() + " is not a variable");
							}
						}
						else {
							out.close();
							throw new LexicalErrorException("syntax error");
						}
					}
					
					out.close();
				}
				catch (FileNotFoundException e) {
					throw new GeneralErrorException("待写文件不存在");
				}
				catch (IOException e) {
					throw new GeneralErrorException("写入文件失败");
				}
			}
		}
		else throw new LexicalErrorException("syntax error");
		
		return "variables saved in " + file_name;	
	}
	
	public static String load(Tokenizer tokenizer) throws TokenException, LexicalErrorException, GeneralErrorException {
		String file_name;
		
		if(tokenizer.hasNextToken()) {
			Token token = tokenizer.readNextToken();
			
			if(tokenizer.hasNextToken() || !token.isString()) throw new LexicalErrorException("syntax error");
			
			file_name = token.getString();
			File file = new File(saved_path + file_name + suffix);
			
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String input;
				
				while((input = reader.readLine()) != null) {
					new Evaluator(input);
				}

				reader.close();
			}
			catch (FileNotFoundException e) {
				throw new GeneralErrorException("待读文件不存在");
			}
			catch (IOException e) {
				throw new GeneralErrorException("读取文件失败");
			}
		}
		else throw new LexicalErrorException("syntax error");
		return file_name + " loaded";
	}
	
	public static String saved(Tokenizer tokenizer) throws LexicalErrorException {
		if(tokenizer.hasNextToken()) throw new LexicalErrorException("syntax error");
		
		String outPut = "";

		File dir = new File(saved_folder);
		String[] file_list = dir.list();
		
		if(file_list.length == 0) return "no saved files";
		
		for(int i = 0; i < file_list.length - 1; ++i) {
			outPut += file_list[i].substring(0, file_list[i].length() - 4) + "\n";
		}
		outPut += file_list[file_list.length - 1].substring(0, file_list[file_list.length - 1].length() - 4);

		return outPut;
	}
}
