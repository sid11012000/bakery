/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bakery.component;

import com.example.bakery.entity.CategoryItem;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class Cart {
    
    private List<CategoryItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CategoryItem> getItems() {
        return items;
    }

    // REPLACE THE OLD addItem METHOD WITH THIS:
    public void addItem(CategoryItem newItem) {
        boolean itemExists = false;

        // 1. Loop through existing items to find a match
        for (CategoryItem existingItem : items) {
            
            // Check if IDs match (Is it the same product?)
            if (existingItem.getId().equals(newItem.getId())) {
                
                // 2. Convert Strings to Integers to do math
                int oldQty = Integer.parseInt(existingItem.getQuantity());
                int incomingQty = Integer.parseInt(newItem.getQuantity());
                
                // 3. Update the existing item's quantity
                existingItem.setQuantity(String.valueOf(oldQty + incomingQty));
                
                itemExists = true;
                break; // Stop looking, we found it
            }
        }

        // 4. If it's a brand new item, add it to the list
        if (!itemExists) {
            this.items.add(newItem);
        }
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            this.items.remove(index);
        }
    }
    
    public void clearCart() {
        this.items.clear();
    }

    // Helper to calculate total price
    public Double getTotalPrice() {
        return items.stream()
                .mapToDouble(CategoryItem::getTotal)
                .sum();
    }
}