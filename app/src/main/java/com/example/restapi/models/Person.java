package com.example.restapi.models;

public class Person {
    private String image;
    private String title;
    private String name;
    private String gender;
    private String location;
    private String email;
    private String phone;

    public Person() {
    }

    public Person(String image, String title, String name, String gender, String location, String email, String phone) {
        this.image = image;
        this.title = title;
        this.name = name;
        this.gender = gender;
        this.location = location;
        this.email = email;
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
} 