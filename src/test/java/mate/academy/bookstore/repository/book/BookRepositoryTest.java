package mate.academy.bookstore.repository.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAllBooks_ValidRequest_True() {
    }

    @Test
    void findAllBooks_InvalidRequest_False() {
    }

    @Test
    void findBookByIsbn_GivenValidIsbn_True() {
    }

    @Test
    void findBookByIsbn_GivenInvalidIsbn_False() {
    }

    @Test
    void findBookByIsbn_EmptyIsbn_False() {
    }

    @Test
    void findBookByIsbn_NullIsbn_False() {
    }

    @Test
    void findAllBooksByCategoryId_ValidCategoryId_True() {
    }

    @Test
    void findAllBooksByCategoryId_InvalidCategoryId_False() {
    }

    @Test
    void findAllBooksByCategoryId_EmptyCategoryId_False() {
    }

    @Test
    void findAllBooksByCategoryId_NullCategoryId_False() {
    }

    @Test
    void findAllBooks_NoBooksInRepository_False() {
    }

    @Test
    void findAllBooksByCategoryId_CategoryHasNoBooks_False() {
    }

    @Test
    void findBookByIsbn_ValidIsbnBookNotInRepository_False() {
    }
}
