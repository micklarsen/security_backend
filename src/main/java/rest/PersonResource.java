package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import entities.Person;
import facades.PersonFacade;
import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

@Path("info")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = Logger.getLogger(CommentResource.class.getName());

    private ConsoleHandler consoleHandler = null;
    private FileHandler fileHandler = null;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        consoleHandler = new ConsoleHandler();

        try {
            fileHandler = new FileHandler("./dat4semlogs/UserInfo.log", true);
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

        /**
         * Returns a <code>java.security.Principal</code> object containing the
         * name of the current authenticated user. If the user has not been
         * authenticated, the method returns null.
         */
        String thisuser = securityContext.getUserPrincipal().getName();

        //Log request to access user info
        LOGGER.log(Level.INFO, "Requested access for user info: {0}", thisuser);

        fileHandler.close();

        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        consoleHandler = new ConsoleHandler();

        try {
            fileHandler = new FileHandler("dat4semlogs/UserInfo.log", true);
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

        /**
         * Returns a <code>java.security.Principal</code> object containing the
         * name of the current authenticated user. If the user has not been
         * authenticated, the method returns null.
         */
        String thisuser = securityContext.getUserPrincipal().getName();

        //Log request to access admin info
        LOGGER.log(Level.WARNING, "Requested access for admin user info: {0}", thisuser);

        fileHandler.close();

        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public PersonDTO addUser(String user) throws Exception {
        // Get rid of any handlers that the root logger has
        LogManager.getLogManager().reset();
        consoleHandler = new ConsoleHandler();

        try {
            fileHandler = new FileHandler("dat4semlogs/UserInfo.log", true);
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

        PersonDTO u = GSON.fromJson(user, PersonDTO.class);
        PersonDTO newPerson = FACADE.makePerson(u);

        //Log request to add a new user
        LOGGER.log(Level.WARNING, "Requested access to add a new user: {0}", newPerson.toString());

        fileHandler.close();

        return newPerson;
    }

}
