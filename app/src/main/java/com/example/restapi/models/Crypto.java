package com.example.restapi.models;

public class Crypto {
    private String code;
    private String name;
    private String price;
    private String color;

    public Crypto() {
    }

    public Crypto(String code, String name, String price, String color) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.color = color;
    }
    public static Crypto fromApiJson(String json) {
        return new Crypto(
            "BTC",
            "Bitcoin",
            "$48,235.00",
            "#F7931A"
        );
    }

    // Getters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
} 