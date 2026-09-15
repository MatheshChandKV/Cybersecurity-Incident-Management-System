package com.cims;

public class RiskCalculator {

    public static int calculateRisk(int impact, int likelihood, int exposure) {
        validateFactor(impact, "Impact");
        validateFactor(likelihood, "Likelihood");
        validateFactor(exposure, "Exposure");
        return impact * likelihood * exposure;
    }

    public static String determineSeverity(int riskScore) {
        if (riskScore >= 1 && riskScore <= 20)   return "LOW";
        if (riskScore <= 50)                     return "MEDIUM";
        if (riskScore <= 75)                     return "HIGH";
        return "CRITICAL"; 
    }

    private static void validateFactor(int val, String name) {
        if (val < 1 || val > 5) {
            throw new IllegalArgumentException(name + " must be between 1 and 5.");
        }
    }
}
