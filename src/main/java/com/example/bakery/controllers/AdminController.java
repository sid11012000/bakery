/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bakery.controllers;



import com.example.bakery.repository.AdminRepository;
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
                        Model model) {

        var admin = adminRepository.findByNameAndPassword(name, password);

        if (admin.isPresent()) {
            return "redirect:/adminhome";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "adminlogin";
        }
    }

    @GetMapping("/adminhome")
    public String adminHome() {
        return "adminhome";
    }

}
