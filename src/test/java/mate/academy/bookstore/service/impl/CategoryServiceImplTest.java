package mate.academy.bookstore.service.impl;

import static mate.academy.bookstore.util.TestConstants.EXPECTED_LIST_SIZE;
import static mate.academy.bookstore.util.TestConstants.NEW_VALID_CATEGORY_NAME;
import static mate.academy.bookstore.util.TestConstants.NUMBER_OF_INVOCATIONS;
import static mate.academy.bookstore.util.TestConstants.PAGE_SIZE;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_DESCRIPTION;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_ID;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_NAME;
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
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category validCategory;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        validCategory = new Category();
        validCategory.setId(VALID_CATEGORY_ID);
        validCategory.setName(VALID_CATEGORY_NAME);
        validCategory.setDescription(VALID_CATEGORY_DESCRIPTION);
    }

    @Test
    @DisplayName("Save a valid category (return CategoryDto)")
    void save_ValidCategory_ReturnCategoryDto() {
        CategoryDto categoryDto = getCategoryDto();

        when(categoryMapper.toEntity(categoryDto)).thenReturn(validCategory);
        when(categoryRepository.findByName(VALID_CATEGORY_NAME)).thenReturn(null);
        when(categoryRepository.save(validCategory)).thenReturn(validCategory);
        when(categoryMapper.toDto(validCategory)).thenReturn(categoryDto);

        CategoryDto savedCategory = categoryService.save(categoryDto);

        assertEquals(categoryDto, savedCategory);
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

        CategoryDto categoryDto = getCategoryDto();

        when(categoryRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(validCategory)));
        when(categoryMapper.toDto(validCategory)).thenReturn(categoryDto);

        List<CategoryDto> categories = categoryService.findAll(pageable);

        assertEquals(EXPECTED_LIST_SIZE, categories.size());
        assertEquals(categoryDto, categories.getFirst());
    }

    @Test
    @DisplayName("Find category by id (return CategoryDto)")
    void getById_ValidRequest_ReturnCategoryDto() {
        CategoryDto categoryDto = getCategoryDto();

        when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.of(validCategory));
        when(categoryMapper.toDto(validCategory)).thenReturn(categoryDto);

        CategoryDto foundCategory = categoryService.getById(VALID_CATEGORY_ID);

        assertEquals(categoryDto, foundCategory);
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
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(NEW_VALID_CATEGORY_NAME);

        when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.of(validCategory));
        when(categoryRepository.findByName(categoryDto.getName())).thenReturn(null);
        when(categoryMapper.toEntity(categoryDto)).thenReturn(validCategory);
        when(categoryRepository.save(validCategory)).thenReturn(validCategory);
        when(categoryMapper.toDto(validCategory)).thenReturn(categoryDto);

        CategoryDto updatedCategory = categoryService.update(VALID_CATEGORY_ID, categoryDto);
        assertEquals(categoryDto, updatedCategory);
    }

    @Test
    @DisplayName("Delete category by id")
    void delete_ValidRequest_DeleteCategory() {

        validCategory.setId(VALID_CATEGORY_ID);

        when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.of(validCategory));

        categoryService.delete(VALID_CATEGORY_ID);
        verify(categoryRepository, times(NUMBER_OF_INVOCATIONS)).deleteById(VALID_CATEGORY_ID);
    }

    private static CategoryDto getCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(VALID_CATEGORY_ID);
        categoryDto.setName(VALID_CATEGORY_NAME);
        categoryDto.setDescription(VALID_CATEGORY_DESCRIPTION);
        return categoryDto;
    }
}
