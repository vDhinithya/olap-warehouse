package com.stag.olap.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fact")
public class Fact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantName;
    private String city;
    private String cuisines;
    private Double averageCostForTwo;
    private Double aggregateRating;
    private Integer votes;
    private String hasOnlineDelivery;

    public Fact() {}

    // RIGHT: Ensure 'this.' is used for every field!
    public Fact(String restaurantName, String city, String cuisines, Double averageCostForTwo, Double aggregateRating, Integer votes, String hasOnlineDelivery) {
        this.restaurantName = restaurantName;
        this.city = city;
        this.cuisines = cuisines;
        this.averageCostForTwo = averageCostForTwo;
        this.aggregateRating = aggregateRating;
        this.votes = votes;
        this.hasOnlineDelivery = hasOnlineDelivery;
    }

    // Ensure all of these Getters exist so the Controller can read the data!
    public String getRestaurantName() { return restaurantName; }
    public String getCity() { return city; }
    public String getCuisines() { return cuisines; }
    public Double getAverageCostForTwo() { return averageCostForTwo; }
    public Double getAggregateRating() { return aggregateRating; }
    public Integer getVotes() { return votes; }
    public String getHasOnlineDelivery() { return hasOnlineDelivery; }
}