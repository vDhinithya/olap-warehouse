package com.stag.olap.controller;

import com.stag.olap.repository.FactRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final FactRepository repository;

    public DashboardController(FactRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(Model model) {
        // --- KPIs ---
        model.addAttribute("totalRestaurants", repository.getTotalRestaurants());

        Long totalVotes = repository.getTotalVotes();
        model.addAttribute("totalVotes", totalVotes != null ? totalVotes : 0L);

        Double avgCost = repository.getAverageCost();
        model.addAttribute("avgCost", avgCost != null ? Math.round(avgCost) : 0.0);

        // Get Top City by count of restaurants for the KPI
        List<Object[]> costData = repository.getAverageCostByCity();
        String bestCity = costData.isEmpty() ? "N/A" : String.valueOf(costData.get(0)[0]);
        model.addAttribute("bestCity", bestCity);

        // --- Chart 1: Average Cost by City ---
        List<String> cityLabels = costData.stream()
                .map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Double> cityCosts = costData.stream()
                .map(obj -> obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0).toList();

        // --- Chart 2: Popularity by Delivery Status ---
        List<Object[]> deliveryData = repository.getPopularityByDeliveryStatus();
        List<String> deliveryLabels = deliveryData.stream()
                .map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Long> deliveryVotes = deliveryData.stream()
                .map(obj -> obj[1] != null ? ((Number) obj[1]).longValue() : 0L).toList();

        // --- Chart 3: Average Rating by Cuisines ---
        List<Object[]> ratingData = repository.getAverageRatingByTopCuisines();
        List<String> cuisineLabels = ratingData.stream()
                .map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Double> cuisineRatings = ratingData.stream()
                .map(obj -> obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0).toList();

        // Attach Chart data to the Model
        model.addAttribute("cityLabels", cityLabels);
        model.addAttribute("cityCosts", cityCosts);
        model.addAttribute("deliveryLabels", deliveryLabels);
        model.addAttribute("deliveryVotes", deliveryVotes);
        model.addAttribute("cuisineLabels", cuisineLabels);
        model.addAttribute("cuisineRatings", cuisineRatings);

        return "dashboard";
    }
}