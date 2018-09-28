CREATE TABLE shav_component(
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) UNIQUE
);

CREATE TABLE shav_item(
  id SERIAL PRIMARY KEY,
  component_id INTEGER NOT NULL,
  amount INTEGER NOT NULL,
  price INTEGER NULL ,
  FOREIGN KEY (component_id) REFERENCES shav_component
);

CREATE TABLE shav_user(
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE shav_role(
  id SERIAL PRIMARY KEY,
  name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE shav_user_role(
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  role_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES shav_user,
  FOREIGN KEY (role_id) REFERENCES shav_role
);

CREATE TABLE shav_payment(
  id SERIAL PRIMARY KEY,
  from_id INTEGER NOT NULL,
  to_id INTEGER NOT NULL,
  amount INTEGER NOT NULL,
  status VARCHAR(32),
  FOREIGN KEY (from_id) REFERENCES shav_user,
  FOREIGN KEY (to_id) REFERENCES shav_user
);

CREATE TABLE shav_order(
  id SERIAL PRIMARY KEY,
  status VARCHAR(32),
  from_id INTEGER NOT NULL,
  to_id INTEGER NULL,
  payment_id INTEGER NULL,
  FOREIGN KEY (from_id) REFERENCES shav_user,
  FOREIGN KEY (to_id) REFERENCES shav_user,
  FOREIGN KEY (payment_id) REFERENCES shav_payment
);

CREATE TABLE shav_order_item(
  id SERIAL PRIMARY KEY,
  order_id INTEGER,
  item_id INTEGER,
  FOREIGN KEY (order_id) REFERENCES shav_order,
  FOREIGN KEY (item_id) REFERENCES shav_item
);

CREATE TABLE shav_storage(
  id SERIAL PRIMARY KEY
);

CREATE TABLE shav_provider(
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) UNIQUE
);

CREATE TABLE shav_storage_item(
  id SERIAL PRIMARY KEY,
  storage_id INTEGER NOT NULL ,
  item_id INTEGER NOT NULL ,
  FOREIGN KEY (storage_id) REFERENCES shav_storage,
  FOREIGN KEY (item_id) REFERENCES shav_item
);

CREATE TABLE shav_provider_item(
  id SERIAL PRIMARY KEY,
  provider_id INTEGER NOT NULL,
  item_id INTEGER NOT NULL
);
