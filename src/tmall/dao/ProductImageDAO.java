package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

public class ProductImageDAO {

	public static final String TYPE_SINGLE = "TYPE_SINGLE";
	public static final String TYPE_DETAIL = "TYPE_DETAIL";

	public int getTotal () {
		int total = 0;

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SEELECT COUNT(*) FROM ProductImage WHERE pid = ";

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public void add(ProductImage bean) {
		String sql = "INSERT INTO ProductImage VALUES(NULL, ?, ?)";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, bean.getProduct().getId());
			ps.setString(2, bean.getType());
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

	public void update(ProductImage bean) {
		String sql = "UPDATE ProductImage SET pid = ?, type = ?, WHERE id = ?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, bean.getProduct().getId());
			ps.setString(2, bean.getType());
			ps.setInt(3, bean.getId());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "DELETE FROM ProductImage WHERE id = " + id;

			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ProductImage get(int id) {
		ProductImage bean = null;

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			bean = new ProductImage();
			String sql = "SELECT * FROM ProductImage WHERE id = " + id;

			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				int pid = rs.getInt("pid");
				String type = rs.getString("type");
				bean.setType(type);
				Product product = new ProductDAO().get(pid);
				bean.setProduct(product);
				bean.setId(id);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<ProductImage> list(Product p, String type) {
		return list(p, type, 0, Short.MAX_VALUE);
	}

	public List<ProductImage> list(Product p, String type, int start, int count) {
		List<ProductImage> beans = new ArrayList<ProductImage>();

		String sql = "SELECT * FROM ProductImage WHERE pid = ? and type = ? ORDER BY id DESC LIMIT ?, ?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, p.getId());
			ps.setString(2, type);
			ps.setInt(3, start);
			ps.setInt(4, count);

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ProductImage bean = new ProductImage();
				int id = rs.getInt(1);

				bean.setType(type);
				bean.setProduct(p);
				bean.setId(id);

				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}