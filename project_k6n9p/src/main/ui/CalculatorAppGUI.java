package ui;

import exceptions.InputOutOfRangeException;
import exceptions.InvalidInputException;
import model.Assignment;
import model.Course;
import model.CourseList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import static tools.NumberUtilities.parseStringInput;
import static tools.NumberUtilities.roundValueTwoDecimalPlaces;

// Calculator application, representing the graphical user interface
// SOURCE: portions of code based on the following sources:
//   - JList: https://docs.oracle.com/javase/tutorial/uiswing/components/list.html#mutable
//   - JDialogBox: https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
//   - Notable mentions to Javadocs for JButton, JPanel, JFrame, ActionEvents, and JLayouts
public class CalculatorAppGUI implements ActionListener {
    private static final String JSON_STORE = "./data/courses.json";
    private static final int mainFrameWidth = 800;
    private static final int mainFrameHeight = 500;

    private static final ImageIcon icon = new ImageIcon("./data/images/calculatorIcon.png");
    private static final ImageIcon angryEmoji = new ImageIcon("./data/images/angryEmoji.png");
    private static final ImageIcon neutralEmoji = new ImageIcon("./data/images/neutralEmoji.png");
    private static final ImageIcon starEyesEmoji = new ImageIcon("./data/images/starEyesEmoji.png");

    private JFrame frame;
    private JPanel menuPanel;
    private JScrollPane viewPanel;
    private JButton addCourseButton;
    private JButton editCourseButton;
    private JButton removeCourseButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton resetButton;

    private JList list;
    private DefaultListModel listModel;

    private CourseList userCourses;
    private double assignmentGrade;
    private double assignmentWeight;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the calculator application and throws FileNotFoundException
    // if the user tries to load data from a file that does not exist
    public CalculatorAppGUI() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runCalculator();
    }

    // MODIFIES: this
    // EFFECTS: runs the calculator application
    public void runCalculator() {
        initializeFrameAndPanels();
        initializeCourses();
    }

    // MODIFIES: this
    // EFFECTS: initializes the main frame before and after the panels have been added
    public void initializeFrameAndPanels() {
        initializeMainFrame();
        initializeMenuPanel();
        initializeViewPanel();

        // add panels to frame
        frame.setLayout(new BorderLayout());
        frame.add(BorderLayout.PAGE_START, menuPanel);
        frame.add(BorderLayout.CENTER, viewPanel);

        // display the window
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    // MODIFIES: this
    // EFFECTS: initializes the main frame by adding initial components
    public void initializeMainFrame() {
        // set up frame
        frame = new JFrame("Grade Calculator Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(mainFrameWidth, mainFrameHeight));
        frame.setResizable(false);

        frame.setIconImage(icon.getImage());

        frame.getContentPane().setBackground(new Color(200, 200, 200));
    }

    // MODIFIES: this
    // EFFECTS: initializes the menu panel by adding initial components
    public void initializeMenuPanel() {
        // set up menu (top) panel
        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(235, 235, 235));
        menuPanel.setSize(mainFrameWidth, 100);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

        // add buttons for edit course, add course, delete course, save, load
        addCourseButton = new JButton("Add Course");
        editCourseButton = new JButton("Edit Course");
        removeCourseButton = new JButton("Remove Course");
        loadButton = new JButton("Load Data");
        saveButton = new JButton("Save Data");
        resetButton = new JButton("Reset");

        addButtons();
        initializeButtons();
    }

    // MODIFIES: this
    // EFFECTS: creates buttons to be added to the menu panel
    public void addButtons() {
        int spaceBetweenButtons = 20;

        menuPanel.add(Box.createRigidArea(new Dimension(55, 40)));
        menuPanel.add(addCourseButton);
        menuPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, spaceBetweenButtons)));
        menuPanel.add(editCourseButton);
        menuPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, spaceBetweenButtons)));
        menuPanel.add(removeCourseButton);
        menuPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, spaceBetweenButtons)));
        menuPanel.add(loadButton);
        menuPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, spaceBetweenButtons)));
        menuPanel.add(saveButton);
        menuPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, spaceBetweenButtons)));
        menuPanel.add(resetButton);
    }

    // MODIFIES: this
    // EFFECTS: activates buttons by adding event handling capabilities
    public void initializeButtons() {
        // set button actionListeners
        addCourseButton.addActionListener(this);
        editCourseButton.addActionListener(this);
        removeCourseButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        resetButton.addActionListener(this);

        addCourseButton.setActionCommand("a");
        editCourseButton.setActionCommand("e");
        removeCourseButton.setActionCommand("r");
        saveButton.setActionCommand("s");
        loadButton.setActionCommand("l");
        resetButton.setActionCommand("v");
    }

    // MODIFIES: this
    // EFFECTS: initializes the view panel by adding initial components
    public void initializeViewPanel() {
        // set up view (bottom) panel
        initializeJList();

        viewPanel = new JScrollPane(list);

        viewPanel.getViewport().setBackground(new Color(200, 200, 200));
        viewPanel.setPreferredSize(new Dimension(mainFrameWidth, mainFrameHeight - 40));
        viewPanel.setLayout(new ScrollPaneLayout());
        viewPanel.createVerticalScrollBar();
        viewPanel.createHorizontalScrollBar();
        viewPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        viewPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    // MODIFIES: this
    // EFFECTS: initializes the list by adding initial components and event handling
    // SOURCE: for the mouse click event, handling code heavily based on
    // http://www.java2s.com/Tutorials/Java/Swing_How_to/JList/Handle_double_click_on_JList.htm
    public void initializeJList() {
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setCellRenderer(new MyListCellRenderer());

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    Course course = (Course) list.getModel().getElementAt(index);
                    courseProgressPopup(course);
                }
            }
        });
    }

    // EFFECTS: produces a dialog box that informs the user on their progress in the course
    public void courseProgressPopup(Course course) {
        double currentCourseGrade = course.calculateCurrentGrade();

        double currentIdealDifferenceUnrounded = course.getIdealGrade() - currentCourseGrade;
        double currentIdealDifferenceRounded = roundValueTwoDecimalPlaces(currentIdealDifferenceUnrounded);

        if (currentIdealDifferenceRounded <= 0) {
            JOptionPane.showMessageDialog(frame,
                    "Wow, you're currently doing " + -currentIdealDifferenceRounded + "%"
                    + " better than your ideal mark in the class. Keep it up!", "Information Message",
                    JOptionPane.INFORMATION_MESSAGE,
                    starEyesEmoji);
        } else if (currentIdealDifferenceRounded <= 5) {
            JOptionPane.showMessageDialog(frame,
                    "You're currently doing " + currentIdealDifferenceRounded + "%"
                            + " lower than your ideal mark in the class. Clutch the final?", "Information Message",
                    JOptionPane.INFORMATION_MESSAGE,
                    neutralEmoji);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "You're currently doing " + currentIdealDifferenceRounded + "%"
                            + " lower than your ideal mark in the class. Yikes...", "Information Message",
                    JOptionPane.INFORMATION_MESSAGE,
                    angryEmoji);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes CourseList (array of courses)
    public void initializeCourses() {
        userCourses = new CourseList();
    }

    @Override
    // MODIFIES: this
    // EFFECTS: processes user command via buttons
    public void actionPerformed(ActionEvent e) {
        String menuCommand = e.getActionCommand();
        if (menuCommand.equals("a")) {
            addCourseValidation();
        } else if (menuCommand.equals("e")) {
            editCourseValidation();
        } else if (menuCommand.equals("r")) {
            removeCourse();
        } else if (menuCommand.equals("s")) {
            saveData();
        } else if (menuCommand.equals("l")) {
            loadData();
        } else if (menuCommand.equals("v")) {
            reset();
        }
    }

    // MODIFIES: this
    // EFFECTS: validates that the course desired to be edited exists
    public void editCourseValidation() {
        Course courseFromName;

        String desiredEdit = JOptionPane.showInputDialog(
                frame,
                "Which course would you like to edit?");

        courseFromName = userCourses.containsByName(desiredEdit);

        // if course exists, edit course
        if (desiredEdit == null) {
            return;
        } else if (courseFromName != null) {
            editCourse(courseFromName);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Course does not exist",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: edits course, adds or deletes assignments
    public void editCourse(Course course) {
        int addOrRemove = addOrRemoveDialogBox();

        if (addOrRemove == JOptionPane.YES_OPTION) {
            // add option
            addAssignment(course);

            int index = listModel.indexOf(course);
            listModel.removeElement(course);
            listModel.add(index, course);
        } else if (addOrRemove == JOptionPane.NO_OPTION) {
            // remove option
            String assignmentToRemoveName = JOptionPane.showInputDialog(
                    frame,
                    "Enter the name of the assignment you would like to remove.",
                    JOptionPane.QUESTION_MESSAGE);

            boolean isAssignmentRemoved = removeAssignment(course, assignmentToRemoveName);

            if (isAssignmentRemoved) {
                int index = listModel.indexOf(course);
                listModel.removeElement(course);
                listModel.add(index, course);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Assignment does not exist. Assignment removal failed.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // EFFECTS: creates a dialog box that asks user whether they want to add or remove a course
    public int addOrRemoveDialogBox() {
        Object[] options = {"Add", "Remove"};

        return JOptionPane.showOptionDialog(frame,
                            "Would you like to add or remove an existing assignment?",
                            "Choose an option",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,     //do not use a custom Icon
                            options,  //the titles of buttons
                            options[0]);
    }

    // MODIFIES: this
    // EFFECTS: validates that the course to be added doesn't already exist
    private void addCourseValidation() {
        String courseName = JOptionPane.showInputDialog(
                            frame,
                            "Enter the name of the course you would like to add");

        Course courseFromName = userCourses.containsByName(courseName);

        // if course exists, edit course
        if (courseFromName != null) {
            errorDialogBox("The course " + courseName + " already exists. Add failed.");
        } else if (courseName == null) {
            return;
        } else if (courseName.equals("")) {
            errorDialogBox("Course name can not be empty. Add failed.");
        } else {
            Course newCourse = new Course(courseName);
            addCourse(newCourse);
        }

    }

    // MODIFIES: this
    // EFFECTS: adds as many assignments as the user wants to a course, and asks for idea; and
    //          predicted grade
    public void addCourse(Course newCourse) {
        userCourses.getCourses().add(newCourse);

        int addAssignment = JOptionPane.showConfirmDialog(
                                frame,
                                "Would you like to add assignments to " + newCourse.getName() + "?",
                                "Choose yes or no",
                                JOptionPane.YES_NO_OPTION);

        if (addAssignment == JOptionPane.YES_OPTION) {
            boolean isAddingAssignment = true;

            while (isAddingAssignment) {
                addAssignment(newCourse);

                int addAnotherAssignment = JOptionPane.showConfirmDialog(
                        frame,
                        "Would you like to add another assignment?",
                        "Choose yes or no",
                        JOptionPane.YES_NO_OPTION);

                // no more assignments to be added, prompt for ideal and predicted grades
                if (addAnotherAssignment == JOptionPane.NO_OPTION) {
                    isAddingAssignment = false;
                    userSetIdealGrade(newCourse);
                    userSetPredictedGrade(newCourse);
                }
            }
        }
        listModel.addElement(newCourse);
    }

    // REQUIRES: assignmentName is string, grade and weight are doubles in range 0.0 to 100.0
    // MODIFIES: this
    // EFFECTS: add assignment to specified course
    public void addAssignment(Course course) {

        String assignmentName = inputDialogBoxString("Enter the name of the assignment.");

        // TODO: fix after cpsc 210 ends
        if (assignmentName == null || assignmentName.equals("")) {
            errorDialogBox("Assignment name can not be empty. Add failed");
            return;
        }

        try {
            String assignmentGradeString = inputDialogBoxString("Enter the grade you received on the assignment.");

            if (assignmentGradeString == null || assignmentGradeString.equals("")) {
                errorDialogBox("Assignment name can not be empty. Add failed");
                return;
            }

            assignmentGrade = parseStringInput(assignmentGradeString);

            String assignmentWeightString = inputDialogBoxString("Enter the weight of the assignment.");

            if (assignmentWeightString == null || assignmentWeightString.equals("")) {
                errorDialogBox("Assignment name can not be empty. Add failed");
                return;
            }

            assignmentWeight = parseStringInput(assignmentWeightString);

        } catch (InvalidInputException e) {
            errorDialogBox("Invalid value. Assignment addition failed.");
            return;
        }

        addNewAssignment(course, assignmentName, assignmentGrade, assignmentWeight);
    }


    // MODIFIES: this
    // EFFECTS: adds new assignment to course
    public void addNewAssignment(Course course,
                                 String assignmentName,
                                 double assignmentGrade,
                                 double assignmentWeight) {
        // create and add entries to assignment
        Assignment newAssignment;

        try {
            newAssignment = new Assignment(assignmentName, assignmentGrade, assignmentWeight);

            addAssignmentStatus(course, newAssignment);

        } catch (InputOutOfRangeException e) {
            JOptionPane.showMessageDialog(frame,
                    "Failed to add assignment. Grade and weight values must be between 0 and 100.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds assignment to course and tells user whether the process was successful or not
    public void addAssignmentStatus(Course course, Assignment newAssignment) {
        // add new assignment to course and tell user whether it was successful or not
        boolean isCourseAdditionSuccessful = course.addAssignment(newAssignment);

        if (!isCourseAdditionSuccessful) {
            String addAssignmentStatusErrorMessage = "Assignment addition unsuccessful, please try again."
                    + "\nThis may be because you are "
                    + "trying to add an assignment with the same name as one you have already added.";
            errorDialogBox(addAssignmentStatusErrorMessage);
        }
    }

    // MODIFIES: this
    // EFFECTS: set the ideal grade for course
    public void userSetIdealGrade(Course course) {
        String idealGradeString = inputDialogBoxString("What grade would you like to receive in this class?");

        double idealGrade;

        try {
            if (idealGradeString == null) {
                return;
            } else if (idealGradeString.equals("")) {
                errorDialogBox("Assignment name can not be empty. Add failed");
                return;
            }

            idealGrade = parseStringInput(idealGradeString);
            course.setIdealGrade(idealGrade);

        } catch (InvalidInputException e) {
            errorDialogBox("Invalid value. Ideal grade will be set to 83.0 by default.");
        } catch (InputOutOfRangeException e) {
            errorDialogBox("Value must be between 0 and 100. Predicted grade will be set to 83.0 by default");
        }
    }

    // MODIFIES: this
    // EFFECTS: set the predicted grade for course
    public void userSetPredictedGrade(Course course) {
        String predictedGradeString = inputDialogBoxString("What grade do you expect to "
                + "receive on the remaining assignments?");

        if (predictedGradeString == null) {
            return;
        } else if (predictedGradeString.equals("")) {
            errorDialogBox("Assignment name can not be empty. Add failed");
            return;
        }

        double predictedGrade;

        try {
            predictedGrade = parseStringInput(predictedGradeString);
            course.setPredictedGrade(predictedGrade);
        } catch (InvalidInputException e) {
            errorDialogBox("Invalid value. Predicted grade will be set to 75.0 by default.");
        } catch (InputOutOfRangeException e) {
            errorDialogBox("Value must be between 0 and 100. Predicted grade will be set to 75.0 by default");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes course from course list
    public void removeCourse() {
        boolean isCourseRemoved;

        String courseName = inputDialogBoxString("Enter the name of the course you would like to remove.");

        if (courseName == null) {
            return;
        } else {
            isCourseRemoved = userCourses.removeCourseByName(courseName);
        }


        if (isCourseRemoved) {
            listModel.removeAllElements();

            for (Course course: userCourses.getCourses()) {
                listModel.addElement(course);
            }

        } else {
            errorDialogBox("Course does not exist, course removal failed.");
        }
    }

    // MODIFIES: this
    // EFFECTS: remove assignment from a specified course, returning true
    //          if successful and false if not successful
    public boolean removeAssignment(Course course, String assignmentName) {
        return course.removeAssignment(assignmentName);
    }

    // MODIFIES: this
    // EFFECTS: saves course list to file
    public void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(userCourses);
            jsonWriter.close();
            JOptionPane.showMessageDialog(frame,
                    "Saved courses to " + JSON_STORE,
                    "Success",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (FileNotFoundException e) {
            errorDialogBox("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads course list to file
    public void loadData() {
        try {
            userCourses = jsonReader.read();

            listModel.removeAllElements();

            for (Course course: userCourses.getCourses()) {
                listModel.addElement(course);
            }

        } catch (IOException e) {
            errorDialogBox("Unable to read from file: " + JSON_STORE);
        } catch (InputOutOfRangeException e) {
            errorDialogBox("Entries in file invalid, load failed.");
        }
    }

    // MODIFIES: this
    // EFFECTS: clears (j)list and course list
    public void reset() {
        userCourses = new CourseList();
        listModel.removeAllElements();
    }

    // EFFECTS: constructs a error box with a specified text message
    public void errorDialogBox(String text) {
        JOptionPane.showMessageDialog(frame,
                text,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: constructs a dialog box with a specified text message to receive a string user input
    public String inputDialogBoxString(String text) {
        return JOptionPane.showInputDialog(frame, text);
    }
}
