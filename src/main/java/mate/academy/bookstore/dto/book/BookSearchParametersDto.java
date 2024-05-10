package mate.academy.bookstore.dto.book;

public record BookSearchParametersDto(
        String[] author,
        String title,
        String isbn,
        Integer minPrice,
        Integer maxPrice
) {
}
