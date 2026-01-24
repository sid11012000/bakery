/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bakery.entity;

/**
 *
 * @author sudhanshumahajan
 */
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ShopOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate; // Stores Date & Time
    
    private String customerName;
    private String phoneNumber;
    private String address;
    private Double totalAmount;
    
    // We store the list of items as a simple text summary for now 
    // (e.g., "Cake x2, Biscuits x1"). This is much easier for beginners than creating a separate table.
    @Column(columnDefinition = "TEXT")
    private String orderDescription; 

    // Constructor
    public ShopOrder() {
        this.orderDate = LocalDateTime.now(); // Auto-set date when created
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getOrderDescription() { return orderDescription; }
    public void setOrderDescription(String orderDescription) { this.orderDescription = orderDescription; }
}