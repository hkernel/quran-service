package quran;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Arabic;
/**
 * Could be updated to a wrapper around lucene index. 
 * No need for lucene now, as the Quran is just a single file with a complex structure.
 * 
 * @author reuse
 *
 */
public class Qr1nIndex {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	//List<String> words = new ArrayList<String>();
	//List<Qr1nLoci> locations = new ArrayList<Qr1nLoci>();
	//Map<String, List<Qr1nLoci>> index = new TreeMap<String, List<Qr1nLoci>>();
	Set<Word> words = new TreeSet<Word>();
	 
	
	/**
	 * This index is too simple, it must be extended to more information
	 * @throws Exception
	 */
	public void build() throws Exception{
		log.info("Building index....");
		//for each verse, pick up the words, add them to the list and map
		for(Aaya verse : Quran.IN().model.allVerses){
//			for(String wordText : verse.rawText.split("\\s")){ 
//				Qr1nLoci loci = Qr1nLoci.parseLocation(verse, wordText);
//				addWordToIndex(wordText, loci);
//			}
			for(Word word : verse.words){
				addWordToIndex(word);
			}
		}
		//sortWordsByName();
		
	}
	
	//this was used for the list interface, now switching to set
//	private void addWordToIndex(Word word) throws Exception{
//		Integer id = Collections.binarySearch(words, word);
//		
//		if(id > 0){
//			if(word.locis.size()>1)
//				throw new Exception(word+" has more than one location, unexpected");
//			words.get(id).locis.add(word.locis.get(0));		//don't like this
//		}else	//new addition to the index
//			words.add(word);
//	}
	
	private void addWordToIndex(Word word) throws Exception {
		for(Iterator<Word> it = words.iterator(); it.hasNext();){
			Word curr = it.next();
			if(curr.kalima.compareTo(word.kalima)==0){
				curr.locis.add(word.locis.get(0));
				return;
			}
		}
		words.add(word);
	}
	
	
	/**
	 * For now we are discarding stopwords, must be improved
	 * @return
	 * @throws Exception
	 */
	public Map<Character, List<Word>> indexMapToABJDMap() throws Exception{
		Map<Character, List<Word>> abjd = Arabic.makeABJDMap();
		//for(Iterator<String> it = index.keySet().iterator(); it.hasNext();){
		for(Word word : words){ 
			//Word word = new Word(wordText, index.get(wordText));
			
			if(Arabic.SVC.isStop(word.kalima)){
				log.info(word.kalima+" ignored");
				continue;
			}
			Character ch = Arabic.SVC.getFirstLetter(word.kalima);
			List<Word> words = abjd.get(ch);
			words.add(word);
		}
		return abjd;
	}
	
	
	//returns a map of character to terms where a term is a collection of words having the same root
	public Map<Character, List<Term>> indexMapToABJDTermsMap() throws Exception{
		Map<Character, List<Term>> abjdTerms = Arabic.getABJDTermsMap();
//		for(Iterator<String> it = index.keySet().iterator(); it.hasNext();){
		for(Word word : words){ 
			//String wordText = it.next();
			//Word word = new Word(wordText, index.get(wordText));	
			if(Arabic.SVC.isStop(word.kalima)){
				log.info(word.kalima+" skipped");
				continue;
			}		
			Term term = Arabic.SVC.parseRoot(word.kalima);
			Character ch = Arabic.SVC.getFirstLetter(word.kalima);
			List<Term> terms = abjdTerms.get(ch); 
			Collections.sort(terms);
			int find = Collections.binarySearch(terms, term);
			if(find < 0){
				term.words.add(word);
				terms.add(term);
			}else{
				terms.get(find).words.add(word);
			}
		}
		return abjdTerms;
	}

	//copied from stack overflow
	static <K,V extends List>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	    			List oList1 = e1.getValue();
	    			List oList2 = e2.getValue();
	    			return (oList1.size() < oList2.size())? -1 
	    					: (oList1.size() == oList2.size())? 0 : 1;
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
	//this just doesn't execute @TODO: break
	class FreqComparator implements Comparator<Map.Entry<String, List>>{

		@Override
		public int compare(Entry<String, List> o1, Entry<String, List> o2) {
			List oList1 = o1.getValue();
			List oList2 = o2.getValue();
			return (oList1.size() < oList2.size())? -1 
					: (oList1.size() == oList2.size())? 0 : 1;
		}		
	}

	public List<Word> sortWordsByLoci(List<Word> list) {
		{
			class LociComparator implements Comparator<Word>{

				@Override
				public int compare(Word word1, Word word2) {
					Integer size1 = word1.locis.size();
					Integer size2 = word2.locis.size();
					return size1.compareTo(size2);
				}
			}
			Collections.sort(list, new LociComparator());
			return list;
		}
	}
	public  List<Word> sortWordsByName(List<Word> list){
		Collections.sort(list);
		return list;
	}
	

}
