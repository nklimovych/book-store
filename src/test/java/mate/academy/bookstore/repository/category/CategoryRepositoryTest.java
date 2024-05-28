package mate.academy.bookstore.repository.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    private static final String DELETE_DATA_FROM_DB = "classpath:database/delete-data-from-db.sql";
    private static final String INSERT_DATA_INTO_DB = "classpath:database/insert-data-into-db.sql";

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find a book by name")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_ValidName_True() {
    }

    @Test
    void findByName_InvalidName_False() {
    }

    @Test
    void findByName_EmptyName_False() {
    }

    @Test
    void findByName_NullName_False() {
    }

    @Test
    void findByName_NameWithDifferentCase_False() {
    }

    @Test
    void findByName_SpecialCharactersInName_False() {
    }

    @Test
    void findByName_MultipleCategoriesWithSameName_True() {
    }
}
