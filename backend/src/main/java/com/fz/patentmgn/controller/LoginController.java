package com.fz.patentmgn.controller;

import com.fz.patentmgn.service.LogService;
import com.fz.patentmgn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UserService userService;
    private final LogService logService;

    public LoginController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session != null && session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        com.fz.patentmgn.model.User user = userService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user.getUsername());
            session.setAttribute("loggedInRole", user.getRole());
            logService.recordLog(user.getUsername(), "使用者登入");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "帳號或密碼錯誤");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            String username = (String) session.getAttribute("loggedInUser");
            if (username != null) {
                logService.recordLog(username, "使用者登出");
            }
            session.invalidate();
        }
        return "redirect:/login";
    }
}
