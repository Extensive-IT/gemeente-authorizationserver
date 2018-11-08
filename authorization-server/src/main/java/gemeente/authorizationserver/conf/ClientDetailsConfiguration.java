package gemeente.authorizationserver.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "oauth2.clients")
public class ClientDetailsConfiguration {
    private List<RegisteredClientConfiguration> registeredClients;

    public List<RegisteredClientConfiguration> getRegisteredClients() {
        return registeredClients;
    }

    public void setRegisteredClients(List<RegisteredClientConfiguration> registeredClients) {
        this.registeredClients = registeredClients;
    }
}
