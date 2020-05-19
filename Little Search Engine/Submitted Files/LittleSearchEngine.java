package lse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile)
	throws FileNotFoundException {
		
		HashMap<String,Occurrence> keyWordMap = new HashMap<String,Occurrence>(); 
		
		Scanner sc = new Scanner(new File(docFile));
		while(sc.hasNext()) {

			String word=sc.next();
			
			String filterkeyWord=getKeyword(word);
			
			if(filterkeyWord!=null) {
				int count=0;
				
				Scanner sc1 = new Scanner(new File(docFile));
				while(sc1.hasNext()) {
					String compareWord = getKeyword(sc1.next());
					if(filterkeyWord.equals(compareWord)){
						count=count+1;
					}
				}
				Occurrence oc = new Occurrence(docFile, count);
				keyWordMap.put(filterkeyWord, oc);
			}
		}
		return keyWordMap;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		
		Set<String> allkeys = kws.keySet();
		for(String key : allkeys)
		{
			if(this.keywordsIndex.containsKey(key))
			{
				this.keywordsIndex.get(key).add(kws.get(key));
				ArrayList<Integer> middleIndexes = insertLastOccurrence(this.keywordsIndex.get(key));
			}
			else
			{
				ArrayList<Occurrence> occrList = new ArrayList<Occurrence>();
				occrList.add(kws.get(key));
				this.keywordsIndex.put(key, occrList);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		
		String formatWord=word.replaceAll("[^a-zA-Z]+$", "");
		
		for(int i=0; i<formatWord.length(); i++) {
			if(!Character.isAlphabetic(formatWord.charAt(i))) {
				
				return null;
			}
		}
		if(formatWord.length()==0)
		{
			return null;
		}
		formatWord = formatWord.toLowerCase();
		return noiseWords.contains(formatWord)?null :formatWord.toLowerCase();
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		if(occs.size()==1){
			return null;
		}
		
		Occurrence objValue = occs.get(occs.size()-1);
		int lastItemFreq=objValue.frequency;
		
		
		int startIndex=0;
		int endIndex=occs.size()-1;
		int middle;
		
		ArrayList<Integer> midIndexs=new ArrayList<Integer>();
		
		while(startIndex<=endIndex){
			
			middle=(startIndex+endIndex)/2;
			midIndexs.add(middle);
			
			if(lastItemFreq>occs.get(middle).frequency){
				endIndex=middle-1;
				
			}else if(lastItemFreq<occs.get(middle).frequency){
				startIndex=middle+1;
			}else{
				break;
			}
		}
		
		if(midIndexs.get(midIndexs.size()-1)==0){
			if(objValue.frequency<occs.get(0).frequency){
				occs.add(1,objValue);
				occs.remove(occs.size()-1);
				
				return midIndexs;
			}
		}
		
		occs.add(midIndexs.size()-1,objValue);
		occs.remove(occs.size()-1);
		
		return midIndexs;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */ 
	
	
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		
		
		if(kw1==null || kw1==null){
				return null;
		}
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<Occurrence> kw1OccurrenceList = new ArrayList<Occurrence>();
		ArrayList<Occurrence> kw2OccurrenceList = new ArrayList<Occurrence>();
		
		if(keywordsIndex.get(kw1)!=null)
		{
			kw1OccurrenceList = keywordsIndex.get(kw1);
		}
		
		if(keywordsIndex.get(kw2)!=null)
		{
			kw2OccurrenceList = keywordsIndex.get(kw2);
		}
		
		ArrayList<String> top5searchedList=addSearchResult(kw1OccurrenceList,kw2OccurrenceList);
		
		if(top5searchedList.size()==0){
		return null;
		}
		return top5searchedList;
	
	}

	private ArrayList<String> addSearchResult(ArrayList<Occurrence> kw1OccurrenceList,
			ArrayList<Occurrence> kw2OccurrenceList) {
		
		ArrayList<String> occurrenceDocList = new ArrayList<String>();
		
		for(int i=0;i<kw1OccurrenceList.size();i++)
		{
			if(occurrenceDocList.size()<5)
			{
				int kw1Frequency = kw1OccurrenceList.get(i).frequency;
				String kw1DocName = kw1OccurrenceList.get(i).document;
				for(int j=0;j<kw2OccurrenceList.size();j++)
				{
					String kw2DocName = kw2OccurrenceList.get(j).document;
					int kw2Frequency = kw2OccurrenceList.get(j).frequency;
					
					if(kw2Frequency<=kw1Frequency)
					{
						if(!occurrenceDocList.contains(kw1DocName) && occurrenceDocList.size()<5) {
							occurrenceDocList.add(kw1DocName);
						}
					}
					else if(kw2Frequency > kw1Frequency)
					{
						if(!occurrenceDocList.contains(kw2DocName) && occurrenceDocList.size()<5)
						{
							occurrenceDocList.add(kw2DocName);
						}
					}
				}
			}
		}
		
		return occurrenceDocList;
	}
}
