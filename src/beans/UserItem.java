package beans;

public class UserItem {

	private int userId;
	private int storeId;
	private int itemId;
	
	public UserItem(int userId, int storeId, int itemId) {
		super();
		this.userId = userId;
		this.storeId = storeId;
		this.itemId = itemId;
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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}