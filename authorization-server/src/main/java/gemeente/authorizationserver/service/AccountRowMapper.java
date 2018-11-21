package gemeente.authorizationserver.service;

import gemeente.authorization.api.Account;
import gemeente.authorization.api.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Account account = new Account();
        account.setId(UUID.fromString(rs.getString("id")));
        account.setRegistrationReferenceId(rs.getString("registration_reference"));
        account.setFullName(rs.getString("salutation"));
        account.setEmailAddress(rs.getString("email"));

        final Address address = new Address();
        address.setStreet(rs.getString("address"));
        address.setPostalCode(rs.getString("postal_code"));
        address.setCity(rs.getString("city"));
        account.setAddress(address);
        return account;
    }
}
