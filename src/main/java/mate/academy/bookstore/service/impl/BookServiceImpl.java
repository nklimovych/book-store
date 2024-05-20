package mate.academy.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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
import mate.academy.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper,
                           BookSpecificationBuilder specificationBuilder) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.specificationBuilder = specificationBuilder;
    }

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        String isbn = requestDto.getIsbn();
        if (bookRepository.findBookByIsbn(isbn) != null) {
            throw new DuplicateIsbnException("Book with ISBN " + isbn + " already exists");
        }

        Book book = bookMapper.toEntity(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found by id " + id));
        return bookMapper.toDto(book);
    }

    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found by id " + id));

        String requestIsbn = requestDto.getIsbn();
        if (!Objects.equals(existingBook.getIsbn(), requestIsbn)
                && bookRepository.findBookByIsbn(requestIsbn) != null) {
            throw new DuplicateIsbnException("Book with ISBN " + requestIsbn + " already exists");
        }

        Book book = bookMapper.toEntity(requestDto);
        book.setId(id);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> specification = specificationBuilder.build(params);
        return bookRepository.findAll(specification, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getAllBookByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllBooksByCategoryId(id, pageable).stream()
                             .map(bookMapper::toDtoWithoutCategories)
                             .toList();
    }
}
