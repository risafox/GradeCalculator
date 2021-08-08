package exceptions;

// exception representing an input value that is out of range
// in this project, it is thrown when a double value is not between 0 and 100
public class InputOutOfRangeException extends Exception {
    public InputOutOfRangeException(String msg) {
        super(msg);
    }
}
