package errorhandling;

public class CommentException extends Exception {

    public CommentException(String message) {
        super(message);
    }

    public CommentException() {
        super("No Connection to the server");
    }
}