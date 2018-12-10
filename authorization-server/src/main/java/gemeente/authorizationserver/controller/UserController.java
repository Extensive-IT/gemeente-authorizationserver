package gemeente.authorizationserver.controller;

import gemeente.authorization.api.Account;
import gemeente.authorization.api.AccountCreationRequest;
import gemeente.authorization.api.AccountInformationResponse;
import gemeente.authorizationserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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

    @PostMapping("/user/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccountInformationResponse createAccount(@RequestBody final AccountCreationRequest accountCreationRequest, final Principal principal) {
        final Account account = new Account();
        account.setRegistrationReferenceId(accountCreationRequest.getRegistrationReferenceId());
        account.setEmailAddress(accountCreationRequest.getEmailAddress());
        account.setFullName(accountCreationRequest.getFullName());
        account.setAddress(accountCreationRequest.getAddress());
        final Account createdAccount = this.accountService.createAccount(account);
        return new AccountInformationResponse(createdAccount);
    }
}