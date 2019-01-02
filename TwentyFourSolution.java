import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwentyFourSolution
{
	private final static double[] four = {8, 4, -1, 1};//{4.0+Math.pow(5.0, 0.25), 8.0+Math.sqrt(5), 69+16*Math.sqrt(5),4.0-Math.pow(5, 0.25)};//{9.0/5,102.0/5,2.0/5,8.0/5};//{19.0, 31.0/3.0, 7.0/5.0, 2.0/3.0};   
	
	//-----------------------------------------------------------------------------------------------------------------------
	
	public static void main(String[] args)
	{
		String[] operations = {"+","+","+"};
		allDigitPermutations(four, operations, 0);
		System.out.println("There is no solution.");
	}
	
	public static void allDigitPermutations(double [] board, String[] operations, int current)
	{
		for(int i = current; i < board.length; i++)
		{
			double[] copy = Arrays.copyOf(board, board.length);
			swap(copy, i, current);
			allOperationPermutations(copy, operations, 0);
			allDigitPermutations(copy, operations, current+1);
			
		}
	}
	
	public static void allOperationPermutations(double[] board, String[] operations, int current)
	{
		String equation = "" + board[0]+operations[0]+board[1]+operations[1]+board[2]+operations[2]+board[3];
		
		complete(equation);
		
		//Puts parenthesis around numbers doing addition and subtraction so they can be computed before division and multiplication. 
		if(current == 3)
		{
			String operationsStr = operations[0] + operations[1] + operations [2];
			if(!operationsStr.matches("(\\+|-)(\\+|-)(\\+|-)"))
			{
				String regex = "(\\+|-)(\\+|-)";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(operationsStr);
				if(m.find())
				{
					int start = m.start(0);
					String part = "(" + board[start] + operations[start] + board[start+1] + operations[start+1] + board[start+2] + ")";
					if(start == 0)
						equation = part + operations[2] + board[3];
					else
						equation = board[0] + operations[0] + part;
					complete(equation);
				}
				else
				{
					regex = "(?:(?<!\\d)-)?\\d+(?:\\.\\d+)?(?:E-?\\d+)?(?:\\+|-)(?:(?<!\\d)-)?\\d+(?:\\.\\d+)?(?:E-?\\d+)?";
					p = Pattern.compile(regex);
					m = p.matcher(equation);
					while(m.find()) 
					{
						String equation1 = equation.substring(0, m.start()) + "(" + m.group(0) + ")" + equation.substring(m.end());
						complete(equation1);
						
						//for (1+2)*(3+4) 
						Matcher parenBoth = p.matcher(equation1);
						if(parenBoth.find(m.end()))
						{
							String equation2 = equation1.substring(0, parenBoth.start()) + "(" + parenBoth.group(0) + ")" + equation1.substring(parenBoth.end());							
							complete(equation2);
						}
					}
				}
			}
			return;
			
		}
		
		allOperationPermutations(Arrays.copyOf(board, board.length), set(operations, current, "-"), current+1);
		allOperationPermutations(Arrays.copyOf(board, board.length), set(operations, current, "*"), current+1);
		allOperationPermutations(Arrays.copyOf(board, board.length), set(operations, current, "/"), current+1);
		allOperationPermutations(Arrays.copyOf(board, board.length), set(operations, current, "+"), current+1);	
	}
	
	public static void swap(double[] board, int a, int b)
	{
		double save = board[a];
		board[a] = board[b];
		board[b] = save;
	}
	
	public static String[] set(String[] a, int index, String value)
	{
		String[] b = new String[3];
		for(int i = 0; i<a.length; i++)
		{
			if(i == index)
				b[i] = value;
			else
				b[i] = a[i];
		}
		return b;
	}
	
	public static void complete(String equation)
	{
		if(Pemdas.calculate(equation) == 24)
		{
			System.out.println("Solution: " + equation);
			System.exit(0);
		}
	}

}





