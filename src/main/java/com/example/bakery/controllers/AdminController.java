/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bakery.controllers;



import com.example.bakery.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;
    @GetMapping("/adminlogin")
    public String adminLogin() {
        return "adminlogin"; // WITHOUT .html
    }
        @PostMapping("/adminlogin")
public String login(@RequestParam String name,
                    @RequestParam String password,
                    HttpSession session,
                    Model model) {

    var admin = adminRepository.findByNameAndPassword(name, password);

    if (admin.isPresent()) {
        session.setAttribute("adminName", name);
        return "redirect:/adminhome";
    } else {
        model.addAttribute("error", "Invalid username or password");
        return "adminlogin";
    }
}


    @GetMapping("/adminhome")
public String adminHome(HttpSession session, Model model) {

    String adminName = (String) session.getAttribute("adminName");

    if (adminName == null) {
        return "redirect:/adminlogin";
    }

    model.addAttribute("adminName", adminName);
    return "adminhome";
}
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();   // destroy session
    return "redirect:/adminlogin";
}
}
