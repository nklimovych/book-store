package mate.academy.bookstore.repository;

import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecification(BookSearchParametersDto params);
}
