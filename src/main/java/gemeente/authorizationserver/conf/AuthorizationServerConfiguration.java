package gemeente.authorizationserver.conf;

import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsConfiguration clientDetailsConfiguration;

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        clientDetailsConfiguration.getRegisteredClients().forEach(registeredClientConfiguration -> {
            builder.withClient(registeredClientConfiguration.getClientId())
                .secret(passwordEncoder.encode(registeredClientConfiguration.getSecret()))
                .authorizedGrantTypes(Iterables
                    .toArray(registeredClientConfiguration.getAuthorizedGrantTypes(), String.class))
                .authorities(
                    Iterables.toArray(registeredClientConfiguration.getAuthorities(), String.class))
                .scopes(Iterables.toArray(registeredClientConfiguration.getScopes(), String.class))
                .autoApprove(registeredClientConfiguration.getAutoApprove())
                .redirectUris(Iterables.toArray(registeredClientConfiguration.getRegisteredRedirectUris(), String.class));
        });
    }

    @Override
    public void configure(
        final AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer)
        throws Exception {
        authorizationServerSecurityConfigurer.tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()").passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).userApprovalHandler(userApprovalHandler()).authenticationManager(authenticationManager)
            .authorizationCodeServices(authorizationCodeServices());
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Bean
    public UserApprovalHandler userApprovalHandler() {
        return new DefaultUserApprovalHandler();
    }
}
