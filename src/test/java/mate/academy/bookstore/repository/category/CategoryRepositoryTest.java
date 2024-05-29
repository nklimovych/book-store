package mate.academy.bookstore.repository.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import mate.academy.bookstore.model.Category;
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
    private static final String VALID_CATEGORY_NAME = "Poetry";
    private static final String INVALID_CATEGORY_NAME = "Coco Jamboo";
    private static final String VALID_CATEGORY_NAME_DIFFERENT_CASES = "pOetRy";
    private static final String CATEGORY_NAME_SPECIAL_CHARACTERS = "Po*try";
    private static final String EMPTY_STRING = "";

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find a category by name - (valid name)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_ValidName_ReturnsCategory() {
        Category category = categoryRepository.findByName(VALID_CATEGORY_NAME);
        assertNotNull(category);
        assertEquals(VALID_CATEGORY_NAME, category.getName());
    }

    @Test
    @DisplayName("Find a category by name - (invalid name)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_InvalidName_ReturnsNull() {
        Category category = categoryRepository.findByName(INVALID_CATEGORY_NAME);
        assertNull(category);
    }

    @Test
    @DisplayName("Find a category by name - (empty name)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_EmptyName_ReturnsNull() {
        Category category = categoryRepository.findByName(EMPTY_STRING);
        assertNull(category);
    }

    @Test
    @DisplayName("Find a category by name - (null name)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_NullName_ReturnsNull() {
        Category category = categoryRepository.findByName(null);
        assertNull(category);
    }

    @Test
    @DisplayName("Find a category by name - (name with different case)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_NameWithDifferentCase_ReturnsNull() {
        Category expected = categoryRepository.findByName(VALID_CATEGORY_NAME);
        Category actual = categoryRepository.findByName(VALID_CATEGORY_NAME_DIFFERENT_CASES);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find a category by name - (name with special characters)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_SpecialCharactersInName_ReturnsNull() {
        Category category = categoryRepository.findByName(CATEGORY_NAME_SPECIAL_CHARACTERS);
        assertNull(category);
    }
}
