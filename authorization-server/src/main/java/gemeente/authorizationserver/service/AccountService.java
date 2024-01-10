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

    @Autowired
    private EmailService emailService;

    /**
     * Get account by username
     * @param username not null
     * @return not null, should exists
     */
    public Account getAccount(final String username) {
        return this.jdbcTemplate.queryForObject("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, u.email FROM accounts a INNER JOIN users u ON (a.id = u.account_id) WHERE u.username = ?", new Object[]{username}, new AccountRowMapper());
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

    public Optional<Account> getAccountById(final String id) {
        final List<Account> accounts = this.jdbcTemplate.query("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, a.email FROM accounts a WHERE a.id = ?", new Object[]{ id }, new AccountRowMapper());
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

    public Account updateOrCreateAccount(final Account account) {
        Optional<Account> accountOptional = this.getAccount(account.getRegistrationReferenceId(), account.getEmailAddress());
        if (accountOptional.isPresent()) {
            final Account existingAccount = accountOptional.get();
            final Object[] params = new Object[] {
                    account.getFullName(),
                    account.getAddress().getStreet(),
                    account.getAddress().getPostalCode(),
                    account.getAddress().getCity(),
                    account.getEmailAddress(),
                    existingAccount.getId().toString() };
            final int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
            final int result =
                    this.jdbcTemplate.update("UPDATE accounts SET salutation = ?, address = ?, postal_code = ?, city = ?, email = ? WHERE id = ?", params, types);
            return this.getAccountById(existingAccount.getId().toString()).orElseThrow(IllegalStateException::new);
        }
        else {
            return this.createAccount(account);
        }
    }

    Optional<Account> getAccount(String registrationReference, String emailAddress) {
        Optional<Account> existingAccountOptionalRegistrationReference = this.getAccountByRegistrationReference(registrationReference);
        if (existingAccountOptionalRegistrationReference.isPresent()) {
            return existingAccountOptionalRegistrationReference;
        }

        Optional<Account> existingAccountOptionalEmail = this.getAccountByEmail(emailAddress);
        return existingAccountOptionalEmail;
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

    public void updateUser(final Account account, final String userName, final String password) throws AccountUpdateException {
        if (!accountJdbcUserDetailsManager.userExists(userName)) {
            throw new AccountUpdateException("User " + userName + " does not exists.");
        }

        accountJdbcUserDetailsManager.updatePassword(userName, passwordEncoder.encode(password));
        accountJdbcUserDetailsManager.resetToken(account, account.getEmailAddress());
    }

    public UUID createResetToken(final Account account, final String email, final ResetToken resetToken) {
        resetToken.setToken(UUID.randomUUID());
        accountJdbcUserDetailsManager.storeResetToken(account, email, resetToken.getToken());
        emailService.sendPasswordReset(account, resetToken);
        return resetToken.getToken();
    }

    public Optional<Account> getAccountByToken(final String token) {
        String username = getUsernameByToken(token);
        if (username != null) {
            return Optional.of(this.getAccount(username));
        }
        return Optional.empty();
    }

    public String getUsernameByToken(final String token) {
        return accountJdbcUserDetailsManager.getUserNameByToken(token);
    }

    public List<Account> getAccounts() {
        final List<Account> accounts = this.jdbcTemplate.query("SELECT a.id, a.registration_reference, a.salutation, a.address, a.postal_code, a.city, a.email FROM accounts a ORDER BY a.salutation", new Object[]{ }, new AccountRowMapper());
        return accounts;
    }
}
