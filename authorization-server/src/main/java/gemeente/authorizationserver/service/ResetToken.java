package gemeente.authorizationserver.service;

import java.util.UUID;

public class ResetToken {
    private UUID token;
    private String url;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String generateUrl() {
        return this.url + "?token=" + this.token.toString();
    }
}
