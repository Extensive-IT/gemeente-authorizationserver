package gemeente.authorizationserver.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.HtmlUtils;

/**
 * Controller for displaying the approval page for the authorization server.
 * Inspired by {@link org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint}
 */
@SessionAttributes("authorizationRequest")
@Controller
public class CustomApprovalEndpoint {

    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(final Map<String, Object> model, HttpServletRequest request) throws Exception {

        final AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        model.put("clientId", authorizationRequest.getClientId());
        model.put("showCsrf", Boolean.FALSE);

        CsrfToken csrfToken = extractCsrfToken(model, request);
        if (csrfToken != null) {
            model.put("showCsrf", Boolean.TRUE);
            model.put("csrfTokenName", csrfToken.getParameterName());
            model.put("csrfTokenValue", csrfToken.getToken());
        }

        if (model.containsKey("scopes") || request.getAttribute("scopes") != null) {
            model.put("scopesView", createScopes(model, request));
        }
        return "authorize";
    }

    private CsrfToken extractCsrfToken(final Map<String, Object> model, final HttpServletRequest request) {
        CsrfToken csrfToken = null;
        if (model.containsKey("_csrf")) {
            csrfToken = (CsrfToken)model.get("_csrf");
        }
        if (csrfToken == null && request.getAttribute("_csrf") != null) {
            csrfToken = (CsrfToken)request.getAttribute("_csrf");
        }
        return csrfToken;
    }

    private CharSequence createScopes(Map<String, Object> model, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("<ul>");
        @SuppressWarnings("unchecked")
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ?
            model.get("scopes") : request.getAttribute("scopes"));
        for (String scope : scopes.keySet()) {
            String approved = "true".equals(scopes.get(scope)) ? " checked" : "";
            String denied = !"true".equals(scopes.get(scope)) ? " checked" : "";
            scope = HtmlUtils.htmlEscape(scope);

            builder.append("<li><div class=\"form-group\">");
            builder.append(scope).append(": <input type=\"radio\" name=\"");
            builder.append(scope).append("\" value=\"true\"").append(approved).append(">Approve</input> ");
            builder.append("<input type=\"radio\" name=\"").append(scope).append("\" value=\"false\"");
            builder.append(denied).append(">Deny</input></div></li>");
        }
        builder.append("</ul>");
        return builder.toString();
    }
}