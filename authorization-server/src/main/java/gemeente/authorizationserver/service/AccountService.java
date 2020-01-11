package gemeente.authorizationserver.service;

import gemeente.authorization.api.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AccountJdbcUserDetailsManager accountJdbcUserDetailsManager;

    /**
     * Get account by username
     * @param username not null
     * @return not null, should exists
     */
    public Account getAccount(final String username) {
        final Account account = this.jdbcTemplate.queryForObject("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, u.email FROM accounts a INNER JOIN users u ON (a.id = u.account_id) WHERE u.username = ?", new Object[]{username}, new AccountRowMapper());
        return account;
    }

    /**
     * Get account by email address
     * @param email not null
     * @return optional of nullable
     */
    public Optional<Account> getAccountByEmail(final String email) {
        final List<Account> accounts = this.jdbcTemplate.query("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, a.email FROM accounts a WHERE a.email = ?", new Object[]{ email }, new AccountRowMapper());
        return accounts.stream().findAny();
    }

    /**
     * Get account by email address
     * @param registrationReference not null
     * @return optional of nullable
     */
    public Optional<Account> getAccountByRegistrationReference(final String registrationReference) {
        final List<Account> accounts = this.jdbcTemplate.query("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, a.email FROM accounts a WHERE a.registration_reference = ?", new Object[]{ registrationReference }, new AccountRowMapper());
        return accounts.stream().findAny();
    }

    public Account createAccount(final Account account) {
        Optional<Account> existingAccountOptionalEmail = this.getAccountByEmail(account.getEmailAddress());
        if (existingAccountOptionalEmail.isPresent()) {
            return existingAccountOptionalEmail.get();
        }

        Optional<Account> existingAccountOptionalRegistrationReference = this.getAccountByRegistrationReference(account.getRegistrationReferenceId());
        if (existingAccountOptionalRegistrationReference.isPresent()) {
            return existingAccountOptionalRegistrationReference.get();
        }

        // Create account
        account.setId(UUID.randomUUID());

        // define query arguments
        final Object[] params = new Object[] { account.getId().toString(), account.getRegistrationReferenceId(), account.getFullName(), account.getAddress().getStreet(), account.getAddress().getPostalCode(), account.getAddress().getCity(), account.getEmailAddress() };
        final int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };

        final int result =
                this.jdbcTemplate.update("INSERT INTO accounts (id, registration_reference, salutation, address, postal_code, city, email) VALUES (?, ?, ?, ?, ?, ?, ?)", params, types);
        return account;
    }

    public void createUser(final Account account, final String userName, final String password) throws AccountCreationException {
        if (accountJdbcUserDetailsManager.userExists(userName)) {
            throw new AccountCreationException("User " + userName + " already exists.");
        }
        try {
            final SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
            accountJdbcUserDetailsManager.createUser(new User(userName, passwordEncoder.encode(password), Arrays.asList(authority)), account);
        }
        catch (RuntimeException e) {
            throw new AccountCreationException("User cannot be created at the moment, due: " + e.getMessage());
        }
    }

    public List<Account> getAccounts() {
        final List<Account> accounts = this.jdbcTemplate.query("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, a.email FROM accounts a ORDER BY a.salutation", new Object[]{ }, new AccountRowMapper());
        return accounts;
    }
}
