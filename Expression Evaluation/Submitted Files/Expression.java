package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is created 
	 * and stored, even if it appears more than once in the expression.
	 * At this time, values for all variables and all array items are set to
	 * zero - they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr The expression
	 * @param vars The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/** DO NOT create new vars and arrays - they are already created before being sent in
		 ** to this method - you just need to fill them in.
		 **/
		String variableName="";

		for(int i=0; i<expr.length();i++) {

			if(expr.charAt(i)>='a' && expr.charAt(i)<='z'|| (expr.charAt(i)>='A'&&expr.charAt(i)<='Z')){
				variableName += expr.charAt(i);   
			} else if (!variableName.equals("")){
				if (expr.charAt(i) == '[') {
					Array arr= new Array(variableName);
					int arri = arrays.indexOf(arr);
					if (arri == -1) {
						arrays.add(arr);
					}
				} else {
					Variable v =  new Variable(variableName);

					int vari = vars.indexOf(v);
					if (vari == -1) {
						vars.add(v);
					}
				}
				variableName = "";
			}

		}

		//variableName -- c

		if (!variableName.equals("")) {
			Variable v =  new Variable(variableName);
			int vari = vars.indexOf(v);
			if (vari == -1) {
				vars.add(v);
			}
		}
	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input 
	 * @param vars The variables array list, previously populated by makeVariableLists
	 * @param arrays The arrays array list - previously populated by makeVariableLists
	 */
	public static void 
	loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok," (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;              
				}
			}
		}

	}

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars The variables array list, with values for all variables in the expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	public static float 
	evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		// following line just a placeholder for compilation
		Stack<Character> operator = new Stack<>();
		Stack<Float> operand = new Stack<>();
		Stack<String> array_name = new Stack<>();
		operator.push('(');

		for(int i = 0; i < expr.length(); i++)
		{

			if (expr.charAt(i) == ' ')                
				continue;    

			if (expr.charAt(i) >= '0' && expr.charAt(i) <= '9'){                
				StringBuffer sbuf = new StringBuffer();          
				sbuf.append(expr.charAt(i));
				while (i < expr.length()-1 && expr.charAt(i+1) >= '0' && expr.charAt(i+1) <= '9')                    
				{
					sbuf.append(expr.charAt(i+1));
					i+=1;
				}

				operand.push(Float.parseFloat(sbuf.toString()));
			}
			else if((expr.charAt(i) >= 'a' && expr.charAt(i) <= 'z') || (expr.charAt(i) >= 'A' && expr.charAt(i) <= 'Z'))
			{
				StringBuffer sbuf = new StringBuffer();
				sbuf.append(expr.charAt(i));
				while (i < expr.length()-1 && ((expr.charAt(i+1) >= 'a' && expr.charAt(i+1) <= 'z') || (expr.charAt(i+1) >= 'A' && expr.charAt(i+1) <= 'Z')))
				{
					sbuf.append(expr.charAt(i+1));
					i+=1;
				}



				if(i < expr.length()-1 && expr.charAt(i+1)=='[')
				{

					array_name.push(sbuf.toString());

				}

				else{
					Variable aa = new Variable(sbuf.toString());
					int fetch_variable_index = vars.indexOf(aa);
					float value  = vars.get(fetch_variable_index).value;
					operand.push(value);
				}
			}
			else if (expr.charAt(i) == '('){
				operator.push(expr.charAt(i));
			}


			else if (expr.charAt(i) == '['){
				operator.push(expr.charAt(i));
			}



			else if (expr.charAt(i) == '+' || expr.charAt(i) == '-' || expr.charAt(i) == '*' || expr.charAt(i) == '/')
			{                
				if(!operator.isEmpty() && prec(expr.charAt(i)) < prec(operator.peek()))
				{
					float temp = apply(operand,operator);
					operand.push(temp);
					operator.push(expr.charAt(i));
				}
				else
				{
					operator.push(expr.charAt(i));
				}
			}  


			else if (expr.charAt(i) == ')'){  
				while (operator.peek() != '(') {
					float val = apply(operand,operator);
					operand.push(val);  
				}

				operator.pop();            
			}
			else if(expr.charAt(i) == ']')
			{
				while (operator.peek() != '[') {

					float val = apply(operand,operator);
					operand.push(val);  

				}
				operator.pop();

				String arr_name = array_name.pop();
				int arr_index =Math.round(operand.pop());

				Array aa = new Array(arr_name);
				int ind = arrays.indexOf(aa);
				int temp[] = arrays.get(ind).values;
				operand.push((float)temp[arr_index]);

			}



		}

		int size = operand.size();
		while (operator.peek() != '(') {
			if(operand.size()>1)
			{
				float res = apply(operand, operator);
				operand.push(res);
			}
		}

		return operand.peek();

	}


	private static float apply(Stack<Float> operand, Stack<Character> operator) {
		// TODO Auto-generated method stub
		float result = 0;
		char operatorin = operator.pop();
		float b = operand.pop();
		float a = operand.pop();
		if(operatorin == '+' && operator.peek() == '-')
		{
			float temp = a;
			a = b;
			b = temp;
			operatorin = '-';
		}
		else if(operatorin == '-' && operator.peek() == '-')
		{

			operatorin = '+';
		}
		else
		{

		}

		switch (operatorin){        
		case '+':
			result = a + b;
			break;
		case '-':
			result = a - b;
			break;
		case '*':
			result = a * b;
			break;
		case '/':
			if (b == 0)throw new UnsupportedOperationException("Cannot divide by zero");
			result = a / b;
			break;
		}
		if(result>0 && operator.peek()=='-' && operatorin=='-')
		{
			operator.pop();
			operator.push('+');
		}

		else if(result>0 && operator.peek()=='-' && operatorin=='-')
		{

		}
		else if(result < 0 && operator.peek()=='-')
		{
			operator.pop();
			operator.push('+');
		}

		return result;


	}


	public static int prec(char op){
		int temp = 0;
		if (op == '*' || op == '/' || op == '%')
			temp = 2;
		else  if (op == '+' || op == '-')
			temp = 1;
		return temp;
	}
}