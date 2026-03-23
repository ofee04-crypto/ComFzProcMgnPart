package com.fz.patentmgn.controller;

import com.fz.patentmgn.model.User;
import com.fz.patentmgn.service.LogService;
import com.fz.patentmgn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LogService logService;

    public UserController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("loggedInRole"));
    }

    @GetMapping
    public String index(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "權限不足，僅限系統管理員操作。");
            return "redirect:/";
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "users";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("user", new User());
        model.addAttribute("isEdit", false);
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "user_form";
    }

    @GetMapping("/edit/{username}")
    public String editForm(@PathVariable("username") String userNameToEdit, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/";
        User user = userService.getUserByUsername(userNameToEdit);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "找不到該帳號");
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("isEdit", true);
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "user_form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/";
        
        String actionBy = (String) session.getAttribute("loggedInUser");
        User existingUser = userService.getUserByUsername(user.getUsername());
        
        userService.saveUser(user);
        
        if (existingUser == null) {
            logService.recordLog(actionBy, "新增系統帳號: " + user.getUsername());
            redirectAttributes.addFlashAttribute("message", "帳號新增成功！");
        } else {
            logService.recordLog(actionBy, "修改系統帳號: " + user.getUsername());
            redirectAttributes.addFlashAttribute("message", "帳號修改成功！");
        }
        
        return "redirect:/users";
    }

    @PostMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String userNameToDelete, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/";
        String actionBy = (String) session.getAttribute("loggedInUser");
        
        if ("admin".equals(userNameToDelete)) {
            redirectAttributes.addFlashAttribute("error", "無法刪除預設管理員 admin！");
        } else {
            userService.deleteUser(userNameToDelete);
            logService.recordLog(actionBy, "刪除系統帳號: " + userNameToDelete);
            redirectAttributes.addFlashAttribute("message", "帳號 " + userNameToDelete + " 已刪除。");
        }
        return "redirect:/users";
    }
}
