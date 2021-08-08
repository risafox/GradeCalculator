package model;

import exceptions.InputOutOfRangeException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Course courseOne;
    private Course courseTwo;
    private Course courseThree;
    private Course courseFour;
    private Assignment assignmentOne;
    private Assignment assignmentTwo;
    private Assignment assignmentThree;

    @BeforeEach
    public void runBefore() {
        courseOne = new Course("math");
        courseTwo = new Course("biology");

        try {
            assignmentOne = new Assignment("quiz 1", 80.23, 10.00);
            assignmentTwo = new Assignment("homework check", 45.50, 2.50);
            assignmentThree = new Assignment("midterm", 91.00, 30.00);
        } catch (InputOutOfRangeException e) {
            fail("Did not expect InvalidNumericalValueException to be thrown.");
        }

        courseTwo.addAssignment(assignmentOne);
        courseTwo.addAssignment(assignmentTwo);
        courseTwo.addAssignment(assignmentThree);
    }

    @Test
    public void testCourseSingleParameters() {
        assertEquals("math", courseOne.getName());
        assertEquals(0, courseOne.getAssignments().size());
        assertEquals(83, courseOne.getIdealGrade());
        assertEquals(75, courseOne.getPredictedGrade());
    }

    @Test
    public void testCourseMultipleParameters() {
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(assignmentOne);
        assignmentList.add(assignmentTwo);

        courseThree = new Course("cpsc 121", assignmentList, 55, 80.2);

        assertEquals("cpsc 121", courseThree.getName());
        assertEquals(2, courseThree.getAssignments().size());
        assertEquals(55, courseThree.getIdealGrade());
        assertEquals(80.2, courseThree.getPredictedGrade());
    }

    @Test
    public void testSetIdealGrade() {
        try {
            courseOne.setIdealGrade(99.4);
        } catch (InputOutOfRangeException e) {
            fail("Did not expect InvalidNumericalValueException to be thrown.");
        }
        assertEquals(99.4, courseOne.getIdealGrade());

        try {
            courseOne.setIdealGrade(-100.1);
            fail("Expected InvalidNumericalValueException to be thrown.");
        } catch (InputOutOfRangeException e) {
            // expect this
        }
        assertEquals(99.4, courseOne.getIdealGrade());

        try {
            courseOne.setIdealGrade(100.1);
            fail("Expected InvalidNumericalValueException to be thrown.");
        } catch (InputOutOfRangeException e) {
            // expect this
        }
        assertEquals(99.4, courseOne.getIdealGrade());
    }

    @Test
    public void testSetPredictedGrade() {
        try {
            courseOne.setPredictedGrade(99.4);
        } catch (InputOutOfRangeException e) {
            fail("Did not expect InvalidNumericalValueException to be thrown.");
        }
        assertEquals(99.4, courseOne.getPredictedGrade());

        try {
            courseOne.setPredictedGrade(100000);
            fail("Expected InvalidNumericalValueException to be thrown.");
        } catch (InputOutOfRangeException e) {
            // expect this
        }
        assertEquals(99.4, courseOne.getPredictedGrade());

        try {
            courseOne.setPredictedGrade(-100000);
            fail("Expected InvalidNumericalValueException to be thrown.");
        } catch (InputOutOfRangeException e) {
            // expect this
        }
        assertEquals(99.4, courseOne.getPredictedGrade());

    }

    @Test
    public void testNumAssignments() {
        assertEquals(0, courseOne.numAssignments());
        assertEquals(3, courseTwo.numAssignments());
    }

    @Test
    public void testContainsAssignmentName() {
        assertFalse(courseOne.containsAssignmentName("quiz 1"));

        courseOne.addAssignment(assignmentOne);
        courseOne.addAssignment(assignmentTwo);

        assertTrue(courseOne.containsAssignmentName("quiz 1"));
        assertTrue(courseOne.containsAssignmentName("homework check"));
        assertFalse(courseOne.containsAssignmentName("quiz 2"));

    }

    @Test
    public void testAddAssignment() {
        assertEquals(0, courseOne.numAssignments());

        courseOne.addAssignment(assignmentOne);
        courseOne.addAssignment(assignmentTwo);

        // test adding assignments that do not exist yet
        assertEquals(2, courseOne.numAssignments());
        assertTrue(courseOne.getAssignments().contains(assignmentOne));
        assertTrue(courseOne.getAssignments().contains(assignmentTwo));
        assertTrue(courseOne.addAssignment(assignmentThree));
        assertTrue(courseOne.getAssignments().contains(assignmentThree));

        // test adding assignments that do exist
        assertEquals(3, courseOne.numAssignments());
        assertFalse(courseOne.addAssignment(assignmentThree));
        assertEquals(3, courseOne.numAssignments());

    }

    @Test
    public void testRemoveAssignment() {
        assertEquals(3, courseTwo.numAssignments());

        // test removing an assignment that exists
        boolean removeMidterm = courseTwo.removeAssignment("midterm");

        assertEquals(2, courseTwo.numAssignments());
        assertTrue(removeMidterm);
        assertFalse(courseTwo.getAssignments().contains(assignmentThree));
        assertTrue(courseTwo.getAssignments().contains(assignmentOne));
        assertTrue(courseTwo.getAssignments().contains(assignmentTwo));

        // test removing an assignment that does not exist

        boolean removeFinal = courseTwo.removeAssignment("final");

        assertEquals(2, courseTwo.numAssignments());
        assertFalse(removeFinal);

    }

    @Test
    public void testCalculateCurrentAverage() {
        // test average with no assignments
        assertEquals(0.00, courseOne.calculateCurrentGrade());

        // test average with multiple assignments
        courseOne.addAssignment(assignmentTwo);
        courseOne.addAssignment(assignmentThree);

        assertEquals(85.79, courseTwo.calculateCurrentGrade());
        assertEquals(87.50, courseOne.calculateCurrentGrade());
    }

    @Test
    public void testCalculateCurrentWeight() {
        // test weight with no assignments
        assertEquals(0.00, courseOne.calculateCurrentWeight());

        // test weight with multiple assignments
        courseOne.addAssignment(assignmentTwo);
        courseOne.addAssignment(assignmentThree);

        assertEquals(42.50, courseTwo.calculateCurrentWeight());
        assertEquals(32.5, courseOne.calculateCurrentWeight());
    }

    @Test
    public void testCalculateIdealFinalGrade(){
        // test course with no assignments
        try {
            courseOne.setIdealGrade(10);
        } catch (InputOutOfRangeException e) {
            e.printStackTrace();
        }
        assertEquals(10, courseOne.calculateIdealFinalGrade());

        // test course with assignments added
        try {
            courseTwo.setIdealGrade(80);
        } catch (InputOutOfRangeException e) {
            e.printStackTrace();
        }
        assertEquals(75.72, courseTwo.calculateIdealFinalGrade());
    }

    @Test
    public void testCalculatePredictedFinalGrade(){
        // test course with no assignments added
        try {
            courseOne.setPredictedGrade(10);
        } catch (InputOutOfRangeException e) {
            e.printStackTrace();
        }
        assertEquals(10, courseOne.calculatePredictedFinalGrade());

        // test course with assignments added
        try {
            courseTwo.setPredictedGrade(75.3);
        } catch (InputOutOfRangeException e) {
            e.printStackTrace();
        }
        assertEquals(79.76, courseTwo.calculatePredictedFinalGrade());
    }

    @Test
    public void testToString() {
        // test method with no assignments added
        String expectedStringOne = "[ Name: math, Assignments: [] ]";
        assertEquals(expectedStringOne, courseOne.toString());

        courseOne.addAssignment(assignmentTwo);
        courseOne.addAssignment(assignmentThree);

        // test method with assignments added
        String expectedStringTwo = "[ Name: math, Assignments: [[ Name: homework check, Grade: 45.5, Weight: 2.5 ], "
                                                             + "[ Name: midterm, Grade: 91.0, Weight: 30.0 ]] ]";
        assertEquals(expectedStringTwo, courseOne.toString());
    }

    @Test
    public void testToJson() {
        JSONObject json = courseOne.toJson();

        // note: testing of the assignments field is done thoroughly in the method
        // testAssignmentsToJson(), so it has been omitted here to reduce redundancy
        assertEquals(json.get("name"), courseOne.getName());
        assertEquals(json.get("ideal grade"), courseOne.getIdealGrade());
        assertEquals(json.get("predicted grade"), courseOne.getPredictedGrade());
    }

    @Test
    public void testAssignmentsToJson() {
        JSONArray jsonArrayCourseOne = courseOne.assignmentsToJson();

        assertTrue(jsonArrayCourseOne.isEmpty());

        JSONArray jsonArrayCourseTwo = courseTwo.assignmentsToJson();

        assertEquals("quiz 1", jsonArrayCourseTwo.getJSONObject(0).get("name"));
        assertEquals("homework check", jsonArrayCourseTwo.getJSONObject(1).get("name"));
        assertEquals("midterm", jsonArrayCourseTwo.getJSONObject(2).get("name"));
    }
}
