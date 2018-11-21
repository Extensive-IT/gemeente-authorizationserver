package gemeente.authorization.api;

import java.util.Objects;
import java.util.StringJoiner;

public class AccountInformationResponse {
    private Account account;

    public AccountInformationResponse() {

    }

    public AccountInformationResponse(final Account account) {
        this.account = Objects.requireNonNull(account);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountInformationResponse.class.getSimpleName() + "[", "]")
                .add("account=" + account)
                .toString();
    }
}
