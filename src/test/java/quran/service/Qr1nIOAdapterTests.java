package quran.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import quran.Launcher;
import quran.Qr1nIOAdapter;
import quran.Quran;

public class Qr1nIOAdapterTests {

	Properties props;
	
	@Before
	public void configure(){
		props = Launcher.loadConfig();
	}
	@Test
	public void loadFromSimpleFileTest() throws IOException{
		String fileName = props.getProperty("sourceFile");
		new Qr1nIOAdapter().loadFromSimpleFile(fileName);
		assertEquals(Quran.IN().model.verseTables.size(), 			114);
		assertEquals(Quran.IN().model.verseMap.entrySet().size(), 	114);
		assertEquals(Quran.IN().model.allVerses.size(), 			6236);
	}
}
