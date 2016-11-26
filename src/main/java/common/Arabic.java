package common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quran.Term;
import quran.Word;

public class Arabic {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	private final static Arabic I = new Arabic();
	
	public Arabic(){
		stops = new ArrayList<String>();
	}
	

	public static void setup(Properties config) throws IOException {
		SVC.setup(config);
	}
	
	public static class SVC{

		public static Arabic setup(Properties config) throws IOException{
			Arabic arabic = new Arabic();
			String fileName = config.getProperty("arabicFile");
			loadStopWords(fileName);
			arabic.isReady = true;
			return arabic;
		}

		private static void loadStopWords(String fileName) throws IOException {
			List<String> lines = Utils.readSimpleFile(fileName);
			for(String line : lines){
				I.stops.add(line);
				I.stops.add("و"+line);
				I.stops.add("ف"+line);
			}
			String tmp = Utils.toString(I.stops);
			I.log.info(tmp);
			
		}
		
		public static boolean isStop(String word){
			return I.stops.contains(word);
		}

		public static boolean isMo3arraf(String word) {
			return word.startsWith("ال") && word.length()>2;
		}

		public static Character getFirstLetter(String word) {
			Character ch = word.charAt(0);
			if(isMo3arraf(word)){
				ch = word.charAt(2);
			}
			return ch;
		}

		/**
		 * tries to find the root of the word, and returns not the abjd root but the passive form
		 * 
		 * @WARNING : this is not working, just a "bouchon", it must be replaced with morphology engine
		 * @TODO: replace with morphology engine
		 * @TODO: add test
		 * 
		 * @param word
		 * @return
		 */
		public static Term parseRoot(String word) {
			String result = new String(word);
			//case of fa, wa
			if(result.startsWith("ف") || result.startsWith("و")  && result.length() > 3+1)
				result = result.substring(1);
			//case of ta3rif
			if(result.startsWith("ال")  && result.length() > 3+2)
				result = result.substring(2);
			//case of ho, ha
			if(result.endsWith("ه")  && result.length() > 3+1)
				result = result.substring(0, result.length()-1);
			if(result.endsWith("ها")  && result.length() > 3+2)
				result = result.substring(0, result.length()-2);
			//case of woon, iin
			if(result.endsWith("ون") || result.endsWith("ين") && result.length() > 3+2)
				result = result.substring(0, result.length()-2);
			if(result.endsWith("ني") && result.length() > 3+2)
				result = result.substring(0, result.length()-2);
			if(result.endsWith("و") && result.length() > 3+1)
				result = result.substring(0, result.length()-1);
			if(result.endsWith("وا") && result.length() > 3+2)
				result = result.substring(0, result.length()-2);
			return new Term(result);
		}

	} 
	public static List getABJDList() {
		List abjdList = new ArrayList(I.letters.length);
		for(int i=0; i<abjdList.size(); i++){
			List objList = new ArrayList();
		}
		return abjdList;
	} 
	
	public static Map<Character, List<Word>> makeABJDMap(){
		TreeMap<Character, List<Word>> map = new TreeMap();
		for(int i=0; i<I.letters.length; i++){
			map.put(I.letters[i], new ArrayList<Word>());
		}
		return map;
	}

	public static Map<Character, List<Term>> getABJDTermsMap(){
		TreeMap<Character, List<Term>> map = new TreeMap();
		for(int i=0; i<I.letters.length; i++){
			map.put(I.letters[i], new ArrayList<Term>());
		}
		return map;
	}

	boolean isReady = false;
	List<String> stops = null; 	// list of words to skip 
	protected Character[] letters = {
			'ا', 
			'أ', 
			'آ', 
			'إ', 
			'ب', 
			'ت', 
			'ث', 
			'ج', 
			'ح', 
			'خ', 
			'د', 
			'ذ', 
			'ر', 
			'ز', 
			'س', 
			'ش', 
			'ص', 
			'ض', 
			'ط', 
			'ظ', 
			'ع',
			'غ', 
			'ف', 
			'ق',
			'ك', 
			'ل', 
			'م',
			'ن', 
			'ه', 
			'و',
			'ي',
			'ۖ','ۗ','ۘ','ۙ','ۚ','ۛ','ۜ','۞','۩'			//special characters
			};

}
