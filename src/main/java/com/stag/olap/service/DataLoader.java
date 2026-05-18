package com.stag.olap.service;

import com.opencsv.CSVReader;
import com.stag.olap.entity.Fact;
import com.stag.olap.repository.FactRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;

import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final FactRepository repository;

    public DataLoader(FactRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args){
        if (repository.count() > 0) {
            System.out.println("Data already loaded. Skipping ETL process.");
            return;
        }

        System.out.println("Starting ETL process: Loading Zomato data into Data Warehouse...");

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("zomato.csv").getInputStream()))) {
            List<String[]> lines = reader.readAll();
            List<Fact> facts = new ArrayList<>();

            // Skip the header row
            for (int i = 1; i < lines.size(); i++) {
                String[] row = lines.get(i);

                try {
                    // Extracting based on standard Kaggle zomato.csv format
                    String restaurantName = row[1];
                    String city = row[3];
                    String cuisines = row[9];
                    Double cost = Double.parseDouble(row[10].isEmpty() ? "0" : row[10]);
                    String hasOnlineDelivery = row[13];
                    Double rating = Double.parseDouble(row[17].isEmpty() ? "0" : row[17]);
                    Integer votes = Integer.parseInt(row[20].isEmpty() ? "0" : row[20]);

                    facts.add(new Fact(restaurantName, city, cuisines, cost, rating, votes, hasOnlineDelivery));

                } catch (Exception e) {
                    // Skip malformed rows gracefully
                }
            }

            repository.saveAll(facts);
            System.out.println("ETL Complete! Successfully loaded " + facts.size() + " records into H2 Database.");

        } catch (Exception e) {
            System.err.println("Failed to load CSV: " + e.getMessage());
        }
    }
}
