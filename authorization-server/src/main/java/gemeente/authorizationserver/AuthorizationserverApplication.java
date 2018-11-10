package gemeente.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@SpringBootApplication
public class AuthorizationserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationserverApplication.class, args);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		final JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(primaryUserDataSource());
		return jdbcUserDetailsManager;
	}

	@Bean
	@ConfigurationProperties(prefix = "datasource.users")
	public DataSource primaryUserDataSource() {
		return DataSourceBuilder.create().build();
	}
}
