package gemeente.authorization.api;

import java.util.Objects;

public class AccountInformationResponse {
    private Account account;

    public AccountInformationResponse(final Account account) {
        this.account = Objects.requireNonNull(account);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
