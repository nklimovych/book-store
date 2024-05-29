package mate.academy.bookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static final String ID = "/{id}";
    private static final String TITLE_PARAM_NAME = "title";
    private static final String AUTHOR_PARAM_NAME = "author";
    private static final String ISBN_PARAM_NAME = "isbn";
    private static final String PRICE_PARAM_NAME = "price";
    private static final String BASE_URL = "/books";
    private static final String SEARCH_URL = BASE_URL + "/search";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";
    private static final String VALID_BOOK_TITLE = "Kobzar";
    private static final String VALID_BOOK_AUTHOR = "Taras Shevchenko";
    private static final String VALID_BOOK_ISBN = "978-0-7847-5628-7";
    private static final BigDecimal VALID_BOOK_PRICE = BigDecimal.valueOf(14.99);
    private static final Set<Long> VALID_CATEGORY_ID = Set.of(1L);
    private static final Long VALID_BOOK_ID = 1L;
    private static final String TITLE_0_EXPRESSION = "$[0].title";
    private static final String TITLE_EXPRESSION = "$.title";

    private static MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDto bookDto;
    private CreateBookRequestDto createBookDto;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setup() {
        bookDto = new BookDto();
        bookDto.setId(VALID_BOOK_ID);
        bookDto.setTitle(VALID_BOOK_TITLE);
        bookDto.setAuthor(VALID_BOOK_AUTHOR);
        bookDto.setIsbn(VALID_BOOK_ISBN);
        bookDto.setPrice(VALID_BOOK_PRICE);

        createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle(VALID_BOOK_TITLE);
        createBookDto.setAuthor(VALID_BOOK_AUTHOR);
        createBookDto.setIsbn(VALID_BOOK_ISBN);
        createBookDto.setCategoryIds(VALID_CATEGORY_ID);
        createBookDto.setPrice(VALID_BOOK_PRICE);
    }

    @Test
    @WithMockUser(roles = {USER_ROLE, ADMIN_ROLE})
    @DisplayName("Get all books (valid request)")
    void getAllBooks_ValidRequest_ReturnsListOfBooks() throws Exception {
        when(bookService.findAll(any(Pageable.class))).thenReturn(
                Collections.singletonList(bookDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TITLE_0_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @WithMockUser(roles = {USER_ROLE, ADMIN_ROLE})
    @DisplayName("Get book by id (valid request)")
    void getBookById_ValidRequest_ReturnsBookDto() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(bookDto);

        mockMvc.perform(get(BASE_URL + ID, VALID_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TITLE_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    @DisplayName("Create book (valid request)")
    void createBook_ValidRequest_ReturnsCreatedBookDto() throws Exception {
        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(bookDto);

        String requestContent = objectMapper.writeValueAsString(createBookDto);

        mockMvc.perform(post(BASE_URL)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(TITLE_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    @DisplayName("Update book (valid request)")
    void updateBook_ValidRequest_ReturnsUpdatedBookDto() throws Exception {
        when(bookService.updateById(anyLong(), any(CreateBookRequestDto.class))).thenReturn(
                bookDto);

        String requestContent = objectMapper.writeValueAsString(createBookDto);

        mockMvc.perform(put(BASE_URL + ID, VALID_BOOK_ID)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestContent))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath(TITLE_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    @DisplayName("Delete book (valid request)")
    void deleteBook_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + ID, VALID_BOOK_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {USER_ROLE, ADMIN_ROLE})
    @DisplayName("Search books (valid request)")
    void searchBooks_ValidRequest_ReturnsListOfBooks() throws Exception {
        when(bookService.search(any(BookSearchParametersDto.class), any(Pageable.class)))
                 .thenReturn(Collections.singletonList(bookDto));

        mockMvc.perform(get(SEARCH_URL)
                       .param(TITLE_PARAM_NAME, VALID_BOOK_TITLE)
                       .param(AUTHOR_PARAM_NAME, VALID_BOOK_AUTHOR)
                       .param(ISBN_PARAM_NAME, VALID_BOOK_ISBN)
                       .param(PRICE_PARAM_NAME, VALID_BOOK_PRICE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TITLE_0_EXPRESSION).value(VALID_BOOK_TITLE));
    }
}
