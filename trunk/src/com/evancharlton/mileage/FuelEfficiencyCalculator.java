package com.evancharlton.mileage;

import com.evancharlton.mileage.dao.Vehicle;
import com.evancharlton.mileage.dao.Fillup;
import com.evancharlton.mileage.dao.FillupSeries;

public class FuelEfficiencyCalculator {
    private static final int PRECISION_SCALE = 8; // For BigDecimal calculations

    public static double calculateEconomy(Vehicle vehicle, FillupSeries series) {
        if (series == null || vehicle == null) {
            return 0D;
        }

        Fillup previous = series.getPrevious();
        Fillup current = series.getCurrent();

        if (previous == null || current == null || current.isPartial()) {
            return 0D;
        }

        double distance = current.getOdometer() - previous.getOdometer();
        double volume = current.getVolume();

        if (distance <= 0 || volume <= 0) {
            return 0D;
        }

        // Calculate without rounding first
        double economy = distance / volume;

        // Convert to proper units if needed
        if (vehicle.isMetric()) {
            economy = convertToKilometersPerLiter(economy);
        } else {
            economy = convertToMilesPerGallon(economy);
        }

        return roundEconomy(economy);
    }

    private static double convertToMilesPerGallon(double kmPerLiter) {
        return kmPerLiter * 2.3521458; // 1 km/L = 2.3521458 mpg
    }

    private static double convertToKilometersPerLiter(double milesPerGallon) {
        return milesPerGallon / 2.3521458;
    }

    private static double roundEconomy(double value) {
        // Round to 2 decimal places for display
        return Math.round(value * 100D) / 100D;
    }
}