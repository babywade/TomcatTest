package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;

public class OrderItemDAO {
	
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT COUNT(*) FROM OrderItem";
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public void add(OrderItem bean) {
		String sql = "INSERT INTO OrderItem VALUES(NULL, ?, ?, ?, ?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, bean.getProduct().getId());
			//订单项在创建时，没有订单信息
			if (bean.getOrder() == null) {
				ps.setInt(2, -1);
			} else {
				ps.setInt(2, bean.getOrder().getId());
			}
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getNumber());
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
			String sql = "DELETE FROM OrderItem WHERE id = " + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(OrderItem bean) {
		String sql = "UPDATE OrderItem SET pid = ?, oid = ?, pid = ?, number = ? WHERE id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, bean.getProduct().getId());
			if (bean.getOrder() == null) {
				ps.setInt(2, -1);
			} else {
				ps.setInt(2, bean.getOrder().getId());
			}
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getNumber());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public OrderItem get(int id) {
		OrderItem bean = null;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT * FROM OrderItem WHERE id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				bean = new OrderItem();
				int pid = rs.getInt("pid");
				int oid = rs.getInt("oid");
				int uid = rs.getInt("uid");
				int number = rs.getInt("number");
				Product product = new ProductDAO().get(pid);
				User user = new UserDAO().get(uid);

				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				bean.setId(id);
				if (oid != -1) {
					Order order = new OrderDAO().get(oid);
					bean.setOrder(order);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<OrderItem> listByUser(int uid) {
		return listByUser(uid, 0, Short.MAX_VALUE);
	}

	public List<OrderItem> listByUser(int uid, int start, int count) {
		List<OrderItem> beans = new ArrayList<OrderItem>();
		String sql = "SELECT * FROM OrderItem WHERE uid = ? ORDER BY id DESC LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, uid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrderItem bean = new OrderItem();
				int id = rs.getInt(1);
				int pid = rs.getInt("pid");
				int oid = rs.getInt("oid");
				int number = rs.getInt("number");
				Product product = new ProductDAO().get(pid);
				User user = new UserDAO().get(uid);

				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				bean.setId(id);
				if (oid != -1) {
					Order order = new OrderDAO().get(oid);
					bean.setOrder(order);
				}
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}

	public List<OrderItem> listByOrder(int oid) {
		return listByOrder(oid, 0, Short.MAX_VALUE);
	}


	public List<OrderItem> listByOrder(int oid, int start, int count) {
		List<OrderItem> beans = new ArrayList<OrderItem>();
		String sql = "SELECT * FROM OrderItem WHERE oid = ? ORDER BY id DESC LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, oid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrderItem bean = new OrderItem();
				int id = rs.getInt(1);
				int pid = rs.getInt("pid");
				int uid = rs.getInt("uid");
				int number = rs.getInt("number");
				Product product = new ProductDAO().get(pid);
				User user = new UserDAO().get(uid);

				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				bean.setId(id);
				if (oid != -1) {
					Order order = new OrderDAO().get(oid);
					bean.setOrder(order);
				}
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}

	public List<OrderItem> listByProduct(int pid) {
		return listByProduct(pid, 0, Short.MAX_VALUE);
	}

	public List<OrderItem> listByProduct(int pid, int start, int count) {
		List<OrderItem> beans = new ArrayList<OrderItem>();
		String sql = "SELECT * FROM OrderItem WHERE pid = ? ORDER BY id DESC LIMIT ?, ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrderItem bean = new OrderItem();
				int id = rs.getInt(1);
				int oid = rs.getInt("oid");
				int uid = rs.getInt("uid");
				int number = rs.getInt("number");
				Product product = new ProductDAO().get(pid);
				User user = new UserDAO().get(uid);

				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				bean.setId(id);
				if (oid != -1) {
					Order order = new OrderDAO().get(oid);
					bean.setOrder(order);
				}
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}

	public void fill(List<Order> os) {
		for (Order o : os) {
			List<OrderItem> ois = listByOrder(o.getId());
			float total = 0;
			int totalNumber = 0;
			for (OrderItem oi : ois) {
				total += oi.getNumber() * oi.getProduct().getPromotePrice();
				totalNumber += oi.getNumber();
			}
			o.setTotal(total);
			o.setTotalNumber(totalNumber);
			o.setOrderItems(ois);
		}
	}

	public void fill(Order o) {
		List<OrderItem> ois = listByOrder(o.getId());
		float total = 0;
		int totalNumber = 0;
		for (OrderItem oi : ois) {
			total += oi.getNumber() * oi.getProduct().getPromotePrice();
			totalNumber += oi.getNumber();
		}
		o.setTotal(total);
		o.setTotalNumber(totalNumber);
		o.setOrderItems(ois);
	}

	public int getSaleCount(int pid) {
		int saleCount = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "SELECT SUM(number) FROM OrderItem WHERE pid = " + pid;
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				saleCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return saleCount;
	}
}