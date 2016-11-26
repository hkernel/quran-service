package academy.islam;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Properties;

import org.junit.Test;

public class LauncherTests {

	@Test
	public void testConfigFileOK(){
		Properties p = quran.Launcher.loadConfig();
		assertEquals(p.isEmpty(),false);
	}
	
	@Test
	public void testConfigFileContentOK(){
		Properties p = quran.Launcher.loadConfig();
		for(Iterator it = p.keySet().iterator(); it.hasNext();){
			String key = (String) it.next();
			if(key.contains("File")){
				String value = p.getProperty(key);
				File file = new File(value);
				assertEquals(file.canRead(), true);
			}
		} 
		
	}
	
	
}
