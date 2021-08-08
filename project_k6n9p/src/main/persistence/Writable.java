package persistence;

import org.json.JSONObject;

// Represents a feature to write data to a JSON object
// SOURCE: UBC CPSC 210's JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}