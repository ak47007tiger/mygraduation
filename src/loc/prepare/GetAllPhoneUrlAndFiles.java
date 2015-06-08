package loc.prepare;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetAllPhoneUrlAndFiles {

	final String url_module_reg = "http://tech.sina.com.cn/mobile/models/[0-9]+.html";
	final String url_detail_reg = "http://tech.sina.com.cn/mobile/models/[0-9]+/detail.shtml";
	final String url_list_reg = "http://data.tech.sina.com.cn/mobile/mobile_search.php\\?brand=%E5%8D%8E%E4%B8%BA\\&ampsort=1\\&p=[0-9]+";
	Stack<String> urlList_over = new Stack<String>();
	Stack<String> urlList_stay = new Stack<String>();
	List<String> list_detail = new ArrayList<String>();
	List<String> list_module = new ArrayList<String>();
	int count = 0;

	public void parseUrls(String url_start) {
		Document doc = null;
		String linkHref;
		if (!urlList_over.contains(url_start)) {
			System.out.println("new document, count = " + count);
			boolean success = false;
			while (!success) {
				try {
					doc = Jsoup.connect(url_start).get();
					success = true;
				} catch (IOException e) {
					e.printStackTrace();
					success = false;
				}
			}
			Elements links = doc.select("a");
			for (Element link : links) {
				linkHref = link.attr("href");
				System.out.println(linkHref);
				if (linkHref.length() > 0) {
					if (linkHref.matches(url_list_reg)) {
						urlList_stay.push(linkHref);
						continue;
					} else if (linkHref.matches(url_module_reg)) {
						String detail_href = linkHref.replace(".html",
								"/detail.shtml");
						if (!list_detail.contains(detail_href)) {
							list_detail.add(detail_href);
							list_module.add(linkHref);
						}
					}
				}
			}
			urlList_over.push(url_start);
			count++;
		}
		if (urlList_stay.size() > 0) {
			parseUrls(urlList_stay.pop());
		}
	}

	List<String> url_list = new ArrayList<String>();
	Stack<String> url_list_stay = new Stack<>();
	Stack<String> url_list_over = new Stack<>();

	public void parseUrlList(String url) throws IOException {
		url_list_stay.push(url);
		while (url_list_stay.size() > 0) {
			String parsing = url_list_stay.pop();
			if (parsing.matches(url_list_reg)) {
				Document doc = Jsoup.connect(parsing).get();
				Elements links = doc.select("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					if (!url_list_over.contains(linkHref)
							&& linkHref.matches(url_list_reg)) {
						System.out.println(linkHref);
						url_list_stay.push(linkHref);
					}
				}
				url_list_over.push(parsing);
			}
		}
	}

	public void printListToFile(String locPath, List<String> list) {
		try {
			File file = new File(locPath);
			BufferedOutputStream buffered_out = new BufferedOutputStream(
					new FileOutputStream(file));
			for (String detail : list) {
				buffered_out.write(detail.getBytes("utf-8"));
				buffered_out.write("\r\n".getBytes("utf-8"));
			}
			buffered_out.flush();
			buffered_out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void work() {
		/*
		 * try { parseUrlList(
		 * "http://data.tech.sina.com.cn/mobile/mobile_search.php?brand=%E5%8D%8E%E4%B8%BA&ampsort=1&p=1"
		 * ); } catch (IOException e) { e.printStackTrace(); }
		 */
		parseUrls("http://data.tech.sina.com.cn/mobile/mobile_search.php?brand=%E5%8D%8E%E4%B8%BA&ampsort=1&p=1");
		/*
		 * printListToFile("E:/graduation/url_details.txt", list_detail);
		 * printListToFile("E:/graduation/url_module.txt", list_module);
		 * printListToFile("E:/graduation/url_list.txt", urlList_over);
		 */
		String realPath = getClass().getClassLoader().getResource("/")
				.getPath();
		File f = new File(realPath);
		File project = f.getParentFile().getParentFile();
		File lucene = new File(project, "lucene");
		File files = new File(lucene, "files");
		// 原网页是gbk，所以用gbk解码，然后用utf-8编码重新写文件
		try {
			downloadFiles(list_module, files, "gbk");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadFiles(List<String> list, File dir, String charset) throws MalformedURLException, IOException {
		byte[] buffer;
		String line;
		HttpURLConnection huc;
		BufferedReader br = null;
		String fileName;
		BufferedOutputStream bout = null;
		byte[] end = null;
		try {
			end = "\r\n".getBytes("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		for (String url : list) {
			huc = (HttpURLConnection) new URL(url).openConnection();
			System.out.println("download-" + huc.getContentEncoding());
			br = new BufferedReader(new InputStreamReader(huc.getInputStream(),
					charset));
			fileName = url.substring(url.lastIndexOf("/") + 1);
			bout = new BufferedOutputStream(new FileOutputStream(new File(dir,
					fileName)));

			while ((line = br.readLine()) != null) {
				buffer = line.getBytes("utf-8");
				bout.write(buffer, 0, buffer.length);
				bout.write(end, 0, end.length);
			}
			br.close();
			bout.close();
		}
	}

	public void downloadFiles(List<String> list, File dir) {
		byte[] buffer = new byte[1024 * 1024 * 5];
		int count;
		HttpURLConnection huc;
		BufferedInputStream bin = null;
		String fileName;
		BufferedOutputStream bout = null;
		for (String url : list) {
			try {
				huc = (HttpURLConnection) new URL(url).openConnection();
				System.out.println("download-" + huc.getContentEncoding());
				bin = new BufferedInputStream(huc.getInputStream());
				fileName = url.substring(url.lastIndexOf("/") + 1);
				bout = new BufferedOutputStream(new FileOutputStream(new File(
						dir, fileName)));

				while ((count = bin.read(buffer)) > 0) {
					bout.write(buffer, 0, count);
				}
				bin.close();
				bout.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != bin) {
					try {
						bin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != bout) {
					try {
						bout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// BufferedReader br = new BufferedReader(new
	// InputStreamReader(huc.getInputStream(), "gbk"));
	public static void main(String[] args) {
		GetAllPhoneUrlAndFiles getter = new GetAllPhoneUrlAndFiles();
		getter.work();
	}
}
