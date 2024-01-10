package gemeente.authorizationserver.controller;

import gemeente.authorization.api.Account;
import gemeente.authorizationserver.service.AccountCreationException;
import gemeente.authorizationserver.service.AccountService;
import gemeente.authorizationserver.service.AccountUpdateException;
import gemeente.authorizationserver.service.ResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    @Value("${content.logo:/resources/img/default-logo.svg}")
    private String pathLogo;

    @Value("${content.login.introduction:}")
    private String loginIntroduction;

    @Value("${content.login.not-registered:}")
    private String notRegisteredIntroduction;

    @Value("${content.register.introduction:}")
    private String registerIntroduction;

    @Value("${content.login.recover-password-heading:}")
    private String recoverPasswordHeading;

    @Value("${content.login.recover-password:}")
    private String recoverPasswordIntroduction;

    @Value("${content.login.reset-password-heading:}")
    private String resetPasswordHeading;

    @Value("${content.login.reset-password:}")
    private String resetPasswordIntroduction;

    @Value("${content.footer}")
    private String footer;

    @Value("${content.title}")
    private String title;

    @Value("${content.baseUrl}")
    private String baseUrl;

    @Value("${content.defaultAppUrl}")
    private String defaultAppUrl;

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String homeApp() {
        return "redirect:" + this.defaultAppUrl;
    }

    @GetMapping("/show-login")
    public String login(final Map<String, Object> model) {
        model.put("logo", pathLogo);
        model.put("introduction", loginIntroduction);
        model.put("notRegisteredIntroduction", notRegisteredIntroduction);
        model.put("title", title);
        model.put("footer", footer);
        return "login";
    }

    @GetMapping("/register")
    public String register(@RequestParam(value = "email", required = false) final String email, final Map<String, Object> model) {
        final UserModel userModel = new UserModel();
        userModel.setEmail(email);
        model.put("user", userModel);
        return showRegisterPage(model);
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserModel userModel, BindingResult bindingResult, final Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            return showRegisterPage(model);
        }

        final Optional<Account> account = this.accountService.getAccountByEmail(userModel.getEmail());
        if (!account.isPresent()) {
            // TODO: MessageSource
            bindingResult.rejectValue("email", "","E-mail address is unknown.");
            return showRegisterPage(model);
        }

        try {
            this.accountService.createUser(account.get(), userModel.getUserName(), userModel.getPassword());
        } catch (AccountCreationException e) {
            bindingResult.rejectValue("userName", "", e.getMessage());
            return showRegisterPage(model);
        }
        return "redirect:/show-login";
    }

    @GetMapping("/recover-password")
    public String recoverPassword(@RequestParam(value = "email", required = false) final String email, final Map<String, Object> model) {
        final EmailModel emailModel = new EmailModel();
        emailModel.setEmail(email);
        model.put("email", emailModel);
        return showRecoverPasswordEmailPage(model);
    }

    @PostMapping("/recover-password")
    public String recoverPasswordEmail(@ModelAttribute("email") @Valid EmailModel emailModel, BindingResult bindingResult, final Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            return showRecoverPasswordEmailPage(model);
        }

        final Optional<Account> account = this.accountService.getAccountByEmail(emailModel.getEmail());
        if (!account.isPresent()) {
            // TODO: MessageSource
            bindingResult.rejectValue("email", "","E-mail address is unknown.");
            return showRecoverPasswordEmailPage(model);
        }

        final ResetToken resetToken = new ResetToken();
        resetToken.setUrl(baseUrl + "/reset-password");

        this.accountService.createResetToken(account.get(), account.get().getEmailAddress(), resetToken);
        return "redirect:/show-login";
    }

    @GetMapping("/reset-password")
    public String displayResetPasswordPage(@RequestParam(value = "token") final String token, final Map<String, Object> model) {

        final Optional<Account> account = this.accountService.getAccountByToken(token);

        if (account.isPresent()) {
            final PasswordResetModel passwordResetModel = new PasswordResetModel();
            passwordResetModel.setToken(token);

            final String username = this.accountService.getUsernameByToken(token);
            model.put("reset", passwordResetModel);
            model.put("username", username);

            return showResetPasswordPage(model);
        }

        return "redirect:/show-login";
    }

    @PostMapping("/reset-password")
    public String recoverPasswordEmail(@ModelAttribute("reset") @Valid PasswordResetModel passwordResetModel, BindingResult bindingResult, final Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            final String username = this.accountService.getUsernameByToken(passwordResetModel.getToken());
            model.put("username", username);
            return showResetPasswordPage(model);
        }

        final Optional<Account> account = this.accountService.getAccountByToken(passwordResetModel.getToken());
        if (!account.isPresent()) {
            // TODO: MessageSource
            bindingResult.rejectValue("token", "","Token is not valid.");
            return showResetPasswordPage(model);
        }

        final String username = this.accountService.getUsernameByToken(passwordResetModel.getToken());

        try {
            this.accountService.updateUser(account.get(), username, passwordResetModel.getPassword());
        } catch (AccountUpdateException e) {
            bindingResult.rejectValue("password", "", e.getMessage());
            return showResetPasswordPage(model);
        }

        return "redirect:/show-login";
    }

    private String showRegisterPage(final Map<String, Object> model) {
        model.put("logo", pathLogo);
        model.put("introduction", registerIntroduction);
        model.put("title", title);
        model.put("footer", footer);
        return "register";
    }

    private String showRecoverPasswordEmailPage(final Map<String, Object> model) {
        model.put("logo", pathLogo);
        model.put("heading", recoverPasswordHeading);
        model.put("introduction", recoverPasswordIntroduction);
        model.put("title", title);
        model.put("footer", footer);
        return "recover-password-email";
    }

    private String showResetPasswordPage(final Map<String, Object> model) {
        model.put("logo", pathLogo);
        model.put("heading", resetPasswordHeading);
        model.put("introduction", resetPasswordIntroduction);
        model.put("title", title);
        model.put("footer", footer);
        return "reset-password";
    }
}


