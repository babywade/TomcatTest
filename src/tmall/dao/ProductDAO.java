package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ProductDAO {
	public int getTotal(int cid) {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT COUNT(*) FROM Product WHERE cid = " + cid;
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public void add(Product bean) {
		String sql = "INSERT INTO Product VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOriginalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6, bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.execute();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "DELETE FROM Product WHERE id = " + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Product bean) {
		String sql = "UPDATE Product SET name = ?, subTitle = ?, originalPrice = ?, promotePrice = ?, stock = ?, cid = ?, createDate = ? WHERE id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOriginalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6, bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(8, bean.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Product get(int id) {
		Product bean = null;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT * FROM Product WHERE id = " + id;
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				bean = new Product();
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Category category = new CategoryDAO().get(cid);
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setId(id);
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<Product> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<Product> list(int start, int count) {
		List<Product> beans = new ArrayList<Product>();
		String sql = "SELECT * FROM Product ORDER BY id DESC LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, start);
			ps.setInt(2, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product bean = new Product();
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Category category = new CategoryDAO().get(cid);
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				int id = rs.getInt(1);

				bean.setId(id);
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				beans.add(bean);
			}
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
 		return beans;
	}

	public List<Product> list(int cid) {
		return list(cid, 0, Short.MAX_VALUE);
	}

	public List<Product> list(int cid, int start, int count) {
		List<Product> beans = new ArrayList<Product>();
		String sql = "SELECT * FROM Product ORDER BY id WHERE cid = ? DESC LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product bean = new Product();
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int id = rs.getInt(1);
				Category category = new CategoryDAO().get(cid);
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				bean.setId(id);
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				beans.add(bean);
			}
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
 		return beans;
	}

	public void fill(List<Category> cs) {
		for (Category c : cs) {
			fill(c);
		}
	}

	public void fill(Category c) {
		List<Product> ps = list(c.getId());
		c.setProducts(ps);
	}

	public void fillByRow(List<Category> cs) {
		int productNumberEachRow = 8;
		for (Category c : cs) {
			List<Product> products = c.getProducts();
			List<List<Product>> productsByRow = new ArrayList<>();
			for (int i = 0; i < products.size(); i += productNumberEachRow) {
				int size = i + productNumberEachRow;
				size = size > products.size() ? products.size() : size;
				List<Product> productsOfEachRow = products.subList(i, size);
				productsByRow.add(productsOfEachRow);
			}
			c.setProductsByRow(productsByRow);
		}
	}

	public void setFirstProductImage(Product p) {
		List<ProductImage> pis = new ProductImageDAO().list(p, ProductImageDAO.TYPE_SINGLE);
		if (!pis.isEmpty()) {
			p.setFirstProductImage(pis.get(0));
		}
	}

	public void setSaleAndReviewNumber(Product p) {
		int saleCount = new OrderItemDAO().getSaleCount(p.getId());
		p.setSaleCount(saleCount);

		int reviewCount = new ReviewDAO().getTotal(p.getId());
		p.setReviewCount(reviewCount);
	}

	public void setSaleAndReviewNumber(List<Product> products) {
		for (Product p : products) {
			setSaleAndReviewNumber(p);
		}
	}

	public List<Product> search(String keyword, int start, int count) {
		List<Product> beans = new ArrayList<>();
		if (keyword == null || keyword.trim().length() == 0) {
			return beans;
		}
		
		String sql = "SELECT * FROM Product WHERE name like ?, LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, keyword);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product bean = new Product();
				String name = rs.getString("name");
				String subTitle = rs.getString("subTitle");
				float originalPrice = rs.getFloat("originalPrice");
				float promotePrice = rs.getFloat("promotePrice");
				int stock = rs.getInt("stock");
				int cid = rs.getInt("cid");
				Category category = new CategoryDAO().get(cid);
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				int id = rs.getInt(1);

				bean.setId(id);
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOriginalPrice(originalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}