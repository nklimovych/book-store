package mate.academy.bookstore.dto;

public record BookSearchParametersDto(
        String[] author,
        String[] title,
        String[] isbn,
        String minPrice,
        String maxPrice
) {
}
