package mate.academy.bookstore.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("FROM Book b JOIN FETCH b.categories")
    List<Book> findAllBooks(Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    Optional<Book> findBookByIsbn(String isbn);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories "
            + "WHERE b.id = :id AND b.isDeleted = false")
    Optional<Book> findBookById(Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories c WHERE c.id = :categoryId")
    List<Book> findAllBooksByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
