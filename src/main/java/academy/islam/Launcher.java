package academy.islam; 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tmp.IndexFiles;
import tmp.SearchFiles;

public class Launcher {

	Logger log = LoggerFactory.getLogger(Launcher.class.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception { 
		Launcher exe = new Launcher();

		final String indexPath = "index";
		
		exe.runIndexing(indexPath);
		exe.runSearching(indexPath);
	}
	
	
	public Launcher() {
		log.info("Hello World");
	}
	
	public void runIndexing(String indexPath) throws IOException{
		
		final boolean create = true;
		final Path docDir = Paths.get("./src/main/resources/data");
		
		 System.out.println("Indexing to directory '" + indexPath + "'...");

	      Directory dir = FSDirectory.open(Paths.get(indexPath));
	      Analyzer analyzer = new StandardAnalyzer();					///do i have to ?
	      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

	      if (create) {
	        iwc.setOpenMode(OpenMode.CREATE);
	      } else {	        // Add new documents to an existing index:
	        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
	      }

	      IndexWriter writer = new IndexWriter(dir, iwc);
	      IndexFiles.indexDocs(writer, docDir);

	      writer.close();
 
	}
	
	public void runSearching(String index) throws Exception{

	    String field = "contents";
	    String queries = null;
	    int repeat = 0;
	    boolean raw = false;
	    String queryString = null;
	    int hitsPerPage = 10;
	    
	    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
	    IndexSearcher searcher = new IndexSearcher(reader);
	    Analyzer analyzer = new StandardAnalyzer();

	    BufferedReader in = null;
	    if (queries != null) {
	      in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
	    } else {
	      in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
	    }
	    QueryParser parser = new QueryParser(field, analyzer);
	    while (true) {
	      if (queries == null && queryString == null) {                        // prompt the user
	        System.out.println("Enter query: ");
	      }

	      String line = queryString != null ? queryString : in.readLine();

	      if (line == null || line.length() == -1) {
	        break;
	      }

	      line = line.trim();
	      if (line.length() == 0) {
	        break;
	      }
	      
	      Query query = parser.parse(line);
	      System.out.println("Searching for: " + query.toString(field));
	            
	      if (repeat > 0) {                           // repeat & time as benchmark
	        Date start = new Date();
	        for (int i = 0; i < repeat; i++) {
	          searcher.search(query, null, 100);
	        }
	        Date end = new Date();
	        System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
	      }

	      SearchFiles.doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

	      if (queryString != null) {
	        break;
	      }
	    }
	    reader.close();
	}


}
