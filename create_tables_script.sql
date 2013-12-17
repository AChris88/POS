drop table store_item;
drop table user_store;
drop table users;
drop table stores;
drop table items;

create table items(
			item_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
			name VARCHAR(150) NOT NULL);
			
create table stores(
			store_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
			name VARCHAR(25) NOT NULL UNIQUE,
			address VARCHAR(50),
			city VARCHAR(50),
			province_state VARCHAR(25),
			country VARCHAR(2),
			postal_code VARCHAR(6),
			currency VARCHAR(3));
			
create table users(
			user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
			username VARCHAR(25) NOT NULL UNIQUE,
			password VARCHAR(25) NOT NULL,
			title VARCHAR(25),
			first_name VARCHAR(20),
			last_name VARCHAR(20),
			address VARCHAR(50),
			city VARCHAR(50),
			province_state VARCHAR(25),
			country VARCHAR(2),
			postal_code VARCHAR(6),
			phone_number VARCHAR(12),
			email_address VARCHAR(45) UNIQUE);
			
create table user_store(
			user_id INT(11),
			store_id INT(11),
			role VARCHAR(25),
			is_active BOOLEAN,
			FOREIGN KEY (user_id) REFERENCES users(user_id),
			FOREIGN KEY (store_id) REFERENCES stores(store_id));
			
create table store_item(
			store_id INT(11),
			item_id INT(11),
			quantity INT(11),
			cost_price DECIMAL(5,2) NOT NULL,
			list_price DECIMAL(5,2) NOT NULL,
			sale_price DECIMAL(5,2) DEFAULT 0,
			PRIMARY KEY (store_id, item_id),
			FOREIGN KEY (store_id) REFERENCES stores(store_id),
			FOREIGN KEY (item_id) REFERENCES items(item_id));
			
