package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "persons")
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "email", length = 50)
    private String email;

    @Basic(optional = false)
    @NotNull
    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String userPass;

    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "email", referencedColumnName = "email")}, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<Role> roleList = new ArrayList<>();

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "firstname", length = 30)
    private String firstName;

    @Column(name = "lastname", length = 30)
    private String lastName;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "person")
    private List<Comment> commentList = new ArrayList<>();

    public Person() {
    }

    public Person(String email, String username, String userPass, String phone, String firstName, String lastName) {
        this.username = username;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));
    }

    //TODO Change when password is hashed
    public boolean verifyPassword(String pw) {
        return (BCrypt.checkpw(pw, this.userPass));
    }

    public Person(String userName, String userPass) {
        this.email = userName;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));
    }

    public String getEmail() {
        return email;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roleList.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public void addComment(Comment userComment) {
        if (userComment != null) {
            commentList.add(userComment);
            userComment.setPerson(this);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
