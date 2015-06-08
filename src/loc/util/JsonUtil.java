package loc.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

	public static void write(Object object, String format, OutputStream out,String charset) {
		BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
		try {
			out.write(toJsonString(object, format).getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != bufferedOut){
				try {
					bufferedOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void write(String jsonString, OutputStream out,String charset) {
		BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
		try {
			out.write(jsonString.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != bufferedOut){
				try {
					bufferedOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void write(Object object, OutputStream out,String charset) {
		BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
		try {
			out.write(toJsonString(object).getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != bufferedOut){
				try {
					bufferedOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String toJsonString(Object object, String format) {
		return JSON.toJSONStringWithDateFormat(object, format);
	}

	public static String toJsonString(Object object) {
		return JSON.toJSONString(object);
	}
}
