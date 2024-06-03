package mate.academy.bookstore.repository.category;

import static mate.academy.bookstore.util.TestConstants.CATEGORY_NAME_SPECIAL_CHARACTERS;
import static mate.academy.bookstore.util.TestConstants.DELETE_DATA_FROM_DB;
import static mate.academy.bookstore.util.TestConstants.EMPTY_STRING;
import static mate.academy.bookstore.util.TestConstants.INSERT_DATA_INTO_DB;
import static mate.academy.bookstore.util.TestConstants.INVALID_CATEGORY_NAME;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_DESCRIPTION;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_ID;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_NAME;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_NAME_DIFFERENT_CASES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import mate.academy.bookstore.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category validCategory;

    @BeforeEach
    void setUp() {
        validCategory = new Category();
        validCategory.setId(VALID_CATEGORY_ID);
        validCategory.setName(VALID_CATEGORY_NAME);
        validCategory.setDescription(VALID_CATEGORY_DESCRIPTION);
    }

    @Test
    @DisplayName("Find a category by name - (valid name)")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByName_ValidName_ReturnsCategory() {
        Category category = categoryRepository.findByName(VALID_CATEGORY_NAME);
        assertNotNull(category);
        assertEquals(validCategory, category);
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
