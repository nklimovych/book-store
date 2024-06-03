package mate.academy.bookstore.util;

import java.math.BigDecimal;
import java.util.Set;

public class TestConstants {
    public static final String DELETE_DATA_FROM_DB = "classpath:database/delete-data-from-db.sql";
    public static final String INSERT_DATA_INTO_DB = "classpath:database/insert-data-into-db.sql";

    public static final String VALID_BOOK_TITLE = "Kobzar";
    public static final String VALID_BOOK_AUTHOR = "Taras Shevchenko";
    public static final String VALID_BOOK_ISBN = "978-1-1516-4732-0";
    public static final BigDecimal VALID_BOOK_PRICE = BigDecimal.valueOf(34.99);
    public static final Set<Long> SET_OF_VALID_CATEGORY_ID = Set.of(1L);
    public static final String VALID_BOOK_DESCRIPTION = "Awesome description...";
    public static final String VALID_BOOK_COVER_IMAGE = "kobzar.png";
    public static final Long VALID_BOOK_ID = 1L;

    public static final Long VALID_CATEGORY_ID = 1L;
    public static final String VALID_CATEGORY_NAME = "Poetry";
    public static final String NEW_VALID_CATEGORY_NAME = "New Poetry";
    public static final String VALID_CATEGORY_DESCRIPTION = "Awesome description...";

    public static final int NUMBER_OF_INVOCATIONS = 1;
    public static final int EXPECTED_LIST_SIZE = 1;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 5;
    public static final int VALID_MIN_PRICE = 10;
    public static final int VALID_MAX_PRICE = 20;
    public static final int NEGATIVE_PRICE = -10;
    public static final int ZERO_MAX_PRICE = 0;

    public static final String INVALID_CATEGORY_NAME = "Coco Jamboo";
    public static final String VALID_CATEGORY_NAME_DIFFERENT_CASES = "pOetRy";
    public static final String CATEGORY_NAME_SPECIAL_CHARACTERS = "Po*try";
    public static final String EMPTY_STRING = "";
    public static final String INVALID_BOOK_ISBN = "978-11-1516-4732-02";
    public static final String VALID_BOOK_ISBN_NOT_IN_DB = "978-1-2345-6789-0";
    public static final Long INVALID_ID = -1L;
    public static final Long EMPTY_CATEGORY_ID = 4L;
}
