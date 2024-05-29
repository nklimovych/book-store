package mate.academy.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.exception.DuplicateIsbnException;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final String VALID_BOOK_TITLE = "Kobzar";
    private static final String VALID_BOOK_AUTHOR = "Taras Shevchenko";
    private static final String VALID_BOOK_ISBN = "978-0-7847-5628-7";
    private static final BigDecimal VALID_BOOK_PRICE = BigDecimal.valueOf(14.99);
    private static final Set<Long> VALID_CATEGORY_ID = Set.of(1L);
    private static final String VALID_BOOK_DESCRIPTION = "Awesome description...";
    private static final String VALID_BOOK_COVER_IMAGE = "kobzar.png";
    private static final Long VALID_BOOK_ID = 1L;
    private static final int EXPECTED_LIST_SIZE = 1;
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 5;
    private static final int VALID_MIN_PRICE = 10;
    private static final int VALID_MAX_PRICE = 20;
    private static final int NEGATIVE_PRICE = -10;
    private static final int ZERO_MAX_PRICE = 0;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @Test
    @DisplayName("Save a valid book (return BookDto)")
    void save_ValidBook_ReturnBookDto() {
        CreateBookRequestDto createBookDto = getCreateBookRequestDto();
        BookDto bookDto = getBookDto(createBookDto);
        Book newBook = new Book();

        when(bookMapper.toEntity(createBookDto)).thenReturn(newBook);
        when(bookRepository.save(newBook)).thenReturn(newBook);
        when(bookMapper.toDto(newBook)).thenReturn(bookDto);

        BookDto savedBook = bookService.save(createBookDto);

        assertEquals(VALID_BOOK_TITLE, savedBook.getTitle());
        assertEquals(VALID_BOOK_AUTHOR, savedBook.getAuthor());
    }

    @Test
    @DisplayName("Save a book with duplicate isbn (throws exception)")
    void save_BookWithDuplicate_Isbn_ThrowException() {
        CreateBookRequestDto createBookDto = getCreateBookRequestDto();
        String expectedErrorMessage = "Book with ISBN " + VALID_BOOK_ISBN + " already exists";

        when(bookRepository.findBookByIsbn(VALID_BOOK_ISBN)).thenReturn(Optional.of(new Book()));

        DuplicateIsbnException exception =
                assertThrows(DuplicateIsbnException.class, () -> bookService.save(createBookDto));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Find all books (return list of BookDto)")
    void findAll_ValidRequest_ReturnListOfBookDto() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Book book = new Book();
        book.setId(VALID_BOOK_ID);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());

        when(bookRepository.findAllBooks(pageable)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> books = bookService.findAll(pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(VALID_BOOK_TITLE, books.getFirst().getTitle());
    }

    @Test
    @DisplayName("Find book by id (return BookDto)")
    void findById_ValidRequest_ReturnBookDto() {
        Book book = new Book();
        book.setId(VALID_BOOK_ID);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());

        when(bookRepository.findBookById(VALID_BOOK_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto foundBook = bookService.findById(VALID_BOOK_ID);

        assertEquals(VALID_BOOK_TITLE, foundBook.getTitle());
    }

    @Test
    @DisplayName("Find book by id (throws EntityNotFoundException)")
    void findById_NonExistentBook_ThrowException() {
        when(bookRepository.findBookById(VALID_BOOK_ID)).thenReturn(Optional.empty());
        String expectedErrorMessage = "Book not found by id " + VALID_BOOK_ID;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookService.findById(VALID_BOOK_ID));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Update book by id (return updated BookDto)")
    void updateById_ValidRequest_ReturnBookDto() {
        Book existingBook = new Book();
        existingBook.setId(VALID_BOOK_ID);
        existingBook.setIsbn(VALID_BOOK_ISBN);

        Book updatedBook = new Book();
        updatedBook.setId(VALID_BOOK_ID);
        CreateBookRequestDto updateBookDto = getCreateBookRequestDto();
        BookDto updatedBookDto = getBookDto(updateBookDto);

        when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(existingBook));
        when(bookMapper.toEntity(updateBookDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto result = bookService.updateById(VALID_BOOK_ID, updateBookDto);

        assertEquals(VALID_BOOK_TITLE, result.getTitle());
        assertEquals(VALID_BOOK_AUTHOR, result.getAuthor());
    }

    @Test
    @DisplayName("Delete book by id")
    void delete_ValidRequest_DeleteBook() {
        bookService.delete(VALID_BOOK_ID);
        verify(bookRepository, times(1)).deleteById(VALID_BOOK_ID);
    }

    @Test
    @DisplayName("Search books with parameters (return list of BookDto)")
    void search_ValidParameters_ReturnListOfBookDto() {
        BookSearchParametersDto params = getSearchParams(VALID_MIN_PRICE, VALID_MAX_PRICE);
        Book book = getMockBook();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());
        Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> books = bookService.search(params, pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(VALID_BOOK_TITLE, books.getFirst().getTitle());
    }

    @Test
    @DisplayName("Search books with negative price (ignore negative min price)")
    void search_NegativeMinPrice_IgnoreNegativeMinPrice() {
        BookSearchParametersDto params = getSearchParams(NEGATIVE_PRICE, VALID_MAX_PRICE);
        Book book = getMockBook();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());
        Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> books = assertDoesNotThrow(() -> bookService.search(params, pageable));
        assertEquals(EXPECTED_LIST_SIZE, books.size());
    }

    @Test
    @DisplayName("Search books with negative price (ignore smaller max price)")
    void search_ZeroMaxPrice_IgnoreSmallerMaxPrice() {
        BookSearchParametersDto params = getSearchParams(NEGATIVE_PRICE, ZERO_MAX_PRICE);
        Book book = getMockBook();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());
        Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> books = assertDoesNotThrow(() -> bookService.search(params, pageable));
        assertEquals(EXPECTED_LIST_SIZE, books.size());
    }

    @Test
    @DisplayName("Get all books by category id (return list of BookDtoWithoutCategoryIds)")
    void getAllBookByCategoryId() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Book book = new Book();
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setTitle(VALID_BOOK_TITLE);
        bookDtoWithoutCategoryIds.setAuthor(VALID_BOOK_AUTHOR);

        when(bookRepository.findAllBooksByCategoryId(VALID_BOOK_ID, pageable))
                .thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> books =
                bookService.getAllBookByCategoryId(VALID_BOOK_ID, pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(VALID_BOOK_TITLE, books.getFirst().getTitle());
    }

    private CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle(VALID_BOOK_TITLE);
        createBookDto.setAuthor(VALID_BOOK_AUTHOR);
        createBookDto.setIsbn(VALID_BOOK_ISBN);
        createBookDto.setPrice(VALID_BOOK_PRICE);
        createBookDto.setCategoryIds(VALID_CATEGORY_ID);
        createBookDto.setDescription(VALID_BOOK_DESCRIPTION);
        createBookDto.setCoverImage(VALID_BOOK_COVER_IMAGE);
        return createBookDto;
    }

    private BookDto getBookDto(CreateBookRequestDto dto) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(dto.getTitle());
        bookDto.setAuthor(dto.getAuthor());
        bookDto.setIsbn(dto.getIsbn());
        bookDto.setPrice(dto.getPrice());
        bookDto.setDescription(dto.getDescription());
        bookDto.setCoverImage(dto.getCoverImage());
        return bookDto;
    }

    private Book getMockBook() {
        Book book = new Book();
        book.setTitle(VALID_BOOK_TITLE);
        book.setAuthor(VALID_BOOK_AUTHOR);
        return book;
    }

    private static BookSearchParametersDto getSearchParams(int minPrice, int maxPrice) {
        return new BookSearchParametersDto(
                new String[]{VALID_BOOK_AUTHOR},
                VALID_BOOK_TITLE,
                VALID_BOOK_ISBN,
                minPrice,
                maxPrice
        );
    }
}
