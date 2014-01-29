package test;

import static org.junit.Assert.*;

import org.junit.Test;

import da.DBManager;
import beans.Item;
import beans.Store;
import beans.StoreItem;
import beans.User;
import beans.UserItem;
import beans.UserStore;

class UnitTest {

	DBManager manager;

	public UnitTest() {
		manager = new DBManager();
	}

	@Test
	public void testTableCreation() {
		manager = new DBManager();
	}

	@Test
	public void testInsertUser() {
		manager = new DBManager();
		assertTrue(manager.insertUser(new User(0, "username", "password",
				"Mr.", "fName", "lName", "address", "city", "QC", "CA",
				"H0H0H0", "1234567", "email@address.com", 1)));
	}

	public void testInsertStore() {
		assertTrue(manager.insertStore(new Store(0, "storeName", "address",
				"H0H0H0", "Montreal", "QC", "CA", "CAD", 1)));
	}

	public void testInsertItem() {
		manager = new DBManager();
		assertTrue(manager.insertItem(new Item(0, "itemName", 5.00, 7.50, 6.25,
				1)));
	}

	public void testInsertStoreItem() {
		assertTrue(manager.insertStoreItem(new StoreItem(1, 1, 8, 8.5, 7.25, 7.5)));
	}
	
	public void testInsertUserStore() {
		assertTrue(manager.insertUserStore(new UserStore(1, 1, "role")));
	}
	
	public void testInsertUserItem() {
		assertTrue(manager.insertUserItem(new UserItem(1, 1, 1)));
	}
	
	public void testUpdateUser() {
		assertTrue(manager.updateUser(new User(1, "username", "password",
				"Mr.", "fName", "lName", "address", "city", "QC", "CA",
				"H0H0H0", "1234567", "email@address.com", 1)));
	}

	public void testUpdateItem() {
		assertTrue(manager.updateItem(new Item(1, "itemName", 7.50, 8.50, 7.25,
				1)));
	}

	public void testUpdateStore() {
		assertTrue(manager.updateStore(new Store(1, "storeName", "address",
				"H0H0H0", "Montreal", "QC", "CA", "CAD", 1)));
	}

	public void testGetUsers() {
		assertTrue(manager.getUsers().size() > 0);
	}

	public void testGetStores() {
		assertTrue(manager.getStores().size() > 0);
	}

	public void testGetItems() {
		assertTrue(manager.getItems().size() > 0);
	}

	public void testToggleItem() {
		assertTrue(manager.toggleItem(new Item()));
	}

	public void testToggleStore() {
		assertTrue(manager.toggleStore(new Store()));
	}

	public void testToggleUser() {
		assertTrue(manager.toggleUser(new User()));
	}

	public void testGetUserStores() {
		assertTrue(manager.getUserStores("1").get(0).getRole().equals("role"));
	}
	
	public void testGetUserPurchases() {
		assertTrue(manager.getUserPurchases("1").get(0).getUserId() == 1);
	}

	public void testGetStoreItems() {
		assertTrue(manager.getStoreItems("1").get(0).getQuantity() == 8);
	}
	
	public void testDropTables() {
		manager.dropTables();
	}
}