package mate.academy.bookstore.repository.book;

import static mate.academy.bookstore.util.TestConstants.DELETE_DATA_FROM_DB;
import static mate.academy.bookstore.util.TestConstants.EMPTY_CATEGORY_ID;
import static mate.academy.bookstore.util.TestConstants.EMPTY_STRING;
import static mate.academy.bookstore.util.TestConstants.INSERT_DATA_INTO_DB;
import static mate.academy.bookstore.util.TestConstants.INVALID_BOOK_ISBN;
import static mate.academy.bookstore.util.TestConstants.INVALID_ID;
import static mate.academy.bookstore.util.TestConstants.PAGE_NUMBER;
import static mate.academy.bookstore.util.TestConstants.PAGE_SIZE;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_AUTHOR;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_DESCRIPTION;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_ID;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_ISBN;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_ISBN_NOT_IN_DB;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_PRICE;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_TITLE;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    private Book validBook;

    @BeforeEach
    void setUp() {
        validBook = new Book();
        validBook.setId(VALID_BOOK_ID);
        validBook.setTitle(VALID_BOOK_TITLE);
        validBook.setAuthor(VALID_BOOK_AUTHOR);
        validBook.setIsbn(VALID_BOOK_ISBN);
        validBook.setPrice(VALID_BOOK_PRICE);
        validBook.setDescription(VALID_BOOK_DESCRIPTION);
    }

    @Test
    @DisplayName("Find all books (valid request)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllBooks_ValidRequest_ReturnsNonEmptyListOfBooks() {
        List<Book> books = bookRepository.findAllBooks(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    @DisplayName("Find a book by isbn - (valid isbn)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_ValidIsbn_ReturnsBookWithMatchingIsbn() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(VALID_BOOK_ISBN);
        assertTrue(bookOptional.isPresent());
        assertEquals(validBook, bookOptional.get());
    }

    @Test
    @DisplayName("Find a book by isbn - (invalid isbn)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_InvalidIsbn_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(INVALID_BOOK_ISBN);
        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find a book by isbn - (empty isbn)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_EmptyIsbn_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(EMPTY_STRING);
        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find a book by isbn - (null isbn)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_NullIsbn_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(null);
        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find book by id (existing book)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookById_ValidId_ReturnsBookWithMatchingId() {
        Book book = bookRepository.findBookById(VALID_BOOK_ID).orElse(null);
        assertNotNull(book);
        assertEquals(validBook, book);
    }

    @Test
    @DisplayName("Find a book by isbn - (valid isbn, not in DB)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_ValidIsbnBookNotInRepository_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(VALID_BOOK_ISBN_NOT_IN_DB);
        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find book by id (non existing book)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookById_InvalidId_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookById(INVALID_ID);
        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find book by id (null id)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookById_NullId_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookById(null);
        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find all book by category id (valid id)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllBooksByCategoryId_ValidCategoryId_ReturnsNonEmptyListOfBooks() {
        List<Book> books = bookRepository.findAllBooksByCategoryId(
                VALID_CATEGORY_ID, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    @DisplayName("Find all book by category id (invalid id")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllBooksByCategoryId_InvalidCategoryId_ReturnsEmptyList() {
        List<Book> books = bookRepository.findAllBooksByCategoryId(
                INVALID_ID, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("Find all book by category id (empty id")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllBooksByCategoryId_NullCategoryId_ReturnsEmptyList() {
        List<Book> books = bookRepository.findAllBooksByCategoryId(
                null, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("Find all book by category id (no books in category")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllBooksByCategoryId_CategoryHasNoBooks_ReturnsEmptyList() {
        List<Book> books = bookRepository.findAllBooksByCategoryId(
                EMPTY_CATEGORY_ID, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }
}
