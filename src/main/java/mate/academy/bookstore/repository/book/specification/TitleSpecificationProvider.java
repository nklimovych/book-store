package mate.academy.bookstore.repository.book.specification;

import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.SpecificationProvider;
import mate.academy.bookstore.repository.book.BookSearchParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return BookSearchParameter.TITLE.getKey();
    }

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get(getKey()), "%" + params.title() + "%");
    }
}
