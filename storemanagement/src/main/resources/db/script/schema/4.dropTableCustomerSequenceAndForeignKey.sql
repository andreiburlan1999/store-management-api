ALTER TABLE `order` DROP CONSTRAINT FK__ORDER__CUSTOMER;
ALTER TABLE `user` DROP CONSTRAINT FK__USER__CUSTOMER;
DROP TABLE customer;
DROP SEQUENCE seq_customer;