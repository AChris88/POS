package beans;

import java.io.Serializable;

public class Store implements Serializable {

	private static final long serialVersionUID = -8692744303904007991L;
	private int id;
	private String name;
	private String address;
	private String postalCode;
	private String city;
	private String provinceOrState;
	private String country;
	private String currency;
	private int active;
	
	public Store() {
		id = 0;
		name = "";
		address = "";
		postalCode = "";
		city = "";
		provinceOrState = "";
		country = "";
		currency = "";
		active = 0;
	}

	public Store(int id, String name, String address, String postalCode,
			String city, String provinceOrState, String country, String currency, int active) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.postalCode = postalCode;
		this.city = city;
		this.provinceOrState = provinceOrState;
		this.country = country;
		this.currency = currency;
		this.active = active;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the provinceOrState
	 */
	public String getProvinceOrState() {
		return provinceOrState;
	}
	/**
	 * @param provinceOrState the provinceOrState to set
	 */
	public void setProvinceOrState(String provinceOrState) {
		this.provinceOrState = provinceOrState;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCurrency() {
		return currency;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
}