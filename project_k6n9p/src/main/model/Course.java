package model;

import tools.NumberUtilities;

import exceptions.InputOutOfRangeException;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

import static tools.NumberUtilities.roundValueTwoDecimalPlaces;

// Represents a course having a name and an array of assignments
public class Course implements Writable {

    private String name;                           // name of assignments
    private List<Assignment> assignments;          // array of assignments for course

    private double totalWeightSoFar;               // total weight of the completed assignments so far
    private double currentCourseGrade;             // current course grade across all assignments
    private double idealGrade;                     // ideal grade for course, default=83
    private double predictedGrade;                 // predicted grade for remaining assignments, default=75

    // REQUIRES: grade and weight of assignments, ideal grade and predicted grade must be in range 0.0 to 100.0
    // total weight of all assignments must not exceed 100.0
    // EFFECTS: construct a course with a name, an empty array of assignments, an ideal grade set to 83, and
    // a predicted grade set to 75
    public Course(String name) {
        this.name = name;
        this.assignments = new ArrayList<>();
        this.idealGrade = 83;
        this.predictedGrade = 75;
    }

    // REQUIRES: grade and weight of assignments, ideal grade and predicted grade must be in range 0.0 to 100.0
    // total weight of all assignments must not exceed 100.0
    // EFFECTS: construct a course with a name, a list of assignments, an ideal grade, and a predicted grade
    public Course(String name, List<Assignment> assignments, double idealGrade, double predictedGrade) {
        this.name = name;
        this.assignments = assignments;
        this.idealGrade = idealGrade;
        this.predictedGrade = predictedGrade;
    }

    public String getName() {
        return name;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public double getIdealGrade() {
        return idealGrade;
    }

    public double getPredictedGrade() {
        return predictedGrade;
    }

    // MODIFIES: this
    // EFFECTS: set ideal grade to a new value
    // throws InputOutOfRangeException if the new ideal grade not between 0 and 100
    public void setIdealGrade(double idealGrade) throws InputOutOfRangeException {
        if (!(0 <= idealGrade && idealGrade <= 100)) {
            throw new InputOutOfRangeException("Ideal grade out of range");
        }
        this.idealGrade = idealGrade;
    }

    // MODIFIES: this
    // EFFECTS: set predicted grade to a new value
    // throws InputOutOfRangeException if the new predicted grade not between 0 and 100
    public void setPredictedGrade(double predictedGrade) throws InputOutOfRangeException {
        if (!(0 <= predictedGrade && predictedGrade <= 100)) {
            throw new InputOutOfRangeException("Predicted grade out of range");
        }
        this.predictedGrade = predictedGrade;
    }

    // EFFECTS: returns number of assignments in list of assignments
    public int numAssignments() {
        return assignments.size();
    }

    // EFFECTS: search for an assignment using its name
    //          return true if found, and false if not found
    public boolean containsAssignmentName(String assignmentName) {
        for (Assignment assignment : assignments) {
            if (assignment.getName().equals(assignmentName)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: if assignment does not exist (searched by name), adds assignment to the list of assignments
    //          returns true if an assignment was added, and false if not added
    public boolean addAssignment(Assignment newAssignment) {
        for (Assignment assignment : assignments) {
            if (assignment.getName().equals(newAssignment.getName())) {
                return false;
            }
        }
        assignments.add(newAssignment);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: if assignment exists (searched by name), remove assignment and return true
    // if assignment does not exist, return false
    public boolean removeAssignment(String assignmentName) {
        for (Assignment assignment : assignments) {
            if (assignment.getName().equals(assignmentName)) {
                assignments.remove(assignment);
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: calculates the (rounded) current course grade from all assignments in array
    public double calculateCurrentGrade() {
        double totalGradeSoFar = 0.00;
        totalWeightSoFar = 0.00;
        currentCourseGrade = 0.00;

        // if block prevents a division by zero
        if (this.assignments.size() == 0) {
            return currentCourseGrade;
        } else {
            for (Assignment assignment : this.assignments) {
                totalGradeSoFar += (assignment.getGrade() * assignment.getWeight()) / 100;
                totalWeightSoFar += assignment.getWeight() / 100;
            }
        }

        currentCourseGrade = totalGradeSoFar / totalWeightSoFar;

        return roundValueTwoDecimalPlaces(currentCourseGrade);
    }

    // EFFECTS: calculates total weight of all assignments in array
    public double calculateCurrentWeight() {
        double currentWeight = 0.00;

        for (Assignment assignment : this.assignments) {
            currentWeight += assignment.getWeight();
        }

        return roundValueTwoDecimalPlaces(currentWeight);
    }

    // EFFECTS: calculates the user's final grade if they receive x% on the remainder of their assignments
    public double calculateIdealFinalGrade() {
        calculateCurrentGrade();

        double gradeNeeded = (idealGrade - (currentCourseGrade * totalWeightSoFar)) / (1 - totalWeightSoFar);

        return roundValueTwoDecimalPlaces(gradeNeeded);
    }

    // EFFECTS: calculates the user's grades needed for the remaining assignments to receive x% as their final mark
    public double calculatePredictedFinalGrade() {
        calculateCurrentGrade();

        double predictedFinalGrade = currentCourseGrade * totalWeightSoFar
                                       + predictedGrade * (1 - totalWeightSoFar);

        return roundValueTwoDecimalPlaces(predictedFinalGrade);
    }

    // EFFECTS: returns a string representation of the course
    @Override
    public String toString() {
        return "[ Name: " + name + ", Assignments: " + assignments + " ]";
    }

    // EFFECTS: returns a course as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("assignments", assignmentsToJson());
        json.put("ideal grade", idealGrade);
        json.put("predicted grade", predictedGrade);

        return json;
    }

    // EFFECTS: returns assignments in this course as a JSON array
    public JSONArray assignmentsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Assignment assignment : assignments) {
            jsonArray.put(assignment.toJson());
        }

        return jsonArray;
    }
}
