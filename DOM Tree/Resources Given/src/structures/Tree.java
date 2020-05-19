package structures;

import java.util.Scanner;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */

public class Tree {

	/**
	 * Root node
	 */
	TagNode root=null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}

	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/

		String first_node=removeBrackets(sc.nextLine());
		TagNode ptr = new TagNode(first_node, null, null);
		root = ptr;
		TagNode parent;
		Stack<TagNode> stack= new Stack<TagNode>();
		parent=root;
		stack.push(root);
		while(sc.hasNext()) {
			String str=sc.nextLine();
			if(str.charAt(0)=='<') {
				if(str.charAt(1)=='/') {
					ptr=stack.pop();
					if(!stack.isEmpty()) {
						parent=stack.peek();
					} else {
						parent = ptr;
					}
				} else {
					str=removeBrackets(str);
					TagNode node = new TagNode(str, null, null);
				
					stack.push(node);
					if(parent.firstChild==null) {
						ptr.firstChild=node;
						ptr=ptr.firstChild;
						parent=node; 
					}else {  
						parent=node; 
						ptr.sibling=node;
						ptr=ptr.sibling;	
					}
				}
			}
			else {
				TagNode textNode = new TagNode(str, null, null);
				if(parent.firstChild==null) {
					ptr.firstChild=textNode;
					ptr=textNode;
				}else {
					ptr.sibling=textNode;
					ptr=ptr.sibling;
				}
			}
		}
	}





	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {  // em , b
		/** COMPLETE THIS METHOD **/
		replaceTag(this.root, oldTag, newTag);
	}


	private void replaceTag(TagNode root,String oldTag, String newTag) {

		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			
			if (ptr.tag.equalsIgnoreCase(oldTag)) {
			
				ptr.tag = newTag;
			}
			if (ptr.firstChild != null) {
				replaceTag(ptr.firstChild, oldTag, newTag);
			}
		}
	}

	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		makeBoldRow(this.root, row);

	}

	private void makeBoldRow(TagNode root, int row) {

		TagNode childPtr;
		for (TagNode ptr=root; ptr != null;	ptr=ptr.sibling) {

			
			if(ptr.tag.equalsIgnoreCase("table")) {
  
				ptr=ptr.firstChild;

				
				for(int i=1; i<row;i++) {
					ptr=ptr.sibling;		

				}

				

				ptr=ptr.firstChild;
				childPtr=ptr.firstChild;
				
				TagNode boldNode = new TagNode("b", null, null);
				boldNode.firstChild=childPtr;
				ptr.firstChild=boldNode;
				
				
				

				while(ptr.sibling!=null) {

					ptr=ptr.sibling;
					childPtr=ptr.firstChild;
					TagNode sibblingBoldNode = new TagNode("b", null, null);
					sibblingBoldNode.firstChild=childPtr;
					ptr.firstChild=sibblingBoldNode;

				}
			}
			
			if (ptr.firstChild != null ) {
				makeBoldRow(ptr.firstChild, row);
			}

		}

	}

	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/

		deleteTag(null,this.root, tag);


	}

	private void deleteTag(TagNode prev,TagNode root,String deleteTag) {


		TagNode finalNode = null;
		for (TagNode ptr=root; ptr != null;	ptr=ptr.sibling) {

			if(ptr.tag.equals(deleteTag)) {

				if(deleteTag.equalsIgnoreCase("p")|| deleteTag.equalsIgnoreCase("em")|| deleteTag.equalsIgnoreCase("b")) {

					if(ptr.firstChild==null && ptr.sibling==null) { 
						finalNode=null;
					}

					else if(ptr.firstChild!=null && ptr.sibling==null) { 
						finalNode=ptr.firstChild;   

					}
					else if(ptr.firstChild==null && ptr.sibling!=null){
						finalNode=ptr.sibling; 
					}
					else if(ptr.firstChild!=null && ptr.sibling!=null) {


						TagNode tempStart;
						TagNode tempEnd;

						tempStart=ptr.firstChild;
						while(tempStart.sibling!=null) {
							tempStart=tempStart.sibling;
						}
						tempEnd=tempStart;
						tempEnd.sibling=ptr.sibling;
						finalNode = ptr.firstChild;

					}


					if (prev.firstChild == ptr) {
						prev.firstChild = finalNode;
						ptr = finalNode;
					} else {
						prev.sibling = finalNode;
						ptr = finalNode;
					}

				}  else if(deleteTag.equalsIgnoreCase("ol")|| deleteTag.equalsIgnoreCase("ul")) {

					TagNode temp = ptr.firstChild;
					while(temp != null) {
						if(temp.tag.equalsIgnoreCase("li")) {
							temp.tag = "p";
						}
						temp = temp.sibling;
					}


					if(ptr.firstChild==null && ptr.sibling==null) { 
						finalNode=null;
					}

					else if(ptr.firstChild!=null && ptr.sibling==null) { 

						finalNode=ptr.firstChild;  
					}
					else if(ptr.firstChild==null && ptr.sibling!=null){


						finalNode=ptr.sibling; 
					}
					else if(ptr.firstChild!=null && ptr.sibling!=null) {

						TagNode tempStart;
						TagNode tempEnd;

						tempStart=ptr.firstChild;
						while(tempStart.sibling!=null) {
							tempStart=tempStart.sibling;
						}
						tempEnd=tempStart;
						tempEnd.sibling=ptr.sibling;
						finalNode = ptr.firstChild;

					}
					if (prev.firstChild == ptr) {
						prev.firstChild = finalNode;
						ptr = finalNode;
					} else {
						prev.sibling = finalNode;
						ptr = finalNode;
					}

				}


			}

			prev=ptr;
						if (ptr.firstChild != null ) {
				deleteTag(prev,ptr.firstChild, deleteTag);
			}

		}

	}




	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		addTagNode(this.root, null, word, tag);
	}


	private static void  addTagNode(TagNode root, TagNode prev, String word, String tag) {
		String  connect="";
		TagNode x=null;
		for (TagNode ptr=root; ptr != null;) {
			x=ptr;
			TagNode next = ptr.sibling; 
			if(ptr.tag.contains(word)) {

				String ptrString = ptr.tag;

				String[] splitArray = ptrString.split(" ");  

				for(int i=0; i<splitArray.length; i++) {
					if(splitArray[i].equalsIgnoreCase(word) 
							|| (splitArray[i].substring(0, splitArray[i].length() -1).equalsIgnoreCase(word)
									&& (splitArray[i].charAt(splitArray[i].length() -1) < 65 || (splitArray[i].charAt(splitArray[i].length() -1) > 122)))) {
						
						TagNode connectNode = null;
						if (!connect.equalsIgnoreCase("")) {
							connectNode = new TagNode(connect,null,null);
						}
						TagNode wordNode = new TagNode(splitArray[i], null, null);
						TagNode tagNode = new TagNode(tag, null, null);

						if(x==ptr) {
							if (connectNode != null) {
								x.tag=connect;
								x.sibling=tagNode;
							} else {
								if (prev.firstChild == ptr) {
									prev.firstChild = tagNode;
								} else {
									prev.sibling = tagNode;
								}
						
							}
						}else {
							if (connectNode != null) {
								x.sibling=connectNode;
								connectNode.sibling=tagNode; 
							} else {
								x.sibling = tagNode;
							}
						}
						x=tagNode;
						tagNode.firstChild = wordNode;
						connect="";	 
					}else { 
						if (connect.equalsIgnoreCase("")) {
							connect = splitArray[i];
						} else {
							connect=connect + " " + splitArray[i];
						}
					}
				}
				if (!connect.equalsIgnoreCase("")) {
					TagNode connectNode = new TagNode(connect,null,null);
					x.sibling=connectNode;
					x=x.sibling;
				}
				x.sibling = next;
				prev = ptr;
				ptr = next;
			}


			if(ptr != null && !ptr.tag.contains(word)) {
				prev = ptr;
			}

			if (ptr != null && ptr.firstChild != null) {
				addTagNode(ptr.firstChild, prev, word, tag);
			}


			if(ptr != null && !ptr.tag.contains(word)) {
				prev = ptr;
				ptr = ptr.sibling;
			}
		}




	}







	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */



	private static String removeBrackets(String str ) {
		str=str.substring(1, str.length()-1);
		return str;

	}

	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}


	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}

	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}