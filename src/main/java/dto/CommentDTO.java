package dto;

import entities.Comment;
import java.util.Date;
import java.util.Objects;

public class CommentDTO {

    private String userComment;
    private Comment comment;
    private Date created;
    private long id;
    private int topicID;
    private String imageID;

    private String userName;
    private String userEmail;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        this.userComment = comment.getComment();
        this.id = comment.getId();
        this.created = comment.getCreated();
        this.topicID = comment.getTopicID();
        this.userName = comment.getPerson().getUsername();
        this.imageID = comment.getImageBase64();
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() { return userEmail; }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.userComment);
        hash = 79 * hash + Objects.hashCode(this.comment);
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommentDTO other = (CommentDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.userComment, other.userComment)) {
            return false;
        }
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommentDTO{" + "userComment=" + userComment + ", comment=" + comment + ", created=" + created + ", id=" + id + ", topicID=" + topicID + ", userName=" + userName + ", userEmail=" + userEmail + '}';
    }



}