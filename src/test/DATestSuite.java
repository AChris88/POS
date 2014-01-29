package test;

public class DATestSuite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UnitTest test = new UnitTest();
		
		System.out.println("//-- Commencing test sequence --\\");
		
		System.out.println("# Dropping tables.");
		
		test.testDropTables();
		System.out.println("\t- Tables dropped.");
		
		System.out.println("# Testing table creation.");
		
		test.testTableCreation();
		System.out.println("\t- Database tables created");
		
		//Insertions tests
		System.out.println("# Testing record creation.");
		
		test.testInsertItem();
		System.out.println("\t- Item successfully created.");
		
		test.testInsertStore();
		System.out.println("\t- Store successfully created.");
		
		test.testInsertUser();
		System.out.println("\t- User successfully created.");
		
		test.testInsertUserStore();
		System.out.println("\t- UserStore successfully created.");
		
		test.testInsertStoreItem();
		System.out.println("\t- StoreItem successfully created.");
		
		test.testInsertUserItem();
		System.out.println("\t- UserItem successfully created.");
		
		//Read tests
		System.out.println("# Testing record retrieval.");
		
		test.testGetItems();
		System.out.println("\t- Items successfully retrieved.");
		
		test.testGetStores();
		System.out.println("\t- Stores successfully retrieved.");
		
		test.testGetUsers();
		System.out.println("\t- Users successfully retrieved.");
		
		//Update tests
		System.out.println("# Testing record modification.");
		
		test.testUpdateItem();
		System.out.println("\t- Item successfully updated.");
		
		test.testUpdateStore();
		System.out.println("\t- Store successfully updated.");
		
		test.testUpdateUser();
		System.out.println("\t- User successfully updated.");
		
		//Toggle tests
		System.out.println("# Testing record toggling.");
		
		test.testToggleItem();
		System.out.println("\t- Item successfully toggled.");
		
		test.testToggleStore();
		System.out.println("\t- Store successfully toggled.");
		
		test.testToggleUser();
		System.out.println("\t- User successfully toggled.");
		
		//Compound query tests
		System.out.println("# Testing compound queries.");
		
		test.testGetUserStores();
		System.out.println("\t- User stores successfully retrieved.");
		
		test.testGetUserPurchases();
		System.out.println("\t- User purchases successfully retrieved.");
		
		test.testGetStoreItems();
		System.out.println("\t- Store items successfully retrieved.");
		
		System.out.println("//-- Test sequence completed --\\");
	}
}
