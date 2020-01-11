package gemeente.authorizationserver.controller;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        if (obj instanceof UserModel) {
            UserModel user = (UserModel) obj;
            return user.getPassword().equals(user.getMatchingPassword());
        }
        if (obj instanceof PasswordResetModel) {
            PasswordResetModel user = (PasswordResetModel) obj;
            return user.getPassword().equals(user.getMatchingPassword());
        }
        return false;
    }
}