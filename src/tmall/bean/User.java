package tmall.bean;

public class User {
	private String name;
	private String password;
	private int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAnonymousName() {
		if (name == null) {
			return null;
		}

		char[] cs = name.toCharArray();
		for (int i = 0; i < cs.length - 1; i++) {
			cs[i] = '*';
		}

		return new String(cs);
	}
}