package mate.academy.bookstore.repository.book.specification;

import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookSearchParameter;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider extends AbstractSpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSearchParameter.ISBN.getKey();
    }
}
