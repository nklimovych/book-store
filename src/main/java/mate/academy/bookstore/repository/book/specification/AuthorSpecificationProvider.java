package mate.academy.bookstore.repository.book.specification;

import java.util.Arrays;
import mate.academy.bookstore.dto.BookSearchParametersDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.SpecificationProvider;
import mate.academy.bookstore.repository.book.BookSearchParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSearchParameter.AUTHOR.getKey();
    }

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) ->
                root.get(getKey()).in(Arrays.stream(params.author()).toArray());
    }
}
