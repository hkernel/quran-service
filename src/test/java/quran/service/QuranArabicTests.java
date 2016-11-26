package quran.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quran.Term;

import common.Arabic;

public class QuranArabicTests {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());

	//@TODO: take a file as input and compare it to output
	@Test
	public void testParseRoots(){
		String result = "سألتمونيها";
		String input = "سأل";
		Term output = Arabic.SVC.parseRoot(input);
		assertEquals(result, output.toString());
		log.info(output.toString());
	}
}
