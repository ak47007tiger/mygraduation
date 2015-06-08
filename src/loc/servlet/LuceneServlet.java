package loc.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loc.prepare.GetMainInfoHtml;
import loc.service.WorkSevice;

/**
 * Servlet implementation class LuceneServlet
 */
public class LuceneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LuceneServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("utf-8");
		WorkSevice workSevice = WorkSevice.getDefault();
		String want = request.getParameter("want");
		if(null != want){
			if(want.equals("statue")){
				OutputStream out = response.getOutputStream();
				out.write(("download work:" + workSevice.getDownloadWrokStatue()).getBytes("utf-8"));
				out.write(" -- ".getBytes("utf-8"));
				out.write(("build index work:" + workSevice.getIndexWorkStatue()).getBytes("utf-8"));
				out.write(" -- ".getBytes("utf-8"));
				out.write(("translate work:" + workSevice.getTranslateWorkStatue()).getBytes("utf-8"));
				out.close();
			}else if(want.equals("download")){
				OutputStream out = response.getOutputStream();
				if(workSevice.getDownloadWrokStatue() != WorkSevice.WORK_RUNNING){
					out.write("start download".getBytes("UTF-8"));
					out.close();
					workSevice.doDownload();
				}else{
					out.write("running".getBytes("utf-8"));
					response.getOutputStream().close();
				}
			}else if(want.equals("buildindex")){
				OutputStream out = response.getOutputStream();
				if(workSevice.getIndexWorkStatue() != WorkSevice.WORK_RUNNING){
					out.write("start build".getBytes("UTF-8"));
					out.close();
					workSevice.doIndex();
				}else{
					out.write("running".getBytes("utf-8"));
					out.close();
				}
			}else if(want.equals("translate")){
				OutputStream out = response.getOutputStream();
				if(workSevice.getTranslateWorkStatue() != WorkSevice.WORK_RUNNING){
					out.write("start translate".getBytes("utf-8"));
					out.close();
					workSevice.doTranslate();
				}else{
					out.write("running".getBytes("utf-8"));
					out.close();
				}
			}
		}
	}

}
