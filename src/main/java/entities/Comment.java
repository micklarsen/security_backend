package entities;

import servlet.UploadServlet;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;


@Entity
@Table(name = "comments")
@NamedQueries({
        @NamedQuery(name = "Comment.deleteAllRows", query = "DELETE from Comment"),
        @NamedQuery(name = "Comment.getAllRows", query = "SELECT c from Comment c")})

public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "person_comment", columnDefinition = "VARCHAR(250)")
    private String comment;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastEdited;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Person person;

    @Column(name = "topic_ID", columnDefinition = "int")
    private int topicID;

    @Column(name = "image_file", columnDefinition = "VARCHAR(255)")
    private String imageFile;

    //UploadServlet uploadServlet = new UploadServlet();

    public Comment(String comment, int topicID, String imageFile) {
        this.comment = comment;
        this.created = new Date();
        this.lastEdited = new Date();
        this.topicID = topicID;
        this.imageFile = imageFile;
    }


//    public String makeFileName(String filename) {
//        try {
//            return uploadServlet.makeMD5(imageFile) + uploadServlet.getFileExtension(imageFile);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    public Comment() {
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageID) {
        this.imageFile = imageID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setRocketID(int topicID) {
        this.topicID = topicID;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", comment=" + comment + ", created=" + created + ", lastEdited=" + lastEdited + ", Person=" + person + ", topicID=" + topicID + '}';
    }

}