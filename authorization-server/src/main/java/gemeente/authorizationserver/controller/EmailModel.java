package gemeente.authorizationserver.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EmailModel {

    @NotNull
    @NotEmpty
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}