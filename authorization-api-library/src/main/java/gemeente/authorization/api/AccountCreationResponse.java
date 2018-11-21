package gemeente.authorization.api;

import java.util.Objects;
import java.util.StringJoiner;

public class AccountCreationResponse {
    private Account account;

    public AccountCreationResponse() {
        // Default constructor
    }

    public AccountCreationResponse(final Account account) {
        this.account = Objects.requireNonNull(account);
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return this.account;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountCreationResponse.class.getSimpleName() + "[", "]")
                .add("account=" + account)
                .toString();
    }
}
