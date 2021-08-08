package persistence;

import exceptions.InputOutOfRangeException;
import model.Assignment;
import model.Course;
import model.CourseList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            CourseList courseList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        } catch (InputOutOfRangeException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testReaderEmptyCourseList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCourseList.json");
        try {
            CourseList courseList = reader.read();

            // expect nothing in courseList
            assertEquals(0, courseList.getCourses().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (InputOutOfRangeException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testReaderGeneralCourseListValidValues() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCourseList.json");
        try {
            // add courses to courseList and assignments to courses
            CourseList courseList = new CourseList();
            Course courseOne = new Course("math");
            Course courseTwo = new Course("science");
            Assignment assignmentOne = new Assignment("quiz 1", 80, 20);
            Assignment assignmentTwo = new Assignment("quiz 2", 70, 12);

            courseOne.addAssignment(assignmentOne);
            courseOne.addAssignment(assignmentTwo);
            courseTwo.addAssignment(assignmentOne);

            courseList.getCourses().add(courseOne);
            courseList.getCourses().add(courseTwo);

            // testing reader
            courseList = reader.read();

            List<Course> courses = courseList.getCourses();

            // make sure two courses are in courseList
            assertEquals(2, courses.size());

            // iterate through courseList to access all fields except list of assignments
            for (int i = 0; i < courses.size(); i++) {
                if (i == 0) {
                    Course firstCourse = courses.get(i);

                    checkCourse(courseOne, "math", 83, 75);

                    List<Assignment> courseOneAssignments = courseOne.getAssignments();

                    // inner for loop to test each assignment
                    for (int j = 0; j < courseOneAssignments.size(); j++) {
                        if (j == 0) {
                            Assignment firstAssignment = courseOneAssignments.get(j);

                            checkAssignment(firstAssignment, "quiz 1", 80, 20);
                        } else if (j == 1) {
                            Assignment secondAssignment = courseOneAssignments.get(j);

                            checkAssignment(secondAssignment, "quiz 2", 70, 12);
                        }
                    }

                } else if (i == 1) {
                    Course secondCourse = courses.get(i);

                    checkCourse(secondCourse, "science", 83, 75);

                    // no for loop needed here because there's only one assignment in list
                    List<Assignment> courseTwoAssignments = courseOne.getAssignments();

                    checkAssignment(courseTwoAssignments.get(0), "quiz 1", 80, 20);
                }
            }

        } catch (InputOutOfRangeException e){
            fail("InputOutOfRangeException should not have been thrown.");
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderGeneralCourseListInvalidValues() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCourseListInvalidValues.json");
        try {
            // add courses to courseList and assignments to courses
            CourseList courseList = new CourseList();
            Course courseOne = new Course("math");
            Course courseTwo = new Course("science");
            Assignment assignmentOne = new Assignment("quiz 1", 80, 20);
            Assignment assignmentTwo = new Assignment("quiz 2", 70, 12);

            courseOne.addAssignment(assignmentOne);
            courseOne.addAssignment(assignmentTwo);
            courseTwo.addAssignment(assignmentOne);

            courseList.getCourses().add(courseOne);
            courseList.getCourses().add(courseTwo);

            // testing reader
            courseList = reader.read();

            List<Course> courses = courseList.getCourses();

            // iterate through courseList to access all fields except list of assignments
            for (int i = 0; i < courses.size(); i++) {
                if (i == 0) {
                    Course firstCourse = courses.get(i);

                    checkCourse(courseOne, "math", 83, 75);

                    List<Assignment> courseOneAssignments = courseOne.getAssignments();

                    // inner for loop to test each assignment
                    for (int j = 0; j < courseOneAssignments.size(); j++) {
                        if (j == 0) {
                            Assignment firstAssignment = courseOneAssignments.get(j);

                            checkAssignment(firstAssignment, "quiz 1", 80, 20);
                        } else if (j == 1) {
                            Assignment secondAssignment = courseOneAssignments.get(j);

                            checkAssignment(secondAssignment, "quiz 2", 70, 12);
                        }
                    }

                } else if (i == 1) {
                    Course secondCourse = courses.get(i);

                    checkCourse(secondCourse, "science", 83, 75);

                    // no for loop needed here because there's only one assignment in list
                    List<Assignment> courseTwoAssignments = courseOne.getAssignments();

                    checkAssignment(courseTwoAssignments.get(0), "quiz 1", 80, 20);
                }
            }


        } catch (InputOutOfRangeException e){
            // expect this to be thrown
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}
