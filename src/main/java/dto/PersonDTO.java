package dto;

import entities.Person;

import java.util.List;

public class PersonDTO {

    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public PersonDTO(Person person) {
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.password = person.getUserPass();
        this.username = person.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
