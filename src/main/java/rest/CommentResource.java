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
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Path("comments")
public class CommentResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CommentFacade FACADE = CommentFacade.getCommentFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = Logger.getLogger(CommentResource.class.getName());

    private ConsoleHandler consoleHandler = null;
    private FileHandler fileHandler = null;

    //For testing to see if we hit our comments 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCommentsForAll() {
        return "{\"msg\":\"Hello from the comment section\"}";
    }

    //To test if we have connection to our database
//    @GET
//    @Path("count")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String commentCount() throws CommentException {
//        // Get rid of any handlers that the root logger has
//        LogManager.getLogManager().reset();
//        consoleHandler = new ConsoleHandler();
//        try {
//            fileHandler = new FileHandler("CountComments.log", true);
//        } catch (IOException | SecurityException ex) {
//            Logger.getLogger(CommentResource.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //Adding formatter
//        fileHandler.setFormatter(new SimpleFormatter());
//
//        //Assigning handlers to LOGGER object
//        LOGGER.addHandler(consoleHandler);
//        LOGGER.addHandler(fileHandler);
//
//        //Setting levels to handlers and LOGGER
//        consoleHandler.setLevel(Level.ALL);
//        fileHandler.setLevel(Level.ALL);
//
//        LOGGER.setLevel(Level.ALL);
//
//        LOGGER.config("Configuration done.");
//
//        //Console handler removed
//        LOGGER.removeHandler(consoleHandler);
//
//        long count = FACADE.getCommentCount();
//
//        //Log request to all comments endpoint 
//        LOGGER.log(Level.INFO, "Requested total count of comments: {0}", count);
//
//        fileHandler.close();
//
//        return "{\"count\":" + count + "}";
//    }
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllComments() throws CommentException {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        consoleHandler = new ConsoleHandler();
        try {
            fileHandler = new FileHandler("AllComments.log", true);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(CommentResource.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        //Log request to get all comments
        LOGGER.log(Level.INFO, "Requested access to all comments");

        fileHandler.close();

        CommentsDTO comment = FACADE.getAllComments();
        return GSON.toJson(comment);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getUSerComment(@PathParam("id") long id) throws CommentException {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        consoleHandler = new ConsoleHandler();
        try {
            fileHandler = new FileHandler("UserCommentsWithID.log", true);
        } catch (IOException | SecurityException ex) {
//            Logger.getLogger(CommentResource.class.getName()).log(Level.SEVERE, null, ex);
              LOGGER.log(Level.SEVERE, "Token exception: {0}", ex);
        }

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

        //Log request to get a comment with an id 
        LOGGER.log(Level.INFO, "Requested access to a comment on ID: {0}", id);

        fileHandler.close();

        return GSON.toJson(FACADE.getUserComment(id));
    }

    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String deleteUserComment(@PathParam("id") long id) throws CommentException {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        consoleHandler = new ConsoleHandler();

        try {
            fileHandler = new FileHandler("DeleteComment.log", true);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(CommentResource.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        //Log request to delete a comment with an id 
        LOGGER.log(Level.INFO, "Requested access to delete an comment: {0}", id);

        fileHandler.close();

        CommentDTO commentDelete = FACADE.deleteComment(id);
        return GSON.toJson("Comment with id: " + commentDelete.getId() + " was deleted");
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addComment(String comment) throws CommentException, NotFoundException, NoSuchAlgorithmException {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        consoleHandler = new ConsoleHandler();

        try {
            fileHandler = new FileHandler("POSTComment.log", true);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(CommentResource.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        CommentDTO c = GSON.fromJson(comment, CommentDTO.class);
        CommentDTO commentAdded = FACADE.addComment(c.getUserComment(), c.getTopicID(), c.getUserName(), c.getImageID());

        String logUserText = "User: " + c.getUserName() + " Comment: " + c.getUserComment();
        //Log request to create a comment 
        LOGGER.log(Level.INFO, "Requested access to create a comment: {0}", logUserText);

        fileHandler.close();

        return GSON.toJson(commentAdded);
    }
}
