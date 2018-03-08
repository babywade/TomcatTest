package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

public class PropertyValueDAO {
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT COUNT(*) FROM PropertyValue";

			s.executeQuery(sql);
			ResultSet rs = s.getGeneratedKeys();
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public void add (PropertyValue bean) {
		String sql = "INSERT INTO PropertyValue VALUES(NULL, ?, ?, ?)";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());

			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			while (rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(PropertyValue bean) {
		String sql = "UPDATE PropertyValue SET pid = ?, ptid = ?, value = ? WHERE id = ?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.setInt(4, bean.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "DELETE FROM PropertyValue WHERE id = " + id;

			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PropertyValue get(int id) {
		PropertyValue bean = null;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT FROM PropertyValue WHERE id = " + id;
			s.execute(sql);

			ResultSet rs = s.getGeneratedKeys();
			if (rs.next()) {
				bean = new PropertyValue();
				int pid = rs.getInt("pid");
				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");

				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);
				bean.setValue(value);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public PropertyValue get(int pid, int ptid) {
		PropertyValue bean = null;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT FROM PropertyValue WHERE pid = " + pid + "AND ptid = " + ptid;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				bean = new PropertyValue();
				String value = rs.getString("value");

				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);
				bean.setValue(value);
				bean.setProduct(product);
				bean.setProperty(property);
				int id = bean.getId();
				bean.setId(id);
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<PropertyValue> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<PropertyValue> list(int start, int count) {
		List<PropertyValue> beans = new ArrayList<PropertyValue>();

		String sql = "SELECT * FROM PropertyValue ORDER BY id DESC LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PropertyValue bean = new PropertyValue();
				int id = rs.getInt(1);
				int pid = rs.getInt("pid");
				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");
				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);

				bean.setValue(value);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setId(id);
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}

	public List<PropertyValue> list(int pid) {
		List<PropertyValue> beans = new ArrayList<PropertyValue>();
		String sql = "SELECT * FROM ProductValue WHERE pid = ? ORDER BY ptid DESC";
		
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, pid);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PropertyValue bean = new PropertyValue();
				int id = rs.getInt(1);

				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");

				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);
				bean.setId(id);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(value);
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}

	public void init(Product p) {
		List<Property> pts = new PropertyDAO().list(p.getCategory().getId());

		for (Property pt : pts) {
			PropertyValue pv = get(pt.getId(), p.getId());
			if (pv == null) {
				pv = new PropertyValue();
				pv.setProduct(p);
				pv.setProperty(pt);
				this.add(pv);
			}
		}
	}
}