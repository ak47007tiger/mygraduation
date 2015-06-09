package loc.luceneuse;

import loc.model.HuaWeiPhone;
import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LuceneSearcher {
	QueryParser parser;
	IndexSearcher searcher;
	IndexReader reader;

	public void init(File indexDir) throws IOException {
		Directory directory = FSDirectory.open(indexDir);
		reader = IndexReader.open(directory);
		searcher = new IndexSearcher(reader);
		parser = new QueryParser(Version.LUCENE_35, "content",
				new PaodingAnalyzer());
	}

	public QueryParser getDefaultParser() {
		return parser;
	}

	public Query buildQuery(String keyWord, QueryParser parser)
			throws ParseException {
		Query query = parser.parse(keyWord);
		return query;
	}

	public Query buildQuery(String keyWord) throws ParseException {
		Query query = parser.parse(keyWord);
		return query;
	}

	public List<HuaWeiPhone> search(Query query, int n) throws IOException {
		List<HuaWeiPhone> docs = new LinkedList<>();
		TopDocs tds = searcher.search(query, n);
		ScoreDoc[] sds = tds.scoreDocs;
		for (ScoreDoc sd : sds) {
			Document document = searcher.doc(sd.doc);
			docs.add(HuaWeiPhone.parseToHuaWeiPhone(document));
		}
		return docs;
	}
	public ScoreDoc[] search_(Query query, int n) throws IOException {
		TopDocs tds = searcher.search(query, n);
		return  tds.scoreDocs;
	}
	public List<HuaWeiPhone> page(int curPage,int pageSize,ScoreDoc[] sds) throws CorruptIndexException, IOException{
		List<HuaWeiPhone> beanList = new LinkedList<>();
		int i = (curPage - 1) * pageSize;
		int count = curPage * pageSize;
		count = count <= sds.length ? count : sds.length;
		for(; i < count; i++){
			ScoreDoc sd = sds[i];
			Document document = searcher.doc(sd.doc);
			beanList.add(HuaWeiPhone.parseToHuaWeiPhone(document));
		}
		return beanList;
	}
	
	
	public List<HuaWeiPhone> search(Query query, int n, int page, int pageSize) throws IOException {
		List<HuaWeiPhone> docs = new LinkedList<>();
		TopDocs tds = searcher.search(query, n);
		ScoreDoc[] sds = tds.scoreDocs;
		int i = (page - 1) * pageSize;
		for(; i < page * pageSize; i++){
			ScoreDoc sd = sds[i];
			Document document = searcher.doc(sd.doc);
			docs.add(HuaWeiPhone.parseToHuaWeiPhone(document));
		}
		return docs;
	}
	

	public void endWork() throws IOException {
		reader.close();
	}

	public static void main(String[] args) {
		File indexDir = new File(System.getProperty("user.dir") + "/lucene/index");
        LuceneSearcher luceneSearcher = new LuceneSearcher();
        try {
            luceneSearcher.init(indexDir);
            String keyWord = "string in";
            luceneSearcher.search(luceneSearcher.buildQuery(keyWord),100);
            luceneSearcher.endWork();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
}
