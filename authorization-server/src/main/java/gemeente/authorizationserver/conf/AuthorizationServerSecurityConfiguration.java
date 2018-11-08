package gemeente.authorizationserver.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
//@Order(1)
@EnableWebSecurity
public class AuthorizationServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean()
        throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET,"/resources/**", "/favicon.ico", "/show-login");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/oauth/authorize").authenticated()
            //.antMatchers(HttpMethod.GET,"/show-login", "/resources/**").permitAll()
            //.antMatchers("/user/**").hasRole("USER")
            .and()
            .exceptionHandling()
            .accessDeniedPage("/show-login?authorization_error=true")
            .and()
            // TODO: put CSRF protection back into this endpoint
            .csrf()
            .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
            .disable()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/show-login")
            .and()
            .formLogin()
            .loginProcessingUrl("/login")
            .failureUrl("/show-login?authentication_error=true")
            .loginPage("/show-login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("john")
            .password(passwordEncoder().encode("123"))
            .roles("USER");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
