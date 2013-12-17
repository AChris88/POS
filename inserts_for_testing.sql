/* test items */
insert into items values(null, "book");
insert into items values(null, "pen");
insert into items values(null, "pencil");
insert into items values(null, "ball");
insert into items values(null, "mop");

/* test stores */
insert into stores values(null, "Store 1", "123 Street", "Montreal", "QC", "CA", "H4P1N1", "CAN");
insert into stores values(null, "Store 2", "456 Road", "Montreal", "QC", "CA", "H4H1H2", "USD");
insert into stores values(null, "Store 3", "789 Blvd", "Montreal", "QC", "CA", "H4Z1Z2", "CAN");

/* test clients */
insert into users values(null, "R.Hammond", "hamster", "Mr", "Richard", "Hammond", "1337 Abbey Road", "Montreal", "Quebec", "Canada", "H0H0H0", "1234510890", "littleMan@gmail.com");
insert into users values(null, "J.May", "slow", "Capt.", "James", "May", "99 St-Catherine Street", "Montreal", "Quebec", "Canada", "J3V9K2", "1234567890", "JamesIsSlow@gmail.com");
insert into users values(null, "J.Clarkson", "orangutan", "Mr", "Jeremy", "Clarkson", "10 Gambon Corner", "Montreal", "Quebec", "Canada", "V2T6K1", "1234567890", "TheClarkson@gmail.com");

/* adding items to store 1 */
insert into store_item values((select store_id from stores where name = "Store 1"), (select item_id from items where name = "book"), 4, 2, 4, 0);
insert into store_item values((select store_id from stores where name = "Store 1"), (select item_id from items where name = "pen"), 7, 1, 2, 0);
insert into store_item values((select store_id from stores where name = "Store 1"), (select item_id from items where name = "ball"), 2, 3, 6, 4);

/* adding items to store 2 */
insert into store_item values((select store_id from stores where name = "Store 2"), (select item_id from items where name = "book"), 4, 2, 5, 0);
insert into store_item values((select store_id from stores where name = "Store 2"), (select item_id from items where name = "pencil"), 17, 0.75, 1.75, 1.25);
insert into store_item values((select store_id from stores where name = "Store 2"), (select item_id from items where name = "mop"), 2, 3, 6, 4);

/* adding items to store 3 */
insert into store_item values((select store_id from stores where name = "Store 3"), (select item_id from items where name = "ball"), 4, 2, 5, 0);
insert into store_item values((select store_id from stores where name = "Store 3"), (select item_id from items where name = "pen"), 17, 0.75, 2, null);
insert into store_item values((select store_id from stores where name = "Store 3"), (select item_id from items where name = "mop"), 2, 3, 6, 4);