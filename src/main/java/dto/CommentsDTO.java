package dto;

import entities.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentsDTO {

    List<CommentDTO> all = new ArrayList();

    public CommentsDTO(List<Comment> commentEntities) {
        commentEntities.forEach((c) -> {
            all.add(new CommentDTO(c));
        });
    }

    public List<CommentDTO> getAll() {
        return all;
    }

    public void setAll(List<CommentDTO> all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return "CommentsDTO{" + "all=" + all + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.all);
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
        final CommentsDTO other = (CommentsDTO) obj;
        if (!Objects.equals(this.all, other.all)) {
            return false;
        }
        return true;
    }


}