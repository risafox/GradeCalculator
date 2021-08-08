package model;

import exceptions.InputOutOfRangeException;
import org.json.JSONObject;
import persistence.Writable;

// Represents an assignment having a name, grade, and weight
public class Assignment implements Writable {

    private String name;                // name of assignment
    private double grade;               // grade received for assignment
    private double weight;              // weight of assignment

    // REQUIRES: grade and weight must be a double between 0 and 100
    // EFFECTS: constructs an assignment object with a name, grade, and weight
    // throws InputOutOfRangeException if either the grade or weight is not between 0 and 100
    public Assignment(String name, double grade, double weight) throws InputOutOfRangeException {
        this.name = name;

        if (!(0 <= grade && grade <= 100)) {
            throw new InputOutOfRangeException("Grade must be between 0 and 100");
        } else if (!(0 <= weight && weight <= 100)) {
            throw new InputOutOfRangeException("Weight must be between 0 and 100");
        }

        this.grade = grade;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    public double getWeight() {
        return weight;
    }

    // EFFECTS: returns a string representation of the assignment
    @Override
    public String toString() {
        return "[ Name: " + name + ", Grade: " + grade + ", Weight: " + weight + " ]";
    }

    // EFFECTS: returns an assignment as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("grade", grade);
        json.put("weight", weight);
        return json;
    }
}
