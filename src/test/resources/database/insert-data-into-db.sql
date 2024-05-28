-- Inserting data into the books table
INSERT INTO books (title, author, isbn, price, description, cover_image)
VALUES
    ('Kobzar', 'Taras Shevchenko', '978-1-1516-4732-0', 34.99, 'Kobzar, Taras Shevchenko.', 'kobzar.jpg'),
    ('Earth', 'Olha Kobylianska', '978-7-3664-5711-2', 14.99, 'Earth, Olha Kobylianska', 'earth.jpg'),
    ('Tiger hunters', 'Ivan Bahriany', '978-8-9176-0894-6', 16.99, 'Tiger hunters, Ivan Bahriany', 'tiger-hunters.jpg'),
    ('Marusja Churai', 'Lesya Ukrainka', '978-0-8386-9622-4', 9.99, 'Marusja Churai, Lesya Ukrainka', 'marusja-churai.jpg'),
    ('The Tale of Igor\'s Campaign', 'Anonymous', '978-6-3292-3392-3', 25.99, 'The Tale of Igor\'s Campaign, Anonymous', 'igor-campaign.jpg');

-- Inserting data into the categories table
INSERT INTO categories (name, description)
VALUES
    ('Poetry', 'Literary genre that uses language for its poetic...'),
    ('Novel', 'Prose literary genre that describes various situations...'),
    ('Epic', 'Literary genre that describes great events, historical episodes...');

-- Inserting data into the books_categories table
INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 2),
    (4, 2),
    (5, 3);
