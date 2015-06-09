package loc.functest;

import java.io.IOException;
import java.io.StringReader;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class TestPaoding {

	public static void main(String[] args) throws IOException {
		Analyzer analyzer = new PaodingAnalyzer();
//		analyzer = new StandardAnalyzer(Version.LUCENE_35);
		String testStr = "3G联通版电信版01月Mate7标配版";
		StringReader sr = new StringReader(testStr);
		TokenStream tokenStream = analyzer.tokenStream(testStr, sr);
		CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
//		tokenStream.reset();
		System.out.println(tokenStream.incrementToken());
		while(tokenStream.incrementToken()){
			System.out.println(attr.toString());
		}
	}
}
