package gemeente.authorizationserver.service;

import gemeente.authorization.api.Account;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountJdbcUserDetailsManager extends JdbcUserDetailsManager {

    private static final String DEF_CREATE_USER_SQL = "insert into users (account_id, username, password, enabled, email) values (?,?,?,?,?)";
    private static final String DEF_INSERT_AUTHORITY_SQL = "insert into authorities (username, authority) values (?,?)";

    public AccountJdbcUserDetailsManager(DataSource dataSource) {
        super(dataSource);
    }

    public void createUser(final UserDetails user, final Account account) {

        getJdbcTemplate().update(DEF_CREATE_USER_SQL, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, account.getId().toString());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPassword());
                ps.setBoolean(4, user.isEnabled());
                ps.setString(5, account.getEmailAddress());
            }

        });

        if (getEnableAuthorities()) {
            insertUserAuthorities(user);
        }
    }

    private void insertUserAuthorities(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            getJdbcTemplate().update(DEF_INSERT_AUTHORITY_SQL, user.getUsername(),
                    auth.getAuthority());
        }
    }
}
