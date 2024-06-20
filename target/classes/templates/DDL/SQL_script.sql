
DROP DATABASE mybasetest;
CREATE DATABASE IF NOT EXISTS mybasetest;
show databases;
use mybasetest;
show tables;
SELECT * FROM user_profiles;
SELECT * FROM users;
SELECT * FROM books;
SELECT * FROM book_authors;
SELECT * FROM book_categories;
SELECT * FROM book_categories_books;
SELECT * FROM user_profiles_book_offers;
SELECT * FROM user_profiles_book_requests;
SELECT * FROM book_authors_books;
SELECT * FROM books_book_authors;
SELECT * FROM user_profiles_favourite_book_authors;
SELECT * FROM user_profiles_favourite_book_categories;
SELECT * FROM books_requesting_users;


INSERT INTO book_categories (name) VALUES ('Art');
INSERT INTO book_categories (name) VALUES ('Comic');
INSERT INTO book_categories (name) VALUES ('Fantasy');
INSERT INTO book_categories (name) VALUES ('Fiction');
INSERT INTO book_categories (name) VALUES ('Biographies');
INSERT INTO book_categories (name) VALUES ('History');
INSERT INTO book_categories (name) VALUES ('Science');
INSERT INTO book_categories (name) VALUES ('Literature');
INSERT INTO book_categories (name) VALUES ('Adventure');
INSERT INTO book_categories (name) VALUES ('Crime');

