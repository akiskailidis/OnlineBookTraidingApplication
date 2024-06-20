package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import app.model.User;
import app.model.UserProfile;
import app.services.UserProfileService;
import app.services.UserService;

@Controller
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    UserProfileService useProfileService;

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        return "auth/signin";
    }

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "auth/signup";
    }

    @RequestMapping("/save")
    public String registerUser(@ModelAttribute("user") User user, Model model){
       
        if(userService.isUserPresent(user)){
            model.addAttribute("successMessage", "User already registered!");
            return "auth/signin";
        }

        userService.saveUser(user);
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(user.getUsername());
        useProfileService.save(userProfile);
        model.addAttribute("successMessage", "User registered successfully!");

        return "auth/signin";
    }

    @RequestMapping("/user/main-menu")
    public String userMainMenu() {
        return "user/main-menu"; // path to the user main menu view
    }
}
