
package logging;

// importing logger functionality
import java.io.IOException;
import java.util.logging.Level;


public class LogTester {
    
    public static void main(String[] args) {
        try{
            // Name of the file where logs will be written to
            Log log = new Log("log.txt");
            
            // Since its warning level only warning and severe will be printed and not info
            log.logger.setLevel(Level.WARNING);
            
            // Levels/layers of logging
            log.logger.info("Info msg");
            log.logger.warning("Warning msg");
            log.logger.severe("Severe msg");
            
        } catch (IOException | SecurityException e){
            System.out.println("logging.Log.main() had an error: " + e.getMessage() );
        }
    }
    
}
