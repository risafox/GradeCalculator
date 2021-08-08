package persistence;


import exceptions.InputOutOfRangeException;
import model.Assignment;
import model.Course;
import model.CourseList;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            CourseList courseList = new CourseList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCourseList() {
        try {
            CourseList courseList = new CourseList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCourseList.json");
            writer.open();
            writer.write(courseList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCourseList.json");
            courseList = reader.read();

            // expect nothing in courseList
            assertEquals(0, courseList.getCourses().size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (InputOutOfRangeException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralCourseList() {
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

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCourseList.json");
            writer.open();
            writer.write(courseList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCourseList.json");
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
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (InputOutOfRangeException e) {
            fail("Exception should not have been thrown");
        }
    }
}

