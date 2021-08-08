package ui;

import exceptions.InputOutOfRangeException;
import exceptions.InvalidInputException;
import model.Assignment;
import model.Course;
import model.CourseList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static tools.NumberUtilities.parseStringInput;

// Calculator application, representing the console user interface
// SOURCE: code organization and some methods based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class CalculatorAppTerminal {
    private static final String JSON_STORE = "./data/courses.json";

    private Scanner input;
    private int greetingCount;
    private CourseList userCourses;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the calculator application
    public CalculatorAppTerminal() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runCalculator();
    }

    // MODIFIES: this
    // EFFECTS: runs the calculator application, processes user input, and indirectly
    //            - greets user
    //            - initializes courses
    //            - displays menu
    public void runCalculator() {
        boolean isCalculating = true;
        String menuCommand;

        greetingCount = 0;

        // initialize scanner object, using \n as the delimiter instead of the default " "
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        greetUser();

        initializeCourses();

        while (isCalculating) {
            displayMenu();

            menuCommand = input.next();
            menuCommand = menuCommand.toLowerCase();

            if (menuCommand.equals("q")) {
                isCalculating = false;
            } else {
                processCommand(menuCommand);
            }

        }
        greetUser();
    }

    // EFFECTS: welcome the user upon the first time the method is called, and
    //          thank the user upon the second time the method is called
    public void greetUser() {

        if (greetingCount == 0) {
            System.out.println("\nWelcome to the grade calculator");
            greetingCount++;
        } else if (greetingCount == 1) {
            System.out.println("\nThank you for using the grade calculator");
        }
    }

    // EFFECTS: displays a menu of options to user
    public void displayMenu() {
        System.out.println("\nPlease select one of the following options:");
        System.out.println("\tView course(s)      -> press v");
        System.out.println("\tEdit a course       -> press e");
        System.out.println("\tAdd a course        -> press a");
        System.out.println("\tRemove a course     -> press r");
        System.out.println("\tSave data           -> press s");
        System.out.println("\tLoad previous data  -> press l");
        System.out.println("\tQuit                -> press q");
    }

    // MODIFIES: this
    // EFFECTS: initializes CourseList (array of courses)
    public void initializeCourses() {
        userCourses = new CourseList();
    }

    // MODIFIES: this
    // EFFECTS: process user command from menu
    public void processCommand(String menuCommand) {
        switch (menuCommand) {
            case "v":
                viewCourse();
                break;
            case "e":
                editCourseValidation();
                break;
            case "a":
                addCourseValidation();
                break;
            case "r":
                removeCourse();
                break;
            case "s":
                saveCourses();
                break;
            case "l":
                loadCourses();
                break;
            default:
                System.out.println("Not a valid entry, please try again");
                break;
        }
    }

    // EFFECTS: displays the statistics for each course, including the current course grade and weight,
    //          grade needed on remaining assignments to get the ideal final grade, and final grade received
    //          if the predicted grade is achieved for the remaining assignments
    public void viewCourse() {
        if (userCourses.getCourses().isEmpty()) {
            System.out.println("You have not added any courses yet");
        } else {
            for (Course course : userCourses.getCourses()) {
                double currentCourseGrade = course.calculateCurrentGrade();
                double currentCourseWeight = course.calculateCurrentWeight();
                double courseIdealGrade = course.calculateIdealFinalGrade();
                double coursePredictedGrade = course.calculatePredictedFinalGrade();

                String title = "========== Statistics for " + course.getName() + " ==========";

                System.out.println(title.toUpperCase());
                System.out.println("\n\tAssignments submitted: ");
                System.out.println(eachAssignmentNewLine(course));
                System.out.println("\tCurrent course grade: " + currentCourseGrade);
                System.out.println("\tCurrent course weight: " + currentCourseWeight);
                System.out.println("\tGrade needed on remaining assignments to achieve " + course.getIdealGrade()
                        + ": " + courseIdealGrade);
                System.out.println("\tFinal grade if you get " + course.getPredictedGrade()
                        + " on the remaining assignments: " + coursePredictedGrade);
                System.out.println("\n");
            }
        }
    }

    // EFFECTS: returns an indented assignment per line for all the assignments in a course
    // NOTE: returns "" to satisfy IntelliJ during its call in viewCourse()
    public String eachAssignmentNewLine(Course course) {
        for (Assignment assignment : course.getAssignments()) {
            System.out.println("\t\t" + assignment);
        }
        return "";
    }

    // MODIFIES: this
    // EFFECTS: validates that the course desired to be edited exists
    public void editCourseValidation() {
        Course courseFromName;

        System.out.println("Which course would you like to edit?");
        String desiredEdit = input.next();

        courseFromName = userCourses.containsByName(desiredEdit);

        // if course exists, edit course
        if (courseFromName != null) {
            editCourse(courseFromName);
        } else {
            System.out.println("Course does not exist");

        }
    }

    // MODIFIES: this
    // EFFECTS: edits course, adds or deletes assignments
    public void editCourse(Course course) {
        System.out.println("Here is what " + course.getName() + " currently looks like: ");
        System.out.println("\n\tAssignments submitted: ");
        System.out.println(eachAssignmentNewLine(course));

        System.out.println("Would you like to add or remove an existing assignment? Enter a or r: ");
        String addOrRemove = input.next();

        if (addOrRemove.equals("a")) {
            addAssignment(course);
        } else if (addOrRemove.equals("r")) {
            System.out.println("Please enter the name of the assignment you would like to remove: ");
            String assignmentToRemoveName = input.next();

            boolean isCourseRemoved = removeAssignment(course, assignmentToRemoveName);

            if (isCourseRemoved) {
                System.out.println("Assignment successfully removed");
            } else {
                System.out.println("Assignment does not exist");
            }

        } else {
            System.out.println("Invalid entry");
        }
    }

    // REQUIRES: input for courseName must be a string
    // MODIFIES: this
    // EFFECTS: validates that the course to be added doesn't already exist
    public void addCourseValidation() {
        System.out.println("Please enter the name of the course you would like to add:");
        String courseName = input.next();

        Course courseFromName = userCourses.containsByName(courseName);

        // if course exists, edit course
        if (courseFromName != null) {
            System.out.println("Sorry, that course already exists.");
        } else {
            Course newCourse = new Course(courseName);
            addCourse(newCourse);

        }
    }

    // REQUIRES: input for addAssignment must be y or n
    // MODIFIES: this
    // EFFECTS: adds as many assignments as the user wants to a course, and asks for idea; and
    //          predicted grade
    public void addCourse(Course newCourse) {
        userCourses.getCourses().add(newCourse);

        System.out.println("Would you like to add assignments to " + newCourse.getName() + "? Type y or n");
        String addAssignment = input.next();

        if (addAssignment.equals("y")) {
            boolean isAddingAssignment = true;

            while (isAddingAssignment) {
                addAssignment(newCourse);

                System.out.println("Would you like to add another assignment? Type y or n");
                String addAnotherAssignment = input.next();

                // no more assignments to be added, prompt for ideal and predicted grades
                if (!addAnotherAssignment.equals("y")) {
                    if (!addAnotherAssignment.equals("n")) {
                        System.out.println("Invalid entry. Will not add another assignment.");
                    }
                    isAddingAssignment = false;
                    userSetIdealPredictedGrade(newCourse);
                }
            }
        } else if (addAssignment.equals("n")) {
            return;
        } else {
            System.out.println("Invalid entry. Will not add assignments.");
        }
    }

    // REQUIRES: assignmentName is string, grade and weight are doubles in range 0.0 to 100.0
    // MODIFIES: this
    // EFFECTS: add assignment to specified course
    public void addAssignment(Course course) {
        // create and add entries to assignment
        Assignment newAssignment;

        System.out.println("Please enter the name of the assignment:");
        String assignmentName = input.next();

        double assignmentGrade = 0;
        double assignmentWeight = 0;

        try {
            System.out.println("Please enter the grade you received on the assignment:");
            assignmentGrade = parseStringInput(input.next());

            System.out.println("Please enter the weight of the assignment:");
            assignmentWeight = parseStringInput(input.next());

        } catch (InvalidInputException e) {
            System.out.println("Invalid value. Assignment addition failed.");
            return;
        }

        try {
            newAssignment = new Assignment(assignmentName, assignmentGrade, assignmentWeight);

            addAssignmentStatus(course, newAssignment);

        } catch (InputOutOfRangeException e) {
            System.out.println("Failed to add assignment. Grade and weight values must be between 0 and 100.");
        }
    }

    // MODIFIES: this course, this assignment;
    // EFFECTS: adds assignment to course and tells user whether the process was successful or not
    public void addAssignmentStatus(Course course, Assignment newAssignment) {
        // add new assignment to course and tell user whether it was successful or not
        boolean isCourseAdditionSuccessful = course.addAssignment(newAssignment);

        if (isCourseAdditionSuccessful) {
            System.out.println("Assignment successfully added: " + newAssignment);
        } else {
            System.out.println("Assignment addition unsuccessful, please try again. This may be because you are "
                    + "trying to add an assignment with the same name as one you have already added.");
        }
    }


    // REQUIRES: ideal and predicted grade must be doubles in range 0.0 to 100.0
    // MODIFIES: this
    // EFFECTS: set the ideal and predicted grade for course
    public void userSetIdealPredictedGrade(Course course) {
        System.out.println("What grade would you like to receive in this class?");
        double idealGrade = 0;
        try {
            idealGrade = parseStringInput(input.next());
            course.setIdealGrade(idealGrade);
        } catch (InvalidInputException e) {
            System.out.println("Invalid value. Ideal grade will be set to 83.0 by default.");
        } catch (InputOutOfRangeException e) {
            System.out.println("Registering of value failed. Ideal grade must be between 0 and 100.");
        }

        System.out.println("What grade do you expect to receive on the remaining assignments?");
        double predictedGrade = 0;
        try {
            predictedGrade = parseStringInput(input.next());
            course.setPredictedGrade(predictedGrade);
        } catch (InvalidInputException e) {
            System.out.println("Invalid value. Predicted grade will be set to 75.0 by default.");
        } catch (InputOutOfRangeException e) {
            System.out.println("Registering of value failed. Predicted grade must be between 0 and 100.");
        }
    }

    // MODIFIES: this
    // EFFECTS: remove course from course list
    public void removeCourse() {
        boolean isCourseRemoved;

        System.out.println("Enter the name of the course you would like to remove");
        String courseName = input.next();

        isCourseRemoved = userCourses.removeCourseByName(courseName);

        if (isCourseRemoved) {
            System.out.println("Course successfully removed");
        } else {
            System.out.println("Course does not exist");
        }
    }

    // MODIFIES: this
    // EFFECTS: remove assignment from a specified course, returning true
    //          if successful and false if not successful
    public boolean removeAssignment(Course course, String assignmentName) {
        return course.removeAssignment(assignmentName);
    }

    // EFFECTS: saves course list to file
    public void saveCourses() {
        try {
            jsonWriter.open();
            jsonWriter.write(userCourses);
            jsonWriter.close();
            System.out.println("Saved courses to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: loads course list to file
    public void loadCourses() {
        try {
            userCourses = jsonReader.read();
            System.out.println("Loaded courses from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (InputOutOfRangeException e) {
            System.out.println("Entries in file invalid, load failed.");
        }
    }
}

