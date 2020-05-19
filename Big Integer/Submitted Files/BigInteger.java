package bigint;


/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		
		integer=integer.trim();
		BigInteger bigInteger = new BigInteger();
		
		
		boolean isValid=validateUserInput(integer);
		
		if(!isValid){
			
			throw new IllegalArgumentException();
		}
		
		boolean isNegative=checkNumberSign(integer);
		
		if(isNegative ){
			
			integer=integer.replace("-","");
		}
		
		if(integer.charAt(0)=='+' ){
			
			integer=integer.replace("+","");
		}
		
		
		integer=replaceLeadingZeros(integer);
		
		if(integer.length()==1 && integer.equalsIgnoreCase("0")){
			isNegative=false;
		}
		
		bigInteger.front=buildDigitLinkedList(integer,bigInteger.front);
		bigInteger.numDigits=calculateNumDigits(bigInteger.front);
		bigInteger.negative=isNegative;
		
		return bigInteger; 
	}


	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		
		BigInteger bigInteger = new BigInteger();
		DigitNode firstList=first.front;
		DigitNode secondList=second.front;
		
		
		if(first!=null && second!=null){
			
			if(first.negative && second.negative){
				bigInteger.negative=true;
			}
			
		
		
		if((first.negative && second.negative) 
				||(!first.negative && !second.negative) ){
		
			bigInteger.front=sumIntegerNodes(firstList,secondList);
		
		}else{
			bigInteger=subtractIntegerNodes(first,second,bigInteger);
		}
		bigInteger.numDigits=calculateNumDigits(bigInteger.front);
		
		}
		return bigInteger; 
	}
	
	


	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	
	
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		int count = 0;
		BigInteger bigInteger = new BigInteger();
		
		if(first!=null && second!=null){
			
			if(first.negative || second.negative){
				bigInteger.negative=true;
			}
			if(first.negative && second.negative){
				bigInteger.negative=false;
			}
			
			if((first.numDigits==1 && first.front.digit==0) ||
					second.numDigits==1 && second.front.digit==0){
				return bigInteger;
			}
			
			DigitNode ptr = first.front;
			DigitNode mulPtr=second.front;
			DigitNode headNode =null;
			int carry=0;
			DigitNode resultHead=null;
			
			while(ptr !=null){
				
				mulPtr=second.front;
				String resultOutput="";
				
				while(mulPtr!=null){
					
						int result=(ptr.digit * mulPtr.digit)+carry;
						carry=result/10;
						result=result%10;
						mulPtr=mulPtr.next;
						resultOutput=result+resultOutput;
					}
				
				if(carry>0){
					resultOutput=carry+resultOutput;
					carry=0;
				}
				count++;
				resultHead=addProduct(resultOutput,resultHead, count);
				
				bigInteger.front=resultHead;
				
				DigitNode x = headNode;
				
				while(x!=null){
					x=x.next;
					
				}
					ptr=ptr.next;
			}
			
		}
		bigInteger.numDigits=calculateNumDigits(bigInteger.front);
		return bigInteger; 
		
	}
	
	
	private static DigitNode addProduct(String resultOutput,DigitNode resultHead, int count) {
		
		if(resultHead==null){
		for(int i=resultOutput.length()-1;i>=0;i--){
			
			DigitNode node = new DigitNode(Character.getNumericValue(resultOutput.charAt(i)), null);
			
			if(resultHead==null){
				
				resultHead=node;
			}else{
				DigitNode ptr = resultHead;
				while(ptr.next!=null){
					
					ptr=ptr.next;
				}
				
				ptr.next=node;
				
			}
			
		}
		
		return resultHead;
		}else{
			String zeros = "";
			for(int i=0; i<count-1;i++) {
				zeros = zeros+"0";
			}
			
			resultOutput=resultOutput+zeros;
			DigitNode temp = resultHead;
			int crry=0;
			for(int i=resultOutput.length()-1;i>=0;i--){
				int sum=0;
				sum=Character.getNumericValue(resultOutput.charAt(i));
				if(temp!=null){
				sum=sum+crry+temp.digit;
				crry=sum/10;
				sum=sum%10;
				temp.digit=sum;
				temp=temp.next;

			}else {
				 	sum=sum+crry;
					crry=sum/10;
					sum=sum%10;
					
					DigitNode node = new DigitNode(sum, null);
					DigitNode ptr = resultHead;
					
					while(ptr.next!=null){
						ptr=ptr.next;
					}
					
					ptr.next=node;
			}

			}
			
			
			if(crry>0){
				
				DigitNode carryNode=new DigitNode(crry,null);
				
				DigitNode ptr = resultHead;
				
				while(ptr.next!=null){
					ptr=ptr.next;
				}
				
				ptr.next=carryNode;
			}
		}
		return resultHead;
	}

	private static boolean validateUserInput(String integer) {

		boolean isValidInput=true;
		int start=0;
		
		if(integer!=null){
			
			if(integer.isEmpty()){
				
				isValidInput=false;
				return isValidInput;
			}
			
			if(integer.charAt(0)=='-' 
					|| integer.charAt(0)=='+' ){
				
				start=1;
			}
			
			for(int i=start;i<integer.length();i++){
				
				boolean isDigit=Character.isDigit(integer.charAt(i));
				
				if(!isDigit){
					isValidInput=false;
					break;
				}
			}
			
		}
		
		
		return isValidInput;
	}
	
	private static boolean checkNumberSign(String integer) {
		
		boolean isNegative=false;
		
		if(integer.charAt(0)=='-' ){
			
			isNegative=true;
		}
		
		return isNegative;
	}
	
	
	private static String replaceLeadingZeros(String integer) {
		boolean allZero = true; 
		int length=integer.length();
		for (int i=0;i<length && allZero;i++)
		{
		    if (integer.charAt(0)!='0'){
		        allZero = false;
		    	break;
		    }
		    if (integer.charAt(0)=='0'){
		    	integer = integer.replaceFirst("0", "");
		    }
		}
		
		if (allZero){
			integer = "0";
		    
		}
		
		return integer;
		
}
	
	
	private static DigitNode buildDigitLinkedList(String integer,DigitNode front) {
		
		
		for(int i=integer.length()-1;i>=0;i--){
			
			DigitNode node = new DigitNode(Character.getNumericValue(integer.charAt(i)), null);
			
			if(front==null){
				
				front=node;
			}
			else{
				
				DigitNode ptr = front;
				
				while(ptr.next!=null){
					
					ptr=ptr.next;
				}
				
				ptr.next=node;
				
			}
		}
		
		
		return front;
	}
	
	private static int calculateNumDigits(DigitNode front) {
		
		int noOfDigits=0;
		if(front==null){
			
		}
		else{
			
			DigitNode ptr=front;
			while(ptr!=null){
				
				noOfDigits=noOfDigits+1;
				ptr=ptr.next;
			}
		}
		
		return noOfDigits;
	}
	
	
	private static DigitNode sumIntegerNodes(DigitNode firstList, DigitNode secondList) {
		
		DigitNode newHead=null;
				
				int sum=0;
				int carry=0;
				
				
				
				while(firstList!=null || secondList!=null){
					
					sum=carry;
					if(firstList!=null){
						sum=sum+firstList.digit;
						firstList=firstList.next;
					}
					if(secondList!=null){
						
						sum=sum+secondList.digit;
						secondList=secondList.next;
					}
					
					carry=sum/10;
					sum=sum%10;
					
					DigitNode node =new DigitNode(sum, null);
					
					if(newHead==null)
					{ 
						newHead=node;
					}else{
						
						DigitNode ptr=newHead;
						while(ptr.next!=null){
							
							ptr=ptr.next;
						}
					ptr.next=node;
					}
				}
				
				
				if(carry!=0){
					DigitNode carryNode=new DigitNode(carry,null);
					
					DigitNode ptr = newHead;
					
					while(ptr.next!=null){
						ptr=ptr.next;
					}
					
					ptr.next=carryNode;
					
				}
				
				return newHead;
			}
	
	
	
	private static BigInteger subtractIntegerNodes(BigInteger firstInteger,
			BigInteger secondInteger,BigInteger bigInteger) {
		
		DigitNode largerInteger=null;
		DigitNode smallerInteger=null;
		
		boolean applendZeros=false;
		
		int firstIntegerLength=firstInteger.numDigits;
		int secondIntegerLength=secondInteger.numDigits;
		int lengthDiff=0;
		
		
		if(firstIntegerLength==secondIntegerLength){
			
			DigitNode revFirst=null;
			DigitNode revSecond=null;
			
			DigitNode temp1= firstInteger.front;
			DigitNode temp2= secondInteger.front;
			
			revFirst=reverseLinkedList(temp1);
			revSecond=reverseLinkedList(temp2);
			
			while(revFirst!=null && revSecond!=null){
				if (revFirst.digit != revSecond.digit) 
                { 
					if(revFirst.digit > revSecond.digit){
						
						largerInteger=reverseLinkedList(revFirst);
						smallerInteger=reverseLinkedList(revSecond);
						bigInteger.negative=firstInteger.negative;
					}else{
						largerInteger=reverseLinkedList(revSecond);
						smallerInteger=reverseLinkedList(revFirst);
						bigInteger.negative=secondInteger.negative;
					}
					
                    break; 
                } 
				revFirst = revFirst.next; 
				revSecond = revSecond.next; 
				
            }
			}
			else if(firstIntegerLength > secondIntegerLength){
			
			largerInteger=firstInteger.front;
			smallerInteger=secondInteger.front;
			
			bigInteger.negative=firstInteger.negative;
			lengthDiff = firstIntegerLength-secondIntegerLength;
			applendZeros=true;
					
		}else if(secondIntegerLength > firstIntegerLength ){
			
			largerInteger=secondInteger.front;
			smallerInteger=firstInteger.front;
			bigInteger.negative=secondInteger.negative;
			lengthDiff = secondIntegerLength-firstIntegerLength;
			
			applendZeros=true;
		}
		
		if(applendZeros){
			smallerInteger=appendZeros(smallerInteger,lengthDiff);
			
		}
		String output="";
		boolean borrow=false;
		DigitNode subtractNode=null;
		if (largerInteger == null && smallerInteger == null && borrow == false){ 
            return null; 
		}
		
		DigitNode ptrLarge=largerInteger;
		DigitNode  ptrSmall=smallerInteger;
		while(ptrLarge!=null && ptrSmall!=null){
			
			int firstDigit=ptrLarge.digit;
			int secondDigit=ptrSmall.digit;
			int resultDigit=0;
			
			if(borrow){
				
				firstDigit=firstDigit-1;
				borrow=false;
			}
			
			if( firstDigit < secondDigit){
				
				firstDigit=firstDigit+10;
				borrow =true;
			}
			secondDigit=ptrSmall.digit;
			resultDigit=firstDigit - secondDigit;
			
			output=resultDigit+output;
			
			ptrLarge=ptrLarge.next;
			ptrSmall=ptrSmall.next;
			
			
		}
			
			output=replaceLeadingZeros(output);
			if(!output.isEmpty())
				{
				
				for(int i=output.length()-1;i>=0;i--){
					
					int digit =Character.getNumericValue(output.charAt(i));
					
					DigitNode newNode =new DigitNode(digit, null);
					
					
					if(subtractNode==null){
						subtractNode=newNode;
					}else{
						
						DigitNode temp = subtractNode;
						
						while(temp.next!=null){
							
							temp=temp.next;
						}
						
						temp.next=newNode;
						
						
					}
				}
		}
		
		bigInteger.front=subtractNode;
		
		return bigInteger;
	}
	
	
	private static DigitNode reverseLinkedList(DigitNode front) {
		
		DigitNode head =front;
		
		if(head==null){
			return null;
		}
		
		
		DigitNode prev = null;
		DigitNode current = front;
		DigitNode next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        head = prev;
		
		return head;
	}

	private static DigitNode appendZeros(DigitNode smallerInteger, int lengthDiff) {
		
		for(int i=0;i<lengthDiff;i++){
			
			DigitNode ptr=smallerInteger;
			while(ptr.next!=null){
				
				ptr=ptr.next;
			}
			
			DigitNode node = new DigitNode(0, null);
			
			ptr.next=node;
		}
	
		return smallerInteger;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}