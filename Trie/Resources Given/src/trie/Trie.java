package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {

		TrieNode root = new TrieNode(null, null, null);
		for (int i = 0; i < allWords.length; i++) {
			Indexes substr = new Indexes(i, (short) (0), (short) (allWords[i].length() - 1));
			TrieNode childNode = new TrieNode(substr, null, null);
			TrieNode ptr = root;
			if (ptr.firstChild == null) {
				ptr.firstChild = childNode;
			} else {
				short count = -1;
				ptr = ptr.firstChild;
				String currentString = allWords[i];
				String checkString = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,
						ptr.substr.endIndex + 1);
				int j = 0;
				while (true) {
					count = -1;
					j = 0;
					while (j < currentString.length() && j < checkString.length()) {
						if (currentString.charAt(j) == checkString.charAt(j))
							count += 1;
						else
							break;
						j += 1;
					}
					if (count == -1) {
						if (ptr.sibling != null) {
							ptr = ptr.sibling;
							checkString = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,
									ptr.substr.endIndex + 1);
							continue;
						}
						childNode.substr.startIndex = ptr.substr.startIndex;
						ptr.sibling = childNode;
						break;
					} else if (count > -1 && count < (ptr.substr.endIndex - ptr.substr.startIndex)) {
						if (ptr.firstChild == null) {
							Indexes Aftercommon = new Indexes(ptr.substr.wordIndex,
									(short) (ptr.substr.startIndex + count + 1), ptr.substr.endIndex);
							childNode.substr.startIndex = (short) (ptr.substr.startIndex + count + 1);
							TrieNode newAdderNode = new TrieNode(Aftercommon, null, childNode);

							ptr.substr.endIndex = (short) (ptr.substr.startIndex + count);
							ptr.firstChild = newAdderNode;
							break;
						} else {
							Indexes Aftercommon = new Indexes(ptr.substr.wordIndex,
									(short) (ptr.substr.startIndex + count + 1), ptr.substr.endIndex);
							childNode.substr.startIndex = (short) (count + 1);
							TrieNode newAdderNode = new TrieNode(Aftercommon, null, null);
							ptr.substr.endIndex = (short) (ptr.substr.startIndex + count);
							TrieNode remaining = ptr.firstChild;
							ptr.firstChild = newAdderNode;
							ptr = ptr.firstChild;
							ptr.firstChild = remaining;
							currentString = currentString.substring(count + 1);
							checkString = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,
									ptr.substr.endIndex + 1);
							continue;
						}
					} else if (count > -1 && count == (ptr.substr.endIndex - ptr.substr.startIndex)) {
						ptr = ptr.firstChild;
						currentString = currentString.substring(count + 1);
						checkString = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,
								ptr.substr.endIndex + 1);
						continue;
					}

				}
			}
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		ArrayList<TrieNode> matchesNode=new ArrayList<TrieNode>();
		
		
		
		String pre_fix = prefix;
		TrieNode ptr = root;
		ptr = ptr.firstChild;
		while(true) {
		String checkString = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1);
		int count = 0;
		int len = 0;
		if(checkString.length()<pre_fix.length())
			len = checkString.length();
		else
			len = pre_fix.length();
		
		for(int i=0; i<len; i++)
		{
			if(pre_fix.charAt(i)==checkString.charAt(i))
				count+=1;
			else
				break;
		}
		if(count == 0)
		{
			if(ptr.sibling != null)
			{
				ptr = ptr.sibling;
				continue;
			}
			else
			{
				break;
			}
		}
		else
		{	if(count<checkString.length() && pre_fix.length()>=checkString.length())
				break;

			if(count==pre_fix.length())
			{
				if(ptr.firstChild == null)
				{
					matchesNode.add(ptr);
					break;
				}
				else {
					ptr = ptr.firstChild;
					printInorder(ptr,matchesNode);
					break;
				}
				
			}
			else if(count< pre_fix.length())
			{
				if(ptr.firstChild != null)
				{
					ptr = 	ptr.firstChild;
					pre_fix = pre_fix.substring(count);
				}
				else
					break;
			}

		}
		
		}
		if(matchesNode.size()==0)
			return null;
		else
		return matchesNode;
		
		}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
	
	private static void printInorder(TrieNode node, ArrayList<TrieNode> matchesNode) 
    { 
		if (node == null) 
            return; 
  
        /* first recur on left child */
        printInorder(node.firstChild,matchesNode); 
  
        /* then print the data of node */
        if(node.firstChild == null) {
        matchesNode.add(node);
        }
  
        /* now recur on right child */
        printInorder(node.sibling, matchesNode); 
    }
}
