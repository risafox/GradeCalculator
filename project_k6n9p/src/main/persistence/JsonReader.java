package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import exceptions.InputOutOfRangeException;
import model.Assignment;
import model.Course;
import model.CourseList;
import org.json.*;

// Represents a reader that reads a course list from JSON data stored in file
// SOURCE: UBC CPSC 210's JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file and
    // throws InputOutOfRangeException if assignment grade or weight is not between 0 and 100
    public CourseList read() throws IOException, InputOutOfRangeException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCourseList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // MODIFIES: this
    // EFFECTS: create a CourseList object with its courses from a JSON object
    // throws InputOutOfRangeException if assignment grade or weight is not between 0 and 100
    private CourseList parseCourseList(JSONObject jsonObject) throws InputOutOfRangeException {
        JSONArray courseList = jsonObject.getJSONArray("courses");
        CourseList convertedCourseList = new CourseList();

        // loop to get all courses for the course list
        for (Object json : courseList) {
            JSONObject jsonObjectTwo = (JSONObject) json;

            String courseName = jsonObjectTwo.getString("name");
            double idealGrade = jsonObjectTwo.getDouble("ideal grade");
            double predictedGrade = jsonObjectTwo.getDouble("predicted grade");

            JSONArray assignments = jsonObjectTwo.getJSONArray("assignments");
            List<Assignment> assignmentList = parseAssignment(assignments);

            Course course = new Course(courseName, assignmentList, idealGrade, predictedGrade);
            convertedCourseList.getCourses().add(course);
        }

        return convertedCourseList;
    }

    // MODIFIES: this
    // EFFECTS: create a list of Assignment objects with its courses from a JSON array
    // throws InputOutOfRangeException if assignment grade or weight is not between 0 and 100
    private List<Assignment> parseAssignment(JSONArray jsonArray) throws InputOutOfRangeException {
        List<Assignment> assignmentList = new ArrayList<>();

        // loop to get all assignments for each course
        for (Object js: jsonArray) {
            JSONObject jsonObjectThree = (JSONObject) js;

            String assignmentName = jsonObjectThree.getString("name");
            double grade = jsonObjectThree.getDouble("grade");
            double weight = jsonObjectThree.getDouble("weight");

            Assignment assignment = null;

            assignment = new Assignment(assignmentName, grade, weight);

            assignmentList.add(assignment);
        }
        return assignmentList;
    }


}
