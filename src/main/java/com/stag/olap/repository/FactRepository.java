package com.stag.olap.repository;

import com.stag.olap.entity.Fact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactRepository extends JpaRepository<Fact, Long> {

    @Query("SELECT z.city, AVG(z.averageCostForTwo) FROM Fact z GROUP BY z.city ORDER BY AVG(z.averageCostForTwo) DESC LIMIT 10")
    List<Object[]> getAverageCostByCity();

    @Query("SELECT z.hasOnlineDelivery, SUM(z.votes) FROM Fact z GROUP BY z.hasOnlineDelivery")
    List<Object[]> getPopularityByDeliveryStatus();

    @Query("SELECT z.cuisines, AVG(z.aggregateRating) FROM Fact z WHERE z.cuisines IS NOT NULL AND z.cuisines != '' GROUP BY z.cuisines ORDER BY COUNT(z.id) DESC LIMIT 10")
    List<Object[]> getAverageRatingByTopCuisines();

    @Query("SELECT COUNT(f) FROM Fact f")
    Long getTotalRestaurants();

    @Query("SELECT SUM(f.votes) FROM Fact f")
    Long getTotalVotes();

    @Query("SELECT AVG(f.averageCostForTwo) FROM Fact f")
    Double getAverageCost();

    @Query("SELECT DISTINCT f.city FROM Fact f WHERE f.city IS NOT NULL ORDER BY f.city")
    List<String> getAllCities();

    @Query("SELECT f.hasOnlineDelivery, SUM(f.votes) FROM Fact f WHERE f.city = :city GROUP BY f.hasOnlineDelivery")
    List<Object[]> getPopularityByDeliveryStatusForCity(@Param("city") String city);

    @Query("SELECT f.cuisines, AVG(f.aggregateRating) FROM Fact f WHERE f.cuisines IS NOT NULL AND f.cuisines != '' AND f.city = :city GROUP BY f.cuisines ORDER BY COUNT(f.id) DESC LIMIT 10")
    List<Object[]> getAverageRatingByTopCuisinesForCity(@Param("city") String city);

    @Query("SELECT COUNT(f) FROM Fact f WHERE f.city = :city")
    Long getTotalRestaurantsByCity(@Param("city") String city);

    @Query("SELECT SUM(f.votes) FROM Fact f WHERE f.city = :city")
    Long getTotalVotesByCity(@Param("city") String city);

    @Query("SELECT AVG(f.averageCostForTwo) FROM Fact f WHERE f.city = :city")
    Double getAverageCostByCityName(@Param("city") String city);

    @Query("SELECT f.restaurantName, AVG(f.averageCostForTwo) FROM Fact f WHERE f.city = :city GROUP BY f.restaurantName ORDER BY AVG(f.averageCostForTwo) DESC LIMIT 15")
    List<Object[]> getTopExpensiveRestaurantsByCity(@Param("city") String city);

    @Query("SELECT f FROM Fact f WHERE f.city = :city ORDER BY f.aggregateRating DESC LIMIT 15")
    List<Fact> getRestaurantDetailsByCity(@Param("city") String city);
}
