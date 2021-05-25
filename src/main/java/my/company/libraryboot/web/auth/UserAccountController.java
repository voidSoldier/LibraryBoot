package my.company.libraryboot.web.auth;

import my.company.libraryboot.model.User;
import my.company.libraryboot.model.enums.Role;
import my.company.libraryboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.HashSet;

import static my.company.libraryboot.config.WebSecurityConfig.PASSWORD_ENCODER;

@Controller
public class UserAccountController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        prepareAndSave(userForm);

//        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        securityService.autoLogin(userForm.getEmail(), userForm.getPassword());

        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your email or password is invalid!!!");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }

    private void prepareAndSave(User user) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? PASSWORD_ENCODER.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        user.setRoles(new HashSet<>(Collections.singletonList(Role.USER))); // immutable

        /*
        user.setRoles(new HashSet<>(Arrays.asList(Role.USER)));
        user.setRoles(new HashSet<>(){{add(Role.USER);}}); // creates anonymous classes each time it's called
        */
        userRepository.save(user);
    }
}
