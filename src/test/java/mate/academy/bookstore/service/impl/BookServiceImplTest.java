package mate.academy.bookstore.service.impl;

import static mate.academy.bookstore.util.TestConstants.EXPECTED_LIST_SIZE;
import static mate.academy.bookstore.util.TestConstants.NEGATIVE_PRICE;
import static mate.academy.bookstore.util.TestConstants.NUMBER_OF_INVOCATIONS;
import static mate.academy.bookstore.util.TestConstants.PAGE_NUMBER;
import static mate.academy.bookstore.util.TestConstants.PAGE_SIZE;
import static mate.academy.bookstore.util.TestConstants.SET_OF_VALID_CATEGORY_ID;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_AUTHOR;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_COVER_IMAGE;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_DESCRIPTION;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_ID;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_ISBN;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_PRICE;
import static mate.academy.bookstore.util.TestConstants.VALID_BOOK_TITLE;
import static mate.academy.bookstore.util.TestConstants.VALID_CATEGORY_ID;
import static mate.academy.bookstore.util.TestConstants.VALID_MAX_PRICE;
import static mate.academy.bookstore.util.TestConstants.VALID_MIN_PRICE;
import static mate.academy.bookstore.util.TestConstants.ZERO_MAX_PRICE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
import org.junit.jupiter.api.BeforeEach;
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
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder specificationBuilder;

    private Book validBook;

    @BeforeEach
    void setUp() {
        validBook = new Book();
        validBook.setId(VALID_BOOK_ID);
        validBook.setTitle(VALID_BOOK_TITLE);
        validBook.setAuthor(VALID_BOOK_AUTHOR);
        validBook.setIsbn(VALID_BOOK_ISBN);
        validBook.setPrice(VALID_BOOK_PRICE);
        validBook.setDescription(VALID_BOOK_DESCRIPTION);
        validBook.setCoverImage(VALID_BOOK_COVER_IMAGE);
    }

    @Test
    @DisplayName("Save a valid book (return BookDto)")
    void save_ValidBook_ReturnBookDto() {
        CreateBookRequestDto createBookDto = getCreateBookRequestDto();
        BookDto bookDto = getBookDto(createBookDto);

        when(bookMapper.toEntity(createBookDto)).thenReturn(validBook);
        when(bookRepository.save(validBook)).thenReturn(validBook);
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        BookDto savedBook = bookService.save(createBookDto);

        assertEquals(bookDto, savedBook);
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
        BookDto bookDto = getBookDto(getCreateBookRequestDto());

        when(bookRepository.findAllBooks(pageable)).thenReturn(List.of(validBook));
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        List<BookDto> books = bookService.findAll(pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(bookDto, books.getFirst());
    }

    @Test
    @DisplayName("Find book by id (return BookDto)")
    void findById_ValidRequest_ReturnBookDto() {
        BookDto bookDto = getBookDto(getCreateBookRequestDto());

        when(bookRepository.findBookById(VALID_BOOK_ID)).thenReturn(Optional.of(validBook));
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        BookDto foundBook = bookService.findById(VALID_BOOK_ID);

        assertEquals(bookDto, foundBook);
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
        Book updatedBook = new Book();
        updatedBook.setId(VALID_BOOK_ID);

        CreateBookRequestDto updateBookDto = getCreateBookRequestDto();
        BookDto updatedBookDto = getBookDto(updateBookDto);

        when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(validBook));
        when(bookMapper.toEntity(updateBookDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto result = bookService.updateById(VALID_BOOK_ID, updateBookDto);

        assertEquals(updatedBookDto, result);
    }

    @Test
    @DisplayName("Delete book by id")
    void delete_ValidRequest_DeleteBook() {
        bookService.delete(VALID_BOOK_ID);
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).deleteById(VALID_BOOK_ID);
    }

    @Test
    @DisplayName("Search books with parameters (return list of BookDto)")
    void search_ValidParameters_ReturnListOfBookDto() {
        BookSearchParametersDto params = getSearchParams(VALID_MIN_PRICE, VALID_MAX_PRICE);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());
        Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(validBook)));
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        List<BookDto> books = bookService.search(params, pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(bookDto, books.getFirst());
    }

    @Test
    @DisplayName("Search books with negative price (ignore negative min price)")
    void search_NegativeMinPrice_IgnoreNegativeMinPrice() {
        BookSearchParametersDto params = getSearchParams(NEGATIVE_PRICE, VALID_MAX_PRICE);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());
        Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(validBook)));
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        List<BookDto> books = assertDoesNotThrow(() -> bookService.search(params, pageable));
        assertEquals(EXPECTED_LIST_SIZE, books.size());
    }

    @Test
    @DisplayName("Search books with negative price (ignore smaller max price)")
    void search_ZeroMaxPrice_IgnoreSmallerMaxPrice() {
        BookSearchParametersDto params = getSearchParams(NEGATIVE_PRICE, ZERO_MAX_PRICE);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookDto bookDto = getBookDto(getCreateBookRequestDto());
        Specification<Book> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(validBook)));
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        List<BookDto> books = assertDoesNotThrow(() -> bookService.search(params, pageable));
        assertEquals(EXPECTED_LIST_SIZE, books.size());
    }

    @Test
    @DisplayName("Get all books by category id (return list of BookDtoWithoutCategoryIds)")
    void getAllBookByCategoryId() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = getBookDtoWithoutCategoryIds();

        when(bookRepository.findAllBooksByCategoryId(VALID_CATEGORY_ID, pageable))
                .thenReturn(List.of(validBook));
        when(bookMapper.toDtoWithoutCategories(validBook)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> books =
                bookService.getAllBookByCategoryId(VALID_CATEGORY_ID, pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(bookDtoWithoutCategoryIds, books.getFirst());
    }

    private CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle(VALID_BOOK_TITLE);
        createBookDto.setAuthor(VALID_BOOK_AUTHOR);
        createBookDto.setIsbn(VALID_BOOK_ISBN);
        createBookDto.setPrice(VALID_BOOK_PRICE);
        createBookDto.setCategoryIds(SET_OF_VALID_CATEGORY_ID);
        createBookDto.setDescription(VALID_BOOK_DESCRIPTION);
        createBookDto.setCoverImage(VALID_BOOK_COVER_IMAGE);
        return createBookDto;
    }

    private BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setTitle(VALID_BOOK_TITLE);
        bookDto.setAuthor(VALID_BOOK_AUTHOR);
        bookDto.setIsbn(VALID_BOOK_ISBN);
        bookDto.setPrice(VALID_BOOK_PRICE);
        bookDto.setDescription(VALID_BOOK_DESCRIPTION);
        bookDto.setCoverImage(VALID_BOOK_COVER_IMAGE);
        return bookDto;
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
