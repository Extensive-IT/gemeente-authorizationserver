package gemeente.authorizationserver.service;

import gemeente.authorization.api.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Account getAccount(final String username) {
        final Account account = this.jdbcTemplate.queryForObject("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, u.email FROM accounts a INNER JOIN users u ON (a.id = u.account_id) WHERE u.username = ?", new Object[]{username}, new AccountRowMapper());
        return account;
    }

    public Account createAccount(final Account account) {
        account.setId(UUID.randomUUID());
        return account;
    }
}
