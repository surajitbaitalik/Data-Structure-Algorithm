/*
 *Long Project LP1: Integer arithmetic with arbitrarily large numbers
 * @author Surajit Baitalik (sxb180026)
 * @author Swathy Priya SathishBarani (sxs175832)
 * @author Vineet Amonkar (vva180000)
 * @author Nikhil Kalekar (nlk180002)
 */
package sxb180026;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;




public class Num implements Comparable<Num> {
	static long defaultBase = 10; // Change as needed
	long base = 1000000000; // Change as needed
	
	long[] arr; // array to store arbitrarily large integers
	boolean isNegative; // boolean flag to represent negative numbers
	int len; // actual number of elements of array that are used; number is stored in
				// arr[0..len-1]
	// It will provide the no of digits each cell contains
	int cellDigits = (int) Math.log10(base());

	/**
	 * Default Constructor
	 */
	public Num() {
		this.len = 0;

	}

	/**
	 * Constructor which takes String as input parameter and create object of Num
	 * 
	 * @param s
	 * @throws NumberFormatException
	 */
	public Num(String s) throws NumberFormatException {

		s = s.trim();
		if (!validateNum(s) || s.isEmpty()) {
			throw new NumberFormatException("Please enter a Valid Number String\n");
		}
		if (s.startsWith("-")) {
			this.isNegative = true;
			s = s.substring(1);
		}
		// the extracted number String which we will use for all operations
		s = trimLeadingZeros(s);
		this.len = (int) Math.ceil((double) s.length() / cellDigits);

		// now we have to create the long array
		this.arr = new long[len];
		// we have to populate each cell of the arr with cell digits,
		// we will reverse the String and store chunks of cellDigits into arr
		StringBuilder sb = new StringBuilder(s);
		s = sb.reverse().toString();
		String[] strChunks = createChunks(s, cellDigits);
		int i = 0;
		for (String st : strChunks) {
			StringBuilder sbt = new StringBuilder(st);
			this.arr[i++] = Long.parseLong(sbt.reverse().toString());

		}
	}

	/**
	 * Constructor which takes long as input parameter and create Object
	 * 
	 * @param x
	 */
	public Num(long x) {
		this();
		int sLength = String.valueOf(x).length();
		len = (int) Math.ceil((double) sLength / cellDigits);
		arr = new long[len];
		int i = 0;
		if (x == 0) {
			this.arr[i] = 0;
			i++;
		}
		if (x < 0) {
			isNegative = true;
			x *= -1;
		}
		while (x > 0) {
			this.arr[i] = x % base;
			x = x / base;
			i++;
		}
		len = i;
	}

	/**
	 * @param a
	 * @param b
	 * @return the result after the sum of two numbers
	 */
	public static Num add(Num a, Num b) {

		Num out = null;
		// For (a+b) & (-)a+(-)b
		if (a.isNegative == b.isNegative) {
			out = unsignedAdd(a, b);
			out.isNegative = a.isNegative;

		}
		// for a+(-)b
		else if (a.compareTo(b) > 0) {
			out = normalSubtract(a, b);
			out.isNegative = a.isNegative;
			// for -b+a
		} else {
			out = normalSubtract(b, a);
			out.isNegative = b.isNegative;
		}

		return out;
	}

	/**
	 * @param a
	 * @param b
	 * @return the difference of two numbers
	 */
	public static Num subtract(Num a, Num b) {
		Num out = null;
		// for (-a)-(b) && a-(-b)
		if (a.isNegative != b.isNegative) {
			out = unsignedAdd(a, b);
			out.isNegative = a.isNegative;
		}
		// for -a-(-b) && a-b
		else if (a.compareTo(b) > 0) {
			out = normalSubtract(a, b);
			out.isNegative = a.isNegative;
		} else {
			// for -b-(-a)
			out = normalSubtract(b, a);
			out.isNegative = b.isNegative;
		}

		return out;
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public static Num product(Num a, Num b) {
		Num out = null;
		// unsigned product
		out = normalProduct(a, b);
		out.isNegative = a.isNegative ^ b.isNegative;
		return out;
	}

	/**
	 * Use divide and conquer
	 * 
	 * @param a
	 * @param n
	 * @return
	 */
	public static Num power(Num a, long n) {
		if (n == 0)
			return new Num(1);
		if (n == 1)
			return a;
		Num pow = power(a, n / 2);
		if (n % 2 == 0)
			return Num.product(pow, pow);
		else
			return Num.product(a, Num.product(pow, pow));
	}

	/**
	 * Use binary search to calculate a/b
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Num divide(Num x, Num y) throws ArithmeticException {

		Num out = new Num();
		Num zero = new Num(0);
		Num one = new Num(1);
		Num minusOne = new Num(-1);
		Num two = new Num(2);

		// Division by ZERO
		if (y.compareTo(zero) == 0)
			throw new ArithmeticException("Division by ZERO.");

		// Numerator is ZERO
		if (x.compareTo(zero) == 0)
			return zero;

		// Denominator is one/ minusOne
		if (y.compareTo(one) == 0) {
			out.isNegative = x.isNegative ^ y.isNegative;
			out.len = x.len;
			out.arr = new long[out.len];
			int i = 0;
			for (long e : x.arr) {
				out.arr[i++] = e;
			}
			return out;
		}

		// When division is by 2
		if (y.compareTo(two) == 0) {
			out.isNegative = (y.isNegative) ? !x.isNegative : x.isNegative;
			out = x.by2();
			return out;
		}

		// |x| < |y|, causing division either ZERO or minusOne
		if (x.compareTo(y) < 0) {
			if (x.isNegative == y.isNegative)
				return zero;
			else
				return minusOne;
		}

		// |x| == |y|
		if (x.compareTo(y) == 0) {
			if (x.isNegative == y.isNegative)
				return one;
			else
				return minusOne;
		}

		// Binary Search begins
		Num low = new Num(0);
		Num high = new Num();

		// Copying a to high
		
		high.len = x.len;
		high.arr = new long[high.len];

		int i = 0;
		for (long e : x.arr) {
			high.arr[i++] = e;
		}

		
		Num mid = new Num();

		while (low.compareTo(high) < 0) {

			Num sum = unsignedAdd(low, high);
			mid = sum.by2();

			if (mid.compareTo(low) == 0)
				break; // When mid = low

			Num prod = normalProduct(mid, y);

			if (prod.compareTo(x) == 0)
				break; // When mid*y = x

			else if (prod.compareTo(x) > 0)
				high = mid;
			else
				low = mid;
		}

		// When x, y has different signs
		if (x.isNegative != y.isNegative)
			mid.isNegative = true;

		return mid;

	}

	// return a%b
	public static Num mod(Num a, Num b) {

		if (a.base != b.base) {
			throw new NumberFormatException("Same Base is required");
		}

		Num one = new Num(1);
		Num zero = new Num(0);

		if (b.compareTo(zero) == 0)
			throw new ArithmeticException("Division by ZERO.");
		if (b.compareTo(one) == 0)
			return zero;
		if (a.compareTo(b) == 0)
			return zero;

		Num quotient = divide(a, b);
		Num prod = product(quotient, b);
		Num remainder = subtract(a, prod);

		return remainder;
	}

	// Use binary search
	public static Num squareRoot(Num a) {

		Num zero = new Num(0);
		Num one = new Num(1);

		if (a.compareTo(zero) < 0)
			return null;

		if (a.compareTo(zero) == 0 || a.compareTo(one) == 0)
			return a;

		Num ans = new Num();
		Num start = zero;
		Num mid = new Num();
		Num end = new Num();
		end.len = a.len;
		end.arr = new long[end.len];
		System.arraycopy(a.arr, 0, end.arr, 0, a.len);

		while (start.compareTo(end) <= 0) {
			Num tempAdd = add(start, end);
			mid = tempAdd.by2();
			if (a.compareTo(product(mid, mid)) == 0)
				return mid;
			if (a.compareTo(product(mid, mid)) == -1)
				end = subtract(mid, one);
			else {
				start = add(mid, one);
				ans = mid;
			}
		}
		return ans;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Num other) {
		// When lengths are unequal
		if (this.len < other.len)
			return -1;

		if (other.len < this.len)
			return 1;

		// If two Num is of same length compare most significant digit
		else {

			int index = this.len - 1;
			while (index > -1) {
				// for this < other
				if (this.arr[index] < other.arr[index])
					return -1;

				// for other < this
				if (other.arr[index] < this.arr[index])
					return 1;

				index--;
			}
		}
		// for equal
		return 0;
	}

	// Output using the format "base: elements of list ..."
	// For example, if base=100, and the number stored corresponds to 10965,
	// then the output is "100: 65 9 1"
	public void printList() {
		System.out.print(base() + ": ");

		if (this.isNegative)
			System.out.print("- ");
		System.out.print("[");
		int i = 0;
		while (i < this.len) {
			if (i == this.len - 1)
				System.out.print(this.arr[i]);
			else
				System.out.print(this.arr[i] + ", ");
			i++;
		}
		System.out.print("]");
		System.out.println();

	}

	
	/* 
	 * Return number to a string in base 10
	 */
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		String numValue = "";

		if (isNegative) {
			numValue = "-";
		}

		for (int i = len - 1; i >= 0; i--) {
			String digit = String.valueOf(arr[i]);
			if (digit.length() < cellDigits) {
				// digit = "0".repeat(cellDigits - digit.length()) + digit;
				digit = new String(new char[cellDigits - digit.length()]).replace("\0", "0") + digit;
			}
			strBuilder.append(digit);
		}
		numValue = numValue + trimLeadingZeros(strBuilder.toString());

		return numValue;
	}

	public long base() {
		return this.base;
	}

	/**
	 * Change the base to the given base
	 * 
	 * @param newBase
	 * @return NUm with the new base
	 */
	public Num convertBase(int newBase) {
		Num nB = new Num(newBase);
		Num zero = new Num(0);
		Num out = new Num();
		out.isNegative = this.isNegative;
		double l2 = Math.ceil(Math.log10(this.base) / Math.log10(newBase)) * this.len;
		out.len = (int) l2;
		out.arr = new long[out.len];
		int index = 0;

		// Copying this number to tmp
		Num tmp = new Num();
		tmp.len = this.len;
		tmp.arr = new long[this.len];
		for (long l : this.arr)
			tmp.arr[index++] = l;
		index = 0;

		// Computing each digit using mod-divide methods
		while (tmp.compareTo(zero) > 0) {
			Num r = mod(tmp, nB);
			String s = r.toString();
			out.arr[index] = Long.parseLong(s);
			Num q = divide(tmp, nB);
			tmp = q;
			index++;
		}
		out.base = newBase;
		return out;
	}

	/**
	 * Divide by 2, for using in binary search
	 * 
	 * @return half of the Num
	 */
	public Num by2() {
		Num half = new Num();
		long carry = 0;
		Num two = new Num(2);
		Num zero = new Num(0);

		if (this.compareTo(two) < 0)
			return zero;

		// When the most significant digit = 1, the half.len = this.len - 1
		if (this.arr[len - 1] == 1) {
			half.len = this.len - 1;
			carry = 1; // Planning to start from index = half.len - 1 = this.len - 2, WITH a carry :)
		}
		// When most significant digit > 1
		else
			half.len = this.len;

		half.isNegative = this.isNegative;
		half.arr = new long[half.len];

		// Assigning correct digits of half.arr from index = most to least significant
		int index = half.len - 1;

		while (index > -1) {
			long sum = 0; // to store the proper digit to be halved.

			// When there exits a carry
			if (carry == 1) {
				sum = this.arr[index] + base;
			} else {
				sum = this.arr[index];
			}

			half.arr[index] = sum / 2; // the proper halved digit
			carry = (sum % 2 == 1) ? 1 : 0;

			index--;
		}
		return half;
	}

	
	/**
	 * Evaluate an expression in postfix and return resulting number
	 *Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
	 *a number: [1-9][0-9]*. There is no unary minus operator
	 * @param expr
	 * @return
	 */
	public static Num evaluatePostfix(String[] expr) {
		Num result = new Num();
		if (expr.length == 0)
			return null;
		Stack<String> operands = new Stack<>();
		Set<String> operators = new HashSet<>();
		operators.add("*");
		operators.add("/");
		operators.add("+");
		operators.add("-");
		operators.add("%");
		operators.add("^");
		int i = 0;
		String operand1, operand2;
		while (i < expr.length) {
			if (!operators.contains(expr[i]))
				operands.push(expr[i]);
			else {
				operand1 = operands.pop();
				operand2 = operands.pop();
				operands.push(result.commonEval(operand1, operand2, expr[i]).toString());
			}
			i++;
		}
		result = new Num(operands.pop());
		return result;
	}

	/**
	 * Helper method to evaluate postfix
	 * @param operand1
	 * @param operand2
	 * @param operator
	 * @return output Num
	 */
	public Num commonEval(String operand1, String operand2, String operator) {

		Num first = null;
		Num second = null;
		Num result = null;
		long power = 0;
		if (operator.equals("^")) {
			first = new Num(operand2);
			power = new Long(operand1);
		} else {
			first = new Num(operand1);
			second = new Num(operand2);
		}

		switch (operator) {

		case "*":
			result = product(second, first);
			break;
		case "/":
			result = divide(second, first);
			break;
		case "+":
			result = add(second, first);
			break;
		case "-":
			result = subtract(second, first);
			break;
		case "%":
			result = mod(second, first);
			break;
		case "^":
			result = power(first, power);
			break;

		}

		return result;

	}

	
	/**
	 * Parse/evaluate an expression in infix and return resulting number
	 * Input expression is a string, e.g., "(3 + 4) * 5"
	 * Tokenize the string and then input them to parser
	 * Implementing this method correctly earns you an excellence credit
	 * @param expr
	 * @return
	 * @throws Exception
	 */
	public static Num evaluateExp(String expr) throws Exception {
		expr = expr.replaceAll("\\[", "(").replaceAll("\\]", ")").replaceAll("\\s+", "");
		String operators = "()^%*/+-";
		if (!expr.matches("^[0-9()^%*/+-]*$")) {
			throw new Exception("Given expression is not valid: " + expr);
		}
		StringTokenizer tokenizer = new StringTokenizer(expr, operators, true);
		Stack<Num> operandStack = new Stack<Num>();
		Stack<String> operatorsStack = new Stack<String>();
		while (tokenizer.hasMoreTokens()) {
			String ch = tokenizer.nextToken();
			if (ch.equals("("))
				operatorsStack.push(ch);
			else if (ch.equals(")")) {
				while (!operatorsStack.peek().equals("(")) {
					Num temp = calculateExpr(operandStack, operatorsStack);
					operandStack.push(temp);
				}
				operatorsStack.pop();
			} else if (operators.contains(ch)) {
				while (!operatorsStack.isEmpty()
						&& operatorPresidency(ch) < operatorPresidency(operatorsStack.peek())) {
					Num temp = calculateExpr(operandStack, operatorsStack);
					operandStack.push(temp);
				}
				operatorsStack.push(ch);
			} else if (ch.matches("[0-9]+"))
				operandStack.push(new Num(ch));
			else
				throw new Exception("Given expression is not valid: " + expr);
		}

		while (!operatorsStack.isEmpty()) {
			Num temp = calculateExpr(operandStack, operatorsStack);
			operandStack.push(temp);
		}
		if (operandStack.size() != 1) {
			throw new Exception("Given expression is not valid: " + expr);
		}
		return operandStack.pop();
	}

	 /*All utilities are defined below*/

	/**
	 * Helper function for product
	 * 
	 * @param x
	 * @param y
	 * @return product of two Num
	 */
	public static Num normalProduct(Num x, Num y) {

		if (x.base != y.base) {
			throw new NumberFormatException("Same Base is required");
		}

		Num result = new Num();
		long[] product = new long[x.len + y.len]; // Max length can be sum of the lengths

		// easy case
		if ((x.len == 1 && x.arr[0] == 0) || (y.len == 1 && y.arr[0] == 0)) {
			result.len = 1;
			result.arr = new long[result.len];
			result.arr[0] = 0;
			return result;
		}

		long carry = 0L;
		for (int i = 0; i < y.len; i++) {
			carry = 0;
			for (int j = 0; j < x.len; j++) {
				product[i + j] += carry + x.arr[j] * y.arr[i];
				carry = product[i + j] / x.base;
				product[i + j] = product[i + j] % x.base;
			}
			product[i + x.len] = carry;
		}

		// Removing trailing zeros
		result.len = findArrayLength(product);
		result.arr = Arrays.copyOfRange(product, 0, result.len);
		return result;
	}

	/**
	 * 
	 * Helper function for subtract
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Num normalSubtract(Num x, Num y) {
		// to iterate on the result
		int index = 0;
		// to iterate over the x and y
		int pX = 0, pY = 0;
		int rLength = Math.max(x.len, y.len);
		Num result = new Num();
		result.len = rLength;
		result.arr = new long[rLength];
		long borrow = 0L;
		while (pX < x.len || pY < y.len || borrow > 0) {
			long a = pX < x.len ? x.arr[pX] : 0;
			long b = pY < y.len ? y.arr[pY] : 0;
			long diff = a - b - borrow;
			if (diff < 0) {
				result.arr[index] = result.base + diff;
				borrow = 1;
			} else {
				result.arr[index] = diff;
				borrow = 0;
			}
			pX++;
			pY++;
			index++;
		}
		result.len = findArrayLength(result.arr);
		return result;

	}

	/**
	 * Helper function This method will perform unsigned add
	 * 
	 * @param x
	 * @param y
	 * @return the Num result
	 */
	public static Num unsignedAdd(Num x, Num y) {
		// to iterate on the result
		int index = 0;
		// to iterate over the x and y
		int pX = 0, pY = 0;
		int rLength = Math.max(x.len, y.len);
		Num result = new Num();
		result.arr = new long[rLength + 1];
		long carry = 0L;
		while (pX < x.len || pY < y.len || carry > 0) {
			long a = pX < x.len ? x.arr[pX] : 0;
			long b = pY < y.len ? y.arr[pY] : 0;
			long sum = a + b + carry;
			result.arr[index] = sum % result.base;
			carry = sum / result.base;
			pX++;
			pY++;
			index++;
		}

		result.len = findArrayLength(result.arr);
		return result;
	}

	/**
	 * Helper function to get the length of an array
	 * @param arr
	 * @return
	 */
	public static int findArrayLength(long[] arr) {
		int i = arr.length - 1;
		while (i > 0) {
			if (arr[i] == 0) {
				i--;
			} else
				break;
		}
		return i + 1;
	}



	/**
	 * Helper function
	 * 
	 * @param str
	 * @return String with no leading zeros
	 */
	public static String trimLeadingZeros(String str) {
		if (!str.startsWith("0")) {
			return str;
		}
		int start = 0;
		while (start < str.length() && '0' == str.charAt(start)) {
			start++;
		}
		if (start == str.length()) {
			return "0";
		}
		return str.substring(start);
	}

	/**
	 * Helper Function
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public String[] createChunks(String str, int length) {
		return str.split("(?<=\\G.{" + length + "})");
	}

	/**
	 * Helper function
	 * 
	 * @param str
	 * @return
	 */
	public boolean validateNum(String str) {
		String regEx = "[-]([0-9]+)||([0-9]+)";
		return str.matches(regEx);
	}

	public static int operatorPresidency(String ch) {
		switch (ch) {
		case "^":
			return 2;
		case "*":
		case "/":
		case "%":
			return 1;
		case "+":
		case "-":
			return 0;
		}
		return -1;
	}

	/**
	 * helper function for evaluateExp
	 * @param operandStack
	 * @param operatorsStack
	 * @return
	 */
	public static Num calculateExpr(Stack<Num> operandStack, Stack<String> operatorsStack) {
		Num second = operandStack.pop();
		Num first = operandStack.pop();
		String operator = operatorsStack.pop();
		Num result = performOperation(first, second, operator);
		return result;
	}

	public static Num performOperation(Num first, Num second, String c) {
		Num result = null;
		switch (c) {
		case "+":
			result = Num.add(first, second);
			break;
		case "-":
			result = Num.subtract(first, second);
			break;
		case "*":
			result = Num.product(first, second);
			break;
		case "/":
			result = Num.divide(first, second);
			break;
		case "%":
			result = Num.mod(first, second);
			break;
		case "^":
			long y = Long.parseLong(second.toString());
			result = Num.power(first, y);
			break;
		}
		return result;
	}


}
