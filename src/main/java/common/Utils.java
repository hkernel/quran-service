package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Utils {


	
	public static void main(String[] args) throws IOException{
		StringWriter out = new StringWriter();
        
		  JSONObject obj = new JSONObject();
		  LinkedHashMap m1 = new LinkedHashMap();
		  LinkedList l1 = new LinkedList();
		  obj.put("k1", "v1");
		  obj.put("k2", m1);
		  obj.put("k3", l1);
		  m1.put("mk1", "mv1");
		  l1.add("lv1");
		  l1.add("lv2");
		  m1.put("mk2", l1);
		        
		  obj.writeJSONString(out);
		  System.out.println("jsonString:");
		  System.out.println(out.toString());
		  String jsonString = obj.toJSONString();
		  System.out.println(jsonString);
	}


	public static String toString(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for(String s : list)
			sb.append(s+" , ");
		sb.replace(sb.length(), sb.length(), "");
		return sb.toString();
			
	}

	/**
	 * Used to determine if an object is valid, at least not null
	 * @param sourceFile
	 * @return false if not valid
	 * 
	 * 
	 * @TODO: extend this to return all possible cases (null, empty)
	 * possibly by rewriting as 
	 * 		boolean CHK(Object obj, Checker checker) where the checker is an adapter(?) 
	 * 		that knows how to check something 
	 */
	public static boolean nok(Object target) {
		if(target == null) return true;
		if(target instanceof String){
			String starget = (String)target;
			if(starget.isEmpty()) return true;
		}
		if(target instanceof Object[]  || target.getClass().isArray()){
			Object[] atarget = (Object[]) target;
			if(atarget.length == 0) return true;
		}
		return false;
	}
	public static boolean ok(Object target){
		return !nok(target);
	}
	
	
	public static void writeSimpleFile(List<String> lines, String fileName) throws IOException{
		File file = new File(fileName);
		if(file.exists()==false) file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		for(String line : lines)
			pw.println(line);
		pw.close();
	}
	
	public static void writeJSONFile(JSONObject goal, String fileName) throws Exception{
//		File file = new File(fileName);
//		if(file.exists()==false) file.createNewFile();
//		PrintWriter pw = new PrintWriter(new FileWriter(file));
//		goal.writeJSONString(pw);
//		pw.close();
		writeSimpleFile(cleanJSON(goal), fileName);
	}
	
	private static List<String> cleanJSON(JSONObject goal) throws Exception{
		List<String> lines = new ArrayList<String>();
		for(Iterator it = goal.keySet().iterator(); it.hasNext();){
			Object key = it.next();
			Object value = goal.get(key);
			if(value instanceof JSONObject){
				JSONObject json = (JSONObject)value;
				lines.addAll(cleanJSON(json));		//toupdate
			}else if (value instanceof JSONArray){
				JSONArray jsons = (JSONArray)value;
				for(Object o : jsons){
					if(o instanceof JSONObject){
						JSONObject gson = (JSONObject)o;
						lines.addAll(cleanJSON(gson));		//toupdate
					}else
						lines.add(o.toString());
				}
			}else
				lines.add(value.toString());
		}
		return lines;
	}

	public static List<String> readSimpleFile(String fileName) throws IOException{
		
		Scanner scan = new Scanner(new FileInputStream(new File(fileName)));
		String line = null;
		List<String> lines = new ArrayList<String>();
		while(scan.hasNext()){
			line = scan.nextLine();
			if(line.startsWith("#")==false)
				lines.add(line);
			else
				System.out.println(line);
		}
		
		System.out.println("size is "+lines.size());
		return lines;
	}
 
}
