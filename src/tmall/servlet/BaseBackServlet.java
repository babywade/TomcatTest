package tmall.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import tmall.util.Page;
import tmall.dao.*;

/**
 * Servlet implementation class BaseBackServlet
 */
@WebServlet("/BaseBackServlet")
public abstract class BaseBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public abstract String add(HttpServletRequest req, HttpServletResponse res, Page page);
	public abstract String delete(HttpServletRequest req, HttpServletResponse res, Page page);
	public abstract String edit(HttpServletRequest req, HttpServletResponse res, Page page);
	public abstract String update(HttpServletRequest req, HttpServletResponse res, Page page);
	public abstract String list(HttpServletRequest req, HttpServletResponse res, Page page);

    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();
    
    public void service(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		int start = 0;
    		int count = 5;
    		try {
    			start = Integer.parseInt(request.getParameter("page.start"));
    		} catch (Exception e) {
    		}
    		try {
    			count = Integer.parseInt(request.getParameter("page.count"));
    		} catch (Exception e) {
    		}
    		Page page = new Page(start, count);
    		//反射
    		String method = (String) request.getAttribute("method");
            Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequestWrapper.class, 
            		javax.servlet.http.HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, request, response, page).toString();
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new RuntimeException(e);
    	}
    	
    }
    
    public InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
    	//传递对象，用对象的方法可以修改这个对象并返回。
    	ServletRequestContext requestContext = new ServletRequestContext(request);
    	InputStream is = null;
    	try {
    		DiskFileItemFactory factory = new DiskFileItemFactory();
    		factory.setSizeThreshold(1024 * 10240);
    		ServletFileUpload upload = new ServletFileUpload(factory);
    		
    		List<FileItem> items = upload.parseRequest(requestContext);
    		Iterator<FileItem> iter = items.iterator();
    		while (iter.hasNext()) {
    			FileItem item = (FileItem) iter.next();
    			if (!item.isFormField()) {
    				is = item.getInputStream();
    			} else {
    				String paramName = item.getFieldName();
    				String paramValue = item.getString();
    				paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
    				//修改params对象。
    				params.put(paramName, paramValue);
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return is;
    }
}
