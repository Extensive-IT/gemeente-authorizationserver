package gemeente.authorizationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 "access_token": "3a8fc366-37ed-4115-9565-95b89bddacba",
 "token_type": "bearer",
 "refresh_token": "5a1e33e1-bf36-437a-8879-4d30d405d8cc",
 "expires_in": 1766,
 "scope": "write"
 }
 */
public class OAuthTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String toString() {
        return "OAuthToken [access_token: " + this.getAccessToken() + "]";
    }
}

