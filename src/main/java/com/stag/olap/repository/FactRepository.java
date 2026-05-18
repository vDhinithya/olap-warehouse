package com.stag.olap.repository;

import com.stag.olap.entity.Fact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
