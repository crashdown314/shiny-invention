package dailyProgrammer.Challenge298.easy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TooManyParenthesis {

	private static final String USAGE = "USAGE: TooManyParenthesis input";
	public static void main(String[] args) {
		args = new String[] {"(((zbcd))(ab))"};
		if (args.length<1){
			System.out.println(USAGE);
			System.exit(1);
		}
		
		TooManyParenthesis tmp = new TooManyParenthesis(args[0]);
		System.out.println(tmp.solve());
	}
	
	private String string;
	public TooManyParenthesis(String input){
		this.string = input;
	}
	
	public String solve(){
		String ans = string.replace("()","");
		if (ans.equals("")) ans="NULL";
		
		ans = nestet(ans,"");
		
		return ans;
	}
	private String nestet(String toCheck,String prefix){
		int start = -1;
		int end = -1;
		int brackets = 0;
		boolean found = false;
		for (int i = 0;i<toCheck.length();i++){
			char c = toCheck.charAt(i);
			if (c=='('){
				if (brackets==0)
					start = i;
				brackets++;
				found=true;
				
			}else if (c == ')')
				brackets--;
		
			if (found && brackets==0){
				end = i;
				break;
			}
		}
		if (!found){
			return prefix + toCheck;
		}else{
			String output = prefix + toCheck.substring(start,end+1);
			if (end!=toCheck.length()-1){
				output += '\n' + prefix + nestet(toCheck.substring(end+1),prefix+"  ");;
			}
			
			output+='\n' + nestet(toCheck.substring(start+1,end),prefix+"  ");
			return output;
		}
		
	}
}

