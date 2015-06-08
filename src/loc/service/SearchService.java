package loc.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import loc.luceneuse.LuceneSearcher;
import loc.model.HuaWeiPhone;
import loc.model.PageBeen;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;

public class SearchService {

	//file:/D:/codingtools/tomcat/apache-tomcat-7.0.61/webapps/uselucene/WEB-INF/classes/
	LuceneSearcher luceneSearcher;
	
	public LuceneSearcher getLuceneSearcher() {
		return luceneSearcher;
	}
	public void setLuceneSearcher(LuceneSearcher luceneSearcher) {
		this.luceneSearcher = luceneSearcher;
	}
	private SearchService(){
	}
	private static SearchService service;
	static{
		service = new SearchService();
	}
	public static SearchService getDefault(){
		return service;
	}
	public PageBeen doSearch(HttpServletRequest request){
		PageBeen pageBeen = null;
		int curPage = Integer.parseInt(request.getParameter("curPage"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String keyWord = request.getParameter("keyWord");
		System.out.println(keyWord);
		try {
			keyWord = URLDecoder.decode(keyWord, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		System.out.println(keyWord);
		try {
			ScoreDoc[] sds = luceneSearcher.search_(luceneSearcher.buildQuery(keyWord), 1000);
			List<HuaWeiPhone> list = luceneSearcher.page(curPage, pageSize, sds);
			pageBeen = new PageBeen();
			pageBeen.setCurPage(curPage);
			pageBeen.setPageSize(pageSize);
			pageBeen.setData(list);
			int pageCount = sds.length % pageSize == 0 ? sds.length / pageSize : sds.length / pageSize + 1;
			pageBeen.setPageCount(pageCount);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pageBeen;
	}
}
