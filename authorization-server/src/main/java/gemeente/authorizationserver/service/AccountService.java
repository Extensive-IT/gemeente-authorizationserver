package gemeente.authorizationserver.service;

import gemeente.authorization.api.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
    public Account getAccount(final String id) {
        final Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setEmailAddress("test-email@test.com");
        account.setFullName("Mr. Test Himself");
        return account;
    }
}
