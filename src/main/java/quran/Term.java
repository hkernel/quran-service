package quran;

import java.util.ArrayList;
import java.util.List;

public class Term implements Comparable<Term>{

	String jadr = null;
	protected List<Word> words = null;
	
	public Term(String root){
		jadr = root;
		words = new ArrayList<Word>();
	}
	
	public String toString(){
		return jadr;
	}

	@Override
	public int compareTo(Term other){
		return jadr.compareTo( other.jadr );
	}
 
}
