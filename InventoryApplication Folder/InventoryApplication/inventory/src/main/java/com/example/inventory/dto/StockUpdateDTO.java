package com.example.inventory.dto;

public class StockUpdateDTO {
    private int amount; // Positive to increase, negative to decrease

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}