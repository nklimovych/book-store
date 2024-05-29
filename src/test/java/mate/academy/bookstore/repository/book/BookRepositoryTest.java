package mate.academy.bookstore.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.Book;
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
    private static final String DELETE_DATA_FROM_DB = "classpath:database/delete-data-from-db.sql";
    private static final String INSERT_DATA_INTO_DB = "classpath:database/insert-data-into-db.sql";
    private static final Long VALID_BOOK_ID_KOBZAR = 1L;
    private static final String VALID_BOOK_TITLE_KOBZAR = "Kobzar";
    private static final String VALID_BOOK_ISBN_KOBZAR = "978-1-1516-4732-0";
    private static final String INVALID_BOOK_ISBN = "978-11-1516-4732-02";
    private static final long INVALID_ID = -1L;
    private static final long VALID_CATEGORY_ID = 1L;
    private static final long EMPTY_CATEGORY_ID = 4L;
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 5;
    private static final String EMPTY_STRING = "";
    private static final String VALID_BOOK_ISBN_NOT_IN_DB = "978-1-2345-6789-0";

    @Autowired
    private BookRepository bookRepository;

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
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(VALID_BOOK_ISBN_KOBZAR);
        assertTrue(bookOptional.isPresent());
        assertEquals(VALID_BOOK_ID_KOBZAR, bookOptional.get().getId());
        assertEquals(VALID_BOOK_ISBN_KOBZAR, bookOptional.get().getIsbn());
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
        Book book = bookRepository.findBookById(VALID_BOOK_ID_KOBZAR).orElse(null);
        assertNotNull(book);
        assertEquals(VALID_BOOK_TITLE_KOBZAR, book.getTitle());
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
