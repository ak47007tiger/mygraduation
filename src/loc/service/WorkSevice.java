package loc.service;

import java.io.File;
import java.io.IOException;

import loc.luceneuse.LuceneIndexer;
import loc.luceneuse.LuceneSearcher;
import loc.prepare.GetAllPhoneUrlAndFiles;
import loc.prepare.GetMainInfoHtml;

public class WorkSevice {

	private static WorkSevice workSevice;
	public static int WORK_NOTSTART = -1;// 没开始
	public static int WORK_RUNNING = 0;// 运行中
	public static int WORK_END = 1;// 运行结束
	int index_workStatus = WORK_NOTSTART;
	int download_workStatus = WORK_NOTSTART;
	int translate_workStatus = WORK_NOTSTART;
	Object lock = new Object();

	GetMainInfoHtml getMainInfoHtml = new GetMainInfoHtml();
	public int getIndexWorkStatue() {
		return index_workStatus;
	}

	public int getDownloadWrokStatue() {
		return download_workStatus;
	}
	
	public int getTranslateWorkStatue(){
		return translate_workStatus;
	}
	
	public void doTranslate(){
		translate_workStatus = WORK_RUNNING;
		try {
			getMainInfoHtml.work();
		} catch (IOException e) {
			e.printStackTrace();
			translate_workStatus = WORK_NOTSTART;
		}
		translate_workStatus = WORK_END;
	}

	static {
		workSevice = new WorkSevice();
	}

	private WorkSevice() {
	}

	public static WorkSevice getDefault() {
		return workSevice;
	}

	public void doDownload() {
		download_workStatus = WORK_RUNNING;
		String realPath = getClass().getClassLoader().getResource("/")
				.getPath();
		File f = new File(realPath);
		File project = f.getParentFile().getParentFile();
		File lucene = new File(project, "lucene");
		new File(lucene, "files").deleteOnExit();
		// 执行下载
		new GetAllPhoneUrlAndFiles().work();
		download_workStatus = WORK_END;
	}

	public void doIndex() {
		index_workStatus = WORK_RUNNING;
		String realPath = getClass().getClassLoader().getResource("/")
				.getPath();
		File f = new File(realPath);
		File project = f.getParentFile().getParentFile();
		File lucene = new File(project, "lucene");
		new File(lucene, "index").deleteOnExit();
		File index = new File(lucene, "index");
		// 构建索引
		LuceneIndexer luceneIndexer = new LuceneIndexer();
		try {
			luceneIndexer.init(index);
			File files = new File(lucene, "files");
			luceneIndexer.indexByRoot(files);
			luceneIndexer.endWork();
		} catch (IOException e) {
			e.printStackTrace();
			synchronized (lock) {
				index_workStatus = WORK_END;
			}
			return;
		}
		// 构建索引搜索器
		LuceneSearcher luceneSearcher = new LuceneSearcher();
		try {
			luceneSearcher.init(index);
		} catch (IOException e) {
			e.printStackTrace();
			synchronized (lock) {
				index_workStatus = WORK_END;
			}
			return;
		}
		SearchService searchService = SearchService.getDefault();
		searchService.setLuceneSearcher(luceneSearcher);
		index_workStatus = WORK_END;
	}
}
