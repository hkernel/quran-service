package quran;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import common.MyObject;


/**
 * Represents a location within the qur'an, a coordinate system
 * 	Point (X, Y, Z), such that : 
 * 		X: chapter
 * 		Y: verse
 * 		Z: word
 * 
 * Depending on the context (zoom), 
 *  a location is at least the chapter id, 
 *  and most precisely the position of the first letter of the word within the verse within the chapter
 * @author reuse
 *
 */
public class Qr1nLoci extends MyObject {

	public enum CONTEXT {WORD, VERSE, CHAPTER, BOOK};
	
	public static Qr1nLoci parseLocation(Aaya verse, String word){
		Qr1nLoci loci = new Qr1nLoci();
		loci = verse.loci.clone();
		loci.idWord = verse.rawText.indexOf(word);
		return loci;
	}
//	public Qr1nLoci(Aaya v, String word) {
//		idChapter = v.p.idChapter;
//		idVerse = v.p.idVerse;
//	}
	public Integer idChapter 	= -1;	//location of the chapter
	public Integer idVerse 		= -1;	//location of the verse in chapter
	public Integer idVerseTotal = -1;	//location of the verse in the quran
	public Integer idWord 		= -1;	//location of the word in the verse
	public Integer idWordChapter = -1;  //location of the word in chapter
	public Integer idWordTotal = -1;	//location of teh word in the quran
	public String hash = null;
	
	
	
	public Qr1nLoci clone(){
		Qr1nLoci myClone = new Qr1nLoci();
		myClone.idChapter = new Integer(this.idChapter);
		myClone.idVerse = new Integer(this.idVerse);
		myClone.idVerseTotal = new Integer(this.idVerseTotal);
		myClone.idWord = new Integer(this.idWord);
		myClone.idWordChapter = new Integer(this.idWordChapter);
		myClone.idWordTotal = new Integer(this.idWordTotal);
		return myClone;
	}
	
	public String toString(){
		//return "("+idChapter+","+idVerse+")";
		return Integer.toString(idChapter*1000+idVerse);
	}
	public Point toPoint(){
		return new Point(idChapter, idVerse);
	}
	
	//this method differs depending on context
	@Deprecated
	public String toString(CONTEXT CTX){
		switch(CTX){
			case WORD:
				return "["+idChapter+"["+idVerse+"@"+idWord+"]"+"]";
			case VERSE:
				return "["+idChapter+"["+idVerse+"]"+"]"; 
			case CHAPTER:
				return "["+idChapter+"]"; 
			default:
			return null;
		}
	}
	

	public static List<String> toListString(List<Qr1nLoci> locis) {
		List<String> goal = new ArrayList<String>();
		for(Qr1nLoci l : locis)
			goal.add(l.toString());
		return goal;
	}

	//minimum requirements for validity, 
	//it could depend on the context, for example, in the case of a word, the loci is only valid if index in phrase
	@Override
	public boolean isValid() {
		return this.idChapter > 0 && this.idVerse > 0;
	}
}	
