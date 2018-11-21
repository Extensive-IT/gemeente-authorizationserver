package gemeente.authorizationserver;

import gemeente.authorization.api.AccountCreationRequest;
import gemeente.authorization.api.AccountCreationResponse;
import gemeente.authorization.api.AccountInformationResponse;
import gemeente.authorizationserver.model.OAuthTokenResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

public class ApiIntegrationTest {
    private static final Logger logger = Logger.getLogger(ApiIntegrationTest.class.getSimpleName());

    private String baseUrl = "http://gemeente-auth:8080/";
    private String username = "testadmin";
    private String password = "test123";

    private RestTemplate restTemplate;

    private String accessToken;

    public ApiIntegrationTest() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setInterceptors(Arrays.asList((ClientHttpRequestInterceptor) new BasicAuthInterceptor("first-app", "12345")));
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Test
    public void testLogon() throws Exception {
        final String url = String.format(baseUrl + "oauth/token?grant_type=password&username=%s&password=%s&scope=read", username, password);
        final ResponseEntity<OAuthTokenResponse> response = restTemplate.postForEntity(url, null, OAuthTokenResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            accessToken = response.getBody().getAccessToken();
        }
        assertNotNull(accessToken);
    }

    private String getToken() {
        if (accessToken == null) {
            final String url = String.format(baseUrl + "oauth/token?grant_type=password&username=%s&password=%s&scope=read", username, password);
            final ResponseEntity<OAuthTokenResponse> response = restTemplate.postForEntity(url, null, OAuthTokenResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                accessToken = response.getBody().getAccessToken();
                assertNotNull(accessToken);
            }
        }
        return this.accessToken;
    }

    @Test
    public void testGetAccountInformation() {
        final String url = String.format(baseUrl + "user/account?access_token=%s", getToken());

        final ResponseEntity<AccountInformationResponse> response = restTemplate.getForEntity(url, AccountInformationResponse.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            Assert.fail("Not right response: " + response.getStatusCode() + ", " + response.getHeaders().toString());
        }
        logger.info(response.getBody().toString());
    }

    @Test
    public void testCreateAccount() {
        final AccountCreationRequest request = new AccountCreationRequest();
        request.setFullName("Test");
        request.setEmailAddress("test@test.com");
        request.setRegistrationReferenceId("NL-123");

        final String url = String.format(baseUrl + "user/create?access_token=%s", getToken());

        final ResponseEntity<AccountCreationResponse> response = restTemplate.postForEntity(url, request, AccountCreationResponse.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            Assert.fail("Not right response: " + response.getStatusCode() + ", " + response.getHeaders().toString());
        }
        logger.info(response.getBody().toString());
    }
}
