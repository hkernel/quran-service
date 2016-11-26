package quran;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Utils;

public class Quran {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

//	private enum STATUS { EMPTY, IMPORT, INDEX } ;
//	private STATUS state;
	//members
	public Qr1nIndex index;
	public Qr1nModel model;
	public Qr1nIOAdapter adapter;
	public Qr1nQuery query;
	
	private static Quran ALM = new Quran();
	
	private Quran(){
		index =  new Qr1nIndex();
		model = new Qr1nModel();
		adapter = new Qr1nIOAdapter();
		query = new Qr1nQuery();
		
		//state = STATUS.EMPTY;	//do we really need this?
	}
	
	public static Quran IN(){
		return ALM;
	}
	public static Qr1nIOAdapter IO(){
		return ALM.adapter;
	}
	public static Qr1nQuery Q(){
		return ALM.query ;
	}
	

	/**
	 * reads the quran from source (simpleFile | DB)
	 * builds the index
	 * 
	 * @param config
	 * @throws Exception
	 */
	public static void setup(Properties config) throws Exception{

		String sourceFile = (String) config.get("sourceFile");
		String metadataFile = (String) config.get("metadataFile");
		
		if( Utils.nok(sourceFile))
			throw new Exception("Quran.Service:: sourceFile property invalid");
		
		Quran.IN().adapter.loadFromSimpleFile(sourceFile);
		//Qr1nReader.readMetaData(q, metadataFile);
		Quran.IN().buildIndex();
		
		Quran.IN().printStats();
	}
	
	
	public void buildIndex() throws Exception {
		index.build();
	}


	public void printStats(){
		System.out.println("chapter size is : "+model.verseTables.size()+" chapters");
		System.out.println("verse size is : "+model.allVerses.size()+" verses");
		System.out.println("index size is : "+index.words.size()+" words");
	}

	/**
	 * In memory representation, could be switched to DB
	 * if persisted, then the data must be imported first.
	 */
	public class Qr1nModel{
		//all aaya altogether, used by 
		public List<Aaya> allVerses = new ArrayList<Aaya>();
		
		//aaya per chapter, as a map
		public Map<Integer, List<Aaya>> verseMap = new HashMap<Integer, List<Aaya>>();
		//aaya per chapter, as a list
		public List<List<Aaya>> verseTables = new ArrayList<List<Aaya>>();  
		
		
		public List<Aaya> getVersesBetween(Point p1, Point p2){ 
			List<Aaya> result = new ArrayList<Aaya>();
			//case of verseTables
			
			return result;
		}
//		public List<Aaya> getVerse(Point p){ 
//		}
//		public List<Aaya> getVersesAt(List<Point> points){
//		}
		public Map<Integer, List<Aaya>> getModelAsMap(){
			return verseMap;
		}
		public List<List<Aaya>> getModelAsTable(){
			return verseTables;
		}
	}


}
