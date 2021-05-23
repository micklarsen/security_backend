package security;

import entities.Person;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements Principal {

  private String username;
  private String useremail;
  private List<String> roles = new ArrayList<>();

  /* Create a UserPrincipal, given the Entity class Person*/
  public UserPrincipal(Person user) {
    this.username = user.getUsername();
    this.useremail = user.getEmail();
    this.roles = user.getRolesAsStrings();
  }

  public UserPrincipal(String username, String useremail, String[] roles) {
    super();
    this.username = username;
    this.useremail = useremail;
    this.roles = Arrays.asList(roles);
  }

  @Override
  public String getName() {
    return username;
  }

  public boolean isUserInRole(String role) {
    return this.roles.contains(role);
  }
}