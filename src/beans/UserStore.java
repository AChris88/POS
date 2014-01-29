package beans;

public class UserStore {

	private int userId;
	private int storeId;
	private String role;
	
	public UserStore() {}
	
	public UserStore(int userId, int storeId, String role) {
		super();
		this.userId = userId;
		this.storeId = storeId;
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}