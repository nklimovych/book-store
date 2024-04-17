package mate.academy.bookstore;

import java.math.BigDecimal;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Kobzar");
            book.setAuthor("Taras Shevchenko");
            book.setDescription("Description");
            book.setIsbn("ISBN 123456");
            book.setPrice(BigDecimal.TEN);

            bookService.save(book);
            bookService.findAll().forEach(System.out::println);
        };
    }
}
