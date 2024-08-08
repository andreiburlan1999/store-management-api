-- Inserts for User
INSERT INTO `user` (id, username, password, role, name, email, address)
VALUES (seq_user.NEXTVAL, 'jdoe', '123', 'USER', 'John Doe', 'john.doe@example.com', '123 Street');

INSERT INTO `user` (id, username, password, role, name, email, address)
VALUES (seq_user.NEXTVAL, 'jsmith', '456', 'USER', 'Jane Smith', 'jane.smith@example.com', '456 Street');

INSERT INTO `user` (id, username, password, role, name, email, address)
VALUES (seq_user.NEXTVAL, 'ajohnson', '789', 'USER', 'Alice Johnson', 'alice.johnson@example.com', '789 Street');

-- Inserts for Category
INSERT INTO category (id, name)
VALUES (seq_category.NEXTVAL, 'Electronics');

INSERT INTO category (id, name)
VALUES (seq_category.NEXTVAL, 'Clothing');

INSERT INTO category (id, name)
VALUES (seq_category.NEXTVAL, 'Books');

-- Inserts for Product
INSERT INTO product (id, name, price, quantity, status, category_id)
VALUES (seq_product.NEXTVAL, 'Smartphone', 699.99, 50, 'enabled', (SELECT id FROM category WHERE name = 'Electronics'));

INSERT INTO product (id, name, price, quantity, status, category_id)
VALUES (seq_product.NEXTVAL, 'Laptop', 1299.99, 30, 'enabled', (SELECT id FROM category WHERE name = 'Electronics'));

INSERT INTO product (id, name, price, quantity, status, category_id)
VALUES (seq_product.NEXTVAL, 'T-shirt', 19.99, 100, 'enabled', (SELECT id FROM category WHERE name = 'Clothing'));

INSERT INTO product (id, name, price, quantity, status, category_id)
VALUES (seq_product.NEXTVAL, 'Novel', 9.99, 200, 'enabled', (SELECT id FROM category WHERE name = 'Books'));

-- Inserts for Order
INSERT INTO `order` (id, user_id)
VALUES (seq_order.NEXTVAL, (SELECT id FROM `user` WHERE username = 'jdoe'));

INSERT INTO `order` (id, user_id)
VALUES (seq_order.NEXTVAL, (SELECT id FROM `user` WHERE username = 'jsmith'));

INSERT INTO `order` (id, user_id)
VALUES (seq_order.NEXTVAL, (SELECT id FROM `user` WHERE username = 'ajohnson'));

-- Inserts for Order_Product
INSERT INTO order_product (order_id, product_id, quantity, price)
VALUES ((SELECT id FROM `order` WHERE user_id = (SELECT id FROM `user` WHERE username = 'jdoe')),
        (SELECT id FROM product WHERE name = 'Smartphone'),
        1, 699.99);

INSERT INTO order_product (order_id, product_id, quantity, price)
VALUES ((SELECT id FROM `order` WHERE user_id = (SELECT id FROM `user` WHERE username = 'jdoe')),
        (SELECT id FROM product WHERE name = 'Laptop'),
        1, 1299.99);

INSERT INTO order_product (order_id, product_id, quantity, price)
VALUES ((SELECT id FROM `order` WHERE user_id = (SELECT id FROM `user` WHERE username = 'jsmith')),
        (SELECT id FROM product WHERE name = 'T-shirt'),
        3, 19.99);

INSERT INTO order_product (order_id, product_id, quantity, price)
VALUES ((SELECT id FROM `order` WHERE user_id = (SELECT id FROM `user` WHERE username = 'ajohnson')),
        (SELECT id FROM product WHERE name = 'Novel'),
        2, 9.99);