package tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetCodec;

import tmall.bean.Category;
import tmall.dao.CategoryDAO;
import tmall.util.Page;

/**
 * Servlet implementation class CategoryServlet
 */
@WebServlet("/CategoryServlet")
public class CategoryServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CategoryServlet() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public String add(HttpServletRequest req, HttpServletResponse res, Page page) {
		// TODO 自动生成的方法存根
		Map<String, String> params = new HashMap<>();
		InputStream is = parseUpload(req, params);
		
		String name = params.get("name");
		Category category = new Category();
		category.setName(name);
		categoryDAO.add(category);
		
		File imageFolder = new File(req.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, category.getId()+".jpg");
		try {
			if (is != null && is.available() != 0) {
				try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
					byte b[] = new byte[1024 * 1024];
					int length = 0;
					while ((length = is.read(b)) != -1) {
						fileOutputStream.write(b, 0, length);
					}
					fileOutputStream.flush();
					//把文件保存为jpg
					BufferedImage image = ImageUtil.change2jpg(file);
					ImageIO.write(image, "jpg", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "@admin_category_list";
	}

	@Override
	public String delete(HttpServletRequest req, HttpServletResponse res, Page page) {
		// TODO 自动生成的方法存根
		int id = Integer.parseInt(req.getParameter("id"));
		categoryDAO.delete(id);
		return "@admin_category_list";
	}

	@Override
	public String edit(HttpServletRequest req, HttpServletResponse res, Page page) {
		// TODO 自动生成的方法存根
		int id = Integer.parseInt(req.getParameter("id"));
		Category category = categoryDAO.get(id);
		req.setAttribute("category", category);
		return "admin/editCategory.jsp";
	}

	@Override
	public String update(HttpServletRequest req, HttpServletResponse res, Page page) {
		// TODO 自动生成的方法存根
		Map<String, String> params = new HashMap<>();
		InputStream is = parseUpload(req, params);
		
		System.out.println("params");
		String name = params.get("name");
		int id = Integer.parseInt(params.get("id"));
		
		Category category = new Category();
		category.setId(id);
		category.setName(name);
		categoryDAO.update(category);
		
		File imageFolder = new File(req.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, category.getId() + ".jpg");
		file.getParentFile().mkdirs();
		try {
			if (is != null && is.available() != 0) {
				try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
					byte b[] = new byte[1024 * 1024];
					int length = 0;
					while ((length = is.read(b)) != -1) {
						fileOutputStream.write(b, 0, length);
					}
					fileOutputStream.flush();
					BufferedImage image = ImageUtil.change2jpg(file);
					ImageIO.write(image, "jpg", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "@admin_category_list";
	}

	@Override
	public String list(HttpServletRequest req, HttpServletResponse res, Page page) {
		// TODO 自动生成的方法存根
		List<Category> cs = categoryDAO.list(page.getStart(), page.getCount());
		int total = categoryDAO.getTotal();
		page.setTotal(total);
		
		req.setAttribute("thecs", cs);
		req.setAttribute("page", page);
		return "admin/listCategory.jsp";
	}

}
