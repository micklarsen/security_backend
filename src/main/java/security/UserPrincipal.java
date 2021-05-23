package security;

import entities.Person;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements Principal {

  private String username;
  private String userAlias;

  private List<String> roles = new ArrayList<>();

  /* Create a UserPrincipal, given the Entity class Person*/
  public UserPrincipal(Person user) {
    this.username = user.getEmail();
    this.userAlias = user.getUsername();
    this.roles = user.getRolesAsStrings();
  }

  public UserPrincipal(String username, String userAlias, String[] roles) {
    super();
    this.username = username;
    this.userAlias = userAlias;
    this.roles = Arrays.asList(roles);
  }

  @Override
  public String getName() { return userAlias; }

  public boolean isUserInRole(String role) {
    return this.roles.contains(role);
  }
}