package gemeente.authorizationserver.controller;

import java.security.Principal;

import gemeente.authorization.api.AccountInformationResponse;
import gemeente.authorization.api.Account;
import gemeente.authorizationserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/user/me")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("/user/account")
    public AccountInformationResponse getAccountInformation(final Principal principal) {
        final Account account = this.accountService.getAccount(principal.getName());
        return new AccountInformationResponse(account);
    }
}