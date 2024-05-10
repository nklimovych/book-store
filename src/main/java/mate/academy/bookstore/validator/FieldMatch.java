package mate.academy.bookstore.validator;

import static java.lang.annotation.ElementType.TYPE;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
public @interface FieldMatch {
    String message() default "{mate.academy.bookstore.validator.FieldMatch}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();
    String fieldMatch();
}
