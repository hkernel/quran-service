package quran;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Arabic;


public class Launcher {

	Logger log = LoggerFactory.getLogger(Launcher.class.getName());

	public static void main(String[] args) throws Exception{
		
		Properties config = loadConfig();

		Arabic.setup(config);
		Quran.setup(config);
		
		Quran.IO().printIndexWordListToFile("output/words.txt");
		Quran.IO().printIndexMapToFile("output/index.txt");
//		Quran.IO().printIndexMapToFileSortByLocation("output/index2.txt");
//		Quran.IO().printABJDMapToFile("output/abjdFile.txt");
		//Quran.IO().printABJDMapToJSON("output/abjdJSON.txt");
//		
//		Quran.IO().printABJDTermsMapToFile("output/abjdTerms.txt");
//		
//		Quran.IO().printStructure(); 
		
		//Quran.Q().searchF2F("input/search1.txt", "output/result1.txt");
//		Quran.Q().searchF2F("input/search2.txt", "output/result2.txt");
		
	}
	
	public static Properties loadConfig(){
		Properties p = new Properties();
		try {
			p.load(new FileReader("src/main/resources/data/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static void printConfig(Properties p){

		for(Iterator it = p.keySet().iterator(); it.hasNext();){
			String key = (String) it.next();
			String value = p.getProperty(key);
			System.out.println(key+":"+value);
		}
		
	}
}
