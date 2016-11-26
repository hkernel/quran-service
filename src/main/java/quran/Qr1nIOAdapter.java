package quran;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Utils;

public class Qr1nIOAdapter{
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	
	/* ##########################			INPUT SECTION *###################*/
	
	public void loadFromSimpleFile(String fileName) throws IOException{
		List<String> lines = Utils.readSimpleFile(fileName); 
		parseSimple(lines); 
	}
	

	/**
	 * Parses list of verses as lines and extracts the quran data structure
	 * @param lines		lines of the quran simple file format
	 */
	private void parseSimple(List<String> lines){
		List<Aaya> local = new ArrayList<Aaya>();
		int cch = 1; //chapter counter starts with chapter 1 
		for(String phrase : lines){
			log.info("Quran:SVC:parseSimple>>>processing..."+phrase);
			String[] parts = phrase.split("\\|");
			Integer ich = Integer.parseInt(parts[0]);
			Integer ivs = Integer.parseInt(parts[1]);
			String verse = parts[2];
			if(cch!=ich){ //new chapter
				Quran.IN().model.verseMap.put(cch, local);	//put previous
				Quran.IN().model.verseTables.add(cch-1, local);
				cch++; //or ich
				local = new ArrayList<Aaya>();
			}
			Quran.IN().model.allVerses.add(new Aaya(verse, ivs, ich));
			local.add(new Aaya(verse, ivs, ich));
		}
		Quran.IN().model.verseMap.put(cch, local);	//put previous
		Quran.IN().model.verseTables.add(cch-1, local);
		
	}
	
	public void printIndex(Quran q){
		System.out.println("printing kalimates\n");
		for(Word word : q.index.words)
			System.out.println(word.kalima);
	}
	

	public void printIndexWordListToFile(String fileName) throws IOException{
		File file = new File(fileName);
		if(file.exists()==false) file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		pw.println("## printing quranic words ");
		for(Word word : Quran.IN().index.words){
			pw.println(word);
		}
		pw.close();
	}
	
	

	public void printIndexMapToFileSortByLocation(String fileName) throws IOException{
		//SortedSet<Entry<String, List<Qr1nLoci>>> index2 = Qr1nIndex.entriesSortedByValues(Quran.IN().index.index);
		Quran.IN().index.sortWordsByLoci(null);
		List<String> lines = new ArrayList<String>();
		lines.add("## printing quranic index sorted by frequency \n");
		
		//for(Iterator<Entry<String, List<Qr1nLoci>>> it = index2.iterator(); it.hasNext();){
		for(Word word : Quran.IN().index.words){
			//String word =  it.next().getKey();
			//List<Qr1nLoci> locis = Quran.IN().index.index.get(word); 
			//pw.println(word + ":" + locis.size());
			lines.add(word.kalima + ":" + word.locis.size());
		}
		
		Utils.writeSimpleFile(lines, fileName);
	}

	public void printIndexMapToFile(String fileName) throws IOException{
		List<String> lines = new ArrayList<String>();
		lines.add("## printing quranic index \n\n");
		//for(Iterator<String> it = Quran.IN().index.index.keySet().iterator(); it.hasNext();){
		for(Word word : Quran.IN().index.words){
			lines.add(word.toStringBuffer().toString());
		} 

		Utils.writeSimpleFile(lines, fileName);
	} 


	/**
	 * For each letter of the alphabet
	 * @param fileName
	 * @throws IOException
	 */
	public void printABJDMapToJSON(String fileName) throws Exception{
		JSONObject goal = new JSONObject();		//TODO: must be library independent
		log.debug("printABJDMapToJSON## printing quranic index as ABJD");
		Map<Character, List<Word>> indexAbjdMap = Quran.IN().index.indexMapToABJDMap();
		for(Iterator<Character> it = indexAbjdMap.keySet().iterator(); it.hasNext();){
			Character letter= it.next();
			List<Word> words = indexAbjdMap.get(letter);
			//goal.put(letter, words);
			JSONArray array = new JSONArray();
			for(Word word : words){
				//Map p = w.toStringMap();
				String tmp = word.toStringBuffer().toString();
				log.debug("printABJDMapToJSON## \t\t"+tmp);
				array.add(tmp);
				//array.add(word.toJSONObject());
			}//log.info("printABJDMapToJSON## "+array.toJSONString());
			goal.put(letter, array);
		} 
		Utils.writeJSONFile(goal, fileName);
	}

	public void printABJDTermsMaptoJSON(String fileName) throws Exception{
		JSONObject goal = new JSONObject();		//TODO: must be library independent
		log.debug("printABJDMapToJSON## printing quranic index as ABJD terms");
		Map<Character, List<Term>> indexAbjdMap = Quran.IN().index.indexMapToABJDTermsMap();
		for(Iterator<Character> it = indexAbjdMap.keySet().iterator(); it.hasNext();){
			Character letter= it.next();
			List<Term> terms = indexAbjdMap.get(letter);
			//goal.put(letter, words);
			JSONArray array = new JSONArray();
			for(Term term : terms){
				JSONObject jsonTerm = new JSONObject();
				JSONArray jsonWords = new JSONArray();
				for(Word word : term.words){
					jsonWords.add(word);
				}
				jsonTerm.put(term.jadr, jsonWords);
				array.add(jsonTerm);
			}//log.info("printABJDMapToJSON## "+array.toJSONString());
			goal.put(letter, array);
		}
		
		Utils.writeJSONFile(goal, fileName);
			
	}

	public void printABJDMapToFile(String fileName) throws Exception{
		List<String> lines = new ArrayList<String>();
		lines.add("## printing quranic index as ABJD");
		Map<Character, List<Word>> indexAbjdMap = Quran.IN().index.indexMapToABJDMap();
		for(Iterator<Character> it = indexAbjdMap.keySet().iterator(); it.hasNext();){
			Character letter= it.next();
			lines.add("character : "+ letter);
			List<Word> words = indexAbjdMap.get(letter);
			for(Word w : words){ 
				StringBuffer sb = new StringBuffer(w.kalima+":");
				for(Qr1nLoci loci : w.locis)  
					sb.append(loci.toString()+" ");  
				lines.add(sb.toString()); 
			} 
		}
		Utils.writeSimpleFile(lines, fileName);
	}


	public void printABJDTermsMapToFile(String fileName) throws Exception{
		List<String> lines = new ArrayList<String>();
		lines.add("## printing quranic index as ABJD");
		Map<Character, List<Term>> indexAbjdMap = Quran.IN().index.indexMapToABJDTermsMap();
		for(Iterator<Character> it = indexAbjdMap.keySet().iterator(); it.hasNext();){
			Character letter= it.next();
			lines.add("character : "+ letter);
			List<Term> terms = indexAbjdMap.get(letter);
			for(Term term: terms){ 
				lines.add("####"+term.jadr+"####");
				for(Word word : term.words){
					StringBuffer sb = new StringBuffer(word.kalima+":");
					for(Qr1nLoci loci : word.locis)  
						sb.append(loci.toString()+" ");  
					lines.add(sb.toString()); 
				}
			} 
		}
		Utils.writeSimpleFile(lines, fileName);
	}
	
	public void printStructure() {
		System.out.println("\n###### BEG. PRINT STRUCTURE #####");
		for(int i = 110; i<114; i++){
			List<Aaya> vList = Quran.IN().model.verseMap.get(i);
			System.out.println("Chapter "+i+" , verses: "+vList.size());
			for(Aaya v : vList){
				System.out.println(v.loci.idVerse+" : "+v.rawText);
			}
		
		}
		System.out.println("\n###### END. PRINT STRUCTURE #####");
	
	}
	
	

	/* ##########################			OUTPUT SECTION *###################*/
	
	public void exportAsSimpleFile(){
		
	}
	
	public void exportAsJSONFile(){
		
	}
}