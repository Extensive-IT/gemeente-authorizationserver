package gemeente.authorizationserver.controller;

import gemeente.authorization.api.Account;
import gemeente.authorizationserver.service.AccountCreationException;
import gemeente.authorizationserver.service.AccountService;
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

    @Value("${content.register.introduction:}")
    private String registerIntroduction;

    @Autowired
    private AccountService accountService;

    @GetMapping("/show-login")
    public String login(final Map<String, Object> model) {
        model.put("logo", pathLogo);
        model.put("introduction", loginIntroduction);
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

    private String showRegisterPage(final Map<String, Object> model) {
        model.put("logo", pathLogo);
        model.put("introduction", registerIntroduction);
        return "register";
    }
}


