package beans;

public class StoreItem {

	private int storeId;
	private int itemId;
	private int quantity;
	private double costPrice;
	private double listPrice;
	private double salePrice;
	
	public StoreItem() {}
	
	public StoreItem(int storeId, int itemId, int quantity, double costPrice,
			double listPrice, double salePrice) {
		super();
		this.storeId = storeId;
		this.itemId = itemId;
		this.quantity = quantity;
		this.costPrice = costPrice;
		this.listPrice = listPrice;
		this.salePrice = salePrice;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}	 
	
}