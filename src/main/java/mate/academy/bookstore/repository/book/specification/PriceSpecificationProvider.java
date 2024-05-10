package mate.academy.bookstore.repository.book.specification;

import java.util.Optional;
import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.SpecificationProvider;
import mate.academy.bookstore.repository.book.BookSearchParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final int DEFAULT_MIN_PRICE = 0;
    private static final int DEFAULT_MAX_PRICE = Integer.MAX_VALUE;

    @Override
    public String getKey() {
        return BookSearchParameter.PRICE.getKey();
    }

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        int minPrice = Optional.ofNullable(params.minPrice()).orElse(DEFAULT_MIN_PRICE);
        int maxPrice = Optional.ofNullable(params.maxPrice()).orElse(DEFAULT_MAX_PRICE);

        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(getKey()), minPrice, maxPrice));
    }
}
