package security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import facades.PersonFacade;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.Person;
import errorhandling.NotFoundExceptionMapper;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import security.errorhandling.AuthenticationException;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

@Path("login")
public class LoginEndpoint {

    public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final PersonFacade USER_FACADE = PersonFacade.getPersonFacade(EMF);

    private static final Logger LOGGER = Logger.getLogger(LoginEndpoint.class.getName());

    private ConsoleHandler consoleHandler = null;
    private FileHandler fileHandler = null;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonString) throws AuthenticationException {
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();

        try {
            // All logger levels in order (FINEST not so important and SEVERE very important
            /*
        SEVERE
        WARNING
        INFO
        CONFIG
        FINE
        FINER
        FINEST
             */

            // Get rid of any handlers that the root logger has
            LogManager.getLogManager().reset();
            //Creating consoleHandler and fileHandler
            consoleHandler = new ConsoleHandler();
            try {
                fileHandler = new FileHandler("Login.log", true);
            } catch (IOException | SecurityException ex) {
                Logger.getLogger(LoginEndpoint.class.getName()).log(Level.SEVERE, null, ex);
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
            LOGGER.log(Level.INFO, "Logged in user: {0}", username);

            //Console handler removed
            LOGGER.removeHandler(consoleHandler);

            Person user = USER_FACADE.getVeryfiedUser(username, password);
            String token = createToken(username, user.getUsername(), user.getRolesAsStrings());
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", username);
            responseJson.addProperty("token", token);
            return Response.ok(new Gson().toJson(responseJson)).build();

        } catch (JOSEException | AuthenticationException ex) {
            if (ex instanceof AuthenticationException) {
                throw (AuthenticationException) ex;
            }
      Logger.getLogger(NotFoundExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
//            LOGGER.log(Level.SEVERE, "Login exception: {0}", ex);
        }
        // Close filehandler
        fileHandler.close();

        throw new AuthenticationException("Invalid username or password! Please try again");
    }

    private String createToken(String userName, String userAlias, List<String> roles) throws JOSEException {

        StringBuilder res = new StringBuilder();
        for (String string : roles) {
            res.append(string);
            res.append(",");
        }
        String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";

        String issuer = "DAT4SEM_Security";

        String tokenInfo = "Token info: " + "User: " + userName + " - role: " + rolesAsString + " - issuer: " + issuer;
        // Log token info
        LOGGER.log(Level.INFO, tokenInfo);

        JWSSigner signer = null;
        try {
            signer = new MACSigner(SharedSecret.getSharedKey());
        } catch (KeyLengthException ex) {
        Logger.getLogger(LoginEndpoint.class.getName()).log(Level.SEVERE, null, ex);
//            LOGGER.log(Level.SEVERE, "Token exception: {0}", ex);
        }
        fileHandler.close();

        Date date = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .claim("username", userName)
                .claim("roles", rolesAsString)
                .claim("issuer", issuer)
                .claim("userAlias", userAlias)
                .issueTime(date)
                .expirationTime(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();

    }
}
