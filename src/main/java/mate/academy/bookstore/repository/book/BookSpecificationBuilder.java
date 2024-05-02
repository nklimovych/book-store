package mate.academy.bookstore.repository.book;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
    private static final String AUTHOR = BookSearchParameter.AUTHOR.getKey();
    private static final String TITLE = BookSearchParameter.TITLE.getKey();
    private static final String ISBN = BookSearchParameter.ISBN.getKey();
    private static final String PRICE = BookSearchParameter.PRICE.getKey();
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto paramsDto) {
        Map<String, Function<BookSearchParametersDto, Specification<Book>>>
                specificationMap = Map.of(
                AUTHOR, params -> params.author() != null ? getSpecification(params, AUTHOR) : null,
                TITLE, params -> params.title() != null ? getSpecification(params, TITLE) : null,
                ISBN, params -> params.isbn() != null ? getSpecification(params, ISBN) : null,
                PRICE, params -> (params.minPrice() != null || params.maxPrice() != null)
                        ? getSpecification(params, PRICE) : null
        );

        return specificationMap.values().stream()
                               .map(func -> func.apply(paramsDto))
                               .filter(Objects::nonNull)
                               .reduce(Specification.where(null), Specification::and);
    }

    private Specification<Book> getSpecification(BookSearchParametersDto params, String key) {
        return specificationProviderManager.getSpecificationProvider(key)
                                           .getSpecification(params);
    }
}
