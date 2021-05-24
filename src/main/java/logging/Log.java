
package logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Log {
    public Logger logger;
    FileHandler fh;
    
    public Log(String file_name) throws SecurityException, IOException { // Logger throws these exceptions so needs to be added
        // If the file does not exist a new will be created
        File file = new File(file_name);
        if (!file.exists()) {
            file.createNewFile();
        }
        // FileHandler is used to write to the given filename
        fh = new FileHandler(file_name, true);
        logger = Logger.getLogger("log.txt");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        
    }
    
}
