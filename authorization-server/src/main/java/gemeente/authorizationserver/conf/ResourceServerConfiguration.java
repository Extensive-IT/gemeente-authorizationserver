package gemeente.authorizationserver.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
//@Order(value = Ordered.LOWEST_PRECEDENCE)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "auth";

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.
                requestMatchers().antMatchers("/user/**")
                .and().
                authorizeRequests().antMatchers("/user/**").access("#oauth2.hasScope('read')");
                //.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
