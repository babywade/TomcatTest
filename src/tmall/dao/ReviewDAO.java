package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ReviewDAO {
	
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT COUNT(*) FROM Review";

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public int getTotal(int pid) {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT COUNT(*) FROM Review WHERE pid = " + pid;

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public void add(Review bean) {
		String sql = "INSERT INTO Review VALUES(NULL, ?, ?, ?, ?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, bean.getContent());
			ps.setInt(2, bean.getUser().getId());
			ps.setInt(3, bean.getProduct().getId());
			ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
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

	public void update(Review bean) {
		String sql = "UPDATE Review SET content = ?, uid = ?, pid = ?, createDate = ? WHERE id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, bean.getContent());
			ps.setInt(2, bean.getUser().getId());
			ps.setInt(3, bean.getProduct().getId());
			ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(4, bean.getId());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "DELETE FROM Review WHERE id = " + id;

			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Review get(int id) {
		Review bean = null;

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT * Review WHERE id = " + id;

			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				bean = new Review();

				int uid = rs.getInt("uid");
				int pid = rs.getInt("pid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				String content = rs.getString("content");
				Product product = new ProductDAO().get(pid);
				User user = new UserDAO().get(uid);

				bean.setContent(content);
				bean.setCreateDate(createDate);
				bean.setUser(user);
				bean.setProduct(product);
				bean.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<Review> list(int pid) {
		return list(pid, 0, Short.MAX_VALUE);
	}

	public List<Review> list(int pid, int start, int count) {
		List<Review> beans = new ArrayList<Review>();

		String sql = "SELECT FROM Review WHERE pid = ? ORDER BY id DESC LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Review bean = new Review();
				String content = rs.getString("content");
				int id = rs.getInt(1);
				int uid = rs.getInt("uid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				User user = new UserDAO().get(uid);
				Product product = new ProductDAO().get(pid);

				bean.setContent(content);
				bean.setCreateDate(createDate);
				bean.setUser(user);
				bean.setProduct(product);
				bean.setId(id);
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}

	public boolean isExist(String content, int pid) {
		String sql = "SELECT FROM Review WHERE content = ?, pid = ?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, content);
			ps.setInt(2, pid);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}