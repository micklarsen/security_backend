package rest;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import errorhandling.ExceptionDTO;
import utils.EMF_Creator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CommentDTO;
import dto.CommentsDTO;
import errorhandling.CommentException;
import errorhandling.NotFoundException;
import facades.CommentFacade;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import java.security.NoSuchAlgorithmException;

@Path("comments")
public class CommentResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CommentFacade FACADE = CommentFacade.getCommentFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
    public String getAllComments() throws CommentException {
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