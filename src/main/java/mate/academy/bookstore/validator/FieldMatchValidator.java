package mate.academy.bookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import org.apache.commons.lang3.StringUtils;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegistrationRequestDto userDto, ConstraintValidatorContext context) {
        return userDto != null
                && !StringUtils.isBlank(userDto.getPassword())
                && Objects.equals(userDto.getPassword(), userDto.getRepeatPassword());
    }
}
