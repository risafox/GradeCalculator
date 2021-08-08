package model;

import exceptions.InputOutOfRangeException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest {

    private Assignment assignmentOne;
    private Assignment assignmentTwo;
    private Assignment assignmentThree;

    @BeforeEach
    public void runBefore() {
        try {
            assignmentOne = new Assignment("quiz 1", 95.3, 35.0);
            assignmentTwo = new Assignment("midterm", 66.666666, 20);
        } catch (InputOutOfRangeException e) {
            fail("Did not expect InvalidNumericalValueException to be thrown.");
        }
    }

    @Test
    public void testAssignment() {
        assertEquals("quiz 1", assignmentOne.getName());
        assertEquals(95.3, assignmentOne.getGrade());
        assertEquals(35.0, assignmentOne.getWeight());

        // case where grade is out of range
        try {
            assignmentThree = new Assignment("quiz 1", 190, 35.0);
            fail("Expected InvalidNumericalValueException to be thrown.");
        } catch (InputOutOfRangeException e) {
            // expecting to throw exception
        }

        // case where weight is out of range
        try {
            assignmentThree = new Assignment("quiz 1", 7, -10);
            fail("Expected InvalidNumericalValueException to be thrown.");
        } catch (InputOutOfRangeException e) {
            // expecting to throw exception
        }

        // case where both grade and weight out of range
        try {
            assignmentThree = new Assignment("quiz 1", -900, 9000);
            fail("Expected InvalidNumericalValueException to be thrown.");
        } catch (InputOutOfRangeException e) {
            // expecting to throw exception
        }

        // case where grade and weight are both valid on the range boundaries
        try {
            assignmentThree = new Assignment("quiz 1", 100.0, 0.0);
        } catch (InputOutOfRangeException e) {
            fail("Did not expect InvalidNumericalValueException to be thrown.");
        }

    }

    @Test
    public void testToString() {
        String expectedStringOne = "[ Name: quiz 1, Grade: 95.3, Weight: 35.0 ]";
        assertEquals(expectedStringOne, assignmentOne.toString());

        String expectedStringTwo = "[ Name: midterm, Grade: 66.666666, Weight: 20.0 ]";
        assertEquals(expectedStringTwo, assignmentTwo.toString());
    }

    @Test
    public void testToJson() {
        JSONObject json = assignmentOne.toJson();

        assertEquals(json.get("name"), assignmentOne.getName());
        assertEquals(json.get("grade"), assignmentOne.getGrade());
        assertEquals(json.get("weight"), assignmentOne.getWeight());
    }
}
