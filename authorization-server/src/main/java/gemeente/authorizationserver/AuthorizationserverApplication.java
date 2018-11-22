package gemeente.authorizationserver;

import gemeente.authorizationserver.service.AccountJdbcUserDetailsManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@SpringBootApplication
public class AuthorizationserverApplication implements WebMvcConfigurer {

	@Value("${content.resources.directory}")
	private String contentResourcesDirectory;

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationserverApplication.class, args);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		final JdbcUserDetailsManager jdbcUserDetailsManager = new AccountJdbcUserDetailsManager(primaryUserDataSource());
		return jdbcUserDetailsManager;
	}

	@Bean
	@ConfigurationProperties(prefix = "datasource.users")
	public DataSource primaryUserDataSource() {
		return DataSourceBuilder.create().build();
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/external/**").addResourceLocations("file://" + contentResourcesDirectory);
	}
}
