package model;

import exceptions.InputOutOfRangeException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseListTest {

    CourseList userCourse;
    Course courseOne;
    Course courseTwo;
    Course courseThree;

    @BeforeEach
    public void runBefore() {
        userCourse = new CourseList();
        courseOne = new Course("cpsc 210");
        courseTwo = new Course("cpsc 110");
        courseThree = new Course("cpsc 121");
    }

    @Test
    public void testCourseList() {
        assertEquals(0, userCourse.getCourses().size());

        userCourse.getCourses().add(courseOne);
        userCourse.getCourses().add(courseTwo);

        assertEquals(2, userCourse.getCourses().size());
        assertTrue(userCourse.getCourses().contains(courseOne));
        assertTrue(userCourse.getCourses().contains(courseTwo));
        assertFalse(userCourse.getCourses().contains(courseThree));
    }

    @Test
    public void testGetIdealGrade() {
        assertEquals(83, courseOne.getIdealGrade());

        try {
            courseOne.setIdealGrade(10);
        } catch (InputOutOfRangeException e) {
            System.out.println("Registering of value failed. Ideal grade must be between 0 and 100.");
        }

        assertEquals(10, courseOne.getIdealGrade());
    }

    @Test
    public void testGetPredictedGrade() {
        assertEquals(75, courseOne.getPredictedGrade());

        try {
            courseOne.setPredictedGrade(10);
        } catch (InputOutOfRangeException e) {
            System.out.println("Registering of value failed. Ideal grade must be between 0 and 100.");
        }

        assertEquals(10, courseOne.getPredictedGrade());
    }

    @Test
    public void testRemoveCourseByName() {
        userCourse.getCourses().add(courseOne);
        userCourse.getCourses().add(courseTwo);

        assertEquals(2, userCourse.getCourses().size());

        assertTrue(userCourse.removeCourseByName("cpsc 210"));
        assertEquals(1, userCourse.getCourses().size());

        assertFalse(userCourse.removeCourseByName("cpsc 310"));
    }


    @Test
    public void testContainsByName() {
        userCourse.getCourses().add(courseOne);
        userCourse.getCourses().add(courseTwo);

        assertEquals(2, userCourse.getCourses().size());

        assertEquals(courseOne, userCourse.containsByName("cpsc 210"));
        assertEquals(2, userCourse.getCourses().size());

        assertNull(userCourse.containsByName("cpsc 310"));
    }

    @Test
    public void testToJson() {
        JSONObject jsonObject = userCourse.toJson();

        // note: testing of the courses field is done thoroughly in the method
        // testCoursesToJson(), so it has been largely omitted here to reduce redundancy
        assertTrue(jsonObject.getJSONArray("courses").isEmpty());
    }

    @Test
    public void testCoursesToJson() {
        JSONArray jsonArrayEmpty = userCourse.coursesToJson();

        assertTrue(jsonArrayEmpty.isEmpty());

        userCourse.getCourses().add(courseOne);
        userCourse.getCourses().add(courseTwo);

        JSONArray jsonArrayNotEmpty = userCourse.coursesToJson();

        assertFalse(jsonArrayNotEmpty.isEmpty());
        assertEquals("cpsc 210", jsonArrayNotEmpty.getJSONObject(0).get("name"));
        assertEquals("cpsc 110", jsonArrayNotEmpty.getJSONObject(1).get("name"));

    }
}
