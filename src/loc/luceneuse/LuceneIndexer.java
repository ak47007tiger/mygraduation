package loc.luceneuse;

import loc.model.HuaWeiPhone;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

public class LuceneIndexer {
	public LuceneIndexer() {
	}

	IndexWriter writer;

	public void init(File indexDirPath) throws IOException {
		Directory indexDir = FSDirectory.open(indexDirPath);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,
				new StandardAnalyzer(Version.LUCENE_35));
		writer = new IndexWriter(indexDir, iwc);
	}

	public void indexByRoot(File root) throws IOException {
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				indexByRoot(file);
			} else {
				Document doc;
				doc = HuaWeiPhone.parseToDocument(file);
				writer.addDocument(doc);
			}
		}
	}

	public void endWork() throws IOException {
		writer.close();
	}

	public static void main(String[] args) {
		File indexDir = new File(System.getProperty("user.dir") + "/lucene/index");
        LuceneIndexer luceneIndexer = new LuceneIndexer();
        try {
            luceneIndexer.init(indexDir);
            File rootDirStayIndex = new File(System.getProperty("user.dir") + "/lucene/example");
            luceneIndexer.indexByRoot(rootDirStayIndex);
            luceneIndexer.endWork();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
