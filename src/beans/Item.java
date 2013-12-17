package beans;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = -6437283746317972132L;
	private int id;
	private String name;
	private double costPrice;
	private double listPrice;
	private double salePrice;

	public Item() {
		this.id = -1;
		this.name = "";
	}

	public Item(int id, String name, double costPrice, double listPrice, double salePrice) {
		this.id = id;
		this.name = name;
		this.costPrice = costPrice;
		this.listPrice = listPrice;
		this.salePrice = salePrice;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cost_price
	 */
	public double getCost_price() {
		return costPrice;
	}

	/**
	 * @param cost_price
	 *            the cost_price to set
	 */
	public void setCost_price(double cost_price) {
		this.costPrice = cost_price;
	}

	/**
	 * @return the list_price
	 */
	public double getList_price() {
		return listPrice;
	}

	/**
	 * @param list_price
	 *            the list_price to set
	 */
	public void setList_price(double list_price) {
		this.listPrice = list_price;
	}

	/**
	 * @return the sale_price
	 */
	public double getSale_price() {
		return salePrice;
	}

	/**
	 * @param sale_price
	 *            the sale_price to set
	 */
	public void setSale_price(double sale_price) {
		this.salePrice = sale_price;
	}
}
