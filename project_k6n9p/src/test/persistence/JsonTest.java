package persistence;

import model.Assignment;
import model.Course;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkCourse(Course course, String name, double idealGrade, double predictedGrade) {
        assertEquals(name, course.getName());
        assertEquals(idealGrade, course.getIdealGrade());
        assertEquals(predictedGrade, course.getPredictedGrade());
    }

    protected void checkAssignment(Assignment assignment, String name, double grade, double weight) {
        assertEquals(name, assignment.getName());
        assertEquals(grade, assignment.getGrade());
        assertEquals(weight, assignment.getWeight());
    }
}
