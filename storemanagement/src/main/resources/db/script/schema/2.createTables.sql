-- ChangeSet for creating Customer table
CREATE TABLE customer (
    id NUMBER,
    name VARCHAR2(200) NOT NULL,
    email VARCHAR2(200) NOT NULL UNIQUE,
    address VARCHAR2(200) NOT NULL,
    CONSTRAINT PK_CUSTOMER PRIMARY KEY (id)
);

-- ChangeSet for creating User table
CREATE TABLE "user" (
    id NUMBER,
    username VARCHAR2(50) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    role VARCHAR2(8) NOT NULL,
    customer_id NUMBER,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT FK__USER__CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- ChangeSet for creating Category table
CREATE TABLE category (
    id NUMBER,
    name VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id)
);

-- ChangeSet for creating Product table
CREATE TABLE product (
    id NUMBER,
    name VARCHAR2(200) NOT NULL,
    price NUMBER NOT NULL,
    quantity NUMBER NOT NULL,
    status VARCHAR2(8) NOT NULL,
    category_id NUMBER NOT NULL,
    CONSTRAINT PK_PRODUCT PRIMARY KEY (id),
    CONSTRAINT FK__PRODUCT__CATEGORY FOREIGN KEY (category_id) REFERENCES category(id)
);

-- ChangeSet for creating Order table
CREATE TABLE "order" (
    id NUMBER,
    customer_id NUMBER NOT NULL,
    CONSTRAINT PK_ORDER PRIMARY KEY (id),
    CONSTRAINT FK__ORDER__CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- ChangeSet for creating Order_Product table
CREATE TABLE order_product (
    order_id NUMBER,
    product_id NUMBER NOT NULL,
    quantity NUMBER NOT NULL,
    price NUMBER NOT NULL,
    CONSTRAINT PK_ORDER_ITEM PRIMARY KEY (order_id, product_id),
    CONSTRAINT FK__ORDER_PRODUCT__ORDER FOREIGN KEY (order_id) REFERENCES "order"(id),
    CONSTRAINT FK__ORDER_PRODUCT__PRODUCT FOREIGN KEY (product_id) REFERENCES product(id)
);