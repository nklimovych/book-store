package mate.academy.bookstore.repository.book.specification;

import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookSearchParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider extends AbstractSpecificationProvider<Book> {
    private static final int MIN_PRICE = 0;
    private static final int MAX_PRICE = 1;

    @Override
    public String getKey() {
        return BookSearchParameter.PRICE.getKey();
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(getKey()), params[MIN_PRICE], params[MAX_PRICE]));
    }
}
