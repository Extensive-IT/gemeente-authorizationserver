package gemeente.authorizationserver.service;

import gemeente.authorization.api.Account;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AccountJdbcUserDetailsManager extends JdbcUserDetailsManager {

    private static final String DEF_CREATE_USER_SQL = "insert into users (account_id, username, password, enabled, email) values (?,?,?,?,?)";
    private static final String DEF_INSERT_AUTHORITY_SQL = "insert into authorities (username, authority) values (?,?)";
    private static final String DEF_SET_RESET_TOKEN = "update users set reset_token = ? where account_id = ? and email = ?";
    private static final String DEF_SET_RESET_TOKEN_RESET = "update users set reset_token = null where account_id = ? and email = ?";
    private static final String DEF_SELECT_USERNAME_BY_TOKEN = "select username from users where reset_token = ? and reset_token IS NOT NULL";

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

    public void storeResetToken(Account account, String email, UUID token) {
        getJdbcTemplate().update(DEF_SET_RESET_TOKEN, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, token.toString());
                ps.setString(2, account.getId().toString());
                ps.setString(3, email);
            }
        });
    }

    public void resetToken(Account account, String email) {
        getJdbcTemplate().update(DEF_SET_RESET_TOKEN_RESET, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, account.getId().toString());
                ps.setString(2, email);
            }
        });
    }

    public String getUserNameByToken(final String token) {
        if (token != null && token.length() > 16) {
            List<String> users = getJdbcTemplate().queryForList(DEF_SELECT_USERNAME_BY_TOKEN,
                    new String[]{token}, String.class);

            if (!users.isEmpty()) {
                return users.get(0);
            }
        }

        return null;
    }

    public void updatePassword(final String userName, final String password) {
        getJdbcTemplate().update(DEF_CHANGE_PASSWORD_SQL, password, userName);
    }

    private void insertUserAuthorities(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            getJdbcTemplate().update(DEF_INSERT_AUTHORITY_SQL, user.getUsername(),
                    auth.getAuthority());
        }
    }
}
