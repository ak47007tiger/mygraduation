package loc.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HuaWeiPhone {
	String id;
	String name;
	String price;
	String time;
	String url;
	String img;

	public static final String basUrl = "http://tech.sina.com.cn/mobile/models/";

	public static Document parseToDocument(File file) throws IOException {
		Document doc = new Document();
		String fileName = file.getName();
		FileReader fileReader = new FileReader(file);
		System.out.println(fileReader.getEncoding());
		doc.add(new Field("content", fileReader));
		doc.add(new Field("filename", fileName, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field("path", file.getAbsolutePath(), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		org.jsoup.nodes.Document htmlDoc = Jsoup.parse(file, "utf-8");
		Element img_element = htmlDoc.select(".main_l table img").get(0);
		Element main_r = htmlDoc.select(".main_r").get(0);
		Element ul = main_r.select("ul").get(0);
		String id = fileName
				.substring(0, file.getName().lastIndexOf("."));
		String name = img_element.attr("alt");
		String price = ul.child(0).select("span").get(0).text();
		Elements e = main_r.select("h3 span");
		String time = "不详";
		if(e.size() > 0){
			time = e.get(0).text();
		}
		String url = basUrl + file.getName();
		String img = img_element.attr("src");
		doc.add(new Field("id", id, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("name", name, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field("price", price, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field("time", time, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field("url", url, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field("img", img, Field.Store.YES, Field.Index.NOT_ANALYZED));
		return doc;
	}

	public static HuaWeiPhone parseToHuaWeiPhone(Document document) {
		HuaWeiPhone phone = new HuaWeiPhone();
		phone.id = document.get("id");
		phone.name = document.get("name");
		phone.price = document.get("price");
		phone.time = document.get("time");
		phone.url = document.get("url");
		phone.img = document.get("img");
		return phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
