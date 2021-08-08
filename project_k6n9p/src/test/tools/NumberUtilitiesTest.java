package tools;

import exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static tools.NumberUtilities.*;

public class NumberUtilitiesTest {
    double percentage;

    @Test
    public void testParseStringInput() {
        // case 1: parse a fraction
        try {
            percentage = parseStringInput("57.5/60");
        } catch (InvalidInputException e) {
            fail("InvalidInputException thrown when it should not have.");
        }
        assertEquals(95.8333, percentage);

        // case 2: parse a double
        try {
            percentage = parseStringInput("57.5");
        } catch (InvalidInputException e) {
            fail("InvalidInputException thrown when it should not have.");
        }
        assertEquals(57.5, percentage);

        // case 3: parse an invalid input
        try {
            percentage = parseStringInput("57.5/0");
            fail("InvalidInputException thrown when it should not have.");
        } catch (InvalidInputException e) {
            // expect this
        }

        try {
            percentage = parseStringInput("cadklsjl");
            fail("InvalidInputException thrown when it should not have.");
        } catch (InvalidInputException e) {
            // expect this
        }

    }

    @Test
    public void testValidateStringInput() {
        assertTrue(validateStringInput("1"));
        assertTrue(validateStringInput("3/100"));
        assertTrue(validateStringInput("3/90.0"));
        assertTrue(validateStringInput("20.3"));
        assertTrue(validateStringInput("13/1"));
        assertTrue(validateStringInput("89.0"));
        assertTrue(validateStringInput("80"));
        assertTrue(validateStringInput("13.2/80.5"));

        assertFalse(validateStringInput("10/0"));
        assertFalse(validateStringInput("10/00"));
        assertFalse(validateStringInput("car"));
        assertFalse(validateStringInput("../.)0"));
    }

    @Test
    public void testConvertStringFractionToDouble() {
        assertEquals(50, convertFractionStringToPercentage("1/2"));
        assertEquals(57, convertFractionStringToPercentage("57/100"));
        assertEquals(26.25, convertFractionStringToPercentage("10.5/40"));
        assertEquals(61.1111, convertFractionStringToPercentage("11/18"));
        assertEquals(90, convertFractionStringToPercentage("18/20"));
        assertEquals(75, convertFractionStringToPercentage("3/4"));
    }

    @Test
    public void testRoundValueFourDecimalPlaces() {
        assertEquals(36.6667, roundValueFourDecimalPlaces(36.6666666666666));
        assertEquals(193.3000, roundValueFourDecimalPlaces(193.30001));
    }

    @Test
    public void testRoundValueTwoDecimalPlaces() {
        assertEquals(36.67, roundValueTwoDecimalPlaces(36.6666666666666));
        assertEquals(193.30, roundValueTwoDecimalPlaces(193.30001));
    }
}
