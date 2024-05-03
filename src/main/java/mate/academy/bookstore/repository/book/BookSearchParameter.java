package mate.academy.bookstore.repository.book;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookSearchParameter {
    AUTHOR("author"),
    TITLE("title"),
    ISBN("isbn"),
    PRICE("price");

    private final String key;
}
