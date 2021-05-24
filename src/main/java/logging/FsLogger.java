
package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FsLogger {

    private static FsLogger instance;

    private final String filename = "./LogFsForensics.log";
    private final String loggerName = "FsLogger";
    private static Logger logger;   
        private FileHandler fh;
        private final int limit = 1024*10000; //10 MB
        private SimpleFormatter sf;

    private FsLogger() throws IOException {
        logger =  Logger.getLogger(loggerName);

        sf = new SimpleFormatter();
        try {
            fh = new FileHandler(filename, limit, 1, true);
            sf = new SimpleFormatter();
            fh.setFormatter(sf);

            logger.addHandler(fh);
            
            fh.close();
        } catch (SecurityException | IOException e) {           
            e.printStackTrace();
        }
    }

    public static FsLogger getInstance() throws IOException {
        if(instance == null) {
            instance = new FsLogger();
        }
        return instance;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warning(String message) {
        logger.warning(message);        
    }

    public void severe(String message) {
        logger.severe(message);
    }
    
}
