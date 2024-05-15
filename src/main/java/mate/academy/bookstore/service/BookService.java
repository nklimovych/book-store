package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    List<BookDto> search(BookSearchParametersDto parametersDto, Pageable pageable);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDtoWithoutCategoryIds> getAllBookByCategoryId(Long id, Pageable pageable);
}
