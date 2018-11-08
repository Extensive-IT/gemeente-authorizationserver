package gemeente.authorizationserver.conf;

import java.util.List;
import org.springframework.core.style.ToStringCreator;

class RegisteredClientConfiguration {

    private String clientId;
    private String secret;
    private List<String> authorizedGrantTypes;
    private List<String> authorities;
    private List<String> scopes;
    private Boolean autoApprove;
    private List<String> registeredRedirectUris;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public Boolean getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public List<String> getRegisteredRedirectUris() {
        return registeredRedirectUris;
    }

    public void setRegisteredRedirectUris(List<String> registeredRedirectUris) {
        this.registeredRedirectUris = registeredRedirectUris;
    }

    public String toString() {
        return new ToStringCreator(this).append("clientId", this.clientId)
            .append("autoApprove", this.autoApprove)
            .append("authorizedGrantTypes", this.authorizedGrantTypes)
            .append("authorities", this.authorities).append("scopes", this.scopes).toString();
    }
}
