package mate.academy.bookstore.controller;

import static mate.academy.bookstore.util.TestConstants.ADMIN_ROLE;
import static mate.academy.bookstore.util.TestConstants.CATEGORIES_ID_BOOKS_URL;
import static mate.academy.bookstore.util.TestConstants.CATEGORIES_ID_URL;
import static mate.academy.bookstore.util.TestConstants.CATEGORIES_URL;
import static mate.academy.bookstore.util.TestConstants.EXPRESSION;
import static mate.academy.bookstore.util.TestConstants.NAME_EXPRESSION;
import static mate.academy.bookstore.util.TestConstants.USER_ROLE;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_ID;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.service.BookService;
import mate.academy.bookstore.service.CategoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    private static MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookService bookService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    @DisplayName("Create new category")
    void create_ValidRequest_ReturnsCreatedCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(VALID_CATEGORY_NAME);

        when(categoryService.save(any(CategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post(CATEGORIES_URL)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(new ObjectMapper().writeValueAsString(categoryDto)))
                       .andExpect(status().isOk())
                       .andExpect(jsonPath(NAME_EXPRESSION).value(VALID_CATEGORY_NAME));
    }

    @Test
    @WithMockUser(roles = USER_ROLE)
    @DisplayName("Get all categories")
    void getAll_ValidRequest_ReturnsListOfCategories() throws Exception {
        List<CategoryDto> categories = List.of(new CategoryDto());

        when(categoryService.findAll(any())).thenReturn(categories);

        mockMvc.perform(get(CATEGORIES_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath(EXPRESSION).exists());
    }

    @Test
    @WithMockUser(roles = USER_ROLE)
    @DisplayName("Delete category by id with invalid role")
    void deleteCategory_WIthInValidRoleUser_Forbidden() throws Exception {
        mockMvc.perform(delete(CATEGORIES_ID_URL, VALID_CATEGORY_ID).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {USER_ROLE, ADMIN_ROLE})
    @DisplayName("Get all books by category id")
    void getBooksByCategoryId_ValidRequest_ReturnsListOfBooks() throws Exception {
        List<BookDtoWithoutCategoryIds> books = List.of(new BookDtoWithoutCategoryIds());

        when(bookService.getAllBookByCategoryId(eq(VALID_CATEGORY_ID),
                any(Pageable.class))).thenReturn(books);

        mockMvc.perform(get(CATEGORIES_ID_BOOKS_URL, VALID_CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(EXPRESSION).exists());
    }
}
