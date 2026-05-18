package com.stag.olap.controller;

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
        // 1. Dropdown ke liye cities Model me bhejo
        model.addAttribute("cities", repository.getAllCities());
        model.addAttribute("selectedCity", selectedCity);

        // --- KPIs (Tumhara purana KPI code waise hi rahega) ---
        model.addAttribute("totalRestaurants", repository.getTotalRestaurants());
        model.addAttribute("totalVotes", repository.getTotalVotes() != null ? repository.getTotalVotes() : 0L);
        model.addAttribute("avgCost", repository.getAverageCost() != null ? Math.round(repository.getAverageCost()) : 0.0);
        List<Object[]> costData = repository.getAverageCostByCity();
        model.addAttribute("bestCity", costData.isEmpty() ? "N/A" : String.valueOf(costData.get(0)[0]));

        // --- Chart 1: Cost by City (Overall hamesha dikhega) ---
        List<String> cityLabels = costData.stream().map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Double> cityCosts = costData.stream().map(obj -> obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0).toList();

        // --- Drill-Down Logic ---
        List<Object[]> deliveryData;
        List<Object[]> ratingData;

        // Agar city selected hai, to filtered data laao, warna overall data laao
        if (selectedCity != null && !selectedCity.isEmpty()) {
            deliveryData = repository.getPopularityByDeliveryStatusForCity(selectedCity);
            ratingData = repository.getAverageRatingByTopCuisinesForCity(selectedCity);
        } else {
            deliveryData = repository.getPopularityByDeliveryStatus();
            ratingData = repository.getAverageRatingByTopCuisines();
        }

        // --- Chart 2 & 3 Mapping ---
        List<String> deliveryLabels = deliveryData.stream().map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Long> deliveryVotes = deliveryData.stream().map(obj -> obj[1] != null ? ((Number) obj[1]).longValue() : 0L).toList();

        List<String> cuisineLabels = ratingData.stream().map(obj -> obj[0] != null ? String.valueOf(obj[0]) : "Unknown").toList();
        List<Double> cuisineRatings = ratingData.stream().map(obj -> obj[1] != null ? ((Number) obj[1]).doubleValue() : 0.0).toList();

        // Attach to Model
        model.addAttribute("cityLabels", cityLabels);
        model.addAttribute("cityCosts", cityCosts);
        model.addAttribute("deliveryLabels", deliveryLabels);
        model.addAttribute("deliveryVotes", deliveryVotes);
        model.addAttribute("cuisineLabels", cuisineLabels);
        model.addAttribute("cuisineRatings", cuisineRatings);

        return "dashboard";
    }
}