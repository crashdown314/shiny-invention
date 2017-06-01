package dailyProgrammer.Challenge291;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class RPNCalculator {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		List<String> opperations = Arrays.asList("+","-","*","/","//","%","^","!");
		Stack<Double> stack = new Stack<Double>();
		int i = 0;
		for (String s : args){
			String opp = "?";
			try{
				stack.push(Double.parseDouble(s));
				opp = "NUM";
			}catch (NumberFormatException e){
				s = s.replace('x', '*');
				if (opperations.contains(s)){
					opp = " "+s+" ";
					double a = stack.pop();
					double b = stack.pop();
					switch (s) {
					case "+":
						stack.push(b+a);
						break;
					case "-":
						stack.push(b-a);
						break;
					case "*":
						stack.push(b*a);
						break;
					case "/":
						stack.push(b/a);
						break;
					case "//":
						stack.push((double) (((int) b)/((int) a)));
						break;
					case "%":
						stack.push(b%a);
						break;
					case "^":
						stack.push(Math.pow(b, a));
						break;
					case "!":
						stack.push(b);
						stack.push((double) factorial((int) a));
						break;
					default:
						break;
					}
				}
			}
			System.out.printf("%d : %s | %s\n",i++,opp,stack);
		}
		if (stack.size()!=1){
			System.out.println("?");
		}else{
			double ans = stack.pop();
			double d = ((int) ans)-ans;
			boolean integer = d>-0.000001 && d<0.000001;
			if (integer)
				System.out.printf("%d\n",(int) ans);
			else
				System.out.println(ans);
		}
		
		

	}
	
	static int factorial(int a){
		if (a==0) return 1;
		return a*factorial(a-1); 
	}

}
