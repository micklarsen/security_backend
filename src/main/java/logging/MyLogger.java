package logging;

import java.io.IOException;
// Java buildin logger
import java.util.logging.*;

public class MyLogger {

    // Logger for this class
    public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);  // Name your logger after your class. Can also use MyLogger.class.getName() to include packages

    public static void setupLogger() {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        // Set the level of the logger
        LOGGER.setLevel(Level.ALL);

//        // ConsoleHandler
//        ConsoleHandler ch = new ConsoleHandler();
//        // The level of what should be displayed in the console
//        ch.setLevel(Level.SEVERE);
//        // Add the handler to the logger
//        LOGGER.addHandler(ch);

        // Since we are working with files IOException is needed
        try {
            // Filehandler to be able to add a file
            FileHandler fh = new FileHandler("myLogger.log", true); // ", true" if you do not want the file handler to overwrite and append instead
            //fh.setFormatter(new SimpleFormatter()); // format it like it is shown in the console. Default is to output it as XML.
            // Set level of what to log in the file
            fh.setLevel(Level.ALL); // Everything above FINE
            // Add the handler to the logger
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File logger not working.", e);
        }
    }

    public static void main(String[] args) throws IOException {
        // Setup the logger
        setupLogger();

        // Simple log with a level and log it
        // It is being printed since it is being passed to the root logger (can be accessed -> Logger root = Logger.getLogger("");)
        // LOGGER.log(Level.INFO, "My first log"); // This logger has a cutoff from INFO so only INFO, WARNING and SEVERE will be displayed
        // The above code written in a shorter way
        LOGGER.info("My first log");
        LOGGER.fine("My second log");

        // purposly throw an error and log it.
//        try {
//            throw new java.io.IOException("Could not read file.");
//        } catch (java.io.IOException e) {
//            LOGGER.log(Level.SEVERE, "File read error.", e);
//            // let the error happen after we've logged it
//            // throw e;
//        }

        // Class test
        Test.test();
        // Method test
        methodTest();
        
        // All levels in order (FINEST not so important and SEVERE very important
        /*
        SEVERE
        WARNING
        INFO
        CONFIG
        FINE
        FINER
        FINEST
         */
    }
    
    // Testing logging from another method
    public static void methodTest() {
         LOGGER.config("I'm from another method");
    }

}

// Class to test logging from another class
class Test {
    // By using the global logger that was made it will get that logger instead of making a new one.
    // This way the setup should be the exact same as the previous one
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    static void test() {
        LOGGER.info("I'm from another class");
    }
}
