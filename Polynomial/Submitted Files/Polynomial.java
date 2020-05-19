package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		Node ptr1=poly1;
		Node ptr2=poly2;
		Node result=null;
		float totalCoeff=0;
		Node ptr3 = null;
		Node resultFront = null;
		
		
	
		while(ptr1!=null && ptr2!=null) {
		  
			result=null;
		
			if(ptr1.term.degree>ptr2.term.degree) {
				
				if (ptr2.term.coeff != 0) {
					result=new Node(ptr2.term.coeff, ptr2.term.degree, null);
				}
				 ptr2=ptr2.next;
				
				
			}else if(ptr2.term.degree>ptr1.term.degree){
				if (ptr1.term.coeff != 0) {
					result=new Node(ptr1.term.coeff, ptr1.term.degree, null);
				}
				 ptr1=ptr1.next;
				
			}else if(ptr1.term.degree==ptr2.term.degree) {
				
			totalCoeff= ptr1.term.coeff+ptr2.term.coeff;
		
			if (totalCoeff != 0) {
				result=new Node(totalCoeff,ptr1.term.degree,null);
			}
			ptr1=ptr1.next;
			ptr2=ptr2.next;
			  
			}
			
			if (result != null) {
			if(ptr3==null) {
				
				ptr3=result;
				resultFront = result;
				
			}else {
				ptr3.next=result;
				ptr3=ptr3.next;
				
			}
			}
		}
		
		while(ptr1 != null) {
			
			if (ptr1.term.coeff != 0) {
			
			result=new Node(ptr1.term.coeff, ptr1.term.degree,null);
			
             if(ptr3==null) {
				
				ptr3=result;
				resultFront = result;
				
				
			}else {
				ptr3.next=result;
				ptr3=ptr3.next;
				
			}
			}
			
             ptr1=ptr1.next;
		}
		
		while (ptr2 != null) {
			
			if (ptr2.term.coeff != 0) {
			result=new Node(ptr2.term.coeff, ptr2.term.degree,null);
            if(ptr3==null) {
				
				ptr3=result;
				resultFront = result;
				
			}else {
				ptr3.next=result;
				ptr3=ptr3.next;
				
			}
			}
            ptr2=ptr2.next;
		}
		

	
		return resultFront;
		
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		
		Node ptr1=poly1;
		float mult;
		int totalDegree;
		Node front = null;
		Node result = null;
		
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		while(ptr1!=null ) {
			Node itteration;
			Node ptr3=null;
			Node ptr2=poly2;
			front = null;
			while(ptr2!=null) {
				
				mult=ptr1.term.coeff*ptr2.term.coeff;
				totalDegree=ptr1.term.degree+ptr2.term.degree;
				if(mult != 0) {
				itteration=new Node(mult,totalDegree,null);
				if(ptr3==null) {
					ptr3=itteration;
					 front=ptr3;
				}else {
					
					ptr3.next=itteration;
					ptr3=ptr3.next;
					
				}
				}
				
				ptr2=ptr2.next;
			}
			
			ptr1=ptr1.next;
			
			result = add(front, result);
			
			
			
			
			
		}
		

		
		
		return result;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 * 
	 *
	 */
	
	
	public static float evaluate(Node poly, float x) {
		
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		Node ptr=poly;
		
		float result=0;
		float productSoFar=1;
		int degreeSoFar=0;
		while(ptr!=null) {
			
			if(ptr.term.degree==0) {
				
				result=result+ptr.term.coeff;
			
				
			}
			else {
				productSoFar=(float) (productSoFar*(Math.pow(x, ptr.term.degree-degreeSoFar)));
				degreeSoFar=ptr.term.degree;
				result=result+ptr.term.coeff*productSoFar;
				
			}
			
			ptr=ptr.next;
			
			
		}
		return result;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */

	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
