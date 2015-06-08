package loc.prepare;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetMainInfoHtml {

	public void work() throws IOException{
		String realPath = getClass().getClassLoader().getResource("/")
				.getPath();
		File f = new File(realPath);
		File project = f.getParentFile().getParentFile();
		File lucene = new File(project, "lucene");
		File dir = new File(lucene, "files");
		File[] files = dir.listFiles();
		List<String> deleteStay = new ArrayList<String>();
		for(File src : files){
			String name = src.getName();
			String newName = "all" + name;
			File newFile = copyFile(newName, src, dir);
			translate(newFile, src, "utf-8");
			deleteStay.add(newFile.getPath());
		}
		for(String path : deleteStay){
			new File(path).delete();
		}
	}
	public File copyFile(String name,File file,File parent) throws IOException{
		File newFile = new File(parent,name); 
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(newFile));
		byte[] buffer = new byte[1024 * 1024 * 10];
		int count;
		while((count = bin.read(buffer)) > 0){
			bout.write(buffer, 0, count);
		}
		bin.close();
		bout.close();
		return newFile;
	}
	public void translate(File src, File dst,String charset) throws IOException{
		Document doc_src = Jsoup.parse(src, charset);
		Elements elements = doc_src.select(".main");
		if(elements.size() > 0){
			String html = elements.get(0).html();
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(dst));
			bout.write(html.getBytes(charset));
			bout.flush();
			bout.close();
		}
	}
}
