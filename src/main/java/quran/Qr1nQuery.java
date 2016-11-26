package quran;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Utils;
 

public class Qr1nQuery {

	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	
	public void searchF2F(String inputFile, String outputFile) throws IOException {
		Long begn = System.currentTimeMillis();
		List<String> inputs = Utils.readSimpleFile(inputFile);
		List<String> lines = new ArrayList<String>();
		for(String input : inputs){
			List results = null;
			lines.add("["+input+"]");
			String[] inputArray = input.trim().split("\\s");
			if(inputArray.length > 1){
				results = findWordsInVerseUnordered(inputArray);
			}else{
				results = find(input);
				
			}
			if(Utils.ok(results)){
				lines.addAll(results);
				lines.add("");
			}else
				lines.add("");
		}
		Long endn = System.currentTimeMillis();
		log.info("Qr1nQuery::searchF2F>> took "+(endn-begn)+" ms to process "+inputs.size()+" words");
		Utils.writeSimpleFile(lines, outputFile);
	}
	
	
	protected List<String> findWordsInVerseUnordered(String[] words){
		log.info("Qr1nQuery::search["+Utils.toString(Arrays.asList(words))+"]");
		List<List> resultList = new ArrayList<List>();
		//using the map
		for(String word : words){
			List<String> results = new ArrayList<String>();
			results = find(word);
			resultList.add(results);
		}
		//intersection
//		 public static Collection intersection(final Collection a, final Collection b) {
//			    ArrayList list = new ArrayList();
//			    Map mapa = getCardinalityMap(a);
//			    Map mapb = getCardinalityMap(b);
//			    Set elts = new HashSet(a);
//			    elts.addAll(b);
//			    Iterator it = elts.iterator();
//			    while(it.hasNext()) {
//			        Object obj = it.next();
//			        for(int i=0,m=Math.min(getFreq(obj,mapa),getFreq(obj,mapb));i<m;i++) {
//			            list.add(obj);
//			        }
//			    }
//			    return list;
//			}
		return null;
	}

	//the simplest ever
	//here we need to partion the map by character to make it faster...
	protected List<String> find(String key){
		log.info("Qr1nQuery::search["+key+"]");
		List<String> results = new ArrayList<String>();
		//using the map
		//for(Iterator<Entry<String, List<Qr1nLoci>>> it = Quran.IN().index.index.entrySet().iterator(); 	it.hasNext();){
		for(Word word : Quran.IN().index.words){
			//String key = it.next().getKey();
			if(key.compareTo(word.kalima)==0){	//case of exact search
				results.add(word.toStringBuffer().toString());
				return results;
			}
		}
//		for(Iterator<Word> it = Quran.IN().index.terms.iterator(); it.hasNext();){
//			Word w = it.next();
//			if(w.kalima.compareTo(word)==0){
//				results.add(w.toString());
//			}
//		}
		return results;
	}

}
