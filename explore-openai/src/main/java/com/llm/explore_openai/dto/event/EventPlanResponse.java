package com.llm.explore_openai.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventPlanResponse {

    private String eventType;
    private String location;
    private String date;
    private Integer numberOfPeople;
    private String foodArrangement;
    private List<String> activities;   
    private String budgetEstimate;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getFoodArrangement() {
        return foodArrangement;
    }

    public void setFoodArrangement(String foodArrangement) {
        this.foodArrangement = foodArrangement;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getBudgetEstimate() {
        return budgetEstimate;
    }

    public void setBudgetEstimate(String budgetEstimate) {
        this.budgetEstimate = budgetEstimate;
    }
}
