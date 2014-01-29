package beans;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -885311480803574841L;
	private int id;
	private String username;
	private String password;
	private String title;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String provinceOrState;
	private String country;
	private String postalCode;
	private String phoneNumber;
	private String emailAddress;
	private int active;

	public User() {
		this.id = 0;
		this.username = "";
		this.password = "";
		this.title = "";
		this.firstName = "";
		this.lastName = "";
		this.address = "";
		this.city = "";
		this.provinceOrState = "";
		this.country = "";
		this.postalCode = "";
		this.active = 0;
	}

	public User(int id, String username, String password, String title,
			String firstName, String lastName, String address, String city,
			String provinceOrState, String country, String postalCode,
			String phoneNumber, String emailAddress, int active) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.provinceOrState = provinceOrState;
		this.country = country;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.active = active;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return firstName;
	}

	/**
	 * @param first_name
	 *            the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.firstName = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return lastName;
	}

	/**
	 * @param last_name
	 *            the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.lastName = last_name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
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
	 * @param provinceOrState
	 *            the provinceOrState to set
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
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
}