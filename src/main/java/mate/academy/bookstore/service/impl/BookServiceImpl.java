package mate.academy.bookstore.service.impl;

import java.util.List;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.BookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.BookRepository;
import mate.academy.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto save(BookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
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

    public BookDto updateById(Long id, BookRequestDto requestDto) {
        bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found by id " + id));

        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
