package quran;

import java.util.ArrayList;
import java.util.List;

/**
 * A Verse
 * @author reuse
 *
 */
public class Aaya {

	public String rawText = null;			//this is the original line of text
	public Qr1nLoci loci = null; 
	List<Word> words = null;
	 
	public Aaya(String verse, Integer ivs, Integer ich) {
		loci = new Qr1nLoci();
		loci.idVerse = ivs;
		loci.idChapter = ich;

		rawText = verse;
		words = new ArrayList<Word>();
		String[] parts = rawText.split("\\s");
		
		for(String part : parts){
			Qr1nLoci loci = Qr1nLoci.parseLocation(this, part);
			Word w = new Word(part, loci);
			words.add(w);
		}
	}
	
 
 
	
}
