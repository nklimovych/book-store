package mate.academy.bookstore.repository.book.specification;

import java.util.Arrays;
import mate.academy.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractSpecificationProvider<T> implements SpecificationProvider<T> {
    @Override
    public Specification<T> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(getKey()).in(Arrays.stream(params).toArray());
    }
}
