package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.BookRequestDto;
import mate.academy.bookstore.dto.BookSearchParametersDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(BookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    List<BookDto> search(BookSearchParametersDto parametersDto);

    BookDto updateById(Long id, BookRequestDto requestDto);

    void delete(Long id);
}
