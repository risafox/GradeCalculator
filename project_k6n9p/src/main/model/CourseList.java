package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents an array of courses
public class CourseList implements Writable {

    private List<Course> courses;              // array of all courses

    // EFFECTS: constructs a course list with an array of courses
    public CourseList() {
        courses = new ArrayList<>();
    }

    public List<Course> getCourses() {
        return courses;
    }

    // MODIFIES: this
    // EFFECTS: if course exists (searched by name), remove course and return true
    //          if course does not exist, return false
    public boolean removeCourseByName(String courseName) {
        for (Course course: courses) {
            if (courseName.equals(course.getName())) {
                courses.remove(course);
                return true;
            }
        }
        return false;
    }


    // MODIFIES: this
    // EFFECTS: if course exists (searched by name), return the course
    //          if course does not exist, return null
    public Course containsByName(String courseName) {
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                return course;
            }
        }
        return null;
    }

    // EFFECTS: returns a course list as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("courses", coursesToJson());

        return json;
    }

    // EFFECTS: returns the courses in the course list as a JSON array
    public JSONArray coursesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Course course : courses) {
            jsonArray.put(course.toJson());
        }

        return jsonArray;
    }
}
