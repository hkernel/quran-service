package quran;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Word implements Comparable<Word>{

	public String kalima; 
	public String root;					//actually a reference
	public List<Qr1nLoci> locis;		//list of locations of this word (index)
	
	//never accessible
	private Word(String text){
		kalima = text;
		locis = new ArrayList<Qr1nLoci>();
	}
	
	public Word(String text, Qr1nLoci loci){
		this(text);
		locis.add(loci);
	}
	
	public Word(String text, List<Qr1nLoci> list) {
		kalima = text;
		locis = list;
	}
	 
	public Map<String, List<String>> toStringMap(){
		HashMap<String, List<String>> map = new HashMap();
		map.put(kalima, Qr1nLoci.toListString(locis));
		return map;
	}

	public StringBuffer toStringBuffer(){
		StringBuffer sb = new StringBuffer();
		sb.append(kalima);
		sb.append(":"+locis.size()+":");
		for(Qr1nLoci loci : locis)
			sb.append(loci.toString()+" ");
		return sb;
	}
 
	public JSONObject toJSONObject(){
		JSONObject json = new JSONObject();
		json.put("word", kalima);
		JSONArray array = new JSONArray();
		for(Qr1nLoci loci:locis)
			array.add(loci);
		json.put("locis", array);
		return json;
	}
	@Override
	public int compareTo(Word other) {
		return this.kalima.compareTo(other.kalima);
	}
 
	
	class LociComparator implements Comparator<Word>{

		@Override
		public int compare(Word word1, Word word2) {
			Integer size1 = word1.locis.size();
			Integer size2 = word2.locis.size();
			return size1.compareTo(size2);
		}
		
	}
}
