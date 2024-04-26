package mate.academy.bookstore.dto;

public record BookSearchParametersDto(
        String[] authors,
        String[] titles,
        String[] isbn,
        String minPrice,
        String maxPrice
) {
}