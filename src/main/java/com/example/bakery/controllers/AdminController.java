/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bakery.controllers;



import com.example.bakery.repository.AdminRepository;
import com.example.bakery.repository.CategoryItemRepository;
import com.example.bakery.repository.CategoryRepository;
import com.example.bakery.repository.ShopOrderRepository;
import com.example.bakery.repository.SubCategoryRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired private CategoryRepository categoryRepo;
    @Autowired private SubCategoryRepository subCategoryRepo;
    @Autowired private CategoryItemRepository itemRepo;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired ShopOrderRepository orderRepo;
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
 
    model.addAttribute("categoryCount", categoryRepo.count());
        model.addAttribute("subCategoryCount", subCategoryRepo.count());
        model.addAttribute("itemCount", itemRepo.count());
    model.addAttribute("adminName", adminName);
    return "adminhome";
}
@GetMapping("/admin/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderRepo.findAllByOrderByOrderDateDesc());
        return "adminOrders"; // We will create this HTML file next
    }
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();   // destroy session
    return "redirect:/adminlogin";
}
}
