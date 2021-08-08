package ui;

import model.Assignment;
import model.Course;

import javax.swing.*;
import java.awt.*;


// Renderer for each cell of JList, with overwritten methods to allow for multiple lines per item
// SOURCE: heavily based on the code from Paul Samsotha
// https://stackoverflow.com/questions/21501770/multiple-lines-in-jlist-java
public class MyListCellRenderer extends DefaultListCellRenderer {

    @Override
    // MODIFIES: this
    // EFFECTS: renders each item in a JList for a course, to display the course name, assignments,
    // ideal grade, and predicted grade
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Course course = (Course) value;
        String name = course.getName().toUpperCase();
        double idealGrade = course.getIdealGrade();
        double predictedGrade = course.getPredictedGrade();

        double currentCourseGrade = course.calculateCurrentGrade();
        double currentCourseWeight = course.calculateCurrentWeight();
        double courseIdealGrade = course.calculateIdealFinalGrade();
        double coursePredictedGrade = course.calculatePredictedFinalGrade();

        String labelText = "<html>&nbsp;COURSE STATISTICS FOR " + name
                         + "<br/>&ensp;&emsp;Submitted assignments: " + eachAssignmentNewLine(course)
                         + "<br/>&ensp;&emsp;Current course grade: " + currentCourseGrade + "%"
                         + "<br/>&ensp;&emsp;Current course weight: " + currentCourseWeight + "%"
                         + "<br/>&ensp;&emsp;Grade needed on remaining assignments to achieve " + idealGrade + "%"
                                                                                        + ": " + courseIdealGrade + "%"
                         + "<br/>&ensp;&emsp;Final grade if you get " + predictedGrade  + "%"
                                                        + " on the remaining assignments: " + coursePredictedGrade + "%"
                         + "<br><br>";
        setText(labelText);

        return this;
    }

    // EFFECTS: constructs a string where each assignment would be on a new line if parsed as html
    public String eachAssignmentNewLine(Course course) {
        String individualAssignments = "";

        for (Assignment assignment : course.getAssignments()) {
            individualAssignments += ("<br/>&ensp;&emsp;&ensp;&emsp;" + assignment);
        }
        return individualAssignments;
    }
}
