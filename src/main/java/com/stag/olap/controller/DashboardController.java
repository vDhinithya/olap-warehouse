package com.stag.olap.controller;

import com.stag.olap.entity.Fact;
import com.stag.olap.repository.FactRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    private final FactRepository repository;

    public DashboardController(FactRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(@RequestParam(value = "city", required = false) String selectedCity, Model model) {
        model.addAttribute("cities", repository.getAllCities());
        model.addAttribute("selectedCity", selectedCity);

        List<Object[]> costData;
        List<Object[]> deliveryData;
        List<Object[]> ratingData;
        String chart3Title;

        if (selectedCity != null && !selectedCity.isEmpty()) {
            model.addAttribute("totalRestaurants", repository.getTotalRestaurantsByCity(selectedCity));

            Long totalVotes = repository.getTotalVotesByCity(selectedCity);
            model.addAttribute("totalVotes", totalVotes != null ? totalVotes : 0L);

            Double avgCost = repository.getAverageCostByCityName(selectedCity);
            model.addAttribute("avgCost", avgCost != null ? Math.round(avgCost) : 0.0);

            model.addAttribute("bestCity", selectedCity); // Highlight selected city

            // Charts data for specific city
            deliveryData = repository.getPopularityByDeliveryStatusForCity(selectedCity);
            ratingData = repository.getAverageRatingByTopCuisinesForCity(selectedCity);
            costData = repository.getTopExpensiveRestaurantsByCity(selectedCity); // Drill-down to restaurants

            chart3Title = "Top Expensive Restaurants in " + selectedCity + " (Drill-Down)";
            List<Fact> cityRestaurants = repository.getRestaurantDetailsByCity(selectedCity);
            model.addAttribute("cityRestaurants", cityRestaurants);

            chart3Title = "Top Expensive Restaurants in " + selectedCity + " (Drill-Down)";
        }
        else {
            model.addAttribute("totalRestaurants", repository.getTotalRestaurants());

            Long totalVotes = repository.getTotalVotes();
            model.addAttribute("totalVotes", totalVotes != null ? totalVotes : 0L);

            Double avgCost = repository.getAverageCost();
            model.addAttribute("avgCost", avgCost != null ? Math.round(avgCost) : 0.0);

            costData = repository.getAverageCostByCity();
            model.addAttribute("bestCity", costData.isEmpty() ? "N/A" : String.valueOf(costData.get(0)[0]));

            deliveryData = repository.getPopularityByDeliveryStatus();
            ratingData = repository.getAverageRatingByTopCuisines();

            chart3Title = "Cost Distribution Across Geography (Roll-up)";
        }

        // Mapping Data to Lists for Chart.js
        List<String> deliveryLabels = deliveryData.stream().map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Long> deliveryVotes = deliveryData.stream().map(obj -> obj[1] != null ? ((Number) obj[1]).longValue() : 0L).toList();

        List<String> cuisineLabels = ratingData.stream().map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Double> cuisineRatings = ratingData.stream().map(obj -> obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0).toList();

        List<String> cityLabels = costData.stream().map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Double> cityCosts = costData.stream().map(obj -> obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0).toList();

        // Attach to Model
        model.addAttribute("deliveryLabels", deliveryLabels);
        model.addAttribute("deliveryVotes", deliveryVotes);
        model.addAttribute("cuisineLabels", cuisineLabels);
        model.addAttribute("cuisineRatings", cuisineRatings);
        model.addAttribute("cityLabels", cityLabels);
        model.addAttribute("cityCosts", cityCosts);
        model.addAttribute("chart3Title", chart3Title); // Dynamic Title for 3rd Chart

        return "dashboard";
    }
}