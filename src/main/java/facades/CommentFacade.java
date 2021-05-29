package facades;

import dto.CommentDTO;
import dto.CommentsDTO;
import dto.PersonDTO;
import entities.Comment;
import errorhandling.CommentException;
import errorhandling.CommentException;
import entities.Person;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import utils.EMF_Creator;

import java.security.NoSuchAlgorithmException;

public class CommentFacade {

    private static EntityManagerFactory emf;
    private static CommentFacade instance;

    private CommentFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static CommentFacade getCommentFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CommentFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CommentsDTO getAllComments() throws CommentException {

        EntityManager em = getEntityManager();
        try {
            return new CommentsDTO(em.createNamedQuery("Comment.getAllRows").getResultList());
        } catch (Exception e) {
            throw new CommentException("No connection to the database");
        } finally {
            em.close();
        }
    }

    public CommentDTO getUserComment(long id) throws CommentException {

        EntityManager em = getEntityManager();

        Comment comment = em.find(Comment.class, id);

        if (comment == null) {
            throw new CommentException("No user comment linked with provided id was found");
        } else {
            try {
                return new CommentDTO(comment);
            } finally {
                em.close();
            }
        }
    }

    public CommentDTO deleteComment(long id) throws CommentException {

        EntityManager em = getEntityManager();
        Comment comment = em.find(Comment.class, id);
        if (comment == null) {
            throw new CommentException("Could not delete, Id was not found");
        } else {
            try {
                em.getTransaction().begin();
                em.remove(comment);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new CommentDTO(comment);
        }
    }

    public CommentDTO addComment(String addComment, int topicID, String userName, String imageID) throws CommentException, NotFoundException, NoSuchAlgorithmException {

        EntityManager em = emf.createEntityManager();
        Comment comment = new Comment(addComment, topicID, imageID);

        Person u = em.find(Person.class, userName);

        if (u == null) {
            throw new NotFoundException("No user with that ID exists");
        }

        u.addComment(comment);

        if ((addComment.length() == 0)) {
            throw new CommentException("Missing input");
        }

        try {
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return new CommentDTO(comment);
    }

}