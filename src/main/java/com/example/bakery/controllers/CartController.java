/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bakery.controllers;

import com.example.bakery.component.Cart;
import com.example.bakery.entity.CategoryItem;

import com.example.bakery.repository.CategoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.bakery.entity.Orderrequest;
import com.example.bakery.entity.ShopOrder;
import com.example.bakery.repository.ShopOrderRepository;
import com.example.bakery.service.pdfService;
import java.io.ByteArrayInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {

    @Autowired
    CategoryItemRepository itemRepo;

    // Inject the Session-Scoped Cart here
    @Autowired
    Cart cart;
    @Autowired ShopOrderRepository orderRepo; // <--- Inject this

    @Autowired pdfService pdfService; // Inject the Service
    @GetMapping("/cart/add/{id}/{qty}")
    public String addToCart(@PathVariable Long id, @PathVariable int qty) {
        
        // 1. Fetch item from DB
        CategoryItem itemFromDb = itemRepo.findById(id).orElse(null);
        
        if (itemFromDb != null) {
            // 2. We must create a COPY or clone of the item because
            // strictly speaking, the DB entity is a singleton reference in JPA.
            // For simplicity here, we set the quantity directly, but in a pro app, 
            // you would map this to a "CartDTO" object.
            itemFromDb.setQuantity(String.valueOf(qty));
            
            // 3. Add to the USER'S specific cart
            cart.addItem(itemFromDb);
        }
        
        return "redirect:/cart";
    }
    @GetMapping("/cart")
    public String viewCart(Model model) {
        // Use the methods we built in the Cart class
        model.addAttribute("cartCount", cart.getItems().size());
        model.addAttribute("total", cart.getTotalPrice());
        model.addAttribute("cart", cart.getItems());
        return "cart";
    }

    @GetMapping("/cart/removeItem/{index}")
    public String removeItem(@PathVariable int index) {
        cart.removeItem(index);
        return "redirect:/cart";
    }
    // 1. SHOW CHECKOUT FORM
    @GetMapping("/checkout")
    public String showCheckout(Model model) {
        if(cart.getItems().isEmpty()) {
            return "redirect:/cart"; // Kick them out if cart is empty
        }
        model.addAttribute("orderRequest", new Orderrequest());
        model.addAttribute("total", cart.getTotalPrice());
        return "checkout";
    }

    // 2. GENERATE PDF & DOWNLOAD
    @PostMapping("/checkout")
    public ResponseEntity<InputStreamResource> checkout(@ModelAttribute Orderrequest request) {
        
        // 1. Create Order Entity
        ShopOrder order = new ShopOrder();
        order.setCustomerName(request.getFullName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setAddress(request.getAddress());
        order.setTotalAmount(cart.getTotalPrice());
        
        StringBuilder summary = new StringBuilder();
        for(CategoryItem item : cart.getItems()){
            summary.append(item.getItemName()).append(" (").append(item.getQuantity()).append("), ");
        }
        order.setOrderDescription(summary.toString());

        // 2. SAVE AND CAPTURE THE SAVED ORDER (To get the ID)
        ShopOrder savedOrder = orderRepo.save(order); // <--- Returns the object with the new ID

        // 3. Generate PDF (Pass the ID now)
        ByteArrayInputStream bis = pdfService.createPdf(request, cart, savedOrder.getId());

        cart.clearCart();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bakery_bill.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }
}