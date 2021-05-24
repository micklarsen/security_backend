package rest;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CommentDTO;
import dto.CommentsDTO;
import errorhandling.CommentException;
import errorhandling.NotFoundException;
import facades.CommentFacade;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import java.security.NoSuchAlgorithmException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Path("comments")
public class CommentResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CommentFacade FACADE = CommentFacade.getCommentFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = Logger.getLogger(CommentResource.class.getName());
    
     private ConsoleHandler consoleHandler = null;
  private FileHandler fileHandler  = null;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCommentsForAll() {
        return "{\"msg\":\"Hello from the comment section\"}";
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public String commentCount() throws CommentException {
        long count = FACADE.getCommentCount();
        return "{\"count\":" + count + "}";
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllComments() throws CommentException, IOException {
//        FsLogger logger = FsLogger.getInstance();
//        logger.warning("New comment logger test");
        consoleHandler = new ConsoleHandler();
            fileHandler  = new FileHandler("AllComments.log", true);
            
            //Adding formatter
            fileHandler.setFormatter(new SimpleFormatter());
             
            //Assigning handlers to LOGGER object
            LOGGER.addHandler(consoleHandler);
            LOGGER.addHandler(fileHandler);
             
            //Setting levels to handlers and LOGGER
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            
            LOGGER.setLevel(Level.ALL);
             
            LOGGER.config("Configuration done.");
             
            //Console handler removed
            LOGGER.removeHandler(consoleHandler);
             
            LOGGER.log(Level.FINE, "Finer logged");
            
            LOGGER.log(Level.SEVERE, "SEVERE logged");
        
        LOGGER.finer("Finest example on LOGGER handler completed.");
        
        CommentsDTO comment = FACADE.getAllComments();
        return GSON.toJson(comment);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getUSerComment(@PathParam("id") long id) throws CommentException {
        return GSON.toJson(FACADE.getUserComment(id));
    }

    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String deleteUserComment(@PathParam("id") long id) throws CommentException {
        CommentDTO commentDelete = FACADE.deleteComment(id);
        return GSON.toJson("Comment with id: " + commentDelete.getId() + " was deleted");
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addComment(String comment) throws CommentException, NotFoundException, NoSuchAlgorithmException {
        CommentDTO c = GSON.fromJson(comment, CommentDTO.class);
        CommentDTO commentAdded = FACADE.addComment(c.getUserComment(), c.getTopicID(), c.getUserName(), c.getImageID());
        System.out.println(c.getImageID());
        return GSON.toJson(commentAdded);
    }
}