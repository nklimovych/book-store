package mate.academy.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.exception.DuplicateEntityException;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class CategoryServiceImplTest {
    private static final Long VALID_CATEGORY_ID = 1L;
    private static final String VALID_CATEGORY_NAME = "Fiction";
    private static final int PAGE_SIZE = 5;
    private static final String NEW_VALID_CATEGORY_NAME = "New Fiction";
    private static final int EXPECTED_LIST_SIZE = 1;
    private static final int NUMBER_OF_INVOCATIONS = 1;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Save a valid category (return CategoryDto)")
    void save_ValidCategory_ReturnCategoryDto() {
        CategoryDto categoryDto = getCategoryDto();
        Category category = getCategory();

        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.findByName(VALID_CATEGORY_NAME)).thenReturn(null);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto savedCategory = categoryService.save(categoryDto);

        assertEquals(VALID_CATEGORY_NAME, savedCategory.getName());
    }

    @Test
    @DisplayName("Save a category with duplicate name (throws exception)")
    void save_CategoryWithDuplicateName_ThrowException() {
        CategoryDto categoryDto = getCategoryDto();
        String expectedErrorMessage =
                "Category with name " + VALID_CATEGORY_NAME + " already exists";

        when(categoryRepository.findByName(VALID_CATEGORY_NAME)).thenReturn(new Category());

        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () ->
                categoryService.save(categoryDto));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Find all categories (return list of CategoryDto)")
    void findAll_ValidRequest_ReturnListOfCategoryDto() {
        Pageable pageable = Pageable.ofSize(PAGE_SIZE);
        Category category = getCategory();
        CategoryDto categoryDto = getCategoryDto();

        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(category)));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> categories = categoryService.findAll(pageable);

        assertEquals(EXPECTED_LIST_SIZE, categories.size());
        assertEquals(VALID_CATEGORY_NAME, categories.getFirst().getName());
    }

    @Test
    @DisplayName("Find category by id (return CategoryDto)")
    void getById_ValidRequest_ReturnCategoryDto() {
        Category category = getCategory();
        CategoryDto categoryDto = getCategoryDto();

        when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto foundCategory = categoryService.getById(VALID_CATEGORY_ID);

        assertEquals(VALID_CATEGORY_NAME, foundCategory.getName());
    }

    @Test
    @DisplayName("Find category by id (throws exception)")
    void getById_NonExistentCategory_ThrowException() {
        when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.empty());
        String expectedErrorMessage = "Category not found by id " + VALID_CATEGORY_ID;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                categoryService.getById(VALID_CATEGORY_ID));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Update category (return updated CategoryDto)")
    void update_ValidRequest_ReturnCategoryDto() {
        Category category = getCategory();
        CategoryDto updateCategoryDto = new CategoryDto();
        updateCategoryDto.setName(NEW_VALID_CATEGORY_NAME);

        when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.findByName(updateCategoryDto.getName())).thenReturn(null);
        when(categoryMapper.toEntity(updateCategoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(updateCategoryDto);

        CategoryDto result = categoryService.update(VALID_CATEGORY_ID, updateCategoryDto);
        assertEquals(updateCategoryDto.getName(), result.getName());
    }

    @Test
    @DisplayName("Delete category by id")
    void delete_ValidRequest_DeleteCategory() {
        Category category = getCategory();
        category.setId(VALID_CATEGORY_ID);

        when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.of(category));

        categoryService.delete(VALID_CATEGORY_ID);
        verify(categoryRepository, times(NUMBER_OF_INVOCATIONS)).deleteById(VALID_CATEGORY_ID);
    }

    private static Category getCategory() {
        Category category = new Category();
        category.setId(VALID_CATEGORY_ID);
        category.setName(VALID_CATEGORY_NAME);
        return category;
    }

    private static CategoryDto getCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(VALID_CATEGORY_ID);
        categoryDto.setName(VALID_CATEGORY_NAME);
        return categoryDto;
    }
}
