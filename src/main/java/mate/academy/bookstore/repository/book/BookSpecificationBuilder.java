package mate.academy.bookstore.repository.book;

import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookSearchParametersDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.SpecificationBuilder;
import mate.academy.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto params) {
        return Stream.of(
                            getSpecification(params.author(), BookSearchParameter.AUTHOR.getKey()),
                            getSpecification(params.title(), BookSearchParameter.TITLE.getKey()),
                            getSpecification(params.isbn(), BookSearchParameter.ISBN.getKey()),
                            getPriceSpecification(params)
                     )
                     .filter(Objects::nonNull)
                     .reduce(Specification.where(null), Specification::and);
    }

    private Specification<Book> getSpecification(String[] values, String parameterKey) {
        if (values != null && values.length > 0) {
            return specificationProviderManager
                    .getSpecificationProvider(parameterKey)
                    .getSpecification(values);
        }
        return null;
    }

    private Specification<Book> getPriceSpecification(BookSearchParametersDto params) {
        if (params.minPrice() != null && params.maxPrice() != null) {
            return specificationProviderManager
                    .getSpecificationProvider(BookSearchParameter.PRICE.getKey())
                    .getSpecification(new String[]{
                            params.minPrice(),
                            params.maxPrice()
                    });
        }
        return null;
    }
}
