INSERT INTO books (id, title, author, isbn, price, description)
VALUES
    (1, 'Kobzar', 'Taras Shevchenko', '978-1-1516-4732-0', 34.99, 'Awesome description...'),
    (2, 'Earth', 'Olha Kobylianska', '978-7-3664-5711-2', 14.99, 'Awesome description...'),
    (3, 'Tiger hunters', 'Ivan Bahriany', '978-8-9176-0894-6', 16.99, 'Awesome description...'),
    (4, 'Marusja Churai', 'Lesya Ukrainka', '978-0-8386-9622-4', 9.99, 'Awesome description...'),
    (5, 'Haidamaky', 'Taras Shevchenko', '978-6-3292-3392-3', 25.99, 'Awesome description...');

INSERT INTO categories (id, name, description)
VALUES
    (1, 'Poetry', 'Awesome description...'),
    (2, 'Novel', 'Awesome description...'),
    (3, 'Epic', 'Awesome description...'),
    (4, 'Drama', 'Awesome description...');

INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 2),
    (4, 2),
    (5, 3);
