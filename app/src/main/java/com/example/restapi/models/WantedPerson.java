package com.example.restapi.models;

public class WantedPerson {
    private String imageUrl;
    private String title;
    private String description;
    private String rewardText;
    private String details;
    private String nationality;
    private String status;

    public WantedPerson() {
    }

    public WantedPerson(String imageUrl, String title, String description, String rewardText, 
                       String details, String nationality, String status) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.rewardText = rewardText;
        this.details = details;
        this.nationality = nationality;
        this.status = status;
    }

    public static WantedPerson fromFbiJson(String json) {
        return new WantedPerson(
            "https://www.fbi.gov/wanted/counterintelligence/yanlai-zhong/@@images/image/large",
            "YANLAI ZHONG",
            "Conspiracy - Export Control Violation",
            "Up to $15 million reward",
            "Wanted for conspiracy to unlawfully export and smuggle U.S.-origin electronic components to Iran",
            "Chinese",
            "WANTED"
        );
    }

    // Getters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRewardText() {
        return rewardText;
    }

    public void setRewardText(String rewardText) {
        this.rewardText = rewardText;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 