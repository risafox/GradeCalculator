package tools;

import exceptions.InvalidInputException;

import java.util.regex.*;

// Class contains utility methods used in other classes when manipulating numbers
public class NumberUtilities {

    // EFFECTS: handles string input, checks whether the input is valid and throws
    // InvalidInputException if the input is not a valid fraction or decimal value
    public static double parseStringInput(String input) throws InvalidInputException {
        double percentage;

        if (validateStringInput(input)) {
            if (input.contains("/")) {
                percentage = convertFractionStringToPercentage(input);
            } else {
                percentage = Double.parseDouble(input);
            }
        } else {
            throw new InvalidInputException();
        }

        return percentage;
    }

    // EFFECTS: returns true if the input is a valid fraction
    public static boolean validateStringInput(String input) {
        Pattern pattern = Pattern.compile("\\d*\\.?\\d+/([1-9]|([1-9]+\\d*\\.?\\d+|))|\\d*\\.?\\d+");
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }


    // EFFECTS: converts a fraction in string form to a percentage in double form
    public static double convertFractionStringToPercentage(String input) {
        String[] splitString = input.split("/");
        String numeratorString = splitString[0];
        String denominatorString = splitString[1];

        double numerator = Double.parseDouble(numeratorString);
        double denominator = Double.parseDouble(denominatorString);

        double convertedFraction = numerator / denominator * 100;

        return roundValueFourDecimalPlaces(convertedFraction);
    }


    // EFFECTS: rounds double value to two decimal places
    // SOURCE: documentation for round method has been found on the java docs and has then been modified
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html
    public static double roundValueTwoDecimalPlaces(double valueToRound) {
        return Math.round(valueToRound * 100.0) / 100.0;
    }


    // EFFECTS: rounds double value to four decimal places
    public static double roundValueFourDecimalPlaces(double valueToRound) {
        return Math.round(valueToRound * 10000.0) / 10000.0;
    }


}
